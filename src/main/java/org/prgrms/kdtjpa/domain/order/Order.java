package org.prgrms.kdtjpa.domain.order;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders") // order: 많은 db의 keyword로 동작
public class Order {
    @Id
    @Column(name = "id")
    private String uuid;

    @Column(name = "memo")
    private String memo;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "order_datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime orderDatetime;

    // member_fk
    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id") // FK name, join하기 위해 참조하는 테이블의 컬럼명
    private Member member;
}
