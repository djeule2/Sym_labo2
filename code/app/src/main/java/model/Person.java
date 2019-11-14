package model;

public class Person {
    private String name    = "";
    private String firstname = "";
    private String middlename    = "";
    private String gender = "";
    private int     numberPhone = 0;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(int numberPhone) {
        this.numberPhone = numberPhone;
    }


}


