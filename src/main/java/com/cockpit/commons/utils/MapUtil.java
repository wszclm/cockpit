package com.cockpit.commons.utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    public static String mapToString(Map<String, String[]> paramMap) {

        if (paramMap == null) {
            return "";
        }
        Map<String, Object> params = new HashMap<>(16);
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            String obj;
            String key = param.getKey();
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
             obj = MapUtil.endWithIgnoreCase(param.getKey(), "password") ? "I am password" : paramValue;
            params.put(key, obj);
        }
        return new Gson().toJson(params);
    }


    public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endWith(str, suffix, true);
    }



    public static boolean endWith(CharSequence str, CharSequence suffix, boolean isIgnoreCase) {
        if (null != str && null != suffix) {
            return isIgnoreCase ? str.toString().toLowerCase().endsWith(suffix.toString().toLowerCase()) : str.toString().endsWith(suffix.toString());
        } else {
            return null == str && null == suffix;
        }
    }
}
