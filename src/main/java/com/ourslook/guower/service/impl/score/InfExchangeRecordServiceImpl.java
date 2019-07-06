package com.ourslook.guower.service.impl.score;

import com.ourslook.guower.dao.score.InfCurrencyDao;
import com.ourslook.guower.dao.score.InfExchangeRecordDao;
import com.ourslook.guower.dao.score.InfScoreUsedDao;
import com.ourslook.guower.dao.user.UserDao;
import com.ourslook.guower.entity.score.InfCurrencyEntity;
import com.ourslook.guower.entity.score.InfExchangeRecordEntity;
import com.ourslook.guower.entity.score.InfScoreUsedEntity;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.score.InfCurrencyService;
import com.ourslook.guower.service.score.InfExchangeRecordService;
import com.ourslook.guower.service.user.UserService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.office.csvexports.CsvExportor;
import com.ourslook.guower.utils.office.excelutils.ExcelColumCellInterface;
import com.ourslook.guower.utils.office.excelutils.ExcelColumn;
import com.ourslook.guower.utils.office.excelutils.ExcelHead;
import com.ourslook.guower.utils.office.excelutils.ExcelHelper;
import com.ourslook.guower.vo.score.InfExchangeRecordVo;
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
@Service("infExchangeRecordService")
public class InfExchangeRecordServiceImpl implements InfExchangeRecordService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private InfExchangeRecordDao infExchangeRecordDao;
    @Resource
    private BeanMapper beanMapper;
    @Resource
    private CsvExportor csvExportor;
    @Resource
    private MessageSource messageSource;
    @Autowired
    private InfCurrencyService currencyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private InfScoreUsedDao infScoreUsedDao;
    @Autowired
    private InfCurrencyDao infCurrencyDao;

    /**
     * 兑换货币（生成兑换记录，更新用户积分，生成积分使用记录，更新货币库存）
     * @param infExchangeRecordEntity   兑换记录实体类，请自行检查
     */
    @Override
    public void exchangeCurrency(InfExchangeRecordEntity infExchangeRecordEntity) {
        //保存兑换记录
        save(infExchangeRecordEntity);
        //更新用户积分
        UserEntity userEntity = userDao.queryObject(infExchangeRecordEntity.getUserId());
        userEntity.setScore(userEntity.getScore()-infExchangeRecordEntity.getScore());
        userDao.update(userEntity);
        //生成积分使用记录
        InfScoreUsedEntity infScoreUsedEntity = new InfScoreUsedEntity();
        infScoreUsedEntity.setUserId(infExchangeRecordEntity.getUserId());
        infScoreUsedEntity.setScoreChange(-infExchangeRecordEntity.getScore());
        infScoreUsedEntity.setChangeType(0);
        infScoreUsedEntity.setCreateDate(LocalDateTime.now());
        infScoreUsedEntity.setChangeNote("兑换货币");
        infScoreUsedDao.save(infScoreUsedEntity);
        //更新货币库存
        InfCurrencyEntity infCurrencyEntity = infCurrencyDao.queryObject(infExchangeRecordEntity.getCurrencyId());
        infCurrencyEntity.setCount(infCurrencyEntity.getCount()-1);
        infCurrencyDao.update(infCurrencyEntity);
    }

    /**
     * 发放货币
     */
    @Override
    public void grant(Map<String, Object> map) {
        infExchangeRecordDao.grant(map);
    }

    @Override
    public List<InfExchangeRecordVo> queryVoList(Map<String, Object> map) {
        return infExchangeRecordDao.queryVoList(map);
    }

    @Override
    public InfExchangeRecordEntity queryObject(Integer id) {
        return infExchangeRecordDao.queryObject(id);
    }

    @Override
    public List<InfExchangeRecordEntity> queryList(Map<String, Object> map) {
        return infExchangeRecordDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return infExchangeRecordDao.queryTotal(map);
    }

    @Override
    public void save(InfExchangeRecordEntity infExchangeRecord) {
        infExchangeRecordDao.save(infExchangeRecord);
    }

    @Override
    public void multiOperate(List<String> modelIds, Integer status) {
        if (XaUtils.isNotEmpty(modelIds)) {
            status = status == null ? Constant.Status.delete : status;
            Query query = new Query();
            query.put("id_IN", modelIds);
            List<InfExchangeRecordEntity> entities = infExchangeRecordDao.queryList(query);
            for (int i = 0, count = entities.size(); i < count; ++i) {
                InfExchangeRecordEntity entity = entities.get(i);
                entity.setState(status);
                infExchangeRecordDao.update(entity);
            }
        }
    }

    @Override
    public void update(InfExchangeRecordEntity infExchangeRecord) {
        Objects.requireNonNull(infExchangeRecord);
        final InfExchangeRecordEntity dest = new InfExchangeRecordEntity();
        Optional.ofNullable(infExchangeRecord.getId()).ifPresent(aInteger -> {
            InfExchangeRecordEntity dests = Optional.ofNullable(infExchangeRecordDao.queryObject(aInteger)).orElse(dest);
            beanMapper.copy(infExchangeRecord, dests);
            infExchangeRecordDao.update(dests);
        });
    }


    @Override
    public void delete(Integer id) {
        infExchangeRecordDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        infExchangeRecordDao.deleteBatch(ids);
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
    public void exportsToExcels(List<InfExchangeRecordEntity> vos, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception {

        //excel结构
        List<ExcelColumn> excelColumns = new ArrayList<>();

        excelColumns.add(new ExcelColumn(excelColumns.size(), "id", "编号", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "userId", "用户id", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "currencyId", "货币id", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "score", "消耗积分", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "createDate", "创建时间", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "purseAddress", "钱包地址", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "sysUserId", "操作人", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "state", "状态【1.兑换中  2.已完成】", CellType.NUMERIC));

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
                    "兑换记录表-" + DateUtils.parseDateTime(new Date(), DateUtils.YYYYMMDDHHMMSSCn));
        } else {
            ExcelHelper.getInstanse().exportExcelFile(request, response, head, vos,
                    "兑换记录表-" + DateUtils.parseDateTime(new Date(), DateUtils.YYYYMMDDHHMMSSCn));
        }

    }

    @Override
    public void saveExchangeRecord(InfExchangeRecordEntity exchangeRecordEntity) {
        InfCurrencyEntity currencyEntity = currencyService.queryObject(exchangeRecordEntity.getCurrencyId());
        if (XaUtils.isEmpty(currencyEntity)) {
            throw new RRException("兑换失败，没有找到该货币信息");
        }
        UserEntity userEntity = userService.queryObject(exchangeRecordEntity.getUserId());
        if (userEntity.getScore() < currencyEntity.getScore()) {
            throw new RRException("积分不够，无法兑换");
        }
        //减掉库存
        currencyEntity.setCount(currencyEntity.getCount() - 1);
        currencyService.update(currencyEntity);
        //减去用户消耗的积分
        userEntity.setScore(userEntity.getScore() - currencyEntity.getScore());
        exchangeRecordEntity.setScore(currencyEntity.getScore());
        this.save(exchangeRecordEntity);

        //添加积分使用记录
        InfScoreUsedEntity infScoreUsedEntity = new InfScoreUsedEntity();
        infScoreUsedEntity.setUserId(exchangeRecordEntity.getUserId());
        infScoreUsedEntity.setCreateDate(LocalDateTime.now());
        infScoreUsedEntity.setChangeNote("兑换货币");
        infScoreUsedEntity.setChangeType(0);
        infScoreUsedDao.save(infScoreUsedEntity);
        userService.update(userEntity);
    }
}
