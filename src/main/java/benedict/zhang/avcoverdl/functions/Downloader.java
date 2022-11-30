package benedict.zhang.avcoverdl.functions;

import benedict.zhang.avcoverdl.datamodel.DownloadRequest;
import benedict.zhang.avcoverdl.datamodel.JAVHTMLDoc;
import benedict.zhang.avcoverdl.utils.NIOUtils;

import java.io.*;
import java.nio.channels.Channels;
import java.util.function.Consumer;
import java.util.function.Function;

public class Downloader {
    public static Consumer<DownloadRequest> downloadTask = downloadRequest -> {
        System.out.println(downloadRequest.toString());
        if (downloadRequest.getCanRequest()) {
            final var saveFile = new File(downloadRequest.getSavePath());
            if (saveFile.exists()) return;
            try (final var is = HttpProcessFunctions.RequestAPI
                    .andThen(HttpProcessFunctions.ToInputStream)
                    .apply(downloadRequest.getCoverUrl())) {
                try (final var outputStream = new BufferedOutputStream(new FileOutputStream(saveFile))) {
                    NIOUtils.copy(Channels.newChannel(is), Channels.newChannel(outputStream));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static Function<File, DownloadRequest> createRequest = file -> {
        final var movieNumber = file.getName();
        final var request = new DownloadRequest();
        if (file.isFile()) {
            System.out.println("Not Directory " + movieNumber);
            return request;
        }
        System.out.println(movieNumber);
        final var resBody =
                HttpProcessFunctions.SearchByKeyAPI
                        .andThen(HttpProcessFunctions.RequestAPI)
                        .andThen(HttpProcessFunctions.ExtractResponseToHtmlString)
//                        .andThen(HttpProcessFunctions.ExtractCoverUrl)
                        .apply(movieNumber);
        var javDoc = JAVHTMLDoc.createDocument(movieNumber, resBody);
        if (javDoc.isSearchResult()) {
            final var moviesResBody =
                    HttpProcessFunctions.RequestAPI
                            .andThen(HttpProcessFunctions.ExtractResponseToHtmlString).apply(javDoc.getMovieUrl().get());
            javDoc = JAVHTMLDoc.createDocument(movieNumber, moviesResBody);
        }
        final var coverSrc = javDoc.getCoverSrc();
        if (coverSrc.isPresent()) {
            request.setCanRequest(Boolean.TRUE);
            final var coverUrl = coverSrc.get();
            request.setCoverUrl(coverUrl);
            request.setSavePath(file.getParent() + File.separator + CoverFileName(movieNumber, coverUrl));
        } else {
            System.out.println("extract failed for " + movieNumber);
        }
        return request;
    };

    public static String CoverFileName(String movieNumber, String coverSrc) {
        final var coverFileNameBuilder = new StringBuilder(movieNumber);
        final var extensionIndex = coverSrc.lastIndexOf(".");
        final var extension = coverSrc.substring(extensionIndex);
        coverFileNameBuilder.append(File.separator).append(movieNumber).append(extension);
        return coverFileNameBuilder.toString();
    }
}
