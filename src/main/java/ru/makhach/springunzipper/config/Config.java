package ru.makhach.springunzipper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.makhach.springunzipper.executors.CancelledTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class Config {
    @Bean
    public ThreadPoolExecutor executor() {
        return new CancelledTaskExecutor(2, 5, 0);
    }
}
