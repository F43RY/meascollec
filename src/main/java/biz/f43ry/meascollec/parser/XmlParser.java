package biz.f43ry.meascollec.parser;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import biz.f43ry.meascollec.model.Hierarchy;
import biz.f43ry.meascollec.model.Hierarchy.NodeType;
import biz.f43ry.meascollec.repositories.HierarchyRepo;
import biz.f43ry.meascollec.services.HierarchyService;
import biz.f43ry.meascollec.utils.StringUtils;
import biz.f43ry.meascollec.utils.ZipUtils;
import biz.f43ry.meascollec.xml.generated.v32435.MeasCollecFile;
import biz.f43ry.meascollec.xml.generated.v32435.MeasInfo;
import biz.f43ry.meascollec.xml.generated.v32435.MeasType;
import biz.f43ry.meascollec.xml.generated.v32435.MeasValue;
import biz.f43ry.meascollec.xml.generated.v32435.R;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class XmlParser {

	@Autowired
	private HierarchyService hierarchyService;
	
    public void processMessage(byte[] xmlData) {
        try {

            MeasCollecFile measFile = unmarshalXml(xmlData);
            processData(measFile);
            // Fai qualcosa con l'oggetto elaborato
            log.info("Processed vendor: " + measFile.getFileHeader().getVendorName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	private MeasCollecFile unmarshalXml(byte[] fileData) {
		MeasCollecFile measFile = null;
		try {
			// Decomprime il file .zip e ottiene il contenuto XML come stringa
			String xmlContent = ZipUtils.unzipFile(fileData);

			// Crea il contesto JAXB per parsificare l'XML
			JAXBContext jaxbContext = JAXBContext.newInstance(MeasCollecFile.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			// Parsifica il contenuto XML in un oggetto Person
			StringReader reader = new StringReader(xmlContent);
			measFile = (MeasCollecFile) unmarshaller.unmarshal(reader);

			// Stampa l'oggetto parsificato
			log.info("Parsed file: " + measFile.getFileHeader().getFileFormatVersion());
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		return measFile;
	}
	
	private void processData(MeasCollecFile measCollecFile) {
		
	
		List<MeasInfo> measInfos = measCollecFile.getMeasData().get(0).getMeasInfo();
//		Map<String, List<MeasInfo>> mi = measInfos.parallelStream().collect(Collectors.groupingBy(MeasInfo::getMeasInfoId));
//		mi.keySet().forEach(System.out::println);
		
		for (MeasInfo measInfo: measInfos) {
			
			Map<BigInteger, String> contatori = measInfo.getMeasType().stream().collect(Collectors.toMap(MeasType::getP, MeasType::getValue));
//			contatori.entrySet().forEach(System.out::println);
			Map<BigInteger, String> valori = new HashMap<BigInteger,String>();
			for(MeasValue measValue: measInfo.getMeasValue()) {
				valori = measValue.getR().stream().collect(Collectors.toMap(R::getP, R::getValue));
//				valori.entrySet().forEach(System.out::println);
			}
			Map<String,String> kv = new HashMap<String,String>();
			for (Entry<BigInteger, String> contatore: contatori.entrySet()) {
				Optional<Entry<BigInteger,String>> valore = valori.entrySet().stream().filter(e->e.getKey()==contatore.getKey()).findFirst();
				if(valore.isPresent()) {
					kv.put(contatore.getValue(), valore.get().getValue());
				}else {
					kv.put(contatore.getValue(), null);
				}
			}
			Optional<Hierarchy> hierarchy = hierarchyService.findByXPath(measInfo.getMeasValue().get(0).getMeasObjLdn());
			if(!hierarchy.isPresent()) {
				log.debug("Gerarchia non trovata, la creo");
				Hierarchy h = Hierarchy.builder()
						.enabled(false)
						.measInfoId(measInfo.getMeasInfoId())
						.measTypes(contatori.values().stream().toList())
						.xPath(StringUtils.extractXPath(measInfo.getMeasValue().get(0).getMeasObjLdn()))
						.nodeType(NodeType.ENODEB_V2)
						.build();
				hierarchyService.save(h);
			}
//			kv.entrySet().forEach(System.out::println);
		};
		
		

	}
	
}
