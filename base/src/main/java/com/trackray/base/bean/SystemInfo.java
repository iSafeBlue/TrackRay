package com.trackray.base.bean;

import com.trackray.base.entity.PortEntity;
import com.trackray.base.entity.SystemEntity;
import com.trackray.base.enums.FingerPrint;
import com.trackray.base.enums.Language;
import com.trackray.base.enums.WEBServer;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
@Data
public class SystemInfo {

    private Set<PortEntity> ports = new HashSet<>();

    private SystemEntity systemOS;

    private volatile WEBServer webServer;

    private volatile Language language = Language.OTHER;

    private volatile FingerPrint finger = FingerPrint.unknown;

    private Map<String,Integer> dirs = new HashMap<>();

}
