package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private int orderPrice;    // 주문가격
    private int count;     // 주문수량

    //protected OrderItem() { } // lombok으로 작성

    //==엔티티 생성 매서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice,int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        
        // 이 매서드 호출 자체가 주문 건이 들어왔다는 것! 그럼 재고 수량을 줄여줘야겠지
        item.removeStock(count);
        return orderItem;
    }
    
    //==비즈니스 로직==//
    /**
     * 재고 수량 원복
     * 주문을 취소하면 취소한 주문 만큼 재고가 채워져야한다.
     */
    public void cancel() {
        // 주문 수량만큼 재고 채워넣음
        getItem().addStock(count);
    }
    //==조회 로직==//
    /**
     * OrderItem 내 orderPrice * count.
     * 주문 상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
