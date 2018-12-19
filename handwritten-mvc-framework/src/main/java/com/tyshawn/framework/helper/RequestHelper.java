package com.tyshawn.framework.helper;

import com.google.common.collect.Maps;
import com.tyshawn.framework.bean.Param;
import com.tyshawn.framework.util.CodecUtil;
import com.tyshawn.framework.util.StreamUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

/**
 * 请求助手类
 */
public final class RequestHelper {

    /**
     * 获取请求参数
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        Map<String, Object> paramMap = Maps.newHashMap();
        Enumeration<String> paramNames = request.getParameterNames();
       if (!paramNames.hasMoreElements()){
           return null;
       }

        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String fieldValue = request.getParameter(fieldName);
            paramMap.put(fieldName, fieldValue);
        }

        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtils.isNotEmpty(body)) {
            String[] kvs = StringUtils.split(body, "&");
            if (ArrayUtils.isNotEmpty(kvs)) {
                for (String kv : kvs) {
                    String[] array = StringUtils.split(kv, "=");
                    if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        paramMap.put(fieldName, fieldValue);
                    }
                }
            }
        }

        return new Param(paramMap);
    }

}
