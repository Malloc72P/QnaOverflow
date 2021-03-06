package scra.qnaboard.dto.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.member.Member;

/**
 * 회원 조회를 위한 DTO.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDTO {

    private long memberId;
    private String memberName;

    public static MemberDTO from(Member member) {
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.memberId = member.getId();
        memberDTO.memberName = member.getNickname();

        return memberDTO;
    }
}
