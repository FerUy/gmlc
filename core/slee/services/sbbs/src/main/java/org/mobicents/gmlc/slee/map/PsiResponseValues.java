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

package org.mobicents.gmlc.slee.map;

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author <a href="mailto:aferreiraguido@gmail.com"> Alejandro Ferreira Guido </a>
 */
public class PsiResponseValues implements Serializable {

    private static final long serialVersionUID = 6506587872040660967L;

  /*
  3GPP TS 29.002 MAP Specification v15.2.0 (2018-03)

  8.11.2	MAP-PROVIDE-SUBSCRIBER-INFO service
    8.11.2.1	Definition
              This service is used to request information (e.g. subscriber state and location) from the VLR,
              SGSN or MME (via an IWF) at any time.
              The MAP-PROVIDE-SUBSCRIBER-INFO service is a confirmed service using the primitives defined in
              table 8.11/2.

    8.11.2.2	Service primitives
              Table 8.11/2: Provide_Subscriber_Information
              Parameter name	                                Request	Indication	Response	Confirm
              Invoke id	                                        M	        M(=)	    M(=)	    M(=)
              Requested Info	                                  M	        M(=)
              IMSI	                                            M	        M(=)
              LMSI	                                            U	        O
              Call Priority	                                    U	        O
              Location Information			                                            C	        C(=)
              Location Information for GPRS			                                    C	        C(=)
              Subscriber State			                                                C	        C(=)
              PS Subscriber State			                                              C	        C(=)
              IMEI			                                                            C	        C(=)
              MS Classmark 2			                                                  C	        C(=)
              GPRS MS Class			                                                    C	        C(=)
              IMS Voice Over PS Sessions Support Indicator		                      C	        C(=)
              Last UE Activity Time			                                            C	        C(=)
              Last RAT Type			                                                    C	        C(=)
              Location Information for EPS			                                    C	        C(=)
              Time Zone			                                                        C	        C(=)
              Daylight Saving Time			                                            C	        C(=)
              User error			                                                      C	        C(=)
              Provider error				                                                          O

      ? (M): mandatory parameter.
      ? (O): provider option.
      ? (C): conditional parameter (i.e. it will always be present in the indication type primitive
             if it was present in the corresponding request type primitive).
      ? (U): TC-user optional parameter.
      ? (=): the parameter must have the same value in the indication primitive as provided in the
             corresponding request primitive.
      ? A blank Indicates that the parameter is not applicable.

  */

    private LocationInformation locationInformation;
    // Includes Cell Global Identity (CI, LAC, MCC, MNC, Age of Location information
    private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
    private CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength;
    private int mcc; // MCC of CGI or LAI
    private int mnc; // MNC of CGI or LAI
    private int lac; // LAC of CGI or LAI
    private int ci; // CI of CGI or LAI
    private int ageOfLocationInfo; // Age of location information of CGI or LAI
    ISDNAddressString vlrNumber; // VLR Global Title at which the MS is attached to
    private LocationInformationGPRS locationInformationGPRS; // The SGSN indicates here the location of the served subscriber.
    private LocationInformationEPS locationInformationEPS; // The MME indicates here the location of the served subscriber.
    private SubscriberState subscriberState; // State of the MS as defined in 3GPP TS 23.018.
    // Possible values are assumedIdle, camelBusy, networkDeterminedNotReachable, notProvidedFromVlr
    private PSSubscriberState psSubscriberState; // Packet-Switched state of the MS.
    private IMEI imei; // International Mobile Equipment Identity.
    private MSClassmark2 msClassmark2; // Defined in 3GPP TS 24.008.
    private GPRSMSClass gprsmsClass;
    private MNPInfoRes mnpInfoRes; // Mobile Number Portability (MNP) information result.
    private boolean imsVoiceOverPsSessionsSupportIndicator;
    // Last UE Activity Time
    // Last RAT Type
    private TimeZone timeZone;


    public PsiResponseValues() {
    }

    public PsiResponseValues(LocationInformation locationInformation, LocationInformationGPRS locationInformationGPRS, SubscriberState subscriberState,
                             PSSubscriberState psSubscriberState, IMEI imei, MSClassmark2 msClassmark2, GPRSMSClass gprsmsClass,
                             LocationInformationEPS locationInformationEPS) {
        this.locationInformation = locationInformation;
        this.locationInformationGPRS = locationInformationGPRS;
        this.subscriberState = subscriberState;
        this.psSubscriberState = psSubscriberState;
        this.imei = imei;
        this.msClassmark2 = msClassmark2;
        this.gprsmsClass = gprsmsClass;
        this.locationInformationEPS = locationInformationEPS;
    }

