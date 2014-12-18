package com.milwaukeetool.mymilwaukee.services;

import com.milwaukeetool.mymilwaukee.model.MTSection;
import com.milwaukeetool.mymilwaukee.model.MTUserItem;
import com.milwaukeetool.mymilwaukee.model.response.MTUserItemResponse;
import com.milwaukeetool.mymilwaukee.util.NamedObject;

import java.util.ArrayList;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 12/17/14.
 */
public class MTUserItemHelper {

    private static final String TAG = makeLogTag(MTUserItemHelper.class);

    public static String NAMED_OBJECT_USER_ITEM = "MTUserItem";
    public static String NAMED_OBJECT_SECTION = "MTSection";

//    public static int getNumberOfRowsForUserItemResponse(MTUserItemResponse response) {
//
//        int itemCount = 0;
//        if (response != null && response.getSections() != null) {
//            for(MTSection section : response.getSections()) {
//                if (section != null && section.getItems() != null && section.getItems().size() > 0) {
//                    ++itemCount;
//                    for (MTUserItem item : section.getItems()) {
//                        if (item != null) {
//                            ++itemCount;
//                        }
//                    }
//                }
//            }
//        }
//        return itemCount;
//    }

//    public int getNumberOfSectionsForResponse(MTUserItemResponse response) {
//
//        if (response == null || response.getSections() == null) {
//            return 0;
//        }
//
//        return response.getSections().size();
//    }
//
//    public int getNumberOfItemsInSection(MTSection section) {
//
//        if (section == null || section.getItems() == null) {
//            return 0;
//        }
//
//        return section.getItems().size();
//    }
//
//    public static MTUserItem getItemInSectionAtIndex(MTSection section, int itemIndex) {
//        if (section == null || section.getItems() == null) {
//            return null;
//        }
//
//        return section.getItems().get(itemIndex);
//    }

//    public static NamedObject getNamedObjectForResponseAtRowIndex(MTUserItemResponse response, int rowIndex) {
//
//        int currentIndex = 0;
//
//        if (response != null && response.getSections() != null) {
//            for(MTSection section : response.getSections()) {
//                if (section != null && section.getItems() != null && section.getItems().size() > 0) {
//
//                    if (currentIndex == rowIndex) {
//                        return new NamedObject(NAMED_OBJECT_SECTION, section);
//                    }
//
//                    ++currentIndex;
//
//                    for (MTUserItem item : section.getItems()) {
//                        if (item != null) {
//                            if (currentIndex == rowIndex) {
//                                return new NamedObject(NAMED_OBJECT_USER_ITEM, item);
//                            }
//                            ++currentIndex;
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }

    public static ArrayList<NamedObject> getAllListItemsForResponse(MTUserItemResponse response) {

        ArrayList<NamedObject> listItems = new ArrayList<>();

        int currentIndex = 0;

        if (response != null && response.getSections() != null) {
            for(MTSection section : response.getSections()) {
                if (section != null && section.getItems() != null && section.getItems().size() > 0) {

                    LOGD(TAG, "Adding item at index: " + currentIndex + " for type: " + NAMED_OBJECT_SECTION);
                    listItems.add(new NamedObject(NAMED_OBJECT_SECTION, section));
                    ++currentIndex;

                    for (MTUserItem item : section.getItems()) {
                        if (item != null) {
                            LOGD(TAG, "Adding item at index: " + currentIndex + " for type: " + NAMED_OBJECT_USER_ITEM);
                            listItems.add(new NamedObject(NAMED_OBJECT_USER_ITEM, item));
                            ++currentIndex;
                        }
                    }
                }
            }
        }
        return listItems;
    }

    public static boolean isSection(NamedObject namedObject) {
        return namedObject.name.equalsIgnoreCase(NAMED_OBJECT_SECTION);
    }

    public static boolean isUserItem(NamedObject namedObject) {
        return namedObject.name.equalsIgnoreCase(NAMED_OBJECT_USER_ITEM);
    }

}
