package cn.hephaestus.smartmeetingroom.mapper;

import cn.hephaestus.smartmeetingroom.model.AgentEvent;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AgentEventMapper {
    @Insert("insert into agent_event(uid,content,startTime,endTime,status) values(#{uid},#{content},#{startTime},#{endTime},#{status})")
    public boolean addAgentEvent(AgentEvent agentEvent);

    @Update("delete from agent_event where id=#{id} and uid=#{uid}")
    public boolean deleteAgentEvent(@Param("uid") int uid, @Param("id") int id);

    @Update("update agent_event set content=#{content},startTime=#{startTime},endTime=#{endTime},status=#{status} where id=#{id} and uid=#{uid}")
    public boolean alterAgentEvent(AgentEvent agentEvent);

    @Select("select * from agent_event where uid=#{uid}")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    public AgentEvent[] getAgentEvent(int uid);
}
