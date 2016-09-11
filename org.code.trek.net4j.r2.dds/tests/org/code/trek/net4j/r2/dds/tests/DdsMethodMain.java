package org.code.trek.net4j.r2.dds.tests;

import java.io.IOException;
import java.io.InputStream;

import org.code.trek.net4j.r2.dds.client.impl.DdsClient;
import org.code.trek.net4j.r2.dds.client.impl.DdsMethod;

public class DdsMethodMain {

    public static void main(String[] args) throws IOException, InterruptedException {

        DdsClient client = new DdsClient("1:/");
        client.activate();

        byte[] payload = newPayload(256);

        for (int i = 0; i < 10000; ++i) {
            Thread.sleep(5);

            DdsMethod method = (DdsMethod) client.newMethod("1:/");
            // method.setPayload(newPayload(256));
            method.setPayload(payload);
            method.execute();
            InputStream response = method.getResponse(1000);
            if (response == null) {
                System.out.println("null response");
                continue;
            }
            if ((i % 100) == 0) {
                System.out.println(i + " : " + response.available());
            }
        }

        client.deactivate();
    }

    private static byte[] newPayload(int size) {
        byte[] payload = new byte[size];
        for (int i = 0; i < size; ++i) {
            payload[i] = (byte) i;
        }
        return payload;
    }
}