    public PsiResponseValues(LocationInformation locationInformation, LocationInformationGPRS locationInformationGPRS, SubscriberState subscriberState,
                             PSSubscriberState psSubscriberState, IMEI imei, MSClassmark2 msClassmark2, GPRSMSClass gprsmsClass,
                             Boolean imsVoiceOverPsSessionsSupportIndicator, LocationInformationEPS locationInformationEPS, TimeZone timeZone) {
        this.locationInformation = locationInformation;
        this.locationInformationGPRS = locationInformationGPRS;
        this.subscriberState = subscriberState;
        this.psSubscriberState = psSubscriberState;
        this.imei = imei;
        this.msClassmark2 = msClassmark2;
        this.gprsmsClass = gprsmsClass;
        this.imsVoiceOverPsSessionsSupportIndicator = imsVoiceOverPsSessionsSupportIndicator;
        this.locationInformationEPS = locationInformationEPS;
        this.timeZone = timeZone;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    public void setLocationInformation(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    public LocationInformationGPRS getLocationInformationGPRS() {
        return locationInformationGPRS;
    }

    public void setLocationInformationGPRS(LocationInformationGPRS locationInformationGPRS) {
        this.locationInformationGPRS = locationInformationGPRS;
    }

    public SubscriberState getSubscriberState() {
        return subscriberState;
    }

    public void setSubscriberState(SubscriberState subscriberState) {
        this.subscriberState = subscriberState;
    }

    public PSSubscriberState getPsSubscriberState() {
        return psSubscriberState;
    }

    public void setPsSubscriberState(PSSubscriberState psSubscriberState) {
        this.psSubscriberState = psSubscriberState;
    }

    public IMEI getImei() {
        return imei;
    }

    public void setImei(IMEI imei) {
        this.imei = imei;
    }

    public MSClassmark2 getMsClassmark2() {
        return msClassmark2;
    }

    public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
        return cellGlobalIdOrServiceAreaIdOrLAI;
    }

    public void setCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI) {
        this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
    }

    public CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalIdOrServiceAreaIdFixedLength() {
        return cellGlobalIdOrServiceAreaIdFixedLength;
    }

    public void setCellGlobalIdOrServiceAreaIdFixedLength(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength) {
        this.cellGlobalIdOrServiceAreaIdFixedLength = cellGlobalIdOrServiceAreaIdFixedLength;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
        this.ci = ci;
    }

    public int getAgeOfLocationInfo() {
        return ageOfLocationInfo;
    }

    public void setAgeOfLocationInfo(int ageOfLocationInfo) {
        this.ageOfLocationInfo = ageOfLocationInfo;
    }

    public ISDNAddressString getVlrNumber() {
        return vlrNumber;
    }

    public void setVlrNumber(ISDNAddressString vlrNumber) {
        this.vlrNumber = vlrNumber;
    }

    public MNPInfoRes getMnpInfoRes() {
        return mnpInfoRes;
    }

    public void setMnpInfoRes(MNPInfoRes mnpInfoRes) {
        this.mnpInfoRes = mnpInfoRes;
    }

    public boolean isImsVoiceOverPsSessionsSupportIndicator() {
        return imsVoiceOverPsSessionsSupportIndicator;
    }

    public void setImsVoiceOverPsSessionsSupportIndicator(boolean imsVoiceOverPsSessionsSupportIndicator) {
        this.imsVoiceOverPsSessionsSupportIndicator = imsVoiceOverPsSessionsSupportIndicator;
    }

    public void setMsClassmark2(MSClassmark2 msClassmark2) {
        this.msClassmark2 = msClassmark2;
    }

    public GPRSMSClass getGprsmsClass() {
        return gprsmsClass;
    }

    public void setGprsmsClass(GPRSMSClass gprsmsClass) {
        this.gprsmsClass = gprsmsClass;
    }

    public Boolean getImsVoiceOverPsSessionsSupportIndicator() {
        return imsVoiceOverPsSessionsSupportIndicator;
    }

    public void setImsVoiceOverPsSessionsSupportIndicator(Boolean imsVoiceOverPsSessionsSupportIndicator) {
        this.imsVoiceOverPsSessionsSupportIndicator = imsVoiceOverPsSessionsSupportIndicator;
    }

    public LocationInformationEPS getLocationInformationEPS() {
        return locationInformationEPS;
    }

    public void setLocationInformationEPS(LocationInformationEPS locationInformationEPS) {
        this.locationInformationEPS = locationInformationEPS;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PslResponseValues [");

        stringBuilder.append(", location information=");
        stringBuilder.append(locationInformation);

        stringBuilder.append(", location information for GPRS");
        stringBuilder.append(locationInformationGPRS);

        stringBuilder.append(", location information for EPS");
        stringBuilder.append(locationInformationEPS);

        stringBuilder.append(", subscriber state");
        stringBuilder.append(subscriberState);

        stringBuilder.append(", PS subscriber state");
        stringBuilder.append(psSubscriberState);

        stringBuilder.append(", IMEI");
        stringBuilder.append(imei);

        stringBuilder.append(", MS Classmark 2");
        stringBuilder.append(msClassmark2);

        stringBuilder.append(", GPRS MS Class");
        stringBuilder.append(gprsmsClass);

        stringBuilder.append(", IMS Voice Over PS Sessions Support Indicator");
        stringBuilder.append(imsVoiceOverPsSessionsSupportIndicator);

        stringBuilder.append(", time zone");
        stringBuilder.append(timeZone);

        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
