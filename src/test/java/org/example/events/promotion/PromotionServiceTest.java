package org.example.events.promotion;

import org.example.events.customer.Customer;
import org.example.events.customer.CustomerRepository;
import org.example.events.customer.CustomerService;
import org.example.events.order.Order;
import org.example.events.order.OrderRepository;
import org.example.events.order.OrderService;
import org.example.events.promotion.PromotionService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.example.events.order.Order.OrderStatus.SAVED;
import static org.mockito.Mockito.times;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PromotionServiceTest
{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @SpyBean
    private PromotionService promotionService;

    @Test
    void registerCustomer_forCustomerWithoutNewsletter_promotionNotCalled(){

        //given
        Customer customer = givenCustomer("john@email.com", 50, false);

        //when
        customerService.register(customer);

        //then
        BDDMockito.then(promotionService).shouldHaveNoInteractions();
    }
    @Test
    void registerCustomer_forCustomerAppliedForNewsletter_callsPromotion(){

        //given
        Customer customer = givenCustomer("john@email.com", 0, true);

        //when
        customerService.register(customer);

        //then
        BDDMockito.then(promotionService).should(times(1)).applyPromotion(customer);
    }

    @Test
    void placeOrder_forCustomer_addsRewardPoints(){
        //given
        Customer customer = givenCustomer("john@email.com", 50, false);

        Order order = new Order(SAVED);
        order.setCustomer(customer);
        orderRepository.save(order);

        //when
        orderService.placeOrder(order);

        //then
        BigDecimal newRewardStatusPoints = customerRepository
                .findById(customer.getId())
                .map(Customer::getRewardPoints)
                .orElseThrow();

        then(newRewardStatusPoints).isEqualTo(BigDecimal.valueOf(60));
    }

    private Customer givenCustomer(String email, int rewardPoints,boolean newsletter)
    {
        Customer customer = new Customer(email);
        customer.setRewardPoints(BigDecimal.valueOf(rewardPoints));
        customer.setNewsletter(newsletter);
        return customerRepository.save(customer);
    }

}