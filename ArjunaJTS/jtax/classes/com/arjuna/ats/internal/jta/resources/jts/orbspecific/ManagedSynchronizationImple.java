/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2008,
 * @author JBoss Inc.
 */
package com.arjuna.ats.internal.jta.resources.jts.orbspecific;

import com.arjuna.ArjunaOTS.ManagedSynchronizationOperations;

public class ManagedSynchronizationImple extends SynchronizationImple implements ManagedSynchronizationOperations
{
    private jakarta.transaction.Synchronization ptr;

    public ManagedSynchronizationImple(jakarta.transaction.Synchronization ptr) {
        super(ptr);
        this.ptr = ptr;
    }

    protected org.omg.PortableServer.Servant getPOATie() {
        return new com.arjuna.ArjunaOTS.ManagedSynchronizationPOATie(this);
    }

    public String implementationType()
    {
        return ptr.getClass().getName();
    }

    public String instanceName()
    {
        return ptr.toString();
    }
}
