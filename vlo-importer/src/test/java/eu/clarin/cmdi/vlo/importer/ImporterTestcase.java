package eu.clarin.cmdi.vlo.importer;

import eu.clarin.cmdi.vlo.config.DefaultVloConfigFactory;
import eu.clarin.cmdi.vlo.config.VloConfig;
import eu.clarin.cmdi.vlo.config.VloConfigFactory;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public abstract class ImporterTestcase {

    private final VloConfigFactory configFactory = new DefaultVloConfigFactory();
    protected VloConfig config;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File testDir;

    protected File createCmdiFile(String name, String content) throws IOException {
        File file = File.createTempFile(name, ".cmdi", testDir);
        FileUtils.writeStringToFile(file, content, "UTF-8");
        return file;
    }

    @After
    public void cleanup() {
        MetadataImporter.config = null;
    }

    @Before
    public void setup() throws Exception {
        testDir = folder.newFolder(String.format("vlo_%s_%d", getClass().getCanonicalName(), System.currentTimeMillis()));

        // read the configuration defined in the packaged configuration file
        MetadataImporter.config = configFactory.newConfig();

        // optionally, modify the configuration here
        MetadataImporter.config.setComponentRegistryRESTURL("http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/");
        config = MetadataImporter.config;
    }

}
