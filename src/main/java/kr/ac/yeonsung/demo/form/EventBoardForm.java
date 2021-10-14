package kr.ac.yeonsung.demo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class EventBoardForm {

    private Long id;

    @NotEmpty(message = "게시글 제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "게시글 내용을 입력해주세요.")
    private String content;

    private String writeDate;
    private String updateDate;
}
