package kr.ac.yeonsung.demo.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "joins")
@Getter @Setter
public class Join {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "join_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "join",cascade = CascadeType.REMOVE)
    private List<JoinClub> joinClubs = new ArrayList<>();

    private LocalDateTime joinDateAt;   // 신청한 날짜

    @Enumerated(EnumType.STRING)
    private JoinStatus status; //신청상태 apply , CANCEL

    //=====연관관계 메서드=====//
    //Member
    public void setMember(Member member){
        this.member = member;
        member.getJoins().add(this);
    }

    //JoinClub
    public void addClub(JoinClub joinClub){
        joinClubs.add(joinClub);
        joinClub.setJoin(this);
    }

    public void delClub(JoinClub joinClub){
        joinClubs.remove(joinClub);
        joinClub.setClub(null);
    }

    //=====생성메서드=====//
    //동아리 가입
    public static Join addJoin(Member member,JoinClub joinClub){
        //System.out.println("=====Join의 addJoin메소드=====");
        Join join = new Join();
        join.setMember(member);
        join.addClub(joinClub);

        //System.out.println("MemberName : " + member.getName());
        //System.out.println("ClubName : " + joinClub.getClub().getName() + " ClubTotalNumber : " + joinClub.getClub().getTotalNumber());
        member.setStatus(JoinStatus.apply);//동아리 가입 상태로 변경
        join.setStatus(JoinStatus.apply);//동아리 가입 상태로 변경
        join.setJoinDateAt(LocalDateTime.now());//동아리 가입시간 설정
        System.out.println("===============");
        return join;
    }

    //=====비즈니스 로직=====//
    //동아리 탈퇴
    public void cancel(){
        this.setStatus(JoinStatus.cancel);//동아리 없는 상태로 변경
        member.setStatus(JoinStatus.cancel);//Member의 동아리 상태 변경
        for(JoinClub joinClub : joinClubs) {
            member.getJoins().remove(0);
            joinClub.cancel();
       }
    }


}
