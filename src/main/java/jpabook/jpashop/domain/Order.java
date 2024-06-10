package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy ="order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //==연관관계 편의 매서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    public void addOrderItem(OrderItem orderItems) {
        this.orderItems.add(orderItems);
        orderItems.setOrder(this);
    }

    //==엔티티 생성 매서드==//
    // Order 엔티티를 외부에서 생성이 필요할 때, 밖에서 new로 접근하는 것이 아니라
    // 생성할 때 부터  createOrder 매서드로 접근해서 엔티티 내부에서 기본 값 셋팅 후 Order 엔티티가 생성되도록!!!
    // 주문 생성에 대한 복잡한 비즈니스 로직을 이안에서 다 핸들링되도록!
    public static Order createOrder (Member member, Delivery delivery, OrderItem... orderItems) {
        System.out.println("member -> " + (member == null) + " ,delivery -> " + (delivery == null));
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    //==비즈니스 로직==//
    /**
     * 주문 취소
     * 주문을 취소하면 취소한 주문 만큼 재고가 채워져야한다.
     */
    public void cancel() {
        if (delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        // 주문 상품 재고 원복
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
        return totalPrice;
    }
}
