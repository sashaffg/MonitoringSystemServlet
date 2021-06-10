/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Service;

import com.mycompany.mavenproject1.Model.UrlForMonitorng;
import com.mycompany.mavenproject1.Service.interfaces.MonitoringService;
import com.mycompany.mavenproject1.dao.implementation.MonitoringRepositoryImpl;
import com.mycompany.mavenproject1.dao.interfaces.MonitoringRepository;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.ws.rs.core.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author SashaFfg
 */
public class MonitoringServiceImpl implements MonitoringService {
    
    @Inject
    MonitoringRepository monitoringRepository;
    Timer time = new Timer();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * runs url check
     */
    @Override
    public void startCheckingService() {
        MonitoringScheduledTask mst = new MonitoringScheduledTask();
        time.schedule(mst, 0, 100);
    }

    /**
     *
     * @param id
     * @throws ServletException
     */
    @Override
    public void changeActivity(int id) throws ServletException {
        monitoringRepository.changeUrlForMonitorngActivity(id);
    }

    /**
     *
     * @param urlForMonitorng
     * @throws ServletException
     * @throws IOException
     * @throws NamingException
     */
    @Override
    public void addNewUrlForMonitoring(UrlForMonitorng urlForMonitorng) throws ServletException, IOException, NamingException {
        monitoringRepository.addUrlForMonitorng(urlForMonitorng);

        UrlMonitoringThread urlMonitoringThread = new UrlMonitoringThread(urlForMonitorng);
        urlMonitoringThread.start();
    }

    /**
     *
     * @param id
     * @throws ServletException
     */
    @Override
    public void removeUrlForMonitorng(int id) throws ServletException {
        monitoringRepository.removeUrlForMonitorng(id);
    }

    /**
     *
     * @param url
     * @return returns a row of monitoring results in the form of a row of a
     * table
     */
    @Override
    public String getObjectTableRow(UrlForMonitorng url) {
        StringBuilder result = new StringBuilder();
        result.append("<tr>");
        if (url.getLastUrlResponse() == null) {
            result.append("<td>").append(url.getUrl()).append("</td>")
                    .append("<td> - ms</td><td> - </td><td> - </td><td> - </td><td> - </td>")
                    .append("<td>").append(url.getIsActive()).append("</td>")
                    .append("<td><form action=\"result-table\" method=\"POST\"> <button value=\"")
                    .append(MonitoringRepositoryImpl.inMemoryUrls.indexOf(url))
                    .append("\" name=\"urlToChangeActivity\" >Change activity</button></form></td>")
                    .append("<td><form action=\"result-table\" method=\"POST\"> <button value=\"")
                    .append(MonitoringRepositoryImpl.inMemoryUrls.indexOf(url)).append("\" name=\"urlToDelete\" >Delete</button></form></td>");
        } else {
            result.append("<td>").append(url.getUrl())
                    .append("</td><td>")
                    .append(url.getLastUrlResponse().getFirstHeader("responseTime").getValue()).append("ms</td>")
                    .append("<td>").append(url.getLastUrlResponse().getStatusLine().toString()).append("</td>")
                    .append("<td>").append(url.getLastUrlResponse().getFirstHeader("requestSendTime").getValue()).append("</td>")
                    .append("<td>").append(url.getUrlStatusForView()).append("</td>")
                    .append("<td>").append(url.getResponseSize()).append("</td>")
                    .append("<td>").append(url.getIsActive()).append("</td>");
            if (url.isSearchSubString()) {
                result.append("<td>").append("word ")
                        .append(url.getSubStringToSearch()).append(" exists: ")
                        .append(url.isSubStringToSearchExists()).append("</td>");
            }
            result.append("<td><form action=\"result-table\" method=\"POST\"> <button value=\"")
                    .append(MonitoringRepositoryImpl.inMemoryUrls.indexOf(url)).append("\" name=\"urlToChangeActivity\" >Change activity</button></form></td>")
                    .append("<td><form action=\"result-table\" method=\"POST\"> <button value=\"")
                    .append(MonitoringRepositoryImpl.inMemoryUrls.indexOf(url)).append("\" name=\"urlToDelete\" >Delete</button></form></td>");
        }
        result.append("</tr>");
        return result.toString();
    }

    /**
     *
     * @param url
     * @return response to the request at url
     * @throws IOException
     */
    @Override
    public CloseableHttpResponse getResponse(String url) throws IOException {

        HttpGet requestTo = new HttpGet(url);
        requestTo.addHeader("custom-key", "mkyong");
        requestTo.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        Long startTime = new Date().getTime();
        CloseableHttpResponse responseFromUrl = httpClient.execute(requestTo);
        Long endTime = new Date().getTime();

        Long resTime = endTime - startTime;
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss a zzz");
        responseFromUrl.addHeader("requestSendTime", formatForDateNow.format(startTime));
        responseFromUrl.addHeader("responseTime", resTime.toString());
        return responseFromUrl;
    }

}
