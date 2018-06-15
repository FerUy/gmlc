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

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class SlrRequestValues  implements Serializable {

  private static final long serialVersionUID = 8712511732599272178L;

  private static Charset isoCharset = Charset.forName("ISO-8859-1");

  /*
  3GPP TS 29.002 MAP Specification v15.0.0 (2017-03-15)

    13A.3   MAP-SUBSCRIBER-LOCATION-REPORT Service

    13A.3.1 Definition
    This service is used by a VMSC or SGSN to provide the location of a target MS to a GMLC
    when a request for location is either implicitly administered or made at some   earlier time.
    This is a confirmed service using the primitives from table 13A.3/1.

    Table 13A.3/1: Subscriber_Location_Report
    Parameter name                                      Request         Indication       Response     Confirm
    Invoke id                                               M               M(=)            M(=)        M(=)
    LCS Event                                               M               M(=)
    LCS Client ID                                           M               M(=)
    Network Node Number                                     M               M(=)
    IMSI                                                    C               C(=)
    MSISDN                                                  C               C(=)
    NA-ESRD                                                 C               C(=)            C           C(=)
    NA-ESRK                                                 C               C(=)            C           C(=)
    IMEI                                                    U               C(=)
    Location Estimate                                       C               C(=)
    GERAN Positioning Data                                  C               C(=)
    UTRAN Positioning Data                                  C               C(=)
    GERAN GANSS Positioning Data                            C               C(=)
    UTRAN GANSS Positioning Data                            C               C(=)
    UTRAN Additional Positioning Data                       C               C(=)
    UTRAN Barometric Pressure Measurement                   C               C(=)
    UTRAN Civic Address                                     C               C(=)
    Age of Location Estimate                                C               C(=)
    LMSI                                                    U               C(=)
    GPRS Node Indicator                                     C               C(=)
    Additional Location Estimate                            C               C(=)
    Deferred MT-LR Data                                     C               C(=)
    LCS-Reference Number                                    C               C(=)            C           C(=)
    NA-ESRK Request                                         C               C(=)
    Cell Id Or SAI                                          C               C(=)
    H-GMLC Address                                          C               C(=)            C           C(=)
    LCS Service Type Id                                     C               C(=)
    Pseudonym Indicator                                     C               C(=)
    Accuracy Fulfilment Indicator                           C               C(=)
    Sequence Number                                         C               C(=)
    Periodic LDR Info                                       C               C(=)
    MO-LR Short Circuit Indicator                           C               C(=)            C           C(=)
    Target Serving Node for Handover                        C               C(=)
    Reporting PLMN List                                                                     C           C(=)
    User error                                                                              C           C(=)
    Provider error                                                                                      O

    ? (M): mandatory parameter.
    ? (O): provider option.
    ? (C): conditional parameter (i.e. it will always be present in the indication type primitive
         if it was present in the corresponding request type primitive).
    ? (U): TC-user optional parameter.
    ? (=): the parameter must have the same value in the indication primitive as provided in the
         corresponding request primitive.
    ? A blank Indicates that the parameter is not applicable.

  */
  private LCSEvent lcsEvent;
  private LCSClientID lcsClientID;
  private ISDNAddressString networkNodeNumber;
  private IMSI imsi;
  private ISDNAddressString msisdn;
  private IMEI imei;
  private ExtGeographicalInformation locationEstimate;
  private PositioningDataInformation geranPositioningDataInformation;
  private UtranPositioningDataInfo utranPositioningDataInfo;
  private GeranGANSSpositioningData geranGANSSpositioningData;
  private UtranGANSSpositioningData utranGANSSpositioningData;
  private int ageOfLocationEstimate;
  private LMSI lmsi;
  private boolean gprsNodeIndicator;
  private AddGeographicalInformation additionalLocationEstimate;
  private DeferredmtlrData deferredmtlrData;
  private int lcsReferenceNumber;
  private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
  private GSNAddress hGmlcAddress;
  private int lcsServiceTypeID;
  private boolean pseudonymIndicator;
  private AccuracyFulfilmentIndicator accuracyFulfilmentIndicator;
  private int sequenceNumber;
  private PeriodicLDRInfo periodicLDRInfo;
  private boolean moLrShortCircuitIndicator;
  private ReportingPLMNList reportingPLMNList;
  private VelocityEstimate velocityEstimate;
  private ServingNodeAddress servingNodeAddress;
  private ISDNAddressString naESRD, naESRK;

  public SlrRequestValues() {
  }

  public SlrRequestValues(IMSI imsi, ExtGeographicalInformation locationEstimate, int lcsReferenceNumber,
                          CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI) {
    this.imsi = imsi;
    this.locationEstimate = locationEstimate;
    this.lcsReferenceNumber = lcsReferenceNumber;
    this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
  }

  public SlrRequestValues(LCSEvent lcsEvent, LCSClientID lcsClientID, ISDNAddressString networkNodeNumber,
                          IMSI imsi, ISDNAddressString msisdn, IMEI imei, ExtGeographicalInformation locationEstimate,
                          PositioningDataInformation geranPositioningDataInformation, UtranPositioningDataInfo utranPositioningDataInfo,
                          GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
                          int ageOfLocationEstimate, LMSI lmsi, boolean gprsNodeIndicator,
                          AddGeographicalInformation additionalLocationEstimate, DeferredmtlrData deferredmtlrData,
                          int lcsReferenceNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
                          GSNAddress hGmlcAddress, int lcsServiceTypeID, boolean pseudonymIndicator,
                          AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, int sequenceNumber,
                          PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator, ReportingPLMNList reportingPLMNList,
                          ISDNAddressString naESRD, ISDNAddressString naESRK) {
    this.lcsEvent = lcsEvent;
    this.lcsClientID = lcsClientID;
    this.networkNodeNumber = networkNodeNumber;
    this.imsi = imsi;
    this.msisdn = msisdn;
    this.imei = imei;
    this.locationEstimate = locationEstimate;
    this.geranPositioningDataInformation = geranPositioningDataInformation;
    this.utranPositioningDataInfo = utranPositioningDataInfo;
    this.geranGANSSpositioningData = geranGANSSpositioningData;
    this.utranGANSSpositioningData = utranGANSSpositioningData;
    this.ageOfLocationEstimate = ageOfLocationEstimate;
    this.lmsi = lmsi;
    this.gprsNodeIndicator = gprsNodeIndicator;
    this.additionalLocationEstimate = additionalLocationEstimate;
    this.deferredmtlrData = deferredmtlrData;
    this.lcsReferenceNumber = lcsReferenceNumber;
    this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
    this.hGmlcAddress = hGmlcAddress;
    this.lcsServiceTypeID = lcsServiceTypeID;
    this.pseudonymIndicator = pseudonymIndicator;
    this.accuracyFulfilmentIndicator = accuracyFulfilmentIndicator;
    this.sequenceNumber = sequenceNumber;
    this.periodicLDRInfo = periodicLDRInfo;
    this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
    this.reportingPLMNList = reportingPLMNList;
    this.naESRD = naESRD;
    this.naESRK = naESRK;
  }

  public LCSEvent getLcsEvent() {
    return lcsEvent;
  }

  public void setLcsEvent(LCSEvent lcsEvent) {
    this.lcsEvent = lcsEvent;
  }

  public LCSClientID getLcsClientID() {
    return lcsClientID;
  }

  public void setLcsClientID(LCSClientID lcsClientID) {
    this.lcsClientID = lcsClientID;
  }

  public ISDNAddressString getNetworkNodeNumber() {
    return networkNodeNumber;
  }

  public void setNetworkNodeNumber(ISDNAddressString networkNodeNumber) {
    this.networkNodeNumber = networkNodeNumber;
  }

  public IMSI getImsi() {
    return imsi;
  }

  public void setImsi(IMSI imsi) {
    this.imsi = imsi;
  }

  public ISDNAddressString getMsisdn() {
    return msisdn;
  }

  public void setMsisdn(ISDNAddressString msisdn) {
    this.msisdn = msisdn;
  }

  public IMEI getImei() {
    return imei;
  }

  public void setImei(IMEI imei) {
    this.imei = imei;
  }

  public ExtGeographicalInformation getLocationEstimate() {
    return locationEstimate;
  }

  public void setLocationEstimate(ExtGeographicalInformation locationEstimate) {
    this.locationEstimate = locationEstimate;
  }

  public PositioningDataInformation getGeranPositioningDataInformation() {
    return geranPositioningDataInformation;
  }

  public void setGeranPositioningDataInformation(PositioningDataInformation geranPositioningDataInformation) {
    this.geranPositioningDataInformation = geranPositioningDataInformation;
  }

  public UtranPositioningDataInfo getUtranPositioningDataInfo() {
    return utranPositioningDataInfo;
  }

  public void setUtranPositioningDataInfo(UtranPositioningDataInfo utranPositioningDataInfo) {
    this.utranPositioningDataInfo = utranPositioningDataInfo;
  }

  public GeranGANSSpositioningData getGeranGANSSpositioningData() {
    return geranGANSSpositioningData;
  }

  public void setGeranGANSSpositioningData(GeranGANSSpositioningData geranGANSSpositioningData) {
    this.geranGANSSpositioningData = geranGANSSpositioningData;
  }

  public UtranGANSSpositioningData getUtranGANSSpositioningData() {
    return utranGANSSpositioningData;
  }

  public void setUtranGANSSpositioningData(UtranGANSSpositioningData utranGANSSpositioningData) {
    this.utranGANSSpositioningData = utranGANSSpositioningData;
  }

  public int getAgeOfLocationEstimate() {
    return ageOfLocationEstimate;
  }

  public void setAgeOfLocationEstimate(int ageOfLocationEstimate) {
    this.ageOfLocationEstimate = ageOfLocationEstimate;
  }

  public LMSI getLmsi() {
    return lmsi;
  }

  public void setLmsi(LMSI lmsi) {
    this.lmsi = lmsi;
  }

  public boolean isGprsNodeIndicator() {
    return gprsNodeIndicator;
  }

  public void setGprsNodeIndicator(boolean gprsNodeIndicator) {
    this.gprsNodeIndicator = gprsNodeIndicator;
  }

  public AddGeographicalInformation getAdditionalLocationEstimate() {
    return additionalLocationEstimate;
  }

  public void setAdditionalLocationEstimate(AddGeographicalInformation additionalLocationEstimate) {
    this.additionalLocationEstimate = additionalLocationEstimate;
  }

  public DeferredmtlrData getDeferredmtlrData() {
    return deferredmtlrData;
  }

  public void setDeferredmtlrData(DeferredmtlrData deferredmtlrData) {
    this.deferredmtlrData = deferredmtlrData;
  }

  public int getLcsReferenceNumber() {
    return lcsReferenceNumber;
  }

  public void setLcsReferenceNumber(int lcsReferenceNumber) {
    this.lcsReferenceNumber = lcsReferenceNumber;
  }

  public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
    return cellGlobalIdOrServiceAreaIdOrLAI;
  }

  public void setCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI) {
    this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
  }

  public GSNAddress gethGmlcAddress() {
    return hGmlcAddress;
  }

  public void sethGmlcAddress(GSNAddress hGmlcAddress) {
    this.hGmlcAddress = hGmlcAddress;
  }

  public int getLcsServiceTypeID() {
    return lcsServiceTypeID;
  }

  public void setLcsServiceTypeID(int lcsServiceTypeID) {
    this.lcsServiceTypeID = lcsServiceTypeID;
  }

  public boolean getPseudonymIndicator() {
    return pseudonymIndicator;
  }

  public void setPseudonymIndicator(boolean pseudonymIndicator) {
    this.pseudonymIndicator = pseudonymIndicator;
  }

  public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
    return accuracyFulfilmentIndicator;
  }

  public void setAccuracyFulfilmentIndicator(AccuracyFulfilmentIndicator accuracyFulfilmentIndicator) {
    this.accuracyFulfilmentIndicator = accuracyFulfilmentIndicator;
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public PeriodicLDRInfo getPeriodicLDRInfo() {
    return periodicLDRInfo;
  }

  public void setPeriodicLDRInfo(PeriodicLDRInfo periodicLDRInfo) {
    this.periodicLDRInfo = periodicLDRInfo;
  }

  public boolean isMoLrShortCircuitIndicator() {
    return moLrShortCircuitIndicator;
  }

  public void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator) {
    this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
  }

  public ReportingPLMNList getReportingPLMNList() {
    return reportingPLMNList;
  }

  public void setReportingPLMNList(ReportingPLMNList reportingPLMNList) {
    this.reportingPLMNList = reportingPLMNList;
  }

  public VelocityEstimate getVelocityEstimate() {
    return velocityEstimate;
  }

  public void setVelocityEstimate(VelocityEstimate velocityEstimate) {
    this.velocityEstimate = velocityEstimate;
  }

  public ServingNodeAddress getServingNodeAddress() {
    return servingNodeAddress;
  }

  public void setServingNodeAddress(ServingNodeAddress servingNodeAddress) {
    this.servingNodeAddress = servingNodeAddress;
  }

  public ISDNAddressString getNaESRD() {
    return naESRD;
  }

  public void setNaESRD(ISDNAddressString naESRD) {
    this.naESRD = naESRD;
  }

  public ISDNAddressString getNaESRK() {
    return naESRK;
  }

  public void setNaESRK(ISDNAddressString naESRK) {
    this.naESRK = naESRK;
  }

  @Override
  public String toString() {

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SlrRequestValues [");

    stringBuilder.append(", IMSI=");
    stringBuilder.append(imsi);

    stringBuilder.append(", MSISDN=");
    stringBuilder.append(msisdn);

    stringBuilder.append(", LMSI=");
    stringBuilder.append(lmsi);

    stringBuilder.append(", IMEI=");
    stringBuilder.append(imei);

    stringBuilder.append(", Network Node Number=");
    stringBuilder.append(networkNodeNumber);

    stringBuilder.append(", GPRS Node Indicator=");
    stringBuilder.append(gprsNodeIndicator);

    stringBuilder.append(", H-GMLC Address=");
    stringBuilder.append(hGmlcAddress);

    stringBuilder.append(", LCS Client ID=");
    stringBuilder.append(lcsClientID);

    stringBuilder.append(", LCS Reference Number=");
    stringBuilder.append(lcsReferenceNumber);

    stringBuilder.append(", LCS Service Type ID=");
    stringBuilder.append(lcsServiceTypeID);

    stringBuilder.append(", Location Estimate=");
    stringBuilder.append(locationEstimate);

    stringBuilder.append(", age of Location Estimate=");
    stringBuilder.append(ageOfLocationEstimate);

    stringBuilder.append(", pseudonym indicator=");
    stringBuilder.append(pseudonymIndicator);

    stringBuilder.append(", MO LR Short Circuit Indicator=");
    stringBuilder.append(moLrShortCircuitIndicator);

    stringBuilder.append(", Positioning Data Information=");
    stringBuilder.append(geranPositioningDataInformation);

    stringBuilder.append(", UTRAN Positioning Data Information=");
    stringBuilder.append(utranPositioningDataInfo);

    stringBuilder.append(", CGI or SAI fixed length=");
    stringBuilder.append(cellGlobalIdOrServiceAreaIdOrLAI);

    stringBuilder.append(", Accuracy Fulfilment Indicator=");
    stringBuilder.append(accuracyFulfilmentIndicator);

    stringBuilder.append(", GERAN GANSS positioning data=");
    stringBuilder.append(geranGANSSpositioningData);

    stringBuilder.append(", UTRAN GANSS positioning data=");
    stringBuilder.append(utranGANSSpositioningData);

    stringBuilder.append(", LCS Event=");
    stringBuilder.append(lcsEvent);

    stringBuilder.append(", Deferred MT LR Data=");
    stringBuilder.append(deferredmtlrData);

    stringBuilder.append(", periodic LDR Info=");
    stringBuilder.append(periodicLDRInfo);

    stringBuilder.append(", reportingPLMNList=");
    stringBuilder.append(reportingPLMNList);

    stringBuilder.append(", sequence number=");
    stringBuilder.append(sequenceNumber);

    stringBuilder.append(", velocity estimate=");
    stringBuilder.append(velocityEstimate);

    stringBuilder.append(", serving node address=");
    stringBuilder.append(servingNodeAddress);

    stringBuilder.append(", NA-ESRD=");
    stringBuilder.append(naESRD);

    stringBuilder.append(", NA-ESRK=");
    stringBuilder.append(naESRK);

    stringBuilder.append("]");
    return stringBuilder.toString();
  }


}
