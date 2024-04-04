package baseball.lostfound.domain.dto.content;

import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.Image;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class ContentWriteDto {
    private String title;
    private String texts;
    private Team team;
    private Position position;
    private List<MultipartFile> images;
    private boolean isImportant;
    public static Content toEntity(ContentWriteDto contentWriteDto, CustomUserDetails user){
        Content content = Content.builder()
                .title(contentWriteDto.getTitle())
                .texts(contentWriteDto.getTexts())
                .team(contentWriteDto.getTeam())
                .position(contentWriteDto.position)
                .user(user.getUserEntity())
                .isImportant(false)
                .build();
        return content;
    }
}
