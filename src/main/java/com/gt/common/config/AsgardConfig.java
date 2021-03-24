package com.gt.common.config;

import com.gt.asgard.Thor;
import com.gt.asgard.postprocess.HeimdallService;
import com.gt.asgard.preprocess.LokiService;
import com.gt.sokovia.service.Wanda;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsgardConfig {

    @Bean
    public Thor thor() {
        return new Thor();
    }

    @Bean
    public HeimdallService heimdallService(Wanda database) {
        return new HeimdallService(database);
    }

    @Bean
    public LokiService lokiService(Wanda database, Thor engine) {
        return new LokiService(database, engine, heimdallService(database));
    }

}
