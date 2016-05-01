package org.code.trek.net4j.r2.eb.client;

import org.code.trek.net4j.r2.client.R2Client;
import org.code.trek.net4j.r2.client.R2Method;

public class EbClient implements R2Client {

    @Override
    public int execute(R2Method method) {
        return method.execute();
    }
}
