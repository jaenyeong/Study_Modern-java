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
