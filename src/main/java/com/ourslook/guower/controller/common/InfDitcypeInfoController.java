package com.ourslook.guower.controller.common;

import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.controller.SysRoleController;
import com.ourslook.guower.entity.common.InfDitcypeInfoEntity;
import com.ourslook.guower.service.common.InfoDitcypeInfoService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.ValidateResult;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 字典类型表
 *
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-08-31 15:32:44
 */
@RestController
@RequestMapping("xaditcypeinfo")
public class InfDitcypeInfoController extends AbstractController {
	@Autowired
	private InfoDitcypeInfoService xaDitcypeInfoService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:xadictypeinfo:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<InfDitcypeInfoEntity> xaDitcypeInfoList = xaDitcypeInfoService.queryList(query);
		int total = xaDitcypeInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(xaDitcypeInfoList, total, query.getLimit(), query.getPage());
		
		return R.ok().putObj("page", pageUtil);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:xadictypeinfo:info")
	public R info(@PathVariable("id") Long id){
		InfDitcypeInfoEntity xaDitcypeInfo = xaDitcypeInfoService.queryObject(id);
		return R.ok().putObj("xaDitcypeInfo", xaDitcypeInfo);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:xadictypeinfo:save")
	public R save(@RequestBody InfDitcypeInfoEntity xaDitcypeInfo){
		ValidatorUtils.validateEntity(xaDitcypeInfo);
		xaDitcypeInfo.setCreateUser(getUserId()+"");
		xaDitcypeInfo.setCreateTime(DateUtils.getCurrentDate());
		xaDitcypeInfoService.save(xaDitcypeInfo);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:xaditcypeinfo:update")
	public R update(@RequestBody InfDitcypeInfoEntity xaDitcypeInfo){
		ValidatorUtils.validateEntity(xaDitcypeInfo);
        xaDitcypeInfo.setCreateUser(getUserId()+"");
		xaDitcypeInfoService.update(xaDitcypeInfo);
        xaDitcypeInfo.setCreateTime(DateUtils.getCurrentDate());
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:xadictypeinfo:delete")
	public R delete(@RequestBody Long[] ids){
		xaDitcypeInfoService.deleteBatch(ids);
		return R.ok();
	}

	/**
     * 检查字典类型是否重复
     *
	 * @see SysRoleController#checkRoleNameExist(HttpServletRequest)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkDictType",method=RequestMethod.POST)
	public ValidateResult checkDictTypeCodeExist(HttpServletRequest request)  {
		String cfgCode = UrlEncode.getUrlParams(request).get("code") + "".trim();

		Query query = new Query();
		query.put("code", cfgCode);
		List<InfDitcypeInfoEntity> entities = xaDitcypeInfoService.queryList(query);
		if (XaUtils.isEmpty(entities)) {
			return ValidateResult.ok();
		} else {
			return ValidateResult.error();
		}
	}

    /**
     * 检查字典类型是否重复
     *
     * @see SysRoleController#checkRoleNameExist(HttpServletRequest)
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value="checkDictName",method=RequestMethod.POST)
    public ValidateResult checkDictTypeNameExist(HttpServletRequest request)  {
        String cfgCode = UrlEncode.getUrlParams(request).get("name") + "".trim();

        Query query = new Query();
        query.put("name", cfgCode);
        List<InfDitcypeInfoEntity> entities = xaDitcypeInfoService.queryList(query);
        if (XaUtils.isEmpty(entities)) {
            return ValidateResult.ok();
        } else {
            return ValidateResult.error();
        }
    }
	
}
