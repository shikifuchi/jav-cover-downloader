package benedict.zhang.avcoverdl;

import benedict.zhang.avcoverdl.datamodel.Configuration;
import benedict.zhang.avcoverdl.functions.Downloader;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;

public class Application {
    
    public static void main(String[] args) {
        init();
        execute();
    }

    public static void init(){
        final var rootUrl = System.getProperty("service.url");
        var api = System.getProperty("service.api");
        if(StringUtils.isEmpty(rootUrl)){
            System.out.println("service url not specified");
        }
        if(StringUtils.isEmpty(api)){
            api = "vl_searchbyid.php";
        }
        Configuration.CONFIGURATION.withRootUrl(rootUrl).withSearchAPI(api);
    }

    public static void execute(){
        var file = new File(System.getProperty("user.dir"));
        final var moviesDirs = file.listFiles();
        if(moviesDirs == null || moviesDirs.length == 0){
            return;
        }
        Arrays.stream(moviesDirs)
                .parallel()
                .filter(File::isDirectory)
                .map(Downloader.createRequest)
                .forEach(Downloader.downloadTask);
    }
}
