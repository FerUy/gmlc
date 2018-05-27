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

package org.mobicents.gmlc.library.slee.services.map.events;

import org.mobicents.gmlc.library.MapLsm;

import java.io.Serializable;

/**
 * @author <a href="mailto:abhayani@gmail.com"> Amit Bhayani </a>
 * @author <a href="mailto:serg.vetyutnev@gmail.com"> Sergey Vetyutnev </a>
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class MapLsmEvent implements Serializable {

  private static final long serialVersionUID = 7510536354373682394L;

  private MapLsm mapLsm;

  public MapLsm getLsm() {
    return mapLsm;
  }

  public void MapLsm(MapLsm lsm) {
    this.mapLsm = lsm;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("MapLsmEvent [");

    if (this.mapLsm != null) {
      sb.append(this.mapLsm.toString());
    }

    sb.append("]");
    return sb.toString();
  }

}
