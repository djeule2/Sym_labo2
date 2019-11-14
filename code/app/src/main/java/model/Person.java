package model;

import java.io.Serializable;

public class Person implements Serializable {
    private String name    = "";
    private String firstname = "";
    private String middlename    = "";
    private String gender = "";
    private Integer    numberPhone = 0;

    public Person() { /* DEFAULT CONSTRUCTOR */}

    public Person(final String nom, final String firsname,
                  final String gender, final int numberPhone) {
        this.name = nom;
        this.firstname = firsname;
        this.gender = gender;
        this.numberPhone = numberPhone;
    }

    public Person(final String nom, final String firsname, final String middlename,
                  final String gender, final int numberPhone) {
        this.name = nom;
        this.firstname = firsname;
        this.middlename = middlename;
        this.gender = gender;
        this.numberPhone = numberPhone;
    }

    //retourne name
    public String getName() {
        return name;
    }

    //modifie un name
    public void setName(String name) {
        this.name = name;
    }

    //retourne firstname
    public String getFirstname() {
        return firstname;
    }

    //modifie un firstname
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    //retourne un middlename
    public String getMiddlename() {
        return middlename;
    }

    //modifie un middlename
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    //retourne un gender
    public String getGender() {
        return gender;
    }


    //modifi un gender
    public void setGender(String gender) {
        this.gender = gender;
    }

    //retourne un numberphone
    public int getNumberPhone() {
        return numberPhone;
    }

    //modifi numberPhone
    public void setNumberPhone(int numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String toString() {
        return "Person{" +'\n'+
                "name=" + name + '\n' +
                "firstname=" + firstname + '\n' +
                "middlename=" + middlename + '\n' +
                "phone number=" + numberPhone+
                '}';
    }


}
