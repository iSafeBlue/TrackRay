package com.trackray.web.repository;

import com.trackray.web.dto.VulnDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VulnRepository extends CrudRepository<VulnDTO, String> {
    List<VulnDTO> findAllByTaskMd5(String taskMd5);
}