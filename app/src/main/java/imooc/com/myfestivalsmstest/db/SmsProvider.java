package imooc.com.myfestivalsmstest.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import imooc.com.myfestivalsmstest.bean.SendedMsg;

/**
 * Created by suncj1 on 2016/1/27.
 */
public class SmsProvider extends ContentProvider {
    private static final String AUTHORITY = "com.imooc.sms.provider.SmsProvider";
    public static final Uri URI_SMS_ALL = Uri.parse("content://"+AUTHORITY+"/sms");

    private static UriMatcher matcher;
    private static final int SMS_ALL = 0;
    private static final int SMS_ONE =1;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY,"sms",SMS_ALL);
        matcher.addURI(AUTHORITY,"sms/#",SMS_ONE);
    }

    private SmsDbOpenHelper mHelper;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        mHelper = SmsDbOpenHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        int match = matcher.match(uri);
        switch (match){
            case SMS_ALL:
                break;
            case SMS_ONE:
                long id = ContentUris.parseId(uri);
                s = "_id = ?";
                strings1 = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: "+uri);
        }
        mDb = mHelper.getReadableDatabase();
        Cursor cr = mDb.query(SendedMsg.TABLE_NAME, strings, s, strings1, null, null, s1);

        //import
        cr.setNotificationUri(getContext().getContentResolver(),URI_SMS_ALL);
        return cr;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = matcher.match(uri);
        if (match != SMS_ALL){
            throw new IllegalArgumentException("Wrong URI: "+uri);
        }
        mDb = mHelper.getWritableDatabase();
        long rowId = mDb.insert(SendedMsg.TABLE_NAME, null, contentValues);
        if (rowId > 1){
            notifyDataSetChanged();
            return ContentUris.withAppendedId(uri,rowId);
        }
        return null;
    }

    private void notifyDataSetChanged() {
        getContext().getContentResolver().notifyChange(URI_SMS_ALL,null);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
