# Spring 공부

# Member CRUD 공부

## 회원 삭제

+MemberRepository
````java
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void delete(Member member) { em.remove(member); }
    
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
}
````
 + delete 메서드 -> 엔티티매니저가 member를 삭제하는 로직

+MemberService
````java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;

    @Transactional // 최신 회원삭제
    public void deleteId(Long memberId) {
        Member findById = memberRepository.findOne(memberId);
        if (findById == null) { // 삭제 유효성 검사
            throw new IllegalStateException("이미 삭제된 회원입니다");
        }
        else {
            memberRepository.delete(findById);
        }
    }
    
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
````
 + deleteId 메서드 -> findOne으로 member의 primary key를 찾아서 유효성 검사 후 통과하면 repository의 delete 메서드 실행

+MemberController
````java
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping("/members/{memberId}")
    public String deleteMember(@PathVariable Long memberId) {
        memberService.deleteId(memberId);
        return "redirect:/members";
    }
}
````
 + memberService의 deleteId를 가지고 삭제 작업을 담당함
 + @DeleteMapping은 @PostMapping과 내부적으로 차이는 없지만 xhrrequest의 요청방식(메소드)가 다름 