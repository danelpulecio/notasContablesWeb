package co.com.bbva.app.notas.contables.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;



public class Session implements Serializable {

    private static final long serialVersionUID = -69586361724916109L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);
    private static final String DEFAULT_TRACE_LOG = "REMOTE_IP=%s HOST_IP=%s [%s]:";

    private String traceId;
    private String user;
    private String remoteIp;
    private String hostIp;
    private String traceLog;

    private String generateTraceId(){
        return UUID.randomUUID().toString();
    }

    private String getLocalhostIpAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException err) {
            LOGGER.error("Failed trying to get localhost ip: [{}]", err.getMessage());
            return null;
        }
    }

    public Session() {
        // default constructor
    }

    public Session(String user, String remoteIp) {
        this.user = user.trim();
        this.remoteIp = remoteIp;
        this.traceId = generateTraceId();
        this.hostIp = getLocalhostIpAddress();
        this.traceLog = String.format(DEFAULT_TRACE_LOG, this.remoteIp, this.hostIp, this.traceId);
    }

    public String getTraceId() {
        return traceId;
    }

    public String getUser() {
        return user;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public String getHostIp() {
        return hostIp;
    }

    public String getTraceLog() {
        return traceLog;
    }
}
