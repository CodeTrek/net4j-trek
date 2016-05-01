package org.code.trek.net4j.r2.eb.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.code.trek.net4j.r2.eb.ServletResponse;

public class EbClientRequest implements ServletResponse {
    private byte[] response = null;
    private final CountDownLatch latch = new CountDownLatch(1);
    private final byte[] payLoad;

    public EbClientRequest(byte[] payLoad) {
        this.payLoad = payLoad;
    }

    public byte[] getPayLoad() {
        return payLoad;
    }

    public byte[] getReponse(long timeoutMS) {
        try {
            latch.await(timeoutMS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void setResponse(byte[] response) {
        this.response = response;
        latch.countDown();
    }
}
