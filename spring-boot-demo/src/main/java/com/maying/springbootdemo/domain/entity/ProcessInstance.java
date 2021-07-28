package com.maying.springbootdemo.domain.entity;

import lombok.Data;

/**
 * 流程实例对象
 *
 */
@Data
public class ProcessInstance {

    private Long processId;

    private String action;

    private String resPerson;

    private String nodeName;

    public ProcessInstance() {

    }

    public ProcessInstance(Long processId, String action, String resPerson, String nodeName) {
        this.processId = processId;
        this.action = action;
        this.resPerson = resPerson;
        this.nodeName = nodeName;
    }
}
