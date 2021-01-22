package fr.redstonneur1256.tineye

class ImageInformation {

    private String domain
    private BackLink backlink
    private String format
    private int width
    private int height
    private int score
    private String imageUrl
    private long fileSize

    ImageInformation(String domain, BackLink backlink, String format, int width, int height, int score, String imageUrl, long fileSize) {
        this.domain = domain
        this.backlink = backlink
        this.format = format
        this.width = width
        this.height = height
        this.score = score
        this.imageUrl = imageUrl
        this.fileSize = fileSize
    }

    String getDomain() {
        return domain
    }

    BackLink getBackLink() {
        return backlink
    }

    String getFormat() {
        return format
    }

    int getWidth() {
        return width
    }

    int getHeight() {
        return height
    }

    int getScore() {
        return score
    }

    String getImageUrl() {
        return imageUrl
    }

    long getFileSize() {
        return fileSize
    }

    @Override
    String toString() {
        return "ImageInformation{" +
                "domain='" + domain + '\'' +
                ", backlink=" + backlink +
                ", format='" + format + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", score=" + score +
                ", imageUrl='" + imageUrl + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }


    static class BackLink {

        String url
        String crawlDate
        String backLink

        BackLink(String url, String crawlDate, String backLink) {
            this.url = url
            this.crawlDate = crawlDate
            this.backLink = backLink
        }

        String getUrl() {
            return url
        }

        String getCrawlDate() {
            return crawlDate
        }

        String getBackLink() {
            return backLink
        }

        @Override
         String toString() {
            return "BackLink{" +
                    "url='" + url + '\'' +
                    ", crawl_date='" + crawlDate + '\'' +
                    ", backlink='" + backLink + '\'' +
                    '}';
        }

    }

}