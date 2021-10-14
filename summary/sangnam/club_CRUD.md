# Club CRUD 기능

## 생성

+ ClubRepository.java
  #### 매개변수로 받은 club의 아이디가 null이면 생성 아니면 병합(marge)

```java
public class ClubRepository {

    private final EntityManager em;

    public void save(Club club) {
        if (club.getId() == null) {
            em.persist(club);
        } else {
            em.merge(club);
        }
    }
}
```

+ createClubForm.html
  ##### 클럽생성폼 값을 clubs/new로 넘김

```html

<form th:action="@{/clubs/new}" th:object="${form}" method="post">
    <div class="form-group">
        <label th:for="name">동아리명</label>
        <input type="text" th:field="*{name}" class="form-control"
               placeholder="이름을 입력하세요">
    </div>
    <div class="form-group">
        <label th:for="price">총인원수</label>
        <input type="number" th:field="*{totalNumber}" class="form-control"
               placeholder="가격을 입력하세요">
    </div>
    <div class="form-group"><label th:for="author">저자</label>
        <input type="text" th:field="*{author}" class="form-control"
               placeholder="저자를 입력하세요">
    </div>
    <div class="form-group">
        <label th:for="isbn">ISBN</label>
        <input type="text" th:field="*{isbn}" class="form-control"
               placeholder="ISBN을 입력하세요">
    </div>
    <button type="submit" class="btn btn-primary">Submit</button>
</form>
```

+ ClubController.java
  #### 폼에서 clubs/new로값이 전달되면 새로우 객체를 생성해 폼에서 넘어온 값들을 설정
    + 넘어온 값을 설정 한 후 clubService.saveClub() 으로 생성

```java

@Controller
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    @GetMapping("/clubs/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "clubs/createClubForm";
    }

    @PostMapping("/clubs/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setTotalNumber(form.getTotalNumber());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        clubService.saveClub(book);

        return "redirect:/";
    }
}
```

## 조회

+ clubRepository.java

```java
public class ClubRepository {

    private final EntityManager em;

    /**
     * 하나조회
     */
    public Club findOne(Long id) {
        return em.find(Club.class, id);
    }

    /**
     * 모두조회
     */

    public List<Club> findAll() {
        return em.createQuery("select c from Club c", Club.class).getResultList();
    }
}
```

+ clubList.html

### 넘겨받은 club의 정보를 모두 보여준다.

```html

<tbody>
<tr th:each="club : ${clubs}">
    <td th:text="${club.id}"></td>
    <td th:text="${club.name}"></td>
    <td th:text="${club.totalNumber}"></td>
    <td>
        <a th:href="'javascript:deleteClub('+${club.id}+')'"
           class="btn btn-danger">삭제</a>
    </td>
    <td>
        <a th:href="'javascript:changeClub('+${club.id}+')'"
           class="btn btn-danger">수정</a>
    </td>
</tr>
</tbody>
```

+ ClubController.java

### 모두 조회해서 clubs/clubList로 리턴한다.

```java

@Controller
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    @GetMapping("/clubs")
    public String list(Model model) {
        List<Club> clubs = clubService.findClub();
        model.addAttribute("clubs", clubs);
        return "clubs/clubList";

    }
}
```

## 수정

+ clubList.html

  ##### 1. 넘겨받은 club의 정보를 모두 보여준다.

  ##### 2. 수정버튼을 누르면 해당 필드의 id값을 받아와 chageClub 함수의 매개변수로 넘긴다.

    + form을 생성해 clubs/id/change 페이지로 값을 넘긴다.

```html

<tbody>
<tr th:each="club : ${clubs}">
    <td th:text="${club.id}"></td>
    <td th:text="${club.name}"></td>
    <td th:text="${club.totalNumber}"></td>
    <td>
        <a th:href="'javascript:deleteClub('+${club.id}+')'"
           class="btn btn-danger">삭제</a>
    </td>
    <td>
        <a th:href="'javascript:changeClub('+${club.id}+')'"
           class="btn btn-danger">수정</a>
    </td>
</tr>
</tbody>
<script>
    function changeClub(id) {
        var form = document.createElement("form");
        form.setAttribute("method", "get");
        form.setAttribute("action", "/clubs/" + id + "/change");
        document.body.appendChild(form);
        form.submit();
    }
</script>
```

### 수정폼

+ ClubController.java

##### clubList에서 아이디값을 받아와 객체를 찾은후 clubs/clubInfoChage로 보냄

```java

@Controller
@RequiredArgsConstructor
public class ClubController {
    @GetMapping("/clubs/{clubId}/change")
    public String changeForm(Model model, @PathVariable("clubId") Long clubId) {
        Club club = clubService.findOne(clubId);
        model.addAttribute("form", club);
        return "clubs/clubInfoChange";
    }
}
```

+ clubInfoChange.html
  ##### 1. 수정할 객체의 값을 미리 셋팅해놓고 수정한다.

  ##### 2. 수정이 완료되면 값을 action="@{'/clubs/'+${clubId}+'/change'}"로 보낸다.

