package kr.ac.yeonsung.demo.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter @DynamicInsert
public class EventBoard {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventBoard_id")
    private Long id;

    @Column(columnDefinition = "varchar(100) default '관리자'")
    private String name;

    private String title;
    private String content;
    private String writeDate;
    private String updateDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member eventMember;

    public void setMember(Member member) {
        this.eventMember = member;
        eventMember.getEventBoardList().add(this);
    }
}
