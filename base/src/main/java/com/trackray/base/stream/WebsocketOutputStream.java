package com.trackray.base.stream;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2020/2/3 17:21
 */
public class WebsocketOutputStream extends PrintStream {

    private WebSocketSession webSocketSession;

    public WebsocketOutputStream(OutputStream out , WebSocketSession webSocketSession) {
        super(out);
        this.webSocketSession = webSocketSession;

    }

    @Override
    public void write(byte[] buf, int off, int len) {
        if (webSocketSession!=null) {
            try {
                Charset cs = Charset.forName("UTF-8");
                ByteBuffer bb = ByteBuffer.allocate(buf.length);
                bb.put(buf);
                bb.flip();
                CharBuffer cb = cs.decode(bb);
                char[] array = cb.array();
                StringBuffer buffer = new StringBuffer();
                buffer.append(array, off, len);
                TextMessage textMessage = new TextMessage(buffer.toString());
                webSocketSession.sendMessage(textMessage);
            } catch (IOException e) {
                BinaryMessage binaryMessage = new BinaryMessage(buf, off, len, false);
                try {
                    webSocketSession.sendMessage(binaryMessage);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
