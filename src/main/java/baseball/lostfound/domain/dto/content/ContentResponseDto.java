package baseball.lostfound.domain.dto.content;

import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.Image;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ContentResponseDto {
    private Long id;
    private String title;
    private String texts;
    private String writer;
    private List<Image> images;
    @Builder
    public ContentResponseDto(Content content){
        id= content.getId();
        title= content.getTitle();
        texts= content.getTexts();
        writer=content.getUser().getNickname();
        images=content.getImages();
    }
}
