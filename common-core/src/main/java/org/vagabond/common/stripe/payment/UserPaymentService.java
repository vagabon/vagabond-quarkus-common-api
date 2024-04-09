package org.vagabond.common.stripe.payment;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.stripe.model.PaymentIntent;

import org.vagabond.common.notification.NotificationService;
import org.vagabond.common.notification.payload.NotificationRequest;
import org.vagabond.common.user.UserEntity;
import org.vagabond.common.user.UserService;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;
import org.vagabond.engine.exeption.MetierException;

@ApplicationScoped
public class UserPaymentService extends BaseService<UserPaymentEntity> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Inject
    private UserPaymentRepository creatorPaymentRepository;

    @Inject
    private UserService userService;

    @Inject
    private NotificationService notificationService;

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

    public void addPremiumPlan(UserEntity user) {
        var endPlan = userService.addOrUpdateProfilePremium(user);
        var title = "Abonnement actif";
        var description = "Votre abonnement est actif jusqu'au : " + endPlan.format(formatter);
        var notification = new NotificationRequest(title, description, "/profile");
        notificationService.sendNotification(user, Arrays.asList(user.id), notification, user.id, "CREATOR", "SUBSCRIPTION", "CREATE");
    }
}