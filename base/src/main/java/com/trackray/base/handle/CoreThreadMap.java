package com.trackray.base.handle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务核心线程集合
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/8 12:28
 */
public class CoreThreadMap extends ConcurrentHashMap<String,Map<String, Object>> {

}
