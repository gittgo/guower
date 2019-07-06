package com.ourslook.guower.dao.common;

import com.ourslook.guower.dao.BaseDao;
import com.ourslook.guower.entity.common.TbUserEntity;
import com.ourslook.guower.vo.TbUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户信息
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-12-05 15:21:55
 *
 */
public interface TbUserDao extends BaseDao<TbUserEntity> {
     /**
      * 根据手机号码查询
      * @param params
      * @return
      */
     TbUserEntity queryByMobile(Map<String,Object> params);

     TbUserEntity queryByMobileAndUsertypeIn(Map<String,Object> params);

     /**
      * 通过usename查找用户
      * @param username
      * @return
      */
     TbUserEntity queryByUsername(String username);

     /**
      * 查找所有用户的手机号
      * @return
      */
     List<String> queryMobileList();

     /**
      * 查找所有用户的id
      * @return
      */
     List<String> queryIdList();

     /**
      * 通过会员编码查找用户
      */
     TbUserEntity queryByUserCode(String userCode);

     /**
      *   查找学生用户
      */
     List<TbUserEntity> queryListUser(Map<String, Object> map);

     int queryTotalUser(Map<String,Object> map);

     /**
      *  根据楼id查询属于这栋楼的配送员userid
      */
     List<String> queryUseridListByStudyHotel(Map<String, Object> map);

     /**
      *  查询本学校所有配送员的userid
      */
     List<String> querySenderUserids(Map<String, Object> map);

     /**
      *  查询本学校某栋楼以外的所有配送员的userid
      */
     List<String> querySenderUseridsByHotelId(Map<String, Object> map);

     /**
      *   查找商家用户
      */
     List<TbUserVo> queryShopList(Map<String, Object> map);

     int queryShopTotal(Map<String,Object> map);

     /**
      *  查询公共下级
      */
     List<TbUserEntity> queryPublicPersonList(Map<String, Object> map);

     /**
      *  根据我的会员编号查询我的所有下级的userid
      */
     List<Long> queryUserIdOfMySubordinate(@Param("usercode") String usercode);

     /**
      * 查询商家及其商家保证金、商家账户余额
      * @param userid
      * @return
      */
     TbUserVo queryShopObject(Long userid);

     /**
      * 根据学校id查询该学校所有的骑手用户id
      * @param schoolId
      * @return
      */
     List<String> queryRiderBySchoolId(Long schoolId);

     /**
      * 根据学校id查询该学校所有的楼栋长用户id
      * @param schoolId
      * @return
      */
     List<String> queryBuildingLeaderBySchoolId(Long schoolId);
}
