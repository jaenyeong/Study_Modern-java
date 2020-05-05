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
  * 스레드 풀을 이용하려면 RecursiveTask<R>의 서브클래스를 만들어야 함
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
    * 스트림은 int, long, double 기본형만 제공하므로 Stream<Character>를 사용해야 함
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
      * ValidationStrategy는 함수형 인터페이스며 Predicate<String>과 같은 함수 디스크립터를 갖고 있음
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
    * 이전에 정의한 makeCustomerHappy의 메서드 시그니처와 일치하도록 Consumer<Customer> 형식을 갖는 두 번째 인수를 processCustomer에 추가
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
    * 작업 처리 객체를 Function<String, String>, 더 정확히 UnaryOperator<String> 형식의 인스턴스로 표현 가능
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
        * String은 파일에서 파싱할 행을 의미하며 Files.lines는 정적 유틸리티 메서드로 Stream<String>을 반환함
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
  * 자바 8은 선택형값 개념의 영향을 받아서 java.util.Optional<T>라는 새로운 클래스 제공

### Optional 클래스 소개
* 자바 8은 하스켈, 스칼라의 영향을 받아 Optional<T>라는 새로운 클래스 제공
* Optional 클래스는 선택형값을 캡슐화하는 클래스
* 값이 있으면 Optional 클래스는 값을 감싸고 없으면 Optional.empty 메서드로 Optional을 반환
* Optional.empty는 Optional의 특별한 싱글턴 인스턴스를 반환하는 정적 팩토리 메서드
* null 참조와 Optional.empty()는 의마상으론 비슷하지만 차이점이 많음
* Optional<Car>
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
      * Person은 Optional<Car>를 참조, Car는 Optional<Insurance>를 참조하는데
      * 이는 사람이 차를 소유했을 수도 아닐 수도 있으며
      * 자동차가 보험에 가입되어 있을 수도 아닐 수도 있음을 명확히 설명함
      * 또한 보험 회사 이름은 Optional<String>이 아닌 String 형식으로 선언됨
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
      * optPerson의 형식은 Optional<Person>이므로 map 메서드를 호출할 수 있음
      * 하지만 getCar는 Optional<Car> 형식의 객체를 반환
      * 즉, map 연산의 결과는 Optional<Optional<Car>> 형식의 객체
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
        * getCar 메서드는 Optional<Car>를 반환, Optional 내부의 Person이 Optional<Car>로 변환되면서 중첩 Optional이 생성됨
        * 따라서 flatMap 연산으로 Optional을 평준화함
        * 평준화 과정이란 이론적으로 두 Optional을 합치는 기능을 수행하면서 둘 중 하나라도 null이면 빈 Optional을 생성하는 연산
        * fflatMap을 빈 Optional에 호출하면 아무일도 일어나지 않고 그대로 반환됨
        * 반면 Optional이 Person을 감싸고 있다면 flatMap에 전달된 Function이 Person에 적용됨
        * Function을 적용한 결과가 이미 Optional이므로 flatMap 메서드는 결과를 그대로 반환할 수 있음
      * 두 번째 단계도 Optional<Car>를 Optional<Insurance>로 변환함
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
  * List<Person>을 인수로 받아 자동차를 소유한 사람들이 가입한 보험 회사의 이름을 포함하는 Set<String>을 반환하는 메서드 구현
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
    * 예제에서 getCar() 메서드가 단순히 Car가 아니라 Optional<Car>를 반환
      * 사람이 자동차를 가지지 않을 수도 있는 상황임을 자각
      * 따라서 첫 번째 map 변환을 수행하고 Stream<Optional<Car>>를 얻음
      * 이어지는 두 개의 map 연산을 이용해 Optional<Car>를 Optional<Insurance>로 변환
        * 그 다음 스트림이 아니라 각각의 요소에 했던 것처럼 각각을 Optional<String>로 변환
      * 세 번의 변환 과정을 거쳐 Stream<Optional<String>>을 얻음
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
    * 이제 두 Optional을 인수로 받아 Optional<Insurance>를 반환하는 null 안전 버전(null safe version)의 메서드를 구현 해야 한다고 가정
      * 인수로 전달한 값 중 하나라도 비어 있으면 빈 Optional<Insurance>를 반환
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
    * 예를 들어 Optional<Integer> 대신 OptionalInt를 반환할 수 있음
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
* 자바 8에서는 값이 있거나 없음을 표현할 클래스 java.util.Optional<T>를 제공함
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
  * 반면 person값이 있으면 flatMap 메서드에 필요한 Optional<Insurance>를 반환하는 Function의 입력으로 person을 사용함
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
    * 결국 TemporalAdjuster 인터페이스를 UnaryOperator<Temporal>과 같은 형식으로 간주할 수 있음

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
      * UnaryOperator<LocalDate>를 인수로 받는 TemporalAdjusters클래스의 정적 팩토리 메서드 ofDateAdjuster를 사용
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
