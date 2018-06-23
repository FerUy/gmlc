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

/**
 *
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public enum RecordStatus {

  FAILED_INVOKE_TIMEOUT,
  FAILED_APP_TIMEOUT,
  FAILED_CORRUPTED_MESSAGE,
  FAILED_TRANSPORT_ERROR,
  FAILED_TRANSPORT_FAILURE,
  FAILED_SYSTEM_FAILURE,
  ABORT_APP,
  TCAP_DIALOG_REJECTED,
  TCAP_DIALOG_PROVIDER_ABORT,
  TCAP_DIALOG_USER_ABORT,
  TCAP_DIALOG_TIMEOUT,
  FAILED_MAP_ERROR_COMPONENT,
  FAILED_MAP_REJECT_COMPONENT,
  ATI_CGI_SUCCESS,
  ATI_STATE_SUCCESS,
  ATI_LAI_SUCCESS,
  ATI_CGI_STATE_SUCCESS,
  ATI_LAI_STATE_SUCCESS,
  ATI_CGI_OR_LAI_OR_STATE_FAILURE,
  ATI_SYSTEM_FAILURE,
  ATI_NOT_ALLOWED,
  ATI_DATA_MISSING,
  ATI_UNEXPECTED_DATA_VALUE,
  ATI_UNKNOWN_SUBSCRIBER,
  SRI_SUCCESS,
  SRI_SYSTEM_FAILURE,
  SRI_DATA_MISSING,
  SRI_UNEXPECTED_DATA_VALUE,
  SRI_FACILITY_NOT_SUPPORTED,
  SRI_UNKNOWN_SUBSCRIBER,
  SRI_ABSENT_SUBSCRIBER,
  SRI_UNAUTHORIZED_REQUESTING_NETWORK,
  PSL_SUCCESS,
  PSL_SYSTEM_FAILURE,
  PSL_DATA_MISSING,
  PSL_UNEXPECTED_DATA_VALUE,
  PSL_FACILITY_NOT_SUPPORTED,
  PSL_UNIDENTIFIED_SUBSCRIBER,
  PSL_ILLEGAL_SUBSCRIBER,
  PSL_ILLEGAL_EQUIPMENT,
  PSL_ABSENT_SUBSCRIBER,
  PSL_UNAUTHORIZED_REQUESTING_NETWORK,
  PSL_UNAUTHORIZED_LCS_CLIENT,
  PSL_POSITION_METHOD_FAILURE,
  SLR_SUCCESS,
  SLR_SYSTEM_FAILURE,
  SLR_DATA_MISSING,
  SLR_RESOURCE_LIMITATION,
  SLR_ABSENT_SUBSCRIBER,
  SLR_UNEXPECTED_DATA_VALUE,
  SLR_FACILITY_NOT_SUPPORTED,
  SLR_UNKNOWN_SUBSCRIBER,
  SLR_ILLEGAL_EQUIPMENT,
  SLR_UNAUTHORIZED_REQUESTING_NETWORK,
  SLR_UNKNOWN_OR_UNREACHABLE_LCS_CLIENT,
  SLR_MM_EVENT_NOT_SUPPORTED,
  SLR_POSITION_METHOD_FAILURE,
  SRISM_SYSTEM_FAILURE,
  SRISM_DATA_MISSING,
  SRISM_UNEXPECTED_DATA_VALUE,
  SRISM_FACILITY_NOT_SUPPORTED,
  SRISM_UNIDENTIFIED_SUBSCRIBER,
  SRISM_ILLEGAL_SUBSCRIBER,
  SRISM_ILLEGAL_EQUIPMENT,
  SRISM_UNKNOWN_EQUIPMENT,
  SRISM_UNKNOWN_SUBSCRIBER,
  SRISM_ABSENT_SUBSCRIBER,
  SRISM_INCOMPATIBLE_TERMINAL,
  SRISM_RESOURCE_LIMITATION,
  SRISM_NUMBER_CHANGED,
  SRISM_UNKNOWN_MSC,
  SRISM_ROMAING_NOT_ALLOWED,
  SRISM_BEARER_SERVICE_NOT_PROVISIONED,
  SRISM_TELESERVICE_NOT_PROVISIONED,
  PSI_STATE_SUCCESS,
  PSI_LOC_SUCCESS,
  PSI_GEO_SUCCESS,
  PSI_MNP_INFO_SUCCESS,
  PSI_SYSTEM_FAILURE,
  PSI_DATA_MISSING,
  PSI_UNEXPECTED_DATA_VALUE,
  PSI_FACILITY_NOT_SUPPORTED,
  PSI_UNIDENTIFIED_SUBSCRIBER,
  PSI_ILLEGAL_SUBSCRIBER,
  PSI_ILLEGAL_EQUIPMENT,
  PSI_UNKNOWN_EQUIPMENT,
  PSI_UNKNOWN_SUBSCRIBER,
  PSI_ABSENT_SUBSCRIBER,
  PSI_INCOMPATIBLE_TERMINAL,
  PSI_RESOURCE_LIMITATION,
  PSI_NUMBER_CHANGED,
  PSI_UNKNOWN_MSC,
  PSI_ROMAING_NOT_ALLOWED,
  PSI_BEARER_SERVICE_NOT_PROVISIONED,
  PSI_TELESERVICE_NOT_PROVISIONED,
  FAILED_NOT_IMPLEMENTED,

}
