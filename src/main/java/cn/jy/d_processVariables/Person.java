package cn.jy.d_processVariables;

import java.io.Serializable;

/**
 * @author DengQiang.Wu
 * @create 2019-10-04 14:44
 */
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;

    private String name;

    private String execution;

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
