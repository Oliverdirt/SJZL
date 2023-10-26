package com.ctsi.ssdc.config;

import com.bstek.ureport.definition.datasource.BuildinDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Ureport报表数据源 
 */
@Component
public class UreportDataSource implements BuildinDatasource {

    @Resource
    private DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Ureport 数据源 获取连接失败！");
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String name() {
        //此处为数据源的名称，可以根据自己的情况进行编写
        return "内置数据源";
    }
}
