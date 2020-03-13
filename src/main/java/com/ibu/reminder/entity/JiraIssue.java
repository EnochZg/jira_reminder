package com.ibu.reminder.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "jiraissue")
public class JiraIssue {

    @Id
    private Integer id;

    @Column(name = "PROJECT")
    private Integer project;

    @Column(name = "issuetype")
    private Integer issueType;

    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "RESOLUTION")
    private Integer resolution;

    @Column(name = "issuestatus")
    private Integer issueStatus;

    @Column(name = "duedate")
    private LocalDate dueDate;

    @Column(name = "ASSIGNEE")
    private String assignee;
}
