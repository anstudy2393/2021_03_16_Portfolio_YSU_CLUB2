# Project 공지사항 게시판 TestCode

### NoticeBoardForm
```java
@Getter
@Setter
public class NoticeBoardForm {

    private Long id;

    @NotBlank(message = "게시글 제목은 필수입니다, 공백으로만 이루어진 제목은 불가능합니다!") // 값이 비어있으거나 공백만 있을시 메세지를 띄움
    @Size(max = 50, message = "제목은 최대 50글짜 까지 입니다!")//최대길이 50글자, 50글자 넘기면 메시지를 띄움
    private String title;//제목

    @NotBlank(message = "게시글 내용은 필수입니다, 공백으로만 이루어진 내용은 불가능합니다!")// 값이 비어있으거나 공백만 있을시 메세지를 띄움
    @Size(max = 2000, message = "내용은 최대 2000글짜 까지 입니다!")//최대길이 2000글자, 2000글자 넘기면 메시지를 띄움
    private String content;//내용

    private String writeDate;//작성날짜
    private String updateDate;//수정날짜
}
```

+ title과 content에 @NotBlank를 사용해 null또는 " "으로만 이루어져있으면 오류 message를 띄운다
+ title과 content에 @size를 사용해 최대길이를 주고 최대 길이를 넘길시 오류 message를 띄운다

### NoticeBoardRepository

```java
@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
    List<NoticeBoard> findTop3ByOrderByIdDesc();//데이터3개만 가져오기
}
```

+ JpaRepository를 상속 받아 사용
+ findTop3ByOrderByIdDesc로 쿼리메소드를 만들어 최신 게시글 3개를 가지고옴

### NoticeBoardController

```java
@Controller
@RequiredArgsConstructor
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    @GetMapping("/noticeBoard/write")
    public String writeForm(Model model) {
        model.addAttribute("noticeBoardForm", new NoticeBoardForm());
        return "noticeBoard/noticeWrite";
    }

    //게시글 작성
    @PostMapping("/noticeBoard/write")
    public String write(@Valid NoticeBoardForm form, BindingResult result) {//BindingResult form에서 오류가 있을시 오류가 result에 담김
        // result에 에러가 있으면 noticeBoard/noticeWrite로 다시 반환
        if (result.hasErrors()) {
            return "noticeBoard/noticeWrite";
        }
        noticeBoardService.write(form);
        return "redirect:/noticeBoard/list"; // 첫 번째 페이지로 넘어가게함
    }

    //게시글 보기
    @GetMapping("/noticeBoard/view/{noticeId}")
    public String noticeView(@PathVariable("noticeId") Long noticeId, Model model) {
        NoticeBoard noticeBoard = noticeBoardService.findOne(noticeId);
        model.addAttribute("noticeId", noticeBoard);
        return "noticeBoard/noticeView";
    }

    //게시글 삭제
    @GetMapping("/noticeBoard/cancel/{noticeId}")//게시글 번호를 가지고옴
    public String cancel(@PathVariable("noticeId") Long noticeId) {
        noticeBoardService.delete(noticeId);
        return "redirect:/noticeBoard/list";
    }

    //게시글 수정
    //=====해당 게시글 title, content가져오기=====//
    @GetMapping("/noticeBoard/update/{noticeId}")
    public String update(@PathVariable("noticeId") Long noticeId, Model model) {
        NoticeBoard findNotice = noticeBoardService.findOne(noticeId);
        model.addAttribute("noticeBoardForm", findNotice);
        return "noticeBoard/noticeUpdate";
    }

    @PostMapping("/noticeBoard/update/{noticeId}")
    public String update(@Valid NoticeBoardForm form, BindingResult result, @PathVariable("noticeId") Long id) {//BindingResult form에서 오류가 있을시 오류가 result에 담김
        // result에 에러가 있으면 noticeBoard/noticeWrite로 다시 반환
        if (result.hasErrors()) {
            return "noticeBoard/noticeUpdate";
        }
        noticeBoardService.update(id, form);//새로 저장한값을 저장시킴
        return "redirect:/noticeBoard/list"; // 게시글 목록으로 이동
    }

    //전체 게시글 가지고오기
    @GetMapping("/noticeBoard/list")
    public String getNoticeBoardList(@PageableDefault Pageable pageable, Model model) {
        Page<NoticeBoard> noticeBoardList = noticeBoardService.findAll(pageable);
        model.addAttribute("noticeList", noticeBoardList);

        List<NoticeBoard> getBoardList = noticeBoardList.getContent();
        model.addAttribute("getBoardList", getBoardList);//list size가져옴, list size확인용
        return "noticeBoard/noticeList";
    }

    //메인 index 최신 공지사항 3개 가져오기
    @GetMapping("/")
    public String getNoticeBoardListTop3(Model model) {
        List<NoticeBoard> getTop = noticeBoardService.findTop3Board();
        model.addAttribute("findTop3", getTop);
        return "index";
    }
}
```
+ 게시글 작성또는 게시글 수정시 메소드에 @Valid NoticeBoardForm form, BindingResult result를 선언해 오류를 잡아낸다. form에서 오류가 있을시 result에 오류가 담겨 작성완료가 되지 않고 작성 페이지로 오류메시지를 넘긴다

