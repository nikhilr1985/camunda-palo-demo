package com.dojo.camunda.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserAccountExists implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String userName = delegateExecution.getBusinessKey();
        ArrayList<String> accountList = getUserAccounts();
        boolean userAccountExists = accountList.contains(userName);
        delegateExecution.setVariable("accountExists",userAccountExists);
    }

    private ArrayList<String> getUserAccounts() {
        ArrayList<String> accountList = new ArrayList<>();
        accountList.add("user1");
        accountList.add("user2");
        accountList.add("user3");
        return accountList;
    }
}
