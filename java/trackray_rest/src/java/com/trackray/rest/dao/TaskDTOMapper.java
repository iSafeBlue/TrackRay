package com.trackray.rest.dao;

import java.util.List;

import com.trackray.rest.dto.TaskDTO;
import com.trackray.rest.dto.TaskDTOExample;
import com.trackray.rest.dto.TaskDTOWithBLOBs;
import org.apache.ibatis.annotations.Param;

public interface TaskDTOMapper {
    long countByExample(TaskDTOExample example);

    int deleteByExample(TaskDTOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TaskDTOWithBLOBs record);

    int insertSelective(TaskDTOWithBLOBs record);

    List<TaskDTOWithBLOBs> selectByExampleWithBLOBs(TaskDTOExample example);

    List<TaskDTO> selectByExample(TaskDTOExample example);

    TaskDTOWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TaskDTOWithBLOBs record, @Param("example") TaskDTOExample example);

    int updateByExampleWithBLOBs(@Param("record") TaskDTOWithBLOBs record, @Param("example") TaskDTOExample example);

    int updateByExample(@Param("record") TaskDTO record, @Param("example") TaskDTOExample example);

    int updateByPrimaryKeySelective(TaskDTOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TaskDTOWithBLOBs record);

    int updateByPrimaryKey(TaskDTO record);
}