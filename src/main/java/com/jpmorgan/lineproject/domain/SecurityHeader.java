package com.jpmorgan.lineproject.domain;

import lombok.Data;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;

@Data
public class SecurityHeader implements WebServiceMessageCallback {
    private String soapAction;
    private String username;
    private String password;
    private String token;

    public SecurityHeader(String username, String password, String token, String soapAction) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.soapAction = soapAction;
    }


    @Override
    public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException {
        try {
            SoapMessage soapMessage = (SoapMessage) webServiceMessage;
            soapMessage.setSoapAction(soapAction);

            SoapHeader soapHeader = soapMessage.getSoapHeader();
            String authentication = null;

            if (token == null) {
                authentication = "<Security xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
                        "<UsernameToken>\n" +
                        "<Username>" + username + "</Username>\n" +
                        "<Password>" + password + "</Password>\n" +
                        "</UsernameToken>\n" +
                        "</Security>";
            } else {
                authentication = "<fueloauth>" + token + "</fueloauth>";
            }

            StringSource stringSource = new StringSource(authentication);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(stringSource, soapHeader.getResult());
        } catch (Exception e) {
            throw new IOException("error when marshalling authentication.", e);
        }
    }
}
