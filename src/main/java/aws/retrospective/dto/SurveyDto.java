package aws.retrospective.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SurveyDto {

    private Long id;
    private String age;
    private String gender;
    private String occupation;
    private String region;
    private String source;
    private String purpose;

    public static SurveyDtoBuilder builder() {
        return new SurveyDtoBuilder();
    }

    public static class SurveyDtoBuilder {

        private Long id;
        private String age;
        private String gender;
        private String occupation;
        private String region;
        private String source;
        private String purpose;

        SurveyDtoBuilder() {
        }

        public SurveyDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SurveyDtoBuilder age(String age) {
            this.age = age;
            return this;
        }

        public SurveyDtoBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public SurveyDtoBuilder occupation(String occupation) {
            this.occupation = occupation;
            return this;
        }

        public SurveyDtoBuilder region(String region) {
            this.region = region;
            return this;
        }

        public SurveyDtoBuilder source(String source) {
            this.source = source;
            return this;
        }

        public SurveyDtoBuilder purpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        public SurveyDto build() {
            return new SurveyDto(id, age, gender, occupation, region, source, purpose);
        }
    }
}
