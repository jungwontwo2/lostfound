package baseball.lostfound.domain.dto.content;

import baseball.lostfound.domain.entity.Content;
import lombok.Builder;
import lombok.Data;

@Data
public class ContentResponseDto {
    private Long id;
    private String title;
    private String texts;
    private String writer;
    @Builder
    public ContentResponseDto(Content content){
        id= content.getId();
        title= content.getTitle();
        texts= content.getTexts();
        writer=content.getUser().getNickname();
    }
}
