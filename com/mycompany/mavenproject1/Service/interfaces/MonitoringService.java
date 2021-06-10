/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Service.interfaces;

import com.mycompany.mavenproject1.Model.UrlForMonitorng;
import java.io.IOException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 *
 * @author SashaFfg
 */
public interface MonitoringService {

    public void startCheckingService();

    public void changeActivity(int id) throws ServletException;

    public void addNewUrlForMonitoring(UrlForMonitorng urlForMonitorng) throws ServletException, IOException, NamingException;

    public void removeUrlForMonitorng(int id) throws ServletException;

    public String getObjectTableRow(UrlForMonitorng url);

    public CloseableHttpResponse getResponse(String url) throws IOException;
}
