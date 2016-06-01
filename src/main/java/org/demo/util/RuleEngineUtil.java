package org.demo.util;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;

import java.util.List;

/**
 * Created by barry.wong
 */
public class RuleEngineUtil {

    public static byte[] createKJar(ReleaseId releaseId, List<String> drls) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        kfs.generateAndWritePomXML(releaseId);
        drls.forEach(drl -> kfs.write("src/main/resources/" + drl.hashCode() + ".drl", drl));
        KieBuilder kb = kieServices.newKieBuilder(kfs).buildAll();
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            for (Message result : kb.getResults().getMessages()) {
                System.out.println(result.getText());
            }
            return null;
        }
        InternalKieModule kieModule = (InternalKieModule) kieServices.getRepository().getKieModule(releaseId);
        byte[] jar = kieModule.getBytes();
        return jar;
    }
}
