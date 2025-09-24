package movies;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record MovieInfo(
        @JsonProperty("movieInfoId")
        long id,

        String name,

        int year,

        @JsonProperty("cast")
        List<String> casts,

        @JsonProperty("release_date")
        LocalDate releaseDate
) {
}