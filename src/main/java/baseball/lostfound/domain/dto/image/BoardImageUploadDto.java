package baseball.lostfound.domain.dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardImageUploadDto {
    private List<MultipartFile> files = new ArrayList<>();
}
