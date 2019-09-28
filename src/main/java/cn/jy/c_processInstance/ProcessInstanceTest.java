package cn.jy.c_processInstance;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author DengQiang.Wu
 * @create 2019-09-28 20:29
 */
public class ProcessInstanceTest {

    /**
     * act_ru_execution 正在执行的执行对象表
     * act_hi_procinst 流程实例的历史表
     * act_ru_task 正在执行的任务表（只有节点是usertask的时候，该表中存在数据）
     * act_hi_taskinst 任务历史表（只有节点是usertask的时候，该表中存在数据）
     * act_hi_actinst 所有活动节点的历史表
     */

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义从zip
     */
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

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        // 流程定义的key
        String processDefinitionKey = "helloworld";
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
        String assignee = "李四";
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
        String taskId = "2504";
        processEngine.getTaskService()// 与正在执行的任务管理相关的service
                .complete(taskId);
        System.out.println("完成任务：任务id："+ taskId);
    }

}
