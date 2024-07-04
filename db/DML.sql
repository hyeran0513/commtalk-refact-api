-- 회원 권한 유형
INSERT INTO member_role (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO member_role (role_name) VALUES ('ROLE_USER');

-- 회원 활동 유형
INSERT INTO activity_type (type_name) VALUES ('POST_LIKE');
INSERT INTO activity_type (type_name) VALUES ('COMMENT_LIKE');
INSERT INTO activity_type (type_name) VALUES ('POST_SCRAP');

-- 관리자 회원 및 계정
SET @admin_role_id = (SELECT member_role_id FROM member_role WHERE role_name = 'ROLE_ADMIN');

INSERT INTO member (member_role_id, nickname, member_name, email, phone) 
VALUES (@admin_role_id, 'admin', '관리자', 'admin@example.com', '010-xxxx-xxxx');

SET @admin_id = (SELECT MAX(MEMBER_ID) FROM member);

INSERT INTO member_password (member_id, password) 
VALUES (@admin_id, 'admin');

-- 게시판
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('자유게시판', '자유게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('비밀게시판', '비밀게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('정보게시판', '정보게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('시사,이슈', '시사,이슈 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('동아리', '동아리 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('여행', '여행 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('요리', '요리 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('알바,과외', '알바,과외 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('자취', '자취 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('건강', '건강 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('패션', '패션 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('연예,방송', '연예,방송 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('게임', '게임 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('스포츠', '스포츠 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('독서', '독서 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('음악', '음악 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('사진', '사진 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('개발자', '개발자 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('공무원', '공무원 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('교직', '교직 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('로스쿨', '로스쿨 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('취업상담', '취업상담 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('강아지', '강아지 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('고양이', '고양이 게시판 소개 영역입니다.', @admin_id);
INSERT INTO board (board_name, board_description, manager_id) 
VALUES ('기타', '기타 게시판 소개 영역입니다.', @admin_id);

commit;

