package kr.ac.yeonsung.demo.controller;

import kr.ac.yeonsung.demo.domain.Address;
import kr.ac.yeonsung.demo.domain.Member;
import kr.ac.yeonsung.demo.form.MemberForm;
import kr.ac.yeonsung.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

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
        return "redirect:/members"; // 첫 번째 페이지로 넘어가게함
    }

//    @GetMapping("/members")
//    public String list(Model model) {
//        List<Member> members = memberService.findMembers();
//        model.addAttribute("members", members);
//        return "members/memberList"; // members의 memberList.html를 반환
//    }


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

    @DeleteMapping("/members/{memberId}")
    public String deleteMember(@PathVariable Long memberId) {
        memberService.deleteId(memberId);
        return "redirect:/members";
    }

    @GetMapping("/members")
    public String list(@PageableDefault Pageable pageable, Model model) {
        Page<Member> memberList = memberService.findAll(pageable);
        model.addAttribute("memberList", memberList);

        List<Member> getMemberList = memberList.getContent();
        model.addAttribute("getMemberList", getMemberList);
        return "members/memberList";
    }

}
