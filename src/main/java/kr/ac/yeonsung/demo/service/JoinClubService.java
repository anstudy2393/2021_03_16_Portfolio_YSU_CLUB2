package kr.ac.yeonsung.demo.service;

import kr.ac.yeonsung.demo.domain.Join;
import kr.ac.yeonsung.demo.domain.JoinClub;
import kr.ac.yeonsung.demo.domain.JoinclubMapping;
import kr.ac.yeonsung.demo.domain.club.Club;
import kr.ac.yeonsung.demo.repository.ClubRepository;
import kr.ac.yeonsung.demo.repository.JoinClubRepository;
import kr.ac.yeonsung.demo.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class JoinClubService {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private JoinRepository joinRepository;
    @Autowired
    private JoinClubRepository joinClubRepository;

    @Transactional
    public JoinClub findOne(Long clubId){  return joinClubRepository.findById(clubId).orElse(null);}

    //=====동아리 신청현황 페이징=====//
    public Page<JoinClub> findAll(Pageable pageable){
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.Direction.DESC,"id");//한페이지당 id를 기준으로 10개씩 가져온다
        return joinClubRepository.findAll(pageable);
    }

    //=====동아리 신청상태 변경=====//
    @Transactional
    public void chageStatus(Club club){
        List<JoinclubMapping> byClub = joinClubRepository.findByClub(club);//club의 정보를 담은 byClub을 가져온다. 
        int size = byClub.size();
        for(int i=0; i<size;i++) {
            Join join = joinRepository.findById(byClub.get(i).getJoin_Id()).orElse(null);
                join.cancel();//동아리 신청상태를 apply->cancel로 변경한다
        }
    }
}
