# Paging

### paging

+ JpaRepository 가 PagingAndSortingRepository 를 상속하고 있다.
+ JpaRepository 를 상속받는 repository인터페이스에 있는 findAll메소드에 pageable 을 넣어주면 된다.

```java

@NoRepositoryBean
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {

    /**
     * Returns all entities sorted by the given options.
     *
     * @param sort
     * @return all entities sorted by the given options
     */
    Iterable<T> findAll(Sort sort);

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    Page<T> findAll(Pageable pageable);
}
```

### clubService

```java
public class ClubService {
    public Page<Club> findAll(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");
        return clubRepository.findAll(pageable);
    }
}
```

+ pageable.getPageNumber()는 현재 페이지를 나타낸다 첫페이지이면 0을 그게 아니라면 현재 페이지에 1을 뺀다
+ Pageable의 page는 index가 0부터 시작
  (0부터 시작하기때문에 1페이지는 index가 0이되고, 2페이지는 index가 1이되야하기때문에 -1을 한다.)
+ PageRequest.of(페이지번호,나타낼 데이터의수, 정렬방식, 정렬기준)
+ pageable에 값을 준후 repository에 findAll(pageable)을 주면 해당 조건에 맞는 값이 들어가게된다

### clubController

```java

@Controller
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;
    private final JoinClubService joinClubService;

    // 페이징
    @GetMapping("/clubs")
    public String list(@PageableDefault Pageable pageable, Model model) {
        Page<Club> clubList = clubService.findAll(pageable);
        model.addAttribute("clubList", clubList);

        List<Club> getClubList = clubList.getContent();
        model.addAttribute("getClubList", getClubList);//list size가져옴, list size확인용
        return "/clubs/clubList";
    }
}
```

+ view 에서 페이지 번호를 받아와 service에 findAlll에서 페이징 처리하고 return받은 값을 clubList로 받아 model을 이용해 view에 전달한다.
+ getClubList는 return받은 값의 갯수를 받는다. 게시글이 있는지 없는지 확인하는용도.

### clubList.html

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragments/header :: header"/>
<body>
<div th:replace="fragments/navbar :: navbar"/>
<div class="container-fluid" th:if="${getClubList.size()}!=0">
    <div class="row">
        <div th:replace="fragments/sidebar :: sidebar"/>
        <div class="col-md-10">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>#</th>
                    <th>동아리명</th>
                    <th>남은 가입가능인원수</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr th:each="club : ${clubList}">
                    <td th:text="${club.id}"></td>
                    <td th:text="${club.name}"></td>
                    <td th:text="${club.totalNumber}"></td>
                    <td>
                        <a th:href="'javascript:deleteClub('+${club.id}+')'"
                           class="btn btn-danger">삭제</a>
                    </td>
                    <td>
                        <a th:href="'javascript:changeClub('+${club.id}+')'"
                           class="btn btn-primary">수정</a>
                    </td>
                </tr>
                </tbody>
            </table>
            <nav style="text-align: center">
                <ul class="pagination centered" style="display: inline-block"
                    th:with="start=${T(Math).floor(clubList.number/10)*10 + 1},
                    last=(${start + 9 < clubList.totalPages ? start + 9 : clubList.totalPages})">
                    <li style="float: left" class="page-item">
                        <a class="page-link" th:href="@{/clubs(page=1)}" aria-label="First">
                            <span aria-hidden="true">First</span>
                        </a>
                    </li>

                    <li style="float: left" th:class="${clubList.first} ? 'page-item'">
                        <a class="page-link" th:href="${clubList.first} ? '#' :@{/clubs(page=${clubList.number})}"
                           aria-label="Previous">
                            <span aria-hidden="true">&lt;</span>
                        </a>
                    </li>

                    <li style="float: left" th:each="page: ${#numbers.sequence(start, last)}"
                        th:class="${page == clubList.number + 1} ? 'page-item'">
                        <a class="page-link" th:text="${page}" th:href="@{/clubs?(page=${page})}"></a>
                    </li>

                    <li style="float: left" th:class="${clubList.last} ? 'page-item'">
                        <a class="page-link" th:href="${clubList.last} ? '#' : @{/clubs(page=${clubList.number + 2})}"
                           aria-label="Next">
                            <span aria-hidden="true">&gt;</span>
                        </a>
                    </li>

                    <li style="float: left" class="page-item">
                        <a class="page-link" th:href="@{/clubs(page=${clubList.totalPages})}" aria-label="Last">
                            <span aria-hidden="true">Last</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div> <!-- /container -->
