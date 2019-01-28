package com.trackray.web.repository;

import com.trackray.web.dto.TaskDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskDTO, Long> {
    List<TaskDTO> findAllByTaskMd5(String taskMd5);
    TaskDTO findTaskDTOByTaskMd5 (String taskMd5);
}