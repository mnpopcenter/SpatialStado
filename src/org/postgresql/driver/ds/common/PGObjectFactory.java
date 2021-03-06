/*****************************************************************************
 * Copyright (C) 2008 EnterpriseDB Corporation.
 * Copyright (C) 2011 Stado Global Development Group.
 * Copyright (c) 2016 Regents of the University of Minnesota
 *
 * This file is part of the Minnesota Population Center's Terra Populus project.
 * For copyright and licensing information, see the NOTICE and LICENSE files
 * in this project's top-level directory, and also online at:
 * https://github.com/mnpopcenter/stado
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
package org.postgresql.driver.ds.common;

import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

import org.postgresql.driver.ds.*;

/**
 * Returns a DataSource-ish thing based on a JNDI reference.  In the case of a
 * SimpleDataSource or ConnectionPool, a new instance is created each time, as
 * there is no connection state to maintain. In the case of a PoolingDataSource,
 * the same DataSource will be returned for every invocation within the same
 * VM/ClassLoader, so that the state of the connections in the pool will be
 * consistent.
 *
 * @author Aaron Mulder (ammulder@chariotsolutions.com)
 */
public class PGObjectFactory implements ObjectFactory
{
    /**
     * Dereferences a PostgreSQL DataSource.  Other types of references are
     * ignored.
     */
    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
                                    Hashtable environment) throws Exception
    {
        Reference ref = (Reference)obj;
        String className = ref.getClassName();
        if (className.equals("org.postgresql.driver.ds.PGSimpleDataSource")
                || className.equals("org.postgresql.driver.jdbc2.optional.SimpleDataSource")
                || className.equals("org.postgresql.driver.jdbc3.Jdbc3SimpleDataSource"))
        {
            return loadSimpleDataSource(ref);
        }
        else if (className.equals("org.postgresql.driver.ds.PGConnectionPoolDataSource")
                 || className.equals("org.postgresql.driver.jdbc2.optional.ConnectionPool")
                 || className.equals("org.postgresql.driver.jdbc3.Jdbc3ConnectionPool"))
        {
            return loadConnectionPool(ref);
        }
        else if (className.equals("org.postgresql.driver.ds.PGPoolingDataSource")
                 || className.equals("org.postgresql.driver.jdbc2.optional.PoolingDataSource")
                 || className.equals("org.postgresql.driver.jdbc3.Jdbc3PoolingDataSource"))
        {
            return loadPoolingDataSource(ref);
        }
        else
        {
            return null;
        }
    }

    private Object loadPoolingDataSource(Reference ref)
    {
        // If DataSource exists, return it
        String name = getProperty(ref, "dataSourceName");
        PGPoolingDataSource pds = PGPoolingDataSource.getDataSource(name);
        if (pds != null)
        {
            return pds;
        }
        // Otherwise, create a new one
        pds = new PGPoolingDataSource();
        pds.setDataSourceName(name);
        loadBaseDataSource(pds, ref);
        String min = getProperty(ref, "initialConnections");
        if (min != null)
        {
            pds.setInitialConnections(Integer.parseInt(min));
        }
        String max = getProperty(ref, "maxConnections");
        if (max != null)
        {
            pds.setMaxConnections(Integer.parseInt(max));
        }
        return pds;
    }

    private Object loadSimpleDataSource(Reference ref)
    {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        return loadBaseDataSource(ds, ref);
    }

    private Object loadConnectionPool(Reference ref)
    {
        PGConnectionPoolDataSource cp = new PGConnectionPoolDataSource();
        return loadBaseDataSource(cp, ref);
    }

    protected Object loadBaseDataSource(BaseDataSource ds, Reference ref)
    {
        ds.setDatabaseName(getProperty(ref, "databaseName"));
        ds.setPassword(getProperty(ref, "password"));
        String port = getProperty(ref, "portNumber");
        if (port != null)
        {
            ds.setPortNumber(Integer.parseInt(port));
        }
        ds.setServerName(getProperty(ref, "serverName"));
        ds.setUser(getProperty(ref, "user"));

        String prepareThreshold = getProperty(ref, "prepareThreshold");
        if (prepareThreshold != null)
            ds.setPrepareThreshold(Integer.parseInt(prepareThreshold));

        return ds;
    }

    protected String getProperty(Reference ref, String s)
    {
        RefAddr addr = ref.get(s);
        if (addr == null)
        {
            return null;
        }
        return (String)addr.getContent();
    }

}
