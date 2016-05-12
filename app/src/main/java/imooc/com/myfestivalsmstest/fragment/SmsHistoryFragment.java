package imooc.com.myfestivalsmstest.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import imooc.com.myfestivalsmstest.R;
import imooc.com.myfestivalsmstest.bean.SendedMsg;
import imooc.com.myfestivalsmstest.db.SmsProvider;
import imooc.com.myfestivalsmstest.view.FlowLayout;

/**
 * Created by suncj1 on 2016/5/6.
 */
public class SmsHistoryFragment extends ListFragment {

    private LayoutInflater mInflater;

    private CursorAdapter mCursorAdapter;
    private static final int LOADER_ID = 1;

    private static final String TAG = "SmsHistoryFragment";
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("suncj", TAG + " onViewCreated");
        mInflater = LayoutInflater.from(getActivity());

        setupListAdapter();
        initLoader();
    }

    private void setupListAdapter() {

        mCursorAdapter = new CursorAdapter(getActivity(), null, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = mInflater.inflate(R.layout.item_sended_msg, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                TextView msg = (TextView) view.findViewById(R.id.id_tv_msg);
                FlowLayout fl = (FlowLayout) view.findViewById(R.id.id_fl_contacts);
                TextView fes = (TextView) view.findViewById(R.id.id_tv_fes);
                TextView date = (TextView) view.findViewById(R.id.id_tv_date);

                msg.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_MSG)));
                fes.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_FES_NAME)));
                date.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_DATE)));

                String names = cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_NAMES));

                if (TextUtils.isEmpty(names)){
                    return;
                }
                fl.removeAllViews(); //由于fl在listview中是复用的，所以每次执行之前需要removeAllViews();
                for (String name: names.split(":")){
                    addTag(name, fl);
                }
            }
        };

        Log.i("suncj", TAG + " mCursorAdapter.count = "+mCursorAdapter.getCount());
        setListAdapter(mCursorAdapter);
    }

    private void addTag(String name, FlowLayout fl) {
        TextView tv = (TextView) mInflater.inflate(R.layout.tag, fl, false);
        tv.setText(name);
        fl.addView(tv);

    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader onCreateLoader(int id, Bundle args) {
                CursorLoader loader = new CursorLoader(getActivity(), SmsProvider.URI_SMS_ALL, null, null, null, null);
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null){
                    Log.i("suncj", TAG + " data.count = "+data.getCount());
                }
                if (loader.getId() == LOADER_ID){
                    mCursorAdapter.swapCursor(data);
                }
            }

            @Override
            public void onLoaderReset(Loader loader) {

                mCursorAdapter.swapCursor(null);
            }
        });
    }
}
