package springboot.service;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import springboot.entity.Destination;
import springboot.serviceImp.DestinationServiceImp;

import javax.annotation.Resource;

@Service
public class DestinationService {
    @Resource
    public DestinationServiceImp destinationServiceImp;

    public int insertDestination(Destination destination) {
        return destinationServiceImp.insertDestination(destination);
    }
}
