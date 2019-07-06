package com.ourslook.guower.utils.mail;

public class SimpleMailSenderTest {

   /* @SuppressWarnings("all")
   public static void main(String[] args) {
        String mailbox = "415052415@qq.com";
        Properties pro = new Properties();
        try {
            pro.load(SimpleMailSenderTest.class.getClassLoader()
                    .getResourceAsStream("email.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(pro.getProperty("email.host"));
        mailInfo.setMailServerPort(pro.getProperty("email.port"));
        mailInfo.setValidate(true);
        mailInfo.setUserName(pro.getProperty("email.fromAddress"));
        mailInfo.setPassword(pro.getProperty("email.password"));// 您的邮箱密码
        mailInfo.setFromAddress(pro.getProperty("email.fromAddress"));
        mailInfo.setToAddress(mailbox);    //415052415@qq.com
        mailInfo.setSubject("【蹓跶蹓跶】绑定邮箱验证");
        String linkurl = "http://www.baidu.com/activeEmail?email=172810573@qq.com&syscode=" + "123412342314";
			*//*if(id != null){
				linkurl += "&id=" + id;
			}*//*
        String contents = "【蹓跶蹓跶】绑定邮箱验证，验证码为：<a '" + linkurl + "'>" + "1324324234" + "</a>";
        mailInfo.setContent(contents);
        // 这个类主要来发送邮件
        try {
            SimpleMailSender sms = new SimpleMailSender();
            sms.sendHtmlMail(mailInfo);// 发送文体格式
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
