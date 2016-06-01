package org.demo.web.controller;

import org.demo.db.repository.RuleBucket;
import org.demo.rule.RulePlaceholder;
import org.demo.rule.RuleService;
import org.demo.web.model.validate.ValidateCandidateRequest;
import org.demo.web.model.validate.ValidateCandidateResponse;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by barry.wong
 */

@Controller
@Transactional(readOnly = true)
@RequestMapping("/api/v1/validate")
public class ValidateController {

    private static final Logger logger = LoggerFactory.getLogger(ValidateController.class);

    @Autowired
    RuleBucket ruleBucket;

    @Autowired
    RuleService ruleService;

    @Transactional(readOnly = false)
    @RequestMapping(value = "/{container}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ValidateCandidateResponse validateCandidate(@PathVariable("container") String container, @Valid @RequestBody ValidateCandidateRequest req) {
        KieSession session = ruleService.getKieSession(container);

        RulePlaceholder candidate = new RulePlaceholder(req.getDetail());

        session.insert(candidate);
        session.fireAllRules();
        session.dispose();

        candidate.getResult().forEach((k, v) -> {
            logger.info(k + " : " + v);
        });

        ValidateCandidateResponse resp = new ValidateCandidateResponse();
        resp.setDetail(candidate.getResult());
        resp.setAnswer(candidate.getResult().size() == 0);

        return resp;
    }
}
