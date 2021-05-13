package com.naer.pdfreader;

public class Student {
    public static String Name;
    private String list_name;
    private Integer marks;
    private Integer rank;

    public Student() {
        list_name = " ";
        marks = 0;
        rank = -1;
    }

    public String getName() {
        return list_name;
    }

    public void setName(String name) {
        this.list_name = name;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

}
