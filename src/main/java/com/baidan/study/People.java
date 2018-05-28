package com.baidan.study;

import javax.lang.model.element.NestingKind;

/**
 * Created by A8885 on 2017/9/12.
 */
public class People {
    int age;
    String name;
    String address;
    String sex;

    void setAge(int age_v){
        age = age_v;
    }

    int getAge(){
        return age;
    }

    void setName(String name_v){
        name = name_v;
    }
    String getName(){
        return name;
    }

    void printPopleInfo() {
        int x;
        System.out.println(name + sex + age + address);
    }

}

class PeopleTestDrive {
    public static  void  main(String[] args){
        People [] ps = new People[2];
        ps[0] = new People();
        ps[0].sex = "女";
        ps[0].age = 10;
        ps[0].name = "李亦非";
        ps[0].address = "深圳市罗湖区";
        ps[0].printPopleInfo();
    }
}