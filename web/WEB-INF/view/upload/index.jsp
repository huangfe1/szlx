<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>由html5实现的文件上传预览功能</title>
<!-- 引用控制层插件样式 -->
<link rel="stylesheet" href="<c:url value='/resources/control/css/zyUpload.css'/>" type="text/css">
<link rel="stylesheet" href="<c:url value="/resources/control/css/jquery.gridly.css"/> " type="text/css">
<script src="http://www.lanrenzhijia.com/ajaxjs/jquery.min.js"></script>
<!-- 引用核心层插件 -->
<script src="<c:url value="/resources/core/zyFile.js"/> "></script>
<!-- 引用核心层插件 -->
<script src="<c:url value="/resources/control/js/jquery.gridly.js"/> "></script>
<!-- 引用控制层插件 -->
<script src="<c:url value="/resources/control/js/zyUpload.js"/> "></script>

<!-- 引用初始化JS -->
<script src="<c:url value="/resources/core/jq22.js"/> "></script>
</head>
<body>
<h1 style="text-align:center;">Html5 Upload示例</h1>
<div id="demo"   class="demo"></div>
<input type="button" value="test"  id="tt" >
<div id="uploadedList_0" class="upload_append_list" >
    <div class="file_bar">
        <div style="padding:5px;"><p class="file_name">4.jpg</p><span class="file_del" data-index="0" title="删除"></span>
        </div>
    </div>
    <a style="height:100px;width:120px;" href="#" class="imgBox">
        <div class="uploadImg" style="width:105px"><img id="uploadImage_0" class="upload_image" src=“”
                                                        style="width:expression(this.width > 105 ? 105px : this.width)">
        </div>
    </a>
    <p id="uploadedProgress_0" class="file_progress"></p>
    <p id="uploadedFailure_0" class="file_failure">上传失败，请重试</p>
    <p id="uploadedSuccess_0" class="file_success"></p>
</div>

<div id="uploadedList_1" class="upload_append_list" >
    <div class="file_bar">
        <div style="padding:5px;"><p class="file_name">5.jpg</p><span class="file_del" data-index="0" title="删除"></span>
        </div>
    </div>
    <a style="height:100px;width:120px;" href="#" class="imgBox">
        <div class="uploadImg" style="width:105px"><img id="uploadImage_0" class="upload_image" src=“”
                                                        style="width:expression(this.width > 105 ? 105px : this.width)">
        </div>
    </a>
    <p id="uploadedProgress_1" class="file_progress"></p>
    <p id="uploadedFailure_1" class="file_failure">上传失败，请重试</p>
    <p id="uploadedSuccess_1" class="file_success"></p>
</div>
<script>
    $(document).ready(function(){
        //alert("==");
        var list0=$("#uploadedList_0");
        var list1=$("#uploadedList_1");
        $("#preview").append(list0);
        $("#preview").append(list1);
        //拖动排序
        $('.gridly').gridly({
            base: 30, // px
            gutter: 20, // px
            columns: 12
        });

        $("#tt").click(function(){
            ZYFILE.submit();
        });
//
//        function a(bf){
//        alert("this is a");
//            bf();
//        }
//
//        function b(){
//            alert("this is b");
//        }

       // $("#uploadList_0").remove();
    });
</script>

</body>
</html>
