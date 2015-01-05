package com.milwaukeetool.mymilwaukee.manager;

import com.milwaukeetool.mymilwaukee.model.MTCategory;
import com.milwaukeetool.mymilwaukee.model.MTManufacturer;
import com.milwaukeetool.mymilwaukee.model.event.MTChangeFilterEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by cent146 on 12/23/14.
 */
public class MyInventoryManager {

    public enum MyInventoryFilterType {
        FILTER_TYPE_DEFAULT,
        FILTER_TYPE_BY_MANUFACTURER,
        FILTER_TYPE_CATEGORY,
        FILTER_TYPE_ALL_INVENTORY,
        FILTER_TYPE_BY_MODEL_NUMBER
    }

    private MTManufacturer mCurrentManufacturer;
    private MTCategory mCurrentCategory;

    private static MyInventoryManager instance;

    // Providing Global point of access
    public static MyInventoryManager sharedInstance() {

        if (null == instance) {
            instance = new MyInventoryManager();
        }
        return instance;
    }

    private MyInventoryManager() {
        EventBus.getDefault().register(this);
    }

    private MyInventoryFilterType mInventoryFilterType = MyInventoryFilterType.FILTER_TYPE_DEFAULT;


    public void onEvent(MTChangeFilterEvent event) {
        if (event != null) {
            mInventoryFilterType = event.getFilterType();
            mCurrentCategory = event.getCategory();
            mCurrentManufacturer = event.getManufacturer();
        }
    }

    public MTManufacturer getCurrentManufacturer() {
        return mCurrentManufacturer;
    }

    public void setCurrentManufacturer(MTManufacturer mCurrentManufacturer) {
        this.mCurrentManufacturer = mCurrentManufacturer;
    }

    public MTCategory getCurrentCategory() {
        return mCurrentCategory;
    }

    public void setCurrentCategory(MTCategory mCurrentCategory) {
        this.mCurrentCategory = mCurrentCategory;
    }

    public MyInventoryFilterType getInventoryFilterType() {
        return mInventoryFilterType;
    }

    public void setInventoryFilterType(MyInventoryFilterType mInventoryFilterType) {
        this.mInventoryFilterType = mInventoryFilterType;
    }
}
