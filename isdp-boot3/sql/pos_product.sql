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

 Date: 08/11/2025 21:11:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pos_product
-- ----------------------------
DROP TABLE IF EXISTS `pos_product`;
CREATE TABLE `pos_product`  (
  `product_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `product_sn` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品编号',
  `product_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `product_description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品描述',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `product_category_id` bigint(0) NOT NULL COMMENT '类别ID',
  `image_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主图URL',
  `detail_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '详情URL',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`product_id`) USING BTREE,
  UNIQUE INDEX `product_sn_index`(`product_sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pos_product
-- ----------------------------
INSERT INTO `pos_product` VALUES (1, '1001', '钢笔', '', 65.00, 1, NULL, NULL, 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (2, '1002', '日记本', '', 20.00, 1, NULL, NULL, 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (3, '1003', '铅笔盒', '', 45.00, 1, NULL, NULL, 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (4, '1004', '毛巾', '', 10.00, 2, NULL, NULL, 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (5, '1005', '香波', '', 80.00, 2, NULL, NULL, 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (6, '1006', '拖鞋', '', 30.00, 2, NULL, NULL, 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (7, '1007', 'Mate X5', '超轻薄四曲折叠，超高分辨率临境双屏', 13499.00, 3, 'https://res6.vmallres.com/pimages//uomcdn/CN/pms/202309/gbom/6942103107320/800_800_959526DD397D0C873FCE80CE67C9A0BFmp.png', 'https://www.vmall.com/product/comdetail/index.html?prdId=10086281788718&sbomCode=2601010457506', 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (8, '1008', 'Pura 70 Pro', '超聚光微距长焦，超高速风驰闪拍', 6499.00, 3, 'https://res2.vmallres.com/pimages//uomcdn/CN/pms/202404/gbom/6942103119071/800_800_AE94E48F4A6370D6E956B4E722588A5Amp.png', 'https://www.vmall.com/product/comdetail/index.html?prdId=10086821546239&sbomCode=2601010486504', 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (9, '1009', 'Mate 60', '超可靠玄武架构，全焦段超清影像', 5499.00, 3, 'https://res8.vmallres.com/pimages//uomcdn/CN/pms/202403/gbom/6942103107719/800_800_5818B6A12914274ED24FB8FE47F1CD34mp.png', 'https://www.vmall.com/product/comdetail/index.html?prdId=10086970184614&sbomCode=2601010453707', 'admin', '2025-10-01 23:58:40', NULL, NULL);
INSERT INTO `pos_product` VALUES (10, '1010', 'Pura 70', '超高速风驰闪拍，第二代昆仑玻璃', 8499.00, 3, 'https://res4.vmallres.com/pimages//uomcdn/CN/pms/202404/gbom/6942103120374/800_800_606AD050130CDD9F17DBBB7EECDD9B4Amp.png', 'https://www.vmall.com/product/comdetail/index.html?prdId=10086157311748&sbomCode=2601010486608', 'admin', '2025-10-01 23:58:40', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;

