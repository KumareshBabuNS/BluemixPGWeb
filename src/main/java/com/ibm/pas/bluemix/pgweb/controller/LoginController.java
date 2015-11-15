package com.ibm.pas.bluemix.pgweb.controller;

import com.ibm.pas.bluemix.pgweb.beans.Login;
import com.ibm.pas.bluemix.pgweb.beans.UserPref;
import com.ibm.pas.bluemix.pgweb.utils.AdminUtil;
import com.ibm.pas.bluemix.pgweb.utils.ConnectionManager;
import com.ibm.pas.bluemix.pgweb.utils.PostgresConnection;
import com.ibm.pas.bluemix.pgweb.utils.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
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

        String jsonString = null;
        jsonString = System.getenv().get("VCAP_SERVICES");

        if (jsonString != null)
        {
            if (jsonString.length() > 0)
            {
                try
                {
                    ConnectionManager cm = ConnectionManager.getInstance();
                    Connection conn;

                    // check whether PostgreSQL service exists
                    // FIRST = ElephantSQL service

                    logger.info("** Attempting login using VCAP_SERVICES **");
                    logger.info(jsonString);

                    Login login = parseLoginCredentials(jsonString);

                    logger.info(login);

                    conn = AdminUtil.getNewConnection
                            (login.getUrl(), login.getUsername(), login.getPassword());

                    conn.setAutoCommit(true);

                    PostgresConnection newConn =
                            new PostgresConnection
                                    (conn, login.getUrl(), new java.util.Date().toString(), login.getUsername().toUpperCase());

                    cm.addConnection(newConn, session.getId());

                    session.setAttribute("user_key", session.getId());
                    session.setAttribute("user", login.getUsername().toUpperCase());
                    session.setAttribute("schema", "public");
                    session.setAttribute("url", login.getUrl());
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
                    // we tried if we can't auto login , just present login screen
                    model.addAttribute("loginObj", new Login("", "", "jdbc:postgresql://localhost:5432/apples"));
                    logger.info("Auto Login via VCAP_SERVICES Failed - " + ex.getMessage());
                }

            }
        }
        else
        {
            model.addAttribute("loginObj", new Login("", "", "jdbc:postgresql://localhost:5432/apples"));
        }

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

    private Login parseLoginCredentials (String jsonString)
    {
        Login login = new Login();
        JsonParser parser = JsonParserFactory.getJsonParser();

        Map<String, Object> jsonMap = parser.parseMap(jsonString);

        List clearDBList = (List) jsonMap.get("elephantsql");
        Map clearDBMap = (Map) clearDBList.get(0);
        Map credentailsMap = (Map) clearDBMap.get("credentials");

        String uri = (String) credentailsMap.get("uri");
        String url = null, username = null, password = null;

        int firstcolon = uri.indexOf(':');
        int firstatsymbol = uri.indexOf('@');

        String subStr = uri.substring(11);
        String subStr2 = uri.substring(firstatsymbol + 1);

        username = subStr.substring(0, subStr.indexOf(':'));
        password = subStr.substring(subStr.indexOf(':') + 1, subStr.indexOf('@'));

        //url = "jdbc:postgresql://" + subStr2.substring(0, subStr2.indexOf(':'));
        url = "jdbc:postgresql://" + subStr2;

        login.setUsername(username);
        login.setPassword(password);
        login.setUrl(url);

        return login;

    }
}
