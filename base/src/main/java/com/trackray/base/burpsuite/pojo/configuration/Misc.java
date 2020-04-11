package com.trackray.base.burpsuite.pojo.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Auto-generated: 2020-01-09 18:42:19
 *
 * @author www.jsons.cn 
 * @website http://www.jsons.cn/json2java/ 
 */
public class Misc {

    @JsonProperty("collaborator_server")
    private CollaboratorServer collaboratorServer;
    private Logging logging;
    @JsonProperty("scheduled_tasks")
    private ScheduledTasks scheduledTasks;
    public void setCollaboratorServer(CollaboratorServer collaboratorServer) {
         this.collaboratorServer = collaboratorServer;
     }
     public CollaboratorServer getCollaboratorServer() {
         return collaboratorServer;
     }

    public void setLogging(Logging logging) {
         this.logging = logging;
     }
     public Logging getLogging() {
         return logging;
     }

    public void setScheduledTasks(ScheduledTasks scheduledTasks) {
         this.scheduledTasks = scheduledTasks;
     }
     public ScheduledTasks getScheduledTasks() {
         return scheduledTasks;
     }

}