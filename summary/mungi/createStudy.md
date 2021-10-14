# Spring 공부

# Member CRUD 공부

## 회원 생성

+ MemberRepository.java
````java
@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManger em;
    
    public void save(Member member) {
        em.persist(member);
    }
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name ", Member.class )
                .setParameter("name", name)
                .getResultList();
    }
}
````
 +1. @Repository -> Repository 어노테이션을 써 자동으로 스프링 빈으로 관리되게함
 +2. @RequiredArgsConstructor -> final에 있는 필드만을 가지고 생성자를 만들어주는 어노테이션
 +3. save 메서드 -> 엔티티매니저가 member를 저장하는 로직

+ MemberService.java
````java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
````
 +1. @Service -> service 로직을 수행할 클래스에 사용하는 어노테이션
 +2. @Transactional(readOnly = true) -> 데이터 변경이 없는 읽기 전용 메서드에만 사용, 따로 @Transactional을 달아주지 않는 메서드들은 전부 적용
 +3. join(회원 가입) 메서드 -> 중복회원 검증 후 문제가 없으면 회원을 저장하고 회원 id를 반환 
 +4. validateDuplicateMember(중복회원 검증) 메서드 -> 회원리스트에서 회원명을 찾아 비어있지 않으면 IllegalStateException을 발생시킴


+ MemberController.java
````java
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm"; // members의 createMemberForm.html을 반환
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) { // BindingResult로 에러를 받아서 member/createMember로 다시 반환
            return "members/createMemberForm";
        }

        Address address = new Address(form.getClassNumber(), form.getDepartment(), form.getLocation());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress((address));

        memberService.join(member);
        return "redirect:/"; // 첫 번째 페이지로 넘어가게함
    }
}
````
 +1. @GetMapping -> GET의 HTTP(S) request를 처리
 +2. @PostMapping -> POST의 HTTP(S) request를 처리
 +3. model.addAttribute("memberForm", new MemberForm()); -> 컨트롤러에서 뷰로 넘어갈 때 memberForm 데이터를 실어서 넘김
 +4. @Valid MemberForm -> MemberForm 안에 있는 어노테이션과 상호작용 (MemberForm의 @NotEmpty), MemberForm의 name의 데이터가 넘어오지 않으면 오류 발생

