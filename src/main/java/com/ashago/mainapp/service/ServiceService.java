package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Service;
import com.ashago.mainapp.repository.ServiceRepository;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.ServiceFilter;
import com.ashago.mainapp.util.ServiceSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public CommonResp getServices(ServiceFilter serviceFilter, ServiceSorter serviceSorter) {
        return null;
    }

    public CommonResp getServiceDetail(String serviceId) {
        Example<Service> serviceExample = Example.of(Service.builder().serviceId(serviceId).build());
        Optional<Service> serviceOptional = serviceRepository.findOne(serviceExample);
        if (serviceOptional.isPresent()) {
            return CommonResp.success().appendData("serviceDetail", serviceOptional.get());
        } else {
            return CommonResp.create("E023", "服务不存在");
        }
    }
}
