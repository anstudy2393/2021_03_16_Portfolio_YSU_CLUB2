package kr.ac.yeonsung.demo.controller;

import kr.ac.yeonsung.demo.domain.NoticeBoard;
import kr.ac.yeonsung.demo.form.NoticeBoardForm;
import kr.ac.yeonsung.demo.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    @GetMapping("/noticeBoard/write")
    public String writeForm(Model model){
        model.addAttribute("noticeBoardForm",new NoticeBoardForm());
        return "noticeBoard/noticeWrite";
    }
    //게시글 작성
    @PostMapping("/noticeBoard/write")
    public String write(@Valid NoticeBoardForm form, BindingResult result) {//BindingResult form에서 오류가 있을시 오류가 result에 담김
        // result에 에러가 있으면 noticeBoard/noticeWrite로 다시 반환
        if (result.hasErrors()) {
            return "noticeBoard/noticeWrite";
        }
        noticeBoardService.write(form);
        return "redirect:/noticeBoard/list"; // 첫 번째 페이지로 넘어가게함
    }

    //게시글 보기
    @GetMapping("/noticeBoard/view/{noticeId}")
    public String noticeView(@PathVariable("noticeId") Long noticeId,Model model){
        NoticeBoard noticeBoard = noticeBoardService.findOne(noticeId);
        model.addAttribute("noticeId",noticeBoard);
        return "noticeBoard/noticeView";
    }

    //게시글 삭제
    @GetMapping("/noticeBoard/cancel/{noticeId}")//게시글 번호를 가지고옴
    public String cancel(@PathVariable("noticeId") Long noticeId){
        noticeBoardService.delete(noticeId);
        return "redirect:/noticeBoard/list";
    }

    //게시글 수정
    //=====해당 게시글 title, content가져오기=====//
    @GetMapping("/noticeBoard/update/{noticeId}")
    public String update(@PathVariable("noticeId") Long noticeId, Model model){
        NoticeBoard findNotice = noticeBoardService.findOne(noticeId);
        model.addAttribute("noticeBoardForm",findNotice);
        return "noticeBoard/noticeUpdate";
    }
    @PostMapping("/noticeBoard/update/{noticeId}")
    public String update(@Valid NoticeBoardForm form, BindingResult result,@PathVariable("noticeId") Long id) {//BindingResult form에서 오류가 있을시 오류가 result에 담김
        // result에 에러가 있으면 noticeBoard/noticeWrite로 다시 반환
        if (result.hasErrors()) {
            return "noticeBoard/noticeUpdate";
        }
        noticeBoardService.update(id,form);//새로 저장한값을 저장시킴
        return "redirect:/noticeBoard/list"; // 게시글 목록으로 이동
    }

    //전체 게시글 가지고오기
    @GetMapping("/noticeBoard/list")
    public String getNoticeBoardList(@PageableDefault Pageable pageable, Model model){
        Page<NoticeBoard> noticeBoardList = noticeBoardService.findAll(pageable);
        model.addAttribute("noticeList", noticeBoardList);

        List<NoticeBoard> getBoardList = noticeBoardList.getContent();
        model.addAttribute("getBoardList",getBoardList);//list size가져옴, list size확인용
        return "noticeBoard/noticeList";
    }
    //메인 index 최신 공지사항 3개 가져오기
    @GetMapping("/")
    public String getNoticeBoardListTop3(Model model){
        List<NoticeBoard> getTop = noticeBoardService.findTop3Board();
        model.addAttribute("findTop3",getTop);
        return "index";
    }

}
