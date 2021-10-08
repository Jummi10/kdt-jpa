package org.prgrms.kdtjpa.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private Long id;
    private int price;
    private int stockQuantity;

    private ItemType type;

    // Food
    private String chef;
    // Car
    private Integer power;
    // Furniture
    private Integer width;
    private Integer height;
}
