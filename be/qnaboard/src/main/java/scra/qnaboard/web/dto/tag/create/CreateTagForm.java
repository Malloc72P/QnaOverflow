package scra.qnaboard.web.dto.tag.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class CreateTagForm {

    @NotBlank
    @Size(min = 1, max = 12)
    private String name;

    @NotBlank
    @Size(min = 6, max = 1000)
    private String description;

}
