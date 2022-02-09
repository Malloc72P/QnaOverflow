package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.service.exception.member.MemberNotFoundException;
import scra.qnaboard.dto.member.MemberDTO;

/**
 * 회원 서비스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 목록조회(페이징)
     */
    public Page<MemberDTO> members(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return memberRepository.findAllByDeletedFalseOrderByCreatedDateDesc(pageRequest)
                .map(MemberDTO::from);
    }

    /**
     * 회원 단건조회
     */
    public Member findMember(long memberId) {
        return memberRepository.findByIdAndDeletedFalse(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void deleteMember(long memberId) {
        memberRepository.deleteMemberById(memberId);
    }
}
