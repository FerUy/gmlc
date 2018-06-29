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

package org.mobicents.gmlc.slee.http.report;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author <a href="mailto:aferreiraguido@gmail.com"> Alejandro Ferreira Guido </a>
 */
public class ReportParameters extends HashMap<String, String> implements Serializable {

    public ReportParameters() {
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("{ ");

        for (HashMap.Entry<String,String> element: this.entrySet()) {
            builder.append(String.format("%s%s = %s",
                    builder.length() > 2 ? ", " : "", element.getKey(), element.getValue()));
        }

        builder.append(" }");

        return builder.toString();
    }
}
