package com.ourslook.guower.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 类的说明：
 * @author 张林	
 * 创建时间：2014-4-21 上午09:53:57
 */
public class MyAuthenticator extends Authenticator {
	
	String userName=null;
    String password=null;
        
    public MyAuthenticator(){   
    }   
    public MyAuthenticator(String username, String password) {
        this.userName = username;    
        this.password = password;    
    }    
    @Override
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(userName, password);
    }   

}
