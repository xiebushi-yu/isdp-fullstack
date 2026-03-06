package edu.scau.mis.pos.mapper;

import edu.scau.mis.pos.domain.SaleItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ISaleItemMapper {
    /**
     *根据id查询订单明细对象
     * @param saleItemId
     * @return
     */
    public SaleItem selectSaleItemById(Long saleItemId);

    /**
     *查询订单明细列表
     * @param saleItem
     * @return
     */
    public List<SaleItem> selectSaleItemList(SaleItem saleItem);

    /**
     *新增订单明细对象
     * @param saleItem
     * @return
     */
    public int insertSaleItem(SaleItem saleItem);

    /**
     * 更新订单对象
     * @param saleItem
     * @return
     */
    public int updateSaleItem(SaleItem saleItem);

    /**
     *批量新增订单明细
     * @param saleItemList
     * @return
     */
    public int batchInsertSaleItemOfCurrentSale(List<SaleItem> saleItemList);
}
