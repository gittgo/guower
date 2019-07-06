package com.ourslook.guower.utils;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.collection.TransformedCollection;
import org.apache.commons.collections.functors.StringValueTransformer;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 * Created by duandazhi on 2016/12/20.
 * 集合处理
 */
@SuppressWarnings("all")
public class CollectionUtils {
    /**
     * 求一个集合的属性的子集合
     * 比如一个学生集合,我要吧学号在单独作为一个集合
     * eg: List<Long> studentIds =  XaUtil.subCollection(studenVOs, "id")
     *
     * @param srcCollection    集合名称
     * @param beanPropertyName 属性名称;实际是get方法的名称; isComm 就传递get方法对应的名称去掉get
     * @param <T>
     * @param <S>
     * @return 可能是null
     * @author duandazhi
     * @date 2016/12/6 下午3:00
     */
    public static <T, S> List<S> subCollection(Collection<T> srcCollection, String beanPropertyName) throws RuntimeException {
        Objects.requireNonNull(srcCollection, "srcCollection not null ");
        Objects.requireNonNull(beanPropertyName, "beanPropertyName not null");
        try {
            List<S> list = new ArrayList<>();
            for (T t : srcCollection) {
                S propertyValue = (S) PropertyUtils.getProperty(t, beanPropertyName);
                list.add(propertyValue);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("对象属性未找到:" + beanPropertyName);
        }
    }

    /**
     * 求一个集合的属性的子集合
     * 同上方法
     *
     * @return
     */
    public static <T, S> Set<S> subCollectionSet(Collection<T> srcCollection, String beanPropertyName) throws RuntimeException {
        List list = subCollection(srcCollection, beanPropertyName);
        if (list == null || list.size() == 0) {
            return new HashSet<>();
        }
        return Sets.newHashSet(list);
    }

    /**
     * 集合 转成 map
     * 返回  Map<String,TbUserVo>
     * 模仿Guava的Maps方法,解决此方法里面当元素有重复值的时候比报错
     *
     * @see com.google.common.collect.Maps 的 listToMap 方法
     */
    public static <K, V> Map<K, V> listToMap(Iterator<V> values, Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        Map<K, V> map = new HashMap<>();
        while (values.hasNext()) {
            V duplicateKeys = values.next();
            map.put(keyFunction.apply(duplicateKeys), duplicateKeys);
        }
        return map;
    }

    /**
     * 集合转map;返回类似
     * 底层用ArrayList实现
     * Map<String,Collection<TbUserVo>>
     *
     * @return
     */
    public static <K, V> Multimap<K, V> listToMulMap(Iterator<V> values, Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        Multimap<K, V> map = ArrayListMultimap.create();
        while (values.hasNext()) {
            V duplicateKeys = values.next();
            map.put(keyFunction.apply(duplicateKeys), duplicateKeys);
        }
        return map;
    }

    /**
     * 过滤集合,返回过滤后的子集合
     *
     * @param srcCollection
     * @param predicate
     */
    public static <T> List filterCopy(Collection<T> srcCollection, Predicate predicate) {
        ArrayList<T> copyList = Lists.newArrayList(srcCollection);
        //注意copyList必须srcList长度相等
        if (copyList != null && predicate != null) {
            Iterator it = copyList.iterator();
            while (it.hasNext()) {
                if (!predicate.evaluate(it.next())) {
                    it.remove();
                }
            }
        }
        return copyList;
    }

    /**
     * 装饰类
     * Collection<String> 转 Collection<Long>
     *
     * @param collection 不能为空
     * @param type
     * @return
     */
    public static <T, S> Collection<S> transformedCollectionForNumber(Collection<T> collection, Class<S> type) {
        Transformer transformer = new StringToNumberTransformer(type);
        Collection<S> typedCollection = org.apache.commons.collections.CollectionUtils.transformedCollection(collection, transformer);
        TransformedCollection transColl = (TransformedCollection) TransformedCollection.decorate(new ArrayList(), transformer);
        //注意这一句才真正开始转换
        transColl.addAll(typedCollection);
        List<S> newList = Lists.newArrayList(transColl.iterator());
        if (collection instanceof List) {
            return newList;
        } else if (collection instanceof Set) {
            Set set = Sets.newHashSet(newList);
            return set;
        } else {
            return newList;
        }
    }

    /**
     * 装饰类
     * Collection<Student> 转 Collection<StudentVo>
     *
     * @param collection
     * @param transformer
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> Collection<S> transformedCollection(Collection<T> collection, Transformer transformer) {
        Collection<S> typedCollection = org.apache.commons.collections.CollectionUtils.transformedCollection(collection, transformer);
        TransformedCollection transColl = (TransformedCollection) TransformedCollection.decorate(new ArrayList(), transformer);
        //注意这一句才真正开始转换
        transColl.addAll(typedCollection);
        List<S> newList = Lists.newArrayList(transColl.iterator());
        if (collection instanceof List) {
            return newList;
        } else if (collection instanceof Set) {
            Set set = Sets.newHashSet(newList);
            return set;
        }
        return newList;
    }


    /**
     * 参见{@link BeanToPropertyValueTransformer},对象只取单个属性
     * 参见{@link StringValueTransformer},对象转出string
     * 字符串转Number
     */
    public static class StringToNumberTransformer implements Transformer {

        private Class numberType;

        public StringToNumberTransformer(Class numberType) {
            this.numberType = numberType;
            if (this.numberType == null) {
                throw new IllegalArgumentException("numberType不能为空(" + this.getClass().getName() + ")");
            }
            if (!(numberType == Long.class || numberType == Integer.class
                    || numberType == Short.class
                    || numberType == Double.class
                    || numberType == Float.class)) {
                throw new IllegalArgumentException("numberType=" + numberType + "类型暂不支持,待完善");
            }
        }

        @Override
        public Object transform(Object input) {
            if (this.numberType == Long.class) {
                return NumberUtils.toLong(input + "");
            } else if (this.numberType == Integer.class) {
                return NumberUtils.toInt(input + "");
            } else if (this.numberType == Short.class) {
                return NumberUtils.toInt(input + "");
            } else if (this.numberType == Double.class) {
                return NumberUtils.toInt(input + "");
            } else if (this.numberType == Float.class) {
                return NumberUtils.toInt(input + "");
            }
            return null;
        }
    }

    /***
     * 组合数组
     * @param <T>
     * @param llist
     * //@param comb
     * //@param str
     */
    public static <T> void comb(List<List<T>> llist , List<T> firstList , List<T> temp ,  List<List<T>> combList){

        for(int i = 0  ; i < llist.size() ; i++){

            if(llist.indexOf(firstList) == i){

                for(T l: firstList){

                    List<T> comb = new ArrayList<T>(0);

                    for(T s : temp){
                        comb.add(s);
                    }
                    comb.add(l);

                    if(i < llist.size()-1){

                        comb(llist , llist.get(i+1) , comb , combList);

                    }else if(i == llist.size()-1){

                        combList.add(comb);
                    }
                }
            }
        }
    }


//    public static void main(String[] args) {
    //Collection collection  = transformedCollection(Lists.newArrayList("1", "2", "3", "5"), Long.class);
    //Collection collection  = transformedCollection(Sets.newHashSet("1", "2", "3", "5"), Long.class);
    //System.out.print(collection);
//    }
}
