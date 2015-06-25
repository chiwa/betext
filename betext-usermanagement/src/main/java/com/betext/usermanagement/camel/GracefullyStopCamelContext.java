package com.betext.usermanagement.camel;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;

/**
 * <p>
 *     Stop camel context, the thread will blocked unit Camel Context gracefully shutdown or timeout occurred
 *     (5mins, set by Camel Context)
 * </p>
 *
 * <p> Implement the SmartLifecycle and implement the PHASE to Integer.MAX_VALUE, to ensure that this bean going to get
 * disposed first </p>
 *
 * <p> According to this document </p>
 *
 * <p> When starting, the objects with the lowest phase start first, and when stopping, the reverse computer is followed.
 * Therefore, an object that implements SmartLifecycle and whose getPhase() method returns Integer.MIN_VALUE would be
 * among the first to start and the last to stop. At the other end of the spectrum, a phase value of Integer.MAX_VALUE
 * would indicate that the object should be started last and stopped first (likely because it depends on other processes
 * to be running). When considering the phase value, it's also important to know that the default phase for any "normal"
 * Lifecycle object that does not implement SmartLifecycle would be 0. Therefore, any negative phase value would
 * indicate that an object should start before those standard components (and stop after them), and vice versa for any
 * positive phase value. </p>
 */
public class GracefullyStopCamelContext implements SmartLifecycle {

    private static final Logger log = LoggerFactory.getLogger(GracefullyStopCamelContext.class);

    @Autowired
    private CamelContext camel;

    private boolean isRunning = false;

    @Override
    public void start() {
        isRunning = true;
    }

    @Override
    public void stop() {
        stopCamel();
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        try {
            stopCamel();
            isRunning = false;
        } finally {
            callback.run();
        }
    }

    @Override
    public int getPhase() {
        // ensure the first bean to be disposed (just over the default '0' phase)
        return 1;
    }

    /**
     * If cannot stopped, then the IllegalStateException will be thrown
     */
    private void stopCamel() {
        try {
            log.info("Stopping camel context");

            long startTime = System.currentTimeMillis();
            camel.stop();
            long timeUsed = System.currentTimeMillis() - startTime;

            log.info("Successful stopped, in (ms) " + timeUsed);
        } catch (Exception e) {
            log.error("Cannot stop camel context, due to " + e.getMessage());
            throw new IllegalStateException("Cannot stop camel context, due to " + e.getMessage(), e);
        }
    }
}
