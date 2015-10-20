package com.ibm.pas.bluemix.pgweb.controller;

import com.ibm.pas.bluemix.pgweb.beans.Result;
import com.ibm.pas.bluemix.pgweb.dao.PGWebDAOFactory;
import com.ibm.pas.bluemix.pgweb.dao.tables.Table;
import com.ibm.pas.bluemix.pgweb.dao.tables.TableDAO;
import com.ibm.pas.bluemix.pgweb.utils.AdminUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.List;

@Controller
public class TableController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public String showTables
            (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        if (session.getAttribute("user_key") == null)
        {
            logger.info("user_key is null new Login required");
            response.sendRedirect("/");
            return null;
        }
        else
        {
            Connection conn = AdminUtil.getConnection((String) session.getAttribute("user_key"));
            if (conn == null )
            {
                response.sendRedirect("/");
                return null;
            }
            else
            {
                if (conn.isClosed())
                {
                    response.sendRedirect("/");
                    return null;
                }
            }

        }

        String schema = null;

        logger.info("Received request to show tables");

        TableDAO tableDAO = PGWebDAOFactory.getTableDAO();

        String selectedSchema = request.getParameter("selectedSchema");
        logger.info("selectedSchema = " + selectedSchema);

        if (selectedSchema != null)
        {
            schema = selectedSchema;
        }
        else
        {
            schema = (String) session.getAttribute("schema");
        }

        logger.info("schema = " + schema);

        String tabAction = request.getParameter("tabAction");
        Result result = new Result();

        if (tabAction != null)
        {
            logger.debug("tabAction = " + tabAction);
            result = null;

            if (tabAction.equalsIgnoreCase("STRUCTURE"))
            {
                //TODO: add code
            }
            else
            {
                result =
                        tableDAO.simpletableCommand
                                (schema,
                                        (String)request.getParameter("tabName"),
                                        tabAction,
                                        (String)session.getAttribute("user_key"));
                model.addAttribute("result", result);
            }
        }

        List<Table> tbls = tableDAO.retrieveTableList
                  (schema, null, (String)session.getAttribute("user_key"));

        model.addAttribute("records", tbls.size());
        model.addAttribute("estimatedrecords", tbls.size());
        model.addAttribute("tables", tbls);
        model.addAttribute("chosenSchema", schema);

        return "tables";
    }
}
