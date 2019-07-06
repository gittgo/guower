package com.ourslook.guower.service.impl.common;

import com.ourslook.guower.dao.common.InfDitcypeInfoDao;
import com.ourslook.guower.entity.common.InfDitcypeInfoEntity;
import com.ourslook.guower.service.common.InfoDitcypeInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("xaDitcypeInfoService")
@Transactional
public class InfDitcypeInfoServiceImpl implements InfoDitcypeInfoService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InfDitcypeInfoDao xaDitcypeInfoDao;

    @Override
    public InfDitcypeInfoEntity queryObject(Long id) {
        return xaDitcypeInfoDao.queryObject(id);
    }

    @Override
    public List<InfDitcypeInfoEntity> queryList(Map<String, Object> map) {
        return xaDitcypeInfoDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return xaDitcypeInfoDao.queryTotal(map);
    }

    @Override
    public void save(InfDitcypeInfoEntity xaDitcypeInfo) {
        xaDitcypeInfoDao.save(xaDitcypeInfo);
    }

    @Override
    public void update(InfDitcypeInfoEntity xaDitcypeInfo) {
        xaDitcypeInfoDao.update(xaDitcypeInfo);
    }

    @Override
    public void delete(Long id) {
        xaDitcypeInfoDao.delete(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        xaDitcypeInfoDao.deleteBatch(ids);
    }

    //测试Transactional;
    //http://blog.csdn.net/u013142781/article/details/50421904
    //http://blog.csdn.net/sdh870680601/article/details/50879753
    //http://blog.csdn.net/weitao233136/article/details/51841707
    // 事物失效的原因：http://www.cnblogs.com/edwinchen/p/4137707.html
    // 事物失效的原因：http://jinnianshilongnian.iteye.com/blog/1850432
    //@Transactional
    @Override
    public void testTransactionals() {
        logger.info("start testTransactionals.......");

//        InfDitcypeInfoEntity entity = new InfDitcypeInfoEntity();
//        entity.setCode("111");
//        entity.setName("1111");
//        xaDitcypeInfoDao.save(entity);
//
//        int bb = 1 / 0;
//
//        entity.setCode("222");
//        entity.setName("2222");
//        //xaDitcypeInfoDao.save(entity);
//
//        this.test3();

        logger.info("end testTransactionals.......");
    }

    @Override
    public synchronized void testSynchronized(String param) {
        try {
            logger.info("========线程开始休眠=========" + param);
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            logger.info("========线程休眠被中断=========" + param);
        }
        logger.info("========线程休眠被结束=========" + param);
    }

    @Override
    public void testSynchronizeds(String param) {
        try {
            synchronized (this) {
                logger.info("========测试synchronized=========" + param);
                Thread.sleep(1000 * 10);
                logger.info("========测试synchronized中断结束=========" + param + "  ");
            }
        } catch (InterruptedException e) {
            logger.info("========测试synchronized中断=========" + param);
        }
    }

    private void test3() {
        InfDitcypeInfoEntity entity = new InfDitcypeInfoEntity();
        entity.setCode("3333");
        entity.setName("333333");
        int bb = 1 / 0;
        entity.setCode("4444");
        entity.setName("444444");
    }
}
