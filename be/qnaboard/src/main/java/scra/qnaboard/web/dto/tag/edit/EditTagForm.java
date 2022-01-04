package scra.qnaboard.web.dto.tag.edit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class EditTagForm {

    @NotBlank
    @Size(min = 1, max = 12)
    private String name;

    @NotBlank
    @Size(min = 6, max = 1000)
    private String description;

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
