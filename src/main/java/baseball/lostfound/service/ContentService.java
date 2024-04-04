package baseball.lostfound.service;

import baseball.lostfound.domain.dto.content.ContentPagingDto;
import baseball.lostfound.domain.dto.content.ContentWriteDto;
import baseball.lostfound.domain.dto.image.BoardImageUploadDto;
import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.Image;
import baseball.lostfound.repository.ContentRepository;
import baseball.lostfound.repository.ImageRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

    private final ImageRepository imageRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Long writeContent(ContentWriteDto contentDto, CustomUserDetails user) throws IOException {
        if(contentDto.getImages()==null || contentDto.getImages().isEmpty()){
            Content content = ContentWriteDto.toEntity(contentDto,user);
            contentRepository.save(content);
            return content.getId();
        }else {
            Content content = ContentWriteDto.toEntity(contentDto,user);
            contentRepository.save(content);
            List<MultipartFile> images = contentDto.getImages();
            for (MultipartFile image : images) {
                if(image.isEmpty()){
                    continue;
                }
                String saveImageUrl = saveImage(image);
                Image buildImage = Image.builder()
                        .url(saveImageUrl)
                        .content(content)
                        .build();
                imageRepository.save(buildImage);
            }
            return content.getId();
        }
    }
    public List<Content> getAllContents(){
        return contentRepository.findAll();
    }

    public Page<ContentPagingDto> paging(Pageable pageable, String criteria) {
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 8;//한페이지에 보여줄 글 개수
        //System.out.println("zz");
        PageRequest pageRequest = PageRequest.of(page, pageLimit, Sort.by(Sort.Order.desc("isImportant"), Sort.Order.desc(criteria),Sort.Order.desc("id")));
        Page<Content> contents = contentRepository.findAll(pageRequest);
        Page<ContentPagingDto> contentsDto = contents.map(content -> new ContentPagingDto(content));
        return contentsDto;
    }

    public Page<ContentPagingDto> getBoardListBySearchword(Pageable pageable, String searchWord) {
        int page=pageable.getPageNumber()-1;//page위치에 있는 값은 0부터 시작한다.
        int pageLimit = 8;//한페이지에 보여줄 글 개수
        PageRequest pageRequest = PageRequest.of(page, pageLimit, Sort.by(Sort.Order.desc("isImportant"), Sort.Order.desc("id")));
        Page<Content> contents = contentRepository.findByTitleContainingOrderByIsImportantDescAndContentIdDesc(pageRequest, searchWord);
        Page<ContentPagingDto> contentDtos = contents.map(content -> new ContentPagingDto(content));
        return contentDtos;
    }
    private String saveImage(MultipartFile multipartFile) throws IOException {
        String filename = getUUIDFileName(multipartFile);
        ObjectMetadata metadata = setObjectMetadata(multipartFile);
        amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, filename).toString();
    }
    private static ObjectMetadata setObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }
    private static String getUUIDFileName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String filename = uuid+"_"+originalFilename;
        return filename;
    }
}
