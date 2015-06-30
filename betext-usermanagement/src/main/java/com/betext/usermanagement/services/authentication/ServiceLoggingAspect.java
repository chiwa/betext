package com.betext.usermanagement.services.authentication;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceLoggingAspect {

    private final Logger log = LogManager.getLogger(ServiceLoggingAspect.class);

    public void logBefore(JoinPoint joinPoint) {
        // log.info("### before call DAO ###");
        String declareTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String pointName = (declareTypeName + "." + joinPoint.getSignature().getName());
        Object[] pointParams = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<String, Object>();
        Date currentTime = new Date(System.currentTimeMillis());
        BigDecimal startCall = new BigDecimal(currentTime.getTime());
        params.put("request_time", startCall);
        pointParams[0] = params;
    }

    @SuppressWarnings("unchecked")
    public void logAfter(JoinPoint joinPoint) {
        // log.info("### after call DAO ###");
        String declareTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String pointName = (declareTypeName + "." + joinPoint.getSignature().getName());
        //String pointName = joinPoint.getSignature().getName();
        Object[] pointParams = joinPoint.getArgs();
        if (pointParams != null && pointParams.length > 0) {
            Object obj = pointParams[0];
            //log.info("absolute class : "+obj.getClass());
            if (obj != null && (java.util.HashMap.class.equals(obj.getClass()))) {
                Map<String, Object> params = (Map<String, Object>) joinPoint.getArgs()[0];
                BigDecimal startCall = (BigDecimal) params.get("request_time");
                Date currentTime = new Date(System.currentTimeMillis());
                BigDecimal afterCall = new BigDecimal(currentTime.getTime());
                //log.info(" after call "+pointName+" at time : "+SDF.format(currentTime) + " : "+afterCall.toString());

                BigDecimal totalTime = (afterCall.subtract(startCall)).divide(new BigDecimal(1000));
                String accessTimeText = "[AccessTime] - " + pointName + " using time : " + totalTime.toString() + " secs.";
                //System.out.println(accessTimeText);
                log.info(accessTimeText);
            } else {
                System.out.println("point " + pointName + " invalid params for aspect loggging");
            }
        } else {
            System.out.println("point " + pointName + " invalid params for aspect logging");
        }
    }
}