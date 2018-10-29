package com.trackray.rest.dao;

import com.trackray.rest.dto.FingerDTO;
import com.trackray.rest.dto.FingerDTOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FingerDTOMapper {
    long countByExample(FingerDTOExample example);

    int deleteByExample(FingerDTOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FingerDTO record);

    int insertSelective(FingerDTO record);

    List<FingerDTO> selectByExample(FingerDTOExample example);

    FingerDTO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FingerDTO record, @Param("example") FingerDTOExample example);

    int updateByExample(@Param("record") FingerDTO record, @Param("example") FingerDTOExample example);

    int updateByPrimaryKeySelective(FingerDTO record);

    int updateByPrimaryKey(FingerDTO record);
}