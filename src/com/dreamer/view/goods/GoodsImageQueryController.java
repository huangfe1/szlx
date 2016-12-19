package com.dreamer.view.goods;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dreamer.repository.system.SystemParameterDAOInf;

import ps.mx.otter.utils.WebUtil;

@Controller
@RequestMapping("/dmz/img")
public class GoodsImageQueryController {
	
	@Value("${img.goods.dir}")
	private String goodsImgDir;
	@Value("${img.mallgoods.dir}")
	private String pointsGoodsImgDir;

	@RequestMapping(value = "/goods/{code}.{suffix}", method = RequestMethod.GET)
	public void goodsImg(@PathVariable("code") String code,@PathVariable("suffix") String suffix,
			HttpServletRequest request,HttpServletResponse response){
		response.setContentType("image/jpeg");
		try{
			/*Path path=Paths.get(goodsImgDir,code+"."+suffix);
			if(!Files.exists(path)){
				String defaultImgDir=WebUtil.getRealPath(request, "/assets/images");
				LOG.debug("/assets/images 绝对路径：{}",defaultImgDir);
				path=Paths.get(defaultImgDir,"default.jpg");
			}
			byte[] bts=Files.readAllBytes(path);*/
			Path imgFile=this.getImgFile(getGoodsImgDir(), code+"."+suffix, request);
			byte[] bts=Files.readAllBytes(imgFile);
			ServletOutputStream out=response.getOutputStream();
			out.write(bts);
			out.flush();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("获取产品图片失败",exp);
		}
	}
	
	@RequestMapping(value = "/pmall/{code}.{suffix}", method = RequestMethod.GET)
	public void pointsGoodsImg(@PathVariable("code") String code,@PathVariable("suffix") String suffix,
			HttpServletRequest request,HttpServletResponse response){
		response.setContentType("image/jpeg");
		try{
			Path imgFile=this.getImgFile(getPointsGoodsImgDir(), code+"."+suffix, request);
			byte[] bts=Files.readAllBytes(imgFile);
			ServletOutputStream out=response.getOutputStream();
			out.write(bts);
			out.flush();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("获取积分产品图片失败",exp);
		}
	}
	
	@RequestMapping(value = {"/goods/null","/pmall/null","/goods/","/pmall/"}, method = RequestMethod.GET)
	public void goodsNullImg(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("image/jpeg");
		try{
			Path path=Paths.get(getGoodsImgDir(),"default.jpg");
			byte[] bts=Files.readAllBytes(path);
			ServletOutputStream out=response.getOutputStream();
			out.write(bts);
			out.flush();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("获取产品图片失败",exp);
		}
	}
	
	private Path getImgFile(String imgDir,String fileName,HttpServletRequest request){
		Path path=Paths.get(imgDir,fileName);
		if(!Files.exists(path)){
			String defaultImgDir=WebUtil.getRealPath(request, "/assets/images");
			LOG.debug("/assets/images 绝对路径：{}",defaultImgDir);
			path=Paths.get(defaultImgDir,"default.jpg");
		}
		return path;
	}
	
	
	public String getGoodsImgDir() {
		return systemParameterDAO.getGoodsImgPath();
	}

	public void setGoodsImgDir(String goodsImgDir) {
		this.goodsImgDir = goodsImgDir;
	}
	
	public String getPointsGoodsImgDir() {
		return systemParameterDAO.getMallGoodsImgPath();
	}

	public void setPointsGoodsImgDir(String pointsGoodsImgDir) {
		this.pointsGoodsImgDir = pointsGoodsImgDir;
	}
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
