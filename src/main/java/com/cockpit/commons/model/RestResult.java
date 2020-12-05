package com.cockpit.commons.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@JsonSerialize
public class RestResult<T> implements Serializable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(RestResult.class);


    private static final String OK = "200";
    private static final String successMsg = "OK";
    private static final String ERROR = "500";

    // 元数据
    private Meta meta;

    @JSONField(
            ordinal = 1
    )
    private int code;
    @JSONField(
            ordinal = 2
    )
    private String message;
    @JSONField(
            ordinal = 3
    )
    private T info;

    public RestResult() {
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(int code,String msg) {
        if (code == 200){
            this.meta = new Meta(msg, OK);
        }
        else {
            this.meta = new Meta(msg, String.valueOf(code));;
        }
    }

    public RestResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestResult(int code, String message, T info) {
        this.code = code;
        this.message = message;
        this.info = info;
    }

    public T getInfo() {
        return this.info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public String toString() {
        return "RestResult(code=" + this.code + ", message=" + this.message + ", data=" + JSON.toJSONString(this.info) + ")";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 元数据
     * @author wucl5
     *
     */
    public class Meta{

        private String statusCd;

        private String message;

        public Meta(boolean isSuccess){
        }

        public Meta(String message){
            this.message = message;
        }

        public Meta(String message, String statusCd){
            this.message = message;
            this.statusCd = statusCd;
        }

        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatusCd() {
            return statusCd;
        }

        public void setStatusCd(String statusCd) {
            this.statusCd = statusCd;
        }



    }
}