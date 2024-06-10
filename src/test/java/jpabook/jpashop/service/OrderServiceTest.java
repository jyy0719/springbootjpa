package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    
    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "노들로", "123-132"));
        em.persist(member);

        Book item = new Book();
        item.setName("난쟁이가 쏘아올린 작은 공");
        item.setPrice(7000);
        item.setAuthor("지은이");
        item.setStockQuantity(70);
        em.persist(item);
        //when
        Long orderId = orderService.order(member.getId(), item.getId(), 2);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문 시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(7000 * 2, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량 이다.");
        assertEquals(68, item.getStockQuantity(), "주문 수량만큼 재고가 줄어야한다.");
    }
    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "노들로", "123-132"));
        em.persist(member);

        Book item = new Book();
        item.setName("난쟁이가 쏘아올린 작은 공");
        item.setPrice(7000);
        item.setAuthor("지은이");
        item.setStockQuantity(70);
        em.persist(item);
        //when
        //then
        assertThrows(NotEnoughStockException.class,()->
                orderService.order(member.getId(), item.getId(), 72));
    }
    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "노들로", "123-132"));
        em.persist(member);

        Book item = new Book();
        item.setName("난쟁이가 쏘아올린 작은 공");
        item.setPrice(7000);
        item.setAuthor("지은이");
        item.setStockQuantity(70);
        em.persist(item);

        Long orderId = orderService.order(member.getId(), item.getId(), 2);
        //when
        orderService.cancel(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "상품 주문 취소 시 상태는 CANCEL 이어야한다.");
        assertEquals(70, item.getStockQuantity(), "상품 주문 취소 시 재고 수량이 원복되어야한다.");
    }
}