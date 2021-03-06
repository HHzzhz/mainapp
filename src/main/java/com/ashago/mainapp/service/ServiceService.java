package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Service;
import com.ashago.mainapp.repository.ServiceRepository;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.ServiceFilter;
import com.ashago.mainapp.util.ServiceSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public CommonResp getServices(ServiceFilter serviceFilter, ServiceSorter serviceSorter) {
        Example<Service> serviceExample = Example.of(Service.builder()
                .category(serviceFilter.getCategory())
                .location(serviceFilter.getLocation())
                .build()
        );

        List<Service> serviceList = serviceRepository.findAll(serviceExample, Sort.by(Sort.Direction.DESC, "priority"));

        return CommonResp.success().appendData("serviceList", serviceList);
    }

    public CommonResp getServiceDetail(String serviceId) {
        Example<Service> serviceExample = Example.of(Service.builder().serviceId(serviceId).build());
        Optional<Service> serviceOptional = serviceRepository.findOne(serviceExample);
        if (serviceOptional.isPresent()) {
            return CommonResp.success().appendData("serviceDetail", serviceOptional.get());
        } else {
            return CommonResp.create("E023", "Service Not Found");
        }
    }
}
