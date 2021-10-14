package kr.ac.yeonsung.demo.domain.club;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("book")
@Getter
@Setter
public class Book extends Club{

}

