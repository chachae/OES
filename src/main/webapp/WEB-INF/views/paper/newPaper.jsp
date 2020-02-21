<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>OES | 新增试卷</title>
    <!-- css style -->
    <%@ include file="../include/css.jsp" %>
    <link rel="stylesheet" href="<c:url value="/static/plugins/datetimepicker/css/bootstrap-datetimepicker.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/webjars/adminlte/2.3.11/plugins/datepicker/datepicker3.css"/>">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    <!-- 顶部导航栏部分 -->
    <%@ include file="../teacher/include/header.jsp" %>
    <!-- 左侧菜单栏 -->
    <jsp:include page="../teacher/include/sider.jsp">
        <jsp:param name="menu" value="paper"/>
    </jsp:include>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">
            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">新增试卷</h3>
                    <div class="box-tools pull-right">
                        <a href="<c:url value="/teacher/paper"/>" class="btn btn-primary btn-sm"><i
                                class="fa fa-arrow-left"></i> 返回列表</a>
                    </div>
                </div>
                <div class="box-body">
                    <form method="post" id="addForm">
                        <div class="form-group">
                            <label for="teacherId">试卷名称</label>
                            <input type="hidden" class="form-control" id="teacherId" name="teacherId"
                                   value="${sessionScope.get('teacherId')}">
                            <input type="hidden" class="form-control" name="paperState" value="未开始">
                            <input type="text" class="form-control" name="paperName">
                        </div>
                        <div class="form-group">
                            <label for="courseId">所属课程</label>
                            <select class="form-control" id="courseId" name="courseId">
                                <option value="">请选择</option>
                                <c:forEach items="${courseList}" var="course">
                                    <option value="${course.id}">${course.courseName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="paperType">试卷类型 </label>
                            <select class="form-control" name="paperType" id="paperType">
                                <option value="">请选择</option>
                                <option value="正式">正式</option>
                                <option value="模拟">模拟</option>
                            </select>
                        </div>
                        <div class="form-group form_datetime">
                            <label for="difficulty">难度</label>
                            <select class="form-control" name="difficulty" id="difficulty">
                                <option value="">难度均摊</option>
                                <option value="1">容易</option>
                                <option value="2">中等</option>
                                <option value="3">困难</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="academy">学院</label>
                            <select id="academy" onchange="getAcademy(this.value)" name="academy" class="form-control">
                                <option value="">请选择</option>
                                <c:forEach items="${academyList}" var="academy">
                                    <option value="${academy.id}">${academy.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="majorId">考试专业</label>
                            <select id="majorId" name="majorId" class="form-control">
                                <option value="">请选择</option>
                                <c:forEach items="${majorList}" var="major">
                                    <option value="${major.id}">${major.major}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="score">试卷总分 (默认模版为100分)</label>
                            <input type="text" id="score" name="score" value="100" class="form-control">
                        </div>
                        <hr/>
                        <div class="form-group">
                            <label style="color: #af0000">注意: 正式试卷必选，模拟试卷无需选择时间</label>
                        </div>
                        <div class="form-group form_datetime">
                            <label for="datepicker">开始时间</label>
                            <input type="text" name="beginTime" class="form-control" id="datepicker">
                        </div>
                        <div class="form-group form_datetime">
                            <label for="datepicker2">结束时间</label>
                            <input type="text" name="endTime" class="form-control" id="datepicker2">
                        </div>
                        <hr/>

                    </form>
                </div>
                <div class="box-footer">
                    <button class="btn btn-primary" id="addBtn">保存</button>
                    <a href="javascript:history.go(0)" class="btn btn-danger" id="reset">重置</a>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <!-- 底部 -->
    <%@ include file="../include/footer.jsp" %>
</div>
<!-- ./wrapper -->
<!-- js -->
<%@ include file="../include/js.jsp" %>
<script src="<c:url value="/static/plugins/datetimepicker/js/bootstrap-datetimepicker.min.js"/>"></script>
<script src="<c:url value="/static/plugins/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"/>"></script>
<script src="<c:url value="/static/plugins/moment/moment.js"/>"></script>
<script src="<c:url value="/webjars/adminlte/2.3.11/plugins/datepicker/bootstrap-datepicker.js"/>"></script>
<script src="<c:url value="/webjars/adminlte/2.3.11/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"/>"></script>
<script src="<c:url value="/static/plugins/jquery-validation/jquery.validate.js"/>"></script>
<script src="<c:url value="/static/plugins/jquery-form/jquery.form.min.js"/>"></script>
<script src="<c:url value="/static/plugins/layer/layer.js"/>"></script>
<script>

    // select2
    $('#academy').select2({width: "100%"});

    //回填的二级类别值
    function getAcademy(id) {
        let options = "";
        //回填的二级类别值
        $('#majorId').empty();
        options += "<option value=''>请选择</option>";
        <c:forEach items="${majorList}" var="major">
        var academyId = "${major.academyId}";
        if (academyId === id) {
            const value = "${major.id}";
            const name = "${major.major}";
            options += "<option selected='true' value=" + value + ">" + name + "</option>";
        }
        </c:forEach>
        $("#majorId").append(options).select2();
    };

    $(function () {
        // select2
        $('#courseId').select2({width: "100%"});
        $('#paperType').select2({width: "100%"});

        const date = new Date();
        const picker = $("#datepicker").datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            language: "zh-CN",
            autoclose: true,
            todayHighlight: true,
            startDate: date
        });
        picker.on("changeDate", function (e) {
            const time = $("#datepicker").val();
            const day = time.split(" ")[0];
            $('#datepicker2').datetimepicker('setStartDate', day);
        });

        let timepicker = $('#datepicker2').datetimepicker({
            format: "yyyy-mm-dd hh:ii",
            language: "zh-CN",
            autoclose: true,
            todayHighlight: true
        });

        // 提交组卷
        $('#addBtn').click(function () {
            $.ajax({
                type: "POST",
                data: $('#addForm').serialize(),
                success: function (res) {
                    if (res.state === "success") {
                        layer.alert("添加成功");
                        window.location.href = "/teacher/paper/show/" + res.data;
                    } else {
                        layer.alert(res.message);
                    }
                }
            });
            return false;
        });

        $("#addForm").validate({
            errorClass: 'text-danger',
            errorElement: 'span',
            rules: {
                paperName: {
                    required: true
                },
                courseId: {
                    required: true
                },
                paperType: {
                    required: true
                },
                major: {
                    required: true
                },
                beginTime: {
                    required: true
                },
                endTime: {
                    required: true
                }
            },
            messages: {
                paperName: {
                    required: "请输入试卷名称"
                },
                courseId: {
                    required: "请选择所属课程"
                },
                paperType: {
                    required: "请输入试卷类型"
                },
                major: {
                    required: "请输入考试专业班级"
                },
                beginTime: {
                    required: "请选择开始时间"
                },
                endTime: {
                    required: "请选择结束时间"
                }
            }
        });
    });
</script>
</body>
</html>

