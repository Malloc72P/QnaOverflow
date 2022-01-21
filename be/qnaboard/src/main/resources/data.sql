insert into members (created_date, last_modified_date, deleted, email, nickname, role)
values ('2022-01-14', '2022-01-14', false, 'no-email', 'member1', 'USER'),
       ('2022-01-14', '2022-01-14', false, 'no-email', 'Admin1', 'ADMIN'),
       ('2022-01-14', '2022-01-14', false, 'no-email', 'Admin2', 'ADMIN'),
       ('2022-01-14', '2022-01-14', false, 'no-email', 'member2', 'USER'),
       ('2022-01-14', '2022-01-14', false, 'no-email', 'member3', 'USER'),
       ('2022-01-14', '2022-01-14', false, 'no-email', 'member4', 'USER'),
       ('2022-01-14', '2022-01-14', false, 'no-email', 'member5', 'USER'),
       ('2022-01-14', '2022-01-14', false, 'no-email', 'member6', 'USER'),
       ('2022-01-14', '2022-01-14', false, 'no-email', 'member7', 'USER');

insert into tag (created_date, last_modified_date, author_id, deleted, description, name)
values ('2022-01-14', '2022-01-14', 2, false, 'description-Angular', 'Angular'),
       ('2022-01-14', '2022-01-14', 2, false, 'description-Web', 'Web'),
       ('2022-01-14', '2022-01-14', 2, false, 'description-JQuery', 'JQuery'),
       ('2022-01-14', '2022-01-14', 3, false, 'description-ReactJS', 'ReactJS'),
       ('2022-01-14', '2022-01-14', 3, false, 'description-VueJS', 'VueJS'),
       ('2022-01-14', '2022-01-14', 2, false, 'description-SpringBoot', 'SpringBoot');

