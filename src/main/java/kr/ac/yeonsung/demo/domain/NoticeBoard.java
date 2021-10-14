package kr.ac.yeonsung.demo.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter @DynamicInsert
public class NoticeBoard {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_notice_id")
    private Long id;//게시글번호

    @Column(columnDefinition = "varchar(100) default '관리자'")
    private String name;//게시자
    
    private String title;//제목

    @Column(length = 4000)//varchar(4000)과 같음
    private String content;//게시글 내용

    private String writeDate;//작성한 날짜
    private String updateDate;//수정한 날짜

    @ManyToOne(fetch = LAZY)//n:1
    @JoinColumn(name = "member_id")
    private Member boardMember;

    public void setMember(Member member){
        this.boardMember = member;
        boardMember.getNoticeBoardList().add(this);
    }
}
