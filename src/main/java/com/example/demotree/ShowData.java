package com.example.demotree;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端树的展示对象
 */
@Data
public class ShowData  implements Cloneable{
    String color = "";
    String name;
    String subName;
    String tag;
    int id;
    List<ShowData> children = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.length()<=5){
            this.subName=name;
        }else{
            this.subName = name.substring(0,5)+"...";
        }
        if(name.length()==64){
            this.tag="H\uD83D\uDC49";
        }else {
            this.tag="D\uD83D\uDC49";
        }

        this.name = name;
    }

    @Override
    public Object clone() {
        ShowData stu = null;
        try{
            stu = (ShowData)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }


}
