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

 Date: 28/11/2025 17:55:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pos_sale
-- ----------------------------
DROP TABLE IF EXISTS `pos_sale`;
CREATE TABLE `pos_sale`  (
  `sale_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '销售ID',
  `sale_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '销售单号',
  `total` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总金额',
  `sale_time` datetime(0) NULL DEFAULT NULL COMMENT '销售时间',
  `payment_id` bigint(0) NULL DEFAULT NULL COMMENT '支付id',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单状态',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`sale_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pos_sale
-- ----------------------------
INSERT INTO `pos_sale` VALUES (1, 'so-1542881137129459712', 135.00, '2022-06-25 14:42:06', NULL, 'paid', '0', NULL, '2022-06-25 14:42:28', NULL, '2022-06-26 14:42:28');
INSERT INTO `pos_sale` VALUES (2, 'so-1542881495386001408', 270.00, '2022-06-30 17:55:25', NULL, 'paid', '0', NULL, '2022-06-25 18:20:50', NULL, '2022-06-26 18:20:50');
INSERT INTO `pos_sale` VALUES (3, 'so-1542881571277635584', 110.00, '2022-06-30 18:18:29', NULL, 'reserved', '0', NULL, '2022-06-25 18:20:56', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
