package baseball.lostfound.service;

import baseball.lostfound.domain.dto.content.ContentPagingDto;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

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
