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
 * Copyright (C) 1998, 1999, 2000,
 *
 * Arjuna Solutions Limited,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: SynchronizationImple.java 2342 2006-03-30 13:06:17Z  $
 */

package com.arjuna.ats.internal.jta.resources.arjunacore;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.arjuna.coordinator.SynchronizationRecord;
import com.arjuna.ats.internal.jta.utils.arjunacore.StatusConverter;
import com.arjuna.ats.jta.logging.jtaLogger;

/**
 * Whenever a synchronization is registered, an instance of this class
 * is used to wrap it.
 *
 * @author Mark Little (mark@arjuna.com)
 * @version $Id: SynchronizationImple.java 2342 2006-03-30 13:06:17Z  $
 * @since JTS 1.2.4.
 */

public class SynchronizationImple implements SynchronizationRecord, Comparable
{

    public SynchronizationImple (jakarta.transaction.Synchronization ptr)
    {
	_theSynch = ptr;
	_theUid = new Uid();
    }

	public SynchronizationImple (jakarta.transaction.Synchronization ptr, boolean isInterposed) {
		_theSynch = ptr;
		_theUid = new Uid();
		_isInterposed = isInterposed;
	}

    public Uid get_uid ()
    {
	return _theUid;
    }

    public boolean beforeCompletion ()
    {
	if (jtaLogger.logger.isTraceEnabled()) {
        jtaLogger.logger.trace("SynchronizationImple.beforeCompletion - Class: " + _theSynch.getClass() + " HashCode: " + _theSynch.hashCode() + " toString: " + _theSynch);
    }

	if (_theSynch != null)
	{
		_theSynch.beforeCompletion();
		return true;
		// Don't catch and swallow unchecked exceptions here, they may be useful to the caller.
	}
	else
	    return false;
    }

    public boolean afterCompletion (int status)
    {
        if (jtaLogger.logger.isTraceEnabled()) {
            jtaLogger.logger.trace("SynchronizationImple.afterCompletion - Class: " + _theSynch.getClass() + " HashCode: " + _theSynch.hashCode() + " toString: " + _theSynch);
        }

        if (_theSynch != null)
        {
            int s = StatusConverter.convert(status);

            try
            {
                _theSynch.afterCompletion(s);

                return true;
            }
            catch (Exception e)
            {
                jtaLogger.i18NLogger.warn_resources_arjunacore_SynchronizationImple(_theSynch.toString(), e);

                return false; // should not cause any affect!
            }
        }
        else
            return false; // should not cause any affect!
    }

	/*
	 * SyncronizationsImples are ordered first according to the isInterposed property and then by Uid.
	 * Interposed synchronizations must come after non-interposed ones  (see TwoPhaseCoordinator and
	 * TransactionSynchronizationRegistry)  The ordering within the interposed/non-interposed categories
	 * is arbitrary but must be defined for correct functioning of the sort. Just don't rely on it in
	 * application level code.
	 *
	 * @param object
	 * @return
	 */
	public int compareTo(Object object)
	{
        // register synch but always after some other one
        //
		SynchronizationImple other = (SynchronizationImple)object;

		if(this._isInterposed && (!other._isInterposed))
		{
			return 1;
		}
		else if((!this._isInterposed) && other._isInterposed)
		{
			return -1;
		}
		else if(this._theUid.equals(other._theUid))
		{
			return 0;
		}
		else
		{
			return this._theUid.lessThan(other._theUid) ? -1 : 1;
		}
	}

    public String toString() {
        return "SynchronizationImple< "+_theUid.stringForm()+", "+_theSynch+" >";
    }

    public boolean isInterposed() {
        return _isInterposed;
    }

    private jakarta.transaction.Synchronization _theSynch;
    private Uid _theUid;
	private boolean _isInterposed;
}
