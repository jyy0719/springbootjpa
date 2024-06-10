package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryNotUsedJpa {
    private final EntityManager em;

    // 리턴값이 Member가 아닌 void 또는 id 반환 , CQS(Command Query Separation) Pattern
    public void save (Member member) {
        // 이 시점에 영속성 컨텍스트가 member 객체를 컨텍스트에 올린다.
        // member 객체에 설정해놓은 Long id 키 값 설정에 따라 다르지만
        // 보통 persist 시 객체의 디비 id값이 member에 박힌다.
        em.persist(member);
    }
    public Member findOne (Long id) {
        // 단건 조회
        return em.find(Member.class, id);
    }
    public List<Member> findAll () {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name ", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
