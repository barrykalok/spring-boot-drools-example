package org.demo.web.controller;

import org.demo.db.entity.Rule;
import org.demo.db.repository.RuleBucket;
import org.demo.rule.RuleService;
import org.demo.web.model.rule.RuleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by barry.wong
 */
@Controller
@Transactional(readOnly = true)
@RequestMapping("/api/v1/rule")
public class RuleController {

    private static final Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
    RuleBucket ruleBucket;

    @Autowired
    RuleService ruleService;

    @Transactional(readOnly = false)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    Rule create(@Valid @RequestBody RuleRequest req) {

        Rule rule = new Rule();
        rule.setContent(req.getContent());
        rule.setName(req.getName());
        checkRule(rule);

        ruleBucket.save(rule);
        return rule;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Rule get(@PathVariable("id") String id) {
        return ruleBucket.findOne(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Rule> list() {
        List<Rule> rules = ruleBucket.findAll();
        return rules;
    }

    @Transactional(readOnly = false)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    void delete(@PathVariable("id") String id) {
        ruleBucket.delete(id);
    }

    @Transactional(readOnly = false)
    @RequestMapping(value = "/container/{container}/refresh", method = RequestMethod.POST)
    public
    @ResponseBody
    void refresh(@PathVariable("container") String container) {
        ruleService.refreshContainer(container);
    }

    private void checkRule(Rule rule) {
        Rule exist = ruleBucket.findByName(rule.getName());
        if (exist != null) {
            throw new DuplicateRuleNameException(rule.getName());
        }
    }
}
