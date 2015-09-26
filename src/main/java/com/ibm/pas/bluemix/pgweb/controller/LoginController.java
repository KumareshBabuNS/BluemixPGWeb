package com.ibm.pas.bluemix.pgweb.controller;

import com.ibm.pas.bluemix.pgweb.beans.Login;
import com.ibm.pas.bluemix.pgweb.utils.AdminUtil;
import com.ibm.pas.bluemix.pgweb.utils.ConnectionManager;
import com.ibm.pas.bluemix.pgweb.utils.PostgresConnection;
import com.ibm.pas.bluemix.pgweb.utils.QueryUtil;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.Map;

@Controller
public class LoginController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login
            (@ModelAttribute("loginAttribute") Login loginAttribute,
             Model model,
             HttpSession session) throws Exception
    {
        logger.debug("Received request to login");
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn;

        logger.debug("url {" + loginAttribute.getUrl() + "}");
        logger.debug("user {" + loginAttribute.getUsername() + "}");
        //logger.debug("password {" + loginAttribute.getPassword() + "}");

        try
        {
            conn = AdminUtil.getNewConnection
                    (loginAttribute.getUrl(),
                            loginAttribute.getUsername(),
                            loginAttribute.getPassword());

            PostgresConnection newConn =
                    new PostgresConnection
                            (conn,
                                    loginAttribute.getUrl(),
                                    new java.util.Date().toString(),
                                    loginAttribute.getUsername().toUpperCase());

            cm.addConnection(newConn, session.getId());

            session.setAttribute("user_key", session.getId());
            session.setAttribute("user", loginAttribute.getUsername());
            session.setAttribute("schema", "public");
            session.setAttribute("url", loginAttribute.getUrl());
            //session.setAttribute("prefs", new UserPref());
            session.setAttribute("history", new LinkedList());
            session.setAttribute("connectedAt", new java.util.Date().toString());

            Map<String, String> schemaMap = AdminUtil.getSchemaMap();

            schemaMap = QueryUtil.populateSchemaMap
                    (conn, schemaMap, "public");

            session.setAttribute("schemaMap", schemaMap);

            logger.info(loginAttribute);

            return "main";
        }
        catch (Exception ex)
        {
            model.addAttribute("error", ex.getMessage());
            // This will resolve to /WEB-INF/jsp/loginpage.jsp
            return "loginpage";
        }
    }
}
