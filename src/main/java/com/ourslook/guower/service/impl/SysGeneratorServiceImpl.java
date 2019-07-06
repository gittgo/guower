package com.ourslook.guower.service.impl;

import com.ourslook.guower.config.DruidConfig;
import com.ourslook.guower.dao.SysGeneratorDao;
import com.ourslook.guower.utils.GenUtilsOracle;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ourslook.guower.service.SysGeneratorService;
import com.ourslook.guower.utils.GenUtilsMySql;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service("sysGeneratorService")
public class SysGeneratorServiceImpl implements SysGeneratorService {
	@Autowired
	private SysGeneratorDao sysGeneratorDao;
	@Value("${spring.datasource.dbType:#{null}}")
	private String dbType;

	@Override
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		return sysGeneratorDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysGeneratorDao.queryTotal(map);
	}

	@Override
	public Map<String, String> queryTable(String tableName) {
		return sysGeneratorDao.queryTable(tableName);
	}

	@Override
	public List<Map<String, String>> queryColumns(String tableName) {
		return sysGeneratorDao.queryColumns(tableName);
	}

	@Override
	public byte[] generatorCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		
		for(String tableName : tableNames){
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			if (DruidConfig.DBTYPE_MYSQL.equalsIgnoreCase(dbType)) {
                GenUtilsMySql.generatorCode(table, columns, zip);
			} else  {
                GenUtilsOracle.generatorCode(table, columns, zip);
            }
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

}
