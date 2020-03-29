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
추가된 자바 8 기능
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
  
## chapter 02 - 동작 파라미터화 코드 전달하기
동작 파라미터화(behavior parameterization)
* 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미
* 정확하게는 위처럼 추후에 다양한 동작을 수행할 수 있는 코드 블록을 해당 메서드의 파라미터로 전달하는 방식을 의미

익명 클래스 : 이름이 없는 클래스 (지역 클래스의 비슷한 개념)

quiz
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

* 올바른 함수형 인터페이스는?
  * [1] O
    ``` java
    public interface Adder {
        int add(int a, int b);
    }
    ```
  * [2] X : 추상 메서드 오버로딩으로 인해 함수형 인터페이스 조건인 추상 메서드 개수가 1개 초과
    ``` java
    public interface SmartAdder extends Adder {
        int add(double a, double b);
    }
    ```
  * [3] X : 추상 메서드가 없음
    ``` java
    public interface Nothing {
    }
    ```
* 어디에 람다를 사용할 수 있는가?
  * [1] O
    ``` java
    execute(() -> {});
    public void execute(Runnable r) {
        r.run();
    }
    ```
  * [2] O
    ``` java
    public Callable<String> fetch() {
        return () -> "Tricky example ;-)";
    }
    ```
  * [3] X : apple.getWeight의 시그니처와 일치하지 않음
    ``` java
  	Predicate<Apple> p = (Apple a) -> a.getWeight();
    ```
