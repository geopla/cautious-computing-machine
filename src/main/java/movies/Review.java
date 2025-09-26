package movies;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Review(
        @JsonProperty("reviewId")
        long id,
        long movieInfoId,
        String comment,
        double rating
) {
}