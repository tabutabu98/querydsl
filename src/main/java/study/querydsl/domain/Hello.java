package study.querydsl.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
public class Hello {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
}
