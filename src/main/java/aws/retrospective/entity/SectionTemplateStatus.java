package aws.retrospective.entity;

import lombok.Getter;

@Getter
public enum SectionTemplateStatus {

    ACTION_ITEMS("Action Items"),
    KEEP("Keep"),
    PROBLEM("Problem"),
    TRY("Try"),
    KUDOS("Kudos"),
    WENT_WELL("Went Well"),
    TO_IMPROVE("To Improve");

    private final String sectionName;

    SectionTemplateStatus(String sectionName) {
        this.sectionName = sectionName;
    }
}
