package com.amigoscode.customer;

import com.amigoscode.amqp.RabbitMQMessageProducer;
import com.amigoscode.clients.fraud.FraudCheckResponse;
import com.amigoscode.clients.fraud.FraudClient;
import com.amigoscode.clients.notification.NotificationClient;
import com.amigoscode.clients.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        // todo: check if email id valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);
        // todo: check if fraudster
      //  FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
             //   "http://FRAUD/api/v1/fraud-check/{customerId}",
              //  FraudCheckResponse.class,
              //  customer.getId()
      //  );
        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());
        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }
        customerRepository.save(customer);
        NotificationRequest notificationRequest =  new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s , welocme to Amigoscode...",
                        customer.getFirstName())
        );
        //make it async i.e add it to queue
       rabbitMQMessageProducer.publish(
               notificationRequest,
               "internal.exchange",
               "internal.notification.routing-key"
       );
    }
}
