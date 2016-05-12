package imooc.com.myfestivalsmstest.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import imooc.com.myfestivalsmstest.ChooseMsgActivity;
import imooc.com.myfestivalsmstest.R;
import imooc.com.myfestivalsmstest.bean.Festival;
import imooc.com.myfestivalsmstest.bean.FestivalLab;

/**
 * Created by suncj1 on 2016/1/21.
 */
public class FestivalCategoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView mGridView;
    private ArrayAdapter<Festival> mAdapter;
    private LayoutInflater mInflater;
    public static final String ID_FESTIVAL = "festival_id";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_festival_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(getActivity());
        mGridView = (GridView) view.findViewById(R.id.id_gv_festival_category);
        mAdapter = new FestivalAdapter(getActivity(), -1, FestivalLab.getInstance().getFestivals());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), ChooseMsgActivity.class);
        intent.putExtra(ID_FESTIVAL, mAdapter.getItem(i).getId());
        startActivity(intent);
    }

    private class FestivalAdapter extends ArrayAdapter<Festival> {
        public FestivalAdapter(Context context, int resource, List<Festival> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.item_festival, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.id_tv_festival_name);
            tv.setText(getItem(position).getName());

            return convertView;
        }
    }
}