insert into post (created_date, last_modified_date, author_id, content, deleted, score, title, view_count, post_type, question_id)
values ('2022-01-14', '2022-01-14', 2, 'target-content', false, 0, 'target-title', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 2, 'no-tag-no-answer', false, 0, 'title-2', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-3', false, 0, 'title-3', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 4, 'content-4', false, 0, 'title-4', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-5', false, 0, 'title-5', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-6', false, 0, 'title-6', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-7', false, 0, 'title-7', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-8', false, 0, 'title-8', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-9', false, 0, 'title-9', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-10', false, 0, 'title-10', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-11', false, 0, 'title-11', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-12', false, 0, 'title-12', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-13', false, 0, 'title-13', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-14', false, 0, 'title-14', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-15', false, 0, 'title-15', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-16', false, 0, 'title-16', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-17', false, 0, 'title-17', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-18', false, 0, 'title-18', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-19', false, 0, 'title-19', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-20', false, 0, 'title-20', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-21', false, 0, 'title-21', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-22', false, 0, 'title-22', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-23', false, 0, 'title-23', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-24', false, 0, 'title-24', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-25', false, 0, 'title-25', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-26', false, 0, 'title-26', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-27', false, 0, 'title-27', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-28', false, 0, 'title-28', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-29', false, 0, 'title-29', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-30', false, 0, 'title-30', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-31', false, 0, 'title-31', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-32', false, 0, 'title-32', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-33', false, 0, 'title-33', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-34', false, 0, 'title-34', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-35', false, 0, 'title-35', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-36', false, 0, 'title-36', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-37', false, 0, 'title-37', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-38', false, 0, 'title-38', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-39', false, 0, 'title-39', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-40', false, 0, 'title-40', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-41', false, 0, 'title-41', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-42', false, 0, 'title-42', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-43', false, 0, 'title-43', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-44', false, 0, 'title-44', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-45', false, 0, 'title-45', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-46', false, 0, 'title-46', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-47', false, 0, 'title-47', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-48', false, 0, 'title-48', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-49', false, 0, 'title-49', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-50', false, 0, 'title-50', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-51', false, 0, 'title-51', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-52', false, 0, 'title-52', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-53', false, 0, 'title-53', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-54', false, 0, 'title-54', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-55', false, 0, 'title-55', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-56', false, 0, 'title-56', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-57', false, 0, 'title-57', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-58', false, 0, 'title-58', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-59', false, 0, 'title-59', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-60', false, 0, 'title-60', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-61', false, 0, 'title-61', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-62', false, 0, 'title-62', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-63', false, 0, 'title-63', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-64', false, 0, 'title-64', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-65', false, 0, 'title-65', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-66', false, 0, 'title-66', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-67', false, 0, 'title-67', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-68', false, 0, 'title-68', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-69', false, 0, 'title-69', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-70', false, 0, 'title-70', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-71', false, 0, 'title-71', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-72', false, 0, 'title-72', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-73', false, 0, 'title-73', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-74', false, 0, 'title-74', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-75', false, 0, 'title-75', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-76', false, 0, 'title-76', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-77', false, 0, 'title-77', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-78', false, 0, 'title-78', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-79', false, 0, 'title-79', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-80', false, 0, 'title-80', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-81', false, 0, 'title-81', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-82', false, 0, 'title-82', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-83', false, 0, 'title-83', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-84', false, 0, 'title-84', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-85', false, 0, 'title-85', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-86', false, 0, 'title-86', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-87', false, 0, 'title-87', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-88', false, 0, 'title-88', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-89', false, 0, 'title-89', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-90', false, 0, 'title-90', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-91', false, 0, 'title-91', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-92', false, 0, 'title-92', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-93', false, 0, 'title-93', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-94', false, 0, 'title-94', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-95', false, 0, 'title-95', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-96', false, 0, 'title-96', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-97', false, 0, 'title-97', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-98', false, 0, 'title-98', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-99', false, 0, 'title-99', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-100', false, 0, 'title-100', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-101', false, 0, 'title-101', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-102', false, 0, 'title-102', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-103', false, 0, 'title-103', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-104', false, 0, 'title-104', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-105', false, 0, 'title-105', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-106', false, 0, 'title-106', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-107', false, 0, 'title-107', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-108', false, 0, 'title-108', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-109', false, 0, 'title-109', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-110', false, 0, 'title-110', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-111', false, 0, 'title-111', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-112', false, 0, 'title-112', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-113', false, 0, 'title-113', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-114', false, 0, 'title-114', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-115', false, 0, 'title-115', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-116', false, 0, 'title-116', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-117', false, 0, 'title-117', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-118', false, 0, 'title-118', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-119', false, 0, 'title-119', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-120', false, 0, 'title-120', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-121', false, 0, 'title-121', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-122', false, 0, 'title-122', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-123', false, 0, 'title-123', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-124', false, 0, 'title-124', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 1, 'content-125', false, 0, 'title-125', 0, 'Question', null),
       ('2022-01-14', '2022-01-14', 5, 'content1', false, 0, false, 1, 'Answer', 1),
       ('2022-01-14', '2022-01-14', 5, 'content2', false, 0, false, 1, 'Answer', 1),
       ('2022-01-14', '2022-01-14', 3, 'content3', false, 0, false, 1, 'Answer', 1),
       ('2022-01-14', '2022-01-14', 2, 'content4', false, 0, false, 1, 'Answer', 1),
       ('2022-01-14', '2022-01-14', 2, 'content5', false, 0, false, 1, 'Answer', 1),
       ('2022-01-14', '2022-01-14', 1, 'content6', false, 0, false, 1, 'Answer', 1),
       ('2022-01-14', '2022-01-14', 1, 'content7', false, 0, false, 1, 'Answer', 1);

insert into question_tag (created_date, last_modified_date, question_id, tag_id)
values ('2022-01-14', '2022-01-14', 1, 1),
       ('2022-01-14', '2022-01-14', 1, 2),
       ('2022-01-14', '2022-01-14', 1, 3),
       ('2022-01-14', '2022-01-14', 1, 4),
       ('2022-01-14', '2022-01-14', 1, 5),
       ('2022-01-14', '2022-01-14', 1, 6);

insert into comment (created_date, last_modified_date, author_id, content, deleted, parent_comment_id, parent_post_id)
values ('2022-01-14', '2022-01-14', 1, 'content-7', false, NULL, 1),
       ('2022-01-14', '2022-01-14', 2, 'content-8', false, NULL, 1),
       ('2022-01-14', '2022-01-14', 2, 'content-1', false, NULL, 1),
       ('2022-01-14', '2022-01-14', 2, 'content-2', false, 3, 1),
       ('2022-01-14', '2022-01-14', 1, 'content-3', false, 4, 1),
       ('2022-01-14', '2022-01-14', 4, 'content-4', false, 5, 1),
       ('2022-01-14', '2022-01-14', 4, 'content-5', false, 6, 1),
       ('2022-01-14', '2022-01-14', 1, 'content-6', false, 6, 1),
       ('2022-01-14', '2022-01-14', 1, '126content-4', false, NULL, 126),
       ('2022-01-14', '2022-01-14', 2, '126content-5', false, NULL, 126),
       ('2022-01-14', '2022-01-14', 3, '126content-6', false, 10, 126),
       ('2022-01-14', '2022-01-14', 1, '126content-1', false, NULL, 126),
       ('2022-01-14', '2022-01-14', 2, '126content-2', false, 12, 126),
       ('2022-01-14', '2022-01-14', 1, '126content-3', false, 12, 126),
       ('2022-01-14', '2022-01-14', 1, '127content-4', false, NULL, 127),
       ('2022-01-14', '2022-01-14', 2, '127content-5', false, NULL, 127),
       ('2022-01-14', '2022-01-14', 3, '127content-6', false, 16, 127),
       ('2022-01-14', '2022-01-14', 1, '127content-1', false, NULL, 127),
       ('2022-01-14', '2022-01-14', 2, '127content-2', false, 18, 127),
       ('2022-01-14', '2022-01-14', 1, '127content-3', false, 18, 127),
       ('2022-01-14', '2022-01-14', 1, '128content-4', false, NULL, 128),
       ('2022-01-14', '2022-01-14', 2, '128content-5', false, NULL, 128),
       ('2022-01-14', '2022-01-14', 3, '128content-6', false, 22, 128),
       ('2022-01-14', '2022-01-14', 1, '128content-1', false, NULL, 128),
       ('2022-01-14', '2022-01-14', 2, '128content-2', false, 24, 128),
       ('2022-01-14', '2022-01-14', 1, '128content-3', false, 24, 128),
       ('2022-01-14', '2022-01-14', 1, '129content-4', false, NULL, 129),
       ('2022-01-14', '2022-01-14', 2, '129content-5', false, NULL, 129),
       ('2022-01-14', '2022-01-14', 3, '129content-6', false, 28, 129),
       ('2022-01-14', '2022-01-14', 1, '129content-1', false, NULL, 129),
       ('2022-01-14', '2022-01-14', 2, '129content-2', false, 30, 129),
       ('2022-01-14', '2022-01-14', 1, '129content-3', false, 30, 129),
       ('2022-01-14', '2022-01-14', 1, '130content-4', false, NULL, 130),
       ('2022-01-14', '2022-01-14', 2, '130content-5', false, NULL, 130),
       ('2022-01-14', '2022-01-14', 3, '130content-6', false, 34, 130),
       ('2022-01-14', '2022-01-14', 1, '130content-1', false, NULL, 130),
       ('2022-01-14', '2022-01-14', 2, '130content-2', false, 36, 130),
       ('2022-01-14', '2022-01-14', 1, '130content-3', false, 36, 130),
       ('2022-01-14', '2022-01-14', 1, '131content-4', false, NULL, 131),
       ('2022-01-14', '2022-01-14', 2, '131content-5', false, NULL, 131),
       ('2022-01-14', '2022-01-14', 3, '131content-6', false, 40, 131),
       ('2022-01-14', '2022-01-14', 1, '131content-1', false, NULL, 131),
       ('2022-01-14', '2022-01-14', 2, '131content-2', false, 42, 131),
       ('2022-01-14', '2022-01-14', 1, '131content-3', false, 42, 131),
       ('2022-01-14', '2022-01-14', 1, '132content-4', false, NULL, 132),
       ('2022-01-14', '2022-01-14', 2, '132content-5', false, NULL, 132),
       ('2022-01-14', '2022-01-14', 3, '132content-6', false, 46, 132),
       ('2022-01-14', '2022-01-14', 1, '132content-1', false, NULL, 132),
       ('2022-01-14', '2022-01-14', 2, '132content-2', false, 48, 132),
       ('2022-01-14', '2022-01-14', 1, '132content-3', false, 48, 132);

insert into vote (member_id, post_id, vote_type)
values (1, 1, 'UP'),
       (2, 1, 'UP'),
       (3, 1, 'UP'),
       (4, 1, 'UP'),
       (5, 1, 'UP'),
       (6, 1, 'UP'),
       (7, 1, 'UP'),
       (8, 1, 'UP'),
       (9, 1, 'UP'),
       (1, 126, 'UP'),
       (2, 126, 'UP'),
       (3, 126, 'UP'),
       (4, 126, 'UP'),
       (5, 126, 'UP'),
       (6, 126, 'UP'),
       (7, 126, 'UP'),
       (8, 126, 'UP'),
       (9, 126, 'UP'),
       (2, 1, 'DOWN'),
       (3, 1, 'DOWN'),
       (4, 1, 'DOWN'),
       (5, 1, 'DOWN');