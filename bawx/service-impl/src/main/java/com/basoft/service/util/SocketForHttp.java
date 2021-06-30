package com.basoft.service.util;

import com.basoft.core.ware.wechat.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class SocketForHttp {
    private static final transient Log logger = LogFactory.getLog(HttpClientUtils.class);

    private int port;
    private String host;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public SocketForHttp(String host, int port) throws Exception {
        this.host = host;
        this.port = port;

        /**
         * http协议  
         */
        //socket = new Socket(this.host, this.port);  

        /**
         * https协议  
         */
        socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(this.host, this.port);
    }

    public void sendGet() throws IOException {
        //String requestUrlPath = "/z69183787/article/details/17580325";  
        String requestUrlPath = "/";

        OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream());
        bufferedWriter = new BufferedWriter(streamWriter);
        bufferedWriter.write("GET " + requestUrlPath + " HTTP/1.1\r\n");
        bufferedWriter.write("Host: " + this.host + "\r\n");
        bufferedWriter.write("\r\n");
        bufferedWriter.flush();

        BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            logger.info(line);
        }
        bufferedReader.close();
        bufferedWriter.close();
        socket.close();

    }

    public String sendPost(String path, String data) throws IOException {
        String returns = "";
        try {
            logger.info("♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥" + data);
            OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
            bufferedWriter = new BufferedWriter(streamWriter);
            bufferedWriter.write("POST " + path + " HTTP/1.1\r\n");
            bufferedWriter.write("Host: " + this.host + "\r\n");
            bufferedWriter.write("Content-Length: " + data.length() + "\r\n");
            bufferedWriter.write("Content-Type: application/x-www-form-urlencoded\r\n");
            bufferedWriter.write("\r\n");
            bufferedWriter.write(data);

            bufferedWriter.write("\r\n");
            bufferedWriter.flush();

            logger.info("♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥ Start");

            BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
            returns = buffer.toString();
            bufferedReader.close();
            bufferedWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returns;
    }


    public static String requestPost(String postUrl, Map<String, Object> params) throws IOException {
        String returns = "";
        try {
            URL url = new URL(postUrl);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuffer buffer = new StringBuffer();
            for (int c; (c = in.read()) >= 0; ) {
                buffer.append((char) c);
            }

            returns = buffer.toString();
            //logger.info("♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥ \n"+returns);

        } catch (Exception e) {
            logger.error("Sokect Https Post Error :" + e.getMessage());
            e.printStackTrace();
        }
        return returns;
    }
}
