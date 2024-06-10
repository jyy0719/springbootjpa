package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
    // 검색할 파라미터 명 셋팅
    private String memberName; // 회원이름
    private OrderStatus orderStatus;  // 주문상태 [ORDER,CANCEL]

}
