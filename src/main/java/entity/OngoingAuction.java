package entity;

import java.math.BigDecimal;

public class OngoingAuction {
	private int auction_id;
	private String itemName;
	private BigDecimal current_bid;
	private long timeRemaining;
	private int totalBids;
	private int totalBidders;

	

	public OngoingAuction(int auction_id, String itemName,
			BigDecimal current_bid, long timeRemaining, int totalBids,
			int totalBidders) {
		this.auction_id = auction_id;
		this.itemName = itemName;
		this.current_bid = current_bid;
		this.timeRemaining = timeRemaining;
		this.totalBids = totalBids;
		this.totalBidders = totalBidders;
	}

	public int getAuction_id() {
		return auction_id;
	}

	public void setAuction_id(int auction_id) {
		this.auction_id = auction_id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getCurrent_bid() {
		return current_bid;
	}

	public void setCurrent_bid(BigDecimal current_bid) {
		this.current_bid = current_bid;
	}

	public long getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(long timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	@Override
	public String toString() {
		return "OngoingAuction [itemName=" + itemName + ", current_bid=" + current_bid + ", timeRemaining="
				+ timeRemaining + "]";
	}

	public int getTotalBids() {
		return totalBids;
	}

	public void setTotalBids(int totalBids) {
		this.totalBids = totalBids;
	}

	public int getTotalBidders() {
		return totalBidders;
	}

	public void setTotalBidders(int totalBidders) {
		this.totalBidders = totalBidders;
	}

}
