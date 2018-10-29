package com.trackray.scanner.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.trackray.scanner.enums.HttpMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

public class HttpClientWrapper {



    private static CloseableHttpClient client;
    private List<ContentBody> contentBodies;
    private List<NameValuePair> nameValuePostBodies;
    private static PoolingHttpClientConnectionManager connManager = null;
    private static String reservedCharacters = "%:/?#@!$&'()*,;=-._~abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public static void enabledSSL(boolean enabled) {
        if (enabled) {
            try {
                SSLContext sslContext = SSLContexts.custom().useTLS().build();
                sslContext.init(null, new TrustManager[] { new X509TrustManager() {

                    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                            throws CertificateException {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                            throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                } }, null);
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                        .<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE)
                        .register("https", new SSLConnectionSocketFactory(sslContext)).build();
                connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
                connManager.setDefaultSocketConfig(socketConfig);
                MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
                        .setMaxLineLength(2000).build();

                ConnectionConfig connectionConfig = ConnectionConfig.custom()
                        .setMalformedInputAction(CodingErrorAction.IGNORE)
                        .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
                        .setMessageConstraints(messageConstraints).build();
                connManager.setDefaultConnectionConfig(connectionConfig);
                connManager.setMaxTotal(200);
                connManager.setDefaultMaxPerRoute(20);
            } catch (KeyManagementException e) {

            } catch (NoSuchAlgorithmException e) {

            }
        }
    }

    public HttpClientWrapper(HttpProxy proxy) {
        super();
        //client                 = HttpClientBuilder.create().build();//不使用连接池
        client = this.getCloseableHttpClient(proxy);
        this.contentBodies = new ArrayList<ContentBody>();
        this.nameValuePostBodies = new LinkedList<NameValuePair>();
    }

    private CloseableHttpClient getCloseableHttpClient(HttpProxy proxy) {
        if (null != proxy && proxy.isProxy()) {
            HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort());
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            if (null != proxy.getUser() && null != proxy.getPassword()) {
                credentialsProvider.setCredentials(new AuthScope(proxy.getHost(), proxy.getPort()),
                        new UsernamePasswordCredentials(proxy.getUser(), proxy.getPassword()));
            }
            return HttpClients.custom().setProxy(proxyHost).setDefaultCredentialsProvider(credentialsProvider).build();
        } else {
            return HttpClients.custom().setConnectionManager(connManager).build();
        }
    }

    /**
     * Get方式访问URL
     *
     * @param url
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public ResponseStatus sendRequest(String url) throws HttpException, IOException {
        return this.sendRequest(url, "UTF-8", HttpMethod.GET, null);
    }

    /**
     * Get方式访问URL
     *
     * @param url
     * @param urlEncoding
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public ResponseStatus sendRequest(String url, String urlEncoding) throws HttpException, IOException {
        return this.sendRequest(url, urlEncoding, HttpMethod.GET, null);
    }

    /**
     * POST方式发送名值对请求URL
     *
     * @param url
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public ResponseStatus postNV(String url) throws HttpException, IOException {
        return this.sendRequest(url, "UTF-8", HttpMethod.POST, null);
    }

    public ResponseStatus postNV(String url, String contentType) throws HttpException, IOException {
        return sendRequest(url, "UTF-8", HttpMethod.POST, contentType);
    }

    public CrawlerPage sendRequest(CrawlerPage crawlerPage){

        if (crawlerPage.getRequest().getUrl() == null)
            return null;

        String url = crawlerPage.getRequest().getUrl();
        /*if (crawlerPage.getRequest().getCharset() != null)
            url = HttpClientWrapper.encodeURL(url.trim(), crawlerPage.getRequest().getCharset());
        */
        URI uri = null;
        if (crawlerPage.getRequest().getCharset() != null) {
            try {
                uri = new URI(escapeURIIfNotAscii(url, crawlerPage.getRequest().getCharset()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        HttpEntity entity = null;
        HttpRequestBase request = null;
        CloseableHttpResponse response = null;
        try {
            if (HttpMethod.GET == crawlerPage.getRequest().getHttpMethod()) {
                request = new HttpGet(uri);
            } else if (HttpMethod.POST == crawlerPage.getRequest().getHttpMethod()) {
                this.parseUrl(url);
                Map<String, String> paramMap = crawlerPage.getRequest().getParamMap();
                String paramStr = crawlerPage.getRequest().getParamStr();
                HttpPost httpPost = new HttpPost(uri);
                List<NameValuePair> nvBodyList = this.getNVBodies();
                if (StringUtils.isBlank(paramStr) && paramMap!=null && !paramMap.isEmpty())
                {
                    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                        addNV(entry.getKey(),entry.getValue());
                    }
                    httpPost.setEntity(new UrlEncodedFormEntity(nvBodyList, crawlerPage.getRequest().getCharset()));
                }else if (StringUtils.isNotBlank(paramStr)){
                    httpPost.setEntity(new StringEntity(paramStr));
                }
                if (null == (httpPost.getFirstHeader("Content-Type")))
                    httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
                request = httpPost;
            }

            if (crawlerPage.getRequest().getHttpHeaders().length>1) {
                request.setHeaders(crawlerPage.getRequest().getHttpHeaders());
            }else {
                request.addHeader(HttpHeaders.USER_AGENT,
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)");
            }

            if (StringUtils.isNotBlank(crawlerPage.getRequest().getCookie())){
                request.setHeader(new BasicHeader("Cookie",crawlerPage.getRequest().getCookie()));
            }

            int timeout = crawlerPage.getRequest().getTimeout();
            if (timeout > 1) {
                RequestConfig build = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).setRedirectsEnabled(crawlerPage.isRedirect()).build();
                request.setConfig(build);
            }
            response = client.execute(request);
            /*if ((response.getStatusLine().getStatusCode() ==301 || response.getStatusLine().getStatusCode()==302) && crawlerPage.isRedirect() )
            {
                String location = response.getFirstHeader("Location").getValue();
                URI locationURI = new URI(escapeURIIfNotAscii(location, "UTF-8"));
                request.setURI(locationURI);
                response = client.execute(request);
            }*/
            entity = response.getEntity(); // 获取响应实体
            StatusLine statusLine = response.getStatusLine();
            ResponseStatus ret = new ResponseStatus();
            ret.setStatusCode(statusLine.getStatusCode());
            ret.setEncoding(crawlerPage.getRequest().getCharset());
            getResponseStatus(entity, ret);
            ret.setHeaders(response.getAllHeaders());
            //cookies
            String respCookie = "";
            Header[] cookies = response.getHeaders("Set-Cookie");
            if (cookies.length>0){
                HashMap<String, String> temp = new HashMap<>();
                for (Header cookie : cookies) {
                    String value = cookie.getValue();
                    if (value.contains(";")){
                        String[] split = value.split(";");
                        for (String s : split) {
                            if (s.contains("=")){
                                String[] sp = s.split("=");
                                String k = sp[0];
                                String v = sp[1];
                                if (k.contains("Max-Age") ||k.contains("expires") || k.contains("path") || k.contains("domain")){
                                    continue;
                                }else {
                                    temp.put(k,v);
                                }
                            }
                        }
                    }
                }
                for (Map.Entry<String, String> e : temp.entrySet()) {
                    respCookie+=e.getKey()+"="+e.getValue()+";";
                }
            }
            ret.setCookie(respCookie);

            Response resp = new Response();
            resp.setStatus(ret);
            crawlerPage.setResponse(resp);
            return crawlerPage;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("timeout "+url);
            throw new RuntimeException(e);
        } finally {
            try {
                close(entity, request, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static String escapeURIIfNotAscii(final String uri,
                                             final String charset) throws UnsupportedEncodingException {
        final StringBuilder buffer = new StringBuilder();
        boolean unchanged = true;
        char ch;

        for (int i = 0; i < uri.length(); ++i) {
            ch = uri.charAt(i);
            if (reservedCharacters.indexOf(ch) != -1) {
                buffer.append(ch);
            } else {
                unchanged = false;
                final byte[] bytes = Character.toString(ch).getBytes(charset);
                for (final byte b : bytes) {
                    buffer.append('%');
                    final char hex1 = Character.toUpperCase(Character.forDigit(
                            (b >> 4) & 0xF, 16));
                    final char hex2 = Character.toUpperCase(Character.forDigit(
                            b & 0xF, 16));
                    buffer.append(hex1);
                    buffer.append(hex2);
                }
            }
        }
        return unchanged ? uri : buffer.toString();
    }

    /**
     * 根据url编码，请求方式，请求URL
     *
     * @param urlstr
     * @param urlEncoding
     * @param bodyType
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public ResponseStatus sendRequest(String urlstr, String urlEncoding, HttpMethod bodyType, String contentType)
            throws HttpException, IOException {

        if (urlstr == null)
            return null;

        String url = urlstr;
        /*if (urlEncoding != null)
            url = HttpClientWrapper.encodeURL(url.trim(), urlEncoding);
*/
        URI uri = null;
        try {
            uri = new URI(escapeURIIfNotAscii(url, "UTF-8"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpEntity entity = null;
        HttpRequestBase request = null;
        CloseableHttpResponse response = null;
        try {
            if (HttpMethod.GET == bodyType) {
                request = new HttpGet(uri);
            } else if (HttpMethod.POST == bodyType) {
                this.parseUrl(url);
                HttpPost httpPost = new HttpPost(uri);
                List<NameValuePair> nvBodyList = this.getNVBodies();
                httpPost.setEntity(new UrlEncodedFormEntity(nvBodyList, urlEncoding));
                request = httpPost;
            }

            if (contentType != null) {
                request.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
            }

            request.addHeader(HttpHeaders.USER_AGENT,
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)");

            response = client.execute(request);
            entity = response.getEntity(); // 获取响应实体
            StatusLine statusLine = response.getStatusLine();
            ResponseStatus ret = new ResponseStatus();
            ret.setStatusCode(statusLine.getStatusCode());
            ret.setEncoding(urlEncoding);
            getResponseStatus(entity, ret);
            return ret;
        } finally {
            close(entity, request, response);
        }
    }

    private void getResponseStatus(HttpEntity entity, ResponseStatus ret) throws IOException {

        Header enHeader = entity.getContentEncoding();
        if (enHeader != null) {
            String charset = enHeader.getValue().toLowerCase();
            ret.setEncoding(charset);
        }
        String contenttype = this.getResponseStatusType(entity);
        ret.setContentType(contenttype);
        ret.setContentTypeString(this.getResponseStatusTypeString(entity));
        ret.setContentBytes(EntityUtils.toByteArray(entity));
    }

    public ResponseStatus postEntity(String url) throws HttpException, IOException {
        return this.postEntity(url, "UTF-8");
    }

    /**
     * POST方式发送名值对请求URL,上传文件（包括图片）
     *
     * @param url
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public ResponseStatus postEntity(String url, String urlEncoding) throws HttpException, IOException {
        if (url == null)
            return null;

        HttpEntity entity = null;
        HttpRequestBase request = null;
        CloseableHttpResponse response = null;
        try {
            this.parseUrl(url);
            HttpPost httpPost = new HttpPost(toUrl());

            //对请求的表单域进行填充
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (NameValuePair nameValuePair : this.getNVBodies()) {
                entityBuilder.addPart(nameValuePair.getName(),
                        new StringBody(nameValuePair.getValue(), ContentType.create("text/plain", urlEncoding)));
            }
            for (ContentBody contentBody : getContentBodies()) {
                entityBuilder.addPart("file", contentBody);
            }
            entityBuilder.setCharset(CharsetUtils.get(urlEncoding));
            httpPost.setEntity(entityBuilder.build());
            request = httpPost;
            response = client.execute(request);

            //响应状态
            StatusLine statusLine = response.getStatusLine();
            // 获取响应对象
            entity = response.getEntity();
            ResponseStatus ret = new ResponseStatus();
            ret.setStatusCode(statusLine.getStatusCode());
            getResponseStatus(entity, ret);
            return ret;
        } finally {
            close(entity, request, response);
        }
    }

    private void close(HttpEntity entity, HttpRequestBase request, CloseableHttpResponse response) throws IOException {
        if (request != null)
            request.releaseConnection();
        if (entity != null)
            entity.getContent().close();
        if (response != null)
            response.close();
    }

    public NameValuePair[] getNVBodyArray() {
        List<NameValuePair> list = this.getNVBodies();
        if (list == null || list.isEmpty())
            return null;
        NameValuePair[] nvps = new NameValuePair[list.size()];
        Iterator<NameValuePair> it = list.iterator();
        int count = 0;
        while (it.hasNext()) {
            NameValuePair nvp = it.next();
            nvps[count++] = nvp;
        }
        return nvps;
    }

    public List<NameValuePair> getNVBodies() {
        return Collections.unmodifiableList(this.nameValuePostBodies);
    }

    private String getResponseStatusType(HttpEntity method) {
        Header contenttype = method.getContentType();
        if (contenttype == null)
            return null;
        String ret = null;
        try {
            HeaderElement[] hes = contenttype.getElements();
            if (hes != null && hes.length > 0) {
                ret = hes[0].getName();
            }
        } catch (Exception e) {
        }
        return ret;
    }

    private String getResponseStatusTypeString(HttpEntity method) {
        Header contenttype = method.getContentType();
        if (contenttype == null)
            return null;
        return contenttype.getValue();
    }

    static Set<Character> BEING_ESCAPED_CHARS = new HashSet<Character>();
    static {
        char[] signArray = { ' ', '\\', '‘', ']', '!', '^', '#', '`', '$', '{', '%', '|', '}', '(', '+', ')', '<', '>',
                ';', '[' };
        for (int i = 0; i < signArray.length; i++) {
            BEING_ESCAPED_CHARS.add(new Character(signArray[i]));
        }
    }

    public static String encodeURL(String url, String encoding) {
        if (url == null)
            return null;
        if (encoding == null)
            return url;

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c == 10) {
                continue;
            } else if (BEING_ESCAPED_CHARS.contains(new Character(c)) || c == 13 || c > 126) {
                try {
                    sb.append(URLEncoder.encode(String.valueOf(c), encoding));
                } catch (Exception e) {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString().replaceAll("\\+", "%20");
    }

    private String protocol;
    private String host;
    private int port;
    private String dir;
    private String uri;
    private final static int DefaultPort = 80;
    private final static String ProtocolSeparator = "://";
    private final static String PortSeparator = ":";
    private final static String HostSeparator = "/";
    private final static String DirSeparator = "/";

    private void parseUrl(String url) {
        this.protocol = null;
        this.host = null;
        this.port = DefaultPort;
        this.dir = "/";
        this.uri = dir;

        if (url == null || url.length() == 0)
            return;
        String u = url.trim();
        boolean MeetProtocol = false;
        int pos = u.indexOf(ProtocolSeparator);
        if (pos > 0) {
            MeetProtocol = true;
            this.protocol = u.substring(0, pos);
            pos += ProtocolSeparator.length();
        }
        int posStartDir = 0;
        if (MeetProtocol) {
            int pos2 = u.indexOf(PortSeparator, pos);
            if (pos2 > 0) {
                this.host = u.substring(pos, pos2);
                pos2 = pos2 + PortSeparator.length();
                int pos3 = u.indexOf(HostSeparator, pos2);
                String PortStr = null;
                if (pos3 > 0) {
                    PortStr = u.substring(pos2, pos3);
                    posStartDir = pos3;
                } else {
                    int pos4 = u.indexOf("?");
                    if (pos4 > 0) {
                        PortStr = u.substring(pos2, pos4);
                        posStartDir = -1;
                    } else {
                        PortStr = u.substring(pos2);
                        posStartDir = -1;
                    }
                }
                try {
                    this.port = Integer.parseInt(PortStr);
                } catch (Exception e) {
                }
            } else {
                pos2 = u.indexOf(HostSeparator, pos);
                if (pos2 > 0) {
                    this.host = u.substring(pos, pos2);
                    posStartDir = pos2;
                } else {
                    this.host = u.substring(pos);
                    posStartDir = -1;
                }
            }

            pos = u.indexOf(HostSeparator, pos);
            pos2 = u.indexOf("?");
            if (pos > 0 && pos2 > 0) {
                this.uri = u.substring(pos, pos2);
            } else if (pos > 0 && pos2 < 0) {
                this.uri = u.substring(pos);
            }
        }

        if (posStartDir >= 0) {
            int pos2 = u.lastIndexOf(DirSeparator, posStartDir);
            if (pos2 > 0) {
                this.dir = u.substring(posStartDir, pos2 + 1);
            }
        }

    }

    private String toUrl() {
        StringBuffer ret = new StringBuffer();
        if (this.protocol != null) {
            ret.append(this.protocol);
            ret.append(ProtocolSeparator);
            if (this.host != null)
                ret.append(this.host);
            if (this.port != DefaultPort) {
                ret.append(PortSeparator);
                ret.append(this.port);
            }
        }
        ret.append(this.uri);
        return ret.toString();
    }

    public void addNV(String name, String value) {
        BasicNameValuePair nvp = new BasicNameValuePair(name, value);
        this.nameValuePostBodies.add(nvp);
    }

    public void clearNVBodies() {
        this.nameValuePostBodies.clear();
    }

    public List<ContentBody> getContentBodies() {
        return contentBodies;
    }

}