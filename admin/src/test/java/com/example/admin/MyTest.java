package com.example.admin;

public class MyTest {
    public static void main(String[] args) {

    }

    public static int doAdd(int i) {
        if (i > 1){
            return  doAdd(i-1) + i;
        }else {
            return i;
        }
    }
}
