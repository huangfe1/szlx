package com.dreamer.service.user;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.authorization.Authorization;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.Agent;
import org.springframework.stereotype.Service;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.utils.date.DateUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
public class AuthorizationLetterHandler {

	public byte[] generateLetter(Agent agent, Authorization auth, Path path) {
		try {
			Goods goods=auth.getAuthorizationType().getGoods();
			GoodsAccount gac=agent.loadAccountForGoodsNotNull(goods);
			ByteArrayInputStream in = new ByteArrayInputStream(
					Files.readAllBytes(path));
			BufferedImage image = ImageIO.read(in);
			BufferedImage tag = new BufferedImage(600, 849,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D gp = (Graphics2D) tag.getGraphics();
			gp.drawImage(image, 0, 0, null);
			//gp.setBackground(Color.WHITE);
			gp.setColor(Color.BLACK);
			gp.setFont(new Font("宋体", Font.PLAIN, 18));
			gp.drawString(agent.getRealName(), 250, 270);//名字
			gp.drawString(replacePrivacy(agent.getMobile()), 250, 304);
			gp.drawString(replacePrivacy(agent.getWeixin()), 250, 340);
			gp.drawString(replacePrivacy(agent.getIdCard()), 250, 374);
			gp.drawString(agent.getAgentCode(), 250, 402);
			gp.setFont(new Font("宋体", Font.BOLD, 28));
			String str =goods.getAuthorizationType().getName();
			int w=304-str.length()*14;
			gp.drawString(str, w, 530);//产品授权名字
			gp.setFont(new Font("宋体", Font.BOLD, 20));
			 str =gac.getAgentLevel().getName();
			 w=304-str.length()*10;
			gp.drawString(str, w, 575);
			gp.setFont(new Font("宋体", Font.PLAIN, 15));
			Date  dt =auth.getUpdateTime();
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.YEAR,1);//加一年
			//指定时间输出格式
			gp.drawString(DateUtil.date2string(dt,DateUtil.DATE_FORMAT)+"至"+DateUtil.date2string(rightNow.getTime(),DateUtil.DATE_FORMAT), 360, 700);
			
			gp.dispose();
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			if (auth.isActive()){//如果有授权
				ImageIO.write(tag,"png",out);
			}
			return out.toByteArray();
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ApplicationException("生成授权证书失败");
		}
	}
	
	private String replacePrivacy(String data){
		if(Objects.isNull(data)){
			return "";
		}
		if(data.length()>4){
			return data.substring(0, data.length()-4)+PRIVACY;
		}else{
			return PRIVACY;
		}
	}

	private final static String PRIVACY="****";
}
