package snapLink.Url.Dto;

public class DashboardResponse {
    private long totalUrls;
    private long totalClicks;
    private long activeLinks;
    private long expiredLinks;
    public DashboardResponse(
            long totalUrls,
            long totalClicks,
            long activeLinks,
            long expiredLinks
    ) {
        this.totalUrls = totalUrls;
        this.totalClicks = totalClicks;
        this.activeLinks = activeLinks;
        this.expiredLinks = expiredLinks;
    }
    public long getTotalUrls() {
        return totalUrls;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public long getActiveLinks() {
        return activeLinks;
    }

    public long getExpiredLinks() {
        return expiredLinks;
    }
}
