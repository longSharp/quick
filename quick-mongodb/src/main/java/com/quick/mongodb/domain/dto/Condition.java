package com.quick.mongodb.domain.dto;

import com.quick.mongodb.common.enums.ConditionType;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Condition<T> {
    
    private Criteria criteria = new Criteria();

    public Condition where(String propertyName, Object value) {
        criteria.and(propertyName).is(value);
        return this;
    }

    public Condition where(String propertyName, ConditionType type, Object value) {
        Criteria andCondition = criteria.and(propertyName);
        invoke(andCondition,type.getType(),value);
        return this;
    }

    public Criteria get() {
        return criteria;
    }


    public void invoke(Object obj, String methodName, Object value){
        Class<?> objClass = obj.getClass();
        Method[] methods = objClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                try {
                    method.invoke(obj,value);
                    break;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
            }
        }
    }
}
