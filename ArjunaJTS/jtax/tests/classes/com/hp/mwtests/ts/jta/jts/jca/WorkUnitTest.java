/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors 
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
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
/*
 * Copyright (C) 2004,
 *
 * Arjuna Technologies Ltd,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.  
 *
 * $Id: xidcheck.java 2342 2006-03-30 13:06:17Z  $
 */

package com.hp.mwtests.ts.jta.jts.jca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import jakarta.resource.spi.work.Work;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.Transaction;

import org.junit.Test;

import com.arjuna.ats.internal.jta.transaction.jts.TransactionImple;
import com.arjuna.ats.internal.jta.transaction.jts.jca.TxWorkManager;
import com.arjuna.ats.internal.jta.transaction.jts.jca.WorkSynchronization;
import com.hp.mwtests.ts.jta.jts.common.TestBase;

public class WorkUnitTest extends TestBase
{
    class DummyWork implements Work
    {       
        public DummyWork ()
        {
        }
        
        public void release ()
        {
        }
        
        public void run ()
        {
        }
    }
    
    @Test
    public void testWorkManager () throws Exception
    {
        DummyWork work = new DummyWork();
        Transaction tx = new TransactionImple();
        
        TxWorkManager.addWork(work, tx);
        
        try
        {
            TxWorkManager.addWork(new DummyWork(), tx);
            
            fail();
        }
        catch (final Throwable ex)
        {
        }
        
        assertTrue(TxWorkManager.hasWork(tx));
        
        assertEquals(work, TxWorkManager.getWork(tx));       
        
        TxWorkManager.removeWork(work, tx);
        
        assertEquals(TxWorkManager.getWork(tx), null);
    }
    
    @Test
    public void testWorkSynchronization () throws Exception
    {
        Transaction tx = new TransactionImple();
        Synchronization ws = new WorkSynchronization(tx);
        DummyWork work = new DummyWork();
        
        TxWorkManager.addWork(work, tx);
        
        try
        {
            ws.beforeCompletion();
            
            fail();
        }
        catch (final IllegalStateException ex)
        {
        }
        
        ws.afterCompletion(Status.STATUS_COMMITTED);
    }
}
