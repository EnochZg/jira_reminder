package com.ibu.reminder.timer;

import com.ibu.reminder.config.RemindConfig;
import com.ibu.reminder.entity.JiraIssue;
import com.ibu.reminder.repository.JiraIssueRepository;
import com.ibu.reminder.util.WeChatPusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@Slf4j
public class SummitTest {

    private RemindConfig remindConfig;

    private JiraIssueRepository jiraIssueRepository;

    @Autowired
    public void setRemindConfig(RemindConfig remindConfig) {
        this.remindConfig = remindConfig;
    }

    @Autowired
    public void setJiraIssueRepository(JiraIssueRepository jiraIssueRepository) {
        this.jiraIssueRepository = jiraIssueRepository;
    }

    @Scheduled(cron = "0 18 * * * ?")
    public void summitTest() throws JSONException {

        //3天内提醒
        long remindDays = 3;
        Integer[] resolution = {10100, 10200};
        Integer[] issueStatus = {10205, 10402, 10204};
        Integer[] projectId = remindConfig.getProjectId();

        WeChatPusher weChatPusher = new WeChatPusher();

        //获取项目下的`子任务`中`处理中`以及`未处理`的子任务
        List<JiraIssue> jiraIssues = jiraIssueRepository.withProjectAndIssueTypeAndResolutionInAndIssueStatusNotIn(projectId, 10004, resolution, issueStatus);

        //按经办人分类子任务
        HashMap<String, List<JiraIssue>> assigneeMaps = new HashMap<>();
        for (JiraIssue jiraIssue : jiraIssues) {
            List<JiraIssue> jiraIssueList = assigneeMaps.get(jiraIssue.getAssignee());
            if (jiraIssueList == null) {
                List<JiraIssue> initJiraIssues = new ArrayList<>();
                initJiraIssues.add(jiraIssue);
                assigneeMaps.put(jiraIssue.getAssignee(), initJiraIssues);
            } else {
                jiraIssueList.add(jiraIssue);
                assigneeMaps.put(jiraIssue.getAssignee(), jiraIssueList);
            }
        }

        //推送内容整合为一条
        for (Map.Entry<String, List<JiraIssue>> entry : assigneeMaps.entrySet()) {
            String suffix, msg = "";
            for (JiraIssue jiraIssue : entry.getValue()) {
                long diffDays = ChronoUnit.DAYS.between(LocalDate.now(), jiraIssue.getDueDate());
                if (diffDays < remindDays) {
                    if (diffDays == 0) {
                        suffix = "任务今天提测，请及时暴露风险";
                    } else if (diffDays == 1) {
                        suffix = "任务明天提测，请及时暴露风险";
                    } else if (diffDays == 2) {
                        suffix = "任务后天提测，请及时暴露风险";
                    } else if (diffDays < 0) {
                        suffix = "任务已过期" + Math.abs(diffDays) + "天，请及时暴露风险";
                    } else {
                        suffix = "任务" + diffDays + "天后提测，请及时暴露风险";
                    }
                    msg = "“" + jiraIssue.getSummary() + "”" + suffix + "\n" + msg;
                }
            }
            if(msg.equals("")) {
                log.info("任务都已完成，跳过执行");
                continue;
            }
            log.info(msg);
            String[] mentionedList = {entry.getKey()};
            weChatPusher.push(msg, mentionedList);
        }
    }
}
