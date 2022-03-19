/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50622
Source Host           : 127.0.0.1:3306
Source Database       : base

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-08-05 10:14:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_zone
-- ----------------------------
DROP TABLE IF EXISTS `t_zone`;
CREATE TABLE `t_zone` (
  `zone` varchar(20) DEFAULT '' COMMENT '区号',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  `position` smallint(6) DEFAULT '0' COMMENT '位置',
  `cn_name` varchar(20) DEFAULT NULL,
  `status` tinyint(2) DEFAULT '0' COMMENT '状态 0不使用 1使用',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_zone
-- ----------------------------
INSERT INTO `t_zone` VALUES ('93', 'Afghanistan', '2', '阿富汗', '1');
INSERT INTO `t_zone` VALUES ('355', 'Albania', '3', '阿尔巴尼亚', '1');
INSERT INTO `t_zone` VALUES ('213', 'Algeria', '4', '阿尔及利亚', '1');
INSERT INTO `t_zone` VALUES ('376', 'Andorra', '5', '安道尔共和国', '1');
INSERT INTO `t_zone` VALUES ('244', 'Angola', '1', '安哥拉', '1');
INSERT INTO `t_zone` VALUES ('1264', 'Anguilla', '6', '安圭拉岛', '1');
INSERT INTO `t_zone` VALUES ('1268', 'Antigua and Barbuda', '7', '安提瓜和巴布达', '1');
INSERT INTO `t_zone` VALUES ('54', 'Argentina', '8', '阿根廷', '1');
INSERT INTO `t_zone` VALUES ('374', 'Armenia', '9', '亚美尼亚', '1');
INSERT INTO `t_zone` VALUES ('247', 'Ascension', '10', '阿森松', '1');
INSERT INTO `t_zone` VALUES ('61', 'Australia', '11', '澳大利亚', '1');
INSERT INTO `t_zone` VALUES ('43', 'Austria', '12', '奥地利', '1');
INSERT INTO `t_zone` VALUES ('994', 'Azerbaijan', '13', '阿塞拜疆', '1');
INSERT INTO `t_zone` VALUES ('1242', 'Bahamas', '14', '巴哈马', '1');
INSERT INTO `t_zone` VALUES ('973', 'Bahrain', '15', '巴林', '1');
INSERT INTO `t_zone` VALUES ('880', 'Bangladesh', '16', '孟加拉国', '1');
INSERT INTO `t_zone` VALUES ('1246', 'Barbados', '17', '巴巴多斯', '1');
INSERT INTO `t_zone` VALUES ('375', 'Belarus', '18', '白俄罗斯', '1');
INSERT INTO `t_zone` VALUES ('32', 'Belgium', '19', '比利时', '1');
INSERT INTO `t_zone` VALUES ('501', 'Belize', '20', '伯利兹', '1');
INSERT INTO `t_zone` VALUES ('229', 'Benin', '21', '贝宁', '1');
INSERT INTO `t_zone` VALUES ('1441', 'Bermuda Is.', '22', '百慕大群岛', '1');
INSERT INTO `t_zone` VALUES ('591', 'Bolivia', '23', '玻利维亚', '1');
INSERT INTO `t_zone` VALUES ('267', 'Botswana', '24', '博茨瓦纳', '1');
INSERT INTO `t_zone` VALUES ('55', 'Brazil', '25', '巴西', '1');
INSERT INTO `t_zone` VALUES ('673', 'Brunei', '26', '文莱', '1');
INSERT INTO `t_zone` VALUES ('359', 'Bulgaria', '27', '保加利亚', '1');
INSERT INTO `t_zone` VALUES ('226', 'Burkina-faso', '28', '布基纳法索', '1');
INSERT INTO `t_zone` VALUES ('95', 'Burma', '29', '缅甸', '1');
INSERT INTO `t_zone` VALUES ('257', 'Burundi', '30', '布隆迪', '1');
INSERT INTO `t_zone` VALUES ('237', 'Cameroon', '31', '喀麦隆', '1');
INSERT INTO `t_zone` VALUES ('1', 'Canada', '32', '加拿大', '1');
INSERT INTO `t_zone` VALUES ('1345', 'Cayman Is.', '33', '开曼群岛', '1');
INSERT INTO `t_zone` VALUES ('236', 'Central African Republic', '34', '中非共和国', '1');
INSERT INTO `t_zone` VALUES ('235', 'Chad', '35', '乍得', '1');
INSERT INTO `t_zone` VALUES ('56', 'Chile', '36', '智利', '1');
INSERT INTO `t_zone` VALUES ('86', 'China', '0', '中国', '1');
INSERT INTO `t_zone` VALUES ('57', 'Colombia', '38', '哥伦比亚', '1');
INSERT INTO `t_zone` VALUES ('242', 'Congo', '39', '刚果', '1');
INSERT INTO `t_zone` VALUES ('682', 'Cook Is.', '40', '库克群岛', '1');
INSERT INTO `t_zone` VALUES ('506', 'Costa Rica', '41', '哥斯达黎加', '1');
INSERT INTO `t_zone` VALUES ('53', 'Cuba', '42', '古巴', '1');
INSERT INTO `t_zone` VALUES ('357', 'Cyprus', '43', '塞浦路斯', '1');
INSERT INTO `t_zone` VALUES ('420', 'Czech Republic', '44', '捷克', '1');
INSERT INTO `t_zone` VALUES ('45', 'Denmark', '45', '丹麦', '1');
INSERT INTO `t_zone` VALUES ('253', 'Djibouti', '46', '吉布提', '1');
INSERT INTO `t_zone` VALUES ('1890', 'Dominica Rep.', '47', '多米尼加共和国', '1');
INSERT INTO `t_zone` VALUES ('593', 'Ecuador', '48', '厄瓜多尔', '1');
INSERT INTO `t_zone` VALUES ('20', 'Egypt', '49', '埃及', '1');
INSERT INTO `t_zone` VALUES ('503', 'EI Salvador', '50', '萨尔瓦多', '1');
INSERT INTO `t_zone` VALUES ('372', 'Estonia', '51', '爱沙尼亚', '1');
INSERT INTO `t_zone` VALUES ('251', 'Ethiopia', '52', '埃塞俄比亚', '1');
INSERT INTO `t_zone` VALUES ('679', 'Fiji', '53', '斐济', '1');
INSERT INTO `t_zone` VALUES ('358', 'Finland', '54', '芬兰', '1');
INSERT INTO `t_zone` VALUES ('33', 'France', '55', '法国', '1');
INSERT INTO `t_zone` VALUES ('594', 'French Guiana', '56', '法属圭亚那', '1');
INSERT INTO `t_zone` VALUES ('689', 'French Polynesia', '136', '法属玻利尼西亚', '1');
INSERT INTO `t_zone` VALUES ('241', 'Gabon', '57', '加蓬', '1');
INSERT INTO `t_zone` VALUES ('220', 'Gambia', '58', '冈比亚', '1');
INSERT INTO `t_zone` VALUES ('995', 'Georgia', '59', '格鲁吉亚', '1');
INSERT INTO `t_zone` VALUES ('49', 'Germany', '60', '德国', '1');
INSERT INTO `t_zone` VALUES ('233', 'Ghana', '61', '加纳', '1');
INSERT INTO `t_zone` VALUES ('350', 'Gibraltar', '62', '直布罗陀', '1');
INSERT INTO `t_zone` VALUES ('30', 'Greece', '63', '希腊', '1');
INSERT INTO `t_zone` VALUES ('1809', 'Grenada', '64', '格林纳达', '1');
INSERT INTO `t_zone` VALUES ('1671', 'Guam', '65', '关岛', '1');
INSERT INTO `t_zone` VALUES ('502', 'Guatemala', '66', '危地马拉', '1');
INSERT INTO `t_zone` VALUES ('224', 'Guinea', '67', '几内亚', '1');
INSERT INTO `t_zone` VALUES ('592', 'Guyana', '68', '圭亚那', '1');
INSERT INTO `t_zone` VALUES ('509', 'Haiti', '69', '海地', '1');
INSERT INTO `t_zone` VALUES ('504', 'Honduras', '70', '洪都拉斯', '1');
INSERT INTO `t_zone` VALUES ('852', 'Hongkong', '71', '香港', '1');
INSERT INTO `t_zone` VALUES ('36', 'Hungary', '72', '匈牙利', '1');
INSERT INTO `t_zone` VALUES ('354', 'Iceland', '73', '冰岛', '1');
INSERT INTO `t_zone` VALUES ('91', 'India', '74', '印度', '1');
INSERT INTO `t_zone` VALUES ('62', 'Indonesia', '75', '印度尼西亚', '1');
INSERT INTO `t_zone` VALUES ('98', 'Iran', '76', '伊朗', '1');
INSERT INTO `t_zone` VALUES ('964', 'Iraq', '77', '伊拉克', '1');
INSERT INTO `t_zone` VALUES ('353', 'Ireland', '78', '爱尔兰', '1');
INSERT INTO `t_zone` VALUES ('972', 'Israel', '79', '以色列', '1');
INSERT INTO `t_zone` VALUES ('39', 'Italy', '80', '意大利', '1');
INSERT INTO `t_zone` VALUES ('225', 'Ivory Coast', '81', '科特迪瓦', '1');
INSERT INTO `t_zone` VALUES ('1876', 'Jamaica', '82', '牙买加', '1');
INSERT INTO `t_zone` VALUES ('81', 'Japan', '83', '日本', '1');
INSERT INTO `t_zone` VALUES ('962', 'Jordan', '84', '约旦', '1');
INSERT INTO `t_zone` VALUES ('855', 'Kampuchea (Cambodia )', '85', '柬埔寨', '1');
INSERT INTO `t_zone` VALUES ('327', 'Kazakstan', '86', '哈萨克斯坦', '1');
INSERT INTO `t_zone` VALUES ('254', 'Kenya', '87', '肯尼亚', '1');
INSERT INTO `t_zone` VALUES ('82', 'Korea', '88', '韩国', '1');
INSERT INTO `t_zone` VALUES ('965', 'Kuwait', '89', '科威特', '1');
INSERT INTO `t_zone` VALUES ('331', 'Kyrgyzstan', '90', '吉尔吉斯坦', '1');
INSERT INTO `t_zone` VALUES ('856', 'Laos', '91', '老挝', '1');
INSERT INTO `t_zone` VALUES ('371', 'Latvia', '92', '拉脱维亚', '1');
INSERT INTO `t_zone` VALUES ('961', 'Lebanon', '93', '黎巴嫩', '1');
INSERT INTO `t_zone` VALUES ('266', 'Lesotho', '94', '莱索托', '1');
INSERT INTO `t_zone` VALUES ('231', 'Liberia', '95', '利比里亚', '1');
INSERT INTO `t_zone` VALUES ('218', 'Libya', '96', '利比亚', '1');
INSERT INTO `t_zone` VALUES ('423', 'Liechtenstein', '97', '列支敦士登', '1');
INSERT INTO `t_zone` VALUES ('370', 'Lithuania', '98', '立陶宛', '1');
INSERT INTO `t_zone` VALUES ('352', 'Luxembourg', '99', '卢森堡', '1');
INSERT INTO `t_zone` VALUES ('853', 'Macao', '100', '澳门', '1');
INSERT INTO `t_zone` VALUES ('261', 'Madagascar', '101', '马达加斯加', '1');
INSERT INTO `t_zone` VALUES ('265', 'Malawi', '102', '马拉维', '1');
INSERT INTO `t_zone` VALUES ('60', 'Malaysia', '103', '马来西亚', '1');
INSERT INTO `t_zone` VALUES ('960', 'Maldives', '104', '马尔代夫', '1');
INSERT INTO `t_zone` VALUES ('223', 'Mali', '105', '马里', '1');
INSERT INTO `t_zone` VALUES ('356', 'Malta', '106', '马耳他', '1');
INSERT INTO `t_zone` VALUES ('1670', 'Mariana Is', '107', '马里亚那群岛', '1');
INSERT INTO `t_zone` VALUES ('596', 'Martinique', '108', '马提尼克', '1');
INSERT INTO `t_zone` VALUES ('230', 'Mauritius', '109', '毛里求斯', '1');
INSERT INTO `t_zone` VALUES ('52', 'Mexico', '110', '墨西哥', '1');
INSERT INTO `t_zone` VALUES ('373', 'Moldova, Republic of', '111', '摩尔多瓦', '1');
INSERT INTO `t_zone` VALUES ('377', 'Monaco', '112', '摩纳哥', '1');
INSERT INTO `t_zone` VALUES ('976', 'Mongolia', '113', '蒙古', '1');
INSERT INTO `t_zone` VALUES ('1664', 'Montserrat Is', '114', '蒙特塞拉特岛', '1');
INSERT INTO `t_zone` VALUES ('212', 'Morocco', '115', '摩洛哥', '1');
INSERT INTO `t_zone` VALUES ('258', 'Mozambique', '116', '莫桑比克', '1');
INSERT INTO `t_zone` VALUES ('264', 'Namibia', '117', '纳米比亚', '1');
INSERT INTO `t_zone` VALUES ('674', 'Nauru', '118', '瑙鲁', '1');
INSERT INTO `t_zone` VALUES ('977', 'Nepal', '119', '尼泊尔', '1');
INSERT INTO `t_zone` VALUES ('599', 'Netheriands Antilles', '120', '荷属安的列斯', '1');
INSERT INTO `t_zone` VALUES ('31', 'Netherlands', '121', '荷兰', '1');
INSERT INTO `t_zone` VALUES ('64', 'New Zealand', '122', '新西兰', '1');
INSERT INTO `t_zone` VALUES ('505', 'Nicaragua', '123', '尼加拉瓜', '1');
INSERT INTO `t_zone` VALUES ('227', 'Niger', '124', '尼日尔', '1');
INSERT INTO `t_zone` VALUES ('234', 'Nigeria', '125', '尼日利亚', '1');
INSERT INTO `t_zone` VALUES ('850', 'North Korea', '126', '朝鲜', '1');
INSERT INTO `t_zone` VALUES ('47', 'Norway', '127', '挪威', '1');
INSERT INTO `t_zone` VALUES ('968', 'Oman', '128', '阿曼', '1');
INSERT INTO `t_zone` VALUES ('92', 'Pakistan', '129', '巴基斯坦', '1');
INSERT INTO `t_zone` VALUES ('507', 'Panama', '130', '巴拿马', '1');
INSERT INTO `t_zone` VALUES ('675', 'Papua New Cuinea', '131', '巴布亚新几内亚', '1');
INSERT INTO `t_zone` VALUES ('595', 'Paraguay', '132', '巴拉圭', '1');
INSERT INTO `t_zone` VALUES ('51', 'Peru', '133', '秘鲁', '1');
INSERT INTO `t_zone` VALUES ('63', 'Philippines', '134', '菲律宾', '1');
INSERT INTO `t_zone` VALUES ('48', 'Poland', '135', '波兰', '1');
INSERT INTO `t_zone` VALUES ('351', 'Portugal', '137', '葡萄牙', '1');
INSERT INTO `t_zone` VALUES ('1787', 'Puerto Rico', '138', '波多黎各', '1');
INSERT INTO `t_zone` VALUES ('974', 'Qatar', '139', '卡塔尔', '1');
INSERT INTO `t_zone` VALUES ('262', 'Reunion', '140', '留尼旺', '1');
INSERT INTO `t_zone` VALUES ('40', 'Romania', '141', '罗马尼亚', '1');
INSERT INTO `t_zone` VALUES ('7', 'Russia', '142', '俄罗斯', '1');
INSERT INTO `t_zone` VALUES ('1758', 'Saint Lueia', '143', '圣卢西亚', '1');
INSERT INTO `t_zone` VALUES ('1784', 'Saint Vincent', '144', '圣文森特岛', '1');
INSERT INTO `t_zone` VALUES ('684', 'Samoa Eastern', '145', '东萨摩亚(美)', '1');
INSERT INTO `t_zone` VALUES ('685', 'Samoa Western', '146', '西萨摩亚', '1');
INSERT INTO `t_zone` VALUES ('378', 'San Marino', '147', '圣马力诺', '1');
INSERT INTO `t_zone` VALUES ('239', 'Sao Tome and Principe', '148', '圣多美和普林西比', '1');
INSERT INTO `t_zone` VALUES ('966', 'Saudi Arabia', '149', '沙特阿拉伯', '1');
INSERT INTO `t_zone` VALUES ('221', 'Senegal', '150', '塞内加尔', '1');
INSERT INTO `t_zone` VALUES ('248', 'Seychelles', '151', '塞舌尔', '1');
INSERT INTO `t_zone` VALUES ('232', 'Sierra Leone', '152', '塞拉利昂', '1');
INSERT INTO `t_zone` VALUES ('65', 'Singapore', '153', '新加坡', '1');
INSERT INTO `t_zone` VALUES ('421', 'Slovakia', '154', '斯洛伐克', '1');
INSERT INTO `t_zone` VALUES ('386', 'Slovenia', '155', '斯洛文尼亚', '1');
INSERT INTO `t_zone` VALUES ('677', 'Solomon Is', '156', '所罗门群岛', '1');
INSERT INTO `t_zone` VALUES ('252', 'Somali', '157', '索马里', '1');
INSERT INTO `t_zone` VALUES ('27', 'South Africa', '158', '南非', '1');
INSERT INTO `t_zone` VALUES ('34', 'Spain', '159', '西班牙', '1');
INSERT INTO `t_zone` VALUES ('94', 'Sri Lanka', '160', '斯里兰卡', '1');
INSERT INTO `t_zone` VALUES ('1758', 'St.Lucia', '161', '圣卢西亚', '1');
INSERT INTO `t_zone` VALUES ('1784', 'St.Vincent', '162', '圣文森特', '1');
INSERT INTO `t_zone` VALUES ('249', 'Sudan', '163', '苏丹', '1');
INSERT INTO `t_zone` VALUES ('597', 'Suriname', '164', '苏里南', '1');
INSERT INTO `t_zone` VALUES ('268', 'Swaziland', '165', '斯威士兰', '1');
INSERT INTO `t_zone` VALUES ('46', 'Sweden', '166', '瑞典', '1');
INSERT INTO `t_zone` VALUES ('41', 'Switzerland', '167', '瑞士', '1');
INSERT INTO `t_zone` VALUES ('963', 'Syria', '168', '叙利亚', '1');
INSERT INTO `t_zone` VALUES ('886', 'Taiwan', '169', '台湾省', '1');
INSERT INTO `t_zone` VALUES ('992', 'Tajikstan', '170', '塔吉克斯坦', '1');
INSERT INTO `t_zone` VALUES ('255', 'Tanzania', '171', '坦桑尼亚', '1');
INSERT INTO `t_zone` VALUES ('66', 'Thailand', '172', '泰国', '1');
INSERT INTO `t_zone` VALUES ('228', 'Togo', '173', '多哥', '1');
INSERT INTO `t_zone` VALUES ('676', 'Tonga', '174', '汤加', '1');
INSERT INTO `t_zone` VALUES ('1809', 'Trinidad and Tobago', '175', '特立尼达和多巴哥', '1');
INSERT INTO `t_zone` VALUES ('216', 'Tunisia', '176', '突尼斯', '1');
INSERT INTO `t_zone` VALUES ('90', 'Turkey', '177', '土耳其', '1');
INSERT INTO `t_zone` VALUES ('993', 'Turkmenistan', '178', '土库曼斯坦', '1');
INSERT INTO `t_zone` VALUES ('256', 'Uganda', '179', '乌干达', '1');
INSERT INTO `t_zone` VALUES ('380', 'Ukraine', '180', '乌克兰', '1');
INSERT INTO `t_zone` VALUES ('971', 'United Arab Emirates', '181', '阿拉伯联合酋长国', '1');
INSERT INTO `t_zone` VALUES ('44', 'United Kiongdom', '182', '英国', '1');
INSERT INTO `t_zone` VALUES ('1', 'United States of America', '183', '美国', '1');
INSERT INTO `t_zone` VALUES ('598', 'Uruguay', '184', '乌拉圭', '1');
INSERT INTO `t_zone` VALUES ('233', 'Uzbekistan', '185', '乌兹别克斯坦', '1');
INSERT INTO `t_zone` VALUES ('58', 'Venezuela', '186', '委内瑞拉', '1');
INSERT INTO `t_zone` VALUES ('84', 'Vietnam', '187', '越南', '1');
INSERT INTO `t_zone` VALUES ('967', 'Yemen', '188', '也门', '1');
INSERT INTO `t_zone` VALUES ('381', 'Yugoslavia', '189', '南斯拉夫', '1');
INSERT INTO `t_zone` VALUES ('243', 'Zaire', '191', '扎伊尔', '1');
INSERT INTO `t_zone` VALUES ('260', 'Zambia', '192', '赞比亚', '1');
INSERT INTO `t_zone` VALUES ('263', 'Zimbabwe', '190', '津巴布韦', '1');
