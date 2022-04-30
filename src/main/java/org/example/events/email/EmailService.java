package org.example.events.email;

import lombok.extern.slf4j.Slf4j;

import org.example.events.customer.Customer;
import org.example.events.order.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailService
{
    public void sendRegisterEmail(Customer customer)
    {
        log.info("Sending registration email to customer {}", customer);
    }

    public void sendCustomerRemovedEmail(Customer customer)
    {
        log.info("Sending removed email for customer {}", customer);
    }
    public void sendOrderEmail(Order order)
    {
        log.info("Sending email for order {}", order);
    }
}
