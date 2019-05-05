package com.trackray.base.entity;

import com.trackray.base.enums.SystemOS;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemEntity {

    private SystemOS os;
    private String version;

}
