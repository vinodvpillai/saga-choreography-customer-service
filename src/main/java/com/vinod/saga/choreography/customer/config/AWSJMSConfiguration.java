package com.vinod.saga.choreography.customer.config;

import javax.jms.Session;

import com.amazonaws.regions.Regions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
@ComponentScan(basePackages = {"com.vinod.saga.choreography.customer"})
@EnableJms
public class AWSJMSConfiguration {

    private String accessKey="accessKey";
    private String secretKey="secretKey";

    private AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey); }

    SQSConnectionFactory sqsConnectionFactory = new SQSConnectionFactory(new ProviderConfiguration().withNumberOfMessagesToPrefetch(10), AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials())).withRegion(Regions.AP_SOUTH_1).build());

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(this.sqsConnectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(this.sqsConnectionFactory);
    }

    @Bean
    public AmazonSNSClient snsClient() {
        return (AmazonSNSClient) AmazonSNSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials())).withRegion(Regions.AP_SOUTH_1).build();
    }
}
