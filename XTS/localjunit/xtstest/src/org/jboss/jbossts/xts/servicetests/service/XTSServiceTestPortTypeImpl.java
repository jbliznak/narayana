/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
 * (C) 2009,
 * @author JBoss Inc.
 */

package org.jboss.jbossts.xts.servicetests.service;

import org.jboss.jbossts.xts.servicetests.generated.ObjectFactory;
import org.jboss.jbossts.xts.servicetests.generated.ResultsType;
import org.jboss.jbossts.xts.servicetests.generated.CommandsType;
import org.jboss.jbossts.xts.servicetests.generated.XTSServiceTestPortType;

import jakarta.jws.*;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * A general purpose web service used to test the WSAT and WSBA services. It implements
 * a single service method which accepts a command list and returns a reesult list. This
 * can be used to register participants and script their behaviour.  
 */
@WebService(targetNamespace = "http://jbossts.jboss.org/xts/servicetests/generated",
        wsdlLocation = "WEB-INF/wsdl/xtsservicetests.wsdl",
        serviceName = "XTSServiceTestService",
        portName = "XTSServiceTestPortType",
        name = "XTSServiceTestPortType"
        )
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
// @EndpointConfig(configName = "Standard WSAddressing Endpoint")
@HandlerChain(file="handlers.xml")
@XmlSeeAlso({
    ObjectFactory.class
})
public class XTSServiceTestPortTypeImpl implements XTSServiceTestPortType
{
    protected @Resource WebServiceContext context;

    /**
     *
     * @param commands
     * @return
     *     returns org.jboss.jbossts.xts.servicetests.generated.ResultsType
     */
    @WebMethod
    @WebResult(name = "results", targetNamespace = "http://jbossts.jboss.org/xts/servicetests/generated", partName = "results")
    public ResultsType serve(
        @WebParam(name = "commands", targetNamespace = "http://jbossts.jboss.org/xts/servicetests/generated", partName = "commands")
        CommandsType commands)
    {
        ResultsType results = new ResultsType();
        List<String> resultsList = results.getResultList();
        List<String> commandList = commands.getCommandList();

        MessageContext messageContext = context.getMessageContext();
        HttpServletRequest servletRequest = ((HttpServletRequest)messageContext.get(MessageContext.SERVLET_REQUEST));
        String path = servletRequest.getServletPath();

        System.out.println("service " + path);
        for (String s : commandList)
        {
            System.out.println("  command " + s);
        }

        XTSServiceTestInterpreter service = XTSServiceTestInterpreter.getService(path);
        service.processCommands(commandList, resultsList);

        return results;
    }
}
