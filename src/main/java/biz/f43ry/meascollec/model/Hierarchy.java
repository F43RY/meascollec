package biz.f43ry.meascollec.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document("hierarchies")
public class Hierarchy {

	@Id
	private String id;
	private String name;
	private boolean enabled;
	private String xPath;
	

}
