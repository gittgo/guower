package com.ourslook.guower.service.impl;

import com.ourslook.guower.dao.SysUserDao;
import com.ourslook.guower.dao.common.TbUserDao;
import com.ourslook.guower.entity.SysUserEntity;
import com.ourslook.guower.entity.common.TbUserEntity;
import com.ourslook.guower.service.SysRoleService;
import com.ourslook.guower.service.SysUserRoleService;
import com.ourslook.guower.service.SysUserService;
import com.ourslook.guower.utils.Constant;
import com.ourslook.guower.utils.RRException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


/**
 * 系统用户
 */
@SuppressWarnings("all")
@Service("sysUserService")
//@Cacheable("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    private Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private TbUserDao tbUserDao;

    @Override
    public List<String> queryAllPerms(Long userId) {
        return sysUserDao.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserDao.queryAllMenuId(userId);
    }

    @Override
    public SysUserEntity queryByUserName(String username) {
        logger.error("start..");
        SysUserEntity sysUserEntity = sysUserDao.queryByUserName(username);
        logger.error("end ..");
        return sysUserEntity;
    }

    @Override
    public SysUserEntity queryByMoble(String mobile) {
        return sysUserDao.queryByMobile(mobile);
    }

    @Override
    public SysUserEntity queryObject(Long userId) {
        //查询用户
        SysUserEntity sysUserEntity = sysUserDao.queryObject(userId);
        //设置权限
        List<Long> roleIds = sysUserRoleService.queryRoleIdList(userId);
        sysUserEntity.setRoleIdList(roleIds);
        return sysUserEntity;
    }

    @Override
    public List<SysUserEntity> queryList(Map<String, Object> map) {
        return sysUserDao.queryList(map);
    }

    @Override
    public List<SysUserEntity> queryList() {
        return sysUserDao.queryAllList();
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysUserDao.queryTotal(map);
    }

    @Override
    @Transactional
    public void save(SysUserEntity user) {
        String oldPassword = user.getPassword();
        user.setCreateTime(new Date());
        //sha256加密
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        sysUserDao.save(user);
        logger.info("save sys user: id= " + user.getUserId() + "" +
                ";成功获取到用户的id,oracle 中不要用触发器，否则这里获取不到用户的id");

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());

        //后台用户信息修改，暂不新增前台信息用户.
        if (Constant.IS_SYNCHRONIZE_NORMAL_USER && false) {
            SysUserEntity sysUserEntity = sysUserDao.queryByMobile(user.getMobile());
            if (sysUserEntity == null) {
                if (false) {
                    //新增管理员，不新增商家
                    TbUserEntity tbUserEntity = new TbUserEntity();
                    tbUserEntity.setCreatetime(LocalDateTime.now());
                    tbUserEntity.setStatus(Constant.Status.valid);
                    tbUserEntity.setMobile(user.getMobile());
                    tbUserEntity.setUsername(user.getUsername());
                    tbUserEntity.setPassword(oldPassword);
                    tbUserEntity.setUserTypes(Constant.UserTypes.USER_SELLER);
                    sysUserDao.save(sysUserEntity);
                }
            } else {
                sysUserEntity.setPassword(user.getPassword());
                sysUserDao.update(sysUserEntity);
            }
        }
    }


    @Override
    @Transactional
    public void update(SysUserEntity user) {
        String oldPassword = user.getPassword();
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            if(StringUtils.isNotBlank(sysUserDao.queryObject(user.getUserId()).getPassword())){
                if(!user.getPassword().equals(sysUserDao.queryObject(user.getUserId()).getPassword())){
                    user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
                }
            }else {
                user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
            }
        }
        sysUserDao.update(user);

        //保存用户与角色关系
        if (user.getRoleIdList() != null) {
            sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
        }

        //后台用户信息修改，暂不新增前台信息用户.
