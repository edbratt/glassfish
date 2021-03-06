/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package request;

import java.io.Serializable;

public class VendorPartRequest implements Serializable {

    public String partNumber;
    public int revision;
    public String description;
    public double price;
    public int vendorId;

    public VendorPartRequest(String partNumber, int revision,
            String description, double price, int vendorId) {

        this.partNumber = partNumber;
        this.revision = revision;
        this.description = description;
        this.price = price;
        this.vendorId = vendorId;
    }
}
