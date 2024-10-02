package biz.f43ry.meascollec.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Document("counters")
public class Counter {
	@Id
	private String id;
	private Hierarchy parent;
}