### noticeWrite

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragments/header :: header"/>
<style>
    .fieldError{
            border-color: #bd2130;
            }
</style>
<body>
<div th:replace="fragments/navbar :: navbar"/>

<div class="container-fluid ">
<div class="row">
<div th:replace="fragments/sidebar :: sidebar"/>
<div class="container col-md-10"style="padding: 10px">
<div class="jumbotron"style="margin:0;padding:10px">
<form role="form"action="/noticeBoard/write"th:object="${noticeBoardForm}"method="post">
<div class="form-group">
<label th:for="title">제목</label>
<input type="text"th:field="*{title}"class="form-control"placeholder="제목을 입력하세요"
        th:class="${#fields.hasErrors('title')}? 'form-control fieldError' : 'form-control'">
<p class="ErrorMessage"th:if="${#fields.hasErrors('title')}"th:errors="*{title}">Incorrect date</p>
</div>
<div class="form-group">
<label th:for="content">내용</label>
<textarea class="form-control"th:field="*{content}"placeholder="내용을 입력하세요"
        style="height: 500px"
        th:class="${#fields.hasErrors('content')}? 'form-control fieldError' : 'form-control'"></textarea>
<p class="ErrorMessage"th:if="${#fields.hasErrors('content')}"th:errors="*{content}">Incorrect date</p>
</div>
<button type="submit"class="btn btn-primary">Submit</button>
</form>
</div>
<br/>
</div><!--/container-->
</div>
</div>

<div th:replace="fragments/footer :: footer"/>
</body>
</html>
```
+ <p>태그에 있는 th:if안에서 에러가 있으면 에러 메시지를 보여준다
### NoticeBoardServiceTest
![img](https://user-images.githubusercontent.com/76415175/119916325-4992e100-bf9f-11eb-8ce6-9fcb7b9a5285.png)
+ @AfterEach는 테스트 코드에 여러 메소드가 있다면 각각 메서드가 끝날때 마다 실행한다
+ 32행은 모든 데이터를 삭제하는 코드
+ 33행은 각각 테스트 코드가 실행하고 끝이나면 게시글 저장,수정,삭제 테스트때 값이 저장이되어 @GeneratedValue로 인해 값이 계속 증가가 되어 메소드가 끝날때 마다 값을 시작값인 41로 초기화 시켜준다
#### 게시글 저장 메서드
```java
private NoticeBoardForm createBoard(){
    NoticeBoardForm noticeBoard = new NoticeBoardForm();
    noticeBoard.setTitle("TestCode제목");
    noticeBoard.setContent("TestCode내용");
    noticeBoardService.write(noticeBoard);
    return noticeBoard;
}
```
+ 게시글을 저장하는 메서드를 만들어 다른 테스트 코드에서 반복적으로 사용이되는 저장을 메소드 선언으로 간편화 시키기 위한 메서드
#### 게시글 생성 메서드
```java
@Test
    public void 게시글작성_성공() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = createBoard();

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부확인
        assertEquals(noticeBoard.getContent(),getNotice.getContent());//값 등록여부확인
    }
