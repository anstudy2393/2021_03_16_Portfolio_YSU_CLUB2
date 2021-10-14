package kr.ac.yeonsung.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.ac.yeonsung.demo.form.NoticeBoardForm;
import kr.ac.yeonsung.demo.domain.NoticeBoard;
import kr.ac.yeonsung.demo.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeBoardService {
    private final NoticeBoardRepository noticeBoardRepository;

    //게시글작성
    @Transactional
    public Long write(NoticeBoardForm form){
        NoticeBoard noticeBoard = new NoticeBoard();
        noticeBoard.setTitle(form.getTitle());
        noticeBoard.setContent(form.getContent());
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        noticeBoard.setWriteDate(dateTime);
        noticeBoardRepository.save(noticeBoard);
        return noticeBoard.getId();
    }

    //게시글 단건 조회
    public NoticeBoard findOne(Long boardId){
        return noticeBoardRepository.findById(boardId).orElse(null);

    }
   
    //게시글 삭제
    @Transactional
    public void delete(Long noticeId){
        NoticeBoard notice = noticeBoardRepository.findById(noticeId).orElse(null);//게시글의 id값으로 해당 게시물을 가지고옴
        noticeBoardRepository.delete(notice);
    }

    //게시글 수정
    @Transactional
    public void update(Long id, NoticeBoardForm form){
        NoticeBoard updateNotice = findOne(id);//id로 이전 작성한 게시물을 가져옴
        updateNotice.setTitle(form.getTitle());//값을 update
        updateNotice.setContent(form.getContent());
        //수정한 날짜를 저장
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        updateNotice.setUpdateDate(dateTime);
        noticeBoardRepository.save(updateNotice);
    }

    //게시글 목록 페이징처리
    public Page<NoticeBoard> findAll(Pageable pageable){
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.Direction.DESC,"id");
        return noticeBoardRepository.findAll(pageable);
    }
    //index.html 최신 공지사항 3개 가져오기
    public List<NoticeBoard> findTop3Board(){
        List<NoticeBoard> getTop = noticeBoardRepository.findTop3ByOrderByIdDesc();
        return getTop;
    }
}
