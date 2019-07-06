package com.ourslook.guower.service.impl.user;

import com.ourslook.guower.api.BusUtils;
import com.ourslook.guower.dao.user.UserDao;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.user.UserService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.office.csvexports.CsvExportor;
import com.ourslook.guower.utils.office.excelutils.ExcelColumCellInterface;
import com.ourslook.guower.utils.office.excelutils.ExcelColumn;
import com.ourslook.guower.utils.office.excelutils.ExcelHead;
import com.ourslook.guower.utils.office.excelutils.ExcelHelper;
import com.ourslook.guower.vo.business.AuthorSortVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserDao userDao;
    @Resource
    private BeanMapper beanMapper;
    @Resource
    private CsvExportor csvExportor;
    @Resource
    private MessageSource messageSource;

    @Override
    public UserEntity queryObject(Integer id) {
        return userDao.queryObject(id);
    }

    @Override
    public List<UserEntity> queryList(Map<String, Object> map) {
        return userDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userDao.queryTotal(map);
    }

    @Override
    public void save(UserEntity user) {
        userDao.save(user);
    }

    @Override
    public void multiOperate(List<String> modelIds, Integer status) {
        if (XaUtils.isNotEmpty(modelIds)) {
            status = status == null ? Constant.Status.delete : status;
            Query query = new Query();
            query.put("id_IN", modelIds);
            List<UserEntity> entities = userDao.queryList(query);
            for (int i = 0, count = entities.size(); i < count; ++i) {
                UserEntity entity = entities.get(i);
                entity.setState(status);
                userDao.update(entity);
            }
        }
    }

    @Override
    public void update(UserEntity user) {
        Objects.requireNonNull(user);
        final UserEntity dest = new UserEntity();
        Optional.ofNullable(user.getId()).ifPresent(aInteger -> {
            UserEntity dests = Optional.ofNullable(userDao.queryObject(aInteger)).orElse(dest);
            beanMapper.copy(user, dests);
            userDao.update(dests);
        });
    }


    @Override
    public void delete(Integer id) {
        userDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        userDao.deleteBatch(ids);
    }

    /**
     * 导出成为excel
     * <p>
     * 导出数据不正确的几种情况分析
     * 1：要检查CellType是否使用正确，如要显示字符串您确使用 CellType.NUMERIC
     * 2：去确定转换函数（excelColumnsConvertMap）的泛型，如数据是整数Map<Integer,String>您确使用了Map<String,String>
     */
    @Override
    @SuppressWarnings("all")
    public void exportsToExcels(List<UserEntity> vos, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception {

        //excel结构
        List<ExcelColumn> excelColumns = new ArrayList<>();

        excelColumns.add(new ExcelColumn(excelColumns.size(), "id", "", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userName", "用户名", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "password", "密码", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "tel", "手机号", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "nickName", "昵称", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "headPortrait", "用户头像", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "score", "积分", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userType", "用户类型【1.app  2.web】", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userLevel", "用户等级【1.企业认证  2.作者认证  3.媒体认证】", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "signature", "个性签名", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "sex", "性别【0.未知  1,男  2.女】", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "pushPosition", "推送位置【1.专栏作者  2.企业专栏  3.作者排行】", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "newsNumber", "文章数", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "totalBrowsing", "总浏览量", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "pushOneSort", "专栏作者排序值", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "pushTwoSort", "企业专栏排序值", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "pushThreeSort", "作者排行排序值", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "state", "状态", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "createDate", "暂留", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userRemarks2", "暂留", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userRemarks3", "暂留", CellType.STRING));

        // 需要特殊转换的单元 map的可以必须好实体的类型一致
        // 一下excelColumnsConvertMap 转换的根据实际情况使用，这里是示例
        Map<String, Map> excelColumnsConvertMap = new HashMap<>();

        Map<String, String> isReceive1 = new HashMap<>();
        /**
         * {@link Constant.OrderStatus}
         */
         /*
        isReceive1.put("0", "未支付");
        isReceive1.put("1", "已支付");
        isReceive1.put("2", "待收货");
        isReceive1.put("3", "已经消费/已经收货");
        isReceive1.put("4", "交易完成");
        isReceive1.put("5", "交易关闭");
        excelColumnsConvertMap.put("orderState", isReceive1);*/

        /**
         * {@link Constant.PayType}
         */
        /*Map<Integer, String> isReceive2 = new HashMap<>();
        try {
            Map<Integer, String> map = ReflectUtils.covetConstaintClassToMap(Constant.PayType.class);
            isReceive2.putAll(map);
            excelColumnsConvertMap.put("paymentKind", isReceive2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException(e.getLocalizedMessage());
        }*/

        //转换函数
        Map<String, ExcelColumCellInterface> excelColumnsConvertFunMap = new HashMap<>();
        //excelColumnsConvertFunMap.put("orderState", new ExcelHead.ExcelColumCellDate());
        //excelColumnsConvertFunMap.put("clearingmoney", new ExcelHead.ExcelColumCellBigDecimal("元"));

        //class类型抓换函数
        //Map<Class, ExcelColumCellInterface> columnsTypeConvertFunMap = new HashMap<>();
        //columnsTypeConvertFunMap.put(LocalDateTime.class, new ExcelHead.ExcelColumCellLocalDateTime());

        //设置头部
        ExcelHead head = new ExcelHead();
        head.setRowCount(1); // 模板中头部所占行数
        head.setColumns(excelColumns);  // 列的定义
        head.setColumnsConvertMap(excelColumnsConvertMap); // 列的转换byMap
        head.setColumnsConvertFunMap(excelColumnsConvertFunMap); // 列的转换by 接口方法
        //执行导出,第一个null是response参数，用来输出到浏览器，第二个null是要导出的数据集合
        if (isCvs) {
            ExcelHelper.getInstanse().exportCsvFile(csvExportor, request, response, head, vos,
                    "用户表-" + DateUtils.parseDateTime(new Date(), DateUtils.YYYYMMDDHHMMSSCn));
        } else {
            ExcelHelper.getInstanse().exportExcelFile(request, response, head, vos,
                    "用户表-" + DateUtils.parseDateTime(new Date(), DateUtils.YYYYMMDDHHMMSSCn));
        }

    }

    @Override
    public UserEntity queryByMobile(String mobile) {
        return userDao.queryByMobile(mobile);
    }

    @Override
    public UserEntity registUser(String mobile, String password, Integer userType) {
        String s = DigestUtils.sha256Hex(password);
        UserEntity userEntity = new UserEntity();
        userEntity.setState(Constant.Status.valid);
        userEntity.setCreateDate(LocalDateTime.now());
        userEntity.setUserName(mobile);
        userEntity.setNickName(mobile);
        userEntity.setTel(mobile);
        userEntity.setPassword(s);
        userEntity.setUserType(userType);
        userEntity.setScore(0);
        userEntity.setSex(0);
        userEntity.setUserLevel(0);
        userDao.save(userEntity);
        //这里要添加用户注册积分
        userEntity.setScore(BusUtils.getPlusScore(userEntity.getId(), Constant.ScoreGetType.TYPE_SCORE_GET_REGISTER));
        userDao.update(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity login(String mobile, String password) {
        UserEntity userEntity = this.queryByMobile(mobile);
        if (XaUtils.isEmpty(userEntity)) {
            throw new RRException("该用户不存在");
        }
        if (XaUtils.isEmpty(userEntity.getPassword())) {
            throw new RRException("第三方登录尚未设置密码，请设置密码或使用第三方登录！");
        }
        String s = DigestUtils.sha256Hex(password);
        if (!userEntity.getPassword().equals(s)) {
            throw new RRException("手机号或者密码错误");
        }
        if (XaUtils.isNotEmpty(userEntity.getState())){
            if (userEntity.getState() == Constant.Status.invalid) {
                throw new RRException("用户已被注销");
            }
            if (userEntity.getState() == Constant.Status.locked) {
                throw new RRException("用户已被被禁用");
            }
            if (userEntity.getState() == Constant.Status.delete) {
                throw new RRException("用户已经被删除");
            }
        }

        return userEntity;
    }

    @Override
    public void forgetPwd(String mobile, String newPassrod) {
        UserEntity userEntity = this.queryByMobile(mobile);
        if (XaUtils.isEmpty(userEntity)) {
            throw new RRException("该用户不存在");
        }
        String s = DigestUtils.sha256Hex(newPassrod);
        userEntity.setPassword(s);
        this.update(userEntity);
    }

    @Override
    public void updatePwd(Integer userId, String password) {
        UserEntity userEntity = this.queryObject(userId);
        if (XaUtils.isEmpty(userEntity)) {
            throw new RRException("用户不存在");
        }
        String s = DigestUtils.sha256Hex(password);
        userEntity.setPassword(s);
        this.update(userEntity);
    }

    @Override
    public List<AuthorSortVo> queryAuthorSort(Map<String, Object> map) {
        return userDao.queryAuthorSort(map);
    }
}
