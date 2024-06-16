/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.108
 Source Server Type    : MySQL
 Source Server Version : 80200 (8.2.0)
 Source Host           : 192.168.1.108:3306
 Source Schema         : py_manage

 Target Server Type    : MySQL
 Target Server Version : 80200 (8.2.0)
 File Encoding         : 65001

 Date: 15/06/2024 18:41:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for client_info
-- ----------------------------
DROP TABLE IF EXISTS `client_info`;
CREATE TABLE `client_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接id',
  `client_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接名称',
  `client_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端描述',
  `client_icon` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '客户端图片',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态',
  `client_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接ip',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户端表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of client_info
-- ----------------------------

-- ----------------------------
-- Table structure for menu_setting
-- ----------------------------
DROP TABLE IF EXISTS `menu_setting`;
CREATE TABLE `menu_setting`
(
    `id`            int NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent`        int NULL DEFAULT NULL COMMENT '上级',
    `path`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
    `name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
    `redirect`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重定向',
    `component`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件',
    `tittle`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
    `is_link`       varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否是链接',
    `is_hide`       int NOT NULL DEFAULT 0 COMMENT '是否隐藏',
    `is_keep_alive` int NOT NULL DEFAULT 1 COMMENT '是否缓存组件状态',
    `is_affix`      int NOT NULL DEFAULT 1 COMMENT '是否固定在 tagsView 栏上',
    `is_iframe`     int NOT NULL DEFAULT 0 COMMENT '是否内嵌窗口',
    `roles`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
    `icon`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu_setting
-- ----------------------------
INSERT INTO `menu_setting` VALUES (1, 0, '/shellManage', 'shellManage', 'shell/config/index', NULL, '脚本管理', '', 0, 0, 0, 0, 'admin,common', 'ele-SetUp');
INSERT INTO `menu_setting` VALUES (2, 1, '/shell/config', 'config', '', 'shell/config/index', '脚本配置', '', 0, 0, 0, 0, 'admin,common', 'ele-Setting');
INSERT INTO `menu_setting` VALUES (3, 1, '/shell/python', 'python', NULL, 'shell/python/index', '脚本编辑', '', 0, 0, 0, 0, 'admin,common', 'ele-Tickets');
INSERT INTO `menu_setting` VALUES (5, 0, '/pay', 'pay', 'pay/payClient/index', '', '支付管理', NULL, 0, 0, 0, 0, 'admin,common', 'ele-Money');
INSERT INTO `menu_setting` VALUES (6, 5, '/pay/payClient', 'pay', NULL, 'pay/payClient/index', '客户端管理', NULL, 0, 0, 0, 0, 'admin,common', 'ele-Iphone');
INSERT INTO `menu_setting` VALUES (7, 5, '/pay/task', 'task', NULL, 'pay/task/index', '任务管理', NULL, 0, 0, 0, 0, 'admin,common', 'ele-Tickets');
INSERT INTO `menu_setting` VALUES (8, 5, '/pay/qr', 'qr', NULL, 'pay/qr/index', '二维码管理', NULL, 0, 0, 0, 0, 'admin,common', 'ele-FullScreen');
INSERT INTO `menu_setting` VALUES (10, 5, '/pay/payConfig', 'payConfig', NULL, 'pay/payConfig/index', '支付方式管理', NULL, 0, 0, 0, 0, 'admin,common', 'ele-Check');
INSERT INTO `menu_setting` VALUES (11, 5, '/pay/order', 'payOrder', NULL, 'pay/order/index', '订单管理', NULL, 0, 0, 0, 0, 'admin,common', 'ele-Goods');

-- ----------------------------
-- Table structure for pay_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_config`;
CREATE TABLE `pay_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端id',
  `pay_topic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付通道',
  `regex` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '匹配数据正则',
  `pay_type` int NOT NULL DEFAULT 1 COMMENT '支付方式',
  `pay_time_out` int NULL DEFAULT NULL COMMENT '支付超时剩余时间，秒',
  `task_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付任务',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_config
-- ----------------------------
INSERT INTO `pay_config` VALUES (17, '', '', '收款到账通知\\|(.*?)\\|收款金额\\|￥\\|(.*?)\\|收款方备注\\|(.*?)\\|', 1, 60, '', 1);

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付流水号',
  `pay_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付姓名',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `pay_type` int NOT NULL DEFAULT 1 COMMENT '支付方式',
  `pay_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付描述',
  `pay_status` int NOT NULL DEFAULT 0 COMMENT '支付状态',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order
-- ----------------------------

-- ----------------------------
-- Table structure for pay_qr_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_qr_config`;
CREATE TABLE `pay_qr_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `qr_base64` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '二维码图片',
  `qr_old_base64` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '原图',
  `qr_mark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码备注号',
  `qr_logo_type` int NULL DEFAULT NULL COMMENT '美化方式',
  `qr_type` int NOT NULL DEFAULT 0 COMMENT '支付方式 1 微信',
  `desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `qr_mark_index`(`qr_mark` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付二维码' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for py_config
-- ----------------------------
DROP TABLE IF EXISTS `py_config`;
CREATE TABLE `py_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `python_bin` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'python安装目录',
  `python_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'python脚本路径',
  `python_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脚本名称',
  `gitee_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gitee账号',
  `gitee_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gitee密码',
  `gitee_address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gitee仓库地址',
  `gitee_branch` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gitee分支',
  `chromium_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器地址',
  `wx_app_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信开发者id',
  `wx_app_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信appSecret',
  `wx_token` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信回调token',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of py_config
-- ----------------------------
INSERT INTO `py_config` VALUES (1, '/usr/bin/python3', '/soft/wx/python', '/chatgpt.py', '', '', '', '', '/snap/bin/chromium', '', '', '', 1);

-- ----------------------------
-- Table structure for py_python_file
-- ----------------------------
DROP TABLE IF EXISTS `py_python_file`;
CREATE TABLE `py_python_file`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `level` int NULL DEFAULT NULL COMMENT '层级',
  `parent` int NULL DEFAULT NULL COMMENT '父级id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中午名称',
  `file_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `python_code` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '脚本代码',
  `status` int NOT NULL DEFAULT 1,
  `type` int NOT NULL DEFAULT 1 COMMENT '1文件 2文件夹',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '脚本文件' ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for py_user
-- ----------------------------
DROP TABLE IF EXISTS `py_user`;
CREATE TABLE `py_user`  (
  `id` int NOT NULL COMMENT 'id',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `bd_orc_client_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '百度ocrId',
  `bd_ocr_client_secret` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '百度ocr secret',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of py_user
-- ----------------------------
INSERT INTO `py_user` VALUES (1, 'admin', 'admin', 'dc483e80a7a0bd9ef71d8cf973673924', '', '', '2024-05-20 15:55:26', '2024-05-20 15:55:29', 1);

-- ----------------------------
-- Table structure for task_execute_log
-- ----------------------------
DROP TABLE IF EXISTS `task_execute_log`;
CREATE TABLE `task_execute_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `topic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行通道',
  `do_params` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下发参数',
  `do_status` int NULL DEFAULT NULL COMMENT '执行状态',
  `do_result` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务执行记录' ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for task_mine_list
-- ----------------------------
DROP TABLE IF EXISTS `task_mine_list`;
CREATE TABLE `task_mine_list`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务id',
  `task_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `task_hex` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '任务文件',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `task_id_index`(`task_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '我的任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_mine_list
-- ----------------------------
INSERT INTO `task_mine_list` VALUES (1, 'c8177664cd49465cb95dca1d85cb6dde', '微信获取订单', '获取助手里面的备注', '7b2266223a7b226964223a34363338342c2272223a322c2265223a5b7b226964223a34363830372c2272223a322c22726674223a7b2230223a22e5bd93e5898de68980e5a484e5ba94e794a8222c2233223a22e5bd93e5898de7aa97e58fa3227d7d2c7b226964223a353235343331357d2c7b226964223a353337383031332c227673223a7b2230223a22313030227d7d2c7b226964223a353436333430362c227673223a7b2230223a22636f6d2e74656e63656e742e6d6d227d7d2c7b226964223a353430343238372c2272223a322c227673223a7b2230223a2232303030227d2c2265223a5b7b226964223a313036323636392c227673223a7b2231223a225b5c22636f6d2e74656e63656e742e6d6d2f2e75692e4c61756e6368657255495c225d227d2c22726663223a7b2230223a22e5bd93e5898de68980e5a484e5ba94e794a8227d7d5d7d2c7b226964223a31373439372c2265223a5b7b226964223a353532333033392c227673223a7b2230223a22e8bf9be585a5e5beaee4bfa1227d7d5d7d2c7b226964223a353430323033352c2272223a322c2265223a5b7b226964223a363333353032362c22726663223a7b2230223a22e5bd93e5898de7aa97e58fa3227d2c2265223a5b7b226964223a313436393331362c227673223a7b2230223a22616e64726f69642e7769646765742e5465787456696577227d7d2c7b226964223a313438323838312c227673223a7b2230223a22636f6d2e74656e63656e742e6d6d3a69642f6f626e227d7d2c7b226964223a313436353136342c227673223a7b2230223a22e5beaee4bfa1e694b6e6acbee58aa9e6898b227d7d5d7d5d7d2c7b226964223a31373439372c2265223a5b7b226964223a353332393835382c22726674223a7b2230223a22e6bba1e8b6b3e69da1e4bbb6e79a84e68ea7e4bbb6227d2c2265223a5b7b226964223a313436393331362c227673223a7b2230223a22616e64726f69642e7769646765742e52656c61746976654c61796f7574227d7d5d7d2c7b226964223a353334373635342c22726674223a7b2230223a22e68ea7e4bbb6e79a84e69687e5ad97227d2c22726663223a7b2230223a22e6bba1e8b6b3e69da1e4bbb6e79a84e68ea7e4bbb6227d7d2c7b226964223a353531373133382c227673223a7b2230223a222573227d2c22726663223a7b2231223a22e68ea7e4bbb6e79a84e69687e5ad97227d7d5d7d2c7b226964223a34383634372c2272223a312c2265223a5b7b226964223a353430323033352c2272223a322c2265223a5b7b226964223a363333353032362c22726663223a7b2230223a22e5bd93e5898de7aa97e58fa3227d2c2265223a5b7b226964223a313436393331362c227673223a7b2230223a22616e64726f69642e766965772e56696577227d7d2c7b226964223a313438323838312c227673223a7b2230223a22636f6d2e74656e63656e742e6d6d3a69642f6b6271227d7d2c7b226964223a313436353136342c227673223a7b2230223a22e5beaee4bfa1e694b6e6acbee58aa9e6898b227d7d5d7d5d7d2c7b226964223a31373439372c2265223a5b7b226964223a353331363934342c22726674223a7b2230223a22e8aea2e58d95222c2231223a22e8aea2e58d95e69687e5ad97227d2c2265223a5b7b226964223a313436393331362c227673223a7b2230223a22616e64726f69642e766965772e56696577227d7d2c7b226964223a313438323838312c227673223a7b2230223a22636f6d2e74656e63656e742e6d6d3a69642f6b6271227d7d2c7b226964223a313436353136342c227673223a7b2230223a22e5beaee4bfa1e694b6e6acbee58aa9e6898b227d7d5d7d2c7b226964223a353430343238372c2272223a322c227673223a7b2230223a2232303030227d2c2265223a5b7b226964223a363333353032362c22726663223a7b2230223a22e5bd93e5898de7aa97e58fa3227d2c2265223a5b7b226964223a313436393331362c227673223a7b2230223a22616e64726f69642e7769646765742e5465787456696577227d7d2c7b226964223a313438323838312c227673223a7b2230223a22636f6d2e74656e63656e742e6d6d3a69642f6f626e227d7d2c7b226964223a313436353136342c227673223a7b2230223a22e5beaee4bfa1e694b6e6acbee58aa9e6898b227d7d5d7d5d7d2c7b226964223a31373439372c2265223a5b7b226964223a353332393835382c22726674223a7b2230223a22e8aea2e58d95e69687e5ad9731227d2c2265223a5b7b226964223a313436393331362c227673223a7b2230223a22616e64726f69642e7769646765742e4c696e6561724c61796f7574227d7d5d7d2c7b226964223a353334373635342c22726674223a7b2230223a22e8aea2e58d95e69687e5ad97227d2c22726663223a7b2230223a22e8aea2e58d95e69687e5ad9731227d7d2c7b226964223a353531373133382c227673223a7b2230223a222573227d2c22726663223a7b2231223a22e8aea2e58d95e69687e5ad97227d7d5d7d5d7d5d7d5d7d2c226d223a7b227469223a22e5beaee4bfa1e88eb7e58f96e8aea2e58d95222c227479223a312c2264223a22e88eb7e58f96e58aa9e6898be9878ce99da2e79a84e5a487e6b3a8222c2263223a313731383239353030313439302c226d223a313731383337343737393735372c2273223a313931343535343434322c22746964223a226338313737363634636434393436356362393564636131643835636236646465222c22746972223a2231383031363230343531313639343233333630222c2276223a36377d7d', '2024-06-14 14:19:41', '2024-06-14 14:19:47', 1);

-- ----------------------------
-- Table structure for task_upload_record
-- ----------------------------
DROP TABLE IF EXISTS `task_upload_record`;
CREATE TABLE `task_upload_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `task_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务id',
  `task_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `task_hex` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '任务文件',
  `upload_client` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上报客户端',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上报时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务上报记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_upload_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
