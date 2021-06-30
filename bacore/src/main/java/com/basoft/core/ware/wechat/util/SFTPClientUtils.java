package com.basoft.core.ware.wechat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;


public class SFTPClientUtils {
	private static final transient Log logger = LogFactory.getLog(ServletRequestUtils.class);
	
	private static int downloadSleep = 100;		// 文件下载失败下次超时重试时间
	private static int downloadRetry = 1;		// 文件下载失败重试次数
	private static int uploadSleep   = Integer.parseInt(PropertiesUtils.getAlliexFtpUploadSleepTime());		// 文件上传失败下次超时重试时间
	private static int uploadRettry  = Integer.parseInt(PropertiesUtils.getAlliexFtpUploadRetryCount());		// 文件上传失败重试次数


	 /** 
	 * @desc 文件上传  
     * @desc 将文件对象上传到sftp作为文件。文件完整路径=basePath+directory
     * @desc 目录不存在则会上传文件夹 
     * @param basePath  服务器的基础路径  
     * @param directory  上传到该目录   
     * @param sftpFileName  sftp端文件名   
     * @param file   文件对象   
     */    
    public synchronized static boolean upload(String basePath, String directory, String filePath, boolean realFlag, String sfpt_id, String sftp_pw, String sftp_ip, int sftp_port)throws Exception{    
    	boolean result = false;
    	Integer i = 0;
        while(!result){
        	
        	ChannelSftp sftp = SFTPConnectionFactory.getInstance().makeConnection(realFlag, sfpt_id, sftp_pw, sftp_ip, sftp_port);
        	
        	try {     
                sftp.cd(basePath);  
                sftp.cd(directory);    
            } catch (SftpException e) {   
            	
//            	logger.info("sftp upload file, create directory");
            	
                String [] dirs=directory.split("/");  
                String tempPath=basePath;  
                for(String dir:dirs){  
                    if(null== dir || "".equals(dir)) continue;  
                    tempPath+="/"+dir;  
                    try{   
                        sftp.cd(tempPath);  
                    }catch(SftpException ex){  
                        try {
    						sftp.mkdir(tempPath);
    						sftp.cd(tempPath);  
    					} catch (SftpException e1) {
    						
    						logger.info("sftp upload file, create directory fail:"+e1.getMessage()+ex.getMessage());
    						
    					}  
                    }  
                }  
            }    
            try {
            	File file = new File(filePath);
    			sftp.put(new FileInputStream(file) , file.getName());
    			if(i>0){
    				logger.info("sftp re try upload success. ftp path:"+basePath+directory+", file name:"+file.getName());
				}else{
					logger.info("sftp upload success. ftp path"+basePath+directory+", file name:"+file.getName());
				}
    			result = true;
    		} catch (Exception e) {
    			i++;
    			logger.info("sftp upload fail re-try"+i+" times, error message:"+e.getMessage());
				if(i>uploadRettry){
					logger.info("sftp upload fail try retry count exceeded, error message:"+e.getMessage());
					return result;
				}
    			try {
					TimeUnit.MILLISECONDS.sleep(uploadSleep);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
    		}  
            
        }
    	
        return result;
    }   
    
    /**  
     * @desc 下载文件。 
     * @param directory 下载目录 
     * @param downloadFile 下载的文件 
     * @param saveFile 存在本地的路径 
     */      
    public synchronized static boolean download(String directory, String downloadFile, String saveFile, boolean realFlag, String sfpt_id, String sftp_pw, String sftp_ip, int sftp_port)throws Exception{    
    	boolean result = false;
    	Integer i = 0;
    	while(!result){
    		ChannelSftp sftp = SFTPConnectionFactory.getInstance().makeConnection(realFlag, sfpt_id, sftp_pw, sftp_ip, sftp_port);
    		if (directory != null && !"".equals(directory)) {    
    			try {
    				sftp.cd(directory);
    			} catch (SftpException e) {
    				logger.info("sftp download fail, file directory does not exist, error message:"+e.getMessage());
    				throw new Exception("sftp download, file directory does not exist, error message:" + e.getMessage());
    				
    			}    
    		}  
    		
    		File file = new File(saveFile+downloadFile); 
    		FileOutputStream fileOutputStream = null;
    		try {
    			fileOutputStream = new FileOutputStream(file);
    		} catch (FileNotFoundException e1) {
    			logger.info("sftp download fail, local file directory does not exist, error message:"+e1.getMessage());
    			throw new Exception("sftp download fail, local file directory does not exist, error message:" + e1.getMessage());
    		}
    		try {
    			sftp.get(downloadFile, fileOutputStream);
				if(i>0){
					logger.info("sftp download success, sftp path:"+directory+", local file path:"+saveFile);	
				}else{
					logger.info("sftp download success, sftp path:"+directory+", local file path:"+saveFile);
				}
				
				long file_size = file.length();
				if(file_size == 0) {
					throw new Exception("server file:" + downloadFile + " file size 0KB.");
				}
    			result = true;
    		} catch (SftpException e1) {
    			i++;
    			logger.info("sftp download fail re-try"+i+" times, error message:"+e1.getMessage());
				if(i>downloadRetry){
					logger.info("sftp download fail try retry count exceeded, error message:"+e1.getMessage());
				}
				try {
					TimeUnit.MILLISECONDS.sleep(downloadSleep);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				throw new Exception("file does not exist on server, error message:" + e1.getMessage());
    		}finally {
    			try {
    				fileOutputStream.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
        return result;
    }    
      
    /**  
     * @desc 删除文件  
     * @param directory 要删除文件所在目录  
     * @param deleteFile 要删除的文件  
     */    
    public synchronized static boolean delete(String directory, String deleteFile, boolean realFlag, String sfpt_id, String sftp_pw, String sftp_ip, int sftp_port)throws Exception{    
    	boolean result = false;
    	ChannelSftp sftp = SFTPConnectionFactory.getInstance().makeConnection(realFlag, sfpt_id, sftp_pw, sftp_ip, sftp_port);
    	try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
        result = true;
        return result;
    }

}
