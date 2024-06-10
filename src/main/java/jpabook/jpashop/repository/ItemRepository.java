package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        // id 값이 없다는 것은 새로 생성한다는 것
        if (item.getId() == null) {
            em.persist(item);
        // id 값이 있다는 것은 이미 디비에 저장되어 있는 것 수정한다는 것
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> finditems() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
