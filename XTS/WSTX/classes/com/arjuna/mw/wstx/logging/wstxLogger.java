/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. 
 * See the copyright.txt in the distribution for a full listing 
 * of individual contributors.
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
 * Copyright (C) 2005,
 *
 * Arjuna Technologies Ltd,
 * Newcastle upon Tyne,
 * Tyne and Wear,
 * UK.
 *
 * $Id: wstxLogger.java,v 1.2 2005/05/19 12:13:40 nmcl Exp $
 */

package com.arjuna.mw.wstx.logging;

import org.jboss.logging.Logger;

import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class wstxLogger
{
    public static final Logger logger = Logger.getLogger("com.arjuna.mw.wstx");
    public static final wstxI18NLogger i18NLogger = Logger.getMessageLogger(wstxI18NLogger.class, "com.arjuna.mw.wstx");

    /**
     * Print the content of the SOAP message.
     *
     * @param soapMessageContext  SOAP message context to extract and log the message content from
     */
    public static final void traceMessage(SOAPMessageContext soapMessageContext) {
        SOAPMessage soapMessage = ((SOAPMessageContext) soapMessageContext).getMessage();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            soapMessage.writeTo(baos);
            logger.trace(baos);
        } catch (IOException | SOAPException e) {
            logger.trace("Failure on logging content of the SOAP message " + soapMessage, e);
        }
    }
}
