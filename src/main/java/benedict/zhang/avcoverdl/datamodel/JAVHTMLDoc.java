package benedict.zhang.avcoverdl.datamodel;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Optional;

public class JAVHTMLDoc{

    private Document document;

    private String movieID;

    private Boolean isMoviePage = Boolean.FALSE;

    private Optional<String> coverSrc = Optional.empty();

    private Optional<String> movieUrl = Optional.empty();
    private JAVHTMLDoc(String movieID,Document aDocument){
        this.document = aDocument;
        this.movieID = movieID;
        init();
    }

    public static JAVHTMLDoc createDocument(String movieID, String document){
        final var documentIns = Jsoup.parse(document);
        return new JAVHTMLDoc(movieID,documentIns);
    }

    private void init(){
        initFromMeta();
        initCoverSrc();
    }

    private void initFromMeta(){
        final var metaElements = document.getElementsByTag(HtmlConstants.META);
        metaElements.stream().forEach(element -> {
            final var metaName = element.attr(HtmlConstants.ATTR_NAME);
            if(HtmlConstants.META_KEYWORD.equals(metaName)){
                isMoviePage = Boolean.TRUE;
                return;
            }
            final var metaProperty = element.attr(HtmlConstants.ATTR_PROPERTY);
            if(HtmlConstants.META_URL.equals(metaProperty)){
                final var url = element.attr(HtmlConstants.ATTR_CONTENT);
                if(!StringUtils.isEmpty(url)){
                    final var indexOfParam = url.indexOf("?");
                    final var newUrl = Configuration.CONFIGURATION.getRootUrl() + "?" + url.substring(indexOfParam);
                    this.movieUrl = Optional.ofNullable(newUrl);
                }
                return;
            }
        });
    }

    private void initCoverSrc(){
        if(isMoviePage()){
            initCoverSrcFromMoviePage();
            return;
        }

        if(isSearchResult()){
            initCoverSrcFromSearchResult();
            return;
        }
    }

    private void initCoverSrcFromMoviePage(){
        final var imgElement = document.getElementById(HtmlConstants.COVER_ID);
        if(imgElement != null){
            var src = imgElement.attr(HtmlConstants.SRC);
            if(StringUtils.isNotEmpty(src)){
                src = "https:" + src;
            }
            this.coverSrc = Optional.ofNullable(src);
        }else{
            this.coverSrc = Optional.empty();
        }
    }

    private void initCoverSrcFromSearchResult(){
        final var searchResultList = document.select("div.video");
        searchResultList.forEach(element -> {
            if(this.coverSrc.isPresent()){
                return;
            }
            final var idElement = element.select("div.id");
            final var movieId = idElement.text();
            if(StringUtils.equals(movieID,movieId)){
                final var imgSrcElement = element.select("img[src]");
                final var imgSrc = imgSrcElement.attr(HtmlConstants.SRC);
                this.coverSrc = Optional.ofNullable(imgSrc);
                final var movieParamId = element.attr(HtmlConstants.ID);
                if(StringUtils.isNotEmpty(movieParamId)){
                    final var movieParam = movieParamId.replace("vid_","?v=");
                    this.movieUrl = Optional.ofNullable(Configuration.CONFIGURATION.getRootUrl() + movieParam);
                }
            }
        });
    }

    public Boolean isMoviePage(){
        return isMoviePage;
    }

    public Boolean isSearchResult(){
        return !isMoviePage;
    }

    public Optional<String> getCoverSrc() {
        return coverSrc;
    }

    public Optional<String> getMovieUrl() {
        return movieUrl;
    }


    @Override
    public String toString() {
        final var coverSrcStr = coverSrc.isPresent() ? coverSrc.get() : "Not Available";
        final var movieUrlStr = movieUrl.isPresent() ? movieUrl.get() : "Not Available";
        return "JAVHTMLDoc{" +
                "movieID='" + movieID + '\'' +
                ", isMoviePage=" + isMoviePage +
                ", coverSrc=" + coverSrcStr +
                ", movieUrl=" + movieUrlStr +
                '}';
    }
}
