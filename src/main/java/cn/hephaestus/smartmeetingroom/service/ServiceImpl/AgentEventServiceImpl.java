package cn.hephaestus.smartmeetingroom.service.ServiceImpl;

import cn.hephaestus.smartmeetingroom.mapper.AgentEventMapper;
import cn.hephaestus.smartmeetingroom.model.AgentEvent;
import cn.hephaestus.smartmeetingroom.service.AgentEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentEventServiceImpl implements AgentEventService {
    @Autowired
    AgentEventMapper agentEventMapper;
    @Override
    public boolean addAgentEvent(AgentEvent agentEvent) {
        return agentEventMapper.addAgentEvent(agentEvent);
    }

    @Override
    public boolean deleteAgenEvent(int uid, int id) {
        return agentEventMapper.deleteAgentEvent(uid,id);
    }

    @Override
    public AgentEvent[] getAllAgentEvent(int uid) {
        return agentEventMapper.getAllAgentEvents(uid);
    }

    @Override
    public AgentEvent getAgentById(int uid,int id) {
        return agentEventMapper.getAgentEventById(id,uid);
    }

    @Override
    public boolean alterAgentEvent(AgentEvent agentEvent) {
        AgentEvent a=getAgentById(agentEvent.getUid(),agentEvent.getId());
        if (a==null){
            return false;
        }
        if (agentEvent.getContent()!=null){
            a.setContent(agentEvent.getContent());
        }
        if (agentEvent.getStartTime()!=null){
            a.setStartTime(agentEvent.getStartTime());
        }
        if (agentEvent.getEndTime()!=null){
            a.setEndTime(agentEvent.getEndTime());
        }
        a.setState(agentEvent.isState());
        return agentEventMapper.alterAgentEvent(a);
    }
}
