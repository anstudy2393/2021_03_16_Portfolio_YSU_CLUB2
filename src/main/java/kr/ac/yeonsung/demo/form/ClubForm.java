package kr.ac.yeonsung.demo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class ClubForm {

    private Long id;

    @NotBlank(message = "동아리명은 필수입니다. 공백으로만 이루어진 동아리명은 불가능합니다.") // 값이 비어있으거나 공백만 있을시 메세지를 띄움
    @Size(max = 10,message = "동아리명은 최대 10글자 까지 입니다!")//최대길이 50글자, 50글자 넘기면 메시지를 띄움
    @Size(min = 2,message = "동아리명은 최소 2글자 이상으로 만들수 있습니다!")//최대길이 50글자, 50글자 넘기면 메시지를 띄움
    @Pattern(regexp = "^[가-힣|a-z|A-Z|0-9|_ |]*$",message = "특수문자나 (자음과모음)으로만 이루어진 동아리명은 사용 불가능합니다.")
    private String name;

    @Max(value = 20,message = "회원수는 최대 20명 이하여야 합니다.")
    @Min(value = 1,message = "회원수는 최소 1명 이상이여야 합니다.")
    private int totalNumber;

    private String clubJang;
}
