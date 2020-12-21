package com.vinod.saga.choreography.customer.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Configuration
@ComponentScan(basePackages = {"com.vinod.saga.choreography.customer"})
@EnableJms
public class AWSJMSConfiguration {

    private Environment environment;

    public AWSJMSConfiguration(Environment environment) {
        this.environment=environment;
    }

    private AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(environment.getProperty("ACCESS_KEY"), environment.getProperty("SECRET_KEY"));
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        SQSConnectionFactory sqsConnectionFactory = new SQSConnectionFactory(new ProviderConfiguration().withNumberOfMessagesToPrefetch(10), AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials())).withRegion(Regions.AP_SOUTH_1).build());
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConnectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        SQSConnectionFactory sqsConnectionFactory = new SQSConnectionFactory(new ProviderConfiguration().withNumberOfMessagesToPrefetch(10), AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials())).withRegion(Regions.AP_SOUTH_1).build());
        return new JmsTemplate(sqsConnectionFactory);
    }

    @Bean
    public AmazonSNSClient snsClient() {
        return (AmazonSNSClient) AmazonSNSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials())).withRegion(Regions.AP_SOUTH_1).build();
    }
}
