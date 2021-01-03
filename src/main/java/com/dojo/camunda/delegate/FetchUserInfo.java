package com.dojo.camunda.delegate;

import com.dojo.camunda.model.UserInfo;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FetchUserInfo implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, UserInfo> userList = new HashMap<String, UserInfo>();
        userList.put("user1", new UserInfo("user1", "John", "Doe"));
        userList.put("user2", new UserInfo("user2", "Brad", "Pitt"));
        userList.put("user3", new UserInfo("user3", "Tom", "Hanks"));
        userList.put("user4", new UserInfo("user4", "Naomi", "Sullivan"));
        userList.put("user5", new UserInfo("user5", "Marshall", "Palmer"));
        userList.put("user6", new UserInfo("user6", "Abraham", "Sanders"));

        String userName = delegateExecution.getBusinessKey();

//        delegateExecution.setVariable("userInfo", Variables.objectValue(userList.get(userName)).serializationDataFormat("application/json").create());
        delegateExecution.setVariable("userInfo", userList.get(userName));
    }
}
