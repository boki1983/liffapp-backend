package com.jpmorgan.lineproject.service;

import com.jpmorgan.lineproject.form.LineUser;
import org.springframework.http.HttpStatus;

public interface DataExtensionService {
    HttpStatus updateLineIdMappingTable(LineUser lineUser);
}
