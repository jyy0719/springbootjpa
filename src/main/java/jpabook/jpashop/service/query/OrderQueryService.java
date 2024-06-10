package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderRepository orderRepository;
    public List<Order>  commandNQuery() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember();
            order.getDelivery();
            List<OrderItem> orderItems = order.getOrderItems();         // 직접조회
            orderItems.stream().forEach(o -> o.getItem().getName());    // 직접조회
        }
        return all;
    }
}
