package com.trackray.module.mvc.nmap;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/4/11 16:32
 */
@Repository
public interface NmapRepository extends CrudRepository<NmapData, Long> {
    public NmapData findNmapDataByTaskid(String taskid);
}
