package kr.ac.yeonsung.demo.domain;

import kr.ac.yeonsung.demo.domain.club.Club;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class JoinClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "join_club_id")
    private Long id;

//체크하기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "join_id")
    private Join join;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    private int count;//가입 인원(사실상1)


    //=====생성 로직=====//
    //동아리세팅
    public static JoinClub addJoinClub(Club club,int count){
        //System.out.println("=====joinClub의 addJoinClub메소드=====");
        JoinClub joinClub = new JoinClub();
        joinClub.setClub(club);
        joinClub.setCount(count);
        //System.out.println("넘어온 count : " + count);//ok
        club.removeMember(count);
        //System.out.println("===============");
        return joinClub;
    }
    public void deleteJoin(){
        setClub(null);
    }
    //=====비즈니스 로직=====//
    //동아리 탈퇴
    public void cancel(){ getClub().addMember(count);//Club클래스의 addMember메소드로 인원수 증가
    }

}
