/*
 *    Copyright 2011-2012 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.ut.biolab.medsavant.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.ut.biolab.medsavant.clientapi.ClientCallbackAdapter;

/**
 *
 * @author mfiume
 */
public class ConnectionController {

    private static final Log LOG = LogFactory.getLog(ConnectionController.class);
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String PROPS = "enableQueryTimeouts=false";//"useCompression=true"; //"useCompression=true&enableQueryTimeouts=false";
    private static final Map<String, ConnectionPool> sessionPoolMap = new HashMap<String, ConnectionPool>();
    private static final Map<String, ClientCallbackAdapter> sessionCallbackMap = new HashMap<String, ClientCallbackAdapter>();

    private static String dbHost;
    private static int dbPort = -1;

    public static void setHost(String value) {
        dbHost = value;
    }

    public static void setPort(int value) {
        dbPort = value;
    }

    private static String getHost() {
        return dbHost;
    }

    private static int getPort() {
        return dbPort;
    }
    
    static String getConnectionString(String host, int port, String db) {
        return String.format("jdbc:mysql://%s:%d/%s?%s", host, port, db, PROPS);
    }
    
    static String getConnectionString(String db) {
        return getConnectionString(dbHost, dbPort, db);
    }

    public static Connection connectOnce(String host, int port, String db, String user, String pass) throws SQLException {
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception ex) {
            if (ex instanceof ClassNotFoundException || ex instanceof InstantiationException) {
                throw new SQLException("Unable to load MySQL driver.");
            }
        }

        return DriverManager.getConnection(getConnectionString(host, port, db), user, pass);
    }

    public static Connection connectPooled(String sessID) throws SQLException {
        synchronized (sessionPoolMap) {
            return sessionPoolMap.get(sessID).getConnection();
        }
    }
    
    public static ResultSet executeQuery(String sid, String query) throws SQLException {
        Connection conn = connectPooled(sid);
        try {
            return conn.createStatement().executeQuery(query);
        } finally {
            conn.close();
        }
    }
    
    public static void executeUpdate(String sessID, String query) throws SQLException {
        Connection conn = connectPooled(sessID);
        try {
            conn.createStatement().executeUpdate(query);
        } finally {
            conn.close();
        }
    }
    
    public static void execute(String sessID, String query) throws SQLException {
        Connection conn = connectPooled(sessID);
        try {
            conn.createStatement().execute(query);
        } finally {
            conn.close();
        }
    }

    /**
     * Utility method to make it easier to execute SELECT-style queries.
     *
     * @param stmt a query, possibly containing '?' placeholder elements
     * @param args arguments for the placeholders
     * @return a ResultSet containing the results of the query
     * @throws SQLException
     */
    public static ResultSet executePreparedQuery(String sessID, String query, Object... args) throws SQLException {
        Connection conn = connectPooled(sessID);
        try {
            return executePreparedQuery(conn, query, args);
        } finally {
            conn.close();
        }
    }

    /**
     * Utility method to make it easier to execute SELECT-style queries.
     *
     * @param stmt a query, possibly containing '?' placeholder elements
     * @param args arguments for the placeholders
     * @return a ResultSet containing the results of the query
     * @throws SQLException
     */
    public static ResultSet executePreparedQuery(Connection conn, String query, Object... args) throws SQLException {
        PreparedStatement st = conn.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            st.setObject(i + 1, args[i]);
        }
        return st.executeQuery();
    }

    /**
     * Utility method to make it easier to execute data-manipulation calls which don't
     * return a result.
     *
     * @param stmt a query, possibly containing '?' placeholder elements
     * @param args arguments for the placeholders
     * @throws SQLException
     */
    public static void executePreparedUpdate(String sessID, String query, Object... args) throws SQLException {
        Connection conn = connectPooled(sessID);
        try {
            executePreparedUpdate(conn, query, args);
        } finally {
            conn.close();
        }
    }

    /**
     * Utility method to make it easier to execute data-manipulation calls which don't
     * return a result.
     *
     * @param stmt a query, possibly containing '?' placeholder elements
     * @param args arguments for the placeholders
     * @throws SQLException
     */
    public static void executePreparedUpdate(Connection conn, String query, Object... args) throws SQLException {
        PreparedStatement st = conn.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            st.setObject(i + 1, args[i]);
        }
        LOG.debug(query);
        st.executeUpdate();
    }

    /**
     * Register credentials for the given session.
     */
    public static void registerCredentials(String sessID, String user, String pass, String db) throws SQLException {
        LOG.debug(String.format("ConnectionController.registerCredentials(%s, %s, %s, %s)", sessID, user, pass, db));
        ConnectionPool pool = new ConnectionPool(db, user, pass);
        LOG.debug(String.format("sc=%s", pool));
        synchronized (sessionPoolMap) {
            sessionPoolMap.put(sessID, pool);

            Connection c = null;
            try {
                c = pool.getConnection();
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
    }

    /*
     * Make sure you get a new connection after this!
     */
    public static void switchDatabases(String sessID, String db) {
        synchronized (sessionPoolMap) {
            sessionPoolMap.get(sessID).setDBName(db);
        }
    }

    public static String getDBName(String sessID) {
        synchronized (sessionPoolMap) {
            return sessionPoolMap.get(sessID).getDBName();
        }
    }

    public static String getUserForSession(String sessID) {
        synchronized (sessionPoolMap) {
            return sessionPoolMap.get(sessID).getUser();
        }
    }

    public static void removeSession(String sessID) throws SQLException {
        synchronized (sessionPoolMap) {
            ConnectionPool pool = sessionPoolMap.remove(sessID);
            pool.close();
        }
    }

    public static Collection<String> getSessionIDs() {
        synchronized(sessionPoolMap) {
            return sessionPoolMap.keySet();
        }
    }
    
    public static Collection<String> getDBNames() {
        List<String> result = new ArrayList<String>();
        for (ConnectionPool pool : sessionPoolMap.values()){
            if (!result.contains(pool.getDBName())){
                result.add(pool.getDBName());
            }
        }
        return result;
    }

    public static void addCallback(String sid, ClientCallbackAdapter c){
        sessionCallbackMap.put(sid, c);
    }

    public static void removeCallback(String sid){
        sessionCallbackMap.remove(sid);
    }

    public static ClientCallbackAdapter getCallback(String sid) {
        return sessionCallbackMap.get(sid);
    }
}
