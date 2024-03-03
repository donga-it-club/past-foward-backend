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
        TeamRepository teamRepository,
        UserRepository userRepository,
        RetrospectiveTemplateRepository templateRepository) {
        this.retrospectiveRepository = retrospectiveRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
    }
    public CreateRetrospectiveResponseDto createRetrospective(CreateRetrospectiveDto dto) {

        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("Not found user: " + dto.getUserId()));
        RetrospectiveTemplate template = templateRepository.findById(dto.getTemplateId())
            .orElseThrow(() -> new EntityNotFoundException("Not found template: " + dto.getTemplateId()));

        Team team = null;
        if (dto.getTeamId() != null) {
            team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + dto.getTeamId()));
        }

        Retrospective retrospective = Retrospective.builder()
            .title(dto.getTitle())
            .team(team)
            .user(user)
            .template(template)
            .build();

        Retrospective savedRetrospective = retrospectiveRepository.save(retrospective);

        CreateRetrospectiveResponseDto responseDto = new CreateRetrospectiveResponseDto();
        responseDto.setId(savedRetrospective.getId());
        responseDto.setTitle(savedRetrospective.getTitle());
        if(savedRetrospective.getTeam() != null) {
            responseDto.setTeamId(savedRetrospective.getTeam().getId());
        }
        responseDto.setUserId(savedRetrospective.getUser().getId());
        responseDto.setTemplateId(savedRetrospective.getTemplate().getId());

        return responseDto;
    }


}
