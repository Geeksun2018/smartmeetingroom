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
        return agentEventMapper.getAgentEvent(uid);
    }

    @Override
    public boolean alterAgentEvent(AgentEvent agentEvent) {
        return agentEventMapper.alterAgentEvent(agentEvent);
    }
}
