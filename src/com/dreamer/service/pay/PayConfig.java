package com.dreamer.service.pay;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PayConfig {
	
	private Integer id;
	//数据版本号
	private Integer version;
	
	private String shopid;
	
	public static String CHARSET="UTF-8";
	
	private String key="huangfei08huanfgei99huangfei1234";
	//微信分配的公众号ID（开通公众号之后可以获取到）
	private String appID = "wx7abb381758c1b9d7";

	private String secret="4e6b20bc4967bd838c3b3e6e903ab768";
	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	private String mchID = "1321845701";

	private String spbill_create_ip="211.149.240.62";
	//受理模式下给子商户分配的子商户号
	private String subMchID = "";

	//HTTPS证书的本地路径
	private String certLocalPath = "";

	//HTTPS证书密码，默认密码等于商户号MCHID
	private String certPassword = "";
	
	//通知URL
	private String notifyUrl="http://www.52zdt.com/zdt/pay/wx/dmz/notify.html";

	//物流费
	private String notifAdvanceUrl="http://www.52zdt.com/zdt/advance/pay/dmz/notify.html";

	//奖金
	private String notifVoucherUrl="http://www.52zdt.com/zdt/voucher/pay/dmz/notify.html";

	private Date createTime;
	
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getMchID() {
		return mchID;
	}

	public void setMchID(String mchID) {
		this.mchID = mchID;
	}

	public String getSubMchID() {
		return subMchID;
	}

	public void setSubMchID(String subMchID) {
		this.subMchID = subMchID;
	}

	public String getCertLocalPath() {
		return certLocalPath;
	}

	public void setCertLocalPath(String certLocalPath) {
		this.certLocalPath = certLocalPath;
	}

	public String getCertPassword() {
		return certPassword;
	}

	public void setCertPassword(String certPassword) {
		this.certPassword = certPassword;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNotifAdvanceUrl() {
		return notifAdvanceUrl;
	}

	public String getNotifVoucherUrl() {
		return notifVoucherUrl;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	
	
}
