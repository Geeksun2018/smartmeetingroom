package cn.hephaestus.smartmeetingroom.service;

import cn.hephaestus.smartmeetingroom.model.AgentEvent;


public interface AgentEventService {
    public boolean addAgentEvent(AgentEvent agentEvent);

    public boolean deleteAgenEvent(int uid,int id);

    public AgentEvent[] getAllAgentEvent(int uid);

    public boolean alterAgentEvent(AgentEvent agentEvent);
}
