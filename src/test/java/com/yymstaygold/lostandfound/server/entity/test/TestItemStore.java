package com.yymstaygold.lostandfound.server.entity.test;

import com.yymstaygold.lostandfound.server.entity.Item;

import java.util.HashMap;

/**
 * Created by yanyu on 2018/4/16.
 */
public class TestItemStore {
    public static void main(String[] args) {
        Item item = new Item();
        item.setCustomTypeName(null);
        item.setType(1);
        item.setImagePath("/");
        item.setProperties(new HashMap<>());
        item.getProperties().put("卡号","2015212064");

        item.store();
        System.out.println(item.getItemId());
    }
}
