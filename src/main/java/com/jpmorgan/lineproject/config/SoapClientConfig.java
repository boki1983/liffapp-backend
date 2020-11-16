package com.jpmorgan.lineproject.config;

import com.jpmorgan.lineproject.service.SfmcSOAPClient;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();

        String[] packageToScan = {"com.jpmorgan.lineproject.wsdl", "com.jpmorgan.lineproject.service"};
        jaxb2Marshaller.setPackagesToScan(packageToScan);

        return jaxb2Marshaller;
    }

    @Bean
    public SfmcSOAPClient soapClient(Jaxb2Marshaller jaxb2Marshaller) {
        SfmcSOAPClient sfmcSOAPClient = new SfmcSOAPClient();
        sfmcSOAPClient.setDefaultUri("https://webservice.s7.exacttarget.com/Service.asmx");
        sfmcSOAPClient.setMarshaller(jaxb2Marshaller);
        sfmcSOAPClient.setUnmarshaller(jaxb2Marshaller);
        return sfmcSOAPClient;
    }
}
