package com.betext.usermanagement.services.authentication;

import org.aspectj.lang.JoinPoint;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class ServiceLoggingAspect  {

    public void logBefore(JoinPoint joinPoint)
    {
        // log.info("### before call DAO ###");
        String declareTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String pointName = (declareTypeName + "." + joinPoint.getSignature().getName());
        Object[] pointParams = joinPoint.getArgs();
        if(pointParams != null && pointParams.length > 0)
        {
            Object obj = pointParams[0];
            //log.info("absolute class : "+obj.getClass());
            if(obj != null && (java.util.HashMap.class.equals(obj.getClass())))
            {
                Map<String,Object> params = (Map<String,Object>)obj;
                Date currentTime = new Date(System.currentTimeMillis());
                BigDecimal startCall = new BigDecimal(currentTime.getTime());
                //log.info(" call "+pointName+" at time : "+SDF.format(currentTime) + " : "+startCall.toString());
                params.put("request_time", startCall);
            }else
            {
                System.out.println("point "+pointName+" invalid params for loggging");
            }
        }else
        {
            System.out.println("point "+pointName+" invalid params for aspect logging");
        }
    }

    @SuppressWarnings("unchecked")
    public void logAfter(JoinPoint joinPoint)
    {
        // log.info("### after call DAO ###");
        String declareTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String pointName = (declareTypeName + "." + joinPoint.getSignature().getName());
        Object[] pointParams = joinPoint.getArgs();
        if(pointParams != null && pointParams.length > 0)
        {
            Object obj = pointParams[0];
            //log.info("absolute class : "+obj.getClass());
            if(obj != null && (java.util.HashMap.class.equals(obj.getClass())))
            {
                Map<String,Object> params = (Map<String,Object>)joinPoint.getArgs()[0];
                BigDecimal startCall = (BigDecimal)params.get("request_time");
                Date currentTime = new Date(System.currentTimeMillis());
                BigDecimal afterCall = new BigDecimal(currentTime.getTime());
                //log.info(" after call "+pointName+" at time : "+SDF.format(currentTime) + " : "+afterCall.toString());

                BigDecimal totalTime = (afterCall.subtract(startCall)).divide(new BigDecimal(1000));
                System.out.println("total call "+pointName + " used time : "+totalTime.toString() + " secs.");
            }else
            {
                System.out.println("point "+pointName+" invalid params for aspect loggging");
            }
        }else
        {
            System.out.println("point "+pointName+" invalid params for aspect logging");
        }
    }
}