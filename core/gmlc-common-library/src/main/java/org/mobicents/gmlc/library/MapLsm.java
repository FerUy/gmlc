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

package org.mobicents.gmlc.library;

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class MapLsm implements Serializable {

  private static final long serialVersionUID = 7284375548289523817L;

  private static Charset isoCharset = Charset.forName("ISO-8859-1");

  private ISDNAddressString mlcNumber; // The E.164 (Global Title) of the requesting GMLC.
  private SubscriberIdentity msisdn; // The MSISDN is provided to identify the target MS.
  private IMSI imsi; // International Mobile Subscriber Identity defined in 3GPP TS 23.003.
  private LMSI lmsi; // Local MS identity allocated by the VLR to a given subscriber for internal management of data in the VLR

  private ISDNAddressString networkNodeNumber; // ISDN number of LCS target node (MSC or MME, SGSN, or IP-SM-GW) or of an LCS Router.

  private boolean gprsNodeIndicator; // Indication that the Network Node Number received from HLR, etc. is to be considered as the SGSN number.
  // The presence of this parameter is mandatory only if the SGSN number is sent in the Network Node Number.

  private AdditionalNumber additionalNumber; // This parameter refers to the ISDN number of an additional LCS target node (MSC or MME or SGSN) or of an LCS Router.
  // This parameter is provided in a successful response.
  // If the Network Node Number and Additional Number are received in the GMLC, the Network Node Number is used in preference to the Additional Number.

  private SupportedLCSCapabilitySets supportedLCSCapabilitySets, addSupportedLCSCapabilitySets; // Capability sets of LCS supported in the VLR or SGSN.
  // Provided only if LCS capability sets are available in HLR and Network Node Number is present in this message.

  private DiameterIdentity mmeName; // Diameter Identity of an MME as defined in 3GPP TS 23.003.

  private DiameterIdentity sgsnName, sgsnRealm; // Diameter Identity of an SGSN as defined in 3GPP TS 23.003.

  private DiameterIdentity aaaServerName; // Diameter Identity of a 3GPP AAA server as defined in 3GPP TS 29.273.

  private GSNAddress hGmlcAddress, vGmlcAddress, pprAddress; // IP address of a H-GMLC, V-GMLC and PPR.

  private LocationType locationType; // Type of location information requested, including locationEstimateType and deferredLocationEventType.
  private LocationEstimateType locationEstimateType; // currentLocation(0), currentOrLastKnownLocation(1), initialLocation(2), activateDeferredLocation(3), cancelDeferredLocation(4)..
  private DeferredLocationEventType deferredLocationEventType; // boolean getMsAvailable(); getEnteringIntoArea(); getLeavingFromArea(); getBeingInsideArea();

  private LCSClientID lcsClientID; // Provides information related to the identity of an LCS client.

  private LCSPriority lcsPriority; // normal or highest priority of LCS.

  private LCSQoS lcsQoS; // This parameter indicates the required quality of service in terms of response time and accuracy.
  // Includes horizontalAccuracy, verticalAccuracy, verticalCoordinateRequest, responseTime, extensionContainer.
  private Integer horizontalAccuracy;
  private Integer verticalAccuracy;
  private boolean verticalCoordinateRequest;
  private ResponseTimeCategory responseTimeCategory; // delaytolerant or lowdelay.
  private ResponseTime responseTime;

  private MAPExtensionContainer mapExtensionContainer;

  private IMEI imei; // International Mobile Equipment Identity.

  private SupportedGADShapes supportedGADShapes; // Includes the following:
  private boolean ellipsoidPoint;
  private boolean ellipsoidPointWithUncertaintyCircle;
  private boolean ellipsoidPointWithUncertaintyEllipse;
  private boolean polygon;
  private boolean ellipsoidPointWithAltitude;
  private boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid;
  private boolean ellipsoidArc;

  private int lcsReferenceNumber; // This parameter shall be included if a deferred MT-LR procedure is performed
  // for a UE available event, an area event or a periodic positioning event.

  private LCSCodeword lcsCodeword; // Contains the codeword associated to current positioning request.

  private int lcsServiceTypeID; // LCS Service Type of the current positioning request.

  private LCSPrivacyCheck lcsPrivacyCheck; // Refers to the requested privacy check related actions
  // (call/session unrelated and/or call/session related) from MSC or SGSN provided by H-GMLC.
  private PrivacyCheckRelatedAction callSessionUnrelated; // notAllowed, allowedIfNoResponse, allowedWithNotification, allowedWithoutNotification, restrictedIfNoResponse.
  private PrivacyCheckRelatedAction callSessionRelated; // // notAllowed, allowedIfNoResponse, allowedWithNotification, allowedWithoutNotification

  private AreaEventInfo areaEventInfo; // This parameter defines the requested deferred MT-LR area event information.
  // The parameter consists of area definition, type of area event, occurrence info and minimum interval time.
  private AreaDefinition areaDefinition;
  private AreaType areaType; // locationAreaId, routingAreaId, cellGlobalId, countryCode, plmnId, utranCellId
  private AreaIdentification areaIdentification;
  private OccurrenceInfo occurrenceInfo; // oneTimeEvent, multipleTimeEvent
  private int intervalTime;

  private ExtGeographicalInformation locationEstimate;
  private double latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis,
      angleOfMajorAxis, uncertaintyAltitude, uncertaintyRadius, offsetAngle, includedAngle;
  private int ageOfLocationEstimate, confidence, altitude, innerRadius;
  TypeOfShape typeOfShape; // Polygon, EllipsoidPoint, EllipsoidArc, EllipsoidPointWithAltitude,
  // EllipsoidPointWithAltitudeAndUncertaintyEllipsoid, EllipsoidPointWithUncertaintyCircle, EllipsoidPointWithUncertaintyEllipse

  private int pseudonymIndicator; // Indicates by its presence that the pseudonym is required

  private boolean deferredMTLRResponseIndicator; // Indicates that this is a response to a deferred mt-lr request.

  private boolean saiPresent; // GERAN = Global Cell Identifier. UTRAN = Service Area Identifier; only for Emergency Calls.

  private boolean moLrShortCircuitIndicator; // Indicates whether MO-LR Short Circuit is permitted for periodic location.

  private PositioningDataInformation geranPositioningDataInformation;
  private UtranPositioningDataInfo utranPositioningDataInfo;
  private AddGeographicalInformation additionalLocationEstimate;

  private CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength;
  private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;

  private AccuracyFulfilmentIndicator accuracyFulfilmentIndicator; // Indicates the fulfilled accuracy of the positioning procedure

  private VelocityEstimate velocityEstimate; //

  private GeranGANSSpositioningData geranGANSSpositioningData;
  private UtranGANSSpositioningData utranGANSSpositioningData;
  private ServingNodeAddress servingNodeAddress;

  private LCSEvent lcsEvent;

  private LCSClientExternalID lcsClientExternalID;
  private LCSClientInternalID lcsClientInternalID;

  private LCSFormatIndicator lcsFormatIndicator;

  private LCSClientName lcsClientName;

  private TerminationCause terminationCause;

  private DeferredmtlrData deferredmtlrData; // Used to report the deferred location event type,
  // the location information and reason why the serving node aborted monitoring the event to the GMLC.

  private PeriodicLDRInfo periodicLDRInfo; // Indicates the reporting amount and reporting interval of deferred periodic location.
  private int reportingAmount;
  private int reportingInterval;

  private ReportingPLMNList reportingPLMNList; // Indicates a list of PLMNs in which subsequent
  // periodic MO-LR TTTP requests will be made.

  private int sequenceNumber; //  refers to the number of the periodic location reports completed.
  // The sequence number would be set to 1 in the first location report and increment by 1 for each new report.

  public MapLsm() {
  }

  public ISDNAddressString getMlcNumber() {
    return mlcNumber;
  }

  public void setMlcNumber(ISDNAddressString mlcNumber) {
    this.mlcNumber = mlcNumber;
  }

  public SubscriberIdentity getMsisdn() {
    return msisdn;
  }

  public void setMsisdn(SubscriberIdentity msisdn) {
    this.msisdn = msisdn;
  }

  public IMSI getImsi() {
    return imsi;
  }

  public void setImsi(IMSI imsi) {
    this.imsi = imsi;
  }

  public LMSI getLmsi() {
    return lmsi;
  }

  public void setLmsi(LMSI lmsi) {
    this.lmsi = lmsi;
  }

  public ISDNAddressString getNetworkNodeNumber() {
    return networkNodeNumber;
  }

  public void setNetworkNodeNumber(ISDNAddressString networkNodeNumber) {
    this.networkNodeNumber = networkNodeNumber;
  }

  public boolean isGprsNodeIndicator() {
    return gprsNodeIndicator;
  }

  public void setGprsNodeIndicator(boolean gprsNodeIndicator) {
    this.gprsNodeIndicator = gprsNodeIndicator;
  }

  public AdditionalNumber getAdditionalNumber() {
    return additionalNumber;
  }

  public void setAdditionalNumber(AdditionalNumber additionalNumber) {
    this.additionalNumber = additionalNumber;
  }

  public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
    return supportedLCSCapabilitySets;
  }

  public void setSupportedLCSCapabilitySets(SupportedLCSCapabilitySets supportedLCSCapabilitySets) {
    this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
  }

  public SupportedLCSCapabilitySets getAddSupportedLCSCapabilitySets() {
    return addSupportedLCSCapabilitySets;
  }

  public void setAddSupportedLCSCapabilitySets(SupportedLCSCapabilitySets addSupportedLCSCapabilitySets) {
    this.addSupportedLCSCapabilitySets = addSupportedLCSCapabilitySets;
  }

  public DiameterIdentity getMmeName() {
    return mmeName;
  }

  public void setMmeName(DiameterIdentity mmeName) {
    this.mmeName = mmeName;
  }

  public DiameterIdentity getSgsnName() {
    return sgsnName;
  }

  public void setSgsnName(DiameterIdentity sgsnName) {
    this.sgsnName = sgsnName;
  }

  public DiameterIdentity getSgsnRealm() {
    return sgsnRealm;
  }

  public void setSgsnRealm(DiameterIdentity sgsnRealm) {
    this.sgsnRealm = sgsnRealm;
  }

  public DiameterIdentity getAaaServerName() {
    return aaaServerName;
  }

  public void setAaaServerName(DiameterIdentity aaaServerName) {
    this.aaaServerName = aaaServerName;
  }

  public GSNAddress gethGmlcAddress() {
    return hGmlcAddress;
  }

  public void sethGmlcAddress(GSNAddress hGmlcAddress) {
    this.hGmlcAddress = hGmlcAddress;
  }

  public GSNAddress getvGmlcAddress() {
    return vGmlcAddress;
  }

  public void setvGmlcAddress(GSNAddress vGmlcAddress) {
    this.vGmlcAddress = vGmlcAddress;
  }

  public GSNAddress getPprAddress() {
    return pprAddress;
  }

  public void setPprAddress(GSNAddress pprAddress) {
    this.pprAddress = pprAddress;
  }

  public LocationType getLocationType() {
    return locationType;
  }

  public void setLocationType(LocationType locationType) {
    this.locationType = locationType;
  }

  public LocationEstimateType getLocationEstimateType() {
    return locationEstimateType;
  }

  public void setLocationEstimateType(LocationEstimateType locationEstimateType) {
    this.locationEstimateType = locationEstimateType;
  }

  public DeferredLocationEventType getDeferredLocationEventType() {
    return deferredLocationEventType;
  }

  public void setDeferredLocationEventType(DeferredLocationEventType deferredLocationEventType) {
    this.deferredLocationEventType = deferredLocationEventType;
  }

  public LCSClientID getLcsClientID() {
    return lcsClientID;
  }

  public void setLcsClientID(LCSClientID lcsClientID) {
    this.lcsClientID = lcsClientID;
  }

  public LCSPriority getLcsPriority() {
    return lcsPriority;
  }

  public void setLcsPriority(LCSPriority lcsPriority) {
    this.lcsPriority = lcsPriority;
  }

  public LCSQoS getLcsQoS() {
    return lcsQoS;
  }

  public void setLcsQoS(LCSQoS lcsQoS) {
    this.lcsQoS = lcsQoS;
  }

  public Integer getHorizontalAccuracy() {
    return horizontalAccuracy;
  }

  public void setHorizontalAccuracy(Integer horizontalAccuracy) {
    this.horizontalAccuracy = horizontalAccuracy;
  }

  public Integer getVerticalAccuracy() {
    return verticalAccuracy;
  }

  public void setVerticalAccuracy(Integer verticalAccuracy) {
    this.verticalAccuracy = verticalAccuracy;
  }

  public boolean isVerticalCoordinateRequest() {
    return verticalCoordinateRequest;
  }

  public void setVerticalCoordinateRequest(boolean verticalCoordinateRequest) {
    this.verticalCoordinateRequest = verticalCoordinateRequest;
  }

  public ResponseTimeCategory getResponseTimeCategory() {
    return responseTimeCategory;
  }

  public void setResponseTimeCategory(ResponseTimeCategory responseTimeCategory) {
    this.responseTimeCategory = responseTimeCategory;
  }

  public ResponseTime getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(ResponseTime responseTime) {
    this.responseTime = responseTime;
  }

  public MAPExtensionContainer getMapExtensionContainer() {
    return mapExtensionContainer;
  }

  public void setMapExtensionContainer(MAPExtensionContainer mapExtensionContainer) {
    this.mapExtensionContainer = mapExtensionContainer;
  }

  public IMEI getImei() {
    return imei;
  }

  public void setImei(IMEI imei) {
    this.imei = imei;
  }

  public SupportedGADShapes getSupportedGADShapes() {
    return supportedGADShapes;
  }

  public void setSupportedGADShapes(SupportedGADShapes supportedGADShapes) {
    this.supportedGADShapes = supportedGADShapes;
  }

  public boolean isEllipsoidPoint() {
    return ellipsoidPoint;
  }

  public void setEllipsoidPoint(boolean ellipsoidPoint) {
    this.ellipsoidPoint = ellipsoidPoint;
  }

  public boolean isEllipsoidPointWithUncertaintyCircle() {
    return ellipsoidPointWithUncertaintyCircle;
  }

  public void setEllipsoidPointWithUncertaintyCircle(boolean ellipsoidPointWithUncertaintyCircle) {
    this.ellipsoidPointWithUncertaintyCircle = ellipsoidPointWithUncertaintyCircle;
  }

  public boolean isEllipsoidPointWithUncertaintyEllipse() {
    return ellipsoidPointWithUncertaintyEllipse;
  }

  public void setEllipsoidPointWithUncertaintyEllipse(boolean ellipsoidPointWithUncertaintyEllipse) {
    this.ellipsoidPointWithUncertaintyEllipse = ellipsoidPointWithUncertaintyEllipse;
  }

  public boolean isPolygon() {
    return polygon;
  }

  public void setPolygon(boolean polygon) {
    this.polygon = polygon;
  }

  public boolean isEllipsoidPointWithAltitude() {
    return ellipsoidPointWithAltitude;
  }

  public void setEllipsoidPointWithAltitude(boolean ellipsoidPointWithAltitude) {
    this.ellipsoidPointWithAltitude = ellipsoidPointWithAltitude;
  }

  public boolean isEllipsoidPointWithAltitudeAndUncertaintyElipsoid() {
    return ellipsoidPointWithAltitudeAndUncertaintyElipsoid;
  }

  public void setEllipsoidPointWithAltitudeAndUncertaintyElipsoid(boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid) {
    this.ellipsoidPointWithAltitudeAndUncertaintyElipsoid = ellipsoidPointWithAltitudeAndUncertaintyElipsoid;
  }

  public boolean isEllipsoidArc() {
    return ellipsoidArc;
  }

  public void setEllipsoidArc(boolean ellipsoidArc) {
    this.ellipsoidArc = ellipsoidArc;
  }

  public int getLcsReferenceNumber() {
    return lcsReferenceNumber;
  }

  public void setLcsReferenceNumber(int lcsReferenceNumber) {
    this.lcsReferenceNumber = lcsReferenceNumber;
  }

  public LCSCodeword getLcsCodeword() {
    return lcsCodeword;
  }

  public void setLcsCodeword(LCSCodeword lcsCodeword) {
    this.lcsCodeword = lcsCodeword;
  }

  public int getLcsServiceTypeID() {
    return lcsServiceTypeID;
  }

  public void setLcsServiceTypeID(int lcsServiceTypeID) {
    this.lcsServiceTypeID = lcsServiceTypeID;
  }

  public LCSPrivacyCheck getLcsPrivacyCheck() {
    return lcsPrivacyCheck;
  }

  public void setLcsPrivacyCheck(LCSPrivacyCheck lcsPrivacyCheck) {
    this.lcsPrivacyCheck = lcsPrivacyCheck;
  }

  public PrivacyCheckRelatedAction getCallSessionUnrelated() {
    return callSessionUnrelated;
  }

  public void setCallSessionUnrelated(PrivacyCheckRelatedAction callSessionUnrelated) {
    this.callSessionUnrelated = callSessionUnrelated;
  }

  public PrivacyCheckRelatedAction getCallSessionRelated() {
    return callSessionRelated;
  }

  public void setCallSessionRelated(PrivacyCheckRelatedAction callSessionRelated) {
    this.callSessionRelated = callSessionRelated;
  }

  public AreaEventInfo getAreaEventInfo() {
    return areaEventInfo;
  }

  public void setAreaEventInfo(AreaEventInfo areaEventInfo) {
    this.areaEventInfo = areaEventInfo;
  }

  public AreaDefinition getAreaDefinition() {
    return areaDefinition;
  }

  public void setAreaDefinition(AreaDefinition areaDefinition) {
    this.areaDefinition = areaDefinition;
  }

  public AreaType getAreaType() {
    return areaType;
  }

  public void setAreaType(AreaType areaType) {
    this.areaType = areaType;
  }

  public AreaIdentification getAreaIdentification() {
    return areaIdentification;
  }

  public void setAreaIdentification(AreaIdentification areaIdentification) {
    this.areaIdentification = areaIdentification;
  }

  public OccurrenceInfo getOccurrenceInfo() {
    return occurrenceInfo;
  }

  public void setOccurrenceInfo(OccurrenceInfo occurrenceInfo) {
    this.occurrenceInfo = occurrenceInfo;
  }

  public int getIntervalTime() {
    return intervalTime;
  }

  public void setIntervalTime(int intervalTime) {
    this.intervalTime = intervalTime;
  }

  public ExtGeographicalInformation getLocationEstimate() {
    return locationEstimate;
  }

  public void setLocationEstimate(ExtGeographicalInformation locationEstimate) {
    this.locationEstimate = locationEstimate;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getUncertainty() {
    return uncertainty;
  }

  public void setUncertainty(double uncertainty) {
    this.uncertainty = uncertainty;
  }

  public double getUncertaintySemiMajorAxis() {
    return uncertaintySemiMajorAxis;
  }

  public void setUncertaintySemiMajorAxis(double uncertaintySemiMajorAxis) {
    this.uncertaintySemiMajorAxis = uncertaintySemiMajorAxis;
  }

  public double getUncertaintySemiMinorAxis() {
    return uncertaintySemiMinorAxis;
  }

  public void setUncertaintySemiMinorAxis(double uncertaintySemiMinorAxis) {
    this.uncertaintySemiMinorAxis = uncertaintySemiMinorAxis;
  }

  public double getAngleOfMajorAxis() {
    return angleOfMajorAxis;
  }

  public void setAngleOfMajorAxis(double angleOfMajorAxis) {
    this.angleOfMajorAxis = angleOfMajorAxis;
  }

  public double getUncertaintyAltitude() {
    return uncertaintyAltitude;
  }

  public void setUncertaintyAltitude(double uncertaintyAltitude) {
    this.uncertaintyAltitude = uncertaintyAltitude;
  }

  public double getUncertaintyRadius() {
    return uncertaintyRadius;
  }

  public void setUncertaintyRadius(double uncertaintyRadius) {
    this.uncertaintyRadius = uncertaintyRadius;
  }

  public double getOffsetAngle() {
    return offsetAngle;
  }

  public void setOffsetAngle(double offsetAngle) {
    this.offsetAngle = offsetAngle;
  }

  public double getIncludedAngle() {
    return includedAngle;
  }

  public void setIncludedAngle(double includedAngle) {
    this.includedAngle = includedAngle;
  }

  public int getAgeOfLocationEstimate() {
    return ageOfLocationEstimate;
  }

  public void setAgeOfLocationEstimate(int ageOfLocationEstimate) {
    this.ageOfLocationEstimate = ageOfLocationEstimate;
  }

  public int getConfidence() {
    return confidence;
  }

  public void setConfidence(int confidence) {
    this.confidence = confidence;
  }

  public int getAltitude() {
    return altitude;
  }

  public void setAltitude(int altitude) {
    this.altitude = altitude;
  }

  public int getInnerRadius() {
    return innerRadius;
  }

  public void setInnerRadius(int innerRadius) {
    this.innerRadius = innerRadius;
  }

  public TypeOfShape getTypeOfShape() {
    return typeOfShape;
  }

  public void setTypeOfShape(TypeOfShape typeOfShape) {
    this.typeOfShape = typeOfShape;
  }

  public int getPseudonymIndicator() {
    return pseudonymIndicator;
  }

  public void setPseudonymIndicator(int pseudonymIndicator) {
    this.pseudonymIndicator = pseudonymIndicator;
  }

  public boolean isDeferredMTLRResponseIndicator() {
    return deferredMTLRResponseIndicator;
  }

  public void setDeferredMTLRResponseIndicator(boolean deferredMTLRResponseIndicator) {
    this.deferredMTLRResponseIndicator = deferredMTLRResponseIndicator;
  }

  public boolean isSaiPresent() {
    return saiPresent;
  }

  public void setSaiPresent(boolean saiPresent) {
    this.saiPresent = saiPresent;
  }

  public boolean isMoLrShortCircuitIndicator() {
    return moLrShortCircuitIndicator;
  }

  public void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator) {
    this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
  }

  public PositioningDataInformation getGeranPositioningDataInformation() {
    return geranPositioningDataInformation;
  }

  public void setGeranPositioningDataInformation(PositioningDataInformation positioningDataInformation) {
    this.geranPositioningDataInformation = positioningDataInformation;
  }

  public UtranPositioningDataInfo getUtranPositioningDataInfo() {
    return utranPositioningDataInfo;
  }

  public void setUtranPositioningDataInfo(UtranPositioningDataInfo utranPositioningDataInfo) {
    this.utranPositioningDataInfo = utranPositioningDataInfo;
  }

  public AddGeographicalInformation getAdditionalLocationEstimate() {
    return additionalLocationEstimate;
  }

  public void setAdditionalLocationEstimate(AddGeographicalInformation additionalLocationEstimate) {
    this.additionalLocationEstimate = additionalLocationEstimate;
  }

  public CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalIdOrServiceAreaIdFixedLength() {
    return cellGlobalIdOrServiceAreaIdFixedLength;
  }

  public void setCellGlobalIdOrServiceAreaIdFixedLength(CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength) {
    this.cellGlobalIdOrServiceAreaIdFixedLength = cellGlobalIdOrServiceAreaIdFixedLength;
  }

  public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
    return cellGlobalIdOrServiceAreaIdOrLAI;
  }

  public void setCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI) {
    this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
  }

  public AccuracyFulfilmentIndicator getAccuracyFulfilmentIndicator() {
    return accuracyFulfilmentIndicator;
  }

  public void setAccuracyFulfilmentIndicator(AccuracyFulfilmentIndicator accuracyFulfilmentIndicator) {
    this.accuracyFulfilmentIndicator = accuracyFulfilmentIndicator;
  }

  public VelocityEstimate getVelocityEstimate() {
    return velocityEstimate;
  }

  public void setVelocityEstimate(VelocityEstimate velocityEstimate) {
    this.velocityEstimate = velocityEstimate;
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

  public ServingNodeAddress getServingNodeAddress() {
    return servingNodeAddress;
  }

  public void setServingNodeAddress(ServingNodeAddress servingNodeAddress) {
    this.servingNodeAddress = servingNodeAddress;
  }

  public LCSEvent getLcsEvent() {
    return lcsEvent;
  }

  public void setLcsEvent(LCSEvent lcsEvent) {
    this.lcsEvent = lcsEvent;
  }

  public LCSClientExternalID getLcsClientExternalID() {
    return lcsClientExternalID;
  }

  public void setLcsClientExternalID(LCSClientExternalID lcsClientExternalID) {
    this.lcsClientExternalID = lcsClientExternalID;
  }

  public LCSClientInternalID getLcsClientInternalID() {
    return lcsClientInternalID;
  }

  public void setLcsClientInternalID(LCSClientInternalID lcsClientInternalID) {
    this.lcsClientInternalID = lcsClientInternalID;
  }

  public LCSFormatIndicator getLcsFormatIndicator() {
    return lcsFormatIndicator;
  }

  public void setLcsFormatIndicator(LCSFormatIndicator lcsFormatIndicator) {
    this.lcsFormatIndicator = lcsFormatIndicator;
  }

  public LCSClientName getLcsClientName() {
    return lcsClientName;
  }

  public void setLcsClientName(LCSClientName lcsClientName) {
    this.lcsClientName = lcsClientName;
  }

  public TerminationCause getTerminationCause() {
    return terminationCause;
  }

  public void setTerminationCause(TerminationCause terminationCause) {
    this.terminationCause = terminationCause;
  }

  public DeferredmtlrData getDeferredmtlrData() {
    return deferredmtlrData;
  }

  public void setDeferredmtlrData(DeferredmtlrData deferredmtlrData) {
    this.deferredmtlrData = deferredmtlrData;
  }

  public PeriodicLDRInfo getPeriodicLDRInfo() {
    return periodicLDRInfo;
  }

  public void setPeriodicLDRInfo(PeriodicLDRInfo periodicLDRInfo) {
    this.periodicLDRInfo = periodicLDRInfo;
  }

  public int getReportingAmount() {
    return reportingAmount;
  }

  public void setReportingAmount(int reportingAmount) {
    this.reportingAmount = reportingAmount;
  }

  public int getReportingInterval() {
    return reportingInterval;
  }

  public void setReportingInterval(int reportingInterval) {
    this.reportingInterval = reportingInterval;
  }

  public ReportingPLMNList getReportingPLMNList() {
    return reportingPLMNList;
  }

  public void setReportingPLMNList(ReportingPLMNList reportingPLMNList) {
    this.reportingPLMNList = reportingPLMNList;
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public String toString() {

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Lsm [");

    stringBuilder.append(", mlcNumber=");
    stringBuilder.append(mlcNumber);

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

    stringBuilder.append(", Supported LCS Capability Sets=");
    stringBuilder.append(supportedLCSCapabilitySets);

    stringBuilder.append(", MME Name=");
    stringBuilder.append(mmeName);

    stringBuilder.append(", SGSN Name=");
    stringBuilder.append(sgsnName);

    stringBuilder.append(", SGSN Realm=");
    stringBuilder.append(sgsnRealm);

    stringBuilder.append(", 3GPP AAA Server Name=");
    stringBuilder.append(aaaServerName);

    stringBuilder.append(", H-GMLC Address=");
    stringBuilder.append(hGmlcAddress);

    stringBuilder.append(", V-GMLC Address=");
    stringBuilder.append(vGmlcAddress);

    stringBuilder.append(", PPR Address=");
    stringBuilder.append(pprAddress);

    stringBuilder.append(", Location Type=");
    stringBuilder.append(locationType);

    stringBuilder.append(", Location Estimate Type=");
    stringBuilder.append(locationEstimateType);

    stringBuilder.append(", Deferred Location Event Type=");
    stringBuilder.append(deferredLocationEventType);

    stringBuilder.append(", LCS Client ID=");
    stringBuilder.append(lcsClientID);

    stringBuilder.append(", LCS Priority=");
    stringBuilder.append(lcsPriority);

    stringBuilder.append(", LCS QoS=");
    stringBuilder.append(lcsQoS);

    stringBuilder.append(", Horizontal Accuracy=");
    stringBuilder.append(horizontalAccuracy);

    stringBuilder.append(", Vertical Accuracy=");
    stringBuilder.append(verticalAccuracy);

    stringBuilder.append(", Vertical Coordinates Request=");
    stringBuilder.append(verticalCoordinateRequest);

    stringBuilder.append(", Response Time Category=");
    stringBuilder.append(responseTimeCategory);

    stringBuilder.append(", Response Time =");
    stringBuilder.append(responseTime);

    stringBuilder.append(", MAP Extension Container =");
    stringBuilder.append(mapExtensionContainer);

    stringBuilder.append(", Supported GAD Shapes=");
    stringBuilder.append(supportedGADShapes);

    stringBuilder.append(", LCS Reference Number=");
    stringBuilder.append(lcsReferenceNumber);

    stringBuilder.append(", LCS Codeword=");
    stringBuilder.append(lcsCodeword);

    stringBuilder.append(", LCS Service Type ID=");
    stringBuilder.append(lcsServiceTypeID);

    stringBuilder.append(", LCS Privacy Check=");
    stringBuilder.append(lcsPrivacyCheck);

    stringBuilder.append(", Area Event Info=");
    stringBuilder.append(areaEventInfo);

    stringBuilder.append(", Area Definition=");
    stringBuilder.append(areaDefinition);

    stringBuilder.append(", Area Type=");
    stringBuilder.append(areaType);

    stringBuilder.append(", Area Identification=");
    stringBuilder.append(areaIdentification);

    stringBuilder.append(", Occurrence Info=");
    stringBuilder.append(occurrenceInfo);

    stringBuilder.append(", Interval Time=");
    stringBuilder.append(intervalTime);

    stringBuilder.append(", Location Estimate=");
    stringBuilder.append(locationEstimate);

    stringBuilder.append(", Latitude=");
    stringBuilder.append(latitude);

    stringBuilder.append(", Longitude=");
    stringBuilder.append(longitude);

    stringBuilder.append(", uncertainty=");
    stringBuilder.append(uncertainty);

    stringBuilder.append(", uncertainty Semi Major Axis=");
    stringBuilder.append(uncertaintySemiMajorAxis);

    stringBuilder.append(", uncertainty Semi Minor Axis=");
    stringBuilder.append(uncertaintySemiMinorAxis);

    stringBuilder.append(", angle of Major Axis=");
    stringBuilder.append(angleOfMajorAxis);

    stringBuilder.append(", uncertainty Altitude=");
    stringBuilder.append(uncertaintyAltitude);

    stringBuilder.append(", uncertainty Radius=");
    stringBuilder.append(uncertaintyRadius);

    stringBuilder.append(", offset Angle=");
    stringBuilder.append(offsetAngle);

    stringBuilder.append(", include Angle=");
    stringBuilder.append(includedAngle);

    stringBuilder.append(", age of Location Estimate=");
    stringBuilder.append(ageOfLocationEstimate);

    stringBuilder.append(", confidence=");
    stringBuilder.append(confidence);

    stringBuilder.append(", altitude=");
    stringBuilder.append(altitude);

    stringBuilder.append(", inner Radius=");
    stringBuilder.append(innerRadius);

    stringBuilder.append(", type of shape=");
    stringBuilder.append(typeOfShape);

    stringBuilder.append(", pseudonym indicator=");
    stringBuilder.append(pseudonymIndicator);

    stringBuilder.append(", deferred MT LR response indicator=");
    stringBuilder.append(deferredMTLRResponseIndicator);

    stringBuilder.append(", SAI present=");
    stringBuilder.append(saiPresent);

    stringBuilder.append(", MO LR Short Circuit Indicator=");
    stringBuilder.append(moLrShortCircuitIndicator);

    stringBuilder.append(", Positioning Data Information=");
    stringBuilder.append(geranPositioningDataInformation);

    stringBuilder.append(", UTRAN Positioning Data Information=");
    stringBuilder.append(utranPositioningDataInfo);

    stringBuilder.append(", CGI or SAI fixed length=");
    stringBuilder.append(cellGlobalIdOrServiceAreaIdFixedLength);

    stringBuilder.append(", CGI or SAI or LAI=");
    stringBuilder.append(cellGlobalIdOrServiceAreaIdOrLAI);

    stringBuilder.append(", Accuracy Fulfilment Indicator=");
    stringBuilder.append(accuracyFulfilmentIndicator);

    stringBuilder.append(", Velocity Estimate=");
    stringBuilder.append(velocityEstimate);

    stringBuilder.append(", GERAN GANSS positioning data=");
    stringBuilder.append(geranGANSSpositioningData);

    stringBuilder.append(", UTRAN GANSS positioning data=");
    stringBuilder.append(utranGANSSpositioningData);

    stringBuilder.append(", Serving Node Address=");
    stringBuilder.append(servingNodeAddress);

    stringBuilder.append(", LCS Event=");
    stringBuilder.append(lcsEvent);

    stringBuilder.append(", LCS Client External ID=");
    stringBuilder.append(lcsClientExternalID);

    stringBuilder.append(", LCS Client Internal ID=");
    stringBuilder.append(lcsClientInternalID);

    stringBuilder.append(", LCS client name=");
    stringBuilder.append(lcsClientName);

    stringBuilder.append(", LCS format indicator=");
    stringBuilder.append(lcsFormatIndicator);

    stringBuilder.append(", Termination Cause=");
    stringBuilder.append(terminationCause);

    stringBuilder.append(", Deferred MT LR Data=");
    stringBuilder.append(deferredmtlrData);

    stringBuilder.append(", periodic LDR Info=");
    stringBuilder.append(periodicLDRInfo);

    stringBuilder.append(", reporting amount=");
    stringBuilder.append(reportingAmount);

    stringBuilder.append(", reporting interval=");
    stringBuilder.append(reportingInterval);

    stringBuilder.append(", reportingPLMNList=");
    stringBuilder.append(reportingPLMNList);

    stringBuilder.append(", sequence number=");
    stringBuilder.append(sequenceNumber);

    stringBuilder.append("]");
    return stringBuilder.toString();
  }

}
