package biz.f43ry.meascollec.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Document(collection = "counters")
public class Counter {
	@Id
	private String id;
	private Hierarchy parent;
	private CounterType type = null;
	
	
	private enum CounterType{
		incremental,
		vector,
		numeric,
		gauge
	}
}
