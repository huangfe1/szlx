package com.dreamer.view.upload;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartResolver;

import com.dreamer.upload.models.Progress;


/**
 * 
 * 创建人：fantasy <br>
 * 创建时间：2013-12-6 <br>
 * 功能描述： 获取上传文件进度controller<br>
 *
 */
@Controller
@RequestMapping("/fileStatus")
public class ProgressController extends BaseController{
	@Resource(name="multipartResolver")
	private MultipartResolver multipartResolver;	
	@RequestMapping(value = "/upfile/progress" )
	@ResponseBody
	public String initCreateInfo(HttpServletRequest request) {
		System.out.println(multipartResolver+"------------");
		Progress status = (Progress) request.getSession().getAttribute("upload_ps");
		System.out.println("===================="+status.toString());
		if(status==null){
			return "{}";
		}
		return status.toString();
	}
}