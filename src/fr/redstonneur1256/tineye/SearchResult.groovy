package fr.redstonneur1256.tineye

import org.jetbrains.annotations.NotNull

class SearchResult {

    private int totalResults;
    private ImageInformation[] images

    SearchResult(int totalResults, ImageInformation[] images) {
        this.totalResults = totalResults
        this.images = images
    }

    int getTotalResults() {
        return totalResults
    }

    @NotNull
    ImageInformation[] getImages() {
        return images
    }

    @Override
    String toString() {
        return "SearchResult{" +
                "totalResults=" + totalResults +
                ", images=" + Arrays.toString(images) +
                '}';
    }

}
