/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.dao.implementation;

import com.mycompany.mavenproject1.Model.UrlForMonitorng;
import com.mycompany.mavenproject1.dao.interfaces.MonitoringRepository;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.*;
import javax.naming.*;
import javax.servlet.*;

/**
 *
 * @author SashaFfg
 */
public class MonitoringRepositoryImpl implements MonitoringRepository {

    private DataSource dataSource;
    public static List<UrlForMonitorng> inMemoryUrls = new LinkedList();
    Properties property = new Properties();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream input;

    @SuppressWarnings("empty-statement")
    public MonitoringRepositoryImpl() {
        try {
            input = classLoader.getResourceAsStream("queries.properties");;
            property.load(input);
            Context ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("java:comp/env/jdbc/HRDB");
        } catch (NamingException | IOException ex) {
            Logger.getLogger(MonitoringRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @return connection to DB
     * @throws ServletException
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws ServletException, SQLException {
        try {
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException ex) {
            throw new ServletException(
                    "Cannot obtain connection", ex);
        }
    }

    /**
     * releases connection to database
     *
     * @param conn
     * @throws ServletException
     */
    @Override
    public void releaseConnection(Connection conn) throws ServletException {
        try {
            conn.close();
        } catch (SQLException ex) {
            throw new ServletException(
                    "Cannot release connection", ex);
        }
    }

    /**
     *
     * @return List of all UrlForMonitoring objects from database
     * @throws ServletException
     */
    @Override
    public List<UrlForMonitorng> getAllUrlForMonitorng() throws ServletException {
        List<UrlForMonitorng> allUrlForMonitorng = new LinkedList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(property.getProperty("getAllUrlForMonitorng"));
            while (resultSet.next()) {
                allUrlForMonitorng.add(new UrlForMonitorng(resultSet.getInt(1), resultSet.getString(2),
                        Boolean.parseBoolean(resultSet.getString(3)), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6),
                        resultSet.getInt(7), resultSet.getInt(8), resultSet.getInt(9), resultSet.getString(10),
                        Boolean.parseBoolean(resultSet.getString(11)), resultSet.getInt(12)));
            }
        } catch (SQLException ex) {
            throw new ServletException("Cannot obtain connection", ex);
        } finally {
            if (conn != null) {
                releaseConnection(conn);
            }
        }
        return allUrlForMonitorng;
    }

    /**
     * saves UrlForMonitoring object to database and to list in memory
     *
     * @param urlForMonitorng
     * @throws ServletException
     */
    @Override
    public void addUrlForMonitorng(UrlForMonitorng urlForMonitorng) throws ServletException {
        inMemoryUrls.add(urlForMonitorng);
        Connection conn = null;
        String query = property.getProperty("addUrlForMonitorng");
        try {
            conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, urlForMonitorng.getUrl());
            preparedStatement.setString(2, String.valueOf(urlForMonitorng.getIsActive()));
            preparedStatement.setInt(3, urlForMonitorng.getRequestResendDelay());
            preparedStatement.setInt(4, urlForMonitorng.getMinResponseSize());
            preparedStatement.setInt(5, urlForMonitorng.getMaxResponseSize());
            preparedStatement.setInt(6, urlForMonitorng.getMinResponseTimeForOk());
            preparedStatement.setInt(7, urlForMonitorng.getMaxResponseTimeForOk());
            preparedStatement.setInt(8, urlForMonitorng.getMaxResponseTimeForWarning());
            preparedStatement.setString(9, urlForMonitorng.getSubStringToSearch());
            preparedStatement.setString(10, String.valueOf(urlForMonitorng.isSearchSubString()));
            preparedStatement.setInt(11, urlForMonitorng.getExpectedResponseCode());

            preparedStatement.executeQuery();

            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MonitoringRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                releaseConnection(conn);
            }
        }

    }

    /**
     *
     * changes the status of activity to the opposite
     *
     * @param id
     * @throws ServletException
     */
    @Override
    public void changeUrlForMonitorngActivity(int id) throws ServletException {
        Connection conn = null;
        UrlForMonitorng urlForMonitorng = inMemoryUrls.get(id);
        boolean activity = urlForMonitorng.getIsActive();
        urlForMonitorng.setIsActive(!activity);
        String query = property.getProperty("changeUrlForMonitorngActivity");
        try {
            conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(!activity));
            preparedStatement.setString(2, urlForMonitorng.getUrl());
            preparedStatement.setString(3, String.valueOf(activity));
            preparedStatement.setInt(4, urlForMonitorng.getRequestResendDelay());
            preparedStatement.setInt(5, urlForMonitorng.getMinResponseSize());
            preparedStatement.setInt(6, urlForMonitorng.getMaxResponseSize());
            preparedStatement.setInt(7, urlForMonitorng.getMinResponseTimeForOk());
            preparedStatement.setInt(8, urlForMonitorng.getMaxResponseTimeForOk());
            preparedStatement.setInt(9, urlForMonitorng.getMaxResponseTimeForWarning());
            preparedStatement.setString(10, String.valueOf(urlForMonitorng.isSearchSubString()));
            preparedStatement.setInt(11, urlForMonitorng.getExpectedResponseCode());

            preparedStatement.executeQuery();

            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MonitoringRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                releaseConnection(conn);
            }
        }
    }

    /**
     *
     * remove url from the database and in memory list
     *
     * @param id
     * @throws ServletException
     */
    @Override
    public void removeUrlForMonitorng(int id) throws ServletException {
        UrlForMonitorng urlForMonitorng = inMemoryUrls.get(id);
        inMemoryUrls.remove(urlForMonitorng);
        Connection conn = null;
        String query = property.getProperty("removeUrlForMonitorng");
        try {
            conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, urlForMonitorng.getUrl());
            preparedStatement.setString(2, String.valueOf(urlForMonitorng.getIsActive()));
            preparedStatement.setInt(3, urlForMonitorng.getRequestResendDelay());
            preparedStatement.setInt(4, urlForMonitorng.getMinResponseSize());
            preparedStatement.setInt(5, urlForMonitorng.getMaxResponseSize());
            preparedStatement.setInt(6, urlForMonitorng.getMinResponseTimeForOk());
            preparedStatement.setInt(7, urlForMonitorng.getMaxResponseTimeForOk());
            preparedStatement.setInt(8, urlForMonitorng.getMaxResponseTimeForWarning());
            preparedStatement.setString(9, String.valueOf(urlForMonitorng.isSearchSubString()));
            preparedStatement.setInt(10, urlForMonitorng.getExpectedResponseCode());

            preparedStatement.executeQuery();

            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MonitoringRepositoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (conn != null) {
                releaseConnection(conn);
            }
        }
    }

}
