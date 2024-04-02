package baseball.lostfound.domain.dto.content;

import baseball.lostfound.domain.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContentPagingDto {
    private Long id;
    private String title;
    private String writer;
    private boolean isImportant;
    private Integer commentCnt;

    public ContentPagingDto(Content content){
        id=content.getId();
        title=content.getTitle();
    }
}
