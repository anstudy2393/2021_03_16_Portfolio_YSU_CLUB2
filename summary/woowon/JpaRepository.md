# JpaRepository
___
### JpaRepository
+ 스프링 부트에서는 기본적인 CRUD가 가능하도록 JpaRepository 인터페이스 기능을 제공
+ 인터페이스를 상속받기만 하면되기 때문에 따로 어노테이션을 추가할 필요 없음
+ JpaRepository<T, ID>
    + T는 Entity의 타입 클래스, ID는 PK값의 타입
```java
public interface BoardRepository extends JpaRepository<Board, Long> {
//Board 타입 클래스의,PK값의 타입은 Long
}
```
### JpaRepository가 제공하는 기능
+ save() : 레코드 저장(insert,update)
+ findOne() : pk값으로 레코드 찾기
+ findAll() : 전체 레코드 찾기,정령,페이징
+ count() : 레코드 갯수
+ delete() : 레코드 삭제

### 쿼리 메소드
+ 위에 기본적으로 제공하는 것이 아닌경우에는 규칙에 맞는 메서드를 추가하면된다
+ findBy... : 쿼리 요청
+ ex
```java
    List<GetId> findByTitle(String title);//String title에 들어가는것만 가지고옴
    List<Board> findTop3ByOrderByIdDesc();//데이터3개만 가져오기
```
### 쿼리 메소드에 포함할수 있는 키워드
+ And,Or,Between,Like,OrderBy 등이 있다
+ https://docs.spring.io/spring-data/jpa/docs/1.10.1.RELEASE/reference/html/#jpa.sample-app.finders.strategies