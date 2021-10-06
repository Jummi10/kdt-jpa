package org.prgrms.kdtjpa.domain.order;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("FURNITURE")
public class Furniture extends Item {
    private int width;
    private int height;
}
