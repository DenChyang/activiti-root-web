package cn.jy.b_processDefinitionTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
    @Test
    public void findProcessDefinition() {
        List<ProcessDefinition> list = processEngine.getRepositoryService()// 流程定义和部署对象相关的service
                .createProcessDefinitionQuery()// 创建一个流程定义的查询
                /* 指定查询条件*/
                //.deploymentId() // 使用部署对象id查询
                //.processDefinitionId()// 使用流程定义id查询
                //.processDefinitionKey()// 使用流程定义的key查询
                //.processDefinitionNameLike()// 使用流程定义的名称模糊查询
                .orderByProcessDefinitionVersion().asc()// 按照版本的升序排列
                /*返回的结果集*/
                .list();// 返回一个集合列表，封装流程定义
                //.singleResult()// 唯一结果集
                // .count()// 返回结果集数量
                // .listPage(firstResult,maxResults);分页查询

        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                System.out.println("流程定义id："+pd.getId());// 流程定义的  key:版本:随机生成数
                System.out.println("流程定义name："+pd.getName());// 对应的helloworld.bpmn文件中的name属性值
                System.out.println("流程定义key："+pd.getKey());// 对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义版本："+pd.getVersion());// 流程定义的key值相同的情况下，版本升级
                System.out.println("资源名称bpmn文件："+pd.getResourceName());
                System.out.println("资源名称png文件："+pd.getDiagramResourceName());
                System.out.println("部署对象的id："+pd.getDeploymentId());
                System.out.println("############################################");
            }
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteProcessDefinition() {
        // 使用部署id，完成删除
        String deploymentId = "1";
        /**
         * 不带级联的删除
         *  只能删除没有启动的流程，如果流程启动，就会抛出异常
         */
        //processEngine.getRepositoryService()
        //        .deleteDeployment(deploymentId);

        /**
         * 级联删除
         *      不管流程是否启动，都能删除
         */
        processEngine.getRepositoryService()
                .deleteDeployment(deploymentId,true);
    }

    /**
     * 查看流程图
     */
    @Test
    public void viewPic() throws IOException {
        /**
         * 将生成的图片放到文件夹下
         */
        String deplomentId = "5001";

        // 获取图片资源名称
        List<String> list = processEngine.getRepositoryService()
                .getDeploymentResourceNames(deplomentId);
        // 定义流程名称
        String resourceName = "";
        if (list != null && list.size() > 0) {
            for (String s : list) {
                if (s.indexOf("png") >= 0) {
                    resourceName = s;
                }
            }
        }
        // 获取图片流
        InputStream in = processEngine.getRepositoryService()
                .getResourceAsStream(deplomentId, resourceName);

        // 输出图片,将图片生成到d盘目录下
        File file = new File("D:/" + resourceName);

        // 将图片下入到d盘下
        FileUtils.copyInputStreamToFile(in, file);
    }

    /**
     * 附加功能：查询最新版本的流程定义
     */
    @Test
    public void findLastVersionprocessDefinition() {
        List<ProcessDefinition> list = processEngine.getRepositoryService()//
                .createProcessDefinitionQuery()//
                .orderByProcessDefinitionVersion().asc()// 使用流程定义的版本升序排列
                .list();

        LinkedHashMap<String, ProcessDefinition> map = new LinkedHashMap<>();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                map.put(pd.getKey(), pd);
            }
        }
        ArrayList<ProcessDefinition> pdList = new ArrayList<>(map.values());
        if (pdList != null && pdList.size() > 0) {
            for (ProcessDefinition pd : pdList) {
                System.out.println("流程定义id："+pd.getId());// 流程定义的  key:版本:随机生成数
                System.out.println("流程定义name："+pd.getName());// 对应的helloworld.bpmn文件中的name属性值
                System.out.println("流程定义key："+pd.getKey());// 对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义版本："+pd.getVersion());// 流程定义的key值相同的情况下，版本升级
                System.out.println("资源名称bpmn文件："+pd.getResourceName());
                System.out.println("资源名称png文件："+pd.getDiagramResourceName());
                System.out.println("部署对象的id："+pd.getDeploymentId());
                System.out.println("############################################");
            }
        }
    }

    /**
     * 附加功能：删除流程定义(删除key相同的所有不同版本的流程定义)
     */
    @Test
    public void deleteProcessDefinitionByKey() {
        // 流程定义的key
        String processDefinitionKey = "helloworld";
        // 先使用流程定义的key查询流程定义，查询出所有的版本
        List<ProcessDefinition> list = processEngine.getRepositoryService()//
                .createProcessDefinitionQuery()//
                .processDefinitionKey(processDefinitionKey)//使用流程定义的key查询
                .list();
        // 遍历
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                // 获取部署id
                String deploymentId = pd.getDeploymentId();
                processEngine.getRepositoryService()
                        .deleteDeployment(deploymentId, true);
            }
        }
    }

    /**
     * 总结：
     * ****************************************
     * deployment 部署对象
     * 一次部署的多个文件的信息，对于不需要的流程可以删除和修改
     * 对应的表：
     * act_re_deployment:部署对象表
     * act_re_prodef：流程定义表
     * act_re_bytearray：资源文件表
     * act_re_property：主键生成策略表
     * ****************************************
     * processDefinition 流程定义
     * 解析bpmn 后得到的流程𨈖一规则的信息，工作流系统就是按照流程定义的规则执行的
     * 相关的表
     *   act_re_deployment:部署对象表
     *   act_re_prodef：流程定义表
     *   act_re_bytearray：资源文件表
     *   act_re_property：主键生成策略表
     */



}
