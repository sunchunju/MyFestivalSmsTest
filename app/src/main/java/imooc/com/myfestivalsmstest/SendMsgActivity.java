package imooc.com.myfestivalsmstest;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

import imooc.com.myfestivalsmstest.bean.Festival;
import imooc.com.myfestivalsmstest.bean.FestivalLab;
import imooc.com.myfestivalsmstest.bean.Msg;
import imooc.com.myfestivalsmstest.bean.SendedMsg;
import imooc.com.myfestivalsmstest.biz.SmsBiz;
import imooc.com.myfestivalsmstest.view.FlowLayout;

public class SendMsgActivity extends AppCompatActivity {

    public static final String KEY_FESTIVAL_ID = "id_festival";
    public static final String KEY_MSG_ID = "id_msg";
    private static final int CODE_REQUEST = 1;

    private int mFestivalId;
    private int mMsgId;

    private Festival mFestival;
    private Msg mMsg;

    private EditText mEdMsg;
    private Button mBtnAddContacts;
    private FlowLayout mFlContacts;
    private FloatingActionButton mFabSendMsg;
    private View mLayoutLoading;

    private HashSet<String> mContactNames = new HashSet<>();
    private HashSet<String> mContactNums = new HashSet<>();

    private LayoutInflater mInflater;

    public static final String ACTION_SEND_MSG = "ACTION_SEND_MSG";
    public static final String ACTION_DELIVER_MSG = "ACTION_DELIVER_MSG";

    private PendingIntent mSendPi;
    private PendingIntent mDeliverPi;

    private BroadcastReceiver mSendBroadcastReceiver;
    private BroadcastReceiver mDeliverBroadcastReceiver;

    private SmsBiz smsBiz;
    private int mTotalSendMsgCount;
    private int mMsgSendCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

        mInflater = LayoutInflater.from(this);
        smsBiz = new SmsBiz(this);

        initDatas();
        initView();
        initEvents();
        initRecivers();
    }

    private void initRecivers() {
        Intent sendIntent = new Intent(ACTION_SEND_MSG);
        mSendPi = PendingIntent.getBroadcast(this, 0 , sendIntent, 0);
        Intent deliverIntent = new Intent(ACTION_DELIVER_MSG);
        mDeliverPi = PendingIntent.getBroadcast(this, 0 , deliverIntent, 0);

        registerReceiver(mSendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mMsgSendCount++;
                if (getResultCode() == RESULT_OK) {
                    Log.e("TAG", "短信发送成功" + (mMsgSendCount + "/" + mTotalSendMsgCount));
                } else {
                    Log.e("TAG", "短信发送失败");
                }
                Toast.makeText(SendMsgActivity.this, (mMsgSendCount + "/" + mTotalSendMsgCount) + " 短信发送成功", Toast.LENGTH_SHORT).show();
                if (mMsgSendCount == mTotalSendMsgCount) {
                    finish(); //关闭当前页面
                }
            }
        }, new IntentFilter(ACTION_SEND_MSG));

        registerReceiver(mDeliverBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == RESULT_OK){
                    Log.e("TAG", "联系人已经成功接收到我们的短信");
                }else if (getResultCode() == RESULT_CANCELED){
                    Log.e("TAG", "联系人未能成功接收到我们的短信");
                }
            }
        }, new IntentFilter(ACTION_DELIVER_MSG));

    }

    private void initEvents() {
        mBtnAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到系统contact界面
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, CODE_REQUEST);
            }
        });

        mFabSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContactNums.size() == 0) {
                    Toast.makeText(SendMsgActivity.this, "请先选择联系人", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = mEdMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(SendMsgActivity.this, "短信内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mLayoutLoading.setVisibility(View.VISIBLE);
                mTotalSendMsgCount = smsBiz.sendMsg(mContactNums, buildSendMsg(msg), mSendPi, mDeliverPi);
                mMsgSendCount = 0;
            }
        });
    }

    private SendedMsg buildSendMsg(String msg) {
        SendedMsg sendedMsg = new SendedMsg();
        sendedMsg.setFestivalName(mFestival.getName());
        sendedMsg.setMsg(msg);
        String names = "";
        for(String name: mContactNames){
            names+=name+":";
        }
        String numbers = "";
        for (String number: mContactNums){
            numbers += number;
        }
        sendedMsg.setNames(names.substring(0, names.length()-1));
        sendedMsg.setNumbers(numbers.substring(0, numbers.length() - 1));

        return sendedMsg;
    }

    private void initDatas() {
        mFestivalId = getIntent().getIntExtra(KEY_FESTIVAL_ID, -1);
        mMsgId = getIntent().getIntExtra(KEY_MSG_ID, -1);

        mFestival = FestivalLab.getInstance().getFestivalById(mFestivalId);
        setTitle(mFestival.getName());
    }

    private void initView() {
        mEdMsg = (EditText) findViewById(R.id.id_et_content);
        mBtnAddContacts = (Button) findViewById(R.id.id_btn_add);
        mFlContacts = (FlowLayout) findViewById(R.id.id_fl_contacts);
        mFabSendMsg = (FloatingActionButton) findViewById(R.id.id_fab_send);
        mLayoutLoading = findViewById(R.id.id_layout_loading);

        mLayoutLoading.setVisibility(View.GONE);

        if (mMsgId != -1){
            mMsg = FestivalLab.getInstance().getMsgByMsgId(mMsgId);
            mEdMsg.setText(mMsg.getContent());
        }else {
            mEdMsg.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST){

            if (resultCode == RESULT_OK){
                Uri contactUri = data.getData();
                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                cursor.moveToFirst();
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                String contactNum = getContactNum(cursor);
                if (!TextUtils.isEmpty(contactNum)){
                    mContactNums.add(contactNum);
                    mContactNames.add(contactName);

                    addTag(contactName);
                }
            }
        }
    }

    private void addTag(String mContactNames) {
        TextView mTvContacts = (TextView) mInflater.inflate(R.layout.tag, mFlContacts, false);
        mTvContacts.setText(mContactNames);
        mFlContacts.addView(mTvContacts);

    }

    private String getContactNum(Cursor cursor) {
        int numberCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        String number = "";
        if (numberCount > 0){
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +
                    contactId, null, null);
            phoneCursor.moveToFirst();
            number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneCursor.close();
        }
        cursor.close();

        return number;
    }

    public static void toActivity(Context context, int festivalId, int msgId){
        Intent intent = new Intent(context, SendMsgActivity.class);
        intent.putExtra(KEY_FESTIVAL_ID, festivalId);
        intent.putExtra(KEY_MSG_ID,msgId);

        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSendBroadcastReceiver);
        unregisterReceiver(mDeliverBroadcastReceiver);
    }
}
