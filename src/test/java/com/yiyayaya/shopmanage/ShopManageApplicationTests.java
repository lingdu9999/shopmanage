package com.yiyayaya.shopmanage;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.yiyayaya.shopmanage.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

@SpringBootTest
class ShopManageApplicationTests {

  @Test
  void verifyJwt() {
    System.out.println(new JwtUtil().getUserId("eyJhbGciOiJIUzI1NiJ9.eyJkYXRhIjoiMSIsImlhdCI6MTczOTAzODc2OSwiZXhwIjoxNzM5MTI1MTY5fQ.wuQbXPmjaicE1sC0tIguWNIwRhVjS11BUWdhnFEoeI0"));
  }

    @Test
    void contextLoads() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/shopmanage?useUnicode=true&characterEncoding=utf8&useSSL=true", "root", "123456")
            .globalConfig(builder -> {
                builder.author("mzy") // 设置作者
                    .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java"); // 指定输出目录
            })
            .dataSourceConfig(builder ->
                builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                })
            )
            .packageConfig(builder ->
                builder.parent("com.yiyayaya.shopmanage") // 设置父包名
                    .entity("entity")
                    .mapper("mapper")
                    .service("service")
                    .serviceImpl("service.impl")
                    .pathInfo(Collections.singletonMap(OutputFile.xml, Paths.get(System.getProperty("user.dir")) + "/src/main/java/resources/mapper")) // 设置mapperXml生成路径
            )
            .strategyConfig(builder ->builder.entityBuilder().enableLombok())
            .templateEngine(new FreemarkerTemplateEngine())
            .execute();
    }

}
