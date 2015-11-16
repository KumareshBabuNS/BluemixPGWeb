package com.ibm.pas.bluemix.pgweb.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ConnectionManager
{

    protected static Logger logger = Logger.getLogger("controller");
    private Map<String,PostgresConnection> conList = new HashMap<String,PostgresConnection>();
    private static ConnectionManager instance = null;

    static
    {
        instance = new ConnectionManager();
    }

    private ConnectionManager()
    {
        // Exists only to defeat instantiation.
    }

    public static ConnectionManager getInstance() throws Exception
    {
        return instance;
    }

    public void addConnection (PostgresConnection conn, String key)
    {
        conList.put(key, conn);
        logger.info("Connection added with key " + key);
    }

    public Connection getConnection (String key)
    {
        try
        {
            return conList.get(key).getConn();
        }
        catch (Exception ex)
        {
            return null;
        }

    }

    public void updateConnection(Connection conn, String key)
    {
        PostgresConnection postgresConn = conList.get(key);
        postgresConn.setConn(conn);
        conList.put(key, postgresConn);
        logger.info("Connection updated with key " + key);
    }

    public void removeConnection(String key) throws SQLException
    {
        if (conList.containsKey(key))
        {
            Connection conn = getConnection(key);
            if (conn != null)
            {
                conn.close();
                conn = null;
            }

            conList.remove(key);
            logger.info("Connection removed with key " + key);
        }
        else
        {
            logger.info("No connection with key " + key + " exists");
        }
    }

    public Map <String,PostgresConnection> getConnectionMap()
    {
        return conList;
    }

    public int getConnectionListSize ()
    {
        return conList.size();
    }

    public String displayMap ()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("-- Current Connection List --\n\n");
        sb.append("Size = " + getConnectionListSize() + "\n\n");
        for (String key : conList.keySet())
        {
            sb.append(String.format("Key %s, Connection %s\n", key, getConnection(key)));
        }

        return sb.toString();
    }
}
