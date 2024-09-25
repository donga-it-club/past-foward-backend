# past-foward-backend


![mainPage](https://github.com/donga-it-club/past-forward-frontend/assets/138123134/b19cc815-677f-42e8-ab35-f7acfa4bf988)

<br>

# 📖 팀 회고 작성 서비스 웹 Past-Forward

- 배포 URL : https://www.pastforward.link/

<br>

## 📝 프로젝트 소개

- Past-Forward는 팀원들과 회고 작성을 할 수 있는 웹페이지입니다.
- 회고 템플릿(총 두가지)에 따라 다른 주제로 회고 카드를 네가지 섹션으로 나누어 작성이 가능합니다.
- 팀원 초대 링크를 통해 팀원들을 초대하고 관리(초대 및 삭제 기능)할 수 있습니다.
- 다양한 유저들을 마음에 드는 회고 카드에 좋아요를 누르거나 댓글을 작성할 수 있습니다.

<br>

## 🌁 프로젝트 배경
회고는 과거의 경험에서 배운 점을 되새기고, 앞으로의 행동을 개선하는 데 중요한 역할을 합니다. 이 과정을 통해 지속적으로 성장할 수 있습니다.

기존의 웹 애플리케이션은 프로젝트 관리, 협업 도구, 또는 노트 작성에 초점을 맞추고 있습니다. 이로 인해 회고 활동에 특화되지 않아, 사용자들이 팀 프로젝트나 개인 경험을 체계적으로 회고하는 데 어려움을 겪고 있습니다. 
또한, 기존의 회고 애플리케이션 중 상당수는 유료 시스템이어서 접근성이 좋지 않습니다.

따라서 이를 통합적으로 관리할 수 있는 플랫폼의 중요성이 커졌습니다. 
Past Forward 애플리케이션은 팀원 간의 효과적인 소통을 촉진하고, 프로젝트 관리를 간소화하여 전체적인 작업 효율을 향상시킬 수 있습니다.

#### 기존의 회고 애플리케이션
|서비스명|플랫폼 유형|특징|단점|
|---|---|---|---|
|Team Retro|WEB|회고 과정을 단계별로 가이드를 해준다.|한글을 지원하지 않으며, 유료 서비스이다.|
|Neatro|WEB|새로운 회의나 작업을 시작할 경우, 이전 활동에서 완료되지 않은 작업이 있다면 자동으로 알려준다.|팀별로 월 비용을 지불해야 한다.|
|Retros.work|WEB|팀원들이 감정과 에너지를 표현할 수 있어, 감정 변화 추이를 확인할 수 있다.|월 비용을 지불해야 한다.|

<br>

## 🛠️ 개발 환경

- Java 17
- Spring boot(3.2.3)
- JPA
- MySQL
- Docker (local DB)

<br>

## 🚀 인프라

- EC2
  - ASG
  - ALB
- RDS
- S3
- Cognito
- Cloudwatch
- Secrets Manager

<br>

## 🧑🏻‍💻 협업 문서

- 디자인 : [Figma](https://www.figma.com/file/zJaBNvTvLlG0d9h5TILICj/Past-Forward-Web-Site?type=design&node-id=1157%3A6652&mode=design&t=eI1Pvgp8EpiHQgEA-1)
- Ground Rule
  - [👩🏻‍💻 커밋 컨벤션](https://seed-spike-d61.notion.site/350f6f3f1edc4300922cf71f6b626756?pvs=25)
  - [이슈/PR 템플릿](https://www.notion.so/ISSUE-PR-996b39eb2c0244c498886f0946692156?pvs=4)

<br>

## 💻 역할 분담

### 유윤지

- **댓글(Comment)**
  - CRUD
- **설문조사(Survey)**
  - 조회

### 정민석

- **회고(Retrospective)**
  - CRUD
  - Bookmark
- **회고 템플릿 (RetrospectiveTemplate)**
- **유저 (User)**
- **AWS Cognito 연동**
  - Spring Security OAuth2 Resource Server
- **인프라 구축**
  - CI/CD
    - Github actions
    - EC2 + CodeDeploy

### 이희망
- **회고 카드(Section)**
  - CRUD
  - 좋아요 및 취소
- Contact
- 알림 기능

### 백민정

- **팀원(Team)**
  - 초대 링크를 통해 팀원 초대

### 변지영
- **설문조사(Survey)**
  - 저장

<br>

## 🗓️ 개발 기간

### 개발 기간

- 1차 개발 기간: 2024.02.20 ~ 2024. 04. 26

<br>


## 🙋‍♂️ 팀 구성

<div align="center">

|                                                                **유윤지**                                                                 |                                                                  **정민석**                                                                  |                                                             **이희망**                                                              |                                                              **백민정**                                                               |                                                             **변지영**                                                              |
| :---------------------------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------: |
| [<img src="https://avatars.githubusercontent.com/u/105447233?v=4" height=150 width=150> <br/> @llcodingll](https://github.com/llcodingll) | [<img src="https://avatars.githubusercontent.com/u/90817465?v=4" height=150 width=150> <br/> @Minseok-2001](https://github.com/Minseok-2001) | [<img src="https://avatars.githubusercontent.com/u/122812652?v=4" height=150 width=150> <br/> @w1shope](https://github.com/w1shope) | [<img src="https://avatars.githubusercontent.com/u/110668121?v=4" height=150 width=150> <br/> @yangheeb](https://github.com/yangheeb) | [<img src="https://avatars.githubusercontent.com/u/156206916?v=4" height=150 width=150> <br/> @zzero-o](https://github.com/zzero-o) |

</div>
<br>
