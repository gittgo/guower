$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'cfgsms/list'
            , height: 480
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'host', width: 200 , title: '接口地址', align: 'center', sort: false}
                , {field: 'appId', width: 200, title: 'appId', align: 'center', sort: false}
                , {field: 'secretKey', width: 200, title: 'secretKey', align: 'center', sort: false}
                , {field: 'status', width: 100, title: '状态', align: 'center', sort: false,
                    templet:function (data) {
                        if (data.status == 1) {
                            return "<span class=\"label label-success\">正常</span>";
                        }else if (data.status == 0) {
                            return "<span class=\"label label-danger\">禁用</span>";
                        }
                    }
                }
                , {field: 'isUse', minWidth: 100 , title: '使用状态', align: 'center', sort: false,
                    templet:function (data) {
                        if (data.isUse == 1) {
                            return "使用中"
                        }else{
                            return "未使用"
                        }

                    }
                }
                , {fixed: '', minWidth:150, align:'center', title: '操作', toolbar: '#balanceBar'}
            ]]
            , page: true
            , limits: [10, 30, 50, 100],
            response: {
                statusName: 'code' //数据状态的字段名称，默认：code
                , statusCode: 0 //成功的状态码，默认：0
                , msgName: 'msg' //状态信息的字段名称，默认：msg
                , countName: 'layuiCount' //数据总数的字段名称，默认：count
                , dataName: 'layuiData' //数据列表的字段名称，默认：data
            }
            , done: function (res, page, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(page);
                //console.log(limit);
                //得到数据总量
                //console.log(count);
            }
        });

        //排序重新加载
        table.on('sort(jqGridFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            vm.layerUiSortObj = obj;
            //vm.reload(obj);
        });

        //监听复选框事件
        table.on('checkbox(jqGridFilter)', function (obj) {

            //取消选中
            $('.layui-form-checkbox').each(function () {
                var tr = $(this).parent().parent().parent();
                var selectIndex = tr.attr('data-index');
                $('tr[data-index=' + selectIndex + ']').removeClass('layuiSucess');
            });

            //选中
            $('.layui-form-checked').each(function () {
                var tr = $(this).parent().parent().parent();
                var selectIndex = tr.attr('data-index');
                $('tr[data-index=' + selectIndex + ']').addClass('layuiSucess');
            });
        });

        table.on('tool(jqGridFilter)', function(obj){
            var data = obj.data;
            if(obj.event === 'showBalance'){
                vm.showBalance(data.id);
            }
        });

    });

    var vm = new Vue({
        el: '#rrapp',
        data: {
            //查询条件，根据需要打开
            //q:{
            //	username: null
            //	,mobile: null
            //},
            showList: true,
            title: null,
            layerUiSortObj: null,
            cfgSms: {}
        },
        mounted: function () {
            //页面挂着完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx
        },
        methods: {
            query: function () {
                vm.reload();
            },
            add: function () {
                vm.showList = false;
                vm.title = "新增";
                vm.cfgSms = {};
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            update: function (event) {
                var selectRow = getLayerSelectedRow(table, 'jqGrid');
                if (selectRow == null) {
                    return;
                }
                vm.showList = false;
                vm.title = "修改";

                vm.getInfo(selectRow.id);
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            saveOrUpdate: function (event) {
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        subClick(event);
                        var url = vm.cfgSms.id == null ? "cfgsms/save" : "cfgsms/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.cfgSms),
                            success: function (r) {
                                changeClick(event);
                                if (r.code === 0) {
                                    alert('操作成功', function (index) {
                                        vm.reload();
                                    });
                                } else {
                                    alert(r.msg);
                                }
                            }
                        });
                    }
                });
            },
            del: function (event) {
                var selectRows = getLayerSelectedRows(table, 'jqGrid');
                if (selectRows == null) {
                    return;
                }
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].id);
                }

                confirm('确定要删除选中的记录？', function () {
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "cfgsms/delete",
                        data: JSON.stringify(ids),
                        success: function (r) {
                            if (r.code == 0) {
                                alert('操作成功', function (index) {
                                    vm.reload();
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                });
            },
            exportsToExcel: function () {
                var ids = getLayerSelectedRows(table, 'jqGrid');
                if (ids == null) {
                    return;
                }

                if (ids.length == 0) {
                    alert("请选择你要导出的数据!");
                }
                //导出选中项
                else if (ids.length > 0) {
                    var idss = [];
                    for (var i = 0; i < ids.length; ++i) {
                        idss.push(ids[i].id);
                    }
                    var modelIds = idss.join(",");
                    location.href = getWebPath() + 'cfgsms/exportsExcel?modelIds=' + modelIds;
                }
            },
            exportsAllToCvs: function () {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q)) + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=true";
                location.href = getWebPath() + 'cfgsms/exportsExcelAll?' + data;
            },
            exportsAllToExcel: function () {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q)) + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=false";
                location.href = getWebPath() + 'cfgsms/exportsExcelAll?' + data;
            },
            getInfo: function (id) {
                $.get(getWebPath() + "cfgsms/info/" + id, function (r) {
                    vm.cfgSms = r.cfgSms;
                });
            },
            showBalance: function (smsId) {
               showLoading();
                $.ajax({
                    type: 'get',
                    dataType: 'json',
                    url: getWebPath() + "cfgsms/balance/"+ smsId,
                    success: function (r) {
                        closeLoading();
                        if (r.code === 0) {
                            layer.msg('当前账号余额为:' + r.balance);
                        } else {
                            layer.msg(r.msg);
                        }
                    },
                    error: function (r) {
                        console.log(r);
                    }
                })
            },
            reload: function (sortObj) {
                vm.showList = true;
                var aSort = sortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                if (isNull(sortObj)) {

                }
                //执行重载
                //1 //重新从第 1 页开始
                table.reload('jqGrid', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    initSort: aSort
                    , where: {//设定异步数据接口的额外参数，任意设
                        //查询条件，根据需要打开
                        //'name': vm.q.name,
                        // 'mobile': vm.q.mobile,
                        sidx: aSortName //排序字段
                        , order: aSortValue//排序方式
                    }
                });
            }
        }
    });
});