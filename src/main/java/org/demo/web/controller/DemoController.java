package org.demo.web.controller;

import org.demo.db.repository.RuleBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by barry.wong
 */

@Controller
@RequestMapping("/demo")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    RuleBucket ruleBucket;

    @RequestMapping(method = RequestMethod.GET)
    public String demo(Model model) {
        return "demo";
    }

}
