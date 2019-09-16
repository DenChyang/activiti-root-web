package cn.jy.b_processDefinitionTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipInputStream;

/**
 * @author DengQiang.Wu
 * @create 2019-09-16 23:06
 */
public class ProcessDefinitionTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinition() {

        // 流程定义与部署对象相关的service
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()// 创建一个部署对象
                .name("流程定义") // 添加部署名称
                .addClasspathResource("diagrams/helloworld.bpmn")// 从classpath的资源加载，一次加载一个文件
                .addClasspathResource("diagrams/helloworld.png")// -->加载到act_ge_bytearray中
                .deploy();// 完成部署

        System.out.println("部署id"+deployment.getId());// -->act_re_deployment
        System.out.println("部署名称"+deployment.getName());

        /**
         * 表act_re_deployment-->部署对象表
         *
         * arc_re_procdef -->流程定义表（当key相同的时候，版本升级）
         *
         * act_ge_bytearry -->资源文件表
         *
         * act_ge_property -->主键生成策略表
         */
    }

    @Test
    public void deploymentProcessDefinition_zip() {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .name("流程定义")
                .addZipInputStream(zipInputStream) // 指定zip格式完成部署
                .deploy();
        System.out.println("部署id："+deploy.getId());
        System.out.println("部署名称："+deploy.getName());

    }
    // p7 22min

}
