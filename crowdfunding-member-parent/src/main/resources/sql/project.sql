CREATE TABLE t_type
(
    id     INT(11) NOT NULL auto_increment,
    NAME   VARCHAR(255) COMMENT '分类名称',
    remark VARCHAR(255) COMMENT '分类介绍',
    PRIMARY KEY (id)
);

CREATE TABLE t_project_type
(
    id        INT NOT NULL auto_increment,
    projectid INT(11),
    typeid    INT(11),
    PRIMARY KEY (id)
);

CREATE TABLE t_tag
(
    id   INT(11) NOT NULL auto_increment,
    pid  INT(11),
    NAME VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE t_project_tag
(
    id        INT(11) NOT NULL auto_increment,
    projectid INT(11),
    tagid     INT(11),
    PRIMARY KEY (id)
);

CREATE TABLE t_project
(
    id                  INT(11) NOT NULL auto_increment,
    project_name        VARCHAR(255) COMMENT '项目名称',
    project_description VARCHAR(255) COMMENT '项目描述',
    money               BIGINT(11) COMMENT '筹集金额',
    DAY                 INT(11) COMMENT '筹集天数',
    STATUS              INT(4) COMMENT '0-即将开始，1-众筹中，2-众筹成功，3-众筹失败',
    deploydate          VARCHAR(10) COMMENT '项目发起时间',
    supportmoney        BIGINT(11) COMMENT '已筹集到的金额',
    supporter           INT(11) COMMENT '支持人数',
    COMPLETION          INT(3) COMMENT '百分比完成度',
    memberid            INT(11) COMMENT '发起人的会员id',
    createdate          VARCHAR(19) COMMENT '项目创建时间',
    follower            INT(11) COMMENT '关注人数',
    header_picture_path VARCHAR(255) COMMENT '头图路径',
    PRIMARY KEY (id)
);

CREATE TABLE t_project_item_pic
(
    id            INT(11) NOT NULL auto_increment,
    projectid     INT(11),
    item_pic_path VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE t_member_launch_info
(
    id                 INT(11) NOT NULL auto_increment,
    memberid           INT(11) COMMENT '会员id',
    description_simple VARCHAR(255) COMMENT '简单介绍',
    description_detail VARCHAR(255) COMMENT '详细介绍',
    phone_num          VARCHAR(255) COMMENT '联系电话',
    service_num        VARCHAR(255) COMMENT '客服电话',
    PRIMARY KEY (id)
);

CREATE TABLE t_return
(
    id               INT(11) NOT NULL auto_increment,
    projectid        INT(11),
    type             INT(4) COMMENT '0 - 实物回报， 1 虚拟物品回报',
    supportmoney     INT(11) COMMENT '支持金额',
    content          VARCHAR(255) COMMENT '回报内容',
    count            INT(11) COMMENT '回报产品限额，“0”为不限回报数量',
    signalpurchase   INT(11) COMMENT '是否设置单笔限购',
    purchase         INT(11) COMMENT '具体限购数量',
    freight          INT(11) COMMENT '运费，“0”为包邮',
    invoice          INT(4) COMMENT '0 - 不开发票， 1 - 开发票',
    returndate       INT(11) COMMENT '项目结束后多少天向支持者发送回报',
    describ_pic_path VARCHAR(255) COMMENT '说明图片路径',
    PRIMARY KEY (id)
);

CREATE TABLE t_member_confirm_info
(
    id       INT(11) NOT NULL auto_increment,
    memberid INT(11) COMMENT '会员id',
    paynum   VARCHAR(200) COMMENT '易付宝企业账号',
    cardnum  VARCHAR(200) COMMENT '法人身份证号',
    PRIMARY KEY (id)
);
