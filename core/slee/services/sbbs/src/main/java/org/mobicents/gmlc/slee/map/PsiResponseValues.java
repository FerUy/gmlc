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
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
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
              Invoke id	                                        M	        M(=)	   M(=)	        M(=)
              Requested Info	                                M	        M(=)
              IMSI	                                            M	        M(=)
              LMSI	                                            U	        O
              Call Priority	                                    U	        O
              Location Information			                                           C	        C(=)
              Location Information for GPRS			                                   C	        C(=)
              Subscriber State			                                               C	        C(=)
              PS Subscriber State			                                           C	        C(=)
              IMEI			                                                           C	        C(=)
              MS Classmark 2			                                               C	        C(=)
              GPRS MS Class			                                                   C	        C(=)
              IMS Voice Over PS Sessions Support Indicator		                       C	        C(=)
              Last UE Activity Time			                                           C	        C(=)
              Last RAT Type			                                                   C	        C(=)
              Location Information for EPS			                                   C	        C(=)
              Time Zone			                                                       C	        C(=)
              Daylight Saving Time			                                           C	        C(=)
              User error			                                                   C	        C(=)
              Provider error				                                                        O

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

    // LocationInformation/GPRS/EPS includes Geographical Information
    // The VLR indicates in this parameter the location of the served subscriber as defined in 3GPP TS 23.018 and 3GPP TS 23.032.
    // Shall be present if the VLR can derive it from the stored service area identity, cell global identity or location area identity; otherwise shall be absent.
    private double geographicalLatitude;
    private double geographicalLongitude;
    private double geographicalUncertainty;
    private TypeOfShape typeOfShape;

    // LocationInformation/GPRS/EPS includes Geodetic Information
    // This information element corresponds to the Calling Geodetic Location defined in ITU-T Recommendation Q.763.
    // Shall be present if the VLR can derive it from the stored service area identity, cell global identity or location area identity; otherwise shall be absent.
    private double geodeticLatitude;
    private double geodeticLongitude;
    private double geodeticUncertainty;
    private int geodeticConfidence;
    private int screeningAndPresentationIndicators;

    // LocationInformation/GPRS/EPS includes if SAI is present
    private boolean saiPresent;

    // LocationInformation/GPRS/EPS includes if current location information is retrieved
    // Shall be present when location information was obtained after a successful paging procedure for Active Location Retrieval.
    private boolean currentLocationRetrieved;

    // LocationInformation includes CGI or SAI
    // SAI: Service area identity of the cell in which the MS is currently in radio contact or in which the MS was last in radio contact.
    // CGI: Cell global identity of the cell in which the MS is currently in radio contact or in which the MS was last in radio contact.
    // SAI shall be present if the MS uses UMTS radio access and the subscriber record is marked as confirmed by radio contact; otherwise shall be absent.
    // CGI shall be present if the MS uses GSM radio access and the subscriber record is marked as confirmed by radio contact; otherwise shall be absent.
    private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
    private CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength;
    // The latter includes MCC, MNC, LAC, CI, Age of Location information
    private int mcc; // MCC of CGI or LAI
    private int mnc; // MNC of CGI or LAI
    private int lac; // LAC of CGI or LAI
    private int ci; // CI of CGI or LAI
    private int ageOfLocationInfo; // Age of CGI or LAI, Measured in minutes. Shall be present if available in the MSC/VLR; otherwise shall be absent.

    // LocationInformation includes Location Number (defined at ITU T Recommendation Q.763)
    // Shall be present if the VLR can derive it from the stored service area identity (for UMTS) or cell global identity (for GSM) or location area identity;
    // otherwise shall be absent. The mapping from service area identity or cell ID and location area to location number is network-specific
    // and outside the scope of the UMTS and GSM standards.
    private LocationNumberMap locationNumberMap;

    // LocationInformation/EPS includes E-UTRAN CGI
    // E-UTRAN cell global identity of the cell in which the MS is currently in radio contact or in which the MS was last in radio contact.
    // Shall be present if the MS uses E-UTRAN radio access and the subscriber record is marked as confirmed by radio contact; otherwise shall be absent.
    private EUtranCgi eUtranCgi;

    // LocationInformation/EPS includes Tracking Area Id
    // Tracking area identity of the cell in which the MS is currently in radio contact or in which the MS was last in radio contact.
    // Shall be present if the MS uses E-UTRAN radio access; otherwise shall be absent.
    TAId taId;

    // LocationInformation includes VLR number
    ISDNAddressString vlrNumber; // VLR Global Title at which the MS is attached to
    ISDNAddressString mscNumber; // MSC Global Title at which the MS is attached to

    // The SGSN indicates in LocationInformationGPRS the location of the served subscriber as defined in 3GPP TS 23.078.
    private LocationInformationGPRS locationInformationGPRS; // The SGSN indicates here the location of the served subscriber.
    // LocationInformationGPRS includes SGSN number, LSAIdentity, RAIdentity
    private ISDNAddressString sgsnNumber;
    private LSAIdentity lsaIdentity;
    private boolean isPLMNSignificantLSA;
    private RAIdentity raIdentity;

    // The MME (via an IWF) indicates in LocationInformationEPS the location of the served subscriber.
    private LocationInformationEPS locationInformationEPS; // The MME indicates here the location of the served subscriber.
    private DiameterIdentity mmeName;

    // SubscriberState: State of the MS as defined in 3GPP TS 23.018.
    // Possible values are assumedIdle, camelBusy, networkDeterminedNotReachable, notProvidedFromVlr
    private SubscriberState subscriberState;

    // PSSubscriberState: Packet-Switched state of the MS.
    private PSSubscriberState psSubscriberState;
    private IMEI imei; // International Mobile Equipment Identity.
    private MSClassmark2 msClassmark2; // Defined in 3GPP TS 24.008.
    private GPRSMSClass gprsmsClass;

    // MNP Info Result refers to the Mobile Number Portability (MNP) information result (3GPP TS 23.078 and 3GPP TS 23.066).
    // This parameter may contain the following information: Routeing Number, IMSI, MSISDN, Number Portability Status.
    private MNPInfoRes mnpInfoRes;
    private int numberPortabilityStatus; // Number portability status of subscriber (3GPP TS 23.066).
    private ISDNAddressString msisdn;
    private String msisdnAddress;
    private IMSI imsi;
    private String imsiData;
    private RouteingNumber routeingNumber; // RouteingNumber refers to a number used for routeing purpose and identifying a network operator (3GPP TS 23.066).
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

    public PsiResponseValues(LocationInformation locationInformation, EUtranCgi eUtranCgi, TAId taId,
                             LocationNumberMap locationNumberMap, LocationInformationGPRS locationInformationGPRS,
                             SubscriberState subscriberState, PSSubscriberState psSubscriberState, IMEI imei, MSClassmark2 msClassmark2, GPRSMSClass gprsmsClass,
                             Boolean imsVoiceOverPsSessionsSupportIndicator, LocationInformationEPS locationInformationEPS, TimeZone timeZone) {
        this.locationInformation = locationInformation;
        this.eUtranCgi = eUtranCgi;
        this.taId = taId;
        this.locationNumberMap = locationNumberMap;
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

    public LocationNumberMap getLocationNumberMap() {
        return locationNumberMap;
    }

    public void setLocationNumberMap(LocationNumberMap locationNumberMap) {
        this.locationNumberMap = locationNumberMap;
    }

    public LocationInformationGPRS getLocationInformationGPRS() {
        return locationInformationGPRS;
    }

    public void setLocationInformationGPRS(LocationInformationGPRS locationInformationGPRS) {
        this.locationInformationGPRS = locationInformationGPRS;
    }

    public ISDNAddressString getSgsnNumber() {
        return sgsnNumber;
    }

    public void setSgsnNumber(ISDNAddressString sgsnNumber) {
        this.sgsnNumber = sgsnNumber;
    }

    public LSAIdentity getLsaIdentity() {
        return lsaIdentity;
    }

    public void setLsaIdentity(LSAIdentity lsaIdentity) {
        this.lsaIdentity = lsaIdentity;
    }

    public boolean isPLMNSignificantLSA() {
        return isPLMNSignificantLSA;
    }

    public void setPLMNSignificantLSA(boolean PLMNSignificantLSA) {
        isPLMNSignificantLSA = PLMNSignificantLSA;
    }

    public RAIdentity getRaIdentity() {
        return raIdentity;
    }

    public void setRaIdentity(RAIdentity raIdentity) {
        this.raIdentity = raIdentity;
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

    public EUtranCgi geteUtranCgi() {
        return eUtranCgi;
    }

    public void seteUtranCgi(EUtranCgi eUtranCgi) {
        this.eUtranCgi = eUtranCgi;
    }

    public TAId getTaId() {
        return taId;
    }

    public void setTaId(TAId taId) {
        this.taId = taId;
    }

    public TypeOfShape getTypeOfShape() {
        return typeOfShape;
    }

    public void setTypeOfShape(TypeOfShape typeOfShape) {
        this.typeOfShape = typeOfShape;
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

    public DiameterIdentity getMmeName() {
        return mmeName;
    }

    public void setMmeName(DiameterIdentity mmeName) {
        this.mmeName = mmeName;
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

        try {
            stringBuilder.append(", location information, CGI MCC=");
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
            stringBuilder.append(", location information, CGI MNC=");
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
            stringBuilder.append(", location information, CGI LAC=");
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
            stringBuilder.append(", location information, CGI CI=");
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode());
        } catch (MAPException e) {
            e.printStackTrace();
        }

        if (locationInformation.getLocationInformationEPS() != null) {
            stringBuilder.append(", location information, E-UTRAN CGI=");
            String eUtranCGI = new String(locationInformation.getLocationInformationEPS().getEUtranCellGlobalIdentity().getData(), StandardCharsets.ISO_8859_1);
            stringBuilder.append(eUtranCGI);
        }

        stringBuilder.append(", location information, geographical latitude=");
        stringBuilder.append(locationInformation.getGeographicalInformation().getLatitude());

        stringBuilder.append(", location information, geographical longitude=");
        stringBuilder.append(locationInformation.getGeographicalInformation().getLongitude());

        stringBuilder.append(", location information, geographical type of shape=");
        stringBuilder.append(locationInformation.getGeographicalInformation().getTypeOfShape());

        stringBuilder.append(", location information, geographical uncertainty=");
        stringBuilder.append(locationInformation.getGeographicalInformation().getUncertainty());

        stringBuilder.append(", location information, geodetic latitude=");
        stringBuilder.append(locationInformation.getGeodeticInformation().getLatitude());

        stringBuilder.append(", location information, geodetic longitude=");
        stringBuilder.append(locationInformation.getGeodeticInformation().getLongitude());

        stringBuilder.append(", location information, geodetic type of shape=");
        stringBuilder.append(locationInformation.getGeodeticInformation().getTypeOfShape());

        stringBuilder.append(", location information, geodetic uncertainty=");
        stringBuilder.append(locationInformation.getGeodeticInformation().getUncertainty());

        stringBuilder.append(", location information, geodetic confidence=");
        stringBuilder.append(locationInformation.getGeodeticInformation().getUncertainty());

        stringBuilder.append(", location information, geodetic screening abd presentation indicators=");
        stringBuilder.append(locationInformation.getGeodeticInformation().getScreeningAndPresentationIndicators());

        stringBuilder.append(", location information for GPRS=");
        stringBuilder.append(locationInformationGPRS);

        if (locationInformationEPS != null) {
            stringBuilder.append(", location information for EPS, E-UTRAN CGI=");
            String eUtranCGI = new String(locationInformationEPS.getEUtranCellGlobalIdentity().getData(), StandardCharsets.ISO_8859_1);
            stringBuilder.append(eUtranCGI);
            stringBuilder.append(", location information for EPS, MME name=");
            String mmeName = new String(locationInformationEPS.getMmeName().getData(), StandardCharsets.ISO_8859_1);
            stringBuilder.append(mmeName);
            stringBuilder.append(", location information for EPS, Tracking Area Id=");
            String taId = new String(locationInformationEPS.getTrackingAreaIdentity().getData(), StandardCharsets.ISO_8859_1);
            stringBuilder.append(taId);
        }

        stringBuilder.append(", subscriber state=");
        stringBuilder.append(subscriberState.getSubscriberStateChoice().toString());

        stringBuilder.append(", PS subscriber state=");
        stringBuilder.append(psSubscriberState.getChoice().toString());

        stringBuilder.append(", IMEI=");
        stringBuilder.append(imei.getIMEI());

        stringBuilder.append(", MSClassmark2=");
        String msClassMark2 = new String(msClassmark2.getData(), StandardCharsets.ISO_8859_1);
        stringBuilder.append(msClassMark2);

        stringBuilder.append(", GPRS MS Class=");
        stringBuilder.append(gprsmsClass);

        stringBuilder.append(", IMS Voice Over PS Sessions Support Indicator=");
        stringBuilder.append(imsVoiceOverPsSessionsSupportIndicator);

        stringBuilder.append(", time zone=");
        stringBuilder.append(timeZone);

        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
