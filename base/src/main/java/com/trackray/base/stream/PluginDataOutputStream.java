package com.trackray.base.stream;

import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/2/4 15:54
 */
public class PluginDataOutputStream extends PrintStream {

    private StringBuffer stringBuffer;
    public PluginDataOutputStream(OutputStream out,StringBuffer stringBuffer) {
        super(out);
        this.stringBuffer = stringBuffer;
    }

    public void write(byte[] buf, int off, int len) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(buf.length);
        bb.put(buf);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        char[] array = cb.array();
        StringBuffer buffer = new StringBuffer();
        buffer.append(array, off, len);
        stringBuffer.append(array,off,len);

    }


}
