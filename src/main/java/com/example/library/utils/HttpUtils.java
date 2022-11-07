package com.example.library.utils;

import com.example.library.domain.ApiCode;
import com.example.library.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http 工具类
 *
 * @author 冯名豪
 * @date 2022-09-21
 * @since 1.0
 */
@Slf4j
public class HttpUtils {

    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * http get请求
     *
     * @param baseUrl 基础url
     * @param param   请求参数
     * @return 响应
     * @throws Exception
     */
    public static String doGet(String baseUrl, Map<String, String> param) throws Exception {
        // 拼接URL
        String realUrl = baseUrl;
        StringBuffer stringBuffer = new StringBuffer();
        if (!MapUtils.isEmpty(param)) {
            int index = 0;
            for (Map.Entry<String, String> entry : param.entrySet()) {
                if (index != 0) {
                    stringBuffer.append("&");
                }
                stringBuffer.append(entry.getKey());
                stringBuffer.append("=");
                stringBuffer.append(entry.getValue());
                index++;
            }
        }
        if (stringBuffer.length() > 0) {
            realUrl = realUrl + "?" + stringBuffer.toString();
            log.info("realUrl: {}", realUrl);
        }
        // 执行GET请求
        try {
            HttpGet httpGet = new HttpGet(realUrl);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            } finally {
                httpClient.close();
                response.close();
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return null;
    }

    /**
     * http post请求
     *
     * @param baseUrl 基础url
     * @param param   请求body为表单
     * @return 响应
     */
    public static String doPost(String baseUrl, Map<String, String> param) {
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpPost httpPost = new HttpPost(baseUrl);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity((urlEncodedFormEntity));

            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpPost);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                throw new Exception("http status = " + response.getStatusLine());
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ioException) {
                    logger.warn(ioException.getMessage());
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ioException) {
                    logger.warn(ioException.getMessage());
                }
            }
        }
    }

    /**
     * http post请求
     *
     * @param baseUrl 基础url
     * @param body    请求body为JSON
     * @return 响应
     */
    public static String doPost(String baseUrl, String body) {
        HttpPost httpPost = new HttpPost(baseUrl);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        CloseableHttpResponse response = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {
            httpEntity.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
            httpEntity.setContentLength(body.getBytes("UTF-8").length);
            httpPost.setEntity(httpEntity);
            // 设置post请求的传输、连接超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .build();
            httpPost.setConfig(requestConfig);

            response = httpClient.execute(httpPost);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                throw new Exception("http status = " + response.getStatusLine());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * post请求
     *
     * @param baseUrl
     * @param param
     * @param resultClass
     * @return 对象
     */
    public static <T> T doPost(String baseUrl, Map<String, String> param, Class<T> resultClass) {
        String result = doPost(baseUrl, param);
        return ObjectMapperUtils.readValue(result, resultClass);
    }

    /**
     * post请求
     *
     * @param baseUrl
     * @param body
     * @param resultClass
     * @return 对象
     */
    public static <T> T doPost(String baseUrl, String body, Class<T> resultClass) {
        String result = doPost(baseUrl, body);
        return ObjectMapperUtils.readValue(result, resultClass);
    }

    /**
     * get请求
     *
     * @param baseUrl
     * @param param
     * @param resultClass
     * @param <T>
     * @return 对象
     * @throws Exception
     */
    public static <T> T doGet(String baseUrl, Map<String, String> param, Class<T> resultClass) throws Exception {
        String result = doGet(baseUrl, param);
        return ObjectMapperUtils.readValue(result, resultClass);
    }

}
