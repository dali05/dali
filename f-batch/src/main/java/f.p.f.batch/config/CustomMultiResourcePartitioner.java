package f.p.f.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomMultiResourcePartitioner implements Partitioner {

    private static final String PARTITION_KEY = "partition";
    private Resource[] resources = new Resource[0];
    private String inputKeyName = "inputFilePath";
    private String outputKeyName = "outputFileName";


    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
        int i = 0;
        for (Resource resource : resources) {
            ExecutionContext context = new ExecutionContext();
            Assert.state(resource.exists(), "Resource does not exist: " + resource);
            try {
                context.putString(inputKeyName, resource.getURL().toExternalForm());
                context.put(outputKeyName, createOutputFilename(i, resource));
            } catch (IOException e) {
                throw new IllegalArgumentException("File could not be located for: " + resource, e);
            }
            map.put(PARTITION_KEY + i, context);
            i++;
        }
        return map;
    }

    private String createOutputFilename(int partitionId, Resource resource) {
        String outputFileName = "output-" + String.valueOf(partitionId) + ".txt";
        log.info(
                "for inputfile:'"
                        + resource.getFilename()
                        + "' outputfilename:'"
                        + outputFileName
                        + "' was created");

        return outputFileName;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }
}
