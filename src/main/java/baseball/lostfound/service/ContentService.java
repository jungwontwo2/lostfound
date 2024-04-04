package baseball.lostfound.service;

import baseball.lostfound.domain.dto.content.ContentPagingDto;
import baseball.lostfound.domain.dto.content.ContentWriteDto;
import baseball.lostfound.domain.dto.image.BoardImageUploadDto;
import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

    public Long writeContent(ContentWriteDto contentDto, CustomUserDetails user){
        if(contentDto.getImages()==null){
            Content content = ContentWriteDto.toEntity(contentDto,user);
            contentRepository.save(content);
            return content.getId();
        }else {
            Content content = ContentWriteDto.toEntity(contentDto,user);
            contentRepository.save(content);
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
}
