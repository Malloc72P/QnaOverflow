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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Page<MemberDTO> members(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return memberRepository.findAllByDeletedFalseOrderByCreatedDateDesc(pageRequest)
                .map(MemberDTO::from);
    }

    public Member findMember(long memberId) {
        return memberRepository.findByIdAndDeletedFalse(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    @Transactional
    public void deleteMember(long memberId) {
        memberRepository.deleteMemberById(memberId);
    }
}
