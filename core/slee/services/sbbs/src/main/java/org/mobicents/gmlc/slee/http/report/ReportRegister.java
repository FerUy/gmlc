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

package org.mobicents.gmlc.slee.http.report;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:aferreiraguido@gmail.com"> Alejandro Ferreira Guido </a>
 */
public class ReportRegister {

    ConcurrentHashMap<Integer, ReportElement> reportRegisteredElements;
    static Integer reportRegisterIdSerial = 0;

    public ReportRegister(){
        reportRegisteredElements = new ConcurrentHashMap<Integer, ReportElement>();
    }

    public Integer add(Integer referenceNumber, ReportParameters reportParameters) {
        Integer reportRegisterId = 0;

        synchronized (reportRegisterIdSerial) {
            reportRegisterId = ++reportRegisterIdSerial;
        }

        reportRegisteredElements.put(reportRegisterId, new ReportElement(referenceNumber, reportParameters));

        return reportRegisterId;
    }

    public ReportElement get(Integer reportRegisterId){

        if (reportRegisteredElements.containsKey(reportRegisterId)) {
            return reportRegisteredElements.get(reportRegisterId);
        }

        return null;
    }

    public void remove(Integer reportRegisterId){
        if (reportRegisteredElements.containsKey(reportRegisterId)) {
            reportRegisteredElements.remove(reportRegisterId);
        }
    }
}
