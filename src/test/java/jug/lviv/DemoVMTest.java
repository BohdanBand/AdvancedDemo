package jug.lviv;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import java.util.HashMap;

public class DemoVMTest extends FunctionalTestCase {
    @Override
    protected String getConfigResources() {
        return "demoflow_out.xml demoflow.xml demoflow_common.xml properties-configurator.xml";
    }

    FakeFtpServer fakeFtpServer = null;

    @Before
    public void setup() {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.setServerControlPort(10021);
        fakeFtpServer.addUserAccount(new UserAccount("user", "pass", "/"));

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/"));

        fakeFtpServer.setFileSystem(fileSystem);

        fakeFtpServer.start();
    }


    @Test
    public void vmDemoTest() throws Exception {

        MuleClient client = new MuleClient(muleContext);

        final String content = "Hi_JeeConf";

        MuleMessage receivedMessage = client.send("vm://process.message.outbound", "/ftp/myFile/Hi_JeeConf", new HashMap<String, Object>());

        Thread.sleep(5000);

        Assert.assertNotNull(receivedMessage);

        Assert.assertEquals(content, receivedMessage.getPayloadAsString());

        client.dispose();
    }
}
