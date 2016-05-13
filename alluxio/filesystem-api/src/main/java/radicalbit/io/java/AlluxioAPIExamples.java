package radicalbit.io.java;

import alluxio.AlluxioURI;
import alluxio.client.file.FileSystem;
import alluxio.client.file.options.LoadMetadataOptions;

public class AlluxioAPIExamples {

  public static void main(String[] args) throws Exception {

    AlluxioURI localMntPoint = new AlluxioURI("/mnt/local");
    AlluxioURI localPath = new AlluxioURI("path/to/local/folder");
    AlluxioURI hadoopMntPoint = new AlluxioURI("/mnt/hadoop");
    AlluxioURI hadoopPath = new AlluxioURI("hdfs://namenode/directory");
    AlluxioURI firstFileToStage = new AlluxioURI("path/to/local/folder/firstFile");
    AlluxioURI secondFileToStage = new AlluxioURI("path/to/local/folder/firstFile");

    FileSystem fileSystem = FileSystem.Factory.get();
    fileSystem.mount(localMntPoint, localPath);
    fileSystem.mount(hadoopMntPoint, hadoopPath);
    fileSystem.loadMetadata(firstFileToStage, LoadMetadataOptions.defaults());
    fileSystem.loadMetadata(secondFileToStage,LoadMetadataOptions.defaults());

  }
}
