package org.vagabond.common.kafka.engine;

public interface ICronComponent {
    static final int MINUTE_ADD_TO_CHECK = 5;

    void runCron();
}
