/*
 Navicat Premium Data Transfer

 Source Server         : 10.2.16.78-31227dev
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 10.2.16.78:31227
 Source Schema         : chxq

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 22/11/2022 10:03:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `bir` timestamp(0) NULL DEFAULT NULL COMMENT '出生日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, '张三', 12, '2022-11-18 10:54:50');
INSERT INTO `employee` VALUES (2, '李四', 22, '2022-11-24 10:55:32');
INSERT INTO `employee` VALUES (3, '王征', 22, NULL);

SET FOREIGN_KEY_CHECKS = 1;
