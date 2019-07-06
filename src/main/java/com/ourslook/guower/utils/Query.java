package com.ourslook.guower.utils;

import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.XaResult;
import com.ourslook.guower.utils.xss.SQLFilter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;


/**
 * 查询参数
 * <p>
 * ！！！如果查询中用到类似 in 、NE 等，统一使用类似 关键属性_LIKE 进行处理
 *
 * <p>
 * <p>
 * 目前的查询参数只支持 == 和 like, 下面的各种操作符都不支持
 * fieldName  value  operator
 * <p>
 * LIKEIGNORE 模糊查找不区分大小写
 * LIKE 模糊查找区分大小写
 * EQ,IN,NOT_IN ,LIKE,LIKEIGNORE,LIKEPINYIN,GT,LT,GTE,LTE,NE,BETWEEN,ISNULL
 * EQ等于,IN包含,LIKE,LIKEIGNORE(是一个闭区间),LIKEPINYIN 忽略大小写、中文和拼音,GT大于,LT小于,GTE大于等于,LTE小于等于,NE不等于,非like;ISNULL空,
 * IN 是一个集合,List or Set 搜 IN(传递集合对象)
 * <p>
 * mysql: columename1 = 1 和 columename1 = '1' 是一样，所以一般的字段,统一使用字符串类型
 * <p>
 * eg:userid_IN、orderstatus_NOT_IN、starttime_BETWEEN 等类似这种的
 *
 * @see R
 * @see XaResult
 */
public class Query extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    @Value("${spring.datasource.dbType:#{null}}")
    private String dbType;

    public static final int PAGE_DEFAULT = 1;

    //当前页码,默认从1开始
    private int page;
    //每页条数
    private int limit;

    public enum Operator {
        EQ, IN, ISNULL, LIKE, GT, LT, GTE, LTE, NE, LIKEIGNORE, BETWEEN, NOTNULL
    }

    public Query() {
        this.putAll(getQueryDefaultParams());
        //设置分页参数
        this.setPageParam();
    }

    /**
     * 注意pageCurrent 最小是1
     * @param pageCurrent 当前页，最小是1；
     * @param pageSize
     */
    public Query(int pageCurrent, int pageSize) {
        if (pageCurrent < PAGE_DEFAULT) {
            throw new RRException("pageCurrent最小是1");
        }
        if (pageSize < 1) {
            throw new RRException("pageSize最小是1");
        }
        this.page = pageCurrent;
        this.limit = pageSize;
        //设置分页参数
        if (this.get("page") == null && this.get("limit") == null) {
            this.getQueryDefaultParams();
        } else {
            this.setPageParam();
        }
    }

    public Query(Map<String, Object> params) {
        this.putAll(params);
        //设置分页参数
        if (this.get("page") == null && this.get("limit") == null) {
            this.limit = Short.MAX_VALUE;
            this.page = PAGE_DEFAULT;
            this.getQueryDefaultParams();
        } else {
            this.setPageParam();
        }
    }

    private void setPageParam() {
        //分页参数
        this.page = NumberUtils.toInt(this.get("page") + "", 0);
        this.limit = NumberUtils.toInt(this.get("limit") + "", 0);
        if (this.limit == 0 && this.page == 0) {
            this.page = PAGE_DEFAULT;
            this.limit = Short.MAX_VALUE;
        }
        this.put("offset", Math.abs((page - 1) * limit));
        this.put("page", page);
        this.put("limit", page * limit + 1);

        //排序参数
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = XaUtils.getNutNullStr(this.get("sidx"), "");
        String order = XaUtils.getNutNullStr(this.get("order"), "");
        this.put("sidx", SQLFilter.sqlInject(sidx));
        this.put("order", SQLFilter.sqlInject(order));
    }

    private Map<String, Object> getQueryDefaultParams() {
        Map<String, Object> params = new HashMap<>();
        //默认第一页
        params.put("page", this.page);
        //默认取所有数据
        params.put("limit", this.limit);

        this.put("offset", Math.abs((page - 1) * limit));
        this.put("limit", page * limit);

        params.put("_search", false);
        params.put("username", "");
        params.put("nd", "");
        params.put("_", "");
        params.put("sidx", "");
        params.put("order", "asc");
        return params;
    }

    /**
     * 段大志添加，用来处理如：BETWEEN这种情况
     *
     * @param m
     */
    @Override
    public void putAll(Map<? extends String, ?> m) {
        if (m != null && m.size() != 0) {
            for (String key : m.keySet()) {
                this.put(key, m.get(key));
            }
        }
    }

    /**
     * 如果是IN、NOT IN ，value 是集合,而不是,分割的字符串
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public Object put(String key, Object values) {
        if (values != null && values instanceof String) {
            values = SQLFilter.sqlInject(values.toString().trim());
        }
        if (values != null && values instanceof Number) {
            if (!"offset".equalsIgnoreCase(key) && !"limit".equalsIgnoreCase(key)) {
                values = String.valueOf(values);
            }
        }
        if (values != null && values instanceof Collection) {
            if (XaUtils.isEmpty(values)) {
                values = null;
            }
        }
        //处理时间 Between 的形式
        if (key.endsWith("_BETWEEN") && XaUtils.isNotEmpty(values)) {
            String value = String.valueOf(values);
            //2018-05-31 - 2018-05-31
            String value1 = value.split(" - ")[0];
            String value2 = value.split(" - ")[1];

            //如果是_BETWEEN 并且是 时间
            if (DateUtils.isValidDate(value1) && DateUtils.isValidDate(value2)) {
                String keyEnd = key + "_2end";
                String keyStart = key + "_1start";

                this.put(keyStart, DateUtils.parseDateTimeObj(value1 + " 00:00:00", DateUtils.YYYYMMDDHHMMSS));
                this.put(keyEnd, DateUtils.parseDateTimeObj(value2 + " 23:59:59", DateUtils.YYYYMMDDHHMMSS));
            }
        }
        return super.put(key, values);
    }

    @Override
    public Object get(Object key) {
        if ("page".equalsIgnoreCase(key.toString())) {
            if (this.page != 0) {
                return this.page;
            }
        }
        if ("limit".equalsIgnoreCase(key.toString())) {
            if (this.limit != 0) {
                return this.limit;
            }
        }
        return super.get(key);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
