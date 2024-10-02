package biz.f43ry.meascollec.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class KafkaZipProducer {
	
	@Test
    void produceMessagemain() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props);
        
        // Leggi il file .zip
        byte[] fileData = null;
		try {
			fileData = Files.readAllBytes(Paths.get("/home/fabry/git/meascollec/schemas/xml/A20190404.1545+0200-1600+0200_SubNetwork=LTE_tokomc_R,MeContext=TO63T,ManagedElement=TO63T_statsfile.zip"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Invia il file .zip a Kafka
        ProducerRecord<String, byte[]> record = new ProducerRecord<>("myTopic", fileData);
        producer.send(record);
        producer.close();
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}