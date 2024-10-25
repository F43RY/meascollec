package biz.f43ry.meascollec.services;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import biz.f43ry.meascollec.parser.XmlParser;
import biz.f43ry.meascollec.utils.ZipUtils;
import biz.f43ry.meascollec.xml.generated.v32435.MeasCollecFile;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;

@Slf4j
@Service
public class KafkaConsumerService {
	//
	//    @Value("${kafka.topic.name}")
	//    private String topicName;
	//    
	//    @Value("${kafka.consumer-group-id}")
	//    private String groupId;
	private final XmlParser xmlParser;

	public KafkaConsumerService(XmlParser xmlParser) {
		this.xmlParser = xmlParser;
	}
	
	private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	@KafkaListener(topics = "#{T(java.util.Arrays).asList('${kafka.topics.name}'.split(','))}", groupId = "${kafka.consumer-group-id}")
	public void consume(ConsumerRecord<String, byte[]> consumerRecord) {

		log.info("File elaborato: " + consumerRecord.key());
		log.info("offset: {}", consumerRecord.offset());
		if(consumerRecord.value() == null) {
			log.warn("Value vuoto");
		}else {
			executorService.submit(()-> xmlParser.processMessage(consumerRecord.value()));
		}
	}
}
