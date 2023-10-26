package com.ctsi.ssdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class KafkaUIApp
{
    public static void main( String[] args )
    {
//        SpringApplication app = new SpringApplication(KafkaUIApp.class);
//        Environment env = app.run(args).getEnvironment();
        System.out.println( "Hello World!" );
    }
}
