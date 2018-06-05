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

package org.mobicents.gmlc.slee.map;

//import io.netty.handler.codec.http.HttpMethod;
import net.java.slee.resource.http.events.HttpServletRequestEvent;
import org.joda.time.DateTime;

import org.mobicents.gmlc.GmlcPropertiesManagement;

import org.mobicents.gmlc.slee.GMLCBaseSbb;
import org.mobicents.gmlc.slee.cdr.CDRInterface;
import org.mobicents.gmlc.slee.cdr.CDRInterfaceParent;
import org.mobicents.gmlc.slee.cdr.GMLCCDRState;
import org.mobicents.gmlc.slee.cdr.RecordStatus;
import org.mobicents.gmlc.slee.http.HttpReport;
import org.mobicents.gmlc.slee.mlp.MLPException;
import org.mobicents.gmlc.slee.mlp.MLPRequest;
import org.mobicents.gmlc.slee.mlp.MLPResponse;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;

import org.mobicents.protocols.ss7.map.api.primitives.*;

import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;

import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.*;

import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;

import org.mobicents.protocols.ss7.map.service.lsm.AreaDefinitionImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AreaEventInfoImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AreaIdentificationImpl;
import org.mobicents.protocols.ss7.map.service.lsm.AreaImpl;
import org.mobicents.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LCSQoSImpl;
import org.mobicents.protocols.ss7.map.service.lsm.LocationTypeImpl;
import org.mobicents.protocols.ss7.map.service.lsm.PeriodicLDRInfoImpl;
import org.mobicents.protocols.ss7.map.service.lsm.ResponseTimeImpl;
import org.mobicents.protocols.ss7.map.service.lsm.SupportedGADShapesImpl;

import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RequestedInfoImpl;

import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.parameter.EncodingScheme;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.resource.map.MAPContextInterfaceFactory;
import org.mobicents.slee.resource.map.events.DialogAccept;
import org.mobicents.slee.resource.map.events.DialogClose;
import org.mobicents.slee.resource.map.events.DialogDelimiter;
import org.mobicents.slee.resource.map.events.DialogNotice;
import org.mobicents.slee.resource.map.events.DialogProviderAbort;
import org.mobicents.slee.resource.map.events.DialogReject;
import org.mobicents.slee.resource.map.events.DialogRelease;
import org.mobicents.slee.resource.map.events.DialogTimeout;
import org.mobicents.slee.resource.map.events.DialogUserAbort;
import org.mobicents.slee.resource.map.events.ErrorComponent;
import org.mobicents.slee.resource.map.events.InvokeTimeout;
import org.mobicents.slee.resource.map.events.RejectComponent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.EventContext;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ResourceAdaptorTypeID;

import java.io.IOException;
import java.io.Serializable;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @contributor <a href="mailto:aferreiraguido@gmail.com"> Alejandro Ferreira </a>
 * @contributor <a href="mailto:abhayani@gmail.com"> Amit Bhayani </a>
 * @contributor <a href="mailto:serg.vetyutnev@gmail.com"> Sergey Vetyutnev </a>
 * @contributor <a href="mailto:eross@locatrix.com"> Andrew Eross </a>
 * @contributor <a href="mailto:lucas@locatrix.com"> Lucas Brown </a>
 * @contributor <a href="mailto:nhanth87@gmail.com"> Tran Huu Nhan </a>
 */
public abstract class MobileCoreNetworkInterfaceSbb extends GMLCBaseSbb implements Sbb, CDRInterfaceParent {

  protected SbbContextExt sbbContext;

  protected Tracer logger;

  protected MAPContextInterfaceFactory mapAcif;
  protected MAPProvider mapProvider;
  protected MAPParameterFactory mapParameterFactory;
  protected ParameterFactory sccpParameterFact;
  protected SccpStackImpl sccpStack;

  protected static final ResourceAdaptorTypeID mapRATypeID = new ResourceAdaptorTypeID("MAPResourceAdaptorType",
          "org.mobicents", "2.0");
  protected static final String mapRaLink = "MAPRA";

  private static final GmlcPropertiesManagement gmlcPropertiesManagement = GmlcPropertiesManagement.getInstance();

  private SccpAddress gmlcSCCPAddress = null;
  private MAPApplicationContext anyTimeEnquiryContext = null;
  private MAPApplicationContext locationSvcEnquiryContext = null;
  private MAPApplicationContext locationSvcGatewayContext = null;
  private MAPApplicationContext shortMsgGatewayContext = null;
  private MAPApplicationContext subscriberInfoEnquiryContext = null;

  private TimerFacility timerFacility = null;

  private String pslLcsReferenceNumber, pslLcsServiceTypeID, pslIntervalTime, pslReportingAmount, pslReportingInterval,
          pslLcsHorizontalAccuracy, pslLcsVerticalAccuracy, pslOccurrenceInfo, pslAreaType, pslAreaId, pslLocationEstimateType, pslDeferredLocationEventType,
          pslLcsPriority, pslVerticalCoordinateRequest, pslResponseTimeCategory, slrCallbackUrl, psiService;

  private HttpReport httpSubscriberLocationReport = new HttpReport();

  /**
   * Creates a new instance of CallSbb
   */
  public MobileCoreNetworkInterfaceSbb() {
    super("MobileCoreNetworkInterfaceSbb");
  }

  /**
   * HTTP Request Types (GET or MLP)
   */
  private enum HttpRequestType {
    REST("rest"),
    MLP("mlp"),
    UNSUPPORTED("404");

    private String path;

    HttpRequestType(String path) {
      this.path = path;
    }

    public String getPath() {
      return String.format("/gmlc/%s", path);
    }

    public static HttpRequestType fromPath(String path) {
      for (HttpRequestType type : values()) {
        if (path.equals(type.getPath())) {
          return type;
        }
      }

      return UNSUPPORTED;
    }
  }

  /**
   * HTTP Request
   */
  private class HttpRequest implements Serializable {

    HttpRequestType type;
    String msisdn;
    String coreNetwork;
    int lcsReferenceNumber = (int) (new Date().getTime()/1000);
    int lcsServiceTypeID;
    LCSPriority lcsPriority;
    Integer horizontalAccuracy;
    Integer verticalAccuracy;
    boolean verticalCoordinateRequest;
    ResponseTimeCategory responseTimeCategory;
    ResponseTime responseTime; // ResponseTimeImpl(responseTimeCategory);
    LCSQoS lcsQoS;// LCSQoSImpl(horizontalAccuracy, verticalAccuracy, verticalCoordinateRequest, responseTime, mapExtensionContainer);
    AreaEventInfo areaEventInfo; // AreaEventInfoImpl(areaDefinition, occurrenceInfo, intervalTime);
    PeriodicLDRInfo periodicLDRInfo; // PeriodicLDRInfoImpl(reportingAmount, reportingInterval);
    String slrCallbackUrl;
    String psiService;


    public HttpRequest(HttpRequestType type, String subscriberIdentity, String coreNetwork,
                       String priority, int horAccuracy, int vertAccuracy, String vertCoordRequest, String responseTimeCat,
                       int lcsReferenceNumber, int lcsServiceTypeID, String areaType, String areaId,
                       String occurrenceInfo, int intervalTime, int reportingAmount, int reportingInterval, String slrCallbackUrl,
                       String psiService) {
      this.type = type;
      this.lcsPriority = LCSPriority.normalPriority;
      if (priority.equalsIgnoreCase("highest")) {
        this.lcsPriority = LCSPriority.highestPriority;
      }
      this.msisdn = subscriberIdentity;
      this.coreNetwork = "GSM";
      if (coreNetwork.equalsIgnoreCase("umts")) {
        this.coreNetwork = "UMTS";
      }
      this.lcsReferenceNumber = lcsReferenceNumber;
      this.lcsServiceTypeID = lcsServiceTypeID;
      this.horizontalAccuracy = horAccuracy;
      this.verticalAccuracy = vertAccuracy;
      this.verticalCoordinateRequest = false;
      if (!vertCoordRequest.equalsIgnoreCase("false")) {
        this.verticalCoordinateRequest = true;
      }
      this.responseTimeCategory = ResponseTimeCategory.delaytolerant;
      if (!responseTimeCat.equalsIgnoreCase("tolerant")) {
        this.responseTimeCategory = ResponseTimeCategory.lowdelay;
      }
      //temporary hardcoded
      this.responseTime = new ResponseTimeImpl(responseTimeCategory);
      MAPExtensionContainer mec = null;
      this.lcsQoS = new LCSQoSImpl(horizontalAccuracy, verticalAccuracy, verticalCoordinateRequest, responseTime, mec);
      try {
        ArrayList<Area> areaArrayList = new ArrayList<Area>();
        AreaType areaT = AreaType.locationAreaId;
        if (areaType.equalsIgnoreCase("routing")) {
          areaT = AreaType.routingAreaId;
        } else if(areaType.equalsIgnoreCase("cgi")) {
          areaT = AreaType.cellGlobalId;
        } else if(areaType.equalsIgnoreCase("cc")) {
          areaT = AreaType.countryCode;
        } else if(areaType.equalsIgnoreCase("utranCid")) {
          areaT = AreaType.utranCellId;
        } else if(areaType.equalsIgnoreCase("plmn")) {
          areaT = AreaType.plmnId;
        }
        byte[] aId = areaId.getBytes();
        AreaIdentification areaIdentification = new AreaIdentificationImpl(aId);
        Area area = new AreaImpl(areaT, areaIdentification);
        areaArrayList.add(area);
        AreaDefinition areaDefinition = new AreaDefinitionImpl(areaArrayList);
        OccurrenceInfo occurrenceInformation = OccurrenceInfo.oneTimeEvent;
        if (occurrenceInfo.equalsIgnoreCase("multiple")) {
          occurrenceInformation = OccurrenceInfo.multipleTimeEvent;
        }
        this.areaEventInfo = new AreaEventInfoImpl(areaDefinition, occurrenceInformation, intervalTime);
      } catch (Exception e) {
        logger.info(String.format("Error while creating AreaEventInfo from HttpRequest:" + e));
      }
      this.periodicLDRInfo = new PeriodicLDRInfoImpl(reportingAmount, reportingInterval);
      this.slrCallbackUrl = slrCallbackUrl;
      this.psiService = psiService;

    }

    public HttpRequest(HttpRequestType type) {

      this(type, "", "", "", -1, -1, "",
              "", -1, -1, "", "", "",
              -1, -1, -1, "", "");
    }
  }

  /**
   * MAP ATI Response Cell Global Identification and State parameters
   */
  private class MLPResponseParams implements Serializable {

    /*** MLP Response ***/
    /*******************/
    String x = "-1";
    String y = "-1";
    String radius = "-1";

  }

  /**
   * For debugging - fake location data
   */
  private String fakeNumber = "19395550113";
  private MLPResponse.MLPResultType fakeLocationType = MLPResponse.MLPResultType.OK;
  private String fakeLocationAdditionalInfoErrorString = "Internal positioning failure occurred";
  private int fakeCellId = 300;
  private String fakeLocationX = "27 28 25.00S";
  private String fakeLocationY = "153 01 43.00E";
  private String fakeLocationRadius = "5000";

  ////////////////////
  // Sbb callbacks //
  //////////////////
  public void setSbbContext(SbbContext sbbContext) {
    this.sbbContext = (SbbContextExt) sbbContext;
    this.logger = sbbContext.getTracer(MobileCoreNetworkInterfaceSbb.class.getSimpleName());
    try {
      this.mapAcif = (MAPContextInterfaceFactory) this.sbbContext.getActivityContextInterfaceFactory(mapRATypeID);
      this.mapProvider = (MAPProvider) this.sbbContext.getResourceAdaptorInterface(mapRATypeID, mapRaLink);
      this.mapParameterFactory = this.mapProvider.getMAPParameterFactory();
      this.sccpParameterFact = new ParameterFactoryImpl();
      this.timerFacility = this.sbbContext.getTimerFacility();
    } catch (Exception ne) {
      logger.severe("Could not set SBB context:", ne);
    }
  }

