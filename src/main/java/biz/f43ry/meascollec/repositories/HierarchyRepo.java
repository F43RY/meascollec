package biz.f43ry.meascollec.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import biz.f43ry.meascollec.model.Hierarchy;
import biz.f43ry.meascollec.xml.generated.v32435.MeasInfo;

public interface HierarchyRepo extends MongoRepository<Hierarchy, String>{

    @Query("{name:'?0'}")
    Hierarchy findItemByName(String name);
    
    @Query("{xpath: ?0}")
    Optional<Hierarchy> findByXPath(String xPath);
}
