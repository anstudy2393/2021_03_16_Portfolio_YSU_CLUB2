# Project Study

# JPA/SpringFramework
+ JpaRepository : https://github.com/Kimginam97/2021_03_16_Portfolio_YSU_CLUB/blob/master/summary/woowon/JpaRepository.md
##### @NotEmpty
+ null과 ""을 허용하지 않게 하지만 " "(공백)은 허용이 된다.
````java
    public class NoticeBoardForm {
    @NotEmpty(message = "이름은 필수입니다.")
    private String name;
    }
````
##### @NotBlank
+ null과 " "(공백)을 허용하지 않는다
```java
    @NotBlank(message = "게시글 제목은 필수입니다, 공백으로만 이루어진 제목은 불가능합니다!")
    private String title;
```
##### @Size
+ 최소,최대 길이를 줄수있다
```java
    @Size(min=10,max=20,message="길이10~20")
    private String msg;
```
##### @Column(columnDefinition="")
+ Column의 기본값을 설정한다.
+ @Column(columnDefinition = "크기 default 'default값'")
+ 테이블이 생성될때 정해둔 default 값이 들어가게 된다.
````java
public class NoticeBoard {
  @Column(columnDefinition = "varchar(100) default '관리자'")
    private String name;
}
````
##### @DynamicInsert
+ insert시 null인 값이 sql문에 포함되지 않는다.
+ @DynamicInsert를 사용하면 name에 값을 세팅하지않고 쿼리를 날리면 default로 설정된 값이 들어가게 된다.
````java
@Entity
@Getter @Setter @DynamicInsert
public class NoticeBoard {
    @Column(columnDefinition = "varchar(10) default '이름'")
    private String name;
}
````
# thymeleaf
##### th:href
+ th:href="@{'경로'}" 으로 페이지를 이동시킬수 있다.
+ th:href="@{'경로'+${보낼 값}}" 으로 페이지를 이동시킬때 값을 전달 할 수 있다.
````html
<a th:href="@{'경로'+${보낼 값}}" />
````
##### th:object
+ form에서 submit을 할때 form의 데이터가 th:object에 설정한 객체로 받아진다
```html
<form role="form" action="/noticeBoard/write" th:object="${noticeBoardForm}" method="post">
```
##### th:field
+ 각각 필드들을 매핑해주는 역할. th:object에서 설정해준 객체와 매칭해준다
```html
<input type="text" th:field="*{title}" placeholder="제목을 입력하세요">
```
##### th:onclick
+ location.href설정을 할때 사용
```html
<a href="" th:href="@{'/noticeBoard/cancel/'+${notice.id}}" class="btn btn-danger" th:onclick="return confirm('게시글을 삭제하시겠습니까?')">삭제</a>
```
##### th:with
+ 변수형태의 값을 설정(변수설정)
```html
<ul class="pagination centered" style="display: inline-block"
                            th:with="start=${T(Math).floor(noticeList.number/10)*10 + 1},
                                last=(${start + 9 < noticeList.totalPages ? start + 9 : noticeList.totalPages})">
```
##### th:if
+ c나 java처럼 조건문
```html
<div class="container col-md-10" th:if="${getBoardList.size()}!=0">
```
##### th:unless
+ if-else문에 else구문에 속한다
```html
<div class="container col-md-10" th:unless="${getBoardList.size()}!=0">
```
##### th:each
+ 콜렉션을 반복할때 사용
```html
<tr th:each="notice : ${noticeList}">
    <td th:text="${notice.id}"></td>
    <td><a th:text="${notice.title}" href="" th:href="@{'/noticeBoard/view/'+${notice.id}}"/></td>
    <td th:text="${notice.writeDate}"></td>
</tr>
```
##### th:errors
+ 해당값에 오류가 있는경우 출력
```html
<p class="ErrorMessage" th:if="${#fields.hasErrors('title')}" th:errors="*{title}">Incorrect date</p>
```
# 페이징
+ https://github.com/Leewoowon980522/YSU_CLUB_Test/blob/master/sumarry/Paging.md

