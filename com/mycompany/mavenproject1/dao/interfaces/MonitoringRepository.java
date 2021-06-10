/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.dao.interfaces;

import com.mycompany.mavenproject1.Model.UrlForMonitorng;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;

/**
 *
 * @author SashaFfg
 */
public interface MonitoringRepository {

    public Connection getConnection() throws ServletException, SQLException;

    public void releaseConnection(Connection conn) throws ServletException;

    public List<UrlForMonitorng> getAllUrlForMonitorng() throws ServletException;

    public void addUrlForMonitorng(UrlForMonitorng urlForMonitorng) throws ServletException;

    public void changeUrlForMonitorngActivity(int id) throws ServletException;

    public void removeUrlForMonitorng(int id) throws ServletException;
}
