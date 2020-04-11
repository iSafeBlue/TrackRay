package com.trackray.base.burpsuite.pojo.configuration;

/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class ProjectOptions {

    private Connections connections;
    private Http http;
    private Misc misc;
    private Sessions sessions;
    private Ssl ssl;
    public void setConnections(Connections connections) {
         this.connections = connections;
     }
     public Connections getConnections() {
         return connections;
     }

    public void setHttp(Http http) {
         this.http = http;
     }
     public Http getHttp() {
         return http;
     }

    public void setMisc(Misc misc) {
         this.misc = misc;
     }
     public Misc getMisc() {
         return misc;
     }

    public void setSessions(Sessions sessions) {
         this.sessions = sessions;
     }
     public Sessions getSessions() {
         return sessions;
     }

    public void setSsl(Ssl ssl) {
         this.ssl = ssl;
     }
     public Ssl getSsl() {
         return ssl;
     }

}