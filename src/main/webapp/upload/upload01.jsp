<%--
  Created by IntelliJ IDEA.
  User: hpywl
  Date: 2019/9/8
  Time: 0:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta http-equiv="X-UA-Compatible" content="IE=11">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ui.progress-bar.css">
    <link media="only screen and (max-device-width: 480px)" href="${pageContext.request.contextPath}/css/ios.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.4.1.min.js" charset="UTF-8"></script>
    <script type="text/javascript">
        var timeInfo = 0;

        $(function(){
            $("#upload").click(function(){
                openProgess();
                uploadFile();
                timeInfo = setInterval("getUploadInfo()",100); //每0.01秒获取一次数据
            });

            $("#cancel").click(function(){
                closeProgess();
                $("#pdBar").css({"width":"0%"});
                clearInterval(timeInfo);
            });
        });

        //文件上传
        function uploadFile(){
            var fileObj = $("#fileObj")[0].files[0];//必须这样写,否则取不到文件
            //$('#form1').serialize() 无法序列化二进制文件，这里采用formData上传
            //需要浏览器支持：Chrome 7+、Firefox 4+、IE 10+、Opera 12+、Safari 5+。
            var formData = new FormData(); // FormData 对象
//      formData.append("author", "hooyes"); // 可以增加表单数据
            formData.append("fileObj", fileObj); // 文件对象
            $.ajax({
                type:"POST",
                url:"${pageContext.request.contextPath}/fileUploadController",
                data:formData,
                async: true,
                cache: false,
                dataType:"json",
                contentType: false,/*必须false才会自动加上正确的Content-Type */
                processData: false,/*必须false才会避开jQuery对 formdata 的默认处理XMLHttpRequest会对 formdata 进行正确的处理*/
                success:function(res){
                    if(res.tag === true){
                        clearInterval(timeInfo);
                        getUploadInfo();//修正得到文件上传信息
                        //setPdWidth(100);//修正文件上传为100%
                        //alert("上传成功!");
                    }else{
                        clearInterval(timeInfo);
                        alert("上传失败!");
                    }
                },
                error:function(){
                    clearInterval(timeInfo);
                    alert("上传错误!");
                }
            });
        }

        //打开进度条
        function openProgess(){
            /* var modalHeight=$(window).height() / 2 - $('#pgID').height() / 2;
            $("#pgID").css({"display":"block","margin":modalHeight}); */
            $("#progress_bar").css({"display":"block"});
        }

        //关闭进度条
        function closeProgess(){
            $("#progress_bar").css({"display":"none"});
            $("#progress_bar .ui-progress").css({"width":"0%"});
        }

        //得到上传文件进度信息
        function getUploadInfo(){
            $.ajax({
                url:"${pageContext.request.contextPath}/getInfo",
                data:{time:new Date()},
                type:"post",
                dataType:"json",
                cache:false,
                success:function(res){
                    //console.log(res);
                    setPdWidth(res.percent);
                    setUploadInfo(res);
                    if(res.percent == 100){
                        clearInterval(timeInfo);
                        return;
                    }
                },
                error:function(){
                    clearInterval(timeInfo);
                    console.log("得到上传文件信息出错!");
                }
            });
        }

        //设置上传文件进度信息
        function setUploadInfo(res){
            $("#startTime").text(res.startTime);
            $("#currentTime").text(res.currentTime);
            $("#time").text(res.time);
            $("#velocity").text(res.velocity);
            $("#totalTime").text(res.totalTime);
            $("#timeLeft").text(res.timeLeft);
            $("#percent").text(res.percent);
            $("#length").text(res.length);
            $("#totalLength").text(res.totalLength);
        }

        function setPdWidth(percent){
            var this1 = $("#progress_bar .ui-progress");
            var labelEl = $('.ui-label', this1),
                valueEl = $('.value', labelEl);
            if (Math.ceil(percent) < 20 && $('.ui-label', this1).is(":visible")) {
                labelEl.hide();
            }else{
                if (labelEl.is(":hidden")) {
                    labelEl.fadeIn();
                };
            }
            if (Math.ceil(percent) == 100) {
                labelEl.text('Done');
                /*setTimeout(function() {
                    labelEl.fadeOut();
                }, 1000);*/
            }else{
                valueEl.text(Math.ceil(percent) + '%');
            }

            $("#progress_bar div").css({"width":percent+"%"});
        }

    </script>
</head>
<body>
<fieldset>
    <div id="container">
        <!--
        ajax提交后form表单中如果存在button按钮的话ajax完成之后form表单
        还是会自动提交
        -->
        <div class="content">
            <h1>Pure CSS Progress Bar</h1>
        </div>
        <form action="${pageContext.request.contextPath}/fileUploadController"
              method="post" id="form1" name="form1"
              enctype="multipart/form-data"
              onsubmit="return false;" role="form" class="form-inline">
            <input type="file" name="fileObj" id="fileObj" class="form-group"/>
        </form>
        <!-- Progress bar -->
        <div id="progress_bar" class="ui-progress-bar ui-container" style="display: none">
            <div class="ui-progress" style="width: 0%;">
                <span class="ui-label" style="display:none;">Processing <b class="value">0%</b></span>
            </div>
        </div>
        <!-- /Progress bar -->
        <div>
            <button class="btn btn-primary" id="upload">上传</button>
            <button class="btn btn-danger" id="cancel">取消</button>
        </div>
        <p>开始时间:<font id="startTime" color="red"></font></p>
        <p>现在时间:<font id="currentTime" color="red"></font></p>
        <p>已经传输了的时间(s):<font id="time" color="red"></font></p>
        <p>传输速度(byte/s):<font id="velocity" color="red"></font></p>
        <p>估计总时间:<font id="totalTime" color="red"></font></p>
        <p>估计剩余时间:<font id="timeLeft" color="red"></font></p>
        <p>上传百分比:<font id="percent" color="red"></font></p>
        <p>已完成数:<font id="length" color="red"></font></p>
        <p>总长度(M):<font id="totalLength" color="red"></font></p>
    </div>
</fieldset>
</body>
</html>
