package com.dreamer.service.pay;

import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class JsApiParameterFactory {

	public HashMap<String,Object> build(PayConfig payConfig,String prepayId){
		HashMap<String,Object> jsapiParam=new HashMap<String,Object>();
		jsapiParam.put("appId", payConfig.getAppID());
		jsapiParam.put("timeStamp", String.valueOf(System.currentTimeMillis()/1000));
		jsapiParam.put("nonceStr", RandomStringGenerator.getRandomStringByLength(32));
		jsapiParam.put("package", "prepay_id="+prepayId);
		jsapiParam.put("signType", "MD5");
		jsapiParam.put("paySign", Signature.getSign(jsapiParam, payConfig.getKey()));
		return jsapiParam;
	}
	
	public HashMap<String,Object> buildEditAddress(PayConfig payConfig,String url,String accessToken){
		HashMap<String,Object> signParam=new HashMap<String,Object>();
		signParam.put("appid", payConfig.getAppID());
		signParam.put("url", url);
		signParam.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
		signParam.put("noncestr", RandomStringGenerator.getRandomStringByLength(32));
		signParam.put("accesstoken", accessToken);
		String addrSign= Signature.getSHA1Sign(signParam);
		HashMap<String,Object> jsapiParam=new HashMap<String,Object>();
		jsapiParam.put("appId", signParam.get("appid"));
		jsapiParam.put("scope", "jsapi_address");
		jsapiParam.put("signType", "sha1");
		jsapiParam.put("addrSign",addrSign);
		jsapiParam.put("timeStamp", signParam.get("timestamp"));
		jsapiParam.put("nonceStr", signParam.get("noncestr"));
		return jsapiParam;
	}
}
