$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'infquestions/list'
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'title', width: '32%', title: '标题', align: 'left', sort: true}
                //, {field: 'content', width: 180, title: '内容', align: 'center', sort: true}
                , {field: 'createTime', width: '32%', title: '创建时间', align: 'center', sort: true}
                , {field: 'sort', width: '31%', title: '排序', align: 'center', sort: true}
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

    });

    var vm = new Vue({
        el: '#rrapp',
        data: {
            //查询条件，根据需要打开
            q:{
            	title: null
            },
            showList: true,
            title: null,
            layerUiSortObj: null,
            haveSaveOrUpdate: false,
            infQuestions: {}
        },
        mounted: function () {
            //页面挂载完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx
        },
        methods: {
            query: function () {
                vm.reload();
            },
            add: function () {
                vm.showList = false;
                vm.title = "新增";
                vm.infQuestions = {};
                var ue = UE.getEditor('ueditor');
                ue.ready(function() {
                    ue.setContent("");
                });
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
                        vm.infQuestions.content = UE.getEditor('ueditor').getContent();
                        var url = vm.infQuestions.id == null ? "infquestions/save" : "infquestions/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.infQuestions),
                            success: function (r) {
                                changeClick(event);
                                vm.haveSaveOrUpdate = true;
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
                        url: getWebPath() + "infquestions/delete",
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
                    location.href = getWebPath() + 'infquestions/exportsExcel?modelIds=' + modelIds;
                }
            },
            exportsAllToCvs: function () {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q)) + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=true";
                location.href = getWebPath() + 'infquestions/exportsExcelAll?' + data;
            },
            exportsAllToExcel: function () {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q)) + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=false";
                location.href = getWebPath() + 'infquestions/exportsExcelAll?' + data;
            },
            getInfo: function (id) {
                $.get(getWebPath() + "infquestions/info/" + id, function (r) {
                    vm.infQuestions = r.infQuestions;
                    var ue = UE.getEditor('ueditor');
                    var content = vm.infQuestions.content;
                    ue.ready(function () {
                        ue.setContent(content);
                    });
                });
            },
            goBack: function () {
                vm.showList = true;
                vm.title = null;
                if (vm.haveSaveOrUpdate) {
                    vm.reload();
                }
            },
            reload: function (sortObj) {
                vm.showList = true;
                var aSort = sortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                if (isNull(sortObj)) {

                }
                vm.haveSaveOrUpdate = false;
                //执行重载
                //1 //重新从第 1 页开始
                table.reload('jqGrid', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    initSort: aSort
                    , where: {//设定异步数据接口的额外参数，任意设
                        //查询条件，根据需要打开
                        'title': vm.q.title,
                        sidx: aSortName //排序字段
                        , order: aSortValue//排序方式
                    }
                });
            }
        }
    });
});