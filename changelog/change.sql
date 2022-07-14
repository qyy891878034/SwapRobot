-- 20191028 新充提
alter table t_currency
    add third_currency varchar(20) DEFAULT '' comment '充提名称';
alter table t_currency
    add third_currency2 varchar(20) DEFAULT '' comment 'usdt ERC20充提币种名称';
alter table t_currency
    add confirm varchar(20) DEFAULT '' comment '充提名称';
alter table t_currency
    add protocol2 smallint DEFAULT 0 comment '协议2';
alter table t_currency
    add confirm2 varchar(20) DEFAULT '' comment '充提2名称';
alter table t_currency drop contract_address;
alter table t_currency drop contract_decimals;
alter table t_currency drop extract_currency_id;


CREATE TABLE `t_re_config`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(200) DEFAULT '' COMMENT '备注',
    `version`     int(11) DEFAULT '0' COMMENT '版本号',
    `url`         varchar(100) DEFAULT '' COMMENT 'url地址',
    `store_no`    varchar(20)  DEFAULT '' COMMENT '商户编码',
    `key`         varchar(50)  DEFAULT '' COMMENT 'key',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
INSERT INTO `t_menu` (`url`, `parent_id`, `name`, `position`)
VALUES ('/admin/reConfig/toList', '0', '配置', '1');


-- 登录日志
CREATE TABLE `t_user_admin_login`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(200) DEFAULT '' COMMENT '备注',
    `version`     int(11) DEFAULT '0' COMMENT '版本号',
    `admin_name`  varchar(20)  DEFAULT 0 COMMENT '登录用户',
    `ip`          varchar(50)  DEFAULT '' COMMENT '登录ip',
    `city`        varchar(50)  DEFAULT '' COMMENT '登录城市',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
INSERT INTO `t_menu` (`url`, `parent_id`, `name`, `position`)
VALUES ('/admin/userAdminLogin/toList', '0', '登录日志', '1');


alter table t_passphrase
    add `create_time` datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间';
alter table t_passphrase
    add `update_time` datetime DEFAULT CURRENT_TIMESTAMP comment '创建时间';
alter table t_passphrase
    add `remark` varchar(200) DEFAULT '' comment '创建时间';
alter table t_passphrase
    add `version` int(6) DEFAULT 0 comment '创建时间';



alter table t_sms_config
    add email_region_id varchar(20) default '' comment '区域';
alter table t_sms_config
    add email_access_key_id varchar(50) default '' comment 'key';
alter table t_sms_config
    add email_secret varchar(50) default '' comment '私钥';
alter table t_sms_config
    add email_account_name varchar(50) default '' comment '账户';
alter table t_sms_config
    add email_tag varchar(20) default '' comment '标签';


CREATE TABLE `t_address`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(200) DEFAULT '' COMMENT '备注',
    `version`     int(11) DEFAULT '0' COMMENT '版本号',
    `address`     varchar(200) DEFAULT '' COMMENT '登录用户',
    `private_key` varchar(200) DEFAULT '' COMMENT '登录ip',
    `protocol`    int(5) DEFAULT 0 COMMENT '登录城市',
    `user_id`     bigint(20) DEFAULT 0 COMMENT '登录城市',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;



CREATE TABLE `t_passphrase_index`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`        varchar(200) DEFAULT '' COMMENT '备注',
    `version`       int(11) DEFAULT '0' COMMENT '版本号',
    `passphrase_id` bigint(20) DEFAULT 0 COMMENT '登录用户',
    `index`         int(5) DEFAULT 0 COMMENT '登录ip',
    `protocol`      int(5) DEFAULT 0 COMMENT '登录城市',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


-----------空投总私钥/汇总地址/提币私钥-------------
CREATE TABLE `t_total_address`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(200) DEFAULT '' COMMENT '备注',
    `version`     int(11) DEFAULT '0' COMMENT '版本号',
    `user_id`     bigint(20) DEFAULT 0 COMMENT '用户id',
    `currency_id` bigint(20) DEFAULT 0 COMMENT '币种id',
    `protocol`    int(5) DEFAULT 0 COMMENT '协议',
    `airdrop`     varchar(300) DEFAULT '' COMMENT '空1',
    `extract`     varchar(300) DEFAULT '' COMMENT '空2',
    `collect`     varchar(300) DEFAULT '' COMMENT '空3',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-------监控哪些合约--------
CREATE TABLE `t_clamp_record`
(
    `id`               bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`           varchar(200) DEFAULT '' COMMENT '备注',
    `version`          int(11) DEFAULT '0' COMMENT '版本号',
    `chain_name`       varchar(300) DEFAULT '' COMMENT '链名称',
    `address`          varchar(300) DEFAULT '' COMMENT '夹的地址',
    `contract_address` varchar(300) DEFAULT '' COMMENT '夹的币合约地址',
    `hash`             varchar(300) DEFAULT '' COMMENT '夹的哈希',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;



CREATE TABLE `t_currency_config`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time`   datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`        varchar(200)   DEFAULT '' COMMENT '备注',
    `version`       int(11) DEFAULT '0' COMMENT '版本号',
    `exchange_code` tinyint(2) DEFAULT 0 COMMENT '代码',
    `currency_name` varchar(300)   DEFAULT '' COMMENT '币种',
    `volume`        decimal(20, 8) DEFAULT 0 COMMENT '面值',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;