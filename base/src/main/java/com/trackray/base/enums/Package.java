package com.trackray.base.enums;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/2/8 14:50
 */
public enum Package {
    exploit,
    auxiliary,
    poc,


    windows(exploit),
    linux(exploit),
    app(exploit),

    smb(windows),
    rdp(windows),

    ssh(linux),

    mysql(exploit),


    phpcms(poc),
    dedecms(poc),

    ;

    Package() {
    }

    Package(Package ...exploit) {

    }

}
