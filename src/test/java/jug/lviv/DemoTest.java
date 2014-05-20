package jug.lviv;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.mule.tck.junit4.FunctionalTestCase;

import java.util.List;

public class DemoTest extends FunctionalTestCase {
    @Override
    protected String getConfigResources() {
        return "demoflow_in.xml demoflow.xml demoflow_common.xml demoflow_out.xml properties-configurator.xml";
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
    public void fTPDemoTest() throws Exception {

        final String url = "http://localhost:8084/ftp/myFile/Hi_JeeConf";

        final String content = "Hi_JeeConf";

        List<FileEntry> files = fakeFtpServer.getFileSystem().listFiles("/");

        Assert.assertEquals(files.size(), 0);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = httpClient.execute(request);

        System.out.println(response.getStatusLine().getStatusCode());

        files = fakeFtpServer.getFileSystem().listFiles("/");

        Assert.assertEquals(1, files.size());

        String fileContent = IOUtils.toString(files.get(0).createInputStream());
        Assert.assertEquals(fileContent, content);

    }
}
