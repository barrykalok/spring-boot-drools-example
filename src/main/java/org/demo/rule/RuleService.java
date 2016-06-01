package org.demo.rule;

import org.demo.db.repository.RuleBucket;
import org.demo.util.RuleEngineUtil;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by barry.wong
 */
@Service
public class RuleService {

    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

    @Autowired
    KieServices kieServices;

    @Autowired
    RuleBucket ruleBucket;

    private ConcurrentHashMap<String, KieContainer> containerMap = new ConcurrentHashMap<>();

    String ruleSetName = "demo-rules";

    @PostConstruct
    void init() {
        logger.info("initializing rule engine");
        // FIXME: should support more than "hk-rules"
        logger.info("setting up {}", ruleBucket);
        refreshContainer(ruleSetName);
    }

    public void refreshContainer(String containerName){

        logger.info("refreshing container {}", containerName);

        ReleaseId releaseId = new ReleaseIdImpl("org.demo.rule", containerName, "1.0.0-" + UUID.randomUUID().toString().replaceAll("-", ""));

        // retrieve rules from db
        List<String> rules = ruleBucket.findAll().stream().map((r -> r.getContent())).collect(Collectors.toList());
        logger.info("total {} rules for [{}]", rules.size(), ruleBucket);
        Resource resource = kieServices.getResources().newByteArrayResource(RuleEngineUtil.createKJar(releaseId, rules));

        KieModule module = kieServices.getRepository().addKieModule(resource);
        KieContainer kieContainer = kieServices.newKieContainer(module.getReleaseId());

        // create container is heavy, we only create once and cache it
        containerMap.put(containerName, kieContainer);

        // remove the repo immediately
        kieServices.getRepository().removeKieModule(module.getReleaseId());
    }

    public KieSession getKieSession(String containerName) {
        KieContainer container = containerMap.get(containerName);
        if (container == null) {
            throw new NoSuchContainerException(containerName);
        }
        return container.newKieSession();
    }

}