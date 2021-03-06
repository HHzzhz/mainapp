package com.ashago.mainapp.service;

import com.ashago.mainapp.domain.Service;
import com.ashago.mainapp.domain.ServiceSubmitRecord;
import com.ashago.mainapp.esrepository.EsServiceRepository;
import com.ashago.mainapp.repository.ServiceRepository;
import com.ashago.mainapp.repository.ServiceSubmitRecordRepository;
import com.ashago.mainapp.req.AppendServiceReq;
import com.ashago.mainapp.req.SubmitServiceReq;
import com.ashago.mainapp.resp.CommonResp;
import com.ashago.mainapp.util.ServiceFilter;
import com.ashago.mainapp.util.ServiceSorter;
import com.ashago.mainapp.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import com.ashago.mainapp.domain.EsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    private SnowFlake snowFlake = new SnowFlake(0, 0);

    @Autowired
    private MailService mailService;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceSubmitRecordRepository serviceSubmitRecordRepository;
    @Autowired
    private EsServiceRepository esServiceRepository;

    public CommonResp getServices(ServiceFilter serviceFilter, ServiceSorter serviceSorter) {
        Example<Service> serviceExample = Example.of(Service.builder()
                .category(serviceFilter.getCategory())
                .city(serviceFilter.getLocation())
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
        String serviceId = "S" + snowFlake.nextId();
        Service service = Service.builder()
                .serviceId(serviceId)
                .serviceProviderId(appendServiceReq.getServiceProviderId())
                .category(appendServiceReq.getCategory())
                .desc(appendServiceReq.getDesc())
                .detailHtml(appendServiceReq.getDetailHtml())
                .detailPlaintext(appendServiceReq.getDetailPlaintext())
                .image(appendServiceReq.getImage())
                .isOffLineSupport(appendServiceReq.getIsOffLineSupport())
                .isOnlineSupport(appendServiceReq.getIsOnlineSupport())
                .city(appendServiceReq.getCity())
                .price(appendServiceReq.getPrice())
                .title(appendServiceReq.getTitle())
                .priority(appendServiceReq.getPriority() == null ? 0 : appendServiceReq.getPriority())
                .build();
        serviceRepository.saveAndFlush(service);
        EsService esService = EsService.builder().serviceId(serviceId)
                .serviceProviderId(appendServiceReq.getServiceProviderId())
                .category(appendServiceReq.getCategory())
                .desc(appendServiceReq.getDesc())
                .detailPlaintext(appendServiceReq.getDetailPlaintext())
                .image(appendServiceReq.getImage())
                .isOffLineSupport(appendServiceReq.getIsOffLineSupport())
                .isOnlineSupport(appendServiceReq.getIsOnlineSupport())
                .city(appendServiceReq.getCity())
                .price(appendServiceReq.getPrice())
                .priority(appendServiceReq.getPriority())
                .title(appendServiceReq.getTitle()).build();
        esServiceRepository.save(esService);
        return CommonResp.success().appendData("appendedService", service);
    }

    public CommonResp removeService(String serviceId) {
        Optional<Service> serviceOptional = serviceRepository.findOne(Example.of(Service.builder().serviceId(serviceId).build()));
        serviceOptional.ifPresent(service -> serviceRepository.deleteById(service.getId()));
        List<EsService> esServiceList = esServiceRepository.findByServiceId(serviceId);
        esServiceList.forEach(esService -> {
            esServiceRepository.deleteById(esService.getId());
        });
        return CommonResp.success();
    }

    public CommonResp submitService(SubmitServiceReq submitServiceReq) {
        Optional<Service> serviceOptional = serviceRepository.findOne(Example.of(Service.builder().serviceId(submitServiceReq.getServiceId()).build()));
        if (!serviceOptional.isPresent()) {
            return CommonResp.create("E024", "service not found");
        }


        ServiceSubmitRecord serviceSubmitRecord = ServiceSubmitRecord.builder()
                .serviceId(submitServiceReq.getServiceId())
                .submitAt(LocalDateTime.now())
                .email(submitServiceReq.getEmail())
                .mobile(submitServiceReq.getMobile())
                .msg(submitServiceReq.getMsg())
                .notified(Boolean.FALSE)
                .whenAndWhere(submitServiceReq.getWhenAndWhere())
                .name(submitServiceReq.getName())
                .build();
        serviceSubmitRecordRepository.saveAndFlush(serviceSubmitRecord);


        String mailText = "service:" + serviceOptional.get().getTitle() + "\r\n" +
                "whenAndWhere:" + submitServiceReq.getWhenAndWhere() + "\r\n" +
                "name:" +submitServiceReq.getName() + "\r\n" +
                "email:" + submitServiceReq.getEmail() + "\r\n" +
                "mobile:" + submitServiceReq.getMobile() + "\r\n" +
                "msg:" + submitServiceReq.getMsg();

        mailService.sendSimpleTextMail("zixuan@ashago.com", "service touched.", mailText);
        mailService.sendSimpleTextMail("huazheng@ashago.com", "service touched.", mailText);
        mailService.sendSimpleTextMail("jemma@ashago.com", "service touched.", mailText);


        return CommonResp.success();
    }
}
