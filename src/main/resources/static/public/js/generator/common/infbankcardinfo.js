$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'infbankcardinfo/list'
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                // , {field: 'userid', width: 120, title: '用户ID', sort: true}
                , {field: 'createuser', minWidth: 120, title: '用户', sort: true}
                // , {field: 'bankcardid', width: 120, title: '银行卡id', sort: true}
                , {field: 'bankcardcode', minWidth: 200, title: '银行卡号', sort: true}
                , {field: 'bankcardname', minWidth: 250, title: '银行名称', sort: true}
                , {field: 'createtime', minWidth: 200, title: '创建时间', sort: true}
            ]]
            , page: true,
            response: {
                statusName: 'code' //数据状态的字段名称，默认：code
                , statusCode: 0 //成功的状态码，默认：0
                , msgName: 'msg' //状态信息的字段名称，默认：msg
                , countName: 'layuiCount' //数据总数的字段名称，默认：count
                , dataName: 'layuiData' //数据列表的字段名称，默认：data
            }
        });

        table.on('sort(jqGridFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            //vm.reload(obj);
        });

        //监听复选框事件
        table.on('checkbox(jqGridFilter)', function(obj){
            //取消选中
            $('.layui-form-checkbox').each(function () {
                var tr =  $(this).parent().parent().parent();
                var selectIndex = tr.attr('data-index');
                $('tr[data-index='+ selectIndex +']').removeClass('layuiSucess');
            });

            //选中
            $('.layui-form-checked').each(function () {
                var tr =  $(this).parent().parent().parent();
                var selectIndex = tr.attr('data-index');
                $('tr[data-index='+ selectIndex +']').addClass('layuiSucess');
            });
        });

    });

    var vm = new Vue({
        el: '#rrapp',
        data: {
            //查询条件，根据需要打开
            q:{
                createuser: null,
                bankcardcode: null,
                bankcardname: null
            },
            showList: true,
            title: null,
            infBankCardInfo: {}
        },
        methods: {
            query: function () {
                vm.reload();
            },
            add: function () {
                vm.showList = false;
                vm.title = "新增";
                vm.infBankCardInfo = {};
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            update: function (event) {
                var bankcardid = getLayerSelectedRow(table, 'jqGrid');
                if (bankcardid == null) {
                    return;
                }
                vm.showList = false;
                vm.title = "修改";

                vm.getInfo(bankcardid);
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            saveOrUpdate: function (event) {
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        var url = vm.infBankCardInfo.bankcardid == null ? "infbankcardinfo/save" : "infbankcardinfo/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.infBankCardInfo),
                            success: function (r) {
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
                var bankcardids = getLayerSelectedRows(table, 'jqGrid');
                if (bankcardids == null) {
                    return;
                }

                confirm('确定要删除选中的记录？', function () {
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "infbankcardinfo/delete",
                        data: JSON.stringify(bankcardids),
                        success: function (r) {
                            if (r.code == 0) {
                                alert('操作成功', function (index) {
                                    $("#jqGrid").trigger("reloadGrid");
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                });
            },
            getInfo: function (bankcardid) {
                $.get(getWebPath() + "infbankcardinfo/info/" + bankcardid, function (r) {
                    vm.infBankCardInfo = r.infBankCardInfo;
                });
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
                        'createuser': vm.q.createuser,
                        'bankcardcode': vm.q.bankcardcode,
                        'bankcardname': vm.q.bankcardname,
                        sidx: aSortName //排序字段
                        , order: aSortValue//排序方式
                    }
                });
            }
        }
    });
});