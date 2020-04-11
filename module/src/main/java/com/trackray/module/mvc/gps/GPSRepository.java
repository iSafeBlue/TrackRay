package com.trackray.module.mvc.gps;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/4/11 16:44
 */
@Repository
public interface GPSRepository extends CrudRepository<GpsData, Long> {
}
