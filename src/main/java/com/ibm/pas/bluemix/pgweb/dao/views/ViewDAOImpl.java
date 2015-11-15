package com.ibm.pas.bluemix.pgweb.dao.views;

import com.ibm.pas.bluemix.pgweb.beans.Result;
import com.ibm.pas.bluemix.pgweb.dao.PGWebDAOUtil;
import com.ibm.pas.bluemix.pgweb.main.BluemixPGWebException;
import com.ibm.pas.bluemix.pgweb.utils.AdminUtil;
import com.ibm.pas.bluemix.pgweb.utils.JDBCUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewDAOImpl implements ViewDAO
{
    protected static Logger logger = Logger.getLogger("controller");

    @Override
    public List<View> retrieveViewList(String schema, String search, String userKey) throws BluemixPGWebException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement stmtForSchema = null;
        ResultSet rset = null;
        List<View>        views = null;
        String            srch = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);

            stmtForSchema = conn.createStatement();
            int result = stmtForSchema.executeUpdate(String.format("set search_path='%s'", schema));

            stmt = conn.prepareStatement(Constants.USER_VIEWS);
            if (search == null)
                srch = "%";
            else
                srch = "%" + search + "%";

            stmt.setString(1, schema);
            stmt.setString(2, srch);
            rset = stmt.executeQuery();

            views = makeViewListFromResultSet(rset);
        }
        catch (SQLException se)
        {
            logger.info("Error retrieving all views with search string = " + search);
            throw new BluemixPGWebException(se);
        }
        catch (Exception ex)
        {
            logger.info("Error retrieving all views with search string = " + search);
            throw new BluemixPGWebException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return views;
    }

    @Override
    public Result simpleviewCommand(String schemaName, String viewName, String type, String userKey) throws BluemixPGWebException
    {
        String            command = null;
        Result            res     = null;

        if (type != null)
        {
            if (type.equalsIgnoreCase("DROP"))
            {
                if (schemaName.equalsIgnoreCase("public"))
                {
                    command = String.format(Constants.DROP_VIEW_PUBLIC, viewName);
                }
                else
                {
                    command = String.format(Constants.DROP_VIEW, schemaName, viewName);
                }
            }
        }

        res = PGWebDAOUtil.runCommand(command, userKey);

        return res;
    }

    @Override
    public String getViewDefinition(String schemaName, String viewName, String userKey) throws BluemixPGWebException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet         rset = null;
        String            def = null;

        try
        {
            conn = AdminUtil.getConnection(userKey);
            stmt = conn.prepareStatement(Constants.USER_VIEW_DEF);
            stmt.setString(1, schemaName);
            stmt.setString(2, viewName);
            rset = stmt.executeQuery();

            rset.next();

            def = rset.getString(1);

        }
        catch (SQLException se)
        {
            logger.info("Error retrieving view definition for view = " + viewName);
            throw new BluemixPGWebException(se);
        }
        catch (Exception ex)
        {
            logger.info("Error retrievingview definition for view = " + viewName);
            throw new BluemixPGWebException(ex);
        }
        finally
        {
            // close all resources
            JDBCUtil.close(rset);
            JDBCUtil.close(stmt);
        }

        return def;

    }

    private List<View> makeViewListFromResultSet (ResultSet rset) throws SQLException
    {
        List<View> views = new ArrayList<View>();

        while (rset.next())
        {
            View view = new View(rset.getString(1),
                    rset.getString(2),
                    rset.getString(3));
            views.add(view);
        }

        return views;

    }
}
