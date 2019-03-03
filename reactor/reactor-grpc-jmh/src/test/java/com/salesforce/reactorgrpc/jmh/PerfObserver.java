/*
 *  Copyright (c) 2019, Salesforce.com, Inc.
 *  All rights reserved.
 *  Licensed under the BSD 3-Clause license.
 *  For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.reactorgrpc.jmh;

import java.util.concurrent.CountDownLatch;

import com.salesforce.reactorgrpc.jmh.proto.Messages;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import org.openjdk.jmh.infra.Blackhole;

public class PerfObserver implements ClientResponseObserver<Messages.SimpleRequest, Messages.SimpleResponse> {

    final Blackhole      blackhole;
    final CountDownLatch latch;

    ClientCallStreamObserver observer;

    public PerfObserver(Blackhole blackhole) {
        this.blackhole = blackhole;
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void beforeStart(ClientCallStreamObserver<Messages.SimpleRequest> observer) {
        this.observer = observer;
    }

    @Override
    public void onNext(Messages.SimpleResponse response) {
        blackhole.consume(response);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        blackhole.consume(throwable);
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        latch.countDown();
    }
}
