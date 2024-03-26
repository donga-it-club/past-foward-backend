package aws.retrospective.service;

import aws.retrospective.common.CommonApiResponse;
import aws.retrospective.dto.InviteTeamMemberDTO;
import aws.retrospective.entity.Team;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.service.InviteTeamMemberService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InviteTeamMemberServiceTest {

    @Test
    public void testGenerateInvitation_WhenTeamFound() {
        // Mock TeamRepository 생성
        TeamRepository teamRepository = mock(TeamRepository.class);
        // Mock Team 데이터 생성
        long teamId = 1L;
        Team mockTeam = new Team("Example Team");
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam)); // 팀을 찾는 경우

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

    @Test
    public void testGenerateInvitation_WhenTeamNotFound() {
        // Mock TeamRepository 생성
        TeamRepository teamRepository = mock(TeamRepository.class);
        // 팀을 찾지 못하는 경우
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        // 테스트 대상 서비스 객체 생성
        InviteTeamMemberService inviteTeamMemberService = new InviteTeamMemberService(teamRepository);

        // 예외 검증
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> inviteTeamMemberService.generateInvitation(1L));
        assertEquals("Team not found for id: 1", exception.getMessage());
    }
}
