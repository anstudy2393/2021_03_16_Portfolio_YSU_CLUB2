package kr.ac.yeonsung.demo.service;

import kr.ac.yeonsung.demo.form.EventBoardForm;
import kr.ac.yeonsung.demo.domain.EventBoard;
import kr.ac.yeonsung.demo.repository.EventBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventBoardService {

    private final EventBoardRepository eventBoardRepository;
    
    // 이벤트 게시판 생성
    @Transactional
    public Long write(EventBoardForm form) {
        EventBoard eventBoard = new EventBoard();
        eventBoard.setTitle(form.getTitle());
        eventBoard.setContent(form.getContent());
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        eventBoard.setWriteDate(dateTime);
        eventBoardRepository.save(eventBoard);
        return eventBoard.getId();
    }

    public EventBoard findOne(Long eventId) {
        return eventBoardRepository.findById(eventId).orElse(null);
    }

    // 이벤트 게시글 수정
    @Transactional
    public void update(Long eventId, EventBoardForm form) {
        EventBoard updateEvent = findOne(eventId);
        updateEvent.setTitle(form.getTitle());
        updateEvent.setContent(form.getContent());
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        updateEvent.setUpdateDate(dateTime);
        eventBoardRepository.save(updateEvent);
    }

    // 이벤트 게시글 삭제
    @Transactional
    public void delete(Long eventId) {
        EventBoard event = eventBoardRepository.findById(eventId).orElse(null);
        eventBoardRepository.delete(event);
    }

    public Page<EventBoard> findAll(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");
        return eventBoardRepository.findAll(pageable);
    }
}
