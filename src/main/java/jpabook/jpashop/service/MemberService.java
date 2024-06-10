package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryNotUsedJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // **중요**
@RequiredArgsConstructor
public class MemberService {
    // final을 하면 생성자를 만들지 않았을 때, 컴파일 시점에 오류를 발생 
    private final MemberRepository memberRepository;
    private final MemberRepositoryNotUsedJpa memberRepositoryNotUsedJpa;

    // @Autowired -> 생성자가 하나만 있는 경우에는 스프링이 @Autowired어노테이션이 없어도 자동으로 빈주입해준다.
    // 더나아가 아래 코드는 @RequiredArgsConstructor를 해주면 자동 필요로 하는 생성자를 주입해준다. 그래서 주석 처리
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember_(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers_() {
        return memberRepository.findAll();
    }
    public Member findMember_(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void update_(Long id , String name) {
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }

    // =================================================================================== //

    /**
     * 같은 이름 회원 검색
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepositoryNotUsedJpa.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findMember(Long memberId) {
        return memberRepositoryNotUsedJpa.findOne(memberId);
    }

    @Transactional
    public void update (Long id , String name) {
        Member member = memberRepositoryNotUsedJpa.findOne(id);
        member.setName(name);
    }
}
