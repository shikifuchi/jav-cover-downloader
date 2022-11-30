package benedict.zhang.avcoverdl.functions;

import benedict.zhang.avcoverdl.datamodel.Configuration;
import benedict.zhang.avcoverdl.datamodel.HtmlConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.function.Function;

public class HttpProcessFunctions {

    public static final Function<String, String> SearchByKeyAPI = (key) -> {
        var urlBuilder = (new StringBuilder(Configuration.CONFIGURATION.getRootUrl()))
                .append(Configuration.CONFIGURATION.getSearchAPI())
                .append("?keyword=").append(key);
        return urlBuilder.toString();
    };

    public static final Function<String,Response> RequestAPI = url -> {
        var client = new OkHttpClient();
        var request = (new Request.Builder()).url(url).build();
        Response response;
        try{
            response = client.newCall(request).execute();
            return response;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    };

    public static final Function<Response, String> ExtractResponseToHtmlString = response -> {
        if (response == null) {
            return null;
        }
        if (response.isSuccessful()) {
            try (ResponseBody body = response.body()) {
                return body.string();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return null;
    };

    public static final Function<Response, InputStream> ToInputStream = response -> {
        if (response == null) {
            return null;
        }
        if (response.isSuccessful()) {
            try  {
                ResponseBody body = response.body();
                return body.byteStream();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    };

    public static final Function<String, Optional<String>> ExtractCoverUrl = body ->{
      final var document = Jsoup.parse(body);
      final var imgElement = document.getElementById(HtmlConstants.COVER_ID);
      if(imgElement != null){
          return Optional.ofNullable(imgElement.attr(HtmlConstants.SRC));
      }else{
          return Optional.empty();
      }
    };

}
