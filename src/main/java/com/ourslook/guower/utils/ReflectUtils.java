package com.ourslook.guower.utils;


import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * 反射的 Utils 函数集合
 * 提供访问私有变量, 获取泛型类型 Class, 提取集合中元素属性等 Utils 函数
 * Created by duandazhi on 2016/12/25.
 */
@SuppressWarnings("all")
public class ReflectUtils {

    public static String getGetterMethodName(Object target, String fieldName)
            throws NoSuchMethodException {
        String methodName = "get" + StringUtils.capitalize(fieldName);

        try {
            target.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException ex) {
            methodName = "is" + StringUtils.capitalize(fieldName);

            target.getClass().getDeclaredMethod(methodName);
        }

        return methodName;
    }

    /**
     * get method value by name.
     *
     * @param target
     *            Object
     * @param methodName
     *            method name
     * @return object
     * @throws Exception
     *             ex
     */
    public static Object getMethodValue(Object target, String methodName)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Method method = target.getClass().getDeclaredMethod(methodName);

        return method.invoke(target);
    }

    /**
     * 将反射时的 "检查异常" 转换为 "运行时异常"
     *
     * @return
     */
    public static IllegalArgumentException convertToUncheckedException(Exception ex) {
        if (ex instanceof IllegalAccessException || ex instanceof IllegalArgumentException
                || ex instanceof NoSuchMethodException) {
            throw new IllegalArgumentException("反射异常", ex);
        } else {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * 转换字符串类型到 toType 类型, 或 toType 转为字符串
     *
     * @param value:  待转换的字符串
     * @param toType: 提供类型信息的 Class, 可以是基本数据类型的包装类或指定格式日期型
     * @return
     */
   /* public static Object convertValue(Object value, Class<?> toType) {
        try {
            DateConverter dc = new DateConverter();

            dc.setUseLocaleFormat(true);
            dc.setPatterns(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});

            ConvertUtils.register(dc, Date.class);

            return ConvertUtils.convert(value, toType);
        } catch (Exception e) {
            e.printStackTrace();
            throw convertToUncheckedException(e);
        }
    }*/

    /**
     * 提取集合中的对象的属性(通过 getter 方法), 组成 List
     *
     * @param collection:   来源集合
     * @param propertyName: 要提取的属性名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List fetchElementPropertyToList(Collection collection, String propertyName) {
        List list = new ArrayList();

        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            convertToUncheckedException(e);
        }

        return list;
    }

    /**
     * 提取集合中的对象属性(通过 getter 函数), 组合成由分隔符分隔的字符串
     *
     * @param collection:   来源集合
     * @param propertyName: 要提取的属性名
     * @param seperator:    分隔符
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String fetchElementPropertyToString(Collection collection, String propertyName,
                                                      String seperator) {
        List list = fetchElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, seperator);
    }

    /**
     * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param clazz
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperGenericType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                //superClass.getMethod(methodName, parameterTypes);
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                //Method 不在当前类定义, 继续向上转型
            }
            //..
        }

        return null;
    }

    /**
     * 使 filed 变为可访问
     *
     * @param field
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object
     * @param filedName
     * @return
     */
    public static Field getDeclaredField(Object object, String filedName) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(filedName);
            } catch (NoSuchFieldException e) {
                //Field 不在当前类定义, 继续向上转型
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected)
     *
     * @param object
     * @param methodName
     * @param parameterTypes
     * @param parameters
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) throws InvocationTargetException {

        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {

        }

        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {

        }
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;

        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {

        }

        return result;
    }

    /**
     * 默认获取所有的属性
     * @param clazz
     * @return
     */
    public static Map<String, Object> getDeclaredFields(final Class<?> clazz) {
        return getDeclaredFields(clazz, new ReflectionUtils.FieldFilter() {
            @Override
            public boolean matches(Field field) {
                int mo = field.getModifiers();    // 得到修饰符的数字
                Class<?> r = field.getType();// 得到属性类型
                String priv = Modifier.toString(mo); // 还原修饰符 public static final
                r.getName();// 得到属性类型
                field.getName();// 输出属性名称
                return Modifier.isPublic(mo)||Modifier.isProtected(mo)||Modifier.isPrivate(mo);
            }
        });
    }

    /**
     * 获取一个类的所有属性
     *
     * @param clazz  类,不能为空
     * @param filter 过滤类,为空,默认过滤(static public)
     * @return
     * @author duandazhi
     * @date 2016/12/26 上午9:15
     */
    public static Map<String, Object> getDeclaredFields(final Class<?> clazz, ReflectionUtils.FieldFilter filter) {
        final Map<String, Object> publicPropety = new HashMap<>();
        Objects.requireNonNull(clazz, "clazz不能为空 in getDeclaredFields");

        if (filter == null) {
            filter = new ReflectionUtils.FieldFilter() {
                @Override
                public boolean matches(Field field) {
                    int mo = field.getModifiers();    // 得到修饰符的数字
                    Class<?> r = field.getType();// 得到属性类型
                    String priv = Modifier.toString(mo); // 还原修饰符 public static final
                    r.getName();// 得到属性类型
                    field.getName();// 输出属性名称
                    boolean fieldStatic = Modifier.isStatic(mo);
                    return Modifier.isPublic(mo) && fieldStatic;
                }
            };
        }
        try {
            final Object obj = clazz.newInstance();
            //取得本类中所有的属性
            ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    String name = field.getName();
                    ReflectionUtils.makeAccessible(field);
                    Object val = field.get(obj);
                    publicPropety.put(name, val);
                }
            }, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicPropety;
    }

    /**
     * 判断一个number是否一个类的一个属性值
     *
     * @param clazz
     * @param number
     * @return
     * @author duandazhi
     * @date 2016/12/26 上午9:15
     * <p>
     * test System.out.printf(numberInClass(Constant.Order_CommentType.class, -1) + "");
     */
    public static boolean numberInClass(final Class<?> clazz, Number number) {
        Objects.requireNonNull(clazz, "baseEntity不能为空 in ReflectUtils");
        Objects.requireNonNull(number, "number不能为空 in numberInClass");
        Map<String, Object> publicPropety = getDeclaredFields(clazz, null);
        for (Map.Entry<String, Object> en : publicPropety.entrySet()) {
            Number val = NumberUtils.createNumber(en.getValue().toString());
            String key = en.getKey();
            if (number instanceof Long || number instanceof Integer || number instanceof Short) {
                if (number.longValue() == val.longValue()) {
                    return true;
                }
            } else if (number instanceof Double || number instanceof Float) {
                if (number.doubleValue() == val.doubleValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean numberInClass(final Class<?> clazz, String number) {
        Objects.requireNonNull(clazz, "baseEntity不能为空 in ReflectUtils");
        Objects.requireNonNull(number, "number不能为空 in numberInClass");
        Map<String, Object> publicPropety = getDeclaredFields(clazz, null);
        for (Map.Entry<String, Object> en : publicPropety.entrySet()) {
            String val = en.getValue().toString();
            String key = en.getKey();
            if (number.equalsIgnoreCase(val)) {
                return true;
            }
        }
        return false;
    }

  /**
     * 把一个常量类转出Map<常量值，常量的中文注释>
     * System.out.printf(getHibernateColumnAnnotationLength(User.class, "mobile") + "");
     * {@link com.ourslook.guower.utils.Constant.ConstantName}
     * @param clazz
     * @author duandazhi
     * @date 2016/12/26 上午9:15
     * @return
     */
    public static Map<Integer,String> covetConstaintClassToMap(Class clazz) throws Exception {
        Objects.requireNonNull(clazz, "clazz能为空 in ReflectUtils");
        Field[] fields = clazz.getDeclaredFields();
        Map<Integer,String> filesMap = new HashMap<>();
        final Object obj = clazz.newInstance();
        for (int i = 0;  fields != null && i < fields.length; ++i) {
            Field field = fields[i];
            Constant.ConstantName constantNameAnnotation = field.getAnnotation(Constant.ConstantName.class);
            if (constantNameAnnotation != null) {
                //获取属性注解的值
                String anoValue = constantNameAnnotation.value();
                //获取属性的值
                String fieldName = field.getName();
                Object objectValue = getFieldValue(obj, fieldName);
                int proValue = NumberUtils.createNumber(objectValue.toString()).intValue();
                //1-->商品
                //2-->旅游
                filesMap.put(proValue, anoValue);
            } else {
                throw new  RuntimeException("covetConstaintClassToMap class的属性必须被Constant.ConstantName注解修饰！");
            }
        }
        return filesMap;
    }

    /**
     * @see
     * @return
     * @throws Exception
     */
    public static String getContantName(Class clazz, Integer constantVal) throws Exception{
        Map<Integer,String> map =  covetConstaintClassToMap(clazz);
        String name = "";
        if (map != null && constantVal != null) {
            name = map.get(constantVal);
        }
        return name;
    }

//
//    public static void main(String[] args) throws Exception {
//        ReflectUtils.getHibernateColumnAnnotationLength(FranchiseTable.class, "franchise_Unit_Name");
//        Map map = ReflectUtils.covetConstaintClassToMap(Constant.Order_CommentType.class);
//        System.out.print(map);
//
//            Map map = ReflectUtils.getDeclaredFields(Permission.class);
//            System.out.print(map);
//    }
}
