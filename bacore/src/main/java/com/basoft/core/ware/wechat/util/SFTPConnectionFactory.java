package com.basoft.core.ware.wechat.util;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * SFTP工厂类，用于获取SFTP的连接
 * @author 
 */
public class SFTPConnectionFactory {
	private static final transient Log logger = LogFactory.getLog(ServletRequestUtils.class);

    /** SFTP Info (Real)*/      
    private static String usernameReal = PropertiesUtils.getAlliexFtpRealUserName();
    private static String passwordReal = PropertiesUtils.getAlliexFtpRealPassword();
    private static String ipReal = PropertiesUtils.getAlliexFtpRealServerIp();
    private static int portReal  = Integer.parseInt(PropertiesUtils.getAlliexFtpRealServerPort());

    /** SFTP Info (Test)*/      
    private static String usernameTest = PropertiesUtils.getAlliexFtpTestUserName();
    private static String passwordTest = PropertiesUtils.getAlliexFtpTestPassword();
    private static String ipTest = PropertiesUtils.getAlliexFtpTestServerIp();
    private static int portTest  = Integer.parseInt(PropertiesUtils.getAlliexFtpTestServerPort());

    private static String privateKeyUse = PropertiesUtils.getAlliexFtpPrivateKeyUse();
    private static String privateKey = PropertiesUtils.getAlliexFtpPrivateKey();
    
	private static final SFTPConnectionFactory factory = new SFTPConnectionFactory();
	private ChannelSftp client;
	private Session session;
	private SFTPConnectionFactory(){
		
	}
	
	public static SFTPConnectionFactory getInstance(){
		return factory;
	}
	synchronized public ChannelSftp makeConnection(boolean realFlag, String sfpt_id, String sftp_pw, String sftp_ip, int sftp_port) throws Exception{
		
		if(client==null||session==null||!client.isConnected()||!session.isConnected()){
			try {    
				JSch jsch = new JSch();
				
				if (privateKey != null && privateKeyUse == "true") {    
					jsch.addIdentity(privateKey);// 设置私钥    
				}
				
				// added by dikim
				String ftpUser = sfpt_id; 
				String ftpPwd = sftp_pw;
				String ftpIp = sftp_ip; 
				int ftpPort = sftp_port; 
				
				session = jsch.getSession(ftpUser, ftpIp, ftpPort);    
				if (ftpPwd != null) {    
					session.setPassword(ftpPwd);      
				}    
				Properties config = new Properties();    
				config.put("StrictHostKeyChecking", "no");    
				
				session.setConfig(config);
				session.connect();
				
				Channel channel = session.openChannel("sftp");    
				channel.connect();    
				
				client = (ChannelSftp) channel;
				
				logger.info("sftp server login success");
				
			} catch (JSchException e) {   
				logger.info("SFTP Login Error, message:" + e.getMessage());
				throw new Exception("SFTP Login Error, message:" + e.getMessage());
				
			} 
		}
		    
		     return client;
	}
	
	/**  
     * @desc 关闭连接 server   
     */    
    public  void logout(){    
        if (client != null) {    
            if (client.isConnected()) {    
            	client.disconnect();    
            }    
        }    
        if (session != null) {    
            if (session.isConnected()) {    
                session.disconnect();    
            }    
        }    
    }
 
}
