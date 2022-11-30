package functions;

import benedict.zhang.avcoverdl.datamodel.Configuration;
import benedict.zhang.avcoverdl.datamodel.JAVHTMLDoc;
import benedict.zhang.avcoverdl.functions.Downloader;
import benedict.zhang.avcoverdl.functions.HttpProcessFunctions;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DownloaderFunctionTest {
//
    @Test
    public void testSearchResult(){
        Configuration.CONFIGURATION.withRootUrl("https://www.x63a.com/cn/").withSearchAPI("vl_searchbyid.php");
        final var response = HttpProcessFunctions.SearchByKeyAPI
                .andThen(HttpProcessFunctions.RequestAPI).apply("GVG-038");
        if (response.isSuccessful()) {
            try (ResponseBody body = response.body()) {
                final var javDoc = JAVHTMLDoc.createDocument("GVG-038",body.string());
                System.out.println(javDoc.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Test
    public void testMovie(){
        Configuration.CONFIGURATION.withRootUrl("https://www.x63a.com/cn/").withSearchAPI("vl_searchbyid.php");
        final var response = HttpProcessFunctions.SearchByKeyAPI
                .andThen(HttpProcessFunctions.RequestAPI).apply("GVG-238");
        if (response.isSuccessful()) {
            try (ResponseBody body = response.body()) {
                final var javDoc = JAVHTMLDoc.createDocument("GVG-238",body.string());
                System.out.println(javDoc.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

//    @Test
//    public void testCoverFileName(){
//        final var coverSrc = "//pics.dmm.co.jp/mono/movie/adult/3wanz225/3wanz225pl.jpg";
//        final var movieNumber = "WANZ-225";
//        final var coverName = Downloader.CoverFileName(movieNumber,coverSrc);
//        System.out.println(coverName);
//        Assertions.assertEquals("WANZ-225.jpg",coverName);
//    }
}
