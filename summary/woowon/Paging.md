# Paging / JPA_SpringBoot_Thymeleaf

### JpaRepository
https://github.com/Leewoowon980522/YSU_CLUB_Test/blob/master/sumarry/JpaRepository.md
### Paging
+ 게시글 등을 가져올떄 가지고올 게시물이 많으면 서버로 부터 가지고올 데이터의 양이 많아지는데 그에 따라 로딩시간이 길어지기 때문에 필요한 데이터의 수를 나누어 화면에 보여주는 것
+ JpaRepository를 상속받는 repository인터페이스에 있는 findAll 메소드에 Pageable구현체를 넣어주면된다

### Entity설계
```java
@Entity
@Getter @Setter
public class Board {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String title;
    private String content;
}
```
### BoardRepository
```java
public interface BoardRepository extends JpaRepository<Board, Long> {

}
```
+ JpaRepository를 상속받는 인터페이스로 JpaRepository에서 제공하는 기능을 사용

### BoardService
```java
@Service
public class BoardService {
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public Page<Board> getBoardList(Pageable pageable){
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber()-1);
        pageable = PageRequest.of(page,5, Sort.Direction.DESC,"id");
        return boardRepository.findAll(pageable);
    }
}
```
+ pageable.getPageNumber()는 현재 페이지를 알려주며 첫페이지이면 0을 그게 아니라면 현재 페이지에 1을 뺀다
+ Pageable의 page는 index가 0부터 시작
+ PageRequest.of는 페이지번호,페이지당 데이터수,정렬방식,기준으로 되어있다.
+ pageable에 값을 준후 repository에 findAll(pageable)을 주면 해당 조건에 맞는 값이 들어가게된다
### BoardController
```java
@Controller
@Slf4j
public class BoardController {
    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/boards")
    public String boardView(@PageableDefault Pageable pageable, Model model) {

        Page<Board> boardList = boardService.getBoardList(pageable);
        model.addAttribute("boardList", boardList);

        List<Board> getBoardList = boardList.getContent();
        model.addAttribute("getBoardList",getBoardList);//list size가져옴, list size확인용

        return "board";
    }
}
```
+ view에서 해당 페이지 번호를 받아 service에getBoardList에서 페이징 처리하고 return 받은 값을 boardList로 받아 model을 이용해 view에 전달한다
+ getBoardList는 boardList.getContent()를 이용해 return받은 값이 갯수를 받는다. 게시글이 있는지 없는지 확인하는 용도
### board.html
![전체코드](https://user-images.githubusercontent.com/76415175/118247763-85797100-b4de-11eb-8a2c-310f30f34396.PNG)
+ 8행과 52행(게시글 존재 여부 확인)
    + controller에서 model로 받은 getBoardList의 size를 확인해 게시글이 있으면 8행을 실행 없으면 52행을 실행한다
    + thymeleaf에서 unless는 if문에 else구문이라 생각하면된다
    + 데이터가 있으면 9행에서 해당 페이지에나오는 게시글의 수를 알수있다
+ 10~21행(게시글)
    + 게시글을 출력하는 table로 16행에서 controller에서 받은 boardList를 반복문으로 돌려 출력한다
+ 22~50행(게시글 이동 버튼)
    + 23~25행(시작페이지,마지막페이지값 변수 지정)
        + th:with는 변수의 값을 정하는것으로 start는 시작페이지를 last는 마지막 페이지를 변수로 저장한다
    + 26~30행(처음페이지 이동,1페이지)
        + 처음 페이지 즉 1페이지를 나타내며 클릭시 1페이지로 이동하는 링크
    + 44~48행(마지막페이지 이동, 전체페이지의 마지막)
        + totalPages를 사용하여 전체 페이지의 수를 받아와 마지막페이지로 이동하는 링크
    + 31~35(이전페이지 이동)
        + 31행은 해당 페이지가 처음 페이지면 li태그를 disabled상태로 만든다
        + 32행은 해당 페이지가 첫페이지면 #을 그게아니라면 페이지의 번호를 보낸다.page의 index는 0부터 시작하기에 이전 페이지는 -1을 하지 않아도 된다
    + 36~38(페이지 번호)
        + 36행은 numbers.sequence를 사용하여 위에서 지정한 start와 last로 시작값~끝값까지 반복을 하고 페이와 boardList.number+1의 값이 같다면 active상태로 만든다
        + 37행은 페이지번호를 보여주고 이동하는 역할
    + 39~43(다음페이지 이동)
        + 39행은 이전페이지와 같이 마지막 페이지면 li태그를 disabled상태로 만든다
        + 40행은 해당 페이지가 마지막페이지면 #을 그게 아니면 다음페이지의 번호를 보낸다 page의 index는 0부터 시작하기에 +2를 해준다.+1을 하면 다음페이지로 이동이 안되고 같은 페이지를 반복한다
___
___
### 특정 조건 데이터 가져오기
### BoardRepository
```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findTop3ByOrderByIdDesc();//데이터3개만 가져오기
}
```
+ 쿼리 메소드로 게시글중 최신 게시글 3개를 가져오는 메소드를 만든다
### BoardService
```java
@Service
public class BoardService {
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }
    public List<Board> findTop3Board(){
        List<Board> getTop = boardRepository.findTop3ByOrderByIdDesc();
        return getTop;
    }

}
```
+ repository에서 만든 쿼리 메소드를 사용하여 getTop에 값을 저장후 getTop를 return한다
### BoardController
```java
@Controller
public class BoardController {
    private BoardService boardService;

    public BoardController(BoardService boardService) {this.boardService = boardService;}

    @GetMapping("/boards")
    public String boardView(@PageableDefault Pageable pageable, Model model) {
        List<Board> getTop = boardService.findTop3Board();
        model.addAttribute("findTop3",getTop);
        return "board";
    }
}
```
+ getTop에 최신 게시글을 저장후 model에 findTop3으로 이름을 주고 view에 뿌린다
### board.html
![데이터 3개](https://user-images.githubusercontent.com/76415175/118254584-36373e80-b4e6-11eb-94c7-dd94c08185ad.PNG)
+ thymeleaf의 반복문을 사용해 findTop3의 값을 반복하여 출력한다
+ 출력결과   
![출력결과](https://user-images.githubusercontent.com/76415175/118255610-6d5a1f80-b4e7-11eb-8799-8f8a6dbdc720.PNG)
+ 전체 데이터수  
![전체 게시글](https://user-images.githubusercontent.com/76415175/118255731-8f53a200-b4e7-11eb-90c3-3cb285641b32.PNG)
+ id를 기준으로 최신 게시글 3개가 나오는걸 알수 있다