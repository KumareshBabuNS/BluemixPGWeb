package com.ibm.pas.bluemix.pgweb.controller;

import com.ibm.pas.bluemix.pgweb.utils.AdminUtil;
import com.ibm.pas.bluemix.pgweb.utils.ConnectionManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

@Controller
public class ConmapController
{
    protected static Logger logger = Logger.getLogger("controller");

    @RequestMapping(value = "/viewconmap", method = RequestMethod.GET)
    public String viewConnections
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

        logger.info("Received request to show connection map");

        ConnectionManager cm = ConnectionManager.getInstance();

        String conMapAction = request.getParameter("conMapAction");
        String key = request.getParameter("key");

        logger.info("conMapAction = " + conMapAction);
        logger.info("key = " + key);

        if (conMapAction != null)
        {
            if (conMapAction.equalsIgnoreCase("DELETE"))
            {
                // remove this connection from Map and close it.
                cm.removeConnection(key);
                logger.info("Connection closed for key " + key);
                model.addAttribute("saved", "Successfully closed connection with key " + key);
            }
        }

        model.addAttribute("conmap", cm.getConnectionMap());
        model.addAttribute("conmapsize", cm.getConnectionListSize());

        return "conmap";
    }
}
