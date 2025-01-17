package com.arjuna.mw.wst11.service;

import com.arjuna.mw.wstx.logging.wstxLogger;

import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.soap.SOAPMessage;

/**
 * specialised version which creates and interposes a subordinate AT transaction when
 * it finds an incoming AT context in the message headers
 */
public class JaxWSSubordinateHeaderContextProcessor extends JaxWSHeaderContextProcessor
{
    /**
     * Process the tx context header that is attached to the received message.
     *
     * @param msgContext
     * @return true
     */
    protected boolean handleInbound(SOAPMessageContext msgContext)
    {
        if (wstxLogger.logger.isTraceEnabled()) {
            wstxLogger.logger.trace("service/JaxWSSubordinateHeaderContextProcessor.handleInbound()");
            wstxLogger.traceMessage(msgContext);
        }

        final SOAPMessageContext soapMessageContext = (SOAPMessageContext)msgContext ;
        final SOAPMessage soapMessage = soapMessageContext.getMessage() ;

        // the generic handler can do the job for us -- just pass the correct flag

        return handleInboundMessage(soapMessage, true);
    }

}
