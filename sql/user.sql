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

 Date: 25/04/2022 13:40:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密盐值',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `role` int(11) NULL DEFAULT NULL COMMENT '0-普通用户; 1-管理员; 2-超级管理员;',
  `status` int(11) NULL DEFAULT NULL COMMENT '0-未激活; 1-已激活；2-禁用',
  `activation_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱激活码',
  `avatar` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_username`(`account`(20)) USING BTREE,
  INDEX `index_email`(`email`(20)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'admin', '超级管理员', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'admin@qq.com', 2, 1, '51ed6bed95174db3bb84f2ab420c0cdb', 'http://localhost:8080/user/avatar/c7a35e17734e42428c2ae5174a4a93a3.jpg', '2022-01-23 17:16:08', '2022-02-07 18:04:53');
INSERT INTO `user` VALUES (3, 'test02', '二娃(管理员）', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test02@qq.com', 1, 1, 'd7a5714a145b4461b5a4199cc5a0014f', 'http://images.nowcoder.com/head/149t.png', '2022-02-07 18:04:50', '2022-03-02 14:56:50');
INSERT INTO `user` VALUES (4, 'test03', '三娃', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test03@qq.com', 0, 0, '5a61faee7af94e89ba7237b1277c9fed', 'http://images.nowcoder.com/head/150t.png', '2022-02-07 18:04:50', '2022-02-07 18:04:56');
INSERT INTO `user` VALUES (5, 'test04', '火娃', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test04@qq.com', 0, 1, 'f217b637e9544e2a9b4a88f78c583d03', 'http://images.nowcoder.com/head/152t.png', '2022-02-07 18:04:50', '2022-02-07 18:04:56');
INSERT INTO `user` VALUES (6, 'test05', '水娃', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test05@qq.com', 0, 1, '107eb2cbb8454fbe96848790e6a730b1', 'http://images.nowcoder.com/head/153t.png', '2022-02-07 18:04:50', '2022-02-07 18:04:56');
INSERT INTO `user` VALUES (7, 'test06', '隐身娃', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test06@qq.com', 0, 1, '69dcd69f4c0145058df820e90820ba1e', 'http://images.nowcoder.com/head/154t.png', '2022-02-07 18:04:50', '2022-02-07 18:04:56');
INSERT INTO `user` VALUES (8, 'test07', '七娃', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test07@qq.com', 0, 1, '69dcd69f4c0145058df820e90820ba1e', 'http://images.nowcoder.com/head/154t.png', '2022-02-07 18:04:50', '2022-02-07 18:04:56');
INSERT INTO `user` VALUES (9, 'test08', '大娃', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test01@qq.com', 0, 1, '69dcd69f4c0145058df820e90820ba1e', 'http://images.nowcoder.com/head/154t.png', '2022-02-07 18:04:50', '2022-02-07 18:04:56');
INSERT INTO `user` VALUES (10, 'test09', '小金刚(未激活）', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test@qq.com', 0, 0, '69dcd69f4c0145058df820e90820ba1e', 'http://images.nowcoder.com/head/154t.png', '2022-02-07 18:04:50', '2022-02-07 18:04:56');
INSERT INTO `user` VALUES (11, 'test10', '大金刚(被禁用）', 'f8fa6555548d2a85b266b869b26b433f', '64866', 'test10@qq.com', 0, 2, '69dcd69f4c0145058df820e90820ba1e', 'http://images.nowcoder.com/head/154t.png', '2022-02-07 18:04:50', '2022-02-07 18:04:56');
INSERT INTO `user` VALUES (15, 'cc', 'cc', '1d27df1d2262f44d2c535449555ae468', '338b7', 'cc@qq.com', 0, 1, '9a66de7cad32400b9e04e9b16904d2f7', 'http://images.nowcoder.com/head/280t.png', '2022-03-05 20:38:06', '2022-03-05 20:38:23');

SET FOREIGN_KEY_CHECKS = 1;
