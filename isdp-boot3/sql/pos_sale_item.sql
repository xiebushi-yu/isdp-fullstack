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

 Date: 28/11/2025 17:55:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pos_sale_item
-- ----------------------------
DROP TABLE IF EXISTS `pos_sale_item`;
CREATE TABLE `pos_sale_item`  (
  `sale_item_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '订单明细id',
  `sale_id` bigint(0) NULL DEFAULT NULL COMMENT '订单id',
  `product_id` bigint(0) NULL DEFAULT NULL COMMENT '产品id',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '销售价格',
  `quantity` int(0) NULL DEFAULT NULL COMMENT '购买数量',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '明细状态',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标识',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`sale_item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pos_sale_item
-- ----------------------------
INSERT INTO `pos_sale_item` VALUES (1, 1, 1, 65.00, 1, 'paid', '0', NULL, '2022-06-25 14:43:26', NULL, '2022-06-26 14:43:26');
INSERT INTO `pos_sale_item` VALUES (2, 1, 2, 20.00, 2, 'paid', '0', NULL, '2022-06-25 14:44:34', NULL, '2022-06-26 14:44:34');
INSERT INTO `pos_sale_item` VALUES (3, 1, 4, 10.00, 3, 'paid', '0', NULL, '2022-06-25 14:45:22', NULL, '2022-06-26 14:45:22');
INSERT INTO `pos_sale_item` VALUES (4, 2, 2, 20.00, 1, 'paid', '0', NULL, '2022-06-25 18:21:06', NULL, '2022-06-26 18:21:06');
INSERT INTO `pos_sale_item` VALUES (5, 2, 5, 80.00, 2, 'paid', '0', NULL, '2022-06-25 18:21:10', NULL, '2022-06-26 18:21:10');
INSERT INTO `pos_sale_item` VALUES (6, 2, 6, 30.00, 3, 'paid', '0', NULL, '2022-06-25 18:21:18', NULL, '2022-06-26 18:21:18');
INSERT INTO `pos_sale_item` VALUES (7, 3, 5, 80.00, 1, 'reserved', '0', NULL, '2022-06-25 18:21:22', NULL, NULL);
INSERT INTO `pos_sale_item` VALUES (8, 3, 6, 30.00, 1, 'reserved', '0', NULL, '2022-06-25 18:21:27', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
