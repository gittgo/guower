package com.ourslook.guower.utils.beanmapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;

import java.util.List;

/**
 * 复制对象属性的工具类.
 *
 * @author Lingo
 * <p>
 * http://www.jianshu.com/p/5daf68dc5758
 */
public class OrikaBeanMapperImpl implements BeanMapper {
    /**
     * 实例.
     */
    private static final MapperFacade MAPPER;

    static {
        // 如果src中属性为null，就不复制到dest
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
                .mapNulls(false).build();
        // 如果属性是Object，就只复制引用，不复制值，可以避免循环复制
        mapperFactory.getConverterFactory().registerConverter(
                new PassThroughConverter(Object.class));

        MAPPER = mapperFactory.getMapperFacade();
    }

    /**
     * 把src中的值复制到dest中.
     * src 和 dest 对象均不能为空
     */
    @Override
    public Object copy(Object src, Object dest) {
        MAPPER.map(src, dest);
        return dest;
    }

    /**
     * 指定复制的src和target的class.
     */
    @Override
    public <S, D> void copy(S src, D target, Class<S> srcClass,
                            Class<D> targetClass) {
        MAPPER.map(src, target, TypeFactory.valueOf(srcClass),
                TypeFactory.valueOf(targetClass));
    }

    /**
     * 复制list.
     */
    @Override
    public <S, D> List<D> copyList(List<S> src, Class<D> clz) {
        return MAPPER.mapAsList(src, clz);
    }
}
