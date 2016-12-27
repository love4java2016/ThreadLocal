package com;

import java.util.Random;

public class ThreadLocalTest {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();// 多线程共享数据

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            // 多个线程往该threadLocal中存入值
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // synchronized (ThreadLocalTest.class) {
                    int data = new Random().nextInt();
                    System.out.println(Thread.currentThread().getName() + " has put data :" + data);
                    threadLocal.set(data);
                    MyThreadScopeData.getThreadInstance().setName("name" + data);
                    MyThreadScopeData.getThreadInstance().setAge(data);
                    // 多个类中读取threadLocal的值，可以看到多个类在同一个线程中共享同一份数据
                    new A().get();
                    new B().get();
                    // }
                }
            }).start();
        }
    }

    /**
     * 模拟业务模块A
     * 
     * @author Administrator
     * 
     */
    static class A {
        public void get() {
            int data = threadLocal.get();
            System.out.println("A from " + Thread.currentThread().getName() + " get data :" + data);
            MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
            System.out.println("A from " + Thread.currentThread().getName() + " getMyData: " + myData.getName() + "," + myData.getAge());
        }
    }

    /**
     * 模拟业务模块B
     * 
     * @author Administrator
     * 
     */
    static class B {
        public void get() {
            int data = threadLocal.get();
            System.out.println("B from " + Thread.currentThread().getName() + " get data :" + data);
            MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
            System.out.println("B from " + Thread.currentThread().getName() + " getMyData: " + myData.getName() + "," + myData.getAge());
        }
    }
}

class MyThreadScopeData {
    private static ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();

    public static MyThreadScopeData getThreadInstance() {
        MyThreadScopeData instance = map.get();
        if (instance == null) {
            instance = new MyThreadScopeData();
            map.set(instance);
        }
        return instance;
    }

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println(Thread.currentThread().getName() + " setName :" + name);
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        System.out.println(Thread.currentThread().getName() + " setAge :" + age);
        this.age = age;
    }
}