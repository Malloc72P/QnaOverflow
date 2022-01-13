package scra.qnaboard.web.dto.member;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberListDTO {

    private final List<MemberDTO> members;

    public MemberListDTO(List<MemberDTO> members) {
        this.members = members;
    }
}
