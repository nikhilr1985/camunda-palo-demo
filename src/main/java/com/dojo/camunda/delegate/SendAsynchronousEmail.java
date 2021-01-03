package com.dojo.camunda.delegate;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SendAsynchronousEmail implements JavaDelegate {

    public static final String SEND_ASYNCHRONOUS_EMAIL_TOPIC = "SendAsynchronousEmailTopic";
    private static final Long LOCK_DURATION = 300000L;
    private static final String EXTERNAL_WORKER_ID = "SendAsynchronousWorker";
    private static final Integer NUM_TASKS_PER_BATCH = 20;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("Starting delegate '" + this.getClass().getSimpleName() + "'");

        ExternalTaskService externalTaskService =
                delegateExecution.getProcessEngineServices().getExternalTaskService();

        List<LockedExternalTask> lockedTasks =
                externalTaskService
                        .fetchAndLock(NUM_TASKS_PER_BATCH, EXTERNAL_WORKER_ID)
                        .topic(SEND_ASYNCHRONOUS_EMAIL_TOPIC, LOCK_DURATION)
                        .execute();

        String taskIds =
                lockedTasks.stream().map(LockedExternalTask::getId).collect(Collectors.joining(", "));
        logger.info("{} tasks locked: {}", lockedTasks.size(), taskIds);

        lockedTasks.forEach(
            task -> {
                VariableMap variables = task.getVariables();
                externalTaskService.complete(task.getId(), EXTERNAL_WORKER_ID, variables);
                logger.info("Task '" + task.getId() + "' completed successfully.");
            }
        );
    }
}
