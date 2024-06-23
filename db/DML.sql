-- 회원 권한 유형
INSERT INTO account_role (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO account_role (role_name) VALUES ('ROLE_USER');

-- 회원 활동 유형
INSERT INTO activity_type (type_name) VALUES ('POST_LIKE');
INSERT INTO activity_type (type_name) VALUES ('COMMENT_LIKE');
INSERT INTO activity_type (type_name) VALUES ('POST_SCRAP');