<div class="container-fluid" th:unless="${getClubList.size()}!=0"><!--데이터가 없을경우-->
    <div class="row">
        <div th:replace="fragments/sidebar :: sidebar"/>
        <div class="col-md-10">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>#</th>
                    <th>동아리명</th>
                    <th>남은 가입가능인원수</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th colspan="4" style="text-align: center">등록된 동아리가 없습니다!</th>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div th:replace="fragments/footer :: footer"/>
</body>
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

    function changeClub(id) {
        var form = document.createElement("form");
        form.setAttribute("method", "get");
        form.setAttribute("action", "/clubs/" + id + "/change");
        document.body.appendChild(form);
        form.submit();
    }


</script>
</html>
```

#### 값 체크

```html
<div class="container-fluid" th:if="${getClubList.size()}!=0">
데이터 값이 0이 아닐경우 
<div class="container-fluid" th:unless="${getClubList.size()}!=0"><!--데이터가 없을경우-->
데이터값이 0인경우를 나타낸다.
```

### 페이지 버튼
#### 시작,마지막페이지값 변수지정
+ th:with는 변수의 값을 정하는것으로 start는 시작페이지를 last는 마지막 페이지를 변수로 저장한다

#### 1페이지
+ 처음 페이지 즉 1페이지를 나타내며 클릭시 1페이지로 이동하는 링크
``` html
<nav style="text-align: center">
    <ul class="pagination centered" style="display: inline-block"
        th:with="start=${T(Math).floor(clubList.number/10)*10 + 1},
        last=(${start + 9 < clubList.totalPages ? start + 9 : clubList.totalPages})">
        <li style="float: left" class="page-item">
            <a class="page-link" th:href="@{/clubs(page=1)}" aria-label="First">
                <span aria-hidden="true">First</span>
            </a>
        </li>
    </ul>
</nav>
```

#### 이전페이지
+ 해당 페이지가 첫페이지면 #을 그게아니라면 페이지의 번호를 보낸다.page의 index는 0부터 시작하기에 이전 페이지는 -1을 하지 않아도 된다
``` html
<nav style="text-align: center">
     <li style="float: left" th:class="${clubList.first} ? 'page-item'">
        <a class="page-link" th:href="${clubList.first} ? '#' :@{/clubs(page=${clubList.number})}"
           aria-label="Previous">
            <span aria-hidden="true">&lt;</span>
        </a>
     </li>
</nav>
```

#### 페이지 번호
+ numbers.sequence를 사용하여 위에서 지정한 start와 last로 시작값~끝값까지 반복을 하고 페이지
  와 clubList.number+1의 값이 같다면 'page-item'으로 만들고,
  페이지 번호를 보여주고 이동한다.
``` html
<nav style="text-align: center">
      <li style="float: left" th:each="page: ${#numbers.sequence(start, last)}"
          th:class="${page == clubList.number + 1} ? 'page-item'">
          <a class="page-link" th:text="${page}" th:href="@{/clubs?(page=${page})}"></a>
      </li>
</nav>
```

#### 다음페이지
+ 해당 페이지가 마지막페이지면 #을 그게 아니면 다음페이지의 번호를 보낸다 page의 index는 0부터 시작하기에 +2를 해준다.+1을 하면 다음페이지로 이동이 안되고 같은 페이지를 반복한다
``` html
<nav style="text-align: center">
    <li style="float: left" th:class="${clubList.last} ? 'page-item'">
        <a class="page-link" th:href="${clubList.last} ? '#' : @{/clubs(page=${clubList.number + 2})}"
           aria-label="Next">
            <span aria-hidden="true">&gt;</span>
        </a>
    </li>
</nav>
```
#### 마지막 페이지
+ clubList.totalPages 를 사용하여 전체 페이지수를 받아와 마지막 페이지로 이동한다.
``` html
<nav style="text-align: center">
  <li style="float: left" class="page-item">
    <a class="page-link" th:href="@{/clubs(page=${clubList.totalPages})}" aria-label="Last">
      <span aria-hidden="true">Last</span>
    </a>
  </li>
</nav>
```

#### 참고 :

#### https://cheese10yun.github.io/spring-jpa-best-12/

#### https://catchdream.tistory.com/181