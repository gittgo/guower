package com.ourslook.guower.webservice;

import org.apache.log4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Use;

/**
 *
 * JFinal
 *
 * @see sun-jaxws.xml
 *
 * @author teamo.li
 * @version V1.0
 * @Title: DemoSoapService.java
 * @Package com.web.print.webservice.DemoSoapService
 * @Description: Webservice，提供：震旦Erp接口，编号：AUR3D15
 * @date 2016年9月10日 下午13:00
 */
@WebService(name = "Aurora3DSoap", targetNamespace = "http://www.ourslook.com/", serviceName = "Aurora3DSoapToERP")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = Use.LITERAL)
public class DemoSoapService {
    private static final Logger LOGGER = Logger.getLogger(DemoSoapService.class);

    @WebMethod(operationName = "AUR3D15", action = "http://www.ourslook.com/AUR3D15", exclude = false)
    @WebResult(name = "AUR3D15Result")
    public String AUR3D15(@WebParam(name = "SponsorXML", mode = Mode.IN, header = false) String SponsorXML) {
        LOGGER.info("接收参数：" + SponsorXML);
        return "接收参数:" + SponsorXML;
    }
}
