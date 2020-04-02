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
