package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.session.Session;

import java.sql.Timestamp;

public class SessionTraceDto extends CommonVO<SessionTraceDto> {

    private String traceId;
    private int userId;
    private String userCode;
    private String remoteIp;
    private String hostIp;
    private Timestamp loginDate;
    private int loginSuccess;
    private Timestamp lastWritten;

    public SessionTraceDto() {
        // default constructor
    }

    public SessionTraceDto(Session session) {
        this.traceId = session.getTraceId();
        this.userCode = session.getUser();
        this.remoteIp = session.getRemoteIp();
        this.hostIp = session.getHostIp();
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public Timestamp getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Timestamp loginDate) {
        this.loginDate = loginDate;
    }

    public int getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess ? 1 : 0;
    }

    public Timestamp getLastWritten() {
        return lastWritten;
    }

    public void setLastWritten(Timestamp lastWritten) {
        this.lastWritten = lastWritten;
    }

    @Override
    public Object getPK() {
        return this.traceId;
    }
}
