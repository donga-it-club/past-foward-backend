package aws.retrospective.controller;


import aws.retrospective.dto.CreateRetrospectiveDto;
import aws.retrospective.dto.CreateRetrospectiveResponseDto;
import aws.retrospective.service.RetrospectiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrospectives")
public class RetrospectiveController {

    private final RetrospectiveService retrospectiveService;


    @PostMapping()
    public CreateRetrospectiveResponseDto createRetrospective(
        @RequestBody CreateRetrospectiveDto createRetrospectiveDto) {
        return retrospectiveService.createRetrospective(createRetrospectiveDto);
    }
}
