package com.ashago.mainapp.controller;

import com.ashago.mainapp.req.AppendServiceReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.service.ServiceService;
import com.ashago.mainapp.util.ServiceFilter;
import com.ashago.mainapp.util.ServiceSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/service/append")
    public CommonResp appendService(@RequestBody AppendServiceReq appendServiceReq) {
        return serviceService.appendService(appendServiceReq);
    }

    @DeleteMapping("/service/remove")
    public CommonResp removeService(@RequestParam String serviceId) {
        return serviceService.removeService(serviceId);
    }

}
