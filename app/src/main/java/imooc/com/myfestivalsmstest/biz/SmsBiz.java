package imooc.com.myfestivalsmstest.biz;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import imooc.com.myfestivalsmstest.bean.SendedMsg;
import imooc.com.myfestivalsmstest.db.SmsProvider;

/**
 * Created by suncj1 on 2016/1/22.
 */
public class SmsBiz {

    Context context;
    public SmsBiz(Context context){
        this.context = context;
    }

    //返回的int表示短信条数
    public int sendMsg(String number, String msg, PendingIntent sentPi, PendingIntent deiverPi){

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> contents = smsManager.divideMessage(msg);
        for (String content: contents){
            smsManager.sendTextMessage(number, null, content, sentPi, deiverPi);
        }
        return contents.size();
    }

    public int sendMsg(Set<String> numbers, SendedMsg msg, PendingIntent sentPi, PendingIntent deiverPi){

        save(msg);
        int result = 0;
        for (String number : numbers){
            int count = sendMsg(number, msg.getMsg(), sentPi, deiverPi);
            result += count;
        }
        return result;
    }

    private void save(SendedMsg msg){
        msg.setDate(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put(SendedMsg.COLUMN_DATE, msg.getDateStr());
        contentValues.put(SendedMsg.COLUMN_MSG, msg.getMsg());
        contentValues.put(SendedMsg.COLUMN_FES_NAME, msg.getFestivalName());
        contentValues.put(SendedMsg.COLUMN_NUMBERS, msg.getNumbers());
        contentValues.put(SendedMsg.COLUMN_NAMES, msg.getNames());

        context.getContentResolver().insert(SmsProvider.URI_SMS_ALL, contentValues);
    }
}
