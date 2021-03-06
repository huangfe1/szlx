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
	<form action="<c:url value='/advance/transfer.json'/>"
		class="form-horizontal" name="editForm" id="editForm" method="post">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title text-primary" id="myModalLabel">物流费转账</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<input type="hidden" name="id" value="${parameter.entity.id}">
							<div class="panel panel-primary">
								<div class="panel-heading">转出方物流费账户基本信息</div>
								<div class="panel-body">
									<div class="form-group">
										<label class="col-sm-2 control-label">代理编码</label>
										<div class="col-sm-6">
											<p class="form-control-static">${fromAgent.agentCode}</p>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">当前物流费余额</label>
										<div class="col-sm-6">
											<p class="form-control-static">${balance}</p>
										</div>
									</div>
								</div>
							</div>
							<div class="panel panel-danger">
								<div class="panel-heading">填写转出基本信息</div>
								<div class="panel-body">
									<div class="form-group">
										<label for="editName" class="col-sm-2 control-label">对方姓名</label>
										<div class="col-sm-4">
											<input type="text" class="form-control" required autofocus="autofocus"
												id="editName" tabIndex="12" name="realName"
												value="" placeholder="输入对方姓名">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editAgentCode" class="col-sm-2 control-label">对方代理编号</label>
										<div class="col-sm-4">
											<input type="text" class="form-control" required
												id="editAgentCode" tabIndex="12"
												name="agentCode" value=""
												placeholder="输入对方代理编号">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<input type="hidden" value="0" name="useVoucher">
									<div class="form-group">
										<label for="voucher" class="col-sm-2 control-label">转出物流费数量</label>
										<div class="col-sm-4">
											<input type="number" class="form-control" required
												id="voucher" tabIndex="12" name="advance" value=""
												<c:if test="${balance<=0}">
										disabled="disabled"
										</c:if>
												placeholder="输入转出数量">
												<c:if test="${balance<=0}">
													<span class="help-block text-danger">物流费余额为0时无法转出</span>
												</c:if>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="currentStock" class="col-sm-2 col-xs-12 control-label">备注说明</label>
										<div class="col-sm-6 col-xs-12">
										<textarea class="form-control" rows="3" name="remark"></textarea>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<div class="form-group">
						<div class="col-md-6 col-xs-12">
							<button type="button" class="btn btn-default btn-block quitBtn"
								id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
								tabindex="4" data-loading-text="正在返回......">
								<span class="glyphicon glyphicon-close">&nbsp;</span>关闭
							</button>
						</div>
						<div class="col-md-6 col-xs-12">
							<button type="submit" class="btn btn-success btn-block"
								form="editForm" id="saveBtn" name="saveBtn" value="saveBtn"
								<c:if test="${balance<=0}">
										disabled="disabled"
										</c:if>
								tabindex="4" data-loading-text="正在提交......">
								<span class="glyphicon glyphicon-save">&nbsp;</span>转让物流费
							</button>
						</div>
					</div>
				</div>
			</div>
	</form>
</div>


<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
		var btn = null;
		$("#editName").focus();
		$("#editForm").validate(
				{
					submitHandler : function(form) {
						$(form).ajaxSubmit(
								{
									beforeSubmit : function() {
										btn.button("loading");
									},
									success : function(responseText,
											statusText, xhr, $form) {
										var m = $.parseJSON(xhr.responseText);
										btn.button("reset");
										if (m.flag == "0") {
											alert("物流费转让成功");
											$(".quitBtn").click();
											$("#search").click();
										} else {
											alert("转让失败,"+m.message);
										}

									},
									error : function(xhr, textStatus,
											errorThrown) {
										var m = $.parseJSON(xhr.responseText);
										$("#alert").empty().html(m.message)
												.removeClass("invisible");
										btn.button("reset");
									}
								});
					},
					rules : {
						agentCode : {
							required : true
						},
						realName:{
							required: true
						}
					},
					onkeyup : false,
					messages : {
						agentCode : {
							required : "请填写代理编码"
						},
						realName:{
							required:"请填写代理人姓名"
						}
					},
					focusInvalid : true,
					errorClass : "text-danger",
					validClass : "valid",
					errorElement : "small",
					errorPlacement : function(error, element) {
						error.appendTo(element.closest("div.form-group")
								.children("div.text-error"));
					}
				});
		$("#saveBtn").click(function() {
			btn = $(this).button();
		});
	}
</script>
