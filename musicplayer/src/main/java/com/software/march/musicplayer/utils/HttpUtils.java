package com.software.march.musicplayer.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description HTTP网络请求帮助类
 * @date 2016/12/28
 */
public class HttpUtils {

    /**
     * 使用HttpURLConnection提交get请求
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String sendGetRequest(String path) throws IOException {
        String result = null;
        // 创建URL对象
        URL url = new URL(path);
        // 打开连接,得到HttpURLConnection对象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置请求方式,连接超时,读取数据超时
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(6000);
        // 连接服务器
        conn.connect();
        // 发请求,得到响应数据
        // 得到响应码,必须是200才读取
        if (conn.getResponseCode() == 200) {
            // 得到InputStream,并读取成String
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toString();
            baos.close();
            is.close();
        }
        // 断开连接
        conn.disconnect();
        return result;
    }

    /**
     * 使用httpUrlConnection提交post请求
     *
     * @param path
     * @param data
     * @return
     * @throws IOException
     */
    public static String sendPostRequest(String path, String data) throws IOException {
        String result = null;
        // 创建URL对象
        URL url = new URL(path);
        // 打开连接,得到HttpURLConnection对象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置请求方式,连接超时,读取数据超时
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(6000);
        // 连接服务器
        conn.connect();
        // 得到OutputStream
        OutputStream os = conn.getOutputStream();
        // 写请求体
        os.write(data.getBytes("utf-8"));
        // 发请求,得到响应数据
        // 得到响应码,必须是200才读取
        if (conn.getResponseCode() == 200) {
            // 得到InputStream,并读取成String
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            result = baos.toString();
            baos.close();
            is.close();
        }
        // 断开连接
        conn.disconnect();
        return result;
    }

    /*
     * 使用httpClient提交get请求
	 */
    /*public void testClientGet(View v) {
        //1. 显示ProgressDialog
        final ProgressDialog dialog = ProgressDialog.show(this, null, "正在请求中...");
        //2. 启动分线程
        new Thread() {
            //3. 在分线程, 发送请求, 得到响应数据
            public void run() {
                try {
                    //1). 得到path, 并带上参数name=Tom1&age=11
                    String path = et_network_url.getText().toString() + "?name=Tom3&age=13";

                    //2). 创建HttpClient对象
                    HttpClient httpClient = new DefaultHttpClient();
                    //3). 设置超时
                    HttpParams params = httpClient.getParams();
                    HttpConnectionParams.setConnectionTimeout(params, 5000);
                    HttpConnectionParams.setSoTimeout(params, 5000);
                    //4). 创建请求对象
                    HttpGet request = new HttpGet(path);
                    //5). 执行请求对象, 得到响应对象
                    HttpResponse response = httpClient.execute(request);

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        //6). 得到响应体文本
                        HttpEntity entity = response.getEntity();
                        final String result = EntityUtils.toString(entity);
                        //4. 要主线程, 显示数据, 移除dialog
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                et_network_result.setText(result);
                                dialog.dismiss();
                            }
                        });
                    }
                    //7). 断开连接
                    httpClient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                    //如果出了异常要移除dialog
                    dialog.dismiss();
                }
            }
        }.start();
    }*/

    /*
     * 使用httpClient提交post请求
     */
    /*public void testClientPost(View v) {
        //1. 显示ProgressDialog
        final ProgressDialog dialog = ProgressDialog.show(this, null, "正在请求中...");
        //2. 启动分线程
        new Thread() {
            //3. 在分线程, 发送请求, 得到响应数据
            public void run() {
                try {
                    //1). 得到path
                    String path = et_network_url.getText().toString();

                    //2). 创建HttpClient对象
                    HttpClient httpClient = new DefaultHttpClient();
                    //3). 设置超时
                    HttpParams params = httpClient.getParams();
                    HttpConnectionParams.setConnectionTimeout(params, 5000);
                    HttpConnectionParams.setSoTimeout(params, 5000);
                    //4). 创建请求对象
                    HttpPost request = new HttpPost(path);
                    //设置请求体
                    List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
                    parameters.add(new BasicNameValuePair("name", "Tom4"));
                    parameters.add(new BasicNameValuePair("age", "14"));
                    HttpEntity entity = new UrlEncodedFormEntity(parameters);
                    request.setEntity(entity);

                    //5). 执行请求对象, 得到响应对象
                    HttpResponse response = httpClient.execute(request);

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        //6). 得到响应体文本
                        entity = response.getEntity();
                        final String result = EntityUtils.toString(entity);
                        //4. 要主线程, 显示数据, 移除dialog
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                et_network_result.setText(result);
                                dialog.dismiss();
                            }
                        });
                    }
                    //7). 断开连接
                    httpClient.getConnectionManager().shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                    //如果出了异常要移除dialog
                    dialog.dismiss();
                }
            }
        }.start();
    }*/
}