package baseball.lostfound.Content;

import baseball.lostfound.domain.dto.content.ContentWriteDto;
import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.Image;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import baseball.lostfound.repository.ContentRepository;
import baseball.lostfound.repository.UserRepository;
import baseball.lostfound.service.ContentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class Content {
    @Autowired
    ContentService contentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentRepository contentRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void writeContentNoImage() throws IOException {
        User user = userRepository.findByLoginId("qwer").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, null,false);
        Long content = contentService.writeContent(contentWriteDto, customUserDetails);
        baseball.lostfound.domain.entity.Content findContent = contentRepository.findById(content).orElse(null);
        Assertions.assertThat(findContent.getTitle()).isEqualTo("aaa");
        Assertions.assertThat(findContent.getTeam()).isEqualTo(Team.한화);
        Assertions.assertThat(findContent.getPosition()).isEqualTo(Position.FIRST_BASE);
        List<baseball.lostfound.domain.entity.Content> all = contentService.getAllContents();
        Assertions.assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void writeContentYesImage() throws IOException {
        User user = userRepository.findByLoginId("qwer").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // MockMultipartFile 생성
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", // 파라미터 이름
                "testImage.jpg", // 파일 이름
                "image/jpeg", // 컨텐츠 타입
                "test image content".getBytes() // 파일 내용
        );
        MockMultipartFile mockImage2 = new MockMultipartFile(
                "image", // 파라미터 이름
                "testImage.jpg", // 파일 이름
                "image/jpeg", // 컨텐츠 타입
                "test image content".getBytes() // 파일 내용
        );

        // 이미지 리스트 생성
        List<MultipartFile> images = new ArrayList<>();
        images.add(mockImage);
        images.add(mockImage2);

        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, images,false);
        Long contentId = contentService.writeContent(contentWriteDto, customUserDetails);
        em.clear();
        baseball.lostfound.domain.entity.Content content = contentRepository.findByIdWithImage(contentId);
        List<Image> imageList = content.getImages();
        Assertions.assertThat(imageList.size()).isEqualTo(2);
        Image first = imageList.getFirst();
        Assertions.assertThat(first.getContent().getId()).isEqualTo(content.getId());
        List<baseball.lostfound.domain.entity.Content> all = contentService.getAllContents();
        Assertions.assertThat(all.size()).isEqualTo(1);
    }
    @Test
    public void deleteContent() throws IOException {
        User findUser = userRepository.findByLoginId("qwer").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(findUser);
        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, null,false);
        Long content = contentService.writeContent(contentWriteDto, customUserDetails);
        baseball.lostfound.domain.entity.Content findContent = contentRepository.findById(content).orElse(null);
        contentService.deleteContent(findContent.getId(),customUserDetails.getUserEntity());
        List<baseball.lostfound.domain.entity.Content> all = contentService.getAllContents();
        Assertions.assertThat(all.size()).isEqualTo(0);
    }

}
