/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : isdp-db

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 08/11/2025 01:12:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pos_category
-- ----------------------------
DROP TABLE IF EXISTS `pos_category`;
CREATE TABLE `pos_category`  (
  `category_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '类别id',
  `parent_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '父级id',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类别名称',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pos_category
-- ----------------------------
INSERT INTO `pos_category` VALUES (1, 0, '文具', 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_category` VALUES (2, 0, '日用品', 'admin', '2025-10-01 23:58:53', NULL, NULL);
INSERT INTO `pos_category` VALUES (3, 0, '手机', 'admin', '2025-10-01 23:59:06', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
