package com.ashago.mainapp.controller;

import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.ServiceService;
import com.ashago.mainapp.util.ServiceFilter;
import com.ashago.mainapp.util.ServiceSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping("/service/list")
    public CommonResp getServiceList(String location, String category) {
        ServiceFilter serviceFilter = ServiceFilter.builder()
                .location(location)
                .category(category)
                .build();
        ServiceSorter serviceSorter = ServiceSorter.builder()
                .priority(ServiceSorter.Sort.DESC).build();
        return serviceService.getServices(serviceFilter, serviceSorter);
    }

    @GetMapping("/service/detail")
    public CommonResp getServiceDetail(@RequestParam String serviceId) {
        return serviceService.getServiceDetail(serviceId);
    }


}
