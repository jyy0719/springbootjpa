package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepositoryNotUsedJpa;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepositoryNotUsedJpa memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional(readOnly = false)
    public Long order(Long memberId, Long itemId, int count ) {
        // 회원정보 조회
        Member member = memberRepository.findOne(memberId);
        // 구매물품 정보 조회
        Item item = itemRepository.findOne(itemId);
        // 배송정보 생성 (어디로 배송할 것인지에 대해)
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // 주문 생성 - 이 예제에서는 orderItem을 하나만 넘기도록 했다. 여러개 넘겨도 되는데 복잡해지니 단건 서비스만 되도록 일단 구현
        Order order = Order.createOrder(member, delivery, orderItem);
        // 주문 저장 - Order 엔티티에 OrderItem과 Delivery엔티티가 CascadeType.ALL로 설정되어 있어서 Order 영속성 저장 시 함께 저장된다.
        orderRepository.save(order); // 영속성 컨텍스트에 저장

        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancel(Long orderId) {
        // 주문 확인
        Order order = orderRepository.findOne(orderId);
        // 배송 상태 확인 및 재고 원복
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
