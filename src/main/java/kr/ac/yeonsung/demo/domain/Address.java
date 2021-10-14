package kr.ac.yeonsung.demo.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String classNumber;
    private String department;
    private String location;

    protected Address(){

    }

    public Address(String classNumber, String department, String location) {
        this.classNumber = classNumber;
        this.department = department;
        this.location = location; // 아.
    }
}
