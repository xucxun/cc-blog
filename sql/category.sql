/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 25/04/2022 13:40:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `display` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '是否前台显示',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '0-正常，1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_category_name`(`name`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文章类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '学习方法1', '学习笔记类别1', 1, '2022-03-03 12:44:28', 0);
INSERT INTO `category` VALUES (2, '项目总结', '项目总结类别', 0, '2022-03-03 14:44:29', 0);
INSERT INTO `category` VALUES (3, '工具技巧', '工具技巧类别', 0, '2022-03-03 15:45:06', 0);
INSERT INTO `category` VALUES (4, '他人优秀笔记', '他人优秀笔记类别', 0, '2022-03-03 16:45:36', 0);
INSERT INTO `category` VALUES (5, '测试', '测试类别', 0, '2022-03-03 12:46:10', 0);
INSERT INTO `category` VALUES (6, '测试23', '测试2类别', 0, '2022-03-03 18:11:50', 0);

SET FOREIGN_KEY_CHECKS = 1;