//        if (Constant.IS_SYNCHRONIZE_NORMAL_USER) {
//            TbUserEntity tbUserEntity = tbUserService.queryByMobile(user.getMobile(), Lists.newArrayList(Constant.UserTypes.USER_SELLER));
//            if (tbUserEntity == null) {
//                //暂不新增，新增管理员，不新增商家；
//                if (false) {
//                    tbUserEntity = new TbUserEntity();
//                    tbUserEntity.setCreatetime(LocalDateTime.now());
//                    tbUserEntity.setStatus(Constant.Status.valid);
//                    tbUserEntity.setMobile(user.getMobile());
//                    tbUserEntity.setUsername(user.getUsername());
//                    tbUserEntity.setPassword(oldPassword);
//                    tbUserEntity.setUserTypes(Constant.UserTypes.USER_SELLER);
//                    tbUserDao.save(tbUserEntity);
//
//                    //保存用户与角色关系
//                    if (user.getRoleIdList() != null) {
//                        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
//                    }
//                }
//            } else {
//
//                tbUserEntity.setPassword(user.getPassword());
//                tbUserDao.update(tbUserEntity);
//            }
//        }
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] userId) {
        sysUserDao.deleteBatch(userId);
        //  sysUserDao.deleteBatch1(userId);

//        if (Constant.IS_SYNCHRONIZE_NORMAL_USER) {
//            for (Long uuuu : userId) {
//                TbUserEntity tbUserEntity = tbUserService.queryObject(uuuu);
//                if (tbUserEntity != null) {
//                    tbUserEntity.setStatus(Constant.Status.delete);
//                    tbUserDao.update(tbUserEntity);
//                }
//            }
//        }
    }

    @Override
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        int num = sysUserDao.updatePassword(map);

//        if (num != 0) {
//            SysUserEntity user = sysUserDao.queryObject(userId);
//            if (Constant.IS_SYNCHRONIZE_NORMAL_USER) {
//                //更新普通用户密码
//                if (user.getMobile() != null && XaUtils.isNotBlank(user.getMobile())) {
//                    TbUserEntity tbUserEntity = tbUserService.queryByMobile(user.getMobile(), Constant.UserTypes.USER_SELLER);
//                    if (tbUserEntity != null) {
//                        tbUserEntity.setPassword(user.getPassword());
//                        tbUserDao.update(tbUserEntity);
//                    } else {
//                        //这里没有找到相关的tbuser表的用户.
//                    }
//                }
//            }
//        }
        return num;
    }

    @Override
    public boolean isSuperAdmin(Long userId) {
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        if (roleIdList.contains(Constant.SYS_ROLO_ID_SUPER_ADMIN) || roleIdList.contains(Constant.SYS_ROLO_ID_SUPER_ADMINID)) {
            return true;
        }
        //只要不是商家用户，都可以做为超级管理员.
        Long tbUserId = getTbUserId(userId);
        if (tbUserId == null) {
            return true;
        }
        return false;
    }

    @Override
    public Long getTbUserId(Long adminUserId) {
        SysUserEntity sysUserEntity =  sysUserDao.queryObject(adminUserId);
//        if (sysUserEntity != null && StringUtils.isNotBlank(sysUserEntity.getMobile())) {
//            TbUserEntity tbUserEntity = tbUserService.queryByMobile(sysUserEntity.getMobile(), Constant.UserTypes.USER_SELLER);
//            if (tbUserEntity != null) {
//                return tbUserEntity.getUserid();
//            }
//        }
        return null;
    }

    /**
     * 检查角色是否越权
     */
    @Override
    public void checkRole(SysUserEntity createUser, SysUserEntity user) {
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (createUser == null || createUser.getCreateUserId() == null || createUser.getCreateUserId() == Constant.SYS_ROLO_ID_SUPER_ADMIN || createUser.getCreateUserId() == Constant.SYS_ROLO_ID_SUPER_ADMINID) {
            return;
        }

        //查询用户创建的角色列表
        List<Long> roleIdList1 = sysUserRoleService.queryRoleIdList(createUser.getUserId());

        List<Long> roleIdList2 = sysRoleService.queryRoleIdList(createUser.getCreateUserId());

        List<Long> roleIdList = new ArrayList<>();
        roleIdList.addAll(roleIdList1);
        roleIdList.addAll(roleIdList2);


        if (user.getRoleIdList() != null) {
            //判断是否越权
            if (!roleIdList.containsAll(user.getRoleIdList())) {
                throw new RRException("新增用户所选角色，不是本人创建, 越权");
            }
        }

    }
}
