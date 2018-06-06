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

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;

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
    // Includes geographical information
    private double geographicalLatitude;
    private double geographicalLongitude;
    private double geographicalUncertainty;
    // includes geodetic information
    private double geodeticLatitude;
    private double geodeticLongitude;
    private double geodeticUncertainty;
    private int geodeticConfidence;
    private int screeningAndPresentationIndicators;
    // other location info
    private boolean saiPresent;
    private boolean currentLocationRetrieved;
    private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
    private CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength;
    // Includes Cell Global Identity (CI, LAC, MCC, MNC, Age of Location information
    private int mcc; // MCC of CGI or LAI
    private int mnc; // MNC of CGI or LAI
    private int lac; // LAC of CGI or LAI
    private int ci; // CI of CGI or LAI
    private int ageOfLocationInfo; // Age of location information of CGI or LAI
    ISDNAddressString vlrNumber; // VLR Global Title at which the MS is attached to
    ISDNAddressString mscNumber; // MSC Global Title at which the MS is attached to
    private LocationInformationGPRS locationInformationGPRS; // The SGSN indicates here the location of the served subscriber.
    private LocationInformationEPS locationInformationEPS; // The MME indicates here the location of the served subscriber.
    private SubscriberState subscriberState; // State of the MS as defined in 3GPP TS 23.018.
    // Possible values are assumedIdle, camelBusy, networkDeterminedNotReachable, notProvidedFromVlr
    private PSSubscriberState psSubscriberState; // Packet-Switched state of the MS.
    private IMEI imei; // International Mobile Equipment Identity.
    private MSClassmark2 msClassmark2; // Defined in 3GPP TS 24.008.
    private GPRSMSClass gprsmsClass;
    private MNPInfoRes mnpInfoRes; // Mobile Number Portability (MNP) information result number portability status, MSISDN, IMSI, Routeing number.
    private int numberPortabilityStatus;
    private ISDNAddressString msisdn;
    private String msisdnAddress;
    private IMSI imsi;
    private String imsiData;
    private RouteingNumber routeingNumber;
    private String routeingNumberStr;
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

    public double getGeoraphicalLatitude() {
        return geographicalLatitude;
    }

    public void setGeoraphicalLatitude(double latitude) {
        this.geographicalLatitude = latitude;
    }

    public double getGeoraphicalLongitude() {
        return geographicalLongitude;
    }

    public void setGeoraphicalLongitude(double longitude) {
        this.geographicalLongitude = longitude;
    }

    public double getGeographicalLatitude() {
        return geographicalLatitude;
    }

    public void setGeographicalLatitude(double geographicalLatitude) {
        this.geographicalLatitude = geographicalLatitude;
    }

    public double getGeographicalLongitude() {
        return geographicalLongitude;
    }

    public void setGeographicalLongitude(double geographicalLongitude) {
        this.geographicalLongitude = geographicalLongitude;
    }

    public double getGeographicalUncertainty() {
        return geographicalUncertainty;
    }

    public void setGeographicalUncertainty(double geographicalUncertainty) {
        this.geographicalUncertainty = geographicalUncertainty;
    }

    public double getGeodeticLatitude() {
        return geodeticLatitude;
    }

    public void setGeodeticLatitude(double geodeticLatitude) {
        this.geodeticLatitude = geodeticLatitude;
    }

    public double getGeodeticLongitude() {
        return geodeticLongitude;
    }

    public void setGeodeticLongitude(double geodeticLongitude) {
        this.geodeticLongitude = geodeticLongitude;
    }

    public double getGeodeticUncertainty() {
        return geodeticUncertainty;
    }

    public void setGeodeticUncertainty(double geodeticUncertainty) {
        this.geodeticUncertainty = geodeticUncertainty;
    }

    public int getGeodeticConfidence() {
        return geodeticConfidence;
    }

    public void setGeodeticConfidence(int geodeticConfidence) {
        this.geodeticConfidence = geodeticConfidence;
    }

    public int getScreeningAndPresentationIndicators() {
        return screeningAndPresentationIndicators;
    }

    public void setScreeningAndPresentationIndicators(int screeningAndPresentationIndicators) {
        this.screeningAndPresentationIndicators = screeningAndPresentationIndicators;
    }

    public ISDNAddressString getVlrNumber() {
        return vlrNumber;
    }

    public void setVlrNumber(ISDNAddressString vlrNumber) {
        this.vlrNumber = vlrNumber;
    }

    public ISDNAddressString getMscNumber() {
        return mscNumber;
    }

    public void setMscNumber(ISDNAddressString mscNumber) {
        this.mscNumber = mscNumber;
    }

    public MNPInfoRes getMnpInfoRes() {
        return mnpInfoRes;
    }

    public void setMnpInfoRes(MNPInfoRes mnpInfoRes) {
        this.mnpInfoRes = mnpInfoRes;
    }

    public int getNumberPortabilityStatus() {
        return numberPortabilityStatus;
    }

    public void setNumberPortabilityStatus(int numberPortabilityStatus) {
        this.numberPortabilityStatus = numberPortabilityStatus;
    }

    public ISDNAddressString getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    public String getMsisdnAddress() {
        return msisdnAddress;
    }

    public void setMsisdnAddress(String msisdnAddress) {
        this.msisdnAddress = msisdnAddress;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    public String getImsiData() {
        return imsiData;
    }

    public void setImsiData(String imsiData) {
        this.imsiData = imsiData;
    }

    public RouteingNumber getRouteingNumber() {
        return routeingNumber;
    }

    public void setRouteingNumber(RouteingNumber routeingNumber) {
        this.routeingNumber = routeingNumber;
    }

    public String getRouteingNumberStr() {
        return routeingNumberStr;
    }

    public void setRouteingNumberStr(String routeingNumberStr) {
        this.routeingNumberStr = routeingNumberStr;
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

    public boolean isSaiPresent() {
        return saiPresent;
    }

    public void setSaiPresent(boolean saiPresent) {
        this.saiPresent = saiPresent;
    }

    public boolean isCurrentLocationRetrieved() {
        return currentLocationRetrieved;
    }

    public void setCurrentLocationRetrieved(boolean currentLocationRetrieved) {
        this.currentLocationRetrieved = currentLocationRetrieved;
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

        stringBuilder.append(", location information, location number=");
        try {
            stringBuilder.append(locationInformation.getLocationNumber().getLocationNumber().getAddress());
        } catch (MAPException e) {
            e.printStackTrace();
        }

        stringBuilder.append(", location information, MSC number=");
        stringBuilder.append(locationInformation.getMscNumber().getAddress());

        stringBuilder.append(", location information, VLR number=");
        stringBuilder.append(locationInformation.getVlrNumber().getAddress());

        stringBuilder.append(", location information, latitude=");
        stringBuilder.append(locationInformation.getGeographicalInformation().getLatitude());

        stringBuilder.append(", location information, longitude=");
        stringBuilder.append(locationInformation.getGeographicalInformation().getLongitude());

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
