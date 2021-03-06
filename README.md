# Modern Java
In Action 전문가를 위한 자바 8,9,10 기법 가이드(라울-게이브리얼 우르마, 마리오 푸스코, 앨런 마이크로프트 지음) 소스

원서 코드 : https://www.manning.com/books/modern-java-in-action
역서 코드 : http://www.hanbit.co.kr/src/10202

## [Settings]
### Java
* zulu 11 jdk

### Gradle
* 5.2.1

---------

## chapter 01 - 자바 8, 9, 10, 11 : 무슨 일이 일어나고 있는가?
### 추가된 자바 8 기능
* 스트림 API
  * 스트림이란 한 번에 한 개씩 만들어지는 연속적인 데이터 항목들의 모임
  * 스트림 API의 핵심은 우리가 하려는 작업을(DB query처럼) 고수준으로 추상화해서 일련의 스트림으로 만들어 처리할 수 있다는 것
  * 스트림 파이프라인을 이용, 입력 부분을 여러 CPU 코어에 쉽게 할당할 수 있음
  * 스트림 API로 스레드를 직접 사용하지 않으면서 병렬성 획득
* 메서드에 코드를 전달하는 기법 (동작 파라미터화)
  * 동작 파라미터화(behavior parameterization)
  * 메서드 참조, 람다, 일급 시민
  * 프레디케이트 : 인수 값을 받아 boolean 값을 반환하는 함수
* 인터페이스의 디폴트 메서드
  * 클래스에서 구현하지 않아도 되는 메서드 정의
---

## chapter 02 - 동작 파라미터화 코드 전달하기
### 동작 파라미터화(behavior parameterization)
* 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미
* 정확하게는 위처럼 추후에 다양한 동작을 수행할 수 있는 코드 블록을 해당 메서드의 파라미터로 전달하는 방식을 의미

### 익명 클래스
* 이름이 없는 클래스 (지역 클래스의 비슷한 개념)

### 올바른 함수형 인터페이스는?
  * [1] O
    ```
    public interface Adder {
        int add(int a, int b);
    }
    ```
  * [2] X : 추상 메서드 오버로딩으로 인해 함수형 인터페이스 조건인 추상 메서드 개수가 1개 초과
    ```
    public interface SmartAdder extends Adder {
        int add(double a, double b);
    }
    ```
  * [3] X : 추상 메서드가 없음
    ```
    public interface Nothing {
    }
    ```

### 어디에 람다를 사용할 수 있는가?
  * [1] O
    ```
    execute(() -> {});
    public void execute(Runnable r) {
        r.run();
    }
    ```
  * [2] O
    ```
    public Callable<String> fetch() {
        return () -> "Tricky example ;-)";
    }
    ```
  * [3] X : apple.getWeight의 시그니처와 일치하지 않음
    ```
  	Predicate<Apple> p = (Apple a) -> a.getWeight();
    ```
---

## chapter 03 - 람다 표현식
### 람다의 특징
* 익명
  * 보통의 메서드와 달리 이름이 없으므르 익명이라 표현
* 함수
  * 메서드처럼 클래스의 종속되지 않아 함수라고 부르나 메서드처럼 파라미터 리스트, 바디, 반환 형식, 가능한 예외 리스트를 포함
* 전달
  * 람다 표현식을 메서드 인수로 전달하거나 변수로 저장 가능
* 간결성
  * 익명 클래스처럼 많은 자질구레한 코드를 구현할 필요 없음
  
### 자바 8의 유효한 람다 표현식(문법)
* 
    ```
    (String s) -> s.length()
    ```
*
    ``` 
    (Apple a) -> a.getWeight() > 150
    ```
*
    ``` 
    (int x, int y) -> {
        System.out.println("Result:");
        System.out.println("Result:");
    }
    ```
*
    ```
    () -> 42
    ```
*
    ```
    (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight())
    ```
### 스타일
* 표현식 스타일
  ``` (parameters) -> expression ```
* 블록 스타일
  ``` (parameters) -> { statements; } ```

### 람다 예제
* 불리언 표현식
    ```
    (List<String> list) -> list.isEmpty()
    ```
* 객체 생성
    ```
    () -> new Apple(10)
    ```
* 객체에서 선택/호출
    ```
    (Apple a) -> {
        System.out.println(a.getWeight());
    }
    ```
* 두 값을 조합
    ```
    (String s) -> s.length()
    ```
* 두 객체 비교
    ```
    (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight())
    ```

### 함수형 인터페이스
* 인터페이스는 디폴트 메서드를 포함할 수 있는데 많은 디폴트 메서드가 있더라도 추상 메서드가 오직 하나면 함수형 인터페이스
  * 디폴트 메서드 : 인터페이스의 메서드를 구현하지 않은 클래스를 고려해서 기본 구현을 제공하는 바디를 포함한 메서드
* 전체 표현식을 함수형 인터페이스의 인스턴스로 취급 (기술적으로는 함수형 인터페이스를 구현한 클래스의 인스턴스)
* @FunctionalInterface
  * 추상 메서드가 오직 하나인 메서드

### 함수 디스크립터
* 함수형 인터페이스의 추상 메서드 시그니처는 람다 표현식의 시그니처를 가리킴
* 람다 표현식의 시그니처를 서술하는 메서드 또는 함수형 인터페이스의 추상 메서드 시그니처

### 실행 어라운드 패턴(execute around pattern)
* 자원 처리에 사용하는 순환 패턴(recurrent pattern)은 자원을 열고, 처리 후 자원을 닫음
* 실제 자원을 처리하는 코드를 설정과 정리 두 과정이 둘러 싸는 형태를 의미

### 미리 작성되어 있는 자바의 함수형 인터페이스
* ``` Predicate<T> ```
   * ``` T -> boolean ```
   * test 추상 메서드를 정의하며 제네릭 형식의 T의 객체를 인수로 받아 boolean 반환
   * [기본형 특화]
     * IntPredicate, LongPredicate, DoublePredicate
* ``` Consumer<T> ```
  * ``` T -> void ```
  * accept 추상 메서드를 정의하며 제네릭 형식의 T의 객체를 인수로 받아 void 반환 (소비)
  * [기본형 특화]
    * IntConsumer, LongConsumer, DoubleConsumer
* ``` Function<T, R> ```
  * ``` T -> R ```
  * apply 추상 메서드를 정의하며 제네릭 형식의 T의 객체를 인수로 받아 제네릭 형식의 R 객체를 반환
  * [기본형 특화]
    * IntFunction<R>, IntToDoubleFunction, IntToLongFunction
    * LongFunction<R>, LongToDoubleFunction, LongToIntFunction
    * DoubleFunction<R>, DoubleToIntFunction, DoubleToLongFunction
    * ToIntFunction<T>, ToDoubleFunction<T>, ToLongFunction<T>
* ``` Supplier<T> ```
  * ``` () -> T ```
  * get 추상 메서드를 정의하며 인수 없이 제네릭 형식의 T 객체를 반환
  * [기본형 특화]
    * BooleanSupplier, IntSupplier, LongSupplier, DoubleSupplier
* ``` UnaryOperator<T> ```
  * ``` T -> T ```
  * apply 추상 메서드를 정의하며 제네릭 형식의 T의 객체를 인수로 받고 T 객체를 반환
  * [기본형 특화]
    * IntUnaryOperator, LongUnaryOperator, DoubleUnaryOperator
* ``` BinaryOperator<T> ```
  * ``` (T, T) -> T ```
  * apply 추상 메서드를 정의하며 제네릭 형식의 T의 객체를 인수로 2개 받고 T 객체를 반환
  * [기본형 특화]
    * IntBinaryOperator, LongBinaryOperator, DoubleBinaryOperator
* ``` BiPredicate<L, R> ```
  * ``` (T, U) -> boolean ```
  * test 추상 메서드를 정의하며 제네릭 형식의 T의 객체를 인수로 2개 받고 boolean 반환
* ``` BiConsumer<T, U> ```
  * ``` (T, U) -> void ```
  * accept 추상 메서드를 정의하며 제네릭 형식의 T, U 객체를 인수로 받아 void 반환
  * [기본형 특화]
    * ObjIntConsumer<T>, ObjLongConsumer<T>, ObjDoubleConsumer<T>
* ``` BiFunction<T, U, R> ```
  * ``` (T, U) -> R ```
  * apply 추상 메서드를 정의하며 제네릭 형식의 T, U 객체를 인수로 받아 제네릭 형식의 R 객체를 반환
  * [기본형 특화]
    * ToIntBiFunction<T, U>, ToLongBiFunction<T, U>, ToDoubleBiFunction<T, U>
  
* primitive 타입 오토 박싱 비용을 줄이기 위하여 기본형 인터페이스가 제공됨

### 예외, 람다, 함수형 인터페이스의 관계
  * 함수형 인터페이스는 확인된 예외를 던지는 동작을 허용하지 않음
  * 예외를 던지는 람다 표현식을 만드려면 확인된 예외를 선언하는 함수형 인터페이스를 직전 정의하거나 람다를 try/catch 블록으로 감싸야 함
  
### 형식 검사
* 어떤 콘텍스트에서 기대되는 람다 표현식의 형식을 대상 형식(target type) 이라고 함
* 다이아몬드 연산자도 마찬가지로 콘텍스트에 따라 제네릭 형식 추론

### 특별한 void 호환 규칙
* 람다의 바디에 일반 표현식이 있으면 void를 반환하는 함수 디스크립터와 호환됨

### 형식 추론
* 콘텍스트를 통해 자바 컴파일러는 람다 파라미터 형식 추론 가능
  * 형식을 추론 하지 않음
    * ``` Comparator<Apple> c = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()) ```
  * 형식 추론
    * ``` Comparator<Apple> c = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight()) ```
    
### 지역 변수 사용
* 자유변수(free variable - 파라미터로 넘겨진 변수가 아닌 외부에서 정의된 변수) 활용시 동작
  * 이와 같은 동작을 람다 캡처링이라고 부름
* 지역 변수는 명시적으로 final 선언 또는 실질적으로 final 변수와 똑같이 사용되어야 함
  * 인스턴스 변수는 힙에 저장되며 지역 변수는 스택에 위치
  * 람다에서 지역 변수에 바로 접근할 수 있다는 가정하에 람다가 스레드에서 실행된다면 변수를 할당한 스레드가 사라져서 변수 할당이 해제 되었는데도  
    람다를 실행하는 스레드에서는 해당 변수에 접근하려 할 수 있음  
    따라서 자바는 원래 변수에 접근을 허용하지 않고 자유 지역 변수의 복사본을 제공함
  * 지역 변수의 제약 때문에 외부 변수를 변화시키는 일반적인 명령형 프로그래밍 패턴(병렬화를 방해하는 요소)에 제동을 걸 수 있음
  
### 클로저
* 함수의 비지역 변수를 자유롭게 참조할 수 있는 함수의 인스턴스를 가리킴
* 예를 들어 클로저를 다른 함수의 인수로 전달할 수 있음
  * 클로저는 클로저 외부에 정의된 변수의 값에 접근하고, 값을 바꿀 수 있음
  * 자바 8의 람다, 익명 클래스는 클로저와 비슷한 동작 수행
  * 람다, 익명 클래스 모두 메서드의 인수로 전달될 수 있으며 자신의 외부 영역에 접근할 수 있음
    * 다만 람다, 익명 클래스는 람다가 정의된 메서드의 지역 변수의 값은 바꿀 수 없음
  * 람다가 정의된 메서드의 지역 변수값은 final 변수여야 함, 지역 변수 값은 스택에 존재하므로 자신을 정의한 스레드와 생존을 같이 해야 함
  
### 메서드 참조
* 람다보다 가독성이 좋을 때도 있음
* 특정 메서드만을 호출하는 람다의 축약형
* 명시적으로 메서드명을 참조함으로써 가독성 높임
* 메서드 참조 유형
  * 정적 메서드 참조
    * ``` Integer::parseInt ```
  * 다양한 형식의 인스턴스 메서드 참조
    * ``` String::length ```
  * 기존 객체의 인스턴스 메서드 참조
    * 예를 들어 Transaction 객체를 할당 받은 expensiveTransaction 지역 변수가 있고, Transaction 객체에는 getValue 메서드가 있다면  
      이를 ``` expensiveTransaction::getValue ``` 라고 표현할 수 있음
* 세 가지 종류의 람다 표현식을 메서드 참조로 바꾸는 방법
  * ``` (args) -> ClassName.staticMethod(args) ```
    * ``` ClassName::staticMethod ```
  * ``` (arg0, rest) -> arg0.instanceMethod(rest) ``` - (arg0은 ClassName 형식)
    * ``` ClassName::instanceMethod ```
  * ``` (args) -> expr.instanceMethod(args) ```
    * ``` expr::instanceMethod ```
* 컴파일러는 람다 표현식의 형식을 검사하던 방식과 비슷한 과정으로 메서드 참조가 주어진 함수형 인터페이스와 호환하는지 확인
  * 콘텍스트 형식과 일치해야 함
    
### Comparator 조합
* ``` Comparator<Apple> c =  Comparator.comparing(Apple::getWeight); ```
* 역정렬
  * ``` inventory.sort(comparing(Apple::getWeight).reversed()); ```
* 연결 - 무게가 같은 두 사과가 존재할 경우 비교 결과를 더 다듬을 수 있는 두번째 Comparator 생성
  * ``` inventory.sort(comparing(Apple::getWeight).reversed().thenComparing(Apple::getCountry)); ```

### Predicate 조합
* Predicate 인터페이스는 negate, and, or 세 가지 메서드 제공
  * 빨간색이 아닌 사과 처럼 특정 프레디케이트를 반전
    * ``` Predicate<Apple> notRedApple = redApple.negate(); ```
  * and 메서드 이용, 빨간색이면서 무거운 사과(150g)를 선택하도록 두 람다 조합
    * ``` Predicate<Apple> redAndHeavyApple = redApple.and(apple -> apple.getWeight() > 150); ```
  * or 메서드 이용, 빨간색이면서 무거운 사과(150g) 또는 그냥 녹색 사과 조건
    * ``` Predicate<Apple> redAndHeavyAppleOrGreen = redApple ```  
      ``` .and(apple -> apple.getWeight() > 150) ```   
      ``` .or(apple -> GREEN.equals(a.getColor())); ```
  * 왼쪽에서 오른쪽으로 연결
    * a.or(b).and(c) == (a || b) && c 와 같음

### Function 조합
* Function 인터페이스는 andThen, compose 두 가지 디폴트 메서드 제공
* andThen 메서드는 주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전닫하는 함수를 반환
  * ``` Function<Integer, Integer> f = x -> x + 1; ```
  * ``` Function<Integer, Integer> g = x -> x * 2; ```
  * ``` Function<Integer, Integer> h = f.andThen(g); ```
  * ``` int result = h.apply(1); ``` 4를 반환
* compose 메서드는 인수로 주어진 함수를 먼저 실행한 다음에 그 결과를 외부 함수의 인수로 제공
  * ``` Function<Integer, Integer> f = x -> x + 1; ```
  * ``` Function<Integer, Integer> g = x -> x * 2; ```
  * ``` Function<Integer, Integer> h = f.compose(g); ```
  * ``` int result = h.apply(1); ``` 3을 반환
  
### 정리
* 람다 표현식은 익명 함수의 일종, 이름은 없지만 파라미터 리스트, 바디, 반환 형식을 가지며 예외를 던질 수 있음
* 람다 표현식으로 간결한 코드 구현 가능
* 함수형 인터페이스는 하나의 추상 메서드만을 정의하는 인터페이스
* 함수형 인터페이스를 기대하는 곳에서만 람다 표현식을 사용할 수 있음
* 람다 표현식을 이용해서 함수형 인터페이스의 추상 메서드를 즉석으로 제공할 수 있으며 람다 펴현식 전체가 함수형 인터페이스의 인스턴스로 취급
* java.util.function 패키지는 자주 사용하는 다양한 함수형 인터페이스 제공
* 제네릭 함수형 인터페이스와 관련한 박싱 동작을 피할 수 있는 기본형 특화 인터페이스도 제공
* 실행 어라운드 패턴을 람다와 활용하면 유연성과 재사용성을 추가로 얻을 수 있음
  * 실행 어라운드 패턴 : 예를 들면 자원 할당, 자원 정리 등 코드 중간에 실행해야 하는 메서드에 꼭 필요한 코드
* 람다 표현식의 기대 형식(type expected)을 대상 형식(target type)이라고 함
* 메서드 참조를 이용하면 기존의 메서드 구현을 재사용하고 직접 전달 가능
* 제공하는 함수형 인터페이스는 람다 표현식을 조합할 수 있는 다양한 디폴트 메서드를 제공
    
### [quiz]
* 규칙에 맞지 않는 람다 표현식 고르기
  * ``` () -> {} ```
  * ``` () -> "Raoul" ```
  * ``` () -> {return "Mario";} ```
  * ``` (Integer i) -> return "Alan" + i; ```
     * return 은 흐름 제어문이기 때문에 (Integer i) -> {return "Alan" + i;} 처럼 되어야 올바른 표현식
  * ``` (String s) -> {"Iron Man";} ```
     * "Iron Man" 은 구문(statement)이 아니라 표현식(expression) 
     * 따라서 (String s) -> "Iron Man" 
     * 또는 (String s) -> {return "Iron Man";} 처럼 되어야 올바른 표현식

* 다음 인터페이스 중 함수형 인터페이스는 어느 것인가?
  * O
    ```
    public interface Adder {
        int add(int a, int b);
    }
    ```
  * X : 두 개의 추상 메서드를 포함하기 때문에
    ```
    public interface SmartAdder extends Adder {
        int add(double a, double b);
    }
    ```
  * X : 추상 메서드가 없기 때문에
    ```
    public interface Nothing {
    }
    ```

* 어디에 람다를 사용할 수 있는가?
  * O
    ```
    execute(() -> {});
    public void execute(Runnable r) {
        r.run();
    }
    ```
  * O
    ```
    public Callable<String> fetch() {
        return () -> "Tricky example ;-)";
    }
    ```
  * X : 메서드 시그니처 불일치로 유효한 람다 표현식이 아님
    ```
    Predicate<Apple> p = (Apple a) -> a.getWeight();
    ```
  
* 다음과 같은 함수형 디스크립터가 있을 때 어떤 함수형 인터페이스를 사용할 수 있는가? 유효한 람다 표현식 제시
  * [1] : ``` T -> R ```
    * ``` Function<T, R> ``` : ``` T ``` 형식의 객체를 R 형식의 객체로 변환할 때 사용
  * [2] : ``` (int, int) -> int ```
    * ``` IntBinaryOperator ``` : ``` (int, int) -> int ``` 형식의 시그니처를 갖는 추상 메서드 applyAsInt 정의
  * [3] : ``` T -> void ```
    * ``` Consumer<T> ``` : ``` T -> void ``` 형식의 시그니처를 갖는 추상 메서드 accept 정의
  * [4] : ``` () -> T ```
    * ``` Supplier<T> ``` : ``` () -> T ``` 형식의 시그니처를 갖는 추상 메서드 get 정의
    * Callable<T> : () -> T 형식의 시그니처를 갖는 추상 메서드 call 정의
  * [5] : ``` (T, U) -> R ```
    * ``` BiFunction<T, U, R> ``` : ``` (T, U) -> R ``` 형식의 시그니처를 갖는 추상 메서드 apply 정의
    
* 다음 코드 문제 해결
  * ``` Object o = () -> { System.out.println("Tricky example"); }; ```
  * 람다 표현식의 콘텍스트는 Object 그러나 Object는 함수형 인터페이스가 아님
    * ``` Runnable r = () -> { System.out.println("Tricky example"); }; ```
    * ``` Object o = (Runnable) -> { System.out.println("Tricky example"); }; ```
  * 같은 함수형 디스크립터를 가진 두 함수형 인터페이스를 갖는 메서드를 오버로딩할 때 이와 같은 기법 활용 가능
    * ``` execute(() -> {}) ``` 와 같은 람다 표현식 경우 Runnable, Action 함수 디스크립터와 혼동됨
      * ``` execute((Action) () -> {}) ```
      
* 다음 람다 표현식과 일치하는 메서드 참조를 구현
  * ``` ToIntFunction<String> stringToInt = (String s) -> Integer.parseInt(s); ```
    * ``` Function<String, Integer> stringToInteger = Integer::parseInt; ```
  * ``` BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element); ```
    * ``` BiPredicate<List<String>, String> contains = List::contains; ```
  * ``` Predicate<String> startsWithNumber = (String string) -> this.startsWithNumber(String); ```
    * ``` Predicate<String> startsWithNumber = this::startsWithNumber; ```

* ``` Color(int, int, int) ``` 처럼 인수가 세 개인 생성자의 생성자 참조를 사용하는 법
  * 생성자 참조와 일치하는 시그니처를 갖는 함수형 인터페이스 작성
    ```
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
    ```
  * 생성자 참조
    ```
    TriFunction<Integer, Integer, Integer, Color> colorFactory = Color::new;
    ```
---

## chapter 04 - 스트림 소개
### 스트림이란 무엇인가?
* 새로운 API
  * 데이터 컬렉션 반복을 처리하는 기능
  * 특징
    * 선언형 : 더 간결하고 가독성이 좋아짐
    * 조립할 수 있음 : 유연성이 좋아짐
    * 병렬화 : 성능이 좋아짐
    
* 정의
  * 데이터 처리 연산을 지원하도록 소스에서 추출된 연속 요소로 정의 가능
  * 연속된 요소
    * 컬렉션과 마찬가지로 스트림은 특정 요소 형식으로 이루어진 연속된 값 집합의 인터페이스를 제공
    * 컬렉션은 자료구조이므로 컬렉션에서는 시간과 공간의 복잡성과 관련된 요소 저장 및 접근 연산이 주를 이룸
      * 예를 들어 ArrayList를 사용할 것인지 아니면 LinkedList를 사용할 것인지
    * 반면 스트림은 filter, sorted, map처럼 표현 계산식이 주를 이룸
    * 즉 컬렉션의 주제는 데이터고 스트림의 주제는 계산
  * 소스
    * 스트림은 컬렉션, 배열, I/O 자원 등의 데이터 제공 소스로부터 데이터를 소비
    * 정렬된 컬렉션으로 스트림을 생성하면 정렬이 그대로 유지됨
      * 즉 리스트로 스트림을 만들면 스트림의 요소는 리스트의 요소와 같은 순서를 유지
  * 데이터 처리 연산
    * 스트림은 함수형 프로그래밍 언어에서 지원하는 연산과 데이터베이스와 비슷한 연산을 지원
      * 예를 들어 filter, map, reduce, find, match, sort 등으로 데이터 조작 가능
    * 스트림 연산은 순차적으로 또는 병렬로 실행 가능
    
* 특징
  * 파이프라이닝(Pipelining)
    * 대부분의 스트림 연산은 스트림 연산끼리 연결해서 커다란 파이프라인을 구성할 수 있도록 스트림 자신을 반환
    * 그 덕분에 게으름(Laziness), 쇼트서킷(short-circuiting) 같은 최적화도 얻을 수 있음
    * 연산 파이프라인은 데이터 소스에 적용하는 데이터베이스 질의와 비슷
  * 내부 반복
    * 반복자를 이용해 명시적으로 반복하는 컬렉션과 달리 스트림은 내부 반복을 지원
    
### 스트림 연산
  * filter
    * 람다를 인수로 받아 스트림에서 특정 요소를 제외
  * map
    * 람다를 이용해서 한 요소를 다른 요소로 변환하거나 정보를 추출
  * limit
    * 정해진 개수 이상의 요소가 스트림에 저장되지 못하게 스트림 크기를 축소 truncate 함
  * collect
    * 스트림을 다른 형식으로 변환
    
### 스트림과 컬렉션
  * 기존 컬렉션과 스티림 모두 연속된 요소 형식의 값을 저장하는 자료구조의 인터페이스 제공
  * 데이터를 언제 계산하느냐가 컬렉션과 스트림의 가장 큰 차이
    * 컬렉션의 모든 요소는 컬렉션을 추가하기전에 계산 되어야 함
      * 컬렉션에 요소를 추가하거나 삭제할 수 있음
      * 이런 연산을 수행할 때마다 컬렉션의 모든 요소를 메모리에 저장해야 하며 컬렉션에 추가하려는 요소는 미리 계산 되어야 함
    * 스트림은 이론적으로 요청할 때만 요소를 계산하는 고정된 자료구조
      * 스트림에서 요소를 추가하거나 스트림에 요소를 제거할 수 없음
      * 사용자가 요청한 값만 스트림에서 추출한다는 것이 핵심
      * 생산자(producer)와 소비자(consumer) 관계를 형성
      * 스트림은 게으르게 만들어지는 컬렉션과 같음 (데이터를 요청할 때만 값을 계산)
    * 컬렉션은 적극적으로 생성 (생산자 중심 supplier-driven : 팔기도 전에 창고를 가득 채움)
  * 반복자와 마찬가지로 스트림도 한 번만 탐색 가능 (탐색된 스트림의 요소는 소비됨)
    * 다시 탐색하려면 초기 데이터 소스에서 새로운 스트림을 만들어야 함
      * 컬렉션처럼 반복 사용할 수 있는 데이터 소스여야 함
      * I/O 채널이라면 소스를 반복 사용할 수 없으므로 새로운 스트림을 생성하지 못함
  * 스트림과 컬렉션의 철학적 접근
    * 스트림은 시간적으로 흩어진 값의 집합으로 간주
    * 컬렉션은 특정 시간에 모든 것이 존재하는 공간(컴퓨터 메모리)에 흩어진 값으로 비유
  * 또 다른 차이점은 데이터 반복 처리 방법
    * 외부 반복
      * for-each 처럼 사용자가 직접 요소를 반복
    * 내부 반복
      * 반복을 알아서 처리, 결과 스트림값을 어딘가에 저장해 줌
      
### 스트림 연산
  * 중간 연산(intermediate operation) : 연결할 수 있는 스트림 연산
    * 쇼트서킷, limit 연산
    * 루프 퓨전(loop fusion) : 다른 연산이 한 과정으로 병함 됨
  * 최종 연산(terminal operation) : 스트림을 닫는 연산

### 스트림 이용하기
  * 질의를 수행할 데이터 소스
  * 스트림 파이프라인을 구성할 중간 연산 연결
  * 스트림 파이프라인을 실행하고 결과를 만들 최종 연산
  * 스트림 파이프라인의 개념은 빌더 패턴과 비슷
  * 중간 연산
    * filter
      * 반환 형식 : ``` Stream<T> ```
      * 연산의 인수 : ``` Predicate<T> ```
      * 함수 디스크립터 : ``` T -> boolean ```
    * map
      * 반환 형식 : ``` Stream<R> ```
      * 연산의 인수 : ``` Function<T, R> ```
      * 함수 디스크립터 : ``` T -> R ```
    * limit
      * 반환 형식 : ``` Stream<T> ```
    * sorted
      * 반환 형식 : ``` Stream<T> ```
      * 연산의 인수 : ``` Comparator<T> ```
      * 함수 디스크립터 : ``` (T, T) -> int ```
    * distinct
      * 반환 형식 : ``` Stream<T> ```
  * 최종 연산
    * forEach
      * 반환 형식 : ``` void ```
      * 목적 : 스트림의 각 요소를 소비하면서 람다 적용
    * count
      * 반환 형식 : ``` long ``` (generic)
      * 목적 : 스트림의 요소 개수 반환
    * collect
      * 목적 : 스트림을 리듀스해서 리스트, 맵, 정수 형식의 컬렉션을 만듦

### 정리
* 스트림은 소스에서 추출된 연속 요소로, 데이터 처리 연산을 지원
* 스트림은 내부 반복 지원함 내부 반복은 filter, map, sorted 등의 연산으로 반복을 추상화 함
* 스트림에는 중간 연산과 최종 연산이 있음
* 중간 연산은 filter, map처럼 스트림을 반환하면서 다른 연산과 연결되는 연산
  * 중간 연산을 이용해서 파이프라인을 구성할 수 있지만 중간 연산으로는 어떤 결과도 생성할 수 없음
* forEach나 count처럼 스트림 파이프라인을 처리해서 스트림이 아닌 결과를 반환하는 연산을 최종 연산이라고 함
* 스트림의 요소는 요청할 때 게으르게 계산됨

### [quiz]
* 다음 스트림 동작 코드를 리팩토링 해보기
    ```
    List<String> highCaloricDishes = new ArrayList<>();
    Iterator<String> iterator = menu.iterator();
    while(iterator.haxNext()) {
        Dish dish = iterator.next();
        if(dish.getCalories() > 300) {
            highCaloricDishes.add(dish.getName);
        } 
    }
    ```
    filter 패턴 사용
    ```
    List<String> highCaloricDish = menu.stream().filter(dish -> dish.getCalories() > 300).collect(toList());
    ```

* 다음 스트림 파이프라인에서 중간 연산과 최종 연산을 구별
    ```
    long count = menu.stream()
                        .filter(d -> d.getCalories() > 300)
                        .distinct()
                        .limit(3)
                        .count();
    
    ```
    count는 최종 연산, filter, distinct, limit는 중간 연산
---

## chapter 05 - 스트림 활용
### 필터링
* filter 메서드는 프레디케이트를 인수로 받아서 프레디케이트와 일치하는 모든 요소를 포함하는 스트림을 반환
* 고유 요소로 이루어진 스트림을 반환하는 distinct 메서드도 지원
  * 고유 여부는 객체의 hashCode, equals로 결정됨
  
### 스트림 슬라이싱
* 자바 9는 스트림의 요소를 효과적으로 선택할 수 있도록 takeWhile, dropWhile 두 가지 새로운 메서드 지원

* takeWhile 활용
  * 처음으로 거짓이 되는 지점부터 나머지 요소를 버림
    ```
    List<Dish> slicedMenu1 = 
            specialMenu.stream()
                        .takeWhile(dish -> dish.getCalories() < 320)
                        .collect(toList());
     ```

* dropWhile 활용 (takeWhile과 정반대의 작업 수행)
  * 처음으로 거짓이 되는 지점까지 발견된 요소를 버림
  * 프레디케이트가 거짓이 되면 그 지점에서 작업을 중단하고 남은 모든 요소를 반환
  * 무한한 남은 요소를 가진 무한 스트림에서도 동작함
    ```
    List<Dish> slicedMenu1 = 
            specialMenu.stream()
                        .dropWhile(dish -> dish.getCalories() < 320)
                        .collect(toList());
    ```
  
* 스트림 축소
  * 스트림은 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환하는 limit(n) 메서드를 지원
  * 정렬되지 않은 스트림(소스가 Set 같은)에도 limit 사용 가능
  * 소스가 정렬되어 있지 않다면 limit의 결과도 정렬되지 않은 상태로 반환됨
    ```
    List<Dish> slicedMenu1 = 
            specialMenu.stream()
                    .filter(dish -> dish.getCalories() < 320)
                    .limit(3)
                    .collect(toList());
    ```

* 요소 건너뛰기
  * 스트림은 처음 n개 요소를 제외한 스트림을 반환하는 skip(n) 메서드 지원
  * n개 이하의 요소를 포함하는 스트림에 skip(n) 메서드 호출 시 빈 스트림 반환됨
  * limit(n), skip(n)은 상호 보완적인 연산 수행
  
### 매핑
* map, flatMap 메서드는 특정 데이터를 선택하는 기능 제공

* 스트림의 각 요소에 함수 적용하기
  * 스트림은 함수를 인수로 받는 map 메서드 지원
  * 인수로 제공된 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑됨
    * 이 과정은 기존 값을 '고친다' 라는 개념보다는 '새로운 버전을 만든다' 라는 개념에 가까움
    * 따라서 '변환(transforming)'에 가까운 '매핑(mapping)' 이라는 단어를 사용
    ```
    List<Integer> dishNameLengths =
            menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(toList());
    ```
    
* 스트림 평면화
  * ["Hello", "World"] 리스트 결과를 ["H", "e", "l", "o", "W", "r", "d"] 리스트로 반환할 것
  * 실패 : map 메서드가 반환한 스트림의 형식은 Stream<String[]>
    ```
    words.stream()
        .map(word -> word.split(""))
        .distinct()
        .collect(toList());
    ```

* map, Arrays.stream 활용
  * 배열 스트림 대신 문자열 스트림이 필요
  * 문자열을 받아 스트림을 만드는 Arrays.stream() 메서드가 있음
  * ``` String[] arrayOfWords = {"Goodbye", "World"}; ```
  * ``` Stream<String> streamOfWords = Arrays.stream(arrayOfWords); ```
  * 예제에 적용 
    ```
    words.stream()
        .map(word -> word.split(""))
        .map(Arrays::stream)
        .distinct()
        .collect(toList());
    ```
  * 결국 스트림 리스트 List<Stream\<String>>가 만들어지므로 문제가 해결되지 않음
  * 문제를 해결하려면 먼저 각 단어를 개별 문자열로 이루어진 배열로 만든 다음, 각 배열을 별도의 스트림으로 만들어야 함
  
* flatMap 사용
  * flatMap은 각 배열을 스트림이 아니라 스트림의 콘텐츠로 매핑
    * map(Arrays::stream)과 달리 flatMap은 하나의 평면화된 스트림을 반환
  * flatMap 메서드는 스트림의 각 값을 다른 스트림으로 만든 다음, 모든 스트림을 하나의 스트림으로 연결하는 기능 수행
    ```
    List<String> uniqueCharacters = 
            words.stream()
                  .map(word -> word.split("")) // 각 단어를 개별 문자를 포함하는 배열로 변환
                  .flatMap(Arrays::stream)      // 생성된 스트림을 하나의 스트림으로 평면화
                  .distinct()
                  .collect(toList());
    ```

### 검색과 매칭
* 특정 속성이 데이터 집합에 있는 지 여부를 검색하는 데이터 처리
* 스트림 API는 allMatch, anyMatch, noneMatch, findFirst, findAny 등 다양한 유틸리티 메서드 제공

* anyMatch : 프레디케이트가 적어도 한 요소와 일치하는지 확인
  * anyMatch는 불리언을 반환하므로 최종 연산
  * menu에 채식 요리가 있는지 확인하는 예제
    ```
    if (menu.stream().anyMatch(Dish::isVegetarian)) {
        System.out.println("The menu is (somewhat) vegetarian friendly!");
    }
    ```

* allMatch : 프레디케이트가 모든 요소와 일치하는지 검사
  * allMatch 메서드는 anyMatch와 달리 스트림의 모든 요소가 주어진 프레디케이트와 일치하는 지 검사
  * 메뉴가 건강식(모든 요리가 1000칼로리 이하면 건강식으로 간주)인 지 확인할 수 있음
    ```
    boolean isHealty = menu.stream().allMatch(dish -> dish.getCalories() < 1000);
    ```
    
* noneMatch : allMatch와 반대 연산 수행
  * 주어진 프레디케이트와 일치하는 요소가 없는지 확인
    ```
    boolean isHealty = menu.stream().noneMatch(d -> d.getCalories() >= 1000);
    ```

* anyMatch, allMatch, noneMatch 세 메서드는 스트림 쇼트서킷 기법, 즉 자바의 &&, || 같은 연상을 활용함

* 쇼트 서킷 평가
  * 때로는 전체 스트림을 처리하지 않았더라도 결과를 반환할 수 있음
  * 예를 들어 and 연산으로 연결된 커다란 불리언 표현식을 평가한다고 가정
    * 표현식에서 하나라도 거짓이라는 결과가 나오면 나머지 표현식의 결과와 상관없이 전체 결과도 거짓이 되는데, 이러한 상황을 쇼트 서킷 이라고 함
  * allMatch, noneMatch, findFirst, findAny 등의 연산은 모든 스트림의 요소를 처리하지 않고도 결과를 반환할 수 있음
    * 원하는 요소를 찾았으면 즉시 결과를 반환할 수 있음
    * 마찬가지로 스트림의 모든 요소를 처리할 필요 없이 주어진 크기의 스트림을 생성하는 limit도 쇼트 서킷 연산
    * 특히 무한한 요소를 가진 스트림을 유한한 크기로 줄일 수 있는 유용한 연산
      
* 쇼트 서킷 추가 설명
  * 특정 표현식으로 인해 추가 표현식들을 연산 처리 하지 않는(생략하는) 상황
    * & 와 | 연산자 : 앞 조건식과 뒤 조건식을 무조건 둘 다 실행 시킴
    * && 와 || 연산자 : 앞 조건식의 결과에 따라 뒤 조건식의 실행 여부를 결정
      * 쇼트 서킷

* 요소 검색
  * findAny 메서드는 현재 스트림에서 임의의 요소를 반환
    * 다른 스트림연산과 연결해서 사용 가능
    * 예를 들어 filter와 findAny를 이용해 채식 요리를 선택할 수 있음
      ```
      Optional<Dish> dish = 
            menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();        
      ```

* Optional이란
  * Optional\<T> 클래스는 값의 존재나 부재 여부를 표현하는 컨테이너 클래스
  * 위 예제에서 findAny 메서드는 아무 요소도 반환하지 않을 수 있음
  * isPresent()는 Optional이 값을 포함하면 true, 값을 포함하지 않으면 false 반환
  * ifPresent(Consumer\<T> block)은 값이 있으면 주어진 블록을 실행함
    * Consumer 함수형 인터페이스는 T 형식의 인수를 받으며 void를 반환하는 람다를 전달할 수 있음
  * T get()은 값이 존재하면 값을 반환, 값이 없으면 NoSuchElementException을 일으킴
  * T orElse(T other)는 값이 있으면 값을 반환, 값이 없으면 기본값을 반환
    ```
    menu.stream()
        .filter(Dish::isVegetarian)
        .findAny()
        .ifPresent(dish -> System.out.println(dish.getName()));
    ```
  
* 첫 번째 요소 찾기
  * 리스트 또는 정렬된 연속 데이터로부터 생성된 스트림처럼 일부 스트림에는 논리적인 아이템 순서가 정해져 있을 수 있음
    * 이런 스트림에서 첫 번째 요소를 찾기
      ```
      List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
      Optional<Integer> firstSquareDivisibleByThree = 
            someNumbers.stream()
                        .map(n -> n * n)
                        .filter(n -> n % 3 == 0)
                        .findFirst();
      ```
      
* findFirst와 findAny 사용
  * 병렬성 때문에 두 메서드 모두 필요함
  * 병렬 실행에서 첫 번째 요소를 찾기 어려움
  * 따라서 요소의 반환 순서가 상관없다면 병렬 스트림에서는 제약이 적은 findAny를 사용
  
### 리듀싱
* 리듀스 연산을 이용, 아래와 같은 질의 즉 스트림 요소를 조합해서 더 복잡한 질의를 표현하는 방법을 설명
  * '메뉴의 모든 칼로리의 합계를 구하시오'
  * '메뉴에서 칼로리가 가장 높은 요리는?'
* 아래와 같은 질의를 수행하려면 Integer 같은 결과가 나올 때까지 스트림의 모든 요소를 반복적으로 처리해야 함
* 이런 질의를 리듀싱 연산이라고 함
  * 모든 스트림 요소를 처리해서 값으로 도출
  * 함수형 프로그래밍 용어로는 폴드(fold)라고 부름
    * 이 과정이 마치 종이를 작은 조각이 될 때까지 반복해서 접는 것과 비슷하다는 의미
    
* 요소의 합
  * for 루프를 이용한 리스트의 일반적인 덧셈 처리
    ```
    int sum = 0;
    for (int x : numbers)  {
        sum += x;
    }
    ```
  * reduce 연산을 이용한 덧셈 처리
    ```
    int sum = numbers.stream().reduce(0, (a, b) -> a + b);
    ```
  * reduce 연산을 이용한 곱셈 처리
    ```
    int product = numbers.stream().reduce(1, (a, b) -> a * b);
    ```
  * 초깃값을 받지 않는 reduce 메서드
    * 이 때 Optional\<Integer> 반환 : 스트림에 아무 요소도 없을 수 있음
    ```
    int sum = numbers.stream().reduce(0, (a, b) -> a + b);
    ```
    
* 최댓값, 최솟값
  * 최댓값
    ```
    Optional<Integer> max = numbers.stream().reduce(Integer::max);
    ```
  * 최솟값
    ```
    Optional<Integer> min = numbers.stream().reduce(Integer::min);
    ```
  * 메서드 참조 대신 ``` (x, y) -> x < y ? x : y; ``` 를 사용해도 무방하나 메서드 참조 표현이 더 읽기 쉬움
  
* reduce 메서드의 장점과 병렬화
  * reduce를 이용하면 내부 반복이 추상화되면서 내부 구현에서 병렬로 reduce를 실행할 수 있게 됨
  * 반복적인 합계에서는 sum 변수를 공유해야 하므로 쉽게 병렬화하기 어려움
  * 강제적으로 동기화 시키더라도 결국 병렬화로 얻어야 할 이득이 스레드 간의 소모적인 경쟁 때문에 상쇄되어 버림
  * 사실 이 작업을 병렬화하려면 입력을 분할하고, 분할된 입력을 더한 다음에, 더한 값을 합쳐야 함
  * 지금까지 살펴본 코드와 다른 양상
  * 가변 누적자 패턴(mutable accumulator pattern)은 병렬화와 거리가 너무 먼 기법
  * stream()을 parallelStream()으로 바꾸면 됨
    ``` int sum = numbers.parallelStream().reduce(0, Integer::sum); ```
    * 위 코드를 병렬로 실행하려면 대가를 지불해야 함 
      * reduce에 넘겨준 람다의 상태(인스턴스 변수 같은)가 바뀌지 말아야 하며
      * 연산이 어떤 순서로 실행되더라도 결과가 바뀌지 않는 구조여야 함

* 스트림 연산 : 상태 없음과 상태 있음
  * 스트림 연산은 각각 다양한 연산을 수행하기 때문에 각각의 연산은 내부적인 상태를 고려해야 함
  * map, filter 등은 입력 스트림에서 각 요소를 받아 0 또는 결과를 출력하는 스트림으로 보냄
  * 따라서 (사용자가 제공한 람다나 메서드 참조가 내부적인 가변 상태를 갖지 않는다는 가정하에)
  * 이들은 보통 상태가 없는, 즉 내부 상태를 갖지 않는 연산(stateless operation)이라 함
  * 하지만 reduce, sum, max 같은 연산은 결과를 누적할 내부 상태가 필요함
  * 예제의 내부 상태는 작은 값
  * 스트림에서 처리하는 요소 수와 관계없이 내부 상태의 크기는 한정(bounded)되어 있음
  * 반면 sorted, distinct 같은 연산은 filter, map처럼 스트림을 입력으로 받아 다른 스트림을 출력하는 것처럼 보임
  * 하지만 sorted, distinct는 filter, map과 다름
  * 스트림의 요소를 정렬하거나 중복을 제거하려면 과거의 이력을 알고 있어야 함
    * 예를 들어 어떤 요소를 출력 스트림으로 추가하려면 모든 요소가 버퍼에 추가되어 있어야 함
  * 연산을 수행하는 데 필요한 저장소 크기는 정해져 있지 않음
  * 따라서 데이터 스트림의 크기나 무한이라면 문제가 생길 수 있음
    * 예를 들어 모든 소수를 포함하는 스트림을 역순으로 만드는 경우?
    * 첫 번째 요소로 가장 큰 소수, 즉 세상에서 존재하지 않는 수를 반환해야 함
  * 이러한 연산을 내부 상태를 갖는 연산(stateful operation)이라 함
  
* 연산 요약
  * filter
    * 형식 : 중간 연산
    * 반환 형식 : ``` Stream<T> ```
    * 사용된 함수형 인터페이스 형식 : ``` Predicate<T> ```
    * 함수 디스크립터 : ``` T -> boolean ```
  * distinct
    * 형식 : 중간 연산(상태 있는 언바운드)
    * 반환 형식 : ``` Stream<T> ```
  * takeWhile
    * 형식 : 중간 연산
    * 반환 형식 : ``` Stream<T> ```
    * 사용된 함수형 인터페이스 형식 : ``` Predicate<T> ```
    * 함수 디스크립터 : ``` T -> boolean ```
  * dropWhile
    * 형식 : 중간 연산
    * 반환 형식 : ``` Stream<T> ```
    * 사용된 함수형 인터페이스 형식 : ``` Predicate<T> ```
    * 함수 디스크립터 : ``` T -> boolean ```
  * skip
    * 형식 : 중간 연산(상태 있는 바운드)
    * 반환 형식 : ``` Stream<T> ```
    * 사용된 함수형 인터페이스 형식 : ``` long ```
  * limit
    * 형식 : 중간 연산(상태 있는 바운드)
    * 반환 형식 : ``` Stream<T> ```
    * 사용된 함수형 인터페이스 형식 : ``` long ```
  * map
    * 형식 : 중간 연산
    * 반환 형식 : ``` Stream<R> ```
    * 사용된 함수형 인터페이스 형식 : ``` Function<T, R> ```
    * 함수 디스크립터 : ``` T -> R ```
  * flatMap
    * 형식 : 중간 연산
    * 반환 형식 : ``` Stream<R> ```
    * 사용된 함수형 인터페이스 형식 : ``` Function<T, Stream<R>> ```
    * 함수 디스크립터 : ``` T -> Stream<R> ```
  * sorted
    * 형식 : 중간 연산(상태 있는 언바운드)
    * 반환 형식 : ``` Stream<T> ```
    * 사용된 함수형 인터페이스 형식 : ``` Comparator<T> ```
    * 함수 디스크립터 : ``` (T, T) -> int ```
  * anyMatch
    * 형식 : 최종 연산
    * 반환 형식 : ``` boolean ```
    * 사용된 함수형 인터페이스 형식 : ``` Predicate<T> ```
    * 함수 디스크립터 : ``` T -> boolean ```
  * noneMatch
    * 형식 : 최종 연산
    * 반환 형식 : ``` boolean ```
    * 사용된 함수형 인터페이스 형식 : ``` Predicate<T> ```
    * 함수 디스크립터 : ``` T -> boolean ```
  * allMatch
    * 형식 : 최종 연산
    * 반환 형식 : ``` boolean ```
    * 사용된 함수형 인터페이스 형식 : ``` Predicate<T> ```
    * 함수 디스크립터 : ``` T -> boolean ```
  * findAny
    * 형식 : 최종 연산
    * 반환 형식 : ``` Optional<T> ```
  * findFirst
    * 형식 : 최종 연산
    * 반환 형식 : ``` Optional<T> ```
  * forEach
    * 형식 : 최종 연산
    * 반환 형식 : ``` void ```
    * 사용된 함수형 인터페이스 형식 : ``` Consumer<T> ```
    * 함수 디스크립터 : ``` T -> void ```
  * collect
    * 형식 : 최종 연산
    * 반환 형식 : ``` R ```
    * 사용된 함수형 인터페이스 형식 : ``` Collector<T, A, R> ```
  * reduce
    * 형식 : 최종 연산(상태 있는 바운드)
    * 반환 형식 : ``` Optional<T> ```
    * 사용된 함수형 인터페이스 형식 : ``` BinaryOperator<T> ```
    * 함수 디스크립터 : ``` (T, T) -> T ```
  * count
    * 형식 : 최종 연산
    * 반환 형식 : ``` long ```
    
### 실전 연습
* 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리
* 거래자가 근무하는 모든 도시를 중복 없이 나열
* 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬
* 모든 거래자의 이름을 알파벳순으로 정렬해서 반환
* 밀라노에 거래자가 있는가
* 케임브리지에 거주하는 거래자의 모든 트랜잭션 값을 추출
* 전체 트랜잭션 중 최대값은?
* 전체 트랜잭션 중 최솟값은?

### 숫자형 스트림
* 아래와 같은 코드는 박싱 비용이 숨어 있음
  ```
  int calories = menu.stream()
        .map(Dish::getCalories)
        .reduce(0, Integer::sum);
  ```

* 기본형 특화 스트림
  * 다음과 같은 세가지 기본형 특화 스트림 제공
    * IntStream
    * DoubleStream
    * LongStream
  * 기본형 특화 스트림은 sum, max, 같이 자주 사용하는 숫자 관련 리듀싱 연산 수행 메서드 제공
  * 또한 필요할 때 다시 객체 스트림으로 복원하는 기능도 제공
  * 특화 스트림은 오직 박싱과정에서 일어나는 효율성과 관련이 있고, 추가 기능을 제공하지는 않음
  * 숫자 스트림으로 매핑, 특화 스트림으로 변환하는 메서드
    * mapToInt
      * mapToInt 메서드는 IntStream을 반환 (Stream\<Integer>가 아님)
      * IntStream은 max, min, average 등 다양한 유틸리티 메서드 제공
      ```
      int calories = menu.stream()
              .mapToInt(Dish::getCalories)
              .sum();
      ```
    * mapToDouble
    * mapToLong
  * 객체 스트림으로 북원
    * IntStream은 기본형의 정수 값만 만들 수 있음
    * IntStream의 map 연산은 IntUnaryOperator(int를 인수로 받아 int를 반환하는 람다)를 인수로 받음
    * boxed 메서드를 이용하여 특화 스트림을 일반 스트림으로 변환
      ```
      IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
      Stream<Integer> stream = intStream.boxed();
      ```
  * 기본값 : OptionalInt
    * IntStream에서 최댓값을 찾을 때는 0이라는 기본값 때문에 잘못된 결과가 도출될 수 있음
    * Optional을 Integer, String, 등의 참조 형식으로 파라미터화 할 수 있음
    * OptionalInt, OptionalDouble, OptionalLong 세 가지 기본형 특화 스트림 버전도 제공
      ```
      OptionalInt maxCalories = menu.stream()
            .mapToInt(Dish::getCalories)
            .max();
      int max = maxCalories.orElse(1); // 값이 없을 때 기본 최대값을 명시적으로 설정
      ```
      
* 숫자 범위
  * IntStream, LongStream에서는 range, rangeClosed라는 두가지 메서드 제공
    * 두 메서드 모두 첫 번째 인수로 시작값, 두 번째 인수로 종료값을 받음
    * range 메서드는 인수로 받은 시작값과 종료값이 결과에 포함되지 않는 반면 rangeClosed는 결과에 포함됨
      ```
      IntStream evenNumbers = IntStream.rangeClosed(1, 100)
              .filter(n -> n % 2 == 0);
      System.out.println(evenNumbers.count()); // 50(개) 출력됨
      ```
    * 위 예제는 filter를 호출해도 실제로 아무런 계산도 이루어지지 않음
      * count는 최종 연산이므로 스트림을 처리함
      * rangeClosed가 아닌 range 메서드 사용시 1, 100을 포함하지 않으므로 짝수 49개를 반환함
      
* 숫자 스트림 활용 : 피타고라스 수
  * 피타고라스 수 : a * a + b * b = c * c
    * (a, b, c)
  * 세 수 표현
    * 여기서는 세 수를 표현할 새 클래스 정의보다 세 요소를 갖는 int 배열을 사용함
      * new int[]{3, 4, 5}
  * 좋은 필터링 조합
    * a * a + b * b의 제곱근이 정수인지 확인
      ```
      Math.sqrt(a*a + b*b) % 1 == 0;
      ```
    * 이 때 x가 부동 소숫점 수라면 x % 1.0이라는 자바 코드로 소숫점 이하 부분을 얻을 수 있음
    * 이를 filter에 다음처럼 활용
      ```
      filter(b -> Math.sqrt(a*a + b*b) % == 1)
      ```
    * 위 코드에서 a라는 값이 주어지고 b는 스트림으로 제공된다고 가정할 때 filter로 a와 함께 피타고라스 수를 구성하는 모든 b를 필터링 할 수 있음
  * 집합 생성
    * 마지막 세 번째 수 찾기
      ```
      stream.filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
            .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)});
      ```
  * b값 생성
    ```
    IntStream.rangeClosed(1, 100)
            .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
            .boxed()
            .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)});
    ```
    * filter 연산 다음에 rangeClosed가 반환한 IntStream을 boxed를 이용해서 Stream\<Integer>로 복원
    * map은 스트림의 각 요소를 int 배열로 변환하기 때문
    * 여기서 Intstream의 map메서드는 스트림의 각 요소로 int가 반환될 것을 기대하지만 이는 우리가 원하는 연산이 아님
    * 개체값 스트림을 반환하는 IntStream의 mapToObj 메서드를 이용, 이 코드를 재구현
      ```
      IntStream.rangeClosed(1, 100)
              .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
              .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)});
      ```
  * a값 생성
    ```
    Stream<int[]> pythagoreanTriples =
        IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> 
                    IntStream.rangeClosed(a, 100)
                            .filter(b-> Math.sqrt(a*a + b*b) % 1 == 0))
                        .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}
                );
    ```
    * a에서 사용할 1부터 100까지의 숫자를 만듦
    * 주어진 a를 이용해서 세 수의 스트림을 만듦
    * 스트림 a의 값을 매핑하면 스트림의 스트림이 만들어질 것
    * 따라서 flatMap 메서드는 생성된 각각의 스트림을 하나의 평준화된 스트림으로 만들어줌
    * 또한 b의 범위가 a에서 100으로 바뀐점 유의할 것
      * b를 1부터 시작하면 중복된 세개의 수가 생성될 수 있음
        * 예를 들어 (3, 4, 5)와 (4, 3, 5)가 생성될 수 있음
  * 코드 실행
    ```
    pythagoreanTriples.limit(5)
                    .forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
    ```
  * 개선
    * 현재 문제 해결 코드에서는 제곱근을 두 번 계산함
    * 따라서 ``` (a*a, b*b, a*a+b*b) ``` 형식을 만족하는 세개의 수를 만든 다음, 원하는 조건에 맞는 결과만 필터링 하는 것이 더 최적화된 방법
      ```
      Stream<double[]> pythagoreanTriples2 =
          IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100))
                .mapToObj(b -> new double[]{a, b, Math.sqrt(a*a + b*b)})
                .filter(t -> t[2] % 1 == 0);
      ```

### 스트림 만들기
* 값으로 스트림 만들기
  * 임의의 수를 인수로 받는 정적 메서드 Stream.of를 이용, 스트림 생성 가능
  * Stream.of로 문자열 스트림을 만들기
    ```
    Stream<String> stream = Stream.of("Modern", "Java", "In", "Action");
    stream.map(String::toUpperCase).forEach(System.out::println);
    ```
  * empty 메서드를 이용해 스트림 비우기
    ```
    Stream<String> emptyStream = Stream.empty();
    ```

* null이 될 수 있는 객체로 스트림 만들기
  * 자바 9에서 null이 될 수 있는 개체를 스트림으로 만들 수 있는 새로운 메서드 추가
    * 기존
      ```
      String homeValue = System.getProperty("home");
      Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(value);
      ```
    * Stream.ofNullable 이용
      ```
      Stream<String> homeValueStream = Stream.ofNullable(System.getProperty("home"));
      ```
    * null이 될 수 있는 개체를 포함하는 스트림 값을 flatMap과 함께 사용하는 상황
      ```
      Stream<String> values = Stream.of("config", "home", "user")
            .flatMap(key -> Stream.ofNullable(System.getProperty(key)));
      ```

* 배열로 스트림 만들기
  * 배열을 인수로 받는 정적 메서드 Arrays.Stream을 이용해서 스트림 생성 가능
    * 예를 들어 기본형 int로 이루어진 배열을 IntStream으로 변환 가능
      ```
      int[] numbers = {2, 3, 5, 7, 11, 13};
      int sum = Arrays.stream(numbers).sum();
      ```

* 파일로 스트림 만들기
  * 파일을 처리하는 등의 I/O 연산에 사용하는 자바의 NIO API(비블록킹 I/O)도 스트림 API를 활용할 수 있도록 업데이트 됨
    * java.nio.file.Files의 많은 정적 메서드가 스트림을 반환함
    * 예를 들어 Files.lines는 주어진 파일의 행 스트림을 문자열로 반환
      ```
      long uniqueWords = 0;
      // 스트림은 자원을 자동으로 해제할 수 있는 AutoCloseable이므로 try-finally가 필요 없음
      try (Stream<String> lines = Files.lines(Paths.get("data.txt"), 
            Charset.defaultCharset())) {
      
        // 고유 단어 계산
        uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                .distinct()
                .count();
      } catch(IOException e) {
        // 파일을 열다가 예외가 발생시 처리
      }
      ```

* 함수로 무한 스트림 만들기
  * 함수에서 스트림을 만들 수 있는 두 정적 메서드 Stream.iterate와 Stream.generate를 제공
  * 위 두 연산을 이용해 무한 스트림(infinite stream)을 만들 수 있음
    * 즉 고정된 컬렉션에서 고정된 크기로 스트림을 만들었던 것과는 달리 크기가 고정되지 않은 스트림을 만들 수 있음
  * iterate와 generate에서 만든 스트림은 요청할 때마다 주어진 함수를 이용해서 값을 만듦
    * 하지만 일반적으로 무한한 값을 출력하지 않도록 limit(n) 함수를 함께 연결해서 사용함
  * iterate 메서드
    ```
    Stream.iterate(0, n -> n + 2)
        .limit(10)
        .forEach(System.out::println);
    ```
    * 초깃값과 람다를 인수로 받아 새로운 값을 끊임없이 생산할 수 있음
    * 기본적으로 iterate 메서드는 기존 결과에 의존해서 순차저으로 연산을 수행
    * iterate 메서드는 요청할 때마다 값을 생산할 수 있으며 끝이 없으므로 무한 스트림(infinite stream)을 만듦
      * 이러한 스트림을 언바운드 스트림(unbounded stream)이라 표현함
      * 이런 특징이 컬렉션과 차이점
    * 일반적으로 연속된 일련의 값을 만들때는 iterate를 사용
    * 자바 9의 iterate 메서드는 프레디케이트를 지원함
      * 예를 들어 0에서 시작해 100보다 크면 숫자 생성을 중단하는 코드를 아래와 같이 작성할 수 있음
        ```
        IntStream.iterate(0, n < 100, n -> n + 4)
                .forEach(System.out::println);
        ```
      * iterate 메서드는 두 번째 인수로 프레디케이트를 받아 언제까지 작업을 수행할 것인지의 기준으로 사용함
      * 아래와 같은 코드는 종료되지 않아 같은 결과를 얻을 수 없음
        ```
        IntStream.iterate(0, n -> n + 4)
                .filter(n -> n < 100)
                .forEach(System.out::println);
        ```
        * filter 메서드가 언제 이 작업을 중단해야 하는지 알 수 없기 때문
      * 스트림 쇼트 서킷을 지원하는 takeWhile을 이용
        ```
        IntStream.iterate(0, n -> n + 4)
                .takeWhile(n -> n < 100)
                .forEach(System.out.::println);
        ```
  * generate 메서드
    * generate 메서드도 요구할 때 값을 계산하는 무한 스트림을 얻을 수 있음
    * 하지만 iterate와 달리 generate는 생산된 각 값을 연속적으로 계산하지 않음
    * Supplier\<T>를 인수로 받아 새로운 값을 생산
      ```
      Stream.generate(Math::random)
            .limit(5)
            .forEach(System.out::println);
      ```
    * 위 예제에서 limit가 없다면 스트림은 언바운드 상태가 됨
    * 상태가 없는 메서드, 즉 나중에 계산에 사용할 어떤 값도 저장해두지 않음
      * 하지만 발행자에 꼭 상태가 없어야 하는 것은 아님
      * 병렬 코드에서는 발행자에 상태가 있으면 안전하지 않음
      * 상태를 갖는 발행자는 실제로 피해야 함
    * IntStream의 generate 메서드는 Supplier\<T> 대신에 IntSupplier를 인수로 받음
      ```
      IntStream ones = IntStream.generate(() -> 1);
      ```
      * 아래처럼 getAsInt를 구현하는 객체를 명시적으로 전달할 수 있음
        ```
        IntStream twos = IntStream.generate(new IntSupplier() {
            public int getAsInt() {
                return 2;
            }
        });
        ```
        * 여기서 익명 클래스와 람다는 비슷한 연산을 수행
          * 하지만 익명 클래스에서는 getAsInt 메서드의 연산을 커스터마이즈 할 수 있는 상태 필드를 정의할 수 있다는 점이 다름
          * 따라서 부작용이 생길 수 있음
      * 피보나치 수열
        ```
        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;
            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
            }
        };
        IntStream.generate(fib)
                .limit(10)
                .forEach(System.out::println);
        ```
        * 위 코드에서 IntSupplier 인스턴스를 만듦
        * 만들어진 객체는 기존 피보나치 요소와 두 인스턴스 변수에 어떤 피보나치 요소가 들어 있는지 추적하므로 가변(mutable) 상태 객체
        * getAsInt를 호출하면 객체 상태가 바뀌며 새로운 값 생산
        * iterate를 사용했을 때는 각 과정에서 새로운 값을 생성하면서도 기존 상태를 바꾸지 않는 순수한 불변(immutable) 상태를 유지했음
        * 스트림을 병렬로 처리하면서 올바른 결과를 얻으려면 불변 상태 기법을 고수해야 함
  * 무한한 크기를 가진 스트림 처리 시 limit을 이용하여 명시적으로 크기를 제한해야 함
    * 그렇지 않으면 최종 연산을 수행했을 때 아무 결과도 계산되지 않음
    * 마찬가지로 정렬하거나 리듀스 할 수 없음
    
### 정리
* 스트림 API를 이용하면 복잡한 데이터 처리 질의를 표현할 수 있음
* filter, distinct, takeWhile(java9), dropWhile(java9), skip, limit 메서드로 스트림을 필터링 하거나 자를 수 있음
* 소스가 정렬되어 있다는 사실을 알고 있을 때 takeWhile, dropWhile 메서드를 효과적으로 사용할 수 있음
* map, flatMap 메서드로 스트림의 요소를 추출하거나 변환할 수 있음
* findFirst, findAny 메서드로 스트림의 요소를 검색할 수 있음
  * allMatch, noneMatch, anyMatch 메서드를 이용, 주어진 프레디케이트와 일치하는 요소를 스트림에서 검색할 수 있음
  * 이들 메서드는 쇼트서킷(short-circuit), 즉 결과를 찾는 즉시 반환하며, 전체 스트림을 처리하지 않음
* reduce 메서드로 스트림의 모든 요소를 반복 조합하며 값을 도출할 수 있음
  * 예를 들어 reduce로 스트림의 최댓값이나 모든 요소의 합계를 계산할 수 있음
* filter, map 등은 상태를 저장하지 않는 상태 없는 연산(stateless operation)
* reduce 같은 연산은 값을 계산하는 데 필요한 상태를 저장함
* sorted, distinct 등의 메서드는 새로운 스트림을 반환하기에 앞서 스트림의 모든 요소를 버퍼에 저장해야 함
  * 상태 있는 연산(stateful operation)
* IntStream, DoubleStream, LongStream은 기본형 특화 스트림
* 컬렉션 뿐 아니라 값, 배열, 파일, iterate, generate 같은 메서드로도 스트림을 만들 수 있음
* 무한한 개수의 요소를 가진 스트림을 무한 스트림이라 함

### [quiz]
* 스트림을 이용, 처음 등장하는 두 고기 요리를 필터링 하기
  * 정답
    ```
    List<Dish> dishes = 
            menu.stream()
                .filter(d -> d.getType() == Dish.Type.MEAT)
                .limit(2)
                .collect(toList());
    ```
    
* 숫자 리스트가 주어졌을 때 각 숫자의 제곱근으로 이루어진 리스트를 반환하기
  * 예를 들어 [1, 2, 3, 4, 5]가 주어지면 [1, 4, 9, 16, 25]를 반환해야 함
    ```
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    List<Integer> squares = 
            numbers.stream()
                    .map(n -> n * n)
                    .collect(toList());
    ```

* 두 개의 숫자 리스트가 있을 때 모든 숫자 쌍의 리스트를 반환하기
  * 예를 들어 두 개의 리스트 [1, 2, 3]과 [3, 4]과 주어지면 [(1, 3), (1, 4), (2, 3), (2, 4), (3, 3), (3, 4)]를 반환해야 함
    ```
    List<Integer> numbers1 = Arrays.asList(1, 2, 3);
    List<Integer> numbers2 = Arrays.asList(3, 4);
    List<int[]> pairs = 
            numbers1.stream()
                    .flatMap(n1 -> numbers2.stream().map(n2 -> new int[]{n1, n2}))
                    .collect(toList());
    ```
  
* 이전 예제에서 합이 3으로 나누어 떨어지는 쌍만 반환하기
  * 예를 들어 (2, 4), (3, 3)을 반환해야 함
    ```
    List<Integer> numbers1 = Arrays.asList(1, 2, 3);
    List<Integer> numbers2 = Arrays.asList(3, 4);
    List<int[]> pairs = 
            numbers1.stream()
                    .flatMap(n1 -> numbers2.stream()
                            .filter(n2 -> (n1 + n2) % 3 == 0)
                            .map(n2 -> new int[]{n1, n2}))
                    .collect(toList());
    ```

* map, reduce 메서드를 이용해서 스트림의 요리 개수를 계산하기
  * 
    ```
    menu.stream()
            .map(d -> 1)
            .reduce(0, (a, b) -> a + b);
    ```
  
  * 맵과 리듀스를 연결하는 기법을 맵 리듀스(map-reduce) 패턴이라 함
    * 쉽게 병렬화하는 특징 덕분에 구글이 웹 검색에 적용하면서 유명해짐
  * 4장에서 count로 스트림 요소를 세는 방법을 살펴봄
    ``` long count = menu.stream().count(); ```
    
* 피보나치수열 집합
  * iterate 메서드를 이용, 피보나치수열의 집합을 20개 만들기
  * UnaryOperator\<T>를 인수로 받는 iterate 메서드를 이용
    ```
    Stream.iterate(new int[]{0, 1}, ???)
        .limit(20)
        .forEach(t -> System.out.println("(" + t[0] + "," + t[1] + ")"));
    ```
  * 답 : ``` n -> new int[]{n[1], n[0] + n[1]} ```
    ```
    Stream.iterate(new int[]{0, 1}, n -> new int[]{n[1], n[0] + n[1]})
        .limit(20)
        .forEach(t -> System.out.println("(" + t[0] + "," + t[1] + ")"));
    ```
---

## chapter 06 - 스트림으로 데이터 수집
* 스트림은 게으른 반복자
* 중간 연산은 한 스트림을 다른 스트림으로 변환하는 연산, 여러 연산을 연결
  * 스트림 파이프라인을 구성하며, 스트림의 요소를 소비하지 않음
* 스트림의 요소를 소비해서 최종 결과를 도출
  * 스트림 파이프라인을 최적화하면서 계산 과정을 짧게 생략하기도 함
* collect와 컬렉터로 구현할 수 있는 질의 예제
  * 통화별로 트랜잭션을 그룹화한 다음에 해당 통화로 일어난 모든 트랜잭션 합계를 계산하기
  * 트랜잭션을 비싼 트랜잭션과 저럼한 트랜잭션 두 그룹으로 분류하기(Map<Boolean, List\<Transaction>> 반환)
  * 트랜잭션을 도시 등 다수준으로 그룹화하기
    * 그리고 각 트랜잭션이 비싼지 저렴한지 구분하기(Map<String, Map<Boolean, List\<Transaction>>> 반환)

### 컬렉터란 무엇인가?
* 함수형 프로그래밍에서는 '무엇'을 원하는지 직접 명시할 수 있어서 어떤 방법으로 이를 얻을지는 신경 쓸 필요가 없음
* 이전 예제에서 collect 메서드로 Collector 인터페이스 구현을 전달
  * Collector 인터페이스 구현은 스트림의 요소를 어떤 식으로 도출할지 지정
  * 5장에서 '각 요소를 리스트로 만들어라'를 의미하는 toList를 Collector 인터페이스의 구현으로 사용함
  * 여기서는 groupingBy를 이용 '각 키 버킷 그리고 각 키 버킷에 대응하는 요소 리스트를 값으로 포함하는 맵을 만드는 동작' 수행
* 다수준(multilevel)으로 그룹화를 수행할 때 명령형 프로그래밍과 함수형 프로그래밍의 차이점이 더욱 두드러짐
  * 명령형 코드에서는 문제글 해결하는 과정에서 다중 루프와 조건문을 추가하며 가독성, 유지보수성이 크게 떨어짐
  * 함수형 프로그래밍에서는 필요한 컬렉터를 쉽게 추가할 수 있음
  
### 고급 리듀싱 기능을 수행하는 컬렉터
* 훌륭한 함수형 API는 높은 수준의 조합성과 재사용성이 장점
* collect로 결과를 수집하는 과정을 간단, 유연한 방식으로 정의할 수 있다는 점이 컬렉터의 장점
  * 구체적으로 스트림에 collect를 호출하면서 스트림의 요소(컬렉터로 파라미터화된)에 리듀싱 연산이 수행됨
* 보통 함수를 요소로 변환할 때는 컬렉터를 적용하며 최종 결과를 저장하는 자료구조에 값을 누적함
  * toList처럼 데이터 자체를 변환하는 것 보다는 데이터 저장 구조를 변환할 때가 많음
* Collectors에서 제공하는 메서드의 기능
  * 스트림 요소를 하나의 값으로 리듀스하고 요약
  * 요소 그룹화
  * 요소 분할

### 리듀싱과 요약
* 기존 : ``` long howManyDishes = menu.stream().collect(Collectors.counting()); ```
* 불필요과정 생략 : ``` long howManyDishes = menu.stream().count(); ```
* counting 컬렉터는 다른 컬렉터와 함께 사용할 때 위력을 발휘함
* 예제 코드는 Collectors 클래스의 정잭 팩토리 메서드를 모두 임포트 했다고 가정함

* 스트림 값에서 최댓값과 최솟값 검색
  * 최댓값 : Collectors.maxBy
  * 최솟값 : Collectors.minBy
  * 위 두 컬렉터는 스트림의 요소를 비교하는데 사용할 Comparator를 인수로 받음
    ```
    Comparator<Dish> dishCaloriesComparator = Comparater.comparingInt(Dish:;getCalories);
    ```
    ```
    Optional<Dish> mostCalories = menu.stream().collect(maxBy(dishCaloriesComparator));
    ```
  * 스트림에 있는 객체의 숫자 필드의 합계나 평균 등을 반환하는 연산에도 리듀싱 기능이 자주 사용됨
    * 이러한 연산을 요약(summarization)연산이라 함

* 요약 연산
  * Collectors 클래스는 Collectors.summingInt라는 특별한 요약 팩토리 메서드 제공
  * 메뉴 리스트의 총 칼로리를 계산하는 코드
    ``` int totalCalories = menu.stream().collect(summingInt(Dish::getCalories)); ```
  * Collectors.summingLong과 Collectors.summingDouble 메서드는 같은 방식으로 동작하며 long, double 형식의 데이터로 요약한다는 점만 다름
  * 단순 합계 외에 평균값 계산 등의 연산도 요약 기능으로 제공됨
    * averagingInt, averagingLong, averagingDouble 등
      ``` double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories)); ```
  * 여러 개의 연산을 한 번에 수행할 때
    ``` IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories)); ```
    * 위 코드 실행 후 출력(예시)
      ``` IntSummaryStatistics{count=9, sum=4300, min=120, average=477.777778, max=800} ```
    * LongSummaryStatistics, DoubleSummaryStatistics 클래스도 있음

* 문자열 연결
  * 컬렉터에 joining 팩토리 메서드를 이용하면 스트림의 각 객체에 toString 메서드를 호출, 추출한 모든 문자열을 하나의 문자열로 연결해서 반환함
    ``` String shortMenu = menu.stream().map(Dish::getName).collect(joining()); ```
  * 객체가 toString() 메서드를 포함하고 있다면 map으로 추출하는 과정 생략 가능
    ``` String shortMenu = menu.stream().collect(joining()); ```
  * 위 두 코드 모두 아래 결과 도출
    ``` porkbeefchickenfrench friesriceseason fruitpizzaprawnssalmon ```
  * 콤마로 구분
    ``` String shortMenu = menu.stream().collect(joining(", ")); ```
    ``` pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon ```
  * joining 메서드는 내부적으로 StringBuilder를 이용해서 문자열을 하나로 만듦

* 범용 리듀싱 요약 연산
  * 위 컬렉터들은 범용 Collectors.reducing 팩토리 메서드로도 정의 가능
  * 범용 팩토리 메서드 대신 특화된 컬렉터를 사용한 이유는 프로그래밍적 편의성 때문
    * 하지만 편의성 뿐만 아니라 가독성도 중요함
  * 예를 들어 reducing 메서드로 만들어진 컬렉터로도 메뉴의 모든 칼로리 합계를 계산할 수 있음
    ``` int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j)); ```
    * reducing은 인수 세 개를 받음
    * 첫 번째 인수는 리듀싱 연산의 시작값이거나 스트림에 적합한 인수가 없을 때 반환 값
      * 숫자 합계에서는 인수가 없을 때 반환값으로 0이 적합
    * 두 번째 인수는 요리를 칼로리 정수로 변환할 때 사용한 변환 함수
    * 세 번째 인수는 같은 종류의 두 항목을 하나의 값으로 더하는 BinaryOperator
  * 한 개의 인수를 가진 reducing 버전을 이용해 찾는 방법
    ```
    Optional<Dish> mostCalories = 
        menu.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
    ```
    * 한 개의 인수를 갖는 reducing 팩토리 메서드는 세 개의 인수를 갖는 reducing 메서드에서 스트림의 첫 번째 요소를 시작 요소  
      즉 첫 번째 인수로 받으며, 자신을 그대로 반환하는 항등 함수(identity function)를 두 번째 인수로 받는 상황에 해당
  
* collect와 reduce
  ```
  Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
  List<Integer> numbers = 
      stream.reduce(
          new ArrayList<Integer>(),
          (List<Integer> l, Integer e) -> {
              l.add(e);
              return l;
          }, 
          (List<Integer> l1, List<Integer> l2) -> {
              l1.addAll(l2); return l1;
          }
      );
  ```
  * 위 코드는 의미론적인 문제, 실용성 문제가 있음
    * 의미론적 문제
      * collect 메서드는 도출하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된 메서드
      * reduce는 두 값을 하나로 도출하는 불변형 연산
      * reduce 메서드는 누적자로 사용된 리스트를 변환시키므로 위 코드는 reduce를 잘못 활용한 예
    * 실용성문제
      * 여러 스레드가 동시에 같은 데이터 구조체를 고치면 리스트 자체가 망가져버리므로 리듀싱 연산을 병렬로 수행할 수도 없다는 점
      * 이 문제를 해결하려면 매번 새로운 리스트를 할당해야 하고 따라서 객체를 할당하느라 성능이 저하될 것
      * 가변 컨테이너 관련 작업이면서 병렬성을 확보하려면 collect 메서드로 리듀싱 연산을 구현하는 것이 바람직

* 컬렉션 프레임워크 유연성 : 같은 연산도 다양한 방식으로 수행 가능
  * reducing 컬렉터를 사용한 이전 예제에 람다 표현식 대신 Integer 클래스의 sum 메서드 참조 이용
    ``` int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, Ieteger::sum)); ```
  * counting 컬렉터도 세 개의 인수를 갖는 reducing 팩토리 메서드를 이용해서 구현할 수 있음
    * 다음 코드처럼 스트림의 Long 객체 형식의 요소를 1로 변환한 다음에 모두 더할 수 있음
      ```
      public static <T> Collector<T, ?, Long> counting() {
          return reducing(0L, e -> 1L, Long::sum);
      }
      ```
      * 제네릭 와일드카드 '?' 사용법
        * 위 예제에서 counting 팩토리 메서드가 반환하는 컬렉터 시그니처의 두 번째 제네릭 형식으로 와일드카드 '?'가 사용됨
        * 이 예제에서 '?'는 컬렉터의 누적자 형식이 알려지지 않았음을, 즉 누적자의 형식이 자유로움을 의미함
        * Collectors 클래스에서 원래 정의된 메서드 시그니처를 그대로 사용했을 뿐
  * 5장에서 컬렉터를 이용하지 않는 방법
    ```
    int totalCalories = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
    ```
    * reduce(Integer::sum)도 빈 스트림과 관련한 널 문제를 피할 수 있도록 int가 아닌 Optional\<Integer>를 반환
    * 그리고 get으로 Optional 객체 내부의 값을 추출함
      * 여기서 요리 스트림은 비어이지 않다는 사실을 알고 있으므로 get을 자유롭게 사용할 수 있음
      * 하지만 Optional의 값을 얻어올 때 orElse, orElseGet 등을 이용해 가져오는 것이 좋음
    * 스트림을 IntStream으로 매핑한 다음, sum 메서드를 호출하는 방법도 가능
      ``` 
      int totalCalories = menu.stream().mapToInt(Dish::getCalories).sum();
      ```

* 자신의 상황에 맞는 최적의 해법 선택
  * 함수형 프로그래밍에서는 하나의 연산을 다양한 방법으로 해결할 수 있음
  * 스트림 인터페이스에서 직접 제공하는 메서드를 비용하는 것에 비해 컬렉터를 이용하는 코드가 더 복잡하다는 사실을 보여줌
    * 코드가 좀 더 복잡한 대신 재사용성과 커스터마이즈 가능성을 제공하는 높은 수준의 추상화와 일반화를 얻을 수 있음
  * 문제를 해결할 수 있는 다양한 해결 방법을 확인 후 가장 일반적으로 문제에 특화된 해결책을 고르는 것이 바람직 (가독성과 성능 둘 다 잡기)
  * 위 예제에서 메뉴의 전체 칼로리를 계산하는 예제는 IntStream을 사용한 마지막 방법이 가독성이 가장 좋고 간결함
    * IntStream 덕분에 자동 언박싱(auto unboxing) 연산을 수행하거나 Integer를 int로 변환하는 과정을 피할 수 있으므로 성능까지 좋음
    
### 그룹화
* Collectors.groupingBy 팩토리 메서드를 이용하여 쉽게 그룹화할 수 있음
  ``` Map<Dish.Type, List<Dish> dishesByType = menu.stream().collect(groupingBy(Dish::getType)); ```
  * 결과
    ``` {FISH=[prawns, salmon], OTHER=[french fries, rice, season fruit, pizza], MEAT=[pork, beef, chicken]} ```
  * 스트림의 각 요리에서 Dish.Type과 일치하는 모든 요리를 추출하는 함수를 groupingBy 메서드로 전달
    * 이 함수를 기준으로 그룹화되므로 이를 분류함수(classification function)라고 부름
  * 단순한 속성 접근자 대신 더 복잡한 분류 기준이 필요한 상황에서는 메서드 참조를 분류 함수로 사용할 수 없음
    * 예를 들어 400칼로리 이하를 'diet', 400~700칼로리를 'normal', 700칼로리 초과를 'fat' 요리로 분류한다고 가정
    * 이때 Dish 클래스에는 연산에 필요한 메서드가 없으므로 참조를 분류 함수로 사용할 수 없음
    * 따라서 메서드 참조 대신 람다 표현식으로 필요한 로직 구현
      ```
      public enum CaloricLevel {DIET, NORMAL, FAT}
      Map<CaloricLevel, List<Dish>> dishesByCalroicLevel = 
            menu.stream()
                .collect(groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                    })
                );
      ```

* 그룹화된 요소 조작
  * 요소를 그룹화 한 다음에 각 결과 그룹의 요소를 조작하는 연산 필요
    * 예를 들어 500칼로리가 넘는 요리만 필터한다고 가정
      ```
      Map<Dish.Type, List<Dish>> caloricDishesByType =
            menu.stream()
                .filter(dish -> dish.getCalories() > 500)
                .collect(groupingBy(Dish::getType));
      ```
      * 결과
        ``` {OTHER=[french fries, pizza], MEAT=[pork, beef]} ```
    * 위 예제는 FISH 타입 데이터가 필터링에 걸리지 않아 결과 맵에서 해당 키(FISH) 자체가 사라짐
    * Collectors 클래스는 일반적인 분류 함수에 Collector 형식의 두 번째 인수를 갖도록 groupingBy 팩토리 메서드를 오버로드해 이 문제를 해결함
      ```
      Map<Dish.Type, List<Dish>> caloricDishesByType =
            menu.stream()
                .collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));
      ```
      * 결과
        ``` {OTHER=[french fries, pizza], MEAT=[pork, beef], FISH=[]} ```
      * filtering 메서드는 Collectors 클래스의 또 다른 정적 팩토리 메서드로 프레디케이트를 인수로 받음
        * 이 프레디케이트로 각 그룹의 요소와 필터링 된 요소를 재그룹화
  * 그룹화된 항목을 조작하는 다른 유용한 기능 중 또 다른 하나로 매핑 함수를 이용해 요소를 변환하는 작업이 있음
    * 매핑 함수와 각 항목에 적용한 함수를 모으는 데 사용하는 또 다른 컬렉터를 인수로 받는 mapping 메서드 제공
      * 예를 들어 이 함수를 이용해 그룹의 각 요리를 관련 이름 목록으로 변환할 수 있음
        ```
        Map<Dish.Type, List<String>> dishNamesByTpe = 
            menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
        ```
      * flatMapping 컬렉터
        ```
        public static final Map<String, List<String>> dishTags = new HashMap<>();
        
        dishTags.put("pork", asList("greasy", "salty"));
        dishTags.put("beef", asList("salty", "roasted"));
        dishTags.put("chicken", asList("fried", "crisp"));
        dishTags.put("french fries", asList("greasy", "fried"));
        dishTags.put("rice", asList("light", "natural"));
        dishTags.put("season fruit", asList("fresh", "natural"));
        dishTags.put("pizza", asList("tasty", "salty"));
        dishTags.put("prawns", asList("tasty", "roasted"));
        dishTags.put("salmon", asList("delicious", "fresh"));
        
        Map<Dish.Type, Set<String>> dishNamesByTpe = 
            menu.stream()
                .collect(groupingBy(
                                    Dish::getType, 
                                    flatMapping(dish -> dishTags.get(dish.getName()).stream(), 
                                    toSet())));
        ```
        * 각 요리에서 태그 리스트를 얻어야 함
          * 두 수준의 리스트를 한 수준으로 평면화하려면 flatMap 수행해야 함
          * 이전처럼 각 그룹에 수행한 flatMapping 연산 결과를 수집해서 리스트가 아니라 집합으로 그룹화해 중복 태그를 제거함
        * 결과 
          ``` 
          {MEAT=[salty, greasy, roasted, fried, crisp],  
           FISH=[roasted, tasty, fresh, delicious],  
           OTHER=[salty, greasy, natural, light, tasty, fresh, fried]}
          ```

* 다수준 그룹화
  * 두 인수를 받는 팩토리 메서드 Collectors.groupingBy를 이용해 항목을 다수준으로 그룹화할 수 있음
    * Collectors.groupingBy는 일반적인 분류 함수와 컬렉터를 인수로 받음
    * 바깥쪽 groupingBy 메서드에 스트림의 항목을 분류할 두 번째 기준을 정의하는 내부 groupingBy를 전달, 두 수준으로 스트림의 항목을 그룹화할 수 있음
    ```
    Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
        menu.stream()
            .collect(groupingBy(Dish::getType, // 첫 번째 수준의 분류 함수
                        groupingBy(dish -> {   // 두 번째 수준의 분류 함수
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT; 
                        })
            )
        );
    ```
    * 결과
      ```
      {MEAT={DIET=[chicken], NORMAL=[beef], FAT=[pork]}, FISH={DIET=[prawns], NORMAL=[salmon]}, 
      OTHER={DIET=[rice, seasonal fruit], NORMAL=[french fries, pizza]}}
      ```
    * 다수준 그룹화 연산은 다양한 수준으로 확장할 수 있음
    * n수준 그룹화의 결과는 n수준 트리 구조로 표현되는 n 수준 맵이 됨
    * 보통 groupingBy 연산을 버킷 개념으로 생각하면 쉬움
      * 첫 번째 groupingBy는 각 키의 버킷을 만듦
      * 그리고 준비된 각각의 버킷을 서브스트림 컬렉터로 채워가기를 반복하면서 n수준 그룹화를 달성

* 서브그룹으로 데이터 수집
  * 위 소스에서 첫 번째 groupingBy로 넘겨주는 컬렉터의 형식은 제한이 없음
    * 예를 들어 다음 코드처럼 groupingBy 컬렉터에 두 번째 인수로 counting 컬렉터를 전달해서 메뉴에서 요리의 수를 종류별로 계산할 수 있음
      ```
      Map<Dish.Type, Long> typescount = menu.stream().collect(groupingBy(Dish::getType, counting()));
      ```
      * 결과
        ``` {MEAT=3, FISH=2, OTHER=4} ```
    * 분류 함수 한 개의 인수를 갖는 groupingBy(f)는 사실 groupingBy(f, toList())의 축약형
      * 요리의 종류를 분류하는 컬렉터로 메뉴에서 가장 높은 칼로리를 가진 요리를 찾는 프로그램도 다시 구현 가능
        ```
        Map<Dish.Type, Optional<Dish>> mostCaloricByType = 
            menu.stream().collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));
        ```
        * 결과
          ``` {FISH=Optional[salmon]}, OTHER=Optional[pizza], MEAT=Optional[pork]} ```
      * 팩토리 메서드 maxBy가 생성하는 컬렉터의 결과 형식에 따라 맵의 값이 Optional 형식이 되었음  
        실제 메뉴의 요리중 Optional.empty()를 값으로 갖는 요리는 존재하지 않음  
        처음부터 존재하지 않는 요리의 키는 맵에 추가되지 않기 때문  
        groupingBy 컬렉터는 스트림의 첫 번째 요소를 찾은 후에야 그룹화 맵에 새로운 키를 (게으르게) 추가함  
        리듀싱 컬렉터가 반환하는 형식을 사용하는 상황이므로 굳이 Optional 래퍼를 사용할 필요가 없음
  * 컬렉터 결과를 다른 형식에 적용
    * 위 그룹화 연산에서 맵에서 Optional 삭제
    * 즉 팩토리 메서드 Collectors.collectingAndThen으로 컬렉터가 반환한 결과를 다른 형식으로 활용할 수 있음
      ```
      Map<Dish.Type, Dish> mostCaloricByType = 
          menu.stream()
            .collect(groupingBy(Dish::getType, // 분류 함수
                            collectingAndThen(maxBy(comparingInt(Dish::getCalories)), // 감싸인 컬렉터
                             Optional::get))); // 변환 함수
      ```
    * collectingAndThen 팩토리 메서드는 적용할 컬렉터와 변환 함수를 인수로 받아 다른 컬렉터를 반환
    * 반환되는 컬렉터는 기존 컬렉터의 래퍼 역할을 하며 collect의 마지막 과정에서 변환 함수로 자신이 반환하는 값을 매핑함
      * 리듀싱 컬렉터는 절대 Optional.empty()를 반환하지 않으므로 안전한 코드
      * 위 예제에서 maxBy로 만들어진 컬렉터가 감싸지는 컬렉터며 변환 함수 Optional::get으로 반환된 Optional에 포함된 값을 추출
        * 결과
          ``` {FISH=salmon, OTHER=pizza, MEAT=pork} ```

* groupingBy와 함께 사용하는 다른 컬렉터 예제
  * 일반적으로 스트림에서 같은 그룹으로 분류된 모든 요소에 리듀싱 작업을 수행할 때는 팩토리 메서드 groupingBy에 두 번째 인수로 전달한 컬렉터를 사용함
    * 예를 들어 메뉴에 있는 모든 요리의 칼로리 합계를 구하려고 만든 컬렉터를 재사용할 수 있음
      ```
      Map<Dish.Type, Integer> totalCaloriesByType =
        menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
      ```
  * mapping 메서드는 스트림의 인수를 변환하는 함수와 변환 함수의 결과 객체를 누적하는 컬렉터를 인수로 받음
    * 입력 요소를 누적하기 전에 매핑 함수를 적용해서 다양한 형식의 객체를 주어진 형식의 컬렉터에 맞게 변환하는 역할을 함
    * 예를 들어 각 요리 형식에 존재하는 모든 CaloricLevel 값을 알고 싶을 때
      ```
      Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = 
          menu.stream().collect(
                groupingBy(Dish::getType, mapping(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT; },
                toSet())));
      ```
      * 결과
        ``` {OTHER=[DIET, NORMAL], MEAT=[DIET, NORMAL, FAT], FISH=[DIET, NORMAL]} ```
      * toCollection 이용
        ```
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = 
            menu.stream().collect(
                  groupingBy(Dish::getType, mapping(dish -> {
                      if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                      else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                      else return CaloricLevel.FAT; },
                  toCollection(HashSet::new))));
        ```

### 분할
* 분할은 분할 함수(partitioning function)라 불리는 프레디케이트를 분류 함수로 사용하는 특수한 그룹화 기능
  * 분할 함수는 불리언을 반환, 맵의 키 형식은 Boolean
  * 결과적으로 그룹화 맵은 최대 두 개의 그룹으로 분류됨
    * 예를 들어 채식 요리와 아닌 요리를 분류
      ```
      Map<Boolean, List<Dish>> partitionedMenu = 
            menu.stream().collect(partitioningBy(Dish::isVegetarian));
      ```
      * 결과
        ``` {false=[pork, beef, chicken, prawns, salmon], true=[french fries, rice, season fruit, pizza]} ```
      ```
      List<Dish> vegetarianDishes = partitionedMenu.get(true);
      ```
      * 프레디케이트로 필터링한 다음 별도의 리스트에 결과를 수집해도 같은 결과를 얻을 수 있음
        ```
        List<Dish> vegetarianDishes = 
                menu.stream().filter(Dish::isVegetarian).collect(toList());
        ```

* 분할의 장점
  * 참, 거짓 두 가지 요소의 스트림 리스트를 모두 유지한다는 것이 분할의 장점
  * 컬렉터를 두 번째 인수로 전달할 수 있는 오버로드된 버전의 partitioningBy 메서드
    ```
    Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType = 
        menu.stream().collect(partitioningBy(Dish::isVegetarian, // 분할 함수
                                groupingBy(Dish::getType))); // 두 번째 컬렉터
    ```
    * 결과
      ```
      {false={FISH=[prawns, salmon], MEAT=[pork, beef, chicken]}, 
      true={OTHER=[french fries, rice, season fruit, pizza]}}
      ```
  * 채식 요리와 아닌 요리 각각 그룹에서 가장 칼로리가 높은 요리 찾기
    ```
    Map<Boolean, Dish> mostCaloricPartitionedByVegetarian = 
        menu.stream().collect(partitioningBy(Dish::isVegetarian,
                                collectingAndThen(maxBy(comparingInt(Dish::getCalories)),
                                Optional::get))); 
    ```
    * 결과
      ``` {false=pork, true=pizza} ```

* 숫자를 소수와 비소수로 분할
  * 정수 n을 인수로 받아 2에서 n까지의 자연수를 소수(prime)와 비소수(nonprime)로 나누는 프로그램 구현
    ```
    public boolean isPrime(int candidate) {
        return IntStream.range(2, candidate) // 2부터 candidate 미만 사이의 자연수를 생성
                .noneMatch(i -> candidate % i == 0); // 스트림의 모든 정수로 candidate를 나눌 수 없으면 참을 반환
    }
    ```
  * 소수의 대상을 주어진 수의 제곱근 이하의 수로 제한할 수 있음
    ```
    public boolean isPrime(int candidate) {
        int candiateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candiateRoot)
                .noneMatch(i -> candidate % i == 0);
    ```
  * 이제 n개의 숫자를 포함하는 스트림을 만든 다음에 우리가 구현한 isPrime 메서드를 프레디케이트로 이용하고  
    partitioningBy 컬렉터로 리듀스해서 소수와 비소수로 분류할 수 있음
    ```
    public Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(2, n)
                .boxed()
                .collect(partitioningBy(candidate -> isPrime(candidate)));
    }
    ```

### Collectors 클래스의 정적 팩토리 메서드
* 모든 컬렉터는 Collector 인터페이스를 구현함
* toList
  * 반환 형식 : List\<T>
  * 사용 예제 : 스트림의 모든 항목을 리스트로 수집
  * 활용 예 : List<Dish> dishes = menuStream.collect(toList());
* toSet
  * 반환 형식 : Set\<T>
  * 사용 예제 : 스트림의 모든 항목을 중복이 없는 집합으로 수집
  * 활용 예 : Set<Dish> dishes = menuStream.collect(toSet());
* toCollection
  * 반환 형식 : Collection\<T>
  * 사용 예제 : 스트림의 모든 항목을 발행자가 제공하는 컬렉션으로 수집
  * 활용 예 : Collection<Dish> dishes = menuStream.collect(toCollection(), ArrayList::new);
* counting
  * 반환 형식 : Long
  * 사용 예제 : 스트림의 항목 수 계산
  * 활용 예 : long howManyDishes = menuStream.collect(counting());
* summingInt
  * 반환 형식 : Integer
  * 사용 예제 : 스트림의 항목에서 정수 프로퍼티값을 더함
  * 활용 예 : int totalCalories = menuStream.collect(summingInt(Dish::getCalories));
* summarizingInt
  * 반환 형식 : IntSummaryStatistics
  * 사용 예제 : 스트림 내 항목의 최댓값, 최솟값, 합계, 평균 등의 정수 정보 통계 수집
  * 활용 예 : IntSummaryStatistics menuStatistics = menuStream.collect(summarizingInt(Dish::getCalories));
* joining
  * 반환 형식 : String
  * 사용 예제 : 스트림의 각 항목에 toString 메서드를 호출한 결과 문자열 연결
  * 활용 예 : String shortMenu = menuStream.map(Dish::getName).collect(joining(", "));
* maxBy
  * 반환 형식 : Optional\<T>
  * 사용 예제 : 주어진 비교자를 이용해서 스트림의 최댓값 요소를 Optional로 감싼 값을 반환. 스트림에 요소가 없을 때는 Optional.empty() 반환
  * 활용 예 : Optional<Dish> fattest = menuStream.collect(maxBy(comparingInt(Dish::getCalories)));
* minBy
  * 반환 형식 : Optional\<T>
  * 사용 예제 : 주어진 비교자를 이용해서 스트림의 최솟값 요소를 Optional로 감싼 값을 반환. 스트림에 요소가 없을 때는 Optional.empty() 반환
  * 활용 예 : Optional<Dish> lightest = menuStream.collect(minBy(comparingInt(Dish::getCalories)));
* reducing
  * 반환 형식 : 'The type produced by the reduction operation'
  * 사용 예제 : 누적자를 초깃값으로 설정한 다음에 BinaryOperator로 스트림의 각 요소를 반복적으로 누적자와 합쳐 스트림을 하나의 값으로 리듀싱
  * 활용 예 : int totalCalories = menuStream.collect(reducing(0, Dish::getCalories, Integer::sum));
* collectionAndThen
  * 반환 형식 : 'The type returned by the transforming function'
  * 사용 예제 : 다른 컬렉터를 감싸고 그 결과에 반환 함수 적용
  * 활용 예 : int howManyDishes = menuStream.collect(collectingAndThen(toList(), List::size));
* groupingBy
  * 반환 형식 : Map<K, List\<T>>
  * 사용 예제 : 하나의 프로퍼티값을 기준으로 스트림의 항목을 그룹화하며 기준 프로퍼티값을 결과 맵의 키로 사용
  * 활용 예 : Map<Dish.Type, List<Dish>> dishesByType = menuStream.collect(groupingBy(Dish::getType));
* partitionBy
  * 반환 형식 : Map<Boolean, List\<T>>
  * 사용 예제 : 하나의 프로퍼티값을 기준으로 스트림의 항목을 그룹화하며 기준 프로퍼티값을 결과 맵의 키로 사용
  * 활용 예 : Map<Boolean, List<Dish>> vegetarianDishes = menuStream.collect(partitioningBy(Dish::isVegetarian));
    
### 인터페이스
* Collector 인터페이스는 리듀싱 연산(컬렉터)을 어떻게 구현할지 제공하는 메서드 집합으로 구성
* Collector 인터페이스를 구현하는 리듀싱 연산을 만들 수 있음
* Collector 인터페이스
  ```
  public interface Collector<T, A, R> {
      Supplier<A> supplier();
      BiConsumer<A, T> accumulator();
      BinaryOperator<A> combiner();
      Function<A, R> finisher();
      Set<Characteristics> characteristics();
  }
  ```
  * 설명
    * T는 수집될 스트림 항목의 제네릭 형식
    * A는 누적자, 즉 수집 과정에서 중간 결과를 누적하는 객체 형식
    * R은 수집 연산 결과 객체의 형식 (항상 그런 것은 아니지만 대개 컬렉션 형식)
  * 예를 들어 Stream\<T>의 모든 요소를 List\<T>로 수집하는 toListCollector\<T>라는 클래스를 구현할 수 있음
    ``` public class ToListCollector<T> implements Collector<T, List<T>, List<T>> ```

* Collector 인터페이스 메서드
  * Characteristics를 제외한 4개의 메서드는 collect 메서드에서 실행하는 함수를 반환
  * Characteristics 메서드는 collect 메서드가 어떤 최적화(병렬화 등)를 이용해서 리듀싱 연산을 수행할 것인지 결정하도록 돕는 힌트 특성 집합을 제공
  * supplier 메서드 : 새로운 결과 컨테이너 만들기
    * supplier 메서드는 빈 결과로 이루어진 Supplier를 반환해야 함
    * supplier는 수집 과정에서 빈 누적자 인스턴스를 만드는 파라미터가 없는 함수
    * ToListCollector처럼 누적자를 반환하는 컬렉터에서는 빈 누적자가 비어있는 스트림의 수집 과정의 결과가 될 수 있음
    * ToListCollector에서 supplier는 빈 리스트를 반환
      ```
      public Supplier<List<T>> supplier() {
          return () -> new ArrayList<T>();
      }
      // 생성자 참조 전달
      public Supplier<List<T>> supplier() {
          return ArrayList::new;
      }      
      ```
  * accumulator 메서드 : 결과 컨테이너에 요소 추가하기
    * accumulator 메서드는 리듀싱 연산을 수행하는 함수를 반환함
    * 스트림에서 n번째 요소를 탐색할 때 두 인수, 즉 누적자(스트림의 첫 n-1개 항목을 수집한 상태)와 n번째 요소를 함수에 적용
    * 함수의 반환값은 void, 즉 요소를 탐색하면서 적용하는 함수에 의해 누적자 내부상태가 바뀌므로 누적자가 어떤 값인지 단정할 수 없ㅇ므
    * ToListCollector에서 accumulator가 반환하는 함수는 이미 탐색한 항목을 포함하는 리스트에 현재 항목을 추가하는 연산을 수행함
      ```
      public BiConsumer<List<T>, T> accumulator() {
          return (list, item) -> list.add(item);
      }
      // 메서드 참조 이용
      public BiConsumer<List<T>, T> accumulator() {
          return List::add;
      } 
      ```
  * finisher 메서드 : 최종 변환값을 결과 컨테이너로 적용하기
    * finisher 메서드는 스트림 탐색을 끝내고 누적자 객체를 최종 결과로 변환하면서 누적 과정을 끝낼 때 호출할 함수를 반환해야 함
    * 때로는 ToListCollector에서 볼 수 있는 것처럼 누적자 객체가 이미 최종 결과인 상황도 있음
    * 이 때는 변환 과정이 필요하지 않으므로 finisher 메서드는 항등 함수를 반환함
      ```
      public Function<List<T>, List<T>> finisher() {
          return Function.identity();
      } 
      ```
  * 위 세 개 메서드로도 순차적 스트림 리듀싱 기능을 수행할 수 있음
    * collect가 동작하기 전에 다른 중간 연산과 파이프라인을 구성할 수 있게 해주는 게으른 특성, 병렬 실행 등으로 인해서 스트림 리듀싱 기능 구현은 복잡함
  * combiner 메서드 : 두 결과 컨테이너 병합
    * combiner는 스트림의 서로 다른 서브파트를 병렬로 처리할 때 누적자가 이 결과를 어떻게 처리할지 정의함
    * toList의 combiner는 비교적 쉽게 구현가능
    * 스트림의 두 번째 서브파트에서 수집한 항목 리스트를 첫 번째 서브파트 결과 리스트 뒤에 추가하면 됨
      ```
      public BinaryOperator<List<T>> combiner() {
          return (list1, list2) -> {
              list1.addAll(list2);
              return list1;
          };
      } 
      ```
    * combiner 메서드를 이용하면 스트림의 리듀싱을 병렬로 수행할 수 있음
      * 스트림의 리듀싱을 병렬로 수행할 때 자바 7의 포크/조인 프레임워크, Spliterator를 사용함
      * 스트림을 분할해야 하는지 정의하는 조건이 거짓으로 바뀌기 전까지 원래 스트림을 재귀적으로 분할함
        * 보통 분산된 작업의 크기가 너무 작아지면 병렬 수행의 속도는 순차 수행의 속도보다 느려짐
        * 즉 병렬 수행의 효과가 상쇄됨
        * 일반적으로 프로세싱 코어의 개수를 초과하는 병렬 작업은 효율적이지 않음
      * 모든 서브스트림의 각 요소에 리듀싱 연산을 순차적으로 적용해서 서브스트림을 병렬로 처리할 수 있음
      * 마지막에는 컬렉터의 combiner 메서드가 반환하는 함수로 모든 부분 결과를 쌍으로 합침
        * 즉 분할된 모든 서브스트림의 결과를 합치면서 연산이 종료됨
  * Characteristics 메서드
    * Characteristics 메서드는 Characteristics 형식의 불변 집합을 반환함
    * Characteristics는 스트림을 병렬로 리듀스할 것인지 그리고 병렬로 리듀스한다면 어떤 최적화를 선택해야 할지 힌트를 제공함
    * Characteristics는 다음 세 항목을 포함하는 열거형
      * UNORDERED
        * 리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에 영향을 받지 않음
      * CONCURRENT
        * 다중 스레드에서 accumulator 함수를 동시에 호출할 수 있으며 이 컬렉터는 스트림의 병릴 리듀싱을 수행할 수 있음
        * 컬렉터의 플래그에 UNORDERED를 함께 설정하지 않았다면 데이터 소스가 정렬되어 있지 않은 상황에서만 병렬 리듀싱을 수행할 수 있음
          * 집합처럼 요소의 순서가 무의미한 상황
      * IDENTITY_FINISH
        * finisher 메서드가 반환하는 함수는 단순히 identity를 적용할 뿐이므로 이를 생략할 수 있음
        * 따라서 리듀싱 과정의 최종 결과로 누적자 객체를 바로 사용할 수 있음
        * 또한 누적자 A를 결과 R로 안전하게 형변환할 수 있음
  * 지금까지 개발한 ToListCollector에서 스트림의 요소를 누적하는 데 사용한 리스트가 최종 결과 형식이므로 추가 변환이 필요 없음
    * 따라서 ToListCollector는 IDENTITY_FINISH
    * 리스트의 순서는 상관이 없으므로 UNORDERED
    * ToListCollector는 CONCURRENT
    
* 응용하기
```
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

	@Override
	public Supplier<List<T>> supplier() {
		// 수집 연산의 시발점
		return ArrayList::new;
	}

	@Override
	public BiConsumer<List<T>, T> accumulator() {
		// 탐색한 항목을 누적하고 바로 누적자를 고침
		return List::add;
	}

	@Override
	public Function<List<T>, List<T>> finisher() {
		// 항등 함수
		return Function.identity();
	}

	@Override
	public BinaryOperator<List<T>> combiner() {
		return (list1, list2) -> { // 두 번째 콘텐츠와 합쳐서 첫 번째 누적자를 고침
			list1.addAll(list2);   // 변경된 첫 번째 누적자를 반환
			return list1;
		};
	}

	@Override
	public Set<Characteristics> characteristics() {
		// 컬렉터의 플래그를 IDENTITY_FINISH, CONCURRENT로 설정
		return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
	}
}
```
* 위 구현이 Collectors.toList 메서드가 반환하는 결과와 완전히 같은 것은 아니지만 사소한 최적화를 제외하면 대체로 비슷함
  * 특히 자바 API에서 제공하는 컬렉터는 싱글턴 Collections.emptyList()로 빈 리스트를 반환함
  * 예제에 적용
    ``` List<Dish> dishes = menuStream.collect(new ToListCollector<Dish>()); ```
  * 기존 코드
    ``` List<Dish> dishes = menuStream.collect(toList()); ```
  * 기존 코드의 toList는 팩토리지만 위 ToListCollector는 new로 인스턴스화한다는 점이 다름
  
* 컬렉터 구현을 만들지 않고도 커스텀 수집 수행하기
  * IDENTITY_FINISH 수집 연산에서는 Collector 인터페이스를 완전히 새로 구현하지 않고도 같은 결과를 얻을 수 있음
  * Stream은 세 함수(발행, 누적, 합침)를 인수로 받는 collect 메서드를 오버로드
    * 각각의 메서드는 Collector 인터페이스의 메서드가 반환하는 함수와 같은 기능 수행
      ```
      List<Dish> dishes = menuStream.collect(
          ArrayList::new, // 발행
          List::add,      // 누적
          List::addAll    // 합침
      );
      ```
      * 위 두 번째 코드가 이전 코드에 비해 간결하고 축약되어 있지만 가독성은 떨어짐
      * 적절한 클래스로 커스텀 컬렉터를 구현하는 편이 중복을 피하고 재사용성을 높이는데 도움이 됨
      * 또한 두 번째 collect 메서드로는 Characteristics를 전달할 수 없음
        * 즉 두 번째 collect 메서드는 IDENTITY_FINISH와 CONCURRENT지만 UNORDERED는 아닌 컬렉터로만 동작함

### 커스텀 컬렉터를 구현해서 성능 개선하기
* 커스텀 컬렉터로 n까지의 자연수를 소수와 비소수로 분할
  ```
  public Map<Boolean, List<Integer>> partitionPrimes(int n) {
      return IntStream.rangeClosed(2, n).boxed().collect(partitioningBy(candidate -> isPrime(candidate)));
  }
  ```
* 제곱근 이하로 대상(candidate)의 숫자 범위를 제한해서 isPrime 메서드를 개선
  ```
  public boolean isPrime(int candidate) {
      int candidateRoot = (int) Math.sqrt((double) candidate);
      return IntStream.rangeClosed(2, candidateRoot).noneMatch(i -> candidate % i == 0);
  }
  ```

* 소수로만 나누기
  * 우선 소수로 나누어 떨어지는지 확인해서 대상의 범위를 좁힐 수 있음
  * 제수(divisor)가 소수가 아니면 소용 없으므로 제수를 현재 숫자 이하에서 발견한 소수로 제한할 수 있음
  * 주어진 숫자가 소수인지 판단해야 하는데, 그러려면 발견한 소수 리스트에 접근해야 함
  * 하지만 우리가 살펴본 컬렉터로는 컬렉터 수집과정에서 부분 결과에 접근할 수 없음
  * 커스텀 컬렉터 클래스로 문제 해결
  * 중간 결과 리스트가 있다면 isPrime 메서드로 중간 결과 리스트를 전달하도록 다음과 같이 코드 구현
    ```
    public static boolean isPrime(List<Integer> primes, int candidate) {
        return primes.stream().noneMatch(i -> candidate % i == 0);
    }
    ```
  * 이번에도 대상 숫자의 제곱근보다 작은 소수만 사용하도록 코드를 최적화해야 함
  * 다음 소수가 대상의 루트보다 크면 소수로 나누는 검사를 멈춰야 하나 스트림 API에는 이런 기능을 제공하는 메서드가 없음
  * filter(p -> p <= candidate)를 이용해 대상의 루트보다 작은 소수를 필터링 할 수 있음
  * 하지만 결국 filter는 전체 스트림을 처리한 다음에 결과를 반환하게 됨
  * 소수 리스트와 대상 숫자의 범위가 아주 크다면 성능 문제 발생 가능
  * 대상의 제곱보다 큰 소수를 찾으면 검사를 중단함으로써 성능 문제를 없앨 수 있음
  * 정렬된 리스트와 프레디케이트를 인수로 받아 리스트의 첫 요소에서 시작
    * 프레디케이트를 만족하는 가장 긴 요소로 이루어진 리스트를 반환하는 takeWhile 메서드 구현
      ```
      public static boolean isPrime(List<Integer> primes, int candidate) {
          int candidateRoot = (int) Math.sqrt((double) candidate);
          return primes.stream().takeWhile(i -> i <= candidateRoot).noneMatch(i -> candidate % i == 0);
      }
      ```

### 커스텀 컬렉터 구현
* 우선 Collector 인터페이스를 구현하는 새로운 클래스 선언, Collector 인터페이스에서 요구하는 메서드 다섯 개 구현

* [1] Collector 클래스 시그니처 정의
  * Collector 인터페이스 정의를 참고해서 클래스 시그니처 만들기
    ``` public interface Collector<T, A, R> ```
    * 위 코드에서 T는 스트림의 형식, A는 중간 결과를 누적하는 객체 형식, R은 collect 연산의 최종 결과 형식
  * 정수로 이루어진 스트림에서 누적자와 최종 결과의 형식이 Map<Boolean, List\<Integer>>인 컬렉터를 구현해야 함
  * 즉 Map<Boolean, List\<Integer>>는 참과 거짓을 키로, 소수와 소수가 아닌 수를 값으로 가짐
    ```
    public class PrimeNumbersCollector 
        implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>>
    ```
* [2] 리듀싱 연산 구현
  * supplier 메서드는 누적자를 만드는 함수를 반환해야 함
    ```
	public Supplier<Map<Boolean, List<Integer>>> supplier() {
		return () -> new HashMap<Boolean, List<Integer>>() {{
			put(true, new ArrayList<>());
			put(false, new ArrayList<>());
		}};
	}
    ```
    * 위 코드에서는 누적자로 사용할 맵을 만들면서 true, false 키와 빈 리스트로 초기화 함
    * 수집 과정에서 빈 리스트에 각각 소수, 비소수를 추가할 것
    * 스트림의 요소를 어떻게 수집할 지 결정하는 것은 accumulator 메서드로 우리 컬렉터에서 가장 중요한 메서드
      * accumulator는 최적화의 핵심이기도 함
    ```
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
            acc.get(isPrime(acc.get(true), candidate)) // isPrime 결과에 따라 소수 리스트, 비소수 리스트를 만듦
                .add(candidate); // candidate를 알맞은 리스트에 추가함
        };
    }
    ```
* [3] 병렬 실행할 수 있는 컬렉터 만들기 (가능할 경우에)
  * 병렬 수집 과정에서 두 부분 누적자를 합칠 수 있는 메서드 생성
  * 예제에서는 단순하게 두 번째 맵의 소수 리스트와 비소수 리스트의 모든 수를 첫 번째 맵에 추가하는 연산이면 충분함
    ```
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
        };
    }
    ```
    * 알고리즘 자체가 순차적이어서 컬렉터를 실제 병렬로 사용할 수 없음
    * 따라서 combiner 메서드는 호출될 일이 없으므로 빈 구현으로 남겨둘 수 있음
      * 또는 UnsupportedOperationException을 던지도록 구현하는 것도 좋은 방법
      * 학습 목적으로 구현한 것
* [4] finisher 메서드와 컬렉터의 characteristics 메서드
  * accumulator의 형식은 컬렉터 결과 형식과 같으므로 변환 과정이 필요 없음
  * 따라서 identity를 반환하도록 finisher 메서드를 구현
    ```
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }
    ```
  * 커스텀 컬렉터는 CONCURRENT도 아니고 UNORDERED도 아니지만 IDENTITY_FINISH이므로 다음처럼 characteristics 메서드를 구현할 수 있음
    ```
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
	}
    ```

* PrimeNumbersCollector 최종 구현 코드
```
public class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

	@Override
	public Supplier<Map<Boolean, List<Integer>>> supplier() {
		return () -> new HashMap<Boolean, List<Integer>>() {{
			put(true, new ArrayList<>());
			put(false, new ArrayList<>());
		}};
	}

	@Override
	public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
		return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
			acc.get(isPrime(acc.get(true), candidate)).add(candidate);
		};
	}

	@Override
	public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
		return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
			map1.get(true).addAll(map2.get(true));
			map1.get(false).addAll(map2.get(false));
			return map1;
		};
	}

	@Override
	public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
	}
}
```

* 커스텀 컬렉터로 교체
```
public Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
    return IntStream.rangeClosed(2, n).boxed().collect(new PrimeNumbersCollector());
}
```

* 컬렉터 성능 비교
  * 팩토리 메서드 partitioningBy로 만든 코드와 커스텀 컬렉터로 만든 코드의 기능은 같음
  * 성능 비교를 위해 간단한 하니스(harness)를 만듦
    ```
    public class CollectorHarness {
    
    	public static void main(String[] args) {
    		long fastest = Long.MAX_VALUE;
    
    		for (int i = 0; i < 10; i++) {
    			long start = System.nanoTime();
    			partitionPrimes(1_000_000);
    			long duration = (System.nanoTime() - start);
    
    			if (duration < fastest) {
    				fastest = duration;
    			}
    		}
    
    		System.out.println("Fastest execution done in " + fastest + " msecs");
    	}
    }
    ```
    * JMH를 사용할 수도 있지만 간단한 예제이기 때문에 사용 안함
    * partitionBy로 백만 개의 자연수를 소수, 비소수로 분류하는 작업을 10회 반복하면서 가장 빨리 실행된 속도를 기록
    * 성능이 약 32% 향상됨

### 정리
* collect는 스트림의 요소를 요약 결과로 누적하는 다양한 방법(컬렉터라 불리는)을 인수로 갖는 최종 연산
* 스트림의 요소를 하나의 값으로 리듀스하고 요약하는 컬렉터뿐 아니라, 최솟값, 최댓값, 평균값을 계산하는 컬렉터 등이 미리 정의되어 있음
* 미리 정의된 컬렉터인 groupingBy로 스트림의 요소를 그룹화하거나, partitioningBy로 스트림의 요소를 분할할 수 있음
* 컬렉터는 다수준의 그룹화, 분할, 리듀싱 연산에 적합하게 설계되어 있음
* Collector 인터페이스에 정의된 메서드를 구현해서 커스텀 컬렉터를 개발할 수 있음

### [quiz]
* 아래 joining 컬렉터를 reducing 컬렉터로 올바르게 바꾼 코드를 모두 선택하기
  ``` String shortMenu = menu.stream().map(Dish::getName).collect(joining()); ```
  * O : 원래의 joining 컬렉터처럼 각 요리를 요리명으로 변환한 다음에 문자열을 누적자로 사용해서 문자열 스트림을 리듀스하면서 요리명을 하나씩 연결
    ```
    String shortMenu = menu.stream()
        .map(Dish::getName)
        .collect(reducing((s1, s2) -> s1 + s2))
        .get();
    ```
  * X : reducing은 binaryOperator\<T>, 즉 BiFunction<T, T, T>를 인수로 받음.  
    즉 reducing은 두 인수를 받아 같은 형식을 반환하는 함수를 인수로 받음  
    하지만 여기서는 두 개의 요리를 인수로 받아 문자열을 반환하기 때문에 컴파일 에러
    ```
    String shortMenu = menu.stream()
        .collect(reducing((d1, d2) -> d1.getName() + d2.getName()))
        .get();
    ```
  * O : 빈 문자열을 포함하는 누적자를 이용해서 리듀싱 과정을 시작, 스트림의 요리를 방문하면서 각 요리를 요리명으로 변환한 다음에 누적자로 추가  
    세 개의 인수를 갖는 reducing은 누적자 초깃값을 설정할 수 있으므로 Optional을 반환할 필요가 없음
    ```
    String shortMenu = menu.stream()
        .collect(reducing("", Dish::getName, (s1, s2) -> s1 + s2))
        .get();
    ```
  * 위 예제는 범용 reducing으로 joining을 구현할 수 있음을 보여주는 예시일 뿐
  * 실무에서는 joining을 사용하는 것이 가독성과 성능에 더 좋음
  
* 다음 코드의 다수준 분할 결과를 예측하기
  * O : 유효한 다수준 분할코드
    ```
    menu.stream().collect(partitioningBy(Dish::isVegetarian, parititionBy(d -> d.getCalories() > 500)));
    ```
    * 결과
      ```
      {false={false=[chicken, prawns, salmon], true=[pork, beef]}, 
      true={false=[rice, season fruit], true=[french fries, pizza]}}
      ```
  * X : parititionBy는 불리언을 반환하는 함수, 프레디케이트를 요구하므로 컴파일 되지 않음
    ```
    menu.stream().collect(partitioningBy(Dish::isVegetarian, parititionBy(Dish::getType)));
    ```
  * O : 분할된 각 항목의 개수를 계산하는 코드
    ```
    menu.stream().collect(partitioningBy(Dish::isVegetarian, counting()));
    ```
    * 결과
      ```
      {false=5, true=4}
      ```

* 자바 8에서 takeWhile 기능 이용하기
  * 정렬된 리스트와 프레디케이트를 인수로 받아 프레디케이트를 만족하는 가장 긴 첫 요소 리스트를 반환하도록 직접 takeWhile 메서드 구현
    ```
    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
        int i = 0;
        for (A item : list) {
            if (!p.test(item)) {
                return list.sublist(0, i);
            }
            i++;
        }
        return list;
    }
    ```
  * 위 메서드를 이용해 isPrime 메서드를 다시 구현. 이번에도 대상 숫자의 제곱근보다 작은 소수만 검사
    ```
    public static boolean isPrime(List<Integer> primes, int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return takeWhile(primes, i -> i <= candidateRoot).stream().noneMatch(p -> candidate % p == 0);
    }
    ```
  * 스트림 API와는 달리 직접 구현한 takeWhile 메서드는 적극적(eager)으로 동작함
    * 따라서 가능하면 noneMatch 동작과 조화를 이룰 수 있도록 자바 9 스트림에서 제공하는 게으른 버전의 takeWhile을 사용하는 것이 좋음
---

## chapter 07 - 병럴 데이터 처리와 성능
* 컬렉션 데이터 처리 속도를 높이려고 따로 고민할 필요 없음
* 멀티코어를 활용해서 파이프라인 연산을 실행할 수 있다는 점이 가장 중요한 특징
* 자바 7 이전에는 데이터 컬렉션을 병렬로 처리하기 어려웠음
  * 데이터를 서브파트로 분할해야 함
  * 분할된 서브파트를 각각의 스레드로 할당
  * 스레드로 할당한 다음에는 의도치 않은 레이스 컨디션(race condition)이 발생하지 않도록 적절한 동기화 추가해야 함
    * 레이스 컨디션(race condition) : 둘 이상의 입력, 조작의 타이밍이나 순서 등이 결과값에 영향을 주는 상태
  * 마지막으로 부분 결과를 합쳐야 함
* 자바 7은 더 쉽게 병렬화를 수행하면서 에러를 최소화 할 수 있도록 포크/조인 프레임워크 기능 제공

### 병렬 스트림
* 컬렉션에 parallelStream을 호출하면 병렬 스트림(parallel stream)이 생성됨
* 병렬 스트림이란 각각의 스레드에서 처리할 수 있도록 스트림 요소를 여러 청크로 분할한 스트림
* 예제
  * 숫자 n을 인수로 받아서 1부터 n까지의 모든 숫자의 합계를 반환하는 메서드를 구현한다고 가정
  * 숫자로 이루어진 무한 스트림을 만든 다음에 인수로 주어진 크기로 스트림을 제한하고, 두 숫자를 더하는 BinaryOperator로 리듀싱 작업을 수행할 수 있음
    ```
    public long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1) // 무한 자연수 스트림 생성
                .limit(n)                     // n개 이하로 제한
                .reduce(0L, Long::sum);       // 모든 숫자를 더하는 스트림 리듀싱 연산
    }
    ```
    * 전통적인 자바
      ```
      public long iterativeSum(long n) {
          long result = 0;
          for (long i = 1L; i <= n; i++) {
              result += i;
          }
          return result;
      }
      ```
    * n이 커진다면 이 연산을 병렬로 처리하는 것이 좋음
    
* 순차 스트림을 병렬 스트림으로 변환하기
  * 순차 스트림에 parallel 메서드를 호출하면 기존의 함수형 리듀싱 연산(숫자 합계 계산)이 병렬로 처리됨
    ```
    public long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()                  // 스트림을 병렬 스트림으로 변환
                .reduce(0L, Long::sum);
    }
    ```
    * 위 코드에서는 리듀싱 연산으로 스트림의 모든 숫자를 더함
      * 스트림이 여러 청크로 분할되어 있음
      * 리듀싱 연산을 여러 청크에 병렬로 수행할 수 있음
      * 리듀싱 연산으로 생성된 부분 결과를 다시 리듀싱 연산으로 합쳐서 전체 스트림의 리듀싱 결과를 도출함
  * 순차 스트림에 parallel을 호출해도 스트림 자체에는 아무 변화도 일어나지 않음
    * 내부적으로는 parallel을 호출하면 이후 연산이 병렬로 수행해야 함을 의미하는 불리언 플래그가 설정됨
    * 반대로 sequential로 병렬 스트림을 순차 스트림으로 바꿀 수 있음
    * 이 두 셈서드를 이용해서 어떤 연산을 병렬로 실행하고 어떤 연산을 순차로 실행할지 제어할 수 있음
    * 에시
      ```
      stream.parallel()
          .filter()
          .sequential()
          .map()
          .parallel()
          .reduce();
      ```
      * parallel, sequential 두 메서드 중 최종적으로 호출된 메서드가 전체 파이프라인에 영향을 미침

* 병렬 스트림에서 사용하는 스레드 풀 설정
  * 병렬 스트림은 내부적으로 ForkJoinPool을 사용함
  * 기본적으로 ForkJoinPool은 프로세서 수, 즉 Runtime.getRuntime(), availableProcessors()가 반환하는 값에 상응하는 스레드를 가짐
    ``` System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12"); ```
    * 이 예제는 전역 설정 코드이므로 이후의 모든 병렬 스트림 연산에 영향을 줌
  * 현재는 하나의 병렬 스트림에 사용할 수 있는 특정한 값을 지정할 수 없음
  * 일반적으로 기기의 프로세서 수와 같으므로 특별한 이유가 없다면 ForkJoinPool의 기본값을 그대로 사용할 것을 권장

* 스트림 성능 측정
  * 성능 최적화 시 세 가지 황금 규칙 첫째도, 둘째도, 셋째도 측정
  * 자바 마이크로벤치마크 하니스(Java Microbenchmark Harness - JMH)라는 라이브러리 이용해 구현
    * JMH를 이용하면 간단하고, 어노테이션 기반 방식을 지원하며, 안정적으로 자바 프로그램이나 JVM을 대상으로 하는 다른 언어용 벤치마크 구현 가능
    * JVM로 실행되는 프로그램을 벤치마크하는 작업은 어려움
    * 핫스팟(Hot spot)이 바이트코드를 최적화 하는데 필요한 준비 시간, 가비지 컬렉터로 인한 오버헤드 등과 같은 여러 요소를 고려해야 함
    ```
    @BenchmarkMode(Mode.AverageTime)                 // 벤치마크 대상 메서드를 실행하는 데 걸린 평균 시간 측정
    @OutputTimeUnit(TimeUnit.MILLISECONDS)           // 벤치마크 결과를 밀리초 단위로 출력
    @Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"}) // 4Gb의 힙 공간을 제공한 환경에서 두 번 벤치마크를 수행해 결과의 신뢰성 확보
    public class ParallelStreamBenchmark {
    	private static final long N = 10_000_000L;
    
    	@Benchmark
    	public long sequentialSum() {
    		return Stream.iterate(1L, i -> i + 1)
    				.limit(N)
    				.reduce(Long::sum)
    				.get();
    	}
    
    	@TearDown(Level.Invocation) // 매 번 벤치마크를 실행한 다음에는 가비지 컬렉터 동작 시도
    	public void tearDown() {
    		System.gc();
    	}
    }
    ```
    * 클래스를 컴파일하면 이전에 설정한 메이븐 플러그인 benchmarks.jar라는 두 번째 파일을 만듦
      * 실행 : java -jar ./target/benchmarks.jar ParallelStreamBenchmark
    * 벤치마크가 가능한 가비지 컬렉터의 영향을 받지 않도록 힙의 크기를 충분하게 설정, 벤치마크가 끝날 때 마다 가비지 컬렉터가 실행되도록 강제함
      * 여전히 결과는 정확하지 않을 수 있음 (기계가 지원하는 코어의 갯수 등이 실행 시간에 영향을 미칠 수 있기 때문)
    * 이 코드를 실행할 때 JMH 명령은 핫스팟이 코드를 최적화 할 수 있도록 20번을 실행하며 벤치마크를 준비한 다음 20번을 더 실행해 최정 결과를 계산함
      * JMH는 기본적으로 20 + 20 회 프로그램 반복 실행함
      * JMH의 특정 어노테이션이나 -w, -i 플래그를 명령행에 추가해 이 기본 동작 횟수를 조절할 수 있음
    * 전통적인 for 루프를 사용해 반복하는 방법이 더 저수준으로 동작할 뿐 아니라, 기본값을 박싱, 언박싱할 필요가 없으므로 더 빠를 것이라 예상함
      ```
      @Benchmark
      public long iterativeSum() {
      	  long result = 0;
      	  for (long i = 1L; i <= N; i++) {
              result += i;
          }
          return result;
      }
      ```
    * 병렬 스트림을 사용
      ```
      @Benchmark
      public long parallelSum() {
          return Stream.iterate(1L, i -> i + 1)
                    .limit(N)
                    .parallel()
                    .reduce(0L, Long::sum);
      }
      ```
    * 병렬 버전이 순차 버전에 비해 다섯 배나 느림
      * 두 가지 문제 발견
        * 반복 결과로 박싱된 객체가 만들어지므로 숫자를 더하려면 언박싱을 해야 함
        * 반복 작업은 병렬로 수행할 수 있는 독립 단위로 나누기가 어려움
      * 이전 연산의 결과에 따라 다음 함수의 입력이 달라지기 때문에 iterate 연산을 청크로 분할하기 어려움
        * iterate는 본질적으로 순차적
      * 이와 같은 상황에서는 리듀싱 연산이 수행되지 않음
        * 리듀싱 과정을 시작하는 시점에 전체 숫자 리스트가 준비되지 않았으므로 스트림을 병렬로 처리할 수 있도록 청크를 분할할 수 없음
        * 스트림이 병렬로 처리되도록 지시했고 각각의 합계가 다른 스레드에서 수행되었지만 결국 순차처리 방식과 크게 다른 점이 없음
          * 스레드를 할당하는 오버헤드만 증가하게 됨

* 더 특화된 메서드 사용
  * LongStreamClosed 메서드는 iterate에 비해 다음과 같은 장점 제공
    * LongStreamClosed는 기본형 long을 직접 사용하므로 박싱, 언박싱 오버헤드가 사라짐
    * LongStreamClosed는 쉽게 청크로 분할할 수 있는 숫자 범위를 생산함
      * 예를 들어 1-20 범위의 숫자를 각가 1-5, 6-10, 11-15, 16-20 범위의 숫자로 분할할 수 있음
    * 순차 스트림 처리 시간 측정
      ```
      @Benchmark
      public long rangedSum() {
      	return LongStream.rangeClosed(1, N)
      			.reduce(0L, Long::sum);
      }
      ```
      * 기존 iterate 팩토리 메서드로 생성한 순차 버전에 의해 이 예제의 숫자 스트림 처리 속도가 더 빠름
        * 특화되지 않은 스트림을 처리할 때는 오토박싱, 언박싱 등의 오버헤드를 수반하기 때문
      * 상황에 따라서는 어떤 알고리즘을 병렬화하는 것보다 적절한 자료구조를 선택하는 것이 더 중요하다는 사실을 단적으로 보여줌
      * 새로운 버전 병렬 스트림
        ```
        @Benchmark
        public long parallelRangedSum() {
            return LongStream.rangeClosed(1, N)
                    .parallel()
                    .reduce(0L, Long::sum);
        }
        ```
        * 순차 실행보다 빠른 성능을 갖는 병렬 리듀싱
        * 함수형 프로그래밍을 올바로 사용하면 반복적으로 코드를 실행하는 방법에 비해  
          최신 멀티 코어 CPU가 제공하는 병렬 실행의 힘을 단순하게 직접적으로 얻을 수 있음
        * 하지만 병렬화는 공짜가 아님
          * 병렬화를 이용하려면 스트림을 재귀적으로 분할, 각 서브스티림을 서로 다른 스레드의 리듀싱으로 연산으로 할당, 결과를 하나의 값으로 합쳐야 함
          * 멀티코어 간의 데이터 이동은 생각보다 비쌈
            * 따라서 코어 간에 데이터 전송 시간보다 훨씬 오래 걸리는 작업만 병렬로 다른 코어에서 수행하는 것이 바람직함
          * 상황에 따라 쉽게 병렬화를 이용할 수 있거나 아니면 아예 병렬화를 이용할 수 없을 때도 있음

* 병렬 스트림의 올바른 사용법
  * 병렬 스트림을 잘못 사용하면서 발생하는 많은 문제는 공유된 상태를 바꾸는 알고리즘을 사용하기 때문에 생김
  * n까지의 자연수를 더하면서 공유된 누적자를 바꾸는 프로그램 구현 코드
    ```
    public long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).forEach(accumulator::add);
        return accumulator.total;
    }

    public class Accumulator {
        public long total = 0;

        public void add(long value) {
            total += value;
        }
    }
    ```
    * 위 코드는 순차 실행할 수 있도록 구현되어 있으므로 병렬로 실행하면 문제가 발생
      * total 접근할 때마다(다수의 스레드에서 동시에 데이터에 접근하는) 데이터 레이스 문제 발생
      * 동기화로 문제를 해결하다보면 결국 병렬화라는 특성이 없어져 버림
        ```
        public long sideEffectParallelSum(long n) {
            Accumulator accumulator = new Accumulator();
            LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
            return accumulator.total;
        }
        ```
        * 성능은 둘째치고, 올바른 결과값이 나오지 않음
        * ``` total += value ``` 는 아토믹 연산이 아님
        * 여러 스레드에서 공유하는 객체의 상태를 바꾸는 forEach 블록 내부에서 add 메서드를 호출하면서 이와 같은 문제 발생

* 병렬 스트림 효과적으로 사용하기
  * '천 개 이상의 요소가 있을 때만 병렬 스트림을 사용하라' 와 같이 양을 기준으로 병렬 스트림 사용을 결정하는 것은 적절하지 않음
    * 정해진 기기에서 정해진 연산을 수행할 때는 이와 같은 기준을 사용할 수 있지만 상황이 달라지면 이와 같은 기준이 제 역할을 하지 못함
    * 그래도 어떤 상황에서 병렬 스트림을 사용할 것인지 약간의 수량적 힌트를 정하는 것이 도움이 될 때도 있음
  * 확신이 서지 않으면 직접 측정하라
    * 순차 스트림을 병렬 스트림으로 쉽게 바꿀 수 있음
    * 하지만 무조건 병렬 스트림으로 바꾸는 것이 능사는 아님
    * 언제나 병렬 스트림이 순차 스트림보다 빠른 것이 아니기 때문
    * 더욱이 병렬 스트림의 수행 과정은 투명하지 않을 때가 많음
    * 따라서 순차 스트림과 병렬 스트림 중 어떤 것이 좋을지 모르겠다면 적절한 벤치마크로 직접 성능을 측정하는 것이 바람직함 
  * 박싱을 주의하라
    * 자동 박싱, 언박싱은 성능을 크게 저하시킬 수 있는 요소
    * 되도록이면 기본형 특화 스트림을 사용하는 것이 좋음 (IntStream, LongStream, DoubleStream)
  * 순차 스트림보다 병렬 스트림에서 성능이 떨어지는 연산이 있음
    * 특히 limit, findFirst처럼 요소의 순서에 의존하는 연산을 병렬 스트림에서 수행하려면 비싼 비용을 치러야 함
    * 예를 들어 findAny는 요소의 순서와 상관없이 연산하므로 findFirst보다 성능이 좋음
    * 정렬된 스트림에 unordered를 호출하면 비정렬된 스트림을 얻을 수 있음
    * 스트림에 N개 요소가 있을 때 요소의 순서가 상관 없다면(얘를 들어 소스가 리스트라면) 비정렬된 스트림에 limit를 호출하는 것이 더 효율적
  * 스트림에서 수행하는 전체 파이프라인 연산 비용을 고려하라
    * 처리해야 할 요소가 N, 하나의 요소를 처리하는 데 드는 비용을 Q라 하면 전체 스트림 파이프라인 처리 비용을 N * Q로 예상할 수 있음
    * Q가 높아진다는 것은 병렬 스트림으로 성능을 개선할 수 있는 가능성이 있음을 의미
  * 소량의 데이터에서는 병렬 스트림이 도움 되지 않음
    * 소량의 데이터를 처리하는 상황에서는 병렬화 과정에서 생기는 부가 비용을 상쇄할 수 있을 만큼의 이득을 얻지 못하기 때문
  * 스트림을 구성하는 자료구조가 적절한지 확인하라
    * 예를 들어 ArrayList를 LinkedList보다 효율적으로 분할할 수 있음
    * LinkedList를 분할하려면 모든 요소를 탐색해야 하지만 ArrayList는 요소를 탐색하지 않고도 리스트를 분할할 수 있기 때문
    * range 팩토리 메서드로 만든 기본형 스트림도 쉽게 분해 가능
    * 커스텀 Spliterator를 구현해서 분해 과정을 완벽하게 제어 가능
  * 스트림의 특성과 파이프라인의 중간 연산이 스트림의 특성을 어떻게 바꾸는지에 따라 분해 과정의 성능이 달라질 수 있음
    * 예를 들어 SIZED 스트림은 정확히 같은 크기의 두 스트림으로 분할할 수 있으므로 효과적으로 스트림을 병렬처리 할 수 있음
    * 반면 필터 연산이 있으면 스트림의 길이를 예측할 수 없으므로 효과적으로 스트림을 병렬 처리할 수 있을지 알 수 없게 됨
  * 최종 연산의 병합 과정(예를 들어 Collector의 combiner 메서드) 비용을 살펴보라
    * 병합 과정의 비용이 비싸다면 병렬 스트림으로 얻은 성능의 이익이 서브스트림의 부분 결과를 합치는 과정에서 상쇄될 수 있음
  * 스트림 소스와 분해성
    * ArrayList
      * 훌륭함
    * LinkedList
      * 나쁨
    * IntStream.range
      * 훌륭함
    * Stream.iterate
      * 나쁨
    * HashSet
      * 좋음
    * TreeSet
      * 좋음
  * 병렬 스트림이 수행되는 내부 인프라구조도 살펴봐야 함
    * 자바 7에서 추가된 포크/조인 프레임워크로 병렬 스트림이 처리됨

### 포크/조인 프레임워크
* 포크/조인 프레임워크는 병렬화할 수 있는 작업을 재귀적으로 작은 작업으로 분할한 다음에 서브태스크의 각각의 결과를 합쳐서 전체 결과를 만들도록 설계됨
* 포크/조인 프레임워크에서는 서브태스크를 스레드 풀(ForkJoinPool)의 작업자 스레드에 분산 할당하는 ExecutorService 인터페이스를 구현함

* RecursiveTask 활용
  * 스레드 풀을 이용하려면 RecursiveTask\<R>의 서브클래스를 만들어야 함
    * 여기서 R은 병렬화된 태스크가 생성하는 결과 형식 또는 결과가 없을 때는(결과가 없더라도 다른 비지역 구조를 바꿀 수 있음) RecursiveAction 형식
    * 추상 메서드 compute를 구현해야 함
      * ``` protected abstract R compute() ```
      * compute 메서드는 태스크를 서브태스크로 분할하는 로직과 더 이상 분할할 수 없을 때 개별 서브태스크의 결과를 생산할 알고리즘을 정의함
      * 따라서 대부분의 compute 메서드 구현은 다음과 같은 의사코드 형식을 유지
        ```
        if (태스크가 충분히 작거나 더 이상 분할할 수 없으면) {
            순차적으로 태스크 계산
        } else {
            태스크를 두 서브태스크로 분할
            태스크가 다시 서브태스크로 분할되도록 이 메서드를 재귀적으로 호출함
            모든 서브태스크의 연산이 완료될 때까지 기다림
            각 서브태스크의 결과를 합침
        }
        ```
  * 이 알고리즘은 분할 후 정복(divide-and-conquer) 알고리즘의 병렬화 버전
    ```
    // RecursiveTask를 상속받아 포크/조인 프레임워크에서 사용할 태스크를 생성함
    public class ForkJoinSumCalculator extends RecursiveTask<Long> {
    	// 이 값 이하의 서브태스크는 더 이상 분할할 수 없음
    	public static final long THRESHOLD = 10_000;
    
    	// 더할 숫자 배열
    	private final long[] numbers;
    
    	// 이 서브태스크에서 처리할 배열의 초기 위치와 최종 위치
    	private final int start;
    	private final int end;
    
    	// 메인 태스크를 생성할 때 사용할 공개 생성자
    	public ForkJoinSumCalculator(long[] numbers) {
    		this(numbers, 0, numbers.length);
    	}
    
    	// 메인 태스크의 서브태스크를 재귀적으로 만들때 사용하는 비공개 생성자
    	private ForkJoinSumCalculator(long[] numbers, int start, int end) {
    		this.numbers = numbers;
    		this.start = start;
    		this.end = end;
    	}
    
    	// RecursiveTask의 추상 메서드 오버라이드
    	@Override
    	protected Long compute() {
    		// 이 태스크에서 더할 배열의 길이
    		int length = end - start;
    
    		// 기준과 같거나 작으면 순차적으로 결과를 계산
    		if (length <= THRESHOLD) {
    			return computeSequentially();
    		}
    
    		// 배열의 첫 번째 절반을 더하도록 서브태스크를 생성
    		ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
    		// ForkJoinPool의 다른 스레드로 새로 생성한 태스크를 비동기로 실행
    		leftTask.fork();
    
    		// 배열의 나머지 절반을 더하도록 서브태스크를 생성
    		ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, start + end);
    		// 두 번째 서브태스크를 동기 실행
    		// 이 때 추가로 분할이 일어날 수 있음
    		Long rightResult = rightTask.compute();
    		// 첫 번째 서브태스크의 결과를 읽거나 아직 결과가 없으면 기다림
    		Long leftResult = leftTask.join();
    
    		// 두 서브태스크의 결과를 조합한 값이 이 태스크의 결과
    		return leftResult + rightResult;
    	}
    
    	// 더 분할할 수 없을 때 서브태스크의 결과를 계산하는 단순한 알고리즘
    	private Long computeSequentially() {
    		long sum = 0;
    		for (int i = start; i < end; i++) {
    			sum += numbers[i];
    		}
    		return sum;
    	}
    
    	// 이와 같이 ForkJoinSumCalculator의 생성자로 원하는 수의 배열을 넘겨줄 수 있음
    	public static long forkJoinSum(long n) {
    		// n까지의 자연수를 포함하는 배열 생성
    		long[] numbers = LongStream.rangeClosed(1, n).toArray();
    		// 생성된 배열을 ForkJoinSumCalculator의 생성자로 전달, ForkJoinTask 생성
    		ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
    		// 마지막으로 생성한 태스크를 새로운 ForkJoinPool의 invoke 메서드로 전달
    		// ForkJoinPool에서 실행되는 마지막 invoke 메서드의 반환값은 ForkJoinSumCalculator에서 정의한 태스크 결과가 됨
    		return new ForkJoinPool().invoke(task);
    	}
    }
    ```
  * 일반적으로 앱에서는 둘 이상의 ForkJoinPool을 사용하지 않음
    * 즉 소프트웨어의 필요한 곳에서 언제든 가져다 쓸 수 있도록 ForkJoinPool을 한 번만 인스턴스화해서 정적 필드에 싱글턴으로 저장
    * ForkJoinPool을 만들면서 인수가 없는 디폴트 생성자를 이용
      * 이는 JVM에서 이용할 수 있는 모든 프로세서가 자유롭게 풀에 접근할 수 있음을 의미함
      * 더 정확하게는 Runtime.availableProcessors의 반환값으로 풀에 사용할 스레드 수를 결정함
      * availableProcessors, '사용할 수 있는 프로세서'라는 이름과는 달리 실제 프로세서 외에 하이퍼스레딩과 관련된 가상 프로세서도 개수에 포함됨

* ForkJoinSumCalculator 실행
  * ForkJoinSumCalculator를 ForkJoinPool로 전달하면 풀의 스레드가 ForkJoinSumCalculator의 compute 메서드를 실행하면서 작업 수행
  * compute 메서드는 병렬로 실행할 수 있을만큼 태스크의 크기가 충분히 작아졌는지 확인
  * 아직 태스크의 크기가 크다고 판단되면 숫자 배열을 반으로 분할해서 두 개의 새로운 ForkJoinSumCalculator로 할당
  * 그러면 다시 ForkJoinPool이 새로 생성된 ForkJoinSumCalculator를 실행
  * 결국 이 과정이 재귀적으로 반복되면서 주어진 조건을 만족할 때까지 태스크 분할을 반복함
  * 이제 각 서브태스크는 순차적으로 처리되며 포킹 프로세스를 만들어진 이진트리의 태스크를 루트에서 역순으로 방문함
  * 각 서브태스크의 부분결과를 합쳐서 태스크의 최종 결과를 계산함
  * 처음 부분 하니스로 포크/조인 프레임워크의 합계 메서드 성능 확인
    ```
    System.out.println("ForkJoin sum done in: " +
        measurePerf(ForkJoinSumCalculator::forkJoinSum, 10_000_000L) + " msecs");
    ```
    * 병렬 스트림을 이용할 때보다 성능이 나빠짐
      * 하지만 이는 ForkJoinSumCalculator 태스크에서 사용할 수 있도록 전체 스트림을 long[]으로 변환했기 때문
    
* 포크/조인 프레임워크를 제대로 사용하는 방법
  * join 메서드를 태스크에 호출하면 태스크가 생산하는 결과가 준비될 때까지 호출자를 블록시킴
    * 따라서 두 서브태스크가 모두 시작된 다음에 join을 호출해야 함
    * 그렇지 않으면 각각의 서브태스크가 다른 태스크가 끝나길 기다리는 일이 발생하며 원래 순차 알고리즘보다 느리고 복잡한 프로그램이 될 수 있음
  * RecursiveTask 내에서는 ForkJoinPool의 invoke 메서드를 사용하지 말아야 함
    * 대신 compute, fork 메서드를 직접 호출할 수 있음
    * 순차 코드에서 병렬 계산을 시작할 때만 invoke를 사용함
  * 서브태스크에 fork 메서드를 호출해서 ForkJoinPool의 일정을 조절할 수 있음
    * 왼쪽 작업과 오른쪽 작업 모두에 fork 메서드를 호출하는 것이 자연스러울 것 같지만
    * 한쪽 작업에는 fork를 호출하는 것 보다는 compute를 호출하는 것이 효율적
    * 그러면 두 서브태스크의 한 태스크에는 같은 스레드를 재사용할 수 있으므로 풀에서 불필요한 태스크를 할당하는 오버헤드를 피할 수 있음
  * 포크/조인 프레임워크를 이용하는 병렬 계산은 디버깅하기 어려움
    * 보통 IDE로 디버깅할 때 스택 트레이스(stack trace)로 문제가 일어난 과정을 쉽게 확인할 수 있는데
    * 포크/조인 프레임워크에서는 fork라 불리는 다른 스레드에서 compute를 호출하므로 스택 트레이스가 도움이 되지 않음
  * 병렬 스트림에서 살펴본 것처럼 멀티코어에 포크/조인 프레임워크를 사용하는 것이 순차 처리보다 무조건 빠를 거라는 생각은 버려야 함
    * 병렬 처리로 성능을 개선하려면 태스크를 여러 독립적인 서브태스크로 분할할 수 있어야 함
    * 각 서브태스크의 실행시간은 새로운 태스크를 포킹하는 데 드는 시간보다 길어야 함
    * 예를 들어 I/O를 한 서브태스크에 할당하고 다른 서브태스크에서는 계산을 실행, 즉 I/O와 계산을 병렬로 실행할 수 있음
    * 또한 순차 버전과 병렬 버전의 성능을 비교할 때는 다른 요소도 고려해야 함
    * 다른 자바 코드와 마찬가지로 JIT 컴파일러에 의해 최적화되려면 몇 차례의 '준비 과정(warmed up)' 또는 실행 과정을 거쳐야 함
    * 따라서 성능을 측정할 때는 지금까지 살펴본 하니스에서 그랬던 것처럼 여러 번 프로그램을 실행한 결과를 측정해야 함
    * 또한 컴파일러 최적화는 병렬 버전보다는 순차 버전에 집중될 수 있다는 사실
      * 예를 들어 순차 버전에서는 죽은 코드를 분석해서 사용되지 않는 계산은 아예 삭제하는 등의 최적화를 달성하기 쉬움

* 작업 훔치기
  * ForkJoinSumCalculator 에제에서는 덧셈을 수행할 숫자가 만 개 이하면 서브태스크 분할을 중단했음
    * 기준값을 바꿔가면서 실험해보는 방법 외에는 좋은 기준을 찾을 뾰족한 방법이 없음
    * 우선 천만 개 항목을 포함하는 배열을 사용하면 ForkJoinSumCalculator는 천 개 이상의 서브 태스크를 포크할 것
    * 대부분의 기기에는 코어가 네 개뿐이므로 천 개 이상의 서브태스크는 자원만 낭비하는 것 같아 보일 수 있음
    * 실제로 각각의 태스크가 CPU로 할당되는 상황이라면 어차피 천 개 이상의 서브태스크로 분할한다고 해서 성능이 좋아지지는 않음
  * 하지만 실제로는 코어 개수와 관계없이 적절한 크기로 분할된 많은 태스크를 포킹하는 것이 바람직함
    * 이론적으로는 코어 개수만큼 병렬화된 태스크로 작업부하를 분할하면 모든 CPU 코어에서 태스크를 실행할 것이고
    * 크기가 같은 각각의 태스크는 같은 시간에 종료될 것이라고 생각할 수 있음
    * 하지만 이 예제보다 복잡한 시나리오가 사용되는 현실에서는 각각의 서브태스크의 작업완료 시간이 크게 달라질 수 있음
    * 분할 기법이 효율적이지 않았기 때문일 수도 있고 아니면 디스크 접근 속도가 저하되었거나 외부 서비스와 협력하는 과정에서 지연이 생길 수 있기 때문
  * 포크/조인 프레임워크에서는 작업 훔치기(work stealing)라는 기법으로 이 문제를 해결함
    * 작업 훔치기 기법에서는 ForkJoinPool의 모든 스레드를 거의 공정하게 분할함
    * 각각의 스레드는 자신에게 할당된 태스크를 포함하는 이중 연결 리스트(doubly linked list)를 참조하면서
    * 작업이 끝날 때마다 큐의 헤드에서 다른 태스크를 가져와 작업을 처리함
    * 이때 한 스레드는 다른 스레드보다 자신에게 할당된 태스크를 더 빨리 처리할 수 있음
    * 즉 다른 스레드는 바쁘게 일하는데 한 스레드는 할일이 다 떨어진 상황 발생
    * 이때 할일이 없어진 스레드는 유휴 상태로 바뀌는 것이 아니라 다른 스레드 큐의 꼬리(tail)에서 작업을 훔쳐옴
    * 모든 태스크가 작업을 끝낼 때까지, 즉 모든 큐가 빌 때까지 이 과정을 반복함
    * 따라서 태스크의 크기를 작게 나눠야 작업자 스레드 간의 작업부하를 비슷한 수준으로 유지할 수 있음
  * 풀에 있는 작업자 스레드의 태스크를 재분배하고 균형을 맞출 때 작업 훔치기 알고리즘을 사용함
    * 작업자의 큐에 있는 태스크를 두 개의 서브 태스크로 분할했을 때 둘 중 하나의 태스크를 다른 유휴 작업자가 훔쳐갈 수 있음
    * 그리고 주어진 태스크를 순차 실행할 단계가 될 때까지 이 과정을 재귀적으로 반복함
  * 스트림을 자동으로 분할해주는 기능
    * Spliterator

### Spliterator 인터페이스
* 자바 8은 Spliterator라는 새로운 인터페이스를 제공함
  * '분할할 수 있는 반복자(splitable iterator)'라는 의미
  * Iterator처럼 Spliterator는 소스의 요소 탐색 기능을 제공한다는 점은 같지만 Spliterator는 병렬 작업에 특화되어 있음
  * 커스텀 Spliterator를 꼭 직접 구현해야 하는 것은 아니지만 Spliterator가 어떻게 동작하는지 이해한다면
  * 병렬 스트림 동작과 관련한 통찰력을 얻을 수 있음
  * 자바 8은 컬렉션 프레임워크에 포함된 모든 자료구조에 사용할 수 있는 디폴트 Spliterator 구현을 제공함
  * 컬렉션은 spliterator라는 메서드를 제공하는 Spliterator 인터페이스를 구현함
  * Spliterator 인터페이스는 여러 메서드를 정의함
    ```
    public interface Spliterator<T> {
        boolean tryAdvance(Consumer<? super T> action);
        Spliterator<T> trySplit();
        long estimateSize();
        int characteristics();
    }
    ```
    * 여기서 T는 Spliterator에서 탐색하는 요소의 형식을 가리킴
      * tryAdvance 메서드는 Spliterator의 요소를 하나씩 순차적으로 소비하면서 탐색해야 할 요소가 남아있으면 참을 반환함
        * 일반적인 Iterator 동작과 같음
      * 반면 trySplit 메서드는 Spliterator의 일부 요소(자신이 반환한 요소)를 분할해서 두 번째 Spliterator를 생성하는 메서드
      * Spliterator에서는 estimateSize 메서드로 탐색해야 할 요소 수 정보를 제공할 수 있음
      * 특히 탐색해야 할 요소 수가 정확하진 않더라도 제공된 값을 이용해서 더 쉽고 공평하게 Spliterator를 분할할 수 있음

* 분할 과정
  * 스트림을 여러 스트림으로 분할하는 과정은 재귀적으로 일어남
  * 첫 번째 Spliterator에 trySplit을 호출하면 두 번째 Spliterator가 생성됨
  * 두 개의 Spliterator에 trySplit을 호출하면 네 개의 Spliterator가 생성됨
  * trySplit의 결과가 null이 될 때까지 이 과정을 반복함
  * 이 분할 과정은 characteristics 메서드로 정의하는 Spliterator의 특성에 영향을 받음
  
* Spliterator 특성
  * Spliterator는 characteristics라는 추상 메서드도 정의함
  * characteristics 메서드는 Spliterator 자체의 특성 집합을 포함하는 int를 반환함
  * Spliterator를 이용하는 프로그램은 이들 특성을 참고해서 Spliterator를 더 잘제어하고 최적화할 수 있음
  * Spliterator 특성 정보 - 일부 특성은 컬렉터와 개념상 비슷함에도 다른 방식으로 정의되었음
    * ORDERED
      * 리스트처럼 요소에 정해진 순서가 있으므로 Spliterator는 요소를 탐색하고 분할할 때 이 순서에 유의해야 함
    * DISTINCT
      * x, y 두 요소를 방문햇을 때 x.equals(y)는 항상 false를 반환함
    * SORTED
      * 탐색된 요소는 미리 정의된 정렬 순서를 따름
    * SIZED
      * 크기가 알려진 소스(예를 들면 Set)로 Spliterator를 생성했으므로 estimateSize()는 정확한 값을 반환함
    * NON-NULL
      * 탐색하는 모든 요소는 null이 아님
    * IMMUTABLE
      * 이 Spliterator의 소스는 불변. 즉 요소를 탐색하는 동안 요소를 추가, 삭제하거나 고칠 수 없음
    * CONCURRENT
      * 동기화 없이 Spliterator의 소스를 여러 스레드에서 동시에 고칠 수 있음
    * SUBSIZED
      * 이 Spliterator 그리고 분할되는 모든 Spliterator는 SIZED 특성을 가짐

* 커스텀 Spliterator 구현하기
  * 구현 예제
    ```
    public static int countWordsIteratively(String s) {
		int counter = 0;
		boolean lastSpace = true;

		for (char c : s.toCharArray()) { // 문자열의 모든 문자를 하나씩 탐색

			if (Character.isWhitespace(c)) {
				lastSpace = true;
			} else {
				// 문자를 하나씩 탐색하다 공백 문자를 만나면 지금까지 탐색한 문자를 단어로 간주하여 (공백 문자는 제외) 단어 수를 증가시킴
				if (lastSpace) counter++;
				lastSpace = false;
			}
		}
		return counter;
	}
    ```
  * 단테의 인페르노의 첫 문장으로 위 메서드 실행
    * http://en.wikipedia.org/wiki/Inferno_(Dante)
      ```
      public static final String SENTENCE =
            " Nel   mezzo del cammin  di nostra  vita "
                    + "mi  ritrovai in una  selva oscura"
                    + " che la  dritta via era   smarrita ";
      ```
    * 공백이 여러 개일 때도 반복 구현이 제대로 동작된다는 것을 보이고자 문장에 임의로 공백을 추가함
      * 여러 개의 공백을 공백 하나로 간주함
    * 결과
      * Found 19 words

* 함수형 단어 수를 세는 메서드 재구현하기
  * String을 스트림으로 변환해야 함
    * 스트림은 int, long, double 기본형만 제공하므로 Stream\<Character>를 사용해야 함
      ``` Stream<Character> stream = IntStream.range(0, SENTENCE.length()).mapToObj(SENTENCE::charAt); ```
    * 스트림에는 리듀싱 연산을 실행하면서 단어 수를 계산할 수 있음
      * 이때 지금까지 발견한 단어 수를 계산하는 int 변수와 마지막 문자가 공백이었는지 여부를 기억하는 boolean 변수 등 두 가지 변수가 필요함
      * 자바에는 튜플이 없으므로 이들 변수 상태를 캡슐화하는 새로운 클래스 WordCounter를 만들어야 함
        * 튜플 - 래퍼 객체 없이 다형 요소의 정렬 리스트를 표현할 수 있는 구조체
        ```
        private static class WordCounter {
            private final int counter;
            private final boolean lastSpace;
    
            public WordCounter(int counter, boolean lastSpace) {
                this.counter = counter;
                this.lastSpace = lastSpace;
            }
    
            // 반복 알고리즘처럼 accumulate 메서드는 문자열의 문자를 하나씩 탐색함
            public WordCounter accumulate(Character c) {
                if (Character.isWhitespace(c)) {
                    return lastSpace ? this : new WordCounter(counter, true);
                } else {
                    // 문자를 하나씩 탐색하다 공백 문자를 만나면 지금까지 탐색한 문자를 단어로 간주하여 단어 수를 증가시킴
                    return lastSpace ? new WordCounter(counter + 1, false) : this;
                }
            }
    
            public WordCounter combine(WordCounter wordCounter) {
                // 두 WordCounter의 counter 값을 더함
                // counter 값만 더할 것이므로 마지막 공백은 신경 쓰지 않음
                return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
            }
    
            public int getCounter() {
                return counter;
            }
        }
        ```
        * accumulate 메서드는 WordCounter의 상태를 어떻게 바꿀 것인지
          * 또는 엄밀히 WordCounter(속성을 바꿀 수 없는)는 불변 클래스이므로 새로운 WordCounter 클래스를 어떤 상태로 생성할 것인지 정의함
          * 스트림을 탐색하면서 새로운 문자를 찾을 때마다 accumulate 메서드를 호출함
          * countWordsIteratively에서처럼 새로운 비공백 문자를 탐색한 다음에 마지막 문자가 공백이면 counter를 증가시킴
        * 두 번째 메서드 combine은 문자열 서브 스트림을 처리한 WordCounter의 결과를 합침
          * combine은 WordCounter의 내부 counter 값을 서로 합침
        * 문자 스트림의 리듀싱 연산을 직관적으로 구현
          ```
          private static int countWords(Stream<Character> stream) {
              WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                      WordCounter::accumulate,
                      WordCounter::combine);
              return wordCounter.getCounter();
          }
          ```
          * 해당 메서드 호출
            ```
            Stream<Character> stream = IntStream.range(0, SENTENCE.length()).mapToObj(SENTENCE::charAt);
            System.out.println("Found " + countWords(stream) + " words");
            ```

* WordCounter 병렬로 수행하기
  * 연산을 병렬 스트림으로 처리
    ``` System.out.println("Found " + countWords(stream.parallel()) + " words"); ```
    * 결과 Found 29 words
  * 원래 문자열을 임의의 위치에서 둘로 나누다보니 예상치 못하게 하나의 단어를 둘로 계산하는 상황이 발생할 수 있음
    * 순차 스트림을 병렬 스트림으로 바꿀 때 스트림 분할 위치에 따라 잘못된 결과가 나올 수 있음
    * 문자열을 임의의 위치에서 분할하지 말고 단어가 끝나는 위치에서만 분할하는 방법으로 이 문제를 해결할 수 있음
    * 그러려면 단어 끝에서 문자열을 분할하는 문자 Spliterator가 필요함
    * Spliterator를 구현한 다음에 병렬 스트림으로 전달하는 코드
      ```
      // 단어 끝에서 문자열을 분할하는 문자 Spliterator
      private static class WordCounterSpliterator implements Spliterator<Character> {
          private final String string;
          private int currentChar = 0;
      
          public WordCounterSpliterator(String string) {
      	      this.string = string;
          }
      
          @Override
          public boolean tryAdvance(Consumer<? super Character> action) {
              // 현재 문자 소비
              action.accept(string.charAt(currentChar++));
              // 소비할 문자가 남아있으면 true를 반환
              return currentChar < string.length();
              }
      
          @Override
          public Spliterator<Character> trySplit() {
              int currentSize = string.length() - currentChar;
      
              if (currentSize < 10) {
                  // 파싱할 문자열을 순차 처리할 수 있을 만큼 충분히 작아졌음을 알리는 null을 반환
                  return null;
              }
      
              // 파싱할 문자열의 중간을 분할 위치로 설정
              for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
                  // 다음 공백이 나올 때까지 분할 위치를 뒤로 이동 시킴
                  if (Character.isWhitespace(string.charAt(splitPos))) {
                      // 처음부터 분할 위치까지 문자열을 파싱할 새로운 WordCounterSpliterator를 생성
      	              Spliterator<Character> spliterator =
                              new WordCounterSpliterator(string.substring(currentChar, splitPos));
                      // 이 WordCounterSpliterator의 시작 위치를 분할 위치로 설정함
                      currentChar = splitPos;
                      // 공백을 찾았고 문자열을 분리했으므로 루프를 종료
      	              return spliterator;
                  }
              }
      
              return null;
          }
      
          @Override
          public long estimateSize() {
              return string.length() - currentChar;
          }
      
          @Override
          public int characteristics() {
              return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
          }
      }
      ```
      * 분석 대상 문자열로 Spliterator를 생성한 다음에 현재 탐색 중인 문자를 가리키는 인덱스를 이용해 모든 문자를 반복 탐색함
        * tryAdvance 메서드는 문자열에서 현재 인덱스에 해당하는 문자를 Consumer에 제공한 다음 인덱스를 증가시킴
          * 인수로 전달된 Consumer는 스트림을 탐색하면서 적용해야 하는 함수 집합이 작업을 처리할 수 있도록 소비한 문자를 전달하는 자바 내부 클래스
          * 예제에서는 스트림을 탐색하면서 하나의 리듀싱 함수, WordCounter의 accumulate 메서드만 적용
          * tryAdvance 메서드는 새로운 커서 위치가 전체 문자열 길이보다 작으면 참을 반환
            * 이는 반복 탐색해야 할 문자가 남아 있음을 의미함
        * trySplit은 반복될 자료구조를 분할하는 로직을 포함하므로 Spliterator에서 가장 중요한 메서드
          * 예제에서 구현한 RecursiveTask의 compute 메서드에서 했던 것처럼 우선 분할 동작을 중단할 한계를 설정해야 함
          * 여기서는 아주 작은 한계값(10개)을 사용했지만 실전 앱에서는 너무 많은 태스크를 만들지 않도록 더 높은 한계값을 설정해야 함
          * 분할 과정에서 남은 문자 수가 한계값 이하면 null을 반환, 즉 분할을 중지하도록 지시함
          * 반대로 분할이 필요한 상황에서는 파싱해야 할 문자열 청크의 중간 위치를 기준으로 분할하도록 지시함
          * 이때 단어 중간을 분할하지 않도록 빈 문자가 나올때까지 분할 위치를 이동시킴
          * 분할할 위치를 찾았으면 새로운 Spliterator를 만듦
          * 새로 만든 Spliterator는 현재 위치부터 분할된 위치까지의 문자를 탐색함
        * 탐색해야 할 요소의 개수(estimateSize)는 Spliterator가 파싱할
          * 문자열 전체 길이 (string.length())와 현재 반복 중인 위치(currentChar)의 차
        * characteristic 메서드는 프레임워크에 Spliterator가 
          * ORDERED(문자열의 문자 등장 순서가 유의미함),
          * SIZED(estimatedSize 메서드의 반환값이 정확함),
          * SUBSIZED(trySplit으로 생성된 Spliterator도 정확한 크기를 가짐),
          * NONNULL(문자열에는 null 문자가 존재하지 않음),
          * IMMUTABLE(문자열 자체가 불변 클래스이므로 문자열을 파싱하므로 속성이 추가되지 않음)
          * 등의 특성임을 알려줌

* WordCounterSpliterator 활용
  * WordCounterSpliterator를 병렬 스트림에 사용할 수 있음
    ```
    Spliterator<Character> spliterator = new WordCounterSpliterator(s);
    Stream<Character> stream = StreamSupport.stream(spliterator, true);
    
    System.out.println("Found " + countWords(SENTENCE) + " words");
    ```
  * Spliterator는 첫 번째 탐색 시점, 첫 번째 분할 시점, 첫 번째 예상 크기 요청 시점에 요소의 소스를 바인딩 할 수 있음
    * 이와 같은 동작을 늦은 바인딩 Spliterator라고 부름

### 정리
* 내부 반복을 이용하면 명시적으로 다른 스레드를 사용하지 않고도 스트림을 병렬로 처리할 수 있음
* 간단하게 스트림을 병렬로 처리할 수 있지만 항상 병렬 처리가 빠른 것은 아님
  * 병렬 소프트웨어 동작 방법과 성능은 직관적이지 않을 때가 많으므로 병렬 처리를 사용했을 때 성능을 직접 측정해봐야 함
* 병렬 스트림으로 데이터 집합을 병렬 실행할 때 특히 처리해야 할 데이터가 아주 많거나 각 요소를 처리하는데 오랜 시간이 걸리 때 성능을 높일 수 있음
* 가능하면 기본형 특화 스트림을 사용하는 등 올바른 자료구조 선택이 어떤 연산을 병렬로 처리하는 것보다 성능적으로 더 큰 영향을 미칠 수 있음
* 포크/조인 프레임워크에서는 병렬화할 수 있는 태스크를 작은 태스크로 분할한 다음
  * 분할된 태스크를 각각의 스레드로 실행하며 서브태스크 각각의 결과를 합쳐서 최종 결과를 생산함
* Spliterator는 탐색하려는 데이터를 포함하는 스트림을 어떻게 병렬화할 것인지 정의함

### [quiz]
---

## chapter 08 - 컬렉션 API 개선

### 컬렉션 팩토리
* 자바 9에서는 작은 컬렉션 객체를 쉽게 만들 수 있는 몇 가지 방법을 제공함
* 적은 요소를 포함하는 리스트 생성
  ```
  List<String> friends = new ArrayList<>();
  friends.add("Raphael");
  friends.add("Olivia");
  friends.add("Thibaut");
  
  // 고정 크기의 리스트를 만들었으므로 요소를 갱신할 순 있으나 새 요소를 추가하거나 삭제할 수 없음
  List<String> friends = Arrays.asList("Raphael", "Olivia", "Thibaut");
  
  // UnsupportedOperationException 발생
  List<String> friends = Arrays.asList("Raphael", "Olivia");
  friends.set(0, "Richard");
  friends.add("Thibaut");
  ```
* UnsupportedOperationException 예외 발생
  * 집합 생성
    ```
    Set<String> friends = new HashSet<>(Arrays.asList("Raphael", "Olivia", "Thibaut"));
    
    Set<String> friends = Stream.of("Raphael", "Olivia", "Thibaut").collect(Collectors.toSet());
    ```
    * 위 두 방법은 모두 매끄럽지 못하며 내부적으로 불필요한 객체 할당을 필요로 함
  * 맵
    * 작은 맵을 만들 수 있는 멋진 방법은 따로 없음
  * 자바 9에서는 작은 리스트, 집합, 맵을 쉽게 만들 수 있도록 팩토리 메서드를 제공하기 때문

* 컬렉션 리터럴
  * 파이썬, 그루비 등을 포함한 일부 언어는 컬렉션 리터럴 즉 [42, 1, 5] 같은 특별한 문법을 이용해 컬렉션을 만들 수 있는 기능을 지원함
  * 자바에서는 너무 큰 언어 변화와 관련된 비용이 든다는 이유로 이와 같은 기능을 지원하지 못했음
  * 자바 9에서는 대신 컬렉션 API를 개선했음
  
* 리스트 팩토리
  * List.of 팩토리 메서드를 이용
    ```
    List<String> friends = List.of("Raphael", "Olivia", "Thibaut");
    System.out.println(friends);
    
    // UnsupportedOperationException 예외 발생
    friends.add("Chih-Chun");
    ```
    * 변경할 수 없는 리스트이기 때문
    * set() 메서드로 아이템을 바꾸려해도 비슷한 예외 발생 (set 메서드로도 리스트를 바꿀 수 없음)
    * 이런 제약은 컬렉션이 의도치 않게 변하는 것을 막아줌
      * 하지만 요소 자체가 변하는 것을 막을 수 있는 방법은 없음
    * 리스트를 바꿔야 하는 상황이라면 직접 리스트를 생성
    * null 요소는 금지하므로 의도치 않은 버그를 방지하고 조금 더 간결한 내부 구현을 달성했음
  * 새로운 컬렉션 팩토리 메서드 대신 스트림 API를 사용해 리스트를 만들어야 하는지
    * 데이터 처리 형식을 설정하거나 데이터를 변환할 필요가 없다면 팩토리 메서드 이용을 권장함

* 오버로딩 vs 가변 인수
  * List 인터페이스에는 List.of의 다양한 오버로드 버전이 있음
    ```
    static <E> List<E> of(E e1, E e2, E e3, E e4)
    static <E> List<E> of(E e1, E e2, E e3, E e4, E e5)
    ```
  * 왜 아래와 같이 다중 요소를 받을 수 있도록 자바 API를 만들지 않은 것인지
    ```
    static <E> List<E> of(E... elements)
    ```
  * 내부적으로 가변 인수 버전은 추가 배열을 할당해서 리스트로 감쌈
    * 따라서 배열을 할당하고 초기화하며 나중에 가비지 컬렉션을 하는 비용을 지불해야 함
    * 고정된 숫자의 요소(최대 10개까지)를 API로 정의하므로 이런 비용을 제거할 수 있음
      * List.of로 열 개 이상의 요소를 가진 리스트를 만들 수도 있지만 이 때는 가변 인수를 이용하는 메서드가 사용됨
    * Set.of와 Map.of에서도 이와 같은 패턴이 등장함을 확인할 수 있음

* 집합 팩토리
  * List.of와 비슷한 방법으로 바꿀 수 없는 집합을 만들 수 있음
    ```
    Set<String> friends = Set.of("Raphael", "Olivia", "Thibaut");
    System.out.println(friends);
    
    // 중복된 요소를 제공해서 만들려고 하면 IllegalArgumentException 발생
    Set<String> friends = Set.of("Raphael", "Olivia", "Olivia");
    ```

* 맵 팩토리
  * 맵을 만드는 것은 리스트, 집합에 비해 조금 복잡한데 맵을 만드려면 키와 값이 있어야 하기 때문
  * 자바 9에서는 두 가지 방법
    * Map.of 팩토리 메서드에 키와 값을 번갈아 제공하는 방법
      ```
      Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Olivia", 26);
      ```
      * 열 개 이하의 키, 값 쌍을 가진 작은 맵을 만들 때 위 메서드가 유용함
    * Map.ofEntries 팩토리 메서드
      * 그 이상의 맵에서는 Map.Entry<K, V> 객체를 인수로 받으며 가변 인수로 구현된 Map.ofEntries 팩토리 메서드를 이용하는 것이 좋음
      * 이 메서드는 키와 값을 감쌀 추가 객체 할당을 필요로 함
      ```
      // Map.entry는 Map.Entry 객체를 만드는 새로운 팩토리 메서드
      import static java.util.Map.entry;
      
      Map<String, Integer> ageOfFriends = 
            Map.ofEntries(entry("Raphael", 30), entry("Olivia", 25), entry("Olivia", 26));
      System.out.println(ageOfFriends);
      ```

### 리스트와 집합 처리
* 자바 8에서는 List, Set 인터페이스에 다음과 같은 메서드를 추가
  * removeIf
    * 프레디케이트를 만족하는 요소를 제거
    * List, Set을 구현하거나 그 구현을 상속받은 모든 클래스에서 이용할 수 있음
  * replaceAll
    * 리스트에서 이용할 수 있는 기능으로 UnaryOperator 함수를 이용해 요소를 바꿈
  * sort
    * List 인터페이스에서 제공하는 기능으로 리스트를 정렬함
* 위 메서드는 호출한 컬렉션 자체를 바꿈
  * 새로운 결과를 만드는 스트림과는 달리 이들 메서드는 기존 컬렉션을 바꿈

* removeIf 메서드
  * 숫자로 시작되는 참조 코드를 가진 트랜잭션을 삭제하는 코드
    ```
    for (Transaction transaction : transactions) {
        if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
            transactions.remove(transaction);
        }
    }
    ```
    * 위 코드에 문제는 ConcurrentModificationException을 발생시킴
      * 내부적으로 for-each 루프는 Iterator 객체를 사용하므로 아래와 같이 해석됨
        ```
        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
            Transaction transaction = iterator.next();
            if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
                // 반복하면서 별도의 두 객체를 통해 컬렉션을 바꾸고 있는 문제
                transactions.remove(transaction);
            }
        }
        ```
        * 두 개의 개별 객체가 컬렉션을 관리함
          * Iterator 객체 : next(), hasNext()를 이용해 소스를 질의함
          * Collection 객체 자체 : remove()를 호출해 요소를 삭제함
        * 결과적으로 반복자의 상태는 컬렉션의 상태와 서로 동기화되지 않음
          * Iterator 객체를 명시적으로 사용하고 그 객체의 remove() 메서드를 호출함으로 이 문제를 해결할 수 있음
            ```
            for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
                Transaction transaction = iterator.next();
                if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
                    iterator.remove();
                }
            }
            ```
          * 이 코드 패턴은 removeIf 메서드로 바꿀 수 있음
          * removeIf 메서드는 삭제할 요소를 가리키는 프레디케이트를 인수로 받음
            ```
            transactions.removeIf(transaction -> Character.isDigit(transaction.getReferenceCode().charAt(0)));
            ```

* replaceAll 메서드
  * List 인터페이스의 replaceAll 메서드를 이용해 리스트의 각 요소를 새로운 요소로 바꿀 수 있음
  * 스트림 API를 사용하면 다음처럼 문제 해결 가능
    ```
    referenceCodes.stream()
            .map(code -> Character.toUpperCase(code.charAt(0)) + code.subString(1))
            .collect(Collectors.toList())
            .forEach(System.out::println);
    ```
    * 하지만 이 코드는 새 문자열 컬렉션을 만듦
    * 기존 컬렉션을 바꾸는 것을 원함
  * ListIterator 객체(요소를 바꾸는 set() 메서드를 지원)를 이용
    ```
    for (ListIterator<String> iterator = referenceCodes.listIterator(); iterator.hasNext();) {
        String code = iterator.next();
        iterator.set(Character.toUpperCase(code.charAt(0)) + code.substring(1));
    }
    ```
    * 컬렉션 객체를 Iterator 객체와 혼용하면 반복과 컬렉션 변경이 동시에 이루어지면서 쉽게 문제를 일으킴
  * 자바 8 기능 이용
    ```
    referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));
    ```

### 맵 처리
* 자바 8에서는 Map 인터페이스에 몇 가지 디폴트 메서드를 추가함

* forEach 메서드
  * Map.Entry<K, V>의 반복자를 이용해 맵의 항목 집합 반복
    ```
    for (Map.Entry<String, Integer> entry : ageOfFriends.entrySet()) {
        String friend = entry.getKey();
        Integer age = entry.getValue();
        System.out.println(friend + " is " + age + " years old");
    }
    ```
  * 자바 8에서부터 Map 인터페이스는 BiConsumer(키와 값을 인수로 받음)를 인수로 받는 forEach 메서드를 지원
    ```
    ageOfFriends.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));
    ```

* 정렬 메서드
  * 정렬은 반복과 관련한 오랜 고민거리
  * 두 개의 새로운 유틸리티를 이용하면 맵의 항목을 값 또는 키를 기준으로 정렬할 수 있음
    * Entry.comparingByValue
    * Entry.comparingByKey
    ```
    Map<String, String> favouriteMovies = Map.ofEntries(
            entry("Raphael", "Star Wars"),
            entry("Cristina", "Matrix"),
            entry("Olivia", "James Bond"));

    favouriteMovies.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEachOrdered(System.out::println); // 사람의 이름을 알파벳 순으로 스트림 요소를 처리함
    ```

* HashMap 성능
  * 기존에 맵의 항목은 키로 생성한 해시코드로 접근할 수 있는 버킷에 저장했음
  * 많은 키가 같은 해시코드를 반환하는 상황이 되면 O(n)의 시간이 걸리는 LinkedList로 버킷을 반환해야 하므로 성능이 저하됨
  * 최근에는 버킷이 너무 커질 경우 이를 O(log(n))의 시간이 소요되는 정렬된 트리를 이용해 동적으로 치환해 충돌이 일어나는 요소 반환 성능을 개선함
  * 하지만 키가 String, Number 클래스 같은 Comparable의 형태여야만 정렬된 트리가 지원됨

* getOrDefault 메서드
  * 기존에는 찾으려는 키가 존재하지 않으면 null이 반환되므로 NullPointerException을 방지하려면 요청 결과가 null인지 확인해야 함
  * 기본값을 반환하는 방식으로 이 문제를 해결할 수 있음
  * getOrDefault 메서드를 이용하여 문제 해결
    * 이 메서드는 첫 번째 인수로 키를, 두 번째 인수로 기본값을 받으며 맵에 키가 존재하지 않으면 두 번째 인수로 받은 기본값을 반환함
      ```
      Map<String, String> favouriteMovies = Map.ofEntries(
            entry("Raphael", "Star Wars"),
            entry("Cristina", "Matrix"),
            entry("Olivia", "James Bond"));
      
      System.out.println(favouriteMovies.getOrDefault("Olivia", "Matrix"));  // James Bond 출력
      System.out.println(favouriteMovies.getOrDefault("Thibaut", "Matrix")); // Matrix 출력
      ```
    * 키가 존재하더라도 값이 null인 상황에서는 getOrDefault가 null을 반환함
    * 키가 존재하느냐의 여부에 따라서 두 번째 인수가 반환될지 결정됨
 
* 계산 패턴
  * 자바 8에서는 키의 값이 존재하는지 여부를 확인할 수 있는 복잡한 몇 개의 패턴 제공
  * 맵에 키가 존재하는지 여부에 따라 어떤 동작을 실행하고 결과를 저정해야 하는 상황이 필요한 때가 있음
    * 예를 들어 키를 이용해 값비싼 동작을 실행해서 얻은 결과를 캐시하려 함
    * 키가 존재하면 결과를 다시 계산할 필요가 없음
    * 다음 연산이 이런 상황에 도움을 줌
      * computeIfAbsent
        * 제공된 키에 해당하는 값이 없으면(값이 없거나 null) 키를 이용해 새 값을 계산하고 맵에 추가함
      * computeIfPresent
        * 제공된 키가 존재하면 새 값을 계산하고 맵에 추가함
      * compute
        * 제공된 키로 새 값을 계산하고 맵에 저장함
  * 정보를 캐시할 때 computeIfAbsent 활용 가능
    * 파일 집합의 각 행을 파싱해 SHA-256을 계산한다고 가정
    * 기존에 이미 데이터를 처리했다면 이 값을 다시 계산할 필요가 없음
    * 맵을 이용해 캐시를 구현했다고 가정하면 다음처럼 MessageDigest 인스턴스로 SHA-256 해시를 계산할 수 있음
      ```
      Map<String, byte[]> dataToHash = new HashMap<>();
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      ```
      * 데이터를 반복하면서 결과를 캐시
        ```
        lines.forEach(line ->
              dataToHash.computeIfAbsent(line, // line은 맵에서 찾을 키
                      this::calculateDigest)); // 키가 존재하지 않으면 동작 실행
        
        // calculateDigest 메서드
        private byte[] calculateDigest(String key) {
            return messageDigest.digest(key.getBytes(StandardCharsets.UTF_8));
        }
        ```
    * Raphael에게 줄 영화 목록을 만든다고 가정
      ```
      String friend = "Raphael";
      List<String> movies = friendsToMovies.get(friend);
      if (movies == null) {
          movies = new ArrayList<>();
          friendsToMovies.put(friend, movies);
      }
      movies.add("Star Wars"); // 영화 추가
      System.out.println(friendsToMovies); // {Raphael:[Star Wars]} 출력
      ```
      * computeIfAbsent는 키가 존재하지 않으면 값을 계산해 맵에 추가하고 키가 존재하면 기존 값을 반환함
      * 이를 이용해 다음처럼 코드 구현
        ```
        friendsToMovies.computeIfAbsent("Raphael", name -> new ArrayList<>())
                .add("Star Wars");
        ```
  * computeIfPresent 메서드는 현재 키와 관련된 값이 맵에 존재하며 null이 아닐 때만 새 값을 계산함
    * 이 메서드의 미묘한 실행 과정 주목
    * 값을 만드는 함수가 null을 반환하면 현재 매핑을 맵에서 제거함
    * 하지만 매핑을 제거할 때는 remove 메서드를 오버라이드하는 것이 더 적합함

* 삭제 패턴
  * 제공된 키에 해당하는 맵 항목을 제거하는 remove 메서드
  * 자바 8에서는 키가 특정한 값과 연관되었을 때만 항목을 제거하는 오버로드 버전 메서드 제공
  * 기존에는 다음처럼 코드를 구현함
    ```
    String key = "Raphael";
    String value = "Jack Reacher 2";
    
    if (favouriteMovies.containsKey(key) && Objects.equals(favouriteMovies.get(key), value)) {
        favouriteMovies.remove(key);
        return true;
    }
    return false;
    ```
  * remove 메서드
    ```
    favouriteMovies.remove(key, value);
    ```

* 교체 패턴
  * 맵의 항목을 바꾸는 데 사용할 수 있는 두 개의 메서드가 맵에 추가됨
    * replaceAll
      * BiFunction을 적용한 결과로 각 항목의 값을 교체함
      * List의 replaceAll과 비슷한 동작 수행
      ```
      Map<String, String> favouriteMovies = new HashMap<>();
      favouriteMovies.put("Raphael", "Star Wars");
      favouriteMovies.put("Olivia", "james bond");

      favouriteMovies.replaceAll((friend, movie) -> movie.toUpperCase());
      System.out.println(favouriteMovies);
      ```
    * Replace
      * 키가 존재하면 맵의 값을 바꿈
      * 키가 특정 값으로 매핑되었을 때만 값을 교체하는 오버로드 버전도 있음

* 합침
  * 두 개의 맵에서 값을 합치거나 바꿀 때
  * 두 그룹의 연락처를 포함하는 두 개의 맵을 합친다고 가정, putAll 사용
    ```
    Map<String, String> family = Map.ofEntries(
            entry("Teo", "Star Wars"), entry("Cristina", "James Bond"));
    Map<String, String> friends = Map.ofEntries(entry("Raphael", "Star Wars"));

    Map<String, String> everyone = new HashMap<>(family);
    everyone.putAll(friends);
    System.out.println(everyone);
    ```
    * 중복된 키가 없다면 위 코드는 잘 동작함
    * 값을 좀 더 유연하게 합칠 때 merge 메서드 이용
      * 이 메서드는 중복된 키를 어떻게 합칠지 결정하는 BiFunction()을 인수로 받음
      * family와 friends 두 맵 모두에 Cristina가 다른 영화 값으로 존재한다고 가정
        ```
        Map<String, String> family = Map.ofEntries(
                entry("Teo", "Star Wars"), entry("Cristina", "James Bond"));
		Map<String, String> friends = Map.ofEntries(
				entry("Raphael", "Star Wars"), entry("Cristina", "Matrix"));
        ```
      * forEach와 merge 메서드를 이용해 충돌 해결 가능
        ```
		Map<String, String> everyone = new HashMap<>(family);
		friends.forEach((k, v) -> everyone.merge(k, v, 
                (movie1, movie2) -> movie1 + " & " + movie2)); // 중복된 키가 있으면 두 값을 연결
		System.out.println(everyone);
        ```
      * 자바독에서 설명하는 것처럼 merge 메서드는 null값과 관련된 복잡한 상황도 처리함
        * 지정된 키와 연관된 값이 없거나 null이면 [merge]는 키를 null이 아닌 값과 연결함
        * 아니면 [merge]는 연결된 값을 주어진 매핑 함수의 [결과]값으로 대치하거나 결과가 null이면 [항목]을 제거함
    * merge를 이용해 초기화 검사를 구현
      * 영화를 몇 회 시청했는지 기록하는 맵이 있다고 가정
      * 해당 값을 증가시키기 전에 관련 영화가 이미 맵에 존재하는지 확인해야 함
        ```
        Map<String, Long> moviesToCount = new HashMap<>();
        String movieName = "JamesBond";
        long count = moviesToCount.get(movieName);
        
        if (count == null) {
            movieToCount.put(movieName, 1);
        } else {
            movieToCount.put(movieName, count + 1);
        }
        ```
        * 위 코드 변환
          ``` moviesToCount.merge(movieName, 1L, (key, count)-> count + 1L); ```
          * 위 코드에서 merge의 두 번째 인수는 1L
          * 자바독에 따르면 이 인수는
            * "키와 연관된 기존 값에 합쳐질 null이 아닌 값 또는 값이 없거나 키에 null값이 연관되어 있다면 이 값을 키와 연결" 하는데 사용됨
          * 키의 반환값이 null이므로 처음에는 1이 사용됨
          * 그 다음부터는 값이 1로 초기화되어 있으므로 BiFunction을 적용해 값이 증가됨

### 개선된 ConcurrentHashMap
* ConcurrentHashMap 클래스는 동시성 친화적이며 최신 기술을 반영한 HashMap 버전
  * 내부 자료구조의 특정 부분만 잠궈 동시 추가, 갱신 작업을 허용함
  * 따라서 동기화된 HashTable 버전에 비해 읽기 쓰기 연산 성능이 월등함 (표준 HashMap은 비동기로 동작함)
* 리듀스와 검색
  * forEach
    * 각 (키, 값) 쌍에 주어진 액션을 수행 
  * reduce
    * 모든 (키, 값) 쌍을 제공된 리듀스 함수를 이용해 결과로 합침
  * search
    * null이 아닌 값을 반환할 때까지 각 (키, 값) 쌍에 함수를 적용
  * 키에 함수 받기, 값, Map.Entry, (키, 값) 인수를 이용한 네 가지 연산 형태를 지원함
    * 키, 값으로 연산
      * forEach, reduce, search
    * 키로 연산
      * forEachKey, reduceKeys, searchKeys
    * 값으로 연산
      * forEachValue, reduceValues, searchValues
    * Map.Entry 객체로 연산
      * forEachEntry, reduceEntries, searchEntries
    * 이 연산들은 ConcurrentHashMap의 상태를 잠그지 않고 연산을 수행함
      * 따라서 이들 연산에 제공한 함수는 게산이 진행되는 동안 바뀔 수 있는 객체, 값, 순서 등에 의존하지 않아야 함
      * 또한 이들 연산에 병렬성 기준값(threshold)을 지정해야 함
      * 맵의 크기가 주어진 기준값보다 작으면 순차적으로 연산을 실행함
      * 기준값을 1로 지정하면 공통 스레드 풀을 이용해 병렬성을 극대화 함
      * Long.MAX_VALUE를 기준값으로 설정하면 한 개의 스레드로 연산을 실행함
      * 소프트웨어 아키텍처가 고급 수준의 자원 활용 최적화를 사용하고 있지 않다면 기준값 규칙을 따르는 것이 좋음
      * reduceValues 메서드를 이용해 맵의 최댓값을 찾는 예제
        ```
        // 여러 키와 값을 포함하도록 갱신될 ConcurrentHashMap
        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
        long parallelismThreshold = 1;
        Optional<Integer> maxValue = 
                Optional.ofNullable(mpa.reduceValues(parallelismThreshold, Long::max));
        ```
      * int, long, double 등의 기본값에는 전용 each reduce 연산이 제공되므로 reduceValuesToInt, reduceKeysToLong 등을 이용할 것
        * 박싱 작업 등을 할 필요가 없고 효율적으로 작업을 처리할 수 있음

* 계수
  * ConcurrentHashMap 클래스는 맵의 매핑 개수를 반환하는 mappingCount 메서드 제공
  * 기존 size 메서드 대신 새 코드에서는 int를 반환하는 mappingCount 메서드를 사용하는 것이 좋음
    * 그래야 매핑의 개수가 int의 범위를 넘어서는 이후의 상황을 대처할 수 있기 때문

* 집합뷰
  * ConcurrentHashMap 클래스는 ConcurrentHashMap을 집합 뷰로 반환하는 keySet 메서드를 제공함
  * 맵을 바꾸면 집합도 바뀌고 반대로 집합을 바꾸면 맵도 영향을 받음
  * newKeySet이라는 새 메서드를 이용해 ConcurrentHashMap으로 유지되는 집합을 만들 수 있음

### 정리
* 자바 9는 적의 원소를 포함하며 바꿀 수 없는 리스트, 집합, 맵을 쉽게 만들 수 있도록 컬렉션 팩토리를 지원함
  * List.of, Set.of, Map.of, Map.ofEntries 등
* 이들 컬렉션 팩토리가 반환한 객체는 만들어진 다음 바꿀 수 없음
* List 인터페이스는 removeIf, replaceAll, sort 세 가지 디폴트 메서드를 지원함
* Set 인터페이스는 removeIf 디폴트 메서드를 지원
* Map 인터페이스는 자주 사용하는 패턴과 버그를 방지할 수 있도록 다양한 디폴트 메서드를 지원함
* ConcurrentHashMap은 Map에서 상속받은 새 디폴트 메서드를 지원함과 동시에 스레드 안정성도 제공함

### [quiz]
* 다음 코드 실행 결과는?
  ```
  List<String> actors = List.of("Keanu", "Jessica");
  actors.set(0, "Brad");
  System.out.println(actors);
  ```
  * 정답
    * UnsupportedOperationException 발생함. List.of로 만든 컬렉션은 바꿀 수 없기 때문

* 다음 코드가 어떤 작업을 수행하는지 파악한 다음 코드를 단순화 할 수 있는 방법 설명하기
  ```
  Map<String, Integer> movies = new HashMap<>();
  movies.put("JamesBond", 20);
  movies.put("Matrix", 15);
  movies.put("Harry Potter", 5);
  Iterator<Map.Entry<String, Integer>> iterator = movies.entrySet().iterator();
  
  while(iterator.haxNext()) {
      Map.Entry<String, Integer> entry = iterator.next();
      if (entry.getValue() < 10) {
          iterator.remove();
      }
  }
  System.out.println(movies);
  ```
  * 정답
    ``` movies.entrySet().removeIf(entry -> entry.getValue() < 10; ```
---

## chapter 09 - 리팩터링, 테스팅, 디버깅
### 가독성과 유연성을 개선하는 리팩토링

* 코드 가독성 개선
  * 일반적으로 코드 가독성이 좋다는 것은 '어떤 코드를 다른 사람도 쉽게 이해할 수 있음'을 의미함
  * 즉, 코드 가독성을 개선한다는 것은 우리가 구현한 코드를 다른 사람이 쉽게 이해하고 유지보수할 수 있게 만드는 것을 의미
  * 코드 가독성을 높이려면 코드의 문서화를 잘하고, 표준 코딩 규칙을 준수하는 등의 노력을 기울여야 함

* 익명 클래스를 람다 표현식으로 리팩터링하기
  * 익명 클래스는 코드를 장황하게 만들고 쉽게 에러를 일으킴
  * 예를 들어 아래는 Runnable 객체를 만드는 익명 클래스, 람다 표현식
    ```
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            System.out.println("Hello1");
        }
    };

    Runnable r2 = () -> System.out.println("Hello2");
    ```
  * 하지만 모든 익명 클래스를 람다 표현식으로 변환할 수 없음
    * 첫째, 익명 클래스에서 사용한 this와 super는 람다 표현식에서 다른 의미를 가짐
      * 익명 클래스에서 this는 익명 클래스 자신을 가리키지만 람다에서 this는 람다를 감싸는 클래스를 가리킴
    * 둘째, 익명 클래스는 감싸고 있는 클래스의 변수를 가릴 수 있음(shadow variable)
      * 하지만 람다 표현식으로는 변수를 가릴 수 없음
      * 아래 코드는 컴파일되지 않음
        ```
        int a = 10;
        Runnable r1 = () -> {
            int a = 2; // 컴파일 에러
            System.out.println(a);
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                int a = 2; // 작동
                System.out.println(a);
            }
        };
        ```
    * 마지막으로 익명 클래스를 람다 표현식으로 바꾸면 컨텍스트 오버로딩에 따른 모호함이 초래될 수 있음
      * 익명 클래스는 인스턴스화할 때 명시적으로 형식이 정해지는 반면 람다의 형식은 컨텍스트에 따라 달라짐
      * 아래 예제에서는 Task라는 Runnable과 같은 시그니처를 갖는 함수형 인터페이스를 선언
        ```
        interface Task {
            public void execute();
        }
    
        public static void doSomething(Runnable r) {r.run();}
        public static void doSomething(Task t) {t.execute();}
        
        // Task를 구현하는 익명 클래스 전달
        doSomething(new Task() {
            @Override
            public void execute() {
                System.out.println("Danger danger!!");
            }
        });

        // doSomething(Runnable), doSomething(Task) 메서드 모두 대상 형식이 되서 문제
		doSomething(() -> System.out.println("Danger"));

        // 명시적 해결
        doSomething((Task)() -> System.out.println("Danger"));
        ```

* 람다 표현식을 메서드 참조로 리팩터링하기
  * 람다 표현식 대신 메서드 참조를 이용하면 가독성을 높일 수 있음
  * 메서드 참조의 메서드명으로 코드의 의도를 명확하게 알릴 수 있기 때문
    * 예를 들어 칼로리 수준으로 요리를 그룹화 하는 코드
      ```
	  Map<CaloricLevel, List<Dish>> dishesByCaloricLevel =
	          menu.stream()
                    .collect(groupingBy(dish -> {
                        if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                        else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                        else return CaloricLevel.FAT;
                    }));
      ```
      ```
      Map<CaloricLevel, List<Dish>> dishesByCaloricLevel =
              menu.stream().collect(groupingBy(Dish::getCaloricLevel));
      
      public class Dish {
          public CaloricLevel getCaloricLevel() {
              if (getCalories() <= 400) return CaloricLevel.DIET;
              else if (getCalories() <= 700) return CaloricLevel.NORMAL;
              else return CaloricLevel.FAT;
          }
      }
      ```
    * comparing, maxBy 같은 정적 헬퍼 메서드를 활용하는 것도 좋음. 이들은 메서드 참조화 조화를 이루도록 설계되었음
    * 3장 예제
      ```
      inventory.sort(
            (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight())); // 비교 구현에 신경 써야 함
      inventory.sort(comparing(Apple::getWeight)); // 코드가 문제 자체를 설명함
      ```
    * sum, maximum 등 자주 사용하는 리듀싱 연산은 메서드 참조와 함께 사용할 수 있는 내장 헬퍼 메서드를 제공
      * 최댓값이나 합계를 계산할 때 람다 표현식과 저수준 리듀싱 연산을 조합하는 것보다 Collectors API를 사용하면 코드의 의도가 더 명확해짐
      * 저수준 리듀싱 연산 조합 코드
        ```
        int totalCalories = menu.stream().map(Dish::getCalories).reduce(0, (c1, c2) -> c1 + c2);
        ```
      * 내장 컬렉터를 이용하면 코드 자체로 문제를 더 명확하게 설명 가능
      * summingInt 사용
        ``` int totalCalories = menu.stream().collect(summingInt(Dish::getCalories)); ```

* 명령형 데이터 처리를 스트림으로 리팩터링하기
  * 이론적으로는 반복자를 이용한 기존의 모든 컬렉션 처리 코드를 스트림 API로 바꿔야 함
    * 스트림 API는 데이터 처리 파이프라인의 의도를 더 명확하게 보여주기 때문
    * 스트림은 쇼트서킷과 게으름이라는 강력한 최적화뿐 아니라 멀티코어 아키텍처를 활용할 수 있는 지름길을 제공함
  * 다음 명령형 코드는 두 가지 패턴으로 엉킨 코드
    ```
    List<String> dishNames = new ArrayList<>();
    for (Dish dish : menu) {
        if (dish.getCalories() > 300) {
            dishNames.add(dish.getName());
        }
    }
    ```
    * 스트림 API 사용
      ```
      menu.parallelStream().filter(d -> d.getCalories() > 300)
            .map(Dish::getName)
            .collect(toList());
      ```
  * 명령형 코드의 break, continue, return 등 제어 흐름문을 분석
  * 같은 기능을 수행하는 스트림 연산으로 유추해야 하므로 명령형 코드를 스트림 API로 바꾸는 것은 쉬운 일이 아님
  * 명령형 코드를 스트림 API로 바꾸도록 도움을 주는 몇 가지 도구
    * 접속 안됨 ~~http://goo.gl/Ma15w9(http://refactoring.info/tools/LambdaFicator)~~

* 코드 유연성 개선
  * 함수형 인터페이스 적용
    * 람다 표현식을 이용하려면 함수형 인터페이스 필요
    * 함수형 인터페이스를 코드에 추가해야 함
    * 조건부 연기 실행(conditional deferred execution)과 실행 어라운드(execute around) 두 가지 자주 사용하는 패턴으로 람다 표현식 리팩터링
  * 조건부 연기 실행(conditional deferred execution)
    * 실제 작업을 처리하는 코드 내부에 제어 흐름문이 복잡하게 얽힌 코드를 흔히 볼 수 있음
      * 흔히 보안 검사나 로깅 관련 코드
    * 다음은 내장 자바 Logger 클래스 예제
      ```
      if (logger.isLoggable(Log.FINER)) {
          logger.finder("problem: " + generateDiagnostic());
      }
      ```
      * 위 코드에 문제
        * logger의 상태가 isLoggable이라는 메서드에 의해 클라이언트 코드로 노출됨
        * 메시지를 로깅할 때마다 logger 객체의 상태를 매번 확인해야 할까?, 이들은 코드를 어지럽힐 뿐
      * 다음처럼 메시지를 로깅하기 전에 logger 객체가 적절한 수준으로 설정되었는지 내부적으로 확인하는 log 메서드를 사용하는 것이 바람직
        ``` logger.log(Level.FINER, "Problem: " + generateDiagnostic()); ```
        * 불필요한 if문을 제거할 수 있으며, logger의 상태를 노출할 필요도 없음
        * 하지만 인수로 전달된 메시지 수준에서 logger가 활성화되어 있지 않더라도 항상 로깅 메시지를 평가하게 되는 문제
      * 람다를 이용하여 해결
        * 특정 조건에서만 메시지가 생성될 수 있도록 메시지 생성 과정을 연기할 수 있어야 함
        * 자바 8 API 설계자는 이와 같은 logger 문제를 해결할 수 있도록 Supplier 인수로 갖는 오버로드된 log 메서드를 제공함
        * 새로 추가된 log 메서드 시그니처
          ``` public void log(Level level, Supplier<String> msgSupplier) ```
        * log 메서드 호출
          ``` logger.log(Level.FINER, () -> "Problem: " + generateDiagnostic()); ```
          * log 메서드는 logger의 수준이 적절하게 설정되어 있을 때만 인수로 넘겨진 람다를 내부적으로 실행함
          * log 메서드의 내부 구현 코드
            ```
            public void log(Level level, Suppler<String> msgSupplier) {
                if (logger.isLoggable(level)) {
                    log(level, msgSupplier.get()); // 람다 실행
                }
            }
            ```
      * 만일 클라이언트 코드에서 객체 상태를 자주 확인하거나(예를 들면 logger의 상태)
      * 객체의 일부 메서드를 호출하는 상황(예를 들면 메시지 로깅)이라면 
      * 내부적으로 객체의 상태를 확인한 다음에 메서드를 호출(람다나 메서드 참조를 인수로 사용)하도록 새로운 메서드를 구현하는 것이 좋음
      * 그러면 코드 가독성이 좋아질 뿐 아니라 캡슐화도 강회됨 (객체 상태가 클라이언트 코드로 노출되지 않음)
  * 실행 어라운드(execute around)
    * 매번 같은 준비, 종료 과정을 반복적으로 수행하는 코드가 있다면 이를 람다로 변환할 수 있음
    * 준비, 종료 과정을 처리하는 로직을 재사용함으로써 코드 중복을 줄일 수 있음
    * 다음은 3장 코드
      ```
      String oneLine = processFile((BufferedReader b) -> b.readLine()); // 람다 전달
      String twoLines = processFile((BufferedReader b) -> b.readLine() + b.readLine()); // 다른 람다 전달
      
      // IOException을 던질 수 있는 람다의 함수형 인터페이스
      public static String processFile(BufferedReaderProcessor p) throws IOException {
          try (BufferedReader br = new BufferedReader(new FileReader("com/jaenyeong/chapter_09/data.txt"))) {
              return p.process(br); // 인수로 전달된 BufferedReaderProcessor를 실행
          }
      }
      
      @FunctionalInterface
      public interface BufferedReaderProcessor {
          String process(BufferedReader b) throws IOException;
      }
      ```
      * 람다로 BufferedReader 객체의 동작을 결졍할 수 있는 것은 함수형 인터페이스 BufferedReaderProcessor 덕분

### 람다로 객체지향 디자인 패턴 리팩터링하기
* 언어에 새로운 기능이 추가되면서 기존 코드 패턴이나 관용코드의 인기가 식기도 함
  * 예를 들어 자바 5에서 추가된 for-each 루프는 에러 발생률이 적으며 간결하므로 기존의 반복자 코드를 대체함
  * 자바 7에 추가된 다이아몬드 연산자<> 때문에 기존의 제네릭 인스턴스를 명시적으로 생성하는 빈도가 줄었음
* 다양한 패턴을 유형별로 정리한 것이 디자인 패턴
  * 디자인 패턴은 공통적인 소프트웨어 문제를 설계할 때 재 사용할 수 있는, 검증된 청사진을 제공함
  * 재사용할 수 있는 부품으로 여러 가지 다리를 건설하는 엔지니어링에 비유할 수 있음
  * 예를 들어 구조체와 동작하는 알고리즘을 서로 분리하고 싶을 때 방문자 디자인 패턴(visitor design pattern)을 사용할 수 있음
  * 또 싱글턴 패턴(singleton pattern)을 이용해서 클래스 인스턴스화를 하나의 객체로 제한할 수 있음

* 전략
  * 전략 패턴은 한 유형의 알고리즘을 보유한 상태에서 런타임에 적절한 알고리즘을 선택하는 기법
  * 다양한 기준을 갖는 입력값을 검증하거나, 다양한 파싱 방법을 사용하거나, 입력 형식을 설정하는 등 다양한 시나리오에 전략 패턴을 활용할 수 있음
  * 전략 패턴은 세 부분으로 구성됨
    * 클라이언트
      * 전략 객체를 사용하는 한 개 이상의 클라이언트
    * 전략 - execute()
      * 알고리즘을 나타내는 인터페이스(Strategy 인터페이스)
    * ConcreateStrategyA, ConcreateStrategyB
      * 다양한 알고리즘을 나타내는 한 개 이상의 인터페이스 구현(구체적인 구현 클래스)
  * 예를 들어 오직 소문자 또는 숫자로 이루어져야 하는 등 텍스트 입력이 다양한 조건에 맞게 포맷 되어 있는지 검증한다고 가정
    * 먼저 String 문자열을 검증하는 인터페이스부터 구현
      ```
      public interface ValidationStrategy {
          boolean execute(String s);
      }
      ```
    * 위 인터페이스를 구현하는 클래스를 하나 이상 정의
      ```
      public class IsAllLowerCase implements ValidationStrategy {
      
          @Override
          public boolean execute(String s) {
              return s.matches("[a-z]+");
          }
      }
      
      public class IsNumeric implements ValidationStrategy {
      
          @Override
          public boolean execute(String s) {
              return s.matches("\\d+");
          }
      }
      ```
    * 다양한 검증 전략으로 활용
      ```
      public class Validator {
          private final ValidationStrategy strategy;
      
          public Validator(ValidationStrategy strategy) {
              this.strategy = strategy;
          }
      
          public boolean validate(String s) {
              return strategy.execute(s);
          }
      }
      ```
    * 사용
      ```
      Validator numericValidator = new Validator(new IsNumeric());
      boolean b1 = numericValidator.validate("aaaa"); // false 반환

      Validator lowerCaseValidator = new Validator(new IsAllLowerCase());
      boolean b2 = lowerCaseValidator.validate("bbbb"); // true 반환
      ```
    * 람다 표현식 사용
      * ValidationStrategy는 함수형 인터페이스며 Predicate\<String>과 같은 함수 디스크립터를 갖고 있음
      * 따라서 다양한 전략을 구현하는 새로운 클래스를 구현할 필요 없이 람다 표현식을 직접 전달하면 코드가 간결해짐
        ```
        Validator numericValidator = new Validator(s -> s.matches("[a-z]+"));
        boolean b1 = numericValidator.validate("aaaa");

        Validator lowerCaseValidator = new Validator(s -> s.matches("\\d+"));
        boolean b2 = lowerCaseValidator.validate("bbbb");
        ```
    * 람다 표현식을 이용하면 전략 디자인 패턴에서 발생하는 자잘한 코드를 제거할 수 있음
      * 람다 표현식은 코드 조각(또는 전략)을 캡슐화 함
      * 람다 표현식으로 디자인 패턴을 대신할 수 있음
    
* 템플릿 메서드
  * 알고리즘의 개요를 제시한 다음에 알고리즘의 일부를 고칠 수 있는 유연함을 제공해야 할 때 사용
  * 다시 말해 '이 알고리즘을 사용하고 싶은데 그대로는 안되고 조금 고쳐야 하는' 상황에 적합
  * 온라인 뱅킹 앱 구현한다고 가정
    * 사용자가 고객 ID를 앱에 입력하면 은행 데이터베이스에서 고객 정보를 가져오고 고객이 원하는 서비스를 제공할 수 있음
    * 예를 들어 고객 계좌에 보너스를 입금한다고 가정
    * 은행마다 다양한 온라인 뱅킹 앱을 사용하며 동작 방법도 다름
    * 온라인 뱅킹 앱 동작을 정의하는 추상 클래스
      ```
	  abstract class OnlineBanking {

	      public void processCustomer(int id) {
              Customer c = Database.getCustomerWithId(id);
              makeCustomerHappy(c);
          }

	      abstract void makeCustomerHappy(Customer c);
	  }
      ```
      * processCustomer 메서드는 온라인 뱅킹 알고리즘이 해야 할 일을 보여줌
        * 우선 주어진 고객 ID를 이용해 고객을 만족시켜야 함
        * 각각의 지점은 OnlineBanking 클래스를 상속받아 makeCustomerHappy 메서드가 원하는 동작을 수행하도록 구현할 수 있음
  * 람다 표현식 사용
    * 이전에 정의한 makeCustomerHappy의 메서드 시그니처와 일치하도록 Consumer\<Customer> 형식을 갖는 두 번째 인수를 processCustomer에 추가
      ```
	  public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
	      Customer c = Database.getCustomerWithId(id);
	      makeCustomerHappy.accept(c);
      }
      ```
    * 사용
      ```
      new TemplateMethodRefactor().processCustomer(1337, (Customer c) -> System.out.println("Hello!"));
      ```

* 옵저버
  * 어떤 이벤트가 발생했을 때 한 객체(주제라 불리는, subject)가 다른 객체 리스트(옵저버, observer)에 자동으로 알림을 보내야 하는 상황에 사용
  * GUI 앱에서 옵저버 패턴이 자주 사용됨
  * 버튼 같은 GUI 컴포넌트에 옵저버를 설정할 수 있음
  * 그리고 사용자가 버튼을 클릭하면 옵저버에 알림이 전달되고 정해진 동작이 수행됨
  * 꼭 GUI에서만 옵저버 패턴을 사용하는 것이 아님
  * 예를 들어 주식의 가격(주제) 변동에 반응하는 다수의 거래자(옵저버) 예제에서도 옵저버 패턴을 사용할 수 있음
    * 옵저버
      * 주제
        * notifyObserver()
      * 옵저버
        * notify()
      * ConcreateObserverA, ConcreateObserverB
  * 옵저버 패턴으로 트위터 같은 커스터마이즈된 알림 시스템을 설계, 구현할 수 있음
    * 다양한 옵저버를 그룹화할 Observer 인터페이스 필요
      * Observer 인터페이스는 새로운 트윗이 있을 때 주제가 호출될 수 있도록 notify라고 하는 하나의 메서드를 제공함
        ```
        interface Observer {
            void notify(String tweet);
        }
        ```
      * 여러 옵저버 정의
        ```
        class NYTimes implements Observer {
    
            @Override
            public void notify(String tweet) {
                if (tweet != null && tweet.contains("money")) {
                    System.out.println("Breaking news in NY! " + tweet);
                }
            }
        }
    
        class Guardian implements Observer {
    
            @Override
            public void notify(String tweet) {
                if (tweet != null && tweet.contains("queen")) {
                    System.out.println("Yet more news from London... " + tweet);
                }
            }
        }
    
        class LeMonde implements Observer {
    
            @Override
            public void notify(String tweet) {
                if (tweet != null && tweet.contains("wine")) {
                    System.out.println("Today cheese, wine and news! " + tweet);
                }
            }
        }
        ```
      * 주제 구현
        * Subject 인터페이스 구현
          ```
          interface Subject {
              void registerObserver(Observer o);
              void notifyObservers(String tweet);
          }
          ```
        * 주제는 registerObserver 메서드로 새로운 옵저버를 등록한 다음에 notifyObservers 메서드로 트윗의 옵저버에 이를 알림
          ```
          class Feed implements Subject {
              private final List<Observer> observers = new ArrayList<>();
          
              @Override
              public void registerObserver(Observer o) {
                  observers.add(o);
              }
          
              @Override
              public void notifyObservers(String tweet) {
                  observers.forEach(o -> o.notify(tweet));
              }
          }
          ```
  * 람다 표현식 사용
    * 여기서 Observer 인터페이스를 구현하는 모든 클래스는 하나의 메서드 notify를 구현했음
      * 즉 트윗이 도착했을 때 어떤 동작을 수행할 것인지 감사는 코드를 구현한 것
      * 세 개의 옵저버를 명시적으로 인스턴스화하지 않고 람다 표현식을 직접 전달해서 실행할 동작을 지정할 수 있음
      ```
      f.registerObserver((String tweet) -> {
          if (tweet != null && tweet.contains("money")) {
              System.out.println("Breaking news in NY! " + tweet);
          }
      });

      f.registerObserver((String tweet) -> {
          if (tweet != null && tweet.contains("queen")) {
              System.out.println("Yet more news from London... " + tweet);
          }
      });
      ```
    * 옵저버가 상태를 가지며, 여러 메서드를 정의하는 등 복잡하다면 람다 표현식보다 기존의 클래스 구현방식을 고수하는 것이 바람직할 수도 있음

* 의무 체인
  * 작업 처리 객체의 체인(동작 체인 등)을 만들 때는 의무 체인 패턴을 사용함
  * 한 객체가 어떤 작업을 처리한 다음에 다른 객체로 결과를 전달하고, 다른 객체도 해야 할 작업을 처리한 다음에 또 다른 객체로 전달하는 식
  * 일반적으로 다음으로 처리할 객체 정보를 유지하는 필드를 포함하는 작업 처리 추상 클래스로 의무 체인 패턴을 구성함
    * 작업 처리 객체가 자신의 작업을 끝냈으면 다음 작업 처리 객체로 결과를 전달함
      ```
      public abstract class ProcessingObject<T> {
          protected ProcessingObject<T> successor;
  
          public void setSuccessor(ProcessingObject<T> successor) {
              this.successor = successor;
          }
  
          public T handle(T input) {
              T r = handleWork(input);
              if (successor != null) {
                  return successor.handle(r);
              }
              return r;
          }
  
          abstract protected T handleWork(T input);
      }
      ```
    * 패턴 활용
      * 두 작업 처리 객체는 텍스트를 처리하는 예제
        ```
        public class HeaderTextProcessing extends ProcessingObject<String> {
    
            @Override
            protected String handleWork(String text) {
                return "From Raoul, Mario and Alan: " + text;
            }
        }
    
        public class SpellCheckerProcessing extends ProcessingObject<String> {
    
            @Override
            protected String handleWork(String text) {
                return text.replaceAll("labda", "lambda");
            }
        }
        ```
  * 람다 표현식 활용
    * 작업 처리 객체를 Function<String, String>, 더 정확히 UnaryOperator\<String> 형식의 인스턴스로 표현 가능
    * andThen 메서드로 이들 함수를 조합해서 체인을 만들 수 있음
      ```
      // 첫 번째 작업 처리 객체
      UnaryOperator<String> headerProcessing =
              (String text) -> "From Raoul, Mario and Alan: " + text;
      // 두 번째 작업 처리 객체
      UnaryOperator<String> spellCheckerProcessing =
              (String text) -> text.replaceAll("labda", "lambda");
      // 동작 체인으로 두 함수를 조합
      Function<String, String> pipeLine =
              headerProcessing.andThen(spellCheckerProcessing);

      String result = pipeLine.apply("Aren't labdas really sexy?!");
      ```

* 팩토리
  * 인스턴스화 로직을 클라이언트에 노출하지 않고 객체를 만들 때 사용
  * 예를 들어 은행에서 일하는 경우 은행에서 취급하는 대출, 채권, 주식 등 다양한 상품을 만들어야 한다고 가정
    * 다양한 상품을 만드는 Factory 클래스 필요
      ```
      public class ProductFactory {
          public static Product createProduct(String name) {
              switch (name) {
                  case "loan" : return new Loan();
                  case "stock" : return new Stock();
                  case "bond" : return new Bond();
                  default: throw new RuntimeException("No such product " + name);
              }
          }
      }
      ```
      * 여기서 Loan, Stock, Bond는 모두 Product의 서브 형식
      * createProduct 메서드는 생산된 상품을 설정하는 로직을 포함할 수 있음
      * 이는 부가적인 기능일 뿐 위 코드의 진짜 장점은 생성자와 설정을 외부로 노출하지 않음으로써 클라이언트가 단순하게 상품을 생산할 수 있다는 것
        ``` Product p1 = ProductFactory.createProduct("loan"); ```
  * 람다 표현식 사용
    * Loan 생성자를 사용하는 코드
      ```
      Supplier<Product> loanSupplier = Loan::new;
      Product loan = loanSupplier.get();
      ```
    * 이제 다음 코드처럼 상품명을 생성자로 연결하는 Map을 만들어서 코드를 재구현할 수 있음
      ```
      final static private Map<String, Supplier<Product>> map = new HashMap<>();
  
      static {
          map.put("loan", Loan::new);
          map.put("stock", Stock::new);
          map.put("bond", Bond::new);
      }
      ```
      * 맵을 이용하여 다양한 상품을 인스턴스화
        ```
        public static Product createProductLambda(String name) {
            Supplier<Product> p = map.get(name);
            if (p != null) {
                return p.get();
            }
            throw new RuntimeException("No such product " + name);
        }
        ```
    * 팩토리 메서드 createProduct가 상품 생성자로 여러 인수를 전달하는 상황에서는 이 기법을 적용하기 어려움
      * 단순히 Supplier 함수형 인터페이스로는 이 문제를 해결할 수 없음
      * 예를 들어 세 인수를 받는 상품의 생성자가 있다고 가정
        * 세 인수를 지원하려면 TriFunction이라는 특별한 함수형 인터페이스를 만들어야 함
        * 결국 다음 코드처럼 Map의 시그니처가 복잡해짐
          ```
          private interface TriFunction<T, U, V, R> {
              R apply(T t, U u, V v);
          }
          
          Map<String, TriFunction<Integer, Integer, String, Product>> map = new HashMap<>();
          ```

### 람다 테스팅
* 개발자의 최종 목표는 제대로 동작하는 코드를 구현하는 것, 깔끔한 코드를 구현하는 것이 아님
* 일반적으로 좋은 소프트웨어 공학자라면 프로그램이 의도대로 동작하는지 확인할 수 있는 단위 테스팅을 진행함
* 예를 들어 다음처럼 그래픽 앱의 일부인 Point 클래스가 있다고 가정
  ```
  public class Point {
  	private final int x;
  	private final int y;
  
  	public Point(int x, int y) {
  		this.x = x;
  		this.y = y;
  	}
  
  	public int getX() {
  		return x;
  	}
  
  	public int getY() {
  		return y;
  	}
  
  	public Point moveRightBy(int x) {
  		return new Point(this.x, this.y);
  	}
  }
  ```
* moveRightBy 메서드가 의도한 대로 동작하는지 확인하는 단위 테스트
  ```
  @Test
  public void testMoveRightBy() throws Exception {
      Point p1 = new Point(5, 5);
      Point p2 = p1.moveRightBy(10);
      assertEquals(15, p2.getX());
      assertEquals(5, p2.getY());
  }
  ```

* 보이는 람다 표현식의 동작 테스팅
  * moveRightBy는 public이므로 문제없이 작동함
  * 하지만 람다는 익명이므로 테스트 코드 이름을 호출할 수 없음
  * 따라서 필요하다면 람다를 필드에 저장해서 재사용할 수 있으며 람다의 로직을 테스트 할 수 있음
  * 메서드를 호출하는 것처럼 람다를 사용할 수 있음
    * 예를 들어 Point 클래스에 compareByXAndThenY라는 정적 필드를 추가했다고 가정
      * compareByXAndThenY를 이용하면 메서드 참조로 생성한 Comparator 객체에 접근할 수 있음
      ```
      public class Point {
          public final static Comparator<Point> compareByXAndThenY = comparing(Point::getX).thenComparing(Point::getY);
      }
      ```
  * 람다 표현식은 함수형 인터페이스의 인스턴스를 생성함
    * 따라서 생성된 인스턴스의 동작으로 람다 표현식을 테스트할 수 있음

* 람다를 사용하는 메서드의 동작에 집중하라
  * 람다의 목표는 정해진 동작을 다른 메서드에서 사용할 수 있도록 하나의 조각으로 캡슐화하는 것
  * 그러러면 세부 구현을 포함하는 람다 표현식을 공개하지 말아야 함
  * 람다 표현식을 사용하는 메서드의 동작을 테스트함으로써 람다를 공개하지 않으면서도 람다 표현식을 검증할 수 있음
  * moveAllPointsRightBy 메서드
    ```
    public static List<Point> moveAllPointsRightBy(List<Point> points, int x) {
        return points.stream().map(p -> new Point(p.getX(), p.getY())).collect(toList());
    }
    ```
    * ``` p -> new Point(p.getX(), p.getY()) ```를 테스트 하는 부분이 없음
    * moveAllPointsRightBy 메서드 동작 확인
      ```
      @Test
      public void testMoveAllPointsRightBy() throws Exception {
          List<Point> points = Arrays.asList(new Point(5, 5), new Point(10, 5));
          List<Point> expectedPoints = Arrays.asList(new Point(15, 5), new Point(20, 5));
          List<Point> newPoints = Point.moveAllPointsRightBy(points, 10);
          assertEquals(expectedPoints, newPoints);
      }
      ```
      * 위 단위 테스트에서 보여주는 것처럼 Point 클래스의 equals 메서드는 중요한 메서드
        * 따라서 Object의 기본적인 equals 구현을 그대로 사용하지 않으려면 equals 메서드를 적절하게 구현해야 함

* 복잡한 람다를 개별 메서드로 분할하기
  * 테스트 코드에서 람다 표현식을 참조할 수 없는데, 람다 표현식을 메서드 참조로 바꾸는 것이 좋음
  * 그러면 일반 메서드를 테스트하듯이 람다 표현식을 테스트할 수 있음

* 고차원 함수 테스팅
  * 함수를 인수로 받거나 다른 함수를 반환하는 메서드는 좀 더 사용하기 어려움
    * 이를 고차원 함수(higher-order functions)라고 함
  * 메서드가 람다를 인수로 받는다면 다른 람다로 메서드의 동작을 테스트할 수 있음
  * 예를 들어 다양한 프레디케이트로 2장에서 만든 filter 메서드를 테스트
    ```
    @Test
    public void testFilter() throws Exception {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        List<Integer> even = filter(numbers, i -> i % 2 == 0);
        List<Integer> smallerThanThree = filter(numbers, i -> i < 3);
        assertEquals(Arrays.asList(2, 4), even);
        assertEquals(Arrays.asList(1, 2), smallerThanThree);
    }
    ```
  * 테스트해야 할 메서드가 다른 함수를 반환한다면 Comparator에서 살펴봤던 것처럼 함수형 인터페이스의 인스턴스로 간주하고 함수의 동작을 테스트할 수 있음
  * 코드를 테스트하면서 람다 표현식에 어떤 문제가 있음을 발견하게 될 것
  * 따라서 디버깅이 필요함

### 디버깅
* 문제가 발생한 코드를 디버깅할 때 개발자는 다음 두 가지를 가장 먼저 확인해야 함
  * 스택 트레이스
  * 로깅
* 하지만 람다 표현식과 스트림은 기존의 디버깅 기법을 무력화

* 스택 트레이스 확인
  * 예외 발생으로 프로그램 실행이 갑자기 중단되었다면 먼저 어디에서 멈췄고 어떻게 멈추게 되었는지 살펴봐야 함
    * 스택 프레임(stack frame)에서 이 정보를 얻을 수 있음
    * 프로그램이 메서드를 호출할 때마다 프로그램에서의 호출 위치, 호출할 때의 인수값, 호출된 메서드의 지역 변수 등을 포함한 호출 정보가 생성
    * 이 정보들은 스택 프레임에 저장됨
    * 따라서 프로그램이 멈췄다면 프로그램이 어떻게 멈추게 되었는지 프레임별로 보여주는 스택 트레이스(stack trace)를 얻을 수 있음
    * 문제가 발생한 지점에 이르게 된 메서드 호출 리스트를 얻을 수 있음
    * 메서드 호출 리스트를 통해 문제가 어떻게 발생했는지 이해할 수 있음

* 람다와 스택 트레이스
  * 람다 표현식은 이름이 없기 때문에 조금 복잡한 스택 트레이스가 생성됨
  * 고의적으로 문제를 일으키도록 구현한 코드
    ```
    public class Debugging {
    
    	public static void main(String[] args) {
    		List<Point> points = Arrays.asList(new Point(12, 2), null);
    		points.stream().map(p -> p.getX()).forEach(System.out::println);
    	}
    }
    ```
    * 출력된 스택 트레이스
      ```
      Exception in thread "main" java.lang.NullPointerException
          at com.jaenyeong.chapter_09.DebuggingExample.Debugging.lambda$main$0(Debugging.java:12)
          at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
          at java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:948)
          at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
          at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
          at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
          at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
          at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
          at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
          at com.jaenyeong.chapter_09.DebuggingExample.Debugging.main(Debugging.java:12)
      ```
      * at com.jaenyeong.chapter_09.DebuggingExample.Debugging.lambda$main$0(Debugging.java:12)
        * $0의 의미?
      * 람다 표현식은 이름이 없으므로 컴파일러가 람다를 참조하는 이름을 만들어낸 것 (lambda$main$0)
    * 메서드 참조를 사용해도 스택 트레이스에는 메서드명이 나타나지 않음
      ```
      points.stream().map(Point::getX).forEach(System.out::println);
      ```
    * 메서드 참조를 사용하는 클래스와 같은 곳에 선언되어 있는 메서드를 참조할 때는 메서드 참조 이름이 스택 트레이스에 나타남
      ```
      public static void main(String[] args) {
          // 메서드 참조를 사용하는 클래스와 같은 곳에 선언되어 있는 메서드를 참조할 때는 메서드 참조 이름이 스택 트레이스에 나타남
          List<Integer> numbers = Arrays.asList(1, 2, 3);
          numbers.stream().map(Debugging::divideByZero).forEach(System.out::println);
      }
      
      public static int divideByZero(int n) {
          return n / 0;
      }
      ```
      * divideByZero 메서드는 스택 트레이스에 제대로 표시됨
        ```
        Exception in thread "main" java.lang.ArithmeticException: / by zero
        	at com.jaenyeong.chapter_09.DebuggingExample.Debugging.divideByZero(Debugging.java:21)
        	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        	at java.base/java.util.Spliterators$ArraySpliterator.forEachRemaining(Spliterators.java:948)
        	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        	at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
        	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        	at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        	at com.jaenyeong.chapter_09.DebuggingExample.Debugging.main(Debugging.java:17)
        ```
        * at com.jaenyeong.chapter_09.DebuggingExample.Debugging.divideByZero(Debugging.java:21)
          * 스택 트레이스에 divideByZero 표시됨
  * 람다 표현식과 관련한 스택 트레이스는 이해하기 어려움
    * 미래 자바 컴파일러가 개선해야 할 부분

* 정보 로깅
  * 스트림의 파이프라인 연산을 디버깅한다고 가정
    * 다음처럼 forEach로 스트림 결과를 출력하거나 로깅할 수 있음
      ```
      List<Integer> numbers = Arrays.asList(2, 3, 4, 5);
      
      numbers.stream()
            .map(x -> x + 17)
            .filter(x -> x % 2 == 0)
            .limit(3)
            .forEach(System.out::println);
      ```
      * 출력
        ```
        20
        22
        ```
      * forEach를 호출하는 순간 전체 스트림이 소비됨
      * 스트림 파이프라인에 적용된 각각의 연산(map, filter, limit)이 어떤 결과를 도출하는지 확인할 수 있다면 좋을 듯
  * peek 스트림 연산
    * 스트림의 각 요소를 소비한 것처럼 동작을 실행함
    * 하지만 forEach처럼 실제로 스트림의 요소를 소비하지는 않음
    * peek은 자신이 확인한 요소를 파이프라인의 다음 연산으로 그대로 전달함
    * 다음 코드에서는 peek으로 스트림 파이프라인의 각 동작 전후의 중간값을 출력함
      ```
      List<Integer> result = numbers.stream()
              .peek(x -> System.out.println("from stream: " + x))
              .map(x -> x + 17)
              .peek(x -> System.out.println("after map: " + x))
              .filter(x -> x % 2 == 0)
              .peek(x -> System.out.println("after filter: " + x))
              .limit(3)
              .peek(x -> System.out.println("after limit: " + x))
              .collect(toList());
      ```
      * 결과
        ```
        from stream: 2
        after map: 19
        from stream: 3
        after map: 20
        after filter: 20
        after limit: 20
        from stream: 4
        after map: 21
        from stream: 5
        after map: 22
        after filter: 22
        after limit: 22
        ```

### 정리
* 람다 표현식으로 가독성이 좋고 더 유연한 코드를 만들 수 있음
* 익명 클래스는 람다 표현식으로 바꾸는 것이 좋음
  * 하지만 이 때 this, 변수 섀도우(shadow variable) 등 미묘하게 의마상 다른 내용이 있음을 주의
* 메서드 참조로 람다 표현식보다 더 가독성이 좋은 코드를 구현할 수 있음
* 반복적으로 컬렉션을 처리하는 루틴은 스트림 API로 대체할 수 있을지 고려하는 것이 좋음
* 람다 표현식으로 전략, 템플릿 메서드, 옵저버, 의무 체인, 팩토리 등의 객체지향 디자인 패턴에서 발생하는 불필요한 코드를 제거할 수 있음
* 람다 표현식도 단위 테스트를 할 수 있음
  * 하지만 람다 표현식 자체를 테스트하는 것보다 람다 표현식이 사용되는 메서드의 동작을 테스트하는 것이 바람직함
* 복잡한 람다 표현식은 일반 메서드로 재구현할 수 있음
* 람다 표현식을 사용하면 스택 트레이스를 이해하기 어려워짐
* 스트림 파이프라인에서 요소를 처리할 때 peek 메서드로 중간값을 확인할 수 있음

### [quiz]
---

## chapter 10 - 람다를 이용한 도메인 전용 언어
* 프로그래밍 언어도 언어
* 언어의 주요 목표는 메시지를 명확하고, 안정적인 방식으로 전달하는 것
* 무엇보다 의도가 명확하게 전달되어야 함
  * 컴퓨터 과학자 하롤드 아벨슨(Harold Abelson)
    * 프로그램은 사람들이 이해할 수 있도록 작성되어야 하는 것이 중요하며 기기가 실행하는 부분은 부차적일 뿐
* 앱의 핵심 비즈니스를 모델링하는 소프트웨어 영역에서 읽기 쉽고, 이해하기 쉬운 코드는 특히 중요함
  * 개발팀과 도메인 전문가가 공유하고 이해할 수 있는 코드는 생산성과 직결
  * 도메인 전문가는 소프트웨어 개발 프로세스에 참여할 수 있고 비즈니스 관점에서 소프트웨어가 제대로 되었는지 확인할 수 있음
  * 버그와 오해를 미리 방지
* 도메인 전용 언어(DSL)로 앱의 비즈니스 로직을 표현함으로 이 문제를 해결
  * DSL은 작은, 범용이 아니라 특정 도메인을 대상으로 만들어진 특수 프로그래밍 언어
  * DSL은 도메인의 많은 특성 용어를 사용함
    * maven, ant 등은 빌드 과정을 표현하는 DSL로 간주할 수 있음
    * HTML은 웹페이지의 구조를 정의하도록 특화된 언어
* 자바로 구현된 데이터베이스
  * DB 내부에서 주어진 레코드를 어디에 저장해야 할지, 테이블의 인덱스를 어떻게 구성할지, 병렬 트랜잭션을 어떻게 처리할지를 계산하는 많은 코드가 존재함
  * 4, 5장에서 "메뉴에서 400 칼로리 이하의 모든 요리를 찾으시오" 같은 쿼리를 프로그램으로 구현한다고 가정
    * 전문가는 다음처럼 저수준코드를 구현
      ```
      while (block != null) {
          read(block, buffer)
          for (every record in buffer) {
              if (record.calorie < 400) {
                  System.out.println(record.name);
              }
          }
          block = buffer.next();
      }
      ```
      * 2가지 문제
        * 로킹(locking), I/O, 디스크 할당 등과 같은 지식이 필요함, 경험이 부족한 프로그래머가 구현하기엔 조금 어렵다는 점
        * 앱 수준이 아니라 시스템 수준의 개념을 다루어야 한다는 점
      * SQL의 menu 테이블에서 데이터를 찾는 것처럼 표현하는 경우
        ``` SELECT name FROM menu WHERE calorie < 400 ```
        * 위는 자바가 아닌 DSL을 이용해 데이터베이스를 조작하자는 의미
        * 기술적으로 이런 종류의 DSL을 외부적(external)이라 함
          * 데이터베이스가 텍스트로 구현된 SQL 표현식을 파싱하고 평가하는 API를 제공하는 것이 일반적이기 때문
      * 스트림 API 이용한 코드
        ```
        menu.stream()
            .filter(d -> d.getCalories() < 400)
            .map(Dish::getName)
            .forEach(System.out::println);
        ```
        * 스트림의 API의 특성인 메서드 체인을 플루언트 스타일(fluent style)이라고 함
          * 보통 자바 루푸의 복잡한 제어와 비교해서 유창함을 의미
        * 이런 스타일은 쉽게 DSL에 적용할 수 있음
          * 위 예제 DSL은 내부적
          * 내부적(internal) DSL에서는 위에서 언급한 SQL의 SELECT FROM 구문처럼 앱 수준의 기본값이 자바 메서드가 사용할 수 있도록  
            데이터베이스를 대표하는 한 개 이상의 클래스 형식으로 노출됨
* 기본적으로 DSL을 만들려면 앱 수준 프로그래머에 어떤 동작이 필요하며 이들을 어떻게 프로그래머에게 제공하는지 고민이 필요함
  * 동시에 시스템 수준의 개념으로 인해 불필요한 오염이 발생하지 않도록 해야함
* 내부적 DSL에서는 유창하게 코드를 구현할 수 있도록 적절하게 클래스와 메서드를 노출하는 과정이 필요함
  * 외부 DSL은 DSL 문법 뿐 아니라 DSL을 평가하는 파서도 구현해야 함
  * 하지만 이를 제대로 설계한다면 숙련도가 떨어지는 프로그래머 일지라도 아름답지만  
    비전문가가 이해하긴 어려운 기존 시스템 수준 코드 환경에서 새코드를 빠르고 효과적으로 구현할 수 있음

### 도메인 전용 언어
* DSL은 특정 비즈니스 도메인의 문제를 해결하려고 만든 언어
* 예를 들어 회계 전용 소프트웨어 앱을 개발한다고 가정
  * 비즈니스 도메인에는 통장 입출금 내역서, 계좌 통합 같은 개념이 포함됨
  * 이런 문제를 표현할 수 있는 DSL을 만들 수 있음
  * 자바에서는 도메인을 표현할 수 있는 클래스와 메서드 집합이 필요함
  * DSL이란 특정 비즈니스 도메인을 인터페이스로 만든 API
* DSL은 범용 프로그래밍 언어가 아님
  * DSL에서 동작과 용어는 특정 도메인에 국한되므로 다른 문제 걱정할 필요 없이 오직 자신의 앞에 놓인 문제를 어떻게 해결할지에만 집중할 수 있음
  * DSL을 이용하면 사용자가 특정 도메인의 복잡성을 더 잘 다룰 수 있음
  * 저수준 구현 세부 사항 메서드는 클래스의 비공개로 만들어서 저수준 구현 세부 내용은 숨길 수 있음
  * 이렇게하면 사용자 친화적인 DSL을 만들 수 있음
* DSL과 거리가 먼 특징
  * 평문 영어가 아님
  * 도메인 전문가가 저수준 비즈니스 로직을 구현하도록 만드는 것은 DSL의 역할이 아님
* 두 가지 필요성
  * 의사 소통의 왕
    * 우리 코드의 의도가 명확히 전달되어야 하며 프로그래머가 아닌 사람도 이해할 수 있어야 함
    * 이런 방식으로 코드가 비즈니스 요구사항에 부합하는지 확인할 수 있음
  * 한 번 코드를 구현하지만 여러번 읽음
    * 가독성은 유지보수의 핵심
    * 항상 우리의 동료가 쉽게 이해할 수 있도록 코드를 구현해야 함

* DSL의 장점과 단점
  * DSL은 코드의 비즈니스 의도를 명확하게 하고 가독성을 높인다는 점이 장점
  * 반면 DSL 구현은 코드이므로 올바로 검증하고 유지보수해야 하는 책임이 따름
  * 장점
    * 간결함
      * API는 비즈니스 로직을 간편하게 캡슐화하므로 반복을 피할 수 있고 코드를 간결하게 만들 수 있음
    * 가독성
      * 도메인 영역의 언어를 사용하므로 비 도메인 전문가도 코드를 쉽게 이해할 수 있음
      * 결과적으로 다양한 조직 구성원 간에 코드와 도메인 영역이 공유될 수 있음
    * 유지보수
      * 잘 설계된 DSL로 구현한 코드는 쉽게 유지보수하고 바꿀 수 있음
      * 유지보수는 비즈니스 관련 코드, 즉 가장 빈번히 바뀌는 앱 부분에 특히 중요함
    * 높은 수준의 추상화
      * DSL은 도메인과 같은 추상화 수준에서 동작하므로 도메인의 문제와 직접적으로 관련되지 않은 세부 사항을 숨김
    * 집중
      * 비즈니스 도메인의 규칙을 표현할 목적으로 설계된 언어이므로 프로그래머가 특정 코드에 집중할 수 있음
      * 결과적으로 생산성이 좋아짐
    * 관심사 분리(Separation of concerns)
      * 지정된 언어로 비즈니스 로직을 표현함으로 앱의 인프라구조와 관련된 문제와 독립적으로 비즈니스 관련된 코드에서 집중하기가 용이함
      * 결과적으로 유지보수가 쉬운 코드를 구현함 
  * 단점
    * DSL 설계의 어려움
      * 간결하게 제한적인 언어에 도메인 지식을 담는 것이 쉬운 작업은 아님
    * 개발 비용
      * 코드에 DSL을 추가하는 작업은 초기 프로젝트에 많은 비용과 시간이 소모되는 작업
      * 또한 DSL 유지보수와 변경은 프로젝트에 부담을 주는 요소
    * 추가 우회 계층
      * DSL은 추가적인 계층으로 도메인 모델을 감싸며 이 때 계층을 최대한 작게 만들어 성능 문제를 회피함
    * 새로 배워야 하는 언어
      * 요즘에는 한 프로젝트에도 여러가지 언어를 사용하는 추세
      * 팀이 배워야 하는 언어가 한 개 더 늘어난다는 부담
      * 여러 비즈니스 도메인을 다루는 개별 DSL을 사용하는 상황이라면 이들을 유기적으로 동작하도록 합치는 일은 쉬운 일이 아님
      * 개별 DSL이 독립적으로 진화할 수 있기 때문
    * 호스팅 언어의 한계
      * 일부 자바 같은 범용 프로그래밍 언어는 장황하고 엄격한 문법을 가짐
        * 이런 언어로는 사용자 친화적인 DSL을 만들기 힘듦
      * 사실 장황한 프로그래밍 언어를 기반으로 만든 DSL은 성가신 문법의 제약을 받아 읽기가 어려워짐
        * 자바 8의 람다 표현식은 이 문제를 해결할 강력한 새 도구

* JVM에서 이용할 수 있는 다른 DSL 해결책
  * DSL의 카테고리를 구분하는 가장 흔한 방법은 마틴 파울러가 소개한 방법으로 내부 DSL, 외부 DSL을 나누는 것
    * 내부 DSL (임베디드 DSL이라고 불림)
      * 순수 자바 코드 같은 기존 호스팅 언어를 기반으로 구현
    * 외부 DSL (stand alone이라고 불림)
      * 호스팅 언어와는 독립적으로 자체의 문법을 가짐
    * 더욱이 JVM으로 인해 내부 DSL과 외부 DSL의 중간 카테고리에 해당하는 DSL이 만들어질 가능성이 생김
      * 스칼라나 그루비처럼 자바가 아니지만 JVM에서 실행되며 더 유연하고 표현이 강력한 언어도 있음
      * 이들을 다중 DSL이라는 세 번째 카테고리로 칭함
  * 내부 DSL
    * 여기서에 내부 DSL이란 자바로 구현한 DSL을 의미함
    * 역사적으로 자바는 다소 귀찮고, 유연성이 떨어지는 문법 때문에 읽기 쉽고, 간단하고, 표현력 있는 DSL을 만드는데 한계가 있었음
    * 람다 표현식이 등장, 이 문제가 어느 정도 해결됨
    * 람다의 동작 파라미터화를 간단하게 만드는데 람다가 큰 역할
    * 람다를 적극적으로 활용하면 익명 내부 클래스를 사용해 DSL을 구현하는 것보다 장황함을 크게 줄여  
      신호 대비 잡음 비율을 적정 수준으로 유지하는 DSL을 만들 수 있음
      * 자바 7 문법
        ```
        List<String> numbers = Arrays.asList("one", "two", "three");
        numbers.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
        ```
      * 람다, 메서드 참조
        ```
        numbers.forEach(s -> System.out.println(s));
        numbers.forEach(System.out::println();
        ```
    * 사용자가 기술적인 부분을 염두에 두고 있다면 자바를 이용해 DSL을 만들 수 있음
      * 자바 문법이 큰 문제가 아니라면 순수 자바로 DSL을 구현함으로 얻는 장점
        * 기존 자바 언어를 이용하면 외부 DSL에 비해 새로운 패턴과 기술을 배워 DSL을 구현하는 노력이 현저하게 줄어듬
        * 순수 자바로 DSL을 구현하면 나머지 코드와 함께 DSL을 컴파일 할 수 있음
          * 따라서 다른 언어의 컴파일러를 이용하거나 외부 DSL을 만드는 도구를 사용할 필요가 없으므로 추가로 비용이 들지 않음
        * 개발팀이 새로운 언어를 배우거나 또는 익숙하지 않고 복잡한 외부 도구를 배울 필요가 없음
        * DSL 사용자는 기존의 자바 IDE를 이용해 자동 완성, 자동 리팩터링 같은 기능을 그대로 즐길 수 있음
          * 최신 IDE는 다른 유명한 JVM 언어도 지원하지만 자바 만큼의 기능을 지원하지는 못함
        * 한 개의 언어로 한 개의 도메인 또는 여러 도메인을 대응하지 못해 추가로 DSL을 개발해야 하는 상황에서  
          자바를 이용한다면 추가 DSL을 쉽게 합칠 수 있음
  * 다중 DSL
    * 요즘 JVM에서 실행되는 언어는 100개가 넘음
      * 스칼라, 루비 같은 유명한 언어의 개발자를 쉽게 찾을 수 있음
      * JRuby, Jython 같은 다른 언어도 JVM의 프로그래밍 언어
      * 코틀린, 실론 같이 스칼라와 호환성을 유지하면서 단순하고 쉽게 배울 수 있는 새 언어도 있음
      * 이 언어들은 모두 자바보다 젊으며 제약을 줄이고, 간편한 문법을 지향하도록 설계되었음
    * 특히 스칼라는 커링, 임의 변환 등 DSL 개발에 필요한 여러 특성을 갖췄음
      * 주어진 함수 f를 주어진 횟수만큼 반복 실행하는 유틸리티 함수를 구현한다고 가정
        * 첫 번째
          ```
          def times(i: Int, f: => Unit): Unit = {
              f // 함수 실행
              if (i > 1) times(i - 1, f) // 횟수가 양수면 횟수를 감소시켜 재귀적으로 times를 실행함
          }
          ```
          * 스칼라에서는 i가 아주 큰 숫자라 하더라도 자바에서처럼 스택 오버플로 문제가 발생하지 않음
            * 스칼라는 꼬리 호출 최적화를 통해 times 함수 호출을 스택에 추가하지 않기 때문
        * "Hello World" 세 번 반복 호출
          ```
          times(3, println("Hello World"))
          ```
        * times 함수를 커링하거나 두 그룹으로 인수를 놓을 수 있음
          ```
          def times(i: Int)(f: => Unit): Unit = {
              f
              if (i > 1) times(i - 1)(f)
          }
          ```
        * 여러 번 실행할 명령을 중괄호 안에 넣어 같은 결과를 얻을 수 있음
          ```
          times(3) {
              println("Hello World")
          }
          ```
        * 스칼라는 함수가 반복할 인수를 받는 한 함수를 가지면서 Int를 익명 클래스로 암묵적 변환하도록 정의할 수 있음
          ```
          // Int를 익명 클래스로 변환하는 암묵적 변환을 정의
          implicit def intToTimes(i: Int) = new {
              // 이 클래스는 다른 함수 f를 인수로 받는 times 함수 한 개만 정의
              def times(f: => Unit): Unit = {
                  // 두 번째 times 함수는 가장 가까운 범주에서 정의한 두 개의 인수를 받는 함수를 이용
                  def times(i: Int, f: => Unit): Unit = {
                      f
                      if (i > 1) times(i - 1, f)
                  }
                  times(i, f) // 내부 times 함수 호출
              }
          }
          ```
          * 세 번 출력하는 함수 실행
            ```
            3 times {
                println("Hello World")
            }
            ```
          * 결과적으로 문법적 잡음이 없음
            * 여기서 숫자 3은 자동으로 컴파일러에 의해 클래스 인스턴스로 변환되며 i 필드에 저장됨
            * 점 표기법을 이용하지 않고 times 함수를 호출, 이 때 반복할 함수를 인수로 받음
            * 자바로 비슷한 결과를 얻기 어려움
            * 단점
              * 새로운 프로그래밍 언어를 배우거나 또는 팀의 누군가가 이미 해당 기술을 가지고 있어야 함
                * 멋진 DSL을 만들려면 이미 기존 언어의 고급 기능을 사용할 수 있는 충분한 지식이 필요하기 때문
              * 두 개 이상의 언어가 혼재하므로 여러 컴파일러로 소스를 빌드하도록 빌드 과정을 개선해야 함
              * JVM에서 실행되는 거의 모든 언어가 자바와 100% 호환을 주장하지만 완벽하지 않을 때가 많음
                * 이런 호환성 때문에 성능 손실 가능성 있음
                  * 예를 들어 스칼라와 자바 컬렉션은 서로 호환되지 않으므로 상호 컬렉션 전달시 기존 컬렉션을 대상 언어의 API에 맞게 변환해야 함
  * 외부 DSL
    * 자산민의 문법과 구문으로 새 언어를 설계해야 함
    * 새 언어 파싱, 파서의 결과 분석, 외부 DSL을 실행할 코드 작성 등은 아주 큰 작업
    * 일반적인 작업이 아니며 쉽게 기술을 얻을 수도 없음
      * 논리 정연한 프로그래밍 언어를 새로 개발한다는 것은 간단한 작업이 아님
      * 외부 DSL을 쉽게 제어 범위를 벗어날 수 있으며 처음 설계한 목적을 벗어나는 경우가 많음
    * 구태여 택할 시 ANTLR 같은 자바 기반 파서 생성기를 이용하면 도움됨
    * 장점
      * 외부 DSL이 제공하는 무한한 유연성
      * 우리에게 필요한 특성을 완벽하게 제공하는 언어를 설계할 수 있다는 점
      * 제대로 설계시 비즈니스 문제를 묘사하고 해결하는 가독성 좋은 언어를 얻을 수 있음
      * 자바로 개발된 인프라구조 코드와 외부 DSL로 구현한 비즈니스 코드를 명확하게 분리한다는 점이 장점
        * 하지만 이 분리로 인해 DSL과 호스트 언어 사이에 인공 계층이 생기므로 양날의 검

### 최신 자바 API의 작은 DSL
* 자바의 새로운 기능의 장점을 적용한 첫 API는 네이티브 자바 API 자신
  * 자바 8 이전의 네이티브 자바 API는 이미 한 개 이상의 추상 메서드를 가진 인터페이스를 갖고 있었음
    * 하지만 익명 내부 클래스를 구현하려면 불필요한 코드가 추가되어야 함
    * 람다, 메서드 참조가 등장하면서 규칙이 바뀜(특히 DSL의 관점에서)
  * 자바 8의 Comparator 인터페이스의 새 메서드 추가
    * 사람을 가리키는 객체 목록을 가지고 있는데 사람의 나이를 기준으로 객체를 정렬한다고 가정
    * 람다가 없으면 내부 클래스로 Comparator 인터페이스를 구현해야 함
      ```
      Collections.sort(persons, new Comparator<Person<() {
          public int compare(Person p1, Person p2) {
              return p1.getAge() - p2.getAge();
          }
      });
      ```
      * 람다 표현
        ``` Collections.sort(people, (p1, p2) -> p1.getAge() - p2.getAge()); ```
    * 자바는 Comparator 객체를 가독성 있게 구현할 수 있는 정적 유틸리티 메서드 집합 제공
      * Comparator 인터페이스도 포함되어 있음
      * 정적으로 Comparator.comparing 메서드를 임포트해 구현
        ```
        Collections.sort(persons, comparing(p -> p.getAge());
        Collections.sort(persons, comparing(Person::getAge));
        ```
      * reverse 메서드를 사용해 사람들을 나이 역순으로 정렬
        ``` Collections.sort(persons, comparing(Person::getAge)).reverse(); ```
      * 이름으로 비교를 수행하는 Comparator를 구현해 같은 나이의 사람들을 알파벳 순으로 정렬
        ``` Collections.sort(persons, comparing(Person::getAge).thenComparing(Person::getName)); ```
      * List 인터페이스에 추가된 새 sort 메서드를 이용, 코드 정리
        ``` persons.sort(comparing(Person::getAge).thenComparing(Person::getName)); ```
  * 이 작은 API는 컬렉션 정렬 도메인의 최소 API
  * 작은 영역에 국한된 예제지만 람다, 메서드 참조를 이용한 DSL이 코드의 가독성, 재사용성, 결합성을 높일 수 있는 지 보여줌

* 스트림 API는 컬렉션을 조작하는 DSL
  * Stream 인터페이스는 네이티브 자바 API에 작은 내부 DSL을 적용한 좋은 예
    * Stream은 컬렉션의 항목을 필터, 정렬, 변환, 그룹화, 조작하는 작지만 강력한 DSL로 볼 수 있음
    * 로그 파일을 읽어서 "ERROR"라는 단어로 시작하는 파일의 첫 40행을 수집하는 작업을 수행한다고 가정
      ```
      List<String> errors = new ArrayList<>();

      int errorCount = 0;
      String fileName = "";
      BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

      String line = bufferedReader.readLine();
      while (errorCount < 40 && line != null) {
          if (line.startsWith("ERROR")) {
              errors.add(line);
              errorCount++;
          }
          line = bufferedReader.readLine();
      }
      ```
      * 에러 처리 코드 생략
      * 의도를 한 눈에 파악하기 어려움
        * 같은 의무를 지닌 코드가 여러 행에 분산되어 있음
          * FileReader가 만들어짐
          * 파일이 종료되었는지 확인하는 while 루프의 두 번째 조건
          * 파일의 다음 행을 읽는 while 루프의 마지막 행
        * 마찬가지로 첫 40행을 수집하는 코드도 세 부분으로 흩어져 있음
          * errorCount 변수를 초기화하는 코드
          * while 루프의 첫 번째 조건
          * "Error"을 로그에서 발견하면 카운터를 증가시키는 행
      * Stream 인터페이스를 이용, 함수형 코드를 구현
        ```
        List<String> errors = Files.lines(Paths.get(FILE_NAME)) // 파일을 열어서 문자열 스트림을 만듦
                .filter(line -> line.startsWith("ERROR")) // "ERROR"로 시작하는 행을 필터링
                .limit(40) // 결과를 첫 40행으로 제한
                .collect(toList()); // 결과 문자열을 리스트로 수집
        ```
        * String은 파일에서 파싱할 행을 의미하며 Files.lines는 정적 유틸리티 메서드로 Stream\<String>을 반환함
        * 파일을 한 행씩 읽는 부분의 코드는 이게 전부
        * 마찬가지로 limit(40)이라는 코드로 "ERROR"가 포함된 행을 첫 40개만 수집함
  * 스트림 API의 플루언트 형식은 잘 설계된 DSL의 또다른 특징
    * 모든 중간 연산은 게으르며 다른 연산으로 파이프라인될 수 있는 스트림으로 반환됨
    * 최종 연산은 적극적이며 전체 파이프라인이 계산을 일으킴

* 데이터를 수집하는 DSL인 Collectors
  * Stream 인터페이스를 데이터 리스트를 조작하는 DSL로 간주할 수 있음을 확인
    * 마찬가지로 Collector 인터페이스는 데이터 수집을 수행하는 DSL로 간주 가능
      * Collector 인터페이스를 이용해 스트림의 항목을 수집, 그룹화, 파티셔닝
      * Collectors 클래스에서 제공하는 정적 팩토리 메서드를 이용, Collector 객체를 만들고 합치는 기능
      * Comparator 인터페이스는 다중 필드 정렬을 지원하도록 합쳐질 수 있음
      * Collectors는 다중 수준 그룹화를 달성할 수 있도록 합쳐질 수 있음
      * 예를 들어 자동차를 브랜드별 그리고 색상별로 그룹화
        ```
        Map<String, Map<Color, List<Car>>> carsByBrandAndColor = 
                cars.stream().collect(groupingBy(Car::getBrand, groupingBy(Car::getColor)));
        ```
        * 두 Comparator를 플루언트 방식으로 연결해서 다중 필드 Comparator 정의
          ```
          Comparator<Person> comparator = comparing(Person::getAge).thenComparaing(Person::getName);
          ```
        * 반면 Collectors API를 이용해 Collectors를 중첩함으로 다중 수준 Collector를 만들 수 있음
          ```
          Collector<? super Car, ?, Map<Brand, Map<Color, List<Car>>>> carGroupingCollector = 
                groupingBy(Car::getBrand, groupingBy(Car::getColor));
          ```
          * 셋 이상의 컴포넌트를 조합할 때는 보통 플루언트 형식이 중첩 형식에 비해 가독성이 좋음
          * 사실 가장 안쪽의 Collector가 첫 번째로 평가되어야 하지만  
            논리적으로는 최종 그룹화에 해당하므로 서로 다른 형식은 이를 어떻게 처리하는지를 상반적으로 보여줌
          * 예제에서 플루언트 형식으로 Collector를 연결하지 않고 Collector 생성을 여러 정적 메서드로 중첩함으로  
            안쪽 그룹화가 처음 평가되고 코드에서는 반대로 가장 나중에 등장하게 됨
      * groupingBy 팩토리 메서드에 작업을 위임하는 GroupingBuilder를 만들어 더 쉽게 해결
        * GroupingBuilder는 유연한 방식으로 여러 그룹화 작업을 만듦
          ```
          import static java.util.stream.Collectors.groupingBy;
          
          public class GroupingBuilder<T, D, K> {
              private final Collector<? super T, ?, Map<K, D>> collector;
          
              public GroupingBuilder(Collector<? super T, ?, Map<K, D>> collector) {
                  this.collector = collector;
          	  }
          
              public Collector<? super T, ?, Map<K, D>> get() {
          	      return collector;
          	  }
          
              public <J> GroupingBuilder<T, Map<K, D>, J> after(Function<? super T, ? extends J> classifier) {
          	      return new GroupingBuilder<>(groupingBy(classifier, collector));
              }
          
              public static <T, D, K> GroupingBuilder<T, List<T>, K> groupOn(Function<? super T, ? extends K> classifier) {
                  return new GroupingBuilder<>(groupingBy(classifier));
              }
          }
          ```
        * 플루언트 형식 빌더에 문제점을 보여주는 코드
          ```
          Collector<? super Car, ?, Map<Brand, Map<Color, List<Car>>>> carGroupingCollector = 
               groupOn(Car::getColor).after(Car::getBrand).get();
          ```
          * 중첩된 그룹화 수준에 반대로 그룹화 함수를 구현해야 하므로 유틸리티 사용 코드가 직관적이지 않음
            * 자바 형식 시스템으로는 이런 순서 문제를 해결할 수 없음

### 자바로 DSL을 만드는 패턴과 기법
* Model 패키지에 Stock, Trade, Order, Main 클래스 작성

* 메서드 체인
  * 쉽게 이해할 수 있는 코드 작성
    * 플루언트 API로 도메인 객체를 만드는 몇 개의 빌더를 구현해야 함
    * 최상위 수준 빌더를 만들고 주문을 감싼 다음 한 개 이상의 거래를 주문에 추가할 수 있어야 함
      * 주문 빌더의 buy(), sell() 메서드는 다른 주문을 만들어 추가할 수 있도록 자신을 만들어 반환
      * 빌더를 계속 이어가려면 Stock 클래스의 인스턴스를 만드는 TradeBuilder의 공개 메서드 이용
      * StockBuilder는 주식의 시장을 지정하고, 거래에 주식을 추가하고, 최종 빌더를 반환하는 on() 메서드 한 개 정의
      * 한 개의 공개 메서드 TradeBuilderWithStock은 거래되는 주식의 단위 가격을 설정한 다음 원래 주문 빌더를 반환
      * MethodChainingOrderBuilder가 끝날 때까지 다른 거래를 플루언트 방식으로 추가 가능
    * 여러 빌드 클래스, 특히 두 개의 거래 빌더를 따로 만듦으로써 사용자가 미리 지정된 절차에 따라 플루언트 API의 메서드를 호출하도록 강제함
    * 덕분에 사용자가 다음 거래를 설정하기 전에 기존 거래를 올바로 설정하게 됨
    * 이 접근 방법은 주문에 사용한 파라미터가 빌더 내부로 국한된다는 다른 이점도 제공함
    * 이 접근 방법은 정적 메서드 사용을 최소화하고 메서드 이름이 인수의 이름을 대신하도록 만듦으로 이런 형식의 DSL의 가독성을 개선하는 효과를 더함
    * 이런 기법을 적용한 플루언트 DSL에는 문법적 잡음이 최소화 됨
  * 빌더를 구현해야 한다는 단점
    * 상위 수준의 빌더를 하위 수준의 빌더와 연결할 접착 많은 접착 코드가 필요함
  * 도메인 객체의 중첩구조와 일치하게 들여쓰기를 강제하는 방법이 없다는 단점

* 중첩된 함수 이용
  * 다른 함수 안에 함수를 이용해 도메인 모델 만듦
  * 중첩된 함수로 주식 거래 만들기
    ```
    // 중첩된 함수로 주식 거래 만들기
    public void nestedFunction() {
        Order order = order("BigBank",
                buy(80,
                        stock("IBM", on("NYSE")),
                        at(125.00)),
                sell(50,
                        stock("GOOGLE", on("NASDAQ")),
                        at(375.00))
        );

        System.out.println("Nested function:");
        System.out.println(order);
    }
    ```
  * 중첩된 함수 DSL을 제공하는 주문 빌더
    ```
    public class NestedFunctionOrderBuilder {
    
    	public static Order order(String customer, Trade... trades) {
    		Order order = new Order(); // 해당 고객의 주문 만들기
    		order.setCustomer(customer);
    		Stream.of(trades).forEach(order::addTrade); // 주문에 모든 거래 추가
    		return order;
    	}
    
    	// 주식 매수 거래 만들기
    	public static Trade buy(int quantity, Stock stock, double price) {
    		return buildTrade(quantity, stock, price, Trade.Type.BUY);
    	}
    
    	// 주식 매도 거래 만들기
    	public static Trade sell(int quantity, Stock stock, double price) {
    		return buildTrade(quantity, stock, price, Trade.Type.SELL);
    	}
    
    	private static Trade buildTrade(int quantity, Stock stock, double price, Trade.Type buy) {
    		Trade trade = new Trade();
    		trade.setQuantity(quantity);
    		trade.setType(buy);
    		trade.setStock(stock);
    		trade.setPrice(price);
    		return trade;
    	}
    
    	// 거래된 주식의 단가를 정의하는 더미 메서드
    	public static double at(double price) {
    		return price;
    	}
    
    	public static Stock stock(String symbol, String market) {
    		Stock stock = new Stock(); // 거래된 주식 만들기
    		stock.setSymbol(symbol);
    		stock.setMarket(market);
    		return stock;
    	}
    
    	// 주식이 거래된 시장을 정의하는 더미 메서드
    	public static String on(String market) {
    		return market;
    	}
    }
    ```
  * 메서드 체인에 비해 함수의 중첩 방식이 도메인 객체 계층 구조에 그대로 반영 된다는 것이 장점
    * 에제에서 주문은 한 개 이상의 거래를 포함하며 각 거래는 한 개의 주식을 참조
  * 문제점
    * 결과 DSL에 더 많은 괄호를 사용해야 한다는 사실
    * 더욱이 인수 목록을 정적 메서드에 넘겨줘야 한다는 제약
    * 도메인 객체에 선택 사항 필드가 있으면 인수를 생략할 수 있으므로 이 가능성을 처리할 수 있도록 여러 메서드 오버라이드를 구현해야 함
    * 인수의 의미가 이름이 아니라 위치에 의해 정의되었음
      * NestedFunctionOrderBuilder의 at(), on() 메서드에서 했던 것처럼 인수의 역할을 확실하게 만드는 여러 더미 메서드를 이용해 완화할 수 있음

* 람다 표현식을 이용한 함수 시퀀싱
  * 함수 시퀀싱으로 주식 거래 주문 만들기
    ```
    Order order = LambdaOrderBuilder.order(o -> {
    			o.forCustomer("BigBank");
    			o.buy(t -> {
    				t.quantity(80);
    				t.price(125.00);
    				t.stock(s -> {
    					s.symbol("IBM");
    					s.market("NYSE");
    				});
    			});
    			o.sell(t -> {
    				t.quantity(50);
    				t.price(375.00);
    				t.stock(s -> {
    					s.symbol("GOOGLE");
    					s.market("NASDAQ");
    				});
    			});
    		});
    ```
  * Builder
    ```
    public class LambdaOrderBuilder {
    	// 빌더로 주문을 감싸기
    	private Order order = new Order();
    
    	public static Order order(Consumer<LambdaOrderBuilder> consumer) {
    		LambdaOrderBuilder builder = new LambdaOrderBuilder();
    		consumer.accept(builder); // 주문 빌더로 전달된 람다 표현식 실행
    		return builder.order; // OrderBuilder의 Consumer를 실행해 만들어진 주문을 반환
    	}
    
    	public void forCustomer(String customer) {
    		// 주문을 요청한 고객 설정
    		order.setCustomer(customer);
    	}
    
    	public void buy(Consumer<TradeBuilder> consumer) {
    		// 주식 매수 주문을 만들도록 TradeBuilder 소비
    		trade(consumer, Trade.Type.BUY);
    	}
    
    	public void sell(Consumer<TradeBuilder> consumer) {
    		// 주식 매도 주문을 만들도록 TradeBuilder 소비
    		trade(consumer, Trade.Type.SELL);
    	}
    
    	private void trade(Consumer<TradeBuilder> consumer, Trade.Type type) {
    		TradeBuilder builder = new TradeBuilder();
    		builder.trade.setType(type);
    		// TradeBuilder로 전달할 람다 표현식 실행
    		consumer.accept(builder);
    		// TradeBuilder의 Consumer를 실행해 만든 거래를 주문에 추가
    		order.addTrade(builder.trade);
    	}
    
    	public static class TradeBuilder {
    		private Trade trade = new Trade();
    
    		public void quantity(int quantity) {
    			trade.setQuantity(quantity);
    		}
    
    		public void price(double price) {
    			trade.setPrice(price);
    		}
    
    		public void stock(Consumer<StockBuilder> consumer) {
    			StockBuilder builder = new StockBuilder();
    			consumer.accept(builder);
    			trade.setStock(builder.stock);
    		}
    	}
    
    	public static class StockBuilder {
    		private Stock stock = new Stock();
    
    		public void symbol(String symbol) {
    			stock.setSymbol(symbol);
    		}
    
    		public void market(String market) {
    			stock.setMarket(market);
    		}
    	}
    }
    ```
  * 이 패턴은 이전 두 가지 DSL 형식의 두 가지 장점을 더함
    * 메서드 체인 패턴처럼 플루언트 방식으로 거래 주문을 정의할 수 있음
    * 또한 중첩 함수 형식처럼 다양한 람다 표현식의 중첩 수준과 비슷하게 도메인 객체의 계층 구조를 유지함
  * 단점
    * 많은 설정 코드가 필요하며 DSL 자체가 자바 8 람다 표현식 문법에 의한 잡음의 영향을 받음

* 조합하기
  * 여러 패턴을 이용해 주식 거래 주문 만들기
    ```
    Order order =
            mixedForCustomer("BigBank", // 최상위 수준 주문의 속성을 지정하는 중첩 함수
                    mixedBuy(t -> t.quantity(80) // 한 개의 주문을 만드는 람다 표현식
                            .stock("IBM") // 거래 객체를 만드는 람다 표현식 바디의 메서드 체인
                            .on("NYSE")
                            .at(125.00)),
                    mixedSell(t -> t.quantity(50)
                            .stock("GOOGLE")
                            .on("NASDAQ")
                            .at(375.00)));

    System.out.println("Mixed:");
    System.out.println(order);
    ```
  * Builder
    ```
    public class MixedBuilder {
    
    	public static Order mixedForCustomer(String customer, TradeBuilder... builders) {
    		Order order = new Order();
    		order.setCustomer(customer);
    		Stream.of(builders).forEach(b -> order.addTrade(b.trade));
    		return order;
    	}
    
    	public static TradeBuilder mixedBuy(Consumer<TradeBuilder> consumer) {
    		return buildTrade(consumer, Trade.Type.BUY);
    	}
    
    	public static TradeBuilder mixedSell(Consumer<TradeBuilder> consumer) {
    		return buildTrade(consumer, Trade.Type.SELL);
    	}
    
    	private static TradeBuilder buildTrade(Consumer<TradeBuilder> consumer, Trade.Type buy) {
    		TradeBuilder builder = new TradeBuilder();
    		builder.trade.setType(buy);
    		consumer.accept(builder);
    		return builder;
    	}
    
    	public static class TradeBuilder {
    		private Trade trade = new Trade();
    
    		public TradeBuilder quantity(int quantity) {
    			trade.setQuantity(quantity);
    			return this;
    		}
    
    		public TradeBuilder at(double price) {
    			trade.setPrice(price);
    			return this;
    		}
    
    		public StockBuilder stock(String symbol) {
    			return new StockBuilder(this, trade, symbol);
    		}
    	}
    
    	public static class StockBuilder {
    		private final TradeBuilder builder;
    		private final Trade trade;
    		private final Stock stock = new Stock();
    
    		private StockBuilder(TradeBuilder builder, Trade trade, String symbol) {
    			this.builder = builder;
    			this.trade = trade;
    			stock.setSymbol(symbol);
    		}
    
    		public TradeBuilder on(String market) {
    			stock.setMarket(market);
    			trade.setStock(stock);
    			return builder;
    		}
    	}
    }
    ```
  * 단점
    * 결과 DSL이 여러 가지 기법을 혼용하고 있으므로 한 가지 기법을 적용한 DSL에 비해 사용자가 DSL을 배우는데 오랜 시간이 걸림
  * 메서드 참조를 이용하면 많은 DSL의 가독성을 높일 수 있음

* DSL에 메서드 참조 사용하기
  * 세금 도메인 추가
    ```
    public class Tax {
    
    	public static double regional(double value) {
    		return value * 1.1;
    	}
    
    	public static double general(double value) {
    		return value * 1.3;
    	}
    
    	public static double surcharge(double value) {
    		return value * 1.05;
    	}
    }
    ```
    * 세금을 적용할 것인지 결정하는 불리언 플래그를 인수로 받는 정적 메서드 이용
      ```
      // 불리언 플래그 집합을 이용해 주문에 세금 적용
      public static double calculate(Order order, boolean useRegional, boolean useGeneral, boolean useSurcharge) {
          double value = order.getValue();
  
          if (useRegional) {
              value = Tax.regional(value);
          }
          if (useGeneral) {
              value = Tax.general(value);
          }
          if (useSurcharge) {
              value = Tax.surcharge(value);
          } 
          return value;
      }
      ```
      * 지역 세금과 추가 요금을 적용하고 일반 세금은 뺀 주문의 최종값을 계산할 수 있음
      	``` double value = TaxCalculator.calculate(order, true, false, true); ```
    * 이 구현의 가독성 문제
      * 불리언 변수의 올바른 순서를 기억하기 어려우며 어떤 세금이 적용되었는지도 파악하기 어려움
      * 유창하게 불리언 플래그를 설정하는 최소 DSL을 제공하는 TaxCalculator를 이용하는 것이 더 좋은 방법
        ```
        public class TaxCalculator {
        
        	private boolean useRegional;
        	private boolean useGeneral;
        	private boolean useSurcharge;
        
        	public TaxCalculator withTaxRegional() {
        		useRegional = true;
        		return this;
        	}
        
        	public TaxCalculator withTaxGeneral() {
        		useGeneral = true;
        		return this;
        	}
        
        	public TaxCalculator withTaxSurcharge() {
        		useSurcharge = true;
        		return this;
        	}
        
        	public double calculate(Order order) {
        		return calculate(order, useRegional, useGeneral, useSurcharge);
        	}
        }
        ```
        * TaxCalculator는 지역 세금과 추가 요금은 주문에 추가하고 싶다는 점을 명확하게 보여줌
          ```
          double value = new TaxCalculator().withTaxRegional()
                    .withTaxSurcharge()
                    .calculate(order);
          ```
      * 코드가 장황하다는 것이 이 기법의 문제
        * 도메인의 각 세금에 해당하는 불리언 필드가 필요하므로 확장성도 제한적
        * 자바의 함수형 기능을 이용하면 더 간결하고 유연한 방식으로 같은 가독성을 달성 가능
          ```
          /**
           * 유창하게 세금 함수를 적용하는 세금 계산기
           * 위 TaxCalculator 내용을 리팩터링
           */
          
          // 주문값에 적용된 모든 세금을 계산하는 함수
          public DoubleUnaryOperator taxFunction = d -> d;
          
          // 새로운 세금 계산 함수를 얻어서 인수로 전달된 함수와 현재 함수를 합침
          public TaxCalculator with(DoubleUnaryOperator f) {
          	taxFunction = taxFunction.andThen(f);
          	return this;
          }
          
          // 주문의 총 합에 세금 계산 함수를 적용해 최종 주문값을 계산
          public double calculateF(Order order) {
          	return taxFunction.applyAsDouble(order.getValue());
          }
          ```
          * 이 기법은 주문의 총 합에 적용할 함수 한 개의 필드만 필요로 하며 TaxCalculator 클래스를 통해 모든 세금 설정이 적용됨
          * 이 함수의 시작값은 확인 함수
          * 처음 시점에서는 세금이 적용되지 않았으므로 최종값은 총합과 같음
          * with() 메서드로 새 세금이 추가되면 현재 세금 계산 함수에 이 세금이 조합되는 방식으로 한 함수에 모든 추가된 세금이 적용됨
          * 마지막으로 주문을 calculate() 메서드에 전달하면 다양한 세금 설정의 결과로 만들어진 세금 계산 함수가 주문의 합계에 적용됨
          * 리팩터링 결과
            ```
            double value = new TaxCalculator().with(Tax::regional)
                    .with(Tax::surcharge)
                    .calculateF(order);
            ```
          * 메서드 참조는 읽기 쉽고 코드를 간결하게 만듦
          * 새로운 세금 함수를 Tax 클래스에 추가해도 함수형 TaxCalculator를 바꾸지 않고 바로 사용할 수 있는 유연성도 제공함

### 실생활의 자바 8 DSL
* DSL 패턴의 장점과 단점
  * 메서드 체인
    * 장점
      * 메서드명이 키워드 인수 역할을 함
      * 선택형 파라미터와 잘 동작함
      * DSL 사용자가 정해진 순서대로 메서드를 호출하도록 강제할 수 있음
      * 정적 메서드를 최소화하거나 없앨수 있음
      * 문법적 잡음을 최소화함
    * 단점
      * 구현이 장황함
      * 빌드를 연결하는 접착 코드가 필요함
      * 들여쓰기 규칙으로만 도메인 객체의 계층을 정의함
  * 중첩 함수
    * 장점
      * 구현의 장황함을 줄일 수 있음
      * 함수 중첩으로 도메인 객체 계층을 반영함
    * 단점
      * 정적 메서드의 사용이 빈번함
      * 이름이 아닌 위치로 인수를 정의함
      * 선택형 파라미터를 처리할 메서드 오버로딩이 필요함
  * 람다를 이용한 함수 시퀀싱
    * 장점
      * 선택형 파라미터와 잘 동작함
      * 정적 메서드를 최소화하거나 없앨 수 있음
      * 람다 중첩으로 도메인 객체 계층을 반영함
      * 빌더의 접착 코드가 없음
    * 단점
      * 구현이 장황함
      * 람다 표현식으로 인한 문법적 잡음이 DSL에 존재함

* jOOQ - 객체 지향 쿼리 (일반적으로 jOOQ라고 함)
  * 활성 레코드 패턴을 구현하는 Java의 경량 데이터베이스 매핑 소프트웨어 라이브러리 
    * 데이터베이스 스키마에서 생성된 클래스에서 쿼리를 구성 할 수 있는 도메인 별 언어를 제공하여 관계형 및 객체 지향적임
  * SQL은 DSL에서 가장 흔히, 광범위하게 사용하는 분야
  * 따라서 SQL 질의를 작성하고 실행하는데 필요한 DSL을 제공하는 자바 라이브러리가 당연히 있음
  * jOOQ는 SQL을 구현하는 내부적 DSL로 자바에 직접 내장된 형식의 안전 언어
  * 데이터베이스 스키마를 역공학하는 소스코드 생성기 덕분에 자바 컴파일러가 복잡한 SQL 구문 형식을 확인할 수 있음
  * 역공학 프로세스 제품이 생성한 정보를 기반으로 우리는 데이터베이스 스키마를 탐색할 수 있음
  * SQL 질의 예시
    ```
    SELECT * FROM BOOK
     WHERE BOOK.PUBLISHED_IN = 2016
     ORDER BY BOOK.TITLE
    ```
  * jOOQ DSL을 이용, 위 질의를 구현
    ```
    create.selectFrom(BOOK)
          .where(BOOK.PUBLISHED_IN.eq(2016))
          .orderBy(BOOK.TITLE)
    ```
  * 스트림 API와 조합해 사용할 수 있다는 것이 jOOQ DSL의 또 다른 장점
    * SQL 질의 실행으로 나온 결과를 한 개의 플루언트 구문으로 데이터를 메모리에서 조작 가능
      ```
      Class.forName("org.h2.driver");
      try (Connection c = getConnection("jdbc:h2:~/sql-goodies-with-mapping", "sa", "")) {
          DSL.using(c)
              .select(BOOK.AUTHOR, BOOK.TITLE) // 만들어진 DB 연결을 이용해 jOOQ SQL 시작
              .where(BOOK.PUBLISHED_IN.eq(2016))
              .orderBy(BOOK.TITLE)
              .fetch() // jOOQ DSL로 SQL문 정의
              .stream() // DB에서 데이터 가져오기 jOOQ문은 여기서 종료
              .collect(groupingBy( // 스트림 API로 DB에서 가져온 데이터 처리 시작
                    r -> r.getValue(BOOK.AUTHOR), 
                    LinkedHashMap::new, 
                    mapping(r -> r.getValue(BOOK.TITLE), toList())))
              // 저자의 이름 목록과 각 저자가 집팔한 책들 출력
              .forEach((author, titles) -> System.out.println(author + " is author of " + titles));
      }
      ```
  * jOOQ DSL을 구현하는데 메서드 체인 패턴을 사용했음을 쉽게 파악
    * 잘 만들어진 SQL 질의 문법을 흉내내려면 메서드 체인 패턴의 여러 특성이 반드시 필요하기 때문
      * 선택적 파라미터를 허용하고 미리 정해진 순서로 특정 메서드가 호출될 수 있게 강제

* 큐컴버 (Cucumber)
  * 동작 주도 개발은 테스트 주도 개발의 확장으로 다양한 비즈니스 시나리오를 구조적으로 서술하는 간단한 도메인 전용 스크립팅 언어를 사용함
    * 동작 주도 개발 (BDD) : Behavior-driven development
  * 큐컴버는 다른 BDD 프레임워크와 마찬가지로 이들 명령문을 실행할 수 있는 테스트 케이스로 변환함
  * 결과적으로 이 개발 기법으로 만든 스크립트 결과물은 실행할 수 있는 테스트임과 동시에 비즈니스 기능의 수용 기준이 됨
  * BDD는 우선 순위에 따른, 확인할 수 있는 비즈니스 가치를 전달하는 개발 노력에 집중하며  
    비즈니스 어휘를 공유함으로 도메인 전문가와 프로그래머 사이의 간격을 줄임
  * 개발자가 비즈니스 시나리오를 평문 영어로 구현할 수 있도록 도와주는 BDD 도구인 큐컴버
    * 이를 이용한 예제
      ```
      Feature: Buy stock
        Scenario: Buy 10 IBM stocks
          Given the price of a "IBM" stock is 125$
          When I buy 10 "IBM"
          Then the order value should be 1250$
      ```
  * 큐컴버는 세 가지 구분되는 개념을 사용함
    * 전체 조건 정의 - Given
    * 시험하려는 도메인 객체의 실질 호출 - When
    * 테스트 케이스 결과를 확인하는 어설션(assertion) - Then
  * 테스트 시나리오를 정의하는 스크립트는 제한된 수의 키워드를 제공하며 자유로운 형식으로 문장을 구현할 수 있는 외부 DSL을 활용함
  * 이들 문장은 테스트 케이싀 변수를 캡처하는 정규 표현식으로 매칭되며 테스트 자체를 구현하는 메서드로 이를 전달함
  * 큐컴버로 주식 거래 도메인 모델 예제를 이용해 주식 거래 주문 값이 제대로 계산되었는지 확인하는 테스트 케이스 개발 가능
    ```
    public class BuyStocksStepsTest {
    	private Map<String, Integer> stockUnitPrices = new HashMap<>();
    	private Order order = new Order();
    
    	// 시나리오의 전제 조건인 주식 단가 정의
    	@Given("^the price of a \"(.*?)\" stock is (\\d+)\\$$")
    	public void setUnitPrice(String stockName, int unitPrice) {
    		stockUnitPrices.put(stockName, unitPrice); // 주식 단가 저장
    	}
    
    	// 테스트 대상인 도메인 모델의 행할 액션 정의
    	@When("^I buy (\\d+) \"(.*?)\"$")
    	public void buyStocks(int quantity, String stockName) {
    		Trade trade = new Trade();
    		trade.setType(Trade.Type.BUY);
    
    		Stock stock = new Stock();
    		stock.setSymbol(stockName);
    
    		trade.setStock(stock);
    		trade.setPrice(stockUnitPrices.get(stockName));
    		trade.setQuantity(quantity);
    		order.addTrade(trade);
    	}
    
    	// 예상되는 시나리오 결과 정의
    	@Then("^the order value should be (\\d+)\\$$")
    	public void checkOrderValue(int expectedValue) {
    		assertEquals(expectedValue, order.getValue());
    	}
    }
    ```
  * 자바 8이 람다 표현식을 지원하면서 두 개의 인수 메서드(기존 어노테이션 값을 포함한 정규 표현식과 테스트 메서드를 구현한 람다)를 이용해  
    어노테이션을 제거하는 다른 문법을 큐컴버로 개발할 수 있음
    * 두 번째 형식의 표기법을 이용해 테스트 시나리오를 다음처럼 구현
      ```
      public BuyStocksSteps() {
          @Given("^the price of a \"(.*?)\" stock is (\\d+)\\$$", 
                  (String stockName, int unitPrice) -> {
                      stockUnitPrices.put(stockName, unitPrice);
                  });
          // When, Then 생략 
      }
      ```
      * 두 번째 문법은 코드가 더 단순해진다는 장점
      * 특히 테스트 메서드가 익명 람다로 바뀌면서 의미를 가진 메서드명을 찾는 부담이 없어짐
        * 보통 이 과정은 테스트 시나리오 가독성에 아무 도움이 되지 않음

* 스프링 통합 (Spring Integration)
  * 유명한 엔터프라이즈 통합 패턴을 지원할 수 있도록 의존성 주입에 기반한 스프링 프로그래밍 모델을 확장함
  * 스프링 통합의 핵심 목표
    * 복잡한 엔터프라이즈 통합 솔루션을 구현하는 단순한 모델을 제공하고 비동기, 메시지 주도 아키텍처를 쉽게 적용할 수 있게 돕는 것
  * 스프링 통합은 스프링 기반 앱 내의 경량의 원격, 메시징, 스케줄링을 지원함
    * 유창한 DSL을 통해 기존의 스프링 XML 설정 파일 기반에도 이들 기능을 지원함
  * 스프링 통합은 채널, 엔드포인트(end points), 폴러(pllers), 채널 인터셉터(channel interceptors) 등  
    메시지 기반의 앱에 필요한 가장 공통 패턴을 모두 구현함
    * 가독성이 높아지도록 엔드포인트는 DSL에서 동사로 구현하며 여러 엔드포인트를 한 개 이상의 메시지 흐름으로 조합해서 통합 과정이 구성됨
  * 스프링 통합이 어떻게 동작하는지를 보여주는 예제
    ```
    @configuration
    @EnableIntegration
    public class MyConfiguration {
    
    	@Bean
    	public MessageSource<?> integerMessageSource() {
    		// 호출시 AtomicInteger를 증가 시키는 새 MessageSource를 생성
    		MethodInvokingMessageSource source = new MethodInvokingMessageSource();
    		source.setObject(new AtomicInteger());
    		source.setMethodName("getAndIncrement");
    		return source;
    	}
    
    	@Bean
    	public DirectChannel inputChannel() {
    		// MessageSource에서 도착하는 데이터를 나르는 채널
    		return new DirectChannel();
    	}
    
    	@Bean
    	public IntegrationFlow myFlow() {
    		return IntegrationFlows
    				// 기존에 정의한 MessageSource를 IntegrationFlow의 입력으로 사용
    				.from(this.integerMessageSource(),
    						// MessageSource를 폴링하면서 MessageSource가 나르는 데이터를 가져옴
    						c -> c.poller(Pollers.fixedRate(10)))
    				.channel(this.inputChannel())
    				// 짝수만 거름
    				.filter((Integer p) -> p % 2 == 0)
    				// MessageSource에서 가져온 정수를 문자열로 변환
    				.transform(Object::toString)
    				// queueChannel을 IntegrationFlow의 결과로 설정
    				.channel(MessageChannels.queue("queueChannel"))
    				// IntegrationFlow 만들기를 끝나고 반환
    				.get();
    	}
    }
    ```
    * 스프링 통합 DSL을 이용해 myFlow()는 IntegrationFlow를 만듦
      * 예제는 메서드 체인 패턴을 구현하는 IntegrationFlow 클래스가 제공하는 유연한 빌더를 사용함
      * 그리고 결과 플로는 고정된 속도로 MessageSource를 폴링하면서 일련의 정수를 제공, 짝수만 거른 다음
      * 문자열로 변환해 최종적으로 결과를 자바 8 스트림 API와 비슷한 방법으로 출력 채널에 전달함
      * inpuutChannel 이름만 알고 있다면 이 API를 이용해 플로 내의 모든 컴포넌트로 메시지를 전달할 수 있음
        ```
        @Bean
        public IntegrationFlow myFlow() {
            return flow -> flow
                    .filter((Integer p) -> p % 2 == 0)
                    .transform(Object::toString)
                    .handle(System.out::println);
        }
        ```
  * 스프링 통합 DSL에서 가장 널리 사용하는 패턴은 메서드 체인
    * 이 패턴은 IntegrationFlow 빌더의 주요 목표인 전달되는 메시지 흐름을 만들고 데이터를 변환하는 기능에 적합함
    * 하지만 최상위 수준의 객체를 만들때(내부의 복잡한 메서드 인수에도)는 함수 시퀀싱과 람다 표현식을 사용함

### 정리
* DSL의 주요 기능은 개발자와 도메인 전문가 사이의 간격을 좁히는 것
  * 앱의 비즈니스 로직을 구현하는 코드를 만든 사람이 프로그램이 사용될 비즈니스 필드의 전문 지식을 갖추긴 어려움
  * 개발자가 아닌 사람도 이해할 수 있는 언어로 이런 비즈니스 로직을 구현할 수 있다고 해서
  * 도메인 전문가가 프로그래머가 될 수 있는 것은 아니지만 적어도 로직을 읽고 검증하는 역할을 할 수 있음
* DSL 분류
  * 내부적 DSL
    * DSL이 사용될 앱을 개발한 언어를 그대로 활용
    * 개발 노력이 적게 드는 반면 호스팅 언어의 문법 제약을 받음
  * 외부적 DSL
    * 직접 언어를 설계해 사용함
    * 높은 유연성을 제공하지만 구현하기가 어려움
* JVM에서 이용할 수 있는 스칼라, 그루비 등의 다른 언어로 다중 DSL을 개발할 수 있음
  * 이들 언어는 자바보다 유연하고 간결한 편
  * 하지만 이들은 자바와 통합하려면 빌드과정이 복잡해지며 자바와의 상호 호환성 문제도 생길 수 있음
* 자바의 장황함과 문법적 엄격함 때문에 보통 자바는 내부적 DSL을 개발하는 언어로는 적합하지 않음
  * 하지만 자바 8의 람다 표현식, 메서드 참조 덕분에 상황이 많이 개선되었음
* 최신 자바는 자체 API에 작은 DSL을 제공함
  * Stream, Collectors 클래스 등에서 제공하는 작은 DSL은 특히 컬렉션 데이터의 정렬, 필터링, 변환, 그룹화에 유용함
* 자바로 DSL을 구현할 때 보통 메서드 체인, 중첩 함수, 함수 시퀀싱 세 가지 패턴이 사용됨
  * 각가의 패턴은 장단점이 있지만 모든 기법을 한 개의 DSL에 합쳐 장점만을 누릴 수 있음
* 많은 자바 프레임워크와 라이브러리를 DSL을 통해 이용할 수 있음
  * jOOQ, 큐컴버, 엔터프라이즈 통합 패턴을 구현한 스프링 통합
  
### [quiz]
---

## chapter 11 - null 대신 Optional 클래스
* 1965년 토니 호어(Tony Hoare)라는 영국 컴퓨터 과학자가 힙에 할당되는 레코드를 사용하며  
  형식을 갖는 최초의 프로그래밍 언어 중 하나인 알골을 설계하면서 처음 null 참조가 등장

### 값이 없는 상황을 어떻게 처리할까?
* Person, Car, Insurance 데이터 모델
  ```
  public class Person {
      private Car car;
  
      public Car getCar() {
          return car;
      }
  }
  public class Car {
      private Insurance insurance;
  
      public Insurance getInsurance() {
          return insurance;
      }
  }
  public class Insurance {
      private String name;
  
      public String getName() {
          return name;
      }
  }
  ```
* 문제?
  ```
  public String getCarInsuranceName(Person person) {
      return person.getCar().getInsurance().getName();
  }
  ```

* 보수적인 자세로 NullPointerException 줄이기
  * null 확인 코드를 추가하여 NullPointerException 줄이는 코드
  * null 안전 시도 : 깊은 의심
    ```
	// null 확인 코드 때문에 나머지 호출 체인의 들여쓰기 수준이 증가함
	public static String getCarInsuranceNameNullSafe(Person person) {
		if (person != null) {
			Car car = person.getCar();
			if (car != null) {
				Insurance insurance = car.getInsurance();
				if (insurance != null) {
					return insurance.getName();
				}
			}
		}
		return "Unknown";
	}
    ```
    * 위 코드에서는 변수를 참조할때마다 null 확인, 중간 과정에 하나라도 null 참조가 있으면 'Unknown' 문자열 반환
    * 상식적으로 모든 회사에는 이름이 있으므로 보허회사의 이름이 null인지 확인하지 않음
    * 확실히 알고 있는 영역을 모델링할 때는 이런 지식을 활용, null 확인을 생략할 수 있음
      * 하지만 데이터를 자바 클래스로 모델링할 때는 이같은 사실을 단정하기 어려움
    * 모든 변수가 null인지 의심하므로 변수를 접근할 때마다 중첩된 if 추가
      * 이와 같은 반복 패턴(recurring pattern) 코드를 '깊은 의심'이라고 부름
      * 변수가 null인지 의심되어 중첩 if 블록을 추가하면 코드 들여쓰기 수준이 증가함
      * 코드 구조 엉망, 가독성 떨어짐
  * null 안전 시도 : 너무 많은 출구
    ```
	// null 확인 코드마다 출구가 생김
	public String getCarInsuranceNameNullSafeV2(Person person) {
		if (person == null) {
			return "Unknown";
		}

		Car car = person.getCar();
		if (car == null) {
			return "Unknown";
		}

		Insurance insurance = car.getInsurance();
		if (insurance == null) {
			return "Unknown";
		}

		return insurance.getName();
	}
    ```
    * 여러 개의 출구로 인해 유지보수가 어려워지기 때문에 좋은 코드가 아님
    * 게다가 null일 때 반환되는 기본값 'Unknown'이 반복, 같은 문자열을 반복하면서 오타 등의 실수 발생(상수를 만들어 사용함으로 해결은 가능)

* null 때문에 발생하는 문제
  * 이론적, 실용적 문제 확인
    * 에러의 근원
      * NullPointerException은 자바에서 가장 흔히 발생하는 에러
    * 코드를 어지럽힘
      * 때로는 중첩된 null 확인 코드를 추가해야 하므로 코드 가독성이 떨어짐
    * 아무 의미가 없음
      * null은 아무 의미도 표현하지 않음
      * 특히 정적 형식 언어에서 값이 없음을 표현하는 방법으로는 적절하지 않음
    * 자바 철학에 위배됨
      * 자바는 개발자로부터 포인터를 숨겼음
      * 하지만 null 포인터는 예외
    * 형식 시스템에 구멍을 만듦
      * null은 무형식이며 정보를 포함하고 있지 않으므로 모든 참조 형식에 null을 할당할 수 있음
      * 이런식으로 null이 할당되기 시작하면서 시스템의 다른 부분으로 null이 퍼졌을 때 애초에 null이 어떤 의미로 사용되었는지 알 수 없음

* 다른 언어는 null 대신 무얼 사용하나?
  * 그루비는 안전 내비게이션 연산자 (safe navigation operator) (?.)를 도입하여 null 문제 해결
    * 사람들이 그들의 자동차에 적용한 보험 회사의 이름을 가져오는 그루비 예제
      ``` def carInsuranceName = person?.car?.insurance?.name ```
    * 이 때 호출 체인에 null인 참조가 있으면 결과로 null 반환됨
  * 자바 7에서도 비슷한 제안이 있었으나 채택되지 않음
    * 모든 자바 개발자가 안전 내비게이션 연산자를 간절히 원하지 않음
    * NullPointerException 발생시 null을 확인하는 if문을 추가해서 문제를 쉽게 해결 가능
    * null 예외를 해결할 수 있지만 이는 문제의 본질을 해결하는 것이 아니라 문제를 뒤로 미루고 숨기는 것이나 마찬가지
  * 하스켈, 스칼라 등의 함수형 언어는 아예 다른 관점에서 null 문제 접근
    * 하스켈은 선택형값(optional value)을 저장할 수 있는 Maybe라는 형식 제공
      * Maybe는 주어진 형식의 값을 갖거나 아무 값도 갖지 않을 수 있음
      * 따라서 null 참조 개념은 자연스럽게 사라짐
    * 스칼라도 T 형식의 값을 갖거나 아무 값을 가지지 않을 수 있는 Option[T]라는 구조를 제공
      * 그리고 Option 형식에서 제공하는 연산을 사용해서 값이 있는지 여부를 명시적으로 확인해야 함(null 확인)
      * 형식 시스템에서 이를 강제하므로 null과 관련한 문제가 일어날 가능성이 줄어듬
  * 자바 8은 선택형값 개념의 영향을 받아서 java.util.Optional\<T>라는 새로운 클래스 제공

### Optional 클래스 소개
* 자바 8은 하스켈, 스칼라의 영향을 받아 Optional\<T>라는 새로운 클래스 제공
* Optional 클래스는 선택형값을 캡슐화하는 클래스
* 값이 있으면 Optional 클래스는 값을 감싸고 없으면 Optional.empty 메서드로 Optional을 반환
* Optional.empty는 Optional의 특별한 싱글턴 인스턴스를 반환하는 정적 팩토리 메서드
* null 참조와 Optional.empty()는 의마상으론 비슷하지만 차이점이 많음
* Optional\<Car>
  * 이는 값이 없을 수 있음을 명시적으로 보여줌
  * Car는 null 참조가 할당될 수 있는데 이것이 올바른 값인지 잘못된 값인지 판단할 아무 정보가 없음
  * 예제
    ```
    public class Person {
    	// 사람이 차를 소유했을 수도 아닐 수도 있으므로 Optional 정의
    	private Optional<Car> car;
    
    	public Optional<Car> getCar() {
    		return car;
    	}
    }
    public class Car {
    	// 자동차가 보험에 가입되어 있을 수도 아닐 수도 있으므로 Optional 정의
    	private Optional<Insurance> insurance;
    
    	public Optional<Insurance> getInsurance() {
    		return insurance;
    	}
    }
    public class Insurance {
    	// 보험 회사에는 반드시 이름이 있음
    	private String name;
    
    	public String getName() {
    		return name;
    	}
    }
    ```
    * Optional 클래스를 사용하면서 모델의 의미(semantic)가 더 명확해졌음을 확인할 수 있음
      * Person은 Optional\<Car>를 참조, Car는 Optional\<Insurance>를 참조하는데
      * 이는 사람이 차를 소유했을 수도 아닐 수도 있으며
      * 자동차가 보험에 가입되어 있을 수도 아닐 수도 있음을 명확히 설명함
      * 또한 보험 회사 이름은 Optional\<String>이 아닌 String 형식으로 선언됨
        * 보험 회사는 반드시 이름을 가져야 함을 보여줌
        * 따라서 보험 회사 이름을 참조할 때 NullPointerException이 발생할 수도 있다는 정보를 확인할 수 있음
        * 하지만 null 확인 코드를 추가할 필요는 없음
          * 고쳐야 할 문제를 감추는 꼴이 되기 때문
        * 보험 회사는 반드시 이름을 가져야 하며 이름이 없는 보험 회사를 발견했다면 이름이 없는 이유를 밝혀 문제를 해결해야 함
  * Optional을 이용하면 값이 없는 상황이 데이터에 문제가 있는 것인지, 알고리즘의 버그인지 명확하게 구분 가능
    * 모든 null 참조를 Optional로 대치하는 것은 바람직하지 않음
    * Optional의 역할은 더 이해하기 쉬운 API를 설계하도록 돕는 것
    * 즉, 메서드의 시그니처만 보고도 선택형값인지 여부를 구별할 수 있음
    * Optional이 등장하면 이를 언랩해서 값이 없을 수 있는 상황에 적절하게 대응하도록 강제하는 효과가 있음

### Optional 적용 패턴
* Optional 객체 만들기
  * 빈 Optional
    * 정적 팩토리 메서드 Optional.empty로 빈 Optional 객체를 얻을 수 있음
      ``` Optional<Car> optCar = Optional.empty(); ```
  * null이 아닌 값으로 Optional 만들기
    * 정적 팩토리 메서드 Optional.of로 null이 아닌 값을 포함하는 Optional을 만들 수 있음
      ``` Optional<Car> optCar = Optional.of(car); ```
      * 이제 car가 null이라면 즉시 NullPointerException이 발생함
      * Optional을 사용하지 않았다면 car의 프로퍼티에 접근하려 할 때 에러가 발생했을 것
  * null값으로 Optional 만들기
    * 정적 팩토리 메서드 Optional.ofNullable로 null값을 저장할 수 있는 Optional을 만들 수 있음
      ``` Optional<Car> optCar = Optional.ofNullable(car); ```
      * car가 null이면 빈 Optional 객체가 반환됨
  * Optional이 비어 있으면 get을 호출했을 때 예외가 발생함
  * Optional을 잘못 사용하면 null을 사용했을 때와 같은 문제를 겪을 수 있음
  * Optional에서 제공하는 기능이 스트림 연산에서 영감을 받았음

* 맵으로 Optional의 값을 추출하고 변환하기
  * 예를 들어 보험 회사의 이름을 추출한다고 가정
    ```
    String name = null;
    if (insurance != null) {
        name = insurance.getName();
    }
    ```
    * 이런 유형의 패턴에 사용할 수 있또록 Optional은 map 메서드를 지원함
      ```
      Optional<Insurance> optInsurance = Optional.ofNullalbe(insurance);
      Optional<String> name = optInsurance.map(Insurance::getName);
      ```
    * Optional의 map 메서드는 스트림의 map 메서드와 개념적으로 비슷함
      * 스트림의 map은 스트림의 각 요소에 제공된 함수를 적용하는 연산
      * 여기서 Optional 객체를 최대 요소의 개수가 한 개 이하인 데이터 컬렉션으로 생각할 수 있음
      * Optional이 값을 포함하면 map의 인수로 제공된 함수가 값을 바꿈
      * Optional이 비어 있으면 아무 일도 일어나지 않음

* flatMap으로 Optional 객체 연결
  * map을 이용해 코드 재구현
    ```
    Optional<Person> optPerson = Optional.of(person);
    Optional<String> name =
            optPerson.map(Person::getCar)
                    .map(Car::getInsurance)
                    .map(Insurance::getName);
    return name.orElse("Unknown");
    ```
    * 위 코드는 컴파일 되지 않음
      * optPerson의 형식은 Optional\<Person>이므로 map 메서드를 호출할 수 있음
      * 하지만 getCar는 Optional\<Car> 형식의 객체를 반환
      * 즉, map 연산의 결과는 Optional<Optional\<Car>> 형식의 객체
      * getInsurance는 또 다른 Optional 객체를 반환하므로 getInsurance 메서드를 지원하지 않음
    * flatMap 메서드로 해결
      * 스트림의 flatMap은 함수를 인수로 받아서 다른 스트림을 반환하는 메서드
      * 보통 인수로 받은 함수를 스트림의 각 요소에 적용하면 스트림의 스트림이 만들어짐
      * 하지만 flatMap은 인수로 받은 함수를 적용해서 생성된 각각의 스트림에서 콘텐츠만 남김
      * 즉, 함수를 적용해서 생성한 모든 스트림의 하나의 스트림으로 병합되어 평준화됨

* Optional로 자동차의 보험 회사 이름 찾기
  * flatMap
    ```
	public String getCarInsuranceName(Optional<Person> person) {
		return person.flatMap(Person::getCar)
				.flatMap(Car::getInsurance)
				.map(Insurance::getName)
				.orElse("Unknown");
	}
    ```
    * null을 확인하느라 조건 분기문을 추가해서 코드를 복잡하게 만들지 않으면서도 쉽게 이해할 수 있는 코드
    * Optional을 사용하므로 도메인 모델과 관련한 암묵적인 지식에 의존하지 않고도 명시적으로 형식 시스템을 정의할 수 있음
    * 정확한 정보 전달은 언어의 가장 큰 목표중 하나
    * Optional을 인수로 받거나, 반환하는 메서드를 정의한다면
    * 결과적으로 이 메서드를 사용하는 모든 이에게 이 메서드가 빈 값을 받거나 빈 결과를 반환할 수 있음을 잘 문서화 해서 제공하는 것과 같음

* Optional을 이용한 Person/Car/Insurance 참조 체인
  * Person을 Optional로 감싼 다음, flatMap(Person::getCar)를 호출
    * 이 호출을 두 단계의 논리적 과정으로 생각할 수 있음
      * 첫 번째 단계에서는 Optional 내부의 Person에 Function을 적용함
        * 여기서는 Person의 getCar 메서드가 Function
        * getCar 메서드는 Optional\<Car>를 반환, Optional 내부의 Person이 Optional\<Car>로 변환되면서 중첩 Optional이 생성됨
        * 따라서 flatMap 연산으로 Optional을 평준화함
        * 평준화 과정이란 이론적으로 두 Optional을 합치는 기능을 수행하면서 둘 중 하나라도 null이면 빈 Optional을 생성하는 연산
        * fflatMap을 빈 Optional에 호출하면 아무일도 일어나지 않고 그대로 반환됨
        * 반면 Optional이 Person을 감싸고 있다면 flatMap에 전달된 Function이 Person에 적용됨
        * Function을 적용한 결과가 이미 Optional이므로 flatMap 메서드는 결과를 그대로 반환할 수 있음
      * 두 번째 단계도 Optional\<Car>를 Optional\<Insurance>로 변환함
      * 세 번째 단계에서는 Optional<Insurance>를 Optional<String>으로 변환함
      * Insurance.getName()은 String을 반환하므로 flatMap을 사용할 필요가 없음
  * 호출 체인 중 어떤 메서드가 빈 Optional을 반환한다면 전체 결과로 빈 Optional을 반환
    * 아니면 관련 보험 회사의 이름을 포함하는 Optional을 반환

* 도메인 모델에 Optional을 사용했을 때 데이터를 직렬화할 수 없는 이유
  * 예제에서 Optional로 우리 도메인 모델에서 값이 꼭 있어야 하는지 아니면 값이 없을 수 있는 지 여부를 구체적으로 표현할 수 있었음
    * Optional 클래스 설계자는 이와는 다른 용도로만 Optional 클래스를 사용할 것을 가정함
  * 자바 언어 아키텍트인 브라이언 고츠(Brian Goetz)는 Optional 용도가 선택형 반환값을 지원하는 것이라고 명확하게 정의함
  * Optional 클래스는 필드 형식으로 사용할 것을 가정하지 않았으므로 Serializable 인터페이스를 구현하지 않음
    * 따라서 우리 도메인 모델에 Optional을 사용한다면 직렬화 모델을 사용하는 도구나 프레임워크에 문제가 생길 수 있음
  * 이와 같은 단점에도 불구하고 여전히 Optional을 사용해서 도메인 모델을 구성하는 것이 바람직하다고 생각함
    * 특히 객체 그래프에서 일부 또는 전체 객체가 null일 수 있는 상황이라면 더욱 바람직
  * 직렬화 모델이 필요하다면 Optional로 값을 반환 받을 수 있는 메서드를 추가하는 방식을 권장함
    ```
    public class Person {
        private Car car;
        public Optional<Car> getCarAsOptional() {
            return Optional.ofNullable(car);
        }
    }
    ```

* Optional 스트림 조작
  * List\<Person>을 인수로 받아 자동차를 소유한 사람들이 가입한 보험 회사의 이름을 포함하는 Set\<String>을 반환하는 메서드 구현
    ```
    public Set<String> getCarInsuranceNames(List<Person> persons) {
        return persons.stream()
                .map(Person::getCar)
                .map(optCar -> optCar.flatMap(Car::getInsurance))
                .map(optInsurance -> optInsurance.map(Insurance::getName))
                .flatMap(Optional::stream)
                .collect(toSet());
    }
    ```
    * 보통 스트림 요소를 조작하려면 반환, 필터 등의 일련의 여러 긴 체인이 필요한데 이 예제는 Optional로 값이 감싸 있으므로 이 과정이 더 복잡해짐
    * 예제에서 getCar() 메서드가 단순히 Car가 아니라 Optional\<Car>를 반환
      * 사람이 자동차를 가지지 않을 수도 있는 상황임을 자각
      * 따라서 첫 번째 map 변환을 수행하고 Stream<Optional\<Car>>를 얻음
      * 이어지는 두 개의 map 연산을 이용해 Optional\<Car>를 Optional\<Insurance>로 변환
        * 그 다음 스트림이 아니라 각각의 요소에 했던 것처럼 각각을 Optional\<String>로 변환
      * 세 번의 변환 과정을 거쳐 Stream<Optional\<String>>을 얻음
        * 사람이 차를 갖고 있지 않거나 또는 차가 보험에 가입되어 있지 않아 결과가 비어있을 수 있음
        * Optional 덕분에 이런 종류의 연산을 null 걱정없이 안전하게 처리할 수 있음
        * 하지만 마지막 결과를 얻으려면 빈 Optional을 제거하고 값을 언랩해야 하는 것이 문제
        * filter, map을 순서적으로 이용해 결과를 얻음
          ```
          Stream<Optional<String>> stream = ...
          Set<String> result = stream
                          .filter(Optional::isPresent)
                          .map(Optional::get)
                          .collect(toSet());
          ```
          * Optional 클래스의 stream() 메서드를 이용하면 한 번의 연산으로 같은 결과를 얻을 수 있음
          * 이 메서드는 각 Optional이 비었는지 아닌지에 따라 Optional을 0개 이상의 항목을 포함하는 스트림으로 변환함
          * 따라서 이 메서드의 참조를 스트림의 한 요소에서 다른 스트림으로 적용하는 함수로 볼 수 있으며  
            이를 원래 스트림에 호출하는 flatMap 메서드로 전달할 수 있음
          * 이런 방법으로 스트림의 요소를 두 수준인 스트림의 스트림으로 변환하고 다시 한 수준인 평면 스트림으로 바꿀 수 있음
          * 이 기법을 이용하면 한 단계의 연산으로 값을 포함하는 Optional을 언랩하고 비어있는 Optional은 건너뛸 수 있음

* 디폴트 액션과 Optional 언랩
  * get()
    * 값을 읽는 가장 간단한 메서드면서 동시에 가장 안전하지 않은 메서드
    * 매핑된 값이 있으면 해당 값을 반환, 없으면 NoSuchElementException을 발생
    * 따라서 Optional에 값이 반드시 있다고 가정할 수 있는 상황이 아니면 사용하지 않는 것이 바람직함
    * 결국 이 상황은 중첩된 null 확인 코드를 넣는 상황가 다르지 않음
  * orElse(T other)
    * Optional이 값을 포함하지 않을 때 기본값을 제공할 수 있음
  * orElseGet(Supplier<? extends T> other)
    * orElse 메서드의 게으른 버전
    * Optional에 값이 없을 때만 Supplier가 실행되기 때문
    * 디폴트 메서드를 만드는 데 시간이 걸리거나(효율성 때문) Optional이 비어 있을 때만 기본값을 생성하고 싶다면(기본값이 꼭 필요한 상황) 사용
  * orElseThrow(Supplier<? extends X> exceptionSupplier)
    * Optional이 비어있을 때 예외를 발생시킨다는 점에서 get 메서드와 비슷
    * 하지만 이 메서드는 발생시킬 예외의 종류를 선택할 수 있음
  * ifPresent(Consumer<? super T> consumer)
    * 값이 존재할 때 인수로 넘겨준 동작을 실행할 수 있음
    * 값이 없으면 아무일도 일어나지 않음
  * ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) - java 9 추가
    * Optional이 비었을 때 실행할 수 있는 Runnable을 인수로 받는다는 점만 ifPresent()와 다름

* 두 Optional 합치기
  * 가장 저렴한 보험료를 제공하는 보험 회사를 찾는 몇몇 복잡하는 비즈니스 로직을 구현한 외부 서비스가 있다고 가정
    ```
    public Insurance findCheapestInsurance(Person person, Car car) {
        // 다른 보험사에서 제공한 질의 서비스
        // 모든 데이터 비교
        return cheapestCompany;
    }
    ```
    * 이제 두 Optional을 인수로 받아 Optional\<Insurance>를 반환하는 null 안전 버전(null safe version)의 메서드를 구현 해야 한다고 가정
      * 인수로 전달한 값 중 하나라도 비어 있으면 빈 Optional\<Insurance>를 반환
      * Optional 클래스는 Optional이 값을 포함하는지 여부를 알려주는 isPresent라는 메서드도 제공함
        * isPresent 이용하여 구현
          ```
          public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
              if (person.isPresent() && car.isPresent()) {
                  return Optional.of(findCheapestInsurance(person.get(), car.get()));
              } else {
                  return Optional.empty();
              }
          }
          ```
          * 이 메서드의 장점은 person과 car의 시그니처만으로 둘 다 아무 값도 반환하지 않을 수 있다는 정보를 명시적으로 보여줌
          * 구현 코드는 null 확인 코드와 크게 다른 점이 없음

* 필터로 특정값 거르기
  * 종종 객체의 메서드를 호출해서 어떤 프로퍼티를 확인 해야 할 때가 존재함
    * 예를 들어 보험 회사 이름이 'CambridgeInsurance'인지 확인 해야 한다고 가정
    * Insurance 객체가 null인지 여부를 확인한 후 getName 메서드를 호출 해야함
      ```
      Insurance insurance = ...;
      if (insurance != null && "CambridgeInsurance".equals(insurance.getName())) {
          System.out.println("ok");
      }
      ```
      * Optional 객체에 filter 메서드를 이용, 재구현
        ```
        Optional<Insurance> optInsurance = ...;
        optInsurance.filter(insurance -> "CambridgeInsurance".equals(insurance.getName()))
                .ifPresent(x -> System.out.println("OK")); 
        ```
        * filter 메서드는 프레디케이트를 인수로 받음
        * Optional 객체가 값을 가지며 프레디케이트와 일치하면 filter 메서드는 그 값을 반환, 그렇지 않으면 빈 Optional 객체를 반환
        * Optional은 최대 한 개의 요소를 포함할 수 있는 스트림과 같음
        * Optional이 비어있다면 filter 연산은 아무 동작도 하지 않고, 값이 있으면 그 값에 프레디케이트를 적용함
        * 프레디케이트 적용 결과가 true면 Optional에는 아무 변화도 일어나지 않고 false면 값은 사라지고 Optional은 빈 상태가 됨

* Optional 클래스의 메서드
  * empty
    * 빈 Optional 인스턴스 반환
  * filter
    * 값이 존재하며 프레디케이트와 일치하면 값을 포함하면 Optional을 반환
    * 값이 없거나 프레디케이트와 일차히지 않으면 빈 Optional을 반환
  * flatMap
    * 값이 존재하면 인수로 제공된 함수를 적용한 결과 Optional을 반환
    * 값이 없으면 빈 Optional을 반환
  * get
    * 값이 존재하면 Optional이 감싸고 있는 값을 반환
    * 값이 없으면 NoSuchElementException이 발생함
  * ifPresent
    * 값이 존재하면 지정된 Customer를 실행
    * 값이 없으면 아무일도 일어나지 않음
  * ifPresentOrElse
    * 값이 존재하면 Customer를 실행
    * 값이 없으면 Runnable를 실행
  * isPresent
    * 값이 존재하면 true 반환
    * 값이 없으면 false 반환
  * map
    * 값이 존재하면 제공된 매핑 함수를 적용함
  * of
    * 값이 존재하면 값을 감싸는 Optional읇 반환
    * 값이 null이면 NullPointerException 발생
  * ofNullable
    * 값이 존재하면 값을 감싸는 Optional읇 반환
    * 값이 null이면 빈 Optional을 반환 
  * or
    * 값이 존재하면 같은 Optional읇 반환
    * 값이 없으면 Supplier에서 만든 Optional을 반환 
  * orElse
    * 값이 존재하면 값을 반환
    * 값이 없으면 기본값을 반환
  * orElseGet
    * 값이 존재하면 값을 반환
    * 값이 없으면 Supplier에서 제공하는 값을 반환 
  * orElseThrow
    * 값이 존재하면 값을 반환
    * 값이 없으면 Supplier에서 생성한 예외를 발생
  * stream
    * 값이 존재하면 존재하는 값만 포함하는 스트림 반환
    * 값이 없으면 빈 스트림 반환

### Optional을 사용한 실용 예제
* 새 Optional 클래스를 효과적으로 이용하려면 잠재적으로 존재하지 않는 값의 처리 방법을 바꿔야 함
* 코드 구현만 바꾸는 것이 아니라, 네이티브 자바 API와 상호작용하는 방식도 바꿔야 함
* 호환성을 유지하다보니 기존 자바 API는 Optional을 적절하게 활용하지 못하고 있음
* Optional 기능을 활용할 수 있도록 우리 코드에 작은 유틸리티 메서드를 추가하는 방식으로 이 문제를 해결 가능

* 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기
  * 기존 자바 API에서는 null을 반환하면서 요청한 값이 없거나 어떤 문제로 계산에 실패했음을 알림
    * 예를 들어 Map의 get 메서드는 요청한 키에 대응하는 값을 찾지 못했을 때 null을 반환
    * Optional로 감싸서 이를 개선
    * 첫 번째, 기존처럼 if-then-else를 추가
    * 두 번째, get 메서드의 반환값을 ofNullable을 이용하여 Optional로 감쌈
      ```
      Optional<Object> value = Optional.ofNullable(map.get("key"));
      ```

* 예외와 Optional 클래스
  * 자바 API는 어떤 이유에서 값을 제공할 수 없을 때 null을 반환하는 대신 예외를 발생시킬 때도 있음
    * 전형적인 예가 문자열을 정수로 변환하는 정적 메서드 Integer.parseInt(String)
      * 이 메서드는 문자열을 정수로 변환하지 못할 때 NumberFormatException을 발생시킴
      * 즉, 문자열이 숫자가 아니라는 사실을 예외로 알림
    * parseInt가 Optional을 반환하도록 모델링
      * 유틸리티 구현
        ```
        public static Optional<Integer> stringToInt(String s) {
            try {
                // 문자열을 정수로 변환할 수 있으면 정수로 변환된 값을 포함하는 Optional을 반환
                return Optional.of(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // 그렇지 않으면 빈 Optional을 반환
                return Optional.empty();
            }
        }
        ```

* 기본형 Optional을 사용하지 말아야 하는 이유
  * 스트림처럼 Optional도 기본형으로 특화된 OptionalInt, OptionalLong, OptionalDouble 등의 클래스 제공
    * 예를 들어 Optional\<Integer> 대신 OptionalInt를 반환할 수 있음
  * 스트림이 많은 요소를 가질 때는 기본형 특화 스트림을 이용해서 성능을 향상시킬 수 있음
  * 하지만 Optional의 최대 요소 수는 한 개이므로 Optional은 기본형 특화 클래스로 성능을 개선할 수 없음
  * 기본형 특화 클래스는 map, flatMap, filter 등을 지원하지 않아 기본형 특화 클래스 사용을 권장하지 않음
  * 기본형 특화 클래스는 일반 Optional과 혼용할 수 없음
    * 예를 들어 OptionalInt를 반환한다면 이를 다른 Optional의 flatMap에 메서드 참조로 전달할 수 없음

* 응용
  * 예를 들어 프로그램의 설정 인수로 Properties를 전달한다고 가정
    ```
    Properties props = new Properties();
    props.setProperty("a", "5");
    props.setProperty("b", "true");
    props.setProperty("c", "-3");
    ```
    * 이제 프로그램에서는 Properties를 읽어서 값을 초 단위의 지속 시간으로 해석함
      * 다음과 같은 메서드 시그니처로 지속 시간 읽을 것
        ``` public int readDuration(Properties props, String name) ```
      * 지속 시간은 양수여야 하므로 문자열이 양의 정수를 가리키면 해당 정수를 반환하지만 그 외는 0을 반환
        * 이를 Junit 어설션(assertion)으로 구현
          ```
          assertEquals(5, readDurationImperative(props, "a"));
          assertEquals(0, readDurationImperative(props, "b"));
          assertEquals(0, readDurationImperative(props, "c"));
          assertEquals(0, readDurationImperative(props, "d"));
          ```
          * 어설션 의미
            * 프로퍼티 'a'는 양수로 변환할 수 있는 문자열을 포함하므로 readDuration 5를 반환
            * 프로퍼티 'b'는 양수로 변환할 수 없는 문자열을 포함하므로 0 반환
            * 프로퍼티 'c'는 음수 문자열을 포함하므로 0 반환
            * 프로퍼티 'd'라는 이름의 프로퍼티는 없으므로 0 반환

### 정리
* 역사적으로 프로그래밍 언어에서는 null 참조로 값이 없는 상황을 표현해옴
* 자바 8에서는 값이 있거나 없음을 표현할 클래스 java.util.Optional\<T>를 제공함
* 팩토리 메서드 Optional.empty, Optional.of, Optional.ofNullable 등을 이용해 Optional 객체를 만들 수 있음
* Optional 클래스는 스트림과 비슷한 연산을 수행하는 map, flatMap, filter 등의 메서드를 제공함
* Optional로 값이 없는 상황을 적절하게 처리하도록 강제할 수 있음
  * 즉, Optional로 예상치 못한 null 예외를 방지할 수 있음
* Optional을 활용하면 더 좋은 API를 설계할 수 있음
  * 즉, 사용자는 메서드의 시그니처만 보고도 Optional값이 사용되거나 반환되는지 예측 가능

### [quiz]
* map, flatMap 메서드를 이용, 기존의 nullSafeFindCheapestInsurance() 메서드를 한줄의 코드로 재구현하기
  ```
  public Optional<Insurance> nullSafeFindCheapestInsurance(
  		Optional<Person> person, Optional<Car> car) {
      return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
  }
  ```
  * 첫 번째 Optional에 flatMap을 호출했으므로 첫 번째 Optional이 비어있다면 인수로 전달한 람다 표현식이 실행되지 않고 빈 Optional 반환
  * 반면 person값이 있으면 flatMap 메서드에 필요한 Optional\<Insurance>를 반환하는 Function의 입력으로 person을 사용함
  * 이 함수의 바디에서는 두 번째 Optional에 map을 호출하므로 Optional이 car값을 포함하지 않으면 Function이 빈 Optional을 반환
  * 결국 nullSafeFindCheapestInsurance 메서드는 빈 Optional을 반환
  * 마지막으로 person, car가 모두 존재하면 map 메서드로 전달한 람다 표현식이 findCheapestInsurance 메서드를 안전하게 호출 가능

* 다음 시그니처를 이용, getCarInsuranceName 메서드 고치기
  ``` public String getCarInsuranceName(Optional<Person> person, int minAge) ```
  * 정답
    ```
    public String getCarInsuranceName(Optional<Person> person, int minAge) {
        return person.filter(p -> p.getAge() >= minAge)
                .flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
    }
    ```
    * 인수 minAge 이상의 나이를 먹은 사람만 선택하도록 프레디케이트를 설정해서 filter 메서드에 전달하는 방식으로 Optional에서 person 필터링 가능

* Optional클래스의 기능과 유틸리티 메서드를 이용해서 위 얘제 프로퍼티에서 지속 시간 읽기의 명령형 코드를 하나의 유연한 코드로 재구현하기
  ```
  public int readDuration(Properties props, String name) {
  	// Properties.getProperty(String)은 null을 반환하므로
  	// ofNullable 팩토리 메서드를 이용 Optional을 반환하도록 변경 가능
  	return ofNullable(props.getProperty(name))
  			// OptionalUtility 클래스 stringToInt 유틸리티 메서드 참조를 전달
  			// Optional<String>을 Optional<Integer>로 변경 가능
  			.flatMap(ReadPositiveIntParam::s2i)
  			// 음수를 필터링해서 제거
  			.filter(i -> i > 0).orElse(0);
  }
  ```
  * 이 과정에서 하나라도 빈 Optional을 반환하면 orElse 메서드에 의해 기본값 0이 반환됨
  * 값을 포함하는 Optional을 반환하면 들어있는 양수를 반환
---

## chapter 12 - 새로운 날짜와 시간 API
* java.util.Date 클래스
  * 날짜를 의미하는 Date라는 클래스명과 달리 특정 시점을 날짜가 아닌 밀리초 단위로 표현함
  * 게다가 1900년을 기준으로 하는 오프셋, 0에서 시작하는 달 인덱스 등 모호한 설계로 유용성이 떨어짐
  * 2017년 9월 21일을 가리키는 Date 인스턴스를 만드는 코드
    ``` Date date = new Date(117, 8, 21); ```
    * 출력 결과
      ``` Thu Sep 21 00:00:00 CET 2017 ```
    * 결과가 직관적이지 않음
    * Date 클래스의 toString으로는 반환되는 문자열을 추가로 활용하기 어려움
    * JVM 기본 시간대인 CET(중앙 유럽 시간대)를 사용함
    * Date 클래스가 자체적으로 시간대 정보를 알고 있는 것도 아님
  * Date 클래스의 문제가 있었지만 과거 버전과 호환성을 유지하면서 해결할 수 있는 방법이 없었음
* java.util.Calendar 클래스를 대안으로 제공
  * 역시 쉽게 에러를 일으키는 설계로 인한 문제
    * 에를 들어 1900년도에서 시작하는 오프셋은 없앴지만 여전히 달의 인덱스는 0부터 시작함
    * Calendar 클래스가 등장하면서 Date 클래스와 함께 개발자에게 혼란을 가중시킴
      * Date, Calendar 중 어떤 것을 사용해야 할지 혼동을 가져옴
    * DateFormat 같은 일부 기능은 Date 클래스에만 작동함
      * DateFormat은 언어 종류와 독립적으로 날짜와 시간의 형식을 조절하고 파싱할 때 사용함
      * DateFormat에도 문제가 있음
        * 스레드에 안전하지 않음
          * 즉 두 스레드가 동시에 하나의 포매터로 날짜를 파싱할 때 예기치 못한 결과 발생할 수 있음
  * Date, Calendar 모두 가변 클래스
    * 유지보수가 어려워짐
* 부실한 날짜와 시간 라이브러리로 인해 많은 개발자는 Joda-Time 같은 서드파티 라이브러리를 사용함
  * java 8에서 Joda-Time의 많은 기능을 java.time 패키지에 추가함

### LocalDate, LocalTime, Instant, Duration, Period 클래스
* java.time 패키지는 LocalDate, LocalTime, Instant, Duration, Period 등 새로운 클래스 제공

* LocalDate, LocalTime 사용
  * LocalDate
    * 시간을 제외한 날짜를 표현하는 불변 객체
    * 어떤 시간대 정보도 포함하지 않음
    * 정적 팩토리 메서드 of로 인스턴스 생성
    * 연도, 달, 요일 등을 반환하는 메서드 제공
      ```
      // 2017-09-21
      LocalDate date = LocalDate.of(2017, 9, 21);
      int year = date.getYear();           // 2017
      Month month = date.getMonth();       // SEPTEMBER
      int day = date.getDayOfMonth();      // 21
      DayOfWeek dow = date.getDayOfWeek(); // THURSDAY
      int length = date.lengthOfMonth();   // 31 (3월의 일 수)
      boolean leap = date.isLeapYear();    // false (윤년이 아님)
      ```
    * 팩토리 메서드 now는 시스템 시계의 정보를 이용해 현재 날짜 정보를 얻음
      ``` LocalDate today = LocalDate.now(); ```
  * get 메서드에 TemporalField를 전달해 정보를 얻는 방법도 있음
    * TemporalField는 시간 관련 객체에서 어떤 필드의 값에 접근할지 정의하는 인터페이스
  * 열거자 ChronoField는 TemporalField 인터페이스를 정의함
    * 열거자 요소를 이용해 원하는 정보를 얻는 코드
      ```
      int year = date.get(ChronoField.YEAR);
      int month = date.get(ChronoField.MONTH_OF_YEAR);
      int day = date.get(ChronoField.DAY_OF_MONTH);
      ```
    * 내장 메서드 getYear(), getMonthValue(), getDayOfMonth() 등을 이용하여 가독성 높임
      ```
      LocalDate date = LocalDate.of(2017, 9, 21);
      int year = date.getYear();
      int month = date.getMonthValue();
      int day = date.getDayOfMonth();
      ```
  * LocalTime
    * 13:45:20 같은 시간 표현
    * 오버로드 버전의 두가지 정적 메서드 of로 LocalTime 인스턴스 생성
      * 시간과 분을 인수로 받는 of 메서드
      * 시간, 분, 초를 인수로 받는 of 메서드
    * LocalDate 클래스처럼 다음과 같은 게터 메서드 제공
      ```
      // 정적 팩토리 메서드 of를 사용
      // 13:45:20
      LocalTime time = LocalTime.of(13, 45, 20);
      int hour = time.getHour();     // 13
      int minute = time.getMinute(); // 45
      int second = time.getSecond(); // 20
      ```
  * parse 정적 메서드로 날짜와 시간 문자열을 통해 LocalDate, LocalTime 인스턴스 생성
    ```
    // 날짜와 시간 문자열로 인스턴스 생성
    LocalDate date = LocalDate.parse("2017-09-21");
    LocalTime time = LocalTime.parse("13:45:20");
    ```
    * DateTimeFormater를 전달 가능
      * DateTimeFormater의 인스턴스는 날짜, 시간 객체의 형식을 지정함
      * DateTimeFormater는 이전에 java.util.DateFormat 클래스를 대체하는 클래스
    * 문자열을 LocalDate, LocalTime으로 파싱할 수 없을 때 DateTimeParseException(RuntimeException 상속) 예외 발생

* 날짜와 시간 조합
  * LocalDateTime
    * LocalDate, LocalTime을 쌍으로 갖는 복합 클래스
    * 날짜와 시간을 모두 표현
    * 직접 만들거나 날짜와 시간을 조합하는 방법
      ```
      LocalDate date = LocalDate.of(2017, 9, 21);
      LocalTime time = LocalTime.of(13, 45, 20);
  
      // 2017-09-21T13:45:20
      LocalDateTime dt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
      LocalDateTime dt2 = LocalDateTime.of(date, time);
      LocalDateTime dt3 = date.atTime(13, 45, 20);
      LocalDateTime dt4 = date.atTime(time);
      LocalDateTime dt5 = time.atDate(date);
      ```
      * LocalDate의 atTime 메서드에 시간을 제공하거나 LocalTime의 atDate 메서드에 날짜를 제공하는 방법
    * LocalDateTime의 toLocalDate, toLocalTime 메서드로 LocalDate, LocalTime 인스턴스 추출 가능
      ```
      LocalDate date1 = dt1.toLocalDate();
      LocalTime time1 = dt1.toLocalTime();
      ```

* Instant 클래스 : 기계의 날짜와 시간
  * 기계에서는 주, 날짜, 시간, 분으로 날짜와 시간을 계산하기 어려움
  * 기계의 관점에서는 연속된 시간에서 특정 지점을 하나의 큰 수로 표현하는 것이 가장 자연스러운 시간 표현 방법
  * java.time.Instant 클래스에서는 이와 같은 기계적 관점에서 시간을 표현
    * 유닉스 에포크 시간(Unix ephoch time - 1970년 1월 1일 0시 0분 0초 UTC)을 기준으로 특정 지점까지의 시간을 초로 표현함
  * 팩토리 메서드 ofEpochSecond에 초를 넘겨줘 Instant 클래스 인스턴스를 만들 수 있음
  * Instant 클래스는 나노초(10억분의 1초)의 정밀도를 제공함
  * 또한 오버로드된 ofEpochSecond 메서드 버전에서는 두 번째 인수를 이용해서 나노초 단위로 시간을 보정 가능
    * 두 번째 인수에는 0 ~ 999,999,999 사이의 값 지정 가능
    * 따라서 다음 네 가지 ofEpochSecond 호출 코드는 같은 Instant를 반환
      ```
      Instant.ofEpochSecond(3);
      Instant.ofEpochSecond(3, 0);
      // 2초 이후의 1억 나노초(1초)
      Instant.ofEpochSecond(2, 1_000_000_000);
      // 4초 이전의 1억 나노초(1초)
      Instant.ofEpochSecond(4, -1_000_000_000);
      ```
  * 사람이 확인할 수 있도록 시간을 표시해주는 정적 팩토리 메서드 now 제공
    * 하지만 Instant 클래슨느 기계 전용 유틸리티
    * 즉 초와 나노초 정보를 포함함
    * 다음 코드는 예외를 일으킴
      ```
      int day = Instant.now().get(ChronoField.DAY_OF_MONTH);
      ```
      * java.time.temporal.UnsupportedTemporalTypeException: Unsupported field: DayOfMonth
  * Instant에서는 Duration과 Period 클래스를 함께 활용 가능

* Duration, Period 정의
  * 지금까지 본 모든 클래스는 Temporal 인터페이스를 구현
    * Temporal 인터페이스는 특정 시간을 모델링하는 객체의 값을 어떻게 읽고 조작할지 정의함
  * 두 시간 객체 사이의 지속시간 duration
    * Duration 클래스의 정적 팩토리 메서드 between으로 두 시간 객체 사이의 지속시간을 만들 수 있음
    * 두 개의 LocalTime, 두 개의 LocalDateTime, 또는 두 개의 Instant로 Duration을 만들 수 있음
      ```
      LocalTime time1 = LocalTime.of(13, 45, 20);
      LocalTime time2 = LocalTime.of(13, 55, 20);
  
      LocalDateTime ldt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
      LocalDateTime ldt2 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 55, 20);
  
      Instant instant1 = Instant.ofEpochSecond(3);
      Instant instant2 = Instant.ofEpochSecond(4);
  
      Duration d1 = Duration.between(time1, time2);
      Duration d2 = Duration.between(ldt1, ldt2);
      Duration d3 = Duration.between(instant1, instant2);
      ```
    * Duration 클래스는 초와 나노초로 시간 단위를 표현하므로 between 메서드에 LocalDate를 전달할 수 없음
  * LocalDateTime은 사람이, Instant는 기계가 사용하도록 만들어진 클래스로 두 인스턴스는 서로 혼합할 수 없음
  * 년, 월, 일로 시간을 표현할 때는 Period 클래스를 사용
    * Period 클래스의 팩토리 메서드 between을 이용하면 두 LocalDate의 차이를 확인할 수 있음
      ```
      Period tenDays = Period.between(
              LocalDate.of(2017, 9, 11),
              LocalDate.of(2017, 9, 21));
      ```
  * Duration, Period는 자신의 인스턴스를 만들 수 있도록 다양한 팩토리 메서드를 지원
    * 두 시간 객체를 사용하지 않고도 Duration, Period 클래스를 만드는 예제
      ```
      Duration threeMinutes1 = Duration.ofMinutes(3);
      Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);
  
      Period tenDays = Period.ofDays(10);
      Period threeWeeks = Period.ofWeeks(3);
      Period twoYearsSixMonthOneDay = Period.of(2, 6, 1);
      ```
  * Duration, Period 클래스가 공통으로 제공하는 메서드 표
    * between
      * 정적 : O
      * 두 시간 사이의 간격을 생성
    * from
      * 정적 : O
      * 시간 단위로 간격을 생성함
    * of
      * 정적 : O
      * 주어진 구성 요소에서 간격 인스턴스를 생성함
    * parse
      * 정적 : O
      * 문자열을 파싱해서 간격 인스턴스를 생성함
    * addTo
      * 정적 : X
      * 현재값의 복사본을 생성한 후, 지정된 Temporal 객체에 추가함
    * get
      * 정적 : X
      * 현재 간격 정보값을 읽음
    * isNegative
      * 정적 : X
      * 간격이 음수인지 확인함
    * isZero
      * 정적 : X
      * 간격이 0인지 확인함
    * minus
      * 정적 : X
      * 현재값에서 주어진 시간을 뺀 복사본을 생성함
    * multipliedBy
      * 정적 : X
      * 현재값에 주어진 값을 곱한 복사본을 생성함
    * negated
      * 정적 : X
      * 주어진 값의 부호를 반전한 복사본을 생성함
    * plus
      * 정적 : X
      * 현재값에 주어진 시간을 더한 복사본을 생성함
    * subtractFrom
      * 정적 : X
      * 지정된 Temporal 객체에서 간격을 뺌

* 지금까지 살펴본 모든 클래스는 불변
  * 불변 클래스는 함수형 프로그래밍, 스레드 안정성과 도메인 모델의 일관성을 유지하는데 좋은 특징
  * 하지만 새로운 날짜와 시간 API에서는 변경된 객체 버전을 만들 수 있는 메서드 제공

### 날짜 조정, 파싱, 포매팅
* withAttribute 메서드로 기존의 LocalDate를 바꾼 버전을 만들 수 있음
* 바뀐 속성을 포함하는 새로운 객체를 반환하는 메서드를 보여주는 예제 코드
  * 모든 메서드는 기존 객체를 바꾸지 않음
  ```
  LocalDate date1 = LocalDate.of(2017, 9, 21);
  LocalDate date2 = date1.withYear(2011);
  LocalDate date3 = date2.withDayOfMonth(25);
  LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 2);
  ```
  * 마지막 행에서 보여주는 것처럼 첫 번째 인수로 TemporalField를 갖는 메서드를 사용하면 범용적으로 메서드를 활용할 수 있음
    * 마지막 with 메서드는 get 메서드와 쌍을 이룸
      * 이들 두 메서드는 날짜와 시간 API의 모든 클래스가 구현하는 Temporal 인터페이스에 정의되어 있음
      * Temporal 인터페이스는 LocalDate, LocalTime, LocalDateTime, Instant처럼 특정 시간을 정의함
      * 정확히 표현하면 get, with 메서드로 Temporal 객체의 필드값을 읽거나 고칠 수 있음
        * with 메서드는 기존의 Temporal 객체를 바꾸는 것이 아니라 필드를 갱신한 복사본을 만듦
        * 이런 과정을 함수형 갱신이라 부름
      * 어떤 Temporal 객체가 지정된 필드를 지원하지 않으면 UnsupportedTemporalTypeException이 발생
        * 예를 들어 Instant에 ChronoField.MONTH_OF_YEAR를 사용시 예외 발생
        * 또는 LocalDate에 ChronoField.NANO_OF_SECOND를 사용시 예외 발생
  * 선언형으로 LocalDate를 사용하는 방법
    * 지정된 시간을 추가, 빼는 예제
      ```
      LocalDate date1 = LocalDate.of(2017, 9, 21);
      LocalDate date2 = date1.plusWeeks(1);
      LocalDate date3 = date2.minusYears(6);
      LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS);
      ```
    * plus, minus도 Temporal 인터페이스에 정의되어 있음
  * 이들 메서드를 이용해 Temporal을 특정 시간만큼 앞뒤로 이동시킬 수 있음
    * 메서드의 인수에 숫자와 TemporalUnit을 활용할 수 있음
    * ChronoUnit 열거형은 TemporalUnit 인터페이스를 쉽게 활용할 수 있는 구현을 제공함

* LocalDate, LocalTime, LocalDateTime, Instant 등 날짜와 시간을 표현하는 모든 클래스는 비슷한 메서드를 제공함
  * 공통 메서드 표
    * from
      * 정적 : O
      * 주어진 Temporal 객체를 이용해서 Temporal 객체의 인스턴스를 생성함
    * now
      * 정적 : O
      * 시스템 시계로 Temporal 객체의 인스턴스를 생성함
    * of
      * 정적 : O
      * 주어진 구성 요소에서 Temporal 객체의 인스턴스를 생성함
    * parse
      * 정적 : O
      * 문자열을 파싱해서 Temporal 객체를 생성함
    * atOffset
      * 정적 : X
      * 시간대 오프셋과 Temporal 객체를 합침
    * atZone
      * 정적 : X
      * 시간대 오프셋과 Temporal 객체를 합침
    * format
      * 정적 : X
      * 지정된 포매터를 이용해서 Temporal 객체를 문자열로 변환함 (Instant는 지원하지 않음)
    * get
      * 정적 : X
      * Temporal 객체의 상태를 읽음
    * minus
      * 정적 : X
      * 특정 시간을 뺀 Temporal 객체의 복사본을 생성함
    * plus
      * 정적 : X
      * 특정 시간을 더한 Temporal 객체의 복사본을 생성함
    * with
      * 정적 : X
      * 일부 상태를 바꾼 Temporal 객체의 복사본을 생성함

* TemporalAdjusters 사용하기
  * 복잡한 날짜 조정 기능이 필요함
    * 예를 들어 다음 주 일요일, 돌아오는 평일, 어떤 달의 마지막 날 등
    * 오버로드된 버전의 with 메서드에 좀 더 다양한 동작을 수행할 수 있도록 하는 기능을 제공하는 TemporalAdjusters를 전달하는 방법으로 해결 가능
  * TemporalAdjusters에서 정의하는 정적 팩토리 메서드로 이용 가능
    * TemporalAdjuster는 인터페이스
    * TemporalAdjusters는 TemporalAdjuster를 반환하는 정적 팩토리 메서드를 포함하는 클래스
  * TemporalAdjusters의 팩토리 메서드로 만들 수 있는 TemporalAdjuster 리스트
    * dayOfWeekInMonth
      * 서수 요일에 해당하는 날짜를 반환하는 TemporalAdjuster를 반환함 (음수를 사용하면 월의 끝에서 거꾸로 계산)
    * firstDayOfMonth
      * 현재 달의 첫 번째 날짜를 반환하는 TemporalAdjuster를 반환함
    * firstDayOfNextMonth
      * 다음 달의 첫 번째 날짜를 반환하는 TemporalAdjuster를 반환함
    * firstDayOfNextYear
      * 내년의 첫 번째 날짜를 반환하는 TemporalAdjuster를 반환함
    * firstDayOfYear
      * 올해의 첫 번째 날짜를 반환하는 TemporalAdjuster를 반환함
    * firstInMonth
      * 현재 달의 첫 번째 요일에 해당하는 날짜를 반환하는 TemporalAdjuster를 반환함
    * lastDayOfMonth
      * 현재 달의 마지막 날짜를 반환하는 TemporalAdjuster를 반환함
    * lastDayOfNextMonth
      * 다음 달의 마지막 날짜를 반환하는 TemporalAdjuster를 반환함
    * lastDayOfNextYear
      * 내년의 마지막 날짜를 반환하는 TemporalAdjuster를 반환함
    * lastDayOfYear
      * 올해의 마지막 날짜를 반환하는 TemporalAdjuster를 반환함
    * lastInMonth
      * 현재 달의 마지막 요일에 해당하는 날짜를 반환하는 TemporalAdjuster를 반환함
    * next previous
      * 현재 달에서 현재 날짜 이후/이전으로 지정한 요일이 처음으로 나타나는 날짜를 반환하는 TemporalAdjuster를 반환함
    * nextOrSame previousOrSame
      * 현재 날짜 이후/이전으로 지정한 요일이 처음으로 나타나는 날짜를 반환하는 TemporalAdjuster를 반환함(현재 날짜도 포함)
  * TemporalAdjuster를 이용하면 좀 더 복잡한 날짜 조정 기능을 직관적으로 해결 가능
    * 원하는 기능이 정의되어 있지 않을 때 쉽게 커스텀 TemporalAdjuster 구현을 만들 수도 있음
    * 실제로 TemporalAdjuster 인터페이스는 다음처럼 하나의 메서드만 정의함
      ```
      @FunctionalInterface
      public interface TemporalAdjuster {
          Temporal adjustInto(Temporal temporal);
      }
      ```
    * TemporalAdjuster 인터페이스 구현은 Temporal 객체를 어떻게 다른 Temporal 객체로 변환할지 정의함
    * 결국 TemporalAdjuster 인터페이스를 UnaryOperator\<Temporal>과 같은 형식으로 간주할 수 있음

* 날짜와 시간 객체 출력과 파싱
  * 포매팅, 파싱 전용 패키지인 java.time.format이 새로 추가됨
    * 이 패키지에서 가장 중요한 클래스는 DateTimeFormatter
      * 정적 팩토리 메서드와 상수를 이용해 손쉽게 포매터를 만들 수 있음
      * DateTimeFormatter 클래스는 BASIC_ISO_DATE, ISO_LOCAL_DATE 등의 상수를 미리 정의하고 있음
      * 날짜나 시간을 특정 형식의 문자열로 만들 수 있음
      * 두 개의 서로 다른 포매터로 문자열을 만드는 예제
        ```
        LocalDate date = LocalDate.of(2014, 3, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18
        ```
      * 반대로 날짜나 시간을 표현하는 문자열을 파싱해 날짜 객체를 다시 만들 수 있음
        * 날짜나 시간 API에서 특정 시점이나 간격을 표현하는 모든 클래스의 팩토리 메서드 parse를 이용, 문자열을 날짜 객체로 만들 수 있음
        ```
        LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);
        ```
  * java.util.DateFormat 클래스와 달리 DateTimeFormatter는 스레드에서 안전하게 사용할 수 있는 클래스
    * 특정 패턴으로 포매터를 만들 수 있는 정적 팩토리 메서드 제공 예제
      ```
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      LocalDate date1 = LocalDate.of(2014, 3, 18);
      String formattedDate = date1.format(formatter);
      LocalDate date2 = LocalDate.parse(formattedDate, formatter);
      ```
      * LocalDate의 format 메서드는 요청 형식의 패턴에 해당하는 문자열을 생성함
      * 그리고 정적 메서드 parse는 같은 포매터를 적용해서 생성된 문자열을 파싱함으로써 다시 날짜를 생성함
      * ofPattern 메서드도 Locale로 포매터를 만들 수 있도록 오버로드 된 메서드를 제공함
        ```
        DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
        LocalDate date1 = LocalDate.of(2014, 3, 18);
        String formattedDate = date1.format(italianFormatter); // 18. marzo 2014
        LocalDate date2 = LocalDate.parse(formattedDate, italianFormatter);
        ```
      * DateTimeFormatterBuilder 클래스로 복합적인 포매터를 정의해서 세부적으로 포매터를 제어할 수 있음
        * DateTimeFormatter 클래스로 아래와 같은 활용 가능
          * 대소문자를 구분하는 파싱
          * 관대한 규칙을 적용하는 파싱 (정해진 형식과 정확하게 일치하지 않는 입력을 해석할 수 있도록 체험적 방식의 파서 사용)
          * 패딩, 포매터의 선택사항 등
          * 예를 들어 위 예제에서 사용한 italianFormatter를 DateTimeFormatterBuilder에 이용하면 프로그램적으로 포매터를 만들 수 있음

### 다양한 시간대와 캘린터 활용 방법
* 새로운 날짜와 시간 API의 편리함 중 하나는 시간대를 간단히 처리할 수 있는 것
* 기존의 java.util.TimeZone을 대체할 수 있는 java.time.ZoneId 클래스
  * 새로운 클래스를 사용하면 서머타임(Daylight Saving Time - DST) 같은 복잡항 사항이 자동으로 처리됨
  * ZoneId 클래스는 불변 클래스

* 시간대 사용하기
  * 표준 시간이 같은 지역을 묶어 시간대(time zone) 규칙 집합을 정의함
  * ZoneRules 클래스에는 약 40개 정도의 시간대가 있음
  * ZoneId의 getRules()를 이용, 해당 시간대의 규정을 획득할 수 있음
  * 지역 ID로 특정 ZoneId를 구분
    ``` ZoneId romeZone = ZoneId.of("Europe/Rome"); ```
    * 지역 ID는 {지역/도시} 형식
      * IANA Time Zone Database에서 제공하는 지역 집합 정보를 사용함 (https://www.iana.org/time-zones)
  * ZoneId의 새로운 메서드 toZoneId로 기존의 TimeZone 객체를 ZoneId 객체로 변환할 수 있음
    ``` ZoneId zoneId = TimeZone.getDefault().toZoneId(); ```
  * ZoneId 객체를 얻은 다음에는 LocalDate, LocalDateTime, Instant를 이용해 ZonedDateTime 인스턴스로 변환 가능
    * ZonedDateTime은 지정한 시간대에 상대적인 시점을 표현함
    ```
    LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
    ZonedDateTime zdt1 = date.atStartOfDay(romeZone);
    LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
    ZonedDateTime zdt2 = dateTime.atZone(romeZone);
    Instant instant = Instant.now();
    ZonedDateTime zdt3 = instant.atZone(romeZone);
    ```
    * ZonedDateTime : 2014-05-14T15:33:05.941+01:00[Europe/London]
      * ZoneId : [Europe/London]
    * LocalDateTime : 2014-05-14T15:33:05
      * LocalDate : 2014-05-14
      * LocalTime : 15:33:05
  * ZoneId를 이용해 LocalDateTime을 Instant로 변경
    ```
    Instant instant = Instant.now();
    LocalDateTime timeFromInstant = LocalDateTime.ofInstant(instant, romeZone);
    ```
    * 기존의 Date 클래스를 처리하는 코드를 사용해야 하는 상황이 있을 수 있으므로 Instant로 작업하는 것이 유리함
    * 폐기된 API와 새 날짜와 시간 API 간의 동작에 도움이 되는 toInstant(), 정적 메서드 fromInstant() 두 개의 메서드가 있음

* UTC/Greenwich 기준의 고정 오프셋
  * UTC(Universal Time Coordinated - 협정 세계시)/GMT(Greenwitch Mean Time - 그리니치 표준시)를 기준으로 표현
  * ZoneId의 서브클래스 ZoneOffset 클래스를 사용하여 시간값의 차이를 표현할 수 있음
    * 예를 들어 뉴욕은 런던보다 5시간 느림. 런던의 그리니치 0도 자오선과 시간값의 차이 표현
      ``` ZoneOffset newYorkOffset = ZoneOffset.of("-05:00"); ```
      * 실제 미국 동부의 표준시의 오프셋값은 =05:00
      * 하지만 위 예제는 서머타임을 제대로 처리할 수 없으므로 권장하지 않는 방식
      * ZoneOffset은 ZoneId이므로 사용 가능
      * 또한 ISO-8601 캘린더 시스템에서 정의하는 UTC/GMT와 오프셋으로 날짜와 시간을 표현하는 OffsetDateTime을 만드는 방법도 있음
        ```
        LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
        OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(dateTime, newYorkOffset);
        ```
  * 새로운 날짜 시간 API는 ISO 캘린더 시스템에 기반하지 않은 정보도 처리할 수 있는 기능을 제공함

* 대안 캘린더 시스템 사용하기
  * ISO-8601 캘린더 시스템은 실질적으로 전 세계에서 통용됨
  * 하지만 자바 8에서는 추가로 4개의 캘린더를 제공함
    * ThaiBuddhistDate
    * MinguoDate
    * JapaneseDate
    * HijrahDate
  * 위 4개 클래스와 LocalDate 클래스는 ChronoLocalDate 인터페이스를 구현
    * ChronoLocalDate는 임의의 연대기에서 특정 날짜를 표현할 수 있는 기능을 제공하는 인터페이스
    * 일반적으로 정적 메서드로 Temporal 인스턴스를 만들 수 있음
      * 예제 코드
        ```
        LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
        JapaneseDate japaneseDate = JapaneseDate.from(date);
        ```
    * 또는 특정 Locale과 Locale에 대한 날짜 인스턴스로 캘린더 시스템을 만드는 방법도 있음
      * 새로운 날짜와 시간 API에서 Chronology는 캘린더 시스템을 의미
        * 정적 팩토리 메서드 ofLocale을 이용해 Chronology의 인스턴스 획득 가능
          ```
          Chronology japaneseChronology = Chronology.ofLocale(Locale.JAPAN);
          ChronoLocalDate now = japaneseChronology.dateNow();
          ```
  * 날짜와 시간 API의 설계자는 ChronoLocalDate보다 LocalDate 사용을 권고
    * 예를 들어 1년은 12개월, 1달은 31일 이하, 최소한 1년은 정해진 수의 달로 이루어졌을 것이라고 가정할 수 있음
    * 하지만 이와 같은 가정은 특히 멀티캘린더 시스템에는 적용되지 않음
    * 따라서 프로그램의 입출력을 지역화하는 상황을 제외하고는 모든 데이터 저장, 조작, 비즈니스 규칙 해석 등 작업에서 LocalDate를 사용해야 함

* 이슬람력
  * 자바 8에서 추가된 새로운 캘린더 중에서 제일 복잡함
    * 이슬람력에는 변형(variant)이 있기 때문
  * Hijrah 캘린더 시스템은 태음월(lunar month)에 기초함
  * 새로운 달(month)을 결정할 때 새로운 달(moon)을 전 세계 어디에서나 볼 수 있는지  
    사우디아라비아에서 처음으로 새로운 달을 볼 수 있는지 등의 변형 방법을 결정하는 메서드가 있음
  * withVariant 메서드로 원하는 변형 방법을 선택할 수 있음
  * 자바 8에는 HijrahDate의 표준 변형 방법으로는 UmmAl-Qura를 제공함
  * 현재 이슬람의 연도의 시작과 끝을 ISO 날짜로 출력하는 예제
    ```
    HijrahDate ramadanDate = HijrahDate.now().with(ChronoField.DAY_OF_MONTH, 1)
            // 현재 Hijrah 날짜를 얻음
            // 얻은 날짜를 Ramadan의 첫 번째 날, 즉 9번째 달로 바꿈
            .with(ChronoField.MONTH_OF_YEAR, 9);

    System.out.println("Ramadan starts on "
            // INSTANCE는 IsoChronology 클래스의 정적 인스턴스
            + IsoChronology.INSTANCE.date(ramadanDate)
            + " and ends on "
            // Ramadan 1438은 2017-05-26에 시작해서 2017-06-24에 종료됨
            + IsoChronology.INSTANCE.date(ramadanDate.with(TemporalAdjusters.lastDayOfMonth())));
    ```

### 정리
* 자바 8 이전 버전에서 제공하는 기존의 java.util.Date 클래스와 관련 클래스는  
  여러 불일치점들과 가변성, 어설픈 오프셋, 기본값, 잘못된 이름 결정 등의 설계 결함이 존재함
* 새로운 날짜 시간 API에서 날짜와 시간 객체는 모두 불변
* 새로운 API는 각각 사람과 기계가 편리하게 날짜와 시간 정보를 관리할 수 있도록 두 가지 표현 방식을 제공함
* 날짜와 시간 객체를 절대적인 방법과 상대적인 방법으로 처리할 수 있으며 기존 인스턴스를 변환하지 않도록 처리 결과로 새로운 인스턴스가 생성됨
* TemporalAdjuster를 이용하면 단순히 값을 바꾸는 것 이상의 복잡한 동작을 수행할 수 있으며 자신만의 커스텀 날짜 변환 기능을 정의 가능
* 날짜와 시간 객체를 특정 포맷으로 출력하고 파싱하는 포매터를 정의할 수 있음
  * 패턴을 이용하거나 프로그램으로 포매터를 만들 수 있으며 포매터는 스레드 안정성을 보장함
* 특정 지역/장소에 상대적인 시간대 또는 UTC/GMT 기준의 오프셋을 이용해서 시간대를 정의할 수 있으며  
  이 시간대를 날짜와 시간 객체에 적용해서 지역화 할 수 있음
* ISO-8601 표준 시스템을 준수하지 않는 캘린더 시스템에도 사용할 수 있음

### [quiz]
* 다음 코드 실행시 date의 변수값은?
  ```
  LocalDate date = LocalDate.of(2014, 3, 18);
  date = date.with(ChronoField.MONTH_OF_YEAR, 9);
  date = date.plusYears(2).minusDays(10);
  date.withYear(2011);
  ```
  * 결과 : 2016-09-08
  * 절대적, 상대적 방식으로 날짜를 조정 가능
    * 각 메서드의 결과로 새로운 LocalDate 객체가 생성되므로 한 행에 여러 날짜 조정 메서드를 연결할 수 있음
    * 마지막 행은 변수에 할당하지 않았으므로 결과적으로 아무 일도 일어나지 않음

* TemporalAdjuster 인터페이스를 구현하는 NextWorkingDay 클래스 구현하기
  * 이 클래스는 날짜를 하루씩 다음날로 바꾸는데 이때 주말은 건너뜀
  * 다음 코드를 실행하면 다음날로 이동함
    ``` date = date.with(new NextWorkingDay()); ```
  * 만일 이동 날짜가 평일이 아니라면, 즉 토요일, 일요일(주말)이라면 월요일로 이동함
  * 구현 코드
    ```
    @Override
    public Temporal adjustInto(Temporal temporal) {
        // 현재 날짜 읽기
        DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));

        // 일반적으로 하루 추가
        int dayToAdd = 1;
        if (dow == DayOfWeek.FRIDAY) { // 오늘이 금요일이면 3일 추가
            dayToAdd = 3;
        } else if (dow == DayOfWeek.SATURDAY) { // 오늘이 토요일이면 2일 추가
            dayToAdd = 2;
        }

        // 적정한 날 수만큼 추가된 날짜를 반환
        return temporal.plus(dayToAdd, ChronoUnit.DAYS);
    }
    ```
    * 람다 구현
      ```
      LocalDate date = LocalDate.now();
      date = date.with(temporal -> {
          DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
  
          int dayToAdd = 1;
          if (dow == DayOfWeek.FRIDAY) {
              dayToAdd = 3;
          } else if (dow == DayOfWeek.SATURDAY) {
              dayToAdd = 2;
          }
  
          return temporal.plus(dayToAdd, ChronoUnit.DAYS);
      });
      ```
    * TemporalAdjuster를 람다로 정의
      * UnaryOperator\<LocalDate>를 인수로 받는 TemporalAdjusters클래스의 정적 팩토리 메서드 ofDateAdjuster를 사용
      ```
      TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(
              temporal -> {
                  DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
  
                  int dayToAdd = 1;
                  if (dow == DayOfWeek.FRIDAY) {
                      dayToAdd = 3;
                  } else if (dow == DayOfWeek.SATURDAY) {
                      dayToAdd = 2;
                  }
  
                  return temporal.plus(dayToAdd, ChronoUnit.DAYS);
              }
      );
  
      date.with(nextWorkingDay);
      ```
---

## chapter 13 - 디폴트 메서드
* 인터페이스를 구현하는 클래스는 인터페이스에서 정의하는 모든 메서드 구현을 제공하거나 아니면 슈퍼클래스의 구현을 상속 받아야 함
  * 라이브러리 설계자 입장에서 새로운 메서드를 추가하는 등 인터페이스를 바꾸고 싶을 때는 문제가 발생함
  * 인터페이스를 바꾸면 이전에 해당 인터페이스를 구현했던 모든 클래스의 구현도 고쳐야 하기 때문
* 자바 8에서는 이 문제를 해결하는 새로운 기능 제공
  * 기본 구현을 포함하는 인터페이스를 정의하는 두 가지 방법을 제공함
    * 첫 번째는 인터페이스 내부의 정적 메서드(static method)를 사용하는 것
    * 두 번째는 인터페이스의 기본 구현을 제공할 수 있도록 디폴트 메서드(default method) 기능을 사용하는 것
      * 자바 8에서는 메서드 구현을 포함하는 인터페이스를 정의할 수 있음
      * 결과적으로 기존 인터페이스를 구현하는 클래스는 자동으로 인터페이스에 추가된 새로운 메서드의 디폴트 메서드를 상속받게 됨
      * 기존 코드의 구현을 바꾸도록 강요하지 않으면서도 인터페이스를 바꿀 수 있음
      * 이와 같은 방식으로 추가된 두 가지 예시 (List 인터페이스의 sort, Collection 인터페이스의 stream)
        * sort 구현 코드
          ```
          default void sort(Comparator<? super E> c) {
              Collections.sort(this, c);
          }
          
          List<Integer> numbers = Arrays.asList(3, 5, 1, 2, 6);
          numbers.sort(Comparator.naturalOrder()); // sort는 List 인터페이스의 디폴트 메서드
          ```
          * default라는 새로운 키워드는 해당 메서드가 디폴트 메서드임을 가리킴
          * sort 메서드는 Collections.sort 메서드를 호출
          * 위 코드에서 Comparator.naturalOrder()라는 새로운 메서드 등장
            * naturalOrder()는 자연순서(표준 알파벳 순서)로 요소를 정렬할 수 있도록  
              Comparator 객체를 반환하는 Comparator 인터페이스의 추가된 새로운 정적 메서드
        * stream 구현 코드
          ```
          default Stream<E> stream() {
              return StreamSupport.stream(spliterator(), false);
          }
          ```
          * stream 메서드는 StreamSupport.stream 메서드 호출
          * Collection 인터페이스의 다른 디폴트 메서드 spliterator도 호출
    * 디폴트 메서드는 주로 라이브러리 설계자들이 사용함
      * 디폴트 메서드를 이용하면 자바 API의 호환성을 유지하면서 라이브러리를 바꿀 수 있음
    * 디폴트 메서드가 없던 시절에는 인터페이스에 메서드를 추가하면서 여러 문제가 발생했음
      * 인터페이스에 새로 추가된 메서드를 구현하도록 인터페이스를 구현한 기존 클래스를 고쳐야 했기 때문
      * 본인이 직접 인터페이스와 이를 구현하는 클래스를 관리할 수 있는 상황이라면 문제 없이 해결할 수 있음
        * 하지만 인터페이스를 대중에 공개했을 때는 상황이 달라짐
      * 그래서 디폴트 메서드가 탄생
      * 디폴트 메서드를 이용하면 인터페이스의 기본 구현을 그대로 상속하므로 인터페이스에 자유롭게 새로운 메서드를 추가 가능
    * 기존 구현을 고치지 않고도 인터페이스를 바꿀 수 있으므로 디폴트 메서드를 이해하는 것이 중요함
      * 디폴트 메서드는 다중 상속 동작이라는 유연성을 제공, 프로그램 구성에 도움을 줌

* 정적 메서드와 인터페이스
  * 자바에서는 인터페이스 그리고 인터페이스의 인스턴스를 활용할 수 있는 다양한 정적 메서드를 정의하는 유틸리티 클래스를 활용함
    * 예를 들어 Collections는 Collection 객체를 활용할 수 있는 유틸리티 클래스
    * 자바 8에서는 인터페이스의 직접 정적 메서드를 선언할 수 있으므로 유틸리티 클래스를 없애고 직접 인터페이스 내부에 정적 메서드를 구현할 수 있음
    * 그럼에도 불구하고 과거 버전과의 호환성을 유지할 수 있도록 자바 API에는 유틸리티 클래스가 남아 있음

### 변화하는 API
* 자바 그리기 라이브러리 설계자라고 가정
  * setHeight, setWidth, getHeight, getWidth, setAbsoluteSize 등의 메서드를 정의하는 Resizable 인터페이스
  * Rectangle, Square 처럼 Resizable을 구현하는 클래스도 제공
  * 일부 사용자는 직접 Resizable 인터페이스를 구현하는 Ellipse라는 클래스를 구현
  * Resizable에 몇 가지 기능 부족
    * Resizable 인터페이스에 크기 조절 인수로 모양의 크기를 조절하는 setRelativeSize라는 메서드
      * Resizable에 위 메서드를 추가하고 Rectangle, Square를 수정
    * 하지만 사용자가 만든 클래스를 변경할 수 없음

* API 버전 1
  * API
    ```
    public interface Resizable extends Drawable {
  	
        int getWidth();
        int getHeight();
        void setWidth(int width);
        void setHeight(int height);
        void setAbsoluteSize(int width, int height);
    }
    ```
  * 사용자가 만든 클래스
    ```
    public class Game {
    
    	public static void main(String[] args) {
    		// 크기를 조절할 수 있는 모양 리스트
    		List<Resizable> resizeableShapes = Arrays.asList(new Square(), new Rectangle(), new Ellipse());
    		Utils.paint(resizeableShapes);
    	}
    }
    
    public class Utils {
    
    	public static void paint(List<Resizable> l) {
    		l.forEach(r -> {
    			r.setAbsoluteSize(42, 42);
    			r.draw();
    		});
    	}
    }
    ```

* API 버전 2
  * Resizable을 구현하는 Rectangle, Square 구현 개선
    ```
    public interface Resizable extends Drawable {
    
    	int getWidth();
    	int getHeight();
    	void setWidth(int width);
    	void setHeight(int height);
    	void setAbsoluteSize(int width, int height);
    
    	// API 버전 2에 추가된 새로운 메서드
    	void setRelativeSize(int widthFactor, int heightFactor);
    }
    ```
  * 사용자가 겪는 문제
    * Resizable 고치면 발생하는 문제
      * Resizable을 구현하는 모든 클래스는 setRelativeSize 메서드를 구현해야 함
      * 인터페이스에 새로운 메서드를 추가하면 바이너리 호환성은 유지됨
        * 바이너리 호환성이란 새로 추가된 메서드를 호출하지만 않으면 새로운 메서드 구현이 없어도 기존 클래스 파일 구현이 잘 동작한다는 의미
      * 하지만 언젠가 누군가가 Resizable을 인수로 받는 Utils.paint()에서 setRelativeSize를 사용하도록 코드를 변경할 수 있음
        * 이때 Ellipse 객체가 인수로 전달되면 Ellipse는 setRelativeSize 메서드를 정의하지 않았으므로 런타임에 다음과 같은 에러 발생
          ```
          Exception in thread "main" java.lang.AbstractMethodError
          ``` 
        * 두 번째 사용자가 Ellipse를 포함하는 전체 앱을 재빌드할 때 다음과 같은 컴파일 에러가 발생
          ```
          Error:(5, 8) java: com.jaenyeong.chapter_13.ExampleAPI.Customer.Ellipse is not abstract and  
          does not override abstract method setRelativeSize(int,int) in  
          com.jaenyeong.chapter_13.ExampleAPI.Developer.Resizable
          ```
    * 공개된 API를 고치면 기존 버전과의 호환성 문제가 발생함
      * 이런 이유로 공식 자바 컬렉션 API 같은 기존 API는 고치기 어려움
      * 바꿀 수 있는 대안이 몇 가지 있지만 완벽한 해결책은 될 수 없음
      * 예를 들어 자신만의 API를 별도로 만든 다음에 예전 버전과 새로운 버전을 직접 관리하는 방법
        * 둘째, 사용자는 같은 코드에 예전 버전과 새로운 버전 두 가지 라이브러리를 모두 사용해야 하는 상황 발생
        * 결국 프로젝트에서 로딩해야 할 클래스 파일이 많아지면서 메모리 사용과 로딩 시간 문제가 발생
    * 디폴트 메서드로 이 모든 문제를 해결
      * 디폴트 메서드를 이용해서 API를 바꾸면 새롭에 바뀐 인터페이스에 자동으로 기본 구현을 제공하므로 기존 코드를 고치지 않아도 됨

* 바이너리 호환성, 소스 호환성, 동작 호환성
  * 자바 프로그램을 바꾸는 문제는 크게 바이너리 호환성, 소스 호환성, 동작 호환성 세 가지로 분류 가능
    * http://goo.gl/JNn4vm
      * https://blogs.oracle.com/darcy/kinds-of-compatibility:-source,-binary,-and-behavioral
  * 인터페이스에 메서드를 추가했을 때는 바이너리 호환성을 유지하지만 인터페이스를 구현하는 클래스를 재컴파일하면 에러가 발생함
    * 다양한 호환성이 있다는 사실을 인지해야 함
  * 바이너리 호환성
    * 뭔가를 바꾼 이후에도 에러 없이 기존 바이너리가 실행될 수 있는 상황을 바이너리 호환성이라고 함
      * 바이너리 실행에는 인증(verification), 준비(preparation), 해석(resolution) 등의 과정이 포함됨
      * 예를 들어 인터페이스에 메서드를 추가했을 때 추가된 메서드를 호출하지 않는 한 문제가 일어나지 않는데 이를 바이너리 호환성이라고 함
  * 소스 호환성
    * 코드를 고쳐도 기존 프로그램을 성공적으로 재컴파일할 수 있음을 의미함
      * 예를 들어 인터페이스에 메서드를 추가하면 소스 호환성이 아님. 추가한 메서드를 구현하도록 클래스를 고쳐야 하기 때문
  * 동작 호환성
    * 코드를 바꾼 다음에도 같은 입력값이 주어지면 프로그램이 같은 동작을 실행한다는 의미
      * 예를 들어 인터페이스에 메서드를 추가하더라도 프로그램에서 추가된 메서드를 호출할 일은  
        없으므로(우연히 구현 클래스가 이를 오버라이드 했을 수도 있음) 동작 호환성은 유지됨
          
### 디폴트 메서드란 무엇인가?
* 인터페이스는 자신을 구현하는 클래스에서 메서드를 구현하지 않을 수 있는 새로운 메서드 시그니처를 제공함
* 인터페이스를 구현하는 클래스에서 구현하지 않은 메서드는 인터페이스 자체에서 기본으로 제공함
  * 그래서 이를 디폴트 메서드라고 부름
* 디폴트 메서드는 default라는 키워드로 시작함
  * 다른 클래스에 선언된 메서드처럼 메서드 바디를 포함함
    * 예를 들어 Sized 인터페이스를 정의
      * 추상 메서드 size와 디폴트 메서드 isEmpty를 포함함
        ```
        public interface Sized {
        	int size();
        	default boolean isEmpty() {
        		return size() == 0;
        	}
        }
        ```
      * Sized 인터페이스를 구현하는 모든 클래스는 isEmpty의 구현도 상속받음
        * 즉, 인터페이스에 디폴트 메서드를 추가하면 소스 호환성이 유지됨
* 자바 그리기 라이브러리 예제
  * 디폴트 메서드를 이용해 setRelativeSize의 디폴트 구현을 제공한다면 호환성을 유지하면서 라이브러리를 고칠 수 있음
    * 라이브러리 사용자는 Resizable 인터페이스를 구현하는 클래스를 고칠 필요가 없음
      ```
      default void setRelativeSize(int wFactor, int hFactor) {
          setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
      }
      ```
  * 자바 8 API에서 디폴트 메서드가 상당히 많이 활용됨
    * 예를 들어 Collection 인터페이스의 stream 메서드처럼 부지불식간에 많은 디폴트 메서드를 사용했음
      * List 인터페이스의 sort 메서드
      * Prdicate, Function, Comparator 등 많은 함수형 인터페이스도 Predicate.and, Function.andThen 같은 디폴트 메서드 포함
        * 함수형 인터페이스는 오직 하나의 추상 메서드를 포함함
          * 디폴트 메서드는 추상 메서드에 해당하지 않음

* 추상 클래스와 자바 8 인터페이스
  * 추상 클래스와 인터페이스는 둘 다 추상 메서드와 바디를 포함하는 메서드를 정의할 수 있음
  * 차이점
    * 클래스는 하나의 추상 클래스만 상속받을 수 있지만 인터페이스는 여러 개를 구현할 수 있음
    * 추상 클래스는 인스턴스 변수(필드)로 공통 상태를 가질 수 있음
      * 인터페이스는 인스턴스 변수를 가질 수 없음

### 디폴트 메서드 활용 패턴
* 디폴트 메서드를 이용하는 두 가지 방식
  * 선택형 메서드(optional method)
  * 동작 다중 상속(multiple inheritance of behavior)

* 선택형 메서드
  * 인터페이스를 구현하는 클래스에서 메서드의 내용이 비어 있는 상황
    * 예를 들어 Iterator 인터페이스
      * hasNext, next, remove 메서드 정의
      * 사용자들이 remove를 잘 사용하지 않으므로 자바 8 이전에는 rmove 기능을 무시함
        * 결과적으로 Iterator를 구현하는 많은 클래스에서는 remove에 빈 구현을 제공함
      * 디폴트 메서드 이용시 remove 같은 메서드에 기본 구형을 제공할 수 있음
        * 인터페이스를 구현하는 클래스에서 빈 구현을 제공할 필요가 없음
          ```
          interface Iterator<T> {
              boolean haxNext();
              T next();
              default void remove() {
                  throw new UnsupportedOperationExcpetion();
              }
          }
          ```

* 동작 다중 상속
  * 디폴트 메서드 사용시 기존에는 불가능했던 동작 다중 상속 기능도 구현할 수 있음
    * 클래스는 다중 상속을 이용해서 기존 코드를 재사용할 수 있음
  * 자바 API에 정의된 ArrayList 클래스
    ```
    public class ArrayLIst<E> extends AbstractList<E>               // 한 개의 클래스 상속
        implements List<E>, RandomAccess, Cloneable, Serializable { // 네 개의 인터페이스 구현
    }
    ```
  * 다중 상속 형식
    * ArrayList는 한 개의 클래스를 상속, 여섯 개의 인터페이스를 구현함
      * ArrayList는 AbstractList, List, RandomAccess, Cloneable, Serializable, Iterable, Collection의 서브 형식(subtype)
      * 따라서 디폴트 메서드를 사용하지 않아도 다중 상속을 활용할 수 있음
    * 자바 8에서는 인터페이스가 구현을 포함하므로 클래스는 여러 인터페이스에서 동작을 상속받을 수 있음
  * 기능이 중복되지 않는 최소의 인터페이스
    * 게임에 다양한 특성을 갖는 여러 모양을 정의한다고 가정
      * 어떤 모양은 회전할 수 없지만 크기는 조절할 수 있음
      * setRotationAngle, getRotationAngle 두 개의 추상 메서드를 포함하는 Rotatable 인터페이스 정의
        * 디폴트 메서드 rotateBy도 구현
        ```
        public interface Rotatable {
        
        	void setRotationAngle(int angleInDegrees);
        	int getRotationAngle();
        	default void rotateBy(int angleInDegrees) {
        		setRotationAngle((getRotationAngle() + angleInDegrees) % 360);
        	}
        }
        ```
        * 위 인터페이스는 구현해야 할 다른 메서드에 따라 뼈대 알고리즘이 결정되는 템플릿 디자인 패턴과 비슷해 보임
        * Rotatable을 구현하는 모든 클래스는 setRotationAngle과 getRotationAngle의 구현을 제공해야 함
          * 하지만 rotateBy는 기본 구현이 제공되므로 따로 구현을 제공하지 않아도 됨
      * Moveable, Resizable을 정의. 두 인터페이스 모두 디폴트 구현을 제공
        ```
        public interface Moveable {
        	int getX();
        	int getY();
        	void setX(int x);
        	void setY(int y);
        
        	default void moveHorizontally(int distance) {
        		setX(getX() + distance);
        	}
        
        	default void moveVertically(int distance) {
        		setY(getY() + distance);
        	}
        }
        
        public interface Resizable {
        
        	int getWidth();
        	int getHeight();
        	void setWidth(int width);
        	void setHeight(int height);
        	void setAbsoluteSize(int width, int height);
        
        	default void setRelativeSize(int wFactor, int hFactor) {
        		setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
        	}
        }
        ```
  * 인터페이스 조합
    * 이 인터페이스들을 조합, 게임에 필요한 다양한 클래스를 구현할 수 있음
      ```
      public class Monster implements Rotatable, Moveable, Resizable {
      	  // 모든 추상메서드의 구현은 제공해야 하지만 디폴트 메서드의 구현은 제공할 필요가 없음
      }
      ```
      * Monster 클래스는 Rotatable, Moveable, Resizable 인터페이스의 디폴트 메서드를 자동으로 상속받음
        * 즉, rotateBy, moveHorizontally, moveVertically, setRelativeSize 구현을 상속받음
        * 상속받은 다양한 메서드를 직접 호출
          ```
          Monster m = new Monster(); // 생성자는 내부적으로 좌표, 높이, 너비, 기본 각도를 설정함
          m.rotateBy(180); // Rotatable의 rotateBy 호출
          m.moveVertically(10); // Moveable의 moveVertically 호출
          ```
      * Sun 클래스 정의
        * 움직일 수 있으며 회전할 수 있지만, 크기는 조절할 수 없는 클래스
          ```
          public class Sun implements Moveable, Rotatable {
              // 모든 추상메서드의 구현은 제공해야 하지만 디폴트 메서드의 구현은 제공할 필요가 없음
          }
          ```
    * 인터페이스에 디폴트 구현을 포함시키면 생기는 또 다른 장점
      * 예를 들어 moveVertically의 구현을 더 효율적으로 고친다고 가정
        * 디폴트 메서드 덕분에 Moveable 인터페이스를 직접 고칠 수 있음
          * 따라서 Moveable을 구현하는 모든 클래스도 자동으로 변경한 코드를 상속 받음 (물론 구현 클래스에서 메서드를 정의하지 않은 상황에 한해서)

* 옳지 못한 상속
  * 상속으로 코드 재사용 문제를 모두 해결할 수 없음
    * 예를 들어 한 개의 메서드를 사용하려고 100개의 메서드와 필드가 정의되어 있는 클래스를 상속받는 것은 좋지 않음
      * 이럴 땐 델리게이션(delegation), 즉 멤버 변수를 이용하여 클래스에서 필요한 메서드를 직접 호출하는 메서드를 작성하는 것이 좋음
    * final로 선언된 클래스
      * 다른 클래스가 이 클래스를 상속받지 못하게 함으로써 원래 동작이 바뀌지 않길 원함
      * 예를 들어 String 클래스
        * String의 핵심 기능을 바꾸지 못하도록 제한할 수 있음

### 해석 규칙
* 자바의 클래스는 하나의 부모 클래스만 상속받을 수 있지만 인터페이스는 여러 개를 동시에 구현할 수 있음
* 자바 8에 디폴트 메서드가 추가되었으므로 같은 시그니처를 갖는 디폴트 메서드를 상속받는 상황이 발생할 수 있음
* 문제를 보여주기 위한 예제 코드
  ```
  public interface A {
  
      default void hello() {
          System.out.println("Hello from A");
      }
  }
  public interface B extends A {
  
      default void hello() {
          System.out.println("Hello from B");
      }
  }
  public class C implements B, A {
  
      public static void main(String[] args) {
          new C().hello();
      }
  }
  ```

* 알아야 할 세 가지 해결 규칙
  * 다른 클래스나 인터페이스로부터 같은 시그니처를 갖는 메서드를 상속받을 때는 세 가지 규칙을 따라야 함
    * [1] 클래스가 항상 이김
      * 클래스나 슈퍼클래스에서 정의한 메서드가 디폴트 메서드보다 우선권을 가짐
    * [2] 1번 규칙 이 외 상황에서는 서브인터페이스가 이김
      * 상속관계를 갖는 인터페이스에서 같은 시그니처를 갖는 메서드를 정의할 때는 서브인터페이스가 이김
      * 즉 예제와 같이 B가 A를 상속받는다면 B가 A를 이김
    * [3] 여전히 디폴트 메서드의 우선순위가 결정되지 않았다면 여러 인터페이스를 상속받는 클래스가 명시적으로 디폴트 메서드를 오버라이도하고 호출해야 함

* 디폴트 메서드를 제공하는 서브인터페이스가 이긴다
  * 예제와 같이 B가 A를 상속받는 경우 (new C().hello() 메서드 호출시) 
    * 컴파일러는 B의 hello를 선택함
  * A를 구현하는 D 클래스와 A, B 인터페이스를 함께 구현하는 경우 (new C().hello 메서드 호출시)
    ```
    public class D implements A {
    }
    
    public class C extends D implements B, A {
    
    	public static void main(String[] args) {
            // 'Hello from B'를 출력함
    		new C().hello();
    	}
    }
    ```
    * 컴파일러는 B의 hello를 선택함
      * 1번 규칙은 클래스의 메서드 구현이 이긴다고 설명함
        * D는 hello를 오버라이드하지 않았고 단순히 인터페이스 A를 구현함
        * 따라서 D는 인터페이스 A의 디폴트 메서드 구현을 상속받음
      * 2번 규칙은 클래스나 슈퍼클래스에서 메서드 정의가 없을 때는 디폴트 메서드를 정의하는 서브인터페이스가 선택됨
        * 따라서 컴파일러는 인터페이스 A의 hello나 인터페이스 B의 hello 둘 중 하나를 선택해야 함
      * 해당 예제에서는 B가 A를 상속받는 관계이므로 이번에도 'Hello from B'가 출력됨

* 충돌 그리고 명시적인 문제 해결
  * B가 A를 상속받지 않은 상황
    ```
    public interface A {
    
    	default void hello() {
    		System.out.println("Hello from A");
    	}
    }
    public interface B {
    
    	default void hello() {
    		System.out.println("Hello from B");
    	}
    }
    public class C implements B, A {
    }
    ```
    * 위 코드에서는 인터페이스 간 상속관계가 없으므로 2번 규칙을 적용할 수 없음
      * 그러므로 A와 B의 hello 메서드를 구별할 기준이 없음
      * 따라서 자바 컴파일러는 어떤 메서드를 호출해야 할지 알 수 없으므로 에러 발생
        * ``` Error: class C inherits unrelated defaults for hello() from types B and A. ```

* 충돌 해결
  * 클래스와 메서드 관계로 디폴트 메서드를 선택할 수 없는 상황에서는 선택할 수 있는 방법이 없음
  * 개발자가 직접 C 클래스에 사용하려는 메서드를 명시적으로 선택해야 함
  * 즉 C 클래스에서 hello 메서드를 오버라이드 한 다음에 호출하려는 메서드를 명시적으로 선택해야 함
  * 자바 8에서는 X.super.m(...) 형태의 새로운 문법을 제공함
    * 여기서 X는 호출하려는 메서드 m의 슈퍼인터페이스
    * 예를 들어 C에서 B의 인터페이스를 호출할 수 있음
      ```
      public class C implements B, A {

          // hello 메서드 직접 명시
          public void hello() {
              F.super.hello();
          }
      }
      ```

* 다이아몬드 문제
  * 시나리오
    ```
    public interface A {
    
    	default void hello() {
    		System.out.println("Hello from A");
    	}
    }
    
    public interface B extends A {
    }
    
    public interface C extends A {
    }
    
    public class D implements B, C {
    
    	public static void main(String[] args) {
    		new D().hello();
    	}
    }
    ```
    * UML 다이어그램이 다이아몬드 모양을 닮아 다이아몬드 문제(diamond problem)라고 부름
    * D 클래스는 'Hello from A' 출력
    * B에도 같은 시그니처의 디폴트 메서드 hello가 있다고 가정
      * B는 A를 상속 받으므로 B가 선택됨
    * B와 C 모두 디폴트 메서드가 hello를 정의한다고 가정
      * 충돌 발생 둘 중 하나의 메서드를 명시적으로 호출(재정의) 해야함
    * C 인터페이스의 추상 메서드 hello(디폴트 메서드 아님)을 정의한다고 가정 (A, B에는 아무 메서드드 정의하지 않는다?)
      * C는 A를 상속받으므로 C의 추상 메서드 hello가 디폴트 메서드 hello보다 우선권을 가지기 때문에 컴파일 에러가 발생
        * 명시적으로 선언(메서드 선택)하여 에러 해결해야 함
  * 디폴트 메서드 세 가지 규칙 (위와 동일)
    * [1] 클래스가 항상 이김
      * 클래스나 슈퍼클래스에서 정의한 메서드가 디폴트 메서드보다 우선권을 가짐
    * [2] 1번 규칙 이 외 상황에서는 서브인터페이스가 이김
      * 상속관계를 갖는 인터페이스에서 같은 시그니처를 갖는 메서드를 정의할 때는 서브인터페이스가 이김
      * 즉 예제와 같이 B가 A를 상속받는다면 B가 A를 이김
    * [3] 여전히 디폴트 메서드의 우선순위가 결정되지 않았다면 여러 인터페이스를 상속받는 클래스가 명시적으로 디폴트 메서드를 오버라이도하고 호출해야 함

* C++ 다이아몬드 문제
  * C++ 다이아몬드 문제는 이보다 더 복잡함
  * C++은 클래스의 다중 상속을 지원함
  * 클래스 D가 클래스 B와 C를 상속받고 B와 C는 클래스 A를 상속받는다고 가정
    * 클래스 D는 B, C 객체의 복사본에 접근할 수 있음
    * 결과적으로 A의 메서드를 사용할 때 B의 메서드인지 C의 메서드인지 명시적으로 해결해야 함
    * 또한 클래스는 상태를 가질 수 있으므로 B의 멤버 변수를 고쳐도 C 객체의 복사본에 반영되지 않음

### 정리
* 자바 8의 인터페이스에는 구현 코드를 포함하는 디폴트 메서드, 정적 메서드를 정의할 수 있음
* 디폴트 메서드의 정의는 default 키워드로 시작, 일반 클래스 메서드처럼 바디를 가짐
* 공개된 인터페이스에 추상 메서드를 추가하면 소스 호환성이 깨짐
* 디폴트 메서드 덕분에 라이브러리 설계자가 API를 바꿔도 기존 버전과 호환성을 유지할 수 있음
* 선택형 메서드와 동작 다중 상속에도 디폴트 메서드를 사용할 수 있음
* 클래스가 같은 시그니처를 갖는 여러 디폴트 메서드를 상속하면서 생기는 충돌 문제를 해결하는 규칙이 있음
* 클래스나 슈퍼클래스에 정의된 메서드가 다른 디폴트 메서드 정의보다 우선함
  * 이 외 상황에서는 서브인터페이스에서 제공하는 디폴트 메서드가 선택됨
* 두 메서드의 시그니처가 같고 상속관계로도 충돌 문제를 해결할 수 없을 때는  
  디폴트 메서드를 사용하는 클래스에서 메서드를 오버라이드해서 어떤 디폴트 메서드를 호출할지 명시적으로 결정해야 함

### [quiz]
* ArrayList, TreeSet, LinkedList 및 다른 모든 컬렉션에서 사용할 수 있는 removeIf 메서드 추가
  * removeIf 메서드는 주어진 프레디케이트와 일치하는 모든 요소를 컬렉션에서 제거하는 기능
  * 좋지 않은 방법
    * 컬레션 API의 모든 구현 클래스에 removeIf를 복사, 붙여넣기
  * 가장 적절하게 추가할 수 있는 방법
    * 모든 컬렉션 클래스는 java.util.Collection 인터페이스를 구현
    * Collection 인터페이스의 디폴트 메서드를 추가함으로써 소스 호환성 유지할 수 있음
    ```
    default <E> boolean removeIf(Predicate<? super E> filter) {
        boolean removed = false;
        Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }

        return removed;
    }
    ```

* 위 해석규칙을 예시에서 D가 명시적으로 A의 hello 메서드를 오버라이드 할 경우 프로그램 실행 결과는?
  ```
  public class D implements A {

      public void hello() {
          System.out.println("Hello from D");
      }
  }
  
  public class C extends D implements B, A {
  
  	public static void main(String[] args) {
  		new C().hello();
  	}
  }
  ```
  * 결과
    * 'Hello from D'가 출력됨
    * 규칙 1에 의해 슈퍼클래스의 메서드 정의가 우선권을 갖기 때문
  * D가 아래와 같이 구현되었을 경우
    ```
    public abstract class D implements A {
    
        public abstract void hello();
    }
    ```
    * 결과
      * 그러면 A 인터페이스에서 디폴트 메서드를 제공함에도 불구하고 C 클래스는 hello를 구현해야 함

* 다음 코드의 출력 결과는?
  ```
  public interface A {
  
      default Number getNumber() {
          return 10;
      }
  }
  
  public interface B {
  
      default Integer getNumber() {
          return 42;
      }
  }
  
  public class C implements B, A {
  
      public static void main(String[] args) {
          System.out.println(new C().getNumber());
      }
  }
  ```
  * 결과
    * C 클래스에서 A와 B 메서드를 구분할 수 없기 때문에 컴파일 에러 발생
---

## chapter 14 - 자바 모듈 시스템
* 자바 9에서 가장 많이 거론되는 새로운 기능은 모듈 시스템
* 직소 프로젝트 내부에서 개발된 기능

### 압력 : 소프트웨어 유추
* 궁극적으로 소프트웨어 아키텍처, 즉 고수준에서는 기반 코드를 바꿔야 할 때 유추하기 쉬우므로 생산성을 높일 수 있는 소프트웨어 프로젝트가 필요함
* 추론하기 쉬운 소프트웨어를 만드는데 도움을 주는 관심사분리(separation of concerns)와 정보 은닉(information hiding)

* 관심사 분리
  * 관심사분리(SoC - Separation Of Concerns)는 컴퓨터 프로그램을 고유의 기능으로 나누는 동작을 권장하는 원칙
  * 다양한 형식으로 구성된 지출을 파싱, 분석한 다음 결과를 고객에게 요약 보고하는 회계 앱을 개발한다고 가정
    * SoC를 적용함으로 파싱, 분석, 레포트 기능을 모듈이라는 각각의 부분, 즉 서로 거의 겹치지 않는 코드 그룹으로 분리할 수 있음
    * 클래스를 그룹화한 모듈을 이용해 앱의 클래스 간의 관계를 시각적으로 보여줄 수 있음
  * 자바 패키지가 클래스를 그룹으로 만들기는 하지만, 자바 9 모듈은 클래스가 어떤 다른 클래스를 볼 수 있는지를 컴파일 시간에 정교하게 제어할 수 있음
    * 특히 자바 패키지는 모듈성을 지원하지 않음
  * SoC 원칙은 모델, 뷰, 컨트롤러 같은 아키텍처 관점 그리고 복구 기법을 비즈니스 로직과 분리하는 등 하위 수준 접근 등 상황에 유용함
    * SoC 원칙 장점
      * 개별 기능을 따로 작업할 수 있으므로 팀이 쉽게 협업 가능함
      * 개별 부분을 재사용하기 쉬움
      * 전체 시스템을 쉽게 유지보수할 수 있음

* 정보 은닉
  * 정보 은닉(information hiding)은 세부 구현을 숨기도록 장려하는 원칙
  * 소프트웨어를 개발할 때 요구사항은 자주 바뀜
    * 세부 구현을 숨김으로 프로그램의 어떤 부분을 바꿨을 때 다른 부분까지 영향을 미칠 가능성을 줄일 수 있음
    * 즉 코드를 관리하고 보호하는 데 유용한 원칙
  * 캡슐화(encapsulation)
    * 캡슐화는 특정 코드 조각이 앱의 다른 부분과 고립되어 있음을 의미함
    * 캡슐화된 코드는 내부적인 변화가 의도치 않게 외부에 영향을 미칠 가능성이 줄어듦
    * 자바에서는 클래스 내의 컴포넌트에 적절하게 private 키워드를 사용했는지를 기준으로 컴파일러를 이용해 캡슐화를 확인할 수 있음
    * 하지만 자바 9 이전까지는 클래스와 패키지가 의도된 대로 공개되었는지를 컴파일러로 확인할 수 있는 기능이 없었음

* 자바 소프트웨어
  * 잘 설계된 소프트웨어를 만들려면 이 두 가지 원칙을 따르는 것이 필수
  * 자바는 객체 지향 언어로 클래스, 인터페이스를 이용함
    * 특정 문제와 관련된 패키지, 클래스, 인터페이스를, 그룹으로 만들어 코드를 그룹화할 수 있음
    * 코드 자체를 보고 소프트웨어의 동작을 추론하긴 현실적으로 어려움
    * 따라서 UML 다이어그램 같은 도구를 이용하면 그룹 코드 간의 의존성을 시각적으로 볼 수 있어 소프트웨어를 추론하는데 도움이 됨
  * 자바에서는 public, protected, private 등의 접근 제한자와 패키지 수준 접근 권한 등을 이용해 메서드, 필드 클래스의 접근을 제어함
    * 하지만 이런 방식으로는 원하는 접근 제한을 달성하기 어려움
    * 심지어 최종 사용자에게 원하지 않는 메서드도 공개해야 하는 상황 발생
    * 초창기에는 앱과 의존성 체인이 상대적으로 작아 큰 문제가 아니었음
    * 하지만 요즘에는 자바 앱이 커지면서 문제가 부각되고 있음
    * 개인적으로 사용할 용도라도 클래스에 public 필드가 있다면 다른 사용자는 당연히 사용할 수 있다고 생각함

### 자바 모듈 시스템을 설계한 이유
* 모듈화의 한계
  * 자바 9 이전까지는 모듈화된 소프트웨어 프로젝트를 만드는 데 한계가 있었음
  * 자바는 클래스, 패키지, JAR 세 가지 수준의 코드 그룹화를 제공함
  * 클래스와 관련해 자바는 접근 제한자와 캡슐화를 지원했음
  * 하지만 패키지와 JAR 수준에서는 캡슐화를 거의 지원하지 않았음

* 제한된 가시성 제어
  * 자바는 정보를 감출 수 있는 접근자를 제공함
    * public, protected, default(package private), private 네 가지 가시성 접근자
  * 패키지의 가시성 제어 기능은 유명무실 수준
    * 한 패키지 클래스와 인터페이스를 다른 패키지로 공개하려면 public으로 이들을 선언해야 함
      * 결과적으로 모두에게 공개됨
      * 특히 기본구현을 제공하는 의미로 'impl'이라는 문자열을 가진 패키지에서 이런 문제가 두드러짐
      * 이런 상황에서 보통 패키지 내부의 접근자가 public이므로 사용자가 이 내부 구현을 마음대로 사용 가능
      * 내부적으로 사용할 목적으로 만든 구현을 다른 프로그래머가 임시적으로 사용해서 정착해버릴 수 있음
      * 결국 기존의 앱을 망가뜨리지 않고 라이브러리 코드를 바꾸기 어려워짐
      * 보안 측면에서 볼 때 코드가 노출되었으므로 코드를 임의로 조작하는 위협에 더 많이 노출될 수 있음

* 클래스 경로
  * class path는 자바 문서에서 가져옴
    * classpath라는 용어는 프로그램 인수와 관련해 사용하는 용어
  * 자바는 앱을 번들하고 실행하는 기능과 관련해 태생적 약점을 갖고 있음
    * 클래스를 모두 컴파일한 다음 보통 한 개의 평범한 JAR 파일에 넣고 클래스 경로에 이 JAR 파일을 추가해 사용할 수 있음
    * 그러면 JVM이 동적으로 클래스 경로에 정의된 클래스를 필요할 때 읽음
  * 클래스 경로와 JAR 조합 약점
    * 첫째, 클래스 경로에는 같은 클래스를 구분하는 버전 개념이 없음
      * 예를 들어 파싱 라이브러리 JSONParser 클래스를 지정할 때 버전 1.0을 사용하는지 2.0을 사용하는지 지정할 수 없음
        * 클래스 경로에 두 가지 버전의 같은 라이브러리가 존재할 때 어떤 일이 발생할지 예측할 수 없음
        * 다양한 컴포넌트가 같은 라이브러리의 다른 버전을 사용하는 상황이 발생할 수 있는 큰 앱에서 이런 문제가 두드러짐
    * 둘째, 클래스 경로는 명시적인 의존성을 지원하지 않음
      * 각각의 JAR 안에 있는 모든 클래스는 classes라는 한 주머니로 합쳐짐
        * 즉 한 JAR가 다른 JAR에 포함된 클래스 집합을 사용하라고 명시적으로 의존성을 정의하는 기능을 제공하지 않음
        * 클래스 경로 때문에 어떤 일이 일어나는지 파악하기 어려움
          * 빠진 게 있는가?
          * 충돌이 있는가?
      * 메이븐, 그레이들 같은 빌드 도구는 이런 문제 해결에 도움을 줌
      * 하지만 자바 9 이전에는 자바, JVM 누구도 명시적인 의존성을 지원하지 않았음
        * 이러한 문제를 JAR 지옥, 클래스 경로 지옥이라 부르기도 함
        * 결국 JVM이 ClassNotFoundException 같은 에러를 발생시키지 않고 앱을 정상적으로 실행할 때까지  
          클래스 경로에 클래스 파일을 더하거나 클래스 경로에서 클래스를 제거해보는 수밖에 없음
        * 자바 9 모듈 시스템을 이용하면 컴파일 타임에 이런 종류 에러를 모두 검출할 수 있음
      * 캡슐화, 클래스 경로 지옥 문제가 우리 소프트웨어에만 발생하는 것이 아님

* 거대한 JDK
  * 자바 개발 키트(JDK)는 자바 프로그램을 만들고 실행하는 데 도움을 주는 두구의 집합
    * 자바 프로그램을 컴파일하는 javac
    * 자바 앱을 로드하고 실행하는 java
    * 입출력을 포함해 런타임을 지원을 제공하는 JDK 라이브러리, 컬렉션, 스트림
  * 초기 JDK 버전은 1996년 발표
    * 많은 기술이 추가되었다가 사장됨 (예를 들어 CORBA)
  * 자바 8에서는 컴팩트 프로파일(compact profiles) 기법 제시
    * 관련 분야에 따라 JDK 라이브러리가 세 가지 프로파일로 나뉘어 각각 다른 메모리 풋프린트를 제공함
    * 하지만 컴팩트 프로파일은 땜질식 처방일 뿐
    * JDK 라이브러리의 많은 내부 API는 공개되지 않아야 함
      * 안타깝게도 자바 언어의 낮은 캡슐화 지원 때문에 내부 API가 외부에 공개됨
        * 예를 들어 스프링, 네티, 모키토 등 여러 곳에서 sun.misc.Unsafe라는 클래스를 사용했음
          * 이 클래스는 JDK 내부에서만 사용하도록 만든 클래스
          * 결과적으로 호환성을 깨지 않고는 관련 API를 바꾸기 어려운 상황
  * JDK에서 필요한 부분만 골라 사용하고, 클래스 경로를 쉽게 유추할 수 있으며 플랫폼을 진화시킬 수 있는 강력한 캡슐화를 제공할 새로운 건축 구조가 필요함

* OSGi와 비교
  * Open Service Gateway initiative
  * 자바에는 이미 OSGi라는 강력한 모듈 시스템이 존재했음
    * 자바 플랫폼의 공식 기능은 아니었음
    * 2000년에 시작해 자바 9 등장 전까지 JVM에서 모듈화 앱을 구현하는 사실상 표준으로 자리잡음
  * 실제 OSGi와 새로운 자바 9 모듈 시스템은 상호 배타적인 관계가 아님
    * 즉 한 앱에서 두 기법이 공존할 수 있음
    * 실제 이 두 기능은 부분적으로만 중복됨
    * OSGi는 훨씬 더 광범위한 영역을 가지며 직소에서 제공하지 않는 여러 기능을 제공함
      * 번들(bundles)이라 불리는 OSGi 모듈은 특정 OSGi 프레임워크 내에서만 실행됨
      * 대표적인(인증받은) OSGi 프레임워크
        * 아파치 펠릭스(Apache Felix)
        * 에퀴녹스(Equinox)
          * 이클립스를 구동하는 데도 사용됨
      * OSGi 프레임워크 내에서 앱을 실행할 때 재부팅하지 않고도 원격으로 개별 번들을 설치, 시작, 중지, 갱신, 제거할 수 있음
        * 명확한 생명주기를 정의(OSGi의 번들 상태)
          * INSTALLED
            * 번들이 성공적으로 설치됨
          * RESOLVED
            * 번들에 필요한 모든 자바 클래스를 찾음
          * STARTING
            * 번들이 시작되면서 BundleActivator.start라는 메서드가 호출되었지만, 아직 start 메서드가 반환되지는 않음
          * ACTIVE
            * 번들이 성공적으로 활성화되고 실행됨
          * STOPPING
            * 번들이 정지되는 중. BundleActivator.stop 메서드가 호출되었지만 아직 stop 메서드가 반환되지는 않음
          * UNINSTALLED
            * 번들의 설치가 제거됨. 다른 상태로 이동할 수 없음
  * 시스템을 재시작하지 않고도 앱의 다른 하위 부분을 핫스왑할 수 있다는 점이 직소와는 다른 OSGi만의 강점
  * 번들 동작에 필요한 외부 패키지가 무엇이며 어떤 내부 패키지가 외부로 노출되어 다른 번들로 제공되는지를 서술하는 텍스트 파일로 각 번들을 정의함
  * 동시에 프레임워크 내에 같은 번들의 다른 버전을 설치할 수 있다는 것도 장점
  * OSGi의 각 번들이 자체적은 클래스 로더를 갖는 반면, 자바 9 모듈 시스템의 직소는 앱당 한 개의 클래스를 사용하므로 버전 제어를 지원하지 않음

### 자바 모듈 : 큰 그림
* 자바 8은 모듈이라는 새로운 자바 프로그램 구조 단위를 제공함
  * 모듈은 module이라는 새 키워드에 이름과 바디를 추가해서 정의함
    * 기술적으로 module, requires, export 같은 자바 9의 모듈 관련 용어는 제한된 키워드(restricted keyword)
      * 따라서 하위 버전 호환성을 이유로 이들 식별자를 프로그램의 원하는 곳에 자유롭게 사용할 수 있지만  
        모듈이 허용된 곳에서는 이들 용어가 키워드로 해석됨
  * 모듈 디스크립터(module descriptor)는 module-info.java라는 특별한 파일에 저장됨
    * 엄밀하게는 텍스트 형식을 모듈 선언(module declaration)이라고 함
    * module-info.class에 저장된 바이너리 형식을 모듈 디스크립터라고 함
  * 모듈 디스크립터는 보통 패키지와 같은 폴더에 위치
     * 한 개 이상의 패키지를 서술하고 캡슐화 할수 있지만 단순한 상황에서는 이들 패키지중 한 개만 외부로 노출 시킴
  * 직소 퍼즐에 비유
    * exports는 돌출부
    * requires는 패인 부분으로 생각할 수 있음

### 자바 모듈 시스템으로 애플리케이션 개발하기
* 애플리케이션 셋업
  * 비용 관리를 해줄 앱을 구현한다고 가정
    * 다음과 같은 여러 작업 처리
      * 파일이나 URL에서 비용 목록을 읽음
      * 비용의 문자열 표현을 파싱함
      * 통계를 계산함
      * 유용한 요약 정보를 표시함
      * 각 태스크의 시작, 마무리 지점을 제공함
    * 앱의 개념을 모델링할 여러 클래스와 인터페이스를 정의해야 함
      * 먼저 Reader 인터페이스는 소스에서 얻어온 직렬화된 지출을 읽는 역할
        * 소스가 어디냐에 따라 HttpReader, FileReader 등 여러 구현을 제공해야 함
      * 또한 JSON 객체를 자바 앱에서 사용할 수 있는 도메인 객체 Expense로 재구성할 Parser 인터페이스도 필요
      * 마지막으로 Expense 객체 목록으로 통계를 계산하고 SummaryStatistics 객체를 반환하는 SummaryCalculator 클래스 필요
    * 모듈화 방법
      * 분리할 수 있는 여러 기능(관심사)
        * 다양한 소스에서 데이터를 읽음
          * Reader, HttpReader, FileReader
        * 다양한 포맷으로 구성된 테이블 파싱
          * Parser, JSONParser, ExpenseJSON-Parser
        * 도메인 객체를 구체화
          * Expense
        * 통계를 계산하고 반환
          * SummaryCalculator, SummaryStatistics
        * 다양한 기능을 분리 조정
          * ExpenseApplication
    * 각 기능을 그룹화 (교수법에 따라 아주 세부적으로 문제를 나누는 접근 방법 이용)
      * expenses.readers
      * expenses.readers.http
      * expenses.readers.file
      * expenses.parsers
      * expenses.parsers.json
      * expenses.model
      * expenses.statistics
      * expenses.application
  * 이 간단한 앱에서는 모듈 시스템의 여러 부분이 두드러질 수 있도록 잘게 분해
    * 단순한 프로젝트를 잘게 분해해 기능까지 캡슐화한다면 장점에 비해 초기 비용이 높아지는 등 옳은 결정인가 논란이 생길 수 있음
    * 하지만 프로젝트가 커지면서 많은 내부 구현이 추가되면 이때부터 캡슐화와 추론의 장점이 두드러짐
    * 위에서 나열한 목록을 앱 경계에 의존하는 패키지 목록으로 생각할 수 있음
    * 아마 각 모듈은 다른 모듈로 노출하고 싶지 않는 내부 구현을 포함할 것
    * 예를 들어 expenses.statistics 모듈은 실험적인 통계 방법을 다른 방법으로 구현한 여러 패키지를 포함할 수 있음
    * 이들 패키지에서 어떤 것을 사용자에게 릴리스할지는 나중에 결졍할 수 있음

* 세부적인 모듈화와 거친 모듈화
  * 시스템을 모듈화할 때 모듈 크기를 결정해야 함
    * 세부적인 모듈화 기법 대부분은 모든 패키지가 자신의 모듈을 가짐
      * 이득에 비해 설계 비용이 증가함
    * 거친 모듈화 기법은 한 모듈이 시스템의 모든 패키지를 포함함
      * 모듈화의 모든 장점을 잃음
  * 주기적으로 확인하는 프로세스를 가짐
    * 가장 좋은 방법은 시스템을 실용적으로 분해하면서 진화하는 소프트웨어 프로젝트가 이해하기 쉽고 고치기 쉬운 수준으로 적절하게 모듈화되어 있는지를 확인
  * 요약하면 모듈화는 소프트웨어 부식의 적

* 자바 모듈 시스템 기초
  * 메인 앱을 지원하는 한 개의 모듈만 갖는 기본적인 모듈화 앱부터 시작
    * 디렉터리 안에 중첩된 프로젝트 디렉터리 구조
      * |-- expenses.application
        * |-- module-info.java
          * |-- com
            * |-- example
              * |-- expenses
                * |-- application
                  * |-- ExpensesApplication.java
  * module-info.java
    * 모듈 디스크립터
    * 모듈의 소스코드 파일 루트에 위치해야 함
    * 모듈의 의존성과 어떤 기능을 외부로 노출할지를 정의함
    * 지출 앱 예제에서는 아직 다른 모듈에 의존하거나 외부로 노출하는 기능이 없으므로 최상의 수준의 module-info.java 파일에 이름만 정의 되어 있음
      * 내용은 비어있음
    * 내용
      ```
      module expenses.application {
      }
      ```
  * 모듈화 앱 실행 방법?
    * 보통 IDE, 빌드 시스템에서 이들 명령을 자동으로 처리하지만
    * 이들 명령이 어떤 동작을 수행하는지 확인하는 것은 내부적으로 어떤 일이 일어나는지 이해하는 데 도움이 됨
    * 모듈 소스 디렉터리에서 다음 명령 실행
      ```
      javac module-info.java
          com/example/expenses/application/ExpensesApplication.java -d target
      
      jar cvfe expenses-application.jar
          com.example.expenses.application.ExpensesApplication -C target
      ```
      * 그럼 어떤 폴더와 클래스 파일이 생성된 JAR에 포함되어 있는지를 보여주는 다음과 같은 결과 출력됨
        * JAR(expenses-application.jar)
          ```
          added manifest
          added module-info: module-info.class adding: com/(in = 0) (out= 0)(stored 0%)
          added: com/example/(in = 0) (out= 0)(stored 0%)
          added: com/example/expenses/(in = 0) (out= 0)(stored 0%)
          added: com/example/expenses/application(in = 0) (out= 0)(stored 0%)
          added: com/example/expenses/application/ExpensesApplication.class(in = 456) (out= 306)(deflated 32%)
          ```
        * 마지막으로 생성된 JAR를 모듈화 앱으로 실행
          ```
          java --module-path expenses-application.jar \ 
               --module expenses/com.example.expenses.application.ExpensesApplication
          ```
      * 처음 두 과정은 자바 앱을 JAR로 패키징하는 표준 방법
        * 새로운 부분은 컴파일 과정에 새로 추가된 module-info.java
  * java 프로그램으로 자바.class 파일을 실행할 때 다음과 같은 두가지 옵션이 새로 추가됨
    * --module-path
      * 어떤 모듈을 로드할 수 있는지 지정함
      * 이 옵션은 클래스 파일을 지정하는 --classpath 인수와는 다름
    * --module
      * 이 옵션은 실행할 메인 모듈과 클래스를 지정함
  * 모듈 정의는 버전 문자열을 포함하지 않음
    * 자바 9 모듈 시스템에서는 버전 선택 문제를 크게 고려하지 않았음
    * 따라서 버전 기능은 지원하지 않음
    * 대신 버전 문제는 빌드 도구나 컨테이너 앱에서 해결해야 할 문제로 넘김

### 여러 모듈 활용하기
* 비용 앱이 소스에서 비용을 읽을 수 있어야 함
* 이 기능을 캡슐화한 expenses.readers라는 새 모듈을 만들 것
* expenses.application, expenses.readers 두 모듈간의 상호 작용은 자바 9에서 지정한 export, requires를 이용해 이루어짐

* exports 구문
  * expenses.readers 모듈 선언
    ```
    module expenses.readers {
        // 이들은 모듈명이 아니라 패키지명
        export com.example.expenses.readers;
        export com.example.expenses.readers.file;
        export com.example.expenses.readers.http;
    }
    ```
  * exports는 다른 모듈에서 사용할 수 있도록 특정 패키지를 공개 형식으로 만듦
  * 기본적으로 모듈 내의 모든 것은 캡슐화됨
    * 모둘 시스템은 화이트 리스트 기법을 이용해 강력한 캡슐화를 제공하므로 다른 모듈에서 사용할 수 있는 기능이 무엇인지 명시적으로 결정해야 함
      * 이 접근법은 실수로 어떤 기능을 외부로 노출함으로 몇 년이 지난 뒤에 해커가 시스템을 남용할 여지를 방지함
  * 프로젝트의 두 모듈 디렉터리 구조
    * expenses.application
      * |-- expenses.application
        * |-- module-info.java
        * |-- com
          * |-- example
            * |-- expenses
              * |-- application
                * |-- ExpensesApplication.java
    * expenses.readers
      * |-- expenses.readers
        * |-- module-info.java
        * |-- com
          * |-- example
            * |-- expenses
              * |-- readers
                * |-- Reader.java
              * |-- file
                * |-- FileReader.java
              * |-- HttpReader.java

* requires 구문
  * 또는 다음처럼 module-info.java를 구현
    ```
    module expenses.readers {
        requires java.base; // 패키지명이 아니라 모듈명
    
        // 이들은 모듈명이 아니라 패키지명
        exports com.example.expenses.readers;
        exports com.example.expenses.readers.file;
        exports com.example.expenses.readers.http;
    }
    ```
  * requires는 의존하고 있는 모듈을 지정함
    * 기본적으로 모든 모듈은 java.base라는 플랫폼 모듈에 의존함
    * 이 플랫폼 모듈은 net, io, util 등의 자바 메인 패키지를 포함함
    * 항상 기본적으로 필요한 모듈이기 때문에 명시적으로 정의할 필요가 없음
    * 자바에서 "class Foo extends Object {...}"처럼 하지 않고 "class Foo {...}"처럼 클래스를 정의하는 것과 같은 원리
    * 따라서 java.base 외의 모듈을 임포트할 때 requires를 사용함
  * 자바 9에서는 requires와 exports 구문을 이용해 더 정교하게 클래스 접근을 제어할 수 있음
    * 자바 9 이전과 이후의 접근자와 가시성 변화를 표여주는 표
    * 자바 9에서는 클래스 가시성을 더 잘 제어할 수 있는 기능을 제공

     |      클래스 가시성      | 자바 9 이전 | 자바 9 이후 |           기타            |
     |---------------------|----------|-----------|--------------------------|
     | 모든 클래스가 모두에 공개됨 |    OO    |    OO    | exports와 requires 구분 혼합 |
     | 제한된 클래스만 공개됨     |    XX    |    OO    | exports와 requires 구분 혼합 |
     | 한 모듈의 내에서만 공개    |    XX    |    O     | export 구문 없음             |
     | Protected            |    OO    |    OO    |                           |
     | Package              |    OO    |    OO    |                           |
     | Private              |    OO    |    OO    |                           |

* 이름 정하기
  * 오라클은 패키지명처럼 인터넷 도메인명을 역순으로 모듈의 이름을 정하도록 권고함
    * 예를 들어 com.iteratrlearning.training
  * 더욱이 모듈명은 노출된 주요 API 패키지와 이름이 같아야 한다는 규칙을 따라야 함
  * 모듈명은 작성자의 인터넷 도메인명을 역순으로 시작해야 함
    * 모듈이 패키지를 포함하지 않거나 어떤 다른 이유로 노출된 패키지 중 하나와 이름이 일치하지 않는 상황을 제외

### 컴파일과 패키징
* 메이븐 빌드 도구를 이용해 프로젝트 컴파일한다고 가정
* 먼저 각 모듈에 pom.xml을 추가
* 사실 각 모듈은 독립적으로 컴파일되므로 자체적으로 각각이 한 개의 프로젝트
* 전체 프로젝트 빌드를 조정할 수 있도록 모든 모듈의 부모 모듈에도 pom.xml을 추가함
* 전체 구조
  * |-- pom.xml
  * |-- expenses.application
    * |-- pom.xml
    * |-- src
      * |-- main
        * |-- java
          * |-- module-info.java
          * |-- com
            * |-- example
              * |-- expenses
                * |-- application
                  * |-- ExpensesApplication.java
  * |-- expenses.readers
    * |-- pom.xml
    * |-- src
      * |-- main
        * |-- java
          * |-- module-info.java
          * |-- com
            * |-- example
              * |-- expenses
                * |-- readers
                  * |-- Reader.java
                * |-- file
                  * |-- FileReader.java
                * |-- http
                  * |-- HttpReader.java
* 이렇게 세 개의 pom.xml 파일을 추가해 메이븐 디렉터리 구조를 완성
  * 모듈 디스크립터(module-info.java)는 src/main/java 디렉터리에 위치해야함
  * 올바른 모듈 소스 경로를 이용하도록 메이븐이 javac를 설정
  * 다음은 expenses.readers 모듈 pom.xml
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.example</groupId>
        <artifactId>expenses.readers</artifactId>
        <version>1.0</version>
        <packaging>jar</packaging>
        <parent>
            <groupId>com.example</groupId>
            <artifactId>expenses</artifactId>
            <version>1.0</version>
        </parent>
    </project>
    ```
    * 순조롭게 빌드될 수 있도록 명시적으로 부모 모듈을 지정한 코드
    * 부모 ID expenses를 포함하는 부산물(artifact)
    * pom.xml에 부모를 정의해야 함
  * 다음은 expenses.application 모듈 pom.xml
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.example</groupId>
        <artifactId>expenses.application</artifactId>
        <version>1.0</version>
        <packaging>jar</packaging>
    
        <parent>
            <groupId>com.example</groupId>
            <artifactId>expenses</artifactId>
            <version>1.0</version>
        </parent>
    
        <dependencies>
            <dependency>
                <groupId>com.example</groupId>
                <artifactId>expenses.readers</artifactId>
                <version>1.0</version>
            </dependency>
        </dependencies>
    
    </project>
    ```
  * 다음은 모듈 expenses.readers와 expenses.application를 참조하도록 완성한 pom.xml
    * 메이븐은 특별한 xml요소 \<module>을 가진 여러 메이븐 모듈을 가진 프로젝트를 지원함
      * \<module> : 자식의 부산물 ID를 참조
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.example</groupId>
        <artifactId>expenses.application</artifactId>
        <packaging>pom</packaging>
        <version>1.0</version>
    
        <modules>
            <module>expenses.application</module>
            <module>expenses.readers</module>
        </modules>
    
        <build>
            <pluginManagement>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-compiler-plugin</artifactId>
                        <version>3.7.0</version>
                        <configuration>
                            <source>9</source>
                            <target>9</target>
                        </configuration>
                    </plugin>
                </plugins>
            </pluginManagement>
        </build>
        
    </project>
    ```
  * ``` mvn clean package ``` 명령을 실행해 프로젝트의 모듈을 JAR로 만들 수 있음
    * 다음과 같은 부산물이 만들어짐
      ``` ./expenses.application/target/expenses.application-1.0.jar ```
      ``` ./expenses.readers/target/expenses.readers-1.0.jar ```
    * 두 JAR를 다음처럼 모듈 경로에 포함해서 모듈 앱을 실행할 수 있음
      ```
      java --module-path \
      ./expenses.application/target/expenses.application-1.0.jar:\
      ./expenses.readers/target/expenses.readers-1.0.jar \
        --module \
       expenses.application/com.example.expenses.application.ExpensesApplication
      ```

### 자동 모듈
* HttpReader를 저수준으로 구현하지 않고 아파치 프로젝트의 httpclient 같은 특화 라이브러리를 사용해 구현한다고 가정
  * requires 구문 이용
    * expenses.readers 프로젝트의 module-info.java에 구문 추가
    * ``` mvn clean package ``` 다시 실행
    * ``` [ERROR] module not found: httpclient ```
    * 의존성을 기술하도록 pom.xml도 갱신해야 하므로 에러가 발생함
    * 메이븐 컴파일러 플러그인은 module-info.java를 포함하는 프로젝트를 빌드할 때  
      모든 의존성 모듈을 경로에 놓아 적절한 JAR를 내려받고 이들이 프로젝트에 인식되도록 함
    * 다음과 같은 의존성 필요
      ```
      <dependency>
          <groupId>org.apache.httpcomponents</groupId>
          <artifactId>httpclient</artifactId>
          <version>4.5.3</version>
      </dependency>
      ```
    * ``` mvn clean package ``` 다시 실행하면 프로젝트가 올바르게 빌드됨
    * 하지만 httpclient는 자바 모듈이 아님
      * httpclient는 자바 모듈로 사용하려는 외부 라이브러리, 모듈화가 되어 있지 않은 않음
    * 자바는 JAR를 자동 모듈이라는 형태로 적절하게 변환함
    * 모듈 경로상에 있으나 module-info 파일을 가지지 않은 모든 JAR는 자동 모듈이 됨
    * 자동 모듈은 암묵적으로 자신의 모든 패키지를 노출시킴
    * 자동 모듈의 이름은 JAR 이름을 이용하여 정해짐
    * jar 도구의 --describe-module 인수를 이용해 자동으로 정해지는 이름을 바꿀 수 있음
      ```
      jar --file=./expenses.readers/target/dependency/httpclient-4.5.3.jar \
          --describe-module httpclient@4.5.3 automatic
      ```
      * 그러면 httpclient라는 이름으로 정의됨
    * 마지막으로 httpclientJAR를 모듈 경로에 추가한 다음 앱을 실행함
      ```
      java --module-path \
      ./expenses.application/target/expenses.application-1.0.jar:\
      ./expenses.readers/target/expenses.readers-1.0.jar \
      ./expenses.readers/target/dependency/httpclient-4.5.3.jar \
        --module \
       expenses.application/com.example.expenses.application.ExpensesApplication
      ```
  * 메이븐에는 자바 9 모듈 시스템을 더 잘 지원하는 프로젝트가 있음
    * https://github.com/moditect/moditect
    * module-info 파일을 자동으로 생성함

### 모듈 정의와 구문들
* 간단한 몇 가지 키워드
  * module 지시어
    * com.iteratrlearning.training 모듈명 예제
      ```
      module com.iteratrlearning.training {
      }
      ```

* requires
  * 컴파일 타임과 런타임에 한 모듈이 다른 모듈에 의존함을 정의
  * 예를 들어 com.iteratrlearning.application은 com.iteratrlearning.ui 모듈에 의존함
    ```
    module com.iteratrlearning.application {
        requires com.iteratrlearning.ui;
    }
    ```
  * 그러면 com.iteratrlearning.ui에서 외부로 노출한 공개 형식을 com.iteratrlearning.application에서 사용 가능

* exports
  * 지정한 패키지를 다른 모듈에서 이용할 수 있또록 공개 형식으로 만듦
  * 아무 패키지도 공개하지 않는 것이 기본 설정
  * 어떤 패키지를 공개할 것인지를 명시적으로 지정함으로 캡슐화를 높일 수 있음
  * com.iteratrlearning.ui.panels와 com.iteratrlearning.ui.widgets 공개 예제
    * 참고로 문법이 비슷함에도 불구하고 exports는 패키지명을 인수로 받지만 requires는 모듈명일 인수로 받는다는 사실 주의
    ```
    module com.iteratrlearning.ui {
        requires com.iteratrlearning.core;
        exports com.iteratrlearning.ui.panels;
        exports com.iteratrlearning.ui.widgets;
    }
    ```

* requires transitive
  * 다른 모듈이 제공하는 공개 형식을 한 모듈에서 사용할 수 있다고 지정 가능
  * 예를 들어 exports com.iteratrlearning.ui 모듈 정의에서 requires를 requires-transitive로 바꿀 수 있음
    ```
    module com.iteratrlearning.ui {
        requires transitive com.iteratrlearning.core;
    
        exports com.iteratrlearning.ui.panels;
        exports com.iteratrlearning.ui.widgets;
    }
    
    module com.iteratrlearning.application {
        requires com.iteratrlearning.ui;
    }
    ```
    * 결과적으로 com.iteratrlearning.application 모듈은 com.iteratrlearning.core에서 노출한 공개 형식에 접근할 수 있음
    * 필요로 하는 모듈(com.iteratrlearning.ui)이 다른 모듈(com.iteratrlearning.core)의 형식을 반환하는 상황에서  
      전이성(transitivity) 선언을 유용하게 사용할 수 있음
    * com.iteratrlearning.application 모듈 내부에 com.iteratrlearning.core 모듈을 다시 선언하는 것은 성가신 일
    * 따라서 이런 상황에서는 transitive를 이용하여 문제 해결
    * com.iteratrlearning.io 모듈에 의존하는 모든 모듈은 자동으로 com.iteratrlearning.core 모듈을 읽을 수 있게 됨

* exports to
  * 사용자에게 공개할 기능을 제한함으로 가시성을 좀 더 정교하게 제어할 수 있음
  * com.iteratrlearning.ui.widghtes의 접근 권한을 가진 사용자의 권한을 com.iteratrlearning.ui.widgetuser로 제한 가능
    ```
    module com.iteratrlearning.ui {
        requires com.iteratrlearning.core;
    
        exports com.iteratrlearning.ui.panels;
        exports com.iteratrlearning.ui.widgets to com.iteratrlearning.uiwidgetuser;
    }
    ```

* open과 opens
  * 모든 패키지를 다른 모듈에 반사적으로 접근을 허용할 수 있음
  * 반사적인 접근 권한을 주는 것 이외에 open 한정자는 모듈의 가시성에 다른 영향을 미치지 않음
    ```
    open com.iteratrlearning.ui {
    }
    ```
  * 자바 9 이전에는 리플렉션으로 객체의 비공개 상태 확인 가능했음
  * 즉 진정한 캡슐화는 존재하지 않았음
  * 하이버네이트(Hibernate) 같은 객체 관계 매핑(Object-relational mapping : ORM) 도구에서는 이런 기능을 이용해 상태를 직접 고치곤 함
  * 자바 9에서는 기본적으로 리플렉션이 이런 기능을 허용하지 않음
  * 이런 기능 필요시 open 구문을 명시적으로 사용해야 함
  * 리플렉션 때문에 전체 모듈을 개방하지 않고도 opens 구문을 모듈 선언에 이용해 필요한 개별 패키지만 개방할 수 있음
  * exports-to로 노출한 패키지를 사용할 수 있는 모듈을 한정했던 것처럼, open에 to를 붙여서 반사적인 접근을 특정 모듈에만 허용할 수 있음

* uses와 provides
  * provies 구문으로 서비스 제공자를, uses 구문으로 서비스 소비자를 지정할 수 있는 기능 제공
  * 서비스와 ServiceLoader를 안다면 친숙한 내용
  * 고급 주제

### 더 큰 예제 그리고 더 배울 수 있는 방법
* 오라클 자바 문서에 가져온 예제
  ```
  module com.example.foo {
      requires com.example.foo.http;
      requires java.logging;
      requires transitive com.example.foo.network;
  
      exports com.example.foo.bar;
      exports com.example.foo.internal to com.example.foo.probes;
  
      opens com.example.foo.quux;
      opens com.example.foo.internal to com.example.foo.network, com.example.foo.probe;
  
      uses com.example.foo.spi.Intf;
      provides com.example.foo.spi.Intf with com.example.foo.Impl;
  }
  ```
* 앱을 자바 9로 이전할 때 EE와 관련한 여러 패키지가 모듈화된 자바 9 가상 머신에서 기본적으로 로드되지 않음
  * 예를 들어 JAXB API 클래스는 이제 자바 EE API로 간주되므로 자바 SE 9의 기본 클래스 경로에 더는 포함되지 않음
  * 따라서 호환성을 유지하려면 ``` --add-modules ``` 명령행을 이용해 명시적으로 필요한 모듈을 추가해야 함
  * 예를 들어 java.xml.bind가 필요하면 ``` --add-modules java.xml.bind ``` 지정해야 함

### 정리
* 관심사분리와 정보 은닉은 추론하기 쉬운 소프트웨어를 만드는 중요한 두 가지 원칙
* 자바 9 이전에는 각각의 기능을 담당하는 패키지, 클래스, 인터페이스로 모듈화를 구현했는데 효과적인 캡슐화를 달성하기엔 역부족
* 클래스 경로 지옥 문제는 앱의 의존성을 추론하기 더욱 어렵게 만듦
* 자바 9 이전의 JDK는 거대했으며 높은 유지 비용과 진화를 방해하는 문제가 존재했음
* 자바 9에서는 새로운 모듈 시스템 제공, module-info.java 파일의 모듈의 이름을 지정하며 필요한 의존성(requires)과 공개 API(exports)를 정의함
* requires 구문으로 필요한 다른 모듈을 정의할 수 있음
* exports 구문으로 특정 패키지를 다른 모듈에서 사용할 수 있는 공개 형식으로 지정할 수 있음
* 인터넷 도메인명을 역순으로 사용하는 것이 권장 모듈 이름 규칙
* 모듈 경로에 포함된 JAR 중에 module-info 파일을 포함하지 않는 모든 JAR는 자동 모듈이 됨
* 자동 모듈은 암묵적으로 모든 패키지를 공개함
* 메이븐은 자바 9 모듈 시스템으로 구조화된 앱을 지원함

### [quiz]
---

## chapter 15 - CompletableFuture와 리액티브 프로그래밍 컨셉의 기초
* 최근 소프트웨어 개발 방법을 획기적으로 뒤집는 두 가지 추세가 있음
  * 한 가지는 앱을 실행하는 하드웨어와 관련된 것
    * 멀티코어 프로세서가 발전하면서 앱의 속도는 멀티코어 프로세서를 얼마나 잘 활용할 수 있도록 소프트웨어를 개발하는가에 따라 달라질 수 있음
    * 한 개의 큰 태스크를 병렬로 실행할 수 있는 개별 하위 태스크로 분리할 수 있음
    * 또한 자바 7부터 사용할 수 있는 포크/조인 프레임워크
    * 자바8에 추가된 병렬 스트림으로 스레드에 비해 단순하고 효과적인 방법으로 병렬 실행을 달성할 수 있음
  * 다른 하나는 앱을 어떻게 구성하는가와 관련된 것
    * 인터넷 서비스에서 사용하는 앱이 증가하고 있는 현상을 반영함
    * 예를 들어 마이크로서비스 아키텍처 선택이 지난 몇 년간 증가함
      * 하나의 거대한 앱 대신 작은 서비스로 앱을 나누는 것
      * 서비스가 작아진 대신 네트워크 통신이 증가함
      * 마찬가지로 구글(지역화된 정보), 페이스북(소셜 정보), 트위터(뉴스) 등의 공개 API를 통해 더 많은 인터넷 서비스를 접할 수 있게 됨
      * 요즘은 독립적으로만 동작하는 웹사이트나 네트워크 앱을 찾아보기 힘듦
      * 향후 앱은 다양한 소스의 콘텐츠를 가져와서 사용자가 삶을 풍요롭게 만들도록 합치는 매시업(mashup) 형태가 될 가능성이 큼
    * 여러 언어로 특정 주제의 댓글 추세를 페이스북이나 트위터 API로 찾은 다음 내부 알고리즘과 가장 일치하는 주제로 등급을 매겨서 주어진  
      프랑스 사용자들에게 제공할 소셜미디어 정서를 수집하고 요약하는 웹 사이트를 만들 수 있음
    * 구글 번역을 이용해 코멘트를 프랑스어로 번역하거나 구글 맵스를 이용해 작성자의 위치 정보를 얻고, 정보를 모두 합쳐 웹 사이트에 표시할 수 있음
    * 물론 외부 네트워크 서비스의 반응 속도가 느린 상황을 대비해 맵 서버가 응답하거나 심지어 타임아웃할 때까지  
      빈 화면 대신 텍스트 결과와 일반 맵에 물음표를 추가해 보여줄 수 있음
    * 이런 앱을 구현하려면 인터넷으로 여러 웹 서비스에 접근해야 함
      * 하지만 이들 서비스의 응답을 기다리는 동안 연산이 블록되거나 귀중한 CPU 클록 사이클 자원을 낭비하기 싫음
      * 예를 들어 페이스북의 데이터를 기다리는 동안 트위터 데이터를 처리할 수 있어야 함
      * 이 상황은 멀티태스크 프로그래밍의 양면성을 보여줌
    * 포크/조인 프레임워크와 병렬 스트림은 병렬성의 귀중한 도구
      * 이들은 한 태스크를 여러 하위 태스크로 나눠서 CPU의 다른 코어 또는 다른 머신에서 이들 하위 태스크를 병렬로 실행함
  * 반면 병렬성이 아니라 동시성을 필요로 하는 상황 즉 조금씩 연관된 작업을 같은 CPU에서 동작하는 것 또는 앱의 생산성을 극대화할 수 있도록  
    코어를 바쁘게 유지하는 것이 목표라면, 원격 서비스나 DB 결과를 기다리는 스레드를 블록함으로 연산 자원을 낭비하는 일을 피해야 함
    * 자바는 이러환 환경에서 사용할 수 있는 두 가지 주요 도구를 제공
      * Future 인터페이스
        * 자바 8의 CompletableFuture 구현은 간단하고 효율적인 해결사
      * 플로 API
        * 자바 9에 추가된 발행 구독 프로토콜에 기반한 리액티브 프로그래밍 개념을 따름
    * 동시성과 병렬성 차이
      * 동시성은 단일 코어 머신에서 발생할 수 있는 프로그래밍 속성으로 실행이 서로 겹칠 수 있음
      * 병렬성은 병렬 실행을 하드웨어 수준에서 지원함

### 동시성을 구현하는 자바 지원의 진화
* 자바 동시 프로그래밍 지원은 지난 20년간 하드웨어, 소프트웨어 시스템, 프로그래밍 컨셉의 큰 변화에 맞춰 진화
* 지금까지의 발전 요약을 보면서 프로그래밍과 시스템 설계의 새로운 기능이 추가된 이유와 역할을 더 잘 이해할 수 있음
  * 처음 자바는 Runnable, Thread를 동기화된 클래스와 메서드를 이용해 잠갔음
  * 자바 5는 좀 더 표현력있는 동시성을 지원
    * 특히 스레드 실행과 태스크 제출을 분리하는 ExecutorService 인터페이스 지원
    * 높은 수준의 결과 즉 Runnable, Thread의 변형을 반환하는 Callable\<T> and Future\<T>, 제네릭 등을 지원
    * ExecutorServices는 Runnable, Callable 둘 다 실행 가능
    * 이런 기능들 덕분에 멀티코어 CPU에서 쉽게 병렬 프로그래밍을 구현할 수 있음
  * 멀티코어 CPU에서 효과적으로 프로그래밍을 실행할 필요성이 커지면서 이후 자바버전에서 개선된 동시성 지원이 추가됨
    * 자바 7에서 분할 그리고 정보 알고리즘의 포크/조인 구현을 지원하는 java.util.concurrent.RecursiveTask 추가
    * 자바 8에서 스트림과 람다 지원에 기반한 병렬 프로세싱이 추가
  * 자바는 Future를 조합하는 기능을 추가하면서 동시성을 강화했고, 자바 9에서는 분산 비동기 프로그래밍을 명시적으로 지원함
    * 이들 API는 매쉬업 앱, 즉 다양한 웹 서비스를 이용하고 이들 정보를 실시간으로 조합해 사용자에게 제공하거나  
      추가 웹 서비스를 통해 제공하는 종류의 앱을 개발하는데 필수적인 기초 모델과 툴킷을 제공함
    * 이 과정을 리액티브 프로그래밍이라 부름
      * 자바 9에서는 발행-구독 프로토콜(java.util.concurrent.Flow 인터페이스 추가)로 이를 지원함
  * CompletableFuture와 java.util.concurrent.Flow의 궁극적인 목표는 가능한한 동시에 실행할 수 있는 독립적인 태스크를 가능하게 만들고  
    멀티코어 또는 여러 기기를 통해 제공되는 병렬성을 쉽게 이용하는 것

* 스레드와 높은 수준의 추상화
  * 단일 CPU 컴퓨터도 여러 사용자를 지원할 수 있는데 이는 운영체제가 각 사용자에 프로세스 하나를 할당하기 때문
    * 운영체제는 두 사용자가 각각 자신만의 공간에 있다고 생각할 수 있도록 가상 주소 공간을 각각의 프로세스에 제공함
    * 운영체제는 주기적으로 번갈아가며 각 프로세스에 CPU를 할당함
    * 프로세스는 다시 운영체제에 한 개 이상의 스레드, 즉 본인이 가진 프로세스와 같은 주소 공간을 공유하는 프로세스를 요청함으로  
      태스크를 동시에, 협력적으로 실행할 수 있음
  * 멀티코어 설정(한 사용자 프로세스만 실행하는 한명의 사용자 노트북)에서는 스레드의 도움 없이 프로그램이 노트북의 컴퓨팅 파워를 모두 활용할 수 없음
    * 각 코어는 한 개 이상의 프로세스나 스레드에 할당될 수 있지만 프로그램이 스레드를 사용하지 않는다면 효율성을 고려해서 프로세서 코어 중 한 개만을 사용할 것
    * 실제로 네 개의 코어를 가진 CPU에서 이론적으로는 프로그램을 네 개의 코어에서 병렬로 실행함으로 실행 속도를 네 배까지 향상 가능
      * 물론 오버헤드로 인해 실제 네 배가 되긴 어려움
    * 학생들이 제출한 숫자 1,000,000개를 저장한 배열을 처리하는 예제
      ```
      long sum = 0;
      for (int i = 0; i < 1_000_000; i++) {
          sum += stats[i];
      }
      ```
      * 위 코드는 한 개의 코어로 며칠동안 작업을 수행함
      * 아래 코드는 첫 스레드 실행
        ```
        long sum0 = 0;
        for (int i = 0; i < 250_000; i++) {
            sum0 += stats[i];
        }
        ```
      * 아래 코드는 네 번째 스레드 실행
        ```
        long sum3 = 0;
        for (int i = 750_000; i < 1_000_000; i++) {
            sum3 += stats[i];
        }
        ```
      * 메인 프로그램은 네 개의 스레드를 완성, 자바의 .start()로 실행한 다음 .join()으로 완료될 때까지 대기 후 다음 계산
        ```
        sum = sum0 + .. + sum3;
        ```
        * 이를 각 루프로 처리하는 것은 성가시며 쉽게 에러가 발생할 수 있는 구조
          * 루프가 아닌 코드라면 어떻게 처리할지도 난감
      * 자바 스트림으로 외부 반복 대신 내부 반복을 통해 쉽게 병렬 처리 달성
        ``` sum = Arrays.stream(stats).parallel().sum(); ```
  * 결론적으로 병렬 스트림 반복은 명시적으로 스레드를 사용하는 것에 비해 높은 수준의 개념
    * 스트림을 이용해 스레드 사용 패턴을 추상화 가능
    * 스트림으로 추상화하는 것은 디자인 패턴을 적용하는 것과 비슷
      * 쓸모 없는 코드가 라이브러리 내부로 구현되면서 복잡성도 줄어든다는 장점이 더해짐
    * java.util.concurrent.RecursiveTask 지원 덕분에 포크/조인 스레드 추상화로 분할, 정복 알고리즘을 병렬화하면서  
      멀티코어 머신에서 배열의 합을 효율적으로 계산하는 높은 수준의 방식을 제공

* Executor와 스레드 풀
  * 자바 5는 Executor 프레임워크와 스레드 풀을 통해 스레드의 힘을 높은 수준으로 끌어올림
    * 자바 프로그래머가 태스크 제출과 실행을 분리할 수 있는 기능을 제공
  * 스레드의 문제
    * 자바 스레드는 직접 운영체제 스레드에 접근함
    * 운영체제 스레드를 만들고 종료하려면 비싼 비용(페이지 테이블과 관련된 상호작용)을 치러야 함
    * 더욱이 운영체제 스레드의 숫자는 제한되어 있는 것이 문제
    * 운영체제가 지원하는 스레드 수를 초과해 사용하면 자바 앱이 예상치 못한 방식으로 크래시될 수 있음
    * 기존 스레드가 실행되는 상태에서 계속 새로운 스레드를 만들지 않도록 주의
    * 보통 운영체제와 자바의 스레드 개수가 하드웨어 스레드 개수보다 많음
      * 인텔 i7-6900K 같은 CPU는 각 코어가 여러 하드웨어 스레드를 포함
      * 캐시 미스(cache miss) 같은 잠깐의 지연 동안에도 유용한 명령어를 실행할 수 있음
    * 일부 운영체제 스레드가 블록되거나 자고 있는 상황에서 모든 하드웨어 스레드가 코드를 실행하도록 할당된 상황에 놓일 수 있음
      * 예를 들어 2016 인텔 코어 i7-9600K 서버 프로세스는 8개의 코어를 가지며
      * 각 코어는 두 개의 대칭 멀티 프로세싱(symmetic multiprocessing - SMP) 하드웨어 스레드를 포함
      * 하드웨어 스레드를 총 16개 포함
      * 서버에는 프로세서를 여러 개 포함할 수 있으므로 일반적으로 하드웨어 스레드 64개를 보유할 수 있음
      * 반면 노트북은 하드웨어 스레드를 한두 개 가지므로 다양한 기기에서 실행할 수 있는 프로그램에서는 미리 하드웨어 스레드 개수를 추측하지 않는 것이 좋음
      * 한편 주어진 프로그램에서 사용할 최적의 자바 스레드 개수는 사용할 수 있는 하드웨어 코어의 개수에 따라 달라짐

* 스레드 풀 그리고 스레드 풀이 더 좋은 이유
  * 자바 ExecutorService는 태스크를 제출하고 나중에 결과를 수집할 수 있는 인터페이스를 제공
  * 프로그램은 newFixedThreadPool 같은 팩토리 메서드 중 하나를 이용해 스레드 풀을 만들어 사용 가능
    ``` ExecutorService newFixedThreadPool(int nThreads) ```
    * 이 메서드는 워커 스레드라 불리는 nThreads를 포함하는 ExecutorService를 만들고 이들을 스레드 풀에 저장
    * 스레드 풀에서 사용하지 않는 스레드로 제출된 태스크를 먼저 온 순서대로 실행
    * 이들 태스크 실행이 종료되면 스레드를 풀로 반환
    * 이 방식의 장점은 하드웨어에 맞는 수의 태스크를 유지함과 동시에 수 천개의 태스크를 스레드 풀에 아무 오버헤드 없이 제출할 수 있다는 것
    * 큐의 크기 조정, 거부 정책, 태스크 종류에 따른 우선순위 등 다양한 설정 가능
    * 프로그래머는 태스크(Runnable, Callable)를 제공하면 스레드가 이를 실행함

* 스레드 풀 그리고 스레드 풀이 나쁜 이유
  * 거의 모든 관점에서 스레드를 직접 사용하는 것보다 스레드 풀을 이용하는 것이 바람직하지만 두 가지 사항을 주의할 것
    * k 스레드를 가진 스레드 풀은 오직 k만큼의 스레드를 동시에 실행할 수 있음
      * 초과로 제출된 태스크는 큐에 저장되며 이전에 태스크 중 하나가 종료되기 전까지는 스레드에 할당되지 않음
      * 불필요하게 많은 스레드를 만드는 일을 피할 수 있으므로 보통 이 상황은 아무 문제가 되지 않지만
        * 잠을 자거나 I/O를 기다리거나 네트워크 연결을 기다리는 태스크가 있다면 주의해야 함
        * I/O를 기다리는 볼록 상황에서 이들 태스크가 워커 스레드에 할당된 상태를 유지하지만 아무 작업도 하지 않게 됨
        * 예를 들어 네 개의 하드웨어 스레드와 5개의 스레드를 갖는 스레드 풀에 20개의 태스크를 제출했다고 가정
          * 모든 태스크가 병렬로 실행되면서 20개의 태스크를 실행할 것이라고 예상할 수 있음
          * 하지만 처음 제출한 세 스레드가 잠을 저거나 I/O를 기다린다고 가정
          * 그러면 나머지 15개의 태스크를 두 스레드가 실행해야 하므로 작업 효율성이 예상보다 절반으로 떨어짐 (병렬 실행 성능 저하)
          * 처음 제출한 태스크가 기존 실행 중인 태스크가 나중의 태스크 제출을 기다리는 상황(Future의 일반적인 패턴)이라면 데드락에 걸릴 수 있음
          * 핵심은 블록(자거나 이벤트를 기다리는)할 수 있는 태스크는 스레드 풀에 제출하지 말아야 한다는 것
            * 물론 항상 이를 지킬 수 있는 것은 아님
    * 중요한 코드를 실행하는 스레드가 죽는 일이 발생하지 않도록 보통 자바 프로그램은 main이 반환하기 전에 모든 스레드의 작업이 끝나길 기다림
      * 따라서 프로그램을 종료하기 전에 모든 스레드 풀을 종료하는 습관을 갖는 것이 중요함
        * 풀의 워커 스레드가 만들어진 다음 다른 태스크 제출을 기다리면서 종료되지 않은 상태일 수 있으므로
      * 보통 장기간 실행하는 인터넷 서비스를 관리하도록 오래 실행되는 ExecutorService를 갖는 것은 흔한 경우
      * 자바는 이런 상황을 다룰 수 있도록 Thread.setDaemon 메서드를 제공

* 스레드의 다른 추상화 : 중첩되지 않은 메서드 호출
  * 7장(병렬 스트림 처리와 포크/조인 프레임워크)에서 설명한 동시성과 지금 설명하는 동시성이 어떻게 다른지 명확히 알 수 있도록 
    * 7장에서 사용한 동시성에서는 한 개의 특별한 속성, 즉 태스크나 스레드가 메서드 호출 안에서 시작되면  
      그 메서드 호출은 반환하지 않고 작업이 끝나기를 기다리게 했음
    * 다시 말해 스레드 생성과 join()이 한 쌍처럼 중첩된 메서드 호출 내에 추가되었음
    * 이를 엄격한(strict) 포크/조인이라고 부름
  * 시작된 태스크를 내부 호출이 아니라 외부 호출에서 종료하도록 기다리는 좀 더 여유로운 방식의 포크/조인을 사용해도 비교적 안전함
    * 제공된 인터페이스를 사용자는 일반 호출로 간주할 수 있음
  * 사용자의 메서드 호출에 의해 스레드가 생성되고 메서드를 벗어나 계속 실행되는 동시성 형태에 초점을 둠
    * 이런 종류, 특히 메서드 호출자에 기능을 제공하도록 메서드가 반환된 후에도 만들어진 태스크 실행이 계속되는 메서드를 비동기 메서드라 함
    * 이들 메서드를 사용할 때 따르는 위험성
      * 스레드 실행은 메서드를 호출한 다음의 코드와 동시에 실행되므로 데이터 경쟁 문제를 일으키지 않도록 주의해야 함
      * 기존 실행 중이던 스레드가 종료되지 않은 상황에서 자바의 main() 메서드가 반환하는 두 가지 방법(어떤 경우도 안전하지 못함)
        * 앱을 종료하지 못하고 모든 스레드가 실행을 끝낼 때까지 대기
          * 잊고서 종료를 못한 스레드에 의해 앱이 크래시될 수 있음
          * 또 다른 문제로 디스크에 쓰기 I/O 작업을 시도하는 일련의 작업을 중단한 경우 이로 인해 외부 데이터의 일관성이 파괴될 수 있음
        * 앱 종료를 방해하는 스레드를 강제종료(kill) 시키고 앱을 종료
    * 위 문제들을 피하려면 앱에서 만든 모든 스레드를 추적하고 앱을 종료하기 전에 스레드 풀을 포함한 모든 스레드를 종료하는 것이 좋음
  * 자바 스레드는 setDaemon() 메서드를 이용해 데몬(daemon) 또는 비데몬으로 구분할 수 있음
    * 데몬 스레드는 앱이 종료될 때 강제 종료되므로 디스크의 데이터 일관성을 파괴하지 않는 동작을 수행할 때 유용하게 사용할 수 있음
    * main() 메서드는 모든 비데몬 스레드가 종료될 때까지 프로그램을 종료하지 않고 기다림
    * 유닉스는 들어오는 ssh 연결을 기다리는 프로세스 또는 스레드를 가리키는 sshd 같은 시스템 서비스에 데몬이라는 용어를 사용하기 시작하며 이 용어가 등장

* 스레드에 무엇을 바라는가?
  * 일반적으로 모든 하드웨어 스레드를 활용해 병렬성의 장점을 극대화하도록 프로그램 구조를 만드는 것
  * 즉 프로그램을 작은 태스크 단위로 구조화하는 것이 목표
    * 하지만 태스크 변환 비용을 고려해 너무 작은 크기는 아니어야 함

### 동기 API와 비동기 API
* 자바 8 스트림을 이용해 명시적으로 병렬 하드웨어를 이용
  * 두 가지 단계로 병렬성을 이용할 수 있음
    * 첫 번째로 외부 반복(명시적 for 루프)을 내부 반복(스트림 메서드 사용)으로 바꿔야 함
    * 그리고 스트림에 parallel() 메서드를 이용하므로 자바 런타임 라이브러리가 복잡한 스레드 작업을 하지 않고 병렬로 요소가 처리되도록 할 수 있음
    * 루프가 실행될 때 추측에 의존해야 하는 프로그래머와 달리 런타임 시스템은 사용할 수 있는 스레드를 더 정확하게 알고 있다는 것이 내부 반복의 장점
* 루프 기반 계산을 제외한 다른 상황에서도 병렬성이 유용함
  * 자바 개발의 배경에는 비동기 API가 있음
  * 다음과 같은 시그니철르 갖는 f, g 두 메서드의 호출을 합하는 예제
    ```
    int f(int x);
    int g(int x);
    ```
    * 참고로 이들 메서드는 물리적 결과를 반환하므로 동기 API라고 부름
    * 두 메서드를 호출하고 합계를 출력하는 코드
      ```
      int y = f(x);
      int z = g(x);
      System.out.println(y + z);
      ```
      * f와 g를 실행하는데 오랜 시간이 걸린다고 가정
        * 메서드는 기울기 하강 같은 수학적 최적화 작업을 수행한다고 가정할 수도 있지만 좀 더 실용적인 예제로 인터넷 질의를 수행하는 것으로 가정
        * f, g 작업을 컴파일러가 완전하게 이해하기 어려움
          * 보통 자바 컴파일러는 코드 최적화의 관련한 아무 작업도 수행하지 않을 수 있음
        * f, g가 서로 상호작용하지 않는다는 사실을 알고 있거나, 상호작용을 전혀 신경쓰지 않는다면  
          f, g를 별도의 CPU 코어로 실행함으로 f, g 중 오래 걸리는 작업의 시간으로 합계 구하는 시간을 단축할 수 있음
        * 별도의 스레드로 f, g를 실행해 이를 구현 가능
        * 의도는 좋지만 이전의 단순했던 코드가 복잡하게 변함
          ```
          class ThreadExample {
          
              public static void main(String[] args) throws InterruptedException {
                  int x = 1337;
                  Result result = new Result();
          
                  Thread t1 = new Thread(() -> {
                      result.left = f(x);
                  });
                  Thread t2 = new Thread(() -> {
                      result.right = g(x);
                  });
          
                  t1.start();
                  t2.start();
                  t1.join();
                  t2.join();
                  System.out.println(result.left + result.right);
              }
          
              private static class Result {
                  private int left;
                  private int right;
              }
          }
          ```
          * Runnable 대신 Future API 인터페이스를 이용해 코드를 더 단순화
            * 이미 ExecutorService로 스레드 풀을 설정했다고 가정한 코드
              ```
              public class ExecutorServiceExample {
              
                  public static void main(String[] args) throws ExecutionException, InterruptedException {
                      int x = 1337;
              
                      ExecutorService executorService = Executors.newFixedThreadPool(2);
              
                      Future<Integer> y = executorService.submit(() -> f(x));
                      Future<Integer> z = executorService.submit(() -> g(x));
              
                      System.out.println(y.get() + z.get());
              
                      executorService.shutdown();
                  }
              }
              ```
              * 여전히 이 코드도 명시적인 submit 메서드 호출 같은 불필요한 코드로 오염됨
              * 명시적 반복으로 병렬화를 수행하던 코드를 스트림을 이용해 내부 반복으로 바꾼것처럼 비슷한 방법으로 해결해야 함
                * 문제 해결은 비동기 API라는 기능으로 API를 바꿔서 해결할 수 있음
                  * 동기 API는 보통 결과가 나올 때까지 물리적인 반환을 지연시킴으로 블록킹 API로도 알려져 있음 (I/O 작업이 가장 흔한 예)
                  * 반면 비동기 API는 블록하지 않는 I/O를 구현함
                    * 비동기 API는 보통 결과를 기다리지 않고 I/O 작업을 시킴
                    * 예를 들어 블록하지 않는 I/O 작업을 제공하는 Netty 같은 라이브러리가 있음
                * 첫 번째 방법
                  * 자바의 Future를 이용하면 이 문제를 조금 개선 가능
                    * 자바 5에서 소개된 Future는 자바 8의 CompletableFuture로 이들을 조합할 수 있게 되면서 더욱 기능이 풍부해짐
                * 두 번째 방법
                  * 발행-구독 프로토콜에 기반한 자바 9의 java.util.concurrent.Flow 인터페이스를 이용하는 방법

* Future 형식 API
  * 대안을 이용하면 f, g 시그니처가 변경됨
    ```
    Future<Integer> f(int x);
    Future<Integer> g(int x);
    ```
    * 호출도 변경
      ```
      Future<Integer> y = f(x);
      Future<Integer> z = g(x);
      System.out.println(y.get() + z.get());
      ```
      * 메서드 f는 호출 즉시 자신의 원래 바디를 평가하는 태스크를 포함하는 Future를 반환
      * 마찬가지 메서드 g도 Future를 반환
      * get() 메서드를 이용해 두 Future가 완료되어 결과가 합쳐지기를 기다림
  * 예제에서는 API는 그대로 유지하고 g를 그대로 호출하면서 f에만 Future를 적용할 수 있었음
  * 하지만 조금 더 큰 프로그램에서는 두 가지 이유로 이런 방식을 사용하지 않음
    * 다른 상황에서도 g에도 Future 형식이 필요할 수 있으므로 API 형식을 통일하는 것이 바람직함
    * 병렬 하드웨어로 프로그램 실행 속도를 극대화하려면 여러 작은 크기(하지만 합리적인)의 태스크로 나누는 것이 좋음

* 리액티브 형식 API
  * 두 번째 대안 핵심은 f, g의 시그니처를 바꿔서 콜백 형식의 프로그래밍을 이용하는 것
    ``` void f(int x, IntConsumer dealWithResult); ```
  * f가 값을 반환하지 않는데 어떻게 프로그램이 동작하는지 이상해 보일 수 있음
    * f에 추가 인수로 콜백(람다)을 전달해서 f의 바디에서는 return 문으로 결과를 반환하는 것이 아니라  
      결과가 준비되면 이를 람다로 호출하는 태스크를 만드는 것
    * 다시 말해 f는 바디를 실행하면서 태스크를 만든 다음 즉시 반환하므로 코드 형식이 다음처럼 바뀜
      ```
      public class CallbackStyleExample {
      
          public static void main(String[] args) {
              int x = 1337;
              Result result = new Result();
      
              f(x, (int y) -> {
                  result.left = y;
                  System.out.println(result.left + result.right);
              });
      
              g(x, (int z) -> {
                  result.right = z;
                  System.out.println(result.left + result.right);
              });
          }
      
          private static class Result {
              private int left;
              private int right;
          }
      }
      ```
    * 하지만 결과가 달라짐
      * f, g의 호출 합계를 정확하게 출력하지 않고 상황에 따라 먼저 계산된 결과를 출력함
      * 락을 사용하지 않으므로 값을 두 번 출력할 수 있을 뿐더러 때로는 +에 제공된 두 피연산자가 println이 호출되기 전에 업데이트 될 수도 있음
      * 다음 두 가지 방법으로 이를 보완할 수 있음
        * if-then-else를 이용, 적절한 락을 이용해 두 콜백이 모두 호출되었는지 확인 후 println을 호출해 원하는 기능 수행 가능
        * 리액티브 형식의 API는 보통 한 결과가 아니라 일련의 이벤트에 반응하도록 설계되어 있으므로 Future를 이용하는 것이 더 적절함
    * 리액티브 형식의 프로그래밍으로 메서드 f, g는 dealWithResult 콜백을 여러 번 호출할 수 있음
      * 원래의 f, g 함수는 오직 한 번만 return을 사용하도록 되어 있음
      * 마찬가지로 Future도 한 번만 완료되며 그 결과는 get()으로 얻을 수 있음
      * 리액티브 형식의 비동기 API는 자연스럽게 일련의 값(나중에 스트림으로 연결)을 처리하는데 적합
      * Future 형식의 API는 일회성의 값을 처리하는데 적합
  * 두 대안 모두 코드를 복잡하게 만듦
    * 어떤 API를 사용할 것인지 미리 잘 생각할 것
    * 하지만 API는 명시적으로 스레드를 처리하는 코드에 비해 사용 코드를 더 단순하게 만들어주며 높은 수준의 구조를 유지할 수 있음
    * 계산이 오래 걸리는 메서드(a) 또는 네트워크나 사람의 입력을 기다리는 메서드(b)에 API를 잘 활용하면 앱의 효율성이 크게 향상됨
    * b의 상황에서는 리소스를 낭비하지 않고 효율적으로 하단의 시스템을 활용할 수 있다는 장점을 추가로 제공

* 잠자기(그리고 기타 블로킹 동작)는 해로운 것으로 간주
  * 사람과 상호작용하거나 어떤 일이 일정 속도로 제한되어 일어나는 상황의 앱을 만들 때 자연스럽게 sleep() 메서드를 사용 가능
    * 하지만 스레드는 잠들어도 여전히 시스템 자원을 점유함
    * 스레드를 단지 몇 개만 사용하는 상황에서는 큰 문제는 아님
    * 하지만 스레드가 많아지고 그 중 대부분이 잠든다면 문제가 심각해짐
  * 스레드 풀에서 잠을 자는 태스크는 다른 태스크가 시작되지 못하게 막음으므로 자원을 소비함
    * 운영체제가 이들 태스크를 관리하므로 일단 스레드로 할당된 태스크는 중지 시키지 못함
  * 물론 스레드 풀에서 잠자는 스레드만 실행을 막는 것이 아님
    * 모든 블록 동작도 마찬가지
      * 블록 동작은 다른 태스크가 어떤 동작을 완료하기를 기다리는 동작(예를 들어 Future에 get() 호출)과  
        외부 상호작용(예를 들어 네트워크, DB 서버에서 읽기 작업을 기다리거나 키보드 입력 같은 사람의 상호작용)을 기다리는 동작 두 가지로 구분 가능
  * 이상적으로는 절대 태스크에서 기다리는 일을 만들지 말거나 코드에서 예외를 일으키는 방법으로 이를 처리할 수 이음
    * 태스크를 앞과 뒤 부분으로 블록되지 않을 때만 뒷부분을 자바가 스케줄링 하도록 요청 가능
    * 한 개의 작업을 갖는 코드 A
      ```
      work1();
      Thread.sleep(10000); // 10초 동안 잠듦
      work2();
      ```
    * 코드 B
      ```
      public class ScheduledExecutorServiceExample {
      
          public static void main(String[] args) {
              ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
             
              work1();
             
              // work1() 끝난 후 10초 뒤에 work2()를 개별 태스크로 스케줄함
              scheduledExecutorService.schedule(ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECONDS);
             
              scheduledExecutorService.shutdown();
          }
          
          public static void work1() {
              System.out.println("Hello from Work1");
          }
          
          public static void work2() {
              System.out.println("Hello from Work2");
          }
      }
      ```
  * 두 태스크 모두 스레드 풀에서 실행된다고 가정
    * 코드 A
      * 먼저 코드는 스레드 풀 큐에 추가되며 나중에 차례가 되면 실행됨
      * 하지만 코드가 실행되면 워커 스레드를 점유한 상태에서 아무것도 하지 않고 10초를 잠
      * 그리고 꺠어나서 work2()를 실행한 다음 작업을 종료하고 워커 스레드를 해제함
    * 코드 B
      * work1()을 실행하고 종료
      * 하지만 work2()가 10초 뒤에 실행될 수 있도록 큐에 추가
    * 코드 A나 B 모두 같은 동작 수행
      * 다른 점은 A가 자는 동안 귀중한 스레드 자원을 점유하는 반면 B는 다른 작업이 실행될 수 있도록 허용
        * 스레드를 사용할 필요가 없이 메모리만 조금 더 사용함
    * 태스크를 만들 때는 이러한 특징을 잘 활용해야 함
      * 태스크가 실행되면 귀중한 자원을 점유하므로 태스크가 끝나서 자원을 해제하기 전까지 태스크를 계속 실행해야 함
      * 태스크를 블록하는 것보다는 다음 작업을 태스크로 제출하고 현재 태스크는 종료하는 것이 바람직함
    * 가능하다면 I/O 작업에도 이 원칙을 적용하는 것이 좋음
      * 고전적으로 읽기 작업을 기다리는 것이 아니라 블록하지 않은 '읽기 시작' 메서드를 호출하고 읽기 작업이 끝나면  
        이를 처리할 다음 태스크를 런타임 라이브러리에 스케줄 하도록 요청하고 종료
    * 이런 디자인 패턴을 따르려면 읽기 어려운 코드가 많아지는 것처럼 보임
      * 하지만 CompletableFuture 인터페이스는 Future에 get()을 이용해 명시적으로 블록하지 않고 콤비네이터를 사용함으로  
        이런 형식의 코드를 런타임 라이브러리 내에 추상화 함
    * 마지막으로 스레드의 제한이 없고 저렴하다면 코드 A와 B는 사실상 같음
      * 하지만 스레드에는 제한이 있고, 저렴하지 않으므로 잠을 자거나 블록해야 하는 여러 태스크가 있을 때 가능하면 코드 B 형식을 따르는 것이 좋음

* 현실성 확인
  * 새로운 시스템을 설계할 때 시스템을 작고 동시 실행되는 많은 태스크로 설계해서 블록할 수 있는 모든 동작을 비동기 호출로 구현한다면  
    병렬 하드웨어를 최대한 활용 가능함
  * 하지만 현실적으로는 '모든 것은 비동기'라는 설계 원칙을 어겨야 함
  * 자바는 1.4에서부터 비블록 IO 기능(java.nio)을 제공했지만 이들은 조금 복잡하고 잘 알려지지 않음
  * 실제로 자바의 개선된 동시성 API를 이용해 유익성을 얻을 수 있는 상황을 찾아보고  
    모든 API를 비동기로 만드는 것을 따지지 말고 개선된 동시성 API를 사용해보길 권장
  * 네트워크 서버의 블록/비블록 API를 일관적으로 제공하는 Netty 같은 새로운 라이브러리를 사용하는 것도 도움이 됨

* 비동기 API에서 예외는 어떻게 처리하는가?
  * Future나 리액티브 형식의 비동기 API에서 호출된 메서드의 실제 바디는 별도의 스레드에서 호출되며  
    이때 발생하는 어떤 에러는 이미 호출자의 실행 범위와는 관계가 없는 상황이 됨
    * 예상치 못한 일이 일어나면 예외를 발생시켜 다른 동작이 실행되어야 함
  * Future를 구현한 CompletableFuture에서는 런타임 get() 메서드에 예외를 처리할 수 있는 기능을 제공하며  
    예외에서 회복할 수 있도록 exceptionally() 같은 메서드도 제공함
  * 리액티브 형식의 비동기 API에서는 return 대신 기존 콜백이 호출되므로 예외가 발생했을 때 실행될 추가 콜백을 만들어 인터페이스를 바꿔야 함
    * 리액티브 API에 여러 콜백을 포함하는 예제
      ```
      void f(int x, Consumer<Integer> dealWithResult, Consumer<Throwable> dealWithException);
      ```
      * f의 바디는 다음을 수행
        ``` dealWithException(e); ```
    * 콜백이 여러 개면 따로 제공하는 것 보다는 한 개체로 이 메서드를 감싸는 것이 좋음
      * 예를 들어 자바 9 플로 API에서는 여러 콜백을 한 객체(네 개의 콜백을 각각 대표하는 네 메서드를 포함하는 Subscriber\<T> 클래스)로 감쌈
        * 예제
          ```
          // 각각의 콜백이 호출됨
          void onComplete() // 값을 다 소진했거나 에러가 발생해서 더 이상 처리할 데이터가 없을 때
          void onError(Throwable throwable) // 도중에 에러가 발생했을 때
          void onNext(T item) // 값이 있을 때
          ```
          * 이전의 f에 이를 적용하면 다음과 같은 시그니처가 됨
            ``` void f(int x, Subscriber<Integer> s); ```
            * f의 바디는 다음처럼 Throwable을 가리키는 t로 예외가 일어났음을 가리킴
              ``` s.onError(t); ```
    * 여러 콜백을 포함하는 API를 파일이나 키보드 장치에서 숫자를 읽는 작업과 비교
      * 이들 장치가 수동적인 데이터 구조체가 아니라  
        "여기 번호가 나왔어요"나 "숫자가 아니라 잘못된 형식의 아이템이 나왔어요"같은 일련의 데이터를 만들어낸 다음  
        마지막으로 "더 이상 처리할 데이터가 없어요(파일의 끝)" 알림을 만듦
      * 보통 이런 종류의 호출을 메시지 또는 이벤트라 부름
        * 예를 들어 파일 리더가 3, 7, 42를 읽은 다음 잘못된 형식의 숫자 이벤트를 내보내고 이어서 2, 파일의 끝 이벤트를 차례로 생성했다고 가정
        * 이런 이벤트를 API의 일부로 보자면 API는 이벤트의 순서(채널 프로토콜 - channel protocol)에는 전혀 개의치 않음
        * 실제 부속 문서에서는 "onComplete 이벤트 다음에는 아무 이벤트도 일어나지 않음" 같은 구문을 사용해 프로토콜을 정의함

### 박스와 채널 모델
* 동시성 모델을 가장 잘 설계하고 개념화하려면 그림이 필요함
  * 이 기법을 박스와 채널 모델(box-and-channel model)이라고 부름
  * 이전 예제인 f(x) + g(x)의 계산을 일반화해서 정수와 관련된 상황이 있다고 가정
    * f나 g를 호출하거나 p 함수에 인수 x를 이용해 호출하고 그 결과를 q1과 q2에 전달하며 다시 이 두 호출의 결과로 함수 r을 호출한 다음 결과를 출력함
    * 편의상 클래스 c의 메서드와 연상 함수 C::m을 구분하지 않음
    * 첫 번째 구현 방법
      ```
      int t = p(x);
      System.out.println( r(q1(t), q2(t)) );
      ```
      * 깔끔해 보이는 코드지만 자바가 q1, q2를 차례로 호출하는데 이는 하드웨어 병렬성 활용가 거리가 멂
    * Future를 이용해 f, g를 병렬로 평가하는 방법
      ```
      int t = p(x);
      Future<Integer> a1 = executorService.submit(() -> q1(t));
      Future<Integer> a2 = executorService.submit(() -> q2(t));
      System.out.println( r(a1.get(), a2.get()) );
      ```
      * 이 예제에서는 박스와 채널 다이어그램의 모양상 p와 r을 Future로 감싸지 않았음
      * p는 다른 어떤 작업보다 먼저 처리해야 하며 r은 모든 작업이 끝난 다음 가장 마지막으로 처리해야 함
      * 아래는 흉내낸 코드
        ```
        System.out.println( r(q1(t), q2(t)) + s(x) );
        ```
        * 우리가 원하는 작업과 거리가 있음
        * 위 코드의 병렬성을 극대화하려면 모든 다섯 함수(p, q1, q2, r, s)를 모두 Future로 감싸야 하기 때문
        * 시스템에서 많은 작업이 동시에 실행되고 있지 않다면 이 방법도 잘 동작할 수 있음
          * 하지만 시스템이 커지고 각각의 많은 박스와 채널 다이어그램이 등장하고 각각의 박스는 내부적으로 자신만의 박스와 채널을 사용한다면 문제가 달라짐
          * 태스크가 get() 메서드를 호출해 Future가 끝나기를 기다리는 상태에 놓일 수 있음
          * 결과적으로 하드웨어의 병렬성을 제대로 활용하지 못하거나 심지어 데드락에 걸릴 수 있음
          * 또한 이런 대규모 시스템 구조가 얼마나 많은 수의 get()을 감당할 수 있는지 이해하기 어려움
          * 자바 8에서는 CompletableFuture와 콤비네이터(combinators)를 이용해 문제를 해결함
          * 두 Function이 있을 때 compose(), andThen() 등을 이용해 다른 Function을 얻을 수 있음
            * add1은 정수 1을 더하고 dble은 정수를 두 배로 만든다고 가정
              * 인수를 두 배로 만들고 결과에 2를 더하는 Function 구현
                ```
                Function<Integer, Integer> myfun = add1.andThen(dble);
                ```
            * 하지만 박스와 채널 다이어그램은 콤비네이터로도 직접 코딩 가능
              * 자바 Function p, q1, q2, BiFunction r로 간단하게 구현
                ```
                p.thenBoth(q1, q2).thenCombine(r);
                ```
                * 안타깝게도 thenBoth나 thenCombine은 자바 Function과 BiFunction의 일부가 아님
* 마지막으로 박스와 채널 모델을 이용해 생각과 코드를 구조화할 수 있다는 사실 강조
  * 박스와 채널 모델로 대규모 시스템 구현의 추상화 수준을 높일 수 있음
  * 박스(또는 프로그램의 콤비네이터)로 원하는 연산을 표현(계산은 나중에 이루어짐)하면 계산을 손으로 코딩한 결과보다 효율적일 것
  * 콤비네이터는 수학적 함수뿐 아니라 Future와 리액티브 스트림 데이터에도 적용할 수 있음
  * 박스와 채널 모델은 병렬성을 직접 프로그래밍하는 관점을 콤비네이터를 이용해 내부적으로 작업을 처리하는 관점으로 바꿔줌
  * 마찬가지로 자바 8 스트림은 자료 구조를 반복해야 하는 코드를 내부적으로 작업을 처리하는 스트림 콤비네이터로 바꿔줌

### CompletableFuture와 콤비네이터를 이용한 동시성
* 동시 코딩 작업을 Future 인터페이스로 생각하도록 유도한다는 점이 Future 인터페이스의 문제
  * 하지만 역사적으로 주어진 연산으로 Future를 만들고, 이를 실행하고, 종료되길 기다리는 등 Future는 FutureTask 구현을 뛰어 넘는 몇 가지 동작을 제공함
  * 이후 버전의 자바에서는 RecursiveTask 같은 더 구조화된 지원을 제공함
* 자바 8에서는 Future 인터페이스의 구현인 CompletableFuture를 이용해 Future를 조합할 수 있는 기능을 추가
  * ComposableFuture가 아닌 CompletableFuture인 이유
    * 일반적으로 Future는 실행해서 get()으로 결과를 얻을 수 있는 Callable로 만들어 짐
    * 하지만 CompletableFuture는 실행할 코드 없이 Future를 만들 수 있도록 허용하며  
      complete() 메서드를 이용해 나중에 어떤 값을 이용해 다른 스레드가 이를 완료할 수 있고 get()으로 값을 얻을 수 있도록 허용함
      * 그래서 CompletableFuture라 부름
    * f(x)와 g(x)를 동시에 실행해 합계를 구하는 코드 구현
      ```
      public class CFComplete {
      
          public static void main(String[] args) throws ExecutionException, InterruptedException {
              ExecutorService executorService = Executors.newFixedThreadPool(10);
              int x = 1337;
              
              CompletableFuture<Integer> a = new CompletableFuture<>();
              executorService.submit(() -> a.complete(f(x)));
              
              int b = g(x);
              System.out.println(a.get() + b);
              
              executorService.shutdown();
          }
      }
      
      // 또는
      public class CFComplete {
      
          public static void main(String[] args) throws ExecutionException, InterruptedException {
              ExecutorService executorService = Executors.newFixedThreadPool(10);
              int x = 1337;
              
              CompletableFuture<Integer> b = new CompletableFuture<>();
              executorService.submit(() -> b.complete(g(x)));
              
              int a = f(x);
              System.out.println(a + b.get());
              
              executorService.shutdown();
          }
      }
      ```
      * 위 두 코드는 f(x)의 실행이 끝나지 않거나 아니면 g(x)의 실행이 끝나지 않는 상황에서 get()을 기다려야 하므로 프로세싱 자원을 낭비할 수 있음
        * 자바 8의 CompletableFuture를 이용하면 이 상황을 해결할 수 있음
        * 스트림에 연산을 조합하는 것도 하나의 조합 예
          ``` myStream.map().filter().sum(); ```
        * 또 다른 예
          * compose(), andThen() 같은 메서드를 두 Function에 이용해 다른 Function을 얻을 수 있음
      * CompletableFuture\<T>에 thenCombine 메서드를 사용함으로 두 연산 결과를 더 효과적으로 사용 가능
        * thenCombine 메서드는 다음과 같은 시그니처를 갖고 있음
          * 제네릭, 와일드카드와 관련된 문제를 피할 수 있게 간소화됨
          ```
          CompletableFuture<V> thenCombine(CompletableFuture<U> other, BiFunction<T, U, V> fn)
          ```
          * 이 메서드는 두 개의 CompletableFuture 값(T, U 결과 형식)을 받아 한 개의 새 값을 생성
            * 처음 두 작업이 끝나면 두 결과 모두에 fn을 적용하고 블록하지 않은 상태로 결과 Future를 반환
            * 코드 구현
              ```
              public class CFCombine {
              
                  public static void main(String[] args) throws ExecutionException, InterruptedException {
                      ExecutorService executorService = Executors.newFixedThreadPool(10);
                      int x = 1337;
              
                      CompletableFuture<Integer> a = new CompletableFuture<>();
                      CompletableFuture<Integer> b = new CompletableFuture<>();
                      CompletableFuture<Integer> c = a.thenCombine(b, (y, z) -> y + z);
              
                      executorService.submit(() -> a.complete(f(x)));
                      executorService.submit(() -> b.complete(g(x)));
              
                      System.out.println(c.get());
                      executorService.shutdown();
                  }
              }
              ```
              * thenCombine 행이 핵심
                * Future a와 Future b의 결과를 알지 못한 상태에서 thenCombine은 두 연산이 끝났을 때  
                  스레드 풀에서 실행된 연산을 만듦
                * 결과를 추가하는 세 번째 연산 c는 다른 두 작업이 끝날 때까지는 스레드에서 실행되지 않음
                  * 먼저 시작해서 블록되지 않는 점이 특징
                * 따라서 기존의 두 가지 버전의 코드에서 발생했던 블록 문제가 어디서도 일어나지 않음
                * Future의 연산이 두 번째 종료되는 상황에서 실제 필요한 스레드는 한 개지만 스레드 풀의 두 스레드가 여전히 활성 상태
                * 이전의 두 버전에서 y+z 연산은 f(x) 또는 g(x)를 실행(블록될 가능성이 있는)한 같은 스레드에서 수행
                * 반면 thenCombine을 이용하면 f(x)와 g(x)가 끝난 다음에야 덧셈 계산이 실행됨
                * 상황에 따라서는 get()을 기다리는 스레드가 큰 문제가 되지 않으므로 기존 자바 8의 Future를 이용한 방식도 해결 방법이 될 수 있음
                  * 하지만 어떤 상황에서는 많은 수의 Future를 사용해야 함 (예를 들어 서비스에 여러 질의를 처리하는 상황)
                  * 이런 상황에서는 CompletableFuture와 콤비네이터를 이용, get()에서 블록하지 않을 수 있음
                  * 그렇게 함으로 병렬 실행의 효율성은 높이고 데드락은 피하는 최상의 해결책을 구현할 수 있음

### 발행-구독 그리고 리액티브 프로그래밍
* Future와 CompletableFuture은 독립적 실행과 병렬성이라는 정식적 모델에 기반
  * 연산이 끝나면 get()으로 Future의 결과를 얻을 수 있음
  * 따라서 Future는 한 번만 실행해 결과를 제공함
* 반면 리액티브 프로그래밍은 시간이 흐르면서 여러 Future 같은 객체를 통해 여러 결과를 제공함
  * 온도계 겍채 예시
    * 이 객체는 매 초마다 온도 값을 반복적으로 제공
  * 웹 서버 컴포넌트 응답을 기다리는 리스너 객체 예시
    * 이 객체는 네트워크에 HTTP 요청이 발생하길 기다렸다가 이후에 결과 데이터를 생성
  * 그리고 다른 코드에서 온도 값 또는 네트워크 결과를 처리함
    * 온도계와 리스너 객체는 다음 결과를 처리할 수 있도록 온도 결과나 다른 네트워크 요청을 기다림
  * 눈여겨봐야 할 두 가지 사실
    * 위 두 예제에서 Future와 같은 동작이 모두 사용되었지만 한 예제에서는 한 번의 결과가 아니라 여러 번의 결과가 필요함
    * 두 번째 예제에서는 모든 결과가 똑같이 중요한 반면 온도계 예제에서는 대부분의 사람에게 가장 최근 온도만 중요함
  * 이런 종류의 프로그래밍을 리액티브라 부르는 이유
    * 낮은 온도를 감지했을 때 이에 반응(예를 들어 히터를 킴)하는 부분이 존재하기 때문
* 스트림을 떠올림
  * 프로그램이 스트림 모델에 잘 맞는 상황이라면 가장 좋은 구현이 될 수 있음
  * 하지만 보통 리액티브 프로그래밍 패러다임은 비싼 편
  * 주어진 자바 스트림은 한 번의 단말 동작으로 소비될 수 있음
  * 스트림 패러다임은 두 개의 파이프라인으로 값을 분리하기 어려우며(포크처럼), 두 개로 분리된 스트림에서 다시 결과를 합치기도(조인처럼) 어려움
  * 스트림은 선형적인 파이프라인 처리 기법에 알맞음
  * 자바 9에서는 java.util.concurrent.Flow의 인터페이스에 발행-구독 모델을 적용해 리액티브 프로그래밍을 제공함
    * 발행-구독 모델은 줄여서 pub-sub 프로토콜이라 불리기도 함
    * 플로 API를 간단히 세 가지로 정리
      * 구독자가 구독할 수 있는 발행자
      * 이 연결을 구독(subscription)이라 함
      * 이 연결을 이용해 메시지(또는 이벤트로 알려짐)를 전송함
    * 여러 컴포넌트가 한 구독자로 구독할 수 있고 한 컴포넌트는 여러 개별 스트림을 발행할 수 있으며 한 컴포넌트는 여러 구독자에 가입할 수 있음

* 두 플로를 합치는 예제
  * 두 정보 소스로부터 발생하는 이벤트를 합쳐서 다른 구독자가 볼 수 있도록 발행하는 예를 통해 발행-구독 특징을 간단하게 확인
    * 사실 이 기능은 수식을 포함하는 스프레드시트의 셀에서 흔히 제공하는 동작
    * "=C1+C2"라는 공식을 포함하는 스프레드시트 셀 C3을 만듦
      * C1이나 C2의 값이 갱신되면 C3에도 새로운 값이 반영됨
        * 사람에 의해서 또는 각 셀이 포함하는 또 다른 공식에 의해서
      * 다음 코드는 셀의 값을 더할 수만 있다고 가정
        ```
        public class SimpleCell {
        	private int value = 0;
        	private String name;
        
        	public SimpleCell(String name) {
        		this.name = name;
        	}
        }
        ```
        * 아직은 코드가 단순한 편, 다음처럼 몇 개의 셀을 초기화 할 수 있음
          ```
          SimpleCell c2 = new SimpleCell("C2");
          SimpleCell c1 = new SimpleCell("C1");
          ```
          * c1이나 c2의 값이 변경됐을 때 c3가 두 값을 더하도록 지정하는 방법
            * c1과 c2에 이벤트가 발생했을 때 c3를 구독하도록 만들어야 함
            * 그러려면 다음과 같은 인터페이스 Publisher\<T>가 필요
              ```
              interface Publisher<T> {
                  void subscribe(Subscriber<? super T> subscriber);
              }
              ```
              * 이 인터페이스는 통신할 구독자를 인수로 받음
            * Subscriber\<T> 인터페이스는 onNext()라는 정보를 전달할 단순 메서드를 포함하며 구현자가 필요한대로 이 메서드를 구현 가능
              ```
              interface Subscriber<T> {
                  void onNext(T t);
              }
              ```
            * 두 개념 합치기
              * 사실 Cell은 Publisher(셀의 이벤트에 구독할 수 있음)이며 동시에 Subscriber(다른 셀의 이벤트에 반응함)임
                ```
                public class SimpleCell implements Publisher<Integer>, Subscriber<Integer> {
                	private int value = 0;
                	private String name;
                	private List<Subscriber> subscribers = new ArrayList<>();
                
                	public SimpleCell(String name) {
                		this.name = name;
                	}
                
                	@Override
                	public void subscribe(Subscriber<? super Integer> subscriber) {
                		subscribers.add(subscriber);
                	}
                
                	@Override
                	public void onNext(Integer newValue) {
                		// 구독한 셀에 새 값이 생겼을 때 값을 갱신해서 반응함
                		this.value = newValue;
                		// 값을 콘솔로 출력하지만 실제로는 UI의 셀을 갱신할 수 있음
                		System.out.println(this.name + ":" + this.value);
                		// 값이 갱신되었음을 모든 구독자에게 알림
                		notifyAllSubscribers();
                	}
                
                	private void notifyAllSubscribers() {
                		// 새로운 값이 있음을 모든 구독자에게 알리는 메서드
                		subscribers.forEach(subscriber -> subscriber.onNext(this.value));
                	}
                }
                ```
              * 간단한 예제 시도
                ```
                SimpleCell c3 = new SimpleCell("C3");
                SimpleCell c2 = new SimpleCell("C2");
                SimpleCell c1 = new SimpleCell("C1");
        
                c1.subscribe(c3);
        
                c1.onNext(10); // C1의 값을 10으로 갱신
                c2.onNext(20); // C2의 값을 20으로 갱신
                ```
                * C3는 직접 C1을 구독하므로 다음 결과 출력
                  ```
                  C1:10
                  C3:10
                  C2:20
                  ```
              * 'C3=C1+C2' 구현을 위해선 왼쪽과 오른쪽의 연산 결과를 저장할 수 있는 별도의 클래스 필요
                ```
                public class ArithmeticCell extends SimpleCell {
                	private int left;
                	private int right;
                
                	public ArithmeticCell(String name) {
                		super(name);
                	}
                
                	public void setLeft(int left) {
                		this.left = left;
                		// 셀 값을 갱신하고 모든 구독자에 알림
                		onNext(left + this.right);
                	}
                
                	public void setRight(int right) {
                		this.right = right;
                		// 셀 값을 갱신하고 모든 구독자에 알림
                		onNext(right + this.left);
                	}
                }
                ```
                * 조금 더 실용적인 예제 시도
                  ```
                  ArithmeticCell c3 = new ArithmeticCell("C3");
                  SimpleCell c2 = new SimpleCell("C2");
                  SimpleCell c1 = new SimpleCell("C1");
            
                  c1.subscribe(c3::setLeft);
                  c2.subscribe(c3::setRight);
            
                  c1.onNext(10); // C1의 값을 10으로 갱신
                  c2.onNext(20); // C2의 값을 20으로 갱신
                  c1.onNext(15); // C1의 값을 15으로 갱신
                  ```
                  * 결과
                    ```
                    C1:10
                    C3:10
                    C2:20
                    C3:30
                    C1:15
                    C3:35
                    ```
                    * 결과를 통해 C1의 값이 15로 갱신되었을 때 C3이 즉시 반응해 자신의 값을 갱신한다는 사실을 확인 가능
                    * 발행자-구독자 상호작용은 발행자 구독자의 그래프를 설정할 수 있음
                      * 예를 들어 "C5=C3+C4"처러 C3과 C3에 의존하는 새로운 셀 C5를 만들 수 있음
                        ```
                        ArithmeticCell c5 = new ArithmeticCell("C5");
                        ArithmeticCell c3 = new ArithmeticCell("C3");
                        
                        SimpleCell c4 = new SimpleCell("C4");
                        SimpleCell c2 = new SimpleCell("C2");
                        SimpleCell c1 = new SimpleCell("C1");
                
                        c1.subscribe(c3::setLeft);
                        c2.subscribe(c3::setRight);
                
                        c3.subscribe(c5::setLeft);
                        c4.subscribe(c5::setRight);
                        ```
                        * 스프레드시트에 다음과 같은 다양한 갱신 작업 수행
                          ```
                          c1.onNext(10); // C1의 값을 10으로 갱신
                          c2.onNext(20); // C2의 값을 20으로 갱신
                          c1.onNext(15); // C1의 값을 15로 갱신
                          c4.onNext(1); // C4의 값을 1로 갱신
                          c4.onNext(3); // C4의 값을 3으로 갱신
                          ```
                          * 결과
                            ```
                            C1:10
                            C3:10
                            C5:10
                            C2:20
                            C3:30
                            C5:30
                            C1:15
                            C3:35
                            C5:35
                            C4:1
                            C5:36
                            C4:3
                            C5:38
                            ```
                            * 최종적으로 C1은 15, C2는 20, C4는 3이므로 C5는 38의 값을 가짐
  * 데이터가 발행자(생산자)에서 구독자(소비자)로 흐름에 착안해 가발자는 이를 업스트림(upstream), 다운스트림(downstream)이라 부름
    * 위 예제에서 데이터 newValue는 업스트림 onNext() 메서드로 전달되고
    * notifyAllSubscribers() 호출을 통해 다운스트림 onNext() 호출로 전달됨
  * 실생활에서 플로를 사용하려면 onNext 이벤트 외에 onError나 onComplete 같은 메서드를 통해 데이터 흐름에서 예외가 발생하거나  
    데이터 흐름이 종료되었음을 알 수 있어야 함
    * 예를 들어 온도계 샘플이 교체되어서 onNext로 더 이상 데이터가 발생하지 않는 상황
  * 자바 9 플로 API의 Subscriber에서는 실제 onError와 onComplete를 지원함
    * 기존의 옵저버 패턴에 비해 새로운 API 프로토콜이 더 강력해진 이유가 이런 메서드 덕분
  * 간단하지만 플로 인터페이스의 개념을 복잡하게 만든 두 가지 기능은 압력과 역압력
    * 스레드 활용에서 이들 기능은 필수
    * 기존의 온도계 예제에서 온도계가 매 초마다 온도를 보고했는데 기능이 업그레이드 되면서 매 밀리초마다 온도계를 보고한다고 가정
      * 이렇게 빠른 속도로 발생하는 이벤트를 문제 없이 처리할 수 있는가?
    * 모든 SMS 메시지를 폰으로 제공하는 발행자에 가입하는 상황
      * 처음에 약간의 SMS 메시지가 있는 새 폰에서는 가입이 잘 동작할 수 있지만 몇 년 후에는 초마다 수천 개의 메시지가 onNext로 전달된다면?
    * 이런 상황을 압력(pressure)이라 부름
  * 공에 담긴 메시지를 포함하는 수직 파이프 상황 가정
    * 이런 상황에서는 출구로 추가될 공의 숫자를 제한하는 역압력 같은 기법이 필요함
    * 자바 9 플로 API에서는 발행자가 무한의 속도로 아이템을 방출하는 대신 요청했을 때만 다음 아이템을 보내도록 하는 request() 메서드를 제공
      * request 메서드는 Subscription이라는 새 인터페이스에 포함
      * 밀어내기(push)모델이 아니라 당김(pull)모델

* 역압력
  * Subscriber 객체는 Publisher에서 Subscriber로 정보를 전달함
  * 정보의 흐름 속도를 역압력(흐름 제어)으로 제어, 즉 Subscriber에서 Publisher로 정보를 요청해야 할 필요가 있을 수 있음
  * Publisher는 Subscriber를 갖고 있으므로 역압력 요청이 한 연결에만 영향을 미쳐야 한다는 것이 문제가 될 수 있음
     * 자바 9 플로 API는 Subscriber 인터페이스는 네 번째 메서드를 포함함
       ``` void onSubscribe(Subscription subscription); ```
     * Publisher와 Subscriber 사이에 채널이 연결되면 첫 이벤트로 이 메서드가 호출됨
     * Subscription 객체는 다음처럼 Subscriber, Publisher와 통신할 수 있는 메서드를 포함함
       ```
       intereface Susbscription {
           void cancel();
           void request(long n);
       }
       ```
     * 콜백을 통한 역방향 소통 효과
       * Publisher는 Subscription 객체를 만들어 Subscriber로 전달하면 Subscriber는 이를 이용해 Publisher로 정보를 보낼 수 있음

* 실제 역압력의 간단한 형태
  * 한 번에 한 개의 이벤트를 처리하도록 발행-구독 연결을 구성하려면 다음과 같은 작업이 필요함
    * Subscriber가 OnSubscribe로 전달된 Susbscription 객체를 subscription 같은 필드에 로컬로 저장함
    * Subscriber가 수많은 이벤트를 받지 않도록  
      OnSubscribe, onNext, onError의 마지막 동작에 channel.request(1)을 추가, 오직 한 이벤트만 요청함
    * 요청을 보낸 채널에만 onNext, onError 이벤트를 보내도록 Publisher의 notifyAllSubscribers 코드를 바꿈
      * 보통 여러 Subscriber가 자신만의 속도를 유지할 수 있도록 Publisher는 새 Subscription을 만들어 각 Subscriber와 연결
  * 구현이 간단해 보일 수 있지만 역압력을 구현하려면 여러가지 장단점을 생각해야 함
    * 여러 Subscriber가 있을 때 이벤트를 가장 느린 속도로 보낼 것인가?
      * 또는 Subscriber에게 보내지 않은 데이터를 저장할 별도의 큐를 가질것인가?
    * 큐가 너무 커지면 어떻게 해야 하는가?
    * Subscriber가 준비가 안되었다면 큐의 데이터를 폐기할 것인가?
    * 위 질문들의 답변은 데이터의 성격에 따라 달라짐
      * 한 온도 데이터를 잃어버리는 것은 그리 대수로운 일이 아니지만 은행 계좌 크레딧이 사라지는 것은 큰 일
  * 당김 기반 리액티브 역압력이라는 개념
    * 이 기법에서는 Subscriber가 Publisher로부터 요청을 당긴다(pull)는 의미에서 리액티브 당김 기반(reactive pull-request)이라 불림
    * 결과적으로 이런 방식으로 역압력 구현 가능

### 리액티브 시스템 vs 리액티브 프로그래밍
* 리액티브 시스템과 리액티브 프로그래밍은 상당히 다른 의미를 가지고 있음

* 리액티브 시스템
  * 런타임 환경이 변화에 대응하도록 전체 아키텍처가 설계된 프로그램
  * 리액티브 시스템이 가져야 할 속성
    * 반응성(responsive)
      * 큰 작업을 처리하느라 간단한 질의의 응답을 지연하지 않고 실시간으로 입력에 반응하는 것을 의미
    * 회복성(resilient)
      * 한 컴포넌트의 실패로 전체 시스템이 실패하지 않음을 의미
      * 네트워크가 고장났어도 이와 관계가 없는 질의에는 아무 영향이 없어야 함
        * 반응이 없는 컴포넌트를 향한 질의가 있다면 다른 대안 컴포넌트를 찾아야 함
    * 탄력성(elastic)
      * 시스템이 자신의 작업 부하에 맞게 적응하며 작업을 효율적으로 처리함을 의미
      * 바에서 음식과 음료를 서빙하는 직원을 동적으로 재배치 하여 두 가지 주문의 대기줄이 일정하게 유지되도록 하듯
      * 각 큐가 원활하게 처리될 수 있도록 다양한 소프트웨어 서비스와 관련된 작업자 스레드를 적절하게 재배치할 수 있음
    * 기타 자세한 것은 http://www.reactivemanifesto.org
    * 여러 가지 방법으로 이런 속성을 구현할 수 있지만
      * java.util.concurrent.Flow 관련된 자바 인터페이스에서 제공하는 리액티브 프로그래밍 형식을 이용하는 것도 주요 방법 중 하나
      * 이들 인터페이스 설계는 Reactive Manifesto의 네 번째이자 마지막 속성, 즉 메시지 주도 속성(message-driven)을 반영함
      * 메시지 주도 시스템은 박스와 채널 모델에 기반한 내부 API를 갖고 있는데  
        여기서 컴포넌트는 처리할 입력을 기다리고 결과를 다른 컴포넌트로 보내면서 시스템이 반응함

### 정리
* 자바의 동시성 지원은 계속 진화해 왔으며 앞으로도 그럴 것
  * 스레드 풀은 보통 유용하지만 블록되는 태스크가 많아지면 문제가 발생함
* 메서드를 비동기(결과를 처리하기 전에 반환)로 만들면 병렬성을 추가할 수 있으며 부수적으로 루프를 최적화 함
* 박스와 채널 모델을 이용해 비동기 시스템을 시각화할 수 있음
* 자바 8 CompletableFuture 클래스와 자바 9 플로 API 모두 박스와 채널 다이어그램으로 표현 가능
* CompletableFuture 클래스는 한 번의 비동기 연산을 표현함
  * 콤비네이터로 비동기 연산을 조합함으로 Future를 이용할 때 발생했던 기존의 블로킹 문제를 해결할 수 있음
* 플로 API는 발행-구독 프로토콜, 역압력을 이용하면 자바의 리액티브 프로그래밍의 기초를 제공함
* 리액티브 프로그래밍을 이용해 리액티브 시스템을 구현할 수 있음

### [quiz]
* 다음과 같은 상황에서 스레드를 완벽하게 활용할 수 있는 태스크를 어떻게 구현할지 생각하기
  * f(x), g(x)를 실행하는 두 개의 활성 스레드가 있는데 한 스레드는 다른 스레드가 return 문을 실행해 종료될 때까지 기다렸다가 시작
    * 결과
      * 정답은 f(x)를 실행하는 한 태스크
      * g(x)를 실행하는 두 번째 태스크
      * 합계를 계산하는 세 번째 태스크
      * 위 세개를 이용하는 것
      * 하지만 처음 두 태스크가 실행되기 전까지 세 번재 태스크는 실행할 수 없음
        * Future를 조합해 문제 해결 가능
---

## chapter 16 - CompletableFuture : 안정적 비동기 프로그래밍
* 15장에서는 사용할 수 있는 여러 다중 처리 리소스(CPU 등)를 고려해  
  프로그램이 이들 자원을 고수준(스레드를 이용한 복잡하고 유지보수하기 어려운 잘못된 구조를 회피함)으로  
  효과적으로 활용할 수 있도록 최신의 동시성 기법을 살펴봄
* 병렬 스트림과 포크/조인 기법을 이용해 컬렉션을 반복하거나 분할, 정복 알고리즘을 활용하는 프로그램에서 높은 수준의 병렬을 적용할 수 있음을 확인
* 자바 8, 자바 9에서는 CompletableFuture와 리액티브 프로그래밍 패러다임 두가지 API를 제공

### Future의 단순 활용
* 자바 5부터는 미래의 어느 시점에 결과를 얻는 모델에 활용할 수 있도록 Future 인터페이스를 제공
  * 비동기 계산을 모델링하는 데 Future 이용 가능
  * Future는 계산이 끝났을 때 결과에 접근할 수 있는 참조를 제공함
  * 시간이 걸릴 수 있는 작업을 Future 내부로 설정하면 호출자 스레드가 결과를 기다리는 동안 다른 유용한 작업을 수행할 수 있음
  * 이를 세탁소에 한 무더기의 옷을 드라이클리닝 서비스를 맡기는 동작에 비유 가능
    * 세탁소 주인은 드라이클리닝이 언제 끝날 지 적힌 영수증(Future)을 제공
    * 드라이클리닝이 진행되는 동안 우리는 원하는 일을 할 수 있음
  * Future는 저수준의 스레드에 비해 직관적으로 이해하기 쉽다는 장점
  * Future를 이용하려면 시간이 오래 걸리는 작업을 Callable 객체 내부로 감싼 다음에 ExecutorService에 제출해야 함
  * 다음 코드는 자바 8 이전 코드
    ```
    // 스레드 풀에 태스크를 제출하려면 ExecutorService를 만들어야 함
    ExecutorService executor = Executors.newCachedThreadPool();

    // Callable을 ExecutorService로 제출
    Future<Double> future = executor.submit(new Callable<Double>() {
        @Override
        public Double call() throws Exception {
            return doSomeLongComputation();
        }
    });

    // 비동기 작업을 수행하는 동안 다른 스레드에서 비동기적으로 실행
    doSomethingElse();

    try {
        // 비동기 작업의 결과를 가져옴
        // 결과가 준비되어 있지 않으면 호출 스레드가 블록됨
        // 하지만 최대 1초까지 기다림
        Double result = future.get(1, TimeUnit.SECONDS);
    } catch (ExecutionException ee) {
        // 계산 중 예외 발생
        ee.printStackTrace();
    } catch (InterruptedException ie) {
        // 현재 스레드에서 대기 중 인터럽트 발생
        ie.printStackTrace();
    } catch (TimeoutException te) {
        // Future가 완료되기 전에 타임아웃 발생
        te.printStackTrace();
    }
    ```
    * 이와 같은 유형의 프로그래밍에서는 ExecutorService에서 제공하는 스레드가 시간이 오래 걸리는 작업을 처리하는 동안  
      우리 스레드로 다른 작업을 동시에 실행할 수 있음
      * 다른 작업을 처리하다가 시간이 오래 걸리는 작업의 결과가 필요한 시점이 되었을 때 Future의 get 메서드로 결과를 가져올 수 있음
      * get 메서드를 호출했을 때 이미 계산이 완료되어 결과가 준비되었다면 즉시 결과를 반환하지만  
        결과가 준비지되 않았다면 작업이 완료될 때까지 우리 스레드를 블록 시킴
    * 오래 걸리는 작업이 영원히 끝나지 않으면 발생하는 문제?
      * 작업이 끝나지 않는 문제가 있을 수 있으므로 get 메서드를 오버로드해서 우리 스레드가 대기할 최대 타임아웃 시간을 설정하는 것이 좋음

* Future 제한
  * 첫 번째로 살펴볼 예제에서는 Future 인터페이스가 아래 메서드들을 제공함으로 보여줌
    * 비동기 계산이 끝났는지 확인할 수 있는 isDone 메서드
    * 계산이 끝나길 기다리는 메서드
    * 결과 회수 메서드 
  * 하지만 위 메서드만으로는 간결한 동시 실행 코드를 구현하기에 충분하지 않음
    * 예를 들어 여러 Future의 결과가 있을 때 이들의 의존성을 표현하기 어려움
    * '오래 걸리는 A라는 계산이 끝나면 그 결과를 다른 오래 걸리는 계산 B로 전달하시오'
    * '그리고 B의 결과가 나오면 다른 질의의 결과와 B의 결과를 조합하시오'
    * 와 같은 요구사항을 쉽게 구현할 수 있어야 하는데 Future로 이와 같은 동작을 구현하는 것은 쉽지 않음
  * 다음과 같은 선언형 기능이 있다면 유용할 것
    * [1] 두 개의 비동기 계산 결과를 하나로 합침
      * 두 가지 계산 결과는 서로 독립적일 수 있으며 또는 두 번째 결과가 첫 번째 결과에 의존하는 상황일 수 있음
    * [2] Future 집합이 실행하는 모든 태스크의 완료를 기다림
    * [3] Future 집합에서 가장 빨리 완료되는 태스크를 기다렸다가 결과를 얻음
      * 예를 들어 여러 태스크가 다양한 방식으로 같은 결과를 구하는 상황
    * [4] 프로그램적으로 Future를 완료 시킴
      * 비동기 동작에 수동으로 결과 제공
    * [5] Future 완료 동작에 반응함
      * 결과를 기다리면서 블록되지 않고 결과가 준비되었다는 알림을 받은 다음에 Future의 결과로 원하는 추가 동작을 수행할 수 있음

* CompletableFuture로 비동기 애플리케이션 만들기
  * 어떤 제품이나 서비스를 이용해야 하는 상황이라고 가정
    * 예산을 줄일 수 있도록 여러 온라인 상점 중 가장 저렴한 가격을 제시하는 상점을 찾는 앱을 완성해가는 예제 (CompletableFuture 기능 살펴 보기)
    * 해당 앱을 만들면서 다음과 같은 기술을 배울 수 있음
      * [1] 고객에게 비동기 API를 제공하는 방법을 배움
        * 온라인 상점을 운영하고 있는 독자에게 특히 유용한 기술
      * [2] 동기 API를 사용해야 할 때 코드를 비블록으로 만드는 법을 배움
        * 두 개의 비동기 동작을 파이프라인으로 만드는 방법과 두 개의 동작 결과를 하나의 비동기 계산으로 합치는 방법
        * 예를 들어 온라인 상점에서 우리가 사려는 물건에 대응하는 할인 코드를 반환한다고 가정
          * 우리는 다른 원격 할인 서비스에 접근해서 할인 코드에 해당하는 할인율을 찾아야 함
          * 그래야 원래 가격에 할인율을 적용해서 최종 결과를 계산할 수 있음
      * [3] 비동기 동작의 완료에 대응하는 방법을 배움
        * 모든 상점에서 가격 정보를 얻을 때까지 기다리는 것이 아니라 각 상점에서 가격 정보를 얻을 때마다 즉시 최저 가격을 찾는 앱을 갱신하는 방법
          * 그렇지 않으면 서버가 다운되는 등 문제가 발생했을 때 사용자에게 검은 화면만 보여주게 될 수 있음

* 동기 API와 비동기 API
  * 동기 API
    * 전통적인 동기 API에서는 메서드를 호출한 다음에 메서드가 계산을 완료할 때까지 기다렸다가 메서드가 반환되면 호출자는 반환된 값으로 계속 다른 동작을 수행함
    * 호출자와 피호출자가 각각 다른 스레드에서 실행되는 상황이었더라도 호출자는 피호출자의 동작 완료를 기다렸을 것
    * 이처럼 동기 API를 사용하는 상황을 블록 호출(blocking call)이라 함
  * 비동기 API
    * 반면 비동기 API에서는 메서드가 즉시 반환되며 끝내지 못한 나머지 작업을 호출자 스레드와 비동기적으로 실행될 수 있도록 다른 스레드에 할당함
    * 이와 같은 비동기 API를 사용하는 상황을 비블록 호출(non-blocking call)이라 함
    * 다른 스레드에 할당된 나머지 계산 결과는 콜백 메서드를 호출해서 전달하거나  
      호출자가 '계산 결과가 끝날 때까지 기다림' 메서드를 추가로 호출하면서 전달됨
    * 주로 I/O 시스템 프로그래밍에서 이와 같은 방식으로 동작을 수행함
    * 즉 계산 동작을 수행하는 동안 비동기적으로 디스크 접근을 수행함
    * 그리고 더 이상 수행할 동작이 없으면 디스크 블록이 메모리로 로딩될 때까지 기다림

### 비동기 API 구현
* 최저가격 검색 앱을 구현하기 위해 먼저 각각의 상점에서 제공해야 하는 API부터 정의
  * 다음은 제품명에 해당하는 가격을 반환하는 메서드 정의 코드
    ```
    public class Shop {
    
    	public double getPrice(String product) {
    		// TODO
    	}
    }
    ```
    * getPrice 메서드는 상점의 DB를 이용해서 가격 정보를 얻는 동시에 다른 외부 서비스에도 접근할 것
      * 예를 들어 물건 발행자나 제조사 관련 프로모션 할인 등
      * 우리는 실제 호출할 서비스까지 구현할 수 없으므로 이처럼 오래 걸리는 작업을 delay라는 메서드로 대체
        * delay는 인위적으로 1초를 지연시키는 메서드
          ```
          public static void delay() {
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
          }
          ```
      * 위에서 구현한 delay를 이용, 지연을 흉내 낸 다음에 임의의 계산값을 반환하도록 getPrice를 구현
        * 아무 계산값이나 반환하는 동작이 비정상적으로 보일 수 있음
        * 아래 코드처럼 제품명에 charAt을 적용해서 임의의 계산값을 반환함
          ```
          public double getPrice(String product) {
              return calculatePrice(product);
          }
          
          private double calculatePrice(String product) {
              delay();
              return random.nextDouble() * product.charAt(0) + product.charAt(1);
          }
          ```
          * 사용자가 이 API를 호출하면 비동기 동작이 완료될 때까지 1초동안 블록됨
          * 최저가격 검색 앱에서 위 메서드를 사용해 네트워크상의 모든 온라인 상점의 가격을 검색해야 하므로 블록 동작은 바람직하지 않음
          * 물론 뒤에서는 동기 API를 비동기적으로 소비하는 방법도 설명함
          * 예제에서는 편의상 사용자가 편리하게 이용할 수 있도록 비동기 API를 만들기로 결정했다고 가정

* 동기 메서드를 비동기 메서드로 변환
  * 동기 메서드 getPrice를 비동기 메서드로 변환하려면 다음 코드처럼 먼저 이름(getPriceAsync)과 반환값을 바꿔야 함
    ``` public Future<Double> getPriceAsync(String product) {...} ```
  * 자바 5부터 비동기 계산의 결과를 표현할 수 있는 java.util.concurrent.Future 인터페이스를 제공
    * 호출자 스레드가 블록되지 않고 다른 작업을 실행할 수 있음
    * Future는 결과값의 핸들일 뿐이며 계산이 완료되면 get 메서드로 결과를 얻을 수 있음
    * getPriceAsync 메서드는 즉시 반환되므로 호출자 스레드는 다른 작업을 수행할 수 있음
    * 자바 8의 CompletableFuture 클래스는 getPriceAsync를 쉽게 구현하는 데 도움이 되는 기능 제공
      ```
      public Future<Double> getPriceAsync(String product) {
          // 계산 결과를 포함할 CompletableFuture 생성
          CompletableFuture<Double> futurePrice = new CompletableFuture<>();
      
          new Thread(() -> {
              // 다른 스레드에서 비동기적으로 계산을 수행
              double price = calculatePrice(product);
              // 오랜 시간이 걸리는 계산이 완료되면 Future에 값을 설정
              futurePrice.complete(price);
          }).start();
      
          // 계산 결과가 완료되길 기다리지 않고 Future를 반환
          return futurePrice;
      }
      ```
      * 비동기 계산과 완료 결과를 포함하는 CompletableFuture 인스턴스 생성
        * 그리고 실제 가격을 계산할 다른 스레드를 만든 다음에 오래 걸리는 계산 결과를 기다리지 않고 결과를 포함할 Future 인스턴스를 바로 반환
        * 요청한 제품의 가격 정보가 도착하면 complete 메서드를 이용해서 CompletableFuture 종료 가능
      * 다음 코드에서 보여주는 것처럼 클라이언트는 getPriceAsync를 활용할 수 있음
        ```
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();

        // 상점에 제품 가격 정보 요청
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");

        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");

        // 제품의 가격을 계산하는 동안
        doSomethingElse();

        // 다른 상점 검색 등 다른 작업 수행
        try {
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");
        ```
        * 클라이언트는 특정 제품의 가격 정보를 상점에 요청함
          * 상점은 비동기 API를 제공하므로 즉시 Future를 반환
          * 클라이언트는 반환된 Future를 이용해 나중에 결과를 얻을 수 있음
          * 그 사이 클라이언트는 다른 상점에 가격 정보를 요청하는 등 첫 번째 상점의 결과를 기다리면서 대기하지 않고 다른 작업 처리 가능
          * 나중에 클라이언트가 특별히 할일이 없으면 Future의 get 메서드 호출
          * 이때 Future가 결과값을 가지고 있다면 Future에 포함된 값을 읽거나 아니면 값이 계산될 때까지 블록
        * 실행 결과
          ```
          Invocation returned after 7 msecs
          Doing something else...
          Price is 123.26
          Price returned after 1023 msecs
          ```
          * 가격 계산이 끝나기 전에 getPriceAsync가 반환됨

* 에러 처리 방법
  * 지금까지 개발한 코드는 아무 문제 없이 동작함
    * 하지만 가격을 계산하는 동안 에러가 발생하면?
    * 예외가 발생하면 해당 스레드에만 영향을 미침
    * 즉 에러가 발생해도 가격 계산은 계속 진행되며 일의 순서가 꼬임
    * 결과적으로 클라이언트는 get 메서드가 반환될 때까지 영원히 기다리게 될 수도 있음
    * 클라이언트 타임아웃값을 받는 get 메서드의 오버로드 버전을 만들어 문제를 해결
      * 블록 문제가 발생할 수 있는 상황에서는 타임아웃을 활용하는 것이 좋음
      * 그래야 문제가 발생했을 때 클라이언트가 영원히 블록되지 않고 타임아웃 시간이 지나면 TimeoutException을 받을 수 있음
      * 하지만 이때 제품가격 계산에 왜 에러가 발생했는지 알 수 있는 방법이 없음
      * 따라서 completeExceptionally 메서드를 이용해 CompletableFuture 내부에서 발생한 예외를 클라이언트로 전달해야 함
        ```
        public Future<Double> getPriceAsync(String product) {
            CompletableFuture<Double> futurePrice = new CompletableFuture<>();
            new Thread(() -> {
                try {
                    double price = calculatePrice(product);
                    // 계산이 정상적으로 종료되면 Future에 가격 정보를 저장한채로 Future를 종료
                    futurePrice.complete(price);
                } catch (Exception ex) {
                    // 도중에 문제가 발생하면 발생한 에러를 포함시켜 Future 종료
                    futurePrice.completeExceptionally(ex);
                }
            }).start();
            return futurePrice;
        }
        ```
        * 이제 클라이언트는 가격 계산 메서드에서 발생한 예외 파라미터를 포함하는 ExecutionException을 받게 됨
          * 예를 들어 'product not avaiable'이라는 RuntimeException 예외 발생시
            ```
            Invocation returned after 7 msecs
            Doing something else...
            Exception in thread "main" java.lang.RuntimeException: java.util.concurrent.ExecutionException: java.lang.RuntimeException: product not available
            	at com.jaenyeong.chapter_16.OnlineStore.Origin.Client.async(Client.java:32)
            	at com.jaenyeong.chapter_16.OnlineStore.Origin.Client.main(Client.java:10)
            Caused by: java.util.concurrent.ExecutionException: java.lang.RuntimeException: product not available
            	at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:395)
            	at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:1999)
            	at com.jaenyeong.chapter_16.OnlineStore.Origin.Client.async(Client.java:29)
            	... 1 more
            Caused by: java.lang.RuntimeException: product not available
            	at com.jaenyeong.chapter_16.OnlineStore.Origin.Shop.exceptionCalculatePrice(Shop.java:62)
            	at com.jaenyeong.chapter_16.OnlineStore.Origin.Shop.lambda$getPriceAsyncV2$1(Shop.java:43)
            	at java.base/java.lang.Thread.run(Thread.java:834)
            ```

* 팩토리 메서드 supplyAsync로 CompletableFuture 만들기
  * 지금까지는 CompletableFuture를 직접 만듦
  * 더 간단하게 CompletableFuture를 만드는 방법
    * 팩토리 메서드 supplyAsync로 CompletableFuture 만들기
    ```
    public Future<Double> getPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }
    ```
    * supplyAsync 메서드는 Supplier를 인수로 받아 CompletableFuture를 반환
    * CompletableFuture는 Supplier를 실행해 비동기적으로 결과를 생성
    * ForkJoinPool의 Executor 중 하나가 Supplier를 실행할 것
    * 하지만 두 번째 인수를 받는 오버로드 버전의 supplyAsync 메서드를 이용해 다른 Executor를 지정할 수 있음
    * 결국 모든 다른 CompletableFuture의 팩토리 메섣의 Executor를 선택적으로 전달할 수 있음
    * supplyAsync 메서드가 반환하는 CompletableFuture는 직접 만든 CompletableFuture과 동일
      * 둘 다 같은 방법으로 에러를 관리

### 비블록 코드 만들기
* 지금부터 Shop 클래스에서 구현한 API를 제어할 권한이 우리에게 없는 상황이며 모든 API는 동기 방식의 블록 메서드라고 가정
  * 실제로 몇몇 서비스에서 제공하는 HTTP API는 이와 같은 방식으로 동작
  * 블록 메서드를 사용할 수밖에 없는 상황에서 비동기적으로 여러 상점에 질의하는 방법 찾기
    * 즉 한 요청의 응답을 기다리며 블록하는 상황을 피해 최저가격 검색 앱의 성능을 높일 수 있는 방법
* 다음과 같은 상점 리스트가 있다고 가정
  ```
  List<Shop> shops = Arrays.asList(
  			new Shop("BestPrice"),
  			new Shop("LetsSaveBig"),
  			new Shop("MyFavoriteShop"),
  			new Shop("ButItAll"));
  ```
  * 다음처럼 제품명을 입력하면 상점명과 제품 가격 문자열 정보를 포함하는 List를 반환하는 메서드를 구현
    ``` public List<String> findPrices(String product); ```
    * 스트림을 이용해 구현 (해당 코드 문제?)
      ```
      public List<String> findPrices(String product) {
          return shops.stream()
              .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
              .collect(toList());
      }
      ```
      * findPrices 메서드로 원하는 제품의 가격을 검색할 수 있음
      * 결과와 성능 확인
        ```
        long start = System.nanoTime();
        System.out.println(findPrices("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println(msg + " done in " + duration + " msecs");
        ```
        * 결과
          ```
          [BestPrice price is 123.26, LetsSaveBig price is 169.47, 
           MyFavoriteShop price is 214.13, ButItAll price is 191.05]
          sequential done in 4040 msecs
          ```
        * 네 개의 상점에서 가격을 검색하는 동안 각각 1초의 대기시간이 있으므로 전체 가격 검색 결과는 4초 이상

* 병렬 스트림으로 요청 병렬화하기
  * 순차 계산을 병렬로 처리해서 성능 개선
    ```
    public List<String> findPrices(String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }
    ```
    * 결과
      ```
      [BestPrice price is 123.26, LetsSaveBig price is 169.47, 
       MyFavoriteShop price is 214.13, ButItAll price is 191.05]
      sequential done in 1031 msecs
      ```

* CompletableFuture로 비동기 호출 구현하기
  * supplyAsync로 CompletableFuture 생성
    ```
    List<CompletableFuture<String>> priceFutures =
            shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(
                            () -> String.format("%s prices is %.2f",
                                    shop.getName(), shop.getPrice(product))))
                    .collect(toList());
    ```
    * 위 코드로 CompletableFuture를 포함하는 리스트 List<CompletableFuture\<String>>를 얻을 수 있음
    * 리스트의 CompletableFuture는 각각 계산 결과가 끝난 상점의 이름 문자열을 포함
    * 하지만 findPrices 메서드의 반환 형식은 List\<String>이므로  
      모든 CompletableFuture의 동작이 완료되고 결과를 추출한 다음 리스트를 반환해야 함
    * 두 번째 map 연산을 List<CompletableFuture\<String>>에 적용할 수 있음
      * 리스트의 모든 CompletableFuture에 join을 호출해 모든 동작이 끝나길 기다림
      * CompletableFuture 클래스의 join 메서드는 Future 인터페이스의 get 메서드와 같은 의미
      * 다만 join은 아무 예외도 발생시키지 않는다는 점이 다름
      * 따라서 두 번째 map의 람다 포현을 try/catch로 감쌀 필요가 없음
      * 재구현 코드
        ```
        public List<String> findPrices(String product) {
            List<CompletableFuture<String>> priceFutures =
                    shops.stream()
                            // CompletableFuture로 각각의 가격을 비동기적으로 계산
                            .map(shop -> CompletableFuture.supplyAsync(
                                    () -> shop.getName() + " price is " + shop.getPrice(product)))
                            .collect(toList());
    
            return priceFutures.stream()
                    // 모든 비동기 동작이 끝나길 기다림
                    .map(CompletableFuture::join)
                    .collect(toList());
        }
        ```
        * 두 map 연산을 하나의 스트림 파이프라인으로 처리하지 않고 두 개의 스트림 파이프라인으로 처리
          * 스트림 연산은 게으른 특성이 있으므로 하나의 파이프라인으로 연산을 처리했다면  
            모든 가격 정보 요청 동작이 동기적, 순차적으로 이루어지는 결과가 됨
          * CompletableFuture로 각 상점의 정보를 요청할 때 기존 요청 작업이 완료되어야  
            join이 결과를 반환하면서 다음 상점 정보를 요청할 수 있기 때문
        * 결과
          ```
          [BestPrice price is 123.25651664705744, LetsSaveBig price is 169.4653393606115, 
           MyFavoriteShop price is 214.12914480588853, ButItAll price is 191.05474145552486]
          sequential done in 2051 msecs
          ```
        * 순차적 블록 방식의 구현에 비해서는 빨라졌으나 이전 병렬 스트림을 사용한 구현보다는 두 배 가량 느림

* 더 확장성이 좋은 해결 방법
  * 병렬 스트림 버전의 코드는 정확히 네 개의 상점에 하나의 스레드를 할당해 네 개의 작업을 병렬로 수행하면서 검색 시간을 최소화 했음
  * 다섯 번째 상점 추가
    * 순차적
      ```
      [BestPrice price is 123.26, LetsSaveBig price is 169.47, 
       MyFavoriteShop price is 214.13, BuyItAll price is 184.74, ShopEasy price is 176.08]
      sequential done in 5030 msecs
      ```
      * 소요 시간이 1초 정도 늘어 5초 이상 소요
    * 병렬 스트림
      ```
      [BestPrice price is 197.15, LetsSaveBig price is 167.59, 
       MyFavoriteShop price is 192.49, BuyItAll price is 199.68, ShopEasy price is 200.77]
      parallel done in 2014 msecs
      ```
      * 네 개의 상점을 검색하느라 네 개의 모든 스레드(일반적으로 스레드 풀에서 제공하는 스레드 수는 4개)가 사용된 상황
        * 다섯 번째 상점을 처리하는 데 추가로 1초 이상 소요
        * 네 개의 스레드 중 누군가가 작업을 완료해야 다섯 번째 질의를 수행할 수 있음
    * CompletableFuture
      ```
      [BestPrice price is 171.10524235618578, LetsSaveBig price is 168.59369176671822, 
       MyFavoriteShop price is 174.79155890558252, BuyItAll price is 154.82955565763797, ShopEasy price is 163.97779997742248]
      CompletableFuture done in 2033 msecs
      ```
  * 병렬 스트림과 CompletableFuture 두 가지 버전 모두 내부적으로  
    Runtime.getRuntime().availableProcessors()가 반환하는 스레드 수를 사용하면서 비슷한 결과가 됨
  * 결과적으로 비슷하지만 CompletableFuture는 병렬 스트림 버전에 비해 작업에 이용할 수 있는 다양한 Executor를 지정할 수 있다는 장점이 있음
  * 따라서 Executor로 스레드 풀의 크기를 조절하는 등 앱에 맞는 최적화된 설정을 만들 수 있음

* 커스텀 Executor 사용하기
  * 스레드 풀 크기 조절
    * 자바 병렬 프로그래밍(브라이언 게츠 : http://jcip.net.s3-website-us-east-1.amazonaws.com/)
    * 스레드 풀이 너무 크면 CPU와 메모리 자원을 서로 경쟁하느라 시간을 낭비할 수 있음
    * 반면 스레드 풀이 너무 작으면 CPU의 일부 코어는 활용되지 않을 수 있음
    * 게츠는 다음 공식으로 대략적인 CPU 활용 비율을 계산할 수 있다고 제안
      * N^threads = N^cpu * U^cpu * (1 + W/C)
        * N^cpu는 Runtime.getRuntime().availableProcessors()가 반환하는 코어 수
        * U^cpu는 0과 1 사이의 값을 갖는 CPU 활용 비율
        * W/C는 대기시간과 계산시간의 비율
  * 우리 앱은 상점의 응답을 대략 99%의 시간만큼 기다리므로 W/C 비율을 100으로 간주할 수 있음
    * 즉 대상 CPU 활용률이 100%라면 400스레드를 갖는 풀을 만들어야 함을 의미
    * 하지만 상점 수보다 많은 스레드를 가지고 있어 봐야 사용할 가능성이 전혀 없으므로 상점 수보다 많은 스레드를 갖는 것은 낭비
    * 따라서 한 상점에 하나의 스레드가 할당될 수 있도록, 즉 가격 정보를 검색하려는 상점 수만큼 스레드를 갖도록 Executor를 설정함
    * 스레드 수가 너무 많으면 오히려 서버가 크래시될 수 있으므로 하나의 Executor에서 사용할 스레드의 최대 개수는 100이하로 설정하는 것이 바람직
    * 우리의 최저가격 검색 앱에 맞는 커스텀 Executor
      ```
      private final Executor executor =
          Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
              @Override
              public Thread newThread(Runnable r) {
                  Thread t = newThread(r);
                  // 프로그램 종료를 방해하지 않는 데몬 스레드를 사용함
                  t.setDaemon(true);
                  return t;
              }
          });
      ```
      * 만드는 풀은 데몬 스레드(daemon thread)를 포함
        * 자바에서 일반 스레드가 실행 중이면 자바 프로그램은 종료되지 않음
        * 따라서 어떤 이벤트를 한없이 기다리면서 종료되지 않는 일반 스레드가 있으면 문제가 될 수 있음
        * 반면 데몬 스레드는 자바 프로그램이 종료될 때 강제로 실행이 종료될 수 있음
        * 두 스레드의 성능은 같음
        * 새로운 Executor를 팩토리 메서드 supplyAsync의 두 번째 인수로 전달 가능
          * 예를 들어 다음 코드처럼 제품가격 정보를 얻는 CompletableFuture를 만들 수 있음
            ```
            CompletableFuture.supplyAsync(() -> shop.getName() + " price is " + shop.getPrice(product), executor);
            ```
            * CompletableFuture 버전의 코드 성능을 확인하니
              * 다섯 개의 상점을 검색할 때는 대략 1021밀리초 소요
              * 아홉 개의 상점을 검색할 때는 대략 1022밀리초 소요
              * 결국 앱의 특성에 맞는 Executor를 만들어 CompletableFuture를 활용하는 것이 바람직

* 스트림 병렬화와 CompletableFuture 병렬화
  * 컬렉션 계산을 병렬화 하는 두 가지 방법
    * [1] 병렬 스트림으로 변환해서 컬렉션을 처리하는 방법
    * [2] 컬렉션을 반복하면서 CompletableFuture 내부의 연산으로 만드는 것
  * CompletableFuture를 이용하면 전체적인 계산이 블록되지 않도록 스레드 풀의 크기를 조절할 수 있음
  * 사용할 병렬화 기법 선택 기준
    * I/O가 포함되지 않은 계산 중심의 동작을 실행할 때는 스트림 인터페이스가 가장 구현하기 효율적일 수 있음
      * 모든 스레드가 계산 작업을 수행하는 상황에서는 프로세서 코어 수 이상의 스레드를 가질 필요가 없음
    * 반면 I/O를 기다리는 작업을 병렬로 실행할 때는 CompletableFuture가 더 많은 유연성을 제공
      * 대기/계산(W/C)의 비율에 적합한 스레드 수를 설정할 수 있음
      * 특히 스트림의 게으른 특성 때문에 스트림에서 I/O를 실제로 언제 처리할지 예측하기 어려움

### 비동기 작업 파이프라인 만들기
* 우리와 계약을 맺은 모든 상점이 하나의 할인 서비스를 사용하기로 했다고 가정
  * 할인 서비스에서는 서로 다른 할인율을 제공하는 다섯 가지 코드 제공
    * Discount.code 표현
      ```
      public class Discount {
          public enum Code {
              NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMON(20);
      
              private final int percentage;
      
              Code(int percentage) {
                  this.percentage = percentage;
              }
          }
      }
      ```
    * 또한 상점에서 getPrice 메서드의 결과 형식 변경
      * 이제 getPrice는 ShopName:price:DiscountCode 형식의 문자열 반환
      * 예제에서는 미리 계산된 임의의 가격과 임의의 Discount.Code 반환
        ```
        public String getPrice(String product) {
            double price = calculatePrice(product);
            Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
    
            return String.format("%s:%.2f:%s", name, price, code);
        }
        
        private double calculatePrice(String product) {
            delay();
            return random.nextDouble() * product.charAt(0) + product.charAt(1);
        }
        ```
        * 실행 결과
          ``` BestPrice:123.26:GOLD ```

* 할인 서비스 구현
  * 할인 서버에서 할인율을 확인해서 최종 가격 계산 가능
    * 할인 코드와 연계된 할인율은 언제든 바뀔 수 있으므로 매번 서버에서 정보를 얻어 와야 함
  * 상점에서 제공한 문자열 파싱은 다음처럼 Quote 클래스로 캡슐화
    ```
    public class Quote {
    	private final String shopName;
    	private final double price;
    	private final Discount.Code discountCode;
    
    	public Quote(String shopName, double price, Discount.Code discountCode) {
    		this.shopName = shopName;
    		this.price = price;
    		this.discountCode = discountCode;
    	}
    
    	public static Quote parse(String s) {
    		String[] split = s.split(":");
    		String shopName = split[0];
    		double price = Double.parseDouble(split[1]);
    		Discount.Code discountCode = Discount.Code.valueOf(split[2]);
    
    		return new Quote(shopName, price, discountCode);
    	}
    
    	public String getShopName() {
    		return shopName;
    	}
    
    	public double getPrice() {
    		return price;
    	}
    
    	public Discount.Code getDiscountCode() {
    		return discountCode;
    	}
    }
    ```
    * 상점에서 얻은 문자열을 정적 팩토리 메서드 parse로 넘겨주면 상점명, 할인전 가격, 할인된 가격 정보를 포함하는 Quote 클래스 인스턴스가 생성됨
    * Discount 서비스에서는 Quote 객체를 인수로 받아 할인된 가격 문자열을 반환하는 applyDiscount 메서드 제공
      ```
      public class Discount {
          // Code 생략
      
          public static String applyDiscount(Quote quote) {
              return quote.getShopName() + " price is " +
                  // 기존 가격에 할인 코드를 적용
                  Discount.apply(quote.getPrice(), quote.getDiscountCode());
          }
      
          private static double apply(double price, Code code) {
              // Disocunt 서비스의 응답 지연을 흉내
              delay();
              return format(price * (100 - code.percentage) / 100);
          }
      }
      ```

* 할인 서비스 사용
  * Discount는 원격 서비스이므로 1초의 지연 추가
    * 순차적 동기 방식으로 findPrices 메서드 구현
      ```
      public List<String> findPrices(String product) {
          return shops.stream()
                  // 각 상점에서 할인 전 가격 얻기
                  .map(shop -> shop.getPrice(product))
                  // 상점에서 반환한 문자열을 Quote 객체로 변환
                  .map(Quote::parse)
                  // Discount 서비스를 이용해서 각 Quote에 할인을 적용
                  .map(Discount::applyDiscount)
                  .collect(toList());
      }
      ```
      * 세 개의 map 연산을 상점 스트림에 파이프라인으로 연결해서 원하는 결과를 얻음
        * 첫 번째 연산에서는 각 상점을 요청한 제품의 가격과 할인 코드로 변환
        * 두 번째 연산에서는 이들 문자열을 파싱해서 Quote 객체 생성
        * 세 번째 연산에서는 원격 Discount 서비스에 접근해서 최종 할인가격을 계산하고 가격에 대응하는 상점명을 포함하는 문자열 반환
      * 해당 구현은 성능 최적화와 거리가 있음
        * 벤치마크로 실제 성능을 측정하는 것이 좋음
      * 순차적으로 다섯 상점에 가격 정보를 요청하느라 5초가 소요되었고  
        다섯 상점에서 반환한 가격 정보에 할인 코드를 적용할 수 있도록 할인 서비스에 5초가 소요되었음
        * 병렬 스트림 이용시 쉽게 성능 개선 가능
        * 하지만 병렬 스트림에서는 스트림이 사용하는 스레드 풀의 크기가 고정되어 있어서  
          상점 수가 늘어났을 때처럼 검색 대상이 확장되었을 때 유연하게 대응할 수 없음
        * 따라서 CompletableFuture에서 수행하는 태스크를 설정할 수 있는 커스텀 Executor를 정의함으로써 CPU 사용을 극대화할 수 있음

* 동기 작업과 비동기 작업 조합하기
  * CompletableFuture에서 제공하는 기능으로 findPrices 메서드를 비동기적으로 구현
    ```
    public List<String> findPrices(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                        .map(shop -> CompletableFuture.supplyAsync(
                                () -> shop.getPrice(product), executor))
                        .map(future -> future.thenApply(Quote::parse))
                        .map(future -> future.thenCompose(
                                quote -> CompletableFuture.supplyAsync(
                                        () -> Discount.applyDiscount(quote), executor)))
                        .collect(toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }
    ```
    * 세 개의 map 연산 적용
      * 다만 이번에는 CompletableFuture 클래스 기능을 이용해 이들 동작을 비동기로 만들어야 함
      * [1] 가격 정보 얻기
        * 팩토리 메서드 supplyAsync에 람다 표현식을 적달해 비동기적으로 상점에서 정보를 조회
        * 첫 번째 변환의 결과는 ``` Stream<CompletableFuture<String>> ```
        * 각 CompletableFuture는 작업이 끝났을 때 해당 상점에서 반환하는 문자열 정보를 포함
        * 커스텀 Executor로 CompletableFuture를 설정
      * [2] Quote 파싱하기
        * 두 번째 변환 과정에서는 첫 번째 결과 문자열을 Quote로 변환
        * 파싱 동작에서는 원격 서비스나 I/O가 없으므로 원하는 즉시 지연 없이 동작을 수행할 수 있음
        * 따라서 첫 번째 과정에서 생성된 CompletableFuture에 thenApply 메서드를 호출한 다음에  
          문자열을 Quote 인스턴스로 변환하는 Function 전달
        * thenApply 메서드는 CompletableFuture가 끝날 때까지 블록하지 않는다는 점 주의
          * 즉 CompletableFuture가 동작을 완전히 완료한 다음에 thenApply 메서드로 전달된 람다표현식을 적용할 수 있음
          * 따라서 ``` CompletableFuture<String> ```을 ``` CompletableFuture<Quote> ```로 변환할 것
          * 이는 마치 CompletableFuture의 결과물로 무엇을 할지 지정하는 것과 같음
          * 스트림 파이프라인에도 같은 기능이 존재
      * [3] CompletableFuture를 조합해서 할인된 가격 계산하기
        * 세 번째 map 연산에서는 상점에서 받은 할인전 가격에 원격 Discount 서비스에서 제공하는 할인율을 적용해야 함
        * 이번에는 원격 실행이 포함되므로 이전 두 변환과 다르며 동기적으로 작업을 수행해야 함
          * 1초의 지연으로 원격 실행을 흉내냄 (delay() 메서드로 흉내)
        * 람다 표현식(lambda expression)으로 이 동작을 팩토리 메서드 supplyAsync에 전달 가능
        * 결국 두 가지 CompletableFuture로 이루어진 연쇄적으로 수행되는 두 개의 비동기 동작을 만들 수 있음
          * 상점에서 가격 정보를 얻어와 Quote로 변환
          * 변환된 Quote를 Discount 서비스로 전달해서 할인된 최종가격 획득
        * 자바 8의 CompletableFuture API는 이와 같이 두 비동기 연산을 파이프라인으로 만들 수 있도록 thenCompose 메서드를 제공함
          * thenCompose 메서드는 첫 번째 연산의 결과를 두 번째 연산으로 전달
          * 즉 첫 번째 CompletableFuture에 thenCompose 메서드를 호출하고  
            Function에 넘겨주는 식으로 두 CompletableFuture를 조합할 수 있음
          * Function은 첫 번째 CompletableFuture 반환 결과를 인수로 받고 두 번째 CompletableFuture를 반환하는데  
            두 번째 CompletableFuture는 첫 번째 CompletableFuture의 결과를 게산의 입력으로 사용함
          * 따라서 Future가 여러 상점에서 Quote를 얻는 동안 메인 스레드는 UI 이벤트에 반응하는 등 유용한 작업을 수행할 수 있음
        * 세 개의 map 연산 결과 스트림의 요소를 리스트로 수집하면 ``` List<CompletableFuture<String>> ``` 형식의 자료를 얻을 수 있음
        * 마지막으로 CompletableFuture가 완료되기를 기다렸다가 join으로 값을 추출할 수 있음
        * CompletableFuture 클래스의 다른 메서드처럼 thenCompose 메서드도 Async로 끝나는 버전이 존재함
          * Async로 끝나지 않는 메서드는 이전 작업을 수행한 스레드와 같은 스레드에서 작업을 실행함을 의미하며  
            Async로 끝나는 메서드는 다음 작업이 다른 스레드에서 실행되도록 스레드 풀로 작업을 제출함
          * 여기서 두 번째 CompletableFuture의 결과는 첫 번째 CompletableFuture에 의존하므로  
            두 CompletableFuture를 하나로 조합하든 Async 버전의 메서드를 사용하든 최종 결과나 개괄적인 실행시간에는 큰 영향을 미치지 않음
          * 따라서 스레드 전환 오버헤드가 적게 발생하면서 효율성이 좀 더 좋은 thenCompose를 사용

* 독립 CompletableFuture와 비독립 CompletableFuture 합치기
  * 위 예제에서는 첫 번째 CompletableFuture에 thenCompose 메서드를 실행한 다음  
    실행 결과를 첫 번째 실행결과를 입력으로 받는 두 번째 CompletableFuture로 전달함
  * 실전에서는 독립적으로 실행된 두 개의 CompletableFuture 결과를 합쳐야 하는 상황이 종종 발생함
    * 물론 첫 번째 CompletableFuture의 동작 완료와 관계없이 두 번째 CompletableFuture를 실행할 수 있어야 함
  * 이런 상황에서는 thenCombine 메서드 사용
    * thenCombine BiFunction을 두 번째 인수로 받음
    * BiFunction은 두 개의 CompletableFuture 결과를 어떻게 합칠ㅈ리 정의함
    * thenCompose와 마찬가지로 thenCombine 메서드에도 Async 버전이 존재함
    * thenCombineAsync 메서드에서는 BiFunction이 정의하는 조합 동작이 스레드 풀로 제출되면서 별도의 태스크에서 비동기적으로 수행됨
  * 예제에서는 한 온라인 상점이 유료(EUR) 가격 정보를 제공하는데, 고객에게는 항상 달러(USD) 가격을 보여줘야 함
    * 우리는 주어진 상품의 가격을 상점에 요청하는 한편 원격 환율 교환 서비스를 이용해 유로와 달러의 현재 환율을 비동기적으로 요청해야 함
    * 두 가지 데이터를 얻었으면 가격에 환율을 곱해서 결과를 합칠 수 있음
    * 이렇게 해서 두 CompletableFuture의 결과가 생성되고 BiFunction으로 합쳐진 다음에 세 번째 CompletableFuture를 얻을 수 있음
    * 코드
      ```
      Future<Double> futurePriceInUSD =
              CompletableFuture.supplyAsync(
                      // 제품가격 정보를 요청하는 첫 번째 태스크를 생성
                      () -> shop.getPrice(product))
                      .thenCombine(CompletableFuture.supplyAsync(
                              // USD, EUR의 환율 정보를 요청하는 독립적인 두 번째 태스크 생성
                              () -> exchangeService.getRate(Money.EUR, Money.USD)),
                              // 두 결과를 곱해서 가격과 환율 정보를 합침
                              (price, rate) -> price * rate);
      ```
      * 여기서 합치는 연산은 단순한 곱셈이므로 별도의 태스크에서 수행하여 자원을 낭비할 필요가 없음
        * 따라서 thenCombineAsync 대신 thenCombine 메서드 사용

* Future의 리플렉션과 CompletableFuture의 리플렉션
  * CompletableFuture는 람다 표현식 사용
    * 람다 덕분에 다양한 동기 태스크, 비동기 태스크를 활용해 복잡한 연산 수행 방법을 효과적으로 쉽게 정의할 수 있는 선언형 API를 만들 수 있음
  * 자바 7로 구현
    * 자바 7과 비교하여 실질적으로 CompletableFuture를 이용했을 때 얻을 수 있는 코드 가독성의 이점 확인
    ```
    // 태스크를 스레드 풀에 제출할 수 있도록 ExecutorService 생성ㄴ
    ExecutorService executor = Executors.newCachedThreadPool();
    
    final Future<Double> futureRate = executor.submit(new Callable<Double>() {
        @Override
        public Double call() {
            // EUR USD 환율 정보를 가져올 Future 생성
            return exchangeService.getRate(Money.EUR, Money.USD);
        }
    });

    Future<Double> futurePriceInUSD = executor.submit(new Callable<Double>() {
        @Override
        public Double call() {
            // 두 번째 Future로 상점에서 요청 제품의 가격을 검색
            double priceInEUR = shop.getPrice(product);
            // 가격을 검색한 Future를 이용해 가격과 환율을 곱함
            return priceInEUR * futureRate.get();
        }
    });
    ```
    * Executor에 EUR과 USD 간의 환율 검색 외부 서비스를 이용하는 Callable을 submit의 인수로 전달 첫 번째 Future를 만듦
      * 그리고 상점에서 해당 재품의 가격을 EUR로 반환하는 두 번째 Future를 만듦
      * 마지막으로 EUR 가격 정보에 환율을 곱함
      * 위 예제가 아닌 전 예제에서 thenCombine 대신 thenCombineAsync를 사용하는 것은  
        위 에제에서 가격과 환율을 곱하는 세 번째 Future를 만드는 것과 같음
      * 단지 두 개의 Future를 조합하는 예제이므로 얼피 보기에는 두 구현이 크게 다른 것 같지 않을 수 있음

* 타임아웃 효과적으로 사용하기
  * Future의 계산 결과를 읽을 때는 무한정 기다리는 상황이 발생할 수 있으므로 블록을 하지 않는 것이 좋음
  * 자바 9에서는 CompletableFuture에서 제공하는 몇 가지 기능을 이용해 이런 문제를 해결 가능
    * orTimeout 메서드
      * 지정된 시간이 지난 후에 CompletableFuture를 TimeoutException으로 완료하면서 록 
        또 다른 CompletableFuture를 반환할 수 있도록 내부적으로 ScheduledThreadExecutor를 활용함
      * 이 메서드를 이용하면 계산 파이프라인을 연결하고 여기서 TimeoutException이 발생했을 때 사용자가 쉽게 이해할 수 있는 메시지를 제공할 수 있음
      * Future가 3초 후에 작업을 끝내지 못할 경우 TimeoutException이 발생하도록 메서드 체인의 끝에 orTimeout 메서드 추가 가능
        * 물론 타임아웃 길이는 필요에 따라 조절 가능
        ```
        CompletableFuture<Double> futurePriceInUSD =
                CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                        .thenCombine(CompletableFuture.supplyAsync(
                                () -> exchangeService.getRate(Money.EUR, Money.USD)),
                                (price, rate) -> price * rate)
                        // 3초 뒤에 작업이 완료되지 않으면 Future가 TimeoutException을 발생시키도록 설정
                        // 자바 9에서는 비동기 타임아웃 관리 기능이 추가됨
                        .orTimeout(3, TimeUnit.SECONDS);
        ```
        * 일시적으로 서비스를 이용할 수 없는 상황에서는 꼭 서버에서 얻은 값이 아닌 미리 지정된 값을 상요할 수 있는 상황도 있음
        * 예를 들어 EUR을 USD로 환전하는 서비스가 1초 이내로 완료되어야 하지만  
          그렇지 않다고 전체 계산을 Exception으로 처리하진 않는 상황이라고 가정
          * 이런 상황에서는 미리 정의한 환전율을 이용해 연산을 이어갈 수 있음
            * 자바 9에 추가된 completeOnTimeout 메서드를 이용하여 이 기능을 쉽게 구현할 수 있음
              ```
              CompletableFuture<Double> futurePriceInUSD =
                      CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                              .thenCombine(CompletableFuture.supplyAsync(
                                      () -> exchangeService.getRate(Money.EUR, Money.USD))
                                      // 환전 서비스가 1초 안에 결과를 제공하지 않으면 기본 환율값 사용
                                      .completeOnTimeout(DEFAULT_RATE, 1, TimeUnit.SECONDS),
                                      (price, rate) -> price * rate)
                              .orTimeout(3, TimeUnit.SECONDS);
              ```
              * orTimeout 메서드처럼 completeOnTimeout 메서드는 CompletableFuture를 반환하여  
                이 결과를 다른 CompletableFuture 메서드와 연결 가능
              * 두 타임아웃 설정
                * 한 개는 3초 이내에 연산을 마치지 못하는 상황에서 발생
                * 다른 타임아웃은 1초 이후에 환율을 얻지 못했을 때 발생
                  * 이 때 타임아웃이 발생하면 미리 지정된 값을 사용함
  * 아직 한 가지 기능이 빠짐
    * 모든 검색이 완료될 때까지 사용자를 기다리게 만들지 말고, 이용할 수 있는 가격 정보는 즉시 사용자에게 보여줄 수 있어야 함
      * 대부분의 자동차보험 웹 사이트나 항공권 가격비교 웹 사이트는 이와 같은 방식으로 동작함
      * get, join으로 CompletableFuture가 완료될 때까지 블록하지 않고 다른 방식으로 CompletableFuture의 종료에 대응

### CompletableFuture의 종료에 대응하는 방법
* 이 장에 모든 예제 코드는 원격 메서드 응답을 1초 지연으로 흉내냄
* 하지만 실전에서 사용하는 다양한 원격 서비스는 얼마나 지연될지 예측하기 어려움
  * 서버 부하에서 네트워크 문제에 이르기까지 다양한 지연 요소가 있기 때문
  * 또한 질의당 얼마를 더 지불하느냐에 따라 우리 앱이 제공하는 서비스의 질이 달라질 수 있음
  * 여러 상점에 정보를 제공했을 때 몇몇 상점은 다른 상점보다 훨씬 먼저 결과를 제공할 가능성이 큼
* 따라서 0.5초에서 2.5초 사이의 임의의 지연으로 이를 시뮬레이션
  * 항상 1초를 지연하는 delay 메서드 대신 randomDelay 메서드를 사용함
    ```
    public static void randomDelay() {
        int delay = 500 + RANDOM.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
    ```
* 지금까지 다양한 상점에서 물건의 가격 정보를 얻어오는 findPrices 메서드를 구현했는데  
  현재는 모든 상점에서 가격 정보를 가져온 다음에 그것을 사용할 수 있음
  * 이제 모든 상점에서 가격 정보를 제공할 때까지 기다리지 않고 각 상점에서 가격 정보를 제공할 때마다 즉시 보여줄 수 있는 최저가격 검색 앱 구현
    * 심지어 일부 상점은 오래 기다리다 못해 타임아웃 일어날 수 있음

* 최저가격 검색 앱 리팩터링
  * 먼저 모든 가격 정보를 포함할 때까지 리스트 생성을 기다리지 않도록 고쳐야 함
  * 그러려면 상점에 필요한 일련의 연산 실행 정보를 포함하는 CompletableFuture의 스트림을 직접 제어해야 함
  * 다음처럼 코드 수정
    ```
    public Stream<CompletableFuture<String>> findPriceStream(String product) {
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(
                        quote -> CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)));
    }
    ```
    * 이제 findPriceStream 메서드 내부에서 세 가지 map 연산을 적용하고 반환하는 스트림에 네 번째 map 연산 적용하기
      * 새로 추가한 연산은 단순하게 CompletableFuture에 동작을 등록함
      * CompletableFuture에 등록된 동작은 CompletableFuture의 계산이 끝나면 값을 소비함
      * 자바 8의 CompletableFuture API는 thenAccept라는 메서드로 이 기능을 제공함
      * thenAccept 메서드는 연산 결과를 소비하는 Consumer를 인수로 받음
      * 우리 예제에서는 할인 서비스에 반환하는 문자열 값
        * 이 문자열은 상점명과 할인율을 적용한 제품의 가격을 포함함
        * 우리가 원하는 동작은 이 값을 출력하는 것
          ```
          findPricesStream("myPhone").map(f -> f.thenAccept(System.out::println));
          ```
        * thenCompose, thenCombine 메서드와 마찬가지로 thenAccept에도 thenAcceptAsync라는 Async 버전 존재
          * thenAcceptAsync 메서드는 CompletableFuture가 완료된 스레드가 아니라 새로운 스레드를 이용해 Consumer를 실행
          * 불필요한 컨텍스트 변경은 피하는 동시에 CompletableFuture가 완료되는 즉시 응답하는 것이 좋으므로 thenAcceptAsync를 사용하지 않음
            * 오히려 thenAcceptAsync를 사용하면 새로운 스레드를 이용할 수 있을 때까지 기다려야 하는 상황이 일어날 수 있음
          * thenAccept 메서드는 CompletableFuture가 생성한 결과를 어떻게 소비할지 미리 지정했으므로 CompletableFuture\<Void>를 반환
            * 따라서 네 번째 map 연산은 <CompletableFuture\<Void>>를 반환
            * 이제 CompletableFuture\<Void>가 동작을 끝낼 때까지 딱히 할 수 있는 일이 없음
      * 또한 가장 느린 상점에서 응답을 받아서 반환된 가격을 출력할 기회를 제공하고 싶다고 가정
        * 그러기 위해서는 다음 코드에서 보여주는 것처럼 스트림의 모든 CompletableFuture\<Void>를 배열로 추가하고 실행 결과를 기다려야 함
          ```
          CompletableFuture[] futures = findPricesStream(product)
                  .map(f -> f.thenAccept(System.out::println))
                  .toArray(size -> new CompletableFuture[size]);
          CompletableFuture.allOf(futures).join();
          ```
          * 팩토리 메서드 allOf는 CompletableFuture 배열을 입력으로 받아 CompletableFuture\<Void>를 반환
            * 전달된 모든 CompletableFuture가 완료되어야 CompletableFuture\<Void>가 완료됨
            * 따라서 allOf 메서드가 반환하는 CompletableFuture에 join을 호출하면  
              원래 스트림의 모든 CompletableFuture의 실행 완료를 기다릴 수 있음
            * 이를 이용해 최저가격 검색 앱은 '모든 상점이 결과를 반환했거나 타임아웃 되었음' 같은 메시지를 사용자에게 보여줌으로써  
              사용자는 추가로 가격 정보를 기다리지 않아도 된다는 사실을 보여줌
          * 반면 배열의 CompletableFuture 중 하나의 작업이 끝나길 기다리는 상황도 있을 수 있음
            * 예를 들어 두 개의 환율 정보 서버에 동시 접근했을 때 한 서버의 응답만 있으면 충분할 때
            * 이때는 팩토리 메서드 anyOf를 사용함
              * anyOf 메서드는 CompletableFuture 배열을 입력 받아 CompletableFuture\<Object>를 반환
              * CompletableFuture\<Object>는 처음으로 완료한 CompletableFuture의 값으로 동작을 완료함

* 응용
  * randomDelay로 0.5초에서 2.5초 사이에 임의의 지연을 발생시켜 원격 서비스 호출 흉내
  * 이제 코드를 실행시키면 예전처럼 가격 정보가 지정된 시간에 나타나지 않을 뿐 아니라 상점 가격 정보가 들어오는 대로 결과가 출력됨
  * 어떤 부분이 달라졌는지 명확하게 확인할 수 있도록 각각의 계산에 소요된 시간을 출력하는 부분을 코드에 추가
    ```
    long start = System.nanoTime();
    CompletableFuture[] futures = findPricesStream("myPhone27S")
            .map(f -> f.thenAccept(
                    s -> System.out.println(
                            s + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
            .toArray(size -> new CompletableFuture[size]);
    CompletableFuture.allOf(futures).join();
    System.out.println("All shops have now responded in " 
    				+ ((System.nanoTime() - start) / 1_000_000) + " msecs");
    ```
    * 결과
      ```
      MyFavoriteShop price is 192.72 (done in 1632 msecs)
      LetsSaveBig price is 135.58 (done in 2024 msecs)
      BuyItAll price is 184.74 (done in 2930 msecs)
      BestPrice price is 110.93 (done in 3090 msecs)
      ShopEasy price is 167.28 (done in 3520 msecs)
      All shops have now responded in 3520 msecs
      ```
    * 임의의 지연이 추가되면 마지막 가격 정보에 비해 처음 가격 정보를 두 배 빨리 얻는다는 것을 출력 결과에서 확인

### 로드맵
* CompletableFuture(연산 또는 값으로 종료하는 일회성 기법)의 기능이 한 번에 종료되지 않고 일련의 값을 생산하도록 일반화하는 자바 9 플로 API

### 정리
* 한 개 이상의 원격 외부 서비스를 사용하는 긴 동작을 실행할 때는 비동기 방식으로 앱의 성능과 반응성을 향상시킬 수 있음
* 우리 고객에게 비동기 API를 제공하는 것을 고려해야 함
  * CompletableFuture의 기능을 이용하면 쉽게 비동기 API를 구현할 수 있음
* CompletableFuture를 이용할 때 비동기 태스크에서 발생한 에러를 관리하고 전달할 수 있음
* 동기 API를 CompletableFuture로 감싸서 비동기적으로 소비할 수 있음
* 여러 비동기 동작을 조립하고 조합할 수 있음 (아래와 같은 상황)
  * 서로 독립적인 비동기 동작
  * 하나의 비동기 동작이 다른 비동기 동작의 결과에 의존
* CompletableFuture에 콜백을 등록해서 Future가 동작을 끝내고 결과를 생산했을 때 어떤 코드를 실행하도록 지정할 수 있음
* CompletableFuture 리스트의 모든 값이 완료될 때까지 기다릴지 아니면 첫 값만 완료되길 기다릴지 선택할 수 있음
* 자바 9에서는 orTimeout, completeOnTimeout 메서드로 CompletableFuture에 비동기 타임아웃 기능을 추가

### [quiz]
---

## chapter 17 - 리액티브 프로그래밍
* 리액티브 프로그래밍 패러다임의 중요성이 증가하는 이유
  * 수년 전까지 대규모 앱은 수십 대의 서버, 기가바이트의 데이터, 수초의 응답 시간, 당연히 여겨졌던 몇 시간의 유지보수 시간 등의 특징을 가졌음
  * 오늘날에는 다음과 같은 세 가지 이유로 상황이 변하고 있음
    * 빅데이터
      * 보통 빅데이터는 페타바이트 단위로 구성되며 매일 증가함
    * 다양한 환경
      * 모바일 디바이스에서부터 수천 개의 멀티 코어 프로세서로 실행되는 클라우드 기반 클러스터에 이르기까지 다양한 환경에 앱이 배포됨
    * 사용 패턴
      * 사용자는 1년 내내 항상 서비스를 이용할 수 있으며 밀리초 단위의 응답 시간을 기대함
  * 예전 소프트웨어 아키텍처로는 오늘날의 요구사항을 만족시킬 수 없음
    * 인터넷 트래픽을 가장 많이 일으키는 디바이스가 모바일인 요즘은 이런 양상이 두드러짐
    * 사물인터넷(IoT) 같은 기기들로 가까운 미래에는 상황이 더욱 심화될 것
  * 리액티브 프로그래밍에서는 다양한 시스템과 소스에서 들어오는 데이터 항목 스트림을 비동기적으로 처리하고 합쳐서 이런 문제를 해결함
    * 실제로 이런 패러다임에 맞게 설계된 앱은 발생한 데이터 항목을 바로 처리함으로 사용자에게 높은 응답성을 제공함
    * 게다가 한개의 컴포넌트나 앱뿐 아니라 전체의 리액티브 시스템을 구성하는 여러 컴포넌트를 조절하는 데도 리액티브 기법을 사용할 수 있음
    * 이런 방식으로 구성된 시스템에서는 고장, 정전 같은 상태에 대처할 뿐 아니라 다양한 네트워크 상태에서 메시지를 교환하고 전달할 수 있음
      * 무거운 작업을 하고 있는 상황에서도 가용성을 제공함
      * 전통적으로 개발자는 자신의 시스템이나 앱이 서로 느슨하게 결합되었다고 가정하지만 실제 이들 컴포넌트 자체가 앱인 상황이 많음
      * 이런 맥락에서 컴포넌트와 앱의 의미는 거의 같다고 할 수 있음

### 리액티브 매니패스토
* [reactive manifesto] https://www.reactivemanifesto.org/ko
* 2013 ~ 2014년에 걸쳐 개발되었으며 리액티브 앱과 시스템 개발의 핵심 원칙을 공식적으로 정의함
  * 조나스 보너(Jonas Boner), 데이브 팔리(Dave Farley), 롤랜드 쿤(Roland Kuhn), 마틴 톰슨(Martin Thompson)에 의해 개발됨
  * 반응성(responsive) - 선언문에는 응답성으로 표기되어 있음
    * 리액티브 시스템은 빠를 뿐 아니라 더 중요한 특징으로 일정하고 예상할 수 있는 반응 시간을 제공함
    * 결과적으로 사용자가 기대치를 가질 수 있음
    * 기대치를 통해 사용자의 확신이 증가하면서 사용할 수 있는 앱이라는 확인을 제공할 수 있음
  * 회복성(resilient) - 선언문에는 탄력성으로 표기되어 있음
    * 장애가 발생해도 시스템은 반응해야 함
    * 컴포넌트 실행 복제, 여러 컴포넌트의 시간(발송자와 수신자가 독립적인 생명주기를 가짐)과 공간(발송자와 수신자가 다른 프로세스에서 실행됨) 분리
    * 각 컴포넌트가 비동기적으로 작업을 다른 컴포넌트에 위임하는 등 리액티브 매니페스토는 회복성을 달성할 수 있는 다양한 기법을 제시함
  * 탄력성(elastic) - 선언문에는 유연성으로 표기되어 있음
    * 앱의 생명주기 동안 다양한 작업 부하를 받게 되는데 이 다양한 작업 부하로 앱의 반응성이 위협받을 수 있음
    * 리액티브 시스템에서는 무서운 작업 부하가 발생하면 자동으로 관련 컴포넌트에 할당된 자원 수를 늘림
  * 메시지 주도(Message-driven) - 선언문에는 메시지 구동으로 표기되어 있음
    * 회복성과 탄력성을 지원하려면 약한 결합, 고립, 위치 투명성을 지원할 수 있도록 시스템을 구성하는 컴포넌트의 경계를 명확하게 정의해야 함
    * 비동기 메시지를 전달해 컴포넌트 끼리의 통신이 이루어짐
    * 이 덕분에 회복성(장애를 메시지로 처리)과 탄력성(주고 받은 메시지의 수를 감시하고 메시지의 양에 따라 적절하게 리소스를 할당)을 얻을 수 있음
* 리액티브 시스템의 핵심 기능
  * 반응성
    * 시스템은 가능한한 적정 시간안에 반응함
    * 반응성이 뒷받침되어야 사용성을 높일 수 있음
  * 탄력성
    * 다양한 작업 부하에도 시스템 반응성이 유지됨
    * 입력 속도가 바뀐다면 이들 입력 관련 서비스에 할당된 자원을 늘리거나 줄임으로 반응할 수 있음
  * 회복성
    * 장애 시에도 시스템의 반응성은 유지됨
  * 컴포넌트 간의 약한 결합, 고립, 위치 투명성이 유지되도록 시스템은 비동기 메시지 전달에 의존함

* 애플리케이션 수준의 리액티브
  * 앱 수준 컴포넌트의 리액티브 프로그래밍의 주요 기능은 비동기로 작업을 수행할 수 있다는 점
    * 17장의 나머지 부분에서는 이벤트 스트림을 블록하지 않고 비동기로 처리하는 것이 최신 멀티코어 CPU의 사용률을 극대화할 수 있는 방법
      * 엄밀히 따지자면 내부적으로 경쟁하는 CPU의 스레드 사용률을 극대화
    * 이 목표를 달성할 수 있도록 리액티브 프레임워크와 라이브러리는  
      스레드(상대적으로 비싸고 희귀한 자원)를 퓨처, 액터, 일련의 콜백을 발생시키는 이벤트 루프 등과 공유하고 처리할 이벤트를 변환하고 관리
  * 배경 지식 확인
    * 이벤트
    * 메시지
    * 시그널
    * 이벤트 루프
    * 발행-구독
    * 리스너
    * 역압력
  * 이들 기술은 스레드보다 가벼울 뿐 아니라 개발자에게 큰 이득을 제공
    * 개발자 입장에서는 이들 기술을 이용함으로 동시, 비동기 앱 구현의 추상 수준을 높일 수 있으므로  
      동기 블록, 경쟁 조건, 데드락 같은 저 수준의 멀티스레드 문제를 직접 처리할 필요가 없어지면서 비즈니스 요구사항을 구현하는 데 더 집중할 수 있음
  * 스레드를 다시 쪼개는 종류의 기술을 이용할 때는 메인 이벤트 루프 안에서는 절대 동작을 블록하지 않아야 한다는 중요한 전제 조건이 항상 따름
    * 데이터베이스나 파일 시스템 접근, 작업 완료까지 얼마나 걸릴지 예측이 힘든 원격 서비스 호출 등 모든 I/O 관련 동작이 블록 동작에 속함
  * 두 스레드를 포함하는 풀이 있고 이벤트 스트림 세 개를 처리하는 상황을 가정
    * 한 번에 오직 두 개의 스트림을 처리할 수 있는 상황
    * 가능하면 이들 스트림은 두 스레드를 효율적이고 공정하게 공유해야 함
    * 어떤 스트림의 이벤트를 처리하다보니 파일 시스템 기록 또는 블록되는 API를 이용해 데이터베이스에서 파일을 가져오는 등 느린 I/O 작업이 시작됨
    * 이런 상황에서 스레드 2는 I/O 동작이 끝나기를 기다리며 소모됨
    * 스레드 1은 여전히 첫 번째 스트림을 처리할 수 있지만 이전에 시작된 블록 동작이 끝나기 전까지 세 번째 스트림은 처리되지 못함
    * RxJava, Akka 같은 리액티브 프레임워크는 별도로 지정된 스레드 풀에서 블록 동작을 실행시켜 이 문제를 해결
    * 메인 풀의 모든 스레드는 방해받지 않고 실행되므로 모든 CPU 코어가 가장 최적의 상황에서 동작할 수 있음
      * 플랫폼의 특성과 라이브러리 구현에 따라서 스레드 풀 종류의 개수가 달라질 수 있음
      * 예를 들어 최근 안드로이드 개발에 사용되는 코틀린에서 제공하는 코루틴(Coroutines)에서 
        * I/O 관련 작업을 실행하도록 지정된 Dispatcher.IO
        * UI 작업을 실행하는 Dispatcher.Main
        * 기타 CPU를 사용하는 작업을 실행하는 Dispatcher.Default
        * 등을 제공함
    * CPU 관련 작업과 I/O 관련 작업을 분리하면 조금 더 정밀하게 풀의 크기 등을 설정할 수 있고 두 종류의 작업의 성능을 관찰할 수 있음
      * CPU 관련 작업이든 I/O 관련 작업이든 현재 스레드를 블록한다는 사실은 같음
      * 하지만 두 작업에는 큰 차이가 있음
      * CPU 관련 작업은 실제로 할당된 CPU 코어 스레드를 100% 활용해 뭔가를 연산하느라 다른 일을 처리할 수 없어 블록되는 상황
      * 반면 I/O 관련 작업에서는 사용자 입력 같은 외부 응답을 기다리면서 CPU 코어나 스레드가 처리할 일이 없이 블록되는 상황
      * 이런 이유에서 개발자는 CPU가 최대로 활용될 수 있도록 특정 작업이 CPU 관련 작업인지 I/O 관련 작업인지를 적절하게 선택해야 함
  * 리액티브 원칙을 따르는 것은 리액티브 프로그래밍의 일부일 뿐이며 가장 어려운 부분은 따로 있음
    * 리액티브 시스템을 만들려면 훌륭하게 설계된 리액티브 앱 집함이 서로 잘 조화를 이루게 만들어야 함

* 시스템 수준의 리액티브
  * 리액티브 시스템은 여러 앱이 한 개의 일관적인, 회복할 수 있는 플랫폼을 구성할 수 있게 해줄 뿐 아니라  
    이들 앱 중 하나가 실패해도 전체 시스템은 계속 운영될 수 있도록 도와주는 소프트웨어 아키텍처
    * 리액티브 앱은 비교적 짧은 시간 동안만 유지되는 데이터 스트림에 기반한 연산을 수행하며 보통 이벤트 주도로 분류됨
    * 리액티브 시스템의 주요 속성으로 메시지 주도를 꼽을 수 있음
  * 메시지는 정의된 목적지 하나를 향하는 반면, 이벤트는 관련 이벤트를 관찰하도록 등록한 컴포넌트가 수신한다는 점이 다름
    * 리액티브 시스템에서는 수신자와 발신자가 각각 수신 메시지, 발신 메시지와 결합하지 않도록 이들 메시지를 비동기로 처리해야 함
    * 그래야 시스템이 장애(회복성)와 높은 부하(탄력성)에서도 반응성을 유지할 수 있음
  * 정확히 말하자면 리액티브 아키텍처에서는 컴포넌트에서 발생한 장애를 고립시킴으로  
    문제가 주변의 다른 컴포넌트로 전파되면서 전체 시스템 장애로 이어지는 것을 막음으로 회복성을 제공함
    * 시스템에 장애가 발생했을 때 서서히 성능이 저하되는 것이 아니라 문제를 격리함으로 장애에서 완전 복구되어 건강한 상태로 시스템이 돌아옴
    * 이런 맥락에서 횝고성은 결함 허용 능력(fault-tolerance)과 같은 의미를 지님
    * 시스템에 장애가 발생했을 때 서서히 성능이 저하되는 것이 아니라 문제를 격리함으로 장애에서 완전히 복구되어 건강한 상태로 시스템이 돌아옴
    * 이런 마법은 에러 전파를 방지하고 이들을 메시지로 바꾸어 다른 컴포넌트로 보내는 등 감독자 역할을 수행함으로 이루어짐
    * 이렇게 컴포넌트 자체로 문제가 한정되고 외부로는 안정성을 보장하는 방식으로 문제를 관리할 수 있음
  * 고립과 비결합이 회복성의 핵심이라면 탄력성의 핵심은 위치 투명성
    * 위치 투명성은 리액티브 시스템의 모든 컴포넌트가 수신자의 위치에 상관없이 다른 모든 서비스와 통신할 수 있음을 의미
    * 위치 투명성 덕분에 시스템을 복제할 수 있으며 현재 작업 부하에 따라 (자동으로) 앱을 확장할 수 있음
    * 위치를 따지지 않는 확장성은 리액티브 앱(시간에 기반한 비동기, 동시적, 비결합)과  
      리액티브 시스템(위치 투명성을 통한 공간적 비결합할 수 있음)의 또 다른 차이를 보여줌

### 리액티브 스트림과 플로 API
* 리액티브 프로그래밍은 리액티브 스트림을 사용하는 프로그래밍
  * 리액티브 스트림은 잠재적으로 무한의 비동기 데이터를 순서대로 그리고 블록하지 않는 역압력을 전제해 처리하는 표준 기술
  * 역압력은 발행-구독 프로토콜에서 이벤트 스트림의 구독자가 이벤트를 발행자가 이벤트를 제공하는 속도보다 느린 속도로 소비하면서  
    문제가 발생하지 않도록 보장하는 장치
  * 이런 상황이 발생했을 때 부하가 발생한 컴포넌트가 완전 불능이 되거나 예기치 않는 방식으로 이벤트를 잃어버리는 등의 문제가 발생하지 않음
  * 부하가 발생한 컴포넌트는 이벤트 발생 속도를 늦추라고 알리거나,  
    얼마나 많은 이벤트를 수신할 수 있는지 알리거나,  
    다른 데이터를 받기 전에 기존의 데이터를 처리하는 데 얼마나 시간이 걸리는지를 업스트림 발행자에게 알릴 수 있어야 함
* 스트림 처리의 비동기적인 특성상 역압력 기능의 내장은 필수라는 사실을 알 수 있음
  * 실제 비동기 작업이 실행되는 동안 시스템에는 암묵적으로 블록 API로 인해 역압력이 제공되는 것
  * 안타깝게도 비동기 작업을 실행하는 동안에는 그 작업이 완료될 때까지 다른 유용한 작업을 실행할 수 없으므로 기다리면서 많은 자원을 낭비하게 됨
  * 반면 비동기 API를 이용하면 하드웨어 사용률을 극대화할 수 있지만 다른 느린 다운스트림 컴포넌트에 너무 큰 부하를 줄 가능성이 생김
  * 따라서 이런 상황을 방지할 수 있도록 역압력이나 제어 흐름 기법이 필요함
  * 이들 기법은 데이터 수신자가 스레드를 블록하지 않고도 데이터 수신자가 처리할 수 없을만큼 많은 데이터를 받는 일을 방지하는 프로토콜을 제공함
* 넷플릭스, 레드햇, 트위터, 라이트밴드 및 기타 회사들이 참여한 리액티브 스트림 프로젝트(Reactive Streams project)
  * http://www.reactive-streams.org
  * 모든 리액티브 스트림 구현이 제공해야 하는 최소 기능 집합을 네 개의 관련 인터페이스로 정의함
  * 자바 9의 새로운 java.util.concurrent.Flow 클래스 뿐 아니라
    * Akka 스트림(라이트벤드)
    * 리액터(피보탈)
    * RxJava(넷플릭스)
    * Vert.x(레드햇)
    * 등 많은 서드 파티 라이브러리에서 이들 인터페이스를 구현

* Flow 클래스 소개
  * 자바 9에서는 리액티브 프로그래밍을 제공하는 클래스 java.util.concurrent.Flow 추가
    * 이 클래스는 정적 컴포넌트 하나를 포함하고 있으며 인스턴스화할 수 없음
    * 리액티브 스트림 프로젝트의 표준에 따라 프로그래밍 발행-구독 모델을 지원할 수 있도록 Flow 클래스는 중첩된 인터페이스 네 개를 포함
      * Publisher
      * Subscriber
      * Subscription
      * Processor
    * Publisher가 항목을 발행하면 Subscriber가 한 개씩 또는 한 번에 여러 항목을 소비하는데
      Subscription이 이 과정을 관리할 수 있도록 Flow 클래스는 관련된 인터페이스와 정적 메서드를 제공
    * Publisher는 수많은 일련의 이벤트를 제공할 수 있지만 Subscriber가 요구사항에 따라 역압력 기법에 의해 이벤트 제공 속도가 제한됨
    * Publisher는 자바의 함수형 인터페이스(한 개의 추상 메서드만 정의)로  
      Subscriber는 Publisher가 발행한 이벤트의 리스너로 자신을 등록할 수 있음
    * Subscription은 Publisher와 Subscriber 사이의 제어 흐름, 역압력을 관리
    * 이들 세 인터페이스 및 Processor 인터페이스
      * Flow.Publisher 인터페이스
        ```
        @FunctionalInterface
        public static interface Publisher<T> {
            public void subscribe(Subscriber<? super T> subscriber);
        }
        ```
      * Flow.Subscriber 인터페이스
        * Subscriber는 Publisher가 관련 이벤트를 발행할 때 호출할 수 있도록 콜백 메서드 네 개를 정의
        ```
        public static interface Subscriber<T> {
            public void onSubscribe(Subscription subscription);
            public void onNext(T item);
            public void onError(Throwable throwable);
            public void onComplete();
        }
        ```
        * 이들 이벤트는 다음 프로토콜에서 정의한 순서로 지정된 메서드 호출을 통해 발행되어야 함
          * onSubscribe onNext* (onError | onComplete)?
          * 위 표기는 onSubscribe 메서드가 항상 처음 호출되고 이어서 onNext가 여러 번 호출될 수 있음을 의미
          * 이벤트 스트림은 영원히 지속되거나 아니면 onComplete 콜백을 통해 더 이상의 데이터가 없고 종료됨을 알릴 수 있으며  
            또는 Publisher에 장애가 발생했을 때는 onError를 호출할 수 있음
            * 터미널로 문자열을 읽을 때 파일의 끝이나 I/O 에러와 비슷하다고 생각하면 쉬움
        * Subscriber가 Publisher에 자신을 등록할 때 Publisher는 처음으로 onSubscribe 메서드를 호출해 Subscription 객체를 전달
          * Subscription 인터페이스는 메서드 두 개를 정의함
          * Subscription은 첫 번째 메서드로 Publisher에게 주어진 개수의 이벤트를 처리할 준비가 되었음을 알릴 수 있음
          * 두 번째 메서드로는 Subscription을 취소, 즉 Publisher에게 더 이상 이벤트를 받지 않음을 통지함
      * Flow.Subscription 인터페이스
        ```
        public static interface Subscription {
            public void request(long n);
            public void cancel();
        }
        ```
      * Flow.Processor 인터페이스
        * Processor 인터페이스는 단지 Publisher와 Subscriber를 상속받을 뿐 아무 메서드도 추가하지 않음
          ```
          public static interface Processor<T,R> extends Subscriber<T>, Publisher<R> {
          }
          ```
        * 실제 이 인터페이스는 리액티브 스트림에서 처리하는 이벤트의 변환 단계를 나타냄
          * Processor가 에러를 수신하면 이로부터 회복하거나(그리고 Subscription은 취소로 간주)  
            즉시 onError 신호로 모든 Subscriber에 에러를 전파할 수 있음
          * 마지막으로 Subscriber가 Subscription을 취소하면 Processor는 자신의 업스트림 Subscription도 취소함으로 취소 신호를 전파해야 함
            * 명세서에서 취소가 엄격하게 요구되지 않음에도 불구하고
    * 자바 9 플로 명세서에서는 이들 인터페이스 구현이 어떻게 서로 협력해야 하는지를 설명하는 규칙 집합을 정의
      * 규칙 요약
        * Publisher는 반드시 Subscription의 request 메서드에 정의된 개수 이하의 요소만 Subscriber에 전달해야 함
          * 하지만 Publisher는 지정된 개수보다 적은 수의 요소를 onNext로 전달할 수 있음
            * 동작이 성공적으로 끝났으면 onComplete를 호출
            * 문제가 발생하면 onError를 호출해 Subscription을 종료할 수 있음
        * Subscriber는 요소를 받아 처리할 수 있음을 Publisher에 알려야 함
          * 이런 방식으로 Subscriber는 Publisher에 역압력을 행사할 수 있고  
            Subscriber가 관리할 수 없이 너무 많은 요소를 받는 일을 피할 수 있음
          * 더욱이 onComplete나 onError 신호를 처리하는 상황에서  
            Subscriber는 Publisher나 Subscription의 어떤 메서드도 호출할 수 없으며 Subscription이 취소되었다고 가정해야 함
          * 마지막으로 Subscriber는 Subscription.request() 메서드 호출이 없이도 언제든 종료 시그널을 받을 준비가 되어 있어야 함
            * Subscription.cancel()이 호출된 이후에라도 한 개 이상의 onNext를 받을 준비가 되어 있어야 함
        * Publisher와 Subscriber는 정확하게 Subscription을 공유해야 하며 각각이 고유한 역할을 수행해야 함
          * 그러려면 onSubscribe와 onNext 메서드에서 Subscriber는 request 메서드를 동기적으로 호출할 수 있어야 함
          * 표준에서는 Subscription.cancel() 메서드는 몇 번을 호출해도 한 번 호출한 것과 같은 효과를 가져야 하며  
            여러 번 이 메서드를 호출해도 다른 추가 호출에 별 영향이 없도록 스레드에 안전해야 한다고 명시함
          * 같은 Subscriber 객체에 다시 가입하는 것은 권장하지 않지만 이런 상황에서 예외가 발생해야 한다고 명세서가 강제하진 않음
          * 이전의 취소된 가입이 영구적으로 적용되었다면 이후의 기능에 영향을 주지 않을 가능성도 있기 때문
    * 자바 9 플로 API/리액티브 스트림 API에서는 Subscriber 인터페이스의 모든 메서드 구현이 Publisher를 블록하지 않도록 강제
      * 하지만 이들 메서드가 이벤트를 동기적으로 처리해야 하는지 아니면 비동기적으로 처리해야 하는지는 지정하지 않음
      * 하지만 이들 인터페이스에 정의된 모든 메서드는 void를 반환, 온전히 비동기 방식으로 이들 메서드를 구현할 수 있음

* 첫 번째 리액티브 애플리케이션 만들기
  * Flow 클래스에 정의된 인터페이스는 대부분 직접 구현하도록 의도된 것이 아님
    * 그럼에도 자바 9 라이브러리는 이들 인터페이스를 구현하는 클래스를 제공하지 않음
    * 이전에 언급한 Akka, RxJava 등의 리액티브 라이브러리에서는 이들 인터페이스를 구현함
    * 자바 9 java.util.concurrent.Flow 명세는 이들 라이브러리가 준수해야 할 규칙과 다양한 리액티브 라이브러리를 이용해 개발된  
      리액티브 앱이 서로 협동하고 소통할 수 있는 공용어를 제시
    * 더욱이 이들 리액티브 라이브러리는 보통 추가적인 기능을 제공함
      * java.util.concurrent.Flow 인터페이스에 정의된 최소 하위 집합을 넘어 리액티브 스트림을 변형하고 합치는 기능과 관련된 클래스와 메서드 등
  * 배경은 그렇지만 자바 9 플로 API를 직접 이용하는 첫 리액티브 앱을 개발하면서 지금까지 배운 네 개의 인터페이스가 어떻게 동작하는지 쉽게 확인
    * 끝에 가서는 리액티브 원칙을 적용해 온도를 보고하는 간단한 프로그램을 완성
    * 이 프로그램은 두 컴포넌트를 포함함
      * TempInfo
        * 원격 온도계를 흉내 냄
        * 0에서 99 사이의 화씨 온도를 임의로 만들어 연속적으로 보고
      * TempSubscriber
        * 레포트를 관찰하면서 각 도시에 설치된 센서에서 보고한 온도 스트림을 출력함
    * 예제 소스 작성
      * 온도를 전달하는 간단한 클래스 정의
        ```
        public class TempInfo {
            public static final Random random = new Random();
            private final String town;
            private final int temp;
        
            public TempInfo(String town, int temp) {
                this.town = town;
                this.temp = temp;
            }
        
            // 정적 팩토리 메서드를 이용해 해당 도시의 TempInfo 인스턴스 생성
            public static TempInfo fetch(String town) {
                // 10분의 1 확률로 온도 가져오기 작업 실패
                if (random.nextInt(10) == 0) {
                    throw new RuntimeException("Error");
                }
                // 0에서 99 사이에서 임의의 화씨 온도를 반환
                return new TempInfo(town, random.nextInt(100));
            }
        
            @Override
            public String toString() {
                return "TempInfo{" +
                    "town='" + town + '\'' +
                    ", temp=" + temp +
                    '}';
            }
        
            public String getTown() {
                return town;
            }
        
            public int getTemp() {
                return temp;
            }
        }
        ```
      * Subscriber가 요청할 때마다 해당 도시의 온도를 전송하도록 Subscription 구현
        ```
        public class TempSubscription implements Subscription {
        	private final Flow.Subscriber<? super TempInfo> subscriber;
        	private final String town;
        
        	public TempSubscription(Flow.Subscriber<? super TempInfo> subscriber, String town) {
        		this.subscriber = subscriber;
        		this.town = town;
        	}
        
        	@Override
        	public void request(long n) {
        		// Subscriber가 만든 요청을 한 개씩 반복
        		for (long i = 0L; i < n; i++) {
        			try {
        				// 현재 온도를 Subscriber로 전달
        				subscriber.onNext(TempInfo.fetch(town));
        			} catch (Exception e) {
        				// 온도 가져오기를 실패하면 Subscriber로 에러를 전달
        				subscriber.onError(e);
        				break;
        			}
        		}
        	}
        
        	@Override
        	public void cancel() {
        		// 구독이 취소되면 완료(onComplete) 신호를 Subscriber로 전달
        		subscriber.onComplete();
        	}
        }
        ```
      * 새 요소를 얻을 때마다 Subscription이 전달한 온도를 출력하고 새 레포트를 요청하는 Subscriber 클래스 구현
        ```
        public class TempSubscriber implements Subscriber<TempInfo> {
        	private Subscription subscription;
        
        	// 구독을 저장하고 첫 번째 요청을 전달
        	@Override
        	public void onSubscribe(Flow.Subscription subscription) {
        		this.subscription = subscription;
        		subscription.request(1);
        	}
        
        	// 수신한 온도를 출력하고 다음 정보를 요청
        	@Override
        	public void onNext(TempInfo tempInfo) {
        		System.out.println(tempInfo);
        		subscription.request(1);
        	}
        
        	// 에러가 발생하면 에러 메시지 출력
        	@Override
        	public void onError(Throwable throwable) {
        		System.err.println(throwable.getMessage());
        	}
        
        	@Override
        	public void onComplete() {
        		System.out.println("DONE!");
        	}
        }
        ```
      * Main 클래스 구현
        * 리액티브 앱이 실제 동작할 수 있도록 Publisher를 만들고 TempSubscriber를 이용해 Publisher에 구독
        ```
        public class Main {
        
        	public static void main(String[] args) {
        		getTemperatures("New York").subscribe(new TempSubscriber());
        	}
        
        	// 구독한 Subscriber에게 TempSubscription을 전송하는 Publisher를 반환
        	private static Publisher<TempInfo> getTemperatures(String town) {
        		return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
        	}
        }
        ```
        * 여기서 getTemperatures 메서드는 subscriber를 인수로 받아 Subscriber의 onSubscribe 메서드를 호출함
          * 메서드 호출시 (새 TempSubscription 인스턴스를 인수로 함)
          * 람다의 시그니처가 Publisher 함수형 인터페이스의 유일한 메서드와 같은 시그니처를 가지므로  
            자바 컴파일러는 자동으로 람다를 Publisher를 바꿀 수 있음
          * main 메서드는 뉴욕의 온도를 보고할 Publisher를 만들고 새 TempSubscriber 클래스 인스턴스를 자신에게 구독시킴
        * main 메서드 실행 결과
          ```
          TempInfo{town='New York', temp=97}
          TempInfo{town='New York', temp=42}
          TempInfo{town='New York', temp=58}
          TempInfo{town='New York', temp=57}
          TempInfo{town='New York', temp=96}
          TempInfo{town='New York', temp=94}
          TempInfo{town='New York', temp=58}
          TempInfo{town='New York', temp=56}
          TempInfo{town='New York', temp=51}
          TempInfo{town='New York', temp=48}
          TempInfo{town='New York', temp=23}
          TempInfo{town='New York', temp=30}
          TempInfo{town='New York', temp=82}
          TempInfo{town='New York', temp=33}
          TempInfo{town='New York', temp=36}
          Error
          ```
      * 스택 오버플로우 발생 문제 해결
        * Executor를 TempSubscription으로 추가한 다음 다른 스레드에서 TempSubscriber로 세 요소를 전달하는 방법이 있음
        * 그러려면 TempSubscription을 바꿔야 함
          ```
          // 기존 코드 생략
          public class TempSubscription implements Flow.Subscription {
              // Executor 추가
              private static final ExecutorService executor = Executors.newSingleThreadExecutor();
          
              @Override
              public void request(long n) {
                  executor.submit(() -> {
                      for (long i = 0L; i < n; i++) {
                          try {
                              subscriber.onNext(TempInfo.fetch(town));
                          } catch (Exception e) {
                              subscriber.onError(e);
                              break;
                          }
                      }
                  });
              }
          ```
    * 지금까지는 플로 API에 정의된 네 개의 인터페이스 중 세 개만 활용

* Processor로 데이터 변환하기
  * Processor는 Subscriber이며 동시에 Publisher
    * 사실 Processor의 목적은 Publisher를 구독한 다음 수신한 데이터를 가공해 다시 제공하는 것
    * Processor 구현
      * 화씨로 제공된 데이터를 섭씨로 변환해 다시 방출하는 예제
      ```
      // 화씨를 섭씨로 변환하는 Processor
      // TempInfo를 다른 TempInfo로 변환하는 프로세서
      public class TempProcessor implements Flow.Processor<TempInfo, TempInfo> {
          private Flow.Subscriber<? super TempInfo> subscriber;
      
          @Override
          public void subscribe(Flow.Subscriber<? super TempInfo> subscriber) {
              this.subscriber = subscriber;
          }
      
          // 다른 모든 신호는 업스트림 구독자에게 전달
          @Override
          public void onSubscribe(Flow.Subscription subscription) {
              subscriber.onSubscribe(subscription);
          }
      
          @Override
          public void onNext(TempInfo temp) {
              // 섭씨로 변환 후 TempInfo를 다시 전송
              subscriber.onNext(new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 / 9));
          }
      
          // 다른 모든 신호는 업스트림 구독자에게 전달
          @Override
          public void onError(Throwable throwable) {
              subscriber.onError(throwable);
          }
      
          // 다른 모든 신호는 업스트림 구독자에게 전달
          @Override
          public void onComplete() {
              subscriber.onComplete();
          }
      }
      ```
      * TempProcessor에서 로직을 포함하는 유일한 메서드인 onNext는 화씨를 섭씨로 변환한 다음 온도를 재전송함
        * Subscriber 인터페이스를 구현하는 다른 모든 메서드는 단순히 수신한 모든 신호를 업스트림 Subscriber로 전달하며  
          Publisher의 subscribe 메서드는 업스트림 Subscriber를 Processor로 등록하는 동작을 수행함
        * 활용 예시
          ```
          public class Main {
          
              public static void main(String[] args) {
                  // 뉴욕의 섭씨 온도를 전송할 Publisher 생성, TempSubscriber를 Publisher로 구독
                  getCelsiusTemperatures("New York").subscribe(new TempSubscriber());
              }
          
              // 구독한 Subscriber에게 TempSubscription을 전송하는 Publisher를 반환
              private static Flow.Publisher<TempInfo> getTemperatures(String town) {
                  return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
              }
          
              public static Flow.Publisher<TempInfo> getCelsiusTemperatures(String town) {
                  return subscriber -> {
                      // TempProcessor를 만들고 Subscriber와 반환된 Publisher 사이로 연결
                      TempProcessor processor = new TempProcessor();
                      processor.subscribe(subscriber);
                      processor.onSubscribe(new TempSubscription(processor, town));
                  };
              }
          }
          ```
          * 결과
            ```
            TempInfo{town='New York', temp=7}
            TempInfo{town='New York', temp=22}
            TempInfo{town='New York', temp=33}
            TempInfo{town='New York', temp=22}
            TempInfo{town='New York', temp=15}
            TempInfo{town='New York', temp=35}
            TempInfo{town='New York', temp=-17}
            TempInfo{town='New York', temp=14}
            TempInfo{town='New York', temp=-15}
            TempInfo{town='New York', temp=-12}
            TempInfo{town='New York', temp=10}
            TempInfo{town='New York', temp=36}
            TempInfo{town='New York', temp=2}
            TempInfo{town='New York', temp=17}
            TempInfo{town='New York', temp=-17}
            Error
            ```

* 자바는 왜 플로 API 구현을 제공하지 않는가
  * 자바 라이브러리는 보통 인터페이스와 구현을 제공하는 반면 플로 API는 구현을 제공하지 않으므로 우리가 직접 인터페이스를 구현
    * 일례로 리스트 API와 비교
    * ArrayList\<T>를 포함한 다양한 클래스가 List\<T> 인터페이스를 구현
    * 엄밀하게는 ArrayList\<T> 클래스는 List\<T> 인터페이스를 구현하는 추상 클래스 AbstractList\<T>를 상속받음
  * 반면 자바 9에서는 Publisher\<T> 인터페이스만 선언하고 구현을 제공하지 않으므로 직접 인터페이스를 구현해야 함
    * 인터페이스가 프로그래밍의 구조를 만드는데 도움이 될 순 있지만 프로그램을 더 빨리 구현하는 데는 도움이 되지 않음
  * API를 만들 당시 Akka, RxJava 등 다양한 리액티브 스트림의 자바 코드 라이브러리가 이미 존재했기 때문
    * 원래 같은 발행-구독 사상에 기반해 리액티브 프로그래밍을 구현
    * 하지만 이들 라이브러리는 독립적으로 개발되었고 서로 다른 이름 규칙과 API를 사용함
    * 자바 9의 표준화 과정에서 기존처럼 자신만의 방법이 아닌 이들 라이브러리는  
      공식적으로 java.util.concurrent.Flow의 인터페이스를 기반으로 리액티브 개념을 구현하도록 진화
      * 이 표준화 작업 덕분에 다양한 라이브러리가 쉽게 협력할 수 있게 되었음
  * 리액티브 스트림 구현을 만드는 것은 복잡한 일이므로 대부분의 사용자는 단순히 기존 구현을 사용할 것
    * 인터페이스를 구현하는 대부분의 클래스가 그렇듯 보통 실제 구현 클래스는 최소 구현에서 요구하는 것 이상의 기능을 제공함

### 리액티브 라이브러리 RxJava 사용하기
* RxJava 2.0버전 사용
* RxJava는 자바로 리액티브 앱을 구현하는 데 사용하는 라이브러리
  * 넷플릭스의 Reactive Extensions(Rx) 프로젝트의 일부로 시작됨
    * 원래 마이크로소프트가 닷넷 환경에서 개발했던 프로젝트
  * Reactive Streams API와 java.util.concurrent.Flow를 지원하도록 RxJava 2.0 버전이 개발됨
* 자바에서 외부 라이브러리를 사용할 때는 import 문에서 그 사실이 두드러짐
  * 예를 들어 자바 플로 인터페이스를 포함하는 코드
    ``` import java.lang.concurrent.Flow.*; ```
  * Publisher의 구현인 Observable 같은 클래스를 이용시 import
    ``` import io.reactivex.Observable; ```
  * 이 시점에서 한 가지 중요한 아키텍처의 속성 강조
    * 좋은 시스템 아키텍처 스타일을 유지하려면 시스템에서 오직 일부에 사용된 개념의 세부 사항을 전체 시스템에서 볼 수 있지 않게 만들어야 함
    * 따라서 Observable의 추가 구조가 필요한 상황에서만 Observable을 사용하고 그렇지 않으면 Publisher의 인터페이스를 사용하는 것이 좋음
      * List 인터페이스에도 이 원칙이 적용되어 있음
      * 예를 들어 전달하는 값이 ArrayList라는 사실을 이미 알고 있지만,  
        이 값의 파라미터 형식을 List로 설정함으로 구현 세부사항을 밖으로 노출할지 않을 수 있음
      * 나중에 ArrayList를 LinkedList로 바꾸더라도 기존 코드를 크게 바꿀 필요가 없게 됨
* RxJava는 Flow.Publisher를 구현하는 두 클래스를 제공함
  * Flowable
    * 리액티브 당김 기반 역압력 기능이 있는 Flow를 포함하는 io.reactivex.Flowable 클래스
      * 역압력은 Publisher가 너무 빠른 속도로 데이터를 발행하면서 Subscriber가 이를 감당할 수 없는 상황에 이르는 것을 방지하는 기능
  * Observable
    * 역압력을 지원하지 않는 기존 버전의 RxJava에서 제공하던 Publisher io.reactivex.Observalbe 클래스
    * 이 클래스는 단순한 프로그램, 마우스 움직임 같은 사용자 인터페이스 이벤트에 더 적합함
      * 이들 이벤트 스트림에는 역압력을 적용하기 어렵기 때문
      * 마우스 움직임을 느리게 하거나 멈출 수 없기 때문
      * 이런 이유로 RxJava는 이벤트 스트림을 두 가지 구현 클래스로 제공함
* RxJava는 아래와 같은 상황에서 역압력을 적용하지 말 것을 권장
  * 천 개 이하의 요소를 가진 스트림
  * 마우스 움직임, 터치 이벤트 등 역압력을 적용하기 힘든 GUI 이벤트
  * 자주 발생하지 않는 종류의 이벤트
* Observable 인터페이스 사용법 설명
  * 모든 구독자는 구독 객체의 request(Long.MAX_VALUE) 메서드를 이용해 역압력 기능을 끌 수 있음
  * 물론 Subscriber가 정해진 시간 안에 수신한 모든 이벤트를 처리할 수 있다고 확신할 수 있는 상황이 아니라면 역압력 기능을 끄지 않는 것이 좋음

* Observable 만들고 사용하기
  * Observable, Flowable 클래스는 다양한 종류의 리액티브 스트림을 편리하게 만들 수 있도록 여러 팩토리 메서드를 제공함
    * 두 클래스는 Publisher를 구현하므로 팩토리 메서드는 리액티브 스트림을 만듦
  * 미리 정의한 몇 개의 요소를 이용해 간단한 Observable을 만들 수 있음
    ``` Observable<String> strings = Observable.just("first", "second"); ```
  * 여기서 just() 팩토리 메서드는 한 개 이상의 요소를 이용해 이를 방출하는 Observable로 변환함
    * Observable의 구독자는 onNext("first"), onNext("second"), onComplete()의 순서로 메시지를 받음
  * 사용자와 실시간으로 상호작용하면서 지정된 속도로 이벤트를 방출하는 상황에서 유용하게 사용할 수 있는 다른 Observable 팩토리 메서드도 있음
    ``` Observable<Long> onePerSec = Observable.interval(1, TimeUnit.SECONDS); ```
    * 팩토리 메서드 interval은 onePerSec라는 변수로 Observable을 반환해 할당함
      * 이 Observable은 0에서 시작해 1초 간격으로 long 형식의 값을 무한으로 증가시키며 값을 방출함
      * 이제 각 도시에서 매 초마다 온도 보고를 방출하는 다른 Observable을 onePerSec로 대신할 것
  * 최종 목표를 달성하기 전에 중간 과정으로 이들 온도를 매 초마다 출력할 수 있음
    * 그러러면 onePerSec에 가입해서 매 초마다 온도를 받고 이를 이용해 관심이 있는 도시의 온도를 출력해야 함
    * RxJava에서 Observable이 플로 API의 Publisher 역할을 하며 Observer는 Flow의 Subscriber 인터페이스 역할을 함
      * Observer 인터페이스와 Observable 클래스는 자바 9에서 제거됨
      * 새로운 코드는 플로 API를 사용해야 함
      * 이 변화에 맞춰 RxJava가 어떻게 진화할 것인지는 지켜봐야 함
    * RxJava Observer 인터페이스는 자바 9 Subscriber와 같은 메서드를 정의하며  
      onSubscribe 메서드가 Subscription 대신 Disposable 인수를 갖는다는 점만 다름
    * Observable은 역압력을 지원하지 않으므로 Subscription의 request 메서드를 포함하지 않음
    * Observer 인터페이스 코드
      ```
      public interface Observer<T> {
          void onSubscribe(Disposable d);
          void onNext(T t);
          void onError(Throwable t);
          void onComplete();
      }
      ```
    * 하지만 RxJava API는 자바 9 네이티브 플로 API보다 유연함
      * 많은 오버로드된 기능을 제공
      * 예를 들어 다른 세 메서드는 생략하고 onNext 메서드의 시그니처에 해당하는 람다 표현식을 전달해 Observable을 구독할 수 있음
      * 즉 이벤트를 수신하는 Consumer의 onNext 메서드만 구현하고  
        나머지 완료, 에러 처리 메서드는 아무것도 하지 않는 기본 동작을 가진 Observer를 만들어 Observable에 가입할 수 있음
      * 이 기능을 활용하면 Observable onPerSec에 가입하고 뉴욕에서 매 초마다 발생하는 온도를 출력하는 기능을 코드 한 줄로 구현 가능
        ``` onePerSec.subscribe(i -> System.out.println(TempInfo.fetch("New York")); ```
      * 위 코드에서 onePerSec Observable은 초당 한 개의 이벤트를 방출하며 메시지를 수신하면 Subscriber가 뉴욕의 온도를 추출해 출력함
      * 하지만 위 코드를 main 메서드에 추가해서 실제 실행해보면 아무것도 출력되지 않는데  
        이는 매 초마다 정보를 발행하는 Observable이 RxJava의 연산 스레드 풀, 즉 데몬 스레드에서 실행되기 때문
        * 공식 문서로는 이 현상을 이해하기 힘듦
      * main 프로그램은 실행하자마자 따로 실행할 코드가 없으므로 바로 종료됨
        * 그리고 프로그램이 종료되었으므로 어떤 결과를 출력하기도 전에 데몬 스레드도 종료되면서 이런 현상이 발생함
      * 위 코드 뒤에 스레드의 sleep 메서드를 추가해 프로그램 종료를 막는 방법도 있음
      * 현재 스레드(예제에서 메인 스레드)에서 콜백을 호출하는 blockingSubscribe 메서드를 사용하면 더 깔끔하게 문제 해결 가능
      * 이 예제에는 blockingSubscribe가 적합함
      * 하지만 실제로는 다음과 같이 blockingSubscribe를 사용할 것
        ```
        onePerSec.blockingSubscribe(
            i -> System.out.println(TempInfo.fetch("New York"))
        );
        ```
        * 결과
          ```
          TempInfo{town='New York', temp=85}
          TempInfo{town='New York', temp=19}
          io.reactivex.exceptions.OnErrorNotImplementedException: The exception was not handled due to missing onError handler in the subscribe() method call. Further reading: https://github.com/ReactiveX/RxJava/wiki/Error-Handling | java.lang.RuntimeException: Error
          	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
          	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:701)
          	at io.reactivex.internal.observers.LambdaObserver.onError(LambdaObserver.java:77)
          	at io.reactivex.internal.observers.LambdaObserver.onNext(LambdaObserver.java:67)
          	at io.reactivex.internal.util.NotificationLite.acceptFull(NotificationLite.java:298)
          	at io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe(ObservableBlockingSubscribe.java:65)
          	at io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe(ObservableBlockingSubscribe.java:103)
          	at io.reactivex.Observable.blockingSubscribe(Observable.java:5487)
          	at com.jaenyeong.chapter_17.RxJavaExample.Main.main(Main.java:16)
          Caused by: java.lang.RuntimeException: Error
          	at com.jaenyeong.chapter_17.RxJavaExample.TempInfo.fetch(TempInfo.java:20)
          	at com.jaenyeong.chapter_17.RxJavaExample.Main.lambda$main$0(Main.java:17)
          	at io.reactivex.internal.observers.LambdaObserver.onNext(LambdaObserver.java:63)
          	... 5 more
          Exception in thread "main" io.reactivex.exceptions.OnErrorNotImplementedException: The exception was not handled due to missing onError handler in the subscribe() method call. Further reading: https://github.com/ReactiveX/RxJava/wiki/Error-Handling | java.lang.RuntimeException: Error
          	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
          	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:701)
          	at io.reactivex.internal.observers.LambdaObserver.onError(LambdaObserver.java:77)
          	at io.reactivex.internal.observers.LambdaObserver.onNext(LambdaObserver.java:67)
          	at io.reactivex.internal.util.NotificationLite.acceptFull(NotificationLite.java:298)
          	at io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe(ObservableBlockingSubscribe.java:65)
          	at io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe(ObservableBlockingSubscribe.java:103)
          	at io.reactivex.Observable.blockingSubscribe(Observable.java:5487)
          	at com.jaenyeong.chapter_17.RxJavaExample.Main.main(Main.java:16)
          Caused by: java.lang.RuntimeException: Error
          	at com.jaenyeong.chapter_17.RxJavaExample.TempInfo.fetch(TempInfo.java:20)
          	at com.jaenyeong.chapter_17.RxJavaExample.Main.lambda$main$0(Main.java:17)
          	at io.reactivex.internal.observers.LambdaObserver.onNext(LambdaObserver.java:63)
          	... 5 more
          ```
          * 설계상 온도를 가져오는 기능이 임의로 실패하기 때문
          * 예제에서 구현한 Observer는 onError 같은 에러 관리 기능을 포함하지 않으므로 위와 같은 처리되지 않은 예외가 사용자에게 직접 보여지게 됨
  * 예제 난도 높임
    * 에러 처리만 추가하는 것이 목표가 아님
    * 이 기능을 일반화해야 함
    * 온도를 직접 출력하지 않고 사용자에게 팩토리 메서드를 제공해 매 초마다 온도를 방출하는 Observable을 반환할 것
    * 예제 코드
      ```
      public static Observable<TempInfo> getTemperature(String town) {
      		// Observer를 소비하는 함수로부터 Observable 만들기
      		return Observable.create(
      				// 매 초마다 무한으로 증가하는 일련의 long값을 방출하는 Observable
      				emitter -> Observable.interval(1, TimeUnit.SECONDS)
      						.subscribe(i -> {
      							// 소비된 옵저버가 아직 폐기되지 않았으면 어떤 작업을 수행 (이전 에러)
      							if (!emitter.isDisposed()) {
      								// 온도를 다섯 번 보고했으면 옵저버를 완료하고 스트림을 종료
      								if (i >= 5) {
      									emitter.onComplete();
      								} else {
      									try {
      										// 아니면 온도를 Observer로 보고
      										emitter.onNext(TempInfo.fetch(town));
      									} catch (Exception e) {
      										// 에러가 발생하면 Observer에 알림
      										emitter.onError(e);
      									}
      								}
      							}
      						}));
      }
      ```
      * 필요한 이벤트를 전송하는 ObservableEmitter를 소비하는 함수로 Observable을 만들어 반환
      * RxJava의 ObservableEmitter 인터페이스는 RxJava의 기본 Emitter(onSubscribe 메서드가 빠진 Observer와 같음)를 상속
        ```
        public interface Emitter<T> {
            void onNext(T t);
            void onError(Throwable t);
            void onComplete();
        }
        ```
        * Emitter는 새 Disposable을 설정하는 메서드와 시퀀스가 이미 다운스트림을 폐기했는지 확인하는 메서드 등을 제공
      * 내부적으로 매 초마다 증가하는 무한의 long값을 발행하는 onePerSec 같은 Observable을 구독
        * 우선 구독 함수 내에서는 ObservableEmitter 인터페이스(subscribe 메서드의 인수로 넘겨짐)에서 제공하는  
          isDisposed 메서드를 이용해 소비된 Observer가 이미 폐기되었는지 확인함 (이전 루프에서 에러가 발생했을 때 이런 상황이 벌어질 수 있음)
        * 온도를 이미 다섯 번 방출했으면 스트림을 종료하면서 Observer를 완료시킴
        * 그렇지 않으면 try/catch 블록안에서 요청된 도시의 최근 온도를 Observer로 보냄
        * 온도를 얻는 과정에서 에러가 발생하면 에러를 Observer로 전달함
      * getTemperature 메서드가 반환하는 Observable에 가입시킬 Observer를 쉽게 완성해서 전달된 온도를 출력할 수 있음
        ```
        public class TempObserver implements Observer<TempInfo> {
        	@Override
        	public void onSubscribe(Disposable d) {
        	}
        
        	@Override
        	public void onNext(TempInfo tempInfo) {
        		System.out.println(tempInfo);
        	}
        
        	@Override
        	public void onError(Throwable e) {
        		System.out.println("Got problem: " + e.getMessage());
        	}
        
        	@Override
        	public void onComplete() {
        		System.out.println("Done!");
        	}
        }
        ```
      * Observer는 TempSubscriber 클래스와 비슷하지만 더 단순함
        * RxJava의 Observable은 역압력을 지원하지 않으므로 전달된 요소를 처리한 다음 추가 요소를 요청하는 request() 메서드가 필요 없기 때문
      * Main 클래스
        * getTemperature 메서드가 반환하는 Observable에 Observer를 구독
        ```
        public class Main {
        
        	public static void main(String[] args) {
                // 매 초마다 뉴욕의 온도 보고를 방출하는 Observable 만들기
                Observable<TempInfo> observable = getTemperature("New York");
                // 단순 Observer로 이 Observable에 가입해서 온도 출력하기
                observable.subscribe(new TempObserver());
        	}
        }
        ```
        * 결과
          * 다섯 번 온도를 출력하는 동안 에러가 발생하지 않고 onComplete 신호가 전송됨
          ```
          TempInfo{town='New York', temp=60}
          TempInfo{town='New York', temp=1}
          TempInfo{town='New York', temp=83}
          TempInfo{town='New York', temp=47}
          TempInfo{town='New York', temp=58}
          Done!
          ```

* Observable을 변환하고 합치기
  * RxJava나 기타 리액티브 라이브러리는 자바 9 플로 API에 비해 스트림을 합치고, 만들고, 거르는 등의 풍부한 도구상자를 제공하는 것이 장점
    * 한 스트림을 다른 스트림의 입력으로 사용할 수 있음
    * 이 외에도 스트림에서 관심있는 요소만 거른 다른 스트림을 만들거나 매핑 함수로 요소를 변환하거나 두 스트림을 다양한 방법으로 합치는 등의 작업 가능
      * Flow.Processor만으로는 달성하기 어려운 일
    * 이런 변환, 합치기 함수는 상당히 복잡하여 설명이 어려움
      * 예를 들어 RxJava의 mergeDelayError 함수 관련 설명
        * Observable을 한 Observable로 방출하는 Observable을 평면화해서  
          모든 Observable 소스에서 성공적으로 방출된 모든 항목을 Observer가 수신할 수 있도록 함  
          이때 이들 중 에러 알림이 발생해도 방해받지 않으며 이들 Observable에 동시 구독할 수 있는 한계가 있음
      * 위 설명은 이해하기 어려움
      * 리액티브 스트림 커뮤니티는 마블 다이어그램이라는 시각적 방법을 이용해 이런 어려움을 해결하고자 노력함
        * 마블 다이어그램(marble diagram)은 수평선으로 표시된 리액티브 스트림에 임의의 순서로 구성된 요소가 기하학적 모형이 나타남
        * 특수 기호는 에러나 완료 신호를 나타냄
        * 박스는 해당 연산이 요소를 어떻게 변환하거나 여러 스트림을 어떻게 합치는지 보여줌
  * 구현한 메서드를 일반화해서 사용자가 한 도시뿐 아니라 여러 도시에서 온도를 방출하는 Observable을 가질 수 있도록 해야 한다고 가정
    * 메서드를 각 도시에 호출한 다음 이들 호출로부터 얻은 모든 Observable을 merge 함수를 이용해 하나로 합쳐서 마지막 요구사항을 구현함
      ```
      public static Observable<TempInfo> getCelsiusTemperatures(String... towns) {
          return Observable.merge(Arrays.stream(towns)
                  .map(TempObservable::getCelsiusTemperature)
                  .collect(toList()));
      }
      ```
      * 이 메서드는 온드를 얻으려는 도시 집합을 포함하는 가변 인수를 받음
      * 이 가변 인수를 문자열 스트림으로 변환한 다음 각 문자열을 getCelsiusTemperature 메서드로 전달
      * 이런식으로 각 도시는 매 초마다 도시의 온도를 방출하는 Observable로 변신
      * 마지막으로 Observable의 스트림은 리스트로 모아지며  
        다시 리스트는 Observable 클래스가 제공하는 정적 팩토리 메서드 merge로 전달됨
      * 이 메서드는 Observable의 Iterable을 인수로 받아 마치 한 개의 Observable처럼 동작하도록 결과를 합침
      * 그 결과 Observable은 전달된 Iterable에 포함된 모든 Observable의 이벤트 발행물을 시간 순서대로 방출함
      * main 에 적용
        ```
        public static void main(String[] args) {
            Observable<TempInfo> observable = getCelsiusTemperatures("New York", "Chicago", "San Francisco");
            observable.blockingSubscribe(new TempObserver());
        }
        ```
        * 결과
          ```
          TempInfo{town='San Francisco', temp=32}
          TempInfo{town='New York', temp=26}
          TempInfo{town='Chicago', temp=-4}
          TempInfo{town='San Francisco', temp=-15}
          TempInfo{town='New York', temp=19}
          TempInfo{town='Chicago', temp=5}
          TempInfo{town='New York', temp=-3}
          TempInfo{town='Chicago', temp=-12}
          TempInfo{town='San Francisco', temp=18}
          TempInfo{town='San Francisco', temp=1}
          TempInfo{town='New York', temp=8}
          Got problem: Error
          ```
          * 각 도시에서 온도를 가져오는 동작을 수행하다 에러가 발생해 데이터 스트림이 동작을 멈추기 전까지 main 메서드는 매 초마다 도시의 온도를 출력함
  * RxJava의 완벽한 가이드를 제공하려면 책 한 권이 따로 필요

### 정리
* 리액티브 프로그래밍의 기초 사상은 이미 20 ~ 30년 전에 수립되었지만 데이터 처리량과 사용자 기대치 덕분에 최근에서야 인기를 얻고 있음
* 리액티브 소프트웨어가 지녀야 할 네 가지 관련 특징을 서술하는 리액티브 매니페스토가 리액티브 프로그래밍 사상을 공식화 함
  * 반응성, 회복성, 탄력성, 메시지 주도
* 여러 앱을 통합하는 리액티브 시스템과 한 개의 앱을 구현할 때에 각각 다른 접근 방식으로 리액티브 프로그래밍 원칙을 적용할 수 있음
* 리액티브 앱은 리액티브 스트림이 전달하는 한 개 이상의 이벤트를 비동기로 처리함을 기본으로 전제함
  * 리액티브 앱 개발에서 리액티브 스트림의 역할이 핵심적이므로 넷플릭스, 피보탈, 라이트벤드, 레드햇 등은  
    다양한 구현의 상호운용성을 극대화할 수 있도록 컨소시엄을 구성해 개념을 표준화함
* 리액티브 스트림은 비동기적으로 처리되므로 역압력 기법이 기본적으로 탑재되어 있음
  * 역압력은 발행자가 구독자보다 빠른 속도로 아이템을 발행하므로 발생하는 문제를 방지함
* 설계와 표준화 절차 결과가 자바에 반영됨
  * 자바 9 플로 API는 Publisher, Subscriber, Subscription, Processor 네 개의 핵심 인터페이스를 정의함
* 대부분의 상황에서는 이들 인터페이스를 직접 구현할 필요가 없으며 실제 이들 인터페이스는 리액티브 패러다임을 구현하는 다양한 라이브러리의 공용어 역할을 함
* 가장 흔한 리액티브 프로그래밍 도구로 RxJava를 꼽을 수 있음
  * 이 라이브러리는 자바 9 플로 API의 기본 기능에 더해 다양한 강력한 연산자를 제공함
  * 예를 들어 한 스트림에서 방출한 요소를 변환하거나 거를 수 있으며 여러 스트림의 데이터를 일부 합치거나 전체를 모을 수 있음

### [quiz]
* 예제에서 개발한 코드에 있는 작은 문제 맞추기
  * TempInfo 팩토리 메서드 내에서 에러를 임의로 발생시키는 코드 때문에 감춰진 상태
  * 임의의 에러를 발생시키는 코드를 없앤 다음 main을 오래 실행하면 발생하는 문제
  * 정답
    * TempSubscriber가 새로운 요소를 onNext 메서드로 받을 때마다 TempSubscription으로 새 요청을 보내면  
      request 메서드가 TempSubscriber 자신에게 또 다른 요소를 보내는 문제가 있음
    * 이런 재귀 호출은 스택이 오버플로우 될때까지 반복해서 일어남
      ```
      Exception in thread "main" java.lang.StackOverflowError
      	at java.base/sun.nio.cs.UTF_8$Encoder.encodeLoop(UTF_8.java:564)
      	at java.base/java.nio.charset.CharsetEncoder.encode(CharsetEncoder.java:576)
      	at java.base/sun.nio.cs.StreamEncoder.implWrite(StreamEncoder.java:292)
      	at java.base/sun.nio.cs.StreamEncoder.implWrite(StreamEncoder.java:281)
      	at java.base/sun.nio.cs.StreamEncoder.write(StreamEncoder.java:125)
      	at java.base/java.io.OutputStreamWriter.write(OutputStreamWriter.java:211)
      	at java.base/java.io.BufferedWriter.flushBuffer(BufferedWriter.java:120)
      	at java.base/java.io.PrintStream.write(PrintStream.java:605)
      	at java.base/java.io.PrintStream.print(PrintStream.java:745)
      	at java.base/java.io.PrintStream.println(PrintStream.java:899)
      ```

* 영하 온도만 거르기
  * Observable 클래스의 filter 메서드는 Predicate를 인수로 받아 Predicate의 조건을 만족하는 요소만 방출하는 두 번째 Observable을 만듦
  * 등록된 도시의 온도가 섭씨 0도 이하일 때만 온도를 방출하도록 observable을 만들 수 있을까
  ```
  public static Observable<TempInfo> getNegativeTemperature(String town) {
      return getCelsiusTemperature(town)
              .filter(temp -> temp.getTemp() < 0);
  }
  ```
---

## chapter 18 - 함수형 관점으로 생각하기
* 일급 함수와 관련 있으며 때로는 객체 변화를 제한하는 것이 함수형과 밀접한 관련을 가짐

### 시스템 구현과 유지보수
* 누군가 우리에게 이미 구현되어 있지만 실제 경험해본 적은 없는 대규모 소프트웨어 시스템 업그레이드 관리를 요청했다고 가정
  * 노련한 자바 개발자들 사이에서는
  * '먼저 프로그램 내에 synchronized라는 키워드가 발견된다면 제안을 거절하라'
  * 'synchronized가 없다면 시스템의 구조를 자세히 검토 해보라'
  * 라는 풍문이 떠돌고 있음
  * 동시성 버그를 고치는 일은 매우 어렵기 때문
* 자바 8의 스트림을 이용하면 잠금(locking) 문제를 신경 쓰지 않을 수 있었음
  * 단 자바 8의 스트림을 이용하려면 상태 없는 동작이어야 한다는 조건을 만족해야 함
    * 즉 스트림 처리 파이프라인의 함수는 다른 누군가가 변수의 값을 바꿀 수 있는 상태에 있는 변수를 사용하지 않음
* 쉽게 유지보수할 수 있으려면 프로그램이 시스템의 구조를 이해하기 쉽게 클래스 계층으로 반영한다면 좋을 것
  * 시스템의 각 부분의 상호 의존성을 가리키는 결합성(coupling)
  * 시스템의 다양한 부분이 서로 어떤 관계를 갖는지 가리키는 응집성(cohesion)
  * 이라는 소프트웨어 엔지니어링 도구로 프로그램 구조를 평가할 수 있음
* 그러나 실질적으로 많은 프로그래머가 유지보수 중 코드를 크래시 디버깅 문제를 가장 많이 겪게 됨
  * 코드 크래시는 예상하지 못한 변숫값 때문에 발생 가능
    * 레거시 코드 활용 전략 (마이클 페더스) 참조
  * 함수형 프로그래밍이 제공하는 부작용 없음(no side effect), 불변성(immutability)라는 개념이 이 문제를 해결하는데 도움을 줌

* 공유된 가변 데이터
  * 변수가 예상하지 못한 값을 갖는 이유는 결국 우리가 유지보수하는 시스템의 여러 메서드에서 공유된 가변 데이터 구조를 읽고 갱신하기 때문
  * 리스트를 참조하는 여러 클래스가 있다고 가정
    * 리스트의 소유자는 어느 클래스일까?
    * 이들 중 하나의 클래스가 리스트를 갱신하면 어떻게 될까?
    * 다른 클래스는 리스트가 갱신되었다는 사실을 알고 있을까?
    * 다른 클래스에 리스트가 갱신되었음을 어떻게 알려줄 수 있을까?
    * 리스트 갱신 사실을 알리는 것이 좋을까 아니면 안전하게 리스트 사본을 만드는 것이 나을까?
    * 이처럼 공유 가변 데이터 구조를 사용하면 프로그램 전체에서 데이터 갱신 사실을 추적하기 어려움
  * 어떤 자료구조도 바꾸지 않는 시스템이 있다고 가정
    * 자신을 포함하는 클래스의 상태 그리고 다른 객체의 상태를 바꾸지 않으며 return 문을 통해서만 자신의 결과를 반환하는 메서드를  
      순수(pure) 메서드 또는 부작용 없는(side-effect free) 메서드라 부름
  * 함수 내에 포함되지 못한 기능을 부작용이라고 함
    * 부작용 예시
      * 자료구조를 고치거나 필드에 값을 할당(setter 메서드 같은 생성자 이외의 초기화 동작)
      * 예외 발생
      * 파일에 쓰기 등의 I/O 동작 수행
  * 불변 객체를 이용해 부작용을 없애는 방법
    * 불변 객체는 인스턴스화한 다음에는 객체의 상태를 바꿀 수 없는 객체이므로 함수 동작에 영향을 받지 않음
    * 즉 인스턴스화한 불변 객체의 상태는 결코 예상하지 못한 상태로 바뀌지 않음
    * 따라서 불변 객체는 복사하지 않고 공유할 수 있으며, 객체의 상태를 바꿀 수 없으므로 스레드 안정성을 제공함
  * 실제 시스템에서 부작용을 없앤다는 것이 현실적으로 가능한지 의아함
    * 부작용 없는 시스템 컴포넌트에서는 메서드가 서로 간섭하는 일이 없으므로 잠금을 사용하지 않고도 멀티코어 병렬성을 사용할 수 있음
    * 또한 프로그램의 어떤 부분이 독립적인지 바로 이해할 수 있음
  * 부작용 없는 시스템의 개념은 함수형 프로그래밍에서 유례됨

* 선언형 프로그래밍
  * declarative programming
    * 함수형 프로그래밍의 기반을 이루는 개념
  * 프로그램을 시스템으로 구현하는 방식은 크게 두 가지로 구분 가능
    * '어떻게'
      * '이 일을 먼저하고 그 다음에 저 값을 갱신하고, 그 다음에..' 처럼 작업을 어떻게 수행할 것인지에 집중하는 방법
      * 예를 들어 리스트에서 가장 비싼 트랜잭션을 계산할 때 다음과 같은 일련의 명령을 실행
        * '리스트에서 트랜잭션을 가져와서 가장 비싼 트랜잭션과 비교한다'
        * '가져온 트랜잭션이 가장 비싼 트랜잭션보다 비싸다면 가져온 트랜잭션이 가장 비싼 트랜잭션이 된다'
        * '리스트의 다음 트랜잭션으로 지금까지의 과정을 반복함'
      * 이처럼 '어떻게'에 집중하는 프로그래밍 형식은 고전의 객체지향 프로그래밍에서 이용하는 방식
      * 때로는 이를 명령형 프로그래밍이라고 부르기도 함
        * 명령어(할당, 조건문, 분기문, 루프 등)가 컴퓨터의 저수준 언어와 비슷하게 생겼기 때문
        ```
        Transaction mostExpensive = transactions.get(0);
        if (mostExpensive == null)
            throw new IllegalArgumentException("Empty list of transactions");
        
        for (Transaction t : transactions.subList(1, transactions.size())) {
            if (t.getValue() > mostExpensive.getValue()) {
                mostExpensive = t;
            }
        }
        ```
    * '무엇을'
      * '어떻게'가 아닌 '무엇을'에 집중하는 방식
      * 스트림 API로 다음과 같은 질의 생성 가능
        ```
        Optional<Transaction> mostExpensive = 
            transactions.stream()
                        .max(comparing(Transaction::getValue));
        ```
        * 질의문 구현 방법은 라이브러리가 결정함
        * 이와 같은 구현 방식을 내부 반복(internal iteration)이라고 함
        * 질의문 자체로 문제를 어떻게 푸는지 명확하게 보여준다는 것이 내부 반복 프로그래밍의 큰 장점
  * '어떻게'로 접근하는 방식을 선언형 프로그래밍이라고 부르기도 함
    * 선언형 프로그래밍에서는 우리가 원하는 것이 무엇이고 시스템이 어떻게 그 목표를 달성할 것인지 등의 규칙을 정함
    * 문제 자체가 코드로 명확하게 드러난다는 점이 선언형 프로그래밍의 강점

* 왜 함수형 프로그래밍인가?
  * 함수형 프로그래밍은 선언형 프로그래밍을 따르는 대표적인 방식이며, 부작용이 없는 계산을 지향함
    * 선언형 프로그래밍과 부작용을 멀리한다는 두 가지 개념은 좀 더 쉽게 시스템을 구현하고 유지보수하는 데 도움을 줌
  * 람다 표현식처럼 작업을 조합하거나 동작을 전달하는 등의 언어 기능은 선언형을 활용해서 자연스럽게 읽고 쓸 수 있는 코드를 구현하는데 많은 도움을 줌
    * 스트림으로는 여러 연산을 연결해서 복잡한 질의를 표현할 수 있음
    * 이러한 기능은 함수형 프로그래밍의 특징을 고스란히 보여줌
  * 함수형 프로그래밍을 이용하면 부작용이 없는 복잡하고 어려운 기능을 수행하는 프로그램을 구현할 수 있음

### 함수형 프로그래밍이란 무엇인가?
* '함수형 프로그래밍이란 무엇인가'라는 질문에 '함수를 이용하는 프로그래밍이다'라고 간단히 답할 수 있음
  * '그럼 함수란 무엇인가'
* int와 double을 인수로 받아 double을 반환하는 메서드가 있음
  * 이 메서드는 자신이 호출된 횟수로 가변 변수를 갱신하는 부작용을 포함하고 있음
* 함수형 프로그래밍에서 함수란 수학적인 함수와 같음
  * 즉 함수는 0개 이상의 인수를 가지며, 한 개 이상의 결과를 반환하지만 부작용이 없어야 함
  * 함수는 여러 입력을 받아서 여러 출력을 생성하는 블랙박스와 같음
  * 자바와 같은 언어에서는 수학적인 함수냐 아니냐가 메서드와 함수를 구분하는 핵심
    * log, sin 등의 수학적 함수는 절대 부작용을 포함하지 않을 것
  * 특히 인수가 같다면 수학적 함수를 반복적으로 호출했을 때 항상 같은 결과가 반환됨
    * 하지만 Random.nextInt 같은 메서드에는 이런 규칙이 적용되지 않음
* 함수형이라는 말은 '수학의 함수처럼 부작용이 없음'을 의미
  * 결과적으로 함수형 프로그래밍은 지금까지의 프로그래밍과는 사뭇 다른 모습일 것
  * 함수형 프로그래밍에서는 함수 그리고 if-then-else 등의 수학적 표현만 사용해야 하는가?
  * 시스템의 다른 부분에 영향을 미치지 않는다면 내부적으로는 함수형이 아닌 기능도 사용할 수 있는가?
    * 즉 내부적으로는 부작용이 발생하지만 호출자가 이를 알아차리지 못한다면 실제로 부작용이 발생한 것이라고 말할 수 있는가?
    * 호출자에 아무 영향을 미치지 않는다면 호출자는 내부적인 부작용을 파악하거나 신경쓸 필요가 없음
* 결론적으로 '함수 그리고 if-then-else 등의 수학적 표현만 사용'하는 방식을 순수 함수형 프로그래밍이라고 함
  * '시스템의 다른 부분에 영향을 미치지 않는다면 내부적으로는 함수형이 아닌 기능도 사용'하는 방식을 함수형 프로그래밍이라고 함

* 함수형 자바
  * 실질적으로 자바로는 완벽한 순수 함수형 프로그래밍을 구현하기 어려움
    * 예를 들어 자바의 I/O 모델 자체에는 부작용 메서드가 포함됨
      * Scanner.nextLine을 호출하면 파일의 행을 소비함
        * Scanner.nextLine을 두 번 호출하면 다른 결과과 반환될 가능성이 존재
    * 하지만 시스템의 컴포넌트가 순수한 함수형인 것처럼 동작하도록 코드를 구현할 수 있음
    * 자바에서는 순수 함수형이 아니라 함수형 프로그램을 구현할 것
    * 실제 부작용이 있지만 아무도 이를 보지 못하게 함으로써 함수형을 달성할 수 있음
      * 부작용을 일으키지 않는 어떤 함수, 메서드가 있는데 다만 진입할 때 어떤 필드의 값을 증가시켰다가 빠져나올 때 필드의 값을 돌려 놓는다고 가정
        * 단일 스레드로 실행되는 프로그램의 입장에선느 이 메서드가 아무 부작용을 일으키지 않으므로 이 메서드는 함수형이라 간주할 수 있음
        * 하지만 다른 스레드가 필드의 값을 확인한다든가 아니면 동시에 이 메서드를 호출하는 상황이 발생할 수 있다면 이 메서드는 함수형이 아님
        * 메서드의 바디를 잠금(lock)으로써 이 문제를 해결할 수 있음
          * 따라서 이 메서드는 함수형이라 할 수 있음
          * 하지만 이런 식으로 문제를 해결하면 멀티코어 프로세서의 두 코어를 활용해 메서드를 병렬로 호출할 수 없게 됨
          * 결국 프로그램 입장에서 부작용이 사라졌지만 프로그래머 관점에서는 프로그램의 실행 속도가 느려지게 된 것
  * 함수나 메서드는 지역 변수만을 변경해야 함수형이라 할 수 있음
    * 그리고 함수나 메서드에서 참조하는 객체가 있다면 그 객체는 불변 객체여야 함
      * 즉 객체의 모든 필드가 final이어야 하고 모든 참조 필드는 불변 객체를 직접 참조해야 함
    * 예외적으로 메서드 내에서 생성한 객체의 필드는 갱신할 수 있다는 사실
      * 단 새로 생성한 객체의 필드 갱신이 외부에 노출되지 않아야 하고 다음에 메서드를 다시 호출한 결과에 영향을 미치지 않아야 함
  * 이 외에 함수형이라고 말할 수 있는 조건
    * 함수형이라면 함수나 메서드가 어떤 예외도 일으키지 않아야 함
      * 예외가 발생하면 블랙박스 모델에 return으로 결과를 반환할 수 없게 될 수 있기 때문
      * 이러한 제약은 함수형을 수학적으로 활용하는 데 큰 걸림돌이 됨
      * 수학적 함수(mathematical function)는 주어진 인수값에 대응하는 하나의 결과를 반환함
      * 실제 대부분의 수학 연산은 부분 함수(partial function)로 활용됨
      * 어떤 입력값이 있을 때 이는 정확하게 하나의 결과로 도출됨
      * 하지만 입력값이 undefined라면 결과가 아예 나오지 않음
        * 예를 들어 9으로 어떤 수를 나눈다든가 sqrt의 인수가 음수인 상황 등이 이에 해당함
      * 이처럼 비정상적인 입력값이 있을 때 자바에서는 예외를 일으키는 것이 자연스러운 방식일 것
      * 치명적인 에러가 있을 때 처리되지 않은 예외를 발생시키는 것은 괜찮지만 예외를 처리하는 과정에서 함수형에 위배되는 제어 흐름이 발생한다면  
        결국 '인수를 전달해서 결과를 받는다'는 블랙박스의 단순한 모델이 깨진다는 의견이 있음
      * 예외를 사용하지 않고 나눗셈 같은 함수를 표현
        * Optional\<T>를 사용하여 이 문제 해결
        * ``` double sqrt(double) ``` 대신 ``` Optional<Double> sqrt(double) ```을 이용하면  
          예외 없이도 결과적으로 연산을 성공적으로 수행했는지 아니면 성공적으로 수행하지 못했는지 확인할 수 있음
        * 상당히 귀찮은 작업처럼 보임
          * 하지만 모든 코드가 Optional을 사용하도록 반드시 고쳐야 하는 것은 아님
          * 함수형 프로그래밍과 순수 함수형 프로그래밍의 장단점을 실용적으로 고려해  
            다른 컴포넌트에 영향을 미치지 않도록 지역적으로만 예외를 사용하는 방법도 있음
    * 함수형에서는 비함수형 동작을 감출 수 있는 상황에서만 부작용을 포함하는 라이브러리 함수를 사용해야 함
      * 즉 먼저 자료구조를 복사한다든가 발생할 수 있는 예외를 적절하게 내부적으로 처리함으로써 자료구조의 변경을 호출자가 알 수 없도록 감춰야 함
      * 이와 같은 설명을 주석으로 표현하거나 마커 애노테이션(maker annotation)으로 메서드를 정의할 수 있음
      * 마커 애노테이션을 사용했다면 Stream.map 같은 병렬 스트림 처리 연산에 전달할 때 이와 같은 제약이 있는지 쉽게 확인할 수 있음
  * 함수형 코드에서는 일종의 로그 파일로 디버깅 정보를 출력하도록 구현하는 것이 좋음
    * 물론 이처럼 디버깅 정보를 출력하는 것은 함수형 규칙에 위배되지만 로그 출력을 제외하고는 함수형 프로그래밍의 장점을 문제없이 누릴 수 있음

* 참조 투명성
  * '부작용을 감춰야 한다'라는 제약은 참조 투명성(referential transparency) 개념으로 귀결
    * 즉 같은 인수로 함수를 호출했을 때 항상 같은 결과를 반복한다면 참조적으로 투명한 함수라고 표현함
    * 예를 들어 ``` "raoul".replace('r', 'R') ```이라는 코드는 항상 같은 결과가 나옴
      * 따라서 String.replace는 참조적으로 투명함
      * String.replace는 this 객체를 갱신하는 것이 아니라 대문자 'R'을 소문자 'r'로 바꿔서 새로운 String을 반환
    * 다시 말해, 함수는 어떤 입력이 주어졌을 때 언제, 어디서 호출하든 같은 결과를 생성해야 함
      * 따라서 Random.nextInt는 함수형이 될 수 없음
      * 마찬가지로 자바의 Scanner 객체로 사용자의 키보드 입력을 받는다면 참조 투명성을 위배함
      * nextLine 메서드를 호출했을 때 매번 다른 결과가 나올 수 있기 때문
      * 하지만 두 개의 final int 변수를 더하는 연산에서는 두 변수를 바꿀 수 없으므로 이 연산은 항상 같은 결과를 생성함
  * 참조 투명성은 프로그램 이해에 큰 도움을 줌
    * 또한 참조 투명성은 비싸거나 오랜 시간이 걸리는 연산을 기억화(memorization) 또는 캐싱(caching)을 통해  
      다시 계산하지 않고 저장하는 최적화 기능도 제공함
  * 자바에는 참조 투명성과 관련한 작은 문제가 있음
    * List를 반환하는 메서드를 두 번 호출한다고 가정
      * 두 번의 호출 결과로 같은 요소를 포함하지만 서로 다른 메모리 공간에 생성된 리스트를 참조할 것
      * 결과 리스트가 가변 객체라면 리스트를 반환하는 메서드는 참조적으로 투명한 메서드가 아니라는 결론
        * 반환된 두 리스트가 같은 객체라 할 수 없으므로
      * 결과 리스트를 (불변의)순수값으로 사용할 것이라면 두 리스트가 같은 객체라고 볼 수 있어 리스트 생성 함수는 참조적으로 투명한 것으로 간주할 수 있음
      * 일반적으로 함수형 코드에서는 이런 함수를 참조적으로 투명한 것으로 간주함

* 객체 지향 프로그래밍과 함수형 프로그래밍
  * 함수형 프로그래밍과 기존의 익스트림 객체지향 프로그래밍 비교
    * 사실 자바 8은 함수형 프로그래밍을 익스트림 객체지향 프로그래밍의 일종으로 간주함
    * 대부분의 자바 프로그래머는 무의식적으로 함수형 프로그래머의 일부 기능과 익스트림 객체지향 프로그래밍의 일부 기능을 사용하게 될 것
    * (멀티코어 등의) 하드웨어 변경과 (DB 질의와 비슷한 방식으로 데이터를 조작하는 등의) 프로그래머의 기대치 때문에  
      결국 자바 소프트웨어 엔지니어의 프로그래밍 형식이 좀 더 함수형으로 다가갈 것
  * 프로그래밍 형식을 스펙트럼으로 표현하자면
    * 스펙트럼의 한 쪽 끝에는 모든 것을 객체로 간주하고 프로그램이 객체의 필드를 갱신하고,  
      메서드를 호출하고, 관련 객체를 갱신하는 방식으로 동작하는 익스트림 객체지향 방식이 위치
    * 반대쪽 끝에는 참조적 투명성을 중시하는, 즉 변화를 허용하지 않는 함수형 프로그래밍 형식이 위치
    * 실제로 자바 프로그래머는 이 두가지 프로그래밍 형식을 혼합함
    * 예를 들어 Iterator로 가변 내부 상태를 포함하는 자료구조를 탐색하면서 함수형 방식으로 자료구조에 들어 있는 값의 합계를 계산할 수 있음
      * 자바에서는 지역 변수 변화가 수반될 수 있음
    * 모듈성이 좋고 멀티코어 프로세서에 적합한 프로그램을 구현하는 데 도움을 주는 함수형 프로그래밍의 기능

* 함수형 실전 연습
  * 간단한 예제
    * {1, 4, 9}처럼 List\<Integer>가 주어졌을 때  
      이것의 모든 서브집합의 멤버로 구성된 List<List\<Integer>>를 만드는 프로그램을 만든다고 가정
    * 예를 들어 ``` {1, 4, 9} ```의 서브집합은  
      ``` {1, 4, 9}, {1, 4}, {1, 9}, {4, 9}, {1}, {4}, {9}, {} ```
      * 빈 집합 {}을 포함해 총 8개의 서브집합이 존재함
      * 각 서브집합은 List\<Integer> 형식으로 이루어져 있으므로 최종 정답의 형식은 List<List\<Integer>>
      * 보통 '{1, 4, 9}의 서브집합 중 1을 포함하는 집합과 아닌 집합'으로 구분해서 문제를 풀려고 시도할 것
        * 일부는 다른 방법인 이진수 표현을 이용하는 기법을 사용할 것
        * 000, 001, 010, 011, 100, 101, 110, 111 이라는 이진 표현을 사용할 수 있음
        * 리스트의 모든 순열(permutation)을 계산함
        * 예를 들어 {1, 4, 9}에는 총 6개의 순열이 존재
      * 1을 포함하지 않는 부분집합은 {4, 9}의 부분집합이고, 1을 포함하는 부분집합은 {4, 9}의 모든 부분집합에 1을 포함시켜 얻을 수 있음
      * 이와 같은 접근 방식은 가장 쉽고, 자연스러운 함수형 자바 프로그래밍
        * 가장 많이 하는 실수는 빈 리스트는 부분집합이 없다고 착각하는 것
        * 편의상 예제에서는 List\<Integer>를 사용했지만 실제로는 List\<T>를 사용한 다음  
          List\<String>, List\<Integer>를 고쳐진 subsets 메서드에 사용할 수 있음
        * subsets 메서드
          ```
          static List<List<Integer>> subsets(List<Integer> list) {
              // 입력 리스트가 비어있다면 빈 리스트 자신이 서브집합
              if (list.isEmpty()) {
                  List<List<Integer>> ans = new ArrayList<>();
                  ans.add(Collections.emptyList());
                  return ans;
              }
              Integer first = list.get(0);
              List<Integer> rest = list.subList(1, list.size());
      
              // 빈 리스트가 아니면 먼저 하나의 요소를 꺼내고
              // 나머지 요소의 모든 서브집합을 찾아서 subans로 전달
              // subans는 절반의 정답을 포함
              List<List<Integer>> subans = subsets(rest);
              // 정답의 나머지 절반을 포함하는 subans2는 subans의 모든 리스트에 처음 꺼낸 요소를 앞에 추가해서 만듦
              List<List<Integer>> subans2 = insertAll(first, subans);
      
              // subans, subans2를 연결하면 정답 완성
              return concat(subans, subans2);
          }
          ```
        * subsets 자체도 기존의 구조를 갱신하지 않으므로 함수형
          * 수학적 귀납법(induction)의 인수처럼 처리됨
        * insertAll 메서드
          * insertAll의 인수를 변환하도록, 즉 subans의 모든 요소가 first를 포함하도록 인수를 변환했다고 가정
          * 결국 subans가 의도하지 않은 값을 포함하면서 이상한 결과가 나옴
          * 따라서 다음처럼 정의
            ```
            static <T> List<List<T>> insertAll(T first, List<List<T>> lists) {
                List<List<T>> result = new ArrayList<>();
        
                for (List<T> l : lists) {
                    // 리스트를 복사한 다음에 복사한 리스트에 요소를 추가
                    // 구조체가 가변이더라도 저수준 구조를 복사하진 않음
                    // Integer는 가변이 아님
                    List<T> copyList = new ArrayList<>();
                    copyList.add(first);
                    copyList.addAll(l);
                    result.add(copyList);
                }
        
                return result;
            }
            ```
          * subans의 모든 요소를 포함하는 새로운 List를 만듦
          * 여기서는 Integer 객체가 불변이라는 사실을 이용
            * 불변이 아니라면 각 요소를 모두 복제해야 함
          * insertAll을 함수형으로 만들어야 하므로 이처럼 호출자가 아니라 insertAll 내부에 리스트를 복사하는 코드를 추가하는 것이 자연스러움
        * concat 메서드
          * 간단하게 concat을 구현한 코드이므로 실무에서 사용하지 않도록 주의
          ```
          static <T> List<List<T>> concat(List<List<T>> a, List<List<T>> b) {
              a.addAll(b);
              return a;
          }
          
          static <T> List<List<T>> concat(List<List<T>> a, List<List<T>> b) {
              List<List<T>> r = new ArrayList<>(a);
              r.addAll(b);
              return r;
          }
          ```
          * 두 번째 버전 좋은 코드
            * 두 번째 버전의 concat은 순수 함수
            * 내부적으로는 리스트 r에 요소를 추가하는 변화가 발생하지만 반환 결과는 오로지 인수에 의해 이뤄지며 인수의 정보는 변경하지 않음
            * 이와 달리 첫 번재 버전은 concat(subans, subans2)를 호출한 다음에 subans의 값을 다시 참조하지 않는다는 가정을 함
            * 실제로 예제에서는 subans의 값을 참좌지 않으므로 더 가벼운 버전의 concat을 사용하는 것이 바람직
            * 각자 상황에 따라 복제를 하는 것이 좋을지, 잠재적인 버그를 찾기 위해 시간을 소비하는 것이 비용이 적을지 고려해서 어떤 방식을 선택할지 결정
          * '메서드의 첫 번째 인수를 임의의 값으로 바꿀 수 있는 상황에서만 메서드를 사용해야 하며 subsets를 고칠 때는 이 사실을 고려해야 함'
            * 이라는 자세한 설명을 주석으로 추가했어도 여전히 누군가가 코드를 사용할 때는 이런 사실을 고려하기보다는  
              애초부터 끔찍한 디버깅 문제가 발생할 가능성이 없도록 구현된 코드를 선호할 것
      * 인수에 의해 출력이 결정되는 함수형 메서드의 관점에서 프로그램 문제를 생각할 것
        * 무엇을 해야 하는가에 중점을 둠
        * 이와 같은 방식으로 문제를 접근하면 설계 단계에서 어떻게 문제를 해결할 것이고,  
          무엇을 변화할 것인지 결정하는 기존 방식에 비해 더 생산적일 때가 많음
          * 설계 단계는 이와 같은 결정을 내리기에 너무 이른 상황이기 때문
        * 재귀는 무엇을 해야 하는가에 집중할 수 있도록 도움을 주는 함수형 프로그래밍의 한 기법

### 재귀와 반복
* 순수 함수형 프로그래밍 언어에서는 while, for 같은 반복문을 포함하지 않음
  * 이러한 반복문 때문에 변화가 자연스럽게 코드에 스며들 수 있기 때문
  * 예를 들어 while 루프의 조건문을 갱신해야 할 때가 있음
    * 그렇지 않으면 루프가 아예 실행되지 않거나 무한으로 반복될 수 있음
    * 이 외의 일반적인 상황에서는 루프를 안전하게 사용할 수 있음
    * 함수형 스타일에서는 다른 누군가가 변화를 알아차리지만 못한다면 아무 상관이 없음
      * 즉 지역 변수는 자유롭게 갱신할 수 있음
    * Iterator로 ``` for (Apple a : apples) {} ``` 라는 for-each 루프 표현 코드
      ```
      Iterator<Apple> it = apples.iterator();
      while (it.hasNext()) {
          Apple apple = it.next();
          // ...
      }
      ```
      * 위 코드에서 호출자는 변화를 확인할 수 없으므로 아무 문제가 없음
        * 즉 next로 Iterator의 상태를 변환했고, while 바디 내부에서 apple 변수에 할당하는 동작을 할 수 있음
        * 하지만 다음 코드처럼 for-each 루프를 사용하는 검색 알고리즘은 문제가 될 수 있음
          ```
          public void searchForGold(List<String> l, Stats stats) {
              for (String s : l) {
                  if ("gold".equals(s)) {
                      stats.incrementFor("gold");
                  }
              }
          }
          ```
          * 루프 바디에서 함수형과 상충하는 부작용이 발생함
            * 즉 루프 내부에서 프로그램의 다른 부분과 공유되는 stats 객체의 상태를 변화시킴
          * 이러한 문제 때문에 하스켈 같은 순수 함수형 프로그래밍 언어에서는 부작용 연산을 원칙적으로 제거함
            * 이론적으로 반복을 이용하는 모든 프로그램은 재귀로도 구현할 수 있는데 재귀를 이용하면 변화가 일어나지 않음
            * 재귀를 이용하면 루프 단계마다 갱신되는 반복 변수를 제거할 수 있음
            * 다음은 팩토리얼 함수로 반복과 재귀 방식으로 해결할 수 있는 고전적 학교 문제
              * 입력은 1보다 크다고 가정
              * 반복 방식의 팩토리얼
                ```
                static int factorialIterative(int n) {
                    int r = 1;
                    for (int i = 1; i <= n; i++) {
                        r *= i;
                    }
                    return r;
                }
                ```
              * 재귀 방식의 팩토리얼
                ```
                static long factorialRecursive(long n) {
                    return n == 1 ? 1 : n * factorialRecursive(n - 1);
                }
                ```
              * 반복 방식 예제는 일반적인 루프를 사용한 코드로 매 반복마다 변수 r과 i가 갱신됨
              * 재귀 방식 예제는 좀 더 수학적인 형식으로 문제를 해결
              * 스트림 팩토리얼
                ```
                static long factorialStreams(long n) {
                    return LongStream.rangeClosed(1, n)
                            .reduce(1, (long a, long b) -> a * b);
                }
                ```
              * 함수형 프로그래밍의 장점이 분명히 있지만 무조건 반복보다는 재귀가 좋다고 주장하는 함수형 프로그래밍 광신도의 주장에 주의할 것
                * 일반적으로 반복코드보다 재귀 코드가 더 비쌈
                  * factorialRecursive 함수를 호출할 때마다 호출 스택에 각 호출시 생성되는 정보를 저정할 새로은 스택 프레임이 만들어짐
                  * 즉 재귀 팩토리얼의 입력값에 비례해서 메모리 사용량이 증가함
                  * 따라서 큰 입력값을 사용하면 다음처럼 StackOverflowError 발생
                * 함수형 언어에서는 꼬리 호출 최적화(tail-call optimization)라는 해결책을 제공함
                  * 꼬리 재귀 팩토리얼
                    ```
                    static long factorialTailRecursive(long n) {
                        return factorialHelper(1, n);
                    }
                
                    static long factorialHelper(long acc, long n) {
                        return n == 1 ? acc : factorialHelper(acc * n, n - 1);
                    }
                    ```
                    * factorialHelper에서 재귀 호출이 가장 마지막에 이루어지므로 꼬리 재귀
                    * 반면 factorialRecursive에서 마지막으로 수행한 연산은 n과 재귀 호출의 결과값의 곱셈
                  * 중간 결과를 각각의 스택 프레임에 저장해야 하는 일반 재귀와 달리  
                    꼬리 재귀에서는 컴파일러가 하나의 스택 프레임을 재활용할 가능성이 생김
                    * 사실 factorialHelper의 정의에서는 중간 결과(팩토리얼의 부분결과)를 함수의 인수로 직접 전달
                  * 자바에서는 이와 같은 최적화를 제공하지 않음
                    * 그럼에도 여전히 고전적 재귀보다는 여러 컴파일러 최적화 여지를 남겨둘 수 있는 꼬리 재귀를 적용하는 것이 좋음
                    * 스칼라, 그루비 같은 최신 JVM 언어는 이와 같은 재귀를 반복으로 변환하는 최적화를 제공함 (속도의 손실 업이)
                    * 결과적으로 순수 함수형을 유지하면서도 유용성뿐 아니라 효율성까지 두 마리의 토끼를 모두 잡을 수 있음
  * 결론적으로 자바 8에서는 반복을 스트림으로 대체해서 변화를 피할 수 있음
   * 또한 반복을 재귀로 바꾸면 더 간결하고, 부작용이 없는 알고리즘을 만들 수 있음
   * 실제로 재귀를 이용하면 좀 더 쉽게 읽고, 쓰고, 이해할 수 있는 예제를 만들 수 있음
   * 또한 약간의 실행시간 차이보다는 프로그래머의 효율성이 더 중요할 때도 많음

### 정리
* 공유된 가변 자료구조를 줄이는 것은 장기적으로 프로그램을 유지보수하고 디버깅하는 데 도움이 됨
* 함수형 프로그래밍은 부작용이 없는 메서드와 선언형 프로그래밍 방식을 지향함
* 함수형 메서드는 입력 인수와 출력 결과만을 가짐
* 같은 인수값으로 함수를 호출했을 때 항상 같은 값을 반환하면 참조 투명성을 갖는 함수
  * while 루프 같은 반복문은 재귀로 대체할 수 있음
* 자바에서는 고전 방식의 재귀보다는 꼬리 재귀를 사용해야 추가적인 컴파일러 최적화를 기대할 수 있음

### [quiz]
---

## chapter 19 - 함수형 프로그래밍 기법

### 함수는 모든 곳에 존재한다
* 함수형 프로그래밍이란 함수나 메서드가 수학의 함수처럼 동작함을 의미
  * 즉 부작용 없이 동작함을 의미
  * 함수형 언어 프로그래머는 함수형 프로그래밍이라는 용어를 좀 더 폭넓게 사용함
    * 함수를 마치 일반값처럼 사용해서 인수로 전달하거나, 결과로 반환받거나, 자료구조에 저장할 수 있음을 의미함
    * 일반 값처럼 취급할 수 있는 함수를 일급 함수(first-class function)라고 함
  * 자바 8부터 일급 함수를 지원
    * ``` :: ``` 연산자로 메서드 참조를 만들거나  
      ``` (int x) -> x + 1 ``` 같은 람다 표현식으로 직접 함숫값을 표현해 메서드를 함수값으로 사용할 수 있음
    * 다음과 같은 메서드 참조로 Integer.parsetInt를 저장할 수 있음
      ``` Function<String, Integer> strToInt = Integer::parseInt; ```
      * strToInt 변수에 ``` Integer::parseInt ``` 한 메서드만 저장하는 상황이라면  
        strToInt 형식을 ``` ToIntFunction<String> ```으로 정의해서 언박싱을 피할 수 있음
      * 이렇게 기본형의 성능을 개선할 수 있지만 중간에 어떤 일이 일어나는지 더 잘 확인할 수 있다는 장점도 있으므로 여기서는 Function을 사용함

* 고차원 함수
  * 지금까지 함숫값을 자바 8 스트림 처리 연산으로 전달하거나  
    filterApples에 함숫값으로 Apple::isGreenApple을 전달해서 동작 파라미터화를 달성하는 용도로만 사용함
  * 이는 함숫값 활용의 일부에 불과함
  * 함수를 인수로 받아서 다른 함수로 변환하는 정적 메서드 Comparator.comparing도 있었음
    ``` Comparator<Apple> c = comparing(Apple::getWeight); ```
  * 3장에서 함수를 조립해서 연산 파이프라인을 만들 때 위 코드와 비슷한 기능을 활용함
    ```
    Function<String, String> transformationPipeline = 
            addHeader.andThen(Letter::checkSpelling)
                    .andThen(Letter::addFooter);
    ```
  * 함수형 프로그래밍 커뮤니티에 따르면 Comparator.comparing처럼  
    다음 중 하나 이상의 동작을 수행하는 함수를 고차원 함수(higher-order functions)라 부름
    * 하나 이상의 함수를 인수로 받음
    * 함수를 결과로 반환
* 고차원 함수 정의
  * 자바 8에서는 함수를 인수로 전달할 수 있을 뿐 아니라  
    결과로 반환하고,  
    지역 변수로 할당하거나,  
    구조체로 삽입할 수 있으므로  
    자바 8의 함수도 고차원 함수라고 할 수 있음
    * 예를 들어 간단한 계산기 프로그램에서 문자열 'sin'을 ``` Function<Double, Double> ```로 매핑하는  
      ``` Map<String, Function<Double, Double>> ```이 있다고 가정
      * 최종적으로 메서드 참조 ``` Math::sin ```을 획득함
    * 미분을 좋아한다면 미분 형식이 다음과 같다고 생각할 수 있음
      ``` Function<Function<Double, Double>, Function<Double, Double>> ```
      * 위 함수는 ``` (Double x) -> x * x ```라는 함수를 인수로 받아  
        ``` (Double x) -> 2 * x ``` 같은 함수를 결과로 반환
      * 결과가 함수 형식(가장 왼쪽 Function)이므로 미분 함수를 다른 함수로 다시 전달할 수 있음
      * 다음의 미분 형식과 시그니처는 결국 위 함수와 같음
        ``` Function<Double, Double> differentiate(Function<Double, Double> func) ```
* 부작용과 고차원 함수
  * 스트림 연산으로 전달하는 함수는 부작용이 없어야 하며, 부작용을 포함하는 함수를 사용하면 문제가 발생함
    * 부작용을 포함하는 함수를 사용하면 부정확한 결과가 발생하거나 레이스 컨디션(race conditions) 때문에 예상치 못한 결과가 발생할 수 있음
  * 고차원 함수를 적용할 때도 같은 규칙이 적용됨
  * 고차원 함수나 메서드를 구현할 때 어떤 인수가 전달될지 알 수 없으므로 인수가 부작용을 포함할 가능성을 염두에 두어야 함
  * 함수를 인수로 받아 사용하면서 코드가 정확히 어떤 작업을 수행하고 프로그램의 상태를 어떻게 바꿀지 예측하기 어려워짐
  * 디버깅도 어려워질 것
  * 따라서 인수로 전달된 함수가 어떤 부작용을 포함하게 될지 정확하게 문서화하는 것이 좋음
  * 물론 부작용을 포함하지 않는 것이 가장 좋을 것

* 커링
  * 함수를 모듈화하고 코드를 재사용하는 데 도움을 주는 기법
  * 이해를 돕는 예제
    * 대부분의 앱은 국제화를 지원해야 하는데 이때 단위 변환 문제가 발생
    * 보통 변환 요소(conversion factor)와 기준치 요소(baseline adjustment factor)가 단위 변환 결과를 좌우함
    * 에를 들어 섭씨를 화씨로 변환하는 공식
      * CtoF(x) = x*9/5 + 32
      * 다음과 같은 패턴으로 단위 표현
        * 변환 요소를 곱함
        * 기준치 조정 요소를 적용
      * 다음과 같은 메서드로 변환 패턴을 표현
        ```
        static double converter(double x, double f, double b) {
            return x * f + b;
        }
        ```
        * 여기서 x는 변환하려는 값이고, f는 변환 요소이며, b는 기준치 조정 요소
        * 온도뿐 아니라 킬로미터, 마일 등의 단위도 변환해야 할 것
        * 세 개의 인수를 받는 converter라는 메서드를 만들어 문제를 해결할 수도 있지만 인수에 변환 요소와 기준치를 넣는 일은 귀찮고 오타 발생도 쉬움
      * 각각을 변환하는 메서드를 따로 만드는 방법도 있지만 그러면 로직을 재활용하지 못한다는 단점이 있음
      * 기존 로직을 활용해 변환기를 특정 상황에 적용할 수 있는 방법이 있음
        * 다음은 커링이라는 개념을 활용해서 한 개의 인수를 갖는 변환 함수를 생산하는 팩토리를 정의하는 코드
          ```
          static DoubleUnaryOperator curriedConverter(double f, double b) {
              return (double x) -> x * f + b;
          }
          ```
          * 위 메서드 변환 요소(f)와 기준치 (b)만 넘겨주면 우리가 원하는 작업을 수행할 함수가 반환됨
          * 예를 들어 팩토리를 이용해 원하는 변환기를 생성하는 코드
            ```
            DoubleUnaryOperator convertCtoF = curriedConverter(9.0/5, 32);
            DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);
            DoubleUnaryOperator convertKmtoMi = curriedConverter(0.6214, 0);
            ```
            * DoubleUnaryOperator는 applyAsDouble이라는 메서드를 정의하므로 다음처럼 변환기를 사용할 수 있음
              ``` double gbp = convertUSDtoGBP.applyAsDouble(1000); ```
            * 결과적으로 기존의 변환 로직을 재활용하는 유연한 코드를 얻음
            * x, f, b라는 세 인수를 converter 메서드로 전달하지 않고 f, b 두 가지 인수로 함수를 요청했으며  
              반환된 함수에 인수 x를 이용해서 x * f + b라는 결과를 얻었음
            * 이런 방식으로 변환 로직을 재활용할 수 있으며 다양한 변환 요소로 다양한 함수를 만들 수 있음

* 커링의 이론적 정의
  * 커링은 x와 y라는 두 인수를 받는 함수 f를 한 개의 인수를 받는 g라는 함수로 대체하는 기법
  * 이때 g라는 함수 역시 하나의 인수를 받는 함수를 반환함
  * 함수 g와 함수 f가 최종적으로 반환하는 값은 같음
    * 즉 ``` f(x,y) = (g(x))(y) ``` 가 성립함
  * 물론 이 과정을 일반화할 수 있음
  * 여섯 개의 인수를 가진 함수를 커리해서 우선 2, 4, 6번째 인수를 받아 5번째 인수를 받는 함수를 반환하고  
    다시 이 함수는 남은 1, 3번째 인수를 받는 함수를 반환함
  * 이와 같은 여러 과정이 끝까지 완료되지 않은 상태를 가리켜 '함수가 부분적으로(partially) 적용되었다'라고 말함

### 영속 자료구조
* 함수형 프로그램에서는 함수형 자료구조, 불변 자료구조 등의 용어도 사용하지만 보통은 영속 자료구조라고 부름
  * 여기서 말하는 영속(persistent)이라는 단어는 DB에서 프로그램 종료 후에도 남아있음을 의미하는 영속과는 다른 의미
* 함수형 메서드에서는 전역 자료구조나 인수로 전달된 구조를 갱신할 수 없음
  * 자료 구조를 바꾼다면 같은 메서드를 두 번 호출했을 때 결과가 달라지면서 참조 투명성에 위배되고,  
    인수를 결과로 단순하게 매핑할 수 있는 능력이 상실되기 때문

* 파괴적인 갱신과 함수형
  * 자료구조를 갱신할 때 발생할 수 있는 문제를 확인
    * A에서 B까지 기차여행을 의미하는 가변 TrainJourney 클래스가 있다고 가정
      * TrainJourney는 간단한 단방향 연결 리스트로 구현되며 여행 구간의 가격 등 상세 정보를 포함하는 int 필드를 포함
      * 기차여행에서는 여러 TrainJourney 객체를 연결할 수 있는 onward(이어지는 여정을 의미) 필요
      * 직통열차나 여정의 마지막 구간에서는 onward가 null이 됨
      ```
      class TrainJourney {
          public int price;
          public TrainJourney onward;
         
          public TrainJourney(int p, TrainJourney t) {
              this.price = p;
              this.onward = t;
          }
      }
      ```
      * 이때 X에서 Y까지 그리고 Y에서 Z까지의 여행을 나타내는 별도의 TrainJourney 객체가 있다고 가정
      * 아마 두 개의 TrainJourney 객체를 연결해서 하나의 여행을 만들 수 있을 것이다
        * 즉 X에서 Y 그리고 Z로 이어지는 여행
      * 기존의 단순한 명령형 메서드라면 다음처럼 기차여행을 연결(link)
        ```
        static TrainJourney link(TrainJourney a, TrainJourney b) {
            if (a == null) {
                return b;
            }
    
            TrainJourney t = a;
            while (t.onward != null) {
                t = t.onward;
            }
    
            t.onward = b;
    
            return a;
        }
        ```
        * 위 코드는 a의 TrainJourney에서 마지막 여정을 찾아 a의 리스트 끝부분을 가리키는 null을 리스트 b로 대체함
          * a가 아무 요소도 포함하지 않은 상황은 따로 고려해야 함
        * 여기서 문제 발생
        * firstJourney라는 변수는 X에서 Y로 경로를 포함하고, secondJourney라는 변수는 Y에서 Z로의 경로를 포함함
        * ``` link(firstJouney, secondJourney) ```를 호출하면 firstJouney가 secondJourney를 포함하면서 파괴적인 갱신이 일어남
          * 즉 firstJourney를 변경시킴
        * 결과적으로 firstJouney 변수는 X에서 Y로의 여정이 아니라 X에서 Z로의 여정을 의미하게 됨
          * 예를 들어 firstJouney가 서울역에서 구미역까지의 기차 여정을 의미하고 있었는데  
            갑자기 이 여정이 서울역에서 구미역을 거쳐 부산역까지 도착하는 여정으로 바뀐다면  
            firstJouney를 참고하고 하던 사용자들은 혼란에 빠짐
          * 이렇게 자료구조를 바꾸면서 생기는 버그를 어떻게 처리할 것인지 문제를 해결해야 함
        * 함수형에서는 이 같은 부작용을 수반하는 메서드를 제한하는 방식으로 문제를 해결함
        * 계산 결과를 표현할 자료구조가 필요하면 기존의 자료구조를 갱신하지 않도록 새로운 자료구조를 만들어야 함
        * 이는 표준 객체지향 프로그래밍의 관점에서도 좋은 기법
        * 함수형을 따르지 않는 프로그램의 문제 중 하나는 부작용을 포함하는 코드와 관련해서  
          '이 점을 기억해야 함', '문서화해둠' 같은 주석을 프로그래머가 과도하게 남용할 수 있다는 점
        * 이 같은 주석은 나중에 코드를 유지보수하는 프로그래머를 괴롭힘
        * 따라서 다음처럼 깔끔한 함수형 해결 방법을 사용하는 것이 좋음
          ```
          static TrainJourney append(TrainJourney a, TrainJourney b) {
              return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
          }
          ```
          * 위 코드는 명확하게 함수형이며 기존 자료구조를 변경하지 않음
            * 전역 심지어 지역적인 변화도 일어나지 않음
          * 하지만 TrainJourney 전체를 새로 만들지 않음
          * a가 n 요소의 시퀀스고 b가 m 요소의 시퀀스라면 n+m 요소의 시퀀스를 반환함
            * 여기서 첫 번째 n 요소는 새로운 노드며 마지막 m 요소는 TrainJourney b와 공유되는 요소
            * 주의할 점은 사용자 역시 append의 결과를 갱신하지 말아야 한다는 것
            * 만약 append의 결과를 갱신하면 시퀀스 b로 전달된 기차 정보도 바뀜

* 트리를 사용한 다른 예제
  * HashMap 같은 인터페이스를 구현할 때는 이진 탐색 트리가 사용됨
    * Tree는 문자열 키와 int 값을 포함함
    * 예를 들어 이름 키와 나이 정보값을 포함할 수 있음
    ```
    public class Tree {
        private String key;
        private int val;
        private Tree left, right;
       
        public Tree(String k, int v, Tree l, Tree r) {
        	this.key = k;
        	this.val = v;
        	this.left = l;
        	this.right = r;
        }
    }
    
    class TreeProcessor {
   
        public static int lookup(String k, int defaultVal, Tree t) {
            if (t == null) {
                return defaultVal;
            }
            
            if (k.equals(t.key)) {
                return t.val;
            }
            
            return lookup(k, defaultVal, k.compareTo(t.key) < 0 ? t.left : t.right);
        }
    }
    ```
    * 이제 이진 탐색 트리를 이용해 문자열값으로 int를 얻을 수 있음
    ```
    public static void update(String k, int newVal, Tree t) {
        if (t == null) {
            // 새로운 노드를 추가해야함
        } else if (k.equals(t.key)) {
            t.val = newVal;
        } else {
            update(k, newVal, k.compareTo(t.key) < 0 ? t.left : t.right);
        }
    }
    ```
    * 새로운 노드를 추가할 때는 주의할 것
      * 가장 쉬운 방법은 update 메서드가 탐색한 트리를 그대로 반환하는 것
        * 새로운 노드를 추가하지 않으면 기존 트리가 그대로 반환됨
      * 하지만 이 방법은 깔끔하지 못함
        * 사용자는 update가 즉석에서 트리를 갱신할 수 있으며,  
          전달한 트리가 그대로 반환된다는 사실과 원래 트리가 비어있으면 새로운 노드가 반환될 수 있다는 사실을 모두 기억해야 하기 때문
    ```
    public static Tree update(String k, int newVal, Tree t) {
        if (t == null) {
            t = new Tree(k, newVal, null, null);
        } else if (k.equals(t.key)) {
            t.val = newVal;
        } else if (k.compareTo(t.key) < 0) {
            t.left = update(k, newVal, t.left);
        } else {
            t.right = update(k, newVal, t.right);
        }
        return t;
    }
    ```
    * 두 가지 update 버전 모두 기존 트리를 변경함
      * 즉 트리에 저장된 맵의 모든 사용자가 변경에 영향을 받음

* 함수형 접근법 사용
  * 이 문제를 함수형으로 처리
    * 우선 새로운 키/값 쌍을 저장할 새로운 노드를 만들어야 함
    * 또한 트리의 루트에서 새로 생성한 노드의 경로에 있는 노드들도 새로 만들어야 함
      * 보통 노드를 새로 만드는 동작은 생각보다 비싸지 않은 편
      * d라는 깊이를 갖는 트리고 균형이 어느 정도 잡혀있다면 2^d만큼의 개체를 가지고 있으므로 재생성 과정은 그 중 일부 노드를 생성하는 과정에 불과함
    ```
    public static Tree fupdate(String k, int newVal, Tree t) {
        return (t == null) ?
                new Tree(k, newVal, null, null) :
                k.equals(t.key) ?
                        new Tree(k, newVal, t.left, t.right) :
                        k.compareTo(t.key) < 0 ?
                                new Tree(t.key, t.val, fupdate(k, newVal, t.left), t.right) :
                                new Tree(t.key, t.val, t.left, fupdate(k, newVal, t.right));

    }
    ```
    * 위 코드에서는 if-then-else 대신 하나의 조건문을 사용했는데 이렇게 해서 위 코드가 부작용이 없는 하나의 표현식임을 강조
      * 하지만 취향에 따라 if-then-else 문으로 코드를 구현할 수 있음
    * update와 fupdate의 차이
      * 이전에 update 메서드는 모든 사용자가 같은 자료구조를 공유하며 프로그램에서 누군가 자료구조를 갱신했을 때 영향을 받음
      * 따라서 비함수형 코드에서는 누군가 언제든 트리를 갱신할 수 있으므로 트리에 어떤 구조체의 값을 추가할 때마다 값을 복사
      * 반면 fupdate는 순수한 함수형
      * fupdate에서는 새로운 Tree를 만듦
      * 하지만 인수를 이용해서 가능한 한 많은 정보를 공유함
      * 사람의 이름과 나이를 노드로 저장하는 트리를 만들었다고 가정
        * 여기서 fupdate를 호출하면 기존의 트리를 갱신하는 것이 아니라 새로운 노드를 만듦
      * 이와 같은 함수형 자료구조를 영속(persistent - 저장된 값이 다른 누군가에 의해 영향을 받지 않는 상태)라고 함
      * 따라서 프로그래머는 fupdate가 인수로 전달된 자료구조를 변화시키지 않을 것이라는 사실을 확신
      * '결과 자료구조를 바꾸지 말라'는 것이 자료구조를 사용하는 모든 사용자에게 요구하는 단 한가지 조건
      * 결과 자료구조를 바꾸지 말라는 조건을 무시한다면 fupdate로 전달된 자료구조에 의도치 않은 그리고 원치 않은 갱신이 일어남
      * fupdate가 좀 더 효율적일 때가 많음
      * '기존 구조를 변화시키지 않는다'라는 법칙 덕분에 조금 다른 구조체간의 공통부분을 공유할 수 있다는 점에서 다른 구조체와 조금 다름
        * 예를 들어 사용자 A가 확인하는 트리와 사용자 B가 확인하는 갱신된 버전처럼
      * Tree 클래스의 key, val, left, right 필드를 final로 선언함으로써 '기존 구조를 변화시키지 않는다'는 규칙을 강제할 수 있음
      * 하지만 final은 필드에만 적용되며 객체에는 적용되지 않으므로 각 객체의 필드에 final을 적절하게 사용해야 함
  * 어떤 사람은 '나는 일부 사용자만 볼 수 있게 트리를 갱신하면서도 다른 일부 사용자는 이를 알아차릴 수 없게 하고 싶다'고 말함
    * 두 가지 방법
      * 하나는 고전적인 자바 해법
        * 어떤 값을 갱신할 때 먼저 복사해야 하는지 주의 깊게 확인
      * 다른 하나는 함수형 해법
        * 갱신을 수행할 때마다 논리적으로 새로운 자료구조를 만든 다음(변화가 일어나지 않도록) 사용자에게 적절한 버전의 자료구조를 전달할 수 있음
        * API로 이 방법을 강제할 수 있음
        * 자료구조의 고객이 볼 수 있도록 갱신을 수행해야 한다면 최신 버전을 반환하는 API를 사용할 수 있음
        * 반면에 보이지 않도록 갱신을 수행해야 한다면  
          예를 들어 오래 실행되는 통계분석 같은 경우에는 변화가 일어나지 않도록 단순히 인수를 복사한 값을 반환하면 됨
    * 이 기법은 레이저로 단 한 번 CD-R에 파일을 '갱신'하는 동작과 비슷함
      * 여러 파일의 버전을 모두 CD에 저장했다가 적절한 파일 시작 주소 블록을 전달할 수 있음
        * 스마트 CD 제작 소프트웨어라면 여러 파일 버전의 공통부분을 공유하는 기능을 제공할 수 있음
      * 자바는 CD보다는 상황이 좋음
        * 자바에서 자료구조의 예전 버전은 적어도 자동으로 가비지 컬렉트 되기 때문

### 스트림과 게으른 평가
* 스트림은 단 한 번만 소비할 수 있다는 제약이 있어 스트림은 재귀적으로 정의할 수 없음

* 자기 정의 스트림
  * 소수를 생성하는 6장 예제로 재귀 스트림
    ```
    public class MyMathUtils {
    
    	public static Stream<Integer> primes(int n) {
    		return Stream.iterate(2, i -> i + 1)
    				.filter(MyMathUtils::isPrime)
    				.limit(n);
    	}
    
    	public static boolean isPrime(int candidate) {
    		int candidateRoot = (int) Math.sqrt((double) candidate);
    		return IntStream.rangeClosed(2, candidateRoot)
    				.noneMatch(i -> candidate % i == 0);
    	}
    }
    ```
    * 그다지 멋진 코드는 아님
      * 우선 후보 수(candidate number)로 정확히 나누어떨어지는지 매번 모든 수를 반복 확인함
        * 실제로 합성수는 나누어떨어지는지 확인할 필요조차 없음
    * 이론적으로 소수로 나눌 수 있는 모든 수는 제외할 수 있음
      * 다음은 소수로 나눌 수 있는 수를 제외하는 과정
        * [1] 소스를 선택할 숫자 스트림이 필요함
        * [2] 스트림에서 첫 번째 수(스트림의 머리-head)를 가져옴
          * 이 숫자는 소수(처음에 이 숫자는 2)
        * [3] 이제 스트림의 꼬리(tail)에서 가져온 수로 나누어떨어지는 모든 수를 걸러 제외시킴
        * [4] 이렇게 남은 숫자만 포함하는 새로운 스트림에서 소수를 찾음
          * 이제 1번부터 다시 이 과정을 반복하게 됨
          * 따라서 이 알고리즘은 재귀
    * 이 알고리즘은 여러 가지 면에서 '부족한' 알고리즘
      * 다만 스트림이 어떻게 동작하는지 쉽게 보여줄 수 있는 좋은 간단한 알고리즘
  * 스트림 API로 이 알고리즘 구현
    * 1 단계 : 스트림 숫자 얻기
      * IntStream.iterate 메서드를 이용하면 2에서 시작하는 무한 숫자 스트림을 생성할 수 있음
        ```
        static IntStream numbers() {
            return IntStream.iterate(2, n -> n + 1);
        }
        ```
    * 2 단계 : 머리 획득
      * IntStream은 첫 번째 요소를 반환하는 findFirst라는 메서드 제공
        ```
        static int head(IntStream numbers) {
            return numbers.findFirst().getAsInt();
        }
        ```
    * 3 단계 : 꼬리 필터링
      * 스트림의 꼬리를 얻는 메서드 정의
        ```
        static IntStream tail(IntStream numbers) {
            return numbers.skip(1);
        }
        ```
      * 획득한 머리로 숫자를 필터링할 수 있음
        ```
        IntStream numbers = numbers();
        int head = head(numbers);
        IntStream filtered = tail(numbers).filter(n -> n % head != 0);
        ```
    * 4 단계 : 재귀적으로 소수 스트림 생성
      * 가장 어려운 부분
      * 반복적으로 머리를 얻어 스트림을 필터링하려 할 수 있음
        ```
        static IntStream primes(IntStream numbers) {
        		int head = head(numbers);
        		return IntStream.concat(
        				IntStream.of(head),
        				primes(tail(numbers).filter(n -> n % head != 0)));
        	}
        ```
    * 나쁜 소식
      * 4 단계 코드를 실행하면  
        "java.lang.IllegalStateException: stream has already been operated upon or closed"라는 에러 발생
      * 사실 우리는 스트림을 머리와 꼬리로 분리하는 두 개의 최종 연산 findFirst, skip을 사용함
      * 최종 연산을 스트림에 호출하면 스트림이 완전 소비됨
    * 게으른 평가
      * 위 나쁜 소식보다 더 심각한 문제가 있음
      * 정적 메서드 IntStream.concat은 두 개의 스트림 인스턴스를 인수로 받음
      * 두 번째 인수가 primes를 직접 재귀적으로 호출하면서 무한 재귀에 빠짐
      * '재귀적 정의 허용하지 않음'같은 자바 8의 스트림 규칙은 우리에게 아무 해도 끼치지 않으며  
        오히려 이 규칙 덕분에 데이터베이스 같은 질의를 표현하고 병렬화할 수 있는 능력을 얻을 수 있음
      * 그래서 자바 8 설계자는 이 같은 제한을 두기로 결정
      * 사실 스칼라, 하스켈 같은 함수형 언어에서는 자바 8에 비해 좀 더 일반적인 기능과 모델을 제공함
      * 결론적으로 concat의 두 번째 인수에서 primes를 게으르게 평가하는 방식으로 문제를 해결할 수 있음
        * 좀 더 기술적인 프로그래밍 언어의 용어로는 게으른 평가(lazy evaluation)를 비엄격한 평가(nonstrict evaluation)  
          또는 이름에 의한 호출(call by name)이라 함
      * 즉 소수를 처리할 필요가 있을 때만 스트림을 실제로 평가함
      * 스칼라에서도 이와 같은 기능을 제공함
      * 스칼라로 알고리즘을 구현한 코드
        * 여기서 ``` #:: ``` 연산자는 게으른 연결을 담당
          * 즉 실제 스트림을 소비해야 하는 상황이 되었을 때 인수를 평가함
          ```
          def numbers(n: Int): Stream[Int] = n #:: numbers(n + 1)
          
          def primes(numbers: Stream[Int]): Stream[Int] = {
              numbers.head #:: primes(numbers.tail filter (n -> n % numbers.head != 0))
          }
          ```
          * 위 코드는 자바와 다른 함수형 언어가 어떻게 다른지 보여주는 것뿐
          * 자바에서는 메서드를 호출하면 모든 인수가 즉시 평가됨
          * 하지만 스칼라에서는 ``` #:: ```을 사용한 연결식이 즉시 반환될 뿐이며 필요한 시점이 되어서야 요소가 평가됨

* 게으른 리스트 만들기
  * 자바 8의 스트림은 요청할 때만 값을 생성하는 블랙박스와 같음
    * 스트림에 일련의 연산을 적용하면 연산이 수행되지 않고 일단 저장됨
    * 스트림에 최종 연산을 적용해서 실제 계산을 해야 하는 상황에서만 실제 연산이 이루어짐
    * 특히 스트림에 여러 연산(filter, map, reduce 등)을 적용할 때 이와 같은 특성을 활용할 수 있음
    * 게으른 특성 때문에 각 연산별로 스트림을 탐색할 필요 없이 한 번에 여러 연산을 처리할 수 있음
    * 일반적인 스트림의 형태인 게으른 리스트의 개념
      * 게으른 리스트는 스트림과 비슷한 개념으로 구성됨
    * 또한 게으른 리스트는 고차원 함수라는 개념도 지원
    * 함숫값을 자료구조에 저장해서 함숫값을 사용하지 않은 상태로 보관할 수 있음
    * 하지만 저장한 함숫값을 호출(요청)하면 더 많은 자료구조를 만들 수 있음
  * 기본적인 연결리스트
    * 다음 코드처럼 MyLinkedList라는 단순한 연결 리스트 형태의 클래스를 정의할 수 있음
      ```
      interface MyList<T> {
          T head();
        
          MyList<T> tail();
        
          default boolean isEmpty() {
              return true;
          }
      }
      
      class MyLinkedList<T> implements MyList<T> {
          private final T head;
          private final MyList<T> tail;
        
          public MyLinkedList(T head, MyList<T> tail) {
              this.head = head;
              this.tail = tail;
          }
        
          @Override
          public T head() {
              return head;
          }
        
          @Override
          public MyList<T> tail() {
              return tail;
          }
        
          @Override
          public boolean isEmpty() {
              return false;
          }
      }
      
      class Empty<T> implements MyList<T> {
     
          @Override
          public T head() {
              throw new UnsupportedOperationException();
          }
        
          @Override
          public MyList<T> tail() {
              throw new UnsupportedOperationException();
          }
      }
      ```
    * MyLinkedList 값 생성
      ``` MyList<Integer> l = new MyLinkedList<>(10, new Empty<>()); ```
  * 기본적인 게으른 리스트
    * ``` Supplier<T> ```를 이용해 게으른 리스트를 만들면 꼬리가 모두 메모리에 존재하지 않게 할 수 있음
      * ``` Supplier<T> ```를 ``` void -> T ```라는 함수형 디스크립터를 가진 팩토리로 생각할 수 있음
    * ``` Supplier<T> ```로 리스트의 다음 노드 생성
    * 게으른 리스트를 만드는 코드
      ```
      class LazyList<T> implements MyList<T> {
          final T head;
          final Supplier<MyList<T>> tail;
        
          public LazyList(T head, Supplier<MyList<T>> tail) {
              this.head = head;
              this.tail = tail;
          }
        
          @Override
          public T head() {
              return head;
          }
        
          @Override
          public MyList<T> tail() {
              // 위 head와 달리 tail에서는 Supplier로 게으른 동작을 만듦
              return tail.get();
          }
        
          @Override
          public boolean isEmpty() {
              return false;
          }
      }
      ```
      * 이제 Supplier의 get 메서드를 호출하면 (마치 팩토리로 새로운 객체를 생성하듯) LazyList의 노드가 만들어짐
      * 이제 연속적인 숫자의 다음 요소를 만드는 LazyList의 생성자에 tail 인수로 Supplier를 전달하는 방식으로  
        n으로 시작하는 무한히 게으른 리스트를 만들 수 있음
        ```
        public static LazyList<Integer> from(int n) {
            return new LazyList<Integer>(n, () -> from(n + 1));
        }
        ```
      * 아래 코드 실행시 '2, 3, 4'라고 출력됨
        * ``` System.out.println ```을 추가해서 확인 가능
        * 실제로 숫자는 요청했을 때 만들어짐
        * 만약 요청했을 때 코드가 실행되는 것이 아니라 2부터 시작해서 모든 수를 미리 계산하려 한다면 프로그램은 영원히 종료되지 않음
        ```
        LazyList<Integer> numbers = from(2);
        int two = numbers.head();
        int three = numbers.tail().head();
        int four = numbers.tail().tail().head();

        System.out.println(two + " " + three + " " + four);
        ```
  * 소수 생성으로 돌아와서
    * 지금까지 만든 코드로 소수 리스트를 생성
      * 스트림 API로는 이 작업을 완료할 수 없었음
    * 다음 코드처럼 기존에 스트림 API를 사용했던 코드에 새로운 LazyList를 적용하려 할 것
      ```
      public static MyList<Integer> primes(MyList<Integer> numbers) {
          return new LazyList<>(
                  numbers.head(),
                  () -> primes(
                          numbers.tail().filter(n -> n % numbers.head() != 0)));
      }
      ```
  * 게으른 필터 구현
    * 아직 filter 메서드를 정의하지 않으므로 위 코드는 컴파일 에러
      ```
      @Override
      public MyList<T> filter(Predicate<T> p) {
          return isEmpty() ?
                  // 새로운 Empty<>()를 반환할 수도 있지만 여기서는 this로 대신할 수 있음
                  this :
                  p.test(head()) ?
                          new LazyList<>(head(), () -> tail().filter(p)) :
                          tail().filter(p);
      }
      ```
    * tail과 head 호출을 연결해 처음 세 개의 소수를 계산할 수 있음
      ```
      int two = primes(numbers).head();
      int three = primes(numbers).tail().head();
      int five = primes(numbers).tail().tail().head();

      System.out.println(two + " " + three + " " + five);
      ```
      * 위 코드 실행시 '2, 3, 5' 세 개의 소수 출력
      * 다양한 시도
        * 모든 소수 출력
          * 프로그램은 반복적으로 리스트의 머리와 꼬리를 출력하면서 printAll 메서드를 무한히 실행할 것
          ```
          static <T> void printAll(MyList<T> list) {
              while (!list.isEmpty()) {
                  System.out.println(list.head());
                  list = list.tail();
              }
          }
          
          printAll(primes(from(2)));
          ```
          * 함수형 프로그래밍을 설명하고 있으므로 위 메서드를 다음처럼 재귀적으로 변경
            ```
            static <T> void printAll(MyList<T> list) {
                if (list.isEmpty()) {
                    return;
                }
        
                System.out.println(list.head());
                printAll(list.tail());
            }
            ```
            * 하지만 위 코드는 무한히 실행되지 않음
              * 자바는 꼬리 호출 제거를 지원하지 않으므로 스택 오버플로가 발생하기 때문
  * 드디어 완성!
    * 게으른 리스트와 함수를 만들었고 모든 소수를 포함하는 자료구조를 정의함
    * 지금까지 만든 자료구조가 제공하는 유용성?
      * 함수를 자료구조 내부로 추가할 수 있다는 사실을 확인
      * 이런 함수는 자료구조를 만드는 시점이 아니라 요청 시점에 실행된다는 사실 또한 확인
      * 체스 같은 게임 프로그램을 구현할 때도 이러한 기능 활용 가능
      * 체스의 말이 움직일 수 있다는 모든 가능성을 개념적으로 표현하는 트리 자료구조(미리 계산하기에는 너무 큰)를 준비하고  
        요청할 때만 이 자료구조를 생성할 수 있음
      * 게으른 리스트는 또한 자바 9의 기능 스트림과의 연결고리를 제공함
    * 아직까지 성능은 살펴보지 않았음
      * 지금까지 적극적으로(eagerly) 기능을 실행하는 것보다는 게으른 편이 좋다고 가정함
      * 물론 전통적인 실행 방법에서처럼 모든 값을 계산하는 것보다는 요청했을 때 값을 계산하는 것이 여러 면에서 좋음
      * 안타깝게도 현실에서는 상황이 이처럼 단순하지 않음
      * 자료구조의 10% 미만의 데이터만 활용하는 상황에서는 게으른 실행으로 인한 오버헤드가 커질 수 있음
      * 결정적으로 LazyList값이 진짜로 게으르진 않을 수 있음
      * from(2)등으로 LazyList값을 탐색할 때 10번째 항목까지는 모든 노드를 두 번 생성하므로 10개가 아니라 20개의 노드가 생성됨
      * 이 작업은 게으르게 처리할 수 없음
      * LazyList 탐색을 요청할 때마다 tail의 Supplier가 반복적으로 호출된다는 점이 문제
      * 처음 탐색 요청을 호출할 때만 tail의 Supplier가 호출되도록 하여(그리고 결과값을 캐시해) 이 문제를 해결할 수 있음
      * LazyList에 ``` private Optional<LazyList<T>> alreadyComputed ``` 필드를 추가하고  
        tail 메서드가 적절하게 리스트를 업데이트하도록 정리할 수 있음
      * 순수 함수형 언어 하스켈은 이와 같은 방식으로 자신의 자료구조를 적당히 게으르게 정리함
    * 게으른 자료구조는 강력한 프로그래밍 도구
      * 앱을 구현하는데 도움을 준다면 게으른 자료구조를 사용할 것
      * 하지만 게으른 자료구조 때문에 효율성이 떨어진다면 전통적인 방식으로 코드를 구현할 것

### 패턴 매칭
* 거의 모든 함수형 프로그래밍 언어에서는 제공하지만 자바에서는 지원하지 않는 기능
* 일반적으로 함수형 프로그래밍을 구분하는 또 하나의 중요한 특징으로 (구조적인) 패턴 매칭(pattern matching)을 들 수 있음
  * 정규표현식, 정규표현식과 관련된 패턴 매칭과는 다름
  * 앞서 수학에서는 다음과 같은 정의를 할 수 있다고 설명함
    ```
    f(0) = 1
    f(n) = n*f(n-1) 그렇지 않으면
    ```
  * 반면 자바에서는 if-then-else나 switch문을 사용해야 함
    * 자료형이 복잡해지면서 이러한 작업을 처리하는 데 필요한 코드(그리고 관련 잡동사니)의 양도 증가함
    * 패턴 매칭을 사용하면 이러한 불필요한 잡동사니를 줄일 수 있음
    * 트리 탐색 예제로 이 문제 확인
      * 숫자와 바이너리 연산자로 구성된 간단한 수학언어가 있다고 가정
        ```
        class Expr {
        }
    
        class Number extends Expr {
            int val;
        }
    
        class BinOp extends Expr {
            String opName;
            Expr left, right;
        }
        ```
        * 표현식을 단순화하는 메서드를 구현해야 한다고 가정
          * 예를 들어 5 + 0은 5로 단순화할 수 있음
          * 즉 ``` new BinOp("+", new Number(5), new Number(0)) ```은 ``` Number(5) ```로 단순화할 수 있음
          * Expr 구조체를 다음처럼 탐색할 수 있음
            ```
            Expr simplifyExpression(Expr expr) {
                if (expr instanceof BinOp 
                    && ((BinOp)expr).opName.equals("+")
                    && ((BinOp)expr).right instanceof Number
                    && .. // 코드가 깔끔하지 못함
                    && .. ) {
                    return (BinOp)expr.left;
            }
            ```

* 방문자 디자인 패턴
  * 자바에서는 방문자 디자인 패턴(visitor pattern)으로 자료형을 언랩할 수 있음
    * 특히 특정 데이터 형식을 '방문'하는 알고리즘을 캡슐화하는 클래스를 따로 만들 수 있음
  * 방문자 클래스는 지정된 데이터 형식의 인스턴스를 입력으로 받음
    * 그리고 인스턴스의 모든 멤버에 접근함
    * 다음과 같이 작동
      * 우선 SimplifyExprVisitor를 인수로 받는 accept를 BinOp에 추가한 다음에  
        BinOp 자신을 SimplifyExprVisitor로 전달함 (Number에서 비슷한 메서드를 추가)
        ```
        static class BinOp extends Expr {
            // .. 다른 코드 생략
    
            // 방문자 패턴
            public Expr accept(SimplifyExprVisitor v) {
                return v.visit(this);
            }
        }
        ```
        * 이제 SimplifyExprVisitor는 BinOp 객체를 언랩할 수 있음
          ```
          static class SimplifyExprVisitor {
          
              public Expr visit(BinOp e) {
                  if ("+".equals(e.opName) && e.right instanceof Number && ...) {
                      return e.left;
                  }
                  return e;
              }
          }
          ```

* 패턴 매칭의 힘
  * 패턴 매칭이라는 좀 더 단순한 해결 방법도 존재
  * 자바는 패턴 매칭을 지원하지 않으므로 스칼라 프로그래밍 언어로 패턴 매칭을 보여줌
  * 자바에서 패턴 매칭을 지원했다면 어떤 방식으로 문제를 해결했을지 유추해볼 것
    * 수식을 표현하는 Expr이라는 자료형이 주어졌을 때 스칼라 프로그래밍 언어로는  
      다음처럼 수식을 분해하는 코드를 구현할 수 있음
      ```
      def simplifyExpression(expr: Expr): expr match {
          case BinOp("+", e, Number(0)) => e     // 0 더하기
          case BinOp("*", e, Number(1)) => e     // 1 더하기
          case BinOp("/", e, Number(1)) => e     // 1 나누기
          case _ => expr                         // expr을 단순화할 수 없음
      }
      ```
      * 트리와 비슷한 자료구조를 다룰 때 이와 같은 패턴 매칭을 사용하면 매우 간결하고 명확한 코드를 구현할 수 있음
        * 특히 컴파일러를 만들거나 비즈니스 규칙 처리 엔진을 만들 때 유용함
          ``` Expression match { case Pattern => Expression ... } ```
        * 위 스칼라 문법은 자바 문법과 아주 비슷함
          ``` switch (Expression) { case Constant : Statement ... } ```
      * 스칼라의 와일드카드 case는 자바의 ``` default: ```와 같은 역할을 함
        * 스칼라와 자바의 가장 큰 구문론적 차이는 스칼라가 표현지향인 반면 자바는 구문지향이라는 점
        * 프로그래머의 관점에서 느끼는 차이점은 자바의 case 패턴에서는  
          몇 가지 기본형, 열거형, 기본형을 감싼 특수한 클래스, 문자열 등을 사용할 수 있다는 점
        * 패턴 매칭을 지원하는 언어의 가장 큰 실용적인 장점은 아주 커다란 switch문, if-then-else 문을 피할 수 있다는 점
      * 스칼라의 패턴 매칭의 쉬운 표현 방식은 자바보다 뛰어난 기능이라는 사실을 쉽게 확인 가능
      * 하지만 자바 8의 람다를 이용하면 패턴 매칭과 비슷한 코드를 만들 수 있음

* 자바로 패턴 매칭 흉내 내기
  * 스칼라의 패턴 매칭인 match 표현식 동작 예제
    ```
    def simplifyExpression(expr: Expr): Expr = expr match {
        case BinOp("+", e, Number(0)) => e
        ...
    }
    ```
    * 위 코드는 expr이 BinOp인지 확인하고 expr에서 세 컴포넌트(opName, left, right)를 추출한 다음에  
      이 컴포넌트에 매칭을 시도함
      * 첫째는 String +, 둘째는 변수 e(항상 매치되는), 셋째는 Number(0)으로 매치함
      * 즉 스칼라(그리고 다른 많은 함수형 언어)의 패턴 매칭은 다수준(multilevel)
      * 자바 8의 람다를 이용한 패턴 매칭 흉내 내기는 단일 수준의 패턴 매칭만 지원함
      * 즉 이전 예제에 BinOp(op, l, r)이나 Number(n)은 괜찮지만 BinOp("+", e, Number(0))은 지원하지 않음
      * 규칙 정하기
        * 람다를 이용하며 코드에 if-then-else가 없어야 함
          * '조건 ? e1: e2'와 메서드 호출로 if-then-else를 대신할 수 있음
            ``` myIf(condition, () -> e1, () -> e2); ```
        * 어딘가에(아마도 라이브러리에) 다음을 정의
          ```
          static <T> T myIf(boolean b, Supplier<T> trueCase, Supplier<T> falseCase) {
              return b ? trueCase.get() : falseCase.get();
          }
          ```
          * T 형식은 조건 표현식의 결과 형식을 의미함
            * 이와 같은 기법을 if-then-else에도 적용할 수 있음
          * 물론 일반 코드에서는 if-then-else를 사용하는 것이 코드의 명확성을 더 높일 수 있음
            * 하지만 자바의 switch와 if-then-else가 패턴 매칭에는 도움이 되질 않으며  
              람다를 이용하면 단일 수준의 패턴 매칭을 간단하게 표현할 수 있으므로  
              여러 개의 if-then-else 구분이 연결되는 상황을 깔끔하게 정리할 수 있음
      * BinOp와 Number 두 서브클래스를 포함하는 Expr 클래스의 패턴 매칭(pattern matching)값으로 돌아와 
        patternMatchExpr이라는 메서드를 정의(여기서도 제네릭 T는 패턴 매칭의 결과 형식)
        ```
        interface TriFunction<S, T, U, R> {
            R apply(S s, T t, U u);
        }
    
        static <T> T patternMatchExpr(
                Expr e,
                TriFunction<String, Expr, Expr, T> binOpCase,
                Function<Integer, T> numCase,
                Supplier<T> defaultCase) {
    
            return (e instanceof BinOp) ?
                    binOpCase.apply(((BinOp) e).opName, ((BinOp) e).left, ((BinOp) e).right) :
                    (e instanceof Number) ?
                            numCase.apply(((Number) e).val) :
                            defaultCase.get();
        }
        ```
      * 다음 코드 확인
        ```
        patternMatchExpr(e, (op, l, r) -> { return binOpCode; },
                            (n) -> { return numCode; },
                            () -> { return defaultCode; }); 
        ```
        * 위 코드는 e가 BinOp인지 (BinOp라면 식별자 op, l, r로 BinOp에 접근할 수 있는 binOpCode를 실행)  
          아니면 Number인지 (Number라면 n값에 접근할 수 있는 numCode를 실행) 확인함
        * 이 메서드에는 BinOp나 Number가 아닌 트리 노드를 생성했을 때 실행되는 defaultCode도 존재함
        * 다음 예제는 patternMatchExpr을 이용해 덧셈과 곱셈 표현식을 단순화하는 방법을 보여줌
          ```
          public static Expr simplify(Expr e) {
              TriFunction<String, Expr, Expr, Expr> binOpCase = (opName, left, right) -> {  // BinOp 표현식 처리
                  // 덧셈 처리
                  if ("+".equals(opName)) {
                      if (left instanceof Number && ((Number) left).val == 0) {
                          return right;
                      }
                      if (right instanceof Number && ((Number) right).val == 0) {
                          return left;
                      }
                  }
                  // 곱셈 처리
                  if ("*".equals(opName)) {
                      if (left instanceof Number && ((Number) left).val == 1) {
                          return right;
                      }
                      if (right instanceof Number && ((Number) right).val == 1) {
                          return left;
                      }
                  }
                  return new BinOp(opName, left, right);
              };
            
              // 숫자 처리
              Function<Integer, Expr> numCase = val -> new Number(val);
              // 수식을 인식할 수 없을 때 기본처리
              Supplier<Expr> defaultCase = () -> new Number(0);
            
              return patternMatchExpr(e, binOpCase, numCase, defaultCase);
          }
          ```

### 기타 정보
* 함수형 그리고 참조 투명성과 관련된 두 가지 세부 주제
  * 하나는 효율성과 관련된 것
  * 다른 하나는 같은 결과를 반환하는 것과 관련된 것
* 또 두 개 이상의 함수를 인수로 받아서 다른 함수를 반환하는 메서드나 함수를 가리키는 콤비네이터(combinater) 개념
  * 콤비네이터는 자바 8 API에 여러 기능을 추가하도록 영감을 준 기능

* 캐싱 또는 기억화
  * 트리 형식의 토포로지(topology)를 갖는 네트워크 범위 내에 존재하는 노드의 수를 계산하는  
    computeNumberOfNodes(Range)라는 부작용 없는 메서드가 있다고 가정
    * 다행히 네트워크는 불변(구조가 변하지 않음)이지만 computeNumberOfNodes를 호출했을 때  
      구조체를 재귀적으로 탐색해야 하므로 노드 계산 비용이 비쌈
    * 게다가 이와 같은 계산을 반복해서 수행해야 할 것
    * 이때 참조 투명성이 유지되는 상황이라면 간단하게 추가 오버헤드를 피할 수 있는 방법이 생김
    * 표준적인 해결책으로 기억화(memorization)라는 기법
      * 기억화는 메서드에 래퍼로 캐시(HashMap 같은)를 추가하는 기법
      * 래퍼가 호출되면 인수, 결과 쌍이 캐시에 존재하는지 먼저 확인함
      * 캐시에 값이 존재하면 캐시에 저장된 값을 즉시 반환
      * 캐시에 값이 존재하지 않으면 computeNumberOfNodes를 호출해서 결과를 계산한 다음에  
        새로운 인수, 결과 쌍을 캐시에 저장하고 결과를 반환함
      * 엄밀히 따져서 캐싱, 즉 다수의 호출자가 공유하는 자료구조를 갱신하는 기법이므로  
        이는 순수 함수형 해결방식은 아니지만 감싼 버전의 코드는 참조 투명성을 유지할 수 있음
    * 캐싱을 사용하는 예제
      ```
      final Map<Range, Integer> numberOfNodes = new HashMap<>();
      	
      Integer computeNumberOfNodesUsingCache(Range range) {
          Integer result = numberOfNodes.get(range);
          if (result != null) {
              return result;
          }
        
          result = computeNumberOfNodes(range);
          numberOfNodes.put(range, result);
          return result;
      }
      ```
      * 자바 8에서는 computeIfAbsent라는 유용한 메서드를 Map 인터페이스에 추가함
        * 메서드 computeIfAbsent를 사용하면 좀 더 명료하게 코드를 구현할 수 있음
          ```
          Integer computeNumberOfNodesUsingCache(Range range) {
              return numberOfNodes.computeIfAbsent(range, this::computeNumberOfNodes);
          }   
          ```
      * 메서드 computeNumberOfNodesUsingCache는 참조 투명성을 가짐(computeNumberOfNodes도 참조 투명하다는 가정 하에)
        * 하지만 numberOfNodes는 공유된 가변 상태며 HashMap은 동기화(synchronized)되지 않았으므로 스레드 안정성이 없는 코드
        * HashMap 대신 잠금으로 보호되는 HashTable이나 잠금 없이 동시 실행을 지원하는 ConcurrentHashMap을 사용할 수 있지만  
          다중 코어에서 numberOfNodes를 동시에 호출하면 성능이 크게 저하될 수 있음
        * 이는 맵에 range가 있는지 찾는 과정과 인수, 결과 쌍을 맵에 추가하는 동작 사이에서 레이스 컨디션이 발생하기 때문
        * 즉, 여러 프로세스가 같은 값을 맵에 추가하기 위해 여러 번 계산하는 일이 발생할 수 있음
        * 가장 좋은 방법은 함수형 프로그래밍을 사용해서 동시성과 가변 상태가 만나는 상황을 완전히 없애는 것
        * 하지만 캐싱 같은 저수준 성능 문제는 해결되지 않음
        * 캐싱을 구현할 것인지 여부와는 별개로 코드를 함수형으로 구현했다면  
          우리가 호출하려는 메서드가 공유된 가변 상태를 포함하지 않음을 미리 알 수 있으므로 동기화 등을 신경 쓸 필요가 없어짐

* '같은 객체를 반환함'은 무엇을 의미하는가?
  * 이진트리 예제 다시 살펴보기
    * 앞선 예제 그림에서 변수 t는 기존 트리를 가리키는데 ``` fupdate("will", 26, t) ```를 호출하면 새로운 트리가 생성되고  
      변수 t2로 할당되는 부작용을 보여줌
    * t 그리고 t에서 접근할 수 있는 모든 자료구조는 변하지 않았다는 것을 보여줌
      * 앞서 수행한 코드를 다시 실행
      ``` t3 = fupdate("Will", 26, t); ```
      * 이번에는 t2 대신 t3라는 변수가 새로 생성된 세 개의 노드를 가리킴
      * 'fupdate는 참조 투명성을 갖는가?'라는 의문
      * 참조 투명성(referentially transparent)이란 '인수가 같다면 결과도 같아야 한다'라는 규칙을 만족함을 의미함
      * 여기서 t2와 t3는 서로 다른 참조
      * 즉 (t2 == t3)를 만족하지 않으므로 fupdate는 참조 투명성을 갖지 않는다고 결론내릴 수 있음
      * 그러나 자료구조를 변경하지 않는 상황에서 참조가 다르다는 것은 큰 의미가 없으며 t2와 t3가 논리적으로는 같다고 판단 가능
    * 참조 투명성이냐 아니냐를 토론하자면 얘기가 아주 길어짐
      * 일반적으로 함수형 프로그래밍에서는 데이터가 변경되지 않으므로 같다는 의미는 ==(참조가 같음)이 아니라  
        구조적인 값이 같다는 것을 의미함
      * 따라서 함수형 프로그래밍 관점에서 fupdate는 참조 투명성을 갖는다고 말할 수 있음

* 콤비네이터
  * 함수형 프로그래밍에서는 두 함수를 인수로 받아 다른 함수를 반환하는 등 함수를 조합하는 고차원(higher-order) 함수를 많이 사용하게 됨
  * 이처럼 함수를 조합하는 기능을 콤비네이터라고 부름
  * 예를 들어 CompletableFuture 클래스에는 thenCombine이라는 메서드가 추가됨
    * thenCombine 메서드는 CompletableFuture와 BiFunction 두 인수를 받아 새로운 CompletableFuture를 생성함
  * 다음은 함수 조합(function composition)이라는 개념을 보여주는 코드
    ```
    static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, B> f) {
        return x -> g.apply(f.apply(x));
    }
    ```
    * compose 함수는 f와 g를 인수로 받아 f의 기능을 적용한 다음에 g의 기능을 적용하는 함수를 반환함
      * 이 함수를 활용하면 콤비네이터로 내부 반복을 수행하는 동작을 정의할 수 있음
      * 데이터를 받아서 f에 연속적으로 n번 적용하는 루프가 있다고 가정
        * 이 함수 이름은 repeat로 f라는 함수를 인수로 받음
        * 예를 들어 다음과 같은 함수 호출시 출력 결과는?
          ``` repeat(3, (Integer x) -> 2*x); ```
          * 위 코드 실행 시 ``` x -> (2*(2*(2*x))) ``` 또는 ``` x -> 8*x ``` 라는 결과가 나옴
          * 코드 실행시 80이 출력됨
            ``` System.out.println(repeat(3, (Integer x) -> 2 * x).apply(10)); ```
        * repeat 메서드를 다음처럼 구현(루프를 한 번도 돌지 않는 상황은 예외적으로 처리함)
          ```
          static <A> Function<A, A> repeat(int n, Function<A, A> f) {
              return n == 0 ? x -> x : compose(f, repeat(n - 1, f));
          }
          ```
          * 이 개념 활용하면 반복 과정에서 전달되는 가변 상태 함수형 모델 등 반복 기능을 좀 더 다양하게 활용 가능함

### 정리
* 일급 함수란 인수로 전달하거나, 결과로 반환하거나, 자료구조에 저장할 수 있는 함수
* 고차원 함수란 한 개 이상의 함수를 인수로 받아서 다른 함수를 반환하는 함수
  * 자바에서는 comparing, andThen, compose 등의 고차원 함수를 제공함
* 커링은 함수를 모듈화하고 코드를 재사용할 수 있도록 지원하는 기법
* 영속 자료구조는 갱신될 때 기존 버전의 자신을 보존함
  * 결과적으로 자신을 복사하는 과정이 따로 필요하지 않음
* 자바의 스트림은 스스로 정의할 수 없음
* 게으른 리스트는 자바 스트림보다 비싼 버전으로 간주할 수 있음
  * 게으른 리스트는 데이터를 요청했을 때 Supplier를 이용해서 요소를 생성함
  * Supplier는 자료구조의 요소를 생성하는 역할을 수행함
* 패턴 매칭은 자료형을 언랩하는 함수형 기능
  * 자바의 switch문을 일반화할 수 있음
* 참조 투명성을 유지하는 상황에서는 계산 결과를 캐시할 수 있음
* 콤비네이터는 둘 이상의 함수나 자료구조를 조합하는 함수형 개념

### [quiz]
---

## chapter 20 - OOP와 FP의 조화 : 자바와 스칼라 비교
* 스칼라는 객체지향과 함수형 프로그래밍을 혼합한 언어
  * 보통 정적 형식의 프로그래밍 언어로 함수형의 기능을 수행하면서도 JVM에서 수행되는 언어
    * 자바 느낌을 원하는 프로그래머가 찾음
  * 스칼라는 자바에 비해 많은 기능을 제공
    * 스칼라는 복잡한 형식 시스템, 형식 추론, 패턴 매칭, 도메인 전용 언어를 단순하게 정의할 수 있는 구조 등을 제공함
    * 스칼라 코드에서는 모든 자바 라이브러리를 사용할 수 있음
* 자바에서 함수형 프로그래밍을 적용하는 방법을 설명
  * 자바와 마찬가지로 스칼라는 컬렉션을 함수형으로 처리하는 개념(스트림과 비슷한 연산), 일급 함수, 디폴트 메서드 등을 제공함
  * 하지만 스칼라는 자바에 비해 더 다양하고 심화된 함수형 기능을 제공함
  * 스칼라와 자바에 적용된 함수형의 기능을 살펴보면서 자바의 한계가 무엇인지 확인
* 자바의 새로운 기능과 스칼라의 기능을 비교하는 데 초점을 맞춤
  * 스칼라를 이용하면 자바에 비해 더 간결하고 가독성이 좋은 코드를 구현할 수 있다는 사실

### 스칼라 소개
* 각각 명령형과 함수형으로 구현된 'Hello World' 예제
  * 스칼라가 지원하는 리스트, 집합, 맵, 스트림, 튜플, 옵션 등의 자료구조와 자바의 자료구조 비교
  * 자바의 인터페이스를 대체하는 스칼라의 트레이트
    * 트레이트는 객체를 인스턴스화할 때 메서드 상속 기능을 지원

* Hello beer
  * 고전 'Hello World' 예제를 맥주(Beer)로 바꿈
    ```
    Hello 2 bottles of beer
    Hello 3 bottles of beer
    Hello 4 bottles of beer
    Hello 5 bottles of beer
    Hello 6 bottles of beer
    ```

* 명령형 스칼라
  * 명령형으로 위 문장을 출력하는 스칼라 프로그램
    ```
    object Beer {
    
        def main (args: Array[String]) {
            var n : Int = 2
            while (n <= 6) {
                println(s"Hello ${n} bottles of beer") // 문자열 보간법
                n += 1
            }
        }
    }
    ```
    * 실행 참조
      * https://docs.scala-lang.org/getting-started/index.html
    * 문자열을 인수로 받는 main 메서드 (자바의 String s와 달리 s : String 이라는 문법 구조를 가짐)
    * main 메서드는 반환값이 없어 자바에서는 void를 사용했겠지만 스칼라에서는 아무것도 선언하지 않음
      * 보통 스칼라의 비재귀 메서드에서는 반환형식을 추론할 수 있으므로 명시적으로 반환형식을 정의하지 않아도 됨
    * object 선언 과정
      * 자바에서는 클래스 내에 main 메서드 선언
      * 하지만 스칼라에서는 object로 직접 싱글턴 객체를 만들 수 있음
      * 위 예제에서는 object로 Beer 클래스를 정의하고 동시에 인스턴스화
      * 한 번에 단 하나의 인스턴스만 생성됨
      * 스칼라 언어의 기능을 이용해 첫 번째 고전 디자인 패턴(싱글턴)을 보여줌
      * object 내부에 선언된 메서드는 정적 메서드로 간주할 수 있음
        * main 메서드 시그니처에 명시적인 static이 없는 것도 이것 때문
    * main 메서드 바디
      * main 바디는 자바와 비슷해 보이지만 구문이 세미콜론으로 끝나지 않음
        * 세미콜론은 선택사항
      * while 루프의 바디에서는 가변 변수 n을 증가시킴
      * n값이 바뀔 때마다 미리 정의된 메서드 println 화면에 n값을 출력함
      * println 메서드는 스칼라의 문자열 보간법(string interplation)이라는 기능을 보여줌
        * 문자열 보간법은 문자열 자체에 변수와 표현식을 바로 삽입하는 기능
        * 위 코드에서 s"Hello ${n} bottles of beer"라는 문자열에 변수 n을 직접 사용
        * 자바에서는 "Hello " + n + " bottles of beer"처럼 명시적으로 문자열과 변수를 연결해야 함

* 함수형 스칼라
  * 자바 예제
    ```
    public class Foo {
    
    	public static void main(String[] args) {
    		IntStream.rangeClosed(2, 6)
    				.forEach(n -> System.out.println("Hello " + n + " bottles of beer"));
    	}
    }
    ```
  * 스칼라 예제
    ```
    object Beer {
        def main(args: Array[String]) {
            2 to 6 foreach {n => println(s"Hello ${n} bottles of beer")}
        }
    }
    ```
  * 스칼라 코드는 자바보다 간결
  * 우선 스칼라에서는 ``` 2 to 6 ```이라는 표현식으로 숫자 범위를 만들 수 있음
  * 2는 Int 형식 객체
  * 스칼라에서는 모든 것이 객체
  * 자바와 달리 기본형이 없음
  * 스칼라는 자바보다 완전한 객체지향
  * 따라서 ``` 2.to(6) ```이라고 구현할 수도 있음
  * 하지만 인수 하나를 받는 메서드는 (위 예제처럼 점을 제외한) 인픽스 형식으로 구현할 수 없음
  * foreach는 자바 forEach와 비슷 (스칼라는 소문자 e, 자바는 대문자 E)
  * foreach는 범위에 사용할 수 있는 메서드로 람다 표현식을 인수로 받아 각 요소에 적용 (이번에도 인픽스 개념 사용)
  * 람다 표현식 문법은 자바와 비슷 (다만 ``` -> ``` 대신 ``` => ``` 사용)
  * 처음 예제의 while 루프처럼 변수를 갱신하지 않으므로 위 예제는 함수형
    * 스칼라에서 '익명 함수'나 '클로저' 등의 용어는 자바의 람다 표현식과 비슷한 의미

* 기본 자료구조 : 리스트, 집합, 맵, 튜플, 스트림, 옵션
  * 대부분의 프로그래머는 자료를 다루고 저장하는 기능을 사용해야 함
  * 컬렉션 만들기
    * 맵을 만드는 코드
      ```
      val authorsToAge = Map("Raoul" -> 23, "Mario" -> 40, "Alan" -> 53)
      ```
      * ``` -> ``` 문법으로 키를 값에 대응시켜 맵을 만들 수 있음
      * 자바 코드와 비교
        ```
        Map<String, Integer> authorsToAge = new HashMap<>();
        authorsToAge.put("Raoul", 23);
        authorsToAge.put("Mario", 40);
        authorsToAge.put("Alan", 53);
        ```
      * 변수 authorsToAge의 형식을 지정하지 않음
        * ``` val authorsToAge : Map[String, Int] ```처럼 명시적으로 형식을 지정할 수 있지만  
          스칼라는 자동으로 변수형을 추론하는 기능이 존재
        * 스칼라는 코드를 정적으로 확인
          * 즉 모든 변수의 형식은 컴파일 할 때 결정됨
      * ``` var ``` 대신 ``` val ```이라는 키워드를 사용함
        * ``` val ``` 변수가 읽기 전용
          * 변수에 값을 할당할 수 없음을 의미 (자바의 final과 같음)
        * ``` var ``` 라는 키워드는 읽고 쓸 수 있는 변수를 가리킴
    * 리스트(단방향 연결리스트), 집합(중복된 요소가 없는)을 만드는 코드
      ```
      val authors = List("Raoul", "Mario", "Alan")
      val numbers = Set(1, 1, 2, 3, 5, 8)
      ```
      * authors 변수는 세 개의 요소, numbers 변수는 다섯 개의 요소 포함
  * 불변과 가변
    * 지금까지 만든 컬렉션은 기본적으로 불변(immutable)
      * 즉, 일단 컬렉션을 만들면 변경할 수 없음
      * 컬렉션이 불변이므로 프로그램에서 언제 컬렉션을 사용하든 항상 같은 요소를 갖게 되고 함수형 프로그래밍에서 유용하게 활용됨
    * 스칼라의 불변 컬렉션 갱신
      * 영속이라는 용어를 스칼라의 컬렉션에도 적용할 수 있음
      * 영속(persistent) - 저장된 값이 다른 누군가에 의해 영향을 받지 않는 상태
    * 기존 버전과 가능한 한 많은 자료를 공유하는 새로운 컬렉션을 만드는 방법으로 자료구조를 갱신함
    * 결과적으로 암묵적인 데이터 의존성을 줄일 수 있음
    * 언제, 어디서 컬렉션(또는 다른 공유된 자료구조 등)을 갱신했는지 크게 신경쓰지 않아도 됨
    * 예제 코드
      ```
      val numbers = Set(2, 5, 3)
      val newNumbers = numbers + 8 // 여기서 +는 집합에 8을 더하는 메서드의 연산 결과로 새로운 Set 객체를 생성함
      println(newNumbers) // 2, 5, 3, 8
      println(numbers) // 2, 5, 3
      ```
      * 숫자 집합은 바뀌지 않음
        * 대신 새로운 요소가 추가된 새로운 집합이 생성됨
    * 스칼라에서는 불변 컬렉션만 사용하도록 강제하는 것은 아니며 단지 불변성을 쉽게 적용할 수 있도록 도와주는 것
    * 패키지 ``` scala.collection.mutable ```에서는 가변 버전의 컬렉션을 제공
    * 변경불가와 불변
      * 자바에서는 변경불가(unmodifiable) 컬렉션을 만드는 다양한 방법을 제공
      * 다음 코드에서 newNumbers는 numbers 집합에서 읽기 전용 값으로 요소를 추출한 변수
        ```
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> newNumbers = Collections.unmodifiableSet(numbers);
        ```
      * 따라서 newNumbers 변수에는 새로운 요소를 추가할 수 없음
        * 하지만 변경불가 컬렉션은 값을 고칠 수 있는 컬렉션의 래퍼의 불과
        * 즉, numbers 변수를 이용하면 새로운 요소를 추가할 수 있음
      * 반면 불변(immutable) 컬렉션은 얼마나 많은 변수가 컬렉션을 참조하는가와 관계없이 컬렉션을 절대 바꿀 수 없다는 점이 다름
      * 영속 자료구조를 변경하면 자신의 기존 버전을 보존
        * 결과적으로 영속 자료구조를 변경하면 갱신된 새로운 자료구조가 생성됨
  * 컬렉션 사용하기
    * 스칼라의 컬렉션 동작은 스트림 API와 비슷
      * 예를 들어 filter와 map이 등장
        ```
        val fileLines = Source.fromFile("data.txt").getLines.toList()
        val linesLongUpper = fileLines.filter(l => l.length() > 10).map(l => l.toUpperCase())
        ```
        * 첫 번째 행은 기본적으로 파일의 모든 행을 문자열 행으로 변환함
          * 자바의 Files.readAllLines와 비슷
        * 두 번째 행은 두 연산의 파이프라인을 생성함
          * filter 연산 : 길이가 10 이상인 행만 선택
          * map 연산 : 긴 행을 대문자로 변환함
        * 다음처럼 구현 가능
          ```
          val linesLongUpper = fileLines filter (_.length() > 10) map(_.toUpperCase())
          ```
          * 위 코드에서는 인픽스 개념과 언더스코어를 사용
            * 언더스코어는 인수로 대치됨
            * 즉, 이 코드에서 ``` _.length()```는 ``` l => l.length() ```로 해석할 수 있음 
            * filter와 map으로 전달된 함수에서 언더스코어는 처리되는 행으로 바운드 됨
    * 스칼라의 컬렉션 API에서는 이 외에도 많은 유용한 연산을 제공함
      * 참조 : https://docs.scala-lang.org/overviews/collections/introduction.html
      * 스칼라의 API는 스트림 API에 비해 풍부한 기능을 제공함
        * 예를 들면 두 리스트의 요소를 합치는 지핑(zipping)이라는 연산 제공
    * 자바에서는 스트림의 parallel을 호출해서 파이프라인을 병렬로 실행할 수 있음
      * 스칼라도 par라는 메서드로 비슷한 기능을 제공
        ```
        val linesLongUpper = fileLines.par filter (_.length() > 10) map(_.toUpperCase())
        ```
  * 튜플
    * 자바 프로그래머를 괴롭히는 기능 중 하나인 튜플(tuple)
      * 예를 들어 사람의 이름과 전화번호를 그룹화하는 튜플을 만들려고 함
      * 새로 클래스를 만들고 객체로 인스턴스화하지 않고  
        ``` ("Raoul, " + 44 007007007), ("Alan, " + 44 003133700) ``` 같은 식으로 바로 튜플을 만들면 좋을 것
      * 안타깝게도 자바는 튜플을 지원하지 않음
        * 따라서 직접 자료구조를 만들어야 함
        * 간단한 Pair 클래스 예제
          ```
          public class Pair<X, Y> {
              public final X x;
              public final Y y;
             
              public Pair(X x, Y y) {
                  this.x = x;
                  this.y = y;
              }
          }
          ```
          * 선언 후 명시적으로 클래스를 인스턴스화
            ```
            Pair<String, String> raoul = new Pair<>("Raoul", "+ 44 007007007");
            Pair<String, String> alan = new Pair<>("Alan", "+ 44 003133700");
            ```
            * 두 개의 요소를 갖는 쌍은 구현, 세 개의 요소를 그룹화한 튜플은 어떻게 해야 할까?
              * 이를 일반화해서 임의 요소를 그룹화하는 튜플은 어떻게 만들 수 있을까?
              * 결과적으로 자바로 튜플을 구현한다는 것은 쉽지 않은 작업, 프로그램의 가독성과 유지보수성을 떨어뜨림
    * 스칼라는 임의 크기(최대 23개 요소를 그룹화하는)의 튜플을 제공함
      * 예를 들어 다음과 같은 방법으로 튜플을 만들 수 있음
        ```
        val book = (2018 "Modern Java in Action", "Manning") // (int, String, String) 형식의 튜플
        val numbers = (42, 1337, 0, 3, 14) // (int, int, int, int) 형식의 튜플
        ```
      * _1, _2 등의 접근자로 튜플의 요소에 접근할 수 있음
        * 접근자는 1부터 시작함
        ```
        println(book._1)
        println(numbers._4)
        ```
  * 스트림
    * 리스트, 집합, 맵, 튜플은 적극적으로(즉시) 평가되었음
      * 지금까지의 자바 스트림은 요청할 때만 평가되었음(게으른 평가)
      * 이러한 특성 덕분에 메모리 오버플로 없이 무한 시퀀스를 표현할 수 있었음
    * 스칼라에서도 스트림이라는 게으르게 평가되는 자료구조를 제공
      * 스칼라의 스트림은 자바의 스트림보다 다양한 기능을 제공
      * 스칼라의 스트림은 이전 요소가 접근할 수 있도록 기존 계산값을 기억함
      * 또한 인덱스를 제공, 리스트처럼 인덱스로 스트림의 요소에 접근할 수 있음
      * 이러한 기능이 추가되면서 스칼라의 스트림은 자바의 스트림에 비해 메모리 효율성이 조금 떨어짐
      * 이전 요소를 참조하려면 요소를 '기억(캐시)'해야 하기 때문
  * 옵션
    * 스칼라의 Option은 자바의 Optional과 같은 기능 제공
    * 사용자는 메서드의 시그니처만 보고도 Optional 값이 반환될 수 있는지 여부를 알 수 있음
    * null 대신 Optional을 사용하면 null 포인터 예외를 방지할 수 있음
    * 사람의 나이가 최소 나이보다 클 때 보험회사 이름을 반환하는 코드에 Optional 활용
      ```
      public String getCarInsuranceName(Optional<Person> person, int minAge) {
          return person
          		.filter(p -> p.getAge() >= minAge)
          		.flatMap(Person::getCar)
          		.flatMap(Car::getInsurance)
          		.map(Insurance::getName)
          		.orElse("Unknown");
      }
      ```
      * 스칼라에서는 Optional과 비슷한 방식으로 Option을 사용할 수 있음
        ```
        def getCarInsuranceName(person: Option[Person], minAge: Int) =
            person
                .filter(_.getAge() >= minAge)
                .flatMap(_.getCar)
                .flatMap(_.getInsurance)
                .map(_.getName)
                .getOrElse("Unknown")
        ```
      * 자바에서는 orElse라는 메서드를 사용, 스칼라에서는 getOrElse라는 메서드 사용라는 점을 제외하면 두 코드는 구조가 같음
        * 자바의 개념을 곧바로 다른 프로그래밍 언어에 적용할 수 있음
        * 안타깝게도 자바와의 호환성 때문에 스칼라에도 null이 존재함
        * 하지만 되도록 null을 사용하지 않는 것이 좋음
      * 위 코드에서 괄호가 있는 ``` _.getCar() ``` 대신 ``` _.getCar ```를 사용
        * 스칼라에서는 인수가 없는 메서드를 호출할 때 괄호를 생략할 수 있음

### 함수
* 스칼라의 함수는 어떤 작업을 수행하는 일련의 명령어 그룹
  * 명령어 그룹을 쉽게 추상화할 수 있는 것도 함수 덕분이며 동시에 함수는 함수형 프로그래밍의 중요한 기초
* 자바에서는 클래스와 관련된 함수에 메서드라는 이름이 사용됨
  * 익명 함수의 일종인 람다 표현식
  * 스칼라에서는 자바에 비해 풍부한 함수 기능을 제공
    * 함수 형식
      * 자바 함수 디스크립터의 개념을 표현하는 편의 문법
      * 즉 함수형 인터페이스에 선언된 추상 메서드의 시그니처를 표현하는 개념
    * 익명 함수
      * 자바의 람다 표현식과 달리 비지역 변수 기록에 제한을 받지 않음
    * 커링 지원
      * 커링은 여러 인수를 받는 함수를 일부 인수를 받는 여러 함수로 분리하는 기법

* 스칼라의 일급 함수
  * 스칼라의 함수는 일급값(first-class value)
    * 즉, Integer나 String처럼 함수를 인수로 전달하거나, 결과로 반환하거나, 변수에 저장할 수 있음
    * 자바의 메서드 참조와 람다 표현식도 일급 함수
  * 스칼라의 일급 함수 예제
    * 사람들이 우리에게 보낸 트윗 문자열 리스트가 있다고 가정
    * 이때 트윗에 Java가 포함되어 있거나 짧은 문자열 등의 조건으로 트윗을 필터링하려 함
    * 이런 두 가지 조건을 프레디케이트(predicate)로 표현할 수 있음
      * predicate는 Boolean을 반환하는 함수
      ```
      def isJavaMentioned(tweet: String) : Boolean = tweet.contains("Java")
      def isShortTweet(tweet: String) : Boolean = tweet.length() < 20
      ```
      * 이들 메서드를 스칼라에서 기본 제공하는 filter로 바로 전달 가능 (자바에서 메서드 참조로 이들을 전달했던 것처럼)
        ```
        val tweets = List(
            "I love the new features in Java 8",
            "How's it going?",
            "An SQL query walks into a bar, sees two tables and says 'Can I join you?'"
        )
        tweets.filter(isJavaMentioned).foreach(println)
        tweets.filter(isShortTweet).foreach(println)
        ```
        * 스칼라에서 제공하는 내장 메서드 filter의 시그니처
          ``` def filter[T](p: (T) => Boolean): List[T] ```
          * 위 코드에서 파라미터 p의 형식은 (T) => Boolean
          * 자바에서는 함수형 인터페이스를 사용
          * ``` (T) => Boolean ```은 T라는 형식의 객체를 받아 Boolean을 반환함을 의미
          * 자바로는 ``` Predicate<T> ``` 또는 ``` Function<T, Boolean> ```과 같은 의미
          * 따라서 p의 형식은 isJavaMentioned, isShortTweet의 시그니처와 일치하므로 이들을 filter의 인수로 전달할 수 있음
          * 자바 언어 설계자는 기존 버전의 언어와 일관성(consistency)을 유지할 수 있도록 이와 같은 함수 형식을 지원하지 않기로 결정함
            * 언어의 새로운 버전에 새로운 문법을 너무 많이 추가하면 코드를 분석하는 데 부담이 발생한다는 이유도 있음

* 익명 함수와 클로저
  * 스칼라도 익명 함수(anonymous function)의 개념을 지원
  * 스칼라는 람다 표현식과 비슷한 문법을 제공
  * 트윗이 긴지를 확인하는 익명 함수를 isLongTweet이라는 변수로 할당하는 예제
    ```
    val isLongTweet : String => Boolean = // String을 Boolean으로 반환하는 함수 형식의 변수
        (tweet : String) => tweet.length() > 60 // 익명 함수
    ```
    * 자바의 람다 표현식으로 함수형 인터페이스의 인스턴스를 만들 수 있음 (스칼라도 비슷한 방식을 지원)
    * 위 코드는 apply 메서드의 구현을 제공하는 scala.Function1(한 개의 인수를 받는 함수) 형식의 익명 클래스를 축약한 것
      ```
      val isLongTweet : String => Boolean =
          new Function1[String, Boolean] {
              def apply(tweet: String): Boolean = tweet.length() > 60 }
      ```
      * isLongTweet 변수는 Function1 형식의 객체를 저장하므로 다음처럼 apply 메서드를 호출할 수 있음
        ``` isLongTweet.apply("A very short tweet") // false를 반환 ```
      * 자바로는 다음처럼 구현
        ```
        Function<String, Boolean> isLongTweet = (String s) -> s.length() > 60;
        boolean long = isLongTweet.apply("A very short tweet");
        ```
    * 자바에서는 람다 표현식을 사용할 수 있도록 Predicate, Function, Consumer 등의 내장 함수형 인터페이스 제공
      * 마찬가지로 스칼라는 트레이트를 지원
      * 스칼라에서는 Function0(인수가 없으며 결과를 반환)에서 Function22(22개 인수를 받음)를 제공 (모두 apply 메서드를 정의)
    * 스칼라에서 보통 함수를 호출하는 것처럼 applly 메서드를 호출할 수 있음
      ``` isLongTweet("A very short tweet") // false를 반환 ```
      * 컴파일러는 f(a)라는 호출을 자동으로 f.apply(a)로 반환함
      * 즉, 일반적으로 컴파일러는 f(a1, ..., an)을 f.apply(a1, ..., an)으로 변환할 수 있으며  
        여기서 f는 apply 메서드를 지원하는 객체
        * apply의 인수 개수는 제한이 없음
  * 클로저
    * 클로저(closure)란 함수의 비지역 변수를 자유롭게 참조할 수 있는 함수의 인스턴스
    * 하지만 자바의 람다 표현식에는 람다가 정의된 메서드의 지역 변수를 고칠 수 없다는 제약이 있음
      * 이들 변수는 암시적으로 final로 취급됨
      * 즉 람다는 변수가 아닌 값을 닫음
    * 스칼라의 익명 함수는 값이 아니라 변수를 캡처할 수 있음
      * 예제 코드
        ```
        def main(args: Array[String]) {
            var count = 0
            val inc = () => count += 1
            inc()          // count를 캡처하고 증가시키는 클로저
            println(count) // 1 출력
            inc()
            println(count) // 2 출력
        }
        ```
      * 자바로 구현한 코드 (count는 암시적으로 final이 되므로 컴파일 에러 발생)
        ```
        public static void main(String[] args) {
            int count = 0;
            Runnable inc = () -> count += 1; // count는 명시적으로 final 또는 final에 준하는 변수여야 함
            inc.run();
            System.out.println(count);
            inc.run();
        }
        ```
      * 프로그램을 쉽게 유지보수하고 병렬화할 수 있도록 변화를 피해야 함
        * 따라서 꼭 필요할 때만 클로저 기능을 사용하는 것이 바람직

* 커링
  * x, y라는 두 인수를 가진 f라는 함수가 있을 때 이는 하나의 인수를 받는 g라는 함수  
    그리고 g라는 함수는 다시 나머지 인수를 받는 함수로 반환되는 상황으로 볼 수 있음
    * 여러 인수를 가진 함수를 커링으로 일반화할 수 있음
    * 즉, 여러 인수를 받는 함수를 인수의 일부를 받는 여러 함수로 분할할 수 있음
    * 스칼라에서는 기존 함수를 쉽게 커리할 수 있는 방법을 제공함
    * 자바 예제
      * 두 정수를 곱하는 간단한 메서드 정의
      ```
      static int multiply(int x, int y) {
          return x * y;
      }
      int r = multiply(2, 10);
      ```
      * 이 함수는 전달된 모든 인수를 사용
      * 다른 함수를 반환하도록 위의 multiply 메서드를 분할
        ```
        static Function<Integer, Integer> multiplyCurry(int x) {
            return (Integer y) -> x * y;
        }
        ```
        * multiplyCurry가 반환하는 함수는 x와 인수 y를 곱한 값(정수값)을 캡처
        * 다음처럼 multiplyCurry를 연결해서 각 요소에 2를 곱할 수 있음
          ```
          Stream.of(1, 3, 5, 7)
              .map(multiplyCurry(2))
              .forEach(System.out::println);
          ```
          * 위 코드를 실행하면 2, 6, 10, 14가 출력됨
          * map은 인수로 Function을 받고 multiplyCurry가 Function을 반환하므로 위 코드는 문제없이 작동함
    * 자바에서 함수를 커리 형식으로 분할하려면 조금 복잡한 과정을 거쳐야 함
      * 특히 함수의 인수가 많을수록 복잡해짐
      * 스칼라는 이 과정을 자동으로 처리하는 특수 문법 제공
      * 스칼라에서는 전형적인 multiply 메서드를 다음처럼 정의할 수 있음
        ```
        def multiply(x: Int, y: Int) = x * y
        var r = multiply(2, 10)
        ```
        * 커리 결과
          ```
          def multiplyCurry(x: Int)(y: Int) = x * y   // 커리된 함수 정의
          var r = multiplyCurry(2)(10)                // 커리된 함수 호출
          ```
          * ``` (x: Int)(y: Int) ``` 같은 문법으로 multiplyCurry 메서드는 Int 파라미터 하나를 포함하는 인수 리스트 둘을 받음
          * 반면 multiply는 Int 파라미터 둘로 구성된 리스트 하나를 인수로 받음
          * multiplyCurry를 호출하면 어떻게 될까?
          * Int(파라미터 x) 하나로 multiplyCurry를 처음 호출 (multiplyCurry(2))하면  
            파라미터 y를 인수로 받는 다른 함수를 반환하며 이를 x 캡처값(여기서는 2)에 곱함
          * 모든 인수를 사용하지 않았으므로 이 상황을 '함수가 부분 적용되었다'고 표현함
          * 두 번째로 함수를 호출하면 x와 y를 곱함
          * 즉 multiplyCurry를 처음 호출한 결과를 내부 변수에 저장했다가 재사용함
            ```
            val multiplyByTwo : Int => multiplyCurry(2)
            val r = multiplyByTwo(10)                    // 20
            ```
            * 자바와 달리 스칼라에서는 커리된 함수를 직접 제공할 필요가 없음
            * 스칼라에서는 함수가 여러 커리된 인수 리스트를 포함하고 있음을 가리키는 함수 정의 문법을 제공하기 때문

### 클래스와 트레이트
* 스칼라의 클래스와 인터페이스는 자바에 비해 더 유연함을 제공

* 간결성을 제공하는 스칼라의 클래스
  * 스칼라는 완전한 객체지향 언어이므로 클래스를 만들고 객체로 인스턴스화 할 수 있음
    * 스칼라는 자바에서 클래스를 만들고 인스턴스화하는 방법과 문법적으로 비슷한 구조를 제공
    * 예를 들어 다음은 스칼라의 Hello 클래스 정의 코드
      ```
      class Hello {
          def sayThankYou() {
              println("Thanks for reading our book")
          }
      }
      val h = new Hello()
      h.sayThankYou()
      ```
  * 게터와 세터
    * 필드 리스트만 정의하는 자바 클래스를 만들어본 적이 있는가?
      * 이런 클래스에는 생성자와 필드 수 만큼의 게터와 세터가 선언됨
      * 엔터프라이즈 자바 앱의 클래스에서 흔히 이 같은 수많은 양의 코드를 발견할 수 있음
      * Student 클래스 예시
        ```
        public class Student {
        	private String name;
        	private int id;
        
        	public String getName() {
        		return name;
        	}
        
        	public Student setName(String name) {
        		this.name = name;
        		return this;
        	}
        
        	public int getId() {
        		return id;
        	}
        
        	public Student setId(int id) {
        		this.id = id;
        		return this;
        	}
        }
        ```
        * 자바에서는 생성자를 정의하고 필드를 초기화했으며 두 개의 게터와 두 개의 세터도 정의함
          * 간단한 클래스인데도 20행이 넘는 코드가 필요함
          * IDE와 기타 도구를 이용해 자동으로 코드를 생성할 수 있음
          * 하지만 실제 비즈니스 로직을 처리하는 데 별 도움이 되지 않는 수많은 코드가 존재한다는 사실은 변하지 않음
        * 스칼라에서는 생성자, 게터, 세터가 암시적으로 생성되므로 코드가 훨씬 단순해짐
          ```
          class Student(var name: String, var id: Int)
          val s = new Student("Raoul", 1)  // Student 객체 초기화
          println(s.name)                  // 이름을 얻어 Raoul 출력
          s.id = 1337                      // id 설정
          println(s.id)                    // 1337 출력
          ```

* 스칼라 트레이트와 자바 인터페이스
  * 스칼라는 트레이트라는 유용한 추상 기능 제공
    * 스칼라의 트레이트는 자바의 인터페이스를 대체
    * 트레이트로 추상 메서드와 기본 구현을 가진 메서드 두 가지를 모두 정의할 수 있음
    * 자바의 인터페이스처럼 트레이트는 다중 상속을 지원하므로 자바의 인터페이스와 디폴트 메서드 기능이 합쳐진 것으로 이해할 수 있음
    * 트레이트는 추상 클래스와 같다고 이해할 수 없음
      * 트레이트는 클래스와 달리 다중 상속될 수 있기 때문
      * 하지만 자바 8에서는 디폴트 메서드 덕분에 동작을 다중 상속할 수 있게 되었지만  
        스칼라의 트레이트와는 달리 상태는 다중 상속할 수 없음
    * Sized라는 트레이트 정의
      * Sized라는 트레이트는 size라는 가변 필드와 기본 구현을 제공하는 isEmpty 메서드를 포함
        ```
        trait Sized {
            var size : Int = 0        // size 필드
            def isEmpty() = size == 0 // 기본 구현을 제공하는 isEmpty 메서드
        }
        ```
        * 트레이트를 클래스와 조합해 선언 가능
          * 항상 0의 크기를 갖는 Empty 클래스를 정의하는 예제
            ```
            class Empty extends Sized      // 트레이트 Sized에서 상속받은 클래스
            println(new Empty().isEmpty()) // true 출력
            ```
        * 객체 트레이트는 인스턴스화 과정에서도 조합할 수 있음 (하지만 조합 결과는 컴파일할 때 결정됨)
          * 예를 들어 Box 클래스를 만든 다음에 어떤 Box 인스턴스는 트레이트 Sized가 정의하는 동작을 지원하도록 결정할 수 있음
            ```
            class Box
            val b1 = new Box() with Sized // 객체를 인스턴스화할 때 트레이트를 조합함
            println(b1.isEmpty())         // true 출력
            val b2 = new Box()
            b2.isEmpty()                  // 컴파일 에러 : Box 클래스 선언이 Sized를 상속하지 않았음
            ```
    * 같은 시그니처를 갖는 메서드나 같은 이름을 갖는 필드를 정의하는 트레이트를 다중 상속하면  
      자바의 디폴트 메서드와 비슷한 제한을 두어 문제를 해결

### 정리
* 자바와 스칼라는 객체지향과 함수형 프로그래밍 모두를 하나의 프로그래밍 언어로 수용
  * 두 언어 모두 JVM에서 실행되며 넓은 의미에서 상호운용성을 가짐
* 스칼라는 자바처럼 리스트, 집합, 맵, 스트림, 옵션 등의 추상 컬렉션을 제공
  * 튜플도 추가로 제공
* 스칼라는 자바에 비해 풍부한 함수 관련 기능을 제공
  * 함수 형식, 지역 변수에 접근할 수 있는 클로저, 내장 커링 형식 등을 지원
* 스칼라의 클래스는 암묵적으로 생성자, 게터, 세터를 제공함
* 스칼라는 트레이트를 지원함
  * 트레이트는 필드와 디폴트 메서드를 포함할 수 있는 인터페이스

### [quiz]
---

## chapter 21 - 결론 그리고 자바의 미래

### 자바 8의 기능 리뷰
* 자바 8에 큰 변화가 생긴 이유 (우연이 아니라 다분히 계획적인 변화)
  * 커다란 두 가지 추세
    * 한 가지 추세는 멀티코어 프로세서의 파워를 충분히 활용해야 한다는 것
      * 무어의 법칙에 따라 실리콘 기술이 발전하면서 개별 CPU 코어의 속도가 빨라지고 있음
      * 즉, 코드를 병렬로 실행해야 더 빠르게 코드를 실행할 수 있음
    * 데이터 소스를 이용해 주어진 조건과 일치하는 모든 데이터를 추출, 결과에 어떤 연산을 적용하는 등  
      선언형으로 데이터를 처리하는 방식 즉 간결하게 데이터 컬렉션을 다루는 추세
      * 간결하게 데이터 컬렉션을 처리하려면 불변값을 생산할 수 있는 불변 객체와 불변 컬렉션이 필요함
  * 필드를 변화하고 반복자를 적용하는 기존의 객체지향, 명령형 언어로는 이러한 추세를 만족시키기 어려움
    * 한 코어에서 데이터를 변화시키고 다른 코어에서 이 데이터를 읽으려면 비싼 비용을 치러야 할 뿐 아니라 잠금 관련 버그도 많이 발생
    * 한편 기존 객체를 반복하면서 변화시키는 방식에만 익숙해져있는 개발자에게는 스트림 같은 프로그래밍 용어가 낯설게 느껴질 것
    * 하지만 함수형 프로그래밍을 사용하면 이 두가지 추세를 모두 달성할 수 있음
    * 자바 8의 모습이 기존 자바와 크게 달라진 이유도 바로 이것 때문

* 동작 파라미터화(람다와 메서드 참조)
  * 재사용할 수 있는 filter 같은 메서드를 구현하려면 filter 메서드의 인수가 필터링 조건을 받도록 만들어야 함
    * 경험이 많은 자바 프로그래머는 기존의 자바로도 이런 기능을 구현할 수 있음
      * 필터링 조건을 메서드 내에서 클래스로 감싼 다음에 해당 클래스의 인스턴스를 전달하는 방법
    * 하지만 보통 상당히 복잡한 코드를 구현해야 하며 따라서 유지보수하는 것도 쉽지 않음
  * 함수형 프로그래밍에서 지원하는 메서드로 코드 블록을 전달하는 기법을 자바 8에서도 제공
    * ``` apple -> apple.getWeight() > 150 ``` 같은 람다 코드를 전달할 수 있음
    * ``` Apple::isHeavy ``` 같은 기존 메서드의 메서드 참조를 전달할 수 있음
  * 메서드로 전달되는 값은 ``` Function<T, R> ```, ``` Predicate<T> ```, ``` BiFunction<T, U, R> ``` 등의 형식을 가지며  
    메서드를 수신한 코드에서는 apply, test 등의 메서드로 코드를 실행할 수 있음
    * 람다가 전체적인 기능에서 꼭 필요한 개념은 아니지만  
      자바 8의 새로운 스트림 API에서는 람다를 효과적으로 사용하면서 자바의 핵심 기능으로 힘을 더해줌

* 스트림
  * 자바의 컬렉션 클래스, 반복자, for-each 구문은 오랫동안 사용된 기능
    * 자바 8 설계자는 기존의 컬렉션에 람다를 활용한 filter, map 등의 메서드를 추가해  
      데이터베이스 질의 같은 기능을 제공하는 비교적 쉬운 방법을 선택할 수 있었음
    * 그럼에도 자바 8 설계자는 그렇게 하지 않았음
    * 대신 자바 8 설계자는 완전히 새로운 스트림 API를 만듦
  * 컬렉션에 어떤 문제가 있으며 스트림과 비슷한 점과 다른 점은 무엇일까?
    * 예를 들어 큰 컬렉션에 세 가지 연산을 적용한다고 가정
      * 우선 컬렉션의 필드를 더할 수 있는 객체를 매핑하고, 어떤 조건을 만족하는 합계를 필터링한 다음에, 결과를 정렬해야 함
      * 따라서 컬렉션을 각각 세 번 탐색해야 함
    * 스트림 API는 이들 연산을 파이프라인이라는 게으른 형식의 연산으로 구성함
    * 그리고 한 번의 탐색으로 파이프라인의 모든 연산을 수행함
    * 큰 데이터 집합일수록 스트림의 데이터 처리 방식이 효율적이며,  
      또한 메모리 캐시 등의 관점에서도 커다란 데이터 집합일수록 탐색 횟수를 최소화하는 것이 아주 중요함
  * 또한 멀티코어 CPU를 활용해 병렬로 요소를 처리하는 기능도 매우 중요한 점
    * 스트림의 parallel 메서드는 스트림을 병렬로 처리하도록 지정하는 역할을 함
    * 상태 변화는 병렬성의 가장 큰 걸림돌
    * 따라서 함수형 개념은 map, filter 등의 연산을 활용하는 스트림의 병렬 처리의 핵심으로 자리 잡음
      * 부작용이 없는 연산, 람다와 메서드 참조로 파라미터화된 메서드, 외부 반복 대신 내부 반복을 지원하는 메서드 등이 함수형 개념의 핵심

* CompletableFuture 클래스
  * 자바 5부터 Future 인터페이스를 제공
    * Future를 이용하면 여러 작업이 동시에 실행될 수 있도록 다른 스레드나 코어로 작업을 할당할 수 있음
    * 즉 멀티코어를 잘 활용할 수 있음
    * 다른 작업을 생성한 기존 작업에서 결과가 필요할 때는 get 메서드를 호출해서 생성된 Future가 완료될 때까지 기다릴 수 있음
  * CompletableFuture는 'CompletableFuture와 Future의 관계는 스트림과 컬렉션의 관계와 같다'라는 좌우명을 주장함
    * 스트림에서는 파이프라인 연산을 구성할 수 있으므로 map, filter 등으로 동작 파라미터화를 제공함
      * 따라서 반복자를 사용했을 때 생기는 불필요한 코드를 피할 수 있음
    * 마찬가지로 CompletableFuture는 Future와 관련한 공통 디자인 패턴을 함수형 프로그래밍으로 간결하게 표현할 수 있도록  
      thenCompose, thenCombine, allOf 등을 제공함
      * 따라서 명령형에서 발생하는 불필요한 코드를 피할 수 있음
  * 스트림이나 CompletableFuture에 비해 단순한 시나리오지만 이와 같은 연산 형식은 자바 8의 Optional에도 적용됨

* Optional 클래스
  * 자바 8 라이브러리는 T 형식의 값을 반환하거나 아니면  
    값이 없음을 의미하는 Optional.empty라는 정적 메서드를 반환할 수 있는 ``` Optioanl<T> ``` 클래스를 제공
    * ``` Optioanl<T> ```는 프로그램을 쉽게 이해하고 문서화하는 데 큰 도움을 줌
    * 즉, ``` Optioanl<T> ```는 에러가 잘 발생할 수 있는 계산을 수행하면서  
      값이 없을 때 에러를 발생시킬 수 있는 null 대신 정해진 데이터 형식을 제공
  * 일관적으로 ``` Optioanl<T> ```를 사용한다면 NullPointerException이 발생하지 않을 것
    * '없는 값의 형식을 다른 값으로 표현하는 기능이 도대체 프로그램 구현에 무슨 도움을 주는가?'
      * ``` Optioanl<T> ``` 클래스는 map, filter, ifPresent를 제공
      * 스트림 클래스가 제공하는 것과 비슷한 동작으로 계산을 연결할 때 함수형으로 map, filter, ifPresent 등을 사용할 수 있으며  
        값이 없는 상황을 사용자 코드에서 확인하는 것이 아니라 라이브러리에서 확인할 수 있음
      * 값을 내부적으로 검사하는 것과 외부적으로 검사하는 것은  
        사용자 코드에서 시스템 라이브러리가 내부 반복을 하느냐, 외부 반복을 하느냐와 같은 의미를 가짐
      * 자바 9에서는 Optional API에 stream(), or(), ifPresentOrElse() 등의 새로운 메서드 추가

* Flow API
  * 자바 9에서는 리액티브 스트림과 리액티브 당김 기반 역압력 프로토콜을 표준화
    * 소비자가 빠른 생산자를 대응하지 못해 발생하는 문제를 방지하는 기법
    * Flow API는 호환성을 높일 수 있도록 라이브러리가 구현할 수 있는 네 개의 인터페이스  
      Publisher, Subscriber, Subscription, Processor를 포함

* 디폴트 메서드
  * 이 기능은 개별 프로그래밍의 구현에는 아무 영향도 미치지 않음
    * 자바 8 이전에는 인터페이스에서 메서드 시그니처만 정의함
    * 하지만 디폴트 메서드 덕분에 인터페이스 설계자는 메서드의 기본 구현을 제공할 수 있음
  * 특히 인터페이스에 새로운 기능을 추가했을 때 기존의 모든 고객(인터페이스를 구현하는 클래스)이 새로 추가된 기능을 구현하지 않을 수 있게 되었다는 점에서  
    디폴트 메서드는 라이브러리 설계자에게 아주 훌륭한 도구라 할 수 있음

### 자바 9 모듈 시스템
* 자바 8에서는 많은 새 기능(람다, 인터페이스의 디폴트 메서드 등과 같은 새 기능)과  
  Stream, CompletableFuture 같은 유용한 새 클래스를 추가
* 자바 9에서는 새 언어 기능은 추가되지 않았음
  * 하지만 takeWhile, dropWhile, CompletableFuture의 completeOnTimeout 등 자바 8에서 시작된 여러 기능을 강화
  * 자바 9의 핵심은 새 모듈 시스템
    * 새 모듈 시스템에서는 module-info.java 파일이 추가되었지만 언어적으로는 바뀐 것이 없음
    * 하지만 모듈 시스템 덕분에 아키텍처 관점에서 앱을 설계하고 구현하는 방식이 바뀌었고 하위 부분간의 경계와 상호작용 방법 정의가 명확해짐
* 안타깝게도 자바 9는 다른 릴리스에 비해 과거 호환성을 많이 해쳤음
  * 하지만 적절한 모듈화라는 장점을 얻기 위한 불가피한 희생
  * 패키지간의 캡슐화 강화는 호환성 희생의 한 가지 이유
    * 사실 자바의 가시성 접근자는 메서드와 클래스 간의 캡슐화를 정의하는 용도일 뿐 패키지 간에는 모든 접근성이 공개됨
    * 이런 제약 때문에 시스템 속성을 모듈화하는 것이 어려웠음
    * 특히 어떤 모듈을 공개하고, 어떤 세부 구현은 다른 모듈이나 앱이 접근할 수 없게 숨겨야 할지 지정할 수 없었음
  * 패키지간의 캡슐화를 저해한 두 번째 요인으로 모듈화 시스템 부재를 꼽을 수 있음
    * 모듈 시스템 없이는 같은 환경에서 실행되는 모든 코드의 보안과 관련된 기능을 노출하는 것을 막을 방법이 없었음
    * 그러면 악의적 코드가 모듈의 핵심 부분에 접근할 수 있게 되고 그러면 모든 보안 기법을 우회할 수 있음
* 마지막으로 새로운 자바 모듈 시스템 덕분에 자바 런타임이 작은 부분으로 나눠질 수 있게 되었으므로  
  앱에서 필요한 부분만 사용할 수 있음
  * 예를 들어 CORBA가 새 자바 프로젝트에 필요하다면 필요한 기능만 추가하면 됨
  * 전통적인 크기의 컴퓨팅 디바이스의 제약에 한정된 얘기로 들릴 수 있겠지만  
    요즘에도 컨테이너화된 환경에서 실행하는 자바 앱에서도 이 기능은 중요할 수 있음
  * 즉 자바 모듈 시스템 덕분에 사물인터넷 앱이나 클라우드에서 자바 런타임을 사용할 수 있음
* 자바 모듈 시스템은 언어 수준의 기법으로 큰 시스템과 자바 런타임 자체를 모듈화함으로 이들 문제를 해결함
  * 자바 모듈 시스템이 제공하는 장점
    * 안정적 설정
      * 모듈 요구사항을 명시적으로 선언함으로 의존성 빠짐, 충돌, 순환 등의 문제를 런타임이 아니라 빌드 과정에서 일찍 확인 가능
    * 강한 캡슐화
      * 자바 모듈 시스템은 특정 패키지만 노출한 다음 각 모듈에서 공개할 부분과 내부 구현의 영역 접근을 분리할 수 있음
    * 보안성 개선
      * 사용자가 모듈의 특정 부분을 사용할 수 없도록 함으로 해커가 보안제어를 뚫기가 어려워짐
    * 성능 개선
      * 클래스가 런타임이 로드된 다른 클래스를 참조하는 상황보다는 적은 수의 컴포넌트를 참조할 때 최적화 기술이 더 효과를 발휘
    * 확장성
      * 자바 모듈 시스템은 자바 SE 플랫폼을 작은 부분으로 나눔으로 실행중인 앱에서 필요한 부분만 사용할 수 있음
* 모듈화는 보통 다루기 어려운 주제이며 자바 8의 람다가 그랬던 것처럼 갑자기 성행할 것 같지 않음
  * 하지만 장기적으로 유지보수라는 관점에서 모듈화에 투자하는 것이 결국은 바람직할 것

### 자바 10 지역 변수형 추론
* 자바에서는 기본적으로 변수가 메서드를 정의할 때 다음 예제처럼 형식을 지정해야 함
  ``` double convertUSDToGBP(double money) { ExchangeRate e = ...; } ```
  * 위 예제는 convertUSDToGBP의 결과, 인수 money, 지역 변수 e라는 세 가지 형식이 사용됨
  * 시간이 지나면서 이와 같은 엄격한 형식 지정이 조금 느슨해짐
  * 우선 컨텍스트로 형식을 유추할 수 있는 상황에서는 제네릭의 형식 파라미터를 생략할 수 있음
  * 예제
    ``` Map<String, List<String>> myMap = new HashMap<String, List<String>>(); ```
    * 자바 7부터 위 코드를 간소화
      ``` Map<String, List<String>> myMap = new HashMap<>(); ```
  * 콘텍스트로 형식을 유추할 수 있는 람다 표현식
    ``` Function<Integer, Boolean> p = (Integer x) -> booleanExpression; ```
    * 위 코드 간소화
      ``` Function<Integer, Boolean> p = x -> booleanExpression; ```
  * 형식이 생략되면 컴파일러가 생략된 형식을 추론(infer)함
  * 한 개의 식별자로 구성된 형식에 형식 추론을 사용하면 다양한 장점이 생김
    * 우선 한 형식을 다른 형식으로 교체할 때 편집 작업이 줄어듦
    * 하지만 형식의 크기가 커지면서 제네릭이 다른 제네릭 형식에 의해 파라미터화 될 수 있음
    * 이런 상황에서는 형식 추론으로 가독성이 좋아질 수 있음
      * 형식 추론이 명확하게 이루어진다는 점은 중요함
      * 형식을 추론하는 방법이 하나뿐이거나, 하나로 쉽게 문서화할 수 있거나, 사용자가 생략한 형식을 다시 만들 수 있는 상황에서 빛을 발함
      * 하지만 시스템이 사용자가 의도했던 형식과 다른 형식을 추론하기 시작하면 문제 발생
      * 따라서 두 가지 이상의 형식으로 추론할 수 있는 상황에서는 임의로 형식을 추론하여 언젠가 문제를 일으키는 것 보다는  
        에러를 발생시키는 것이 바람직함
    * 스칼라와 C# 언어는 지역 변수의 형식을 var 키워드로 대체할 수 있음
    * 그러면 컴파일러가 변수 할당문의 오른쪽 내용을 기초로 형식을 추론함
    * 예시
      ``` var myMap = new HashMap<String, List<String>>(); ```
      * 이를 지역 변수형 추론(local variable type inference)이라 부름
        * 자바 10에 추가된 기능
      * 하지만 지역 변수형 추론 과정에서 몇 가지 문제가 발생할 수 있음
        * Vehicle을 상속하는 Car가 있을 때 다음과 같이 정의했다고 가정
          ``` var x = new Car(); ```
          * x의 형식은 Car? Vehicle?
          * 상식적으로 변수의 형식은 초기화의 형식과 같다고 간주할 수 있음
          * 또한 초기화 코드가 없을 때는 var를 사용할 수 없다는 제약이 있으므로 큰 문제는 없음
          * 자바 10에서는 이를 공식적으로 지원하며 초깃값이 없을 때는 var을 사용할 수 없음도 설명함

### 자바의 미래
* JDK 개선 제안(JDK Enhancement Proposal) 웹사이트 : http://openjdk.java.net/jeps/0
  * 좋은 제안임에도 불구하고 기존 성능과의 상호 작용이나 기타 문제 때문에 자바에 채택되지 못한 이유를 설명

* 선언 사이트 변종
  * 자바에서는 제네릭의 서브형식을 와일드카드로 지정할 수 있는 유연성을 허용
    * 보통 이를 사용 사이트 변종(use-site variance)이라 함
  * 즉 다음처럼 제네릭 지정 가능
    ``` List<? extends Number> numbers = new ArrayList<Integer>(); ```
    * extends가 없으면 컴파일 에러
      ``` List<Number> numbers = new ArrayList<Integer>(); // 호환되지 않는 형식 ```
    * C#, 스칼라 같은 많은 프로그래밍 언어는 선언 사이트 변종이라는 다른 변종 기법을 지원함
      * 선언 사이트 변종을 이용하면 제네릭 클래스를 정의할 때 프로그래머가 변종을 지정할 수 있음
      * 본질적으로 변종적인(variant) 클래스에서 이 기능을 유용하게 사용할 수 있음
      * 예를 들어 Iterator는 본질적으로 공변적(covariant)이며 Comparator는 본질적으로 반변적(contravariant)
      * 선언 사이트 변종에서는 ``` ? extends ```, ``` ? super ```를 사용할 필요가 없음
      * 선언 사이트 변종은 클래스를 정의할 때 이와 같은 규격명세를 표현하므로 자바에 선언 사이트 변종을 추가하면 좋을 것
      * 결과적으로 프로그래머가 좀 더 쉽게 코드를 파악할 수 있게 됨

* 패턴 매칭
  * 보통 함수형 언어는 switch를 개선한 기능은 패턴 매칭을 제공
    * 패턴 매칭을 이용하면 '이 값이 주어진 클래스의 인스턴스인가?'라는 질문을 직접 할 수 있으며  
      객체의 필드가 어떤 값을 가지고 있는지도 재귀적으로 물을 수 있음
    * 간단한 자바 예제 코드
      ```
      if (op instanceof BinOp) {
          Expr e = ((BinOp) op).getLeft();
      }
      ```
      * 위 코드에서 op의 형식이 명확한 상황인데도 형변환 코드에서 BinOp형을 반복 사용
      * 물론 더 복잡한 계층을 표현해야 하는 상황이거나 여러 if문을 연결하면서 코드가 더 복잡해질 수 있음
        * 전통적인 객체지향 디자인에서는 switch 대신 방문자 패턴(visitor pattern)등을 사용할 것을 권장
        * 방문자 패턴에서는 데이터 형식에 종속된 제어 흐름이 switch가 아닌 메서드에 의해 결정됨
        * 하지만 함수형 프로그래밍에서는 상황이 달라짐
        * 함수형 프로그래밍에서는 데이터 형식의 값에 패턴 매칭을 적용하는 것이 프로그램을 가장 쉽게 설계하는 지름길
  * 스칼라 형식의 패턴 매칭을 완벽하게 자바로 적용하는 것은 쉽지 않은 일
    * 문자열을 허용하는 switch문이 등장한 것처럼 조만간 더 유연한, 즉 instanceof 문법을 사용해 객체를 사용하는 switch문이 등장할 가능성도 존재
    * BinOp와 Number는 Expr 클래스를 상속했다고 가정한 예제
      ```
      swtich (someExpr) {
          case (op instanceof BinOp):
              doSomething(op.getOpName(), op.getLeft(), op.getRight());
          case (op instanceof Number):
              dealWithLeafNode(n.getValue());
          default:
              defaultAction(someExpr);
      }
      ```
      * 위 예제는 패턴 매칭에서 몇 가지 아이디를 가져옴
        * ``` case (op instanceof BinOp): ```에서 op는 (BinOp 형식의) 새로운 지역 변수로 someExpr과 같은 값으로 바운드 됨
        * 마찬가자로 n은 Number 형식의 변수가 됨
        * default 케이스에서는 아무 변수도 바운드 되지 않음
        * 이 제안을 적용한다면 if-then-else 그리고 서브형식 캐스팅에 사용되는 여러 불필요한 코드를 제거할 수 있음
        * 전통적인 객체지향 설계자라면 이 예제처럼 데이터 형식에 따라 코드 실행을 결정할 때  
          차라리 서브형식으로 오버라이드된 방문자 형식의 메서드로 구현하는 것이 더 좋다고 생각할 수 있음
        * 하지만 함수형 프로그래밍의 관점에서는 관련 코드가 여러 클래스 정의로 흩어지는 결과를 초래할 뿐
        * 사실 이 문제는 '표현 문제'라는 양립적으로 논쟁 중인 오래된 주제
          * 참조 : https://en.wikipedia.org/wiki/Expression_problem

* 풍부한 형식의 제네릭
  * 구체화된 제네릭
    * 자바 5에서 제네릭을 소개했을 때 제네릭이 기존 JVM과 호환성을 유지해야 했음
      * 결과적으로 ``` ArrayList<String> ```이나 ``` ArrayList<Integer> ``` 모두 런타임 표현이 같게 되었음
      * 이를 제네릭 다형성의 삭제 모델(generic polymorphism erasure model)이라고 함
      * 이 때문에 약간의 런타임 비용을 지불하게 되었으며 제네릭 형식의 파라미터로 객체만 사용할 수 있게 되었음
        * 즉 기본형은 제네릭 형식으로 사용할 수 없음
      * 예를 들어 자바가 ``` ArrayList<int> ```를 지원한다고 가정
      * 그러면 int 42 같은 기본형을 포함하는 ArrayList 객체를 힙에 할당할 수 있겠지만  
        ArrayList 컨테이너가 String 같은 객체값을 포함하는 것인지 아니면 42 같은 기본형 int값을 포함하는 것인지 알 수 없게 됨
    * 그래도 크게 문제될 점은 없는 것 같음
      * 만일 ``` ArrayList<int> ```에서는 기본형 42를 얻을 수 있고  
        ``` ArrayList<String> ```에서는 'abc'라는 문자열 객체를 얻을 수 있다면  
        왜 ArrayList 컨테이너를 구별할 수 있는지 여부를 걱정해야 할까?
      * 불행하게도 가비지 컬렉션(GC) 때문
      * 런타임에 ArrayList의 콘텐츠 형식 정보를 확인할 수 없으므로 ArrayList의 13이라는 요소가  
        Integer 참조인지(GC가 사용중[in use]으로 표시) 아니면 int 기본값인지(GC 수행 불가) 분간할 수 없음
    * C#에서는 ``` ArrayList<String> ```, ``` ArrayList<Integer> ```, ``` ArrayList<int> ```가 각각 다른 의미를 가짐
      * 하지만 자바처림 이와 같은 선언이 모두 같은 상황이라면  
        가비지 컬렉션이 필드가 참조인지 기본형인지 알 수 있도록 충분한 형식 정보를 런타임에 유지해야 함
      * 이를 제네릭 다형성 구체화 모델(reified model of generic polymorphism)  
        또는 줄여서 구체화 제네릭(reified generic)이라고 부름
      * 구체화란 '암묵적인(implicit) 어떤 것을 명시적(explicit)으로 바꾼다'는 의미
      * 구체화된 제네릭이 더 명확함
    * 구체화된 제네릭으로 기본형과 기본형에 대응하는 객체형을 완전하게 통합할 수 있음
      * 자바의 가장 큰 걸림돌은 과거 버전과의 호환성
      * 리플렉션을 사용하는 JVM과 기존 프로그램은 제네릭이 사라지길 원함
  * 제네릭이 함수 형식에 제공하는 문법적 유연성
    * 제네릭은 자바 8의 다양한 람다 형식과 메서드 참조를 표현하는 데도 도움이 됨
      * 다음은 한 개의 인수를 받는 함수 표현 예제
        ```
        Function<Integer, Integer> square = x -> x * x;
        ```
        * 두 개의 인수를 받는 함수의 형식은 ``` BiFunction<T, U, R> ```로 사용할 수 있음
          * 여기서 T는 첫 번째 인수의 형식이고, U는 두 번째 인수의 형식이며, R은 결과 형식
          * 하지만 TriFunction은 직접 선언해야 함
        * 따라서 인수를 받지 않고 R 형식의 결과를 반환하는 메서드 참조에는  
          ``` Function<T, R> ```을 사용할 수 없고 ``` Supplier<R> ```을 사용해야 함
        * 기본적으로 자바 8 람다는 코드 구현을 풍부하게 할 수 있도록 해주었지만 형식 시스템은 코드 유연성을 따라잡지 못하고 있음
          * 예를 들어 많은 함수형 언어에서는 ``` (Integer, Double) => String ```이라는 형식을 구현할 수 있는데  
            자바 8에는 ``` BiFunction<Integer, Double, String> ```에 해당함
          * 여기서 =>는 Function, BiFunction, Supplier 등의 인픽스 버전으로 이해할 수 있음
          * 자바 문법에 이와 같은 형식이 추가된다면 스칼라와 비슷한 수준의 이해하기 쉬운 형식 시스템을 구성할 수 있음
  * 기본형 특화와 제네릭
    * 자바의 모든 기본형에는 대응하는 객체형이 존재함 (예를 들어 int 기본형에 대응하는 객체형은 java.lang.Integer)
      * 이를 언박스드(unboxed) 타입과 박스드(boxed) 타입이라고 부름
      * 이와 같은 구분으로 런타임 효율성은 조금 증가했지만 형식은 오히려 혼란스러워짐
      * 예를 들어 자바 9에서 왜 ``` Function<Apple, Boolean> ```이 아니라 ``` Predicate<Apple> ```로 구현해야 할까?
        * 그것은 test 메서드로 ``` Predicate<Apple> ``` 객체형을 호출했을 때 기본형 boolean을 반환하기 때문
    * 반면 모든 제네릭과 마찬가지로 Function도 객체형으로만 파라미터화 할 수 있음
      * 즉, ``` Function<Apple, Boolean> ```의 객체형은 기본형 boolean이 아니라 객체형 Boolean
      * ``` Precicate<Apple> ```은 boolean을 Boolean으로 박싱할 필요가 없으므로 좀 더 효율적
      * 이런 이유로 자바 언어를 개념적으로 복잡하게 만드는 LongToIntFunction과 BooleanSupplier 같은 인터페이스 등장
    * 비슷한 문제로 메서드 반환 형식이 아무 값도 없는 것임을 가리키는 void와 값으로 null을 갖는 객체형 Void가 있음
      * ``` Supplier<T> ``` 같은 특별한 유형의 Function은  
        ``` () => T ```처럼 표현할 수 있는데 이 때 기본형과 객체형의 차이가 더욱 두드러짐
      * 구체화된 제네릭으로 이런 문제를 해결할 수 있음
  
* 더 근본적인 불변성 지원
  * 일부는 자바 8에 다음과 같은 세 가지 종류의 값이 있다고 설명했을 때 동의하기 어려울 수 있음
    * 기본형의 값
    * 객체(참조)
    * 함수(참조)
  * 기본적으로 '메서드는 이 세 가지 종류의 값을 인수로 받아 결과로 반환할 수 있다'라는 원칙은 타협할 수 없음
    * 하지만 이 원칙에도 문제가 있다는 사실을 인정해야 함
    * 가변 배열의 참조를 반환하는 상황이라면 어디까지 (수학적인) 값을 반환하는 것으로 인정할 수 있는지 혼란이 생김
    * 문자열이나 불변 배열은 명확하게 값으로 인정할 수 있지만 가변 객체나 배열은 이를 수학적인 값으로 인정할 수 있을지 여부가 불명확함
      * 예를 들어 메서드에서는 오름차순으로 정렬된 요소를 포함하는 배열을 반환했지만 나중에 누군가가 요소를 바꿀 수 있기 때문
  * 자바에서 함수형 프로그래밍을 구현하려면 '불변값'을 언어적으로 지원해야 함
    * final로는 필드값 갱신만 막을 수 있으므로 final만으로 불변값이라는 목표를 달성하긴 어려움
      * 예제
        ```
        final int[] arr = {1, 2, 3};
        final List<T> list = new ArrayList<>();
        ```
        * 위 예제에서 첫 번째 행의 arr은 arr = ... 같이 다른 값은 할당할 수 없지만 ``` arr[1] = 2 ```처럼 요소는 바꿀 수 있음
        * 두 번째 행에서도 list에 다른 값을 할당할 수 없지만 다른 메서드에서 list의 요소 수를 바꿀 수 있음
        * 기본형에는 final 키워드로 값이 바뀌는 것을 막을 수 있지만 객체 참조에서는 큰 효과가 없음
    * 함수형 프로그래밍에서는 기존 구조체를 변화시키지 않는 것이 중요하므로  
      필드값이든 객체 참조든 직접 또는 간접적으로 모든 값의 변경을 방지하는 transitively_final 같은 만능 키워드가 있으면 좋을 것
    * 결국 transitively_final 같은 키워드 덕분에 값은 불변이 되며 값을 포함하는 변수는 다른 불변값을 포함하도록 바뀔 수 있음
      * 자바의 저자는 자바값이 가변 배열로 바뀌는 엉뚱한 가능성을 얘기함
  * 값 형식은 불변값만 포함할 수 있는데 변수가 final로 선언되지 않았다면 변수의 값 형식은 바뀔 수 있음

* 값 형식
  * 기본형(primitive type)과 객체형(object type)의 차이점
    * 객체지향 프로그래밍에 객체가 필요한 것처럼 함수형 프로그래밍에 값 형식이 도움을 줌
  * 컴파일러가 Integer와 int를 같은 값으로 취급할 수는 없을까?
    * 암묵적 박싱과 언박싱이 자바 1.1 이후로 서서히 퍼져나갔음을 생각해보면  
      이제 그냥 단순하게 자바에서 객체형값과 기본형값을 같은 값으로 취급할 때가 된 것 아닐까라는 생각을 해볼 수 있음
    * 예를 들어 컴파일러가 Integer와 int를 JVM에 맞게 같은 값으로 최적화해준다면 문제를 해결할 수 있음
      * 상당히 괜찮은 아이디어지만 실현이 쉽지 않음
      * Complex 예제
        * 다음은 실수부(real part)와 허수부(imaginary part)로 구성되는 복소수(complex number)를 표현하는 Complex 코드
        ```
        public class Complex {
        	public final double re;
        	public final double im;
        
        	public Complex(double re, double im) {
        		this.re = re;
        		this.im = im;
        	}
        
        	public static Complex add(Complex a, Complex b) {
        		return new Complex(a.re + b.re, a.im + b.im);
        	}
        }
        ```
        * Complex는 참조 형식이므로 Complex에 어떤 동작을 수행하려면 객체를 생성해야 함
          * 이제 Complex에 대응하는 complex라는 기본형이 필요함
        * Complex를 언박싱한 객체가 필요한데 자바와 JVM은 이런 기능을 지원하지 않음
          * '컴파일러가 뭔가 해줄 수 있지 않을까'라고 기대해볼 수 있음
          * 안타깝게도 이는 생각처럼 쉽지 않음
          * 탈출 분석(escape analysis), 즉 언박싱 동작이 괜찮은지 결정하는 컴파일러 최적화 기법이 있지만  
            이는 자바 1.1 이후에 제공되는 객체에만 적용됨
          * 다음 문제
            ```
            double d1 = 3.14;
            double d2 = d1;
            Double o1 = d1;
            Double o2 = d2;
            Double ox = o1;
            System.out.println(d1 == d2 ? "yes" : "no");
            System.out.println(o1 == o2 ? "yes" : "no");
            System.out.println(o1 == ox ? "yes" : "no");
            ```
            * 결과
              * 'yes', 'no', 'yes'
              * 경험 많은 자바 프로그래머라면 '마지막 두 행에는 == 말고 equals를 사용해야 해'라고 말할 것
              * 위 예제에서 모든 기본형과 객체형은 3.14라는 불변값을 포함하는 서로 구분할 수 없는 상황인데도 
                (같음을 판단하는) == 연산자는 새로운 객체 o1과 o2를 서로 다르다고 판단
              * 기본형에서는 비트를 비교해서 같음을 판단하지만 객체에서는 참조로 같음을 판단
              * 따라서 기본형으로 해결할 수 있는 상황인데도 어쩔 수 없이 컴파일러 요구사항에 맞춰 Double 같은 객체를 생성하는 상황이 발생
  * 변수형 : 모든 것을 기본형이나 객체형으로 양분하지 않는다
    * 다음과 같은 자바의 가정을 조금 바꾸면 이 문제를 해결할 수 있을 것
      * [1] 기본형이 아닌 모든 것은 객체형이므로 Object를 상속받음
      * [2] 모든 참조는 객체 참조
    * 값에는 두 가지 형식이 존재함
      * [1] 객체형은 (final로 정의되지 않았을 때) 변화할 수 있는 필드를 포함하며 ==로 값이 같음을 검사할 수 있음
      * [2] 값 형식은 불변이며 참조 식별자를 포함하지 않음. 기본형값은 넓은 의미에서 값 형식의 일종
    * 사용자가 값 형식을 정의하도록 허용할 수 있음
      * 사용자는 int나 boolean처럼 기본형이라는 점을 강조하려고 소문자로 시작하는 값 형식으로 이름을 정의할 수 있음
      * 값 형식에서는 하드웨어가 비트 단위로 비교해서 int가 같은지 검사하는 것처럼 ==도 기본적으로 요소 단위의 비교로 값이 같은지 확인함
      * 이 과정을 마치 좀 더 복잡한 비교를 수행하는 소수점 비교에 비유할 수 있음
      * Complex 형식은 기본형이 아닌 값 형식의 전형적인 예
      * 이 형식은 C#의 struct와 비슷
    * 값 형식에는 참조 식별자가 없으므로 저장 공간을 적게 차지
      * 여기서부터는 책에 있는 그림 설명
        * 세 가지 요소를 갖는 배열을 보여주는 그림
        * 배열 요소 0은 밝은 회색, 배열 요소 1은 흰색, 배열 요소 2는 어두운 회색으로 표시
        * 왼쪽 다이어그램은 Pair와 Complex가 객체일 때의 저장 공간 모습을 보여주며  
          오른쪽은 Pair와 Complex가 값 형식일 때의 저장 공간 모습을 보여줌
          * 이들이 기본형과 비슷하다는 의미에서 소문자 pair, compelx로 표시
        * 또한 값 형식은 데이터 접근(여러 수준에 걸친 포인터 참조 대신 단일 인덱스 주소 명령어를 사용하므로)뿐만 아니라  
          (데이터의 근접성 덕분에) 하드웨어 캐시 활용에도 좋은 성능을 제공할 가능성이 큼
        * 값 형식에는 참조 식별자가 없으므로 컴파일러가 자유롭게 값 형식을 박싱하거나 언박싱할 수 있음
        * 한 함수에서 Complex를 다른 함수의 인수로 전달하면 컴파일러가 자연스럽게 두 개의 double로 전달할 것
          * JVM에서는 64비트 머신 레지스터로 표현할 수 있는 메서드 반환 명령어만 지원하므로 JVM에서 박싱하지 않고 값을 반환하는 것은 쉽지 않음
        * 하지만 (커다란 불변 배열처럼) 더 큰 값 형식을 인수로 전달하면 컴파일러가 인수를 박싱한 다음에  
          이러한 변환을 눈치 채지 못할 만큼 자연스럽게 변환해서 참조로 사용자에게 전달할 수 있음
        * 이미 비슷한 기법이 C#에 적용되어 있음
          * 마이크로소프트는 다음과 같이 말함
            * "값 형식에 기반한 변수는 직접 값을 포함한다."
              * "한 값 형식 변수의 값을 다른 값 형식 변수에 할당하면 포함된 값이 복사된다."
              * "참조 형식 변수의 할당에서는 객체 자체가 아니라 객체의 참조가 복사된다는 점이 다르다."
            * 참조
              * 영문 : http://docs.microsoft.com/en-us/dotnet/csharp/language-reference/keywords/value-types
              * 한글 : https://docs.microsoft.com/ko-kr/dotnet/csharp/language-reference/builtin-types/value-types
  * 박싱, 제네릭, 값 형식 : 상호 의존 문제
    * 함수형 프로그래밍에서는 식별자가 없는 불변값을 이용하므로 자바에서 값 형식을 지원한다면 좋을 것
    * 자바에 값 형식이 지원된다면 기본형도 일종의 값 형식이 될 것이며 현재의 제네릭은 사라질 것
    * (참조로 같음을 구별하는) 객체형을 삭제하는 것은 쉽지 않으므로  
      Integer처럼 기본형 int를 (박싱한) 객체 버전은 계속 컬렉션과 자바 제네릭에 사용될 것
    * 이렇게 서로 얽힌 문제를 하나라도 해결한다면 모든 문제를 해결할 수 있음

### 더 빠르게 발전하는 자바
* 지난 22년동안 10번의 주요 릴리스 즉 평균적으로 2년이 넘어야 주요 업데이트가 릴리스 되었음
* 어떤 때는 5년이 걸렸음
* 자바 아키텍트는 이런 정책으로는 빠르게 언어가 진화할 수 없으므로 더 이상 유지할 수 없음을 깨달음
* 실제 자바를 넘어서는 기능을 제공하는 스칼라, 코틀린 같은 JVM 언어가 등장한 이유도 이 때문
* 람다, 자바 모듈 시스템 같은 혁신적인 기능이 등장하면서 릴리스 사이클이 길어지는 것은 이해할 수 있는 현상이지만  
  그 외의 작은 개선 사항은 특별한 이유없이 다음 릴리스가 이루어질 때까지 기다려야 했음
  * 예를 들어 컬렉션 팩토리 메서드는 자바 9 모듈 시스템이 완성되기 전에 이미 출시 준비를 마쳤음
* 이런 이유로 이제부터 자바는 6개월 개발 주기를 갖기로 결정
* 자바 아키텍트는 이런 빠른 개발 주기가 언어 자체에도 이로울 뿐 아니라  
  새로운 기술을 끊임없이 시험하는 빠르게 변화하는 회사와 개발자에게도 도움이 된다는 사실을 깨달음
* 반면 느린 속도로 소프트웨어를 갱신하는 보수적인 회사라면 문제가 될 수 있음
* 이런 이유로 자바 아키텍트는 매 3년마다 이후 3년 동안 지원을 보장하는 장기 지원([LTS]long-term support) 릴리스도 결정
* 특히 모든 소프트웨어 시스템과 언어가 빨리 개선되어야 하는 요즘 상황에서 자바가 더 짧은 개발 주기를 갖게 되는 것은 바람직한 현상
* 짧은 개발 주기 덕분에 자바는 적당한 속도로 진화할 수 있으며 앞으로도 개발에 유용하고 적절한 언어가 될 수 있을 것

### 결론
* 자바 8은 자바 역사를 통틀어 가장 큰 변화가 일어났던 버전
  * 자바 8과 비교할 만한 릴리스를 꼽자면 2005년 자바 5에 제네릭이 추가되었던 버전 뿐
* 자바 9의 주요 기능은 오래 기다렸던 모듈 시스템으로 개발자보다는 소프트웨어 아키텍트가 더 많은 관심을 가질 것으로 보임
  * 자바 9는 또한 플로 API를 통해 리액티브 스트림의 프로토콜을 표준화
* 자바 10에서는 이미 다른 언어에서 생산성을 높이도록 지원하기 시작한 지역 변수형 추론 기능을 도입
* 자바 11에서는 암묵적 형식의 람다 표현식의 파라미터 목록에 지역 변수형 문법을 사용할 수 있도록 지원
  * 더욱 중요한 사실은 자바 11에서는 CompletableFuture를 온전히 적용한 새로운 비동기 HTTP 클라이언트를 통해  
    동시성과 리액티브 프로그래밍 사상을 제공할 것
* 자바 12는 함수형 프로그래밍 언어의 핵심 기능으로 swtich 구문에 구분 대신 표현식을 사용할 수 있도록 허용할 것
  * 사실 switch 형식은 자바 패턴 매칭 도입의 초석
* 이런 모든 언어 업데이트는 함수형 프로그래밍 사상과 영향이 자바의 미래에도 계속 적용될 것임을 보여줌
* 자바 8, 9, 10, 11이 중요한 초석을 하나씩 추가하면서 릴리스되었지만 이런 변화는 앞으로도 계속 되어야 함
---

## APPENDIX A : 기타 언어 업데이트

### 어노테이션
* 자바 8의 어노테이션은 두 가지 개선
  * 어노테이션을 반복할 수 있음
  * 모든 형식에 어노테이션을 사용할 수 있음
* 자바의 어노테이션(annotation)은 부가 정보를 프로그램에 장식할 수 있는 기능
  * 자바 8 이전에는 선언에만 어노테이션을 사용할 수 있었음
  * 즉 어노테이션은 문법적 메타데이터(syntactic metadata)
  * 예를 들어 JUnit 프레임워크에서는 많은 어노테이션을 사용
    * 예제 코드
      ```
      @Before
      public void setUp() {
          this.list = new ArrayList<>();
      }
      
      @Test
      public void testAlgorithm() {
          ...
          assertEquals(5, list.size());
      }
      ```
    * 다음과 같은 용도로 어노테이션을 활용할 수 있음
      * JUnit의 콘텍스트에서 어노테이션으로 설정 작업을 하는 메서드와 단위 테스트를 실행하는 메서드를 구분할 수 있음
      * 문서화에 어노테이션을 사용할 수 있음
        * 예를 들어 더 이상 사용하지 말아야 할 메서드에 @Deprecated 어노테이션 사용
      * 자바 컴파일러도 어노테이션을 사용해서 에러를 검출하고, 경고를 줄이고, 코드를 생성할 수 있음
      * 자바 EE에서 엔터프라이즈 앱을 설정할 때 어노테이션을 많이 사용함

* 어노테이션 반복
  * 이전 자바에서는 선언에서 지정한 하나의 어노테이션만 허용
    * 예를 들어 다음은 유효하지 않은 코드
      ```
      @interface Author { String name(); }
      
      @Author(name="Raoul") @Author(name="Mario") @author(name="Alan") // 에러 : 중복된 어노테이션
      class Book {}
      ```
      * 종종 자바 EE 프로그래머는 이와 같은 제약을 피할 수 있는 방법을 만듦
        * 다음처럼 반복하고 싶은 어노테이션을 포함하는 배열을 새로운 어노테이션으로 정의할 수 있음
          ```
          @interface Author {
              String name();
          }
          
          @interface Authors {
              Author[] value();
          }
          
          @Authors({ @Author(name = "Raoul"), @Author(name = "Mario"), @Author(name = "Alan") })
          public class Book {
          }
          ```
          * Book 클래스의 중첩 어노테이션 때문에 코드가 복잡해짐
          * 이런 이유로 자바 8에서는 반복 어노테이션과 관련한 제한을 해제함
          * 이제 반복 조건만 만족한다면 선언을 할 때 하나의 어노테이션 형식에 여러 어노테이션을 지정할 수 있음
          * 어노테이션 반복은 기본으로 제공되는 기능이 아니므로 어노테이션임을 명시적으로 지정해야 함

* 반복할 수 있는 어노테이션 만들기
  * 반복할 수 있는 어노테이션을 설계하면 바로 어노테이션을 활용할 수 있음
  * 하지만 사용자에게 어노테이션을 제공하는 상황이라면 어노테이션을 반복할 수 있음을 설정하는 과정이 필요
    * [1] 어노테이션을 ``` @Repeatable ```로 표시
    * [2] 컨테이너 어노테이션 제공
  * 다음처럼 반복할 수 있는 ``` @Author ``` 어노테이션을 만들 수 있음
    ```
    @Repeatable(Authors.class)
    @interface Author {
    	String name();
    }
    
    @interface Authors {
    	Author[] value();
    }
    ```
    * 이제 Book 클래스에 여러 ``` @Author ``` 어노테이션을 사용할 수 있음
      ```
      @Author(name = "Raoul")
      @Author(name = "Mario")
      @Author(name = "Alan")
      public class Book {
      }
      ```
      * 컴파일을 할 때 Book은  
        ``` @Authors({ @Author(name = "Raoul"), @Author(name = "Mario"), @Author(name = "Alan") }) ```  
        이라는 어노테이션이 사용된 것으로 간주
      * 즉, 자바 프로그래머가 구현해야 했던 복잡한 어노테이션을 간편하게 표현할 수 있음
      * 하지만 기존 리플렉션 메서드와 동작 호환성을 유지하려면 어노테이션을 컨테이너로 감싸야 함
      * 자바 API의 ``` getAnnotation(Class<T> annotationClass) ``` 메서드는 어노테이션된 요소의 T 형식 어노테이션을 반환
      * 그러나 T라는 형식에 여러 어노테이션이 있다면 어떤 어노테이션이 반환될까?
      * 결론부터 말하자면 Class 클래스는 반복된 어노테이션에 사용할 수 있는 getAnnotationByType을 제공
        * 예를 들어 Book 클래스의 모든 Author 어노테이션을 출력하는 예제
          ```
          public static void main(String[] args) {
              Author[] authors = Book.class.getAnnotationsByType(Author.class);
              Arrays.asList(authors).stream().forEach(a -> {
                  System.out.println(a.name());
              });
          }
          ```
          * 여기서 반복할 수 있는 어노테이션과 컨테이너는 런타임 보유 정책(RUNTIME retention policy)을 반드시 갖고 있어야 함

* 형식 어노테이션
  * 자바 8에서는 모든 형식에 어노테이션을 적용할 수 있음
    * 즉, new 연산자, instanceof, 형식 캐스트, 제네릭 형식 인수, implements, throws 등에 어노테이션을 사용할 수 있음
    * @NonNull 어노테이션으로 문자열 name이 null이 될 수 없음을 지시하는 예제
      ``` @NonNull String name = person.getName(); ```
    * 다음처럼 리스트의 요소 형식도 어노테이션으로 지정 가능
      ``` List<@NonNull Car> cars = new ArrayList<>(); ```
    * 형식 어노테이션은 프로그램을 분석할 때 유용함
      * 위 예제에서 getName은 null을 반환하지 않음을 그리고 리스트의 모든 요소가 null이 아님을 확신할 수 있음
      * 덕분에 코드를 실행하면서 예상하지 못한 에러 발생 가능성을 줄일 수 있음
  * 자바 8은 어노테이션을 편리하게 사용할 수 있는 공식 어노테이션이나 도구를 따로 제공하지 않음
    * 자바 8에서는 형식 어노테이션 기능만 제공
    * 다행히 다양한 형식 어노테이션을 정의하는 Checker 프레임워크라는 도구가 존재
      * Checker 프레임워크를 이용해 형식 확인을 개선할 수 있음
      * 참고 : https://checkerframework.org/
  * 어노테이션을 코드에 어떻게 사용할 수 있는지
    * 참조 : https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.4

### 일반화된 대상 형식 추론
* 자바 8은 제네릭 인수 추론 기능을 개선
  * 자바 8 이전에도 콘텍스트 정보를 이용한 형식 추론 지원
  * 예를 들어 다음처럼 emptyList 메서드를 정의 가능
    ``` static <T> List<T> emptyList(); ```
    * emptyList를 T라는 형식 파라미터로 파라미터화
    * 다음처럼 메서드를 호출할 때 명시적으로 형식을 사용해 형식 파라미터의 형식을 지정할 수 있음
      ``` List<Car> cars = Collections.<Car>emptyList(); ```
      * 하지만 자바는 암시적으로 제네릭 인수를 추론할 수 있음
      * 위와 같은 동작 코드
        ``` List<Car> cars = Collections.emptyList(); ```
  * 자바 8 이전까지는 콘텍스트에만 의존해서 추론이 실행됨 (대상 형식화)
    * 예를 들어 다음 코드는 사용 불가
      ```
      static void cleanCars(List<Car> cars) {
      }
      
      cleanCars(Collections.emptyList());
      ```
      * 위 코드 실행시 에러 발생
        * ``` cleanCars (java.util.List<Car>) cannot be applied to (java.util.List<java.lang.Object>) ```
      * 명시적 형식 인수를 제공해 위 에러 해결 가능
    * 자바 8에서는 대상 형식이 메서드 인수에도 적용되므로 명시적인 제네릭 인수를 제공하지 않아도 됨
      ```
      List<Car> cleanCars = dirtyCars.stream()
                                     .filter(Car::isClean)
                                     .collect(Collectors.toList());
      ```
      * 따라서 ``` Collectors.<Car>toList() ``` 대신 ``` Collectors.toList() ```를 사용할 수 있음
---

## APPENDIX B : 기타 라이브러리 업데이트

### 컬렉션
* 추가 메서드
  * 자바 설계자는 많은 새로운 메서드를 컬렉션 인터페이스와 클래스에 추가
    * 추가된 대부분의 메서드는 디폴트 메서드
    * 추가 목록
      * Map
        * getOrDefault
        * forEach
        * compute
        * computeIfAbsent
        * computeIfPresent
        * merge
        * putIfAbsent
        * remove(key, value)
        * replace
        * replaceAll
      * Iterable
        * forEach
        * spliterator
      * Iterator
        * forEachRemaining
      * Collection
        * removeIf
        * stream
        * parallelStream
      * List
        * replaceAll
        * sort
      * BitSet
        * Stream
  * 맵
    * Map은 다양한 편의 메서드가 추가되면서 가장 많이 업데이트된 인터페이스
      * 예를 들어 키에 매핑되는 값이 있는지 여부를 확인하는 기존의 get 메서드를 getOrDefault로 대신할 수 있음
        * getOrDefault는 키에 매핑되는 값이 없으면 기본값을 반환
        * 기존 코드
          ```
          Map<String, Integer> carInventory = new HashMap<>();
          Integer count = 0;
          if (map.containsKey("Aston Martin")) {
              count = map.get("Aston Martin");
          }
          ```
          * 대체 코드
            ``` Integer count = map.getOrDefault("Aston Martin", 0); ```
          * 위 코드는 키에 매핑되는 값이 없을 때만 예상대로 작동
            * 예를 들어 키가 명시적으로 null로 매핑되어 있으면 기본값이 아니라 null이 반환됨
      * computeIfAbsent도 유용한 메서드
        * computeIfAbsent를 이용하면 간편하게 캐싱 패턴을 활용할 수 있음
        * 다양한 웹사이트에서 데이터를 얻어 처리해야 한다고 가정
          * 이때 데이터를 캐시할 수 있다면 비용이 빘나 데이터를 얻어오는 과정을 생략할 수 있음
            ```
            public String getData(String url) {
                String data = cache.get(url);
                if (data == null) {       // 데이터가 이미 캐시되어 있는지 확인
                    data = getData(url);
                    cache.put(url, data); // 캐시되어 있는 데이터가 없으면 데이터를 가져와 나중에 사용할 수 있도록 맵에 캐시
                }
                return data;
            }
            ```
            * 대체 코드
              ```
              public String getData(String url) {
                  return cache.computeIfAbsent(url, this::getData);
              }
              ```
  * 컬렉션
    * removeIf 메서드로 프레디케이트와 일치하는 모든 요소를 컬렉션에서 제거할 수 있음
      * removeIf 메서드는 스트림 API의 filter와는 다름
      * 스트림 API의 filter 메서드는 새로운 스트림을 생성하므로 현재 스트림이나 소스를 갱신하지 않음
  * 리스트
    * replaceAll 메서드는 리스트의 각 요소에 주어진 연산자를 리스트에 적용한 결과로 대체
      * replaceAll 메서드는 스트림의 map과 비슷하지만 새로운 요소를 생성하는 map과 달리 리스트의 요소를 갱신한다는 점이 다름
      * 예를 들어 다음 코드는 리스트가 바뀌면서 ``` [2, 4, 6, 8, 10] ```을 출력함
        ```
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.replaceAll(x -> x * 2);
        System.out.println(numbers);  // [2, 4, 6, 8, 10] 출력
        ```

* Collections 클래스
  * Collections는 오랫동안 컬렉션 관련 동작을 수행하고 반환하는 역할을 담당해온 클래스
    * Collections는 불변의(unmodifiable), 동기화된(synchronized), 검사된(checked), 빈(empty)  
      NavigableMap과 NavigableSet을 반환할 수 있는 새로운 메서드를 포함
    * 또한 동적 형식 검사에 기반을 둔 Queue 뷰를 반환하는 checkedQueue라는 메서드도 제공

* Comparator
  * Comparator 인터페이스는 디폴트 메서드와 정적 메서드를 추가로 제공
  * 추가된 인스턴스 메소드
    * reversed
      * 현재 Comparator를 역순으로 반전시킨 Comparator를 반환
    * thenComparing
      * 두 객체가 같을 때 다른 Comparator를 사용하는 Comparator를 반환
    * thenComparingInt, thenComparingDouble, thenComparingLong
      * thenComparing과 비슷한 동작을 수행하지만 기본형에 특화된 함수를 인수로 받음
        * ToIntFunction, ToDoubleFunction, ToLongFunction
  * 추가된 정적 메서드
    * comparingInt, comparingDouble, comparingLong
      * comparing과 비슷한 동작을 수행하지만 기본형에 특화된 함수를 인수로 받음
        * ToIntFunction, ToDoubleFunction, ToLongFunction
    * naturalOrder
      * Comparable 객체에 자연 순서를 적용한 Comparable 객체를 반환
    * nullsFirst
      * null 객체를 null이 아닌 객체보다 작은 값으로 취급하는 Comparator를 반환
    * nullsLast
      * null 객체를 null이 아닌 객체보다 큰 값으로 취급하는 Comparator를 반환
    * reverseOrder
      * ``` naturalOrder().reversed() ```와 같음

### 동시성
* CompletableFuture도 동시성 업데이트 중 하나

* 아토믹
  * java.util.concurrent.atomic 패키지는 AtomicInteger, AtomicLong 등 단일 변수에 아토믹 연산을 지원하는 숫자 클래스 지원
    * getAndUpdate
      * 제공된 함수의 결과를 현재값에 아토믹하게 적용하고 기존값을 반환
    * updateAndGet
      * 제공된 함수의 결과를 현재값에 아토믹하게 적용하고 업데이트된 값을 반환
    * getAndAccumulate
      * 제공된 함수를 현재값과 인수값에 적용하고 기존값을 반환
    * accumulateAndGet
      * 제공된 함수를 현재값과 인수값에 적용하고 업데이트된 값을 반환
  * atomicInteger와 10 중 작은 수를 아토믹으로 설정하는 예제
    ``` int min =  atomicInteger.accumulateAndGet(10, Integer::min); ```

* Adder와 Accumulator
  * 자바 API는 여러 스레드에서 읽기 동작보다 갱신 동작을 많이 수행하는 상황이라면  
    Atomic 클래스 대신 ``` LongAdder, LongAccumulator, DoubleAdder, DoubleAccumulator ```를 사용하라고 권고
    * 예를 들면 통계작업
  * 이들 클래스는 동적으로 커질 수 있도록 설계되었으므로 스레드 간의 경쟁을 줄일 수 있음
  * LongAdder, DoubleAdder는 덧셈 연산을 지원하며 LongAccumulator와 DoubleAccumulator는 제공된 함수로 값을 모음
  * 예를 들어 다음처럼 LongAdder로 여러 값의 합계를 계산할 수 있음
    ```
    LongAdder adder = new LongAdder();  // 디폴트 생성자에서 초기 sum값을 0으로 설정
    adder.add(10);                      // 여러 스레드에서 어떤 작업을 수행
    // ...
    long sum = adder.sum();             // 어떤 시점에서 합계를 구함
    ```
  * 또는 다음처럼 LongAccumulator 사용
    ```
    LongAccumulator acc = new LongAccumulator(Long::sum, 0);
    acc.accumulate(10);       // 여러 스레드에서 값을 누적
    // ...
    long result = acc.get();  // 특정 시점에서 결과를 얻음
    ```

* ConcurrentHashMap
  * ConcurrentHashMap은 동시 실행 환경에 친화적은 새로운 HashMap
    * ConcurrentHashMap은 내부 자료구조의 일부만 잠근 상태로 동시 덧셈이나 갱신 작업을 수행할 수 있는 기능을 제공
    * 따라서 기존의 동기화된 HashTable에 비해 빠른 속도로 읽기 쓰기 연산을 수행
  * 성능
    * 성능을 개선하면서 ConcurrentHashMap의 내부 구조가 바뀜
    * 보통 맵의 객체는 키로 생성한 해시코드로 접근할 수 있는 버킷에 저장됨
    * 하지만 키가 같은 해시코드를 반환하는 상황에서는 O(n) 성능의 리스트로 버킷을 구현해야 하므로 성능이 나빠짐
    * 자바 8에서 버킷이 너무 커지면 동적으로 리스트를 정렬 트리로(O(long(n))의 성능으로) 교체
    * 키가 Comparable일때만(예를 들면 String이나 Number 클래스) 이 기능을 사용할 수 있음
  * 스트림 같은 연산
    * ConcurrentHashMap은 마치 스트림을 연상시키는 세 종류의 연산을 지원
      * forEach
        * 각 키/값 쌍에 주어진 동작을 실행
      * reduce
        * 제공된 리듀싱 함수로 모든 키/값 쌍의 결과를 도출
      * search
        * 함수가 null이 아닌 결과를 도출할 때까지 각 키/값 쌍에 함수를 적용
    * 각 연산은 키, 값, Map.Entry, 키/값 쌍을 인수로 받는 함수를 인수로 받으며 다음과 같은 네 가지 형식의 연산을 지원
      * 키/값 쌍으로 연산
        * forEach
        * reduce
        * search
      * 키로 연산
        * forEachKey
        * reduceKeys
        * searchKeys
      * 값으로 연산
        * forEachValue
        * reduceValues
        * searchValues
      * Map.entry 객체로 연산
        * forEachEntry
        * reduceEntries
        * searchEntries
    * 이들 연산은 ConcurrentHashMap의 상태를 잠그지 않고 요소에 직접 연산을 수행
      * 연산에 적용된 함수는 순서에 의존하지 않아야 하며, 계산 과정에서 바뀔 수 있는 다른 객체나 값에 의존하지 않아야 함
    * 또한 모든 연산에 병렬성 한계값(parallelism threshold)을 지정해야 함
      * 현재 맵의 크기가 한계값보다 작다고 추정되면 순차적으로 연산을 수행
      * 즉, 1이라는 값은 공용 스레드 풀을 사용해서 병렬성을 최대화하며 Long.MAX_VALUE라는 값은 하나의 스레드로 연산을 수행한다는 의미
    * reduceValues를 이용해 맵의 최댓값을 찾는 예제
      ```
      ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
      Optional<Integer> maxValue = Optional.of(map.reduceValues(1, Integer::max));
      ```
    * 또한 (reduceValuesToInt, reduceKeyToLong 등) int, long, double 기본형에 특화된 reduce 연산이 있음
  * 카운팅
    * ConcurrentHashMap 클래스는 맵의 매핑 개수를 long으로 반환하는 mappingCount라는 새로운 메서드를 제공
      * 맵의 매핑 개수가 정수 범위를 초과할 수 있으므로 앞으로는 int를 반환하는 size대신 mappingCount를 사용하는 것이 좋음
  * 집합 뷰
    * ConcurrentHashMap 클래스는 ConcurrentHashMap을 집합 뷰로 변환하는 새로운 메서드 keySet을 제공
      * 즉, 맵을 바꾸면 집합에도 그 결과가 반영되며 마찬가지로 집합을 바꾸어도 맵에 영향을 미침
      * 또한 newKeySet을 이용하면 ConcurrentHashMap의 원소를 포함하는 새로운 집합을 만들 수 있음

### Arrays
* parallelSort 사용하기
  * parallelSort 메서드는 자연 순서(natural order)나 객체 배열의 추가 Comparator를 사용해 특정 배열을 병렬로 정렬하는 기능 수행

* setAll, parallelSetAll 사용하기
  * setAll은 지정된 배열의 모든 요소를 순차적으로 설정하고, parallelSetAll은 모든 요소를 병렬로 설정
  * 두 메서드 모두 제공된 함수로 각 요소를 계산하는데, 이 함수는 요소 인덱스를 받아 해당 값을 반환
  * parallelSetAll은 병렬로 실행되므로 parallelSetAll에 전달하는 함수는 부작용이 없어야 함
    * 예를 들어 다음은 setAll 메서드로 ``` 0, 2, 4, 6, ... ``` 같은 값을 만드는 예제
      ```
      int[] evenNumbers = new int[10];
      Arrays.setAll(evenNumbers, i -> i * 2);
      ```

* parallelPrefix 사용하기
  * parallelPrefix 메서드는 제공된 이항 연산자를 이용해 배열의 각 요소를 병렬로 누적하는 동작을 수행
  * ``` 1, 2, 3, 4, 5, 6, 7 ... ``` 형식의 값을 만드는 예제
    ```
    int[] ones = new int[10];
    Arrays.fill(ones, 1);
    Arrays.parallelPrefix(ones, (a, b) -> a + b); // ones는 [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    ```

### Number와 Math
* Number
  * Number 클래스에 추가된 메서드
    * Short, Integer, Long, Float, Double 클래스에는 정적 메서드 sum, min, max가 추가됨
    * Integer와 Long 클래스에는 부호가 없는 값을 처리하는  
      compareUnsigned, divideUnsigned, remainderUnsigned, toUnsignedString 등의 메서드가 추가됨
    * Integer와 Long 클래스에는 문자열을 부호가 없는 int나 long으로 파싱하는  
      정적 메서드 parseUnsignedInt, parseUnsignedLong이 추가됨
    * Byte와 Short 클래스는 인수를 비부호 int나 long으로 변환하는 toUnsignedInt와 toUnsignedLong 메서드를 제공
      * 마찬가지로 Integer 클래스에도 toUnsignedLong 정적 메서드 추가됨
    * Double과 Float 클래스에는 인수가 유한 소수점인지 검사하는 정적 메서드 isFinite가 추가됨
    * Boolean 클래스에는 두 불리언 값에 and, or, xor 연산을 적용하는 메서드 logicalAnd, logicalOr, logicalXor이 추가됨
    * BigInteger 클래스에는 BigInteger를 다양한 기본형으로 바꿀 수 있는  
      byteValueExact, shortValueExact, intValueExact, longValueExact 메서드 추가됨
      * 그러나 변환 과정에서 정보 손실이 발생하면 산술 연산 예외가 발생함

* Math
  * Math 클래스에는 연산 결과에 오버플로가 발생했을 때 산술 예외를 발생시키는  
    addExact, subtractExact, multiplyExact, incrementExact, decrementExact, negateExact 등의 메서드가 추가됨  
    int와 long을 인수로 받음
  * 또한 long을 int로 변경하는 정적 메서드 toIntExact, floorMod, floorDiv, nextDonw 등의 정적 메서드도 추가됨

### Files
* Files 클래스에는 파일에서 스트림을 만들 수 있는 기능이 추가됨
  * 파일을 스트림으로 게으르게 읽을 수 있는 기능을 제공하는 Files.lines 정적 메서드 등
  * 그 외 스트림을 반환하는 정적 메서드
    * Files.list
      * 주어진 디렉터리의 개체를 포함하는 ``` Stream<Path> ```를 생성
      * 이 과정은 재귀가 아님
      * 스트림은 게으르게 소비되므로 특히 큰 디렉터리를 처리할 때 유용한 메서드
    * Files.walk
      * Files.list와 마찬가지로 주어진 디렉터리의 개체를 포함하는 ``` Stream<Path> ```를 생성
      * 이 과정은 재귀적으로 실행되며 깊이 수준을 설정할 수 있음
      * 깊이 우선(depth-first)방식으로 탐색을 수행함
    * Files.find
      * 디렉터리를 재귀적으로 탐색하면서 주어진 프레디케이트와 일치하는 개체를 찾아서 ``` Stream<Path> ```를 생성

### 리플렉션
* 리플렉션 API에서 이름, 변경자 등의 메서드 파라미터 정보를 사용할 수 있게 됨
* java.lang.reflect.Parameter라는 클래스가 새로 추가되었는데 Method와 Constructor의 공통 기능을 제공하는 슈퍼클래스 역할을 하는  
  java.lang.reflect.Executable에서 java.lang.reflect.Parameter를 참조

### String
* String 클래스에는 구분 기호(delimiter)로 문자열을 연결(join)할 수 있는 join이라는 새로운 정적 메서드가 추가됨
  * join 메서드 활용 예제
    ```
    String authors = String.join(", ", "Raoul", "Mario", "Alan");
    System.out.println(authors);  // Raoul, Mario, Alan
    ```
---

## APPENDIX C : 스트림에 여러 연산 병렬로 실행하기
* 스트림에서는 한 번만 연산을 수행할 수 있으므로 결과도 한 번만 얻을 수 있다는 것이 자바 8 스트림의 가장 큰 단점
  * 스트림을 두 번 탐색하려 한다면 다음과 같은 에러 발생
    ``` java.lang.IllegalStateException: stream has already been operated upon or closed ```
  * 한 스트림에서 여러 결과를 얻어야 하는 상황이 있을 수 있음
    * 예를 들어 로그 파일을 스트림으로 파싱해서 한 번에 여러 통계를 얻는 상황이 있을 수 있음
    * 그러러면 한 번에 한 개 이상의 람다를 스트림으로 적용해야 함
    * 즉 fork 같은 메서드를 이용해 스트림을 포크(분기)시키고 포크된 스트림에 다양한 함수를 적용해야 함
    * 심지어 여러 연산을 각각의 스레드에서 병렬로 실행할 수 있다면 더 좋을 것
    * 안타깝게도 자바 8 스트림에서는 이 기능을 제공하지 않음
      * Spliterator(특히 늦은 바인딩 기능을 활용), BlockingQueue, Future를 이용하여 직접 편리한 API로 만듦

### 스트림 포킹
* 스트림에서 여러 연산을 병렬로 실행하려면 먼저 원래 스트림을 감싸면서 다른 동작을 정의할 수 있도록 StreamForker를 만들어야 함
  * 예제 코드
    ```
    public class StreamForker<T> {
    	private final Stream<T> stream;
    	private final Map<Object, Function<Stream<T>, ?>> forks = new HashMap<>();
    
    	public StreamForker(Stream<T> stream) {
    		this.stream = stream;
    	}
    
    	public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f) {
    		forks.put(key, f); // 스트림에 적용할 함수 저장
    		return this;       // 유연하게 fork 메서드를 여러 번 호출할 수 있도록 this 반환
    	}
    
    	public Results getResults() {
    		// TODO
    	}
    }
    ```
    * fork는 두 인수를 받음
      * 스트림을 특정 연산의 결과 형식으로 변환하는 Function
      * 연산의 결과를 제공하는 키, 내부 맵에 키/함수 쌍 저장
    * fork 메서드는 StreamForker 자신을 반환
      * 따라서 여러 연산을 포킹(forking:분기)해서 파이프라인을 만들 수 있음
    * 여기서 사용자는 스트림에서 세 가지 키로 인덱스된 세 개의 동작을 정의함
      * StreamForker는 원래 스트림을 탐색하면서 세 개의 스트림으로 포크시킴
      * 이제 포크된 스트림에 세 연산을 병렬로 적용할 수 있으며 결과 맵에 각 키의 인덱스를 이용해 함수 적용 결과를 얻을 수 있음
      * getResults 메서드를 호출하면 fork 메서드로 추가한 모든 연산이 실행됨
      * getResults는 다음과 같은 Results 인터페이스의 구현을 반환
        ```
        public static interface Results {
            public <R> R get(Object key);
        }
        ```
        * Results 인터페이스는 fork 메서드에서 사용하는 key 객체를 받는 하나의 메서드 정의를 포함
        * 이 메서드는 키에 대응하는 연산 결과를 반환

* ForkingStreamConsumer로 Results 인터페이스 구현하기
  * getResults 메서드 구현
    ```
    public Results getResults() {
        ForkingStreamConsumer<T> consumer = build();
        try {
            stream.sequential().forEach(consumer);
        } finally {
            consumer.finish();
        }
        return consumer;
    }
    ```
    * ForkingStreamConsumer는 Results 인터페이스와 Consumer 인터페이스를 구현
      * 스트림의 모든 요소를 소비해서 for 메서드로 전달된 연산 수 만큼의 BlockingQueue로 분산시키는 것이  
        ForkingStreamConsumer의 주요 역할
      * forEach 메서드를 병렬 스트림에 수행하면 큐에 삽입되는 요소의 순서가 흐트러질 수 있으므로 스트림을 순차로 처리하도록 지시
      * finish 메서드는 더 이상 처리할 항목이 없음을 지시하는 특별한 요소를 추가
      * build 메서드로 ForkingStreamConsumer를 만들 수 있음
        ```
        private ForkingStreamConsumer<T> build() {
            // 각각의 연산을 저장할 큐 리스트 생성
            List<BlockingQueue<T>> queues = new ArrayList<>();
    
            // 연산 결과를 포함하는 Future 연산을 식별할 수 있는 키에 대응시켜 맵에 저장
            Map<Object, Future<?>> actions =
                    forks.entrySet()
                            .stream()
                            .reduce(
                                    new HashMap<Object, Future<?>>(),
                                    (map, e) -> {
                                        map.put(e.getKey(),
                                                getOperationResult(queues, e.getValue()));
                                        return map;
                                    },
                                    (m1, m2) -> {
                                        m1.putAll(m2);
                                        return m1;
                                    });
    
            return new ForkingStreamConsumer<>(queues, actions);
        }
        ```
        * 먼저 이전에 설명한 BlockingQueue의 리스트를 만듦
        * 스트림에서 실행할 다양한 동작을 식별할 수 있는 키와 대응하는 연산 결과를 포함하는 Future를 값으로 포함하는 맵을 만듦
        * BlockingQueue의 List와 Future의 Map을 ForkingStreamConsumer 생성자로 전달
        * getOperationResult 메서드로 각각의 Future를 만듦
          ```
          private Future<?> getOperationResult(List<BlockingQueue<T>> queues, Function<Stream<T>, ?> f) {
              BlockingQueue<T> queue = new LinkedBlockingQueue<>();
            
              // 큐를 만들어 큐 리스트에 추가
              queues.add(queue);
              // 큐의 요소를 탐색하는 Spliterator
              Spliterator<T> spliterator = new BlockingQueueSpliterator<>(queue);
              // Spliterator를 소스로 갖는 스트림을 생성
              Stream<T> source = StreamSupport.stream(spliterator, false);
              // 스트림에서 주어진 함수를 비동기로 적용해서 결과를 얻을 Future 생성
              return CompletableFuture.supplyAsync(() -> f.apply(source));
          }
          ```

* ForkingStreamConsumer, BlockingQueueSpliterator 구현하기
  * ForkingStreamConsumer 구현 코드
    ```
    class ForkingStreamConsumer<T> implements Consumer<T>, Results {
    	static final Object END_OF_STREAM = new Object();
    
    	private final List<BlockingQueue<T>> queues;
    	private final Map<Object, Future<?>> actions;
    
    	ForkingStreamConsumer(List<BlockingQueue<T>> queues, Map<Object, Future<?>> actions) {
    		this.queues = queues;
    		this.actions = actions;
    	}
    
    	@Override
    	public <R> R get(Object key) {
    		try {
    			// 키에 대응하는 동작의 결과를 반환, Future의 계산 완료 대기
    			return ((Future<R>) actions.get(key)).get();
    		} catch (Exception e) {
    			throw new RuntimeException(e);
    		}
    	}
    
    	@Override
    	public void accept(T t) {
    		// 스트림에서 탐색한 요소를 모든 큐로 전달
    		queues.forEach(q -> q.add(t));
    	}
    
    	void finish() {
    		// 스트림의 끝을 알리는 마지막 요소를 큐에 삽입
    		accept((T) END_OF_STREAM);
    	}
    }
    ```
    * ForkingStreamConsumer 클래스는 Consumer 인터페이스와 Results 인터페이스를 구현하며,  
      BlockingQueue의 List 참조와 스트림에 다양한 연산을 수행하는 Future의 Map 참조를 유지함
    * Consumer 인터페이스는 accept 메서드를 정의함
      * ForkingStreamConsumer가 스트림 요소를 받을 때마다 요소를 BlockingQueue로 추가
      * 그리고 기존 스트림의 모든 요소를 큐에 추가한 다음에는 finish 메서드를 호출해서 마지막 요소를 추가
      * BlockingQueueSpliterator가 큐의 마지막 요소를 확인하는 순간 더 이상 처리할 요소가 없음을 판단할 수 있음
    * Result 인터페이스는 get 메서드를 정의
      * get 메서드는 인수 키로 맵에서 Future를 가져온 다음에 값을 언랩하거나 값이 없으면 결과를 기다림
    * 마지막으로 스트림에서 수행될 각 연산에 대한 BlockingQueueSpliterator를 구현해야 함
      * 각 BlockingQueueSpliterator는 ForkingStreamConsumer에서 만든 BlockingQueue의 참조 중 하나를 포함
  * BlockingQueueSpliterator 구현 코드
    ```
    class BlockingQueueSpliterator<T> implements Spliterator<T> {
    	private final BlockingQueue<T> q;
    
    	public BlockingQueueSpliterator(BlockingQueue<T> q) {
    		this.q = q;
    	}
    
    	@Override
    	public boolean tryAdvance(Consumer<? super T> action) {
    		T t;
    		while (true) {
    			try {
    				t = q.take();
    				break;
    			} catch (InterruptedException e) {
    			}
    		}
    
    		if (t != ForkingStreamConsumer.END_OF_STREAM) {
    			action.accept(t);
    			return true;
    		}
    
    		return false;
    	}
    
    	@Override
    	public Spliterator<T> trySplit() {
    		return null;
    	}
    
    	@Override
    	public long estimateSize() {
    		return 0;
    	}
    
    	@Override
    	public int characteristics() {
    		return 0;
    	}
    }
    ```
    * 위 코드에서는 스트림을 어떻게 분할할지는 정의하지 않고 늦은 바인딩 기능만 활용하도록 Spliterator를 정의
      * 따라서 trySplit은 구현하지 않음
    * 큐에서 몇 개의 요소를 가져올 수 있는지 미리 알 수 없으므로 estimatedSized값은 큰 의미가 없음
      * 또한 분할을 하지 않은 상황이므로 estimatedSized값을 활용하지도 않음
      * 위 예제에서는 Spliterator 특성을 사용하지 않으므로 characteristic 메서드는 0을 반환
    * 예제에서는 tryAdvance 메서드만 구현
      * ForkingStreamConsumer가 원래의 스트림에서 추가한 요소를 BlockingQueue에서 가져옴
      * 이렇게 가져온 요소를 다음 스트림 소스로 사용할 수 있도록 Consumer로 보냄
      * getOperationResult에서 생성한 Spliterator에서 요소를 보낼 Consumer를 결정하며,  
        fork 메서드로 전달된 함수를 새로 만든 스트림에 적용함
      * tryAdvance 메서드는 ForkingStreamConsumer가 (더 이상 큐에 처리할 요소가 없음을 알릴 목적으로)  
        추가한 특별한 객체를 발견하기 전까지 true를 반환하며 소비할 다른 요소가 있음을 알림
  * StreamForker는 스트림에 수행할 각 연산을 포함하는 맵을 포함
    * 맵의 인덱스는 키, 값은 수행할 함수
    * ForkingStreamConsumer는 이들 연산을 저장할 큐를 가지고 있으며 원래 스트림에서 모든 요소를 소비해서 모든 큐로 요소를 분산시킴
    * 각 큐는 항목을 가져오면서 다른 스트림의 소스 역할을 하는 BlockingQueueSpliterator를 포함하고 있음
    * 마지막으로 원래 스트림에서 포크된 각 스트림은 함수의 인수로 전달되며 각각 수행해야 할 연산을 실행함

* StreamForker 활용
  * 4장의 메뉴 데이터 모델을 다시 정의
  * 네 개의 연산을 병렬로 수행할 수 있도록 원래 스트림을 포킹
  * 모든 요리명을 콤마로 분리한 리스트를 만들고, 메뉴의 총 칼로리를 계산하고, 가장 칼로리가 높은 요리를 찾고, 요리를 종류별로 그룹화하는 연산 수행
  * StreamForker 활용 코드
    ```
    Stream<Dish> menuStream = menu.stream();
    
    Results results = new StreamForker<Dish>(menuStream)
            .fork("shortMenu",
                    s -> s.map(Dish::getName).collect(joining(", ")))
            .fork("totalCalories",
                    s -> s.mapToInt(Dish::getCalories).sum())
            .fork("mostCaloricDish",
    				s -> s.collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)).get())
            .fork("dishesByType",
                    s -> s.collect(groupingBy(Dish::getType)))
            .getResults();

    String shortMenu = results.get("shortMenu");
    int totalCalories = results.get("totalCalories");
    Dish mostCaloricDish = results.get("mostCaloricDish");
    Map<Dish.Type, List<Dish>> dishesByType = results.get("dishesByType");

    System.out.println("Short menu: " + shortMenu);
    System.out.println("Total calories: " + totalCalories);
    System.out.println("Most caloric dish: " + mostCaloricDish);
    System.out.println("Dishes by type: " + dishesByType);
    ```
    * StreamForker는 스트림을 포크하고 포크된 스트림에 다른 연산을 할당할 수 있도록 편리하고 유연한 API를 제공
      * 각각의 연산은 스트림에 함수를 적용하는 형식으로 되어 있는데, 이 연산을 객체로 식별할 수 있음
      * 위 예제에서는 문자열로 연산을 식별함
      * 더 포크할 스트림이 없으면 StreamForker에 getResults를 호출해서 정의한 연산을 모두 수행하고 Results를 얻을 수 있음
      * 내부에서는 연산을 비동기적으로 실행하므로 getResults 메서드를 호출하면 결과를 기다리는 것이 아니라 즉시 반환됨
    * Results 인터페이스에 키를 전달해서 특정 연산의 결과를 얻을 수 있음
      * 연산 결과가 끝난 상황이라면 get 메서드가 결과를 반환하며, 아직 연산이 끝나지 않았으면 결과가 나올때까지 호출이 블록됨
      * 결과
        ```
        Short menu: pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon
        Total calories: 4300
        Most caloric dish: pork
        Dishes by type: {MEAT=[pork, beef, chicken], 
                         FISH=[prawns, salmon], 
                         OTHER=[french fries, rice, season fruit, pizza]}
        ```

### 성능 문제
* 위 방법이 스트림을 여러 번 탐색하는 방식에 비해 성능이 더 좋다고 할 수 없음
  * 특히 메모리에 있는 데이터로 스트림을 만든 상황에서는 블록 큐를 사용하면서 발생하는 오버헤드가 병렬 실행으로 인한 이득보다 클 수 있음
* 반대로 아주 큰 파일을 스트림으로 사용하는 등 비싼 I/O 동작을 수행하는 상황에서는 한 번만 스트림을 활용하는 것이 더 좋은 선택일 수 있음
  * 어느 쪽이 성능이 좋은지 판단하는 가장 좋은 방법은 '직접 측정'
* 이 예제는 람다 표현식의 유연성과 기존의 기능을 약간 응용하고 활용하면 자바 API에서 제공하지 않는 기능을 직접 구현할 수 있다는 것을 보여줌
---

## APPENDIX D : 람다와 JVM 바이트코드

### 익명 클래스
* 익명 클래스로 함수형 인터페이스를 구현할 수 있음
* 람다 표현식은 함수형 인터페이스의 추상 구현을 제공하므로 자바 컴파일러가 컴파일을 할 때  
  람다 표현식을 익명 클래스로 변환할 수 있다면 문제가 해결될 것 같지만 익명 클래스는 앱 성능에 악영향을 주는 특성을 포함함
  * 컴파일러는 익명 클래스에 대응하는 새로운 클래스 파일을 생성함
    * 보통 익명 클래스는 ClassName$1 등의 파일명을 갖는데 여기서 ClassName은 익명 클래스를 포함하는 클래스의 이름
    * 클래스 파일을 사용하려면 먼저 각각의 클래스를 로드하고 검증하는 과정이 필요하므로 앱 스타트업의 성능에 악영향을 미침
    * 람다를 익명 클래스로 변환한다면 람다 표현식에 대응하는 새로운 클래스 파일이 생성됨
  * 새로운 익명 클래스는 클래스나 인터페이스의 새로운 서브 형식을 만듦
    * Comparator를 표현하는 수백 개의 람다가 있다면 결국 수백 가지의 Comparator 서브형식이 생긴다는 의미
    * 이와 같은 상황에서는 JVM 런타임 성능을 개선하기 어려울 수 있음

### 바이트코드 생성
* 자바 컴파일러는 자바 소스 파일을 자바 바이트코드로 컴파일함
  * JVM은 생성된 바이트코드를 실행하면서 앱을 동작시킴
  * 익명 클래스와 람다 표현식은 각기 다른 바이트코드 명령어로 컴파일 됨
  * 다음의 명령어로 클래스 파일의 바이트코드와 상수 풀을 확인할 수 있음
    ``` javap -c -v ClassName ```
  * 자바 7 문법으로 Function 인터페이스의 인스턴스를 익명 내부 클래스로 구현
    ```
    public class InnerClass {
    	Function<Object, String> f = new Function<Object, String>() {
    		@Override
    		public String apply(Object obj) {
    			return obj.toString();
    		}
    	};
    }
    ```
    * 익명 내부 클래스로 구현한 Function의 바이트 코드
      ```
       0: aload_0
       1: invokespecial #1   // Method java/lang/Object."<init>":()V
       4: aload_0
       5: new           #2   // class (패키지명생략)/InnerClass$1
       8: dup
       9: aload_0
      10: invokespecial #3   // Method (패키지명생략)/InnerClass$1."<init>":(Lcom/jaenyeong/appendix/D/InnerClass;)V
      13: putfield       #4   // Field f:Ljava/util/function/Function;
      16: return
      ```
      * 위 코드 내용 확인
        * new라는 바이트코드 연산으로 InnerClass$1이라는 객체 형식을 인스턴스화
          * 동시에 새로 생성한 객체 참조를 스택으로 푸시함
        * dup 연산은 스택에 있는 참조를 복제
        * 객체를 초기화하는 invokespecial 명령어로 값을 소비
        * 스택의 톱(top)에는 여전히 객체 참조가 있으며 putfield 명령어로 객체 참조를 LambdaBytecode의 f1 필드에 저장함
      * InnerClass$1은 컴파일러가 익명 클래스에 붙인 이름
        * Function 인터페이스 구현 코드
          ```
          class InnerClass$1 implements java.util.function.Function<java.lang.Object, java.lang.String> {
              final InnerClass this$0;
              public java.lang.String apply(java.lang.Object);
                  Code:
                      0: aload_1
                      1: invokevirtual #3 // Method java/lang/Object.toString:()Ljava/lang/String;
                      4: areturn
          }
          ```

### 구원투수 InvokeDynamic
* 람다로 Function을 구현한 코드
  ```
  public class Lambda {
      Function<Object, String> f = obj -> obj.toString();
  }
  ```
  * 생성된 바이트코드 명령
    ```
     0: aload_0
     1: invokespecial #1         // Method java/lang/Object."<init>":()V
     4: aload_0
     5: invokedynamic #2,  0     // InvokeDynamic #0:apply:()Ljava/util/function/Function;
    10: putfield       #3         // Field f:Ljava/util/function/Function;
    13: return
    ```
    * 결과적으로 람다 표현식은 익명 내부 클래스와는 다른 방식으로 컴파일됨
      * 추가로 클래스를 생성하는 부분이 invokedynamic이라는 명령어로 대치되었음
    * 위에서 사용한 invokedynamic은 원래 용도와는 조금 다른 의도를 가짐
      * 예제에서는 invokedynamic으로 람다 표현식을 바이트코드로 변환하는 작업을 런타임까지 고의로 지연함
      * 즉, 이 같은 방식으로 invokedynamic을 사용해 람다 표현식을 구현하는 코드의 생성을 런타임으로 미룰 수 있음
      * 이러한 설계로 얻는 장점
        * 람다 표현식의 바디를 바이트코드로 변환하는 작업이 독립적으로 유지됨
          * 따라서 변환 작업이 동적으로 바뀌거나 나중에 JVM 구현에서 이를 더 최적화하거나 변환 작업을 고칠 수 있음
          * 변환 작업은 독립적이므로 바이트코드의 과거버전 호환성을 염려할 필요가 없음
        * 람다 덕분에 추가적인 필드나 정적 초기자 등의 오버헤드가 사라짐
        * 상태 없는(캡처하지 않는) 람다에서 람다 객체 인스턴스를 만들고, 캐시하고, 같은 결과를 반환할 수 있음
          * 자바 8 이전에도 이런 방식을 사용했음
          * 예를 들어 정적 final 변수에 특정 Comparator 인스턴스를 선언할 수 있음
        * 람다를 처음 실행할 때만 변환과 결과 연결 작업이 실행되므로 추가적인 성능 비용이 들지 않음
          * 즉, 두 번째 호출부터는 이전 호출에서 연결된 구현을 바로 이용할 수 있음

### 코드 생성 전략
* 런타임에 생성된 정적 메서드에 람다 표현식의 바디를 추가하면 람다 표현식이 바이트코드로 변환됨
  * 가장 간단하게 변환할 수 있는 람다 형식은 상태를 포함하지 않는 람다
    * 자신을 감싸는 영역에서 상태를 캡처하지 않음
  * 이 경우 컴파일러는 람다 표현식과 같은 시그니처를 갖는 메서드를 생성
  * 다음은 변환 처리 결과를 논리적으로 보여줌
    ```
    public class Lambda {
        Function<Object, String> f = [dynamic invocation of lambda$1]
    
        static String lambda$1(Object obj) {
            return obj.toString();
        }
    }
    ```
  * final (또는 final에 준하는) 지역 변수나 필드를 캡처하는 다음과 같은 람다 표현식의 변환과정은 좀 더 복잡함
    ```
    public class Lambda {
        String header = "This is a ";
        Function<Object, String> f = obj -> header + obj.toString();
    }
    ```
    * 이번에는 생성된 메서드에 람다를 감싸는 콘텍스트의 추가 상태를 전달할 인수가 필요하므로  
      메서드의 시그니처와 람다 표현식이 시그니처와 일치하지 않음
    * 가장 간단한 해결 방법은 람다 표현식의 인수에 캡처한 각 변수를 추가하는 것
    * 위 예제의 람다 표현식을 다음처럼 변환
      ```
      public class Lambda {
          String header = "This is a ";
          Function<Object, String> f = [dynamic invocation of lambda$1]
      
          static String lambda$1(String header, Object obj) {
              return obj -> header + obj.toString();
          }
      }
      ```
---
