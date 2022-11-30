package benedict.zhang.avcoverdl.datamodel;

public class Configuration {

    public static final Configuration CONFIGURATION = new Configuration();

    private String rootUrl;

    private String searchAPI;


    private Configuration(){

    }

    public Configuration withRootUrl(String rootUrl){
        this.rootUrl = rootUrl;
        return this;
    }

    public Configuration withSearchAPI(String api){
        this.searchAPI = api;
        return this;
    }

    public String getRootUrl(){
        return rootUrl;
    }

    public String getSearchAPI() {
        return searchAPI;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "rootUrl='" + rootUrl + '\'' +
                ", searchAPI='" + searchAPI + '\'' +
                '}';
    }
}
