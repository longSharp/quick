package com.quick.mongodb.repository;

import com.quick.mongodb.domain.dto.Condition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class BaseSimpleMongoRepository<T, ID> extends SimpleMongoRepository<T, ID> implements BaseMongoRepository<T, ID> {
    private MongoEntityInformation<T, ID> metadata;
    private MongoOperations mongoOperations;
    private Class<T> clazz;

     public BaseSimpleMongoRepository(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
         super(metadata, mongoOperations);
         this.mongoOperations = mongoOperations;
        this.metadata = metadata;
        this.clazz = metadata.getJavaType();
    }

    @Override
    public List<T> queryByCondition(Condition condition) {
        Query query = new Query(condition.get());
        return mongoOperations.find(query, clazz, metadata.getCollectionName());
    }

    @Override
    public long countByCondition(Condition condition) {
        return mongoOperations.count(new Query(condition.get()).limit(-1).skip(-1), clazz, metadata.getCollectionName());
    }

    @Override
    public Page<T> queryByCondition(Condition condition, Pageable pageable) {
        Query query = new Query(condition.get()).with(pageable);
        List<T> list = mongoOperations.find(query, clazz, metadata.getCollectionName());
        return PageableExecutionUtils.getPage(list, pageable, () -> mongoOperations.count(new Query(condition.get()).limit(-1).skip(-1), clazz, metadata.getCollectionName()));
    }

     @Override
    public long updateById(ID id, Update update) {
        if (update.getUpdateObject().isEmpty()) {
            return 0;
        }
        return mongoOperations.updateFirst(new Query(Criteria.where("_id").is(id)), update, metadata.getCollectionName()).getModifiedCount();
    }
}