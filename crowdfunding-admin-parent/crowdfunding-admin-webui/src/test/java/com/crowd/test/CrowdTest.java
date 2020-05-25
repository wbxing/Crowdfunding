package com.crowd.test;

import com.crowd.entity.Admin;
import com.crowd.mapper.AdminMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-persist-mybatis.xml")
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Test
    public void testLog() {
        // 获取 Logger 对象，传入 Class 对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        // 根据不同的日志级别来控制日志打印
        logger.debug("Debug Level!!!");
        logger.debug("Debug Level!!!");
        logger.debug("Debug Level!!!");

        logger.info("Info Level!!!");
        logger.info("Info Level!!!");
        logger.info("Info Level!!!");

        logger.warn("Warn Level!!!");
        logger.warn("Warn Level!!!");
        logger.warn("Warn Level!!!");

        logger.error("Error Level!!!");
        logger.error("Error Level!!!");
        logger.error("Error Level!!!");

    }

    @Test
    public void testInsert() {
        Admin admin = new Admin(null, "tom", "123123", "汤姆", "tom@qq.com", null);
        int insert = adminMapper.insert(admin);
        System.out.println("受影响的行数 = " + insert);
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
