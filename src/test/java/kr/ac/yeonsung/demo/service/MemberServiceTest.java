package kr.ac.yeonsung.demo.service;

import kr.ac.yeonsung.demo.domain.Address;
import kr.ac.yeonsung.demo.domain.Member;
import kr.ac.yeonsung.demo.form.MemberForm;
import kr.ac.yeonsung.demo.repository.MemberRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em; // db의 쿼리를 보기 위해 생성

    @After
    public void reset() {
        memberRepository.deleteAll();
        this.em.createNativeQuery("ALTER TABLE member ALTER COLUMN `member_id` RESTART WITH 41").executeUpdate();
    }

    private Member createMember() {
        Member member = new Member();
        Address address = new Address("2015114033", "테스트학과", "테스트 지역명");
        member.setName("테스트이름");
        member.setAddress(address);
        memberService.join(member);
        return member;
    }


    @Test
//    @Rollback(false) // Transctional이 기본적으로 Rollback을 해버리기 때문에 db의 쿼리를 보기 위해 이용함
    public void 회원가입() throws Exception {
        // given
        Member member = createMember();

        // when
//        Long savedId = memberService.join(member);
        Member getMember = memberService.findOne(member.getId());

        // then
        assertEquals(member.getId(), getMember.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = createMember();

        Member member2 = createMember();

        // when
        memberService.join(member1);
        memberService.join(member2);
        // then
        fail("예외 발생");
    }

    @Test
    public void 회원수정() throws Exception {
        //  given
        Member member = createMember();

        //  when
        Member getMember = memberService.findOne(member.getId());
        Address address = new Address("2015114034", "학과변경", "지역명 변경");
        member.setName("이름변경");
        member.setAddress(address);

        memberService.updateMember(getMember.getId(), member.getName(), member.getAddress().getClassNumber(), member.getAddress().getDepartment(), member.getAddress().getLocation());

        Member changeMember = memberService.findOne(getMember.getId());

        //  then
        assertEquals(changeMember.getName(), "이름변경");
        assertEquals(changeMember.getAddress().getClassNumber(), "2015114034");
        assertEquals(changeMember.getAddress().getDepartment(), "학과변경");
        assertEquals(changeMember.getAddress().getLocation(), "지역명 변경");
    }

    @Test
    public void 회원탈퇴() throws Exception {
        //  given
        Member member = createMember();

        //  when
        memberService.deleteId(member.getId());
        Member id = memberService.findOne(41L);

        //  then
        assertNull(id);
    }

    @Test
    public void 회원명조건() throws Exception {
        //  given
        String pattern = "[가-힣]{2,10}";
        Member member = createMember();


        //  when
        Member getMember = memberService.findOne(member.getId());
        int name = getMember.getName().length();

        //  then
        assertTrue(Pattern.matches(pattern, getMember.getName()));
        assertTrue(name >= 2);
        assertTrue(name <= 10);
    }
    
    @Test
    public void 학번조건() throws Exception {
        //given
        String pattern = "[0-9]{10}";
        Member member = createMember();

        //when
        Member getMember = memberService.findOne(member.getId());
        int classNumber = getMember.getAddress().getClassNumber().length();

        //then
        assertTrue(Pattern.matches(pattern, getMember.getAddress().getClassNumber()));
        assertTrue(classNumber == 10);
    }

    @Test
    public void 학과조건() throws Exception {
        //given
        String pattern = "[가-힣]{2,10}";
        Member member = createMember();

        //when
        Member getMember = memberService.findOne(member.getId());
        int department = getMember.getAddress().getDepartment().length();

        //then
        assertTrue(Pattern.matches(pattern, getMember.getAddress().getDepartment()));
        assertTrue(department >= 2);
        assertTrue(department <= 10);
    }

    @Test
    public void 지역조건() throws Exception {
        //given
        String pattern = "[가-힣]{3,5}\\s[가-힣]{3,5}";
        Member member = createMember();

        //when
        Member getMember = memberService.findOne(member.getId());
        int location = getMember.getAddress().getLocation().length();

        //then
        assertTrue(Pattern.matches(pattern, getMember.getAddress().getLocation()));
        assertTrue(location >= 7);
        assertTrue(location <= 10);
    }
}
