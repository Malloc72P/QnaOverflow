package scra.qnaboard.dto.tag.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 태그 생성 요청을 위한 DTO
 * 컨트롤러의 파라미터에서 사용함
 */
@Getter
@AllArgsConstructor
public class CreateTagForm {

    @NotBlank
    @Size(min = 1, max = 40)
    private String name;

    @NotBlank
    @Size(min = 6, max = 1000)
    private String description;

}
