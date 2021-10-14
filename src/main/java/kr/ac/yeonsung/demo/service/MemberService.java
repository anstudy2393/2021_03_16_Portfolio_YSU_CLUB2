package kr.ac.yeonsung.demo.service;

import kr.ac.yeonsung.demo.domain.Address;
import kr.ac.yeonsung.demo.domain.EventBoard;
import kr.ac.yeonsung.demo.domain.Member;
import kr.ac.yeonsung.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByAddress_ClassNumber(member.getAddress().getClassNumber());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Transactional // 최신 회원수정
    public void updateMember(Long memberId, String name, String classNumber, String department, String location) {
        Address address = new Address(classNumber, department, location);
        Member updateMember = findOne(memberId);
        updateMember.setName(name);
        updateMember.setAddress(address);
        memberRepository.save(updateMember);
    }

    @Transactional // 최신 회원삭제
    public void deleteId(Long memberId) {
        Member findById = memberRepository.findById(memberId).orElse(null);
        if (findById == null) { // 삭제 유효성 검사
            throw new IllegalStateException("이미 삭제된 회원입니다");
        }
        else {
            memberRepository.delete(findById);
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Page<Member> findAll(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");
        return memberRepository.findAll(pageable);
    }

}

