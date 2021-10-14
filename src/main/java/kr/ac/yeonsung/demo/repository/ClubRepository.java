package kr.ac.yeonsung.demo.repository;

import kr.ac.yeonsung.demo.domain.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {
    List<Club> findByName(String name);//이름으로 검색
}
