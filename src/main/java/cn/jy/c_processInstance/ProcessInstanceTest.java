package cn.jy.c_processInstance;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
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

    /**
     * 查询流程状态,判断流程正在执行,还是结束
     */
    @Test
    public void queryProcessState() {
        String processInstanceId = "5001";
        // 使用正在执行的流程实例表进行查询
        ProcessInstance pi = processEngine.getRuntimeService()// 正在执行的流程实例和执行对象
                .createProcessInstanceQuery()// 创建流程实例查询
                .processDefinitionId(processInstanceId)// 使用流程实例id查询
                .singleResult();
        if (pi == null) {
            // 流程实例结束了,实例表中就删除执行对象表中的数据
            System.out.println("流程已经结束");
        }else {

            System.out.println("流程灭有结束");
        }
    }

    /**
     * 查询历史任务
     */
    @Test
    public void queryHisTask() {
        String taskAssignee = "张三";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()// 与历史表相关的service
                .createHistoricTaskInstanceQuery()// 创建历史流程实例查询
                .taskAssignee(taskAssignee)// 指定历史任务的班里人
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hisTaskInstance : list) {
                System.out.println(hisTaskInstance.getId()+"  "+hisTaskInstance.getName()+"  "
                        +hisTaskInstance.getProcessInstanceId()+"  "
                        +hisTaskInstance.getStartTime()+" "+hisTaskInstance.getEndTime());
                System.out.println("***************************");

            }
        }
    }

    /**
     * 查询历史流程实例
     */
    @Test
    public void queryHisProcessInstance() {
        String processInstanceId = "2501";
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()// 使用历史流程实例查询
                .createHistoricProcessInstanceQuery()// 创建历史流程实例查询
                .processInstanceId(processInstanceId)// 使用流程实例id查询
                .singleResult();

        System.out.println(historicProcessInstance.getId()+" "+historicProcessInstance.getName()+" "
        +historicProcessInstance.getStartTime()+"  "
        +historicProcessInstance.getBusinessKey());
    }

    /**
     * 执行对象
     *  按流程定义的规则执行一次的过程
     *      表:
     *         act_ru_execution:正在执行的信息
     *         act_hi_procinst:已经执行完的历史流程实例信息
     *         act_hi_actinst:存放历史所有完成的活动(所有活动节点的历史表)
     * 流程实例
     *  指流程从开始到结束的最大执行分支,一个执行的流程,流程实例只有一个
     *      注意:
     *          如果是单实例流程,执行对象id就是流程实例id
     *          如果一个流程有分支和聚合,那么执行对象id和流程实例id就不相同
     *          一个流程中,流程实例只有一个,执行对象可以存在多个
     *  task任务
     *      执行到某任务环节时生成的任务信息
     *        表:
     *          act_ru_task:正在执行的任务信息
     *          act_hi_taskinst:已经执行完的历史任务信息
     */

}
