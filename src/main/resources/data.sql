insert into user
(user_id, email, username, phone, thumbnail, is_administrator, is_email_consent)
values(1, 'past-forward@pf.com', 'hope', '010-1234-5678', 'asdas-scsgss-vscss', false, false);

insert into retrospective_template
(id, name)
values(1, 'KPT');

insert into retrospective
(retrospective_id, title, thumbnail, description, user_id, template_id, status)
values(1, 'title-1', 'thumbnail-1', 'description-1', 1, 1,  'NOT_STARTED');

insert into template_section
(id, template_id, section_name, sequence)
values(1, 1, 'Keep', 0);

insert into section
(section_id, content, retrospective_id, user_id, template_section_id, like_cnt)
values(1, 'content1-', 1, 1, 1, 0);

insert into comment
(comment_id, content, user_id, section_id)
values(1, 'content-1', 1, 1);