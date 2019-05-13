package com.client_b.demo;

import com.client_b.demo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ServiceBController {

    private final Logger logger=  LoggerFactory.getLogger(ServiceBController.class);
    @Autowired
    private Registration registration; // 服务注册
    @Autowired
    private DiscoveryClient client; //注入发现客户端

    @Value("${server.port}")
    String port;
    @RequestMapping("/hi")
    public String home(@RequestParam String id) {
        return "hi "+id+",i am from port:" +port;
    }

    /**
     * parameter test
     */
    @GetMapping(value = "/greet/{dd}")
    public String greet(@PathVariable String dd){
        logger.info("/hello port:"+port);
        return "hello "+dd;
    }

    /**
     * 返回测试对象
     */
    @GetMapping("/user")
    public User getUser(){
        ServiceInstance serviceInstance = serviceInstance();
        logger.info("/user "+serviceInstance.getHost()+" port:"+serviceInstance.getPort()+" serviceInstanceid:"+serviceInstance.getServiceId());
        return new User("hell","female", "123456789");
    }

    /**
     * 根据名称返回对象，这里模拟查数据库操作
     */
    @GetMapping("/user/{name}")
    public User getUserSelect(@PathVariable String name){
        ServiceInstance serviceInstance = serviceInstance();
        logger.info("/user "+serviceInstance.getHost()+" port:"+serviceInstance.getPort()+" serviceInstanceid:"+serviceInstance.getServiceId());
        if(name.isEmpty()){
            return new User();
        }else if(name.equals("hell")){
            return new User("hell","female", "123456789");
        }else{
            return new User("随机用户","male", "987654321");
        }
    }

    public ServiceInstance serviceInstance() {
        List<ServiceInstance> list = client.getInstances(registration.getServiceId());
        if (list != null && list.size() > 0) {
            for(ServiceInstance itm : list){
                if(itm.getPort() == 10003)
                    return itm;
            }
        }
        return null;
    }
}
