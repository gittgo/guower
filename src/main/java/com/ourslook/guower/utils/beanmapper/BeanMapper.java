package com.ourslook.guower.utils.beanmapper;

import java.util.List;

/**
 * 类型copy接口
 *
 * @author dazers
 * @date 2017/12/4 下午5:27
 * @see java.util.function.Function
 */
public interface BeanMapper {
    /**
     * 对象之间copy，可以是相同类型，也可以是不同类型
     *
     * @param src
     * @param dest
     * @return  dest
     */
    <D> D  copy(Object src, D dest);

    /**
     * copy集合
     * <p>
     * eg:List<BssOrderVo> bssOrderVos = beanMapper.copyList(bssOrders, BssOrderVo.class);
     *
     * @param src 原始集合
     * @param clz 要转换之后的类型
     * @param <S>
     * @param <D>
     * @return
     */
    <S, D> List<D> copyList(List<S> src, Class<D> clz);

    /**
     * @param src
     * @param target
     * @param srcClass
     * @param targetClass
     * @param <S>
     * @param <D>
     */
    <S, D> void copy(S src, D target, Class<S> srcClass,
                     Class<D> targetClass);
}
