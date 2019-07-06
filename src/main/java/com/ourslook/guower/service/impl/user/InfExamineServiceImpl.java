package com.ourslook.guower.service.impl.user;

import com.ourslook.guower.dao.user.InfExamineDao;
import com.ourslook.guower.dao.user.UserDao;
import com.ourslook.guower.entity.user.ExamineCheck;
import com.ourslook.guower.entity.user.InfExamineEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.user.InfExamineService;
import com.ourslook.guower.service.user.UserService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.DateUtils;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.office.csvexports.CsvExportor;
import com.ourslook.guower.utils.office.excelutils.ExcelColumCellInterface;
import com.ourslook.guower.utils.office.excelutils.ExcelColumn;
import com.ourslook.guower.utils.office.excelutils.ExcelHead;
import com.ourslook.guower.utils.office.excelutils.ExcelHelper;
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
@Service("infExamineService")
public class InfExamineServiceImpl implements InfExamineService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private InfExamineDao infExamineDao;
    @Autowired
    private UserDao userDao;
    @Resource
    private BeanMapper beanMapper;
    @Resource
    private CsvExportor csvExportor;
    @Resource
    private MessageSource messageSource;
    @Autowired
    private UserService userService;

    /**
     * 认证审核（同步修改用户认证级别）
     * @param examineCheck	审核结果实体类
     */
    @Override
    public void examine(ExamineCheck examineCheck) {
        infExamineDao.examine(examineCheck);
        //同步到用户等级(认证通过)
        if(examineCheck.getResult() == Constant.ExamineType.TYPE_EXAMINE_PASS){
            for (Integer id : examineCheck.getIds()){
                InfExamineEntity infExamineEntity = queryObject(id);
                UserEntity userEntity = userDao.queryObject(infExamineEntity.getUserId());
                userEntity.setUserLevel(infExamineEntity.getUserType());
                userDao.update(userEntity);
            }
        }
    }

    @Override
    public InfExamineEntity queryObject(Integer id) {
        return infExamineDao.queryObject(id);
    }

    @Override
    public List<InfExamineEntity> queryList(Map<String, Object> map) {
        return infExamineDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return infExamineDao.queryTotal(map);
    }

    @Override
    public void save(InfExamineEntity infExamine) {
        infExamineDao.save(infExamine);
    }

    @Override
    public void multiOperate(List<String> modelIds, Integer status) {
//        if (XaUtils.isNotEmpty(modelIds)) {
//            status = status == null ? Constant.Status.delete : status;
//            Query query = new Query();
//            query.put("id_IN", modelIds);
//            List<InfExamineEntity> entities =  infExamineDao.queryList(query);
//            for (int i = 0, count = entities.size(); i < count; ++i) {
//                InfExamineEntity entity = entities.get(i);
//                entity.setState(status);
//                infExamineDao.update(entity);
//            }
//        }
    }

    @Override
    public void update(InfExamineEntity infExamine) {
        Objects.requireNonNull(infExamine);
        final InfExamineEntity dest = new InfExamineEntity();
        Optional.ofNullable(infExamine.getId()).ifPresent(aInteger -> {
            InfExamineEntity dests = Optional.ofNullable(infExamineDao.queryObject(aInteger)).orElse(dest);
            beanMapper.copy(infExamine, dests);
            infExamineDao.update(dests);
        });
    }


    @Override
    public void delete(Integer id) {
        infExamineDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        infExamineDao.deleteBatch(ids);
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
    public void exportsToExcels(List<InfExamineEntity> vos, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception {

        //excel结构
        List<ExcelColumn> excelColumns = new ArrayList<>();

        excelColumns.add(new ExcelColumn(excelColumns.size(), "id", "编号", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userId", "用户id", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userType", "认证类型", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userName", "真实姓名", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userIdCard", "身份证号", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userTel", "手机号码", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userEmail", "邮箱", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userCertificatesImage", "证件照", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "enterpriseName", "企业名称", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "enterpriseIdCard", "企业证件号", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "enterpriseImage", "营业执照", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "result", "审核结果【1.通过  0.审核中  2.未通过】", CellType.NUMERIC));

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
                    "审核表-" + DateUtils.parseDateTime(new Date(), DateUtils.YYYYMMDDHHMMSSCn));
        } else {
            ExcelHelper.getInstanse().exportExcelFile(request, response, head, vos,
                    "审核表-" + DateUtils.parseDateTime(new Date(), DateUtils.YYYYMMDDHHMMSSCn));
        }

    }

    @Override
    public InfExamineEntity queryObjectByUserId(Integer userId) {
        return infExamineDao.queryObjectByUserId(userId);
    }

    @Override
    public void saveAndUpdateUser(InfExamineEntity examine) {
        examine.setCreateDate(LocalDateTime.now());
        examine.setResult(Constant.ExamineType.TYPE_EXAMINE_UNTREATED);
        this.save(examine);

        //切记要更新user表user_level
        UserEntity userEntity = userService.queryObject(examine.getUserId());
        userEntity.setUserLevel(examine.getUserType());
        userService.update(userEntity);
    }
}
