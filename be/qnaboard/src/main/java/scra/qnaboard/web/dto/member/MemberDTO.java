package scra.qnaboard.web.dto.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.member.Member;

@Getter
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
