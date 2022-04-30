package org.example.events.email;

import lombok.extern.slf4j.Slf4j;

import org.example.events.customer.Customer;
import org.example.events.customer.CustomerRepository;
import org.example.events.email.EmailService;
import org.example.events.order.Order;
import org.example.events.order.OrderRepository;
import org.example.events.order.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.example.events.order.Order.OrderStatus.SAVED;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@Slf4j
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmailOrderServiceTest
{
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @SpyBean
    private EmailService emailService;

    @Test
    void forPlaceOrder_whenRolledBacked_dontSendEmail()
    {

        //given
        Customer customer = givenCustomer(90);
        Order order = givenOrder(customer);

        //when
        try
        {
            orderService.placeOrder(order);
        }
        catch (Exception e)
        {
            log.error("Exception while placing an order", e);
        }

        //then
        then(emailService).shouldHaveNoInteractions();

    }

    @Test
    void forPlaceOrder_whenCommitSuccessful_sendAnEmail()
    {

        //given
        Customer customer = givenCustomer(50);
        Order order = givenOrder(customer);

        //when
        orderService.placeOrder(order);

        //then
        then(emailService).should(times(1)).sendOrderEmail(order);

    }

    private Order givenOrder(Customer customer)
    {
        Order order = new Order(SAVED);
        order.setCustomer(customer);
        return orderRepository.save(order);
    }

    private Customer givenCustomer(int rewardPoints)
    {
        Customer customer = new Customer("john@email.com");
        customer.setRewardPoints(BigDecimal.valueOf(rewardPoints));
        return customerRepository.save(customer);
    }



}