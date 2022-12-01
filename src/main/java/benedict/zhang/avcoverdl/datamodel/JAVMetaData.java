package benedict.zhang.avcoverdl.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class JAVMetaData {

    @JsonProperty
    private String id;

    @JsonProperty
    private String title;

    @JsonProperty
    private String[] actors;

    @JsonProperty("publish_date")
    private String publishDate;


    public static final String NO_VALUE = "NA";

    @Override
    public String toString() {
        final var mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "JAVMetaData{" +
                    "title='" + title + '\'' +
                    ", actors=" + Arrays.toString(actors) +
                    ", number='" + id + '\'' +
                    ", publishDate='" + publishDate + '\'' +
                    '}';
        }
    }
}
