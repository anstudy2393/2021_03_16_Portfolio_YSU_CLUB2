package kr.ac.yeonsung.demo.repository;

import kr.ac.yeonsung.demo.domain.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard,Long> {
    List<NoticeBoard> findTop3ByOrderByIdDesc();//데이터3개만 가져오기
}
