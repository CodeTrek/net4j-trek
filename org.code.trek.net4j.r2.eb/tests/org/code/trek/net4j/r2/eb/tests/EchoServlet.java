package org.code.trek.net4j.r2.eb.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.code.trek.net4j.r2.eb.server.EbServlet;
import org.code.trek.net4j.r2.eb.server.EbServletRequest;
import org.code.trek.net4j.r2.eb.server.EbServletResponse;

public class EchoServlet extends EbServlet {

    @Override
    protected void doRequest(EbServletRequest request, EbServletResponse response) throws IOException {
        InputStream in = request.getInputStream();
        OutputStream out = response.getOutputStream();

        while (true) {
            int nextByte = in.read();
            if (nextByte == -1) {
                break;
            }
            out.write(nextByte);
        }

        out.flush();
    }
}
