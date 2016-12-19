package com.dreamer.service.mall.goods;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ps.mx.otter.exception.ApplicationException;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.mall.goods.MallGoodsStockBlotter;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.mall.goods.MallGoodsStockBlotterDAO;
import com.dreamer.repository.system.SystemParameterDAOInf;
import com.dreamer.repository.user.MutedUserDAO;

@Service
public class MallGoodsHandler {

	@Transactional
	public void addMallGoods(MallGoods mallGoods,String imgType,byte[] imgBytes){
		LOG.debug("新增积分商城商品 name:{}",mallGoods.getName());
		if(Objects.nonNull(imgType)){
			String fileName=generateFileName(imgType);
			writeImgFile(imgBytes, fileName);
			mallGoods.setImgFile(fileName);
		}
		mallGoodsDAO.save(mallGoods);
	}
	
	@Transactional
	public void modifyMallGoods(MallGoods mallGoods,String imgType,byte[] imgBytes){
		LOG.debug("修改积分商城商品 id:{}",mallGoods.getId());
		if(Objects.nonNull(imgType)){
			String fileName=mallGoods.getImgFile();
			if(Objects.isNull(fileName)||fileName.isEmpty()){
				fileName=generateFileName(imgType);
				mallGoods.setImgFile(fileName);
			}
			writeImgFile(imgBytes, fileName);
		}
		mallGoodsDAO.merge(mallGoods);
	}
	
	@Transactional
	public void removeMallGoods(MallGoods mallGoods){
		LOG.debug("删除积分商城商品 id:{}",mallGoods.getId());
		mallGoodsDAO.delete(mallGoods);
	}
	
	@Transactional
	public void addStock(MallGoodsStockBlotter stock) {
		MallGoods goods = mallGoodsDAO.findById(stock.getGoods().getId());
		goods.addStockBlotter(stock);
		mallGoodsStockBlotterDAO.save(stock);
		LOG.debug("新增库存成功");
	}
	
	private String generateFileName(String imgType){
		String s=UUID.randomUUID().toString();
		StringBuilder sbd=new StringBuilder();
		sbd.append(s.substring(0,8)).append(s.substring(9,13)).append(s.substring(14,18)).append(s.substring(19,23)).append(s.substring(24)).append(imgType);
		return sbd.toString();
	}
	

	private void writeImgFile(byte[] imgBytes,String fileName) {
		try {
			Path path = Paths.get(systemParameterDAO.getMallGoodsImgPath());
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			Path imgFile=path.resolve(fileName);
			if(!Files.exists(imgFile)){
				Files.createFile(imgFile);
			}
			Files.write(imgFile, imgBytes, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException iop) {
			LOG.error("写图片文件失败",iop);
			throw new ApplicationException(iop);
		}
	}
	
	@Autowired
	private MallGoodsDAO mallGoodsDAO;
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
	@Autowired
	private MutedUserDAO mutedUserDAO;
	@Autowired
	private MallGoodsStockBlotterDAO mallGoodsStockBlotterDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
