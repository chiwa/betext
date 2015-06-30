package com.betext.usermanagement.services.authentication;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceLoggingAspect {

    public static final String REQUEST_TIME = "request_time";
    public static final String UNCHECKED = "unchecked";
    public static final String ACCESS_TIME = "[AccessTime] - ";
    public static final String INVALID_PARAMS_FOR_ASPECT_LOGGING = " invalid params for aspect logging";
    public static final String POINT = "point ";
    public static final String USING_TIME = " using time : ";
    public static final String SECS = " secs.";
    private final Logger log = LogManager.getLogger(ServiceLoggingAspect.class);

    public void logBefore(JoinPoint joinPoint) {
        String declareTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String pointName = (declareTypeName + "." + joinPoint.getSignature().getName());
        Object[] pointParams = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<String, Object>();
        Date currentTime = new Date(System.currentTimeMillis());
        BigDecimal startCall = new BigDecimal(currentTime.getTime());
        params.put(REQUEST_TIME, startCall);
        pointParams[0] = params;
    }

    @SuppressWarnings(UNCHECKED)
    public void logAfter(JoinPoint joinPoint) {
       // String declareTypeName = joinPoint.getSignature().getDeclaringTypeName();
       // String pointName = (declareTypeName + "." + joinPoint.getSignature().getName());
        String pointName = joinPoint.getSignature().getName();
        Object[] pointParams = joinPoint.getArgs();
        if (pointParams != null && pointParams.length > 0) {
            Object obj = pointParams[0];
            if (obj != null && (java.util.HashMap.class.equals(obj.getClass()))) {
                Map<String, Object> params = (Map<String, Object>) joinPoint.getArgs()[0];
                BigDecimal startCall = (BigDecimal) params.get(REQUEST_TIME);
                Date currentTime = new Date(System.currentTimeMillis());
                BigDecimal afterCall = new BigDecimal(currentTime.getTime());
                BigDecimal totalTime = (afterCall.subtract(startCall)).divide(new BigDecimal(1000));
                String accessTimeText = ACCESS_TIME + pointName + USING_TIME + totalTime.toString() + SECS;
                log.info(accessTimeText);
            } else {
                System.out.println(POINT + pointName + INVALID_PARAMS_FOR_ASPECT_LOGGING);
            }
        } else {
            System.out.println(POINT + pointName + INVALID_PARAMS_FOR_ASPECT_LOGGING);
        }
    }
}