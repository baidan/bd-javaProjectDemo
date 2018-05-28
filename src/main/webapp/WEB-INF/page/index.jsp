<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<html>
<head>
    <title>demo</title>
</head>
<%--<link rel="stylesheet" href="/asset/css/style.css"/>--%>
<script src="<%=basePath%>/static/js/jquery.js"></script>

<body>
<form id="userForm" action="<%=basePath%>/user/find" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password">
    <button type="button" onclick="login()">登录</button>
</form>
</body>
<script>
    function login() {
        $.ajax({
            type: 'post',
            url: $('#userForm').attr('action'),
            data: $('#userForm').serialize(),
            dataType: 'json',
            success: function (data) {
                if (data.result === "success") {
                    console.log(data);
                    alert("登录成功");

                   // location.href = '<%=basePath%>/user/success';
                } else {
                    alert("用户名或者密码错误！");
                }
            }
        });
    }


</script>
</html>
