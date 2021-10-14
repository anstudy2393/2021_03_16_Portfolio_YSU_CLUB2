package kr.ac.yeonsung.demo.service;

import kr.ac.yeonsung.demo.domain.Join;
import kr.ac.yeonsung.demo.domain.JoinStatus;
import kr.ac.yeonsung.demo.domain.Member;
import kr.ac.yeonsung.demo.domain.club.Club;
import kr.ac.yeonsung.demo.repository.ClubRepository;
import kr.ac.yeonsung.demo.repository.JoinClubRepository;
import kr.ac.yeonsung.demo.repository.JoinRepository;
import kr.ac.yeonsung.demo.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.regex.Pattern;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ClubServiceTest {

    @Autowired ClubService clubService;
    @Autowired JoinClubService joinClubService;
    @Autowired JoinService joinService;
    @Autowired ClubRepository clubRepository;
    @Autowired JoinClubRepository joinClubRepository;
    @PersistenceContext
    private EntityManager em;

    @Autowired JoinRepository joinRepository;
    @Autowired MemberRepository memberRepository;
    @AfterEach//테스트 메소드 실행후 호출
    public void reset(){
        this.em.createNativeQuery("ALTER TABLE member ALTER COLUMN `member_id` RESTART WITH 41").executeUpdate();//시작값을 41로 초기화
        this.em.createNativeQuery("ALTER TABLE joins ALTER COLUMN `join_id` RESTART WITH 1").executeUpdate();//시작값을 1로 초기화
        this.em.createNativeQuery("ALTER TABLE club ALTER COLUMN `club_id` RESTART WITH 41").executeUpdate();//시작값을 41로 초기화
        this.em.createNativeQuery("ALTER TABLE join_club ALTER COLUMN `join_club_id` RESTART WITH 1").executeUpdate();//시작값을 1로 초기화
    }

    @Test
    public void 클럽생성() throws Exception{
        //given
        Member member = createMember();
        Club club = createClub("testClub",10, member.getName());
        //when
        em.flush();
        Club club2 = clubService.findOne(club.getId());
        //then
        assertEquals(club, club2);
    }

    @Test
    public void 삭제() throws Exception{
        //given
        Member member = createMember();
        Club club = createClub("TestName",10, member.getName());

        int joinCount = 1;
        Long join = joinService.Join(member.getId(), club.getId(), joinCount);
        Join getjoin = joinService.findOne(join);

        //when
        joinClubService.chageStatus(club); // 멤버상태변경

        joinClubRepository.deleteById(1L); //joinClub삭제
        clubService.deleteClub(club.getId()); // Club삭제
        Club one = clubService.findOne(club.getId());// 동아리아이디로 찾기 (삭제되었으면 null)
        //then

        assertEquals("동아리 탈퇴 확인", JoinStatus.cancel,getjoin.getStatus());//삭제후 Join의 status가 cancel인지 확인
        assertEquals("Member 동아리 탈퇴 확인",JoinStatus.cancel,member.getStatus());//삭제후 Member의 status가 cancel인지 확인
        assertNull(one); //삭제후 동아리가 null이 맞는지 확인

    }

    @Test
    public void 조회() throws Exception{
        //given
        Member member = createMember();

        Club club = createClub("테스트2",10,member.getName());
        //when
        clubService.findClub();
        //then
    }

    @Test
    public void 수정() throws Exception{
        //given
        Member member = createMember();
        Club club  = createClub("동아리테스트",10,member.getName());
        Club one = clubService.findOne(club.getId()); //생성하고 해당 객체를 찾아놓는다.
        //when
        clubService.updateClub(club.getId(),"수정테스트동아리",10,"수정테스트 멤버이름"); //값을 변경한다.
        //then
        assertEquals("동아리 번호확인",one.getId(),club.getId()); //수정전 객체와 수정후 객체의 번호가 같은지 확인
        assertEquals("동아리 이름수정확인","수정테스트동아리",club.getName());
        assertEquals("동아리 가입인원 확인",10,club.getTotalNumber());
        assertEquals("동아리 회장이름 확인","수정테스트 멤버이름",club.getClubJang());
    }

    @Test
    public void 동아리명_null() throws Exception{
        //given
        Club club = new Club();
        Member member = createMember();
        club.setClubJang(member.getName());
        club.setTotalNumber(10);
        clubService.saveClub(club);
        //when
        Club one = clubService.findOne(41l);

        //then
        assertEquals(club.getName(), one.getName());
        assertNull(one.getName());

    }
    @Test
    public void 동아리명_not_null() throws Exception{
        //given
        Club club = new Club();
        club.setName("testName");
        Long aLong = clubService.saveClub(club);
        //when
        Club one = clubService.findOne(aLong);

        //then
        assertEquals(club.getName(), one.getName());
        assertNotNull(one.getName());
    }
    @Test
    public void 동아리명10초과() throws Exception{
        //given
        Club club = new Club();
        club.setName("일이삼사오육칠팔구십초과");
        clubService.saveClub(club);
        //when
        Club one = clubService.findOne(41l);
        int length = one.getName().length();
        //then
        assertEquals(club.getName(), one.getName());
        assertTrue(length>10);
    }
    @Test
    public void 동아리명10이하() throws Exception{
        //given
        Club club = new Club();
        club.setName("일이삼사오육칠팔구");
        clubService.saveClub(club);
        //when
        Club one = clubService.findOne(41l);
        int length = one.getName().length();
        //then
        assertEquals(club.getName(), one.getName());
        assertTrue(length<10);
    }
    @Test
    public void 가입인원1미만() throws Exception{
        //given
        Club club = new Club();
        club.setTotalNumber(0);
        clubService.saveClub(club);
        //when
        Club one = clubService.findOne(41l);
        int totalNumber = one.getTotalNumber();
        //then
        assertEquals(club.getId(),one.getId());
        assertTrue(totalNumber<1);
    }
    @Test
    public void 가입인원20초과() throws Exception{
        //given
        Club club = new Club();
        club.setTotalNumber(21);
        clubService.saveClub(club);
        //when
        Club one = clubService.findOne(41l);
        int totalNumber = one.getTotalNumber();
        //then
        assertEquals(club.getId(),one.getId());
        assertTrue(totalNumber>20);
    }
    @Test
    public void 동아리명공백() throws Exception{
        //정규식
        String pattern = "^[\\s]*$"; //공백만체크

        //given
        Club club = new Club();
        club.setName("    name");
        clubService.saveClub(club);
        //when
        Club one = clubService.findOne(41l);
        //then
        assertFalse(Pattern.matches(pattern,one.getName()));//title이 공백만있는게 아니라면 True
    }
    @Test
    public void 동아리명특수문자제외() throws Exception{
        //정규식
        String pattern = "^[가-힣|a-z|A-Z|0-9|_ |]*$"; //특수문자

        //given
        Club club = new Club();
        club.setName("@테스트네임");
        clubService.saveClub(club);
        //when
        Club one = clubService.findOne(41l);
        //then
        assertFalse(Pattern.matches(pattern,one.getName())); //name이 정규식에 일치하지않으면 통과
        assertThat(pattern.matches("he!!o"), is(false)); //해당 문자열이 정규식에 일치하지않는다.
        assertThat(pattern.matches("hell()"), is(false));
        assertThat(pattern.matches("\"hello\""), is(false));
        assertThat(pattern.matches("hello^^"), is(false));
        assertThat(pattern.matches("<hello>"), is(false));
        assertThat(pattern.matches("#hello$"), is(false));
    }



    private Club createClub(String name,int totalCount,String memberName) {
        Club club = new Club();
        club.setName(name);
        club.setTotalNumber(totalCount);
        club.setClubJang(memberName);
        em.persist(club);
        return club;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원테스트1");
        em.persist(member);
        return member;
    }
}