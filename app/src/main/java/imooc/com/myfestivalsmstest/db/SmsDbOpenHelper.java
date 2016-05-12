package imooc.com.myfestivalsmstest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import imooc.com.myfestivalsmstest.bean.SendedMsg;

/**
 * Created by suncj1 on 2016/1/27.
 */
public class SmsDbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="sms.db";
    private static final int DB_VERSION=1;

    private SmsDbOpenHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    private static SmsDbOpenHelper mHelper;
    public static SmsDbOpenHelper getInstance(Context context){
        if (mHelper == null){
            synchronized (SmsDbOpenHelper.class){
                if (mHelper == null){
                    mHelper = new SmsDbOpenHelper(context);
                }
            }
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "create table "+ SendedMsg.TABLE_NAME +" ( "+
                "_id integer primary key autoincrement , "+
                SendedMsg.COLUMN_DATE + " integer , "+
                SendedMsg.COLUMN_FES_NAME + " text , "+
                SendedMsg.COLUMN_MSG + " text , "+
                SendedMsg.COLUMN_NAMES + " text , "+
                SendedMsg.COLUMN_NUMBERS + " text  "+
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
