package aws.retrospective.entity;

import lombok.Getter;

@Getter
public enum MailStatus {
    SITE("사이트 문의"),
    CREATOR("제작자 문의"),
    ERROR("오류 문의"),
    OTHER("기타");

    private String name;

    MailStatus(String name) {
        this.name = name;
    }
}
