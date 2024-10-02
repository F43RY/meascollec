package biz.f43ry.meascollec.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import biz.f43ry.meascollec.repositories.HierarchyRepo;
import biz.f43ry.meascollec.utils.ZipUtils;
import biz.f43ry.meascollec.xml.generated.v32435.MeasCollecFile;
import biz.f43ry.meascollec.xml.generated.v32435.MeasInfo;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class XmlParser {

	@Autowired
	private HierarchyRepo hierarchyRepo;
	
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
	
	private void processData(MeasCollecFile measFile) {
		Map<String, List<MeasInfo>> mi = measFile.getMeasData().get(0).getMeasInfo().parallelStream().collect(Collectors.groupingBy(MeasInfo::getMeasInfoId));
		mi.keySet().forEach(System.out::println);
	}
	
}
