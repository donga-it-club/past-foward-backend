package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InviteTeamMemberServiceTest {

    @Test
    public void testGenerateInvitation() {
        // Mock TeamRepository 생성
        TeamRepository teamRepository = mock(TeamRepository.class);
        // Mock Team 데이터 생성
        long teamId = 1L;
        Team mockTeam = new Team("Example Team");
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(mockTeam));

        // 테스트 대상 서비스 객체 생성
        InviteTeamMemberService inviteTeamMemberService = new InviteTeamMemberService(teamRepository);

        // 테스트 실행
        CommonApiResponse<InviteTeamMemberDTO> response = inviteTeamMemberService.generateInvitation(teamId);

        // 결과 검증
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getCode());
        InviteTeamMemberDTO dto = response.getData();
        assertNotNull(dto);
        assertNotNull(dto.getInvitationCode());
        assertNotNull(dto.getInvitationUrl());
        assertNotNull(dto.getExpirationTime());
    }
}
