package com.ashago.mainapp.req;

import lombok.Data;

@Data
public class SubmitServiceReq {
   private String serviceId;
   private String name;
   private String email;
   private String mobile;
   private String msg;
   private String whenAndWhere;
}
