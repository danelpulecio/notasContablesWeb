package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.SessionTraceDto;

import java.sql.Connection;
import java.sql.ResultSet;

public class SessionTraceDao extends CommonDAO<SessionTraceDto> {

    private static String TABLE = "NC_SESSION_TRACE";
    private static String COLUMNS = "TRACE_ID, USER_ID, USER_CODE, REMOTE_IP, HOST_IP, LOGIN_DATE, LOGIN_SUCCESS, LAST_WRITTEN";
    private static String PRIMARY_KEY = "TRACE_ID";

    private static String SQL_INSERT_SENTENCE = "INSERT INTO NC_SESSION_TRACE " +
            "(TRACE_ID, USER_CODE, REMOTE_IP, HOST_IP) " +
            "VALUES (?, ?, ?, ?)";
    private static String SQL_UPDATE_SENTENCE = "UPDATE NC_SESSION_TRACE " +
            "SET USER_ID = ?, LOGIN_SUCCESS = ? WHERE TRACE_ID = ?";

    public SessionTraceDao() {
        super(TABLE, COLUMNS, PRIMARY_KEY, new SessionTraceDto());
    }

    public void saveTrace(SessionTraceDto sessionTrace) throws Exception {
        Connection conn = null;
        try {
            conn = getConexion(false);
            executeUpdate(conn, SQL_INSERT_SENTENCE, new Object[]{sessionTrace.getTraceId(), sessionTrace.getUserCode(),
                    sessionTrace.getRemoteIp(), sessionTrace.getHostIp()});
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    public void updateTrace(SessionTraceDto sessionTrace) throws Exception {
        Connection conn = null;
        try {
            conn = getConexion(false);
            executeUpdate(conn, SQL_UPDATE_SENTENCE, new Object[]{sessionTrace.getUserId(), sessionTrace.getLoginSuccess(),
                    sessionTrace.getTraceId()});
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    @Override
    protected void internalUpdate(Connection con, SessionTraceDto row) throws Exception {
        // unused
    }

    @Override
    protected Object[] getDataToAdd(Connection con, SessionTraceDto row) throws Exception {
        return new Object[0];
    }

    @Override
    protected SessionTraceDto getResultSetToVO(ResultSet result) throws Exception {
        return null;
    }
}
