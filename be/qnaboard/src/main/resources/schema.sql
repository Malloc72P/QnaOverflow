drop table if exists comment cascade;
drop table if exists question_tag cascade;
drop table if exists vote cascade;
drop table if exists post cascade;
drop table if exists tag cascade;
drop table if exists members cascade;

create table comment
(
    comment_id         bigint        not null auto_increment,
    created_date       datetime(6)   not null,
    last_modified_date datetime(6),
    content            varchar(1024) not null,
    deleted            bit           not null,
    author_id          bigint,
    parent_comment_id  bigint,
    parent_post_id     bigint,
    primary key (comment_id)
) engine = InnoDB;


create table members
(
    members_id         bigint       not null auto_increment,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6),
    deleted            bit          not null,
    email              varchar(255),
    nickname           varchar(255) not null,
    role               varchar(255) not null,
    primary key (members_id)
) engine = InnoDB;


create table post
(
    post_type          varchar(31)  not null,
    post_id            bigint       not null auto_increment,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6),
    content            text(2000),
    deleted            bit          not null,
    score              bigint       not null,
    title              varchar(200) not null,
    view_count         bigint,
    author_id          bigint,
    question_id        bigint,
    primary key (post_id)
) engine = InnoDB;


create table question_tag
(
    question_id        bigint      not null,
    tag_id             bigint      not null,
    created_date       datetime(6) not null,
    last_modified_date datetime(6),
    primary key (question_id, tag_id)
) engine = InnoDB;

create table tag
(
    tag_id             bigint       not null auto_increment,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6),
    deleted            bit          not null,
    description        varchar(1024),
    name               varchar(40) not null,
    author_id          bigint,
    primary key (tag_id)
) engine = InnoDB;

create table vote
(
    id        bigint not null auto_increment,
    vote_type varchar(30),
    member_id bigint,
    post_id   bigint,
    primary key (id)
) engine = InnoDB;

alter table comment
    add constraint fk_comment_author foreign key (author_id) references members (members_id);

alter table comment
    add constraint fk_comment_child_comment foreign key (parent_comment_id) references comment (comment_id);

alter table comment
    add constraint fk_comment_post foreign key (parent_post_id) references post (post_id);

alter table post
    add constraint fk_post_author foreign key (author_id) references members (members_id);

alter table post
    add constraint fk_answer_question foreign key (question_id) references post (post_id);

alter table question_tag
    add constraint fk_question_tag_post foreign key (question_id) references post (post_id);

alter table question_tag
    add constraint fk_question_tag_tag foreign key (tag_id) references tag (tag_id);

alter table tag
    add constraint fk_tag_author foreign key (author_id) references members (members_id);

alter table vote
    add constraint fk_vote_author foreign key (member_id) references members (members_id);

alter table vote
    add constraint fk_vote_post foreign key (post_id) references post (post_id);

create index index_post_create_date on post (post_type, created_date);
