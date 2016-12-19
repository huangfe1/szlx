package com.dreamer.view.pmall;


public class PointsGoodsDTO {
	
	private Integer id;
	private String name;
	private String spec;
	private Double voucher;
	private Double price;
	private Double pointPrice;
	private Double moneyPrice;
	private String imgUrl;
	private Double benefitPoints;
	private Integer stockQuantity;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Double getVoucher() {
		return voucher;
	}
	public void setVoucher(Double voucher) {
		this.voucher = voucher;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getPointPrice() {
		return pointPrice;
	}
	public void setPointPrice(Double pointPrice) {
		this.pointPrice = pointPrice;
	}
	public Double getMoneyPrice() {
		return moneyPrice;
	}
	public void setMoneyPrice(Double moneyPrice) {
		this.moneyPrice = moneyPrice;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Double getBenefitPoints() {
		return benefitPoints;
	}
	public void setBenefitPoints(Double benefitPoints) {
		this.benefitPoints = benefitPoints;
	}
	public Integer getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
