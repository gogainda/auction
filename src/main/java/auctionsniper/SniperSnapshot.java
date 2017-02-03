package auctionsniper;

public class SniperSnapshot {
    public final String itemId;
    public final int lastPrice;
    public final int lastBid;
    public final SniperState state;


    public SniperSnapshot(String itemId, int price, int bid, SniperState bidding) {
        this.itemId = itemId;
        this.lastPrice = price;
        this.lastBid = bid;
        this.state = bidding;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SniperSnapshot that = (SniperSnapshot) o;

        if (lastPrice != that.lastPrice) return false;
        if (lastBid != that.lastBid) return false;
        return itemId != null ? itemId.equals(that.itemId) : that.itemId == null;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + lastPrice;
        result = 31 * result + lastBid;
        return result;
    }

    @Override
    public String toString() {
        return "SniperSnapshot{" +
                "itemId='" + itemId + '\'' +
                ", lastPrice=" + lastPrice +
                ", lastBid=" + lastBid +
                '}';
    }
}
