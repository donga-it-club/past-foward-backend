# -- User 데이터 생성
# insert into user(created_date, updated_date, email, phone, username, thumbnail, is_administrator, is_email_consent)
# values(now(), now(), 'lee@example.com', '010-1234-1234', 'hope', '4728e325-6215-40ba-abd8-7bf26bcd9029', false, false),
#       (now(), now(), 'test@example.com', '010-1234-1234', 'test', '1231aas-6215-40ba-abd8-7bf26bcd9029', false, false);
#
# -- Team 데이터 생성
# INSERT INTO team (team_name)
# VALUES
#     ('teamA'),
#     ('teamB');
#
# -- UserTeam 데이터 생성
# INSERT INTO user_team (user_id, team_id)
# VALUES
#     (1, 1);
#
# -- RetrospectiveTemplate 데이터 생성
# INSERT INTO retrospective_template (name)
# VALUES ('KPT'),
#        ('KUDOS');
#
# -- Retrospective 데이터 생성
# INSERT INTO retrospective (title, team_id, user_id, template_id, status)
# VALUES
#     ('회고1', 1, 1, 1, 'IN_PROGRESS'),
#     ('회고2', null, 1, 2, 'IN_PROGRESS');
#
# INSERT INTO template_section(template_id, section_name, sequence)
# VALUES
#     (1, 'Keep', 1),
#     (1, 'Problem', 2),
#     (1, 'Try', 3),
#     (1, 'Action Items', 4),
#     (2, 'Kudos', 1),
#     (2, 'Went Well', 2),
#     (2, 'To Improve', 3),
#     (2, 'Action Items', 4);
#
#
# INSERT INTO section (content, retrospective_id, user_id, template_section_id)
# VALUES
#     ('content1', 1, 1, 1),
#     ('content2', 1, 1, 2),
#     ('content3', 1, 1, 3),
#     ('content4', 1, 1, 4),
#     ('content1', 2, 1, 5),
#     ('content2', 2, 1, 6),
#     ('content3', 2, 1, 7),
#     ('content4', 2, 1, 8);
#
#
#
# HSET Notification:notification _class "aws.retrospective.entity.NotificationRedis" lastNotificationTime "2024-05-25T23:33:07.049436" notification "notification"
