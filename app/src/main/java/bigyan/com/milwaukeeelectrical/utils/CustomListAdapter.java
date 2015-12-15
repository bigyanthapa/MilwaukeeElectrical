package bigyan.com.milwaukeeelectrical.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bigyan.com.milwaukeeelectrical.R;
import bigyan.com.milwaukeeelectrical.model.Model;

/**
 * Created by bigyanthapa on 12/14/15.
 */
public class CustomListAdapter extends BaseAdapter {

    private List<Model> weatherModelList;
    private Context context;

    //create a view
    private View newView;

    public CustomListAdapter(List<Model> descriptionModelList, Context context) {
        this.weatherModelList = descriptionModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return weatherModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return weatherModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return weatherModelList.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        newView = inflater.inflate(R.layout.list_item, null);


        TextView cityName = (TextView) newView.findViewById(R.id.cityNameTextView);
        TextView status = (TextView) newView.findViewById(R.id.statusTextView);

        Model descriptionModel = weatherModelList.get(i);

        // we will set values here later

        cityName.setText(descriptionModel.getName());
        status.setText(descriptionModel.getWeather().get(i).getDescription());

        return newView;
    }
}
