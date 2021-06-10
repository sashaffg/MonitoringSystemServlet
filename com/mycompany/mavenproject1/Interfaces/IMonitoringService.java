
package com.mycompany.mavenproject1.Interfaces;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 *
 * @author SashaFfg
 */
public interface IMonitoringService {
     public CloseableHttpResponse getResponse(String url) throws  IOException ;
}
