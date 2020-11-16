package com.jpmorgan.lineproject.controller;

import com.jpmorgan.lineproject.form.LineUser;
import com.jpmorgan.lineproject.service.DataExtensionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class DataExtensionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataExtensionController.class);

    @Autowired
    private DataExtensionService dataExtensionService;

    @RequestMapping(method = RequestMethod.PUT,
    value = "/updateMappingTable",
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateMappingTable(@RequestBody LineUser lineUser) {
        HttpStatus statusCode = dataExtensionService.updateMappingTable(lineUser);
        return ResponseEntity.status(statusCode).build();
    }

}
