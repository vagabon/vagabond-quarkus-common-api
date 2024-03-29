package org.vagabond.common.kafka.engine;

public interface ICronComponent {
    static final int SECONDES_ADD_TO_CHECK = 30;

    void runCron();
}
