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

import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class SriForLcsResponseValues implements Serializable {

  private static final long serialVersionUID = -8634011303930031063L;

  private static Charset isoCharset = Charset.forName("ISO-8859-1");

  /*
    3GPP TS 29.002 MAP Specification v15.0.0 (2017-03-15)

    13A.1	MAP-SEND-ROUTING-INFO-FOR-LCS Service

    13A.1.1	Definition
    This service is used between the GMLC and the HLR to retrieve the routing information needed for routing a location service request
    to the servicing VMSC, SGSN, MME or 3GPP AAA server. The MAP-SEND-ROUTING-INFO-FOR-LCS is a confirmed service using the primitives
    from table 13A.1/1.

    13A.1.2	Service Primitives

        Table 13A.1/1: MAP-SEND-ROUTING-INFO-FOR-LCS
        Parameter name              Request     Indication  Response    Confirm
        Invoke Id                      M           M(=)        M(=)       M(=)
        MLC Number                     M           M(=)
        MSISDN                         C           C(=)        C          C(=)
        IMSI                           C           C(=)        C          C(=)
        LMSI                                                   C          C(=)
        Network Node Number                                    C          C(=)
        GPRS Node Indicator                                    C          C(=)
        Additional Number                                      C          C(=)
        Supported LCS Capability Sets                          C          C(=)
        Additional LCS Capability Sets                         C          C(=)
        MME Name                                               C          C(=)
        SGSN Name                                              C          C(=)
        AAA Server Name                                        C          C(=)
        V-GMLC Address                                         U          C(=)
        Additional V-GMLC Address                              U          C(=)
        H-GMLC Address                                         C          C(=)
        PPR Address                                            U          C(=)
        User error                                             C          C(=)
        Provider error                                                    O

    ? (M): mandatory parameter.
    ? (O): provider option.
    ? (C): conditional parameter (i.e. it will always be present in the indication type primitive
           if it was present in the corresponding request type primitive).
    ? (U): TC-user optional parameter.
    ? (=): the parameter must have the same value in the indication primitive as provided in the
           corresponding request primitive.
    ? A blank Indicates that the parameter is not applicable.

    */
  private SubscriberIdentity msisdn; // The MSISDN is provided to identify the target MS.
  private IMSI imsi; // International Mobile Subscriber Identity defined in 3GPP TS 23.003.
  private LMSI lmsi; // Local MS identity allocated by the VLR to a given subscriber for internal management of data in the VLR
  private ISDNAddressString networkNodeNumber; // ISDN number of LCS target node (MSC or MME, SGSN, or IP-SM-GW) or of an LCS Router.
  private boolean gprsNodeIndicator; // Indication that the Network Node Number received from HLR, etc. is to be considered as the SGSN number.
  private AdditionalNumber additionalNumber; // This parameter refers to the ISDN number of an additional LCS target node (MSC or MME or SGSN) or of an LCS Router.
  private SupportedLCSCapabilitySets supportedLCSCapabilitySets, addSupportedLCSCapabilitySets; // Capability sets of LCS supported in the VLR or SGSN. Provided only if LCS capability sets are available in HLR and Network Node Number is present in this message.
  private DiameterIdentity mmeName; // Diameter Identity of an MME as defined in 3GPP TS 23.003.
  private DiameterIdentity sgsnName, sgsnRealm; // Diameter Identity of an SGSN as defined in 3GPP TS 23.003. These parameters are provided in a successful response when the serving node is an SGSN and the SGSN has indicated its support for Lgd interface.
  private DiameterIdentity aaaServerName; // Diameter Identity of a 3GPP AAA server as defined in 3GPP TS 29.273.
  private GSNAddress hGmlcAddress, vGmlcAddress, pprAddress, addVGmlcAddress; // IP address of a H-GMLC, V-GMLC and PPR.

  public SriForLcsResponseValues() {
  }

  public SriForLcsResponseValues(SubscriberIdentity msisdn, IMSI imsi, ISDNAddressString networkNodeNumber) {
    this.msisdn = msisdn;
    this.imsi = imsi;
    this.networkNodeNumber = networkNodeNumber;
  }

  public SriForLcsResponseValues(SubscriberIdentity msisdn, IMSI imsi, LMSI lmsi,
                                 ISDNAddressString networkNodeNumber, boolean gprsNodeIndicator,
                                 AdditionalNumber additionalNumber, SupportedLCSCapabilitySets supportedLCSCapabilitySets,
                                 SupportedLCSCapabilitySets addSupportedLCSCapabilitySets, DiameterIdentity mmeName,
                                 DiameterIdentity sgsnName, DiameterIdentity sgsnRealm, DiameterIdentity aaaServerName,
                                 GSNAddress hGmlcAddress, GSNAddress vGmlcAddress, GSNAddress pprAddress) {
    this.msisdn = msisdn;
    this.imsi = imsi;
    this.lmsi = lmsi;
    this.networkNodeNumber = networkNodeNumber;
    this.gprsNodeIndicator = gprsNodeIndicator;
    this.additionalNumber = additionalNumber;
    this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
    this.addSupportedLCSCapabilitySets = addSupportedLCSCapabilitySets;
    this.mmeName = mmeName;
    this.sgsnName = sgsnName;
    this.sgsnRealm = sgsnRealm;
    this.aaaServerName = aaaServerName;
    this.hGmlcAddress = hGmlcAddress;
    this.vGmlcAddress = vGmlcAddress;
    this.pprAddress = pprAddress;
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

  public GSNAddress getAddVGmlcAddress() {
    return addVGmlcAddress;
  }

  public void setAddVGmlcAddress(GSNAddress addVGmlcAddress) {
    this.addVGmlcAddress = addVGmlcAddress;
  }

  @Override
  public String toString() {

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SriForLcsResponseValues [");

    stringBuilder.append(", IMSI=");
    stringBuilder.append(imsi);

    stringBuilder.append(", MSISDN=");
    stringBuilder.append(msisdn);

    stringBuilder.append(", LMSI=");
    stringBuilder.append(lmsi);

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

    stringBuilder.append("]");
    return stringBuilder.toString();
  }


}
