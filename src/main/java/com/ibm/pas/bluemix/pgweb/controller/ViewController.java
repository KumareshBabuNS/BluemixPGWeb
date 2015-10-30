package com.ibm.pas.bluemix.pgweb.controller;

import com.ibm.pas.bluemix.pgweb.beans.Result;
import com.ibm.pas.bluemix.pgweb.dao.PGWebDAOFactory;
import com.ibm.pas.bluemix.pgweb.dao.PGWebDAOUtil;
import com.ibm.pas.bluemix.pgweb.dao.views.View;
import com.ibm.pas.bluemix.pgweb.dao.views.ViewDAO;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ViewController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/views", method = RequestMethod.GET)
    public String showViews
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

        int startAtIndex = 0, endAtIndex = 0;
        String schema = null;

        logger.info("Received request to show views");

        ViewDAO viewDAO = PGWebDAOFactory.getViewDAO();
        Result result = new Result();

        String viewAction = request.getParameter("viewAction");
        String selectedSchema = request.getParameter("selectedSchema");
        logger.info("selectedSchema = " + selectedSchema);

        if (selectedSchema != null)
        {
            schema = selectedSchema;
        }
        else
        {
            schema = (String)session.getAttribute("schema");
        }

        logger.debug("schema = " + schema);

        if (viewAction != null)
        {
            logger.debug("viewAction = " + viewAction);

            if (viewAction.equals("DEF"))
            {
                String def =
                        viewDAO.getViewDefinition
                                (schema,
                                        (String)request.getParameter("viewName"),
                                        (String)session.getAttribute("user_key"));

                model.addAttribute("viewName", (String)request.getParameter("viewName"));
                model.addAttribute("viewdef", def);
            }
            else
            {
                result = null;
                result =
                        viewDAO.simpleviewCommand
                                (schema,
                                        (String)request.getParameter("viewName"),
                                        viewAction,
                                        (String)session.getAttribute("user_key"));

                model.addAttribute("result", result);
            }
        }

        List<View> views = viewDAO.retrieveViewList
                (schema,
                        null,
                        (String)session.getAttribute("user_key"));

        model.addAttribute("records", views.size());
        model.addAttribute("estimatedrecords", views.size());
        model.addAttribute("views", views);

        model.addAttribute("schemas",
                PGWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

        return "views";
    }

    @RequestMapping(value = "/views", method = RequestMethod.POST)
    public String performViewAction
            (Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception
    {
        String schema = null;
        int startAtIndex = 0, endAtIndex = 0;

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

        Result result = new Result();
        List<View> views = null;

        logger.info("Received request to perform an action on the views");

        String selectedSchema = request.getParameter("selectedSchema");
        logger.info("selectedSchema = " + selectedSchema);

        if (selectedSchema != null)
        {
            schema = selectedSchema;
        }
        else
        {
            schema = (String)session.getAttribute("schema");
        }

        logger.info("schema = " + schema);

        ViewDAO viewDAO = PGWebDAOFactory.getViewDAO();
        if (request.getParameter("search") != null)
        {
            views = viewDAO.retrieveViewList
                    (schema,
                            (String)request.getParameter("search"),
                            (String)session.getAttribute("user_key"));

            model.addAttribute("search", (String)request.getParameter("search"));
        }
        else
        {
            String[] tableList  = request.getParameterValues("selected_view[]");
            String   commandStr = request.getParameter("submit_mult");

            logger.info("tableList = " + Arrays.toString(tableList));
            logger.info("command = " + commandStr);

            // start actions now if tableList is not null

            if (tableList != null)
            {
                List al = new ArrayList<Result>();
                for (String view: tableList)
                {
                    result = null;
                    result =
                            viewDAO.simpleviewCommand
                                    (schema,
                                            view,
                                            commandStr,
                                            (String)session.getAttribute("user_key"));
                    al.add(result);
                }

                model.addAttribute("arrayresult", al);
            }

            views = viewDAO.retrieveViewList
                    (schema,
                            null,
                            (String)session.getAttribute("user_key"));

        }

        model.addAttribute("records", views.size());
        model.addAttribute("estimatedrecords", views.size());
        model.addAttribute("views", views);
        model.addAttribute("schemas",
                PGWebDAOUtil.getAllSchemas
                        ((String) session.getAttribute("user_key")));

        model.addAttribute("chosenSchema", schema);

        return "views";
    }
}
