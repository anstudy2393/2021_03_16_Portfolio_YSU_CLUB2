# 게시글 번호 가져오기

### Entity
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
### GetId(Interface)
```java
public interface GetId {
    Long getId();
}
```
### BoardRepository
```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    //List<Board> findByTitle(String title);
    List<GetId> findByTitle(String title);
}
```
+ title과 같은 데이터를 찾는다. GetId 인터페이스에는 getId()가 있어 id값만 가지고 온다
+ 실제 쿼리   
![image](https://user-images.githubusercontent.com/76415175/118257301-6f24e280-b4e9-11eb-9357-e4b7e39dc9ea.png)

### BoardService
```java
@Service
@Slf4j
public class BoardService {
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }
    public List<GetId> findBoardTitle(String title){
        List<GetId> getId = boardRepository.findByTitle(title);
        return getId;
    }
    /*
    public List<Long> findBoardTitles(String title){
        List<Board> findBoardTitle = boardRepository.findByTitle(title);
        List<Long> getTitle = new ArrayList<>();
        for(int i=0;i<=findBoardTitle.size()-1;i++){
            getTitle.add(findBoardTitle.get(i).getId());
        }
        return getTitle;
    }*/
}
```
+ boardRepository에있는 findByTitle실행해 값을 getId에 저장한후 getId를 return한다
### BoardServiceTest
```java
@SpringBootTest
class BoardServiceTest {
    @Autowired BoardService boardService;

    @Test
    public void 번호_가져오기v1(){
        List<GetId> findTitle = boardService.findBoardTitle("title");
        System.out.println("============findTitleNumber============");
        findTitle.forEach(getId -> System.out.println(getId.getId()));
    }
    /*
    @Test
    public void 번호_가져오기v2(){
        List<Long> findTitle = boardService.findBoardTitles("testTitle");
        System.out.println("============findTitleNumber============");
        System.out.println(findTitle);
    }
    */
}
```
+ title의 내용이 "title"인 게시글의 번호를 가지고 온다
+ 출력결과   
![image](https://user-images.githubusercontent.com/76415175/118257602-c925a800-b4e9-11eb-8c6a-096d71d7a5b6.png)
+ 위코드에서 주석처리된부분들도 id를 가져오는 같은 코드이나 주석된 코드는 쿼리를 날릴때 id하나가아닌 테이블에 있는 모든 Column을 가져오기때문에 List를 하나 선언해서 쿼리를 날리고 return 받은 데이터를 처음부터 끝까지 돌려서 id값만 따로 저장을해 출력하는 방식이다

