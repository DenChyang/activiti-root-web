package cn.jy.h_parallerGateWay;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author DengQiang.Wu
 * @create 2019-10-07 18:46
 */
public class ParallerGateWayTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义从zip
     */
    @Test
    public void deploymentProcessDefinition_inputStream() {
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("/diagrams/parallerGateWay.bpmn");
        InputStream inputStreamPng = this.getClass().getResourceAsStream("/diagrams/parallerGateWay.png");
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .name("并行网关")
                .addInputStream("parallerGateWay.bpmn",inputStreamBpmn) // 指定zip格式完成部署
                .addInputStream("parallerGateWay.png",inputStreamPng)
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
        String processDefinitionKey = "parallerGateWay";
        ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的service
                .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，
        System.out.println("流程实例id:"+pi.getId());// 流程实例id
        System.out.println("流程定义id:"+pi.getProcessDefinitionId());// 流程定义id
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonal() {
        String assignee = "王五";
        List<Task> list = processEngine.getTaskService()// 与正在执行的任务相关
                .createTaskQuery()// 创建一个任务的查询对象
                /** 查询条件 where部分**/
                .taskAssignee(assignee)// 指定个人任务查询，指定办理人
                //.taskCandidateUser(candidateUser)// 组任务的办理人查询
                //.processDefinitionId(processDefinitionId)// 使用流程定义id查询
                //.processInstanceId(processInstanceId)// 使用流程实例id查询
                //.executionId(executionId)// 使用执行对象id查询
                /**排序**/
                /**返回结果集**/
                //.singleResult()// 返回唯一结果集
                //.count()// 返回结果集数量
                //.listPage(firstResult,maxResult)// 分页
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
        String taskId = "67504";
        HashMap<String, Object> map = new HashMap<>();
        map.put("money", 520);

        processEngine.getTaskService()// 与正在执行的任务管理相关的service
                .complete(taskId,map);
        System.out.println("完成任务：任务id："+ taskId);
    }
}
