package com.jafir.gps;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReactivexCompat {

    private static ExecutorService sFixedThreadPool = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors() + 1);

    public static <T> MaybeTransformer<T, T> maybeThreadSchedule() {
        return upstream -> upstream.subscribeOn(getThreadScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> singleThreadSchedule() {
        return upstream -> upstream.subscribeOn(getThreadScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer completableThreadSchedule() {
        return upstream -> upstream.subscribeOn(getThreadScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> observableThreadSchedule() {
        return upstream -> upstream.subscribeOn(getThreadScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> FlowableTransformer<T, T> flowableThreadSchedule() {
        return upstream -> upstream.subscribeOn(getThreadScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Scheduler getThreadScheduler() {
        return Schedulers.io();
    }

}
