package org.mule.api.security.tls;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.Test;
import org.mule.api.MuleMessage;

import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class RestrictedSSLSocketFactoryTest extends FunctionalTestCase {

    public static final String EXPECTED_ANSWER = "Payload answer";
    public static final int PORT = 19000;

    @Override
    protected String[] getConfigFiles() {
        String[] configFiles = {"test-config.xml"};
        return configFiles;
    }

    @Test
    public void testThatMuleCouldConnectToServerTalkingOnlyTLSv1_1() throws Exception {
        String[] includedProtocols = {"TLSv1.1"};
        Server server = startSSLServer(includedProtocols);

        String answer = callSSLServerWithMule();
        assertEquals(EXPECTED_ANSWER, answer);

        server.stop();
    }

    private String callSSLServerWithMule() throws Exception {
        MuleClient muleClient = new MuleClient(muleContext);
        MuleMessage reply = muleClient.send("vm://in-test", "Message payload", null);
        return reply.getPayloadAsString();
    }

    public Server startSSLServer(String[] includedProtocols) throws Exception {
        Server server = new Server();
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        SslContextFactory sslContextFactory = createSslContextFactory(includedProtocols);
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https));
        sslConnector.setPort(PORT);
        server.setConnectors(new Connector[] { sslConnector });

        server.setHandler(new RequestHandler());
        server.start();


        return server;
    }


    private SslContextFactory createSslContextFactory(String[] includeProtocols) {
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(RestrictedSSLSocketFactoryTest.class.getClassLoader().getResource(
                "certs/tp.jks").toExternalForm());
        sslContextFactory.setKeyStorePassword("password");
        sslContextFactory.setKeyManagerPassword("password");
        sslContextFactory.setTrustStorePath(RestrictedSSLSocketFactoryTest.class.getClassLoader().getResource(
                "certs/truststore.jks").toExternalForm());
        sslContextFactory.setTrustStorePassword("password");
        sslContextFactory.setIncludeProtocols(includeProtocols);
        sslContextFactory.setExcludeCipherSuites("");
        return sslContextFactory;
    }

    public class RequestHandler extends AbstractHandler {
        public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response)
                throws IOException, ServletException        {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().print(EXPECTED_ANSWER);
        }
    }

}