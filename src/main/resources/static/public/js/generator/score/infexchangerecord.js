$(function () {

    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'infexchangerecord/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            //还有一个table里面至少有一个是百分比布局或者是一个minWidth，否则在超大分辨率下面会有问题.
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                //, {field: 'id', width: 120, title: 'id', align: 'center', sort: true}
                , {field: 'userName', minWidth: 120, title: '用户', align: 'center', sort: true}
                , {field: 'currencyName', minWidth: 120, title: '货币', align: 'center', sort: true}
                , {field: 'score', minWidth: 120, title: '消耗积分', align: 'center', sort: true}
                , {field: 'createDate', width: 200, title: '兑换时间', align: 'center', sort: true}
                , {field: 'sysUserName', minWidth: 120, title: '操作人', align: 'center', sort: true}
                , {field: 'purseAddress', minWidth: 1000, title: '钱包地址', align: 'center', sort: true}
                , {field: 'state', minWidth: 120, title: '状态',fixed:'right', align: 'center', sort: true,templet : function(data){
                        if(data.state == 1){
                            return '<span  class="label label-primary">兑换中</span>';
                        }else if(data.state == 2){
                            return '<span  class="label label-success">已完成</span>';
                        }else{
                            return '';
                        }
                    }}
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
                closeLoading();
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

    var vm = new Vue({
        el:'#rrapp',
        data:{
            //查询条件，根据需要打开
            q:{
                userId: ''
                ,currencyId: ''
                ,createDate_BETWEEN: ''
                ,sysUserId: ''
                ,state: ''
            },
            showList: 1,
            title: null,
            layerUiSortObj:null,
            haveSaveOrUpdate: false,
            infExchangeRecord: {},
            sysUsers : [],//发布人
            users : [],//用户
            currencys :[]//货币
        },
        mounted: function() {
            vm = this;
            //钩子函数、页面挂载完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx；比如页面初始化的时候，查询一些数据
            $.get(getWebPath() + "/sys/user/list",{}, function(r){
                vm.sysUsers = r.page.list;
                for (var i = 0; i < vm.sysUsers.length; i++) {
                    $("#selectSysUser").append("<option value='" + vm.sysUsers[i].userId + "'>" + vm.sysUsers[i].username + "</option>");
                }
                $(".selectpicker").selectpicker({noneSelectedText: '请选择发布人'});
                $(".selectpicker").selectpicker('refresh');
                $('#selectSysUser').selectpicker("val","");
            });
            $.get(getWebPath() + "/user/list",{}, function(r){
                vm.users = r.page.list;
                for (var i = 0; i < vm.users.length; i++) {
                    var ur = vm.users[i].nickName == null || vm.users[i].nickName == ''?'':'['+vm.users[i].nickName+']'
                    $("#selectUser").append("<option value='" + vm.users[i].id + "'>" + vm.users[i].userName + ur + "</option>");
                }
                $(".selectpicker").selectpicker({noneSelectedText: '请选择用户'});
                $(".selectpicker").selectpicker('refresh');
                $('#selectUser').selectpicker("val","");
            });
            $.get(getWebPath() + "/infcurrency/list",{}, function(r){
                vm.currencys = r.page.list;
                for (var i = 0; i < vm.currencys.length; i++) {
                    $("#selectCurrency").append("<option value='" + vm.currencys[i].id + "'>" + vm.currencys[i].currencyName + "</option>");
                }
                $(".selectpicker").selectpicker({noneSelectedText: '请选择货币'});
                $(".selectpicker").selectpicker('refresh');
                $('#selectCurrency').selectpicker("val","");
            });
        },
        computed: {
            //这里是vue计算属性
        },
        methods: {
            inits: function () {
                //这里同mounted 进行初始化 操作; 如果在mounted加载有问题的就放到这里，进行初始化
                vm.initDaterangepicker();
            },
            query: function () {
                vm.reload();
            },
            info: function () {
                //查看详情
                var selectRow = getLayerSelectedRow(table, 'jqGrid');
                if (selectRow == null) {
                    return;
                }
                vm.showList  == 2;
                vm.title = "兑换记录表";
                vm.infExchangeRecord = selectRow;

                //vm.getInfo(selectRow.id);//这里根据情况打开，如果是要管理查询就打开，否则不用重新请求后台

                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            grant : function (){
                var selectRows = getLayerSelectedRows(table, 'jqGrid');
                if(selectRows == null){
                    return ;
                }
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].id);
                }
                confirm('确定已发放？', function(){
                    showLoading();
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "infexchangerecord/grant",
                        data: JSON.stringify(ids),
                        success: function(r){
                            closeLoading();
                            if(r.code === 0){
                                alert('操作成功', function(index){
                                    vm.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                });
            },
            add: function(){
                vm.showList = false;
                vm.title = "新增";
                vm.infExchangeRecord = {};
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            update: function (event) {
                var selectRow = getLayerSelectedRow(table, 'jqGrid');
                if(selectRow == null){
                    return ;
                }
                vm.showList = 0;
                vm.title = "修改";

                vm.infExchangeRecord = selectRow;
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
                        showLoading(event);
                        var url = vm.infExchangeRecord.id == null ? "infexchangerecord/save" : "infexchangerecord/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.infExchangeRecord),
                            success: function(r){
                                changeClick(event);
                                closeLoading(event);
                                vm.haveSaveOrUpdate = true;
                                if(r.code === 0){
                                    alert('操作成功', function(index){
                                        vm.reload();
                                    });
                                }else{
                                    alert(r.msg);
                                }
                            }
                        });
                    }
                });
            },
            del: function (event) {
                //这里专门处理物理删除
                var selectRows = getLayerSelectedRows(table, 'jqGrid');
                if(selectRows == null){
                    return ;
                }
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].id);
                }

                confirm('确定要删除选中的记录？', function(){
                    showLoading();
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "infexchangerecord/delete",
                        data: JSON.stringify(ids),
                        success: function(r){
                            closeLoading();
                            if(r.code === 0){
                                alert('操作成功', function(index){
                                    vm.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                });
            },
            operateByIds: function(status) {
                //这里处理，发布、取消发布、删除、禁用等状态操作; 参数status由调用放传入
                var selectRows = getLayerSelectedRows(table, 'jqGrid');
                if(selectRows == null){
                    return ;
                }
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].id);
                }

                status = isNull(status) ? 3 : status;
                var tips = "请确定是否要进行操作??????";
                if (status === 2) {
                    tips = "您确定要发布吗?";
                } else if (status === 3) {
                    tips = "确定要删除选中的记录?";
                } else if (status === 1) {
                    tips = "请选择要撤消选中项吗？";
                    //tips = "请选择要启用选中项吗？";
                } else if (status === -1) {
                    tips = "确定要禁用选中项吗？";
                }

                confirm(tips, function(){
                    showLoading();
                    var data = "modelIds=" + ids.join(",") + "&status=" + status;
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "infexchangerecord/operateInfExchangeRecordByIds",
                        contentType: "application/x-www-form-urlencoded",
                        data: data,
                        success: function(r){
                            closeLoading();
                            if(r.code === 0){
                                alert('操作成功', function(index){
                                    vm.reload();
                                });
                            }else{
                                alert(r.msg);
                            }
                        }
                    });
                });
            },
            initDaterangepicker: function () {
                //这里初始化所有的日期范围选择，用不到的请自行删除
                                                                                                                                                                                                            $(document).ready(function () {
                //这里要手工给 日期文本框增加一个id, id 为：createDate_BETWEEN}
                $('#createDate_BETWEEN').daterangepicker(daterangepickerConfig, function (start, end, label) {
                        console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
                        var dateValue = start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD');
                        if ('全部' === label) {
                            vm.q.createDate_BETWEEN = '';
                        } else {
                            vm.q.createDate_BETWEEN = dateValue;
                        }
                        vm.reload();
                    })
                    //.click();
                });
            },
            exportsAllToExcel:function (isCvs) {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;

                //指定行导出
                var modelIds = "";
                {
                    var ids = getLayerSelectedRowsNoAlert(table, 'jqGrid');
                    if (ids != null && ids.length > 0) {
                        var idss = [];
                        for (var i= 0; i < ids.length; ++i) {
                            idss.push(ids[i].id);
                        }
                        modelIds = idss.join(",");
                    }
                }

                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q))  + "&sidx=" + aSortName + "&order=" + aSortValue  + "&isCvs=" + isCvs +"&modelIds=" + modelIds;
                location.href = getWebPath() + 'infexchangerecord/exportsExcelAll?' + data;
            },
            getInfo: function(id){
                //获取详情信息
                $.get(getWebPath() + "infexchangerecord/info/"+id, function(r){
                    vm.infExchangeRecord = r.infExchangeRecord;
                });
            },
            goBack: function () {
                //详情返回列表
                vm.showList = 1;
                vm.title = null;
                if (vm.haveSaveOrUpdate) {
                    vm.reload();
                    vm.haveSaveOrUpdate = false;
                }
            },
            reload: function (sortObj) {
                vm.showList = 1;
                var aSort = sortObj;
                var aSortName  = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                if (isNull(sortObj)) {

                }
                showLoading();
                //执行重载
                //1 //重新从第 1 页开始
                table.reload('jqGrid', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    initSort : aSort
                    , where: {//设定异步数据接口的额外参数，任意设
                        //查询条件，根据需要打开
                        //'name': vm.q.name,
                        // 'mobile': vm.q.mobile,
                        //这里是所有的筛选条件，没用的请自行删除，不用注释
                        'userId': vm.q.userId,
                        'currencyId': vm.q.currencyId,
                        'createDate_BETWEEN': vm.q.createDate_BETWEEN,
                        'sysUserId': vm.q.sysUserId,
                        'state': vm.q.state,
                        //end
                        sidx: aSortName //排序字段
                        ,order: aSortValue//排序方式
                    }
                });
            }
        }
        //自定添加的方法，请放到reload方法之前，reload统一放到方法最后面
    });
    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();
});