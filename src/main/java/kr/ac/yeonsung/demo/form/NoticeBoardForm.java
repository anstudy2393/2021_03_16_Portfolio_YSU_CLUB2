package kr.ac.yeonsung.demo.form;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter @Setter
public class NoticeBoardForm {

    private Long id;

    @NotBlank(message = "게시글 제목은 필수입니다, 공백으로만 이루어진 제목은 불가능합니다!") // 값이 비어있으거나 공백만 있을시 메세지를 띄움
    @Size(max = 50,message = "제목은 최대 50글짜 까지 입니다!")//최대길이 50글자, 50글자 넘기면 메시지를 띄움
    private String title;//제목

    @NotBlank(message = "게시글 내용은 필수입니다, 공백으로만 이루어진 내용은 불가능합니다!")// 값이 비어있으거나 공백만 있을시 메세지를 띄움
    @Size(max = 2000,message = "내용은 최대 2000글짜 까지 입니다!")//최대길이 2000글자, 2000글자 넘기면 메시지를 띄움
    private String content;//내용

    private String writeDate;//작성날짜
    private String updateDate;//수정날짜
}
