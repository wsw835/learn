package com.wensw.service.impl;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component(value = "userService01")
public class UserServiceImpl{

    private String name = "initBean01";


    public UserServiceImpl() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserServiceImpl{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserServiceImpl that = (UserServiceImpl) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
