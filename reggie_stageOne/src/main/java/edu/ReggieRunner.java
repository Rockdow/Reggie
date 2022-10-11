package edu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieRunner {
    public static void main(String[] args) {
        /*使用lombok中的slf4j标签可以获取log变量，从而打印日志信息*/
        log.info("启动SpringBoot");
        SpringApplication.run(ReggieRunner.class,args);
    }
}
