package bigyan.com.milwaukeeelectrical;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bigyan.com.milwaukeeelectrical.model.CityModel;

/**
 * Created by bigyanthapa on 12/14/15.
 */
public class CustomListAdapter extends BaseAdapter {


    private List<CityModel> cityModelList = new ArrayList<>();
    private Context context;

    private View newView;

    public CustomListAdapter(List<CityModel> cityModelList, Context context) {
        this.cityModelList = cityModelList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        newView = inflater.inflate(R.layout.list_item, null);

        TextView cityText = (TextView) newView.findViewById(R.id.cityNameTextView);
        TextView statusText = (TextView) newView.findViewById(R.id.statusTextView);

        CityModel cityModel = cityModelList.get(i);

        // we will set values here later

        cityText.setText(cityModel.getCityName());
        statusText.setText(cityModel.getCityStatus());


        return null;
    }
}
