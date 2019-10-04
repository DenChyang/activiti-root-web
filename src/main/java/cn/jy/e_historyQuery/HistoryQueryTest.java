package cn.jy.e_historyQuery;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.junit.Test;

import java.util.List;

/**
 * @author DengQiang.Wu
 * @create 2019-10-04 16:34
 */
public class HistoryQueryTest {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

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
     * 查历史活动
     */
    @Test
    public void queryHistoryActiviti() {
        String processInstanceId = "2501";
        List<HistoricActivityInstance> list = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()// 创建历史活动流程实例查询
                .processDefinitionId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc()
                .list();

        if (list != null && list.size() > 0) {
            for (HistoricActivityInstance his : list) {
                System.out.println(his.getProcessInstanceId());
                System.out.println(his.getEndTime());
                System.out.println(his.getStartTime());
                System.out.println(his.getActivityName());

            }
        }
    }
}
