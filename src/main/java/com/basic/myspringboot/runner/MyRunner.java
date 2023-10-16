package com.basic.myspringboot.runner;

import com.basic.myspringboot.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class MyRunner implements ApplicationRunner {
    @Value("${myboot.name}")
    private String name;

    @Value("${myboot.age}")
    private  int age;

    @Value("${myboot.fullName}")
    private  String fullName;

    @Autowired
    private Environment environment;

    @Autowired
    private Customer customer;

    Logger logger= LoggerFactory.getLogger(MyRunner.class);       //이 로그가 어디서 찍히는지 알기 위해 클래스를 넣어줌

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Port Number = "+environment.getProperty("local.server.port"));
        logger.info("myboot.name = "+name);
        logger.info("myboot.age = "+age);
        logger.info("myboot.fullName = "+fullName);
        logger.info("Customer name : {}",customer.getName());    // 로그찍을 때 {}를 넣고 싶은자리 설정하고 ,뒤에 변수
        
        logger.debug("VM Argument foo = {} , Program Argument bar = {}"
                ,args.containsOption("foo")
                ,args.containsOption("bar"));

        logger.info("로커 클래스 이름 {}",logger.getClass().getName());

    }
}
