package baseball.lostfound.domain.dto.content;

import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.Image;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ContentPagingDto {
    private Long id;
    private String title;
    private String writer;

    private Team team;
    private Position position;
    private boolean isImportant;
    private Integer commentCnt;
    private Image image;

    public ContentPagingDto(Content content){
        id=content.getId();
        title=content.getTitle();
        writer=content.getUser().getNickname();
        team=content.getTeam();
        position=content.getPosition();
        isImportant=content.isImportant();
        commentCnt=content.getCommentCnt();
        image=content.getImages().getFirst();
    }
}
