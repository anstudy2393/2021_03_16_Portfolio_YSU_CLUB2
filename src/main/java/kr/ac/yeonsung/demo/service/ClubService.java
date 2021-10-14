package kr.ac.yeonsung.demo.service;

import kr.ac.yeonsung.demo.domain.club.Club;
import kr.ac.yeonsung.demo.repository.ClubRepository;
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
public class ClubService {
    private  final ClubRepository clubRepository;

    //=====동아리 등록=====//
    @Transactional
    public Long saveClub(Club club){
        validateDuplicateClub(club.getName());
        clubRepository.save(club);
        return club.getId();
    }

    //=====동아리 이름 중복체크=====//
    private void validateDuplicateClub(String name) {
        // EXCEPTION
        List<Club> findClubs = clubRepository.findByName(name);
        if (!findClubs.isEmpty()) {// 이름으로 동아리를 검색해 findClubs가 비어있지 않다면 존재하는 동아리므로 exception을 날림
            throw new IllegalStateException("이미 존재하는 동아리명입니다.");
        }
    }

    //=====전체 동아리 조회=====//
    public List<Club> findClub(){
        return  clubRepository.findAll();
    }

    //=====동아리 단건 조회=====//
    public Club findOne(Long clubId){
        return clubRepository.findById(clubId).orElse(null);
    }

    //=====club삭제=====//
    @Transactional
    public void deleteClub(Long clubId){
        Club club1 = clubRepository.findById(clubId).orElse(null);
        clubRepository.delete(club1);
    }

    //=====페이징=====//
    public Page<Club> findAll(Pageable pageable){
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.Direction.DESC,"id");//id를 기준으로 한페이지당 보여지는 게시글 10개씩
        return clubRepository.findAll(pageable);
    }

    //=====club update=====//
    @Transactional
    public void updateClub(Long clubId, String name, int totalNumber, String clubJang) {
        Club club = findOne(clubId);
        validateDuplicateClub(name);
        club.setName(name);
        club.setTotalNumber(totalNumber);
        club.setClubJang(clubJang);
    }
}
