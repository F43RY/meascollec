package biz.f43ry.meascollec.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import biz.f43ry.meascollec.model.Hierarchy;

public interface HierarchyRepo extends MongoRepository<Hierarchy, String>{

    @Query("{name:'?0'}")
    Hierarchy findItemByName(String name);
}
