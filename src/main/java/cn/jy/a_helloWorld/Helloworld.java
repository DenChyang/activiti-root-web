package cn.jy.a_helloWorld;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @author DengQiang.Wu
 * @create 2019-09-16 22:04
 */
public class Helloworld {


    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinition() {

        // 流程定义与部署对象相关的service
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()// 创建一个部署对象
                .name("helloworld入门程序") // 添加部署名称
                .addClasspathResource("diagrams/helloworld.bpmn")// 从classpath的资源加载，一次加载一个文件
                .addClasspathResource("diagrams/helloworld.png")// -->加载到act_ge_bytearray中
                .deploy();// 完成部署

        System.out.println("部署id"+deployment.getId());// -->act_re_deployment
        System.out.println("部署名称"+deployment.getName());

    }

    /**
     * 启动流程引擎
     */
    @Test
    public void startProcessInstance() {
        String processDefinitionKey = "helloworld";
        ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的service
                .startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，
                                        // key对应helloworld.bpmn文件中id的属性值
                                        // 使用key启动，默认按照最新版本流程定义启动
        System.out.println("流程实例id"+processInstance.getId()); //5001
        System.out.println("流程定义id"+processInstance.getProcessDefinitionId());
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonal() {
        String assignee = "李四";
        List<Task> list = processEngine.getTaskService()// 与正在执行的任务相关
                        .createTaskQuery()// 创建一个任务的查询对象
                        .taskAssignee(assignee)// 指定个人任务查询，执行办理人
                        .list();//返回一个集合对象
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务创建时间："+task.getCreateTime());
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("流程实例Id："+task.getProcessInstanceId());
                System.out.println("执行对象id："+task.getExecutionId());
                System.out.println("流程定义id："+task.getProcessDefinitionId());
            }
        }
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask() {
        // 任务id
        String taskId = "2504";
        processEngine.getTaskService()// 与正在执行的任务管理相关的service
                .complete(taskId);
        System.out.println("完成任务：任务id："+ taskId);
    }
}
