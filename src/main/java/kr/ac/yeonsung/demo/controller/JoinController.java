package kr.ac.yeonsung.demo.controller;


import kr.ac.yeonsung.demo.domain.Join;
import kr.ac.yeonsung.demo.domain.JoinClub;
import kr.ac.yeonsung.demo.domain.Member;
import kr.ac.yeonsung.demo.domain.club.Club;
import kr.ac.yeonsung.demo.repository.JoinClubRepository;
import kr.ac.yeonsung.demo.service.ClubService;
import kr.ac.yeonsung.demo.service.JoinClubService;
import kr.ac.yeonsung.demo.service.JoinService;
import kr.ac.yeonsung.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final JoinClubService joinClubService;
    private final JoinService joinService;
    private final MemberService memberService;
    private final ClubService clubService;

    //=====동아리 신청정보 담기=====//
    @GetMapping("/join")
    public String createForm(Model model){
        //전체 회원과 전체 동아리의 정보를 각각 담는 List선언
        List<Member> members = memberService.findMembers();
        List<Club> clubs = clubService.findClub();

        //회원과 동아리의 정보를 담고있는 변수를 model객체로 넘긴다
        model.addAttribute("members",members);
        model.addAttribute("clubs",clubs);
        return "join/joinForm";
    }
    //=====동아리 신청=====//
    @PostMapping("/join")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("clubId") Long clubId,
                        @RequestParam("count") int count){
        joinService.Join(memberId,clubId,count);//동아리 신청

        return "redirect:/joins";
    }

    // 페이징
    @GetMapping("/joins")
    public String list(@PageableDefault Pageable pageable, Model model){
        Page<JoinClub> joinClubList = joinClubService.findAll(pageable);
        model.addAttribute("joinClubList", joinClubList);

        List<JoinClub> getJoinClubList = joinClubList.getContent();
        model.addAttribute("getJoinClubList",getJoinClubList);//list size가져옴, list size확인용
        return "/join/joinList";
    }

    //=====동아리 탈퇴=====//
    @PostMapping("joins/{joinId}/cancel")
    public String cancelOrder(@PathVariable("joinId") Long joinId){
        joinService.cancelClub(joinId);// joinId로 동아리를 탈퇴한다
        return "redirect:/joins";
    }

}
