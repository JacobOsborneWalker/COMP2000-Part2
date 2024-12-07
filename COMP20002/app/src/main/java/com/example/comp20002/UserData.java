package com.example.comp20002;

public class UserData {
    int id;
    public String firstname;
    String lastname;
    String email;
    String department;
    float salary;
    String joiningdate;
    int leaves;

    // Getters for better encapsulation (optional)
    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public float getSalary() {
        return salary;
    }

    public String getJoiningdate() {
        return joiningdate;
    }

    public int getLeaves() {
        return leaves;
    }
}
