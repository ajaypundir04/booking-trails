package com.element.insurance.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.TimeZone;


@SpringBootApplication
@EnableSwagger2
public class ApplicationLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLauncher.class);

    public static void main(String[] args) throws UnknownHostException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        ConfigurableApplicationContext run = SpringApplication.run(ApplicationLauncher.class, args);
        Environment env = run.getEnvironment();
        String serverPort = env.getProperty("server.port");

        LOGGER.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "Swagger: \thttp://localhost:{}/swagger-ui/index.html\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                serverPort,
                serverPort,
                InetAddress.getLocalHost().getHostAddress(), serverPort);

        LOGGER.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));

    }
}
