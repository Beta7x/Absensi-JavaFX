package Model;

import java.sql.Date;

public class Student {

    int id;
    String nim, name;
    Date birth;
    String address, email;

    public Student(int id, String nim, String name, Date date, String address, String email) {
        this.id = id;
        this.nim = nim;
        this.name = name;
        this.birth = date;
        this.address = address;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return birth;
    }

    public void setDate(Date date) {
        this.birth = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