```
+ 게시글을 저장한후 저장한 게시글을 가져와 자신이 저장한 게시글과 디비에 있는 게시글이 같은지를 확인한다
+ assertEquals는 두 값을 비교해 같으면 통과 시킨다
#### 게시글 수정 메서드
```java
    @Test
    public void 게시글수정_성공() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = createBoard();

        //값 변경
        NoticeBoard getNotice = noticeBoardService.findOne(41L);
        NoticeBoardForm noticeForm = new NoticeBoardForm();
        noticeForm.setTitle("변경");
        noticeForm.setContent("변경2");

        noticeBoardService.update(getNotice.getId(), noticeForm);

        //변경된 값 가져오기
        NoticeBoard changeNotice = noticeBoardService.findOne(getNotice.getId());

        //값 변경 확인
        assertEquals(changeNotice.getTitle(), "변경");
        assertEquals(changeNotice.getContent(), "변경2");
    }
```
+ 게시글을 저장후 저장한 데이터를 찾아와 값을 변경하고 변경한 값을 가지고와 assertEquals로 값이 변경되었는지 확인한다
#### 게시글 삭제 메서드
```java
    @Test
    public void 게시글삭제_성공() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = createBoard();

        //값 삭제
        noticeBoardService.delete(41L);

        //값 찾기
        NoticeBoard notice = noticeBoardService.findOne(41L);
        assertNull(notice);//Null인지 확인,삭제확인
    }
```
+ 게시글을 저장한다음 저장한값을 삭제후 저장했을때 데이터를 찾아와 assertNull로 해당 값이 null인지 확인한다 null이면 통과
#### 게시글 조건 확인 메서드(Null)
```java
@Test
    public void 게시글제목Null() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setContent("TestCode내용");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertNull(getNotice.getTitle());//Null값 확인
    }
    @Test
    public void 게시글제목NotNull() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("TestCode제목");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertNotNull(getNotice.getTitle());//NotNull값 확인
    }
```
+ 게시글을 저장할때 title에 값을 넣지않아 null로 만들고 저장을한후 assertNull로 해당값이 null인지 확인한다
+ 게시글을 저장할때 title에 값을 넣어 null로 만들고 저장을한후 assertNotNull로 해당값이 null이 아닌지 확인한다
#### 게시글 조건 확인 메서드(공백체크)
```java
 @Test
    public void 게시글제목공백() throws Exception {
        //정규식
        String pattern = "^[\\s]*$"; //공백만체크

        //값저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("    ");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertTrue(Pattern.matches(pattern,getNotice.getTitle()));//title이 공백만있다면 True
    }
    @Test
    public void 게시글제목공백_문자() throws Exception {
        //정규식
        String pattern = "^[\\s]*$"; //공백만체크

        //값저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("    1234");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertFalse(Pattern.matches(pattern,getNotice.getTitle()));//title이 공백만있는게 아니라면 True
    }
```
+ 게시글을 저장할때 title에 공백만 주고 값을 저장한후 assertTrue로 title이 정규식패턴에 걸리는지 확인한다. 걸린다면 통과
+ 게시글을 저장할때 title에 값을 주고 값을 저장한후 assertFalse로 title이 정규식패턴에 걸리는지 확인한다. 걸리지않는다면 통과
#### 게시글 조건 확인 메서드(길이체크)
```java
    @Test
    public void 게시글제목길이50이하() throws Exception {
        //길이는 최대 50글자
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("TestCode제목");
        noticeBoard.setContent("TestCode내용");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);
        int titlel = getNotice.getTitle().length();//길이 저장

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertTrue(titlel<=50);//제목의 크기가 50보다 작으면 True
    }
    @Test
    public void 게시글제목길이50초과() throws Exception {
        //길이는 최대 50글자
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("TestCode제목TestCode제목TestCode제목TestCode제목TestCode제목");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);
        int titlel = getNotice.getTitle().length();//길이 저장

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertFalse(titlel<50);//제목의 크기가 50보다 크면 True
    }
```
+ 게시글을 저장할때 title에 값을 저장하고 디비에 저장된 값을 가져와 titlel에 title의 길이를 저장한후 assertTrue로 길이를 체크한다. 50이하면 통과
+ 게시글을 저장할때 title에 값을 저장하고 디비에 저장된 값을 가져와 titlel에 title의 길이를 저장한후 assertFalse로 길이를 체크한다. 50초과면 통과