package edu.scau.mis.pos.mapper;

import edu.scau.mis.pos.domain.Sale;
import edu.scau.mis.pos.domain.SaleItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISaleMapper {
    /**
     *根据id查询订单对象
     * @param saleId
     * @return
     */
    public Sale selectSaleById(Long saleId);

    /**
     *查询订单列表
     * @param sale
     * @return
     */
    public List<Sale> selectSaleList(Sale sale);

    /**
     *新增订单对象
     * @param sale
     * @return
     */
    public int insertSale(Sale sale);

    /**
     *更新订单对象
     * @param sale
     * @return
     */
    public int updateSale(Sale sale);
}
