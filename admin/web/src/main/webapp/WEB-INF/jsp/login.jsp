<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix='security' uri='http://www.springframework.org/security/tags'%>
<%
	String basePath = request.getContextPath();
%>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>登录</title>

<!-- Bootstrap Core CSS -->
<link href="<%=basePath%>/static/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- MetisMenu CSS -->
<link href="<%=basePath%>/static/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="<%=basePath%>/static/dist/css/sb-admin-2.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="<%=basePath%>/static/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

	<div class="container">
		<input type="hidden" value="${SPRING_SECURITY_LAST_EXCEPTION.message}" id="errormsg">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">请登录</h3>
					</div>
					<div class="panel-body">
						<form role="form" action="<%=basePath%>/login.do" method="post"
							id="form" onsubmit="return submitCheck()">
							<security:csrfInput />
							<fieldset>
								<div class="form-group">
									<input class="form-control" placeholder="请输入用户名"
										name="username" id="username" type="username" autofocus>
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="请输入密码" name="password"
										id="password" type="password" value="">
								</div>
								<input type="submit" id="submitLogin"
									class="btn btn-lg btn-success btn-block" value="登录">
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- jQuery -->
	<script src="<%=basePath%>/static/bower_components/jquery/dist/jquery.min.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="<%=basePath%>/static/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="<%=basePath%>/static/bower_components/metisMenu/dist/metisMenu.min.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="<%=basePath%>/static/dist/js/sb-admin-2.js"></script>
	<script src="<%=basePath%>/static/layer/layer.js"></script>
	<script src="<%=basePath%>/static/js/jquery.form.js"></script>
	<script>
		$(function() {
			if (self != top) {
				top.location = self.location.href;
				return;
			}

			var errormsg = $("#errormsg");
			if ($.trim(errormsg.val()) != "") {
				layer.alert(errormsg.val());
			}
		});

		function submitCheck() {
			var username = $("#username");
			var password = $("#password");
			if ($.trim(username.val()) == "") {
				layer.msg("用户名不能为空！");
				return false;
			}
			if ($.trim(password.val()) == "") {
				layer.msg("密码不能为空！");
				return false;
			}
		}
	</script>
</body>
</html>