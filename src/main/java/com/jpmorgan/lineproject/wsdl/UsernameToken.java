package com.jpmorgan.lineproject.wsdl;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = Security.SECURITY_NS)
@Data
public class UsernameToken {

    @XmlElement(namespace = Security.SECURITY_NS, name = "Username")
    private String username;

    @XmlElement(namespace = Security.SECURITY_NS, name = "Password")
    private String password;

    public UsernameToken() {

    }

    public UsernameToken(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
