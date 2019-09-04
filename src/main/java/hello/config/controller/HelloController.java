package hello.config.controller;

import hello.DependencyInjectionExample;
import hello.config.FileConfiguration;
import hello.model.EPSClaim;
import hello.utils.CSVDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    private final DependencyInjectionExample dependencyInjectionExample;

   @Autowired
   private FileConfiguration fileConfiguration;

    public HelloController(DependencyInjectionExample dependencyInjectionExample) {
        this.dependencyInjectionExample = dependencyInjectionExample;
    }

    @RequestMapping("/")
    public String index(){
        CSVDataLoader csvDataLoader = new CSVDataLoader();
        csvDataLoader.loadObjectList(EPSClaim.class, fileConfiguration.getFileName());

        return dependencyInjectionExample.getHelloValue();
    }
}
