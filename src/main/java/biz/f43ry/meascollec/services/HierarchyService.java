package biz.f43ry.meascollec.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import biz.f43ry.meascollec.model.Hierarchy;
import biz.f43ry.meascollec.repositories.HierarchyRepo;
import biz.f43ry.meascollec.utils.StringUtils;

@Service
public class HierarchyService {

	@Autowired HierarchyRepo hierarchyRepository;
	
    public Optional<Hierarchy> findByXPath(String measObjLdn) {
        String xPath = StringUtils.extractXPath(measObjLdn);
        Optional<Hierarchy> existingHierarchy = hierarchyRepository.findByXPath(StringUtils.extractXPath(xPath));
        return existingHierarchy;
    }
    
    public Hierarchy save(Hierarchy hierarchy) {
    	return hierarchyRepository.save(hierarchy);
    }
    
}
