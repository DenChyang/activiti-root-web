package cn.jy.d_processVariables;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author DengQiang.Wu
 * @create 2019-10-03 11:00
 */
public class ProcessVariablesTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    /**
     * 部署流程定义从 inputstream
     */
    @Test
    public void deploymentProcessDefinition_inputStream() {

        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .name("流程定义")
                .addInputStream("processVariables.bpmn",inputStreamBpmn) // 使用资源文件的名称(与资源文件的名称一致)和输入流完成部署
                .addInputStream("processVariables.png",inputStreamPng) // 使用资源文件的名称(与资源文件的名称一致)和输入流完成部署
                .deploy();
        System.out.println("部署id："+deploy.getId());
        System.out.println("部署名称："+deploy.getName());

    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        // 流程定义的key
        String processDefinitionKey = "processVariables";
        ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的service
                .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，
        System.out.println("流程实例id:"+pi.getId());// 流程实例id
        System.out.println("流程定义id:"+pi.getProcessDefinitionId());// 流程定义id
    }


    /**
     * 设置流程变量
     */
    @Test
    public void setVariables() {
        TaskService taskService = processEngine.getTaskService();
        // 任务id
        String taskId = "17504";
        /**
         * 这是流程变量,使用基本数据类型
         */
        //taskService.setVariableLocal(taskId,"请假天数",3);// 与当前任务id绑定
        //taskService.setVariable(taskId,"请假日期",new Date());
        //taskService.setVariable(taskId,"请假原因","回家");

        /**
         * 序列化,对象的形式
         */
        Person person = new Person();
        person.setId(2);
        person.setName("小黑");
        taskService.setVariable(taskId,"人员对象",person);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&");

    }

    /**
     * 获取流程变量
     */
    @Test
    public void getVariables() {
        TaskService taskService = processEngine.getTaskService();
        String taskId = "17504";
        /**
         * 获取流程变量,使用基本数据类型
         */
        //Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
        //Date date = (Date) taskService.getVariable(taskId, "请假日期");
        //String resean = (String) taskService.getVariable(taskId, "请假原因");
        //System.out.println(days+"  "+date+"  "+resean);

        /**
         * 设置流程变量的时候,当javabean发生变化,会抛出异常.
         */
        Person person = (Person)taskService.getVariable(taskId, "人员对象");
        System.out.println(person.getId()+"   "+person.getName());

    }

    /**
     * 模拟设置和获取流程变量的场景
     */
    @Test
    public void setAndGetVariables() {
        /**
         * 与流程实例,执行对象相关的service,正在执行的
         */
        RuntimeService runtimeService = processEngine.getRuntimeService();

        /**
         * 与任务相关的,正在执行的
         */
        TaskService taskService = processEngine.getTaskService();

        //runtimeService.setVariable(executionId,variableName,value)// 表示使用执行对象id,和流程变量的名称,设置流程变量的值,一次只能设置一个值
        //runtimeService.setVariables(executionId,map)// 表示使用执行对象id,和map集合设置流程变量的名称,map集合的value就是流程变量的值
        //taskService.setVariable(taskId,variableName,value);// 表示使用任务id,和流程变量的名称,设置流程变量的值,一次只能设置一个值
        //taskService.setVariables(taskId,map)// 表示使用任务id,和map集合设置流程变量的名称,map集合的value就是流程变量的值

        //runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);// 启动流程实例的同时,设置流程变量,用map集合
        //taskService.complete(taskId,variables);// 完成任务的同时,设置流程变量,用map集合


        /**
         * 获取流程变量
         */
        //runtimeService.getVariable(executionId,variableName);// 使用执行对象和流程变量的名称,获取流程变量的值
        //taskService.getVariable(taskId,variableName);// 使用任务id和流程变量的名称获取流程变量的值;
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask() {
        // 任务id
        String taskId = "10004";
        processEngine.getTaskService()// 与正在执行的任务管理相关的service
                .complete(taskId);
        System.out.println("完成任务：任务id："+ taskId);
    }
}



/**
 * this.getClass().getClassLoader().getResourceAsStream("diagreams/testVariables.bpmn");
 * this.getClass().getClassLoader().getResourceAsStream("diagreams/testVariables.png");
 * 从classpath根目录下加载指定名称的文件
 *
 * this.getClass().getResourceAsStream("testVariables.bpmn");
 * this.getClass().getResourceAsStream("testVariables.png");
 * 从当前包下加载指定名称的文件
 *
 * this.getClass().getResourceAsStream("/diagreams/testVariables.bpmn")
 * this.getClass().getResourceAsStream("/diagreams/testVariables.png")
 * 从classpath根目录下加载指定名称的文件
 *
 */