  public void sbbCreate() throws CreateException {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("Created KnowledgeBase");
    }
  }

  public void sbbPostCreate() throws CreateException {
  }

  public void sbbActivate() {
  }

  public void sbbPassivate() {
  }

  public void sbbLoad() {
  }

  public void sbbStore() {
  }

  public void sbbRemove() {
  }

  public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
  }

  public void sbbRolledBack(RolledBackContext rolledBackContext) {
  }

  private void forwardEvent(SbbLocalObject child, ActivityContextInterface aci) {
    try {
      aci.attach(child);
      aci.detach(sbbContext.getSbbLocalObject());
    } catch (Exception e) {
      logger.severe("Unexpected error: ", e);
    }
  }

  // //////////////////////
  // MAP Stuff handlers //
  // ////////////////////

  /**
   * Subscriber Information services
   * MAP_ANY_TIME_INTERROGATION (ATI) Events
   */

  /*
   * MAP ATI Request Event
   */
  public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest event, ActivityContextInterface aci) {
    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onAnyTimeInterrogationRequest = " + event);
      }

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onAnyTimeInterrogationRequest=%s", event), e);
    }

  }

  /*
   * MAP ATI Response Event
   */
  public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse event, ActivityContextInterface aci) {

    MAPErrorMessage mapErrorMessage = this.getErrorResponse();

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onAnyTimeInterrogationResponse = " + event);
      }

      MAPDialogMobility mapDialogMobility = event.getMAPDialog();
      SubscriberInfo subscriberInfo = event.getSubscriberInfo();
      AtiResponseValues atiResponseValues = new AtiResponseValues();
      MLPResponse.MLPResultType mlpRespResult;
      String mlpClientErrorMessage = null;

      // CDR initialization stuff
      CDRInterface cdrInterface = this.getCDRInterface();
      GMLCCDRState gmlcCdrState = cdrInterface.getState();
      if (!gmlcCdrState.isInitialized()) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonAnyTimeInterrogationResponse: CDR state is NOT initialized: " + gmlcCdrState + ", initiating\n");
        }
        gmlcCdrState.init(mapDialogMobility.getLocalDialogId(), mapDialogMobility.getReceivedDestReference(), mapDialogMobility.getReceivedOrigReference(),
                subscriberInfo.getLocationInformation().getVlrNumber(), mapDialogMobility.getLocalAddress(), mapDialogMobility.getRemoteAddress());
        gmlcCdrState.setDialogStartTime(DateTime.now());
        gmlcCdrState.setRemoteDialogId(mapDialogMobility.getRemoteDialogId());
        cdrInterface.setState(gmlcCdrState);

        // attach, in case impl wants to use more of dialog.
        SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
        aci.attach(sbbLO);
      }
      // Set timer last
      this.setTimer(aci);

      // Inquire if MAP ATI response includes subscriber's info
      if (subscriberInfo != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        // Inquire if subscriber state is included in MAP ATI response subscriber's info
        if (subscriberInfo.getSubscriberState() != null) {
          mlpRespResult = MLPResponse.MLPResultType.OK;
          // Subscriber state is included in MAP ATI response, get it and store it as a response parameter
          atiResponseValues.setSubscriberState(subscriberInfo.getSubscriberState());
          //atiResponse.subscriberState = subscriberInfo.getSubscriberState().getSubscriberStateChoice().toString();
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setSubscriberState(atiResponseValues.getSubscriberState().getSubscriberStateChoice().toString());
            if (subscriberInfo.getLocationInformation() == null) {
              if (this.logger.isFineEnabled()) {
                this.logger.fine("\nonAnyTimeInterrogationResponse: "
                        + "CDR state is initialized, ATI_STATE_SUCCESS");
              }
              this.createCDRRecord(RecordStatus.ATI_STATE_SUCCESS);
            }
          }
        }
        // Inquire if Location information is included in MAP ATI response subscriber's info
        if (subscriberInfo.getLocationInformation() != null) {
          atiResponseValues.setLocationInformation(subscriberInfo.getLocationInformation());
          mlpRespResult = MLPResponse.MLPResultType.OK;
          // Location information is included in MAP ATI response, then
          // Inquire if Cell Global Identity (CGI) or Service Area Identity (SAI) or Location Area Identity (LAI) are included in MAP ATI response
          if (subscriberInfo.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI() != null) {
            // CGI or SAI or LAI are included in MAP ATI response
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = subscriberInfo.getLocationInformation()
                    .getCellGlobalIdOrServiceAreaIdOrLAI();
            // Inquire and get parameters of CGI or SAI or LAI included in MAP ATI response
            if (cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
              if (this.logger.isFineEnabled()) {
                this.logger.fine("\nonAnyTimeInterrogationResponse: "
                        + "received CellGlobalIdOrServiceAreaIdFixedLength, decoding MCC, MNC, LAC, CI");
              }
              atiResponseValues.setCellGlobalIdOrServiceAreaIdFixedLength(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength());
              try {
                atiResponseValues.setMcc(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
                atiResponseValues.setMnc(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
                atiResponseValues.setLac(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
                atiResponseValues.setCi(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode());
              } catch (MAPException e1) {
                e1.printStackTrace();
              }
              // Inquire if Age of Location Information is included in MAP ATI response subscriber's info
              if (subscriberInfo.getLocationInformation().getAgeOfLocationInformation() != null) {
                atiResponseValues.setAgeOfLocationInfo(subscriberInfo.getLocationInformation().getAgeOfLocationInformation().intValue());
              }
              // Inquire if VLR number (Global Title) is included in MAP ATI response subscriber's info
              if (subscriberInfo.getLocationInformation().getVlrNumber() != null) {
                atiResponseValues.setVlrNumber(subscriberInfo.getLocationInformation().getVlrNumber());
              }
              if (gmlcCdrState.isInitialized()) {
                if (this.logger.isFineEnabled()) {
                  this.logger.fine("\nonAnyTimeInterrogationResponse: "
                          + "CDR state is initialized, ATI_CGI_SUCCESS");
                }
                try {
                  gmlcCdrState.setMcc(atiResponseValues.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
                  gmlcCdrState.setMnc(atiResponseValues.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
                  gmlcCdrState.setLac(atiResponseValues.getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
                  gmlcCdrState.setCi(atiResponseValues.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode());
                } catch (MAPException e1) {
                  e1.printStackTrace();
                }
                gmlcCdrState.setAol(atiResponseValues.getAgeOfLocationInfo());
                gmlcCdrState.setAtiVlrGt(atiResponseValues.getVlrNumber());
                if (gmlcCdrState.getSubscriberState() != null) {
                  this.createCDRRecord(RecordStatus.ATI_CGI_STATE_SUCCESS);
                } else {
                  this.createCDRRecord(RecordStatus.ATI_CGI_SUCCESS);
                }
              }
            } else if (cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength() != null) {
              // Case when LAI length is fixed
              if (this.logger.isFineEnabled()) {
                this.logger.fine("\nonAnyTimeInterrogationResponse: "
                        + "received laiFixedLength, decoding MCC, MNC, LAC (no CI)");
              }
              atiResponseValues.setMcc(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMCC());
              atiResponseValues.setMcc(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMNC());
              atiResponseValues.setLac(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getLac());
              if (gmlcCdrState.isInitialized()) {
                if (this.logger.isFineEnabled()) {
                  this.logger.fine("\nonAnyTimeInterrogationResponse: "
                          + "CDR state is initialized, ATI_LAI_SUCCESS");
                }
                gmlcCdrState.setMcc(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMCC());
                gmlcCdrState.setMnc(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMNC());
                gmlcCdrState.setLac(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getLac());
                if (gmlcCdrState.getSubscriberState() != null) {
                  this.createCDRRecord(RecordStatus.ATI_LAI_STATE_SUCCESS);
                } else {
                  this.createCDRRecord(RecordStatus.ATI_LAI_SUCCESS);
                }
              }
            }
          }
        }

        // ATI Error CDR creation
        if (mapErrorMessage != null) {
          if (gmlcCdrState.isInitialized()) {
            if (mapErrorMessage.getErrorCode() == 49)
              this.createCDRRecord(RecordStatus.ATI_NOT_ALLOWED);
            if (mapErrorMessage.getErrorCode() == 34)
              this.createCDRRecord(RecordStatus.ATI_SYSTEM_FAILURE);
            if (mapErrorMessage.getErrorCode() == 35)
              this.createCDRRecord(RecordStatus.ATI_DATA_MISSING);
            if (mapErrorMessage.getErrorCode() == 36)
              this.createCDRRecord(RecordStatus.ATI_UNEXPECTED_DATA_VALUE);
            if (mapErrorMessage.getErrorCode() == 1)
              this.createCDRRecord(RecordStatus.ATI_UNKNOWN_SUBSCRIBER);
          }
        }


      } else {
        if (gmlcCdrState.isInitialized()) {
          this.createCDRRecord(RecordStatus.ATI_CGI_OR_LAI_OR_STATE_FAILURE);
        }
        mlpRespResult = MLPResponse.MLPResultType.SYSTEM_FAILURE;
        mlpClientErrorMessage = "Bad AnyTimeInterrogationResponse received: " + event;
      }
      // Handle successful retrieval of subscriber's info
      this.handleLocationResponse(mlpRespResult, atiResponseValues, mlpClientErrorMessage);

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process AnyTimeInterrogationResponse=%s", event), e);
      this.createCDRRecord(RecordStatus.ATI_SYSTEM_FAILURE);
      this.handleLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null,
              "Internal failure occurred while processing network response: " + e.getMessage());
    }
  }

  /**
   * Location Service Management (LSM) services
   * MAP_SEND_ROUTING_INFO_FOR_LCS (SRIforLCS) Events
   */
  /*
   * MAP SRIforLCS Request Event
   */
  public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest event, ActivityContextInterface aci) {

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onSendRoutingInfoForLCSRequest = " + event);
      }

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onSendRoutingInfoForLCSRequest=%s", event), e);
    }
  }

  /*
   * MAP SRIforLCS Response Event
   */
  public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse event, ActivityContextInterface aci, EventContext eventContext) {

    MAPErrorMessage mapErrorMessage = this.getErrorResponse();

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onSendRoutingInfoForLCSResponse = " + event);
      }

      MAPDialogLsm mapDialogLsm = event.getMAPDialog();
      SubscriberIdentity subscriberIdentity = event.getTargetMS();
      LCSLocationInfo lcsLocationInfo = event.getLCSLocationInfo();
      MAPExtensionContainer mapExtensionContainer = event.getExtensionContainer();
      GSNAddress vGmlcAddress = event.getVgmlcAddress();
      GSNAddress hGmlcAddress = event.getHGmlcAddress();
      GSNAddress pprAddress = event.getPprAddress();
      GSNAddress additionalVGmlcAddress = event.getAdditionalVGmlcAddress();

      SriForLcsResponseValues sriForLcsResponseValues = new SriForLcsResponseValues();
      MLPResponse.MLPResultType mlpRespResult = null;
      String mlpClientErrorMessage = null;

      // CDR initialization stuff
      CDRInterface cdrInterface = this.getCDRInterface();
      GMLCCDRState gmlcCdrState = cdrInterface.getState();
      if (!gmlcCdrState.isInitialized()) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForLCSResponse: CDR state is NOT initialized: " + gmlcCdrState + ", initiating\n");
        }
        gmlcCdrState.init(mapDialogLsm.getLocalDialogId(), mapDialogLsm.getReceivedDestReference(), mapDialogLsm.getReceivedOrigReference(),
                subscriberIdentity.getMSISDN(), mapDialogLsm.getLocalAddress(), mapDialogLsm.getRemoteAddress());
        gmlcCdrState.setDialogStartTime(DateTime.now());
        gmlcCdrState.setRemoteDialogId(mapDialogLsm.getRemoteDialogId());
        cdrInterface.setState(gmlcCdrState);

        // attach, in case impl wants to use more of dialog.
        SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
        aci.attach(sbbLO);
      }
      // Set timer last
      this.setTimer(aci);

      if (subscriberIdentity != null) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForLCSResponse: received SubscriberIdentity");
        }
        if (subscriberIdentity.getMSISDN() != null) {
          sriForLcsResponseValues.setMsisdn(event.getTargetMS());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setMsisdn(sriForLcsResponseValues.getMsisdn().getMSISDN());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                      + "CDR state is initialized, MSISDN set");
            }
          }
        }
        if (subscriberIdentity.getIMSI() != null) {
          sriForLcsResponseValues.setImsi(event.getTargetMS().getIMSI());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setImsi(sriForLcsResponseValues.getImsi());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                      + "CDR state is initialized, IMSI set");
            }
          }
        }
      } else {
        this.createCDRRecord(RecordStatus.SRI_UNKNOWN_SUBSCRIBER);
      }

      if (lcsLocationInfo != null) {

        logger.info("\nonSendRoutingInfoForLCSResponse, lcsLocationInfo != null");
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (lcsLocationInfo.getNetworkNodeNumber() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: received NetworkNodeNumber: "+lcsLocationInfo.getNetworkNodeNumber().getAddress());
          }
          sriForLcsResponseValues.setNetworkNodeNumber(lcsLocationInfo.getNetworkNodeNumber());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setNetworkNodeNumber(sriForLcsResponseValues.getNetworkNodeNumber());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                      + "CDR state is initialized, NNN set");
            }
          }
        }

        if (lcsLocationInfo.getLMSI() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: received LMSI");
          }
          sriForLcsResponseValues.setLmsi(lcsLocationInfo.getLMSI());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setLmsi(sriForLcsResponseValues.getLmsi());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                      + "CDR state is initialized, LMSI set");
            }
          }
        }

        if (lcsLocationInfo.getAdditionalNumber() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: received AdditionalNumber");
          }
          sriForLcsResponseValues.setAdditionalNumber(lcsLocationInfo.getAdditionalNumber());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setAdditionalNumber(sriForLcsResponseValues.getAdditionalNumber());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                      + "CDR state is initialized, Additional Number set");
            }
          }
        }

        if (lcsLocationInfo.getSupportedLCSCapabilitySets() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: received SupportedLCSCapabilitySets");
          }
          sriForLcsResponseValues.setSupportedLCSCapabilitySets(lcsLocationInfo.getSupportedLCSCapabilitySets());
        }

        if (lcsLocationInfo.getAdditionalLCSCapabilitySets() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: received AdditionalLCSCapabilitySets");
          }
          sriForLcsResponseValues.setAddSupportedLCSCapabilitySets(lcsLocationInfo.getAdditionalLCSCapabilitySets());
        }

        if (lcsLocationInfo.getGprsNodeIndicator() != false) {
          sriForLcsResponseValues.setGprsNodeIndicator(lcsLocationInfo.getGprsNodeIndicator());
        }

        if (lcsLocationInfo.getMmeName() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: received MMEName");
          }
          sriForLcsResponseValues.setMmeName(lcsLocationInfo.getMmeName());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setMmeName(sriForLcsResponseValues.getMmeName());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                      + "CDR state is initialized, MME Name set");
            }
          }
        }

        if (lcsLocationInfo.getAaaServerName() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: received AAAServerName");
          }
          sriForLcsResponseValues.setAaaServerName(lcsLocationInfo.getAaaServerName());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setAaaServerName(sriForLcsResponseValues.getAaaServerName());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                      + "CDR state is initialized, AAA Server Name set");
            }
          }
        }
      } else {
        this.createCDRRecord(RecordStatus.SRI_ABSENT_SUBSCRIBER);
      }

      if (mapExtensionContainer != null) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForLCSResponse: received MAPExtensionContainer");
        }
      }

      if (vGmlcAddress != null) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForLCSResponse: received VGMLCAddress");
        }
        sriForLcsResponseValues.setvGmlcAddress(vGmlcAddress);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setvGmlcAddress(sriForLcsResponseValues.getvGmlcAddress());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                    + "CDR state is initialized, V-GMLC Address set");
          }
        }
      }

      if (hGmlcAddress != null) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForLCSResponse: received HGMLCAddress");
        }
        sriForLcsResponseValues.sethGmlcAddress(hGmlcAddress);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.sethGmlcAddress(sriForLcsResponseValues.gethGmlcAddress());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                    + "CDR state is initialized, H-GMLC Address set");
          }
        }
      }

      if (pprAddress != null) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForLCSResponse: received HGMLCAddress, decoding Data, GSNAddressAddressType and GSNAddressAddressData parameters");
        }
        sriForLcsResponseValues.setPprAddress(pprAddress);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setPprAddress(sriForLcsResponseValues.getPprAddress());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                    + "CDR state is initialized, PPR Address set");
          }
        }
      }

      if (additionalVGmlcAddress != null) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForLCSResponse: received AdditionalVGmlcAddress, decoding Data, GSNAddressAddressType and GSNAddressAddressData parameters");
        }
        sriForLcsResponseValues.setAddVGmlcAddress(additionalVGmlcAddress);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setvGmlcAddress(sriForLcsResponseValues.getAddVGmlcAddress());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForLCSResponse: "
                    + "CDR state is initialized, Additional V-GMLC Address set");
          }
        }
      }

      this.setSendRoutingInfoForLCSResponse(event);

      if (this.getSendRoutingInfoForLCSResponse() != null) {

        ISDNAddressString mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, this.getGmlcSccpAddress().getGlobalTitle().getDigits());

        // LocationType for PSL composed from HTTP request values
        LocationEstimateType locationEstimateType = LocationEstimateType.valueOf(pslLocationEstimateType);
        String deferredLocEvType = this.pslDeferredLocationEventType;
        DeferredLocationEventType deferredLocationEventType = new DeferredLocationEventTypeImpl();
        switch (deferredLocEvType) {
          case "inside":
            deferredLocationEventType.getBeingInsideArea();
            break;
          case "entering":
            deferredLocationEventType.getEnteringIntoArea();
            break;
          case "leaving":
            deferredLocationEventType.getLeavingFromArea();
            break;
          case "available":
            deferredLocationEventType.getMsAvailable();
            break;
          default:
            deferredLocationEventType.getBeingInsideArea();
            break;
        }
        LocationType locationType = new LocationTypeImpl(locationEstimateType, deferredLocationEventType);

        // IMEI hardcoded to null for now
        IMEI imei = null;

        // LCSClientID hardcoded to nul for now
        LCSClientID lcsClientID = null;

        // LCSPriority for PSL composed from HTTP request values
        LCSPriority lcsPriority = LCSPriority.valueOf(pslLcsPriority);

        // LCSQoS for PSL composed from HTTP request values
        Integer horizontalAccuracy = Integer.valueOf(this.pslLcsHorizontalAccuracy);
        Integer verticalAccuracy = Integer.valueOf(this.pslLcsVerticalAccuracy);
        boolean verticalCoordinateRequest = Boolean.parseBoolean(pslVerticalCoordinateRequest);
        ResponseTimeCategory responseTimeCategory = ResponseTimeCategory.valueOf(pslResponseTimeCategory);
        ResponseTime responseTime = new ResponseTimeImpl(responseTimeCategory);
        MAPExtensionContainer PslMapExtensionContainer = null;
        LCSQoS lcsQoS = new LCSQoSImpl(horizontalAccuracy, verticalAccuracy, verticalCoordinateRequest, responseTime, PslMapExtensionContainer);

        // SupportedGADShapes hardcoded to true for all shapes for now
        boolean ellipsoidPoint = true;
        boolean ellipsoidPointWithUncertaintyCircle = true;
        boolean ellipsoidPointWithUncertaintyEllipse = true;
        boolean polygon = true;
        boolean ellipsoidPointWithAltitude = true;
        boolean ellipsoidPointWithAltitudeAndUncertaintyElipsoid = true;
        boolean ellipsoidArc = true;
        SupportedGADShapes supportedGADShapes = new SupportedGADShapesImpl(ellipsoidPoint, ellipsoidPointWithUncertaintyCircle,
                ellipsoidPointWithUncertaintyEllipse, polygon, ellipsoidPointWithAltitude, ellipsoidPointWithAltitudeAndUncertaintyElipsoid, ellipsoidArc);

        // LCSCodeword hardcoded to nul for now
        LCSCodeword lcsCodeword = null;

        // LCSPrivacyCheck hardcoded to nul for now
        LCSPrivacyCheck lcsPrivacyCheck = null;

        // AreaEventInfo for PSL composed from HTTP request values
        ArrayList<Area> areaList = new ArrayList<Area>();
        AreaType areaType = AreaType.valueOf(pslAreaType);
        byte[] areaId = pslAreaId.getBytes();
        AreaIdentification areaIdentification = new AreaIdentificationImpl(areaId);
        Area area1 = new AreaImpl(areaType, areaIdentification);
        areaList.add(area1);
        AreaDefinition areaDefinition = new AreaDefinitionImpl(areaList);
        OccurrenceInfo occurrenceInfo = OccurrenceInfo.valueOf(pslOccurrenceInfo);
        Integer intervalTime = Integer.parseInt(this.pslIntervalTime);
        AreaEventInfo areaEventInfo = new AreaEventInfoImpl(areaDefinition, occurrenceInfo, intervalTime);

        // PeriodicLDRInfo for PSL composed from HTTP request values
        PeriodicLDRInfo periodicLDRInfo = new PeriodicLDRInfoImpl(Integer.parseInt(pslReportingAmount), Integer.parseInt(pslReportingInterval));

        // moLrShortCircuitIndicator hardcoded to false for now
        boolean moLrShortCircuitIndicator = false;

        // ReportingPLMNList hardcoded to null for now
        ReportingPLMNList reportingPLMNList = null;

        AddressString origAddressString, destAddressString1;
        origAddressString = destAddressString1 = null;

        MAPDialogLsm mapDialogLsmPsl = this.mapProvider.getMAPServiceLsm().createNewDialog(
                this.getMAPPslSlrApplicationContext(),  this.getGmlcSccpAddress(), origAddressString,
                getNNNSCCPAddress(lcsLocationInfo.getNetworkNodeNumber().getAddress()), destAddressString1);

        mapDialogLsmPsl.addProvideSubscriberLocationRequest(locationType, mlcNumber, lcsClientID, false,
                sriForLcsResponseValues.getImsi(), sriForLcsResponseValues.getMsisdn().getClass().newInstance().getMSISDN(),
                sriForLcsResponseValues.getLmsi(), imei, lcsPriority, lcsQoS, mapExtensionContainer,
                supportedGADShapes, Integer.parseInt(pslLcsReferenceNumber), Integer.parseInt(pslLcsServiceTypeID), lcsCodeword, lcsPrivacyCheck,
                areaEventInfo, hGmlcAddress, moLrShortCircuitIndicator, periodicLDRInfo, reportingPLMNList);

        // Keep ACI in across MAP dialog for PSL
        ActivityContextInterface lsmPslDialogACI = this.mapAcif.getActivityContextInterface(mapDialogLsmPsl);
        lsmPslDialogACI.attach(this.sbbContext.getSbbLocalObject());

        // ProvideSubscriberLocationRequest is now composed by values taken from SRIforLCS response and HTTP request
        // Send PSL
        mapDialogLsmPsl.send();

      } else {
        // ATI Error CDR creation
        if (mapErrorMessage != null) {
          if (gmlcCdrState.isInitialized()) {
            if (mapErrorMessage.getErrorCode() == 52)
              this.createCDRRecord(RecordStatus.SRI_UNAUTHORIZED_REQUESTING_NETWORK);
            if (mapErrorMessage.getErrorCode() == 34)
              this.createCDRRecord(RecordStatus.SRI_SYSTEM_FAILURE);
            if (mapErrorMessage.getErrorCode() == 35)
              this.createCDRRecord(RecordStatus.SRI_DATA_MISSING);
            if (mapErrorMessage.getErrorCode() == 36)
              this.createCDRRecord(RecordStatus.SRI_UNEXPECTED_DATA_VALUE);
            if (mapErrorMessage.getErrorCode() == 1)
              this.createCDRRecord(RecordStatus.SRI_UNKNOWN_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 27)
              this.createCDRRecord(RecordStatus.SRI_ABSENT_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 21)
              this.createCDRRecord(RecordStatus.SRI_FACILITY_NOT_SUPPORTED);
          }
        }
      }

    } catch (MAPException me) {
      logger.severe(String.format("MAPException while trying to process SendRoutingInfoForLCSResponse=%s", event), me);
      logger.severe("MAP error message when processing onSendRoutingInfoForLCSResponse: " + mapErrorMessage);
      this.handleLsmLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null, null, null,
              "Internal failure occurred while processing network response: " + me.getMessage());
    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process SendRoutingInfoForLCSResponse=%s", event), e);
      this.handleLsmLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null, null, null,
              "Internal failure occurred while processing network response: " + e.getMessage());
    }
  }

  /**
   * Location Service Management (LSM) services
   * MAP_PROVIDE_SUBSCRIBER_LOCATION (PSL) Events
   */

  /**
   * MAP PSL Request Event
   */
  public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest event, ActivityContextInterface aci) {

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onProvideSubscriberLocationRequest = " + event);
      }

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onProvideSubscriberLocationRequest=%s", event), e);
    }

  }

  /**
   * MAP PSL Response Event
   */
  public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse event, ActivityContextInterface aci) {

    MAPErrorMessage mapErrorMessage = this.getErrorResponse();
    this.setProvideSubscriberLocationResponse(event);

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onProvideSubscriberLocationResponse = " + event);
      }

      MAPDialogLsm mapDialogLsm = event.getMAPDialog();
      ExtGeographicalInformation extGeographicalInformation = event.getLocationEstimate();
      Integer ageOfLocationEstimate = event.getAgeOfLocationEstimate();
      PositioningDataInformation geranPositioningData = event.getGeranPositioningData();
      UtranPositioningDataInfo utranPositioningDataInfo = event.getUtranPositioningData();
      AddGeographicalInformation addGeographicalInformation = event.getAdditionalLocationEstimate();
      MAPExtensionContainer mapExtensionContainer = event.getExtensionContainer();
      boolean deferredMTLRResponseIndicator = event.getDeferredMTLRResponseIndicator();
      CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = event.getCellIdOrSai();
      boolean saiPresent = event.getSaiPresent();
      AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = event.getAccuracyFulfilmentIndicator();
      VelocityEstimate velocityEstimate = event.getVelocityEstimate();
      boolean moLrShortCircuitIndicator = event.getMoLrShortCircuitIndicator();
      GeranGANSSpositioningData geranGANSSpositioningData = event.getGeranGANSSpositioningData();
      UtranGANSSpositioningData utranGANSSpositioningData = event.getUtranGANSSpositioningData();
      ServingNodeAddress servingNodeAddress = event.getTargetServingNodeForHandover();

      PslResponseValues pslResponseValues = new PslResponseValues();
      MLPResponse.MLPResultType mlpRespResult = null;
      String mlpClientErrorMessage = null;

      // CDR initialization stuff
      CDRInterface cdrInterface = this.getCDRInterface();
      GMLCCDRState gmlcCdrState = cdrInterface.getState();
      if (!gmlcCdrState.isInitialized()) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: CDR state is NOT initialized: " + gmlcCdrState + ", initiating\n");
        }
        gmlcCdrState.init(mapDialogLsm.getLocalDialogId(), mapDialogLsm.getReceivedDestReference(), mapDialogLsm.getReceivedOrigReference(),
                null, mapDialogLsm.getLocalAddress(), mapDialogLsm.getRemoteAddress());
        gmlcCdrState.setDialogStartTime(DateTime.now());
        gmlcCdrState.setRemoteDialogId(mapDialogLsm.getRemoteDialogId());
        cdrInterface.setState(gmlcCdrState);

        // attach, in case impl wants to use more of dialog.
        SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
        aci.attach(sbbLO);
      }
      // Set timer last
      this.setTimer(aci);

      if (extGeographicalInformation != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: "
                  + "received LocationEstimate, decoding ExtGeographicalInformation parameters");
        }
        pslResponseValues.setLocationEstimate(extGeographicalInformation);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setLocationEstimate(pslResponseValues.getLocationEstimate());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, Location Estimate set");
          }
        }
        if (extGeographicalInformation.getTypeOfShape() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate type of shape: " +extGeographicalInformation.getTypeOfShape());
          }
        }
        if (extGeographicalInformation.getLatitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate latitude: " +extGeographicalInformation.getLatitude());
          }
        }
        if (extGeographicalInformation.getLongitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate longitude: " +extGeographicalInformation.getLongitude());
          }
        }
        if (extGeographicalInformation.getUncertainty() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate uncertainty: " +extGeographicalInformation.getUncertainty());
          }
        }
        if (extGeographicalInformation.getUncertaintySemiMajorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate uncertainty Semi Major Axis: " +extGeographicalInformation.getUncertaintySemiMajorAxis());
          }
        }
        if (extGeographicalInformation.getUncertaintySemiMinorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate uncertainty Semi Minor Axis: " +extGeographicalInformation.getUncertaintySemiMinorAxis());
          }
        }
        if (extGeographicalInformation.getAngleOfMajorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate angle of major axis: " +extGeographicalInformation.getAngleOfMajorAxis());
          }
        }
        if (extGeographicalInformation.getConfidence() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate confidence: " +extGeographicalInformation.getConfidence());
          }
        }
        if (extGeographicalInformation.getAltitude() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate altitude: " +extGeographicalInformation.getAltitude());
          }
        }
        if (extGeographicalInformation.getUncertaintyAltitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate altitude uncertainty: " +extGeographicalInformation.getUncertaintyAltitude());
          }
        }
        if (extGeographicalInformation.getInnerRadius() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate inner radius: " +extGeographicalInformation.getInnerRadius());
          }
        }
        if (extGeographicalInformation.getUncertaintyRadius() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate radius uncertainty: " +extGeographicalInformation.getUncertaintyRadius());
          }
        }
        if (extGeographicalInformation.getOffsetAngle() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate offset angle: " +extGeographicalInformation.getOffsetAngle());
          }
        }
        if (extGeographicalInformation.getIncludedAngle() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "LocationEstimate include angle: " +extGeographicalInformation.getIncludedAngle());
          }
        }
      }

      if (geranPositioningData != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received GeranPositioningDataInformation");
        }
        pslResponseValues.setGeranPositioningDataInformation(geranPositioningData);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setGeranPositioningDataInformation(pslResponseValues.getGeranPositioningDataInformation());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, GERAN Positioning Data Info set");
          }
        }
      }

      if (utranPositioningDataInfo != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received UtranPositioningDataInformation");
        }
        pslResponseValues.setUtranPositioningDataInfo(utranPositioningDataInfo);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setUtranPositioningDataInfo(pslResponseValues.getUtranPositioningDataInfo());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, UTRAN Positioning Data Info set");
          }
        }
      }

      if (ageOfLocationEstimate != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received AgeOfLocationInformation parameter");
        }
        pslResponseValues.setAgeOfLocationEstimate(ageOfLocationEstimate);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setAgeOfLocationEstimate(pslResponseValues.getAgeOfLocationEstimate());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, Age of Location Estimate set");
          }
        }
      }

      if (addGeographicalInformation != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received AdditionalLocationEstimate");
        }
        pslResponseValues.setAdditionalLocationEstimate(addGeographicalInformation);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setAdditionalLocationEstimate(pslResponseValues.getAdditionalLocationEstimate());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, Additional Location Estimate set");
          }
        }
        if (addGeographicalInformation.getTypeOfShape() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate type of shape: " +addGeographicalInformation.getTypeOfShape());
          }
        }
        if (addGeographicalInformation.getLatitude() <= Double.MAX_VALUE) {
          mlpRespResult = MLPResponse.MLPResultType.OK;
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate latitude: " +addGeographicalInformation.getLatitude());
          }
        }
        if (addGeographicalInformation.getLongitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate longitude: " +addGeographicalInformation.getLongitude());
          }
        }
        if (addGeographicalInformation.getUncertainty() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate uncertainty: " +addGeographicalInformation.getUncertainty());
          }
        }
        if (addGeographicalInformation.getUncertaintySemiMajorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate uncertainty Semi Major Axis: " +addGeographicalInformation.getUncertaintySemiMajorAxis());
          }
        }
        if (addGeographicalInformation.getUncertaintySemiMinorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate uncertainty Semi Minor Axis: " +addGeographicalInformation.getUncertaintySemiMinorAxis());
          }
        }
        if (addGeographicalInformation.getAngleOfMajorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate angle of Major Axis: " +addGeographicalInformation.getAngleOfMajorAxis());
          }
        }
        if (addGeographicalInformation.getConfidence() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate confidence: " +addGeographicalInformation.getConfidence());
          }
        }
        if (addGeographicalInformation.getAltitude() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate altitude: " +addGeographicalInformation.getAltitude());
          }
        }
        if (addGeographicalInformation.getUncertaintyAltitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate altitude uncertainty: " +addGeographicalInformation.getUncertaintyAltitude());
          }
        }
        if (addGeographicalInformation.getInnerRadius() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate inner radius: " +addGeographicalInformation.getInnerRadius());
          }
        }
        if (addGeographicalInformation.getUncertaintyRadius() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate radius uncertainty: " +addGeographicalInformation.getUncertaintyRadius());
          }
        }
        if (addGeographicalInformation.getOffsetAngle() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate offset angle: " +addGeographicalInformation.getOffsetAngle());
          }
        }
        if (addGeographicalInformation.getIncludedAngle() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "AdditionalLocationEstimate include angle: " +addGeographicalInformation.getIncludedAngle());
          }
        }
      }

      if (mapExtensionContainer != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received MAPExtensionContainer");
        }
      }

      if (deferredMTLRResponseIndicator != false) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received DeferredMTLRResponseIndicator");
        }
        pslResponseValues.setDeferredMTLRResponseIndicator(deferredMTLRResponseIndicator);
      }

      if (cellGlobalIdOrServiceAreaIdOrLAI != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received CellGlobalIdOrServiceAreaIdOrLAI");
        }
        if (cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
          pslResponseValues.setCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdOrLAI);
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setCellGlobalIdOrServiceAreaIdOrLAI(pslResponseValues.getCellGlobalIdOrServiceAreaIdOrLAI());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonProvideSubscriberLocationResponse: "
                      + "CDR state is initialized, CGI or SAI or LAI set");
            }
          }
        } else if (cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength() != null) {
          pslResponseValues.setCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdOrLAI);
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setCellGlobalIdOrServiceAreaIdOrLAI(pslResponseValues.getCellGlobalIdOrServiceAreaIdOrLAI());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonProvideSubscriberLocationResponse: "
                      + "CDR state is initialized, CGI or SAI or LAI set");
            }
          }
        }
      }

      if (saiPresent != false) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received SAIPresent");
        }
      }

      if (accuracyFulfilmentIndicator != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received AccuracyFulfilmentIndicator");
        }
        pslResponseValues.setAccuracyFulfilmentIndicator(accuracyFulfilmentIndicator);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setAccuracyFulfilmentIndicator(pslResponseValues.getAccuracyFulfilmentIndicator());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, Accuracy fulfillment indicator set");
          }
        }
      }

      if (velocityEstimate != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received VelocityEstimate");
        }
        pslResponseValues.setVelocityEstimate(velocityEstimate);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setVelocityEstimate(pslResponseValues.getVelocityEstimate());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, Velocity Estimate set");
          }
        }
      }

      if (moLrShortCircuitIndicator != false) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received MoLrShortCircuitIndicator");
        }
        pslResponseValues.setMoLrShortCircuitIndicator(moLrShortCircuitIndicator);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setMoLrShortCircuitIndicator(moLrShortCircuitIndicator);
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, MO LR Short Circuit Indicator set");
          }
        }
      }

      if (geranGANSSpositioningData != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received GeranGANSSpositioningData");
        }
        pslResponseValues.setGeranGANSSpositioningData(geranGANSSpositioningData);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setGeranGANSSpositioningData(pslResponseValues.getGeranGANSSpositioningData());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, GERAN GANSS Positioning Data set");
          }
        }
      }

      if (utranGANSSpositioningData != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received UtranGANSSpositioningData");
        }
        pslResponseValues.setUtranGANSSpositioningData(utranGANSSpositioningData);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setUtranGANSSpositioningData(pslResponseValues.getUtranGANSSpositioningData());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, UTRAN GANSS Positioning Data set");
          }
        }
      }

      if (servingNodeAddress != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberLocationResponse: received ServingNodeAddress");
        }
        pslResponseValues.setServingNodeAddress(servingNodeAddress);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setServingNodeAddress(pslResponseValues.getServingNodeAddress());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonProvideSubscriberLocationResponse: "
                    + "CDR state is initialized, Serving Node Address set");
          }
        }
      }

      // PSL CDR creation
      if (extGeographicalInformation != null || geranPositioningData != null || utranPositioningDataInfo != null || geranPositioningData != null
              || utranPositioningDataInfo != null || addGeographicalInformation != null || cellGlobalIdOrServiceAreaIdOrLAI != null
              || velocityEstimate != null || geranGANSSpositioningData != null || utranGANSSpositioningData != null || servingNodeAddress != null) {
        if (gmlcCdrState.isInitialized()) {
          this.createCDRRecord(RecordStatus.PSL_SUCCESS);
        } else {
          if (gmlcCdrState.isInitialized()) {
            if (mapErrorMessage.getErrorCode() == 52)
              this.createCDRRecord(RecordStatus.PSL_UNAUTHORIZED_REQUESTING_NETWORK);
            if (mapErrorMessage.getErrorCode() == 53)
              this.createCDRRecord(RecordStatus.PSL_UNAUTHORIZED_LCS_CLIENT);
            if (mapErrorMessage.getErrorCode() == 54)
              this.createCDRRecord(RecordStatus.PSL_POSITION_METHOD_FAILURE);
            if (mapErrorMessage.getErrorCode() == 34)
              this.createCDRRecord(RecordStatus.PSL_SYSTEM_FAILURE);
            if (mapErrorMessage.getErrorCode() == 35)
              this.createCDRRecord(RecordStatus.PSL_DATA_MISSING);
            if (mapErrorMessage.getErrorCode() == 36)
              this.createCDRRecord(RecordStatus.PSL_UNEXPECTED_DATA_VALUE);
            if (mapErrorMessage.getErrorCode() == 21)
              this.createCDRRecord(RecordStatus.PSL_FACILITY_NOT_SUPPORTED);
            if (mapErrorMessage.getErrorCode() == 27)
              this.createCDRRecord(RecordStatus.PSL_ABSENT_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 9)
              this.createCDRRecord(RecordStatus.PSL_ILLEGAL_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 12)
              this.createCDRRecord(RecordStatus.PSL_ILLEGAL_EQUIPMENT);
            if (mapErrorMessage.getErrorCode() == 5)
              this.createCDRRecord(RecordStatus.PSL_UNIDENTIFIED_SUBSCRIBER);
          }
        }
      }

      // Handle successful retrieval of response to subscriber's location request (PSL response) info
      this.handleLsmLocationResponse(mlpRespResult, null, pslResponseValues, null, mlpClientErrorMessage);


    } catch (Exception e) {
      this.createCDRRecord(RecordStatus.PSL_SYSTEM_FAILURE);
      logger.severe(String.format("Error while trying to process onProvideSubscriberLocationResponse=%s", event), e);
      this.handleLsmLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null, null, null,
              "Internal failure occurred while processing network response: " + e.getMessage());
    }
  }


  /**
   * Location Service Management (LSM) services
   * MAP_SUBSCRIBER_LOCATION_REPORT (SLR) Events
   */

  /**
   * MAP SLR Request Event
   */
  public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest event, ActivityContextInterface aci) {

    //temporary log
    this.logger.severe("\nReceived onSubscriberLocationReportRequest = " + event);

    MAPErrorMessage mapErrorMessage = this.getErrorResponse();

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onSubscriberLocationReportRequest = " + event);
      }

      MAPDialogLsm mapDialogLsmSlr = event.getMAPDialog();
      LCSEvent lcsEvent = event.getLCSEvent();
      LCSClientID lcsClientID = event.getLCSClientID();
      LCSLocationInfo lcsLocationInfo = event.getLCSLocationInfo();
      ISDNAddressString msisdn = event.getMSISDN();
      IMSI imsi = event.getIMSI();
      IMEI imei = event.getIMEI();
      ISDNAddressString naESRD = event.getNaESRD();
      ISDNAddressString naESRK = event.getNaESRK();
      ExtGeographicalInformation extGeographicalInformation = event.getLocationEstimate();
      Integer ageOfLocationEstimate = event.getAgeOfLocationEstimate();
      SLRArgExtensionContainer slrArgExtensionContainer = event.getSLRArgExtensionContainer();
      AddGeographicalInformation addGeographicalInformation = event.getAdditionalLocationEstimate();
      DeferredmtlrData deferredmtlrData = event.getDeferredmtlrData();
      Integer lcsReferenceNumber = event.getLCSReferenceNumber();
      PositioningDataInformation geranPositioningData = event.getGeranPositioningData();
      UtranPositioningDataInfo utranPositioningDataInfo = event.getUtranPositioningData();
      CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = event.getCellGlobalIdOrServiceAreaIdOrLAI();
      GSNAddress hgmlcAddress = event.getHGMLCAddress();
      Integer lcsServiceTypeID = event.getLCSServiceTypeID();
      boolean saiPresent = event.getSaiPresent();
      boolean pseudonymIndicator = event.getPseudonymIndicator();
      AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = event.getAccuracyFulfilmentIndicator();
      VelocityEstimate velocityEstimate = event.getVelocityEstimate();
      Integer sequenceNumber = event.getSequenceNumber();
      PeriodicLDRInfo periodicLDRInfo = event.getPeriodicLDRInfo();
      boolean moLrShortCircuitIndicator = event.getMoLrShortCircuitIndicator();
      GeranGANSSpositioningData geranGANSSpositioningData = event.getGeranGANSSpositioningData();
      UtranGANSSpositioningData utranGANSSpositioningData = event.getUtranGANSSpositioningData();
      ServingNodeAddress servingNodeAddress = event.getTargetServingNodeForHandover();

      SlrRequestValues slrRequestValues = new SlrRequestValues();
      MLPResponse.MLPResultType mlpRespResult = null;
      String mlpClientErrorMessage = null;

      // CDR initialization stuff
      CDRInterface cdrInterface = this.getCDRInterface();
      GMLCCDRState gmlcCdrState = cdrInterface.getState();
      if (!gmlcCdrState.isInitialized()) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: CDR state is NOT initialized: " + gmlcCdrState + ", initiating\n");
        }
        gmlcCdrState.init(mapDialogLsmSlr.getLocalDialogId(), mapDialogLsmSlr.getReceivedDestReference(), mapDialogLsmSlr.getReceivedOrigReference(),
                msisdn, mapDialogLsmSlr.getLocalAddress(), mapDialogLsmSlr.getRemoteAddress());
        gmlcCdrState.setDialogStartTime(DateTime.now());
        gmlcCdrState.setRemoteDialogId(mapDialogLsmSlr.getRemoteDialogId());
        cdrInterface.setState(gmlcCdrState);

        // attach, in case impl wants to use more of dialog.
        SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
        aci.attach(sbbLO);
      }
      // Set timer last
      this.setTimer(aci);

      if (lcsEvent != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received LCSEvent parameter");
        }
        slrRequestValues.setLcsEvent(lcsEvent);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setLcsEvent(slrRequestValues.getLcsEvent());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Serving Node Address set");
          }
        }
      }

      if (lcsClientID != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received LCSClientID");
        }
        slrRequestValues.setLcsClientID(lcsClientID);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setLcsClientID(slrRequestValues.getLcsClientID());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Serving Node Address set");
          }
        }
        if (lcsClientID.getLCSClientType() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received LCSClient type");
          }
        }
        if (lcsClientID.getLCSClientExternalID() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received LCSClient External ID");
          }
        }
        if (lcsClientID.getLCSClientInternalID() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received LCSClient Internal ID");
          }
        }
        if (lcsClientID.getLCSClientDialedByMS() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received LCSClientDialedByMS");
          }
        }
        if (lcsClientID.getLCSClientName() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received LCSClient name");
          }
        }
        if (lcsClientID.getLCSAPN() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received LCSClient APN");
          }
        }
        if (lcsClientID.getLCSRequestorID() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received LCSClient requestor ID");
          }
        }
      }

      if (lcsLocationInfo != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received LCSLocationInfo parameters");
        }
        if (lcsLocationInfo.getNetworkNodeNumber() != null) {
          slrRequestValues.setNetworkNodeNumber(lcsLocationInfo.getNetworkNodeNumber());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setNetworkNodeNumber(slrRequestValues.getNetworkNodeNumber());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: "
                      + "CDR state is initialized, Network Node Number set");
            }
          }
        }
        if (lcsLocationInfo.getLMSI() != null) {
          slrRequestValues.setLmsi(lcsLocationInfo.getLMSI());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setLmsi(slrRequestValues.getLmsi());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: "
                      + "CDR state is initialized, LMSI set");
            }
          }
        }
        if (lcsLocationInfo.getExtensionContainer() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received extension container");
          }
        }
        if (lcsLocationInfo.getGprsNodeIndicator() != true) {
          slrRequestValues.setGprsNodeIndicator(lcsLocationInfo.getGprsNodeIndicator());
        }
        if (lcsLocationInfo.getAdditionalNumber() != null) {
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setAdditionalNumber(lcsLocationInfo.getAdditionalNumber());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: "
                      + "CDR state is initialized, Additional Number set");
            }
          }
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received AdditionaNumber");
          }
          if (lcsLocationInfo.getAdditionalNumber().getMSCNumber() != null) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: received MSC number");
            }
          }
          if (lcsLocationInfo.getAdditionalNumber().getSGSNNumber() != null) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: received SGSN number");
            }
          }
        }
        if (lcsLocationInfo.getSupportedLCSCapabilitySets() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received SupportedLCSCapabilitySets parameters");
          }
          if (lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: CapabilitySetRelease98_99 = false");
            }
          }
          if (lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease4() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: CapabilitySetRelease4 = false");
            }
          }
          if (lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease5() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: CapabilitySetRelease5 = false");
            }
          }
          if (lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease6() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: CapabilitySetRelease6 = false");
            }
          }
          if (lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease7() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: CapabilitySetRelease7 = false");
            }
          }
        }
        if (lcsLocationInfo.getAdditionalLCSCapabilitySets() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received AdditionalLCSCapabilitySets parameters");
          }
          if (lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease98_99() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: additional CapabilitySetRelease98_99() = false");
            }
          }
          if (lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease4() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: additional CapabilitySetRelease4 = false");
            }
          }
          if (lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease5() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: additional CapabilitySetRelease5 = false");
            }
          }
          if (lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease6() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: additional CapabilitySetRelease6 = false");
            }
          }
          if (lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease7() != true) {
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: additional CapabilitySetRelease7 = false");
            }
          }
        }
        if (lcsLocationInfo.getMmeName() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received MMEName parameter");
          }
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setMmeName(lcsLocationInfo.getMmeName());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: "
                      + "CDR state is initialized, MME Name set");
            }
          }
        }
        if (lcsLocationInfo.getAaaServerName() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: received AAAServerName parameter");
          }
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setAaaServerName(lcsLocationInfo.getAaaServerName());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSubscriberLocationReportRequest: "
                      + "CDR state is initialized, AAA Server Name set");
            }
          }
        }

      }

      if (msisdn != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received MSISDN parameter");
        }
        slrRequestValues.setMsisdn(msisdn);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setMsisdn(slrRequestValues.getMsisdn());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, MSISDN set");
          }
        }
      }

      if (imsi != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received IMSI parameter");
        }
        slrRequestValues.setImsi(imsi);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setImsi(slrRequestValues.getImsi());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, IMSI set");
          }
        }
      }

      if (imei != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received IMEI parameter");
        }
        slrRequestValues.setImei(imei);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setImei(slrRequestValues.getImei());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, IMEI set");
          }
        }
      }

      if (naESRD != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received NaESRD parameter");
        }
      }

      if (naESRK != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received NaESRK parameter");
        }
      }

      if (extGeographicalInformation != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: "
                  + "received LocationEstimate, decoding ExtGeographicalInformation parameters");
        }
        slrRequestValues.setLocationEstimate(extGeographicalInformation);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setLocationEstimate(slrRequestValues.getLocationEstimate());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Location Estimate set");
          }
        }
        if (extGeographicalInformation.getTypeOfShape() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate type of shape: " +extGeographicalInformation.getTypeOfShape());
          }
        }
        if (extGeographicalInformation.getLatitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate latitude: " +extGeographicalInformation.getLatitude());
          }
        }
        if (extGeographicalInformation.getLongitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate longitude: " +extGeographicalInformation.getLongitude());
          }
        }
        if (extGeographicalInformation.getUncertainty() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate uncertainty: " +extGeographicalInformation.getUncertainty());
          }
        }
        if (extGeographicalInformation.getUncertaintySemiMajorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate uncertainty Semi Major Axis: " +extGeographicalInformation.getUncertaintySemiMajorAxis());
          }
        }
        if (extGeographicalInformation.getUncertaintySemiMinorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate uncertainty Semi Minor Axis: " +extGeographicalInformation.getUncertaintySemiMinorAxis());
          }
        }
        if (extGeographicalInformation.getAngleOfMajorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate angle of major axis: " +extGeographicalInformation.getAngleOfMajorAxis());
          }
        }
        if (extGeographicalInformation.getConfidence() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate confidence: " +extGeographicalInformation.getConfidence());
          }
        }
        if (extGeographicalInformation.getAltitude() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate altitude: " +extGeographicalInformation.getAltitude());
          }
        }
        if (extGeographicalInformation.getUncertaintyAltitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate altitude uncertainty: " +extGeographicalInformation.getUncertaintyAltitude());
          }
        }
        if (extGeographicalInformation.getInnerRadius() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate inner radius: " +extGeographicalInformation.getInnerRadius());
          }
        }
        if (extGeographicalInformation.getUncertaintyRadius() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate radius uncertainty: " +extGeographicalInformation.getUncertaintyRadius());
          }
        }
        if (extGeographicalInformation.getOffsetAngle() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate offset angle: " +extGeographicalInformation.getOffsetAngle());
          }
        }
        if (extGeographicalInformation.getIncludedAngle() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "LocationEstimate include angle: " +extGeographicalInformation.getIncludedAngle());
          }
        }
      }

      if (ageOfLocationEstimate != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received AgeOfLocationInformation parameter");
        }
        slrRequestValues.setAgeOfLocationEstimate(ageOfLocationEstimate);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setAgeOfLocationEstimate(slrRequestValues.getAgeOfLocationEstimate());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Age of Location Estimate set");
          }
        }
      }

      if (slrArgExtensionContainer != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received SLRArgExtensionContainer parameter");
        }
      }

      if (addGeographicalInformation != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: "
                  + "received AdditionalLocationEstimate, decoding AddGeographicalInformation parameters");
        }
        slrRequestValues.setAdditionalLocationEstimate(addGeographicalInformation);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setAdditionalLocationEstimate(slrRequestValues.getAdditionalLocationEstimate());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Additional Location Estimate set");
          }
        }
        if (addGeographicalInformation.getTypeOfShape() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate type of shape: " +extGeographicalInformation.getTypeOfShape());
          }
        }
        if (addGeographicalInformation.getLatitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate latitude: " +extGeographicalInformation.getLatitude());
          }
        }
        if (addGeographicalInformation.getLongitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate longitude: " +extGeographicalInformation.getLongitude());
          }
        }
        if (addGeographicalInformation.getUncertainty() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate uncertainty: " +extGeographicalInformation.getUncertainty());
          }
        }
        if (addGeographicalInformation.getUncertaintySemiMajorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate uncertainty Semi Major Axis: " +extGeographicalInformation.getUncertaintySemiMajorAxis());
          }
        }
        if (addGeographicalInformation.getUncertaintySemiMinorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate uncertainty Semi Minor Axis: " +extGeographicalInformation.getUncertaintySemiMinorAxis());
          }
        }
        if (addGeographicalInformation.getAngleOfMajorAxis() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate angle of major axis: " +extGeographicalInformation.getAngleOfMajorAxis());
          }
        }
        if (addGeographicalInformation.getConfidence() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate confidence: " +extGeographicalInformation.getConfidence());
          }
        }
        if (addGeographicalInformation.getAltitude() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate altitude: " +extGeographicalInformation.getAltitude());
          }
        }
        if (addGeographicalInformation.getUncertaintyAltitude() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate altitude uncertainty: " +extGeographicalInformation.getUncertaintyAltitude());
          }
        }
        if (addGeographicalInformation.getInnerRadius() <= Integer.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate inner radius: " +extGeographicalInformation.getInnerRadius());
          }
        }
        if (addGeographicalInformation.getUncertaintyRadius() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate radius uncertainty: " +extGeographicalInformation.getUncertaintyRadius());
          }
        }
        if (addGeographicalInformation.getOffsetAngle() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate offset angle: " +extGeographicalInformation.getOffsetAngle());
          }
        }
        if (addGeographicalInformation.getIncludedAngle() <= Double.MAX_VALUE) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "AdditionalLocationEstimate include angle: " +extGeographicalInformation.getIncludedAngle());
          }
        }
      }

      if (deferredmtlrData != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received DeferredMTLRData");
        }
        slrRequestValues.setDeferredmtlrData(deferredmtlrData);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setDeferredmtlrData(slrRequestValues.getDeferredmtlrData());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Deferred MT LR Data set");
          }
        }
        if (deferredmtlrData.getDeferredLocationEventType() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "received DeferredMTLRData event type");
          }
        }
        if (deferredmtlrData.getTerminationCause() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "received DeferredMTLRData termination cause");
          }
        }
        if (deferredmtlrData.getLCSLocationInfo() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "received DeferredMTLRData location info");
          }
        }
      }

      if (lcsReferenceNumber <= Integer.MAX_VALUE && lcsReferenceNumber>=0) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received GeranPositioningDataInformation");
        }
        slrRequestValues.setLcsReferenceNumber(lcsReferenceNumber);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setLcsReferenceNumber(slrRequestValues.getLcsReferenceNumber());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, LCS Reference Number set");
          }
        }
      }

      if (geranPositioningData != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received GeranPositioningDataInformation");
        }
        slrRequestValues.setGeranPositioningDataInformation(geranPositioningData);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setGeranPositioningDataInformation(slrRequestValues.getGeranPositioningDataInformation());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, GERAN Positining Data Info set");
          }
        }
      }

      if (utranPositioningDataInfo != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received UtranPositioningDataInformation");
        }
        slrRequestValues.setUtranPositioningDataInfo(utranPositioningDataInfo);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setUtranPositioningDataInfo(slrRequestValues.getUtranPositioningDataInfo());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, GERAN Positining Data Info set");
          }
        }
      }

      if (cellGlobalIdOrServiceAreaIdOrLAI != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received cellGlobalIdOrServiceAreaIdOrLAI");
        }
        slrRequestValues.setCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdOrLAI);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setCellGlobalIdOrServiceAreaIdOrLAI(slrRequestValues.getCellGlobalIdOrServiceAreaIdOrLAI());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, CGI or SAI or LAI set");
          }
        }
        if (cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "received CellGlobalIdOrServiceAreaIdFixedLength on cellGlobalIdOrServiceAreaIdOrLAI");
          }
        } else if (cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength() != null) {
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "received CellGlobalIdOrServiceAreaIdFixedLength on cellGlobalIdOrServiceAreaIdOrLAI");
          }
        }
      }

      if (hgmlcAddress != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received HGMLCAddress parameter");
        }
        slrRequestValues.sethGmlcAddress(hgmlcAddress);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.sethGmlcAddress(slrRequestValues.gethGmlcAddress());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, H-GMLC Address set");
          }
        }
      }

      if (lcsServiceTypeID <= Integer.MAX_VALUE && lcsServiceTypeID>0) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received LCSServiceTypeID parameter");
        }
        slrRequestValues.setLcsServiceTypeID(lcsServiceTypeID);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setLcsServiceTypeID(slrRequestValues.getLcsServiceTypeID());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, LCS Service Type ID set");
          }
        }
      }

      if (saiPresent != true) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received SAIPresent parameter");
        }
      }

      if (pseudonymIndicator != true) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received PseudonymIndicator parameter");
        }
        slrRequestValues.setPseudonymIndicator(pseudonymIndicator);
      }

      if (accuracyFulfilmentIndicator != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received AccuracyFulfilmentIndicator");
        }
        slrRequestValues.setAccuracyFulfilmentIndicator(accuracyFulfilmentIndicator);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setAccuracyFulfilmentIndicator(slrRequestValues.getAccuracyFulfilmentIndicator());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Accuracy Fulfilment Indicator set");
          }
        }
      }

      if (velocityEstimate != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received VelocityEstimate");
        }
        slrRequestValues.setVelocityEstimate(velocityEstimate);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setVelocityEstimate(slrRequestValues.getVelocityEstimate());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Velocity Estimate set");
          }
        }
      }

      if (sequenceNumber <= Integer.MAX_VALUE) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received SequenceNumber");
        }
        slrRequestValues.setSequenceNumber(sequenceNumber);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setSequenceNumber(slrRequestValues.getSequenceNumber());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Sequence Number set");
          }
        }
      }

      if (periodicLDRInfo != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received periodicLDRInfo");
        }
        slrRequestValues.setPeriodicLDRInfo(periodicLDRInfo);
      }

      if (moLrShortCircuitIndicator != false) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received MoLrShortCircuitIndicator");
        }
        slrRequestValues.setMoLrShortCircuitIndicator(moLrShortCircuitIndicator);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setPeriodicLDRInfo(slrRequestValues.getPeriodicLDRInfo());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, Periodic LDR Info set");
          }
        }
      }

      if (geranGANSSpositioningData != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received GeranGANSSpositioningData");
        }
        slrRequestValues.setGeranGANSSpositioningData(geranGANSSpositioningData);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setGeranGANSSpositioningData(slrRequestValues.getGeranGANSSpositioningData());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, GERAN GANSS Positioning Data set");
          }
        }
      }

      if (utranGANSSpositioningData != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received UtranGANSSpositioningData");
        }
        slrRequestValues.setUtranGANSSpositioningData(utranGANSSpositioningData);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setUtranGANSSpositioningData(slrRequestValues.getUtranGANSSpositioningData());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, UTRAN GANSS Positioning Data set");
          }
        }
      }

      if (servingNodeAddress != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSubscriberLocationReportRequest: received ServingNodeAddress");
        }
        slrRequestValues.setServingNodeAddress(servingNodeAddress);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setServingNodeAddress(slrRequestValues.getServingNodeAddress());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSubscriberLocationReportRequest: "
                    + "CDR state is initialized, UTRAN GANSS Positioning Data set");
          }
        }
      }

      // SLR CDR creation
      if (lcsEvent != null || lcsClientID != null || lcsLocationInfo != null || extGeographicalInformation != null || geranPositioningData != null || utranPositioningDataInfo != null || geranPositioningData != null
              || utranPositioningDataInfo != null || addGeographicalInformation != null || deferredmtlrData != null || cellGlobalIdOrServiceAreaIdOrLAI != null
              || velocityEstimate != null || geranGANSSpositioningData != null || utranGANSSpositioningData != null || servingNodeAddress != null
              || periodicLDRInfo != null) {
        if (gmlcCdrState.isInitialized()) {
          this.createCDRRecord(RecordStatus.SLR_SUCCESS);
        }
      } else {
        if (gmlcCdrState.isInitialized()) {
          if (mapErrorMessage.getErrorCode() == 52)
            this.createCDRRecord(RecordStatus.SLR_UNAUTHORIZED_REQUESTING_NETWORK);
          if (mapErrorMessage.getErrorCode() == 58)
            this.createCDRRecord(RecordStatus.SLR_UNKNOWN_OR_UNREACHABLE_LCS_CLIENT);
          if (mapErrorMessage.getErrorCode() == 59)
            this.createCDRRecord(RecordStatus.SLR_MM_EVENT_NOT_SUPPORTED);
          if (mapErrorMessage.getErrorCode() == 54)
            this.createCDRRecord(RecordStatus.SLR_POSITION_METHOD_FAILURE);
          if (mapErrorMessage.getErrorCode() == 51)
            this.createCDRRecord(RecordStatus.SLR_RESOURCE_LIMITATION);
          if (mapErrorMessage.getErrorCode() == 34)
            this.createCDRRecord(RecordStatus.SLR_SYSTEM_FAILURE);
          if (mapErrorMessage.getErrorCode() == 35)
            this.createCDRRecord(RecordStatus.SLR_DATA_MISSING);
          if (mapErrorMessage.getErrorCode() == 36)
            this.createCDRRecord(RecordStatus.SLR_UNEXPECTED_DATA_VALUE);
          if (mapErrorMessage.getErrorCode() == 21)
            this.createCDRRecord(RecordStatus.SLR_FACILITY_NOT_SUPPORTED);
          if (mapErrorMessage.getErrorCode() == 27)
            this.createCDRRecord(RecordStatus.SLR_ABSENT_SUBSCRIBER);
          if (mapErrorMessage.getErrorCode() == 12)
            this.createCDRRecord(RecordStatus.SLR_ILLEGAL_EQUIPMENT);
          if (mapErrorMessage.getErrorCode() == 5)
            this.createCDRRecord(RecordStatus.PSL_UNIDENTIFIED_SUBSCRIBER);
          if (mapErrorMessage.getErrorCode() == 1)
            this.createCDRRecord(RecordStatus.SLR_UNKNOWN_SUBSCRIBER);
        }
      }

      this.setSubscriberLocationReportRequest(event);
      if(this.getSubscriberLocationReportRequest() != null) {

        MAPExtensionContainer mapExtensionContainer = null;

        mapDialogLsmSlr.addSubscriberLocationReportResponse(event.getInvokeId(),naESRD, naESRD, mapExtensionContainer);

        // SubscriberLocationReportResponse is now composed by values taken from SubscriberLocationReportRequest and ready to be sent:
        mapDialogLsmSlr.close(false);

        // Handle successful retrieval of subscriber's location report request (SLR request) info by sending HTTP POST back to the requestor
        httpSubscriberLocationReport.Perform(HttpReport.HttpMethod.POST, lcsReferenceNumber);

      }


    } catch (MAPException me) {
      logger.severe(String.format("MAPException while trying to process onSubscriberLocationReportRequest=%s", event), me);
      logger.severe("MAP error message when processing onSubscriberLocationReportRequest: " + mapErrorMessage);
      this.handleLsmLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null, null, null,
              "Internal failure occurred while processing network response: " + me.getMessage());
    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onSubscriberLocationReportRequest=%s", event), e);
      this.handleLsmLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null, null, null,
              "Internal failure occurred while processing network response: " + e.getMessage());
    }
  }

  /**
   * MAP SLR Response Event
   */
  public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse event, ActivityContextInterface aci) {

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onSubscriberLocationReportResponse = " + event);
      }

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onSubscriberLocationReportResponse=%s", event), e);
    }

  }

  /**
   * Subscriber Information Services (another way to get location information via MAP)
   * MAP-SEND-ROUTING-INFO-FOR-SM (SRI) Events
   */
  public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest event, ActivityContextInterface aci) {
    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onSendRoutingInfoForSmRequest = " + event);
      }

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onSendRoutingInfoForSmRequest=%s", event), e);
    }
  }

  public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse event, ActivityContextInterface aci) {

    MAPErrorMessage mapErrorMessage = this.getErrorResponse();

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onSendRoutingInfoForSmResponse = " + event);
      }
      MAPDialog mapDialogSriForSM = event.getMAPDialog();
      IMSI imsi = event.getIMSI();
      LocationInfoWithLMSI locationInfoWithLMSI = event.getLocationInfoWithLMSI();
      MAPExtensionContainer mapExtensionContainer = event.getExtensionContainer();

      SriForSmResponseValues sriForSmResponseValues = new SriForSmResponseValues();
      MLPResponse.MLPResultType mlpRespResult = null;
      String mlpClientErrorMessage = null;

      // CDR initialization stuff
      CDRInterface cdrInterface = this.getCDRInterface();
      GMLCCDRState gmlcCdrState = cdrInterface.getState();
      if (!gmlcCdrState.isInitialized()) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForSmResponse: CDR state is NOT initialized: " + gmlcCdrState + ", initiating\n");
        }
        gmlcCdrState.init(mapDialogSriForSM.getLocalDialogId(), mapDialogSriForSM.getReceivedDestReference(), mapDialogSriForSM.getReceivedOrigReference(),
                locationInfoWithLMSI.getNetworkNodeNumber(), mapDialogSriForSM.getLocalAddress(), mapDialogSriForSM.getRemoteAddress());
        gmlcCdrState.setDialogStartTime(DateTime.now());
        gmlcCdrState.setRemoteDialogId(mapDialogSriForSM.getRemoteDialogId());
        cdrInterface.setState(gmlcCdrState);

        // attach, in case impl wants to use more of dialog.
        SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
        aci.attach(sbbLO);
      }
      // Set timer last
      this.setTimer(aci);

      if (locationInfoWithLMSI != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForSmResponse: received LocationInfoWithLMSI parameter");
        }
        if (locationInfoWithLMSI.getNetworkNodeNumber() != null) {
          sriForSmResponseValues.setNetworkNodeNumber(locationInfoWithLMSI.getNetworkNodeNumber());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setNetworkNodeNumber(sriForSmResponseValues.getNetworkNodeNumber());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForSmResponse: CDR state is initialized, NNN set");
            }
          }
        }
        if (locationInfoWithLMSI.getAdditionalNumber() != null) {
          sriForSmResponseValues.setAdditionalNumber(locationInfoWithLMSI.getAdditionalNumber());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setAdditionalNumber(sriForSmResponseValues.getAdditionalNumber());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForSmResponse: CDR state is initialized, Additional Number set");
            }
          }
        }
        if (locationInfoWithLMSI.getLMSI() != null) {
          sriForSmResponseValues.setLmsi(locationInfoWithLMSI.getLMSI());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setLmsi(sriForSmResponseValues.getLmsi());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForSmResponse: CDR state is initialized, LMSI set");
            }
          }
        }
        if (locationInfoWithLMSI.getGprsNodeIndicator() != false && locationInfoWithLMSI.getGprsNodeIndicator() != true) {
          sriForSmResponseValues.setGprsNodeIndicator(locationInfoWithLMSI.getGprsNodeIndicator());
          if (gmlcCdrState.isInitialized()) {
            gmlcCdrState.setGprsNodeIndicator(sriForSmResponseValues.isGprsNodeIndicator());
            if (this.logger.isFineEnabled()) {
              this.logger.fine("\nonSendRoutingInfoForSmResponse: CDR state is initialized, GPRS Node Indicator set");
            }
          }
        }
      }

      if (imsi != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonSendRoutingInfoForSmResponse: received IMSI parameter");
        }
        sriForSmResponseValues.setImsi(imsi);
        if (gmlcCdrState.isInitialized()) {
          gmlcCdrState.setImsi(sriForSmResponseValues.getImsi());
          if (this.logger.isFineEnabled()) {
            this.logger.fine("\nonSendRoutingInfoForSmResponse: CDR state is initialized, IMSI set");
          }
        }
      }

      this.setSendRoutingInfoForSMResponse(event);

      if (this.getSendRoutingInfoForSMResponse() != null) {

        LMSI lmsi = null;
        if (locationInfoWithLMSI.getLMSI() != null)
          lmsi = event.getLocationInfoWithLMSI().getLMSI();
        RequestedInfo requestedInfo = new RequestedInfoImpl(true, true, null, true, null, true, false, false);

        AddressString origAddressString, destAddressString1;
        origAddressString = destAddressString1 = null;

        MAPDialogMobility mapDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(
                this.getMAPPsiApplicationContext(), this.getGmlcSccpAddress(), origAddressString,
                getNNNSCCPAddress(locationInfoWithLMSI.getNetworkNodeNumber().getAddress()), destAddressString1);

        mapDialogMobility.addProvideSubscriberInfoRequest(imsi, lmsi, requestedInfo, mapExtensionContainer, null);

        // Keep ACI in across MAP dialog for PSL
        ActivityContextInterface lsmPslDialogACI = this.mapAcif.getActivityContextInterface(mapDialogMobility);
        lsmPslDialogACI.attach(this.sbbContext.getSbbLocalObject());

        // ProvideSubscriberInfoRequest is now composed by values taken from SRIforSM response
        // Send PSI
        mapDialogMobility.send();
      } else {
        if (mapErrorMessage != null) {
          // SRIforSM error CDR creation
          if (gmlcCdrState.isInitialized()) {
            if (mapErrorMessage.getErrorCode() == 34)
              this.createCDRRecord(RecordStatus.SRISM_SYSTEM_FAILURE);
            if (mapErrorMessage.getErrorCode() == 35)
              this.createCDRRecord(RecordStatus.SRISM_DATA_MISSING);
            if (mapErrorMessage.getErrorCode() == 36)
              this.createCDRRecord(RecordStatus.SRISM_UNEXPECTED_DATA_VALUE);
            if (mapErrorMessage.getErrorCode() == 21)
              this.createCDRRecord(RecordStatus.SRISM_FACILITY_NOT_SUPPORTED);
            if (mapErrorMessage.getErrorCode() == 27)
              this.createCDRRecord(RecordStatus.SRISM_ABSENT_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 28)
              this.createCDRRecord(RecordStatus.SRISM_INCOMPATIBLE_TERMINAL);
            if (mapErrorMessage.getErrorCode() == 51)
              this.createCDRRecord(RecordStatus.SRISM_RESOURCE_LIMITATION);
            if (mapErrorMessage.getErrorCode() == 1)
              this.createCDRRecord(RecordStatus.SRISM_UNKNOWN_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 3)
              this.createCDRRecord(RecordStatus.SRISM_UNKNOWN_MSC);
            if (mapErrorMessage.getErrorCode() == 4)
              this.createCDRRecord(RecordStatus.SRISM_NUMBER_CHANGED);
            if (mapErrorMessage.getErrorCode() == 5)
              this.createCDRRecord(RecordStatus.SRISM_UNIDENTIFIED_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 7)
              this.createCDRRecord(RecordStatus.SRISM_UNKNOWN_EQUIPMENT);
            if (mapErrorMessage.getErrorCode() == 8)
              this.createCDRRecord(RecordStatus.SRISM_ROMAING_NOT_ALLOWED);
            if (mapErrorMessage.getErrorCode() == 9)
              this.createCDRRecord(RecordStatus.SRISM_ILLEGAL_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 10)
              this.createCDRRecord(RecordStatus.SRISM_BEARER_SERVICE_NOT_PROVISIONED);
            if (mapErrorMessage.getErrorCode() == 11)
              this.createCDRRecord(RecordStatus.SRISM_TELESERVICE_NOT_PROVISIONED);
            if (mapErrorMessage.getErrorCode() == 12)
              this.createCDRRecord(RecordStatus.SRISM_ILLEGAL_EQUIPMENT);
          }
        }
      }

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onSendRoutingInformationForSmRequest=%s", event), e);
    }
  }

  /**
   * MAP-PROVIDE-SUBSCRIBER-INFO (PSI) Events
   */
  public void onProvideSubscriberInformationRequest(ProvideSubscriberInfoRequest event, ActivityContextInterface aci) {
    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onProvideSubscriberInformationRequest = " + event);
      }

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onProvideSubscriberInformationRequest=%s", event), e);
    }
  }

  public void onProvideSubscriberInformationResponse(ProvideSubscriberInfoResponse event, ActivityContextInterface aci) {

    MAPErrorMessage mapErrorMessage = this.getErrorResponse();
    this.setProvideSubscriberInformationResponse(event);

    try {
      if (this.logger.isFineEnabled()) {
        this.logger.fine("\nReceived onProvideSubscriberInformationResponse = " + event);
      }
      MAPDialogMobility mapDialogMobility = event.getMAPDialog();
      SubscriberInfo subscriberInfo = event.getSubscriberInfo();
      MAPExtensionContainer mapExtensionContainer = event.getExtensionContainer();

      PsiResponseValues psiResponseValues = new PsiResponseValues();
      MLPResponse.MLPResultType mlpRespResult = null;
      String mlpClientErrorMessage = null;

      // CDR initialization stuff
      CDRInterface cdrInterface = this.getCDRInterface();
      GMLCCDRState gmlcCdrState = cdrInterface.getState();
      if (!gmlcCdrState.isInitialized()) {
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberInformationResponse: CDR state is NOT initialized: " + gmlcCdrState + ", initiating\n");
        }
        gmlcCdrState.init(mapDialogMobility.getLocalDialogId(), mapDialogMobility.getReceivedDestReference(), mapDialogMobility.getReceivedOrigReference(),
                null, mapDialogMobility.getLocalAddress(), mapDialogMobility.getRemoteAddress());
        gmlcCdrState.setDialogStartTime(DateTime.now());
        gmlcCdrState.setRemoteDialogId(mapDialogMobility.getRemoteDialogId());
        cdrInterface.setState(gmlcCdrState);

        // attach, in case impl wants to use more of dialog.
        SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
        aci.attach(sbbLO);
      }
      // Set timer last
      this.setTimer(aci);

      if (subscriberInfo != null) {
        mlpRespResult = MLPResponse.MLPResultType.OK;
        if (this.logger.isFineEnabled()) {
          this.logger.fine("\nonProvideSubscriberInformationResponse: "
                  + "received subscriberInfo, decoding parameters");
          if (subscriberInfo.getLocationInformation() != null) {
            psiResponseValues.setLocationInformation(subscriberInfo.getLocationInformation());
            if (gmlcCdrState.isInitialized()) {
              gmlcCdrState.setLocationInformation(psiResponseValues.getLocationInformation());
              if (this.logger.isFineEnabled()) {
                this.logger.fine("\nonProvideSubscriberInformationResponse: "
                        + "CDR state is initialized, Location Information set");
              }
            }
          }
          // Inquire if subscriber state is included in MAP PSI response subscriber's info
          if (subscriberInfo.getSubscriberState() != null) {
            mlpRespResult = MLPResponse.MLPResultType.OK;
            // Subscriber state is included in MAP PSI response, get it and store it as a response parameter
            psiResponseValues.setSubscriberState(subscriberInfo.getSubscriberState());
            //atiResponse.subscriberState = subscriberInfo.getSubscriberState().getSubscriberStateChoice().toString();
            if (gmlcCdrState.isInitialized()) {
              gmlcCdrState.setSubscriberState(psiResponseValues.getSubscriberState().getSubscriberStateChoice().toString());
              if (subscriberInfo.getLocationInformation() == null) {
                if (this.logger.isFineEnabled()) {
                  this.logger.fine("\nonProvideSubscriberInformationResponse: "
                          + "CDR state is initialized, ATI_STATE_SUCCESS");
                }
                this.createCDRRecord(RecordStatus.PSI_STATE_SUCCESS);
              }
            }
          }
          // Inquire if Location information is included in MAP PSI response subscriber's info
          if (subscriberInfo.getLocationInformation() != null) {
            psiResponseValues.setLocationInformation(subscriberInfo.getLocationInformation());
            mlpRespResult = MLPResponse.MLPResultType.OK;
            // Location information is included in MAP PSI response, then
            // Inquire if Cell Global Identity (CGI) or Service Area Identity (SAI) or Location Area Identity (LAI) are included in MAP PSI response
            if (subscriberInfo.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI() != null) {
              // CGI or SAI or LAI are included in MAP PSI response
              CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = subscriberInfo.getLocationInformation()
                      .getCellGlobalIdOrServiceAreaIdOrLAI();
              // Inquire and get parameters of CGI or SAI or LAI included in MAP PSI response
              if (cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
                if (this.logger.isFineEnabled()) {
                  this.logger.fine("\nonProvideSubscriberInformationResponse: "
                          + "received CellGlobalIdOrServiceAreaIdFixedLength, decoding MCC, MNC, LAC, CI");
                }
                psiResponseValues.setCellGlobalIdOrServiceAreaIdFixedLength(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength());
                try {
                  psiResponseValues.setMcc(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
                  psiResponseValues.setMnc(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
                  psiResponseValues.setLac(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
                  psiResponseValues.setCi(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode());
                } catch (MAPException e1) {
                  e1.printStackTrace();
                }
                // Inquire if Age of Location Information is included in MAP PSI response subscriber's info
                if (subscriberInfo.getLocationInformation().getAgeOfLocationInformation() != null) {
                  psiResponseValues.setAgeOfLocationInfo(subscriberInfo.getLocationInformation().getAgeOfLocationInformation().intValue());
                  gmlcCdrState.setAgeOfLocationEstimate(subscriberInfo.getLocationInformation().getAgeOfLocationInformation().intValue());
                }
                if (gmlcCdrState.isInitialized()) {
                  if (this.logger.isFineEnabled()) {
                    this.logger.fine("\nonProvideSubscriberInformationResponse: "
                            + "CDR state is initialized, PSI_LOCATION_SUCCESS");
                  }
                  try {
                    gmlcCdrState.setMcc(psiResponseValues.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
                    gmlcCdrState.setMnc(psiResponseValues.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
                    gmlcCdrState.setLac(psiResponseValues.getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
                    gmlcCdrState.setCi(psiResponseValues.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode());
                  } catch (MAPException e1) {
                    e1.printStackTrace();
                  }
                  gmlcCdrState.setAol(psiResponseValues.getAgeOfLocationInfo());
                  gmlcCdrState.setAtiVlrGt(psiResponseValues.getVlrNumber());
                  if (gmlcCdrState.getSubscriberState() != null) {
                    this.createCDRRecord(RecordStatus.PSI_CGI_AND_STATE_SUCCESS);
                  } else {
                    this.createCDRRecord(RecordStatus.PSI_CGI_SUCCESS);
                  }
                }
              } else if (cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength() != null) {
                // Case when LAI length is fixed
                if (this.logger.isFineEnabled()) {
                  this.logger.fine("\nonProvideSubscriberInformationResponse: "
                          + "received laiFixedLength, decoding MCC, MNC, LAC (no CI)");
                }
                psiResponseValues.setMcc(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMCC());
                psiResponseValues.setMcc(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMNC());
                psiResponseValues.setLac(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getLac());
                if (gmlcCdrState.isInitialized()) {
                  if (this.logger.isFineEnabled()) {
                    this.logger.fine("\nonProvideSubscriberInformationResponse: "
                            + "CDR state is initialized, PSI_LAI_SUCCESS");
                  }
                  gmlcCdrState.setMcc(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMCC());
                  gmlcCdrState.setMnc(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMNC());
                  gmlcCdrState.setLac(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getLac());
                  if (gmlcCdrState.getSubscriberState() != null) {
                    this.createCDRRecord(RecordStatus.PSI_LAI_AND_STATE_SUCCESS);
                  } else {
                    this.createCDRRecord(RecordStatus.PSI_LAI_SUCCESS);
                  }
                }
              }
            }
            // Inquire if VLR number (Global Title) is included in MAP PSI response subscriber's info
            if (subscriberInfo.getLocationInformation().getVlrNumber() != null) {
              psiResponseValues.setVlrNumber(subscriberInfo.getLocationInformation().getVlrNumber());
              if (gmlcCdrState.isInitialized()) {
                if (this.logger.isFineEnabled()) {
                  this.logger.fine("\nonProvideSubscriberInformationResponse: "
                          + "CDR state is initialized, PSI_LOC_SUCCESS");
                }
                gmlcCdrState.setPsiVlrNumber(subscriberInfo.getLocationInformation().getVlrNumber());
                if (gmlcCdrState.getLocationInformation().getVlrNumber() != null)
                  this.createCDRRecord(RecordStatus.PSI_LOC_SUCCESS);
              }
            }
            // Inquire if MSC number (Global Title) is included in MAP PSI response subscriber's info
            if (subscriberInfo.getLocationInformation().getMscNumber() != null) {
              psiResponseValues.setMscNumber(subscriberInfo.getLocationInformation().getMscNumber());
              if (gmlcCdrState.isInitialized()) {
                if (this.logger.isFineEnabled()) {
                  this.logger.fine("\nonProvideSubscriberInformationResponse: "
                          + "CDR state is initialized, PSI_LOC_SUCCESS");
                }
                gmlcCdrState.setPsiMscNumber(subscriberInfo.getLocationInformation().getMscNumber());
                if (gmlcCdrState.getLocationInformation().getMscNumber() != null)
                  this.createCDRRecord(RecordStatus.PSI_LOC_SUCCESS);
              }
            }
            // Inquire if subscriber Geographical information is included in MAP PSI response subscriber's info
            if (subscriberInfo.getLocationInformation().getGeographicalInformation() != null) {
              // Geographical information is included in MAP PSI response
              if (this.logger.isFineEnabled()) {
                this.logger.fine("\nonProvideSubscriberInformationResponse: "
                        + "received Geographical information, decoding latitude and longitude");
              }
              double latitude = subscriberInfo.getLocationInformation().getGeographicalInformation().getLatitude();
              double longitude = subscriberInfo.getLocationInformation().getGeographicalInformation().getLongitude();
              psiResponseValues.setLatitude(latitude);
              psiResponseValues.setLongitude(longitude);
              if (gmlcCdrState.isInitialized()) {
                if (this.logger.isFineEnabled()) {
                  this.logger.fine("\nonProvideSubscriberInformationResponse: "
                          + "CDR state is initialized, PSI_LOC_SUCCESS");
                }
                gmlcCdrState.setPsiLatitude(subscriberInfo.getLocationInformation().getGeographicalInformation().getLatitude());
                gmlcCdrState.setPsiLongitude(subscriberInfo.getLocationInformation().getGeographicalInformation().getLongitude());
                if (gmlcCdrState.getLocationInformation().getGeographicalInformation() != null)
                  this.createCDRRecord(RecordStatus.PSI_GEO_SUCCESS);
              }
            }
          }
        }
      } else {
        if (mapErrorMessage != null) {
          // PSI error CDR creation
          if (gmlcCdrState.isInitialized()) {
            if (mapErrorMessage.getErrorCode() == 34)
              this.createCDRRecord(RecordStatus.PSI_SYSTEM_FAILURE);
            if (mapErrorMessage.getErrorCode() == 35)
              this.createCDRRecord(RecordStatus.PSI_DATA_MISSING);
            if (mapErrorMessage.getErrorCode() == 36)
              this.createCDRRecord(RecordStatus.PSI_UNEXPECTED_DATA_VALUE);
            if (mapErrorMessage.getErrorCode() == 21)
              this.createCDRRecord(RecordStatus.PSI_FACILITY_NOT_SUPPORTED);
            if (mapErrorMessage.getErrorCode() == 27)
              this.createCDRRecord(RecordStatus.PSI_ABSENT_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 28)
              this.createCDRRecord(RecordStatus.PSI_INCOMPATIBLE_TERMINAL);
            if (mapErrorMessage.getErrorCode() == 51)
              this.createCDRRecord(RecordStatus.PSI_RESOURCE_LIMITATION);
            if (mapErrorMessage.getErrorCode() == 1)
              this.createCDRRecord(RecordStatus.PSI_UNKNOWN_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 3)
              this.createCDRRecord(RecordStatus.PSI_UNKNOWN_MSC);
            if (mapErrorMessage.getErrorCode() == 4)
              this.createCDRRecord(RecordStatus.PSI_NUMBER_CHANGED);
            if (mapErrorMessage.getErrorCode() == 5)
              this.createCDRRecord(RecordStatus.PSI_UNIDENTIFIED_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 7)
              this.createCDRRecord(RecordStatus.PSI_UNKNOWN_EQUIPMENT);
            if (mapErrorMessage.getErrorCode() == 8)
              this.createCDRRecord(RecordStatus.PSI_ROMAING_NOT_ALLOWED);
            if (mapErrorMessage.getErrorCode() == 9)
              this.createCDRRecord(RecordStatus.PSI_ILLEGAL_SUBSCRIBER);
            if (mapErrorMessage.getErrorCode() == 10)
              this.createCDRRecord(RecordStatus.PSI_BEARER_SERVICE_NOT_PROVISIONED);
            if (mapErrorMessage.getErrorCode() == 11)
              this.createCDRRecord(RecordStatus.PSI_TELESERVICE_NOT_PROVISIONED);
            if (mapErrorMessage.getErrorCode() == 12)
              this.createCDRRecord(RecordStatus.PSI_ILLEGAL_EQUIPMENT);
          }
        }
      }

      // handle successful retrieval of PSI response
      handlePsiResponse(MLPResponse.MLPResultType.OK, psiResponseValues, mlpClientErrorMessage);

    } catch (Exception e) {
      logger.severe(String.format("Error while trying to process onProvideSubscriberInformationResponse=%s", event), e);
    }
  }

  /**
   * DIALOG Events
   */
  public void onDialogTimeout(DialogTimeout event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nRx :  onDialogTimeout " + event);
    }
    this.handleDialogError(MLPResponse.MLPResultType.SYSTEM_FAILURE, "Dialog Timeout: " + event);
    MAPDialog mapDialog = event.getMAPDialog();
    CDRInterface cdrInterface = this.getCDRInterface();
    GMLCCDRState gmlcCdrState = cdrInterface.getState();
    gmlcCdrState.init(mapDialog.getLocalDialogId(), mapDialog.getReceivedDestReference(), mapDialog.getReceivedOrigReference(),
            null, mapDialog.getLocalAddress(), mapDialog.getRemoteAddress());
    gmlcCdrState.setDialogStartTime(DateTime.now());
    gmlcCdrState.setRemoteDialogId(mapDialog.getRemoteDialogId());
    cdrInterface.setState(gmlcCdrState);
    SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
    aci.attach(sbbLO);
    this.setTimer(aci);
    if (gmlcCdrState.isInitialized()) {
      this.createCDRRecord(RecordStatus.TCAP_DIALOG_TIMEOUT);
    }
  }

  public void onDialogDelimiter(DialogDelimiter event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nReceived onDialogDelimiter = " + event);
    }
  }

  public void onDialogAccept(DialogAccept event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nReceived onDialogAccept = " + event);
    }
  }

  public void onDialogReject(DialogReject event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nRx :  onDialogReject " + event);
    }
    this.handleDialogError(MLPResponse.MLPResultType.SYSTEM_FAILURE, "Dialog Rejected: " + event);
    MAPDialog mapDialog = event.getMAPDialog();
    CDRInterface cdrInterface = this.getCDRInterface();
    GMLCCDRState gmlcCdrState = cdrInterface.getState();
    gmlcCdrState.init(mapDialog.getLocalDialogId(), mapDialog.getReceivedDestReference(), mapDialog.getReceivedOrigReference(),
            null, mapDialog.getLocalAddress(), mapDialog.getRemoteAddress());
    gmlcCdrState.setDialogStartTime(DateTime.now());
    gmlcCdrState.setRemoteDialogId(mapDialog.getRemoteDialogId());
    cdrInterface.setState(gmlcCdrState);
    SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
    aci.attach(sbbLO);
    this.setTimer(aci);
    if (gmlcCdrState.isInitialized()) {
      this.createCDRRecord(RecordStatus.TCAP_DIALOG_REJECTED);
    }
  }

  public void onDialogUserAbort(DialogUserAbort event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nRx :  onDialogUserAbort " + event);
    }
    this.handleDialogError(MLPResponse.MLPResultType.SYSTEM_FAILURE, "Dialog U-ABORT: " + event);
    MAPDialog mapDialog = event.getMAPDialog();
    CDRInterface cdrInterface = this.getCDRInterface();
    GMLCCDRState gmlcCdrState = cdrInterface.getState();
    gmlcCdrState.init(mapDialog.getLocalDialogId(), mapDialog.getReceivedDestReference(), mapDialog.getReceivedOrigReference(),
            null, mapDialog.getLocalAddress(), mapDialog.getRemoteAddress());
    gmlcCdrState.setDialogStartTime(DateTime.now());
    gmlcCdrState.setRemoteDialogId(mapDialog.getRemoteDialogId());
    cdrInterface.setState(gmlcCdrState);
    SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
    aci.attach(sbbLO);
    this.setTimer(aci);
    if (gmlcCdrState.isInitialized()) {
      this.createCDRRecord(RecordStatus.TCAP_DIALOG_USER_ABORT);
    }
  }

  public void onDialogProviderAbort(DialogProviderAbort event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nRx :  onDialogProviderAbort " + event);
    }
    this.handleDialogError(MLPResponse.MLPResultType.SYSTEM_FAILURE, "Dialog P-ABORT: " + event);
    MAPDialog mapDialog = event.getMAPDialog();
    CDRInterface cdrInterface = this.getCDRInterface();
    GMLCCDRState gmlcCdrState = cdrInterface.getState();
    gmlcCdrState.init(mapDialog.getLocalDialogId(), mapDialog.getReceivedDestReference(), mapDialog.getReceivedOrigReference(),
            null, mapDialog.getLocalAddress(), mapDialog.getRemoteAddress());
    gmlcCdrState.setDialogStartTime(DateTime.now());
    gmlcCdrState.setRemoteDialogId(mapDialog.getRemoteDialogId());
    cdrInterface.setState(gmlcCdrState);
    SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
    aci.attach(sbbLO);
    this.setTimer(aci);
    if (gmlcCdrState.isInitialized()) {
      this.createCDRRecord(RecordStatus.TCAP_DIALOG_PROVIDER_ABORT);
    }
  }

  public void onDialogClose(DialogClose event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nReceived onDialogClose = " + event);
    }
  }

  public void onDialogNotice(DialogNotice event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nReceived onDialogNotice = " + event);
    }
  }

  public void onDialogRelease(DialogRelease event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nReceived onDialogRelease = " + event);
    }
  }

  /**
   * Component Events
   */
  public void onInvokeTimeout(InvokeTimeout event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nReceived onInvokeTimeout = " + event);
    } else {
      this.logger.severe("\nReceived onInvokeTimeout = " + event);
    }
    this.handleDialogError(MLPResponse.MLPResultType.SYSTEM_FAILURE, "Invoke timeout: " + event);
    MAPDialog mapDialog = event.getMAPDialog();
    CDRInterface cdrInterface = this.getCDRInterface();
    GMLCCDRState gmlcCdrState = cdrInterface.getState();
    gmlcCdrState.init(mapDialog.getLocalDialogId(), mapDialog.getReceivedDestReference(), mapDialog.getReceivedOrigReference(),
            null, mapDialog.getLocalAddress(), mapDialog.getRemoteAddress());
    gmlcCdrState.setDialogStartTime(DateTime.now());
    gmlcCdrState.setRemoteDialogId(mapDialog.getRemoteDialogId());
    cdrInterface.setState(gmlcCdrState);
    SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
    aci.attach(sbbLO);
    this.setTimer(aci);
    if (gmlcCdrState.isInitialized()) {
      this.createCDRRecord(RecordStatus.FAILED_INVOKE_TIMEOUT);
    }
  }

  public void onErrorComponent(ErrorComponent event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nReceived onErrorComponent = " + event);
    } else {
      this.logger.severe("\nReceived onErrorComponent = " + event);
    }

    MAPErrorMessage mapErrorMessage = event.getMAPErrorMessage();
    long error_code = mapErrorMessage.getErrorCode().longValue();

    this.handleDialogError(
            (error_code == MAPErrorCode.unknownSubscriber ? MLPResponse.MLPResultType.UNKNOWN_SUBSCRIBER
                    : MLPResponse.MLPResultType.SYSTEM_FAILURE), "Component error: " + String.valueOf(error_code) + " : "
                    + event.getMAPErrorMessage());
    MAPDialog mapDialog = event.getMAPDialog();
    CDRInterface cdrInterface = this.getCDRInterface();
    GMLCCDRState gmlcCdrState = cdrInterface.getState();
    gmlcCdrState.init(mapDialog.getLocalDialogId(), mapDialog.getReceivedDestReference(), mapDialog.getReceivedOrigReference(),
            null, mapDialog.getLocalAddress(), mapDialog.getRemoteAddress());
    gmlcCdrState.setDialogStartTime(DateTime.now());
    gmlcCdrState.setRemoteDialogId(mapDialog.getRemoteDialogId());
    cdrInterface.setState(gmlcCdrState);
    SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
    aci.attach(sbbLO);
    this.setTimer(aci);
    if (gmlcCdrState.isInitialized()) {
      this.createCDRRecord(RecordStatus.FAILED_MAP_ERROR_COMPONENT);
    }
  }

  public void onRejectComponent(RejectComponent event, ActivityContextInterface aci) {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("\nRx : onRejectComponent " + event);
    } else {
      this.logger.severe("\nRx : onRejectComponent " + event);
    }
    this.handleDialogError(MLPResponse.MLPResultType.SYSTEM_FAILURE, "RejectedComponent: " + event);
    MAPDialog mapDialog = event.getMAPDialog();
    CDRInterface cdrInterface = this.getCDRInterface();
    GMLCCDRState gmlcCdrState = cdrInterface.getState();
    gmlcCdrState.init(mapDialog.getLocalDialogId(), mapDialog.getReceivedDestReference(), mapDialog.getReceivedOrigReference(),
            null, mapDialog.getLocalAddress(), mapDialog.getRemoteAddress());
    gmlcCdrState.setDialogStartTime(DateTime.now());
    gmlcCdrState.setRemoteDialogId(mapDialog.getRemoteDialogId());
    cdrInterface.setState(gmlcCdrState);
    SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
    aci.attach(sbbLO);
    this.setTimer(aci);
    if (gmlcCdrState.isInitialized()) {
      this.createCDRRecord(RecordStatus.FAILED_MAP_REJECT_COMPONENT);
    }
  }

  /**
   * Handle HTTP POST request
   *
   * @param event
   * @param aci
   * @param eventContext
   */
  public void onPost(HttpServletRequestEvent event, ActivityContextInterface aci, EventContext eventContext) {
    onRequest(event, aci, eventContext);
  }

  /**
   * Handle HTTP GET request
   *
   * @param event
   * @param aci
   * @param eventContext
   */
  public void onGet(HttpServletRequestEvent event, ActivityContextInterface aci, EventContext eventContext) {
    onRequest(event, aci, eventContext);
  }

  /**
   * Entry point for all location lookups
   * Assigns a protocol handler to the request based on the path
   */
  private void onRequest(HttpServletRequestEvent event, ActivityContextInterface aci, EventContext eventContext) throws IllegalArgumentException {

    setEventContext(eventContext);
    HttpServletRequest httpServletRequest = event.getRequest();
    HttpRequestType httpRequestType = HttpRequestType.fromPath(httpServletRequest.getPathInfo());
    String requestingMLP, requestingMSISDN, coreNetwork, inputIllegalArgument="";

    switch (httpRequestType) {
      case REST: {
        requestingMSISDN = httpServletRequest.getParameter("msisdn");
        try {
          coreNetwork = httpServletRequest.getParameter("coreNetwork");
          if (coreNetwork == null) {
            coreNetwork = "gsm";
          }

          this.pslLcsPriority = httpServletRequest.getParameter("priority");
          if (pslLcsPriority == null) {
            pslLcsPriority = String.valueOf(LCSPriority.normalPriority);
          } else if (!pslLcsPriority.equalsIgnoreCase("normalPriority") && !pslLcsPriority.equalsIgnoreCase("highestPriority")) {
            inputIllegalArgument = "priority argument must be normalPriority or highestPriority";
            throw new IllegalArgumentException();
          }

          this.pslLcsHorizontalAccuracy = httpServletRequest.getParameter("horizontalAccuracy");
          if (pslLcsHorizontalAccuracy == null) {
            pslLcsHorizontalAccuracy = "999999";
          }

          this.pslLcsVerticalAccuracy = httpServletRequest.getParameter("verticalAccuracy");
          if (pslLcsVerticalAccuracy == null) {
            pslLcsVerticalAccuracy = "999999";
          }

          this.pslVerticalCoordinateRequest   = httpServletRequest.getParameter("vertCoordinateRequest");
          if (pslVerticalCoordinateRequest == null) {
            pslVerticalCoordinateRequest = String.valueOf(false);
          } else if (!pslVerticalCoordinateRequest.equalsIgnoreCase("true") && !pslVerticalCoordinateRequest.equalsIgnoreCase("false")) {
            logger.info("");
            inputIllegalArgument = "vertCoordinateRequest argument must be true or false";
            throw new IllegalArgumentException();
          }

          this.pslResponseTimeCategory = httpServletRequest.getParameter("responseTimeCategory");
          if (pslResponseTimeCategory == null) {
            pslResponseTimeCategory = String.valueOf(ResponseTimeCategory.delaytolerant);
          } else if (!pslResponseTimeCategory.equalsIgnoreCase("delaytolerant") && !pslResponseTimeCategory.equalsIgnoreCase("lowdelay")) {
            inputIllegalArgument = "responseTimeCategory argument must be delaytolerant or lowdelay";
            throw new IllegalArgumentException();
          }

          this.pslLocationEstimateType = httpServletRequest.getParameter("locationEstimateType");
          if (pslLocationEstimateType == null) {
            pslLocationEstimateType = String.valueOf(LocationEstimateType.currentOrLastKnownLocation);
          } else if (!pslLocationEstimateType.equalsIgnoreCase("currentOrLastKnownLocation") && !pslLocationEstimateType.equalsIgnoreCase("initialLocation")
                  && !pslLocationEstimateType.equalsIgnoreCase("currentLocation") && !pslLocationEstimateType.equalsIgnoreCase("activateDeferredLocation")
                  && !pslLocationEstimateType.equalsIgnoreCase("cancelDeferredLocation")) {
            inputIllegalArgument = "locationEstimateType argument must be one of currentLocation, currentOrLastKnownLocation, initialLocation, activateDeferredLocation or cancelDeferredLocation";
            throw new IllegalArgumentException();
          }

          this.pslDeferredLocationEventType = httpServletRequest.getParameter("deferredLocationEventType");
          if (pslDeferredLocationEventType == null) {
            pslDeferredLocationEventType = "available";
          } else if (!pslDeferredLocationEventType.equalsIgnoreCase("inside") && !pslDeferredLocationEventType.equalsIgnoreCase("entering")
                  && !pslDeferredLocationEventType.equalsIgnoreCase("leaving") && !pslDeferredLocationEventType.equalsIgnoreCase("available")) {
            inputIllegalArgument = "deferredLocationEventType argument must be one of available, inside, entering or leaving";
            throw new IllegalArgumentException();
          }

          this.pslAreaType = httpServletRequest.getParameter("areaType");
          if (pslAreaType == null) {
            pslAreaType = String.valueOf(AreaType.cellGlobalId);
          } else if (!pslAreaType.equalsIgnoreCase("locationAreaId") && !pslAreaType.equalsIgnoreCase("cellGlobalId")
                  && !pslAreaType.equalsIgnoreCase("countryCode") && !pslAreaType.equalsIgnoreCase("plmnId") &&
                  !pslAreaType.equalsIgnoreCase("routingAreaId") && !pslAreaType.equalsIgnoreCase("utranCellId")) {
            inputIllegalArgument = "areaType argument must be one of locationAreaId, cellGlobalId, countryCode, plmnId, routingAreaId or utranCellId";
            throw new IllegalArgumentException();
          }

          this.pslAreaId = httpServletRequest.getParameter("areaId");
          if (pslAreaId == null) {
            pslAreaId = "9999999";
          }

          this.pslOccurrenceInfo = httpServletRequest.getParameter("occurrenceInfo");
          if (pslOccurrenceInfo == null) {
            pslOccurrenceInfo = String.valueOf(OccurrenceInfo.oneTimeEvent);
          } else if (!pslOccurrenceInfo.equalsIgnoreCase("oneTimeEvent") && !pslOccurrenceInfo.equalsIgnoreCase("multipleTimeEvent")) {
            inputIllegalArgument = "occurrenceInfo argument must be oneTimeEvent or multipleTimeEvent";
            throw new IllegalArgumentException();
          }

          this.pslLcsReferenceNumber = httpServletRequest.getParameter("lcsReferenceNumber");
          if (pslLcsReferenceNumber == null) {
            pslLcsReferenceNumber = "0";
          }

          this.pslLcsServiceTypeID =  httpServletRequest.getParameter("lcsServiceTypeID");
          if (pslLcsServiceTypeID == null) {
            pslLcsServiceTypeID = "1";
          }

          this.pslIntervalTime =  httpServletRequest.getParameter("intervalTime");
          if (pslIntervalTime == null) {
            pslIntervalTime = "999999";
          }

          this.pslReportingAmount =  httpServletRequest.getParameter("reportingAmount");
          if (pslReportingAmount == null) {
            pslReportingAmount = "0";
          }

          this.pslReportingInterval =  httpServletRequest.getParameter("reportingInterval");
          if (pslReportingInterval == null) {
            pslReportingInterval = "999999";
          }

          this.slrCallbackUrl = httpServletRequest.getParameter("slrCallbackUrl");
          if (this.slrCallbackUrl == null) {
            slrCallbackUrl = "http://localhost:8080";
          }

          this.psiService = httpServletRequest.getParameter("psiService");
          if (psiService == null) {
            psiService = "false";
          }

        } catch (IllegalArgumentException iae) {
          iae.printStackTrace();
          handleLsmLocationResponse(MLPResponse.MLPResultType.FORMAT_ERROR, null, null, null, "Failure: " + inputIllegalArgument);
          return;
        } catch (Exception e) {
          e.printStackTrace();
          handleLsmLocationResponse(MLPResponse.MLPResultType.FORMAT_ERROR, null, null, null, "System Failure: Failed to read from server input stream");
          return;
        }

      }
      break;
      case MLP:
        try {
          // Get the XML request from the POST data
          InputStream body = httpServletRequest.getInputStream();
          // Parse the request and retrieve the requested MSISDN and coreNetwork
          MLPRequest mlpRequest = new MLPRequest(logger);
          requestingMLP = mlpRequest.parseRequest(body);
          String[] output = requestingMLP.split(";");
          requestingMSISDN = output[0];
          coreNetwork = output[1];
          this.pslLcsPriority =  output[2];
          this.pslLcsHorizontalAccuracy= output[3];
          this.pslLcsVerticalAccuracy = output[4];
          this.pslVerticalCoordinateRequest  = output[5];
          this.pslResponseTimeCategory = output[6];
          this.pslLocationEstimateType = output[7];
          this.pslDeferredLocationEventType = output[8];
          this.pslAreaType = output[9];
          this.pslAreaId = output[10];
          this.pslOccurrenceInfo = output[11];
          this.pslLcsReferenceNumber = output[12];
          this.pslLcsServiceTypeID  = output[13];
          this.pslIntervalTime = output[14];
          this.pslReportingAmount = output[15];
          this.pslReportingInterval = output[16];
          this.slrCallbackUrl = output[17];
          this.psiService = output[18];

        } catch (MLPException e) {
          handleLsmLocationResponse(e.getMlpClientErrorType(), null, null, null, "System Failure: " + e.getMlpClientErrorMessage());
          return;
        } catch (IOException e) {
          e.printStackTrace();
          handleLsmLocationResponse(MLPResponse.MLPResultType.FORMAT_ERROR, null, null, null, "System Failure: Failed to read from server input stream");
          return;
        }
        break;
      default:
        sendHTTPResult(HttpServletResponse.SC_NOT_FOUND, "Request URI unsupported");
        return;
    }

    setHttpRequest(new HttpRequest(httpRequestType, requestingMSISDN, coreNetwork, pslLcsPriority, Integer.parseInt(pslLcsHorizontalAccuracy),
            Integer.parseInt(pslLcsVerticalAccuracy), pslVerticalCoordinateRequest, pslResponseTimeCategory, Integer.parseInt(pslLcsReferenceNumber),
            Integer.parseInt(pslLcsServiceTypeID), pslAreaType, pslAreaId, pslOccurrenceInfo, Integer.parseInt(pslIntervalTime),
            Integer.parseInt(pslReportingAmount), Integer.parseInt(pslReportingInterval), slrCallbackUrl, psiService));

    if (logger.isFineEnabled()) {
      logger.fine(String.format("Handling %s request, MSISDN: %s from %s", httpRequestType.name().toUpperCase(), requestingMSISDN, coreNetwork));
    }

    if (requestingMSISDN != null) {
      eventContext.suspendDelivery();
      setEventContextCMP(eventContext);
      if (psiService.equalsIgnoreCase("true")) {
        // TODO getMsisdnCGIthruPSI(requestingMSISDN)
        getLocationViaSubscriberInformation(requestingMSISDN);
        //handlePsiNotSupportedYet(MLPResponse.MLPResultType.SYSTEM_FAILURE, "PSI not supported yet", aci);
      } else {
        if (coreNetwork.equalsIgnoreCase("UMTS")) {
          getMsisdnGeolocationViaLsm(requestingMSISDN);
        } else {
          getMsisdnCellGlobalId(requestingMSISDN);
        }
      }


    } else {
      logger.info("MSISDN is null, sending back -1 for Global Cell Identity");
      handleLsmLocationResponse(MLPResponse.MLPResultType.FORMAT_ERROR, null, null, null, "Invalid MSISDN specified");
    }
  }

  /**
   * CMP
   */
  public abstract void setEventContext(EventContext eventContext);

  public abstract EventContext getEventContext();

  public abstract void setEventContextCMP(EventContext eventContext);

  public abstract EventContext getEventContextCMP();

  public abstract void setHttpRequest(HttpRequest httpRequest);

  public abstract HttpRequest getHttpRequest();

  public abstract void setTimerID(TimerID value);

  public abstract TimerID getTimerID();

  public abstract void setGMLCCDRState(GMLCCDRState gmlcSdrState);

  public abstract GMLCCDRState getGMLCCDRState();

  public abstract void setSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInfoForLCSResponse);

  public abstract SendRoutingInfoForLCSResponse getSendRoutingInfoForLCSResponse();

  public abstract void setProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse provideSubscriberLocationResponse);

  public abstract ProvideSubscriberLocationResponse getProvideSubscriberLocationResponse();

  public abstract void setSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequest);

  public abstract SubscriberLocationReportRequest getSubscriberLocationReportRequest();

  public abstract void setSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMResponse);

  public abstract SendRoutingInfoForSMResponse getSendRoutingInfoForSMResponse();

  public abstract void setProvideSubscriberInformationResponse(ProvideSubscriberInfoResponse subscriberLocationReportRequest);

  public abstract ProvideSubscriberInfoResponse getProvideSubscriberInformationResponse();

  public abstract void setErrorResponse(MAPErrorMessage errorResponse);

  public abstract MAPErrorMessage getErrorResponse();

  /**
   * Private helper methods
   */

  /**
   * Retrieve the location for the specified MSISDN via MAP ATI
   */
  private void getMsisdnCellGlobalId(String requestingMSISDN) {

    if (!requestingMSISDN.equals(fakeNumber)) {
      try {
        AddressString origAddressString, destAddressString1;
        origAddressString = destAddressString1 = null;
        MAPDialogMobility mapDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(
                this.getMAPAtiApplicationContext(), this.getGmlcSccpAddress(), origAddressString,
                getHlrSCCPAddress(requestingMSISDN), destAddressString1);
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, requestingMSISDN);
        SubscriberIdentity subscriberIdentity = new SubscriberIdentityImpl(msisdn);
        boolean locationInformation = true;
        boolean subscriberState = true;
        MAPExtensionContainer mapExtensionContainer = null;
        boolean currentLocation = false;
        DomainType requestedDomain = null;
        boolean imei = false;
        boolean msClassmark = false;
        boolean mnpRequestedInfo = false;
        RequestedInfo requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, mapExtensionContainer, currentLocation,
                requestedDomain, imei, msClassmark, mnpRequestedInfo);
        // requestedInfo (MAP ATI):
        // locationInformation: true (response includes mcc, mnc, lac, cellid, aol, vlrNumber)
        // subscriberState: true (response can be assumedIdle, camelBusy, networkDeterminedNotReachable, notProvidedFromVlr)
        // Rest of params are null or not requested
        ISDNAddressString gscmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN,
                gmlcPropertiesManagement.getGmlcGt());

        mapDialogMobility.addAnyTimeInterrogationRequest(subscriberIdentity, requestedInfo, gscmSCFAddress, mapExtensionContainer);

        ActivityContextInterface atiDialogACI = this.mapAcif.getActivityContextInterface(mapDialogMobility);
        atiDialogACI.attach(this.sbbContext.getSbbLocalObject());
        mapDialogMobility.send();

      } catch (MAPException e) {
        this.logger.severe("MAPException while trying to send MAP ATI request for MSISDN=" + requestingMSISDN, e);
        this.handleLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null,
                "System Failure: Failed to send request to network for position: " + e.getMessage());
      } catch (Exception e) {
        this.logger.severe("Exception while trying to send MAP ATI request for MSISDN=" + requestingMSISDN, e);
        this.handleLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null,
                "System Failure: Failed to send request to network for position: " + e.getMessage());
      }

    } else {
      // Handle fake location type given fake number set as MSISDN
      if (this.fakeLocationType == MLPResponse.MLPResultType.OK) {
        AtiResponseValues response = new AtiResponseValues();
        try {
          response.setCi(fakeCellId);
          response.setMcc(Integer.parseInt(fakeLocationX));
          response.setMcc(Integer.parseInt(fakeLocationY));
          response.setLac(Integer.parseInt(fakeLocationRadius));
        } catch (Exception e) {
          logger.severe("MAP Exception while assigning ATI response values for MLP fake location:" +e);
        }

        String mlpClientErrorMessage = null;
        this.handleLocationResponse(MLPResponse.MLPResultType.OK, response, mlpClientErrorMessage);

      } else {
        AtiResponseValues response;
        response = null;
        this.handleLocationResponse(this.fakeLocationType, response, this.fakeLocationAdditionalInfoErrorString);
      }
    }
  }


  /**
   * Retrieve the core network location for the specified MSISDN via MAP LSM operations
   */
  private void getMsisdnGeolocationViaLsm(String requestingMSISDN) {

    if (!requestingMSISDN.equals(fakeNumber)) {
      try {
        AddressString origAddressString, destAddressString1;
        origAddressString = destAddressString1 = null;
        MAPDialogLsm mapDialogLsmSRIforLCS = this.mapProvider.getMAPServiceLsm().createNewDialog(
                this.getMAPSRIforLCSApplicationContext(),  this.getGmlcSccpAddress(), origAddressString,
                getHlrSCCPAddress(requestingMSISDN), destAddressString1);
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, requestingMSISDN);
        SubscriberIdentity subscriberIdentity = new SubscriberIdentityImpl(msisdn);
        ISDNAddressString gmlcAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, this.getGmlcSccpAddress().getGlobalTitle().getDigits());
        MAPExtensionContainer mapExtensionContainer = null;
        mapDialogLsmSRIforLCS.addSendRoutingInfoForLCSRequest(gmlcAddress, subscriberIdentity, mapExtensionContainer);
        // Create the ACI and attach this SBB
        ActivityContextInterface sriForLcsDialogACI = this.mapAcif.getActivityContextInterface(mapDialogLsmSRIforLCS);
        sriForLcsDialogACI.attach(this.sbbContext.getSbbLocalObject());
        mapDialogLsmSRIforLCS.send();

      } catch (MAPException e) {
        this.logger.severe("MAPException while trying to send MAP SRIforLCS request for MSISDN=" + requestingMSISDN, e);
        this.handleLsmLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null, null, null,
                "System Failure: Failed to send request to network for position: " + e.getMessage());
      } catch (Exception e) {
        this.logger.severe("Exception while trying to send MAP SRIforLCS request for MSISDN=" + requestingMSISDN, e);
        this.handleLsmLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null, null, null,
                "System Failure: Failed to send request to network for position: " + e.getMessage());
      }

    } else {
      // Handle fake location type given fake number set as MSISDN
      if (this.fakeLocationType == MLPResponse.MLPResultType.OK) {
        PslResponseValues response = new PslResponseValues();
        final CellGlobalIdOrServiceAreaIdFixedLength cellFake = new CellGlobalIdOrServiceAreaIdFixedLength() {
          @Override
          public byte[] getData() { return new byte[0]; }
          @Override
          public int getMCC() { return 0; }
          @Override
          public int getMNC()  { return 0; }
          @Override
          public int getLac() { return 0; }
          @Override
          public int getCellIdOrServiceAreaCode() { return fakeCellId; }
        };
        CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAI() {
          @Override
          public CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalIdOrServiceAreaIdFixedLength() { return cellFake; }
          @Override
          public LAIFixedLength getLAIFixedLength() { return null; }
        };
        response.setCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdOrLAI);
        ExtGeographicalInformation extGeographicalInformation = new ExtGeographicalInformation() {
          @Override
          public byte[] getData() { return new byte[0]; }
          @Override
          public TypeOfShape getTypeOfShape() { return null; }
          @Override
          public double getLatitude() { return Double.parseDouble(fakeLocationX); }
          @Override
          public double getLongitude() { return Double.parseDouble(fakeLocationY); }
          @Override
          public double getUncertainty() { return 0; }
          @Override
          public double getUncertaintySemiMajorAxis() { return 0; }
          @Override
          public double getUncertaintySemiMinorAxis() { return 0; }
          @Override
          public double getAngleOfMajorAxis() { return 0; }
          @Override
          public int getConfidence() { return 0; }
          @Override
          public int getAltitude() { return 0; }
          @Override
          public double getUncertaintyAltitude() { return 0; }
          @Override
          public int getInnerRadius() { return Integer.valueOf(fakeLocationRadius); }
          @Override
          public double getUncertaintyRadius() { return 0; }
          @Override
          public double getOffsetAngle() { return 0; }
          @Override
          public double getIncludedAngle() { return 0; }
        };
        response.setLocationEstimate(extGeographicalInformation);
        String mlpClientErrorMessage = null;
        this.handleLsmLocationResponse(MLPResponse.MLPResultType.OK, null, response, null, mlpClientErrorMessage);

      } else {
        SriForLcsResponseValues response;
        response = null;
        this.handleLsmLocationResponse(this.fakeLocationType, response, null, null, this.fakeLocationAdditionalInfoErrorString);
      }
    }
  }

  /**
   * Retrieve the location for the specified MSISDN via MAP ATI
   */
  private void getLocationViaSubscriberInformation(String requestingMSISDN) {

    if (!requestingMSISDN.equals(fakeNumber)) {
      try {
        AddressString origAddressString, destAddressString1;
        origAddressString = destAddressString1 = null;
        MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(
                this.getMAPSRIforSMApplicationContext(), this.getGmlcSccpAddress(), origAddressString,
                getHlrSCCPAddress(requestingMSISDN), destAddressString1);
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, requestingMSISDN);
        boolean sm_RP_PRI = true;
        AddressString serviceCentreAddressString = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, gmlcPropertiesManagement.getGmlcGt());
        mapDialogSms.addSendRoutingInfoForSMRequest(msisdn, sm_RP_PRI, serviceCentreAddressString,
                null, false, null, null, null);
        // Create the ACI and attach this SBB
        ActivityContextInterface sriDialogACI = this.mapAcif.getActivityContextInterface(mapDialogSms);
        sriDialogACI.attach(this.sbbContext.getSbbLocalObject());

        // Send SRIforSM
        mapDialogSms.send();

      } catch (MAPException e) {
        this.logger.severe("MAPException while trying to send MAP ATI request for MSISDN=" + requestingMSISDN, e);
        this.handleLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null,
                "System Failure: Failed to send request to network for position: " + e.getMessage());
      } catch (Exception e) {
        this.logger.severe("Exception while trying to send MAP ATI request for MSISDN=" + requestingMSISDN, e);
        this.handleLocationResponse(MLPResponse.MLPResultType.SYSTEM_FAILURE, null,
                "System Failure: Failed to send request to network for position: " + e.getMessage());
      }

    } else {
      // Handle fake location type given fake number set as MSISDN
      if (this.fakeLocationType == MLPResponse.MLPResultType.OK) {
        AtiResponseValues response = new AtiResponseValues();
        try {
          response.setCi(fakeCellId);
          response.setMcc(Integer.parseInt(fakeLocationX));
          response.setMcc(Integer.parseInt(fakeLocationY));
          response.setLac(Integer.parseInt(fakeLocationRadius));
        } catch (Exception e) {
          logger.severe("MAP Exception while assigning ATI response values for MLP fake location:" +e);
        }

        String mlpClientErrorMessage = null;
        this.handleLocationResponse(MLPResponse.MLPResultType.OK, response, mlpClientErrorMessage);

      } else {
        AtiResponseValues response;
        response = null;
        this.handleLocationResponse(this.fakeLocationType, response, this.fakeLocationAdditionalInfoErrorString);
      }
    }
  }


  protected SccpAddress getGmlcSccpAddress() {

    if (this.gmlcSCCPAddress == null) {
      int translationType = 0; // Translation Type = 0 : Unknown
      EncodingScheme encodingScheme = null;
      GlobalTitle gt = sccpParameterFact.createGlobalTitle(gmlcPropertiesManagement.getGmlcGt(), translationType,
              NumberingPlan.ISDN_TELEPHONY, encodingScheme, NatureOfAddress.INTERNATIONAL);
      this.gmlcSCCPAddress = sccpParameterFact.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
              gt, translationType, gmlcPropertiesManagement.getGmlcSsn());

//			GlobalTitle0100 gt = new GlobalTitle0100Impl(gmlcPropertiesManagement.getGmlcGt(),0,BCDEvenEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY,NatureOfAddress.INTERNATIONAL);
//			this.serviceCenterSCCPAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, gmlcPropertiesManagement.getGmlcSsn());
    }
    return this.gmlcSCCPAddress;
  }

  private MAPApplicationContext getMAPAtiApplicationContext() {

    if (this.anyTimeEnquiryContext == null) {
      this.anyTimeEnquiryContext = MAPApplicationContext.getInstance(
              MAPApplicationContextName.anyTimeEnquiryContext, MAPApplicationContextVersion.version3);
    }
    return this.anyTimeEnquiryContext;
  }

  private MAPApplicationContext getMAPSRIforLCSApplicationContext() {
    if (this.locationSvcGatewayContext == null) {
      this.locationSvcGatewayContext = MAPApplicationContext.getInstance(
              MAPApplicationContextName.locationSvcGatewayContext, MAPApplicationContextVersion.version3);
    }
    return this.locationSvcGatewayContext;
  }

  private MAPApplicationContext getMAPPslSlrApplicationContext() {
    if (this.locationSvcEnquiryContext == null) {
      this.locationSvcEnquiryContext = MAPApplicationContext.getInstance(
              MAPApplicationContextName.locationSvcEnquiryContext, MAPApplicationContextVersion.version3);
    }
    return this.locationSvcEnquiryContext;
  }

  private MAPApplicationContext getMAPSRIforSMApplicationContext() {
    if (this.shortMsgGatewayContext == null) {
      this.shortMsgGatewayContext = MAPApplicationContext.getInstance(
              MAPApplicationContextName.shortMsgGatewayContext, MAPApplicationContextVersion.version3);
    }
    return this.shortMsgGatewayContext;
  }

  private MAPApplicationContext getMAPPsiApplicationContext() {
    if (this.subscriberInfoEnquiryContext == null) {
      this.subscriberInfoEnquiryContext = MAPApplicationContext.getInstance(
              MAPApplicationContextName.subscriberInfoEnquiryContext, MAPApplicationContextVersion.version3);
    }
    return this.subscriberInfoEnquiryContext;
  }

  private SccpAddress getHlrSCCPAddress(String address) {

    int translationType = 0; // Translation Type = 0 : Unknown
    EncodingScheme encodingScheme = null;
    GlobalTitle gt = sccpParameterFact.createGlobalTitle(address, translationType, NumberingPlan.ISDN_TELEPHONY, encodingScheme,
            NatureOfAddress.INTERNATIONAL);
    return sccpParameterFact.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, translationType,
            gmlcPropertiesManagement.getHlrSsn());

