package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.ui.Model;

@Getter
@Setter
public class BookForm {
    private Long id;
    @NotEmpty(message = "책 제목 입력은 필수 입니다.")
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;

}
