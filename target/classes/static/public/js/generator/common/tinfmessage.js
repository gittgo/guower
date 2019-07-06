$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'tinfmessage/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'messagetitle', width: '42%', title: '标题', align: 'left', sort: true}
                , {field: 'createtime', width: '50%', title: '创建时间', align: 'center', sort: true}
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

    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        //查询条件，根据需要打开
        q:{
            messagetitle: null
        },
        showList: true,
        title: null,
        tInfMessage: {}
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.tInfMessage = {};
            var ue = UE.getEditor('ueditor');
            ue.setContent("");
        },
        update: function (event) {
            var messageid = vm.getCheckData();
            if (messageid.length != 1) {
                alert("请选择一条记录");
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            $('#addOrUpdateForm').validator("cleanUp");
            vm.getInfo(messageid[0].messageid)
        },
        saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    subClick(event);
                    vm.tInfMessage.messagecontent = UE.getEditor('ueditor').getContent();
                    var url = vm.tInfMessage.messageid == null ? "tinfmessage/save" : "tinfmessage/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.tInfMessage),
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
            var messageids = vm.getCheckData();
            if (messageids.length == 0) {
                alert("请选择一条记录");
                return;
            }
            var ids = [];
            for (var i = 0; i < messageids.length; ++i) {
                ids.push(messageids[i].messageid);
            }
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: getWebPath() + "tinfmessage/delete",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (messageid) {
            $.get(getWebPath() + "tinfmessage/info/" + messageid, function (r) {
                vm.tInfMessage = r.tInfMessage;
                vm.$nextTick(function () {
                    var ue = UE.getEditor('ueditor');
                    var content = vm.tInfMessage.messagecontent;
                    ue.ready(function () {
                        ue.setContent(content);
                    });
                });
            });
        },
        getDictTypeList: function () {
            $.get(getWebPath() + "tinfmessage/targetUserList", function (r) {
                vm.xaPtInfo = r;
                if (vm.xaPtInfo !== null && vm.xaPtInfo.length !== 0) {
                    vm.xaPtInfo.type = r[0];
                }
                console.log(vm.xaPtInfo)
            });
        },
        getCheckData: function () { //获取选中数据
            var table = layui.table;
            var checkStatus = table.checkStatus('jqGrid')
                , data = checkStatus.data;
            return data;
        },
        reload: function (sortObj) {
            var table = layui.table;
            vm.showList = 1;
            var aSort = sortObj;
            var aSortName  = isNotnull(aSort) ? aSort.field : null;
            var aSortValue = isNotnull(aSort) ? aSort.type : null;
            if (isNull(sortObj)) {

            }
            //执行重载
            //1 //重新从第 1 页开始
            table.reload('jqGrid', {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                initSort : aSort
                , where: {//设定异步数据接口的额外参数，任意设
                    //查询条件，根据需要打开
                    'messagetitle': vm.q.messagetitle,
                    sidx: aSortName //排序字段
                    ,order: aSortValue//排序方式
                }
            });
        }
    }
});