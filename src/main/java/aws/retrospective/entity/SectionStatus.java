package aws.retrospective.entity;

import lombok.Getter;

@Getter
public enum SectionStatus {

    ACTION_ITEMS("Action Items");

    private final String sectionName;

    SectionStatus(String sectionName) {
        this.sectionName = sectionName;
    }
}
