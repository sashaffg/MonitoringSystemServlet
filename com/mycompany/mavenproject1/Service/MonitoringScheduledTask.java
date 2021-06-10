/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.Service;

import com.mycompany.mavenproject1.Model.UrlForMonitorng;
import com.mycompany.mavenproject1.dao.implementation.MonitoringRepositoryImpl;
import java.util.Date;
import java.util.TimerTask;

/**
 *
 * @author SashaFfg
 */
public class MonitoringScheduledTask extends TimerTask {

    @Override
    public void run() {
        for (UrlForMonitorng urlForMonitorng : MonitoringRepositoryImpl.inMemoryUrls) {
            if ((urlForMonitorng.getRequestResendTime().before(new Date())
                    || urlForMonitorng.getRequestResendTime().equals(new Date()))
                    && urlForMonitorng.getIsActive()) {
                UrlMonitoringThread urlMonitoringThread = new UrlMonitoringThread(urlForMonitorng);
                urlMonitoringThread.start();
            }
        }
    }
}
