package hello;

import hello.config.AppConfiguration;
import org.springframework.stereotype.Component;

@Component
public class DependencyInjectionExample {
    private final AppConfiguration appConfiguration;


    public DependencyInjectionExample(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public String getHelloValue(){
        return appConfiguration.getHelloValue();
    }
}
