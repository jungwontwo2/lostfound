package baseball.lostfound.domain.dto.content;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentEditDto {
    private Long id;
    private String title;
    private String texts;
}
