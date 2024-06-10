package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;
    @ManyToMany // 다대다는 실무에서 사용하면 안됨!
    @JoinTable(
        name = "category_item", // 중간테이블 이름
        joinColumns = { @JoinColumn(name = "category_id") }, // 중간테이블에 카테고리id
        inverseJoinColumns = { @JoinColumn(name = "item_id")}) // 중간테이블에 아이템id
    private List<Item> items = new ArrayList<>();

    // 일반적으로 하나의 부모, 여러명의 자식이라고생각하면 부모는 one , 자식은 many
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 편의 매서드==//
    public void addChildCategory(Category child)  {
        this.child.add(child);
        child.setParent(this);
    }
}
