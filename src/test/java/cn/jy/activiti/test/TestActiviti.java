package cn.jy.activiti.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @author DengQiang.Wu
 * @create 2019-09-15 21:27
 */
public class TestActiviti {

    /**
     * 创建activit的23张表-代码
     */
    @Test
    public void createTable() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        // 连接数据库
        processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activiti5?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("root");

        /**
         * 不能自动创建表，需要表存在才可以
         * public static final String DB_SCHEMA_UPDATE_FALSE = "false";
         *
         * 先删除表，再创建表
         * public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
         *
         * 如果表不存在，自动创建表
         * public static final String DB_SCHEMA_UPDATE_TRUE = "true";
         */
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        // 流程引擎对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    }

    /**
     * 从配置文件中读取，创建23张表
     */
    @Test
    public void createTableByConfig() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }

}
