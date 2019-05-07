package com.trackray.web.repository;

import com.trackray.web.dto.TaskDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends CrudRepository<TaskDTO, Long> {
    List<TaskDTO> findAllByTaskMd5(String taskMd5);
    TaskDTO findTaskDTOByTaskMd5 (String taskMd5);
    List<TaskDTO> findAllByUserEquals(String user);
}