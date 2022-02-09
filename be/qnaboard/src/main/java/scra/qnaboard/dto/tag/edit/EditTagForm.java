package scra.qnaboard.dto.tag.edit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 태그 수정을 위한 DTO.
 * 컨트롤러의 파라미터에서 사용함.
 * 태그 수정 페이지의 내용을 채우기 위해서 사용한다.
 * 수정에 실패한 경우, 왜 실패했는지 사용자에게 알려주기 위해서 사용하기도 함
 */
@Getter
@AllArgsConstructor
public class EditTagForm {

    @NotBlank
    @Size(min = 1, max = 40)
    private String name;

    @NotBlank
    @Size(min = 6, max = 200)
    private String description;

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
