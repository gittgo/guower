package com.ourslook.guower.utils.beanmapper;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * <p>
 * 1. 持有Mapper的单例. 2. 返回值类型转换. 3. 批量转换Collection中的所有对象. 4.
 * 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 * <p>
 * DozerBeanMapper对象之间相同属性名赋值 http://blog.csdn.net/luo201227/article/details/24021791
 *
 * @author calvin
 */
public abstract class DozerBeanMapperImpl implements BeanMapper {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 基于Dozer转换对象的类型.
     */
    public <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public <T> List<T> mapList(
            @SuppressWarnings("rawtypes") Collection sourceList,
            Class<T> destinationClass) {
        List<T> destinationList = new ArrayList<T>();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     * src 和 dest 对象均不能为空
     */
    @Override
    public Object copy(Object source, Object destinationObject) {
        Objects.requireNonNull(source, "原对象不能为空！");
        Objects.requireNonNull(destinationObject, "目标对象不能为空！");
        dozer.map(source, destinationObject);
        return destinationObject;
    }

    @Override
    public <S, D> void copy(S src, D target, Class<S> srcClass, Class<D> targetClass) {

    }
}