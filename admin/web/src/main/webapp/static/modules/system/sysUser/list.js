define([
    "dojo",
    "modules/common/tools/util",
    "modules/common/componet/datatableUtil",
    "modules/common/tools/datastore"
], function (dojo, util, table, datastore) {
    var module={
        constants: {
            listUrl: 'sysUser/queryList',
            getById: 'sysUser/queryById',
            deleteUrl: 'sysUser/deleteUser',
            queryRole: 'sysUser/querySysRole',
            restPwd:'sysUser/restPwd'
        },
        initPage: function () {
            module.__initDataTable();
            module.__gatherEvent();
        },

        __loadSelect:function () {
            $('#selectRole').select2({
                placeholder: "请选择角色",
                allowClear: true,
                ajax: {
                    url: module.constants.queryRole,
                    cache: true,
                    processResults: function (data) {
                        return {
                            results: data
                        };
                    }
                }
            });
        },
        __loadSelectUpdate:function () {
            $('#selectRoleUpdate').select2({
                placeholder: "请选择角色",
                allowClear: true,
                ajax: {
                    url: module.constants.queryRole,
                    cache: true,
                    processResults: function (data) {
                        return {
                            results: data
                        };
                    }
                }
            });
        },
        __gatherEvent:function(){
            //查询按钮
            $("#query").click(function () {
                module.datatable.fnReloadAjax();
            });
            //添加
            $("#add").bind("click", function () {
                layer.open({
                    area: ['500px', '500px'],
                    title: "添加用户",
                    type: 1,
                    content: $("#addWin"),
                    btn: ['添加', '关闭'],
                    success: function (layero, index) {
                        $("#addForm").resetForm();
                        $("#addForm").parsley().reset();
                        module. __loadSelect();
                    },
                    yes: function (layero, index) {
                        if ($('#addForm').parsley().validate()) {
                            $("#addForm").ajaxSubmit({
                                success: function (d) {
                                    if (d.code == 200) {
                                        layer.msg("添加数据成功");
                                        module.datatable.fnReloadAjax();
                                        layer.closeAll();
                                    } else {
                                        layer.msg(d.data);
                                    }
                                }
                            })
                        }
                    },
                    cancel: function (index, layero) {
                        layer.close(index);
                    }
                });
            });
        },
        __initDataTable:function () {
            var aoColumns = [
                {"mData": "id"},
                {"mData": "username"},
                {"mData": "name"},
                {"mData": "lastLogin"},
                {"mData": "loginIp"},
                {"mData": "outLoginTime"},
                {"mData": "roleName"},
                {"mData": null}
            ];
            var aoColumnDefs = [{
                "aTargets": [7],
                "mRender": function (a, b, c, d) {
                    return a.bttonOption;
                }
            }];
            var data = $("#csrfName").val() + "=" + $("#csrfToken").val() + "&menuId=" + $("#menuId").val();
            module.datatable = table.initPageTable($("#dataTables-example"), module.constants.listUrl + "?" + data, aoColumns, aoColumnDefs, module.handler.__queryHandler, module.handler.__initHandler);

        },
        handler: {
            __queryHandler:function (condition) {
                var username = $("#username").val();
                var name = $("#name").val();
                if (util.assertNotNullStr(username)) condition.username = username;
                if (util.assertNotNullStr(name)) condition.name = name;
            },
            __initHandler:function(){
                /**
                 * 删除
                 */
                $("#dataTables-example tbody").on("click", "button[name='del']", function () {
                    var table = $('#dataTables-example').DataTable();
                    var d = table.row($(this).parents('tr')).data();
                    var data = module.getTokenData();
                    dojo.mixin(data, {id: d.id});
                    layer.confirm("你确定要删除该数据吗？", function (index) {
                        util.post(module.constants.deleteUrl, data).then(function (data) {
                            layer.msg(data.message);
                            if (data.code == 200) {
                                module.datatable.fnReloadAjax();
                                layer.msg("删除成功！");
                            }
                        });
                    });
                });
                /**
                 * 修改
                 */
                $("#dataTables-example .update").click(function () {
                    var table = $('#dataTables-example').DataTable();
                    var data = table.row($(this).parents('tr')).data();
                    var dataUpdate = module.getTokenData();
                    dojo.mixin(dataUpdate, {id: data.id});
                    util.post(module.constants.getById, dataUpdate).then(function (data) {
                        $("#updateForm").parsley().reset();
                        if (data.code == 200) {
                            var d = data.data;
                            $("#usernameupdate").val(d.user.username);
                            $("#nameupdate").val(d.user.name);
                            $("#userId").val(d.user.id);
                            var option = "<option value='" + d.role.id + "' selected='selected'>" + d.role.role_name + "</option>";
                            $("#selectRoleUpdate").empty();
                            $("#selectRoleUpdate").append(option);
                            layer.open({
                                area: ['500px', '450px'],
                                title: "修改",
                                type: 1,
                                content: $("#myModal"),
                                btn: ['确定', '关闭'],
                                success:function (layero, index) {
                                    module. __loadSelectUpdate();
                                },
                                yes: function (layero, index) {
                                    if ($('#updateForm').parsley().validate()) {
                                        $("#updateForm").ajaxSubmit({
                                            success: function (d) {
                                                if (d.code == 200) {
                                                    layer.closeAll();
                                                    module.datatable.fnReloadAjax();
                                                } else {
                                                    layer.msg("更新数据失败");
                                                }
                                            }
                                        })
                                    }
                                },
                                cancel: function (index, layero) {
                                    layer.close(index);
                                }
                            });
                        } else {
                            layer.alert("获取数据失败");
                        }

                    });
                });
                /**
                 * 重置密码
                 */
                $("#dataTables-example tbody").on("click", "button[name='restPwd']", function () {
                    var table = $('#dataTables-example').DataTable();
                    var d = table.row($(this).parents('tr')).data();
                    var data = module.getTokenData();
                    dojo.mixin(data, {id: d.id});
                    layer.confirm("此操作会重置密码为：666666，是否继续？", function (index) {
                        util.post(module.constants.restPwd, data).then(function (data) {
                            layer.msg("重置成功");
                            layer.close(index);
                        }, "json");

                    });
                })
            }
        },

        /**
         * 获取权限token
         */
        getTokenData: function () {
            var csrfName = $("#csrfName").val();
            var csrfToken = $("#csrfToken").val();
            var data = {};
            data[csrfName] = csrfToken;
            return data;
        }

    };
    return module;

});