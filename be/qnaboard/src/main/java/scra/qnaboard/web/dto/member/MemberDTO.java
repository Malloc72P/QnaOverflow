package scra.qnaboard.web.dto.member;

import lombok.*;
import scra.qnaboard.domain.entity.member.Member;

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
