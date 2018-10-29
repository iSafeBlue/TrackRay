package com.trackray.rest.dao;

import com.trackray.rest.dto.VulnDTO;
import com.trackray.rest.dto.VulnDTOExample;
import com.trackray.rest.dto.VulnDTOWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VulnDTOMapper {
    long countByExample(VulnDTOExample example);

    int deleteByExample(VulnDTOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VulnDTOWithBLOBs record);

    int insertSelective(VulnDTOWithBLOBs record);

    List<VulnDTOWithBLOBs> selectByExampleWithBLOBs(VulnDTOExample example);

    List<VulnDTO> selectByExample(VulnDTOExample example);

    VulnDTOWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VulnDTOWithBLOBs record, @Param("example") VulnDTOExample example);

    int updateByExampleWithBLOBs(@Param("record") VulnDTOWithBLOBs record, @Param("example") VulnDTOExample example);

    int updateByExample(@Param("record") VulnDTO record, @Param("example") VulnDTOExample example);

    int updateByPrimaryKeySelective(VulnDTOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(VulnDTOWithBLOBs record);

    int updateByPrimaryKey(VulnDTO record);
}