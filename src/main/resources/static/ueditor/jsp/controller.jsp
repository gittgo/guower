<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page import="com.ourslook.guower.utils.XaUtils" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	//String rootPath = application.getRealPath( "/" );
	//dazer 修改上传目录

	String rootPath = XaUtils.getWebUploadVirtalRootPath(request);

	out.write( new ActionEnter( request, rootPath ).exec() );
%>