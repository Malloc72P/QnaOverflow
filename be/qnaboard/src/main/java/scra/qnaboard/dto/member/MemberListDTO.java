package scra.qnaboard.dto.member;

import lombok.Getter;

import java.util.List;

/**
 * 회원 목록조회를 위한 DTO
 */
@Getter
public class MemberListDTO {

    private final List<MemberDTO> members;

    public MemberListDTO(List<MemberDTO> members) {
        this.members = members;
    }
}
