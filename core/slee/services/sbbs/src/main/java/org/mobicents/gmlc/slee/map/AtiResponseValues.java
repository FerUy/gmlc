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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;

import java.io.Serializable;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class AtiResponseValues implements Serializable {

  private static final long serialVersionUID = -5012512208618460289L;

  /*
   3GPP TS 29.002 MAP Specification v15.0.0 (2017-03-15)

    8.11	Subscriber Information services

    8.11.1	MAP-ANY-TIME-INTERROGATION service

    8.11.1.1	Definition
    This service is used by the gsmSCF, to request information (e.g. subscriber state and location) from the HLR or the GMLC at any time.
    This service may also be used by the gsmSCF to request the Mobile Number Portability (MNP) information from the NPLR.
    This service is also used by the Presence Network Agent to request information, (e.g. subscriber state and location)
    about the subscriber (associated with a presentity) from the HLR at any time (see 3GPP TS 23.141).
    When this service is used to the HLR, the subscriber state, location, Time Zone, or T-ADS data may be requested.
    When this service is used to the GMLC, only the location may be requested.
    When this service is used to the NPLR, only the MNP information may be requested.

    8.11.1.2	Service primitives
    Parameter name                              Request     Indication  Response    Confirm
    Invoke id                                      M           M(=)        M(=)       M(=)
    Requested Info                                 M           M(=)
    Requested domain                               C           C(=)
    MNP Requested Info                             C           C(=)
    gsmSCF-Address                                 M           M(=)
    IMSI                                           C           C(=)
    MSISDN                                         C           C(=)
    Location Information                                                    C         C(=)
    Location Information for GPRS                                           C         C(=)
    Location Information for EPS                                            C         C(=)
    Subscriber State                                                        C         C(=)
    PS Subscriber State                                                     C         C(=)
    EPS Subscriber State                                                    C         C(=)
    IMEI                                                                    C         C(=)
    MS Classmark 2                                                          C         C(=)
    GPRS MS Class                                                           C         C(=)
    MNP info Result                                                         C         C(=)
    IMS Voice Over PS Sessions Support Indicator                            C         C(=)
    Last UE Activity Time                                                   C         C(=)
    Last RAT Type                                                           C         C(=)
    Time Zone                                                               C         C(=)
    Daylight Saving Time                                                    C         C(=)
    User error                                                              C         C(=)
    Provider error                                                                    O

  ? (M): mandatory parameter.
  ? (O): provider option.
  ? (C): conditional parameter (i.e. it will always be present in the indication type primitive
         if it was present in the corresponding request type primitive).
  ? (U): TC-user optional parameter.
  ? (=): the parameter must have the same value in the indication primitive as provided in the
         corresponding request primitive.
  ? A blank Indicates that the parameter is not applicable.

  */
  private LocationInformation locationInformation; // The VLR indicates in this parameter the location of the served subscriber as defined in 3GPP TS 23.018.
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


  public AtiResponseValues() {
  }

  public AtiResponseValues(LocationInformation locationInformation, SubscriberState subscriberState) {
    this.locationInformation = locationInformation;
    this.subscriberState = subscriberState;
  }

  public LocationInformation getLocationInformation() {
    return locationInformation;
  }

  public void setLocationInformation(LocationInformation locationInformation) {
    this.locationInformation = locationInformation;
    this.ageOfLocationInfo = locationInformation.getAgeOfLocationInformation();
    this.vlrNumber = locationInformation.getVlrNumber();
  }

  public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
    return cellGlobalIdOrServiceAreaIdOrLAI;
  }

  public void setCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI) {
    this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
    if (cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength() != null) {
      try {
        this.mcc = cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMCC();
        this.mnc = cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMNC();
        this.lac = cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getLac();
      } catch (MAPException e) {
        e.printStackTrace();
      }
    }
    if (cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
      try {
        this.mcc = cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC();
        this.mnc = cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC();
        this.lac = cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac();
        this.ci = cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode();
      } catch (MAPException e) {
        e.printStackTrace();
      }
    }
  }

  public CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalIdOrServiceAreaIdFixedLength() {
    return cellGlobalIdOrServiceAreaIdFixedLength;
  }

  public void setCellGlobalIdOrServiceAreaIdFixedLength(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength) {
    this.cellGlobalIdOrServiceAreaIdFixedLength = cellGlobalIdOrServiceAreaIdFixedLength;
    try {
      this.mcc = cellGlobalIdOrServiceAreaIdFixedLength.getMCC();
      this.mnc = cellGlobalIdOrServiceAreaIdFixedLength.getMNC();
      this.lac = cellGlobalIdOrServiceAreaIdFixedLength.getLac();
      this.ci = cellGlobalIdOrServiceAreaIdFixedLength.getCellIdOrServiceAreaCode();
    } catch (MAPException e) {
      e.printStackTrace();
    }
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

  public ISDNAddressString getVlrNumber() {
    return vlrNumber;
  }

  public void setVlrNumber(ISDNAddressString vlrNumber) {
    this.vlrNumber = vlrNumber;
  }

  public int getAgeOfLocationInfo() {
    return ageOfLocationInfo;
  }

  public void setAgeOfLocationInfo(int ageOfLocationInfo) {
    this.ageOfLocationInfo = ageOfLocationInfo;
  }

  public LocationInformationGPRS getLocationInformationGPRS() {
    return locationInformationGPRS;
  }

  public void setLocationInformationGPRS(LocationInformationGPRS locationInformationGPRS) {
    this.locationInformationGPRS = locationInformationGPRS;
  }

  public LocationInformationEPS getLocationInformationEPS() {
    return locationInformationEPS;
  }

  public void setLocationInformationEPS(LocationInformationEPS locationInformationEPS) {
    this.locationInformationEPS = locationInformationEPS;
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

  public void setMsClassmark2(MSClassmark2 msClassmark2) {
    this.msClassmark2 = msClassmark2;
  }

  public GPRSMSClass getGprsmsClass() {
    return gprsmsClass;
  }

  public void setGprsmsClass(GPRSMSClass gprsmsClass) {
    this.gprsmsClass = gprsmsClass;
  }

  public MNPInfoRes getMnpInfoRes() {
    return mnpInfoRes;
  }

  public void setMnpInfoRes(MNPInfoRes mnpInfoRes) {
    this.mnpInfoRes = mnpInfoRes;
  }

  @Override
  public String toString() {

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("AtiResponseValues [");

    stringBuilder.append(", locationInformation=");
    stringBuilder.append(locationInformation);

    stringBuilder.append(", cellGlobalIdOrServiceAreaIdOrLAI=");
    stringBuilder.append(cellGlobalIdOrServiceAreaIdOrLAI);

    stringBuilder.append(", cellGlobalIdOrServiceAreaIdFixedLength=");
    stringBuilder.append(cellGlobalIdOrServiceAreaIdFixedLength);

    stringBuilder.append(", locationInformationGPRS=");
    stringBuilder.append(locationInformationGPRS);

    stringBuilder.append(", locationInformationEPS=");
    stringBuilder.append(locationInformationEPS);

    stringBuilder.append(", subscriberState=");
    stringBuilder.append(subscriberState);

    stringBuilder.append(", PSSubscriberState=");
    stringBuilder.append(psSubscriberState);

    stringBuilder.append(", IMEI=");
    stringBuilder.append(imei);

    stringBuilder.append(", MSClassmark2=");
    stringBuilder.append(msClassmark2);

    stringBuilder.append(", GPRSMSClass=");
    stringBuilder.append(gprsmsClass);

    stringBuilder.append(", MNPInfoRes=");
    stringBuilder.append(mnpInfoRes);

    stringBuilder.append("]");
    return stringBuilder.toString();
  }

}
