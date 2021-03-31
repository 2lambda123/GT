package com.bifrost.common.config;

import com.bifrost.asgard.Thor;
import com.bifrost.asgard.ThorThread;
import com.bifrost.asgard.postprocess.HeimdallService;
import com.bifrost.asgard.preprocess.LokiService;
import com.bifrost.asgard.preprocess.LokiThread;
import com.bifrost.sokovia.service.BifrostThread;
import com.bifrost.sokovia.service.Wanda;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import redis.clients.jedis.Jedis;

@Log
@Configuration
public class AsgardConfig {

    @Bean
    public Jedis jedis() {
       return new Jedis("redis");
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
    }

    @Bean
    public CommandLineRunner bifrostRunner(TaskExecutor executor) {
        log.info("Starting Bifrost");
        return args -> executor.execute(new BifrostThread());
    }

    @Bean
    public CommandLineRunner thorRunner(TaskExecutor executor) {
        log.info("Starting Thor");
        return args -> executor.execute(new ThorThread());
    }

    @Bean
    public CommandLineRunner lokiRunner(TaskExecutor executor) {
        log.info("Starting Loki");
        return args -> executor.execute(new LokiThread());
    }

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
