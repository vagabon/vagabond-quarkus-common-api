package org.vagabond.common.stripe.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.entity.BaseCrudEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user-payment")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserPaymentEntity extends BaseCrudEntity {

    public String intent;
    public String secret;

    public Long amount;
    public Long amountReceived;
    public String currency;
    public String paymentId;
    public String paymentMethod;
    public String source;
    public String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserEntity user;
}
