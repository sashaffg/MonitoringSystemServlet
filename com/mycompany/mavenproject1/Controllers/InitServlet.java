/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Controllers;

import com.mycompany.mavenproject1.Service.interfaces.MonitoringService;
import com.mycompany.mavenproject1.dao.implementation.MonitoringRepositoryImpl;
import com.mycompany.mavenproject1.dao.interfaces.MonitoringRepository;
import java.io.IOException;
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
@WebServlet("/result")
public class InitServlet extends HttpServlet {

    @Inject
    MonitoringService monitoringService;
    @Inject
    MonitoringRepository monitoringRepository;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = "/result-table";
        MonitoringRepositoryImpl.inMemoryUrls.addAll(monitoringRepository.getAllUrlForMonitorng());
        monitoringService.startCheckingService();
        response.sendRedirect(request.getContextPath() + path);
    }

}
