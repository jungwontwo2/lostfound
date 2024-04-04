package baseball.lostfound.Content;

import baseball.lostfound.domain.dto.content.ContentWriteDto;
import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import baseball.lostfound.repository.ContentRepository;
import baseball.lostfound.repository.UserRepository;
import baseball.lostfound.service.ContentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional
public class Content {
    @Autowired
    ContentService contentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentRepository contentRepository;

    @Test
    public void writeContentNoImage() throws IOException {
        User user = userRepository.findByLoginId("qwer").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, null,false);
        contentService.writeContent(contentWriteDto,customUserDetails);
        List<baseball.lostfound.domain.entity.Content> all = contentService.getAllContents();
        Assertions.assertThat(all.size()).isEqualTo(1);
    }


}
