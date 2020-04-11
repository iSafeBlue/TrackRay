package com.trackray.module.mvc.mindMap;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/4/11 16:41
 */
@Repository
public interface MindMapRepository extends CrudRepository<MindMapData, Long> {


}
