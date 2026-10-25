package org.vagabond.engine.cron.component;

public interface ICronComponent {
    static final int SECONDES_ADD_TO_CHECK = 30;

    void runCron();
}
