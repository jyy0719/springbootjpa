package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {
    private final EntityManager em;

    // v6
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderItems oi" +
                                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }

    // v5
    public List<OrderQueryDto> findAllByDto_optimization() {
        // order, member, delivery 한방쿼리 조회 [1]
        List<OrderQueryDto> result = findOrders();
        // 조회 된 정보를 가지고 orderItem에 바로 넘기지 않고, 조회 된 정보의 orderId만 다시 뽑음
        List<Long> orderIds = toOrderIds(result);
        // findOrderItemMap 매서드 안에서 in절을 이용하여, orderId들을 다 넘겨 in절로 한번에 데이터 받아옴
        // n+1 해소
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);
        // key는 orderId 인 Map에서 해당  [1]에서 조회 한 id 값을 조회해서 값을 얻어와 set
        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream().map(r -> r.getOrderId()).collect(Collectors.toList());
    }

    private java.util.Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds){
        List<OrderItemQueryDto> orderItemIds = em.createQuery("select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count) " +
                "from OrderItem oi join oi.item i " +
                "where oi.order.id in :orderIds", OrderItemQueryDto.class).setParameter("orderIds", orderIds).getResultList();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>" + orderItemIds.toString());
        return orderItemIds.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }
    // v4
    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> orders = findOrders();
        orders.forEach( o ->
                o.setOrderItems(findOrderItems(o.getOrderId()))
        );
        System.out.println(orders.toString() + " ..............................");
        return orders;
    }
    private List<OrderQueryDto> findOrders() {
        return em.createQuery (

                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address) from Order o " +
                        "join o.member m " +
                        "join o.delivery d" , OrderQueryDto.class
        ).getResultList();
    }
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count ) " +
                "from OrderItem oi " +
                "join oi.item i " +
                "where oi.order.id = :orderId",
                        OrderItemQueryDto.class
        )
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
