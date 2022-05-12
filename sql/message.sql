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

 Date: 25/04/2022 13:40:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sender_id` int(11) NULL DEFAULT NULL COMMENT '发送人id',
  `receiver_id` int(11) NULL DEFAULT NULL COMMENT '接收人id',
  `type` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型（点赞；评论；关注）',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '发送内容',
  `status` int(11) NULL DEFAULT NULL COMMENT '0-未读;1-已读;2-删除;',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_from_id`(`sender_id`) USING BTREE,
  INDEX `index_to_id`(`receiver_id`) USING BTREE,
  INDEX `index_conversation_id`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (25, 1, 2, 'comment', '{\"entityType\":1,\"articleId\":1,\"entityId\":1,\"title\":\"水水水水\",\"reply\":\"6666\",\"userId\":3}', 1, '2022-02-19 17:16:24', '2022-02-19 19:52:23');
INSERT INTO `message` VALUES (31, 1, 2, 'like', '{\"entityType\":1,\"articleId\":1,\"entityId\":1,\"title\":\"水水水水\",\"userId\":3}', 1, '2022-02-19 18:31:59', '2022-02-19 19:52:27');
INSERT INTO `message` VALUES (32, 1, 2, 'follow', '{\"entityType\":3,\"entityId\":2,\"userId\":3}', 1, '2022-02-19 18:32:35', '2022-02-19 19:52:09');
INSERT INTO `message` VALUES (33, 1, 2, 'like', '{\"entityType\":1,\"articleId\":1,\"entityId\":1,\"title\":\"水水水水\",\"userId\":4}', 1, '2022-02-19 18:34:08', '2022-02-19 19:52:27');
INSERT INTO `message` VALUES (34, 1, 3, 'like', '{\"entityType\":2,\"articleId\":1,\"entityId\":13,\"comment\":\"6666\",\"title\":\"水水水水\",\"userId\":4}', 1, '2022-02-19 18:34:10', '2022-02-21 17:40:29');
INSERT INTO `message` VALUES (35, 1, 2, 'comment', '{\"entityType\":1,\"articleId\":1,\"entityId\":1,\"title\":\"水水水水\",\"reply\":\"你好\",\"userId\":4}', 1, '2022-02-19 18:34:23', '2022-02-19 19:52:23');
INSERT INTO `message` VALUES (36, 1, 2, 'like', '{\"entityType\":2,\"articleId\":1,\"entityId\":18,\"comment\":\"你好\",\"title\":\"水水水水\",\"userId\":3}', 1, '2022-02-19 18:38:27', '2022-02-19 20:24:53');
INSERT INTO `message` VALUES (37, 1, 2, 'comment', '{\"entityType\":2,\"articleId\":1,\"entityId\":18,\"comment\":\"你好\",\"title\":\"水水水水\",\"reply\":\"你也好\",\"userId\":3}', 1, '2022-02-19 18:38:36', '2022-02-19 19:52:23');
INSERT INTO `message` VALUES (38, 1, 3, 'like', '{\"entityType\":1,\"articleId\":3,\"entityId\":3,\"title\":\"二娃的文章\",\"userId\":2}', 1, '2022-02-25 16:25:14', '2022-02-25 17:53:23');
INSERT INTO `message` VALUES (39, 1, 3, 'like', '{\"entityType\":1,\"articleId\":3,\"entityId\":3,\"title\":\"二娃的文章\",\"userId\":2}', 1, '2022-02-27 16:29:35', '2022-02-27 16:30:05');
INSERT INTO `message` VALUES (40, 1, 3, 'follow', '{\"entityType\":3,\"entityId\":3,\"userId\":2}', 1, '2022-02-28 12:23:14', '2022-04-22 13:17:33');
INSERT INTO `message` VALUES (41, 1, 3, 'follow', '{\"entityType\":3,\"entityId\":3,\"userId\":2}', 1, '2022-02-28 12:27:50', '2022-04-22 13:17:33');
INSERT INTO `message` VALUES (42, 1, 3, 'follow', '{\"entityType\":3,\"entityId\":3,\"userId\":2}', 1, '2022-02-28 12:51:05', '2022-04-22 13:16:40');
INSERT INTO `message` VALUES (43, 1, 3, 'follow', '{\"entityType\":3,\"entityId\":3,\"userId\":2}', 1, '2022-02-28 13:01:16', '2022-04-22 13:16:40');
INSERT INTO `message` VALUES (44, 1, 3, 'like', '{\"entityType\":1,\"articleId\":3,\"entityId\":3,\"title\":\"二娃的文章\",\"userId\":2}', 1, '2022-02-28 13:01:22', '2022-04-22 13:16:48');
INSERT INTO `message` VALUES (45, 1, 3, 'follow', '{\"entityType\":3,\"entityId\":3,\"userId\":2}', 1, '2022-02-28 13:01:52', '2022-04-22 13:16:40');
INSERT INTO `message` VALUES (46, 1, 3, 'like', '{\"entityType\":1,\"articleId\":3,\"entityId\":3,\"title\":\"二娃的文章\",\"userId\":2}', 1, '2022-02-28 13:02:02', '2022-04-22 13:16:48');
INSERT INTO `message` VALUES (47, 1, 3, 'follow', '{\"entityType\":3,\"entityId\":3,\"userId\":2}', 1, '2022-02-28 13:02:14', '2022-04-22 13:16:40');
INSERT INTO `message` VALUES (48, 1, 3, 'like', '{\"entityType\":1,\"articleId\":3,\"entityId\":3,\"title\":\"二娃的文章\",\"userId\":2}', 1, '2022-02-28 13:04:36', '2022-04-22 13:16:48');
INSERT INTO `message` VALUES (49, 1, 3, 'follow', '{\"entityType\":3,\"entityId\":3,\"userId\":2}', 1, '2022-02-28 13:04:40', '2022-04-22 13:16:40');
INSERT INTO `message` VALUES (50, 1, 3, 'comment', '{\"entityType\":1,\"articleId\":13,\"entityId\":13,\"title\":\"CCC\",\"reply\":\"你好\",\"userId\":2}', 1, '2022-03-04 17:08:49', '2022-04-22 13:16:45');
INSERT INTO `message` VALUES (51, 1, 4, 'like', '{\"entityType\":2,\"articleId\":1,\"entityId\":16,\"comment\":\"你好\",\"title\":\"水水水水\",\"userId\":2}', 0, '2022-04-22 13:16:03', NULL);
INSERT INTO `message` VALUES (52, 1, 3, 'like', '{\"entityType\":2,\"articleId\":1,\"entityId\":13,\"comment\":\"6666\",\"title\":\"水水水水\",\"userId\":2}', 1, '2022-04-22 13:16:12', '2022-04-22 13:16:48');
INSERT INTO `message` VALUES (53, 1, 2, 'like', '{\"entityType\":1,\"articleId\":26,\"entityId\":26,\"title\":\"java***\",\"userId\":3}', 1, '2022-04-22 13:18:11', '2022-04-22 13:18:18');
INSERT INTO `message` VALUES (54, 1, 2, 'like', '{\"entityType\":2,\"articleId\":26,\"entityId\":27,\"comment\":\"哈哈哈***\",\"title\":\"java***\",\"userId\":3}', 1, '2022-04-22 13:18:41', '2022-04-22 13:18:43');
INSERT INTO `message` VALUES (55, 1, 2, 'like', '{\"entityType\":2,\"articleId\":26,\"entityId\":27,\"comment\":\"哈哈哈***\",\"title\":\"java***\",\"userId\":3}', 1, '2022-04-22 13:18:50', '2022-04-22 13:18:55');
INSERT INTO `message` VALUES (56, 1, 3, 'comment', '{\"entityType\":1,\"articleId\":13,\"entityId\":13,\"title\":\"CCC\",\"reply\":\"CCC\",\"userId\":2}', 1, '2022-04-22 13:37:05', '2022-04-22 13:37:23');
INSERT INTO `message` VALUES (57, 1, 2, 'like', '{\"entityType\":2,\"articleId\":13,\"entityId\":28,\"comment\":\"CCC\",\"title\":\"CCC\",\"userId\":3}', 1, '2022-04-22 13:37:29', '2022-04-22 13:37:37');
INSERT INTO `message` VALUES (58, 1, 3, 'follow', '{\"entityType\":3,\"entityId\":3,\"userId\":2}', 0, '2022-04-22 17:34:22', NULL);

SET FOREIGN_KEY_CHECKS = 1;
