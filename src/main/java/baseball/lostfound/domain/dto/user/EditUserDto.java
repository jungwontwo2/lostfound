package baseball.lostfound.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditUserDto {
    private String loginId;
    private String nickname;

}
