package aws.retrospective.entity;

import lombok.Getter;

@Getter
public enum SectionTemplateStatus {

    ACTION_ITEMS("Action_Items"),
    KEEP("Keep"),
    PROBLEM("Problem"),
    TRY("Try"),
    KUDOS("Kudos"),
    WENT_WELL("Went_Well"),
    TO_IMPROVE("To_Improve");

    private final String sectionName;

    SectionTemplateStatus(String sectionName) {
        this.sectionName = sectionName;
    }
}
