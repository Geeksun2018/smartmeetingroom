package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RedisSession;
import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.OrganizationInfo;
import cn.hephaestus.smartmeetingroom.service.OrganizationService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用来管理一个企业或者学校机构账号，通过该控制器注册的账号权限均为root
 */
@RestController
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;
    //企业修改信息
    @RequestMapping("/alterorganization")
    public RetJson alterOrganization(OrganizationInfo organizationInfo){
        organizationInfo.setId(1);
        Boolean b=organizationService.alterOne(organizationInfo);
        if (b){
            return RetJson.succcess(null);
        }
        return RetJson.fail(-1,"修改信息失败");
    }

//    @RequestMapping("/test")
//    public RetJson test(HttpServletRequest request){
//        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJpc3MiOiJTZXJ2aWNlIiwic2Vzc2lvbmlkIjoiNTE4M2FmNTYtMjlkNy00YWEzLTlmMWEtMTQxMjkwY2EyNDU0IiwiZXhwIjoxNTQ1OTEyNjMxLCJ1dWlkIjoiNjJiYTgzNjYtMDBhMi00NWY1LWI1NzEtYzIwYmVjOGI4ZjU1IiwiaWF0IjoxNTQ1MzA3ODMxfQ.OzHE8RZ4bTSOShbJsMFaHMQo_wBqrj3jzuNvlVmXLA8";
//        RedisSession redisSession=RedisSession.getRedisSession(token);
//        redisSession.setAttribute("haha","123");
//        redisSession.getAttribute("haha");
//        redisSession.removeAttribute("haha");
//        return RetJson.succcess(null);
//    }
}
