package kr.ac.yeonsung.demo.service;

import kr.ac.yeonsung.demo.domain.Exception.NotJoinException;
import kr.ac.yeonsung.demo.domain.Join;
import kr.ac.yeonsung.demo.domain.JoinStatus;
import kr.ac.yeonsung.demo.domain.Member;
import kr.ac.yeonsung.demo.domain.club.Book;
import kr.ac.yeonsung.demo.domain.club.Club;
import kr.ac.yeonsung.demo.repository.JoinRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JoinServiceTest {

    @PersistenceContext EntityManager em;
    @Autowired
    JoinRepository joinRepository;
    @Autowired JoinService joinService;

    @Test
    public void 동아리_가입() throws Exception{
        //given
        Member member = createMember();
        Club club = createBook("테스트1",10);
        int joinCount = 1;

        System.out.println("MemberId : " + member.getId() + " ClubId : " + club.getId() + " 추가되는 인원수 : " + joinCount);//값은 들어가는데 NullPointException이뜬다
        System.out.println("TestMemberName : " + member.getName()+" TestClubName : "+club.getName()+" TestCount : "+club.getTotalNumber());

        //when
        Long joinId = joinService.Join(member.getId(),club.getId(),joinCount);

        //then
        Join getJoin = joinRepository.findById(joinId).orElse(null);

        assertEquals("상태확인", JoinStatus.apply,getJoin.getStatus());
        assertEquals("빠진 멤버수 확인",9,club.getTotalNumber());//10-1
        assertEquals("들어간 동아리 확인",member.getJoins().get(0).getJoinClubs().get(0).getClub().getName(),club.getName());
    }

    @Test
    public void 동아리_탈퇴(){
        //given
        Member member = createMember();
        Club club = createBook("테스트1",10);
        int joinCount = 1;
        Long joinId = joinService.Join(member.getId(),club.getId(),joinCount);

        //when

        joinService.cancelClub(joinId);


        //then
        Join getjoin = joinRepository.findById(joinId).orElse(null);
        assertEquals("동아리 탈퇴 확인",JoinStatus.cancel,getjoin.getStatus());//탈퇴후 Join의 status가 cancel인지 확인
        assertEquals("Member 동아리 탈퇴 확인",JoinStatus.cancel,member.getStatus());//탈퇴후 Member의 status가 cancel인지 확인
        assertEquals("동아리 총인원 증가 확인",10,club.getTotalNumber());//탈퇴후 Club totalNumber 증가 확인
    }

    @Test(expected = NotJoinException.class)//예외설정
    public void 동아리_회원_초과(){
        //given
        Member member = createMember();
        Club club = createBook("테스트1",10);
        int joinCount = 100;

        //when
        joinService.Join(member.getId(), club.getId(), joinCount);//가입

        //then
        fail("인원초과 에러 예외발생해야함");
    }


    //동아리생성 메소드
    private Book createBook(String name,int totalCount) {
        Book book = new Book();
        book.setName(name);
        book.setTotalNumber(totalCount);
        em.persist(book);
        return book;
    }

    //Member생성 메소드
    private Member createMember() {
        Member member = new Member();
        member.setName("회원테스트1");
        em.persist(member);
        return member;
    }
}