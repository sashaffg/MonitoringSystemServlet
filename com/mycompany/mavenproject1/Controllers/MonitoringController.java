/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Controllers;

import com.mycompany.mavenproject1.Model.UrlForMonitorng;
import com.mycompany.mavenproject1.Service.interfaces.MonitoringService;
import com.mycompany.mavenproject1.dao.implementation.MonitoringRepositoryImpl;
import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author SashaFfg
 */
@WebServlet("/result-table")
public class MonitoringController extends HttpServlet {

    @Inject
    MonitoringService monitoringService;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Refresh", "5");
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MonitoringResoultServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Results </h1>");
            out.println("<table border=\"1\">");
            out.println("<tr>\n"
                    + "    <th>Url</th>\n"
                    + "    <th>Response time</th>\n"
                    + "    <th>Status Line</th>\n"
                    + "    <th>Last request</th>\n"
                    + "    <th>Url status</th>\n"
                    + "    <th>Response size in Bytes</th>\n"
                    + "    <th>Is monitorng active</th>\n"
                    + "   </tr>");
            for (UrlForMonitorng url : MonitoringRepositoryImpl.inMemoryUrls) {
                out.println(monitoringService.getObjectTableRow(url));
            }
            out.println("</table>");
            out.println("<form action=\"add-new-url\" method=\"GET\">"
                    + " <button>Add new URL</button>"
                    + "</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("urlToChangeActivity") != null) {
            monitoringService.changeActivity(Integer.parseInt(request.getParameter("urlToChangeActivity")));
        } else if (request.getParameter("urlToDelete") != null) {
            monitoringService.removeUrlForMonitorng(Integer.parseInt(request.getParameter("urlToDelete")));
        }
        doGet(request, response);
    }
}
