/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Controllers;

import com.mycompany.mavenproject1.Model.UrlForMonitorng;
import com.mycompany.mavenproject1.Service.interfaces.MonitoringService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author SashaFfg
 */
@RequestScoped
@WebServlet("/add-new-url")
public class AddNewUrl extends HttpServlet {

    @Inject
    MonitoringService monitoringService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddNewUrl</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<form action=\"add-new-url\" method=\"POST\">"
                    + "    url: <input required name=\"url\" type=\"url\" />"
                    + "    <br>"
                    + "    </br>"
                    + "    expected response code: <input required name=\"expectedResponseCode\" type=\"number\" />"
                    + "    <br>"
                    + "    </br>"
                    + "    request resend delay: "
                    + "    <input required name=\"requestResendDelay\" type=\"number\" min=\"1\" max=\"3600\"/>"
                    + "    <br>"
                    + "    </br>"
                    + "    string to search(50 chars): "
                    + "    <input name=\"subStringToSearch\" maxlenght=\"50\" value=\"\"/>"
                    + "    <br>"
                    + "    </br>"
                    + "    min response size: "
                    + "    <input required value=\"0\" name=\"minResponseSize\" type=\"number\" min=\"0\" />"
                    + "    <br>"
                    + "    </br>"
                    + "    max required size: "
                    + "    <input required value=\"0\" name=\"maxResponseSize\" type=\"number\" min=\"1\" />"
                    + "    <br>"
                    + "    </br>"
                    + "    min response time for OK: "
                    + "    <input readonly value=\"0\" name=\"minResponseTimeForOk\" type=\"number\" min=\"0\" />"
                    + "    <br>"
                    + "    </br>"
                    + "    max response time for OK and min esponse time for WARNING: "
                    + "    <input required name=\"maxResponseTimeForOk\" type=\"number\" min=\"1\" />  "
                    + "    <br>"
                    + "    </br>"
                    + "    max response time for WARNING and min esponse time for CRITICAL: "
                    + "    <input required name=\"maxResponseTimeForWarning\" type=\"number\" min=\"2\" />   "
                    + "    <input type=\"submit\" value=\"Add\" />\n"
                    + "</form>"
                    + "<form action=\"result-table\" method=\"GET\">"
                    + " <button>Back</button>"
                    + "</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            monitoringService.addNewUrlForMonitoring(new UrlForMonitorng(
                    request.getParameter("url"),
                    true,
                    Integer.parseInt(request.getParameter("expectedResponseCode")),
                    Integer.parseInt(request.getParameter("requestResendDelay")),
                    Integer.parseInt(request.getParameter("minResponseSize")),
                    Integer.parseInt(request.getParameter("maxResponseSize")),
                    request.getParameter("subStringToSearch"),
                    Integer.parseInt(request.getParameter("minResponseTimeForOk")),
                    Integer.parseInt(request.getParameter("maxResponseTimeForOk")),
                    Integer.parseInt(request.getParameter("maxResponseTimeForWarning"))));
        } catch (NamingException ex) {
            Logger.getLogger(AddNewUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

        String path = "/result-table";
        response.sendRedirect(request.getContextPath() + path);
    }
}


