package kr.ac.yeonsung.demo.repository;

import kr.ac.yeonsung.demo.domain.Join;
import kr.ac.yeonsung.demo.domain.JoinClub;
import kr.ac.yeonsung.demo.domain.JoinclubMapping;
import kr.ac.yeonsung.demo.domain.club.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface JoinClubRepository extends JpaRepository<JoinClub,Long> {
    List<JoinclubMapping> findByClub(Club club);
}

