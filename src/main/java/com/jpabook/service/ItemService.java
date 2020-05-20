package com.jpabook.service;

import com.jpabook.domain.item.Book;
import com.jpabook.domain.item.Item;
import com.jpabook.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }


    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

    @Transactional
    public void updateItem(Long id,String name, int price, int stockQuantity) {
        Item item = itemRepository.findOne(id);
        item.changeName(name);      // 명시적으로 엔티티에서 바꿔주는 습관을 들이자.
        item.changePrice(price);     // 그래야 어디 엔티티에서 바뀌었는지 명시적으로 알 수 있다.
        item.changeStockQuantity(stockQuantity);
    }
}
