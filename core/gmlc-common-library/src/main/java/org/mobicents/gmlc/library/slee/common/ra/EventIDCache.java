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

package org.mobicents.gmlc.library.slee.common.ra;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.ResourceAdaptorContext;

/**
 *
 * @author <a href="mailto:abhayani@gmail.com"> Amit Bhayani </a>
 * @author <a href="mailto:bbaranow@redhat.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class EventIDCache {

  private ConcurrentHashMap<String, FireableEventType> eventIds = new ConcurrentHashMap<String, FireableEventType>();
  private final String eventVendor;
  private final String eventVersion;

  private Tracer tracer;
  private EventLookupFacility eventLookupFacility;
  public EventIDCache(ResourceAdaptorContext raContext, final String vendor, final String version) {
    this.tracer = raContext.getTracer(EventIDCache.class.getSimpleName());
    this.eventLookupFacility = raContext.getEventLookupFacility();
    this.eventVendor = vendor;
    this.eventVersion = version;
  }

  public FireableEventType getEventId(String eventName) {

    FireableEventType eventType = eventIds.get(eventName);
    if (eventType == null) {
      try {
        eventType = eventLookupFacility.getFireableEventType(new EventTypeID(eventName, this.eventVendor,
            this.eventVersion));
      } catch (Throwable e) {
        this.tracer.severe("Error while looking up for GMLC Events", e);
      }
      if (eventType != null) {
        this.eventIds.put(eventName, eventType);
      }
    }
    return eventType;
  }
}
