package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.domain.repository.tag.TagSimpleQueryRepository;
import scra.qnaboard.web.dto.tag.list.TagDTO;
import scra.qnaboard.web.dto.tag.list.TagListDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final TagSimpleQueryRepository tagSimpleQueryRepository;

    public TagListDTO list() {
        List<TagDTO> tags = tagSimpleQueryRepository.tagsWithAuthor()
                .stream()
                .map(TagDTO::from)
                .collect(Collectors.toList());

        return new TagListDTO(tags);
    }

}
