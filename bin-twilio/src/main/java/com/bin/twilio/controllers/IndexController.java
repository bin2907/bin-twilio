/*
 * Copyright 2011-2017 Nakivo Inc.
 * ALL RIGHTS RESERVED.
 *
 * PROPRIETARY/CONFIDENTIAL.
 *
 * This software is the confidential and proprietary information
 * of Company Inc.
 *
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Company Inc.
 */
package com.bin.twilio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: Binh
 * Date: 18.03.2017
 * Time: 18:28
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
