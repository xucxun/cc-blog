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

 Date: 25/04/2022 13:40:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `entity_type` int(11) NULL DEFAULT NULL COMMENT '评论对象：1-文章，2-评论',
  `entity_id` int(11) NULL DEFAULT NULL COMMENT '被评论对象id',
  `target_id` int(11) NULL DEFAULT NULL COMMENT '评论的评论id；二次被评论id',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '评论内容',
  `status` int(11) NULL DEFAULT NULL COMMENT '0-未删除、1-已删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_user_id`(`user_id`) USING BTREE,
  INDEX `index_entity_id`(`entity_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (11, 5, 2, 7, 0, '你也哈皮', 0, '2022-02-19 16:59:35', NULL);
INSERT INTO `comment` VALUES (13, 3, 1, 1, 0, '6666', 0, '2022-02-19 17:16:24', NULL);
INSERT INTO `comment` VALUES (16, 4, 1, 1, 0, '你好', 0, '2022-02-19 18:34:23', NULL);
INSERT INTO `comment` VALUES (17, 4, 2, 16, 0, '你好了', 0, '2022-02-19 18:34:47', NULL);
INSERT INTO `comment` VALUES (18, 2, 1, 1, 0, '你好', 0, '2022-02-19 18:37:46', NULL);
INSERT INTO `comment` VALUES (19, 3, 2, 18, 0, '你也好', 0, '2022-02-19 18:38:36', NULL);
INSERT INTO `comment` VALUES (20, 3, 1, 3, 0, '你好', 0, '2022-02-21 18:02:41', NULL);
INSERT INTO `comment` VALUES (24, 3, 1, 3, 0, '你好啦', 0, '2022-02-21 18:39:49', NULL);
INSERT INTO `comment` VALUES (26, 2, 1, 13, 0, '你好', 0, '2022-03-04 17:08:49', NULL);
INSERT INTO `comment` VALUES (27, 2, 1, 26, 0, '哈哈哈***', 0, '2022-03-08 15:07:45', NULL);
INSERT INTO `comment` VALUES (28, 2, 1, 13, 0, 'CCC', 0, '2022-04-22 13:37:05', NULL);
INSERT INTO `comment` VALUES (29, 2, 2, 28, 0, 'hhh', 0, '2022-04-22 17:34:07', NULL);
INSERT INTO `comment` VALUES (30, 2, 2, 28, 2, 'hhh', 0, '2022-04-22 17:34:14', NULL);

SET FOREIGN_KEY_CHECKS = 1;
