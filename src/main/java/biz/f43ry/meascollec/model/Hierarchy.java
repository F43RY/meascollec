package biz.f43ry.meascollec.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "hierarchies")
@CompoundIndexes({
    @CompoundIndex(name = "xPath_nodeType_idx", def = "{'xPath': 1, 'nodeType': 1}", unique = true)
})
public class Hierarchy {

	@Id
	private String id;
	private String measInfoId;
	private String measInfoName;
	private String collectionName;
	private boolean enabled;
	private String xPath;
	private NodeType nodeType;
	private List<String> measTypes = new ArrayList<String>();
	
	public enum NodeType {
		BSC,
		RNC,
		ENODEB_V1,
		ENODEB_V2,
		GNODEB
	}

}
