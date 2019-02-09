package cn.hephaestus.smartmeetingroom.controller;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.model.AgentEvent;
import cn.hephaestus.smartmeetingroom.service.AgentEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RestController
public class AgentEventController {

    @Autowired
    AgentEventService agentEventService;


    @RequestMapping("/addAgentEvent")
    public RetJson addAgentTime( AgentEvent agentEvent, HttpServletRequest request){
        Integer uid= (Integer) request.getAttribute("id");
        agentEvent.setUid(uid);

        if (agentEventService.addAgentEvent(agentEvent)){
            return RetJson.succcess(null);
        }else {
            return RetJson.fail(-1,"添加失败");
        }
    }

    @RequestMapping("/deleteAgentEvent")
    public RetJson deleteAgentTime(int id, HttpServletRequest request){
        Integer uid= (Integer) request.getAttribute("id");
        if (agentEventService.deleteAgenEvent(uid,id)){
            return RetJson.succcess(null);
        }else {
            return RetJson.fail(-1,"删除失败");
        }
    }

    @RequestMapping("/alterAgentEvent")
    public RetJson alterAgentTime(AgentEvent agentEvent, HttpServletRequest request){
        Integer uid= (Integer) request.getAttribute("id");
        agentEvent.setUid(uid);
        if (agentEventService.alterAgentEvent(agentEvent)){
            return RetJson.succcess(null);
        }else{
            return RetJson.fail(-1,"修改失败");
        }
    }

    @RequestMapping("/getAgentEvent")
    public RetJson getAgentTime(HttpServletRequest request){
        Integer uid= (Integer) request.getAttribute("id");
        AgentEvent[] agentEvents = agentEventService.getAllAgentEvent(uid);
        return RetJson.succcess("agentTimeList", agentEvents);
    }

}
