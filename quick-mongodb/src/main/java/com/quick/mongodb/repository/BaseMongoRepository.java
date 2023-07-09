package com.quick.mongodb.repository;

import com.quick.mongodb.domain.dto.Condition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
@NoRepositoryBean
public interface BaseMongoRepository<T, ID> extends MongoRepository<T, ID> {
    List<T> queryByCondition(Condition condition);
    long countByCondition(Condition condition);
    Page<T> queryByCondition(Condition condition, Pageable pageable);
    long updateById(ID id, Update update);
}