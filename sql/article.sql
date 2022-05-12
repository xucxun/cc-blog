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

 Date: 25/04/2022 13:39:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `status` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '0-正常; 1-删除;',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `comment_count` int(11) NULL DEFAULT NULL COMMENT '评论数',
  `score` double NULL DEFAULT NULL COMMENT '博客分数',
  `top` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '是否置顶',
  `marrow` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '是否精华',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '类别ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 2, '水水水水', '水水水水水大大大式风格时代顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶看过看过客人和韩庚韩庚个人个人割让给如果人热高热高热水水水水水大大大式风格时代顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶看过看过客人和韩庚韩庚个人个人割让给如果人热高热高热水水水水水大大大式风格时代顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶看过看过客人和韩庚韩庚个人个人割让给如果人热高热高热水水水水水大大大式风格时代顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶看过看过客人和韩庚韩庚个人个人割让给如果人热高热高热', 0, '2022-02-04 16:33:09', '2022-03-01 12:50:46', 3, 0, 1, 1, 5);
INSERT INTO `article` VALUES (2, 4, '水军三号的文章', '水军三号', 0, '2022-02-15 17:51:18', '2022-02-23 20:59:18', 1, 0, 0, 0, 5);
INSERT INTO `article` VALUES (3, 3, '二娃的文章', '二娃有千里眼', 0, '2022-02-21 17:41:17', '2022-04-22 13:15:42', 2, 33.99563519459755, 0, 1, 5);
INSERT INTO `article` VALUES (5, 3, '二娃的文章2', '二娃有千里眼', 0, '2022-02-24 16:20:08', NULL, 0, 0, 0, 0, 5);
INSERT INTO `article` VALUES (6, 4, '三娃的文章', '三娃铜头铁臂', 0, '2022-02-24 17:46:13', NULL, 0, 0, 0, 0, 5);
INSERT INTO `article` VALUES (9, 4, '三娃的文章2', '三娃金刚不坏', 0, '2022-02-24 17:56:25', '2022-02-25 15:42:09', 0, 0, 0, 1, 5);
INSERT INTO `article` VALUES (10, 4, '三娃的文章3', '三娃666', 1, '2022-02-24 17:58:48', '2022-02-24 18:03:24', 0, 0, 0, 0, 5);
INSERT INTO `article` VALUES (11, 2, 'AAA', 'AAA', 0, '2022-02-27 15:14:25', '2022-03-03 21:16:57', 0, 38.8750612633917, 0, 1, 5);
INSERT INTO `article` VALUES (12, 2, 'BBB', 'BBBB', 0, '2022-02-27 15:14:52', '2022-03-04 14:15:16', 0, 38.93951925261862, 0, 1, 5);
INSERT INTO `article` VALUES (13, 3, 'CCC', 'CCC', 0, '2022-02-27 16:42:46', '2022-04-22 17:34:27', 2, 38.97772360528885, 1, 1, 5);
INSERT INTO `article` VALUES (14, 2, 'DDD', 'DDD', 1, '2022-03-04 19:58:30', '2022-04-22 13:35:54', 0, 43.30102999566398, 0, 0, 6);
INSERT INTO `article` VALUES (15, 2, 'EEE', 'EEE', 0, '2022-03-05 14:25:22', '2022-03-05 17:19:11', 0, 43, 0, 0, 1);
INSERT INTO `article` VALUES (16, 2, 'FFF', 'FFF', 1, '2022-03-05 15:56:49', '2022-04-22 13:35:38', 0, 43, 0, 0, 3);
INSERT INTO `article` VALUES (17, 2, 'Java基础', '# Java基础\r\n### 一、面向对象\r\n\r\n#### **1.Java中JDK、JRE、JVM三者之间的关系**\r\n\r\n- JDK是（Java Development Kit）的缩写，它是功能齐全的 Java SDK。它拥有 JRE 所拥有的一切，还有编译器（javac）和工具（如 javadoc 和 jdb）。它能够创建和编译程序。\r\n\r\n- JRE是Java Runtime Environment缩写，它是运行已编译 Java 程序所需的所有内容的集合，包括 \r\n- Java 虚拟机（JVM），Java 类库，java 命令和其他的一些基础构件。但是，它不能用于创建新程序。\r\n- JDK包含JRE，JRE包含JVM。\r\n', 0, '2022-03-06 16:19:59', NULL, 0, 44, 0, 0, 1);
INSERT INTO `article` VALUES (20, 2, 'java', '#### **2.方法重载（Overload）和 重写（Override）**\n\n##### 重载\n\n-  在同一个类当中。\n-  方法名相同。\n-  参数列表不同：个数不同算不同，顺序不同算不同，类型不同也算不同。\n- ⽅法返回值和访问修饰符可以不同\n\n##### 重写\n\n- 重写发生在子类与父类之间, \n- 重写方法返回值和形参都不能改变，与方法返回值和访问修饰符无关，即重载的方法不能根据返回类型进行区分。\n- 即外壳不变，核心重写！\n- 私有方法不能被重写(父类私有成员子类是不能继承的)\n- 子类私有访问权限不能更低（public > 默认 > 私有）', 0, '2022-03-06 19:48:27', NULL, 0, 45, 0, 0, 2);
INSERT INTO `article` VALUES (25, 2, 'java', '#### **2.方法重载（Overload）和 重写（Override）**\n\n##### 重载\n\n-  在同一个类当中。\n-  方法名相同。\n-  参数列表不同：个数不同算不同，顺序不同算不同，类型不同也算不同。\n- ⽅法返回值和访问修饰符可以不同\n\n##### 重写\n\n- 重写发生在子类与父类之间, \n- 重写方法返回值和形参都不能改变，与方法返回值和访问修饰符无关，即重载的方法不能根据返回类型进行区分。\n- 即外壳不变，核心重写！\n- 私有方法不能被重写(父类私有成员子类是不能继承的)\n- 子类私有访问权限不能更低（public > 默认 > 私有）\n![](/upload/9614a27012cc4ab29a8a15302f942d08.jpg)', 0, '2022-03-06 21:50:04', '2022-04-22 13:38:12', 0, 45.30102999566398, 0, 0, 2);
INSERT INTO `article` VALUES (26, 2, 'java***', '#### **2.方法重载（Overload）和 重写（Override）**\n\n##### 重载\n\n-  在同一个类当中。\n-  方法名相同。\n-  参数列表不同：个数不同算不同，顺序不同算不同，类型不同也算不同。\n- ⽅法返回值和访问修饰符可以不同\n\n##### 重写\n\n- 重写发生在子类与父类之间, \n- 重写方法返回值和形参都不能改变，与方法返回值和访问修饰符无关，即重载的方法不能根据返回类型进行区分。\n- 即外壳不变，核心重写！\n- 私有方法不能被重写(父类私有成员子类是不能继承的)\n- 子类私有访问权限不能更低（public > 默认 > 私有）\n![](/upload/9614a27012cc4ab29a8a15302f942d08.jpg)', 0, '2022-03-08 15:07:18', '2022-04-22 13:36:08', 1, 47.93951925261862, 1, 1, 2);
INSERT INTO `article` VALUES (27, 2, 'Java异常', '异常是指程序出现了不正常的情况\n![](/upload/10e258777a124365b4bc2fa1fc18ac1c.jpg)\n- Exception 和  Error ⼆者都是 Java 异常处理的重要子类，各⾃都包含大量⼦类。\n\n- Exception：程序本身可以处理的异常，可以通过  catch 来进⾏捕获。 Exception 又可以分为 `受检查异常`(必须处理) 和 `不受检查异常`(可以不处理)。\n\n- Error ： Error 属于程序无法处理的错误 ，我们没办法通过  catch 来进行捕获 。例如，Java 虚拟机运行错误（ Virtual MachineError ）、虚拟机内存不够错误( OutOfMemoryError )、类定义错误（ NoClassDefFoundError ）等 。这些异常发⽣时，Java虚拟机（JVM）⼀般会选择线程终止。\n\n- **受检查异常**（**编译时异常**）\n\n  必须显示处理，否则程序发生错误，无法通过编译。\n\n  除了 RuntimeException 及其子类以外，其他的 Exception 类及其子类都属于`受检查异常` 。常见的受检查异常有： IO 相关的异常、 ClassNotFoundException 、 SQLException ...。\n\n- **非受检查异常**（**运行时异常**）\n  Java 代码在编译过程中 ，我们即使不处理不受检查异常也可以正常通过编译。\n\n  RuntimeException 及其子类都统称为`非受检查异常`，例如： NullPointExecrption 、NumberFormatException （字符串转换为数字）、 ArrayIndexOutOfBoundsException （数组越界）、 ClassCastException （类型转换错误）、 ArithmeticException （算术错误）等。', 0, '2022-04-22 13:42:50', '2022-04-22 13:43:11', 0, 92.88649072517248, 1, 1, 4);

SET FOREIGN_KEY_CHECKS = 1;
