# Valid
|Anotation|제약조건|
|:---:|:---:|
|@NotNull|널불가|
|@Null|널만 가능|
|@NotEmpty|널,빈문자열 불가|
|@NotBlnk|널,빈문자열,스페이스만 있는 문자열 불가|
|@Size(min,max)|문자열, 배열 해당크기에 만족하는가|
|@Pattern(regex)|해당 정규식을 만족하는지|
|@Min|지정값 이하|
|@max|지정값 이상|
|@Email|이메일 형식만 가능|
+ BookForm
#### regexp :한글문자나 영어대소문자나 숫가 0번이상 반복된다.
#### 가-힣 : 가부터 힣까지 모든 한글 문자
#### a-z|A-Z| : 영어 대소문자
#### 0-9 : 숫자
#### _ | : _(언더바)와 공백 포함
```java
public class BookForm {

    private Long id;

    @NotBlank(message = "동아리명은 필수입니다. 공백으로만 이루어진 동아리명은 불가능합니다.") // 값이 비어있으거나 공백만 있을시 메세지를 띄움
    @Size(max = 10,message = "동아리명은 최대 10글자 까지 입니다!")//최대길이 50글자, 50글자 넘기면 메시지를 띄움
    @Size(min = 2,message = "동아리명은 최소 2글자 이상으로 만들수 있습니다!")//최대길이 50글자, 50글자 넘기면 메시지를 띄움
    @Pattern(regexp = "^[가-힣|a-z|A-Z|0-9|_ |]*$",message = "특수문자나 (자음과모음)으로만 이루어진 동아리명은 사용 불가능합니다.")
    private String name;

    @Max(value = 20,message = "회원수는 최대 20명 이하여야 합니다.")
    @Min(value = 1,message = "회원수는 최소 1명 이상이여야 합니다.")
    private int totalNumber;

    private String clubJang;
}
```
### ClubController
+ controller에 @vaild 어노테이션을 써 사용한다.
+ BindingResult form에서 오류가 있을시 오류가 result에 담김
+ result에 오류가있을시 작성을 완료하지 않고 members를 모델에 담아 clubs/createClubForm로 되돌려보낸다.
```java
public class ClubController {

    @PostMapping("/clubs/new")
    public String create(@Valid BookForm bookForm,BindingResult result, @RequestParam("memberId") Long memberId,Model model) {//BindingResult form에서 오류가 있을시 오류가 result에 담김
        if(result.hasErrors()){
            List<Member> members = memberService.findMembers();
            model.addAttribute("members",members);
            return "clubs/createClubForm";
        }
        Book book = new Book();
        Member member = memberService.findOne(memberId);
        book.setName(bookForm.getName());
        book.setTotalNumber(bookForm.getTotalNumber());
        book.setClubJang(member.getName());
        try {
            clubService.saveClub(book);
        }catch (IllegalStateException e){
            String message = e.getMessage();
            log.info(message);
            model.addAttribute("message",message);
            List<Member> members = memberService.findMembers();
            model.addAttribute("members",members);

            return "clubs/createClubForm";
        }


        return "redirect:/clubs";
    }
}
```
+ 오류가 없다면 각 필드의 값을 넣어준후 저장메소드인 saveClub을 호출한다.
+ 이때 중복체크후 오류가 있을 가능성이 있어 try catch로 오류를 검사한다.
+ 오류가 있을 시 message값을 모델에 담아 clubs/createClubForm으로 반환한다.
###ClubService
+ 저장하기전에 validateDuplicateClub을 불러와 동아리명 중복체크를 한다.
+ 동아리명으로 찾은 findClubs가 비어있지않다면 존재하는 동아리명 이므로 오류를 생성한다.
```java
public class ClubService{
@Transactional
    public Long saveClub(Club club){
        validateDuplicateClub(club.getName());
        clubRepository.save(club);
        return club.getId();
    }
    private void validateDuplicateClub(String name) {
        // EXCEPTION
        List<Club> findClubs = clubRepository.findByName(name);
        if (!findClubs.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 동아리명입니다.");
        }
    }
 }
```
### clubCreateForm.html
+ th:errors=*{필드} 에러가 있을경우 필드의 값을 보여준다.
+ th:if="${message}!=null 모델로받은 message의 값이 null이 아닐경우 해당 오류메세지를 보여준다. 
```html
<form th:action="@{/clubs/new}" th:object="${bookForm}" method="post">
    <div class="form-group">
        <label th:for="name">동아리명</label>
        <input type="text" th:field="*{name}" class="form-control" placeholder="이름을 입력하세요"
               th:class="${#fields.hasErrors('name')}? 'form-control fieldError' : 'form-control'">
        <p class="ErrorMessage" th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
        <p class="ErrorMessage" th:name="${message}" th:if="${message}!=null" th:text="${message}"/>
```