//	    GlobalTitle0100 gt = new GlobalTitle0100Impl(address, 0, BCDEvenEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL);
//		return new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, gmlcPropertiesManagement.getHlrSsn());
  }

  protected ISDNAddressString getCalledPartyISDNAddressString(String destinationAddress, int ton, int npi) {
    return this.mapParameterFactory.createISDNAddressString(AddressNature.getInstance(ton),
            org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan.getInstance(npi), destinationAddress);
  }

  private SccpAddress getNNNSCCPAddress(String address) {

    int translationType = 0; // Translation Type = 0 : Unknown
    EncodingScheme encodingScheme = null;
    GlobalTitle gt = sccpParameterFact.createGlobalTitle(address, translationType, NumberingPlan.ISDN_TELEPHONY, encodingScheme,
            NatureOfAddress.INTERNATIONAL);
    return sccpParameterFact.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, translationType,
            gmlcPropertiesManagement.getMscSsn());

  }

  /**
   * Handle generating the appropriate HTTP response
   * We're making use of the MLPResponse class for both GET/POST requests for convenience and
   * because eventually the GET method will likely be removed
   *
   * @param mlpResultType         OK or error type to return to client
   * @param atiResponseValues     ATIResponse on location attempt
   * @param mlpClientErrorMessage Error message to send to client
   */
  protected void handleLocationResponse(MLPResponse.MLPResultType mlpResultType, AtiResponseValues atiResponseValues, String mlpClientErrorMessage) {

    HttpRequest request = getHttpRequest();
    EventContext httpEventContext = this.resumeHttpEventContext();

    switch (request.type) {
      case REST:
        if (mlpResultType == MLPResponse.MLPResultType.OK && httpEventContext != null) {

          HttpServletRequestEvent httpRequest = (HttpServletRequestEvent) httpEventContext.getEvent();
          HttpServletResponse httpServletResponse = httpRequest.getResponse();
          httpServletResponse.setStatus(HttpServletResponse.SC_OK);
          int mcc = -1;
          int mnc = -1;
          int lac = -1;
          int ci = -1;
          int ageOfLocationInfo;
          String vlrNumber;
          String subscriberState;

          StringBuilder atiResponseSb = new StringBuilder();
          atiResponseSb.append("mcc=");
          try {
            if (atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength() != null) {
              mcc = atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC();
              mnc = atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC();
              lac = atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac();
            } else if (atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
              mcc = atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC();
              mnc = atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC();
              lac = atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac();
              ci = atiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode();
            }
            ageOfLocationInfo = atiResponseValues.getLocationInformation().getAgeOfLocationInformation().intValue();
            vlrNumber = atiResponseValues.getLocationInformation().getVlrNumber().getAddress();
            subscriberState = atiResponseValues.getSubscriberState().getSubscriberStateChoice().toString();
            atiResponseSb.append(mcc);
            atiResponseSb.append(",mnc=");
            atiResponseSb.append(mnc);
            atiResponseSb.append(",lac=");
            atiResponseSb.append(lac);
            atiResponseSb.append(",cellid=");
            atiResponseSb.append(ci);
            atiResponseSb.append(",aol=");
            atiResponseSb.append(ageOfLocationInfo);
            atiResponseSb.append(",vlrNumber=");
            atiResponseSb.append(vlrNumber);
            atiResponseSb.append(",subscriberState=");
            atiResponseSb.append(subscriberState);
          } catch (MAPException me) {
            logger.severe("Map exception while retrieving ATI response values: "+me);
          } catch (Exception e) {
            logger.severe("Exception while retrieving ATI response values: " +e);
          }

          this.sendHTTPResult(httpServletResponse.SC_OK, atiResponseSb.toString());

        } else {
          this.sendHTTPResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, mlpClientErrorMessage);
        }
        break;

      case MLP:
        String svcResultXml;
        MLPResponse mlpResponse = new MLPResponse(this.logger);

        if (mlpResultType == MLPResponse.MLPResultType.OK) {
          MLPResponseParams atiResponse = new MLPResponseParams();
          svcResultXml = mlpResponse.getSinglePositionSuccessXML(atiResponse.x, atiResponse.y, atiResponse.radius, request.msisdn);
        } else if (MLPResponse.isSystemError(mlpResultType)) {
          svcResultXml = mlpResponse.getSystemErrorResponseXML(mlpResultType, mlpClientErrorMessage);
        } else {
          svcResultXml = mlpResponse.getPositionErrorResponseXML(request.msisdn, mlpResultType, mlpClientErrorMessage);
        }

        this.sendHTTPResult(HttpServletResponse.SC_OK, svcResultXml);
        break;
    }
  }

  /**
   * Handle generating the appropriate HTTP response
   * We're making use of the MLPResponse class for both GET/POST requests for convenience and
   * because eventually the GET method will likely be removed
   *
   * @param mlpResultType         OK or error type to return to client
   * @param sri                   SRIForLCS response on location attempt
   * @param psl                   PSL response on location attempt
   * @param slrReq                SLR request report after location attempt
   * @param mlpClientErrorMessage Error message to send to client
   */
  protected void handleLsmLocationResponse(MLPResponse.MLPResultType mlpResultType, SriForLcsResponseValues sri,
                                           PslResponseValues psl, SlrRequestValues slrReq, String mlpClientErrorMessage) {

    HttpRequest request = getHttpRequest();
    EventContext httpEventContext = this.resumeHttpEventContext();
    MLPResponseParams mapLsmResponse = new MLPResponseParams();

    switch (request.type) {
      case REST:
        if (mlpResultType == MLPResponse.MLPResultType.OK && httpEventContext != null) {

          HttpServletRequestEvent httpRequest = (HttpServletRequestEvent) httpEventContext.getEvent();
          HttpServletResponse httpServletResponse = httpRequest.getResponse();
          httpServletResponse.setStatus(HttpServletResponse.SC_OK);

          StringBuilder mapLsmResponseSb = new StringBuilder();

          try {
            if (sri != null) {
              // Render and send SRIforLCS response values
              mapLsmResponseSb.append("MAP Send Routing Info for LCS response. ");
              mapLsmResponseSb.append("MSISDN=");
              mapLsmResponseSb.append(sri.getMsisdn());
              mapLsmResponseSb.append("IMSI=");
              mapLsmResponseSb.append(sri.getImsi());
              if (sri.getNetworkNodeNumber() !=null) {
                mapLsmResponseSb.append(", NetworkNodeNumber=");
                mapLsmResponseSb.append(sri.getNetworkNodeNumber().getAddress());
              }
              mapLsmResponseSb.append(", GPRSNodeIndicator=");
              mapLsmResponseSb.append(sri.isGprsNodeIndicator());
              mapLsmResponseSb.append(", MMEName=");
              mapLsmResponseSb.append(sri.getMmeName());
              mapLsmResponseSb.append(", SGSNName=");
              mapLsmResponseSb.append(sri.getSgsnName());
              mapLsmResponseSb.append(", 3GPPAAAServerName=");
              mapLsmResponseSb.append(sri.getAaaServerName());
              mapLsmResponseSb.append(", H-GMLCAddress=");
              mapLsmResponseSb.append(sri.gethGmlcAddress());
              mapLsmResponseSb.append(", V-GMLCAddress=");
              mapLsmResponseSb.append(sri.getvGmlcAddress());
              mapLsmResponseSb.append(", PPRAddress=");
              mapLsmResponseSb.append(sri.getPprAddress());
            }

            if (psl != null) {
              // Render and send PSL response values
              mapLsmResponseSb.append("MAP Provide Subscriber Location response: ");
              if (psl.getLocationEstimate() != null) {
                mapLsmResponseSb.append("Location Estimate:");
                mapLsmResponseSb.append(" latitude=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getLatitude());
                mapLsmResponse.x = String.valueOf(psl.getLocationEstimate().getLatitude());
                mapLsmResponseSb.append(", longitude=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getLongitude());
                mapLsmResponse.y = String.valueOf(psl.getLocationEstimate().getLongitude());
                mapLsmResponseSb.append(", typeofShape=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getTypeOfShape());
                mapLsmResponseSb.append(", uncertainty=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getUncertainty());
                mapLsmResponseSb.append(", uncertaintySemiMajorAxis=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getUncertaintySemiMajorAxis());
                mapLsmResponseSb.append(", uncertaintySemiMinorAxis=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getUncertaintySemiMinorAxis());
                mapLsmResponseSb.append(", confidence=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getConfidence());
                mapLsmResponseSb.append(", altitude=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getAltitude());
                mapLsmResponseSb.append(", uncertaintyAltitude=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getUncertaintyAltitude());
                mapLsmResponseSb.append(", innerRadius=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getInnerRadius());
                mapLsmResponseSb.append(", uncertaintyRadius=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getUncertaintyRadius());
                mapLsmResponseSb.append(", offsetAngle=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getOffsetAngle());
                mapLsmResponseSb.append(", includedAngle=");
                mapLsmResponseSb.append(psl.getLocationEstimate().getIncludedAngle());
              }

              mapLsmResponseSb.append(", aol=");
              mapLsmResponseSb.append(psl.getAgeOfLocationEstimate());
              mapLsmResponseSb.append(", deferredMTLRresponseIndicator=");
              mapLsmResponseSb.append(psl.isDeferredMTLRResponseIndicator());
              mapLsmResponseSb.append(", MOLRShortCircuitIndicator=");
              mapLsmResponseSb.append(psl.isMoLrShortCircuitIndicator());
              mapLsmResponseSb.append(", CGIorSAIorLAI:");
              if (psl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength() != null) {
                mapLsmResponseSb.append(", mcc=");
                mapLsmResponseSb.append(psl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC());
                mapLsmResponseSb.append(", mnc=");
                mapLsmResponseSb.append(psl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC());
                mapLsmResponseSb.append(", lac=");
                mapLsmResponseSb.append(psl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac());
              } else if (psl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
                mapLsmResponseSb.append(", mcc=");
                mapLsmResponseSb.append(psl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
                mapLsmResponseSb.append(", mnc=");
                mapLsmResponseSb.append(psl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
                mapLsmResponseSb.append(", lac=");
                mapLsmResponseSb.append(psl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
                mapLsmResponseSb.append(", cellid=");
                mapLsmResponseSb.append(psl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode());
              }
              mapLsmResponseSb.append(", AccuracyFulfilmentIndicator=");
              mapLsmResponseSb.append(psl.getAccuracyFulfilmentIndicator());
            }

            if (slrReq != null) {
              // Render and send SLR request values (not repeating previously displayed values)
              mapLsmResponseSb.append("MAP Subscriber Location Report: ");
              if (slrReq.getLcsClientID() != null) {
                mapLsmResponseSb.append("ReportLCSClientID:");
                mapLsmResponseSb.append(", ReportLCSClientType=");
                mapLsmResponseSb.append(slrReq.getLcsClientID().getLCSClientType());
                mapLsmResponseSb.append(", ReportLCSClientExternalID=");
                mapLsmResponseSb.append(slrReq.getLcsClientID().getLCSClientExternalID());
                mapLsmResponseSb.append(", ReportLCSClientInternalID=");
                mapLsmResponseSb.append(slrReq.getLcsClientID().getLCSClientInternalID());
                mapLsmResponseSb.append(", ReportLCSClientName=");
                mapLsmResponseSb.append(slrReq.getLcsClientID().getLCSClientName());
                mapLsmResponseSb.append(", ReportLCSClientDialedByMS=");
                mapLsmResponseSb.append(slrReq.getLcsClientID().getLCSClientDialedByMS());
                mapLsmResponseSb.append(", ReportLCSAPN=");
                mapLsmResponseSb.append(slrReq.getLcsClientID().getLCSAPN());
                mapLsmResponseSb.append(", ReportLCSRequestorID=");
                mapLsmResponseSb.append(slrReq.getLcsClientID().getLCSRequestorID());
              }
              mapLsmResponseSb.append(", ReportLCSEvent=");
              mapLsmResponseSb.append(slrReq.getLcsEvent());
              mapLsmResponseSb.append(", ReportLCSReferenceNumber=");
              mapLsmResponseSb.append(slrReq.getLcsReferenceNumber());
              mapLsmResponseSb.append(", ReportLCSServiceTypeID=");
              mapLsmResponseSb.append(slrReq.getLcsServiceTypeID());
              if (slrReq.getLocationEstimate() != null) {
                mapLsmResponseSb.append(", ReportLocationEstimate:");
                mapLsmResponseSb.append(" ReportLatitude=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getLatitude());
                mapLsmResponseSb.append(", ReportLongitude=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getLongitude());
                mapLsmResponseSb.append(", ReportTypeofShape=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getTypeOfShape());
                mapLsmResponseSb.append(", ReportUncertainty=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getUncertainty());
                mapLsmResponseSb.append(", ReportUncertaintySemiMajorAxis=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getUncertaintySemiMajorAxis());
                mapLsmResponseSb.append(", ReportUncertaintySemiMinorAxis=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getUncertaintySemiMinorAxis());
                mapLsmResponseSb.append(", ReportConfidence=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getConfidence());
                mapLsmResponseSb.append(", ReportAltitude=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getAltitude());
                mapLsmResponseSb.append(", ReportUncertaintyAltitude=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getUncertaintyAltitude());
                mapLsmResponseSb.append(", ReportInnerRadius=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getInnerRadius());
                mapLsmResponseSb.append(", ReportUncertaintyRadius=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getUncertaintyRadius());
                mapLsmResponseSb.append(", ReportOffsetAngle=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getOffsetAngle());
                mapLsmResponseSb.append(", ReportIncludedAngle=");
                mapLsmResponseSb.append(slrReq.getLocationEstimate().getIncludedAngle());
              }
              if (slrReq.getLocationEstimate() != null) {
                mapLsmResponseSb.append(", ReportAdditionalLocationEstimate:");
                mapLsmResponseSb.append(" ReportAdditionalLatitude=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getLatitude());
                mapLsmResponseSb.append(", ReportAdditionalLongitude=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getLongitude());
                mapLsmResponseSb.append(", ReportAdditionalTypeofShape=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getTypeOfShape());
                mapLsmResponseSb.append(", ReportAdditionalUncertainty=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getUncertainty());
                mapLsmResponseSb.append(", ReportAdditionalUncertaintySemiMajorAxis=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getUncertaintySemiMajorAxis());
                mapLsmResponseSb.append(", ReportAdditionalUncertaintySemiMinorAxis=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getUncertaintySemiMinorAxis());
                mapLsmResponseSb.append(", ReportAdditionalConfidence=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getConfidence());
                mapLsmResponseSb.append(", ReportAdditionalAltitude=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getAltitude());
                mapLsmResponseSb.append(", ReportAdditionalUncertaintyAltitude=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getUncertaintyAltitude());
                mapLsmResponseSb.append(", ReportAdditionalInnerRadius=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getInnerRadius());
                mapLsmResponseSb.append(", ReportAdditionalUncertaintyRadius=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getUncertaintyRadius());
                mapLsmResponseSb.append(", ReportAdditionalOffsetAngle=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getOffsetAngle());
                mapLsmResponseSb.append(", ReportAdditionalIncludedAngle=");
                mapLsmResponseSb.append(slrReq.getAdditionalLocationEstimate().getIncludedAngle());
              }
              mapLsmResponseSb.append(", ReportAgeOfLocationEstimate=");
              mapLsmResponseSb.append(slrReq.getAgeOfLocationEstimate());
              mapLsmResponseSb.append(", ReportPseudonymIndicator=");
              mapLsmResponseSb.append(slrReq.getPseudonymIndicator());
              mapLsmResponseSb.append(", ReportMOLRShortCircuitIndicator=");
              mapLsmResponseSb.append(slrReq.isMoLrShortCircuitIndicator());
              mapLsmResponseSb.append(", ReportCGIorSAIorLAI:");
              if (slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength() != null) {
                mapLsmResponseSb.append(", ReportMCC=");
                mapLsmResponseSb.append(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC());
                mapLsmResponseSb.append(", ReportMNC=");
                mapLsmResponseSb.append(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC());
                mapLsmResponseSb.append(", ReportLAC=");
                mapLsmResponseSb.append(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac());
              } else if (slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
                mapLsmResponseSb.append(", ReportMCC=");
                mapLsmResponseSb.append(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
                mapLsmResponseSb.append(", ReportMNC=");
                mapLsmResponseSb.append(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
                mapLsmResponseSb.append(", ReportLAC=");
                mapLsmResponseSb.append(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
                mapLsmResponseSb.append(", ReportCellIdOrSai=");
                mapLsmResponseSb.append(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode());
              }
              mapLsmResponseSb.append(", ReportAccuracyFulfilmentIndicator=");
              mapLsmResponseSb.append(slrReq.getAccuracyFulfilmentIndicator());
              if (slrReq.getDeferredmtlrData() != null) {
                mapLsmResponseSb.append(", ReportDeferredMTLRData=");
                mapLsmResponseSb.append(", terminationCause=");
                mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getTerminationCause().getCause());
                if (slrReq.getDeferredmtlrData().getLCSLocationInfo() != null) {
                  if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getNetworkNodeNumber() != null) {
                    mapLsmResponseSb.append(", networkNodeNumber=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getNetworkNodeNumber().getAddress());
                  }
                  if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getLMSI() != null) {
                    mapLsmResponseSb.append(", LMSI=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getLMSI().getData().toString());
                  }
                  if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getMmeName() != null) {
                    mapLsmResponseSb.append(", MMEName=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getMmeName().getData().toString());
                  }
                  if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getAaaServerName() != null) {
                    mapLsmResponseSb.append(", AAAServerName=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getAaaServerName().getData().toString());
                  }
                  if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalNumber() != null) {
                    if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalNumber().getMSCNumber() != null) {
                      mapLsmResponseSb.append(", MSCNumber=");
                      mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalNumber().getMSCNumber().getAddress());
                    }
                    if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalNumber().getSGSNNumber() != null) {
                      mapLsmResponseSb.append(", SGSNNumber=");
                      mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalNumber().getSGSNNumber().getAddress());
                    }
                  }
                  if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getSupportedLCSCapabilitySets() != null) {
                    mapLsmResponseSb.append(", SupportedLCSCapabilitySets=");
                    mapLsmResponseSb.append(", LCSCapabilitySets98_99=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
                    mapLsmResponseSb.append(", LCSCapabilitySets4=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
                    mapLsmResponseSb.append(", LCSCapabilitySets5=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease5());
                    mapLsmResponseSb.append(", LCSCapabilitySets6=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease6());
                    mapLsmResponseSb.append(", LCSCapabilitySets47");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease7());
                  }
                  if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalLCSCapabilitySets() != null) {
                    mapLsmResponseSb.append(", AdditionalSupportedLCSCapabilitySets=");
                    mapLsmResponseSb.append(", AdditionalLCSCapabilitySets98_99=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease98_99());
                    mapLsmResponseSb.append(", AdditionalLCSCapabilitySets4=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease4());
                    mapLsmResponseSb.append(", AdditionalLCSCapabilitySets5=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease5());
                    mapLsmResponseSb.append(", AdditionalLCSCapabilitySets6=");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease6());
                    mapLsmResponseSb.append(", AdditionalLCSCapabilitySets47");
                    mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease7());
                  }
                  mapLsmResponseSb.append(", GPRSnodeIndicator=");
                  mapLsmResponseSb.append(slrReq.getDeferredmtlrData().getLCSLocationInfo().getGprsNodeIndicator());
                }
                if (slrReq.getPeriodicLDRInfo() != null) {
                  mapLsmResponseSb.append(", ReportPeriodicLDRInfo:");
                  mapLsmResponseSb.append(", reportingAmount=");
                  mapLsmResponseSb.append(slrReq.getPeriodicLDRInfo().getReportingAmount());
                  mapLsmResponseSb.append(", reportingInterval=");
                  mapLsmResponseSb.append(slrReq.getPeriodicLDRInfo().getReportingInterval());
                }
              }
              mapLsmResponseSb.append(", ReportSequenceNumber=");
              mapLsmResponseSb.append(slrReq.getSequenceNumber());
            }

          } catch (MAPException me) {
            logger.severe("MAP Exception while processing LSM operations for displaying via HTTP at handleLsmLocationResponse" +me);
          } catch (Exception e) {
            logger.severe("Exception while processing LSM operations for displaying via HTTP at handleLsmLocationResponse" +e);
          }

          this.sendHTTPResult(httpServletResponse.SC_OK, mapLsmResponseSb.toString());

        } else {
          this.sendHTTPResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, mlpClientErrorMessage);
        }
        break;

      case MLP:
        String svcResultXml;
        MLPResponse mlpResponse = new MLPResponse(this.logger);

        if (mlpResultType == MLPResponse.MLPResultType.OK) {
          String radius = "-1";
          svcResultXml = mlpResponse.getSinglePositionSuccessXML(mapLsmResponse.x, mapLsmResponse.y, radius, request.msisdn);
        } else if (MLPResponse.isSystemError(mlpResultType)) {
          svcResultXml = mlpResponse.getSystemErrorResponseXML(mlpResultType, mlpClientErrorMessage);
        } else {
          svcResultXml = mlpResponse.getPositionErrorResponseXML(request.msisdn, mlpResultType, mlpClientErrorMessage);
        }

        this.sendHTTPResult(HttpServletResponse.SC_OK, svcResultXml);
        break;
    }
  }

  /**
   * Handle generating the appropriate HTTP response
   * We're making use of the MLPResponse class for both GET/POST requests for convenience and
   * because eventually the GET method will likely be removed
   *
   * @param mlpResultType           OK or error type to return to client
   * @param psiResponseValues       PSI values on SPI attempt
   * @param mlpClientErrorMessage   Error message to send to client
   */
  protected void handlePsiResponse(MLPResponse.MLPResultType mlpResultType, PsiResponseValues psiResponseValues, String mlpClientErrorMessage) {

    HttpRequest request = getHttpRequest();
    EventContext httpEventContext = this.resumeHttpEventContext();

    switch (request.type) {
      case REST:
        if (mlpResultType == MLPResponse.MLPResultType.OK && httpEventContext != null) {

          HttpServletRequestEvent httpRequest = (HttpServletRequestEvent) httpEventContext.getEvent();
          HttpServletResponse httpServletResponse = httpRequest.getResponse();
          httpServletResponse.setStatus(HttpServletResponse.SC_OK);
          int mcc = -1;
          int mnc = -1;
          int lac = -1;
          int ci = -1;
          int ageOfLocationInfo = -1;
          String vlrNumber = "";
          String subscriberState = "";
          Double geographicalLatitude = 0.00;
          Double geographicalLongitude = 0.00;
          Double geographicalUncertainty = 0.00;
          Double geodeticLatitude = 0.00;
          Double geodeticLongitude = 0.00;
          Double geodeticUncertainty = 0.00;
          int geodeticConfidence = -1;
          int geodeticScreeningAndPresentationIndicators = -1;
          boolean saiPresent = false;
          boolean currentLocationRetrieved = false;
          String mscNumber = "";
          String mmeName = "";
          int mnpInfoResultNumberPortabilityStatus = -1;
          String mnpInfoResultMSISDN = "";
          String mnpInfoResultIMSI = "";
          String mnpInfoResultRouteingNumber = "";

          StringBuilder psiResponseSb = new StringBuilder();
          psiResponseSb.append("PSI response=");
          try {
            if (psiResponseValues.getLocationInformation() != null) {
              if (psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength() != null) {
                mcc = psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC();
                mnc = psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC();
                lac = psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac();
                psiResponseSb.append(", mcc=");
                psiResponseSb.append(mcc);
                psiResponseSb.append(",mnc=");
                psiResponseSb.append(mnc);
                psiResponseSb.append(",lac=");
                psiResponseSb.append(lac);
              } else if (psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
                mcc = psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC();
                mnc = psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC();
                lac = psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac();
                ci = psiResponseValues.getLocationInformation().getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode();
                psiResponseSb.append(", mcc=");
                psiResponseSb.append(mcc);
                psiResponseSb.append(",mnc=");
                psiResponseSb.append(mnc);
                psiResponseSb.append(",lac=");
                psiResponseSb.append(lac);
                psiResponseSb.append(",cellid=");
                psiResponseSb.append(ci);
              }
              if (psiResponseValues.getLocationInformation().getAgeOfLocationInformation() >= Integer.MIN_VALUE
                      && psiResponseValues.getLocationInformation().getAgeOfLocationInformation() <= Integer.MAX_VALUE) {
                ageOfLocationInfo = psiResponseValues.getLocationInformation().getAgeOfLocationInformation().intValue();
                psiResponseSb.append(",aol=");
                psiResponseSb.append(ageOfLocationInfo);
              }
              if (psiResponseValues.getLocationInformation().getVlrNumber() != null) {
                vlrNumber = psiResponseValues.getLocationInformation().getVlrNumber().getAddress();
                psiResponseSb.append(",vlrNumber=");
                psiResponseSb.append(vlrNumber);
              }
              if (psiResponseValues.getLocationInformation().getGeographicalInformation() != null) {
                geographicalLatitude = psiResponseValues.getLocationInformation().getGeographicalInformation().getLatitude();
                geographicalLongitude = psiResponseValues.getLocationInformation().getGeographicalInformation().getLongitude();
                geographicalUncertainty = psiResponseValues.getLocationInformation().getGeographicalInformation().getUncertainty();
                psiResponseSb.append(", geographicalLatitude=");
                psiResponseSb.append(geographicalLatitude);
                psiResponseSb.append(", geographicalLongitude=");
                psiResponseSb.append(geographicalLongitude);
                psiResponseSb.append(", geographicalUncertainty=");
                psiResponseSb.append(geographicalUncertainty);
              }
              if (psiResponseValues.getLocationInformation().getGeodeticInformation() != null) {
                geodeticLatitude = psiResponseValues.getLocationInformation().getGeodeticInformation().getLatitude();
                geodeticLongitude = psiResponseValues.getLocationInformation().getGeodeticInformation().getLongitude();
                geodeticUncertainty = psiResponseValues.getLocationInformation().getGeodeticInformation().getUncertainty();
                geodeticConfidence = psiResponseValues.getLocationInformation().getGeodeticInformation().getConfidence();
                geodeticScreeningAndPresentationIndicators = psiResponseValues.getLocationInformation().getGeodeticInformation().getScreeningAndPresentationIndicators();
                psiResponseSb.append(", geodeticLatitude=");
                psiResponseSb.append(geodeticLatitude);
                psiResponseSb.append(", geodeticLongitude=");
                psiResponseSb.append(geodeticLongitude);
                psiResponseSb.append(", geodeticUncertainty=");
                psiResponseSb.append(geodeticUncertainty);
                psiResponseSb.append(", geodeticConfidence=");
                psiResponseSb.append(geodeticConfidence);
                psiResponseSb.append(", geodeticScreeningAndPresentationIndicators=");
                psiResponseSb.append(geodeticScreeningAndPresentationIndicators);
              }
              if (psiResponseValues.getLocationInformation().getMscNumber() != null) {
                mscNumber = psiResponseValues.getLocationInformation().getMscNumber().getAddress();
                psiResponseSb.append(", mscNumber=");
                psiResponseSb.append(mscNumber);
              }
              if (psiResponseValues.getLocationInformation().getVlrNumber() != null) {
                vlrNumber = psiResponseValues.getLocationInformation().getVlrNumber().getAddress();
                psiResponseSb.append(", vlrNumber=");
                psiResponseSb.append(vlrNumber);
              }
              if (psiResponseValues.getLocationInformation().getSaiPresent() != false) {
                saiPresent = true;
                psiResponseSb.append(", saiPresent=");
                psiResponseSb.append(saiPresent);
              } else {
                psiResponseSb.append(", saiPresent=");
                psiResponseSb.append(saiPresent);
              }
              if (psiResponseValues.getLocationInformation().getCurrentLocationRetrieved() != false) {
                currentLocationRetrieved = true;
                psiResponseSb.append(", currentLocationRetrieved=");
                psiResponseSb.append(currentLocationRetrieved);
              } else {
                psiResponseSb.append(", currentLocationRetrieved=");
                psiResponseSb.append(currentLocationRetrieved);
              }
            }
            if (psiResponseValues.getSubscriberState() != null) {
              subscriberState = psiResponseValues.getSubscriberState().getSubscriberStateChoice().toString();
              psiResponseSb.append(",subscriberState=");
              psiResponseSb.append(subscriberState);
            }
            if (psiResponseValues.getMnpInfoRes() != null) {
              if (psiResponseValues.getMnpInfoRes().getNumberPortabilityStatus() != null) {
                mnpInfoResultNumberPortabilityStatus = psiResponseValues.getMnpInfoRes().getNumberPortabilityStatus().getType();
                psiResponseSb.append(",mnpInfoResultNumberPortabilityStatus=");
                psiResponseSb.append(mnpInfoResultNumberPortabilityStatus);
              }
              if (psiResponseValues.getMnpInfoRes().getMSISDN() != null) {
                mnpInfoResultMSISDN = psiResponseValues.getMnpInfoRes().getMSISDN().getAddress();
                psiResponseSb.append(",mnpInfoResultMSISDN=");
                psiResponseSb.append(mnpInfoResultMSISDN);
              }
              if (psiResponseValues.getMnpInfoRes().getIMSI() != null) {
                mnpInfoResultIMSI = psiResponseValues.getMnpInfoRes().getIMSI().getData();
                psiResponseSb.append(",mnpInfoResultIMSI=");
                psiResponseSb.append(mnpInfoResultIMSI);
              }
              if (psiResponseValues.getMnpInfoRes().getRouteingNumber() != null) {
                mnpInfoResultRouteingNumber = psiResponseValues.getMnpInfoRes().getRouteingNumber().getRouteingNumber();
                psiResponseSb.append(",mnpInfoResultRouteingNumber=");
                psiResponseSb.append(mnpInfoResultRouteingNumber);
              }
            }
            if (psiResponseValues.getLocationInformationEPS() != null) {
              if (psiResponseValues.getLocationInformationEPS().getMmeName() != null) {
                mmeName = psiResponseValues.getLocationInformationEPS().getMmeName().toString();
                psiResponseSb.append(",mmeName=");
                psiResponseSb.append(mmeName);
              }
            }

          } catch (MAPException me) {
            logger.severe("Map exception while retrieving ATI response values: "+me);
          } catch (Exception e) {
            logger.severe("Exception while retrieving ATI response values: " +e);
          }

          this.sendHTTPResult(httpServletResponse.SC_OK, psiResponseSb.toString());

        } else {
          this.sendHTTPResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, mlpClientErrorMessage);
        }
        break;

      case MLP:
        String svcResultXml;
        MLPResponse mlpResponse = new MLPResponse(this.logger);

        if (mlpResultType == MLPResponse.MLPResultType.OK) {
          MLPResponseParams atiResponse = new MLPResponseParams();
          svcResultXml = mlpResponse.getSinglePositionSuccessXML(atiResponse.x, atiResponse.y, atiResponse.radius, request.msisdn);
        } else if (MLPResponse.isSystemError(mlpResultType)) {
          svcResultXml = mlpResponse.getSystemErrorResponseXML(mlpResultType, mlpClientErrorMessage);
        } else {
          svcResultXml = mlpResponse.getPositionErrorResponseXML(request.msisdn, mlpResultType, mlpClientErrorMessage);
        }

        this.sendHTTPResult(HttpServletResponse.SC_OK, svcResultXml);
        break;
    }
  }


  /**
   * Handle generating the appropriate HTTP response
   * We're making use of the MLPResponse class for both GET/POST requests for convenience and
   * because eventually the GET method will likely be removed
   *
   * @param mlpResultType   OK or error type to return to client
   * @param errorMessage    Error message to send to client
   */
  protected void handlePsiNotSupportedYet(MLPResponse.MLPResultType mlpResultType, String errorMessage, ActivityContextInterface aci) {

    HttpRequest request = getHttpRequest();
    EventContext httpEventContext = this.resumeHttpEventContext();

    CDRInterface cdrInterface = this.getCDRInterface();
    GMLCCDRState gmlcCdrState = cdrInterface.getState();
    gmlcCdrState.init(null, null, null,null, null, null);
    gmlcCdrState.setDialogStartTime(DateTime.now());
    gmlcCdrState.setRemoteDialogId(null);
    cdrInterface.setState(gmlcCdrState);
    SbbLocalObject sbbLO = (SbbLocalObject) cdrInterface;
    aci.attach(sbbLO);
    this.setTimer(aci);
    if (gmlcCdrState.isInitialized()) {
      this.createCDRRecord(RecordStatus.FAILED_NOT_IMPLEMENTED);
    }

    switch (request.type) {
      case REST:
        this.sendHTTPResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMessage);
        break;

      case MLP:
        String svcResultXml;
        MLPResponse mlpResponse = new MLPResponse(this.logger);
        svcResultXml = mlpResponse.getPositionErrorResponseXML(request.msisdn, mlpResultType, errorMessage);
        this.sendHTTPResult(HttpServletResponse.SC_OK, svcResultXml);
        break;
    }
  }

  /**
   * Handle generating the appropriate HTTP response
   * We're making use of the MLPResponse class for both GET/POST requests for convenience and
   * because eventually the GET method will likely be removed
   *
   * @param mlpResultType           OK or error type to return to client
   * @param tcapDialogErrorMessage  Error message to send to client
   */
  protected void handleDialogError(MLPResponse.MLPResultType mlpResultType, String tcapDialogErrorMessage) {

    HttpRequest request = getHttpRequest();
    EventContext httpEventContext = this.resumeHttpEventContext();

    switch (request.type) {
      case REST:
        this.sendHTTPResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, tcapDialogErrorMessage);
        break;

      case MLP:
        String svcResultXml;
        MLPResponse mlpResponse = new MLPResponse(this.logger);
        svcResultXml = mlpResponse.getPositionErrorResponseXML(request.msisdn, mlpResultType, tcapDialogErrorMessage);
        this.sendHTTPResult(HttpServletResponse.SC_OK, svcResultXml);
        break;
    }
  }

  /**
   * HTTP POST to send back to location requestor after SLR request
   */
  protected void sendHttpPostToCallbackUrlAfterSLR(SlrRequestValues slrReq, String slrCallbackUrl) throws Exception {

    HttpRequest request = getHttpRequest();
    EventContext httpEventContext = this.resumeHttpEventContext();

    URL url = new URL(slrCallbackUrl);
    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

    httpURLConnection.setRequestMethod("POST");
    String urlParameters;

    List<String> urlParamsList = new ArrayList<>();
    if (slrReq.getLcsClientID() != null) {
      if (slrReq.getLcsClientID().getLCSClientType() != null && (slrReq.getLcsClientID().getLCSClientType().getType() > Integer.MIN_VALUE
              && slrReq.getLcsClientID().getLCSClientType().getType() < Integer.MAX_VALUE))
        urlParamsList.add("LCSClientIDType=" + Integer.toString(slrReq.getLcsClientID().getLCSClientType().getType()));
      if (slrReq.getLcsClientID().getLCSClientExternalID() != null)
        urlParamsList.add("LCSClientIDExternalID=" + slrReq.getLcsClientID().getLCSClientExternalID().getExternalAddress().getAddress());
      if (slrReq.getLcsClientID().getLCSClientInternalID() != null
              && (slrReq.getLcsClientID().getLCSClientInternalID().getId() > Integer.MIN_VALUE
              && slrReq.getLcsClientID().getLCSClientInternalID().getId() < Integer.MAX_VALUE))
        urlParamsList.add("LCSClientIDInternalID=" + Integer.toString(slrReq.getLcsClientID().getLCSClientInternalID().getId()));
      if (slrReq.getLcsClientID().getLCSClientName() != null) {
        urlParamsList.add("LCSClientIDNameString=" + slrReq.getLcsClientID().getLCSClientName().getNameString().toString());
        urlParamsList.add("LCSClientIDNameDCS=" + Integer.toString(slrReq.getLcsClientID().getLCSClientName().getDataCodingScheme().getCode()));
        urlParamsList.add("LCSClientIDNameFormatIndicator=" + Integer.toString(slrReq.getLcsClientID().getLCSClientName().getLCSFormatIndicator().getIndicator()));
      }
      if (slrReq.getLcsClientID().getLCSAPN() != null)
        // urlParamsList.add("LCSClientIDAPN=" + slrReq.getLcsClientID().getLCSAPN().getApn().getBytes()); // temporary fail avoidance
        if (slrReq.getLcsClientID().getLCSRequestorID() != null) {
          urlParamsList.add("LCSClientIDRequestorIDEncodedString=" + slrReq.getLcsClientID().getLCSRequestorID().getRequestorIDString().getEncodedString().toString());
          urlParamsList.add("LCSClientIDRequestorIDFormatIndicator=" + Integer.toString(slrReq.getLcsClientID().getLCSRequestorID().getLCSFormatIndicator().getIndicator()));
          urlParamsList.add("LCSClientIDRequestorIDDCS=" + Integer.toString(slrReq.getLcsClientID().getLCSRequestorID().getDataCodingScheme().getCode()));
        }
      if (slrReq.getLcsClientID().getLCSClientDialedByMS() != null)
        urlParamsList.add("LCSClientIDLCSClientDialedByMS=" + slrReq.getLcsClientID().getLCSClientDialedByMS().getAddress());
    }
    if (slrReq.getLcsEvent() != null) {
      urlParamsList.add("LCSEvent=" + Integer.toString(slrReq.getLcsEvent().getEvent()));
    }
    if (slrReq.getLcsReferenceNumber() > Integer.MIN_VALUE && slrReq.getLcsReferenceNumber() < Integer.MAX_VALUE) {
      urlParamsList.add("LCSReferenceNumber=" + Integer.toString(slrReq.getLcsReferenceNumber()));
    }
    if (slrReq.getLcsServiceTypeID() > Integer.MIN_VALUE && slrReq.getLcsServiceTypeID() < Integer.MAX_VALUE) {
      urlParamsList.add("LCSServiceTypeID=" + Integer.toString(slrReq.getLcsServiceTypeID()));
    }
    if (slrReq.getLocationEstimate() != null) {
      urlParamsList.add("Latitude=" + Double.toString(slrReq.getLocationEstimate().getLatitude()));
      urlParamsList.add("Longitude=" + Double.toString(slrReq.getLocationEstimate().getLongitude()));
      urlParamsList.add("Altitude=" + Double.toString(slrReq.getLocationEstimate().getAltitude()));
      urlParamsList.add("Confidence=" + Double.toString(slrReq.getLocationEstimate().getInnerRadius()));
      urlParamsList.add("Uncertainty=" + Double.toString(slrReq.getLocationEstimate().getUncertainty()));
      urlParamsList.add("UncertaintyAltitude=" + Double.toString(slrReq.getLocationEstimate().getUncertaintyAltitude()));
      urlParamsList.add("UncertaintyInnerRadius=" + Double.toString(slrReq.getLocationEstimate().getUncertaintyRadius()));
      urlParamsList.add("OffsetAngle=" + Double.toString(slrReq.getLocationEstimate().getOffsetAngle()));
      urlParamsList.add("IncludeAngle=" + Double.toString(slrReq.getLocationEstimate().getIncludedAngle()));
      urlParamsList.add("TypeOfShape=" + Integer.toString(slrReq.getLocationEstimate().getTypeOfShape().getCode()));

    }
    if (slrReq.getAdditionalLocationEstimate() != null) {
      urlParamsList.add("addLatitude=" + Double.toString(slrReq.getAdditionalLocationEstimate().getLatitude()));
      urlParamsList.add("addLongitude=" + Double.toString(slrReq.getAdditionalLocationEstimate().getLongitude()));
      urlParamsList.add("addAltitude=" + Double.toString(slrReq.getAdditionalLocationEstimate().getAltitude()));
      urlParamsList.add("addConfidence=" + Double.toString(slrReq.getAdditionalLocationEstimate().getInnerRadius()));
      urlParamsList.add("addUncertainty=" + Double.toString(slrReq.getAdditionalLocationEstimate().getUncertainty()));
      urlParamsList.add("addUncertaintyAltitude=" + Double.toString(slrReq.getAdditionalLocationEstimate().getUncertaintyAltitude()));
      urlParamsList.add("addUncertaintyInnerRadius=" + Double.toString(slrReq.getAdditionalLocationEstimate().getUncertaintyRadius()));
      urlParamsList.add("addOffset=" + Double.toString(slrReq.getAdditionalLocationEstimate().getOffsetAngle()));
      urlParamsList.add("addInclude=" + Double.toString(slrReq.getAdditionalLocationEstimate().getIncludedAngle()));
      urlParamsList.add("addTypeOfShape=" + Integer.toString(slrReq.getAdditionalLocationEstimate().getTypeOfShape().getCode()));

    }
    if (slrReq.getAgeOfLocationEstimate() > Integer.MIN_VALUE && slrReq.getAgeOfLocationEstimate() < Integer.MAX_VALUE) {
      urlParamsList.add("AgeOfLocationEstimate=" + Integer.toString(slrReq.getAgeOfLocationEstimate()));
    }
    if (slrReq.getAccuracyFulfilmentIndicator() != null) {
      urlParamsList.add("AccuracyFulfillmentIndicator=" + Integer.toString(slrReq.getAccuracyFulfilmentIndicator().getIndicator()));
    }
    if (slrReq.getCellGlobalIdOrServiceAreaIdOrLAI() != null) {
      if (slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength() != null) {
        urlParamsList.add("CGIorSAIorLAIMCC=" + Integer.toString(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC()));
        urlParamsList.add("CGIorSAIorLAIMNC=" + Integer.toString(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC()));
        urlParamsList.add("CGIorSAIorLAILAC=" + Integer.toString(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac()));
      }
      if (slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
        urlParamsList.add("CGIorSAIorLAIMCC=" + Integer.toString(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC()));
        urlParamsList.add("CGIorSAIorLAIMNC=" + Integer.toString(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC()));
        urlParamsList.add("CGIorSAIorLAILAC=" + Integer.toString(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac()));
        urlParamsList.add("CGIorSAIorLAICI=" + Integer.toString(slrReq.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode()));
      }
    }
    if (slrReq.getPseudonymIndicator() != false && slrReq.getPseudonymIndicator() != true) {
      urlParamsList.add("PseudonymIndicator=" + Boolean.toString(slrReq.getPseudonymIndicator()));
    }
    if (slrReq.isMoLrShortCircuitIndicator() != false && slrReq.isMoLrShortCircuitIndicator() != true) {
      urlParamsList.add("MOLRShortCircuitIndicator=" + Boolean.toString(slrReq.isMoLrShortCircuitIndicator()));
    }
    if (slrReq.getPeriodicLDRInfo() != null) {
      urlParamsList.add("ReportingAmount=" + Integer.toString(slrReq.getPeriodicLDRInfo().getReportingAmount()));
      urlParamsList.add("ReportingInterval=" + Integer.toString(slrReq.getPeriodicLDRInfo().getReportingInterval()));
    }
    if (slrReq.getSequenceNumber() > Integer.MIN_VALUE && slrReq.getSequenceNumber() < Integer.MAX_VALUE) {
      urlParamsList.add("Sequence Number=" + Integer.toString(slrReq.getSequenceNumber()));
    }
    if (slrReq.getDeferredmtlrData() != null) {
      if (slrReq.getDeferredmtlrData().getDeferredLocationEventType() != null) {
        urlParamsList.add("EnteringArea=" + Boolean.toString(slrReq.getDeferredmtlrData().getDeferredLocationEventType().getEnteringIntoArea()));
        urlParamsList.add("InsideArea=" + Boolean.toString(slrReq.getDeferredmtlrData().getDeferredLocationEventType().getBeingInsideArea()));
        urlParamsList.add("LeavingArea=" + Boolean.toString(slrReq.getDeferredmtlrData().getDeferredLocationEventType().getLeavingFromArea()));
        urlParamsList.add("MSAvailable=" + Boolean.toString(slrReq.getDeferredmtlrData().getDeferredLocationEventType().getMsAvailable()));
      }
      if (slrReq.getDeferredmtlrData().getLCSLocationInfo() != null) {
        urlParamsList.add("GPRSNodeIndicator=" + Boolean.toString(slrReq.getDeferredmtlrData().getLCSLocationInfo().getGprsNodeIndicator()));
        if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getNetworkNodeNumber() != null)
          urlParamsList.add("DeferredMTLRNetworkNodeNumber=" + slrReq.getDeferredmtlrData().getLCSLocationInfo().getNetworkNodeNumber().getAddress());
        if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getLMSI() != null)
          urlParamsList.add("DeferredMTLRLMSI=" + slrReq.getDeferredmtlrData().getLCSLocationInfo().getLMSI().getData().toString());
        if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getMmeName() != null)
          urlParamsList.add("DeferredMTLRMMEName=" + slrReq.getDeferredmtlrData().getLCSLocationInfo().getMmeName().getData().toString());
        if (slrReq.getDeferredmtlrData().getLCSLocationInfo().getAaaServerName() != null)
          urlParamsList.add("DeferredMTLRAAAServerName=" + slrReq.getDeferredmtlrData().getLCSLocationInfo().getAaaServerName().getData().toString());
      }
    }

    StringBuilder params = new StringBuilder();
    Iterator<String> itr = urlParamsList.iterator();
    int urlParamsListSize = urlParamsList.size();
    int c = 0;
    while (itr.hasNext()) {
      params.append(itr.next());
      c++;
      if (c <= urlParamsListSize)
        params.append("&");
    }
    urlParameters = params.toString();
    System.out.println(urlParameters);

    // Send post request
    httpURLConnection.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
    wr.writeBytes(urlParameters);
    wr.flush();
    wr.close();

    int responseCode = httpURLConnection.getResponseCode();
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + urlParameters);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(
            new InputStreamReader(httpURLConnection.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    System.out.println(response.toString());

  }

  /**
   * Return the specified response data to the HTTP client
   *
   * @param responseData Response data to send to client
   */
  protected void sendHTTPResult(int statusCode, String responseData) {
    try {
      EventContext ctx = this.getEventContext();
      if (ctx == null) {
        if (logger.isWarningEnabled()) {
          logger.warning("When responding to HTTP no pending HTTP request is found, responseData=" + responseData);
          return;
        }
      }

      HttpServletRequestEvent event = (HttpServletRequestEvent) ctx.getEvent();

      HttpServletResponse response = event.getResponse();
      response.setStatus(statusCode);
      PrintWriter w;
      w = response.getWriter();
      w.print(responseData);
      w.flush();
      response.flushBuffer();

      if (ctx.isSuspended()) {
        ctx.resumeDelivery();
      }

      if (logger.isFineEnabled()) {
        logger.fine("HTTP Request received and response sent, responseData=" + responseData);
      }

      // getNullActivity().endActivity();
    } catch (Exception e) {
      logger.severe("Error while sending back HTTP response", e);
    }
  }

  /**
   *
   */
  private EventContext resumeHttpEventContext() {
    EventContext httpEventContext = getEventContextCMP();

    if (httpEventContext == null) {
      logger.severe("No HTTP event context, can not resume ");
      return null;
    }

    httpEventContext.resumeDelivery();
    return httpEventContext;
  }

  // //////////////////
  // SBB LO methods //
  // ////////////////

  /*
   * (non-Javadoc)
   *
   * @see org.mobicents.gmlc.slee.cdr.CDRInterfaceParent#
   * recordGenerationSucceeded(org.mobicents.gmlc.slee.cdr.CDRInterfaceParent.RecordType)
   */
  @Override
  public void recordGenerationSucceeded() {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("Generated CDR for Status: " + getCDRInterface().getState());
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see org.mobicents.gmlc.slee.cdr.CDRInterfaceParent#
   * recordGenerationFailed(java.lang.String)
   */
  @Override
  public void recordGenerationFailed(String message) {
    if (this.logger.isSevereEnabled()) {
      this.logger.severe("Failed to generate CDR! Message: '" + message + "'");
      this.logger.severe("Status: " + getCDRInterface().getState());
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see org.mobicents.gmlc.slee.cdr.CDRInterfaceParent#
   * recordGenerationFailed(java.lang.String, java.lang.Throwable)
   */
  @Override
  public void recordGenerationFailed(String message, Throwable t) {
    if (this.logger.isSevereEnabled()) {
      this.logger.severe("Failed to generate CDR! Message: '" + message + "'", t);
      this.logger.severe("Status: " + getCDRInterface().getState());
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * org.mobicents.gmlc.slee.cdr.CDRInterfaceParent#initFailed(java.lang.String, java.lang.Throwable)
   */
  @Override
  public void initFailed(String message, Throwable t) {
    if (this.logger.isSevereEnabled()) {
      this.logger.severe("Failed to initialize CDR Database! Message: '" + message + "'", t);
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see
   * org.mobicents.gmlc.slee.cdr.CDRInterfaceParent#initSuccessed()
   */
  @Override
  public void initSucceeded() {
    if (this.logger.isFineEnabled()) {
      this.logger.fine("CDR Database has been initialized!");
    }

  }

  //////////////////////
  //  CDR interface  //
  ////////////////////

  protected static final String CDR = "CDR";

  public abstract ChildRelationExt getCDRPlainInterfaceChildRelation();

  public CDRInterface getCDRInterface() {
    GmlcPropertiesManagement gmlcPropertiesManagement = GmlcPropertiesManagement.getInstance();
    ChildRelationExt childExt;
    if (gmlcPropertiesManagement.getCdrLoggingTo() == GmlcPropertiesManagement.CdrLoggedType.Textfile) {
      childExt = getCDRPlainInterfaceChildRelation();
    } else {
      //childExt = getCDRInterfaceChildRelation();
      childExt = null; // temporary
    }

    CDRInterface child = (CDRInterface) childExt.get(CDR);
    if (child == null) {
      try {
        child = (CDRInterface) childExt.create(CDR);
      } catch (TransactionRequiredLocalException e) {
        logger.severe("TransactionRequiredLocalException when creating CDR child", e);
      } catch (IllegalArgumentException e) {
        logger.severe("IllegalArgumentException when creating CDR child", e);
      } catch (NullPointerException e) {
        logger.severe("NullPointerException when creating CDR child", e);
      } catch (SLEEException e) {
        logger.severe("SLEEException when creating CDR child", e);
      } catch (CreateException e) {
        logger.severe("CreateException when creating CDR child", e);
      }
    }

    return child;
  }

  protected void createCDRRecord(RecordStatus recordStatus) {
    try {
      this.getCDRInterface().createRecord(recordStatus);
    } catch (Exception e) {
      logger.severe("Error while trying to create CDR Record", e);
    }
  }

  // ///////////////////////////////////////////////
  // protected child stuff, to be used in parent //
  // /////////////////////////////////////////////

  protected void cancelTimer() {
    try {
      TimerID timerID = this.getTimerID();
      if (timerID != null) {
        this.timerFacility.cancelTimer(timerID);
      }
    } catch (Exception e) {
      logger.severe("Could not cancel Timer", e);
    }
  }

  protected void setTimer(ActivityContextInterface ac) {
    TimerOptions options = new TimerOptions();
    long waitingTime = gmlcPropertiesManagement.getDialogTimeout();
    // Set the timer on ACI
    TimerID timerID = this.timerFacility.setTimer(ac, null, System.currentTimeMillis() + waitingTime, options);
    this.setTimerID(timerID);
  }

}
