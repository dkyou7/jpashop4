package com.jpabook.service;

import com.jpabook.domain.Member;
import com.jpabook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    //@Autowired
    //MemberRepository memberRepository;
    private final MemberRepository memberRepository;

    //public MemberService(MemberRepository memberRepository) {
    //    this.memberRepository = memberRepository;
    //}

    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);  // DB에 값이 보장이 된다.
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id,String name){
        Member mem = memberRepository.findOne(id);
        mem.setName(name);
    }
}
