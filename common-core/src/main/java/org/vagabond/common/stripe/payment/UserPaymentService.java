package org.vagabond.common.stripe.payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.stripe.model.PaymentIntent;

import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;
import org.vagabond.engine.exeption.MetierException;

@ApplicationScoped
public class UserPaymentService extends BaseService<UserPaymentEntity> {

    @Inject
    private UserPaymentRepository creatorPaymentRepository;

    @Override
    public BaseRepository<UserPaymentEntity> getRepository() {
        return creatorPaymentRepository;
    }

    public UserPaymentEntity createPayment(String intentId, String intentSecret, PaymentIntent intent) {
        var nbPayment = countBy("where paymentId = ?1", intent.getId());
        if (nbPayment > 0) {
            throw new MetierException("ERRORS:PAYMENT_FOUND");
        }
        var creatorPayment = new UserPaymentEntity();
        creatorPayment.intent = intentId;
        creatorPayment.secret = intentSecret;
        creatorPayment.amount = intent.getAmount();
        creatorPayment.amountReceived = intent.getAmountReceived();
        creatorPayment.currency = intent.getCurrency();
        creatorPayment.paymentId = intent.getId();
        creatorPayment.paymentMethod = intent.getPaymentMethod();
        creatorPayment.source = intent.getSource();
        creatorPayment.status = intent.getStatus();
        return persist(creatorPayment);
    }
}