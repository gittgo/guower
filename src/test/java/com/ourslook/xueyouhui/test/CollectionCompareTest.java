//package com.ourslook.guower.test;
//
//import jodd.util.collection.SortedArrayList;
//import org.junit.Test;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
///**
// * Created by dazer on 2017/8/4.
// */
//@SuppressWarnings("all")
//public class CollectionCompareTest {
//    @Test
//    public void test() {
//        List<Person> list = new SortedArrayList<>();
//        Person person1 = new Person(10, "张三");
//        Person person2 = new Person(2, "李四");
//        Person person3 = new Person(14, "王五");
//        Person person4 = new Person(10, "张三");
//        list.add(person1);
//        list.add(person2);
//        list.add(person3);
//        list.add(person4);
//        System.out.println(list);
//        System.out.println();
//
//
//        List<Map> list1 = new SortedArrayList<>();
//        Map map1 = new MapComparableMap();
//        map1.put("id", 1);
//        map1.put("name", "张三");
//        list1.add(map1);
//        Map map2 = new MapComparableMap();
//        map2.put("id", 3);
//        map2.put("name", "李四");
//        list1.add(map2);
//        Map map3 = new MapComparableMap();
//        map3.put("id", 2);
//        map3.put("name", "王五");
//        list1.add(map3);
//        System.out.println(list1);
//    }
//
//    public static class MapComparableMap extends TreeMap implements Comparable<Map> {
//        @Override
//        public int compareTo(Map o) {
//            return new Integer(this.get("id") + "").compareTo(new Integer(o.get("id") + ""));
//        }
//    }
//
//    public static class Person implements Comparable<Person> {
//        private int age;
//        private String name;
//
//        public Person() {
//        }
//
//        public Person(int age, String name) {
//            this.age = age;
//            this.name = name;
//        }
//
//        public int getAge() {
//            return age;
//        }
//
//        public void setAge(int age) {
//            this.age = age;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        @Override
//        public String toString() {
//            return "Person{" +
//                    "age=" + age +
//                    ", name='" + name + '\'' +
//                    '}';
//        }
//
//        @Override
//        public int compareTo(Person o) {
//            return new Integer(this.age).compareTo(o.getAge());
//        }
//    }
//}
