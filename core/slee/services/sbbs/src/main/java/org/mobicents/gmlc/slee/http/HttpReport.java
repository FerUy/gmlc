/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.gmlc.slee.http;

import org.mobicents.gmlc.slee.http.report.ReportElement;
import org.mobicents.gmlc.slee.http.report.ReportParameters;
import org.mobicents.gmlc.slee.http.report.ReportRegister;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:aferreiraguido@gmail.com"> Alejandro Ferreira Guido </a>
 */
public class HttpReport {

    public enum HttpMethod {POST, GET}

    // URL's are stored on a separate list by referenceNumber
    HashMap<Integer, String> callbackUrlHashMap;
    // Registration is made on internal id storing referenceNumber for callbackUrl and reportParameters passed
    ReportRegister reportRegister;

    public HttpReport() {
        callbackUrlHashMap = new HashMap<Integer, String>();
        reportRegister = new ReportRegister();
    }

    public Integer Register(Integer referenceNumber, String callbackUrl, ReportParameters reportParameters) {
        // register if not there the callback url by referenceNumber passed from client
        if (!callbackUrlHashMap.containsKey(referenceNumber)) {
            callbackUrlHashMap.put(referenceNumber, callbackUrl);
        }

        // return registered id for later retrieval and perform http method callback
        return reportRegister.add(referenceNumber, reportParameters);
    }

    public void Cancel(Integer referenceNumber) {
        // remove previous registration
        if (callbackUrlHashMap.containsKey(referenceNumber)) {
            callbackUrlHashMap.remove(referenceNumber);
            reportRegister.remove(referenceNumber);
        }
    }

    public void Perform(HttpMethod httpMethod, Integer reportRegisterId, List<String> slrReportParameters) throws IOException {
        // get the report element register by previous registered id
        ReportElement reportElement = reportRegister.get(reportRegisterId);
        if (reportElement != null) {
            // get the callback from report element saved by referenceNumber identifying callback url
            String callbackUrl = callbackUrlHashMap.get(reportElement.referenceNumber);

            // perform the http method
            URL urlCallback = new URL(callbackUrl);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlCallback.openConnection();
            httpUrlConnection.setRequestMethod(httpMethod.toString());
            httpUrlConnection.setDoOutput(true);
            OutputStream httpOutputStream = httpUrlConnection.getOutputStream();

            if (reportElement.reportParameters == null && slrReportParameters == null)
                httpOutputStream.write("{}".getBytes());

            if (reportElement.reportParameters != null)
                httpOutputStream.write(reportElement.reportParameters.toString().getBytes());

            if (slrReportParameters != null) {
                Iterator<String> slrReportParametersIterator = slrReportParameters.iterator();
                while (slrReportParametersIterator.hasNext())
                    httpOutputStream.write(slrReportParametersIterator.next().getBytes());
            }

            httpOutputStream.flush();
            httpOutputStream.close();

            // obtain http response for log
            Integer httpResponseCode = httpUrlConnection.getResponseCode();
        }
    }
}
