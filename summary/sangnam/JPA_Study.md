#JPA 정리
## Entity
+ 데이터 베이스에 저장하기 위해 유저가 정의한 클래스
+ table을 객체화 시킨것으로 보면 된다.
### @id
+ PK를 가지는 변수다.
###@Column(name = "")
+ 컬럼명을 지정한 변수명과 다르게 해주고 싶을때 사용한다.

#### JpaRepository
 + 기본적인 CRUD기능이 있는 인터페이스
 + 인터페이스를 상속하여  사용하면 된다. JpaRepository<Entity,PK타입>
```java
@Repository
public interface JoinClubRepository extends JpaRepository<JoinClub, Long> {
    List<JoinclubMapping> findByClub(Club club);
}
```
#### 제공하는 기능
+ save() : 저장
+ findeById() : PK값으로 하나찾기
+ findAll() : 전체 찾기(페이징,정렬가능)
+ count() : 레코드 갯수 
+ delete() : 삭제
#### 쿼리메소드
+ 위의 기능을 제외한 조회기능을 추가하고 싶을때 사용
+ 규칙에 맞게 메소드를 작성해야 한다.
findBy로 시작
+ 예시
```java
@Repository
public interface JoinClubRepository extends JpaRepository<JoinClub, Long> {
    List<JoinclubMapping> findByClub(Club club);
}
```



