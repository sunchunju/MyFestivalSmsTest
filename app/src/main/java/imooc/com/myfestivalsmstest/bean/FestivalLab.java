package imooc.com.myfestivalsmstest.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suncj1 on 2016/1/21.
 */
public class FestivalLab {
    //单例模式

    public static FestivalLab mInstance;

    private List<Festival> mFestivals = new ArrayList<Festival>();
    private List<Msg> mMsgs = new ArrayList<Msg>();

    private FestivalLab(){
        mFestivals.add(new Festival(1,"国庆节"));
        mFestivals.add(new Festival(2,"中秋节"));
        mFestivals.add(new Festival(3,"元旦"));
        mFestivals.add(new Festival(4,"春节"));
        mFestivals.add(new Festival(5,"端午节"));
        mFestivals.add(new Festival(6,"七夕节"));
        mFestivals.add(new Festival(7,"圣诞节"));
        mFestivals.add(new Festival(8,"儿童节"));

        mMsgs.add(new Msg(1,1,"a"));
        mMsgs.add(new Msg(2,1,"ab"));
        mMsgs.add(new Msg(3,1,"ac"));
        mMsgs.add(new Msg(4,1,"ad"));
        mMsgs.add(new Msg(5,1,"ae"));
        mMsgs.add(new Msg(6,1,"af"));
        mMsgs.add(new Msg(7,1,"aa"));
        mMsgs.add(new Msg(8,1,"ahd"));
    }

    public List<Msg> getMsgsByFestivalId(int fesId){
        List<Msg> msgs = new ArrayList<Msg>();
        for (Msg msg: mMsgs){
            if (msg.getFestivalId() == fesId)
                msgs.add(msg);
        }
        return  msgs;
    }

    public Msg getMsgByMsgId(int id){
        for (Msg msg:mMsgs){
            if (msg.getId() == id)
                return msg;
        }
        return null;
    }

    public List<Festival> getFestivals(){
        return new ArrayList<>(mFestivals);
    }

    public Festival getFestivalById(int fesId){
        for (Festival festival: mFestivals){
            if (festival.getId() == fesId)
                return festival;
        }
        return null;
    }

    public static FestivalLab getInstance(){
        if (mInstance == null){ //第一层非空判断主要是为了提升效率，不需要每次都new instance
            synchronized (FestivalLab.class){
                if (mInstance == null) //第二层非空判断主要是防止如果有两个线程同时进入后，再一起new instance
                    mInstance = new FestivalLab();
            }
        }
        return mInstance;
    }
}
