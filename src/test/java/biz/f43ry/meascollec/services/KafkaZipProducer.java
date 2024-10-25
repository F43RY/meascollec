package biz.f43ry.meascollec.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;

import jakarta.annotation.PreDestroy;

@SpringBootTest
public class KafkaZipProducer {

    @Value("classpath:xml/myItem_statsfile.gz")
    private Resource resource;
    
    @Value("${kafka.topics.name}")
    private String topic;
    
    @Autowired
    Properties props;
    
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Test
    void sendMessage() {
    	
    	byte[] content = null;
		try {
			content = Files.readAllBytes(resource.getFile().toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (content != null) {
			kafkaTemplate.send(Arrays.asList(topic.split(",")).get(0).toString(), resource.getFilename(), content);
		}else {
			assertTrue(false);
		}
    }
    
    @AfterAll
    static void cleanup() {
    	try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}