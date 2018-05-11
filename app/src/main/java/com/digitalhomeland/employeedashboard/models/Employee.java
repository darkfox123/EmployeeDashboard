package com.digitalhomeland.employeedashboard.models;

/**
 * Created by Asus on 2/3/2018.
 */

public class Employee {
    String _id;
    String firstName;
    String middleName;
    String lastName;
    String phone;
    String email;
    String aadharId;
    String employeeId;

    public Employee(String _id, String firstName, String middleName, String lastName, String phone, String email,String aadharId, String employeeId)
    {
        this._id = _id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.aadharId = aadharId;
        this.employeeId = employeeId;
    }

    public String get_id(){return _id;}
    public String getFirstName(){return  firstName;}
    public String getMiddleName() {return middleName;}
    public String getLastName() {return lastName;}
    public String getPhone() {return phone;}
    public String getEmail() {return email;}
    public String getAadharId() {return aadharId;}
    public String getEmployeeId() { return employeeId;}
}
