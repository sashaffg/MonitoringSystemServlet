/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Service;

import com.mycompany.mavenproject1.Model.UrlForMonitorng;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SashaFfg
 */
public class UrlMonitoringThread extends Thread {

    private final MonitoringServiceImpl monitoringService;
    UrlForMonitorng urlForMonitorng;

    public UrlMonitoringThread(UrlForMonitorng urlForMonitoring) {
        this.monitoringService = new MonitoringServiceImpl();
        this.urlForMonitorng = urlForMonitoring;
    }

    @Override
    public void run() {
        try {
            urlForMonitorng.setLastUrlResponse(monitoringService.getResponse(urlForMonitorng.getUrl()));
            urlForMonitorng.setRequestResendTime(new Date());
            this.interrupt();
        } catch (IOException ex) {
            Logger.getLogger(UrlMonitoringThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
