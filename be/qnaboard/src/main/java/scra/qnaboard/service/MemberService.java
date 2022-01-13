package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.service.exception.member.MemberNotFoundException;
import scra.qnaboard.web.dto.member.MemberDTO;
import scra.qnaboard.web.dto.member.MemberListDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberListDTO findAllMember() {
        List<MemberDTO> collect = memberRepository.findAll()
                .stream()
                .map(MemberDTO::from)
                .collect(Collectors.toList());
        return new MemberListDTO(collect);
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
