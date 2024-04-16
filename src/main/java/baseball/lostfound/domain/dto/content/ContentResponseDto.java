package baseball.lostfound.domain.dto.content;

import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.Image;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ContentResponseDto {
    private Long id;
    private String title;
    private String texts;
    private Team team;
    private Position position;
    private String writer;
    private Image image;
    @Builder
    public ContentResponseDto(Content content){
        id= content.getId();
        title= content.getTitle();
        texts= content.getTexts();
        position=content.getPosition();
        team=content.getTeam();
        writer=content.getUser().getNickname();
        image=content.getImage();
    }
}
