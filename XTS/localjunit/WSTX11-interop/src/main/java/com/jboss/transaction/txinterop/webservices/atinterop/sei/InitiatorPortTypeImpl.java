/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package com.jboss.transaction.txinterop.webservices.atinterop.sei;

import com.jboss.transaction.txinterop.webservices.atinterop.processors.ATInitiatorProcessor;
import com.arjuna.webservices11.SoapFault11;
import org.jboss.ws.api.addressing.MAP;
import com.arjuna.webservices11.wsaddr.AddressingHelper;
import org.xmlsoap.schemas.soap.envelope.Fault;

import jakarta.xml.ws.Action;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.annotation.Resource;

/**
 * Implementor class for OASIS WS-Interop 1.1 Initiator Service
 */
import jakarta.jws.Oneway;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.WebParam;
import jakarta.jws.soap.SOAPBinding;

/**
 * Implementation class for WSTX 1.1 AT Interop Test Initiator service
 */
@WebService(name = "InitiatorPortType",
        targetNamespace = "http://fabrikam123.com",
        portName = "InitiatorPortType",
        // wsdlLocation="/WEB-INF/wsdl/interopat-initiator-binding.wsdl",
        serviceName="InitiatorService")
@Addressing(required=true)
public class InitiatorPortTypeImpl // implements InitiatorPortType, SoapFaultPortType
{

    /**
     * injected resource providing access to WSA addressing properties
     */
    @Resource
    private WebServiceContext webServiceCtx;

    /**
     *
     */
    @WebMethod(operationName = "Response", action = "http://fabrikam123.com/Response")
    @Oneway
    @Action(input="http://fabrikam123.com/Response")
    @RequestWrapper(localName = "Response", targetNamespace = "http://fabrikam123.com", className = "com.jboss.transaction.txinterop.webservices.atinterop.generated.TestMessageType")
    public void response()
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        MAP inboundMap = AddressingHelper.inboundMap(ctx);

        ATInitiatorProcessor.getInitiator().handleResponse(inboundMap) ;
    }

    @WebMethod(operationName = "SoapFault", action = "http://fabrikam123.com/SoapFault")
    @Oneway
    @Action(input="http://fabrikam123.com/SoapFault")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public void soapFault(
            @WebParam(name = "Fault", targetNamespace = "http://schemas.xmlsoap.org/soap/envelope/", partName = "parameters")
            Fault fault)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        MAP inboundMap = AddressingHelper.inboundMap(ctx);

        SoapFault11 soapFaultInternal = SoapFault11.fromFault(fault);
        ATInitiatorProcessor.getInitiator().handleSoapFault(soapFaultInternal, inboundMap) ;
    }

}
