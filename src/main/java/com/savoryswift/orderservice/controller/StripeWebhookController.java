package com.savoryswift.orderservice.controller;

import com.savoryswift.orderservice.entity.Order;
import com.savoryswift.orderservice.service.EmailService;
import com.savoryswift.orderservice.service.OrderService;
import com.savoryswift.orderservice.service.SmsService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    private final Logger logger = Logger.getLogger(StripeWebhookController.class.getName());

    private final OrderService orderService;
    private final EmailService emailService;
    private final SmsService smsService;

    public StripeWebhookController(OrderService orderService, EmailService emailService, SmsService smsService) {
        this.orderService = orderService;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload
    ) {
        try {
            logger.info("📥 Received Stripe webhook");
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
            logger.info("✅ Successfully verified webhook signature for event: " + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {
                logger.info("🛒 Processing checkout.session.completed event");
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

                if (session != null) {
                    logger.info("✅ Successfully parsed checkout session: " + session.getId());

                    if (session.getMetadata() == null || !session.getMetadata().containsKey("orderId")) {
                        logger.warning("❌ No orderId found in session metadata.");
                        return ResponseEntity.ok("Webhook received but no orderId in metadata");
                    }

                    String orderId = session.getMetadata().get("orderId");
                    logger.info("📦 Found orderId in metadata: " + orderId);

                    try {
                        // Mark the order as paid
                        orderService.markOrderAsPaid(orderId);
                        logger.info("💰 Order " + orderId + " successfully marked as paid");

                        // Fetch order details
                        Order order = orderService.getOrderById(orderId); // You must have this method in OrderService

                        // Prepare email content
                        String subject = "Your Order " + orderId + " is Confirmed!";
                        String emailBody = "Thank you for your order! Your payment was successful. 🎉";

                        // Send Email
                        try {
                            emailService.sendEmail(order.getCustomerEmail(), subject, emailBody);
                            logger.info("📧 Confirmation email sent to " + order.getCustomerEmail());
                        } catch (IOException e) {
                            logger.severe("❌ Failed to send email: " + e.getMessage());
                        }

                        // Prepare SMS content
                        String smsBody = "Hi! Your order #" + orderId + " has been paid. Thank you! 🛵";

                        // Send SMS
                        try {
                            smsService.sendSms(order.getCustomerPhoneNumber(), smsBody);
                            logger.info("📱 Confirmation SMS sent to " + order.getCustomerPhoneNumber());
                        } catch (Exception e) {
                            logger.severe("❌ Failed to send SMS: " + e.getMessage());
                        }

                        return ResponseEntity.ok("Webhook received: Payment success");

                    } catch (Exception e) {
                        logger.severe("❌ Error updating order status or sending notifications: " + e.getMessage());
                        return ResponseEntity.ok("Webhook received but error updating order");
                    }

                } else {
                    logger.warning("❌ Failed to parse session from event");
                    return ResponseEntity.ok("Webhook received but session parsing failed");
                }

            } else {
                logger.info("ℹ️ Received non-checkout event: " + event.getType() + " (ignoring)");
            }

            return ResponseEntity.ok("Webhook received: Event acknowledged");

        } catch (SignatureVerificationException e) {
            logger.warning("❌ Invalid Stripe signature: " + e.getMessage());
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            logger.severe("❌ Webhook error: " + e.getMessage());
            return ResponseEntity.status(500).body("Webhook error: " + e.getMessage());
        }
    }
}
