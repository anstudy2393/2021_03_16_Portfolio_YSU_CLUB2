# Spring 공부

# Member CRUD 공부

## 회원 수정

+MemberRepository
````java
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
````
 + findOne 메서드 -> Long 타입 id(Member의 primary key)를 파라미터로 받아 엔티티매니저가 회원 아이디를 반환


+MemberService
````java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    
    @Transactional // 최신 회원수정
    public void updateMember(Long memberId, String name, String classNumber, String department, String location) {
        Address address = new Address(classNumber, department, location);
        Member findMember = memberRepository.findOne(memberId);
        findMember.setName(name);
        findMember.setAddress(address);
    }
    
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
````
 + updateMember 메서드 -> 값을 넣기위한 Address 객체를 생성 후 primary key를 찾아와 그 key에 맞는 name과 address를 넣음


+MemberController
````java
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList"; // members의 memberList.html를 반환
    }

    @GetMapping("members/{memberId}/edit")
    public String updateMemberForm(@PathVariable("memberId") Long memberId, Model model) {
        Member member = memberService.findOne(memberId);

        MemberForm form = new MemberForm();
        form.setId(member.getId());
        form.setName(member.getName());
        form.setClassNumber(member.getAddress().getClassNumber());
        form.setDepartment(member.getAddress().getDepartment());
        form.setLocation(member.getAddress().getLocation());

        model.addAttribute("form", form);
        return "members/updateMemberForm";
    }

    @PostMapping("members/{memberId}/edit")
    public String updateMember(@PathVariable Long memberId, @ModelAttribute("form") MemberForm form) {
        memberService.updateMember(memberId, form.getName(), form.getClassNumber(), form.getDepartment(), form.getLocation());
        return "redirect:/members";
    }
}
````
 +1. updateMemberForm 메서드 -> 수정페이지를 보여주는 메서드 ,@PathVariable 어노테이션으로 memberId 변수를 받아 주소 실행 
 +2. updateMember 메서드 -> memberService 의 updateMember 메서드를 가지고 수정작업을 담당함