package kr.ac.yeonsung.demo.service;

import kr.ac.yeonsung.demo.form.NoticeBoardForm;
import kr.ac.yeonsung.demo.domain.NoticeBoard;
import kr.ac.yeonsung.demo.repository.NoticeBoardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.regex.Pattern;

import static org.junit.Assert.*;


@SpringBootTest
@Transactional
public class NoticeBoardServiceTest {

    @Autowired
    private NoticeBoardRepository noticeBoardRepository;
    @Autowired
    private NoticeBoardService noticeBoardService;
    @PersistenceContext
    private EntityManager em;

    @AfterEach//테스트 메소드 실행후 호출
    public void reset(){
        noticeBoardRepository.deleteAll();//전체 data 삭제
        this.em.createNativeQuery("ALTER TABLE notice_board ALTER COLUMN `board_Notice_id` RESTART WITH 41").executeUpdate();//시작값을 41로 초기화
    }

    //=====게시글 생성 메서드=====//
    private NoticeBoardForm createBoard(){
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("TestCode제목");
        noticeBoard.setContent("TestCode내용");
        noticeBoardService.write(noticeBoard);
        return noticeBoard;
    }

    //==게시글 작성,수정,삭제==//
    @Test
    public void 게시글작성_성공() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = createBoard();

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부확인
        assertEquals(noticeBoard.getContent(),getNotice.getContent());//값 등록여부확인
    }

    @Test
    public void 게시글수정_성공() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = createBoard();

        //값 변경
        NoticeBoard getNotice = noticeBoardService.findOne(41L);
        NoticeBoardForm noticeForm = new NoticeBoardForm();
        noticeForm.setTitle("변경");
        noticeForm.setContent("변경2");

        noticeBoardService.update(getNotice.getId(), noticeForm);

        //변경된 값 가져오기
        NoticeBoard changeNotice = noticeBoardService.findOne(getNotice.getId());

        //값 변경 확인
        assertEquals(changeNotice.getTitle(), "변경");
        assertEquals(changeNotice.getContent(), "변경2");
    }

    @Test
    public void 게시글삭제_성공() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = createBoard();

        //값 삭제
        noticeBoardService.delete(41L);

        //값 찾기
        NoticeBoard notice = noticeBoardService.findOne(41L);
        assertNull(notice);//Null인지 확인,삭제확인
    }

    //==게시글작성,수정 입력조건Test==//
    //==공통조건 : NotNull,공백만 있는거X==//
    @Test
    public void 게시글제목Null() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setContent("TestCode내용");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertNull(getNotice.getTitle());//Null값 확인
    }
    @Test
    public void 게시글제목NotNull() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("TestCode제목");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertNotNull(getNotice.getTitle());//NotNull값 확인
    }

    @Test
    public void 게시글제목공백() throws Exception {
        //정규식
        String pattern = "^[\\s]*$"; //공백만체크

        //값저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("    ");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertTrue(Pattern.matches(pattern,getNotice.getTitle()));//title이 공백만있다면 True
    }
    @Test
    public void 게시글제목공백_문자() throws Exception {
        //정규식
        String pattern = "^[\\s]*$"; //공백만체크

        //값저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("    1234");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertFalse(Pattern.matches(pattern,getNotice.getTitle()));//title이 공백만있는게 아니라면 True
    }

    @Test
    public void 게시글제목길이50이하() throws Exception {
        //길이는 최대 50글자
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("TestCode제목");
        noticeBoard.setContent("TestCode내용");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);
        int titlel = getNotice.getTitle().length();//길이 저장

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertTrue(titlel<=50);//제목의 크기가 50보다 작으면 True
    }
    @Test
    public void 게시글제목길이50초과() throws Exception {
        //길이는 최대 50글자
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setTitle("TestCode제목TestCode제목TestCode제목TestCode제목TestCode제목");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);
        int titlel = getNotice.getTitle().length();//길이 저장

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertFalse(titlel<50);//제목의 크기가 50보다 크면 True
    }

    @Test
    public void 게시글내용Null() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertNull(getNotice.getContent());//Null값 확인
    }
    @Test
    public void 게시글내용NotNull() throws Exception {
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setContent("TestCode내용");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertEquals(noticeBoard.getTitle(),getNotice.getTitle());//값 등록여부 확인
        assertNotNull(getNotice.getContent());//NotNull값 확인
    }

    @Test
    public void 게시글내용공백() throws Exception {
        //정규식
        String pattern = "^[\\s]*$"; //공백만체크

        //값저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setContent("    ");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertTrue(Pattern.matches(pattern,getNotice.getContent()));//title이 공백만있다면 True
    }
    @Test
    public void 게시글내용공백_문자() throws Exception {
        //정규식
        String pattern = "^[\\s]*$"; //공백만체크

        //값저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setContent("    TestCode내용");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);

        assertFalse(Pattern.matches(pattern,getNotice.getContent()));//title이 공백만있는게 아니라면 True
    }

    @Test
    public void 게시글내용길이2000이하() throws Exception {
        //길이는 최대 2000글자
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setContent("Test내용입니다!");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);
        int contentl = getNotice.getContent().length();

        assertEquals(noticeBoard.getContent(),getNotice.getContent());//값 등록여부 확인
        assertTrue(contentl<=2000);//내용의 크기가 2000보다 작으면 True
    }
    @Test
    public void 게시글내용길이2000초과() throws Exception {
        //길이는 최대 2000글자
        //값 저장
        NoticeBoardForm noticeBoard = new NoticeBoardForm();
        noticeBoard.setContent("Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!Test내용입니다!");
        noticeBoardService.write(noticeBoard);

        //값 찾기
        NoticeBoard getNotice = noticeBoardService.findOne(41L);
        int contentl = getNotice.getContent().length();

        assertEquals(noticeBoard.getContent(),getNotice.getContent());//값 등록여부 확인
        assertFalse(contentl<2000);//내용의 크기가 2000보다 크면 True
    }
}