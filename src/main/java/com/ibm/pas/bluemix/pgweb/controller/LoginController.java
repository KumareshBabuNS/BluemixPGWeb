package com.ibm.pas.bluemix.pgweb.controller;

import com.ibm.pas.bluemix.pgweb.beans.Login;
import com.ibm.pas.bluemix.pgweb.beans.UserPref;
import com.ibm.pas.bluemix.pgweb.utils.AdminUtil;
import com.ibm.pas.bluemix.pgweb.utils.ConnectionManager;
import com.ibm.pas.bluemix.pgweb.utils.PostgresConnection;
import com.ibm.pas.bluemix.pgweb.utils.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.Map;

@Controller
public class LoginController
{
    protected static Logger logger = Logger.getLogger("controller");

    @Autowired
    UserPref userPref;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login(Model model, HttpSession session) throws Exception
    {
        logger.info("Received request to show login page");
        model.addAttribute("loginObj", new Login("", "", "jdbc:postgresql://localhost:5432/apples"));
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login
            (@RequestParam(value="username", required=true) String username,
             @RequestParam(value="password", required=true) String password,
             @RequestParam(value="url", required=true) String url,
             Model model,
             HttpSession session) throws Exception
    {
        logger.info("Received request to login");
        ConnectionManager cm = ConnectionManager.getInstance();
        Connection conn;

        Login loginObj = new Login(username, password, url);

        logger.info("url {" + loginObj.getUrl() + "}");
        logger.info("user {" + loginObj.getUsername() + "}");

        try
        {
            conn = AdminUtil.getNewConnection
                    (url, username, password);

            conn.setAutoCommit(true);

            PostgresConnection newConn =
                    new PostgresConnection
                            (conn,
                                    url,
                                    new java.util.Date().toString(),
                                    username.toUpperCase());

            cm.addConnection(newConn, session.getId());

            session.setAttribute("user_key", session.getId());
            session.setAttribute("user", username.toUpperCase());
            session.setAttribute("schema", "public");
            session.setAttribute("url", url);
            session.setAttribute("prefs", userPref);
            session.setAttribute("history", new LinkedList());
            session.setAttribute("connectedAt", new java.util.Date().toString());

            Map<String, String> schemaMap = AdminUtil.getSchemaMap();

            schemaMap = QueryUtil.populateSchemaMap
                    (conn, schemaMap, "public");

            session.setAttribute("schemaMap", schemaMap);

            logger.info("schemaMap=" + schemaMap);
            logger.info(userPref.toString());

            return "main";
        }
        catch (Exception ex)
        {
            model.addAttribute("loginerror", ex.getMessage());
            model.addAttribute("loginObj");
            return "login";
        }
    }
}
