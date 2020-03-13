package com.ibu.reminder.repository;

import com.ibu.reminder.entity.JiraIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JiraIssueRepository extends JpaRepository<JiraIssue, Integer> {

    @Query("select ji from JiraIssue ji where ji.project in :project and ji.issueType = :issueType and ji.resolution in :resolution and ji.issueStatus not in :issueStatus")
    List<JiraIssue> withProjectAndIssueTypeAndResolutionInAndIssueStatusNotIn(@Param("project") Integer[] project, @Param("issueType") Integer issueType, @Param("resolution") Integer[] resolution, @Param("issueStatus") Integer[] issueStatus);
}
