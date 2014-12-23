package com.milwaukeetool.mymilwaukee.adapter;

/**
 * Created by cent146 on 12/22/14.
 */

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.config.MTConstants;
import com.milwaukeetool.mymilwaukee.model.MTSection;
import com.milwaukeetool.mymilwaukee.model.MTUserItem;
import com.milwaukeetool.mymilwaukee.services.MTUserItemManager;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.NamedObject;
import com.milwaukeetool.mymilwaukee.util.StringHelper;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTListItemHeaderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;

public class InventoryItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private Activity mActivity = null;

    private ArrayList<NamedObject> _listItems = null;

    public InventoryItemAdapter(Context context, ArrayList<NamedObject> listItems) {

        if (listItems != null) {
            _listItems = listItems;
        } else {
            _listItems = new ArrayList<>();
        }

        if (context instanceof Activity) {
            mActivity = (Activity)context;
        }

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (_listItems == null) {
            return 0;
        }
        return _listItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (_listItems == null) {
            return null;
        }
        return _listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        NamedObject namedObject = _listItems.get(position);

        LOGD("InventoryItemAdapter", "Get View for type: " + namedObject.name + " at index: " + position);

        if(convertView == null) {
            if (MTUserItemManager.isSection(namedObject) && (namedObject.object instanceof MTSection)) {
                view = createNewHeaderView(namedObject);
                updateHeader(namedObject, view);

            } else if (MTUserItemManager.isUserItem(namedObject) && (namedObject.object instanceof MTUserItem)) {
                view = createNewItemView(namedObject, parent);
                updateItem(namedObject, view);
            }
        } else {

            if (MTUserItemManager.isSection(namedObject) && (namedObject.object instanceof MTSection)) {

                if (view != null && view instanceof MTListItemHeaderView) {
                    view = convertView;
                } else {
                    view = createNewHeaderView(namedObject);
                }

                updateHeader(namedObject, view);

            } else if (MTUserItemManager.isUserItem(namedObject) && (namedObject.object instanceof MTUserItem)) {

                if (view != null && !(view instanceof MTListItemHeaderView)) {
                    view = convertView;
                } else {
                    view = createNewItemView(namedObject, parent);
                }

                updateItem(namedObject, view);
            }
        }

        if (view == null) {
            view = new View(mActivity);
        }

        return view;
    }

    public View createNewHeaderView(NamedObject namedObject) {
        MTSection section = (MTSection)namedObject.object;

        View view = new MTListItemHeaderView(mActivity);
        ((MTListItemHeaderView)view).setMargins(0, 0, 0, UIUtils.getPixels(0));

        return view;
    }

    public View createNewItemView(NamedObject namedObject, ViewGroup parent) {

        View view = mInflater.inflate(R.layout.view_list_item_inventory_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.thumbnailImageView = (ImageView)view.findViewById(R.id.itemImageView);
        holder.descriptionTextView = (TextView)view.findViewById(R.id.itemDescriptionTextView);
        holder.modelNumberTextView = (TextView)view.findViewById(R.id.itemModelNumberTextView);
        holder.actionImageView = (ImageView)view.findViewById(R.id.addItemImageView);
        view.setTag(holder);


        final IconDrawable arrowRight = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                Iconify.IconValue.fa_angle_right).colorRes(R.color.mt_common_gray).sizeDp(25);
        holder.actionImageView.setImageDrawable(arrowRight);

        return view;
    }

    public void updateItem(NamedObject namedObject, View view) {

        ViewHolder holder = (ViewHolder)view.getTag();

        MTUserItem userItem = (MTUserItem)namedObject.object;

        if (userItem != null && holder != null) {

            int placeholderResId;
            if (userItem.getManufacturer().isPrimary()) {
                placeholderResId = R.drawable.ic_mkeplaceholder;
            } else {
                placeholderResId = R.drawable.ic_otherplaceholder;
            }

            Picasso.with(mActivity)
                    .load(MTConstants.HTTP_PREFIX + userItem.getImageUrl())
                    .placeholder(placeholderResId)
                    .error(placeholderResId)
                    .into(holder.thumbnailImageView);

            Date serverDate = StringHelper.parseServerDate(userItem.getDateAdded());
            String formattedDateString = StringHelper.getDateFull(serverDate);

            String descriptionString = userItem.getManufacturer().getName() + " " + userItem.getItemDescription();

            // Build out the model number string
            String modelNumberString = userItem.getModelNumber() + " • ";
            if (!TextUtils.isEmpty(userItem.getCustomIdentifier())) {
                modelNumberString += userItem.getCustomIdentifier();
            } else if (!TextUtils.isEmpty(userItem.getSerialNumber())) {
                modelNumberString += userItem.getSerialNumber();
            } else {
                modelNumberString += MiscUtils.getString(R.string.all_inventory_details_date) + formattedDateString;
            }

            holder.descriptionTextView.setText(descriptionString);
            holder.modelNumberTextView.setText(modelNumberString);
        }
    }

    public void updateHeader(NamedObject namedObject, View view) {
        MTSection section = (MTSection)namedObject.object;

        if (view instanceof MTListItemHeaderView) {
            ((MTListItemHeaderView)view).setHeader(section.getTitle());
        }
    }

    private class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView descriptionTextView;
        public TextView modelNumberTextView;
        public ImageView actionImageView;
    }

    public void clearListItems() {
        _listItems.clear();
        notifyDataSetChanged();
    }

    public void updateListItems(MTUserItemManager userItemManager) {
        _listItems = MTUserItemManager.getAllListItemsForResponse(userItemManager.getInventorySections());
        notifyDataSetChanged();
    }
}
