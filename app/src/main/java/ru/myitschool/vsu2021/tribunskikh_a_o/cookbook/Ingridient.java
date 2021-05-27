package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

public class Ingridient {
    private long id;
    private String name;

    public Ingridient(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
