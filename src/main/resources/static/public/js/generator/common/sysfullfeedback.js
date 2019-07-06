$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sysfullfeedback/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'feedbacksourceName', width: '15%', title: '反馈平台', align: 'center', sort: true}
                , {field: 'feedbackusername', width: '15%', title: '反馈人', align: 'center', sort: true}
                , {field: 'feedbackcontent', width: '17%', title: '意见内容', align: 'center', sort: true}
                , {field: 'tel', width: '15%', title: '联系电话', align: 'center', sort: true}
                , {field: 'createDate', width: '15%', title: '反馈时间', align: 'center', sort: true}
                , {field: 'status', width: '15%', title: '状态', align: 'center', sort: true, templet : function(data) {
                    return data.status == 0 ?
                        '<span class="label label-danger">待处理</span>' :
                        '<span class="label label-success">已处理</span>';
                    }
                }
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
        q: {
            feedbackusername: '',
            feedbacksource: '',
            status:''
        },
        showList: true,
        title: null,
        deviceInfos: {},
        sysFullFeedback: {}
    },
    mounted:function(){
        this.getDeviceList();
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.sysFullFeedback = {};
        },
        update: function (event) {
            var id = vm().getCheckData();
            if (id.length != 1) {
                alert("请选择一条记录");
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    subClick(event);
                    var url = vm.sysFullFeedback.id == null ? "sysfullfeedback/save" : "sysfullfeedback/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.sysFullFeedback),
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
            var lock = false; //默认未锁定
            var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选记录");
                return;
            }
            var ids = [];
            for (var i = 0; i < selectRows.length; ++i) {
                ids.push(selectRows[i].id);
            }
            confirm('确定要删除选中的记录？', function () {
                if(!lock){
                    lock = true;
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "sysfullfeedback/delete",
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
                }

            });
        },
        handle: function (event) {
            var lock = false;
            var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选记录");
                return;
            }
            var ids = [];
            for (var i = 0; i < selectRows.length; ++i) {
                ids.push(selectRows[i].id);
            }
            confirm('确定反馈已处理完成？', function () {
                if(!lock){
                    lock = true;
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "sysfullfeedback/handle",
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
                }

            });
        },
        getInfo: function (id) {
            $.get(getWebPath() + "sysfullfeedback/info/" + id, function (r) {
                vm.sysFullFeedback = r.sysFullFeedback;
            });
        },
        getDeviceList: function(){
            $.get(getWebPath() + "xadictinfo/deviceList", function(r){
                vm.deviceInfos = r;
                if (vm.deviceInfos !== null && vm.deviceInfos.length !== 0) {
                    vm.infVersionInfo.device = r[0];
                }
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
                    'feedbackusername': vm.q.feedbackusername,
                    'feedbacksource':vm.q.feedbacksource,
                    'status':vm.q.status,
                    sidx: aSortName //排序字段
                    ,order: aSortValue//排序方式
                }
            });
        }
    }
});