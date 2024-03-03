package aws.retrospective.controller;


import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.service.RetrospectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {

    private final RetrospectiveService retrospectiveService;

    @Autowired
    public RetrospectiveController(RetrospectiveService retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }

    @PostMapping()
    public CreateRetrospectiveResponseDto createRetrospective(
        @RequestBody CreateRetrospectiveDto createRetrospectiveDto) {
        return retrospectiveService.createRetrospective(createRetrospectiveDto);
    }
}
