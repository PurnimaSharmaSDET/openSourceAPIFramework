package com.opensourceFramework.utils.mongoDb;

import lombok.Data;
import org.bson.types.ObjectId;
@Data
public class User {
    private ObjectId id;
    private String name;
    private int age;

    public User(String alice, int i) {
    }
}
