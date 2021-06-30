package com.basoft.eorder.interfaces.command;

/**
 * @Author:DongXifu
 * @Description:
 * @Date Created in 上午9:42 2019/3/5
 **/
public class StoresTest{
    public static final String NAME = "StoresTest";

    private Long id;
    private String name;
    private String city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
