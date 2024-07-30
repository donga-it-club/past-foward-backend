package aws.retrospective.service;

import aws.retrospective.entity.UserTeam;
import aws.retrospective.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserTeamSaveHelper {

    private final UserTeamRepository userTeamRepository;

    /**
     * 독립된 트랜잭션(REQUIRES_NEW)으로 UserTeam을 저장합니다.
     * 동시 요청으로 uk_user_team_user_id_team_id 제약조건 위반 시,
     * 내부 트랜잭션만 롤백되며 외부 트랜잭션은 유효한 상태로 유지됩니다.
     *
     * @return true  저장 성공 (최초 요청)
     *         false 중복 키 위반 발생 (동시 요청에서 선점됨)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean saveIfNotDuplicate(UserTeam userTeam) {
        try {
            userTeamRepository.save(userTeam);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}
