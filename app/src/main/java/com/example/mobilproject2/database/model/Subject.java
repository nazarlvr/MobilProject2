package com.example.mobilproject2.database.model;

public class Subject
{
    private int id;
    private String name;
    private String professor;
    private String address;
    private int mark;

    public Subject(String name, String professor, String address, int mark)
    {
        this.name = name;
        this.professor = professor;
        this.address = address;
        this.mark = mark;
    }

    public Subject(int id, String name, String professor, String address, int mark)
    {
        this(name, professor, address, mark);
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
