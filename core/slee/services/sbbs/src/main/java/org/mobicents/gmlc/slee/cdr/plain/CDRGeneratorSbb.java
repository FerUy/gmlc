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

package org.mobicents.gmlc.slee.cdr.plain;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.SbbContext;
import javax.slee.serviceactivity.ServiceStartedEvent;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.mobicents.gmlc.slee.cdr.GMLCCDRState;
import org.mobicents.gmlc.slee.cdr.RecordStatus;
import org.mobicents.gmlc.slee.map.MobileCoreNetworkInterfaceSbb;
import org.mobicents.protocols.ss7.indicator.AddressIndicator;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;

import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMNList;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import org.mobicents.gmlc.slee.cdr.CDRInterface;

/**
 *
 * @author <a href="mailto:bbaranow@redhat.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public abstract class CDRGeneratorSbb extends MobileCoreNetworkInterfaceSbb implements CDRInterface {

  private static final Logger cdrTracer = Logger.getLogger(CDRGeneratorSbb.class);

  private static final String CDR_GENERATED_TO = "Textfile";

  public CDRGeneratorSbb() {
    //super("CDRGeneratorSbb");
    super();
    // TODO Auto-generated constructor stub
  }

  // -------------------- SLEE Stuff -----------------------
  // --------------- CDRInterface methods ------------------
  /*
   * (non-Javadoc)
   *
   * @see org.mobicents.gmlc.slee.cdr.CDRInterface#init(boolean)
   */
  @Override
  public void init(final boolean reset) {
    super.logger.info("Setting CDR_GENERATED_TO to "+ CDR_GENERATED_TO);
  }

  /* (non-Javadoc)
   * @see org.mobicents.gmlc.slee.cdr.CDRInterface#createRecord(org.mobicents.gmlc.slee.cdr.Status)
   */
  @Override
  public void createRecord(RecordStatus outcome) {

    GMLCCDRState state = getState();

    if (state.isGenerated()) {
      super.logger.severe("");
    } else {
      if (super.logger.isFineEnabled()) {
        super.logger.fine("Generating record, status '" + outcome + "' for '" + state + "'");
      }
      DateTime startTime = state.getDialogStartTime();
      if (startTime != null) {
        DateTime endTime = DateTime.now();
        Long duration = endTime.getMillis() - startTime.getMillis();
        state.setDialogEndTime(endTime);
        state.setDialogDuration(duration);
      }
      state.setRecordStatus(outcome);
      state.setGenerated(true);
      this.setState(state);
      String data = this.toString(state);
      if (this.logger.isFineEnabled()) {
        this.logger.fine(data);
      } else {
        this.cdrTracer.debug(data);
      }
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see org.mobicents.gmlc.slee.cdr.CDRInterface#setState(org.mobicents.gmlc.slee.cdr.GMLCCDRState)
   */
  @Override
  public void setState(GMLCCDRState state) {
    this.setGMLCCDRState(state);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.mobicents.gmlc.slee.cdr.CDRInterface#getState()
   */
  @Override
  public GMLCCDRState getState() {
    return this.getGMLCCDRState();
  }

  // CMPs
  public abstract GMLCCDRState getGMLCCDRState();

  public abstract void setGMLCCDRState(GMLCCDRState state);

  public void onStartServiceEvent(ServiceStartedEvent event, ActivityContextInterface aci) {
    this.init(true);
  }

  // --------------- SBB callbacks ---------------

  /*
   * (non-Javadoc)
   *
   * @see javax.slee.Sbb#sbbCreate()
   */
  @Override
  public void sbbCreate() throws CreateException {
    this.setGMLCCDRState(new GMLCCDRState());
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
   */
  @Override
  public void setSbbContext(SbbContext ctx) {
    super.setSbbContext(ctx);
    super.logger = super.sbbContext.getTracer(TRACER_NAME);
  }

  /*
   * (non-Javadoc)
   *
   * @see javax.slee.Sbb#unsetSbbContext()
   */
  @Override
  public void unsetSbbContext() {
    super.unsetSbbContext();
  }

  // -------- helper methods
  private static final String SEPARATOR = ":";
  /**
   * @param gmlcCdrState
   * @return
   */
  protected String toString(GMLCCDRState gmlcCdrState) {

    final StringBuilder stringBuilder = new StringBuilder(); //StringBuilder is faster than StringBuffer

    final Timestamp time_stamp = new Timestamp(System.currentTimeMillis());

    // TIMESTAMP
    stringBuilder.append(time_stamp).append(SEPARATOR);

    // ID
    stringBuilder.append(gmlcCdrState.getId()).append(SEPARATOR);

    // RECORD STATUS
    stringBuilder.append(gmlcCdrState.getRecordStatus().toString()).append(SEPARATOR);

    // LOCAL DIALOG_ID
    stringBuilder.append(gmlcCdrState.getLocalDialogId()).append(SEPARATOR);

    // REMOTE DIALOG_ID
    stringBuilder.append(gmlcCdrState.getRemoteDialogId()).append(SEPARATOR);

    // DIALOG_DURATION
    Long dialogDuration = gmlcCdrState.getDialogDuration();
    if (dialogDuration != null) {
      // TODO: output as millis or?
      stringBuilder.append(dialogDuration).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * LOCAL Address
     */
    SccpAddress localAddress = gmlcCdrState.getLocalAddress();
    if (localAddress != null) {
      AddressIndicator addressIndicator = localAddress.getAddressIndicator();

      // Local SPC
      if (addressIndicator.isPCPresent()) {
        stringBuilder.append(localAddress.getSignalingPointCode()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }

      // Local SSN
      if (addressIndicator.isSSNPresent()) {
        stringBuilder.append(localAddress.getSubsystemNumber()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
      // Local Routing Indicator
      if (addressIndicator.getRoutingIndicator() != null) {
        stringBuilder.append((byte) addressIndicator.getRoutingIndicator().getValue()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }

      //Local GLOBAL TITLE
      GlobalTitle localAddressGlobalTitle = localAddress.getGlobalTitle();
      // Local GLOBAL TITLE INDICATOR
      if (localAddressGlobalTitle != null && localAddressGlobalTitle.getGlobalTitleIndicator() != null) {
        stringBuilder.append((byte) localAddressGlobalTitle.getGlobalTitleIndicator().getValue()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
      // Local GLOBAL TITLE DIGITS
      if (localAddressGlobalTitle != null && localAddressGlobalTitle.getDigits() != null) {
        stringBuilder.append(localAddressGlobalTitle.getDigits()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
    }

    /**
     * REMOTE Address
     */
    SccpAddress remoteAddress = gmlcCdrState.getRemoteAddress();
    if (remoteAddress != null) {
      AddressIndicator addressIndicator = remoteAddress.getAddressIndicator();

      // Remote SPC
      if (addressIndicator.isPCPresent()) {
        stringBuilder.append(remoteAddress.getSignalingPointCode()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }

      // Remote SSN
      if (addressIndicator.isSSNPresent()) {
        stringBuilder.append(remoteAddress.getSubsystemNumber()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }

      // Remote Routing Indicator
      if (addressIndicator.getRoutingIndicator() != null) {
        stringBuilder.append((byte) addressIndicator.getRoutingIndicator().getValue()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }

      // Remote GLOBAL TITLE
      GlobalTitle remoteAddressGlobalTitle = remoteAddress.getGlobalTitle();
      if (remoteAddressGlobalTitle != null && remoteAddressGlobalTitle.getGlobalTitleIndicator() != null) {
        // Remote GLOBAL TITLE INDICATOR
        stringBuilder.append((byte) remoteAddressGlobalTitle.getGlobalTitleIndicator().getValue()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
      // Remote GLOBAL TITLE DIGITS
      if (remoteAddressGlobalTitle != null && remoteAddressGlobalTitle.getDigits() != null) {
        stringBuilder.append(remoteAddressGlobalTitle.getDigits()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
    }

    /**
     * ORIGINATING REFERENCE Address
     */
    AddressString addressString = gmlcCdrState.getOrigReference();
    if(addressString != null) {
      // Originating Reference ADDRESS NATURE
      stringBuilder.append((byte) addressString.getAddressNature().getIndicator()).append(SEPARATOR);
      // Originating Reference NUMBERING PLAN INDICATOR
      stringBuilder.append((byte) addressString.getNumberingPlan().getIndicator()).append(SEPARATOR);
      // Originating Reference ADDRESS DIGITS
      stringBuilder.append(addressString.getAddress()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
    }

    /**
     * DESTINATION REFERENCE Address
     */
    addressString = gmlcCdrState.getDestReference();
    if(addressString != null) {
      // Destination Reference ADDRESS NATURE
      stringBuilder.append((byte) addressString.getAddressNature().getIndicator()).append(SEPARATOR);
      // Destination Reference NUMBERING PLAN INDICATOR
      stringBuilder.append((byte) addressString.getNumberingPlan().getIndicator()).append(SEPARATOR);
      // Destination Reference ADDRESS DIGITS
      stringBuilder.append(addressString.getAddress()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
    }

    /**
     * ISDN Address
     */
    ISDNAddressString isdnAddressString= gmlcCdrState.getISDNAddressString();
    if(isdnAddressString != null) {
      // ISDN ADDRESS NATURE
      stringBuilder.append((byte) isdnAddressString.getAddressNature().getIndicator()).append(SEPARATOR);
      // ISDN NUMBERING PLAN INDICATOR
      stringBuilder.append((byte) isdnAddressString.getNumberingPlan().getIndicator()).append(SEPARATOR);
      // ISDN ADDRESS DIGITS
      stringBuilder.append(isdnAddressString.getAddress()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
    }

    /**
     * CELL GLOBAL IDENTITY (gathered from MAP ATI)
     */
    // CGI MOBILE COUNTRY CODE (MCC)
    int mcc = gmlcCdrState.getMcc();
    if(mcc != -1) {
      stringBuilder.append(mcc).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // CGI MOBILE NETWORK CODE (MNC)
    int mnc = gmlcCdrState.getMnc();
    if(mnc != -1) {
      stringBuilder.append(mnc).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // CGI LAC
    int lac = gmlcCdrState.getLac();
    if(lac != -1) {
      stringBuilder.append(lac).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // CGI CI
    int ci = gmlcCdrState.getCi();
    if(ci != -1) {
      stringBuilder.append(ci).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // AOL
    int aol = gmlcCdrState.getAol();
    if(aol != -1) {
      stringBuilder.append(aol).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // VLR GT
    ISDNAddressString atiVlrGt = gmlcCdrState.getAtiVlrGt();
    if(atiVlrGt != null) {
      stringBuilder.append(atiVlrGt.getAddress()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // SUBSCRIBER STATE
    String subscriberState = gmlcCdrState.getSubscriberState();
    if(subscriberState != null) {
      stringBuilder.append(subscriberState).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * MAP LSM (SRIforLCS, PSL & SLR) gathered parameters
     */

    /**
     * IMSI
     */
    IMSI imsi = gmlcCdrState.getImsi();
    if(imsi != null) {
      stringBuilder.append(imsi.getData().getBytes(Charset.forName("ISO-8859-1")).toString()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * LMSI
     */
    LMSI lmsi = gmlcCdrState.getLmsi();
    if(lmsi != null) {
      String lmsiStr = new String(lmsi.getData(), StandardCharsets.ISO_8859_1);
      stringBuilder.append(lmsiStr).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * Network Node Number
     */
    ISDNAddressString networkNodeNumber = gmlcCdrState.getNetworkNodeNumber();
    if(networkNodeNumber != null) {
      stringBuilder.append(networkNodeNumber.getAddress()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * GPRS Node Indicator
     */
    boolean gprsNodeIndicator = gmlcCdrState.isGprsNodeIndicator();
    if(gprsNodeIndicator == true || gprsNodeIndicator == false){
      stringBuilder.append(gprsNodeIndicator).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * Additional Number
     */
    AdditionalNumber additionalNumber = gmlcCdrState.getAdditionalNumber();
    if(additionalNumber != null) {
      if (additionalNumber.getMSCNumber() != null) {
        // MAP LSM Additional Number (MSC)
        stringBuilder.append(additionalNumber.getMSCNumber().getAddress()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
      if (additionalNumber.getSGSNNumber() != null) {
        // MAP LSM Additional Number (SGSN)
        stringBuilder.append(additionalNumber.getSGSNNumber().getAddress()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
    }

    /**
     * MME Name
     */
    DiameterIdentity mmeName = gmlcCdrState.getMmeName();
    if(mmeName != null) {
      String mmeNameStr = new String(mmeName.getData(), StandardCharsets.ISO_8859_1);
      stringBuilder.append(mmeNameStr).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * SGSN Name
     */
    DiameterIdentity sgsnName = gmlcCdrState.getSgsnName();
    if(sgsnName != null) {
      String sgsnNameStr = new String(sgsnName.getData(), StandardCharsets.ISO_8859_1);
      stringBuilder.append(sgsnNameStr).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * SGSN Realm
     */
    DiameterIdentity sgsnRealm = gmlcCdrState.getSgsnRealm();
    if(sgsnRealm != null) {
      String sgsnRealmStr = new String(sgsnRealm.getData(), StandardCharsets.ISO_8859_1);
      stringBuilder.append(sgsnRealmStr).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * PPR Address
     */
    GSNAddress pprAddress = gmlcCdrState.getPprAddress();
    if(pprAddress != null) {
      // PPR Address Type
      if (pprAddress.getGSNAddressAddressType() != null) {
        stringBuilder.append(pprAddress.getGSNAddressAddressType().toString()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
      // PPR Address Data
      if (pprAddress.getGSNAddressData() != null) {
        stringBuilder.append(pprAddress.getGSNAddressData().toString()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
    }

    /**
     * Additional V-GMLC Address
     */
    GSNAddress addVGmlcAddress = gmlcCdrState.getvGmlcAddress();
    if(addVGmlcAddress != null) {
      // V-GMLC Address Type
      if (addVGmlcAddress.getGSNAddressAddressType() != null) {
        stringBuilder.append(addVGmlcAddress.getGSNAddressAddressType().toString()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
      // V-GMLC Address Data
      if (addVGmlcAddress.getGSNAddressData() != null) {
        stringBuilder.append(addVGmlcAddress.getGSNAddressData().toString()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
    }

    /**
     * Location Estimate
     */
    // Location Estimate LATITUDE
    ExtGeographicalInformation latitude = gmlcCdrState.getLocationEstimate();
    if(latitude != null) {
      stringBuilder.append(latitude.getLatitude()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // Location Estimate LONGITUDE
    ExtGeographicalInformation longitude = gmlcCdrState.getLocationEstimate();
    if(longitude != null) {
      stringBuilder.append(longitude.getLongitude()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // Location Estimate ALTITUDE
    ExtGeographicalInformation altitude = gmlcCdrState.getLocationEstimate();
    if(altitude != null) {
      stringBuilder.append(altitude.getAltitude()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // Location Estimate CONFIDENCE
    ExtGeographicalInformation confidence = gmlcCdrState.getLocationEstimate();
    if(confidence != null) {
      stringBuilder.append(latitude.getConfidence()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // Location Estimate INNER RADIUS
    ExtGeographicalInformation innerRadius = gmlcCdrState.getLocationEstimate();
    if(innerRadius != null) {
      stringBuilder.append(latitude.getInnerRadius()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // GERAN Positioning Data Info
    PositioningDataInformation geranPositioningDataInformation = gmlcCdrState.getGeranPositioningDataInformation();
    if(geranPositioningDataInformation != null) {
      String geranPositioningDataInformationStr = new String(geranPositioningDataInformation.getData(), StandardCharsets.ISO_8859_1);
      stringBuilder.append(geranPositioningDataInformationStr).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // UTRAN Positioning Data Info
    UtranPositioningDataInfo utranPositioningDataInfo = gmlcCdrState.getUtranPositioningDataInfo();
    if(utranPositioningDataInfo != null) {
      String utranPositioningDataInformationStr = new String(utranPositioningDataInfo.getData(),  StandardCharsets.ISO_8859_1);
      stringBuilder.append(utranPositioningDataInformationStr).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * GERAN GANSS Pos Data Info
     */
    GeranGANSSpositioningData geranGANSSPositioningDataInformation = gmlcCdrState.getGeranGANSSpositioningData();
    if(geranGANSSPositioningDataInformation != null) {
      String geranGANSSPositioningDataInformationStr = new String(geranGANSSPositioningDataInformation.getData(), StandardCharsets.ISO_8859_1);
      stringBuilder.append(geranGANSSPositioningDataInformationStr).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * UTRAN GANSS Pos Data Info
     */
    UtranGANSSpositioningData utranGANSSpositioningData = gmlcCdrState.getUtranGANSSpositioningData();
    if(utranGANSSpositioningData != null) {
      String utranGANSSPositioningDataInformationStr = new String(utranGANSSpositioningData.getData(), StandardCharsets.ISO_8859_1);
      stringBuilder.append(utranGANSSPositioningDataInformationStr).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * AGE OF LOCATION ESTIMATE
     */
    int ageOfLocationEstimate = gmlcCdrState.getAgeOfLocationEstimate();
    if(ageOfLocationEstimate != -1) {
      stringBuilder.append(ageOfLocationEstimate).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * ADDITIONAL LOCATION ESTIMATE
     */
    // Additional Location Estimate LATITUDE
    AddGeographicalInformation additionalLatitude = gmlcCdrState.getAdditionalLocationEstimate();
    if(additionalLatitude != null) {
      stringBuilder.append(additionalLatitude.getLatitude()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // Additional Location Estimate LONGITUDE
    AddGeographicalInformation additionalLongitude = gmlcCdrState.getAdditionalLocationEstimate();
    if(additionalLongitude != null) {
      stringBuilder.append(additionalLongitude.getLongitude()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // Additional Location Estimate ALTITUDE
    AddGeographicalInformation additionalAltitude = gmlcCdrState.getAdditionalLocationEstimate();
    if(additionalAltitude != null) {
      stringBuilder.append(additionalAltitude.getAltitude()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // Additional Location Estimate CONFIDENCE
    AddGeographicalInformation additionalConfidence = gmlcCdrState.getAdditionalLocationEstimate();
    if(additionalConfidence != null) {
      stringBuilder.append(additionalConfidence.getConfidence()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // Additional Location Estimate INNER RADIUS
    AddGeographicalInformation additionalInnerRadius = gmlcCdrState.getAdditionalLocationEstimate();
    if(additionalInnerRadius != null) {
      stringBuilder.append(additionalInnerRadius.getInnerRadius()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * DEFERRED MT LR RESPONSE INDICATOR
     */
    boolean deferredMTLRResponseIndicator = gmlcCdrState.isDeferredMTLRResponseIndicator();
    if(deferredMTLRResponseIndicator == true || deferredMTLRResponseIndicator == false){
      stringBuilder.append(deferredMTLRResponseIndicator).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * CGI or SAI or LAI
     */
    CellGlobalIdOrServiceAreaIdOrLAI lsmCGIorSAIorLAI = gmlcCdrState.getCellGlobalIdOrServiceAreaIdOrLAI();
    if(lsmCGIorSAIorLAI != null) {
      if(lsmCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
        try {
          stringBuilder.append(lsmCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC()).append(SEPARATOR);
          stringBuilder.append(lsmCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC()).append(SEPARATOR);
          stringBuilder.append(lsmCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac()).append(SEPARATOR);
          stringBuilder.append(lsmCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode()).append(SEPARATOR);
        } catch (MAPException e) {
          e.printStackTrace();
        }
      }
      if(lsmCGIorSAIorLAI.getLAIFixedLength() != null) {
        try {
          stringBuilder.append(lsmCGIorSAIorLAI.getLAIFixedLength().getMCC()).append(SEPARATOR);
          stringBuilder.append(lsmCGIorSAIorLAI.getLAIFixedLength().getMNC()).append(SEPARATOR);
          stringBuilder.append(lsmCGIorSAIorLAI.getLAIFixedLength().getLac()).append(SEPARATOR);
          stringBuilder.append(SEPARATOR);
        } catch (MAPException e) {
          e.printStackTrace();
        }
      }
    } else {
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
    }

    /**
     * PSEUDONYM INDICATOR
     */
    boolean pseudonymIndicator = gmlcCdrState.isPseudonymIndicator();
    if(pseudonymIndicator == true || pseudonymIndicator == false){
      stringBuilder.append(pseudonymIndicator).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * ACCURACY FULFILLMENT INDICATOR
     */
    AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = gmlcCdrState.getAccuracyFulfilmentIndicator();
    if(accuracyFulfilmentIndicator != null) {
      stringBuilder.append(accuracyFulfilmentIndicator.getIndicator()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * SEQUENCE NUMBER
     */
    int sequenceNumber = gmlcCdrState.getSequenceNumber();
    if(sequenceNumber != -1) {
      stringBuilder.append(sequenceNumber).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * VELOCITY ESTIMATE
     */
    // HORIZONTAL ESTIMATE
    VelocityEstimate horizontalVelocityEstimate = gmlcCdrState.getVelocityEstimate();
    if(horizontalVelocityEstimate != null) {
      stringBuilder.append(horizontalVelocityEstimate.getHorizontalSpeed()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // VERTICAL ESTIMATE
    VelocityEstimate verticalVelocityEstimate = gmlcCdrState.getVelocityEstimate();
    if(verticalVelocityEstimate != null) {
      stringBuilder.append(verticalVelocityEstimate.getVerticalSpeed()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * SERVING NODE ADDRESS
     */
    ServingNodeAddress servingNodeAddress = gmlcCdrState.getServingNodeAddress();
    if (servingNodeAddress != null) {
      if(servingNodeAddress.getMscNumber() == null) {
        stringBuilder.append(SEPARATOR);
      } else {
        // MAP LSM Serving Node Address MSC Number
        stringBuilder.append(servingNodeAddress.getMscNumber().getAddress()).append(SEPARATOR);
      }
      if(servingNodeAddress.getSgsnNumber() == null) {
        stringBuilder.append(SEPARATOR);
      } else {
        // MAP LSM Serving Node Address SGSN Number
        stringBuilder.append(servingNodeAddress.getSgsnNumber().getAddress()).append(SEPARATOR);
      }
      if(servingNodeAddress.getMmeNumber() == null) {
        stringBuilder.append(SEPARATOR);
      } else {
        // MAP LSM Serving Node Address MME Number
        String mmeNumStr = new String(servingNodeAddress.getMmeNumber().getData(), StandardCharsets.ISO_8859_1);
        stringBuilder.append(mmeNumStr).append(SEPARATOR);
      }
    }

    /**
     * LCSClientID
     */
    LCSClientID lcsClientID = gmlcCdrState.getLcsClientID();
    if(lcsClientID != null) {
      try {
        if (lcsClientID.getLCSClientType() != null && (lcsClientID.getLCSClientType().getType() > Integer.MIN_VALUE
                && lcsClientID.getLCSClientType().getType() < Integer.MAX_VALUE)) {
          stringBuilder.append(lcsClientID.getLCSClientType().getType()).append(SEPARATOR);
        }
        if (lcsClientID.getLCSClientName() != null) {
          stringBuilder.append(lcsClientID.getLCSClientName().getNameString()).append(SEPARATOR);
          stringBuilder.append(lcsClientID.getLCSClientName().getDataCodingScheme().getCode()).append(SEPARATOR);
          stringBuilder.append(lcsClientID.getLCSClientName().getLCSFormatIndicator().getIndicator()).append(SEPARATOR);
          stringBuilder.append(lcsClientID.getLCSClientName().getNameString().getEncodedString().toString()).append(SEPARATOR);
        }
        if (lcsClientID.getLCSAPN() != null) {
          if (lcsClientID.getLCSAPN().getApn() != null)
            stringBuilder.append(lcsClientID.getLCSAPN().getApn()).append(SEPARATOR);
        }
        if (lcsClientID.getLCSClientDialedByMS() != null)
          stringBuilder.append(lcsClientID.getLCSClientDialedByMS().getAddress()).append(SEPARATOR);
        if (lcsClientID.getLCSClientExternalID() != null)
          stringBuilder.append(lcsClientID.getLCSClientExternalID().getExternalAddress().getAddress()).append(SEPARATOR);
        if (lcsClientID.getLCSClientInternalID() != null && (lcsClientID.getLCSClientInternalID().getId() > Integer.MIN_VALUE
                && lcsClientID.getLCSClientInternalID().getId() < Integer.MAX_VALUE))
          stringBuilder.append(lcsClientID.getLCSClientInternalID().getId()).append(SEPARATOR);
        if (lcsClientID.getLCSRequestorID() != null) {
          stringBuilder.append(lcsClientID.getLCSRequestorID().getDataCodingScheme().getCode()).append(SEPARATOR);
          stringBuilder.append(lcsClientID.getLCSRequestorID().getLCSFormatIndicator().getIndicator()).append(SEPARATOR);
          stringBuilder.append(lcsClientID.getLCSRequestorID().getRequestorIDString().getEncodedString()).append(SEPARATOR);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
    }

    /**
     * LCS QoS
     */
    // HORIZONTAL ACCURACY
    LCSQoS horizontalAccuracy = gmlcCdrState.getLcsQoS();
    if(horizontalAccuracy != null) {
      stringBuilder.append(horizontalAccuracy.getHorizontalAccuracy().toString()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // VERTICAL ACCURACY
    LCSQoS verticalAccuracy = gmlcCdrState.getLcsQoS();
    if(verticalAccuracy != null) {
      stringBuilder.append(verticalAccuracy.getVerticalAccuracy().toString()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // RESPONSE TIME
    LCSQoS responseTime = gmlcCdrState.getLcsQoS();
    if(responseTime != null) {
      stringBuilder.append(responseTime.getResponseTime().toString()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * LCS REFERENCE NUMBER
     */
    int lcsReferenceNumber = gmlcCdrState.getLcsReferenceNumber();
    if(lcsReferenceNumber != -1) {
      stringBuilder.append(lcsReferenceNumber).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * LCS SERVICE TYPE ID
     */
    int lcsServiceTypeID = gmlcCdrState.getLcsServiceTypeID();
    if(lcsServiceTypeID != -1) {
      stringBuilder.append(lcsServiceTypeID).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * BAROMETRIC PRESSURE
     */
    String barometricPressureMeasurement = gmlcCdrState.getBarometricPressureMeasurement();
    if(barometricPressureMeasurement != null) {
      stringBuilder.append(barometricPressureMeasurement).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * CIVIC ADDRESS
     */
    String civicAddress = gmlcCdrState.getCivicAddress();
    if(civicAddress != null) {
      stringBuilder.append(civicAddress).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * LCS EVENT
     */
    LCSEvent lcsEvent = gmlcCdrState.getLcsEvent();
    if(lcsEvent != null) {
      stringBuilder.append(lcsEvent.getEvent()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * MSISDN
     */
    ISDNAddressString msisdn = gmlcCdrState.getMsisdn();
    if(msisdn != null) {
      stringBuilder.append(msisdn.getAddress()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * IMEI
     */
    IMEI imei = gmlcCdrState.getImei();
    if(imei != null) {
      stringBuilder.append(imei.getIMEI().getBytes().toString()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * DEFERRED MT LR DATA
     */
    // LOCATION EVENT TYPE
    DeferredmtlrData deferreddMTLRDataDeferredLocationEventType = gmlcCdrState.getDeferredmtlrData();
    if(deferreddMTLRDataDeferredLocationEventType != null) {
      if (deferreddMTLRDataDeferredLocationEventType.getDeferredLocationEventType().getEnteringIntoArea() == true)
        stringBuilder.append("EnteringIntoArea").append(SEPARATOR);
      if (deferreddMTLRDataDeferredLocationEventType.getDeferredLocationEventType().getBeingInsideArea() == true)
        stringBuilder.append("InsideArea").append(SEPARATOR);
      if (deferreddMTLRDataDeferredLocationEventType.getDeferredLocationEventType().getMsAvailable() == true)
        stringBuilder.append("Available").append(SEPARATOR);
      if (deferreddMTLRDataDeferredLocationEventType.getDeferredLocationEventType().getLeavingFromArea() == true)
        stringBuilder.append("LeavingFromArea").append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // LOCATION INFO
    DeferredmtlrData deferreddMTLRDataDeferredLcsLocationInfo = gmlcCdrState.getDeferredmtlrData();
    if(deferreddMTLRDataDeferredLcsLocationInfo != null) {
      stringBuilder.append(deferreddMTLRDataDeferredLcsLocationInfo.getLCSLocationInfo()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // TERMINATION CAUSE
    DeferredmtlrData deferreddMTLRDataDeferredTerminationCause = gmlcCdrState.getDeferredmtlrData();
    if(deferreddMTLRDataDeferredTerminationCause != null) {
      stringBuilder.append(deferreddMTLRDataDeferredTerminationCause.getTerminationCause()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * PERIODIC LDR INFO
     */
    // REPORTING AMOUNT
    PeriodicLDRInfo periodicLDRInfoReportingAmount = gmlcCdrState.getPeriodicLDRInfo();
    if(periodicLDRInfoReportingAmount != null) {
      stringBuilder.append(periodicLDRInfoReportingAmount.getReportingAmount()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    // REPORTING INTERVAL
    PeriodicLDRInfo periodicLDRInfoReportingInterval = gmlcCdrState.getPeriodicLDRInfo();
    if(periodicLDRInfoReportingInterval != null) {
      stringBuilder.append(periodicLDRInfoReportingInterval.getReportingInterval()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * MO LR SHORT-CIRCUIT INDICATOR
     */
    boolean moLrShortCircuitIndicator = gmlcCdrState.isMoLrShortCircuitIndicator();
    if(moLrShortCircuitIndicator == true || moLrShortCircuitIndicator == false){
      stringBuilder.append(moLrShortCircuitIndicator).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * REPORTING PLMN LIST
     */
    ReportingPLMNList plmnList = gmlcCdrState.getReportingPLMNList();
    if(plmnList != null) {
      stringBuilder.append(plmnList.getPlmnList()).append(SEPARATOR);
    } else {
      stringBuilder.append(SEPARATOR);
    }

    /**
     * Location Information (from PSI)
     */
    LocationInformation locationInformation = gmlcCdrState.getLocationInformation();
    if(locationInformation != null) {
      if(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI() != null) {
        if(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength() != null) {
          try {
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC()).append(SEPARATOR);
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC()).append(SEPARATOR);
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac()).append(SEPARATOR);
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode()).append(SEPARATOR);
          } catch (MAPException e) {
            e.printStackTrace();
          }
        } else {
          stringBuilder.append(SEPARATOR);
          stringBuilder.append(SEPARATOR);
          stringBuilder.append(SEPARATOR);
          stringBuilder.append(SEPARATOR);
        }
        if(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength() != null) {
          try {
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC()).append(SEPARATOR);
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC()).append(SEPARATOR);
            stringBuilder.append(locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac()).append(SEPARATOR);
            stringBuilder.append(SEPARATOR);
          } catch (MAPException e) {
            e.printStackTrace();
          }
        } else {
          stringBuilder.append(SEPARATOR);
          stringBuilder.append(SEPARATOR);
          stringBuilder.append(SEPARATOR);
          stringBuilder.append(SEPARATOR);
        }
      }
      if(locationInformation.getVlrNumber() != null) {
        stringBuilder.append(locationInformation.getVlrNumber().getAddress()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
      if(locationInformation.getMscNumber() != null) {
        stringBuilder.append(locationInformation.getMscNumber().getAddress()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
      }
      if(locationInformation.getGeographicalInformation() != null) {
        stringBuilder.append(locationInformation.getGeographicalInformation().getLatitude()).append(SEPARATOR);
        stringBuilder.append(locationInformation.getGeographicalInformation().getLongitude()).append(SEPARATOR);
        stringBuilder.append(locationInformation.getGeographicalInformation().getUncertainty()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(SEPARATOR);
      }
      if(locationInformation.getGeodeticInformation() != null) {
        stringBuilder.append(locationInformation.getGeodeticInformation().getLatitude()).append(SEPARATOR);
        stringBuilder.append(locationInformation.getGeodeticInformation().getLongitude()).append(SEPARATOR);
        stringBuilder.append(locationInformation.getGeodeticInformation().getUncertainty()).append(SEPARATOR);
        stringBuilder.append(locationInformation.getGeodeticInformation().getConfidence()).append(SEPARATOR);
      } else {
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(SEPARATOR);
      }
    } else {
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
      stringBuilder.append(SEPARATOR);
    }

    /**
     * Subscriber STATE (from PSI)
     */
    SubscriberInfo subscriberInfo = gmlcCdrState.getSubscriberInfo();
    if(subscriberInfo != null) {
      stringBuilder.append(subscriberInfo.getSubscriberState().getSubscriberStateChoice());//.append(SEPARATOR);
    } else {
      /*stringBuilder.append(SEPARATOR);*/ /// Uncomment if further fields are added
    }

    return stringBuilder.toString();
  }


}
