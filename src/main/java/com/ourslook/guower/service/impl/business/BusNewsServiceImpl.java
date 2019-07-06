package com.ourslook.guower.service.impl.business;

import com.ourslook.guower.api.BusUtils;
import com.ourslook.guower.dao.business.BusNewsDao;
import com.ourslook.guower.dao.score.InfScoreUsedDao;
import com.ourslook.guower.dao.user.UserDao;
import com.ourslook.guower.entity.business.BusNewsEntity;
import com.ourslook.guower.entity.score.InfScoreUsedEntity;
import com.ourslook.guower.entity.user.ExamineCheck;
import com.ourslook.guower.entity.user.UserEntity;
import com.ourslook.guower.service.business.BusNewsService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.DateUtils;
import com.ourslook.guower.utils.ShiroUtils;
import com.ourslook.guower.utils.beanmapper.BeanMapper;
import com.ourslook.guower.utils.office.csvexports.CsvExportor;
import com.ourslook.guower.utils.office.excelutils.ExcelColumCellInterface;
import com.ourslook.guower.utils.office.excelutils.ExcelColumn;
import com.ourslook.guower.utils.office.excelutils.ExcelHead;
import com.ourslook.guower.utils.office.excelutils.ExcelHelper;
import com.ourslook.guower.vo.business.BusNewsVo;
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
@Service("busNewsService")
public class BusNewsServiceImpl implements BusNewsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BusNewsDao busNewsDao;
    @Resource
    private BeanMapper beanMapper;
    @Resource
    private CsvExportor csvExportor;
    @Resource
    private MessageSource messageSource;
    @Autowired
    private UserDao userDao;
    @Autowired
    private InfScoreUsedDao infScoreUsedDao;

    @Override
    public BusNewsEntity queryObject(Integer id) {
        return busNewsDao.queryObject(id);
    }

    @Override
    public List<BusNewsEntity> queryList(Map<String, Object> map) {
        return busNewsDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return busNewsDao.queryTotal(map);
    }

    @Override
    public void save(BusNewsEntity busNews) {
        busNewsDao.save(busNews);
    }

    @Override
    public List<BusNewsVo> queryVoList(Map<String, Object> map) {
        return busNewsDao.queryVoList(map);
    }

    @Override
    public void multiOperate(List<String> modelIds, Integer status) {
//        if (XaUtils.isNotEmpty(modelIds)) {
//            status = status == null ? Constant.Status.delete : status;
//            Query query = new Query();
//            query.put("id_IN", modelIds);
//            List<BusNewsEntity> entities =  busNewsDao.queryList(query);
//            for (int i = 0, count = entities.size(); i < count; ++i) {
//                BusNewsEntity entity = entities.get(i);
//                entity.setState(status);
//                busNewsDao.update(entity);
//            }
//        }
    }

    @Override
    public void update(BusNewsEntity busNews) {
        Objects.requireNonNull(busNews);
        final BusNewsEntity dest = new BusNewsEntity();
        Optional.ofNullable(busNews.getId()).ifPresent(aInteger -> {
            BusNewsEntity dests = Optional.ofNullable(busNewsDao.queryObject(aInteger)).orElse(dest);
            Optional.ofNullable(dests.getIsRelease()).filter(isRelease -> !isRelease.equals(busNews.getIsRelease()) && busNews.getIsRelease() == 1).ifPresent(isRelease->{
                    busNews.setReleaseUserId(Optional.ofNullable(ShiroUtils.getUserId()).isPresent()?ShiroUtils.getUserId().intValue():null);
                    busNews.setReleaseDate(LocalDateTime.now());
            });
            beanMapper.copy(busNews, dests);
            busNewsDao.update(dests);
        });
    }

    @Override
    public void examine(ExamineCheck examineCheck) {
        busNewsDao.examine(examineCheck);
        if(examineCheck.getResult() == Constant.ExamineType.TYPE_EXAMINE_PASS){
            for (Integer newsId : examineCheck.getIds()){
                BusNewsEntity busNewsEntity = busNewsDao.queryObject(newsId);
                Optional.ofNullable(busNewsEntity).filter(news -> news.getAuthor()!=null).ifPresent(news ->{
                    Integer score = BusUtils.getPlusScore(news.getAuthor(),Constant.ScoreGetType.TYPE_SCORE_GET_NEWS);
                    //更新用户积分
                    UserEntity userEntity = userDao.queryObject(news.getAuthor());
                    userEntity.setScore(userEntity.getScore()==null?score:userEntity.getScore()+score);
                    userDao.update(userEntity);
                    //生成积分使用记录
                    InfScoreUsedEntity infScoreUsedEntity = new InfScoreUsedEntity();
                    infScoreUsedEntity.setUserId(news.getAuthor());
                    infScoreUsedEntity.setScoreChange(score);
                    infScoreUsedEntity.setChangeType(Constant.ScoreGetType.TYPE_SCORE_GET_NEWS);
                    infScoreUsedEntity.setCreateDate(LocalDateTime.now());
                    infScoreUsedEntity.setChangeNote(Constant.ScoreGetType.caseName(Constant.ScoreGetType.TYPE_SCORE_GET_NEWS));
                    infScoreUsedDao.save(infScoreUsedEntity);
                });
            }
        }
    }

    @Override
    public BusNewsVo queryObjectVo(Integer id) {
        return busNewsDao.queryObjectVo(id);
    }

    @Override
    public void delete(Integer id) {
        busNewsDao.delete(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {
        busNewsDao.deleteBatch(ids);
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
    public void exportsToExcels(List<BusNewsEntity> vos, HttpServletRequest request, HttpServletResponse response, boolean isCvs) throws Exception {

        //excel结构
        List<ExcelColumn> excelColumns = new ArrayList<>();

        excelColumns.add(new ExcelColumn(excelColumns.size(), "id", "编号", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "title", "标题", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "smallTitle", "副标题【简介】", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "image", "大图标", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "smallImage", "小图标", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "tag", "文章标签【2.NEW 1.HOT 0.无标签】", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "tag1", "内容标签1", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "tag2", "内容标签2", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "tag3", "内容标签3", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "mainText", "正文【详情】", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "newsType", "文章类型", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "lookTimes", "阅读量", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "author", "作者", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "responsibleEditor", "责任编辑", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "releaseType", "发布类型【1.后台 2.作者】", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "releaseUserId", "发布人", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "releaseDate", "发布时间", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "isAdvertisement", "是否为广告【1.广告  0.非广告】", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "isHotspot", "是否上热点【1.热点  0.非热点】", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "newsRemarks1", "暂留", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "newsRemarks2", "暂留", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "newsRemarks3", "暂留", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "sort", "排序", CellType.NUMERIC));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "sortTime", "排序到期时间", CellType.STRING));
        excelColumns.add(new ExcelColumn(excelColumns.size(), "examineType", "审核类型【-1.未通过 0.审核中 1.通过】", CellType.NUMERIC));

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
                    "新闻文章表-" + DateUtils.parseDateTime(new Date(), DateUtils.YYYYMMDDHHMMSSCn));
        } else {
            ExcelHelper.getInstanse().exportExcelFile(request, response, head, vos,
                    "新闻文章表-" + DateUtils.parseDateTime(new Date(), DateUtils.YYYYMMDDHHMMSSCn));
        }

    }

    @Override
    public int getBrowseTotalByAuthor(Long author) {
        return busNewsDao.getBrowseTotalByAuthor(author);
    }

    @Override
    public List<BusNewsEntity> getHotNews(Map<String, Object> map) {
        return busNewsDao.getHotNews(map);
    }

    @Override
    public void addNews(BusNewsEntity busnews) {
        busnews.setTag(0);
        busnews.setLookTimes(BusUtils.getNewsLookTimes());
        busnews.setReleaseDate(LocalDateTime.now());
        busnews.setReleaseType(Constant.NewsReleaseType.AUTHOR);
        busnews.setIsAdvertisement(0);
        busnews.setIsHotspot(0);
        busnews.setExamineType(Constant.ExamineType.TYPE_EXAMINE_UNTREATED);
        busnews.setIsRelease(0);
        busnews.setSmallImage(busnews.getImage());
        busnews.setImage(null);//前端发布的大图为null
        this.save(busnews);
    }
}
