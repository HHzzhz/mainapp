package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Service;
import com.ashago.mainapp.repository.ServiceRepository;
import com.ashago.mainapp.req.AppendServiceReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.ServiceFilter;
import com.ashago.mainapp.util.ServiceSorter;
import com.ashago.mainapp.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    private SnowFlake snowFlake = new SnowFlake(0,0);

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

    public CommonResp appendService(AppendServiceReq appendServiceReq) {
        Service service = Service.builder()
                .serviceId("S" + snowFlake.nextId())
                .serviceProviderId(appendServiceReq.getServiceProviderId())
                .category(appendServiceReq.getCategory())
                .desc(appendServiceReq.getDesc())
                .detailHtml(appendServiceReq.getDetailHtml())
                .detailPlaintext(appendServiceReq.getDetailPlaintext())
                .image(appendServiceReq.getImage())
                .isOffLineSupport(appendServiceReq.getIsOffLineSupport())
                .isOnlineSupport(appendServiceReq.getIsOnlineSupport())
                .location(appendServiceReq.getLocation())
                .price(appendServiceReq.getPrice())
                .title(appendServiceReq.getTitle())
                .priority(appendServiceReq.getPriority() == null?0:appendServiceReq.getPriority())
                .build();
        serviceRepository.saveAndFlush(service);
        return CommonResp.success().appendData("appendedService", service);
    }

    public CommonResp removeService(String serviceId) {
        Optional<Service> serviceOptional = serviceRepository.findOne(Example.of(Service.builder().serviceId(serviceId).build()));
        serviceOptional.ifPresent(service -> serviceRepository.deleteById(service.getId()));
        return CommonResp.success();
    }
}
