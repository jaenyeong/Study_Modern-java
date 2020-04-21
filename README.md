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
  * 결국 스트림 리스트 List<Stream<String>>가 만들어지므로 문제가 해결되지 않음
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
  * Optional<T> 클래스는 값의 존재나 부재 여부를 표현하는 컨테이너 클래스
  * 위 예제에서 findAny 메서드는 아무 요소도 반환하지 않을 수 있음
  * isPresent()는 Optional이 값을 포함하면 true, 값을 포함하지 않으면 false 반환
  * ifPresent(Consumer<T> block)은 값이 있으면 주어진 블록을 실행함
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
    * 이 때 Optional<Integer> 반환 : 스트림에 아무 요소도 없을 수 있음
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
      * mapToInt 메서드는 IntStream을 반환 (Stream<Integer>가 아님)
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
    * filter 연산 다음에 rangeClosed가 반환한 IntStream을 boxed를 이용해서 Stream<Integer>로 복원
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
  * 자바 9에서 null이 될 수 있는 개체를 스트림으로 만들 수 있는 새로운 메소드 추가
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
    * 자바 9의 iterate 메소드는 프레디케이트를 지원함
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
    * Supplier<T>를 인수로 받아 새로운 값을 생산
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
    * IntStream의 generate 메서드는 Supplier<T> 대신에 IntSupplier를 인수로 받음
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
  * UnaryOperator<T>를 인수로 받는 iterate 메서드를 이용
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
  * 트랜잭션을 비싼 트랜잭션과 저럼한 트랜잭션 두 그룹으로 분류하기(Map<Boolean, List<Transaction>> 반환)
  * 트랜잭션을 도시 등 다수준으로 그룹화하기
    * 그리고 각 트랜잭션이 비싼지 저렴한지 구분하기(Map<String, Map<Boolean, List<Transaction>>> 반환)

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
    * reduce(Integer::sum)도 빈 스트림과 관련한 널 문제를 피할 수 있도록 int가 아닌 Optional<Integer>를 반환
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
  * 반환 형식 : List<T>
  * 사용 예제 : 스트림의 모든 항목을 리스트로 수집
  * 활용 예 : List<Dish> dishes = menuStream.collect(toList());
* toSet
  * 반환 형식 : Set<T>
  * 사용 예제 : 스트림의 모든 항목을 중복이 없는 집합으로 수집
  * 활용 예 : Set<Dish> dishes = menuStream.collect(toSet());
* toCollection
  * 반환 형식 : Collection<T>
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
  * 반환 형식 : Optional<T>
  * 사용 예제 : 주어진 비교자를 이용해서 스트림의 최댓값 요소를 Optional로 감싼 값을 반환. 스트림에 요소가 없을 때는 Optional.empty() 반환
  * 활용 예 : Optional<Dish> fattest = menuStream.collect(maxBy(comparingInt(Dish::getCalories)));
* minBy
  * 반환 형식 : Optional<T>
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
  * 반환 형식 : Map<K, List<T>>
  * 사용 예제 : 하나의 프로퍼티값을 기준으로 스트림의 항목을 그룹화하며 기준 프로퍼티값을 결과 맵의 키로 사용
  * 활용 예 : Map<Dish.Type, List<Dish>> dishesByType = menuStream.collect(groupingBy(Dish::getType));
* partitionBy
  * 반환 형식 : Map<Boolean, List<T>>
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
  * 예를 들어 Stream<T>의 모든 요소를 List<T>로 수집하는 toListCollector<T>라는 클래스를 구현할 수 있음
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
  * 정수로 이루어진 스트림에서 누적자와 최종 결과의 형식이 Map<Boolean, List<Integer>>인 컬렉터를 구현해야 함
  * 즉 Map<Boolean, List<Integer>>는 참과 거짓을 키로, 소수와 소수가 아닌 수를 값으로 가짐
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
  * X : reducing은 binaryOperator<T>, 즉 BiFunction<T, T, T>를 인수로 받음.  
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
  * 위 메소드를 이용해 isPrime 메소드를 다시 구현. 이번에도 대상 숫자의 제곱근보다 작은 소수만 검사
    ```
    public static boolean isPrime(List<Integer> primes, int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return takeWhile(primes, i -> i <= candidateRoot).stream().noneMatch(p -> candidate % p == 0);
    }
    ```
  * 스트림 API와는 달리 직접 구현한 takeWhile 메서드는 적극적(eager)으로 동작함
    * 따라서 가능하면 noneMatch 동작과 조화를 이룰 수 있도록 자바 9 스트림에서 제공하는 게으른 버전의 takeWhile을 사용하는 것이 좋음
---
