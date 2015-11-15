package com.ibm.pas.bluemix.pgweb.dao.tables;

import com.ibm.pas.bluemix.pgweb.beans.Result;
import com.ibm.pas.bluemix.pgweb.dao.PGWebDAOUtil;
import com.ibm.pas.bluemix.pgweb.main.BluemixPGWebException;
import com.ibm.pas.bluemix.pgweb.utils.AdminUtil;
import com.ibm.pas.bluemix.pgweb.utils.JDBCUtil;
import org.apache.log4j.Logger;

import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAOImpl implements TableDAO
{
    protected static Logger logger = Logger.getLogger("controller");

    @Override
    public List<Table> retrieveTableList(String schema, String search, String userKey) throws BluemixPGWebException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement stmtForSchema = null;
        ResultSet rset = null;
        List<Table>       tbls = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmtForSchema = conn.createStatement();
            int result = stmtForSchema.executeUpdate(String.format("set search_path='%s'", schema));

            stmt = conn.prepareStatement(Constants.USER_TABLES);

            if (search == null)
                srch = "%";
            else
                srch = "%" + search + "%";


            stmt.setString(1, schema);
            stmt.setString(2, srch);
            rset = stmt.executeQuery();

            tbls = makeTableListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.info("Error retrieving all tables with search string = " + search);
            throw new BluemixPGWebException(se);
        }
        catch (Exception ex)
        {
            logger.info("Error retrieving all tables with search string = " + search);
            throw new BluemixPGWebException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return tbls;
    }

    @Override
    public Result simpletableCommand(String schemaName, String tableName, String type, String userKey) throws BluemixPGWebException
    {
        String            command = null;
        Result            res     = null;

        if (type != null)
        {
            if (type.equalsIgnoreCase("DROP"))
            {
                if (schemaName.equalsIgnoreCase("public"))
                {
                    command = String.format(Constants.DROP_TABLE_PUBLIC, tableName);
                }
                else
                {
                    command = String.format(Constants.DROP_TABLE, schemaName, tableName);
                }
            }
            else if (type.equalsIgnoreCase("EMPTY"))
            {
                if (schemaName.equalsIgnoreCase("public"))
                {
                    command = String.format(Constants.TRUNCATE_TABLE_PUBLIC, tableName);
                }
                else
                {
                    command = String.format(Constants.TRUNCATE_TABLE, schemaName, tableName);
                }
            }

        }

        res = PGWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    @Override
    public javax.servlet.jsp.jstl.sql.Result getTableStructure(String schema, String tableName, String userKey) throws BluemixPGWebException
    {
        Connection        conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        javax.servlet.jsp.jstl.sql.Result res = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.USER_TAB_COLUMNS);
            stmt.setString(1, schema);
            stmt.setString(2, tableName);
            rset = stmt.executeQuery();

            res = ResultSupport.toResult(rset);

        }
        catch (SQLException se)
        {
            logger.info("Error retrieving table structure for table " + tableName);
            throw new BluemixPGWebException(se);
        }
        catch (Exception ex) {
            logger.info("Error retrieving table structure for table  " + tableName);
            throw new BluemixPGWebException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return res;
    }

    private List<Table> makeTableListFromResultSet (ResultSet rset) throws SQLException
    {
        List<Table> tbls = new ArrayList<Table>();

        while (rset.next())
        {
            Table table = new Table(rset.getString(1),
                                    rset.getString(2),
                                    rset.getString(3));

            tbls.add(table);
        }

        return tbls;

    }
}
