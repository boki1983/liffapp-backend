package com.jpmorgan.lineproject.wsdl;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = Security.SECURITY_NS)
@Data
public class Security {
    public static final String SECURITY_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    @XmlElement(namespace = Security.SECURITY_NS, name = "UsernameToken")
    private UsernameToken usernameToken;

    public Security() {

    }

    public Security(UsernameToken usernameToken) {
        this.usernameToken = usernameToken;
    }
}
