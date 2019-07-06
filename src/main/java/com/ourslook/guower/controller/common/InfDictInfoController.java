package com.ourslook.guower.controller.common;

import com.ourslook.guower.controller.AbstractController;
import com.ourslook.guower.controller.SysRoleController;
import com.ourslook.guower.entity.common.InfDictInfoEntity;
import com.ourslook.guower.service.common.InfDictInfoService;
import com.ourslook.guower.utils.*;
import com.ourslook.guower.utils.result.R;
import com.ourslook.guower.utils.result.ValidateResult;
import com.ourslook.guower.utils.validator.ValidatorUtils;
import com.ourslook.guower.vo.InfDictVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 字典表
 * 
 * @author dazer
 * @email ab601026460@qq.com
 * @date 2017-08-31 15:32:44
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("xadictinfo")
public class InfDictInfoController extends AbstractController {
	@Autowired
	private InfDictInfoService xaDictInfoService;


	/**
	 * 列表
	 * @see com.ourslook.guower.api.ApiCfgController#dictInfos(String)
	 * @see Constant.DictTypes
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:xadictinfo:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<InfDictVo> xaDictInfoList = xaDictInfoService.queryVoList(query);
		int total = xaDictInfoService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(xaDictInfoList, total, query.getLimit(), query.getPage());
		
		return R.ok().putObj("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:xadictinfo:info")
	public R info(@PathVariable("id") Long id){
		R result = R.ok();
		InfDictInfoEntity xaDictInfo = xaDictInfoService.queryObject(id);
		result.put("xaDictInfo",  xaDictInfo);
		return result;
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:xadictinfo:save")
	public R save(@RequestBody InfDictInfoEntity xaDictInfo){
		ValidatorUtils.validateEntity(xaDictInfo);
		xaDictInfo.setCreateUser(getUserId()+"");
		xaDictInfo.setCreateTime(DateUtils.getCurrentDate());
		//验证
		Query query = new Query();
		query.put("type", xaDictInfo.getType());
		query.put("remarks", xaDictInfo.getRemarks());
		List<InfDictInfoEntity> entities = xaDictInfoService.queryList(query);
		if (XaUtils.isNotEmpty(entities)) {
			return R.error("该字典取值在该类型中已存在");
		}
		xaDictInfoService.save(xaDictInfo);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:xadictinfo:update")
	public R update(@RequestBody InfDictInfoEntity xaDictInfo){
		ValidatorUtils.validateEntity(xaDictInfo);
		xaDictInfo.setCreateUser(getUserId()+"");
		xaDictInfo.setCreateTime(DateUtils.getCurrentDate());
		xaDictInfoService.update(xaDictInfo);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:xadictinfo:delete")
	public R delete(@RequestBody Long[] ids){
		xaDictInfoService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 字典数据code验证
	 *
	 * @see SysRoleController#checkRoleNameExist(HttpServletRequest)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkDictInfoCode",method=RequestMethod.POST)
	public ValidateResult checkDictInfoCodeExist(HttpServletRequest request)  {
		String cfgCode = UrlEncode.getUrlParams(request).get("code") + "".trim();

		Query query = new Query();
		query.put("code", cfgCode);
		List<InfDictInfoEntity> entities = xaDictInfoService.queryList(query);
		if (XaUtils.isEmpty(entities)) {
			return ValidateResult.ok();
		} else {
			return ValidateResult.error();
		}
	}

	/**
	 * 字典数据name验证
	 *
	 * @see SysRoleController#checkRoleNameExist(HttpServletRequest)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkDictInfoName",method=RequestMethod.POST)
	public ValidateResult checkDictInfoNameExist(HttpServletRequest request)  {
		String cfgCode = UrlEncode.getUrlParams(request).get("name") + "".trim();

		Query query = new Query();
		query.put("name", cfgCode);
		List<InfDictInfoEntity> entities = xaDictInfoService.queryList(query);
		if (XaUtils.isEmpty(entities)) {
			return ValidateResult.ok();
		} else {
			return ValidateResult.error();
		}
	}

	/**
	 * 字典数据remarks验证
	 *
	 * @see SysRoleController#checkRoleNameExist(HttpServletRequest)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="checkDictRemarks",method=RequestMethod.POST)
	public ValidateResult checkDictRemarks(HttpServletRequest request)  {
		Map map = UrlEncode.getUrlParams(request);
		String type = map.get("type") + "".trim();
		String remarks = map.get("remarks") + "".trim();

		Query query = new Query();
		query.put("type", type);
		query.put("remarks", remarks);
		List<InfDictInfoEntity> entities = xaDictInfoService.queryList(query);
		if (XaUtils.isEmpty(entities)) {
			return ValidateResult.ok();
		} else {
			return ValidateResult.error();
		}
	}

	/**
	 * 根据字典类型TYPE-CODE获取 自定详情数组
	 * getWebPath() + "xadictinfo/listByType?type=TYPE_CLAUS
	 * @param params
	 * @return
	 */
	@RequestMapping("/listByType")
	public R listByType(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<InfDictVo> dictVos = xaDictInfoService.queryVoList(query);
		return R.ok().setData(dictVos);
	}

}
