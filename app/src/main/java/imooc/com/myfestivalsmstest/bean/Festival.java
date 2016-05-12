package imooc.com.myfestivalsmstest.bean;

/**
 * Created by suncj1 on 2016/1/21.
 */
public class Festival {
    private int id;
    private String name;

    public Festival(int id, String name){
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
