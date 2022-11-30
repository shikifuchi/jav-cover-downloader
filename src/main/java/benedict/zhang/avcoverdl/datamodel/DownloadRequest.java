package benedict.zhang.avcoverdl.datamodel;

public class DownloadRequest {

    private Boolean canRequest = Boolean.FALSE;

    private String coverUrl;

    private String savePath;

    public Boolean getCanRequest() {
        return canRequest;
    }

    public void setCanRequest(Boolean canRequest) {
        this.canRequest = canRequest;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    @Override
    public String toString() {
        return "DownloadRequest{" +
                "canRequest=" + canRequest +
                ", coverUrl='" + coverUrl + '\'' +
                ", savePath='" + savePath + '\'' +
                '}';
    }
}
