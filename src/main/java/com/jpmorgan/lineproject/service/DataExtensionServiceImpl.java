package com.jpmorgan.lineproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmorgan.lineproject.config.SoapClientConfig;
import com.jpmorgan.lineproject.domain.BusinessUnit;
import com.jpmorgan.lineproject.domain.Token;
import com.jpmorgan.lineproject.form.LineUser;
import com.jpmorgan.lineproject.wsdl.APIProperty;
import com.jpmorgan.lineproject.wsdl.DataExtensionObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.jpmorgan.lineproject.constant.LineConstant.LINE_AUTH_URL;
import static com.jpmorgan.lineproject.constant.LineConstant.LINE_CHANNEL_ID;

@Service
public class DataExtensionServiceImpl implements DataExtensionService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public HttpStatus updateLineIdMappingTable(LineUser lineUser) {
        String lineUserId = lineUser.getLineUserId();
        if (StringUtils.isEmpty(lineUserId)) {
            lineUserId = getLineUserID(lineUser);
            lineUser.setLineUserId(lineUserId);
        }

        DataExtensionObject dataExtensionObject = getCustomerMappingTableDataRow(lineUser.getUuid());

        if (dataExtensionObject == null) {
            return HttpStatus.NOT_FOUND;
        } else {
            List<APIProperty> APIProperties = dataExtensionObject.getProperties().getProperty();
            for (APIProperty apiProperty : APIProperties) {
                if ("LINE_ID".equalsIgnoreCase(apiProperty.getName())) {
                    if (!StringUtils.isEmpty(apiProperty.getValue())) {
                        return HttpStatus.CONFLICT;
                    }
                }
            }

            return "OK".equalsIgnoreCase(updateCustomerLineIdMappingTable(lineUser)) ?
                    HttpStatus.OK : HttpStatus.NO_CONTENT;
        }
    }

    private String getLineUserID(LineUser lineUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", LINE_CHANNEL_ID);
        map.add("id_token", lineUser.getUserToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(LINE_AUTH_URL, request , String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("sub").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String updateCustomerLineIdMappingTable(LineUser lineUser) {
        SfmcSOAPClient sfmcSOAPClient = returnSoapClient(BusinessUnit.TAIWAN_DIRECT);
        return sfmcSOAPClient.updateCustomerMappingTableDataToDataExtension(lineUser);
    }

    private DataExtensionObject getCustomerMappingTableDataRow(String uuid) {
        SfmcSOAPClient sfmcSOAPClient = returnSoapClient(BusinessUnit.TAIWAN_DIRECT);
        DataExtensionObject dataExtensionObject = sfmcSOAPClient.getCustomerMappingTableDataRowFromDataExtension(uuid);
        System.out.println(dataExtensionObject);
        return dataExtensionObject;
    }

    private String getToken(BusinessUnit businessUnit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "https://mc25p3chm7s3wj1n3804x5t7p-74.auth.marketingcloudapis.com/v2/token";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("grant_type", "client_credentials");
        paramMap.add("client_id", businessUnit.getClientId());
        paramMap.add("client_secret", businessUnit.getClientSecret());

        Token token = restTemplate.postForObject(url, paramMap, Token.class);
        System.out.println("result1==================" + token);
        return token.getAccess_token();
    }

    private SfmcSOAPClient returnSoapClient(final BusinessUnit businessUnit) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext
                = new AnnotationConfigApplicationContext(SoapClientConfig.class);
        SfmcSOAPClient sfmcSOAPClient = annotationConfigApplicationContext.getBean(SfmcSOAPClient.class);
        sfmcSOAPClient.setTokenStr(getToken(businessUnit));
        return sfmcSOAPClient;
    }
}
