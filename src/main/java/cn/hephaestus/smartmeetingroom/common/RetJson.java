package cn.hephaestus.smartmeetingroom.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class RetJson {
    int code;
    String err;
    Map data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public static RetJson succcess(Map map){
        RetJson retJson=new RetJson();
        retJson.setCode(0);
        retJson.setData(map);
        return retJson;
    }

    public static RetJson fail(int code,String mesg){
        RetJson retJson=new RetJson();
        retJson.setCode(code);
        retJson.setErr(mesg);
        return retJson;
    }

    @Override
    public String toString() {
        ObjectMapper mapper=new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }
}
