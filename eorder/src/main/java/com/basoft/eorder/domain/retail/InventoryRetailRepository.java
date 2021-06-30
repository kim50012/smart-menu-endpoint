package com.basoft.eorder.domain.retail;

import com.basoft.eorder.batch.job.model.retail.RetailToDoRecoverTempOrder;
import com.basoft.eorder.domain.model.retail.InventoryRetail;
import com.basoft.eorder.interfaces.command.retail.SaveInventoryRetail;

import java.util.List;

/**
 * 零售产品库存表(InventoryRetail)表数据库访问层
 *
 * @author DongXifu
 * @since 2020-04-07 17:45:08
 */
public interface  InventoryRetailRepository{

	Long saveInventoryRetail(SaveInventoryRetail saveInventoryRetail);

	Long  saveInventoryRetails(List<InventoryRetail> inventoryRetails);

	//新增零售产品库存表
	Long  insertInventoryRetail(InventoryRetail inventoryRetail);

	//修改零售产品库存表
	Long  updateInventoryRetail (InventoryRetail inventoryRetail);

	//修改零售产品库存表状态
	Long  updateInventoryRetailStatus(InventoryRetail inventoryRetail);

	/**
	 * 零售业务：库存恢复
	 *
	 * @param orderTempList
	 * @param recoverType
	 */
    void recoverRetailInventory(List<RetailToDoRecoverTempOrder> orderTempList, String recoverType);
}