package aws.retrospective.service;


import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.entity.Retrospective;
import aws.retrospective.entity.RetrospectiveTemplate;
import aws.retrospective.entity.Team;
import aws.retrospective.entity.User;
import aws.retrospective.repository.RetrospectiveRepository;
import aws.retrospective.repository.RetrospectiveTemplateRepository;
import aws.retrospective.repository.TeamRepository;
import aws.retrospective.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrospectiveService {

    private final RetrospectiveRepository retrospectiveRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final RetrospectiveTemplateRepository templateRepository;

    @Autowired
    public RetrospectiveService(RetrospectiveRepository retrospectiveRepository,
        TeamRepository teamRepository, UserRepository userRepository,
        RetrospectiveTemplateRepository templateRepository) {
        this.retrospectiveRepository = retrospectiveRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
    }

    public CreateRetrospectiveResponseDto createRetrospective(CreateRetrospectiveDto dto) {
        User user = findUserById(dto.getUserId());
        RetrospectiveTemplate template = findTemplateById(dto.getTemplateId());
        Optional<Team> team = findTeamByIdOptional(dto.getTeamId());

        Retrospective retrospective = Retrospective.builder().title(dto.getTitle())
            .team(team.orElse(null)).user(user).template(template).build();

        Retrospective savedRetrospective = retrospectiveRepository.save(retrospective);
        return toResponseDto(savedRetrospective);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Not found user: " + userId));
    }

    private RetrospectiveTemplate findTemplateById(Long templateId) {
        return templateRepository.findById(templateId)
            .orElseThrow(() -> new EntityNotFoundException("Not found template: " + templateId));
    }

    private Optional<Team> findTeamByIdOptional(Long teamId) {
        return (teamId != null) ? teamRepository.findById(teamId) : Optional.empty();
    }

    private CreateRetrospectiveResponseDto toResponseDto(Retrospective retrospective) {
        CreateRetrospectiveResponseDto responseDto = new CreateRetrospectiveResponseDto();
        responseDto.setId(retrospective.getId());
        responseDto.setTitle(retrospective.getTitle());
        responseDto.setTeamId(
            Optional.ofNullable(retrospective.getTeam()).map(Team::getId).orElse(null));
        responseDto.setUserId(retrospective.getUser().getId());
        responseDto.setTemplateId(retrospective.getTemplate().getId());
        return responseDto;
    }

}
