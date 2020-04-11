package com.trackray.base.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/1/4 15:23
 */
@Repository
public interface SettingRepository extends CrudRepository<SettingDTO, Integer> {

    List<SettingDTO> findAllByPluginKey(String pluginKey);
    SettingDTO findSettingDTOByFormNameAndPluginKey(String formName,String pluginKey);

}
