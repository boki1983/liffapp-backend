package com.jpmorgan.lineproject.service;

import com.jpmorgan.lineproject.domain.BusinessUnit;
import com.jpmorgan.lineproject.domain.SecurityHeader;
import com.jpmorgan.lineproject.form.LineUser;
import com.jpmorgan.lineproject.wsdl.*;
import lombok.Data;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.ClientHttpRequestMessageSender;


@Component
@Data
public class SfmcSOAPClient extends WebServiceGatewaySupport {
    private static final int CONNECT_TIMEOUT = (10 * 1000);
    private static final int READ_TIMEOUT = (10 * 1000);

    private String tokenStr;

    public SfmcSOAPClient() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory =
                new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        simpleClientHttpRequestFactory.setReadTimeout(READ_TIMEOUT);

        setMessageSender(new ClientHttpRequestMessageSender(simpleClientHttpRequestFactory));
    }

    public DataExtensionObject getCustomerMappingTableDataFromDataExtension(String uuid) {
        RetrieveRequest retrieveRequest = new RetrieveRequest();

        // Data Extension CustomerKey
        retrieveRequest.setObjectType("DataExtensionObject[CUSTOMER_MAPPING_TABLE]");

        // Fields of Data Extension
        retrieveRequest.getProperties().add("UUID");
        retrieveRequest.getProperties().add("JPM_CID");
        retrieveRequest.getProperties().add("LINE_ID");
        retrieveRequest.getProperties().add("LINE_ID_TOKEN");
        retrieveRequest.getProperties().add("FIRST_NAME");
        retrieveRequest.getProperties().add("LAST_NAME");
        retrieveRequest.getProperties().add("EMAIL");
        retrieveRequest.getProperties().add("MOBILE_NUMBER");
        retrieveRequest.getProperties().add("LOCALE");
        retrieveRequest.getProperties().add("TAGS");
        retrieveRequest.getProperties().add("CREATED_AT");
        retrieveRequest.getProperties().add("CREATED_BY");
        retrieveRequest.getProperties().add("UPDATED_AT");
        retrieveRequest.getProperties().add("UPDATED_BY");

        SimpleFilterPart simpleFilterPart = new SimpleFilterPart();
        simpleFilterPart.setProperty("UUID");
        simpleFilterPart.setSimpleOperator(SimpleOperators.EQUALS);
        simpleFilterPart.getValue().add(uuid);
        retrieveRequest.setFilter(simpleFilterPart);

        RetrieveRequestMsg retrieveRequestMsg = new RetrieveRequestMsg();
        retrieveRequestMsg.setRetrieveRequest(retrieveRequest);

        RetrieveResponseMsg responseMsg = (RetrieveResponseMsg) returnResponses(retrieveRequestMsg, "Retrieve");
        System.out.println("Response:: " + responseMsg.getOverallStatus());
        if (responseMsg.getResults() != null && responseMsg.getResults().size() > 0) {
            DataExtensionObject dataExtensionObject = (DataExtensionObject) responseMsg.getResults().get(0);
            return dataExtensionObject;
        }

        return null;
    }

    public String updateCustomerMappingTableDataToDataExtension(LineUser lineUser) {
        SaveOption option = new SaveOption();
        option.setPropertyName("DataExtensionObject");
        option.setSaveAction(SaveAction.UPDATE_ONLY);

        Options.SaveOptions saveOptions = new Options.SaveOptions();
        saveOptions.getSaveOption().add(option);

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.setSaveOptions(saveOptions);

        // update data extension Object
        DataExtensionObject dataExtensionObject = new DataExtensionObject();
        dataExtensionObject.setCustomerKey("CUSTOMER_MAPPING_TABLE");

        DataExtensionObject.Keys keys = new DataExtensionObject.Keys();

        APIProperty a1 = new APIProperty();// primary key in DE is UUID Column
        a1.setName("UUID");
        a1.setValue(lineUser.getUuid());
        keys.getKey().add(a1);
        dataExtensionObject.setKeys(keys);

        ObjectExtension.Properties properties = new ObjectExtension.Properties();
        APIProperty a2 = new APIProperty();  // updating LINE_ID field to new value
        a2.setName("LINE_ID");
        a2.setValue(lineUser.getLineUserId());
        properties.getProperty().add(a2);
        dataExtensionObject.setProperties(properties);

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setOptions(updateOptions);
        updateRequest.getObjects().add(dataExtensionObject);
        UpdateResponse updateResponse = (UpdateResponse) returnResponses(updateRequest, "Update");
        System.out.println("Response :: " + updateResponse.getOverallStatus());
        return updateResponse.getOverallStatus();
    }

    private Object returnResponses(Object object, String action) {
        return getWebServiceTemplate().marshalSendAndReceive(object,
                new SecurityHeader("", "", this.tokenStr, action));
    }

    private ClientID returnClientID(BusinessUnit businessUnit) {
        ClientID clientID = new ClientID();
        clientID.setClientID(businessUnit.getMid());
        return clientID;
    }

    private void setBusinessUnit(RetrieveRequest retrieveRequest) {
        final ClientID twDirectUnit = returnClientID(BusinessUnit.TAIWAN_DIRECT);
        retrieveRequest.getClientIDs().add(twDirectUnit);
    }

}
