package baseball.lostfound.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserDto {
    @NotBlank(message = "아이디를 입력하세요")
    @Size(min = 4,max = 10,message = "최소 4자 이상, 10자 이하로 입력하세요")
    @Pattern(regexp = "^[a-z0-9]*$", message = "알파벳 소문자(a~z), 숫자(0~9)만 입력 가능합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 8,max = 20,message = "최소 8자 이상, 20자 이하로 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    private String password;
}
