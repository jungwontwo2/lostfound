package baseball.lostfound.domain.dto.content;

import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.Image;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class ContentWriteDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String texts;
    @NotNull(message = "팀을 선택해주세요.")
    private Team team;
    @NotNull(message = "위치를 선택해주세요.")
    private Position position;
    private List<MultipartFile> images;
    public static Content toEntity(ContentWriteDto contentWriteDto, CustomUserDetails user){
        Content content = Content.builder()
                .title(contentWriteDto.getTitle())
                .texts(contentWriteDto.getTexts())
                .team(contentWriteDto.getTeam())
                .position(contentWriteDto.position)
                .commentCnt(0)
                .user(user.getUserEntity())
                .isImportant(false)
                .build();
        return content;
    }
}
