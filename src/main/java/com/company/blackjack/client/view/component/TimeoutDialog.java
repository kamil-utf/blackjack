package com.company.blackjack.client.view.component;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Dialog;
import javafx.util.Duration;

public abstract class TimeoutDialog<T> extends Dialog<T> {

    private final Timeline timeline;
    private final IntegerProperty timerProperty;

    public TimeoutDialog(int timeout) {
        this.timerProperty = new SimpleIntegerProperty(timeout);

        this.timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(timeout),
                        e -> close(),
                        new KeyValue(timerProperty, 0)
                )
        );
        this.timeline.setCycleCount(1);
    }

    protected void startTimer() {
        timeline.play();
    }

    protected IntegerProperty getTimerProperty() {
        return timerProperty;
    }

}
