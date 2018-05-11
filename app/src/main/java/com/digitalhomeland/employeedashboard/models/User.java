package com.digitalhomeland.employeedashboard.models;

/**
 * Created by Asus on 1/11/2018.
 */

public class User {
    String _id;
    String firstName;
    String middleName;
    String lastName;
    String phone;
    String email;
    String aadharId;
    String employeeId;
    String storeId;
    String teamName;
    String designation;

    public User(String _id, String firstName, String middleName, String lastName, String phone, String email,String aadharId, String employeeId, String storeId)
    {
        this._id = _id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.aadharId = aadharId;
        this.employeeId = employeeId;
        this.storeId = storeId;
    }

    public User(String _id, String firstName, String middleName, String lastName, String phone, String email,String aadharId, String employeeId, String storeId, String designation, String teamName)
    {
        this._id = _id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.aadharId = aadharId;
        this.employeeId = employeeId;
        this.storeId = storeId;
        this.designation = designation;
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String get_id(){return _id;}
    public String getFirstName(){return  firstName;}
    public String getMiddleName() {return middleName;}
    public String getLastName() {return lastName;}
    public String getPhone() {return phone;}
    public String getEmail() {return email;}
    public String getAadharId() {return aadharId;}
    public String getEmployeeId() { return employeeId;}
    public String getStoreId() {return storeId;}

}
