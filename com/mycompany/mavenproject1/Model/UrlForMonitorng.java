
package com.mycompany.mavenproject1.Model;

import java.io.IOException;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 *
 * @author SashaFfg
 */
public class UrlForMonitorng {

    String Url;
    CloseableHttpResponse lastUrlResponse;
    boolean isActive = true;
    Date requestResendTime;
    int requestResendDelay;
    int id;
    int minResponseTimeForOk;
    int maxResponseTimeForOk;
    int maxResponseTimeForWarning;
    int minResponseSize;
    int maxResponseSize;
    String urlStatus;
    String subStringToSearch = "";
    boolean searchSubString = false;
    boolean isSubStringToSearchExists = false;
    int expectedResponseCode;
    long responseSize;

    public UrlForMonitorng(String Url, boolean isActive, int id, int requestResendDelay) {
        this.Url = Url;
        this.isActive = isActive;
        this.id = id;
        this.requestResendDelay = requestResendDelay;
        requestResendTime = new Date();
    }

    public UrlForMonitorng(int id, String Url, boolean isActive, int requestResendDelay,
            int minResponseSize, int maxResponseSize,
            int minResponseTimeForOk, int maxResponseTimeForOk, int maxResponseTimeForWarning,
            String subStringToSearch, boolean searchSubString, int expectedResponseCode) {
        this.id = id;
        this.Url = Url;
        this.isActive = isActive;
        this.expectedResponseCode = expectedResponseCode;
        requestResendTime = new Date();
        this.requestResendDelay = requestResendDelay;
        this.minResponseSize = minResponseSize;
        this.maxResponseSize = maxResponseSize;
        this.minResponseTimeForOk = minResponseTimeForOk;
        this.maxResponseTimeForOk = maxResponseTimeForOk;
        this.maxResponseTimeForWarning = maxResponseTimeForWarning;
        this.subStringToSearch = subStringToSearch;
        this.searchSubString = searchSubString;
        urlStatus = "OK";
        responseSize = 0;
    }

    public UrlForMonitorng(String Url, boolean isActive, int expectedResponseCode, int requestResendDelay,
            int minResponseSize, int maxResponseSize,
            String subStringToSearch, int minResponseTimeForOk, int maxResponseTimeForOk, int maxResponseTimeForWarning) {
        this.Url = Url;
        this.isActive = isActive;
        this.expectedResponseCode = expectedResponseCode;
        requestResendTime = new Date();
        this.requestResendDelay = requestResendDelay;
        this.minResponseSize = minResponseSize;
        this.maxResponseSize = maxResponseSize;
        this.minResponseTimeForOk = minResponseTimeForOk;
        this.maxResponseTimeForOk = maxResponseTimeForOk;
        this.maxResponseTimeForWarning = maxResponseTimeForWarning;
        this.subStringToSearch = subStringToSearch;
        if (!"".equals(subStringToSearch)) {
            searchSubString = true;
        }
        urlStatus = "OK";
        responseSize = 0;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public void setLastUrlResponse(CloseableHttpResponse lastUrlResponse) throws IOException {
        this.lastUrlResponse = lastUrlResponse;
        byte[] data = IOUtils.toByteArray(this.lastUrlResponse.getEntity().getContent());
        responseSize = data.length;
        if (searchSubString) {
            isSubStringToSearchExists = new String(data).contains(subStringToSearch);
        }
        if (Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) > minResponseTimeForOk
                && responseSize > minResponseSize
                && responseSize < maxResponseSize
                && Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) < maxResponseTimeForOk
                && lastUrlResponse.getStatusLine().getStatusCode() == expectedResponseCode) {
            urlStatus = "<font color=\"green\">OK</font>";
        } else if (Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) > maxResponseTimeForOk
                && responseSize > minResponseSize && responseSize < maxResponseSize
                && Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) < maxResponseTimeForWarning
                && lastUrlResponse.getStatusLine().getStatusCode() == expectedResponseCode) {
            urlStatus = "WARNING";
        } else if (Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) > maxResponseTimeForWarning
                || responseSize < minResponseSize || responseSize > maxResponseSize
                || lastUrlResponse.getStatusLine().getStatusCode() != expectedResponseCode) {
            urlStatus = "CRITICAL";
        }
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;

    }

    public String getUrl() {
        return Url;
    }

    public CloseableHttpResponse getLastUrlResponse() {
        return lastUrlResponse;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Date getRequestResendTime() {
        return requestResendTime;
    }

    public void setRequestResendTime(Date requestResendTime) {
        this.requestResendTime = new Date(new Date().getTime() + requestResendDelay * 1000);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRequestResendDelay() {
        return requestResendDelay;
    }

    public void setRequestResendDelay(int requestResendDelay) {
        this.requestResendDelay = requestResendDelay;
    }

    public int getMinResponseTimeForOk() {
        return minResponseTimeForOk;
    }

    public int getMaxResponseTimeForOk() {
        return maxResponseTimeForOk;
    }

    public int getMaxResponseTimeForWarning() {
        return maxResponseTimeForWarning;
    }

    public String getUrlStatus() {
        return urlStatus;
    }

    public String getUrlStatusForView() {
        if ("CRITICAL".equals(urlStatus)) {
            return "<font color=\"red\">" + urlStatus + "</font>" + "<audio autoplay>\n"
                    + "    <source src=\"/mavenproject1/resource/CRITICAL.mp3\" type=\"audio/mpeg\">"
                    + "  </audio>";
        }
        if ("WARNING".equals(urlStatus)) {
            return "<font color=\"yellow\">" + urlStatus + "</font>" + "<audio autoplay>\n"
                    + "    <source src=\"/mavenproject1/resource/WARNING.mp3\" type=\"audio/mpeg\">"
                    + "  </audio>";
        }
        return urlStatus;
    }

    public int getExpectedResponseCode() {
        return expectedResponseCode;
    }

    public void setMinResponseTimeForOk(int minResponseTimeForOk) {
        this.minResponseTimeForOk = minResponseTimeForOk;
    }

    public void setMaxResponseTimeForOk(int maxResponseTimeForOk) {
        this.maxResponseTimeForOk = maxResponseTimeForOk;
    }

    public void setMaxResponseTimeForWarning(int maxResponseTimeForWarning) {
        this.maxResponseTimeForWarning = maxResponseTimeForWarning;
    }

    public void setUrlStatus(String urlStatus) {
        this.urlStatus = urlStatus;
    }

    public void setExpectedResponseCode(int expectedResponseCode) {
        this.expectedResponseCode = expectedResponseCode;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }

    public String getSubStringToSearch() {
        return subStringToSearch;
    }

    public boolean isSearchSubString() {
        return searchSubString;
    }

    public boolean isSubStringToSearchExists() {
        return isSubStringToSearchExists;
    }

    public void setSubStringToSearch(String subStringToSearch) {
        this.subStringToSearch = subStringToSearch;
    }

    public void setSearchSubString(boolean searchSubString) {
        this.searchSubString = searchSubString;
    }

    public void setIsSubStringToSearchExists(boolean isSubStringToSearchExists) {
        this.isSubStringToSearchExists = isSubStringToSearchExists;
    }

    public int getMinResponseSize() {
        return minResponseSize;
    }

    public void setMinResponseSize(int minResponseSize) {
        this.minResponseSize = minResponseSize;
    }

    public int getMaxResponseSize() {
        return maxResponseSize;
    }

    public void setMaxResponseSize(int maxResponseSize) {
        this.maxResponseSize = maxResponseSize;
    }

}
