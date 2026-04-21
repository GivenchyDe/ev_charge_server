package org.example.ev_charge_server;

import com.baomidou.mybatisplus.generator.config.OutputFile;
import java.util.Collections;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;



public class CodeGenerator {
    public static void main(String[] args) {
        // 1. 数据库配置（只需要改这里的密码！）
        String url = "jdbc:mysql://localhost:3306/evcsmp?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456"; // 改成你自己的数据库密码

        // 2. 你的12张表（已全部填好，自动跳过admin表）
        String[] tables = {
                "announcement",
                "billing_rule",
                "charger",
                "charging_order",
                "chat_message",
                "feedback",
                "gun",
                "message",
                "order_review",
                "recharge_order",
                "station",
                "user"
        };

        FastAutoGenerator.create(url, username, password)
                // 3. 全局配置（完全适配你的项目路径）
                .globalConfig(builder -> {
                    builder.outputDir("D:\\Develop\\project\\evcsmp\\ev_charge_server\\src\\main\\java")
                            .author("evcsmp-admin") // 可以改成你的名字
                            .disableOpenDir();
                })

                // 4. 包配置（完全适配你的项目结构）
                .packageConfig(builder -> {
                    builder.parent("org.example.ev_charge_server")
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.xml,
                                    "D:\\Develop\\project\\evcsmp\\ev_charge_server\\src\\main\\resources\\mapper"
                            ));
                })

                // 5. 生成策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 生成上面的12张表
                            .addExclude("admin") // 跳过你已有的admin表，不会覆盖

                            // 实体类配置：自动加Lombok、字段注解
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()

                            // 控制器配置：自动加@RestController
                            .controllerBuilder()
                            .enableRestStyle()

                            // Mapper配置：自动加@Mapper注解
                            .mapperBuilder()
                            .enableMapperAnnotation();
                })

                // 6. 模板引擎（必须和pom里的freemarker依赖对应）
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

        System.out.println("✅ 所有表代码生成完成！");
        System.out.println("👉 请去以下目录查看：");
        System.out.println("实体类：org.example.ev_charge_server.entity");
        System.out.println("Mapper：org.example.ev_charge_server.mapper");
        System.out.println("Service：org.example.ev_charge_server.service");
        System.out.println("Controller：org.example.ev_charge_server.controller");
    }
}