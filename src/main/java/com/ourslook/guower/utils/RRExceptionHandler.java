package com.ourslook.guower.utils;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.ourslook.guower.utils.result.R;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 异常处理器
 * <p>
 * 使用Spring MVC的@ControllerAdvice注解做Json的异常处理
 * http://blog.csdn.net/z69183787/article/details/52290057
 *
 * @see RRException
 */
@RestControllerAdvice
public class RRExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        logger.error(e.getMessage(), e);
        /**
         * 要打印堆栈信息，否则根据看不出来错误原因，一般业务逻辑不要直接抛出异常
         */
        e.printStackTrace();
        return R.error(e);
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(RRException.class)
    public R handleRRException(RRException e) {
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        logger.error(e.getMessage(), e);
        e.printStackTrace();
        return r;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return R.error("数据库中已存在该记录");
    }

    @ExceptionHandler(AuthorizationException.class)
    public R handleAuthorizationException(AuthorizationException e) {
        logger.error(e.getMessage(), e);
        return R.error("没有权限，请联系管理员授权");
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public R handleJSONConvertException(HttpMessageConversionException jsonEx) {    //JSON格式转换异常
        /**
         * 要打印堆栈信息，否则根据看不出来错误原因，一般业务逻辑不要直接抛出异常
         */
        jsonEx.printStackTrace();
        logger.error("JSON格式转换异常,1：前台json属性key和后台属性不一致,或者大小写不一致 或者输入的类型不能正常转换为后台类型,如后台需要Number类型，结果您输入的是字符串、后台需要日期，您乱输入的日期不能被转换为日期 ;\n 2:如果使用ajax没有使用JSON.stringify把对象序列化为json字符串" + jsonEx);
        return R.error("JSON格式转换异常,1：前台json属性key和后台属性不一致,或者大小写不一致 或者输入的类型不能正常转换为后台类型 ;\n 2:如果使用ajax没有使用JSON.stringify把对象序列化为json字符串;\n ");
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public R handleRequestException(ServletRequestBindingException ex) {        //请求参数异常
        logger.error("请求参数异常(5)：" + ex.getMessage(), ex);
        String errorMsg = ex.getLocalizedMessage();
        if (ex instanceof MissingServletRequestParameterException) {
            errorMsg = "paramter参数缺失异常,请检查参数名称大小写，有无空格等：" + errorMsg;
        } else if (ex instanceof MissingPathVariableException) {
            errorMsg = "path参数缺失异常：" + errorMsg;
        }
        return R.error(errorMsg);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public R handleTypeMismatchException(ConversionFailedException ex) {
        logger.error("spring类型转换异常 ConversionFailedException,(6)，请自定义类型转换器或者用@DateTimeFormat注解处理" + ex.getMessage(), ex);
        return R.error("spring类型转换异常 ConversionFailedException,(6)，请自定义类型转换器或者用@DateTimeFormat注解处理" + ex.getLocalizedMessage());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public R handleTypeMismatchException(TypeMismatchException ex) {            //方法参数异常
        String errorMsg = "类型参数异常(7)，请检查类型,前后传入的参数类型是否正确 或者后台的默认值类型取值有问";
        String numError = "java.lang.NumberFormatException";
        if ((ex.getMessage() + "").contains(numError)) {
            errorMsg += ";输入参数不能转换为指定的number类型";
        }
        errorMsg += ";" + ex.getMessage();
        logger.error(errorMsg, ex);
        return R.error(errorMsg);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public R handleHttpMediaTypeException(HttpMediaTypeException ex) {            //方法参数异常
        logger.error("HttpMediaType异常(8)，请检查" + ex.getMessage(), ex);
        return R.error("HttpMediaType异常(8)，请检查" + ex.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public R handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {            //
        logger.error("InvalidDataAccessApiUsageException异常(9)，请检查" + ex.getMessage(), ex);
        return R.error("InvalidDataAccessApiUsageException错误，1:请检测自己的jsonFilter，是否包含空格等其符号 2:检查jpa @Modifying是否书写正确 3：其他,如查询jpa参数异常;详细信息:" + ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public R handleRequestException(MissingServletRequestPartException ex) {        //请求参数异常
        logger.error("请求参数缺失(10)：" + ex.getMessage(), ex);
        String errorMsg = "请求参数缺失" + ex.getMessage();
        return R.error(errorMsg);
    }


    /**
     * @see org.apache.tomcat.jdbc.pool.PoolExhaustedException
     * @see DataSourceProperties
     * <p>
     * unable to fetch a connection in 100 seconds, none available[size:100；busy:100；idle:0； lastwait：10000
     */
    @ExceptionHandler(SQLException.class)
    public R handleSQLException(SQLException ex) {
        logger.error("SQLException(10)，请检查" + ex.getMessage(), ex);
        return R.error("SQLException,sql异常，1：可能是数据库语法错误 2：sql数据库连接池获取异常:\" + ex.getMsg()");
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public R handleSQLException(BadSqlGrammarException ex) {
        /**
         * 要打印堆栈信息，否则根据看不出来错误原因，一般业务逻辑不要直接抛出异常
         */
        ex.printStackTrace();
        if (ex.getLocalizedMessage().contains("this is incompatible with sql_mode=only_full_group_by")) {
            logger.error("BadSqlGrammarException(101),请查看: https://blog.csdn.net/qq_34707744/article/details/78031413，请检查" + ex.getMessage(), ex);
        } else {
            logger.error("BadSqlGrammarException(101)，请检查" + ex.getMessage(), ex);
        }
        return R.error("BadSqlGrammarException,sql异常，1：可能是数据库语法错误 2：sql数据库连接池获取异常:\" + ex.getMsg()");
    }


    /**
     * 请查看sql字段是否写错，是否缺少， 等等语法错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(UncategorizedSQLException.class)
    public R handleSqlException(UncategorizedSQLException e) {
        logger.error("1：如新增字段是否少了粘贴了逗号; 2:关联查询，每个字段都要加表前缀等..............................;3:项目不支持emoj表情"
                + e.getMessage(), e);
        String value = "Incorrect string value";
        String sqlInjectValue = "sql injection violation,";
        if (e.getLocalizedMessage().toLowerCase().contains(value)) {
            return R.error("项目暂时不支持emoj表情");
        } else
        if (e.getLocalizedMessage().toLowerCase().contains(sqlInjectValue)) {
            return R.error("可能有SQL注入的风险，不要直接在代码里面写入：1=1这种SQL; 一般都是sql写错了");
        }else {
            return R.error("SQL语法错误，请检查SQL！");
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public R handleSqlException(DataIntegrityViolationException e) {
        logger.error("=========================== 关联查询的时候记得每个字段前面写表的别名，否则多张表一样的字段就会报错.. =========================== ");
        /**
         * 要打印堆栈信息，否则根据看不出来错误原因，一般业务逻辑不要直接抛出异常
         */
        e.printStackTrace();
        return R.error(e.getLocalizedMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public R handleMultipartException(MultipartException e) {
        logger.error(e.getMessage(), e);

        return R.error("MultipartException:上次文件大小限制！" + e.getLocalizedMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleMultipartException(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return R.error("请求方法不支持，请仔细对照文档查看请求方式:");
    }

    @ExceptionHandler(NoSuchMessageException.class)
    public R handleMultipartException(NoSuchMessageException e) {
        logger.error(e.getMessage(), e);
        return R.error("国际化配置有误，请检查属性文件格式是否正确，或者对应code是否存在！");
    }
}
