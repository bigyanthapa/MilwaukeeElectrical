package com.milwaukeetool.mymilwaukee.model.event;

import com.milwaukeetool.mymilwaukee.manager.MyInventoryManager;
import com.milwaukeetool.mymilwaukee.model.MTCategory;
import com.milwaukeetool.mymilwaukee.model.MTManufacturer;

/**
 * Created by cent146 on 12/23/14.
 */
public class MTChangeFilterEvent extends MTEvent {
    private MyInventoryManager.MyInventoryFilterType filterType = MyInventoryManager.MyInventoryFilterType.FILTER_TYPE_DEFAULT;

    private MTCategory category = null;

    private MTManufacturer manufacturer = null;

    public MTChangeFilterEvent(Object _originatedFrom, MyInventoryManager.MyInventoryFilterType _filterType) {
        super.MTEvent(_originatedFrom);
        filterType = _filterType;
    }

    public MTChangeFilterEvent(Object _originatedFrom, MyInventoryManager.MyInventoryFilterType _filterType, MTManufacturer _manufacturer) {
        super.MTEvent(_originatedFrom);
        filterType = _filterType;
        manufacturer = _manufacturer;
    }

    public MTChangeFilterEvent(Object _originatedFrom, MyInventoryManager.MyInventoryFilterType _filterType, MTCategory _category) {
        super.MTEvent(_originatedFrom);
        filterType = _filterType;
        category = _category;
    }

    public MyInventoryManager.MyInventoryFilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(MyInventoryManager.MyInventoryFilterType mFilterType) {
        this.filterType = mFilterType;
    }

    public MTCategory getCategory() {
        return category;
    }

    public void setCategory(MTCategory category) {
        this.category = category;
    }

    public MTManufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(MTManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
}
