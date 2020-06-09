CREATE DATABASE `crowd` CHARACTER SET utf8;

use crowd;

drop table if exists t_admin;
CREATE TABLE t_admin
(
    id          INT          NOT NULL auto_increment, # 主键
    login_acct  VARCHAR(255) NOT NULL,                # 登录账号
    user_pswd   CHAR(32)     NOT NULL,                # 登录密码
    user_name   VARCHAR(255) NOT NULL,                # 昵称
    email       VARCHAR(255) NOT NULL,                # 邮件地址
    create_time CHAR(19),                             # 创建时间
    PRIMARY KEY (id)
);

ALTER TABLE t_admin
    ADD UNIQUE INDEX (`login_acct`);

ALTER TABLE `crowd`.`t_admin`
    CHANGE `user_pswd` `user_pswd` CHAR(100) CHARSET utf8 COLLATE utf8_general_ci NOT NULL;

CREATE TABLE `crowd`.`t_role`
(
    `id`   INT NOT NULL AUTO_INCREMENT,
    `name` CHAR(100),
    PRIMARY KEY (`id`)
);

create table t_menu
(
    id   int(11) not null auto_increment, # 主键
    pid  int(11),                         # 父节点 id
    name varchar(200),
    url  varchar(200),                    # 点击节点时跳转的位置
    icon varchar(200),
    primary key (id)
);

INSERT INTO `t_menu` (`id`, `pid`, `name`, `icon`, `url`)
VALUES ('1', NULL, '系统权限菜单', 'glyphicon glyphicon-th-list', NULL),
       ('2', '1', ' 控制面板', 'glyphicon glyphicon-dashboard', 'main.htm'),
       ('3', '1', '权限管理', 'glyphicon glyphicon glyphicon-tasks', NULL),
       ('4', '3', ' 用户维护', 'glyphicon glyphicon-user', 'user/index.htm'),
       ('5', '3', ' 角色维护', 'glyphicon glyphicon-king', 'role/index.htm'),
       ('6', '3', ' 菜单维护', 'glyphicon glyphicon-lock', 'permission/index.htm'),
       ('7', '1', ' 业务审核', 'glyphicon glyphicon-ok', NULL),
       ('8', '7', ' 实名认证审核', 'glyphicon glyphicon-check', 'auth_cert/index.htm'),
       ('9', '7', ' 广告审核', 'glyphicon glyphicon-check', 'auth_adv/index.htm'),
       ('10', '7', ' 项目审核', 'glyphicon glyphicon-check', 'auth_project/index.htm'),
       ('11', '1', ' 业务管理', 'glyphicon glyphicon-th-large', NULL),
       ('12', '11', ' 资质维护', 'glyphicon glyphicon-picture', 'cert/index.htm'),
       ('13', '11', ' 分类管理', 'glyphicon glyphicon-equalizer', 'certtype/index.htm'),
       ('14', '11', ' 流程管理', 'glyphicon glyphicon-random', 'process/index.htm'),
       ('15', '11', ' 广告管理', 'glyphicon glyphicon-hdd', 'advert/index.htm'),
       ('16', '11', ' 消息模板', 'glyphicon glyphicon-comment', 'message/index.htm'),
       ('17', '11', ' 项目分类', 'glyphicon glyphicon-list', 'projectType/index.htm'),
       ('18', '11', ' 项目标签', 'glyphicon glyphicon-tags', 'tag/index.htm'),
       ('19', '1', ' 参数管理', 'glyphicon glyphicon-list-alt', 'param/index.htm');

# 创建关联关系表
CREATE TABLE `crowd`.`inner_admin_role`
(
    `id`       INT NOT NULL AUTO_INCREMENT,
    `admin_id` INT,
    `role_id`  INT,
    PRIMARY KEY (`id`)
);

CREATE TABLE `t_auth`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(200) DEFAULT NULL,
    `title`       varchar(200) DEFAULT NULL,
    `category_id` int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO t_auth (id, `name`, title, category_id)
VALUES (1, '', '用户模块', NULL),
       (2, 'user:delete', '删除', 1),
       (3, 'user:get', '查询', 1),
       (4, '', '角色模块', NULL),
       (5, 'role:delete', '删除', 4),
       (6, 'role:get', '查询', 4),
       (7, 'role:add', '新增', 4);

CREATE TABLE `crowd`.`inner_role_auth`
(
    `id`      INT NOT NULL AUTO_INCREMENT,
    `role_id` INT,
    `auth_id` INT,
    PRIMARY KEY (`id`)
);
