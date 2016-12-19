<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<style>
<!--
-->
.none {
	display: none;
}
</style>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title text-primary" id ="myModalLabel">产品详情页编辑</h4>
		</div>
		<div class="modal-body">

            <%--<div id="uploadedList_0" class="upload_append_list" >--%>
                <%--<div class="file_bar">--%>
                    <%--<div style="padding:5px;"><p class="file_name">4.jpg</p><span class="file_del" data-index="0" title="删除"></span>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<a style="height:100px;width:120px;" href="#" class="imgBox">--%>
                    <%--<div class="uploadImg" style="width:105px"><img id="uploadImage_0" class="upload_image" src=“”--%>
                                                                    <%--style="width:expression(this.width > 105 ? 105px : this.width)">--%>
                    <%--</div>--%>
                <%--</a>--%>
                <%--<p id="uploadedProgress_0" class="file_progress"></p>--%>
                <%--<p id="uploadedFailure_0" class="file_failure">上传失败，请重试</p>--%>
                <%--<p id="uploadedSuccess_0" class="file_success"></p>--%>
            <%--</div>--%>

                <%--<div id="demo"   class="demo"></div>--%>



		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-success btn-block" form="editForm" tabIndex="27"
						id="saveBtn" name="saveBtn" value="saveBtn" tabindex="4"
						data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-save">&nbsp;</span>保存
					</button>
				</div>
			</div>
		</div>
	</div>
</div>

<%--<jsp:include page="/WEB-INF/view/common/script_hf_upload.jsp"></jsp:include>--%>

<script type="text/javascript">

//    /* www.jq22.com */
//    $(function() {
//// 初始化插件
//        $("#demo").zyUpload({
//            isSubmitForm: false,
//            myForm: $("#editForm").get(0),//需要提交的表单
//            width: "650px",                 // 宽度
//            height: "400px",                 // 宽度
//            itemWidth: "120px",                 // 文件项的宽度
//            itemHeight: "100px",                 // 文件项的高度
//            url: "http://localhost:8080/upload",  // 上传文件的路径
//            multiple: true,                    // 是否可以多个文件上传
//            dragDrop: true,                    // 是否可以拖动上传文件
//            del: true,                    // 是否可以删除文件
//            finishDel: false,  				  // 是否在上传文件完成后删除预览
//            /* 外部获得的回调接口 */
//            onSelect: function (files, allFiles) {                    // 选择文件的回调方法
//                console.info("当前选择了以下文件：");
//                console.info(files);
//                console.info("之前没上传的文件：");
//                console.info(allFiles);
//            },
//            onDelete: function (file, surplusFiles) {                     // 删除一个文件的回调方法
//                console.info("当前删除了此文件：");
//                console.info(file);
//                console.info("当前剩余的文件：");
//                console.info(surplusFiles);
//            },
//            onSuccess: function (file) {                    // 文件上传成功的回调方法
//                console.info("此文件上传成功：");
//                console.info(file);
//            },
//            onFailure: function (file) {                    // 文件上传失败的回调方法
//                console.info("此文件上传失败：");
//                console.info(file);
//            },
//            onComplete: function (responseInfo) {           // 上传完成的回调方法  提交表单
//                console.info("文件上传完成");
//                console.info(responseInfo);
//            },
//            onCompleteSubmit: function () {
//                alert("正在提交表单");
//            },
//        });
//    });


</script>