```html

<form th:action="@{'/clubs/'+${clubId}+'/change'}" th:object="${form}" method="post">
    <div class="form-group">
        <label th:for="name">동아리명</label>
        <input type="text" th:field="*{name}" class="form-control"
               placeholder="이름을 입력하세요">
    </div>
    <div class="form-group">
        <label th:for="price">총인원수</label>
        <input type="number" th:field="*{totalNumber}" class="form-control"
               placeholder="가격을 입력하세요">
    </div>
    <div class="form-group"><label th:for="author">저자</label>
        <input type="text" th:field="*{author}" class="form-control"
               placeholder="저자를 입력하세요">
    </div>
    <div class="form-group">
        <label th:for="isbn">ISBN</label>
        <input type="text" th:field="*{isbn}" class="form-control"
               placeholder="ISBN을 입력하세요">
    </div>
    <button type="submit" class="btn btn-primary">수정</button>
</form>
```

+ ClubControlle.java
  #### 수정폼에서 값을 받아와 clubService.updateClub의 매개변수로 넣어준다.

```java

@Controller
@RequiredArgsConstructor
public class ClubController {
    @PostMapping("/clubs/{clubId}/change")
    public String change(BookForm form, @PathVariable("clubId") Long clubId) {
        clubService.updateClub(clubId, form.getName(), form.getTotalNumber(),
                form.getAuthor(), form.getIsbn());
        return "redirect:/clubs";
    }
}
```

+ clubService.java
  ##### findone함수로 하나를 찾아온 후 값을 변경한다.
    + findone 찾으면 영속상태가 되어 값이변경되면 자동으로 변경감지를 하여 update를 한다.

```java
public class ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    public void updateClub(Long id, String name, int totalNumber, String autor, String isbn) {
        Book book = (Book) clubRepository.findOne(id);
        book.setName(name);
        book.setTotalNumber(totalNumber);
        book.setAuthor(autor);
        book.setIsbn(isbn);
    }
}
```

## 삭제

+ clubList.html

  ##### 1. 넘겨받은 club의 정보를 모두 보여준다.

  ##### 2. 삭제버튼을 누르면 해당 필드의 id값을 받아와 deleteClub 함수의 매개변수로 넘긴다.

    + form을 생성해 clubs/id/delete 페이지로 값을 넘긴다.

```html

<tbody>
<tr th:each="club : ${clubs}">
    <td th:text="${club.id}"></td>
    <td th:text="${club.name}"></td>
    <td th:text="${club.totalNumber}"></td>
    <td>
        <a th:href="'javascript:deleteClub('+${club.id}+')'"
           class="btn btn-danger">삭제</a>
    </td>
    <td>
        <a th:href="'javascript:changeClub('+${club.id}+')'"
           class="btn btn-danger">수정</a>
    </td>
</tr>
</tbody>
<script>
    function deleteClub(id) {
        if (confirm(id + "번 동아리를 삭제하시겠습니까?")) {
            var form = document.createElement("form");
            form.setAttribute("method", "post");
            form.setAttribute("action", "/clubs/" + id + "/delete");
            document.body.appendChild(form);
            form.submit();
        }

    }
</script>
```

##### id값을 받아와 clubservice.deleteClub의 매개변수로 넘긴다.

##### 받아온 id값으로 해당 엔티티를 검색하여 changeStatus와deleteClub 메소드의 매개변수로 넣어준다.

```java

@Controller
@RequiredArgsConstructor
public class ClubController {
    @
    @PostMapping("clubs/{clubId}/delete")
    public String deleteClub(@PathVariable("clubId") Long clubId) {
        Club club = clubService.findOne(clubId);
        joinClubService.chageStatus(club);
        clubService.deleteClub(club);
        return "redirect:/clubs";
    }
}
```

### 1. 매개변수로 받은 club에 가입한 멤버의 상태를 cancle로 바꿔준다.

+ JoinClubRepository
####매개변수로 넘어온 클럽으로 join_id를 찾는다.

 ```java
  public interface JoinclubMapping {
    Long getJoin_Id();  //조인아이디
}
 ```

```java

@Repository
public interface JoinClubRepository extends JpaRepository<JoinClub, Long> {
    List<JoinclubMapping> findByClub(Club club);
}
```
+ JoinClubService
#### 매개변수로 넘어온 클럽의 join_id값을 받아와 해당 사이즈만큼 포문으로 돌리며
#### 해당하는 멤버의 상태를 cancel로 바꾼다.
```java
public class JoinClubService {
    @Transactional
    public void chageStatus(Club club) {
        List<JoinclubMapping> byClub = joinClubRepository.findByClub(club);
        int size = byClub.size();
        for (int i = 0; i < size; i++) {
            Join join = joinRepository.findOne(byClub.get(i).getJoin_Id());
            join.cancel();
        }
    }
```

### 2. 매개변수로 받은 club을 삭제한다.

+ clubRepository

```java
public class ClubRepository {

    private final EntityManager em;

    public void deleteOne(Club club) {
        em.remove(club);
    }
}
```

+ clubService

```java
public class ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    public void deleteClub(Long id) {
        Club club = clubRepository.findOne(id);
        clubRepository.deleteOne(club);
    }
}
```

+ JoinClub과 연관관계를 맺고있고 cascade.REMOVE옵션을 추가하여. 클럽이 삭제될 경우 같이 삭제 된다.

```java
public abstract class Club {

    @OneToMany(mappedBy = "club", cascade = CascadeType.REMOVE)
    private List<JoinClub> joinClubs = new ArrayList<>();
}
```

## 아직 수정해야 할것

+ 삭제시 member 신청 상태를 cancle로 바꾸기.
+ 클럽의 아이디를 컨트톨러에서 받아와서 -> 조인클럽 서비스의 changestatus를 부른다. ->
  조인클럽 리포지토리에 선언해놓은 findByClub 으로 클럽객체를 넣어 찾는다. 다시 정리할거다.
  




