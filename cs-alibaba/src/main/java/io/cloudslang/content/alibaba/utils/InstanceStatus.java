package io.cloudslang.content.alibaba.utils;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;

import java.util.List;

public class InstanceStatus {
    public static String getInstanceStatus(String region,IAcsClient client, String instanceId) throws Exception{
        String status=null;

        DescribeInstanceStatusRequest describeInstanceStatusRequest=new DescribeInstanceStatusRequest();

        describeInstanceStatusRequest.setRegionId("us-west-1");
        DescribeInstanceStatusResponse response = client.getAcsResponse(describeInstanceStatusRequest);
        System.out.println(" getInstanceStatus response : "+response.toString());
        List<DescribeInstanceStatusResponse.InstanceStatus> list=response.getInstanceStatuses();
        System.out.println("size : "+list.size());
        for(DescribeInstanceStatusResponse.InstanceStatus instanceStatus:list){
            System.out.println(instanceStatus.getInstanceId() +" : " +instanceStatus.getStatus());
            if(instanceStatus.getInstanceId().equalsIgnoreCase(instanceId)){
                System.out.println("Same");
                status=instanceStatus.getStatus().toString();
            }

        }
        return status;
    }
}
