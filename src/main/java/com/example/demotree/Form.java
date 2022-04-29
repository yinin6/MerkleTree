package com.example.demotree;

import lombok.Data;

@Data
public class Form {
    Integer index;
    String data;
    String choice;
    Boolean isExist;

    public Form(){

    }

    @Override
    public String toString() {
        return "Form{" +
                "index=" + index +
                ", data='" + data + '\'' +
                ", choice='" + choice + '\'' +
                ", isExist=" + isExist +
                '}';
    }
}
