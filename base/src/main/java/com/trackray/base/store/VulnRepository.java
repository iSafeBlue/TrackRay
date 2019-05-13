package com.trackray.base.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VulnRepository extends CrudRepository<VulnDTO, String> {
    List<VulnDTO> findAllByTaskMd5(String taskMd5);
    List<VulnDTO> deleteAllByTaskMd5(String taskMd5);
    List<VulnDTO> removeAllByTaskMd5(String taskMd5);
}