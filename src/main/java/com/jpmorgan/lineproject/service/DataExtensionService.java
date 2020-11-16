package com.jpmorgan.lineproject.service;

import com.jpmorgan.lineproject.form.LineUser;
import org.springframework.http.HttpStatus;

public interface DataExtensionService {
    HttpStatus updateMappingTable(LineUser lineUser);
}
