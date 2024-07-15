package com.vhalency.students;



public class Student {
    private int id;
    private String name;
    private String lastName;
    private byte[] image;
    private String observarion;
    public Student(String name, String lastName, byte[] image, int id, String observarion) {
        this.name = name;
        this.lastName = lastName;
        this.image = image;
        this.id = id;
        this.observarion = observarion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getObservarion() {
        return observarion;
    }

    public void setObservarion(String observarion) {
        this.observarion = observarion;
    }
}

