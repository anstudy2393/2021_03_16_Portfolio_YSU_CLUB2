package kr.ac.yeonsung.demo.domain;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Join> joins=new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private JoinStatus status;//[apply , cancel]

    @OneToMany(mappedBy = "boardMember")
    private List<NoticeBoard> noticeBoardList = new ArrayList<>();

    @OneToMany(mappedBy = "eventMember")
    private List<EventBoard> eventBoardList = new ArrayList<>();

////,,,,,,,
    public void delClub(Join join){
        joins.remove(join);
        join.setMember(null);
    }
}
