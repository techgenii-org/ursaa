package com.example.ursaa.controller;

import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.WriteReactiveApi;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InfluxDbService {

    @Autowired
    private InfluxDBClientReactive influxDBClientReactive;


    public void writeData(Temperature temperature) {
        WriteReactiveApi writeReactiveApi = influxDBClientReactive.getWriteReactiveApi();
//        Flowable<Temperature> measurements = Flowable.interval(10, TimeUnit.MILLISECONDS)
//                .map(time -> {
//                    Temperature temperature = new Temperature();
//                    temperature.location = getLocation();
//                    temperature.value = getValue();
//                    temperature.time = Instant.now();
//                    return temperature;
//                });


        Publisher<WriteReactiveApi.Success> publisher = writeReactiveApi.writeMeasurements(WritePrecision.NS, Flowable.fromCallable(() -> temperature));

        Disposable subscriber = Flowable.fromPublisher(publisher)
                .doOnError(throwable -> {
                    log.error("Something went wrong ; {}",throwable.getMessage());
                }).subscribe(success -> {
                    log.info("Successfully written measurements");
                });

    }
}
