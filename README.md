# past-foward-backend

Past Forward Backend

# 프로젝트

TODO

# 팀원

TODO

# 개발 환경

- Java
- Spring boot(3.2.3)
- JPA
- MySQL
- Docker (local DB)

# 인프라

- EC2
  - ASG
  - ALB
- RDS
- S3
- Cognito
- Cloudwatch
- Secrets Manager

## CI/CD

Github actions 사용

### CI

- [.github\workflows\ci.yml](https://github.com/donga-it-club/past-foward-backend/blob/main/.github/workflows/ci.yml)

### CD

- main 브랜치 merge시 CodeDeploy 배포 스크립트 동작
- [.github/workflows/cd.yml](https://github.com/donga-it-club/past-foward-backend/blob/main/.github/workflows/cd.yml)

## 컨벤션

- 노션 링크화

# 이슈 관리

- Github Issue에 맞추어서 브랜치 및 PR 생성(부가 설명 필요)
