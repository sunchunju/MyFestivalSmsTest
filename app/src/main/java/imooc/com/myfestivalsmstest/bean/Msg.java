package imooc.com.myfestivalsmstest.bean;

/**
 * Created by suncj1 on 2016/1/21.
 */
public class Msg {
    private int id;
    private int festivalId;
    private String content;

    public Msg(int id, int festivalId, String content) {
        this.id = id;
        this.festivalId = festivalId;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public int getFestivalId() {
        return festivalId;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFestivalId(int festivalId) {
        this.festivalId = festivalId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
