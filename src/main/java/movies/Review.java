package movies;

public record Review(
        long id,
        long movieInfoId,
        String comment,
        double rating
) {
}