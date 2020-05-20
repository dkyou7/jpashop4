package com.jpabook.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;

    @Override
    public void changeName(String name) {
        this.setName(name);
    }

    @Override
    public void changePrice(int price) {
        this.setPrice(price);
    }

    @Override
    public void changeStockQuantity(int stockQuantity) {
        this.setStockQuantity(stockQuantity);
    }
}
