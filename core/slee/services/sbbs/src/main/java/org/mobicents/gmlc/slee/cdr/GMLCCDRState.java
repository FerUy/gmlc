/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.gmlc.slee.cdr;

import java.io.Serializable;
import java.util.UUID;

import org.joda.time.DateTime;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;

import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMNList;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Represents state associated with ongoing dialog required for proper CDR
 * generation. Data which should be used for CDR is spread across many objects.
 * So we need object which can be used to store them in one convenient place.
 *
 * @author <a href="mailto:bbaranow@redhat.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class GMLCCDRState implements Serializable {

  public static final String GMLC_STRING_SEPARATOR = ",";

  // TODO: AddressString and IMSI hashCode + equals
  protected boolean initiated, generated;

  protected String id;

  protected RecordStatus recordStatus;

  // Dialog params
  protected Long localDialogId, remoteDialogId;
  //NB: once we fully update to JDK8, we should revert to using standard java.time package
  protected DateTime dialogStartTime, dialogEndTime;
  protected Long dialogDuration;

  // Circuit Switched Core Network / SS7 params
  protected AddressString origReference, destReference;

  protected IMSI imsi;
  protected AddressString vlrAddress;

  protected ISDNAddressString isdnAddressString;
  protected SccpAddress localAddress, remoteAddress;

  //MAP ATI response CGI and subscriber state parameters
  protected int ci;
  protected int lac;
  protected int mcc;
  protected int mnc;
  protected int aol;
  protected ISDNAddressString atiVlrGt;
  protected String subscriberState;

  //MAP LSM operations exclusive parameters.
  protected LCSClientID lcsClientID;
  protected LMSI lmsi;
  protected ISDNAddressString networkNodeNumber;
  protected boolean gprsNodeIndicator;
  protected AdditionalNumber additionalNumber;
  protected DiameterIdentity mmeName, sgsnName, sgsnRealm, aaaServerName;
  protected GSNAddress hGmlcAddress, vGmlcAddress, pprAddress;
  protected ExtGeographicalInformation locationEstimate;
  protected boolean moLrShortCircuitIndicator;
  protected PositioningDataInformation geranPositioningDataInformation;
  protected UtranPositioningDataInfo utranPositioningDataInfo;
  protected GeranGANSSpositioningData geranGANSSpositioningData;
  protected UtranGANSSpositioningData utranGANSSpositioningData;
  protected int ageOfLocationEstimate;
  protected AddGeographicalInformation additionalLocationEstimate;
  protected boolean deferredMTLRResponseIndicator;
  protected CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
  protected AccuracyFulfilmentIndicator accuracyFulfilmentIndicator;
  protected VelocityEstimate velocityEstimate;
  protected ServingNodeAddress servingNodeAddress;
  protected LCSQoS lcsQoS;
  protected int lcsReferenceNumber;
  protected String barometricPressureMeasurement;
  protected String civicAddress;
  protected LCSEvent lcsEvent;
  protected ISDNAddressString msisdn;
  protected IMEI imei;
  protected DeferredmtlrData deferredmtlrData;
  protected int lcsServiceTypeID;
  protected boolean pseudonymIndicator;
  protected int sequenceNumber;
  protected PeriodicLDRInfo periodicLDRInfo;
  protected ReportingPLMNList reportingPLMNList;

  //MAP PSI operation parameters.
  protected SubscriberInfo subscriberInfo;
  protected LocationInformation locationInformation;
  protected Double psiGeographicLatitude, psiGeographicLongitude, psiGeographicUncertainty;
  protected Double psiGeodeticLatitude, psiGeodeticLongitude, psiGeodeticUncertainty;
  protected int psiGeodeticConfidence, psiScreeningAndPresentationIndicators;
  protected TypeOfShape typeOfShape;
  protected MNPInfoRes mnpInfoRes;
  protected int mnpStatus, mnpRouteingNumber;
  protected IMSI mnpIMSI;
  protected ISDNAddressString mnpMSISDN;
  protected ISDNAddressString psiVlrNumber;
  protected ISDNAddressString psiMscNumber;
  protected boolean psiSaiPresent;
  protected boolean currentLocationRetrieved;

  /****************/
  /*** GETTERS ***/
  /**************/

  /**
   * @return the GMLC string separator
   */
  public static String getGmlcStringSeparator() {
    return GMLC_STRING_SEPARATOR;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the initiated
   */
  public boolean isInitiated() {
    return initiated;
  }

  /**
   * @return the initiated
   */
  public boolean isInitialized() {
    return this.initiated;
  }

  /**
   * @return the generated
   */
  public boolean isGenerated() {
    return generated;
  }

  /**
   * @return the origReference
   */
  public AddressString getOrigReference() {
    return origReference;
  }

  /**
   * @return the destReference
   */
  public AddressString getDestReference() {
    return destReference;
  }

  /**
   * @return the imsi
   */
  public IMSI getImsi() {
    return imsi;
  }

  /**
   * @return the vlrAddress
   */
  public AddressString getVlrAddress() {
    return vlrAddress;
  }

  /**
   * @return the ISDNAddressString
   */
  public ISDNAddressString getISDNAddressString() {
    return isdnAddressString;
  }

  /**
   * @return the ISDNAddressString
   */
  public ISDNAddressString getIsdnAddressString() {
    return isdnAddressString;
  }

  /**
   * @return the localAddress (GMLC)
   */
  public SccpAddress getLocalAddress() {
    return localAddress;
  }

  /**
   * @return the remoteAddress (Core Network Entity responding address)
   */
  public SccpAddress getRemoteAddress() {
    return remoteAddress;
  }

  /**
   * @return the localDialogId
   */
  public Long getLocalDialogId() {
    return localDialogId;
  }

  /**
   * @return the remoteDialogId
   */
  public Long getRemoteDialogId() {
    return this.remoteDialogId;
  }

  /**
   * @return the dialogStartTime
   */
  public DateTime getDialogStartTime() {
    return this.dialogStartTime;
  }

  /**
   * @return dialogEndTime
   */
  public DateTime getDialogEndTime() {
    return this.dialogEndTime;
  }

  /**
   * @return dialogDuration to set
   */
  public Long getDialogDuration() {
    return this.dialogDuration;
  }

  /**
   * @return the recordStatus
   */
  public RecordStatus getRecordStatus() {
    return recordStatus;
  }

  /**
   * @return the CGI Cell Id
   */
  public int getCi() {
    return ci;
  }

  /**
   * @return the CGI Location Area Code
   */
  public int getLac() {
    return lac;
  }

  /**
   * @return the CGI Mobile Country Code
   */
  public int getMcc() {
    return mcc;
  }

  /**
   * @return the CGI Mobile Network Code
   */
  public int getMnc() {
    return mnc;
  }

  /**
   * @return the CGI Age of Location
   */
  public int getAol() {
    return aol;
  }

  /**
   * @return the VLR Global Title since last MAP Update Location
   */
  public ISDNAddressString getAtiVlrGt() {
    return atiVlrGt;
  }

  /**
   * @return the target subscriber state from MAP ATI
   */
  public String getSubscriberState() {
    return subscriberState;
  }

  /**
   * @return the LCS Client ID
   */
  public LCSClientID getLcsClientID() {
    return lcsClientID;
  }

  /**
   * @return the LMSI
   */
  public LMSI getLmsi() {
    return lmsi;
  }

  /**
   * @return the Network Node Number
   */
  public ISDNAddressString getNetworkNodeNumber() {
    return networkNodeNumber;
  }

  /**
   * @return GPRS Node Indicator
   */
  public boolean isGprsNodeIndicator() {
    return gprsNodeIndicator;
  }

  /**
   * @return the Additional Number
   */
  public AdditionalNumber getAdditionalNumber() {
    return additionalNumber;
  }

  /**
   * @return the MME Name
   */
  public DiameterIdentity getMmeName() {
    return mmeName;
  }

  /**
   * @return the SGSN Name
   */
  public DiameterIdentity getSgsnName() {
    return sgsnName;
  }

  /**
   * @return the SGSN Realm
   */
  public DiameterIdentity getSgsnRealm() {
    return sgsnRealm;
  }

  /**
   * @return the AAA Server Name
   */
  public DiameterIdentity getAaaServerName() {
    return aaaServerName;
  }

  /**
   * @return the Home GMLC Address
   */
  public GSNAddress gethGmlcAddress() {
    return hGmlcAddress;
  }

  /**
   * @return the Visited GMLC Address
   */
  public GSNAddress getvGmlcAddress() {
    return vGmlcAddress;
  }

  /**
   * @return the PPR Address
   */
  public GSNAddress getPprAddress() {
    return pprAddress;
  }

  /**
   * @return the location estimate
   */
  public ExtGeographicalInformation getLocationEstimate() {
    return locationEstimate;
  }

  /**
   * @return the MO-LR Short Circuit indicator
   */
  public boolean isMoLrShortCircuitIndicator() {
    return moLrShortCircuitIndicator;
  }

  /**
   * @return the GERAN positioning Data info
   */
  public PositioningDataInformation getGeranPositioningDataInformation() {
    return geranPositioningDataInformation;
  }

  /**
   * @return the UTRAN positioning data info
   */
  public UtranPositioningDataInfo getUtranPositioningDataInfo() {
    return utranPositioningDataInfo;
  }

  /**
   * @return the GERAN GANSS positioning data info
   */
  public GeranGANSSpositioningData getGeranGANSSpositioningData() {
    return geranGANSSpositioningData;
  }

  /**
   * @return the UTRAN GANSS positioning data info
   */
  public UtranGANSSpositioningData getUtranGANSSpositioningData() {
    return utranGANSSpositioningData;
  }

  /**
   * @return the age of location estimate
   */
  public int getAgeOfLocationEstimate() {
    return ageOfLocationEstimate;
  }

  /**
   * @return the additional location estimate
   */
  public AddGeographicalInformation getAdditionalLocationEstimate() {
    return additionalLocationEstimate;
  }

  /**
   * @return the deferred MT LR response indicator
   */
  public boolean isDeferredMTLRResponseIndicator() {
    return deferredMTLRResponseIndicator;
  }

  /**
   * @return the CGI or SAI or LAI
   */
  public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
    return cellGlobalIdOrServiceAreaIdOrLAI;
  }

  /**
   * @return the accuracy fulfillment indicator
   */
  public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
    return accuracyFulfilmentIndicator;
  }

  /**
   * @return the velocity estimate
   */
  public VelocityEstimate getVelocityEstimate() {
    return velocityEstimate;
  }

  /**
   * @return the serving node address
   */
  public ServingNodeAddress getServingNodeAddress() {
    return servingNodeAddress;
  }

  /**
   * @return the LCS QoS
   */
  public LCSQoS getLcsQoS() {
    return lcsQoS;
  }

  /**
   * @return the LCS reference number
   */
  public int getLcsReferenceNumber() {
    return lcsReferenceNumber;
  }

  /**
   * @return the barometric pressure
   */
  public String getBarometricPressureMeasurement() {
    return barometricPressureMeasurement;
  }

  /**
   * @return the civic address
   */
  public String getCivicAddress() {
    return civicAddress;
  }

  /**
   * @return the LCS event
   */
  public LCSEvent getLcsEvent() {
    return lcsEvent;
  }

  /**
   * @return the MSISDN
   */
  public ISDNAddressString getMsisdn() {
    return msisdn;
  }

  /**
   * @return the IMEI
   */
  public IMEI getImei() {
    return imei;
  }

  /**
   * @return the deferred MT LR data
   */
  public DeferredmtlrData getDeferredmtlrData() {
    return deferredmtlrData;
  }

  /**
   * @return the LCS Service type ID
   */
  public int getLcsServiceTypeID() {
    return lcsServiceTypeID;
  }

  /**
   * @return the pseudonym indicator
   */
  public boolean isPseudonymIndicator() {
    return pseudonymIndicator;
  }

  /**
   * @return the location report sequence number
   */
  public int getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * @return the periodic LDR info
   */
  public PeriodicLDRInfo getPeriodicLDRInfo() {
    return periodicLDRInfo;
  }

  /**
   * @return the reporting PLMN list
   */
  public ReportingPLMNList getReportingPLMNList() {
    return reportingPLMNList;
  }

  /**
   * @return the subscriber info
   */
  public SubscriberInfo getSubscriberInfo() {
    return subscriberInfo;
  }

  /**
   * @return the subscriber's location info
   */
  public LocationInformation getLocationInformation() {
    return locationInformation;
  }

  /**
   * @return the subscriber's geographic
   */
  public Double getPsiGeographicLatitude() {
    return psiGeographicLatitude;
  }

  /**
   * @return the subscriber's geographic
   */
  public Double getPsiGeographicLongitude() {
    return psiGeographicLongitude;
  }

  /**
   * @return the subscriber's geographic
   */
  public Double getPsiGeographicUncertainty() {
    return psiGeographicUncertainty;
  }

  /**
   * @return the subscriber's geodetic
   */
  public Double getPsiGeodeticLatitude() {
    return psiGeodeticLatitude;
  }

  /**
   * @return the subscriber's geodetic
   */
  public Double getPsiGeodeticLongitude() {
    return psiGeodeticLongitude;
  }

  /**
   * @return the subscriber's geodetic
   */
  public Double getPsiGeodeticUncertainty() {
    return psiGeodeticUncertainty;
  }

  /**
   * @return the subscriber's geodetic
   */
  public int getPsiGeodeticConfidence() {
    return psiGeodeticConfidence;
  }

  /**
   * @return the subscriber's geodetic
   */
  public int getPsiScreeningAndPresentationIndicators() {
    return psiScreeningAndPresentationIndicators;
  }

  /**
   * @return the subscriber's geodetic or geographical type of shape
   */
  public TypeOfShape getTypeOfShape() {
    return typeOfShape;
  }

  /**
   * @return the subscriber's MNP info result
   */
  public MNPInfoRes getMnpInfoRes() {
    return mnpInfoRes;
  }

  /**
   * @return the subscriber's MNP Status
   */
  public int getMnpStatus() {
    return mnpStatus;
  }

  /**
   * @return the subscriber's MNP Routeing Number
   */
  public int getMnpRouteingNumber() {
    return mnpRouteingNumber;
  }

  /**
   * @return the subscriber's MNP IMSI
   */
  public IMSI getMnpIMSI() {
    return mnpIMSI;
  }

  /**
   * @return the subscriber's MNP MSISDN address
   */
  public ISDNAddressString getMnpMSISDN() {
    return mnpMSISDN;
  }

  /**
   * @return the subscriber's
   */
  public boolean isPsiSaiPresent() {
    return psiSaiPresent;
  }

  /**
   * @return the subscriber's
   */
  public boolean isCurrentLocationRetrieved() {
    return currentLocationRetrieved;
  }

  /**
   * @return the subscriber's location info VLR number
   */
  public ISDNAddressString getPsiVlrNumber() {
    return psiVlrNumber;
  }

  /**
   * @return the subscriber's location info MSC number
   */
  public ISDNAddressString getPsiMscNumber() {
    return psiMscNumber;
  }

  /****************/
  /*** SETTERS ***/
  /**************/

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @param initiated the initiated to set
   */
  public void setInitiated(boolean initiated) {
    this.initiated = initiated;
  }

  /**
   * @param generated to set
   */
  public void setGenerated(boolean generated) {
    this.generated = generated;
  }

  /**
   * @param origReference the origReference to set
   */
  public void setOrigReference(AddressString origReference) {
    this.origReference = origReference;
  }

  /**
   * @param destReference the destReference to set
   */
  public void setDestReference(AddressString destReference) {
    this.destReference = destReference;
  }

  /**
   * @param imsi the IMSI to set
   */
  public void setImsi(IMSI imsi) {
    this.imsi = imsi;
  }

  /**
   * @param vlrAddress the VLR Address to set
   */
  public void setVlrAddress(AddressString vlrAddress) {
    this.vlrAddress = vlrAddress;
  }

  /**
   * @param iSDNString the ISDNString to set
   */
  public void setISDNAddressString(ISDNAddressString iSDNString) {
    isdnAddressString = iSDNString;
  }

  /**
   * @param isdnAddressString the ISDNString to set
   */
  public void setIsdnAddressString(ISDNAddressString isdnAddressString) {
    this.isdnAddressString = isdnAddressString;
  }

  /**
   * @param localAddress the localAddress to set
   */
  public void setLocalAddress(SccpAddress localAddress) {
    this.localAddress = localAddress;
  }

  /**
   * @param remoteAddress the remoteAddress to set
   */
  public void setRemoteAddress(SccpAddress remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  /**
   * @param localDialogId the localDialogId to set
   */
  public void setLocalDialogId(Long localDialogId) {
    this.localDialogId = localDialogId;
  }

  /**
   * @param remoteDialogId to set
   */
  public void setRemoteDialogId(Long remoteDialogId) {
    this.remoteDialogId = remoteDialogId;
  }

  /**
   * @param dialogStartTime to set
   */
  public void setDialogStartTime(DateTime dialogStartTime) {
    this.dialogStartTime = dialogStartTime;
  }

  /**
   * @param dialogEndTime to set
   */
  public void setDialogEndTime(DateTime dialogEndTime) {
    this.dialogEndTime = dialogEndTime;
  }

  /**
   * @param dialogDuration to set
   */
  public void setDialogDuration(Long dialogDuration) {
    this.dialogDuration = dialogDuration;
  }

  /**
   * @param recordStatus the recordStatus to set
   */
  public void setRecordStatus(RecordStatus recordStatus) {
    this.recordStatus = recordStatus;
  }

  /**
   * @param ci to set
   */
  public void setCi(int ci) {
    this.ci = ci;
  }

  /**
   * @param lac to set
   */
  public void setLac(int lac) {
    this.lac = lac;
  }

  /**
   * @param mcc to set
   */
  public void setMcc(int mcc) {
    this.mcc = mcc;
  }

  /**
   * @param mnc to set
   */
  public void setMnc(int mnc) {
    this.mnc = mnc;
  }

  /**
   * @param aol to set
   */
  public void setAol(int aol) {
    this.aol = aol;
  }

  /**
   * @param atiVlrGt to set
   */
  public void setAtiVlrGt(ISDNAddressString atiVlrGt) {
    this.atiVlrGt = atiVlrGt;
  }

  /**
   * @param subscriberState to set
   */
  public void setSubscriberState(String subscriberState) {
    this.subscriberState = subscriberState;
  }

  /**
   * @param lcsClientID to set
   */
  public void setLcsClientID(LCSClientID lcsClientID) {
    this.lcsClientID = lcsClientID;
  }

  /**
   * @param lmsi to set
   */
  public void setLmsi(LMSI lmsi) {
    this.lmsi = lmsi;
  }

  /**
   * @param networkNodeNumber to set
   */
  public void setNetworkNodeNumber(ISDNAddressString networkNodeNumber) {
    this.networkNodeNumber = networkNodeNumber;
  }

  /**
   * @param gprsNodeIndicator to set
   */
  public void setGprsNodeIndicator(boolean gprsNodeIndicator) {
    this.gprsNodeIndicator = gprsNodeIndicator;
  }

  /**
   * @param additionalNumber to set
   */
  public void setAdditionalNumber(AdditionalNumber additionalNumber) {
    this.additionalNumber = additionalNumber;
  }

  /**
   * @param mmeName to set
   */
  public void setMmeName(DiameterIdentity mmeName) {
    this.mmeName = mmeName;
  }

  /**
   * @param sgsnName to set
   */
  public void setSgsnName(DiameterIdentity sgsnName) {
    this.sgsnName = sgsnName;
  }

  /**
   * @param sgsnRealm to set
   */
  public void setSgsnRealm(DiameterIdentity sgsnRealm) {
    this.sgsnRealm = sgsnRealm;
  }

  /**
   * @param aaaServerName to set
   */
  public void setAaaServerName(DiameterIdentity aaaServerName) {
    this.aaaServerName = aaaServerName;
  }

  /**
   * @param hGmlcAddress to set
   */
  public void sethGmlcAddress(GSNAddress hGmlcAddress) {
    this.hGmlcAddress = hGmlcAddress;
  }

  /**
   * @param vGmlcAddress to set
   */
  public void setvGmlcAddress(GSNAddress vGmlcAddress) {
    this.vGmlcAddress = vGmlcAddress;
  }

  /**
   * @param pprAddress to set
   */
  public void setPprAddress(GSNAddress pprAddress) {
    this.pprAddress = pprAddress;
  }

  /**
   * @param locationEstimate to set
   */
  public void setLocationEstimate(ExtGeographicalInformation locationEstimate) {
    this.locationEstimate = locationEstimate;
  }

  /**
   * @param moLrShortCircuitIndicator to set
   */
  public void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator) {
    this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
  }

  /**
   * @param geranPositioningDataInformation to set
   */
  public void setGeranPositioningDataInformation(PositioningDataInformation geranPositioningDataInformation) {
    this.geranPositioningDataInformation = geranPositioningDataInformation;
  }

  /**
   * @param utranPositioningDataInfo to set
   */
  public void setUtranPositioningDataInfo(UtranPositioningDataInfo utranPositioningDataInfo) {
    this.utranPositioningDataInfo = utranPositioningDataInfo;
  }

  /**
   * @param geranGANSSpositioningData to set
   */
  public void setGeranGANSSpositioningData(GeranGANSSpositioningData geranGANSSpositioningData) {
    this.geranGANSSpositioningData = geranGANSSpositioningData;
  }

  /**
   * @param utranGANSSpositioningData to set
   */
  public void setUtranGANSSpositioningData(UtranGANSSpositioningData utranGANSSpositioningData) {
    this.utranGANSSpositioningData = utranGANSSpositioningData;
  }

  /**
   * @param ageOfLocationEstimate to set
   */
  public void setAgeOfLocationEstimate(int ageOfLocationEstimate) {
    this.ageOfLocationEstimate = ageOfLocationEstimate;
  }

  /**
   * @param additionalLocationEstimate to set
   */
  public void setAdditionalLocationEstimate(AddGeographicalInformation additionalLocationEstimate) {
    this.additionalLocationEstimate = additionalLocationEstimate;
  }

  /**
   * @param deferredMTLRResponseIndicator to set
   */
  public void setDeferredMTLRResponseIndicator(boolean deferredMTLRResponseIndicator) {
    this.deferredMTLRResponseIndicator = deferredMTLRResponseIndicator;
  }

  /**
   * @param cellGlobalIdOrServiceAreaIdOrLAI to set
   */
  public void setCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI) {
    this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
  }

  /**
   * @param accuracyFulfilmentIndicator to set
   */
  public void setAccuracyFulfilmentIndicator(AccuracyFulfilmentIndicator accuracyFulfilmentIndicator) {
    this.accuracyFulfilmentIndicator = accuracyFulfilmentIndicator;
  }

  /**
   * @param velocityEstimate to set
   */
  public void setVelocityEstimate(VelocityEstimate velocityEstimate) {
    this.velocityEstimate = velocityEstimate;
  }

  /**
   * @param servingNodeAddress to set
   */
  public void setServingNodeAddress(ServingNodeAddress servingNodeAddress) {
    this.servingNodeAddress = servingNodeAddress;
  }

  /**
   * @param lcsQoS to set
   */
  public void setLcsQoS(LCSQoS lcsQoS) {
    this.lcsQoS = lcsQoS;
  }

  /**
   * @param lcsReferenceNumber to set
   */
  public void setLcsReferenceNumber(int lcsReferenceNumber) {
    this.lcsReferenceNumber = lcsReferenceNumber;
  }

  /**
   * @param barometricPressureMeasurement to set
   */
  public void setBarometricPressureMeasurement(String barometricPressureMeasurement) {
    this.barometricPressureMeasurement = barometricPressureMeasurement;
  }

  /**
   * @param civicAddress to set
   */
  public void setCivicAddress(String civicAddress) {
    this.civicAddress = civicAddress;
  }

  /**
   * @param lcsEvent to set
   */
  public void setLcsEvent(LCSEvent lcsEvent) {
    this.lcsEvent = lcsEvent;
  }

  /**
   * @param msisdn to set
   */
  public void setMsisdn(ISDNAddressString msisdn) {
    this.msisdn = msisdn;
  }

  /**
   * @param imei to set
   */
  public void setImei(IMEI imei) {
    this.imei = imei;
  }

  /**
   * @param deferredmtlrData to set
   */
  public void setDeferredmtlrData(DeferredmtlrData deferredmtlrData) {
    this.deferredmtlrData = deferredmtlrData;
  }

  /**
   * @param lcsServiceTypeID to set
   */
  public void setLcsServiceTypeID(int lcsServiceTypeID) {
    this.lcsServiceTypeID = lcsServiceTypeID;
  }

  /**
   * @param pseudonymIndicator to set
   */
  public void setPseudonymIndicator(boolean pseudonymIndicator) {
    this.pseudonymIndicator = pseudonymIndicator;
  }

  /**
   * @param sequenceNumber to set
   */
  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * @param periodicLDRInfo to set
   */
  public void setPeriodicLDRInfo(PeriodicLDRInfo periodicLDRInfo) {
    this.periodicLDRInfo = periodicLDRInfo;
  }

  /**
   * @param reportingPLMNList to set
   */
  public void setReportingPLMNList(ReportingPLMNList reportingPLMNList) {
    this.reportingPLMNList = reportingPLMNList;
  }

  /**
   * @param subscriberInfo to set
   */
  public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
    this.subscriberInfo = subscriberInfo;
  }

  /**
   * @param locationInformation to set
   */
  public void setLocationInformation(LocationInformation locationInformation) {
    this.locationInformation = locationInformation;
  }

  /**
   * @param psiVlrNumber subscriber's location information to set
   */
  public void setPsiVlrNumber(ISDNAddressString psiVlrNumber) {
    this.psiVlrNumber = psiVlrNumber;
  }

  /**
   * @param psiMscNumber subscriber's location information to set
   */
  public void setPsiMscNumber(ISDNAddressString psiMscNumber) {
    this.psiMscNumber = psiMscNumber;
  }

  /**
   * @param psiGeographicLatitude subscriber's location information to set
   */
  public void setPsiGeographicLatitude(Double psiGeographicLatitude) {
    this.psiGeographicLatitude = psiGeographicLatitude;
  }

  /**
   * @param psiGeographicLongitude subscriber's location information to set
   */
  public void setPsiGeographicLongitude(Double psiGeographicLongitude) {
    this.psiGeographicLongitude = psiGeographicLongitude;
  }

  /**
   * @param psiGeographicUncertainty subscriber's location information to set
   */
  public void setPsiGeographicUncertainty(Double psiGeographicUncertainty) {
    this.psiGeographicUncertainty = psiGeographicUncertainty;
  }

  /**
   * @param psiGeodeticLatitude subscriber's location information to set
   */
  public void setPsiGeodeticLatitude(Double psiGeodeticLatitude) {
    this.psiGeodeticLatitude = psiGeodeticLatitude;
  }

  /**
   * @param psiGeodeticLongitude subscriber's location information to set
   */
  public void setPsiGeodeticLongitude(Double psiGeodeticLongitude) {
    this.psiGeodeticLongitude = psiGeodeticLongitude;
  }

  /**
   * @param psiGeodeticUncertainty subscriber's location information to set
   */
  public void setPsiGeodeticUncertainty(Double psiGeodeticUncertainty) {
    this.psiGeodeticUncertainty = psiGeodeticUncertainty;
  }

  /**
   * @param psiGeodeticConfidence subscriber's location information to set
   */
  public void setPsiGeodeticConfidence(int psiGeodeticConfidence) {
    this.psiGeodeticConfidence = psiGeodeticConfidence;
  }

  /**
   * @param psiScreeningAndPresentationIndicators subscriber's location information to set
   */
  public void setPsiScreeningAndPresentationIndicators(int psiScreeningAndPresentationIndicators) {
    this.psiScreeningAndPresentationIndicators = psiScreeningAndPresentationIndicators;
  }

  /**
   * @param typeOfShape subscriber's location information to set
   */
  public void setTypeOfShape(TypeOfShape typeOfShape) {
    this.typeOfShape = typeOfShape;
  }

  /**
   * @param psiSaiPresent subscriber's location information to set
   */
  public void setPsiSaiPresent(boolean psiSaiPresent) {
    this.psiSaiPresent = psiSaiPresent;
  }

  public void setCurrentLocationRetrieved(boolean currentLocationRetrieved) {
    this.currentLocationRetrieved = currentLocationRetrieved;
  }

  /**
   * @param mnpInfoRes subscriber's location information to set
   */
  public void setMnpInfoRes(MNPInfoRes mnpInfoRes) {
    this.mnpInfoRes = mnpInfoRes;
  }

  /**
   * @param mnpStatus subscriber's MNP information to set
   */
  public void setMnpStatus(int mnpStatus) {
    this.mnpStatus = mnpStatus;
  }

  /**
   * @param mnpRouteingNumber subscriber's MNP information to set
   */
  public void setMnpRouteingNumber(int mnpRouteingNumber) {
    this.mnpRouteingNumber = mnpRouteingNumber;
  }

  /**
   * @param mnpIMSI subscriber's MNP information to set
   */
  public void setMnpIMSI(IMSI mnpIMSI) {
    this.mnpIMSI = mnpIMSI;
  }

  /**
   * @param mnpMSISDN subscriber's MNP information to set
   */
  public void setMnpMSISDN(ISDNAddressString mnpMSISDN) {
    this.mnpMSISDN = mnpMSISDN;
  }

  public void init(final Long dialogId, final AddressString destRef, final AddressString origRef, final ISDNAddressString isdnAddressString,
                   final SccpAddress localAddress, final SccpAddress remoteAddress) {
    this.localDialogId = dialogId;
    this.destReference = destRef;
    this.origReference = origRef;
    this.isdnAddressString = isdnAddressString;
    this.localAddress = localAddress;
    this.remoteAddress = remoteAddress;
    // This should be enough to be unique
    this.id = UUID.randomUUID().toString();
    this.initiated = true;
    this.dialogStartTime = null;
    this.dialogEndTime = null;
    this.dialogDuration =null;
  }

  public void init(String id, boolean initiated, boolean generated, AddressString origReference, AddressString destReference,
                   IMSI imsi, AddressString vlrAddress, ISDNAddressString isdnAddressString,
                   SccpAddress localAddress, SccpAddress remoteAddress, Long localDialogId, Long remoteDialogId,
                   DateTime dialogStartTime, DateTime dialogEndTime, Long dialogDuration, RecordStatus recordStatus,
                   int ci, int lac, int mcc, int mnc, int aol, ISDNAddressString atiVlrGt, String subscriberState) {
    // This should be enough to be unique
    this.id = UUID.randomUUID().toString();
    this.initiated = true;
    this.generated = generated;
    this.origReference = origReference;
    this.destReference = destReference;
    this.imsi = imsi;
    this.vlrAddress = vlrAddress;
    this.isdnAddressString = isdnAddressString;
    this.localAddress = localAddress;
    this.remoteAddress = remoteAddress;
    this.localDialogId = localDialogId;
    this.remoteDialogId = remoteDialogId;
    this.dialogStartTime = null;
    this.dialogEndTime = null;
    this.dialogDuration = null;
    this.recordStatus = recordStatus;
    this.ci = ci;
    this.lac = lac;
    this.mcc = mcc;
    this.mnc = mnc;
    this.aol = aol;
    this.atiVlrGt = atiVlrGt;
    this.subscriberState = subscriberState;
  }

  public void init(boolean initiated, boolean generated, String id, RecordStatus recordStatus, Long localDialogId, Long remoteDialogId,
                   DateTime dialogStartTime, DateTime dialogEndTime, Long dialogDuration, AddressString origReference, AddressString destReference,
                   IMSI imsi, AddressString vlrAddress, ISDNAddressString isdnAddressString, SccpAddress localAddress, SccpAddress remoteAddress,
                   int ci, int lac, int mcc, int mnc, int aol, ISDNAddressString atiVlrGt, String subscriberState, LCSClientID lcsClientID, LMSI lmsi,
                   ISDNAddressString networkNodeNumber, boolean gprsNodeIndicator, AdditionalNumber additionalNumber, DiameterIdentity mmeName,
                   DiameterIdentity sgsnName, DiameterIdentity sgsnRealm, DiameterIdentity aaaServerName, GSNAddress hGmlcAddress, GSNAddress vGmlcAddress,
                   GSNAddress pprAddress, ExtGeographicalInformation locationEstimate, boolean moLrShortCircuitIndicator,
                   PositioningDataInformation geranPositioningDataInformation, UtranPositioningDataInfo utranPositioningDataInfo, GeranGANSSpositioningData geranGANSSpositioningData,
                   UtranGANSSpositioningData utranGANSSpositioningData, int ageOfLocationEstimate, AddGeographicalInformation additionalLocationEstimate,
                   boolean deferredMTLRResponseIndicator, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator,
                   VelocityEstimate velocityEstimate, ServingNodeAddress servingNodeAddress, LCSQoS lcsQoS, int lcsReferenceNumber, String barometricPressureMeasurement,
                   String civicAddress, LCSEvent lcsEvent, ISDNAddressString msisdn, IMEI imei, DeferredmtlrData deferredmtlrData, int lcsServiceTypeID,
                   boolean pseudonymIndicator, int sequenceNumber, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList,
                   SubscriberInfo subscriberInfo, LocationInformation locationInformation, ISDNAddressString psiVlrNumber, ISDNAddressString psiMscNumber) {
    this.initiated = initiated;
    this.generated = generated;
    this.id = id;
    this.recordStatus = recordStatus;
    this.localDialogId = localDialogId;
    this.remoteDialogId = remoteDialogId;
    this.dialogStartTime = dialogStartTime;
    this.dialogEndTime = dialogEndTime;
    this.dialogDuration = dialogDuration;
    this.origReference = origReference;
    this.destReference = destReference;
    this.imsi = imsi;
    this.vlrAddress = vlrAddress;
    this.isdnAddressString = isdnAddressString;
    this.localAddress = localAddress;
    this.remoteAddress = remoteAddress;
    this.ci = ci;
    this.lac = lac;
    this.mcc = mcc;
    this.mnc = mnc;
    this.aol = aol;
    this.atiVlrGt = atiVlrGt;
    this.subscriberState = subscriberState;
    this.lcsClientID = lcsClientID;
    this.lmsi = lmsi;
    this.networkNodeNumber = networkNodeNumber;
    this.gprsNodeIndicator = gprsNodeIndicator;
    this.additionalNumber = additionalNumber;
    this.mmeName = mmeName;
    this.sgsnName = sgsnName;
    this.sgsnRealm = sgsnRealm;
    this.aaaServerName = aaaServerName;
    this.hGmlcAddress = hGmlcAddress;
    this.vGmlcAddress = vGmlcAddress;
    this.pprAddress = pprAddress;
    this.locationEstimate = locationEstimate;
    this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
    this.geranPositioningDataInformation = geranPositioningDataInformation;
    this.utranPositioningDataInfo = utranPositioningDataInfo;
    this.geranGANSSpositioningData = geranGANSSpositioningData;
    this.utranGANSSpositioningData = utranGANSSpositioningData;
    this.ageOfLocationEstimate = ageOfLocationEstimate;
    this.additionalLocationEstimate = additionalLocationEstimate;
    this.deferredMTLRResponseIndicator = deferredMTLRResponseIndicator;
    this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
    this.accuracyFulfilmentIndicator = accuracyFulfilmentIndicator;
    this.velocityEstimate = velocityEstimate;
    this.servingNodeAddress = servingNodeAddress;
    this.lcsQoS = lcsQoS;
    this.lcsReferenceNumber = lcsReferenceNumber;
    this.barometricPressureMeasurement = barometricPressureMeasurement;
    this.civicAddress = civicAddress;
    this.lcsEvent = lcsEvent;
    this.msisdn = msisdn;
    this.imei = imei;
    this.deferredmtlrData = deferredmtlrData;
    this.lcsServiceTypeID = lcsServiceTypeID;
    this.pseudonymIndicator = pseudonymIndicator;
    this.sequenceNumber = sequenceNumber;
    this.periodicLDRInfo = periodicLDRInfo;
    this.reportingPLMNList = reportingPLMNList;
    this.subscriberInfo = subscriberInfo;
    this.locationInformation = locationInformation;
    this.psiVlrNumber = psiVlrNumber;
    this.psiMscNumber = psiMscNumber;
  }


  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((isdnAddressString == null) ? 0 : isdnAddressString.hashCode());
    result = prime * result + ((destReference == null) ? 0 : destReference.hashCode());
    result = prime * result + ((localDialogId == null) ? 0 : localDialogId.hashCode());
    result = prime * result + ((remoteDialogId == null) ? 0 : remoteDialogId.hashCode());
    result = prime * result + ((imsi == null) ? 0 : imsi.hashCode());
    result = prime * result + ((vlrAddress == null) ? 0 : vlrAddress.hashCode());
    result = prime * result + (generated ? 1231 : 1237);
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + (initiated ? 1231 : 1237);
    result = prime * result + ((localAddress == null) ? 0 : localAddress.hashCode());
    result = prime * result + ((origReference == null) ? 0 : origReference.hashCode());
    result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
    result = prime * result + ((lcsClientID == null) ? 0 : lcsClientID.hashCode());
    result = prime * result + ((lmsi == null) ? 0 : lmsi.hashCode());
    result = prime * result + ((networkNodeNumber == null) ? 0 : networkNodeNumber.hashCode());
    result = prime * result + ((additionalNumber == null) ? 0 : additionalNumber.hashCode());
    result = prime * result + ((mmeName == null) ? 0 : mmeName.hashCode());
    result = prime * result + ((sgsnName == null) ? 0 : sgsnName.hashCode());
    result = prime * result + ((sgsnRealm == null) ? 0 : sgsnRealm.hashCode());
    result = prime * result + ((aaaServerName == null) ? 0 : aaaServerName.hashCode());
    result = prime * result + ((hGmlcAddress == null) ? 0 : hGmlcAddress.hashCode());
    result = prime * result + ((vGmlcAddress == null) ? 0 : vGmlcAddress.hashCode());
    result = prime * result + ((pprAddress == null) ? 0 : pprAddress.hashCode());
    result = prime * result + ((locationEstimate == null) ? 0 : locationEstimate.hashCode());
    result = prime * result + ((geranPositioningDataInformation == null) ? 0 : geranPositioningDataInformation.hashCode());
    result = prime * result + ((utranPositioningDataInfo == null) ? 0 : utranPositioningDataInfo.hashCode());
    result = prime * result + ((geranGANSSpositioningData == null) ? 0 : geranGANSSpositioningData.hashCode());
    result = prime * result + ((utranGANSSpositioningData == null) ? 0 : utranGANSSpositioningData.hashCode());
    result = prime * result + ((networkNodeNumber == null) ? 0 : networkNodeNumber.hashCode());
    result = prime * result + ((additionalLocationEstimate == null) ? 0 : additionalLocationEstimate.hashCode());
    result = prime * result + ((cellGlobalIdOrServiceAreaIdOrLAI == null) ? 0 : cellGlobalIdOrServiceAreaIdOrLAI.hashCode());
    result = prime * result + ((accuracyFulfilmentIndicator == null) ? 0 : accuracyFulfilmentIndicator.hashCode());
    result = prime * result + ((velocityEstimate == null) ? 0 : velocityEstimate.hashCode());
    result = prime * result + ((servingNodeAddress == null) ? 0 : servingNodeAddress.hashCode());
    result = prime * result + ((lcsQoS == null) ? 0 : lcsQoS.hashCode());
    result = prime * result + ((barometricPressureMeasurement == null) ? 0 : barometricPressureMeasurement.hashCode());
    result = prime * result + ((civicAddress == null) ? 0 : civicAddress.hashCode());
    result = prime * result + ((lcsEvent == null) ? 0 : lcsEvent.hashCode());
    result = prime * result + ((msisdn == null) ? 0 : msisdn.hashCode());
    result = prime * result + ((imei == null) ? 0 : imei.hashCode());
    result = prime * result + ((deferredmtlrData == null) ? 0 : deferredmtlrData.hashCode());
    result = prime * result + ((periodicLDRInfo == null) ? 0 : periodicLDRInfo.hashCode());
    result = prime * result + ((reportingPLMNList == null) ? 0 : reportingPLMNList.hashCode());
    result = prime * result + ((locationInformation == null) ? 0 : locationInformation.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (getClass() != obj.getClass())
      return false;
    GMLCCDRState other = (GMLCCDRState) obj;

    if (isdnAddressString == null) {
      if (other.isdnAddressString != null)
        return false;
    } else if (!isdnAddressString.equals(other.isdnAddressString))
      return false;

    if (destReference == null) {
      if (other.destReference != null)
        return false;
    } else if (!destReference.equals(other.destReference))
      return false;

    if (localDialogId == null) {
      if (other.localDialogId != null)
        return false;
    } else if (!localDialogId.equals(other.localDialogId))
      return false;

    if (remoteDialogId == null) {
      if (other.remoteDialogId != null)
        return false;
    } else if (!remoteDialogId.equals(other.remoteDialogId))
      return false;

    if (imsi == null) {
      if (other.imsi != null)
        return false;
    } else if (!imsi.equals(other.imsi))
      return false;

    if (vlrAddress == null) {
      if (other.vlrAddress != null)
        return false;
    } else if (!vlrAddress.equals(other.vlrAddress))
      return false;

    if (generated != other.generated)
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;

    if (initiated != other.initiated)
      return false;

    if (localAddress == null) {
      if (other.localAddress != null)
        return false;
    } else if (!localAddress.equals(other.localAddress))
      return false;

    if (origReference == null) {
      if (other.origReference != null)
        return false;
    } else if (!origReference.equals(other.origReference))
      return false;

    if (recordStatus != other.recordStatus)
      return false;

    if (remoteAddress == null) {
      if (other.remoteAddress != null)
        return false;
    } else if (!remoteAddress.equals(other.remoteAddress))
      return false;

    if (recordStatus == null) {
      if (other.recordStatus != null)
        return false;
    } else if (!recordStatus.equals(other.recordStatus))
      return false;

    if (ci == -1) {
      if (other.ci != -1)
        return false;
    } else if (ci != other.ci)
      return false;

    if (lac == -1) {
      if (other.lac != -1)
        return false;
    } else if (lac != other.lac)
      return false;

    if (mcc == -1) {
      if (other.mcc != -1)
        return false;
    } else if (mcc != other.mcc)
      return false;

    if (mnc == -1) {
      if (other.mnc != -1)
        return false;
    } else if (mnc != other.mnc)
      return false;

    if (aol == -1) {
      if (other.aol != -1)
        return false;
    } else if (aol != other.aol)
      return false;

    if (atiVlrGt == null) {
      if (other.atiVlrGt != null)
        return false;
    } else if (!atiVlrGt.equals(other.atiVlrGt))
      return false;

    if (subscriberState == null) {
      if (other.subscriberState != null)
        return false;
    } else if (!subscriberState.equals(other.subscriberState))
      return false;

    if (lcsClientID == null) {
      if (other.lcsClientID != null)
        return false;
    } else if (!lcsClientID.equals(other.lcsClientID))
      return false;

    if (lmsi == null) {
      if (other.lmsi != null)
        return false;
    } else if (!lmsi.equals(other.lmsi))
      return false;

    if (networkNodeNumber == null) {
      if (other.networkNodeNumber != null)
        return false;
    } else if (!networkNodeNumber.equals(other.networkNodeNumber))
      return false;

    if (gprsNodeIndicator != false && gprsNodeIndicator != true) {
      if (other.gprsNodeIndicator == false && other.gprsNodeIndicator == true)
        return false;
    } else if (!(gprsNodeIndicator == (other.gprsNodeIndicator)))
      return false;

    if (additionalNumber == null) {
      if (other.additionalNumber != null)
        return false;
    } else if (!additionalNumber.equals(other.additionalNumber))
      return false;

    if (mmeName == null) {
      if (other.mmeName != null)
        return false;
    } else if (!mmeName.equals(other.mmeName))
      return false;

    if (sgsnName == null) {
      if (other.sgsnName != null)
        return false;
    } else if (!sgsnName.equals(other.sgsnName))
      return false;

    if (sgsnRealm == null) {
      if (other.sgsnRealm != null)
        return false;
    } else if (!sgsnRealm.equals(other.sgsnRealm))
      return false;

    if (aaaServerName == null) {
      if (other.aaaServerName != null)
        return false;
    } else if (!aaaServerName.equals(other.aaaServerName))
      return false;

    if (hGmlcAddress == null) {
      if (other.hGmlcAddress != null)
        return false;
    } else if (!hGmlcAddress.equals(other.hGmlcAddress))
      return false;

    if (vGmlcAddress == null) {
      if (other.vGmlcAddress != null)
        return false;
    } else if (!vGmlcAddress.equals(other.vGmlcAddress))
      return false;

    if (pprAddress == null) {
      if (other.pprAddress != null)
        return false;
    } else if (!pprAddress.equals(other.pprAddress))
      return false;

    if (locationEstimate == null) {
      if (other.locationEstimate != null)
        return false;
    } else if (!locationEstimate.equals(other.locationEstimate))
      return false;

    if (locationEstimate == null) {
      if (other.locationEstimate != null)
        return false;
    } else if (!locationEstimate.equals(other.locationEstimate))
      return false;

    if (moLrShortCircuitIndicator != false && moLrShortCircuitIndicator != true) {
      if (other.moLrShortCircuitIndicator == false && other.moLrShortCircuitIndicator == true)
        return false;
    } else if (!(moLrShortCircuitIndicator == (other.moLrShortCircuitIndicator)))
      return false;

    if (geranPositioningDataInformation == null) {
      if (other.geranPositioningDataInformation != null)
        return false;
    } else if (!geranPositioningDataInformation.equals(other.geranPositioningDataInformation))
      return false;

    if (utranPositioningDataInfo == null) {
      if (other.utranPositioningDataInfo != null)
        return false;
    } else if (!utranPositioningDataInfo.equals(other.utranPositioningDataInfo))
      return false;

    if (geranGANSSpositioningData == null) {
      if (other.geranGANSSpositioningData != null)
        return false;
    } else if (!geranGANSSpositioningData.equals(other.geranGANSSpositioningData))
      return false;

    if (utranGANSSpositioningData == null) {
      if (other.utranGANSSpositioningData != null)
        return false;
    } else if (!utranGANSSpositioningData.equals(other.utranGANSSpositioningData))
      return false;

    if (ageOfLocationEstimate == -1) {
      if (other.ageOfLocationEstimate != -1)
        return false;
    } else if (ageOfLocationEstimate != other.ageOfLocationEstimate)
      return false;

    if (additionalLocationEstimate == null) {
      if (other.additionalLocationEstimate != null)
        return false;
    } else if (!additionalLocationEstimate.equals(other.additionalLocationEstimate))
      return false;

    if (deferredMTLRResponseIndicator != false && deferredMTLRResponseIndicator != true) {
      if (other.deferredMTLRResponseIndicator == false && other.deferredMTLRResponseIndicator == true)
        return false;
    } else if (!(deferredMTLRResponseIndicator == (other.deferredMTLRResponseIndicator)))
      return false;

    if (cellGlobalIdOrServiceAreaIdOrLAI == null) {
      if (other.cellGlobalIdOrServiceAreaIdOrLAI != null)
        return false;
    } else if (!cellGlobalIdOrServiceAreaIdOrLAI.equals(other.cellGlobalIdOrServiceAreaIdOrLAI))
      return false;

    if (accuracyFulfilmentIndicator == null) {
      if (other.accuracyFulfilmentIndicator != null)
        return false;
    } else if (!accuracyFulfilmentIndicator.equals(other.accuracyFulfilmentIndicator))
      return false;

    if (velocityEstimate == null) {
      if (other.velocityEstimate != null)
        return false;
    } else if (!velocityEstimate.equals(other.velocityEstimate))
      return false;

    if (servingNodeAddress == null) {
      if (other.servingNodeAddress != null)
        return false;
    } else if (!servingNodeAddress.equals(other.servingNodeAddress))
      return false;

    if (lcsQoS == null) {
      if (other.lcsQoS != null)
        return false;
    } else if (!lcsQoS.equals(other.lcsQoS))
      return false;

    if (lcsReferenceNumber == -1) {
      if (other.lcsReferenceNumber != -1)
        return false;
    } else if (lcsReferenceNumber != other.lcsReferenceNumber)
      return false;

    if (barometricPressureMeasurement == null) {
      if (other.barometricPressureMeasurement != null)
        return false;
    } else if (!barometricPressureMeasurement.equals(other.barometricPressureMeasurement))
      return false;

    if (civicAddress == null) {
      if (other.civicAddress != null)
        return false;
    } else if (!civicAddress.equals(other.civicAddress))
      return false;

    if (lcsEvent == null) {
      if (other.lcsEvent != null)
        return false;
    } else if (!lcsEvent.equals(other.lcsEvent))
      return false;

    if (msisdn == null) {
      if (other.msisdn != null)
        return false;
    } else if (!msisdn.equals(other.msisdn))
      return false;

    if (imei == null) {
      if (other.imei != null)
        return false;
    } else if (!imei.equals(other.imei))
      return false;

    if (deferredmtlrData == null) {
      if (other.deferredmtlrData != null)
        return false;
    } else if (!deferredmtlrData.equals(other.deferredmtlrData))
      return false;

    if (lcsServiceTypeID == -1) {
      if (other.lcsServiceTypeID != -1)
        return false;
    } else if (lcsServiceTypeID != other.lcsServiceTypeID)
      return false;

    if (pseudonymIndicator != false && pseudonymIndicator != true) {
      if (other.pseudonymIndicator == false && other.pseudonymIndicator == true)
        return false;
    } else if (!(pseudonymIndicator == (other.pseudonymIndicator)))
      return false;

    if (sequenceNumber == -1) {
      if (other.sequenceNumber != -1)
        return false;
    } else if (sequenceNumber != other.sequenceNumber)
      return false;

    if (periodicLDRInfo == null) {
      if (other.periodicLDRInfo != null)
        return false;
    } else if (!periodicLDRInfo.equals(other.periodicLDRInfo))
      return false;

    if (reportingPLMNList == null) {
      if (other.reportingPLMNList != null)
        return false;
    } else if (!reportingPLMNList.equals(other.reportingPLMNList))
      return false;

    if (subscriberInfo == null) {
      if (other.subscriberInfo != null)
        return false;
    } else if (!subscriberInfo.equals(other.subscriberInfo))
      return false;

    if (locationInformation == null) {
      if (other.locationInformation != null)
        return false;
    } else if (!locationInformation.equals(other.locationInformation))
      return false;

    if (psiVlrNumber == null) {
      if (other.psiVlrNumber != null)
        return false;
    } else if (!psiVlrNumber.equals(other.psiVlrNumber))
      return false;

    if (psiMscNumber == null) {
      if (other.psiMscNumber != null)
        return false;
    } else if (!psiMscNumber.equals(other.psiMscNumber))
      return false;

    if (psiGeographicLatitude == null) {
      if (other.psiGeographicLatitude != null)
        return false;
    } else if (!psiGeographicLatitude.equals(other.psiGeographicLatitude))
      return false;

    if (psiGeographicLongitude == null) {
      if (other.psiGeographicLongitude != null)
        return false;
    } else if (!psiGeographicLongitude.equals(other.psiGeographicLongitude))
      return false;

    if (psiGeographicUncertainty == null) {
      if (other.psiGeographicUncertainty != null)
        return false;
    } else if (!psiGeographicUncertainty.equals(other.psiGeographicUncertainty))
      return false;

    if (psiGeodeticLatitude == null) {
      if (other.psiGeodeticLatitude != null)
        return false;
    } else if (!psiGeodeticLatitude.equals(other.psiGeodeticLatitude))
      return false;

    if (psiGeodeticLongitude == null) {
      if (other.psiGeodeticLongitude != null)
        return false;
    } else if (!psiGeodeticLongitude.equals(other.psiGeodeticLongitude))
      return false;

    if (psiGeodeticUncertainty == null) {
      if (other.psiGeodeticUncertainty != null)
        return false;
    } else if (!psiGeodeticUncertainty.equals(other.psiGeodeticUncertainty))
      return false;

    if (psiGeodeticConfidence == -1) {
      if (other.psiGeodeticConfidence != -1)
        return false;
    } else if (psiGeodeticConfidence != psiGeodeticConfidence)
      return false;

    if (psiScreeningAndPresentationIndicators == -1) {
      if (other.psiScreeningAndPresentationIndicators != -1)
        return false;
    } else if (psiScreeningAndPresentationIndicators != psiScreeningAndPresentationIndicators)
      return false;

    if (typeOfShape == null) {
      if (other.typeOfShape != null)
        return false;
    } else if (!typeOfShape.equals(other.typeOfShape))
      return false;

    if (mnpInfoRes == null) {
      if (other.mnpInfoRes != null)
        return false;
    } else if (!mnpInfoRes.equals(other.mnpInfoRes))
      return false;

    if (mnpStatus == -1) {
      if (other.mnpStatus != -1)
        return false;
    } else if (mnpStatus != mnpStatus)
      return false;

    if (mnpRouteingNumber == -1) {
      if (other.mnpRouteingNumber != -1)
        return false;
    } else if (mnpRouteingNumber != mnpRouteingNumber)
      return false;

    if (mnpIMSI == null) {
      if (other.mnpIMSI != null)
        return false;
    } else if (!mnpIMSI.equals(other.mnpIMSI))
      return false;

    if (mnpMSISDN == null) {
      if (other.mnpMSISDN != null)
        return false;
    } else if (!mnpMSISDN.equals(other.mnpMSISDN))
      return false;

    if (psiSaiPresent != false && psiSaiPresent != true) {
      if (other.psiSaiPresent == false && other.psiSaiPresent == true)
        return false;
    } else if (!(psiSaiPresent == (other.psiSaiPresent)))
      return false;

    if (currentLocationRetrieved != false && currentLocationRetrieved != true) {
      if (other.currentLocationRetrieved == false && other.currentLocationRetrieved == true)
        return false;
    } else if (!(currentLocationRetrieved == (other.currentLocationRetrieved)))
      return false;

    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String msisdnStr, imsiStr, lmsiStr, imeiStr;
    msisdnStr = imsiStr = lmsiStr = imeiStr = "";
    String lcsClientID_Name, lcsClientID_APN, lcsClientID_LCSClientExternalID, lcsClientID_LCSClientDialedByMS, lcsClientID_LCSRequestorID_IDString;
    lcsClientID_Name = lcsClientID_APN = lcsClientID_LCSClientDialedByMS = lcsClientID_LCSClientExternalID = lcsClientID_LCSRequestorID_IDString = "";
    String additionalMSCNumber, additionalSGSNNumber;
    additionalMSCNumber = additionalSGSNNumber = "";
    String mmeNameStr, sgsnNameStr, sgsnRealmStr, aaaServerNameStr, hGmlcAddressStr, vGmlcAddressStr, pprAddressStr;
    mmeNameStr = sgsnNameStr = sgsnRealmStr = aaaServerNameStr = hGmlcAddressStr = vGmlcAddressStr = pprAddressStr = "";
    int lcsClientID_DCS, lcsClientID_FI, lcsClientID_LCSClientInternalID, lcsClientID_LCSClientType, lcsClientID_LCSRequestorID_DCS, lcsClientID_LCSRequestorID_FI;
    lcsClientID_DCS = lcsClientID_FI = lcsClientID_LCSClientInternalID = lcsClientID_LCSClientType =
            lcsClientID_LCSRequestorID_DCS = lcsClientID_LCSRequestorID_FI = -1;
    Double locEstLatitude, locEstLongitude, locEstUncertainty, locEstIncludeAngle, locEstOffsetAngle, locEstUncertaintyAltitude,
            locEstUncertaintyRadius, locEstAngleOfMajorAxis, locEstUncertaintySemiMajorAxis, locEstUncertaintySemiMinorAxis;
    locEstLatitude = locEstLongitude = locEstUncertainty = locEstIncludeAngle = locEstOffsetAngle = locEstUncertaintyAltitude = locEstUncertaintyRadius =
            locEstAngleOfMajorAxis = locEstUncertaintySemiMajorAxis = locEstUncertaintySemiMinorAxis = 0.0;
    int locEstConfidence, locEstAltitude, locEstInnerRadius;
    locEstConfidence = locEstAltitude = locEstInnerRadius = -1;
    String locEstTypeOfShape = "";
    String geranPosDataInfo = "";
    String utranPosDataInfo = "";
    String geranGANSSPosDataInfo = "";
    String utranGANSSPosDataInfo = "";
    Double addLocEstLatitude, addLocEstLongitude, addLocEstUncertainty, addLocEstIncludeAngle, addLocEstOffsetAngle, addLocEstUncertaintyAltitude,
            addLocEstUncertaintyRadius, addLocEstAngleOfMajorAxis, addLocEstUncertaintySemiMajorAxis, addLocEstUncertaintySemiMinorAxis;
    addLocEstLatitude = addLocEstLongitude = addLocEstUncertainty = addLocEstIncludeAngle = addLocEstOffsetAngle = addLocEstUncertaintyAltitude = addLocEstUncertaintyRadius =
            addLocEstAngleOfMajorAxis = addLocEstUncertaintySemiMajorAxis = addLocEstUncertaintySemiMinorAxis = 0.0;
    int addLocEstConfidence, addLocEstAltitude, addLocEstInnerRadius;
    addLocEstConfidence = addLocEstAltitude = addLocEstInnerRadius = -1;
    String addLocEstTypeOfShape = "";
    String moLrShortCircuitIndicatorStr = "";
    String deferredMTLRResponseIndicatorStr = "";
    int lsmMCC, lsmMNC, lsmLAC, lsmCI;
    lsmMCC = lsmMNC = lsmLAC = lsmCI = -1;
    int accuracyFulfilmentIndicatorValue = -1;
    int sequenceNumberValue = -1;
    int velEstHorizontalSpeed, velEstVerticalSpeed, velEstBearing, velEstHorSpeedUncertainty, velEstVertSpeedUncertainty, velocityTypeValue;
    velEstHorizontalSpeed = velEstVerticalSpeed = velEstBearing = velEstHorSpeedUncertainty = velEstVertSpeedUncertainty = velocityTypeValue = -1;
    String servingNodeAddressMSC, servingNodeAddressSGSN, servingNodeAddressMME;
    servingNodeAddressMSC = servingNodeAddressSGSN = servingNodeAddressMME = "";
    int lcsQoSHorizontalAccuracy, lcsQoSVerticalAccuracy, lcsQoSResponseTimeCategory;
    lcsQoSHorizontalAccuracy = lcsQoSVerticalAccuracy = lcsQoSResponseTimeCategory = -1;
    String lcsQoSVerticalCoordRequest = "";
    int lcsServiceTypeIDValue = -1;
    int lcsEventValue = -1;
    String pseudonymIndicatorStr = "";
    String deferredmtlrDataLMSI, deferredmtlrDataNNN, deferredmtlrDataMSCnum, deferredmtlrDataSGSNnum, deferredmtlrDataAAAServerName,
            deferredmtlrDataMMEname, deferredmtlrDataGPRSNodeInd, deferredmtlrDataEventType;
    deferredmtlrDataLMSI = deferredmtlrDataNNN = deferredmtlrDataMSCnum = deferredmtlrDataSGSNnum = deferredmtlrDataAAAServerName =
            deferredmtlrDataMMEname = deferredmtlrDataGPRSNodeInd = deferredmtlrDataEventType = "";
    int deferredmtlrDataTerminationCause = -1;
    int periodicLDRInfoReportingAmount, periodicLDRInfoReportingInterval;
    periodicLDRInfoReportingAmount = periodicLDRInfoReportingInterval = -1;
    String reportingPLMNListArray, reportingPLMNListPrioritized;
    reportingPLMNListArray = reportingPLMNListPrioritized = "";

    int psiMCC, psiMNC, psiLAC, psiCI;
    psiMCC = psiMNC = psiLAC = psiCI = -1;
    double psiGeogLat, psiGeogLong, psiGeogUncertainty, psiGeodLat, psiGeodLong, psiGeodUncertainty;
    psiGeogLat = psiGeogLong = psiGeogUncertainty = psiGeodLat = psiGeodLong = psiGeodUncertainty = 0.00;
    int psiGeodConfidence, psiGeodScreeningAndPresentationInd;
    psiGeodConfidence = psiGeodScreeningAndPresentationInd = -1;
    String psiGeogTypeOfShape = "";
    String psiGeodTypeOfShape = "";
    String psiVLRnum = "";
    String psiMSCnum = "";
    String psiMNPImsi, psiMNPMsisdn, psiMNPRouteingNum;
    psiMNPImsi = psiMNPMsisdn = psiMNPRouteingNum = "";
    int psiMNPstatus = -1;
    String psiSAIpresent, psiCurrentLocRetrieved;
    psiSAIpresent = psiCurrentLocRetrieved = "";
    String psiSubscriberState = "";

    try {

      if (msisdn.getAddress() != null)
        msisdnStr = msisdn.getAddress();

      if (imsi.getData() != null)
        imsiStr = new String(imsi.getData());

      if (lmsi.getData() != null)
        lmsiStr = new String(lmsi.getData());

      if (imei.getIMEI() != null)
        imeiStr = new String(imei.getIMEI());

      if (locationEstimate != null) {
        locEstLatitude = locationEstimate.getLatitude();
        locEstLongitude = locationEstimate.getLongitude();
        locEstUncertainty = locationEstimate.getUncertainty();
        locEstAltitude = locationEstimate.getAltitude();
        locEstUncertaintyAltitude = locationEstimate.getUncertaintyAltitude();
        locEstConfidence = locationEstimate.getConfidence();
        locEstInnerRadius = locationEstimate.getInnerRadius();
        locEstUncertaintyRadius = locationEstimate.getUncertaintyRadius();
        locEstIncludeAngle = locationEstimate.getIncludedAngle();
        locEstOffsetAngle = locationEstimate.getOffsetAngle();
        locEstAngleOfMajorAxis = locationEstimate.getAngleOfMajorAxis();
        locEstUncertaintySemiMajorAxis = locationEstimate.getUncertaintySemiMajorAxis();
        locEstUncertaintySemiMinorAxis = locationEstimate.getUncertaintySemiMinorAxis();
        if (locationEstimate.getTypeOfShape() != null)
          locEstTypeOfShape = locationEstimate.getTypeOfShape().name();
      }

      if (geranPositioningDataInformation.getData() != null)
        geranPosDataInfo = new String(geranPositioningDataInformation.getData());

      if (utranPositioningDataInfo.getData() != null)
        utranPosDataInfo = new String(utranPositioningDataInfo.getData());

      if (geranGANSSpositioningData.getData() != null)
        geranGANSSPosDataInfo = new String(geranGANSSpositioningData.getData());

      if (utranGANSSpositioningData.getData() != null)
        utranGANSSPosDataInfo = new String(utranGANSSpositioningData.getData());

      if (additionalNumber.getMSCNumber() != null)
        additionalMSCNumber = additionalNumber.getMSCNumber().getAddress();
      if (additionalNumber.getSGSNNumber() != null)
        additionalSGSNNumber = additionalNumber.getSGSNNumber().getAddress();

      if (mmeName.getData() != null)
        mmeNameStr = new String(mmeName.getData());

      if (sgsnName.getData() != null)
        sgsnNameStr = new String(sgsnName.getData());

      if (sgsnRealm.getData() != null)
        sgsnRealmStr = new String(sgsnRealm.getData());

      if (aaaServerName.getData() != null)
        aaaServerNameStr = new String(aaaServerName.getData());

      if (hGmlcAddress.getData() != null)
        hGmlcAddressStr = new String(hGmlcAddress.getData());

      if (vGmlcAddress.getData() != null)
        vGmlcAddressStr = new String(vGmlcAddress.getData());

      if (pprAddress.getData() != null)
        pprAddressStr = new String(pprAddress.getData());

      if (additionalLocationEstimate != null) {
        addLocEstLatitude = additionalLocationEstimate.getLatitude();
        addLocEstLongitude = additionalLocationEstimate.getLongitude();
        addLocEstUncertainty = additionalLocationEstimate.getUncertainty();
        addLocEstAltitude = additionalLocationEstimate.getAltitude();
        addLocEstUncertaintyAltitude = additionalLocationEstimate.getUncertaintyAltitude();
        addLocEstConfidence = additionalLocationEstimate.getConfidence();
        addLocEstInnerRadius = additionalLocationEstimate.getInnerRadius();
        addLocEstUncertaintyRadius = additionalLocationEstimate.getUncertaintyRadius();
        addLocEstIncludeAngle = additionalLocationEstimate.getIncludedAngle();
        addLocEstOffsetAngle = additionalLocationEstimate.getOffsetAngle();
        addLocEstAngleOfMajorAxis = additionalLocationEstimate.getAngleOfMajorAxis();
        addLocEstUncertaintySemiMajorAxis = additionalLocationEstimate.getUncertaintySemiMajorAxis();
        addLocEstUncertaintySemiMinorAxis = additionalLocationEstimate.getUncertaintySemiMinorAxis();
        if (additionalLocationEstimate.getTypeOfShape() != null)
          addLocEstTypeOfShape = additionalLocationEstimate.getTypeOfShape().name();
      }

      if (moLrShortCircuitIndicator != false && moLrShortCircuitIndicator != true)
        moLrShortCircuitIndicatorStr = String.valueOf(moLrShortCircuitIndicator);

      if (deferredMTLRResponseIndicator != false && deferredMTLRResponseIndicator != true)
        deferredMTLRResponseIndicatorStr = String.valueOf(deferredMTLRResponseIndicator);

      if (cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
        lsmMCC = + cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC();
        lsmMNC = + cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC();
        lsmLAC = + cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac();
        lsmCI = + cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode();
      }
      if (cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength() != null) {
        lsmMCC = + cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMCC();
        lsmMNC = + cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMNC();
        lsmLAC = + cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getLac();
      }

      if (accuracyFulfilmentIndicator != null)
        accuracyFulfilmentIndicatorValue = accuracyFulfilmentIndicator.getIndicator();

      try {
        sequenceNumberValue = sequenceNumber;
      } catch (NumberFormatException nfe) {
        sequenceNumberValue = -1;
      }

      if (velocityEstimate.getData() != null) {
          velEstHorizontalSpeed = velocityEstimate.getHorizontalSpeed();
          velEstVerticalSpeed = velocityEstimate.getVerticalSpeed();
        try {
          velEstHorSpeedUncertainty = velocityEstimate.getUncertaintyHorizontalSpeed();
          velEstVertSpeedUncertainty = velocityEstimate.getUncertaintyVerticalSpeed();
          velEstBearing = velocityEstimate.getBearing();
          velocityTypeValue = velocityEstimate.getVelocityType().getCode();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      if (pseudonymIndicator != false && pseudonymIndicator != true)
        pseudonymIndicatorStr = String.valueOf(pseudonymIndicator);

      if (servingNodeAddress.getMscNumber() != null)
        servingNodeAddressMSC = servingNodeAddress.getMscNumber().getAddress();

      if (servingNodeAddress.getSgsnNumber() != null)
        servingNodeAddressSGSN = servingNodeAddress.getSgsnNumber().getAddress();

      if (servingNodeAddress.getMmeNumber() != null)
        servingNodeAddressMME = new String(servingNodeAddress.getMmeNumber().getData());

      if (lcsClientID.getLCSAPN() != null)
        lcsClientID_APN =  new String(lcsClientID.getLCSAPN().getApn());
      if (lcsClientID.getLCSClientName() != null) {
        lcsClientID_Name = lcsClientID.getLCSClientName().getNameString().toString();
        lcsClientID_DCS = lcsClientID.getLCSClientName().getDataCodingScheme().getCode();
        lcsClientID_FI = lcsClientID.getLCSClientName().getLCSFormatIndicator().getIndicator();
      }
      if (lcsClientID.getLCSClientExternalID() != null)
        lcsClientID_LCSClientExternalID = lcsClientID.getLCSClientExternalID().getExternalAddress().getAddress();
      if (lcsClientID.getLCSClientInternalID() != null)
        lcsClientID_LCSClientInternalID = lcsClientID.getLCSClientInternalID().getId();
      if (lcsClientID.getLCSClientDialedByMS() != null)
        lcsClientID_LCSClientDialedByMS = lcsClientID.getLCSClientDialedByMS().getAddress();
      if (lcsClientID.getLCSClientType() != null)
        lcsClientID_LCSClientType = lcsClientID.getLCSClientType().getType();
      if (lcsClientID.getLCSRequestorID() != null) {
        lcsClientID_LCSRequestorID_DCS = lcsClientID.getLCSRequestorID().getDataCodingScheme().getCode();
        lcsClientID_LCSRequestorID_FI = lcsClientID.getLCSRequestorID().getLCSFormatIndicator().getIndicator();
        lcsClientID_LCSRequestorID_IDString = lcsClientID.getLCSRequestorID().getRequestorIDString().getEncodedString().toString();
      }

      if (lcsQoS.getHorizontalAccuracy() != null)
        lcsQoSHorizontalAccuracy = lcsQoS.getHorizontalAccuracy();
      if (lcsQoS.getVerticalAccuracy() != null)
        lcsQoSVerticalAccuracy = lcsQoS.getVerticalAccuracy();
      if (lcsQoS.getResponseTime() != null) {
        try {
          lcsQoSResponseTimeCategory = lcsQoS.getResponseTime().getResponseTimeCategory().getCategory();
        } catch (NumberFormatException nfe) {
          nfe.printStackTrace();
        }
      }
      if (lcsQoS.getVerticalCoordinateRequest() != true && lcsQoS.getVerticalCoordinateRequest() != false)
        lcsQoSVerticalCoordRequest = String.valueOf(lcsQoSVerticalCoordRequest);

      try {
        lcsServiceTypeIDValue = lcsServiceTypeID;
      } catch (NumberFormatException nfe) {
        nfe.printStackTrace();
      }

      try {
        lcsEventValue = lcsEvent.getEvent();
      } catch (NumberFormatException nfe) {
        nfe.printStackTrace();
      }

      if (deferredmtlrData.getDeferredLocationEventType() != null) {
        if (deferredmtlrData.getDeferredLocationEventType().getEnteringIntoArea() == true)
          deferredmtlrDataEventType = "EnteringIntoArea";
        else if (deferredmtlrData.getDeferredLocationEventType().getBeingInsideArea() == true)
          deferredmtlrDataEventType = "BeingInsideArea";
        else if (deferredmtlrData.getDeferredLocationEventType().getLeavingFromArea() == true)
          deferredmtlrDataEventType = "LeavingFromArea";
        else if (deferredmtlrData.getDeferredLocationEventType().getMsAvailable() == true)
          deferredmtlrDataEventType = "MSAvailable";
      }
      if (deferredmtlrData.getLCSLocationInfo() != null) {
        if (deferredmtlrData.getLCSLocationInfo().getLMSI() != null)
          deferredmtlrDataLMSI = new String(deferredmtlrData.getLCSLocationInfo().getLMSI().getData());
        if (deferredmtlrData.getLCSLocationInfo().getNetworkNodeNumber() != null)
          deferredmtlrDataNNN = deferredmtlrData.getLCSLocationInfo().getNetworkNodeNumber().getAddress();
        if (deferredmtlrData.getLCSLocationInfo().getAdditionalNumber().getMSCNumber() != null)
          deferredmtlrDataMSCnum = deferredmtlrData.getLCSLocationInfo().getAdditionalNumber().getMSCNumber().getAddress();
        if (deferredmtlrData.getLCSLocationInfo().getAdditionalNumber().getSGSNNumber() != null)
          deferredmtlrDataSGSNnum = deferredmtlrData.getLCSLocationInfo().getAdditionalNumber().getSGSNNumber().getAddress();
        if (deferredmtlrData.getLCSLocationInfo().getAaaServerName() != null)
          deferredmtlrDataAAAServerName = new String(deferredmtlrData.getLCSLocationInfo().getAaaServerName().getData());
        if (deferredmtlrData.getLCSLocationInfo().getMmeName() != null)
          deferredmtlrDataMMEname = new String(deferredmtlrData.getLCSLocationInfo().getMmeName().getData());
        if (deferredmtlrData.getLCSLocationInfo().getGprsNodeIndicator() != true &&
                deferredmtlrData.getLCSLocationInfo().getGprsNodeIndicator() != false)
          deferredmtlrDataGPRSNodeInd = String.valueOf(deferredmtlrData.getLCSLocationInfo().getGprsNodeIndicator());
      }
      if (deferredmtlrData.getTerminationCause() != null)
        deferredmtlrDataTerminationCause = deferredmtlrData.getTerminationCause().getCause();

      if (periodicLDRInfo != null) {
        periodicLDRInfoReportingAmount = periodicLDRInfo.getReportingAmount();
        periodicLDRInfoReportingInterval = periodicLDRInfo.getReportingInterval();
      }

      if (reportingPLMNList.getPlmnList() != null) {
        int i = 0;
        reportingPLMNListArray = "[ ";
        while (reportingPLMNList.getPlmnList().iterator().hasNext()) {
          reportingPLMNListArray = reportingPLMNListArray + reportingPLMNList.getPlmnList().get(i);
          i++;
          if (reportingPLMNList.getPlmnList().iterator().hasNext()) {
            reportingPLMNListArray = reportingPLMNListArray + ", ";
          } else {
            reportingPLMNListArray = reportingPLMNListArray + " ]";
          }
        }
      }
      if (reportingPLMNList.getPlmnListPrioritized() != true &&
              reportingPLMNList.getPlmnListPrioritized() != false)
        reportingPLMNListPrioritized = String.valueOf(reportingPLMNList.getPlmnListPrioritized());


      if (locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
        psiMCC = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC();
        psiMNC = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC();
        psiLAC = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac();
        psiCI = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode();
      }
      if (locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength() != null) {
        psiMCC = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC();
        psiMNC = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC();
        psiLAC = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac();
      }
      if (locationInformation.getGeographicalInformation() != null) {
        psiGeogLat = locationInformation.getGeographicalInformation().getLatitude();
        psiGeogLong = locationInformation.getGeographicalInformation().getLongitude();
        psiGeogUncertainty = locationInformation.getGeographicalInformation().getUncertainty();
        psiGeogTypeOfShape = locationInformation.getGeographicalInformation().getTypeOfShape().name();
      }
      if (locationInformation.getGeodeticInformation() != null) {
        psiGeodLat = locationInformation.getGeodeticInformation().getLatitude();
        psiGeodLong = locationInformation.getGeodeticInformation().getLongitude();
        psiGeodUncertainty = locationInformation.getGeodeticInformation().getUncertainty();
        psiGeodTypeOfShape = locationInformation.getGeodeticInformation().getTypeOfShape().name();
        psiGeodConfidence = locationInformation.getGeodeticInformation().getConfidence();
        psiGeodScreeningAndPresentationInd = locationInformation.getGeodeticInformation().getScreeningAndPresentationIndicators();
      }
      if (locationInformation.getVlrNumber() != null)
        psiVLRnum = locationInformation.getVlrNumber().getAddress();
      if (locationInformation.getMscNumber() != null)
        psiMSCnum = locationInformation.getMscNumber().getAddress();
      if (locationInformation.getSaiPresent() != true && locationInformation.getSaiPresent() != false)
        psiSAIpresent = String.valueOf(locationInformation.getSaiPresent());
      if (locationInformation.getCurrentLocationRetrieved() != true && locationInformation.getCurrentLocationRetrieved() != false)
        psiCurrentLocRetrieved = String.valueOf(locationInformation.getCurrentLocationRetrieved());

      if (subscriberInfo.getSubscriberState() != null)
        psiSubscriberState = subscriberInfo.getSubscriberState().getSubscriberStateChoice().toString();
      if (subscriberInfo.getMNPInfoRes() != null) {
        psiMNPImsi = new String(subscriberInfo.getMNPInfoRes().getIMSI().getData());
        psiMNPMsisdn = subscriberInfo.getMNPInfoRes().getMSISDN().getAddress();
        psiMNPstatus = subscriberInfo.getMNPInfoRes().getNumberPortabilityStatus().getType();
        psiMNPRouteingNum = subscriberInfo.getMNPInfoRes().getRouteingNumber().getRouteingNumber();
      }
    } catch (MAPException e) {
      e.printStackTrace();
    }



    return "GMLCCDRState [initiated=" + initiated +
            ", generated=" + generated +
            ", id=" + id +
            ", recordStatus=" + recordStatus +
            ", localDialogId=" + localDialogId  +
            ", remoteDialogId=" + remoteDialogId +
            ", dialogDuration" + dialogDuration +
            ", origReference=" + origReference +
            ", destReference=" + destReference +
            ", localAddress=" + localAddress +
            ", remoteAddress=" + remoteAddress +
            ", vlrAddress=" + vlrAddress +
            ", ISDNString=" + isdnAddressString +

            // MAP ATI response parameters concerning detail records
            ", atiMcc=" + mcc +
            ", atimMnc=" + mnc +
            ", atiLac=" + lac +
            ", atiCid=" + ci +
            ", atiAol=" + aol +
            ", atiVlrGT=" + atiVlrGt +
            ", atiSubscriberState=" + subscriberState +

            // MAP LSM response parameters parameters concerning detail records
            ", IMSI=" + imsiStr +
            ", LMSI=" + lmsiStr +
            ", networkNodeNumber=" + networkNodeNumber.getAddress() +
            ", GPRSNodeIndicator=" + gprsNodeIndicator +
            ", additionalMSCNumber=" + additionalMSCNumber +
            ", additionalSGSNNumber=" + additionalSGSNNumber +
            ", MMEName=" + mmeNameStr +
            ", SGSNName=" + sgsnNameStr +
            ", SGSNRealm=" + sgsnRealmStr +
            ", AAAServerName=" + aaaServerNameStr +
            ", vGMLCAddressData=" + vGmlcAddressStr +
            ", hGMLCAddressData=" + hGmlcAddressStr +
            ", PPRAddressData=" + pprAddressStr +
            ", locationEstimateLatitude=" + locEstLatitude +
            ", locationEstimateLongitude=" + locEstLongitude +
            ", locationEstimateUncertainty=" + locEstUncertainty +
            ", locationEstimateAltitude=" + locEstAltitude +
            ", locationEstimateUncertaintyAltitude=" + locEstUncertaintyAltitude +
            ", locationEstimateConfidence=" + locEstConfidence +
            ", locationEstimateInnerRadius=" + locEstInnerRadius +
            ", locationEstimateUncertaintyInnerRadius=" + locEstUncertaintyRadius +
            ", locationEstimateIncludeAngle=" + locEstIncludeAngle +
            ", locationEstimateOffsetAngle=" + locEstOffsetAngle +
            ", locationEstimateAngleOfMajorAxis=" + locEstAngleOfMajorAxis +
            ", locationEstimateUncertaintySemiMajorAxis=" + locEstUncertaintySemiMajorAxis +
            ", locationEstimateUncertaintySemiMinorAxis=" + locEstUncertaintySemiMinorAxis +
            ", locationEstimateTypeOfShape=" + locEstTypeOfShape +
            ", geranPositioningDataInformation=" + geranPosDataInfo +
            ", utranPositioningDataInfo=" + utranPosDataInfo +
            ", geranGANSSPositioningData=" + geranGANSSPosDataInfo +
            ", utranGANSSPositioningData=" + utranGANSSPosDataInfo +
            ", ageOfLocationEstimate=" + ageOfLocationEstimate +
            ", additionalLocationEstimateLatitude=" + addLocEstLatitude +
            ", additionalLocationEstimateLongitude=" + addLocEstLongitude +
            ", additionalLocationEstimateUncertainty=" + addLocEstUncertainty +
            ", additionalLocationEstimateAltitude=" + addLocEstAltitude +
            ", additionalLocationEstimateUncertaintyAltitude=" + addLocEstUncertaintyAltitude +
            ", additionalLocationEstimateConfidence=" + addLocEstConfidence +
            ", additionalLocationEstimateInnerRadius=" + addLocEstInnerRadius +
            ", additionalLocationEstimateUncertaintyInnerRadius=" + addLocEstUncertaintyRadius +
            ", additionalLocationEstimateIncludeAngle=" + addLocEstIncludeAngle +
            ", additionalLocationEstimateOffsetAngle=" + addLocEstOffsetAngle +
            ", additionalLocationEstimateAngleOfMajorAxis=" + addLocEstAngleOfMajorAxis +
            ", additionalLocationEstimateUncertaintySemiMajorAxis=" + addLocEstUncertaintySemiMajorAxis +
            ", additionalLocationEstimateUncertaintySemiMinorAxis=" + addLocEstUncertaintySemiMinorAxis +
            ", additionalLocationEstimateTypeOfShape=" + addLocEstTypeOfShape +
            ", deferredMTLRResponseIndicator=" + deferredMTLRResponseIndicatorStr +
            ", locationServicesMCC=" + lsmMCC +
            ", locationServicesMNC=" + lsmMNC +
            ", locationServicesLAC=" + lsmLAC +
            ", locationServicesCI=" + lsmCI +
            ", accuracyFulfilmentIndicator=" + accuracyFulfilmentIndicatorValue +
            ", sequenceNumber" + sequenceNumberValue +
            ", horizontalVelocityEstimate=" + velEstHorizontalSpeed +
            ", verticalVelocityEstimate=" + velEstVerticalSpeed +
            ", pseudonymIndicator" + pseudonymIndicatorStr +
            ", servingNodeAddressMSCNumber=" + servingNodeAddressMSC +
            ", servingNodeAddressSGSNNumber=" + servingNodeAddressSGSN +
            ", servingNodeAddressMMENumber=" + servingNodeAddressMME +
            ", lcsClientID_LCSClientName=" + lcsClientID_Name +
            ", lcsClientID_LCSClientName_DCS=" + lcsClientID_DCS +
            ", lcsClientID_LCSClientName_FormatIndicator=" + lcsClientID_FI +
            ", lcsClientID_LCS_APN=" + lcsClientID_APN +
            ", lcsClientID_LCSClientDialedByMS=" + lcsClientID_LCSClientDialedByMS +
            ", lcsClientID_LCSClientExternalID=" + lcsClientID_LCSClientExternalID +
            ", lcsClientID_LCSClientInternalID=" + lcsClientID_LCSClientInternalID +
            ", lcsClientID_LCSClientType=" + lcsClientID_LCSClientType +
            ", lcsClientID_LCSRequestorID_DCS=" + lcsClientID_LCSRequestorID_DCS +
            ", lcsClientID_LCSRequestorID_FormatIndicator=" +  lcsClientID_LCSRequestorID_FI +
            ", lcsClientID_LCSRequestorID_IDString=" + lcsClientID_LCSRequestorID_IDString +
            ", lcsQoShorizontalAccuracy=" + lcsQoSHorizontalAccuracy +
            ", lcsQoSverticalAccuracy=" + lcsQoSVerticalAccuracy +
            ", lcsQoSResponseTimeCategory=" + lcsQoSResponseTimeCategory +
            ", lcsQoSVertCoordRequest=" + lcsQoSVerticalCoordRequest +
            ", lcsReferenceNumber=" + lcsReferenceNumber +
            ", lcsServiceTypeID" + lcsServiceTypeIDValue +
            //", barometricPressureMeasurement=" + barometricPressureMeasurement +
            //", civicAddress=" + civicAddress +
            ", lcsEvent=" + lcsEventValue +
            ", MSISDN=" + msisdnStr +
            ", IMEI=" + imeiStr +
            ", deferredmtlrData_EventType=" + deferredmtlrDataEventType +
            ", deferredmtlrData_LMSI=" + deferredmtlrDataLMSI +
            ", deferredmtlrData_NNN=" + deferredmtlrDataNNN +
            ", deferredmtlrData_MSCnum=" + deferredmtlrDataMSCnum +
            ", deferredmtlrData_SGSNnum=" + deferredmtlrDataSGSNnum +
            ", deferredmtlrData_AAAserverName=" + deferredmtlrDataAAAServerName +
            ", deferredmtlrData_MMEName=" + deferredmtlrDataMMEname +
            ", deferredmtlrData_GPRSNodeIndicator=" + deferredmtlrDataGPRSNodeInd +
            ", deferredmtlrData_TerminationCause=" + deferredmtlrDataTerminationCause +
            ", PeriodicReportingAmount=" + periodicLDRInfoReportingAmount +
            ", PeriodicReportingInterval=" + periodicLDRInfoReportingInterval +
            ", moLrShortCircuitIndicator=" + moLrShortCircuitIndicatorStr +
            ", ReportingPLMNList=" + reportingPLMNListArray +
            ", ReportingPLMNList_Prioritized=" + reportingPLMNListPrioritized +

            // MAP PSI response parameters parameters concerning detail records
            ", subscriberInfo=" + subscriberInfo +
            ", psiMCC=" + psiMCC +
            ", psiMNC=" + psiMNC +
            ", psiLAC=" + psiLAC +
            ", psiCI=" + psiCI +
            ", psiGeographicLatitude=" + psiGeogLat +
            ", psiGeographicLongitude=" + psiGeogLong +
            ", psiGeographicUncertainty=" + psiGeogUncertainty +
            ", psiGeographicTypeOfShape=" + psiGeogTypeOfShape +
            ", psiGeodeticLatitude=" + psiGeodLat +
            ", psiGeodeticLongitude=" + psiGeodLong +
            ", psiGeodeticUncertainty=" + psiGeodUncertainty +
            ", psiGeodeticConfidence=" + psiGeodConfidence +
            ", psiGeodeticScreeningAndPresentationIndicators=" + psiGeodScreeningAndPresentationInd +
            ", psiGeodeticTypeOfShape=" + psiGeodTypeOfShape +
            ", psiVLRNumber=" + psiVLRnum +
            ", psiMSCNumber=" + psiMSCnum +
            ", psiSAIpresent=" + psiSAIpresent +
            ", psiCurrentLocationRetrieved=" + psiCurrentLocRetrieved +
            ", psiMNPStatus=" + psiMNPstatus +
            ", psiMNPRouteingNumber=" + psiMNPRouteingNum +
            ", psiMNPImsi=" + psiMNPImsi +
            ", psiMNPMsisdn=" + psiMNPMsisdn +
            ", psiSubscriberState=" + psiSubscriberState +
            "]@" + super.hashCode();
  }

}
