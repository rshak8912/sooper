package com.simgok.sooper.repositories;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.simgok.sooper.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemRepositoryExtensionImpl extends QuerydslRepositorySupport implements ItemRepositoryExtension {


    public ItemRepositoryExtensionImpl() {
        super(Item.class);
    }



    @Override
    public Page<Item> findByKeyword(String keyword, Pageable pageable) {
        QItem item = QItem.item;
        JPQLQuery<Item> query = from(item)
                .where(item.stockQuantity.goe(1)
                .and(item.name.containsIgnoreCase(keyword)
                        .or(item.category.name.containsIgnoreCase(keyword)
                        )));
        JPQLQuery<Item> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Item> fetchResults = pageableQuery.fetchResults();

        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
    @Override
    public Page<Item> findAll(Pageable pageable) {
        QItem item = QItem.item;
        JPQLQuery<Item> query = from(item)
                .where(item.stockQuantity.goe(1));
        JPQLQuery<Item> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Item> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}
