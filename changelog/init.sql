DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


CREATE TABLE QRTZ_JOB_DETAILS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_CALENDARS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
)ENGINE=InnoDB;

CREATE TABLE QRTZ_LOCKS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL,
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
)ENGINE=InnoDB;



CREATE TABLE `t_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT '0' COMMENT '?????????',
  `name` varchar(50) DEFAULT '' COMMENT '??????',
  `val` varchar(100) DEFAULT '',
  `remarks` varchar(200) DEFAULT '',
  `type` int(11) DEFAULT '0' COMMENT '1.?????? 2.?????? 3.??????',
  PRIMARY KEY (`id`),
  UNIQUE KEY `config_name` (`name`),
  KEY `config_name_time` (`create_time`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
-- ----------------------------
-- Records of t_config
-- ----------------------------
INSERT INTO `t_config` VALUES ('1', '2018-10-31 17:18:24', '2018-11-01 08:56:53', null, '0', 'auth.minute.count', '6', '????????????????????????', '1');
INSERT INTO `t_config` VALUES ('2', '2018-10-31 18:27:14', '2018-10-31 18:27:14', null, '0', 'auth.hour.count', '10', '????????????????????????', '1');
INSERT INTO `t_config` VALUES ('3', '2018-10-31 18:27:23', '2018-10-31 18:27:23', null, '0', 'auth.day.count', '40', '?????????????????????', '1');
INSERT INTO `t_config` VALUES ('4', '2018-11-01 13:14:24', '2018-11-01 13:14:43', null, '0', 'default.pwd', '123456', '??????????????????', '1');

-- ----------------------------
-- Table structure for t_content
-- ----------------------------
DROP TABLE IF EXISTS `t_content`;
CREATE TABLE `t_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT NULL COMMENT '?????????',
  `title` varchar(100) DEFAULT NULL COMMENT '??????',
  `content` varchar(4000) DEFAULT NULL COMMENT '??????',
  `status` int(11) DEFAULT '0' COMMENT '??????0?????????  1??????',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_content
-- ----------------------------

-- ----------------------------
-- Table structure for t_logger
-- ----------------------------
DROP TABLE IF EXISTS `t_logger`;
CREATE TABLE `t_logger` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT 0 COMMENT '?????????',
  `type` int(11) DEFAULT -1 COMMENT '?????????1.?????? 2.?????????',
  `title` varchar(50) DEFAULT '' COMMENT '??????',
  `content` varchar(255) DEFAULT '' COMMENT '??????',
  `admin_id` bigint DEFAULT -1 COMMENT '?????????ID',
  `create_date` varchar(50) DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`id`),
  KEY `t_logger_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_logger
-- ----------------------------

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT 0 COMMENT '?????????',
  `url` varchar(200) COLLATE utf8_bin DEFAULT '' COMMENT '????????????',
  `parent_id` bigint(11) DEFAULT 0 COMMENT '???id',
  `name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '????????????',
  `position` int(11) NOT NULL COMMENT '??????',
  PRIMARY KEY (`id`),
  KEY `t_menu_time` (`create_time`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='?????????';

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '2018-10-31 19:42:52', '2018-10-31 19:46:59', null, '0', '', null, '????????????', '1');
INSERT INTO `t_menu` VALUES ('2', '2018-10-31 19:43:18', '2018-10-31 19:43:18', null, '0', '', null, '????????????', '2');
INSERT INTO `t_menu` VALUES ('3', '2018-10-31 19:43:45', '2018-11-01 16:18:35', null, '0', '', null, '????????????', '13');
INSERT INTO `t_menu` VALUES ('4', '2018-10-31 19:44:08', '2018-10-31 19:44:08', null, '0', '/admin/userAdmin/toList', '1', '???????????????', '1');
INSERT INTO `t_menu` VALUES ('5', '2018-10-31 19:44:28', '2018-10-31 19:44:28', null, '0', '/admin/user/toList', '1', '????????????', '2');
INSERT INTO `t_menu` VALUES ('6', '2018-10-31 19:47:23', '2018-10-31 19:47:23', null, '0', '/admin/menu/toList', '3', '????????????', '1');
INSERT INTO `t_menu` VALUES ('7', '2018-10-31 19:47:42', '2018-10-31 19:47:42', null, '0', '/admin/role/toList', '3', '????????????', '2');
INSERT INTO `t_menu` VALUES ('8', '2018-10-31 19:48:02', '2018-10-31 19:48:02', null, '0', '/admin/config/toList', '3', '????????????', '3');
INSERT INTO `t_menu` VALUES ('9', '2018-10-31 19:52:29', '2018-11-01 13:15:12', null, '0', '/admin/logger/toList', '2', '????????????', '1');
INSERT INTO `t_menu` VALUES ('10', '2018-11-01 16:18:24', '2018-11-01 16:18:24', null, '0', '', null, '????????????', '3');
INSERT INTO `t_menu` VALUES ('11', '2018-11-01 16:18:53', '2018-11-01 16:18:53', null, '0', '/admin/task/toList', '10', '??????????????????', '1');
INSERT INTO `t_menu` VALUES ('12', '2018-11-01 16:19:14', '2018-11-01 16:19:14', null, '0', '/admin/taskrepeat/toList', '10', '??????????????????', '2');
INSERT INTO `t_menu` VALUES ('13', '2018-11-01 18:13:52', '2018-11-01 18:13:52', null, '0', '', null, '????????????', '12');
INSERT INTO `t_menu` VALUES ('14', '2018-11-01 18:14:13', '2018-11-01 18:14:13', null, '0', '/admin/content/toList', '13', '????????????', '1');
INSERT INTO `t_menu` VALUES ('15', '2018-11-01 18:14:44', '2018-11-01 18:14:44', null, '0', '/admin/slide/toList', '13', '???????????????', '2');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT '0' COMMENT '?????????',
  `name` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '????????????',
  PRIMARY KEY (`id`),
  KEY `t_role_time` (`create_time`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='?????????';

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '2018-11-01 08:47:53', '2018-11-01 08:47:53', null, '0', '???????????????');

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '??????id',
  `menu_id` int(11) NOT NULL COMMENT '??????id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='???????????????';

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES ('1', '1', '1');
INSERT INTO `t_role_menu` VALUES ('2', '1', '2');
INSERT INTO `t_role_menu` VALUES ('3', '1', '3');
INSERT INTO `t_role_menu` VALUES ('4', '1', '4');
INSERT INTO `t_role_menu` VALUES ('5', '1', '5');
INSERT INTO `t_role_menu` VALUES ('6', '1', '6');
INSERT INTO `t_role_menu` VALUES ('7', '1', '7');
INSERT INTO `t_role_menu` VALUES ('8', '1', '8');
INSERT INTO `t_role_menu` VALUES ('9', '1', '9');
INSERT INTO `t_role_menu` VALUES ('10', '1', '10');
INSERT INTO `t_role_menu` VALUES ('11', '1', '11');
INSERT INTO `t_role_menu` VALUES ('12', '1', '12');
INSERT INTO `t_role_menu` VALUES ('13', '1', '13');
INSERT INTO `t_role_menu` VALUES ('14', '1', '14');

