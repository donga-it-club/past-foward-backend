package aws.retrospective.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import aws.retrospective.dto.CreateSectionRequest;
import aws.retrospective.dto.GetSectionsRequestDto;
import aws.retrospective.service.CommentService;
import aws.retrospective.service.SectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(controllers = SectionController.class)
class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SectionService sectionService;
    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("신규 회고카드를 등록한다.")
    void createSection() throws Exception {
        //given
        CreateSectionRequest request = CreateSectionRequest.builder()
            .retrospectiveId(1L)
            .templateSectionId(2L)
            .sectionContent("내용")
            .build();

        //when //then
        mockMvc.perform(
                post("/sections")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("201"))
            .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    @DisplayName("신규 회고카드 등록 시에 회고보드 정보가 필요하다.")
    void createSectionWithEmptyRetrospectiveId() throws Exception {
        //given
        CreateSectionRequest request = CreateSectionRequest.builder()
            .templateSectionId(1L)
            .sectionContent("내용")
            .build();

        //when //then
        mockMvc.perform(
                post("/sections")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("회고카드가 작성된 회고보드 ID는 필수 값입니다."))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("신규 회고카드 등록 시에 회고보드 정보가 필요하다.")
    void createSectionWithEmptyTemplateSectionId() throws Exception {
        //given
        CreateSectionRequest request = CreateSectionRequest.builder()
            .retrospectiveId(1L)
            .sectionContent("내용")
            .build();

        //when //then
        mockMvc.perform(
                post("/sections")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("회고카드가 작성된 템플릿 ID는 필수 값입니다."))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("신규 회고카드 등록 시에 회고보드 정보가 필요하다.")
    void createSectionWithEmptySectionContent() throws Exception {
        //given
        CreateSectionRequest request = CreateSectionRequest.builder()
            .retrospectiveId(1L)
            .templateSectionId(1L)
            .build();

        //when //then
        mockMvc.perform(
                post("/sections")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("회고카드에 작성된 내용은 필수 값입니다."))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("회고보드에 작성된 모든 회고카드를 조회할 수 있다.")
    void getSections() throws Exception {
        //given
        GetSectionsRequestDto request = GetSectionsRequestDto.builder()
            .retrospectiveId(1L)
            .teamId(1L)
            .build();

        String qString = String.format("?retrospectiveId=%d&teamId=%d",
            request.getRetrospectiveId(), request.getTeamId());

        //when //then
        mockMvc.perform(
                get("/sections" + qString)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.message").doesNotExist())
            .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("회고보드에 작성된 모든 회고카드를 조회할 때, 회고보드 ID는 필수 값이다..")
    void getSectionsWithEmptyRetrospectiveId() throws Exception {
        //given
        GetSectionsRequestDto request = GetSectionsRequestDto.builder()
            .teamId(1L)
            .build();

        String qString = String.format("?teamId=%d", request.getTeamId());

        //when //then
        mockMvc.perform(
                get("/sections" + qString)
                    .with(csrf())
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("회고보드 id는 필수 값입니다."));
    }

}