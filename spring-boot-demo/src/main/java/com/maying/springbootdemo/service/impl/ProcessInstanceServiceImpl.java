package com.maying.springbootdemo.service.impl;


import com.maying.springbootdemo.domain.entity.ProcessInstance;
import com.maying.springbootdemo.service.ProcessInstanceService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 */
public class ProcessInstanceServiceImpl implements ProcessInstanceService {


    @Override
    @Test
    public void streamDisticntTest() {
        List<ProcessInstance> instanceServiceList = new ArrayList<>();
        instanceServiceList.add(new ProcessInstance(Long.parseLong("1"),null,null,"开始"));
        instanceServiceList.add(new ProcessInstance(Long.parseLong("2"),"Approved","AAAAAA","一级审批"));
        instanceServiceList.add(new ProcessInstance(Long.parseLong("3"),null,null,"一级审批"));
        instanceServiceList.add(new ProcessInstance(Long.parseLong("4"),"Approved","BBBBB","二级审批"));
        instanceServiceList.add(new ProcessInstance(Long.parseLong("5"),null,null,"二级审批"));
        instanceServiceList.add(new ProcessInstance(Long.parseLong("6"),"Reject","CCCCCCCC","三级审批"));
        instanceServiceList.add(new ProcessInstance(Long.parseLong("7"),null,null,"结束"));

        //去重测试
        instanceServiceList = instanceServiceList.stream().collect(Collectors.toMap(ProcessInstance::getNodeName, a -> a ,(o1,o2)->{
            if(o1.getAction()!=null){
                return o1;
            } else {
                return o2;
            }
        })).values().stream().sorted(Comparator.comparing(ProcessInstance::getProcessId)).collect(Collectors.toList());


        System.out.println(instanceServiceList.toString());

        return ;
    }
}