CREATE TABLE `t_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT '0' COMMENT '?????????',
  `menu_id` bigint(20) DEFAULT 0 COMMENT '??????id',
  `permission_name` varchar(32) DEFAULT '' COMMENT '????????????',
  `method_name` varchar(100) DEFAULT '' COMMENT '??????????????????',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `role_id` bigint(20) DEFAULT 0 COMMENT '??????id',
  `permission_id` bigint(20) DEFAULT 0 COMMENT '??????id',
  PRIMARY KEY (`id`),
  KEY `t_role_permission_roleid` (`role_id`),
  KEY `t_role_permission_permissionid` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for t_slide
-- ----------------------------
DROP TABLE IF EXISTS `t_slide`;
CREATE TABLE `t_slide` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT NULL COMMENT '?????????',
  `path` varchar(200) DEFAULT NULL COMMENT '????????????',
  `href_path` varchar(200) DEFAULT NULL COMMENT '????????????',
  `position` int(11) DEFAULT NULL COMMENT '??????',
  `pic` longblob COMMENT '??????',
  `out_href` int(11) DEFAULT '1' COMMENT '??????',
  `status` int(11) DEFAULT '0' COMMENT '?????? 0????????? 1??????',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_slide
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT '0' COMMENT '?????????',
  `name` varchar(50) DEFAULT '' COMMENT '??????',
  `pwd` varchar(100) DEFAULT '' COMMENT '??????',
  `trade_pwd` varchar(100) DEFAULT '' COMMENT '????????????',
  `phone` varchar(20) DEFAULT '' COMMENT '?????????',
  `email` varchar(50) DEFAULT '' COMMENT '??????',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '??????id',
  `real_name` varchar(20) DEFAULT '' COMMENT '????????????',
  `idcart` varchar(50) DEFAULT '' COMMENT '?????????',
  `bank_name` varchar(50) DEFAULT '' COMMENT '????????????',
  `bank_no` varchar(50) DEFAULT '' COMMENT '??????',
  `bank_address` varchar(50) DEFAULT '' COMMENT '????????????',
  `tree_level` int(11) DEFAULT '1' COMMENT '?????????',
  `tree_info` varchar(2000) DEFAULT '' COMMENT '???????????????',
  `status` int(11) DEFAULT '0' COMMENT '?????? 0?????????  1??????  2?????? ',
  `auth_status` int(11) DEFAULT '0' COMMENT '?????? 0?????????  1?????????  2????????? 3???????????? ',
  `pwd_strength` int(11) DEFAULT '0' COMMENT '???????????? 0???  1???  2???',
  `last_login_time` datetime DEFAULT NULL COMMENT '??????????????????',
  `zone` varchar(20) DEFAULT '' COMMENT '??????',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_user_name` (`name`),
  KEY `t_user_time` (`create_time`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '2018-11-01 13:15:50', '2018-11-01 13:16:03', null, '0', '112233', 'e10adc3949ba59abbe56e057f20f883e', 'e10adc3949ba59abbe56e057f20f883e', '18674006013', '456@qq.com', '1', '1', '2', '3', '4', '5', '2', ',1,12,', '1', '0', null, null, '');

-- ----------------------------
-- Table structure for t_user_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_user_admin`;
CREATE TABLE `t_user_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '??????',
  `create_time` datetime DEFAULT NULL COMMENT '????????????',
  `update_time` datetime DEFAULT NULL COMMENT '????????????',
  `remark` varchar(200) DEFAULT NULL COMMENT '??????',
  `version` int(11) DEFAULT '0' COMMENT '?????????',
  `name` varchar(50) DEFAULT '' COMMENT '??????',
  `pwd` varchar(100) DEFAULT '' COMMENT '??????',
  `status` int(11) DEFAULT '0' COMMENT '0????????? 1?????? 2??????',
  `role_id` bigint(20) DEFAULT '0' COMMENT '??????id',
  PRIMARY KEY (`id`),
  KEY `t_user_admin_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_admin
-- ----------------------------
INSERT INTO `t_user_admin` VALUES ('1', '2018-11-01 09:56:15', '2018-11-01 13:15:31', null, '0', 'admin', '879d6457d5315e047d842e5507c262b5', '1', '1');

-- ??????ada2sd=111334455

-- ????????????