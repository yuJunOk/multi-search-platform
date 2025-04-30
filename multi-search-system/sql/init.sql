create database db_multi

create table tb_user
(
    id          bigint auto_increment comment 'id'
        primary key,
    user_name   varchar(256)                       null comment '用户名',
    avatar_url  varchar(1024)                      null comment '头像链接',
    gender      tinyint                            null comment '性别',
    login_name  varchar(256)                       null comment '账号',
    login_pwd   varchar(512)                       not null comment '密码',
    phone       varchar(128)                       null comment '电话',
    email       varchar(512)                       null comment '邮箱',
    status      int      default 0                 not null comment '用户状态',
    user_role   int      default 0                 not null comment '用户角色 0为普通用户 1为管理员',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     tinyint  default 0                 not null comment '是否删除',
    constraint uni_login_name
        unique (login_name)
)
    comment '用户表';

INSERT INTO `db_multi`.`tb_user` (`user_name`, `avatar_url`, `gender`, `login_name`, `login_pwd`, `phone`, `email`, `status`, `user_role`, `create_time`, `update_time`, `deleted`) VALUES ('小a', 'https://vitejs.cn/vite3-cn/logo.svg', 0, '13416393834', '3371e00731ba0313ea06d12d97f0844e', '13416393834', '1375841038@qq.com', 0, 1, '2025-04-15 00:06:41', '2025-04-15 00:06:41', 0);

create table tb_post
(
    id          bigint auto_increment comment 'id'
        primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    thumb_num   int      default 0                 not null comment '点赞数',
    favour_num  int      default 0                 not null comment '收藏数',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     tinyint  default 0                 not null comment '是否删除'
)
    comment '帖子';

create index idx_user_id
    on tb_post (user_id);

create table tb_post_favour
(
    id          bigint auto_increment comment 'id'
        primary key,
    post_id     bigint                             not null comment '帖子 id',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '帖子收藏';

create index idx_post_id
    on tb_post_favour (post_id);

create index idx_user_id
    on tb_post_favour (user_id);

create table tb_post_thumb
(
    id          bigint auto_increment comment 'id'
        primary key,
    post_id     bigint                             not null comment '帖子 id',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '帖子点赞';

create index idx_post_id
    on tb_post_thumb (post_id);

create index idx_user_id
    on tb_post_thumb (user_id);