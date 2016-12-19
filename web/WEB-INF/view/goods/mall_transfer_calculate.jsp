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
td.quantitys{
width:20%;
}
td.span4{
width:25%;
}
</style>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title text-primary" id="myModalLabel">转货明细</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
			<div class="row">
				<div class="col-xs-12">
				<h2 class="text-center">转货明细核算,请根据核算数据填写汇款信息</h2>
					<div class="table-responsive">
						<table class="table table-striped table-condensed table-bordered">
							<thead>
								<tr>
									<th>货物名称</th>
									<th>数量</th>
									<th>价格等级</th>
									<th>单价</th>
									<th>合计</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${items}" var="item">
								<tr>
									<td>${item.goodsName}</td>
									<td>${item.quantity}</td>
									<td>${item.levelName}</td>
									<td>${item.price}</td>
									<td>${item.amount}</td>
								</tr>
							</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<th colspan="4">合计</th>
									<th>${transfer_calculate.amount}</th>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
				<div class="col-xs-12">
				<%--<h4>使用奖金:&nbsp;${transfer_calculate.voucher}</h4>--%>
				<%--<h4>应付款:&nbsp;${transfer_calculate.amount-transfer_calculate.voucher}</h4>--%>
                    <%--<h4>应付款:&nbsp;${transfer_calculate.amount}</h4>--%>
				</div>
			</div>
			</div>
		</div>
		<div id="tt" class="modal-footer">
			<div class="form-group">
				<div class="col-md-6 col-xs-6">
					<a type="button" href="#" class="btn btn-warning btn-block" data-dismiss="modal" form="editForm" tabindex="26" id="quitBtn" name="quitBtn" value="quitBtn" data-loading-text="正在关闭......">
						<span class="fa fa-truck">&nbsp;</span>取消核算
					</a>
				</div>
				<div class="col-md-6 col-xs-6" id="sb">
					<button type="button" id="trBtn" class="btn btn-danger btn-block"  tabindex="26" name="btn-transfer" value="btn-transfer" data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-transfer">&nbsp;</span>转入库存
					</button>
				</div>
			</div>
		</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){

    $("#trBtn").click(function(){
        $("#btn-transfer").trigger("click");
    });

	$("#totalQuantity").text(${transfer_calculate.quantity});
	$("#totalAmount").text(${transfer_calculate.amount});
	$("#voucherPayable").text(${transfer_calculate.voucher});
	$("#actualAmount").text(${transfer_calculate.amount-transfer_calculate.voucher});
});
</script>
