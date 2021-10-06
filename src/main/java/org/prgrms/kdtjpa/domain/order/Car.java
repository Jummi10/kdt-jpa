package org.prgrms.kdtjpa.domain.order;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("CAR")
public class Car extends Item {
    private int power;
}
