/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
 * (C) 2007,
 * @author JBoss Inc.
 */
package com.arjuna.mw.wst11.client;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import com.arjuna.mw.wstx.logging.wstxLogger;
import com.arjuna.webservices11.wscoor.CoordinationConstants;

/**
 * The class is used to perform WS-Transaction context insertion
 * and extraction for application level SOAP messages using JaxWS.
 * This is the client side version.
 */
public class JaxWSHeaderContextProcessor extends JaxBaseHeaderContextProcessor implements SOAPHandler<SOAPMessageContext>
{
    /**
     * Process a message. Determines if it is inbound or outbound and dispatches accordingly.
     *
     * @param msgContext
     * @return true
     */
    public boolean handleMessage(SOAPMessageContext msgContext)
    {
        return handleMessage(msgContext, true);
    }

    /**
     * Process a message. Determines if it is inbound or outbound and dispatches accordingly.
     *
     * @param msgContext
     * @param mustUnderstand
     * @return true
     */
    public boolean handleMessage(SOAPMessageContext msgContext, boolean mustUnderstand) {
        Boolean outbound = (Boolean)msgContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound == null)
            throw new IllegalStateException("Cannot obtain required property: " + MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        return outbound ? handleOutbound(msgContext, mustUnderstand) : handleInbound(msgContext);
    }

    /**
     * Tidy up the Transaction/Thread association.
     *
     * @param messageContext
     * @return true
     */
    public boolean handleFault(SOAPMessageContext messageContext)
    {
        if (wstxLogger.logger.isTraceEnabled()) {
            wstxLogger.logger.trace("client/JaxWSHeaderContextProcessor.handleFault()");
            wstxLogger.traceMessage(messageContext);
        }

        final SOAPMessageContext soapMessageContext = (SOAPMessageContext)messageContext ;
        final SOAPMessage soapMessage = soapMessageContext.getMessage() ;

        resumeTransaction(soapMessage);
        return true;
    }

    public void close(MessageContext messageContext)
    {
    }

    /**
     * Gets the header blocks that can be processed by this Handler instance.
     */
    public Set<QName> getHeaders()
    {
        Set<QName> headerSet = new HashSet<QName>();
        headerSet.add(new QName(CoordinationConstants.WSCOOR_NAMESPACE, CoordinationConstants.WSCOOR_ELEMENT_COORDINATION_CONTEXT));

        return headerSet;
    }

    /**
     * Sets the header blocks that can be processed by this Handler instance.
     * Note: this impl ignores this function's args as the values are hardcoded.
     */
    public void setHeaders(Set headers)
    {
    }

    /**
     * Tidy up the Transaction/Thread association before control is returned to the user.
     *
     * @param messageContext
     * @return true
     */
    protected boolean handleInbound(SOAPMessageContext messageContext)
    {
        if (wstxLogger.logger.isTraceEnabled()) {
            wstxLogger.logger.trace("client/JaxWSHeaderContextProcessor.handleInbound()");
            wstxLogger.traceMessage(messageContext);
        }

        final SOAPMessage soapMessage = messageContext.getMessage() ;

        resumeTransaction(soapMessage);
        return true ;
    }

    /**
     * Process the tx thread context and attach serialized version as msg header
     *
     * @param messageContext
     * @param mustUnderstand
     * @return true
     */
    protected boolean handleOutbound(SOAPMessageContext messageContext, boolean mustUnderstand)
    {
        if (wstxLogger.logger.isTraceEnabled()) {
            wstxLogger.logger.trace("client/JaxWSHeaderContextProcessor.handleOutbound()");
            wstxLogger.traceMessage(messageContext);
        }

        final SOAPMessage soapMessage = messageContext.getMessage() ;

        return handleOutboundMessage(soapMessage, mustUnderstand);
    }
}
