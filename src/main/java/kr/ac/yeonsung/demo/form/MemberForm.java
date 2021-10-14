package kr.ac.yeonsung.demo.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberForm {


    private Long id;

    @Pattern(regexp = "[가-힣]{2,10}", message = "이름은 한글로, 2~10글자 사이로 입력해주세요.")
    private String name;

    @Pattern(regexp = "[0-9]{10}", message = "학번은 10자리 숫자만 입력해주세요.")
    private String classNumber;

    @Pattern(regexp = "[가-힣]{2,10}", message = "학과는 한글로, 2~10글자 사이로 입력해주세요.")
    private String department;

    @Pattern(regexp = "[가-힣]{3,5}\\s[가-힣]{3,5}", message = "지역은 한글로, XXX(시|도) XXX(시|군|구)로 입력해주세요 ")
    private String location;
}