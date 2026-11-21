package com.fjnu.lab3;

import java.util.ArrayList;
import java.util.List;

/**
 * 动物数据模型：名称 + 图片资源 id。
 * 图片来自实验提供的 QQ 群附件资源，已放入 res/drawable。
 */
public class Animal {

    private final String name;
    private final int photoRes;

    public Animal(String name, int photoRes) {
        this.name = name;
        this.photoRes = photoRes;
    }

    public String getName() {
        return name;
    }

    public int getPhotoRes() {
        return photoRes;
    }

    /** 实验用到的 6 种动物，供 ListView 与 RecyclerView 共用。 */
    public static List<Animal> createZoo() {
        List<Animal> zoo = new ArrayList<>();
        zoo.add(new Animal("Lion", R.drawable.lion));
        zoo.add(new Animal("Tiger", R.drawable.tiger));
        zoo.add(new Animal("Monkey", R.drawable.monkey));
        zoo.add(new Animal("Dog", R.drawable.dog));
        zoo.add(new Animal("Cat", R.drawable.cat));
        zoo.add(new Animal("Elephant", R.drawable.elephant));
        return zoo;
    }
}
