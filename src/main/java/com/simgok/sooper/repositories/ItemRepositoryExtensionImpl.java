package com.simgok.sooper.repositories;

import com.querydsl.jpa.JPQLQuery;
import com.simgok.sooper.model.Item;
import com.simgok.sooper.model.QItem;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.simgok.sooper.model.QItem.item;

public class ItemRepositoryExtensionImpl extends QuerydslRepositorySupport implements ItemRepositoryExtension {
    public ItemRepositoryExtensionImpl() {
        super(Item.class);
    }

    @Override
    public List<Item> findByKeyword(String keyword) {
        QItem Item = item;
        List<Item> resultQuery = from(item)
                .where(item.stockQuantity.goe(1)
                                .and(item.name.containsIgnoreCase(keyword)
                                .or(item.category.name.containsIgnoreCase(keyword)))
                        ).fetch();
        return resultQuery;
    }
}
