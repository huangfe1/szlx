package com.dreamer.domain.goods;
import com.dreamer.domain.user.Enum;
public enum  GoodsType implements Enum{
	MALL("虚拟产品");

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return desc;
	}
	
	private String desc;
	
	GoodsType(String desc){
		this.desc=desc;
	}

}
