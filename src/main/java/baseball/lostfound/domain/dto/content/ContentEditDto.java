package baseball.lostfound.domain.dto.content;

import baseball.lostfound.domain.entity.Image;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class ContentEditDto {
    private Long id;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String texts;
    @NotNull(message = "팀을 선택해주세요.")
    private Team team;
    @NotNull(message = "위치를 선택해주세요.")
    private Position position;
    @NotNull(message = "이미지를 선택해주세요.")
    private MultipartFile image;
}
