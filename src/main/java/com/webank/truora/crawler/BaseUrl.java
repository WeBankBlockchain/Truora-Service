package com.webank.truora.crawler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class BaseUrl {
    String name;
    String url;
    public BaseUrl(){}
    public BaseUrl(String name_, String url_){
        this.name = name_;
        this.url = url_;
    }
    public String toJSONString() throws JsonProcessingException {
        //JSONObject res = (JSONObject)JSONObject.toJSON(this);
        String jsonString = new ObjectMapper().writeValueAsString(this);
        return jsonString;
    }
}
