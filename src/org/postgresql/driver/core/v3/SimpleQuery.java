/*****************************************************************************
 * Copyright (C) 2008 EnterpriseDB Corporation.
 * Copyright (C) 2011 Stado Global Development Group.
 *
 * This file is part of Stado.
 *
 * Stado is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Stado is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Stado.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can find Stado at http://www.stado.us
 *
 ****************************************************************************/
package org.postgresql.driver.core.v3;

import org.postgresql.driver.core.*;

import java.lang.ref.PhantomReference;

/**
 * V3 Query implementation for a single-statement query.
 * This also holds the state of any associated server-side
 * named statement. We use a PhantomReference managed by
 * the QueryExecutor to handle statement cleanup.
 * 
 * @author Oliver Jowett (oliver@opencloud.com)
 */
class SimpleQuery implements V3Query {

    SimpleQuery(String[] fragments, ProtocolConnectionImpl protoConnection)
    {
        this.fragments = fragments;
        this.protoConnection = protoConnection;
    }

    public ParameterList createParameterList() {
        if (fragments.length == 1)
            return NO_PARAMETERS;

        return new SimpleParameterList(fragments.length - 1, protoConnection);
    }

    public String toString(ParameterList parameters) {
        StringBuffer sbuf = new StringBuffer(fragments[0]);
        for (int i = 1; i < fragments.length; ++i)
        {
            if (parameters == null)
                sbuf.append('?');
            else
                sbuf.append(parameters.toString(i));
            sbuf.append(fragments[i]);
        }
        return sbuf.toString();
    }

    public String toString() {
        return toString(null);
    }

    public void close() {
        unprepare();
    }

    //
    // V3Query
    //

    public SimpleQuery[] getSubqueries() {
        return null;
    }

    //
    // Implementation guts
    //

    String[] getFragments() {
        return fragments;
    }

  
    
    void setStatementName(String statementName) {
        this.statementName = statementName;
        this.encodedStatementName = Utils.encodeUTF8(statementName);
    }

    void setStatementTypes(int[] paramTypes) {
        this.preparedTypes = paramTypes;
    }

    int[] getStatementTypes() {
        return preparedTypes;
    }

    String getStatementName() {
        return statementName;
    }

    boolean isPreparedFor(int[] paramTypes) {
        if (statementName == null)
            return false; // Not prepared.

        // Check for compatible types.
        for (int i = 0; i < paramTypes.length; ++i)
            if (paramTypes[i] != Oid.UNSPECIFIED && paramTypes[i] != preparedTypes[i])
                return false;

        return true;
    }

    boolean hasUnresolvedTypes() {
        if (preparedTypes == null)
            return true;

        for (int i=0; i<preparedTypes.length; i++) {
            if (preparedTypes[i] == Oid.UNSPECIFIED)
                return true;
        }

        return false;
    }

    byte[] getEncodedStatementName() {
        return encodedStatementName;
    }

    void setFields(Field[] fields) {
        this.fields = fields;
    }
    Field[] getFields() {
        return fields;
    }

    // Have we sent a Describe Portal message for this query yet?
    boolean isPortalDescribed() {
        return portalDescribed;
    }
    void setPortalDescribed(boolean portalDescribed) {
        this.portalDescribed = portalDescribed;
    }

    // Have we sent a Describe Statement message for this query yet?
    // Note that we might not have need to, so this may always be false.
    boolean isStatementDescribed() {
        return statementDescribed;
    }
    void setStatementDescribed(boolean statementDescribed) {
        this.statementDescribed = statementDescribed;
    }

    void setCleanupRef(PhantomReference cleanupRef) {
        if (this.cleanupRef != null) {
            this.cleanupRef.clear();
            this.cleanupRef.enqueue();
        }
        this.cleanupRef = cleanupRef;
    }

    void unprepare() {
        if (cleanupRef != null)
        {
            cleanupRef.clear();
            cleanupRef.enqueue();
            cleanupRef = null;
        }

        statementName = null;
        encodedStatementName = null;
        fields = null;
        portalDescribed = false;
        statementDescribed = false;
    }

    private final String[] fragments;
    private final ProtocolConnectionImpl protoConnection;
    private String statementName;
    private byte[] encodedStatementName;
    private Field[] fields;
    private boolean portalDescribed;
    private boolean statementDescribed;
    private PhantomReference cleanupRef;
    private int[] preparedTypes;

    final static SimpleParameterList NO_PARAMETERS = new SimpleParameterList(0, null);
}


