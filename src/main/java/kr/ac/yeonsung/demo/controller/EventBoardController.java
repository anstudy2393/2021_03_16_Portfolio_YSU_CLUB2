package kr.ac.yeonsung.demo.controller;

import kr.ac.yeonsung.demo.domain.EventBoard;
import kr.ac.yeonsung.demo.form.EventBoardForm;
import kr.ac.yeonsung.demo.service.EventBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class EventBoardController {

    private final EventBoardService eventBoardService;

    @GetMapping("/eventBoard/write")
    public String writeForm(Model model) {
        model.addAttribute("eventBoardForm", new EventBoardForm());
        return "eventBoard/eventWrite";
    }

    @PostMapping("/eventBoard/write")
    public String write(@Valid EventBoardForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "eventBoard/eventWrite";
        }
        eventBoardService.write(form);
        return "redirect:/eventBoard/list";
    }

    @GetMapping("/eventBoard/view/{eventId}")
    public String eventView(@PathVariable("eventId") Long eventId, Model model) {
        EventBoard eventBoard = eventBoardService.findOne(eventId);
        model.addAttribute("eventId", eventBoard);
        return "eventBoard/eventView";
    }

    @GetMapping("/eventBoard/updateEdit/{eventId}")
    public String update(@PathVariable("eventId") Long eventId, Model model) {
        EventBoard findEvent = eventBoardService.findOne(eventId);
        model.addAttribute("eventBoardForm", findEvent);
        return "eventBoard/eventUpdate";
    }

    @PostMapping("/eventBoard/updateEdit/{eventId}")
    public String update(@Valid EventBoardForm form, BindingResult result, @PathVariable("eventId") Long id) {
        if (result.hasErrors()) {
            return "eventBoard/eventUpdate";
        }
        eventBoardService.update(id, form);
        return "redirect:/eventBoard/list";
    }

    @DeleteMapping("/eventBoard/cancel/{eventId}")
    public String cancel(@PathVariable("eventId") Long eventId) {
        eventBoardService.delete(eventId);
        return "redirect:/eventBoard/list";
    }


    @GetMapping("/eventBoard/list")
    public String list(@PageableDefault Pageable pageable, Model model) {
        Page<EventBoard> eventBoardList = eventBoardService.findAll(pageable);
        model.addAttribute("eventList", eventBoardList);

        List<EventBoard> getEventList = eventBoardList.getContent();
        model.addAttribute("getEventList", getEventList);
        return "eventBoard/eventList";
    }
}
