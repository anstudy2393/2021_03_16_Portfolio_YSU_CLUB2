package kr.ac.yeonsung.demo.controller;


import kr.ac.yeonsung.demo.domain.Member;
import kr.ac.yeonsung.demo.domain.club.Book;
import kr.ac.yeonsung.demo.domain.club.Club;
import kr.ac.yeonsung.demo.form.ClubForm;
import kr.ac.yeonsung.demo.service.ClubService;
import kr.ac.yeonsung.demo.service.JoinClubService;
import kr.ac.yeonsung.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j//log확인용
public class ClubController {
    private final ClubService clubService;
    private final JoinClubService joinClubService;
    private final MemberService memberService;
    private Object Null;

    //=====동아리 생성=====//
    @GetMapping("/clubs/new")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();//전체 회원을 담은 members List
        try {
            members.get(0).getId();//members의 index 0번의 id를 가지고온다. null이면 catch문에서 clubListError페이지로 이동한다
        }catch (Exception e) {
            return "clubs/clubListError";
        }
        model.addAttribute("clubForm", new ClubForm());//ClubForm을 clubForm이라는 이름으로 model 객체로 넘긴다
        model.addAttribute("members",members);// 회원 목록을 members라는 model객체로 넘긴다
        return "clubs/createClubForm";
    }
    //=====동아리 생성, 입력값 저장=====//
    @PostMapping("/clubs/new")
    public String create(@Valid ClubForm bookForm, BindingResult result, @RequestParam("memberId") Long memberId, Model model) {//BindingResult form에서 오류가 있을시 오류가 result에 담김
        if(result.hasErrors()){//입력한 값중 에러가 있다면 동아리를 생성하지 않고 동아리 생성 페이지로 돌려준다. 전체 회원의 정보를 담은 model객체를 다시 생성해 넘긴다
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
            clubService.saveClub(book);//저장에 오류가 있다면 catch문을 실행한다
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

    // 페이징
    @GetMapping("/clubs")
    public String list(@PageableDefault Pageable pageable, Model model){
        Page<Club> clubList = clubService.findAll(pageable);
        model.addAttribute("clubList", clubList);

        List<Club> getClubList = clubList.getContent();
        model.addAttribute("getClubList",getClubList);//list size가져옴, list size확인용
        return "/clubs/clubList";
    }

    //=====동아리 삭제=====//
    @PostMapping("clubs/{clubId}/delete")
    public String deleteClub(@PathVariable("clubId") Long clubId){
        Club club = clubService.findOne(clubId);//clubId로 동아리를 찾아 저장
        joinClubService.chageStatus(club);//동아리 신청상태 변경
        clubService.deleteClub(clubId);//동아리 삭제
        return "redirect:/clubs";
    }

    //=====동아리 정보 수정, 동아리 정보 가저오기=====//
    @GetMapping("/clubs/{clubId}/change")
    public String changeForm(Model model,@PathVariable("clubId") Long clubId){
        List<Member> members = memberService.findMembers();//전체 회원을 찾고 저장

        Club club = clubService.findOne(clubId);//clubId로 수정할 동아리 정보를 가지고온다
        model.addAttribute("clubForm",club);//clubForm으로 동아리 정보를 model객체로 넘긴다
        model.addAttribute("members",members);//members로 회원 목록을 model객체로 넘긴다
        return "clubs/clubInfoChange";
    }

    //=====동아리 정보 수정=====//
    @PostMapping("/clubs/{clubId}/change")
    public String change(@Valid ClubForm clubForm, BindingResult result, @PathVariable("clubId") Long clubId, @RequestParam("memberId") Long memberId, Model model){
        if(result.hasErrors()){//입력 정보에 에러가 있으면 실행
            List<Member> members = memberService.findMembers();//전체 멤버를 List에 담고 model객체로 저장
            model.addAttribute("members",members);
            return "clubs/clubInfoChange";//동아리 정보 수정 페이지로 이동
        }

       try {
           Member member = memberService.findOne(memberId);//선택한 회원의 id로 회원 정보를 찾아 저장
           clubService.updateClub(clubId, clubForm.getName(), clubForm.getTotalNumber(),
                   member.getName());// 동아리 정보를 update
       }catch (IllegalStateException e){
           String message = e.getMessage();//동아리 정보를 수정하는 도중 exception이 떴다면 에러 메시지를 message에담고 log로 보여준다
           log.info(message);
           model.addAttribute("message",message);
           List<Member> members = memberService.findMembers();
           model.addAttribute("members",members);

           return "clubs/clubInfoChange";
       }
        return "redirect:/clubs";//수정이 완료되면 동아리 목록으로 리다이렉트한다
    }

}
