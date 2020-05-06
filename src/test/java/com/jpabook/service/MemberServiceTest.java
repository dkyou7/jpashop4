package com.jpabook.service;

import com.jpabook.domain.Member;
import com.jpabook.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void join() {
        Member member = new Member();
        member.setName("test");

        Long savedId = memberService.join(member);

        assertEquals(member,memberRepository.findOne(savedId));
    }

    // 이거 통과 못한 이유가 fail 떄문인데 이유 알기 IllegalStateException은 왜 안되는걸까 어떻게 하면 통과될까
    @Test(expected = AssertionError.class)
    public void assertionErrorTest(){

        Member member1 = new Member();
        member1.setName("test1");

        Member member2 = new Member();
        member2.setName("test2");

        memberService.join(member1);
        memberService.join(member2);

        fail("예외 발생해야함.");
    }

}