package ru.makhach.springunzipper.models;

import java.util.concurrent.Callable;

public interface CancelledTask<V> extends Callable<V> {
    boolean cancel();
}
