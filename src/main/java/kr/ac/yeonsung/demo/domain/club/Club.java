package kr.ac.yeonsung.demo.domain.club;

import kr.ac.yeonsung.demo.domain.CategoryClub;
import kr.ac.yeonsung.demo.domain.Exception.NotJoinException;
import kr.ac.yeonsung.demo.domain.JoinClub;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    private String name;

    private int totalNumber;//가입가능 총 인원수

    //@OneToMany(mappedBy = "club" ,cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "club",cascade = CascadeType.ALL)
    private List<CategoryClub> categoryClubs = new ArrayList<>();

    @OneToMany(mappedBy = "club",cascade = CascadeType.REMOVE)
    private List<JoinClub> joinClubs = new ArrayList<>();

    private String clubJang;

    //=====비즈니스 로직=====//
    //동아리 탈퇴(총인원에서 1증가)
    public void addMember(int count){
        this.totalNumber += count;
    }
    //동아리 가입(총인원에서 1감소)
    public void removeMember(int count){
        int restMember = this.totalNumber - count;//총인원수에서 가입인원 감소
        if(restMember < 0){//가입가능 총인원수가 0보다 작으면 가입 불가
            throw new NotJoinException("인원 초과로 인해 가입이 불가능합니다.");
        }
        //=====가입가능한 인원수가 있다면=====//
        this.totalNumber = restMember;//총인원수 변경
    }
}
