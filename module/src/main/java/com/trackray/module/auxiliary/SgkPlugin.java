package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.CommonPlugin;
import com.trackray.module.inner.SgkSearch;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/6/24 13:30
 */
@Plugin(title = "信息泄露查询")
@Rule(
        params = {
                @Param(key = "email" , desc = "邮箱地址")
        }
)
public class SgkPlugin extends CommonPlugin {
    @Override
    public boolean check(Map param) {
        sgkSearch.getParam().putAll(param);
        return true;
    }

    @Autowired
    private SgkSearch sgkSearch;

    @Override
    public Object start() {
        sgkSearch.process();
        return sgkSearch.result();
    }
}
