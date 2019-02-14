/*  Copyright (c) 2017, salesforce.com, inc.
 *  All rights reserved.
 *  Licensed under the BSD 3-Clause license.
 *  For full license text, see LICENSE.txt file in the repo root  or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.rxgrpc;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.testing.GrpcServerRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractStubTest {
    @Rule
    public GrpcServerRule serverRule = new GrpcServerRule();

    @Rule
    public UnhandledRxJavaErrorRule errorRule = new UnhandledRxJavaErrorRule().autoVerifyNoError();

    @Test
    public void getChannelWorks() {
        ManagedChannel channel = serverRule.getChannel();
        RxGreeterGrpc.RxGreeterStub stub = RxGreeterGrpc.newRxStub(channel);

        assertThat(stub.getChannel()).isEqualTo(channel);
    }

    @Test
    public void settingCallOptionsWorks() {
        ManagedChannel channel = serverRule.getChannel();
        Deadline deadline = Deadline.after(42, TimeUnit.SECONDS);

        RxGreeterGrpc.RxGreeterStub stub = RxGreeterGrpc.newRxStub(channel).withDeadline(deadline);

        assertThat(stub.getCallOptions().getDeadline()).isEqualTo(deadline);
    }
}
