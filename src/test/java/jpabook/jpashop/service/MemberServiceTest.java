package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryNotUsedJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryNotUsedJpa memberRepository;
    @Autowired EntityManager entityManager;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("member1");
        //when
        Long saveId = memberService.join(member);
        //then
        entityManager.flush();
        assertEquals(member,memberRepository.findOne(saveId));
    }
    //@Test(expected = IllegalStateException.class) -> jUnit4 에서는 이와같이 오류 처리
    @Test
    public void 중복_회원_에러() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("김사랑");
        Member member2 = new Member();
        member2.setName("김사랑");
        //when
        memberService.join(member1);
        Assertions.assertThrows(IllegalStateException.class,()-> memberService.join(member2)); // [1] 예외가 발생해야 한다. -> jUnit5 에서는 이와같이 오류 처리
        //then
        //fail("Assertions.fail() MSG => 예외가 발생해야 한다."); // [2] 여기까지 오면 테스트 실패 한 것.
    }
}