package com.milwaukeetool.mymilwaukee.config;

/**
 * Created by cent146 on 11/6/14.
 */
public class MTConstants {

    public static final String PRIVACY_POLICY_URL = "http://www.milwaukeetool.com/privacy";

    public static final String LOG_IN_GRANT_TYPE_PASSWORD = "password";
    public static final String TOKEN_TYPE_BEARER = "Bearer";
    public static final String DEVICE_MFR_SAMSUNG = "samsung";
    public static final String HTTP_PREFIX = "http:";

    public static final String INVENTORY_SEARCH_QUERY = "InventorySearchQuery";
    public static final String INVENTORY_SEARCH_ACTION = "InventorySearchAction";

    public static final String EMPTY_STRING = "";

    public static final String INTENT_EXTRA_INVENTORY_FILTER_TYPE = "Intent_InventoryFilterType";
    public static final String INTENT_EXTRA_SELECT_ITEM_ARRAY_LIST = "Intent_SelectItemArrayList";
    public static final String INTENT_EXTRA_CATEGORY = "Intent_Category";
    public static final String INTENT_EXTRA_MANUFACTURER = "Intent_Manufacturer";
    public static final String INTENT_EXTRA_SELECTED_INDEX = "Intent_SelectedIndex";
    public static final String INTENT_EXTRA_TITLE = "Intent_Title";
    public static final String INTENT_EXTRA_SEARCH_ITEM_RESULT = "Intent_MTItemSearchResult";
    public static final String INTENT_EXTRA_KIT_ITEM_ARRAY_LIST = "Intent_KitItemArrayList";
    public static final String INTENT_EXTRA_ITEM_TYPE = "Intent_ItemType";

    public static final int FILTER_INVALID_MANUFACTURER = -1;
    public static final int FILTER_INVALID_CATEGORY = 0;

    public static final String MILWAUKEE_ITEM = "MilwaukeeItem";
    public static final String OTHER_ITEM = "OtherItem";

    public static final int CREATE_ACCOUNT_REQUEST = 1000;
    public static final int LOGIN_REQUEST = 1001;
    public static final int SELECT_CATEGORY_REQUEST = 2000;
    public static final int SELECT_CATEGORY_ITEM_REQUEST = 2010;
    public static final int SELECT_MANUFACTURER_ITEM_REQUEST = 2011;
    public static final int ADD_OTHER_ITEM_REQUEST = 3000;

}
