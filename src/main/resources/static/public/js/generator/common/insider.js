$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'user/insiderlist'
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'username', width: 212, title: '用户名', align: 'center', sort: true}
                , {field: 'userTypes', width: 212, title: '用户类型', align: 'center', sort: true,templet:function (data) {
                    if(data.userTypes == '22'){
                        return '负责人';
                    }
                    if(data.userTypes == '23'){
                        return '合伙人';
                    }
                    if(data.userTypes == '24'){
                        return '栋楼长';
                    }
                }}
                , {field: 'mobile', width: 212, title: '手机号', align: 'center', sort: true}
                , {field: 'headportraitimg', width: 212, title: '用户头像', align: 'center', sort: true}
                , {field: 'sex', width: 212, title: '性别，0保密、1男、2女', align: 'center', sort: true,templet:function (data) {
                    if(data.sex == '0' || data.sex == null){
                        return '保密';
                    }
                    if(data.sex == '1'){
                        return '男';
                    }
                    if(data.sex == '2'){
                        return '女';
                    }
                }}
               , {field: 'remaindermoney', width: 212, title: '账户余额', align: 'center', sort: true,templet:function (data) {
                    if(data.reamindermoney == null || data.remaindermoney == '0'){
                        return '￥0元';
                    }
                    return '￥'+data.reamindermoney+'元';
                }}
                , {field: 'usercode', width: 212, title: '会员编码', align: 'center', sort: true}
                , {field: 'createtime', width: 212, title: '创建时间', align: 'center', sort: true}
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

    var vm = new Vue({
        el:'#rrapp',
        data:{
            //查询条件，根据需要打开
            //q:{
            //	username: null
            //	,mobile: null
            //},
            showList: true,
            title: null,
            layerUiSortObj:null,
            tbUser: {}
        },
        mounted: function() {
            //页面挂载完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx
        },
        methods: {
            query: function () {
                vm.reload();
            },
            add: function(){
                vm.showList = false;
                vm.title = "新增";
                vm.tbUser = {};
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            update: function (event) {
                var selectRow = getLayerSelectedRow(table, 'jqGrid');
                if(selectRow == null){
                    return ;
                }
                vm.showList = false;
                vm.title = "修改";

                vm.getInfo(selectRow.userid);
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            saveOrUpdate: function (event) {
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        subClick(event);
                        var url = vm.tbUser.userid == null ? "tbuser/save" : "tbuser/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.tbUser),
                            success: function(r){
                                changeClick(event);
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
                var selectRows = getLayerSelectedRows(table, 'jqGrid');
                if(selectRows == null){
                    return ;
                }
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].userid);
                }

                confirm('确定要删除选中的记录？', function(){
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "tbuser/delete",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
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
            exportsToExcel:function () {
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
                    for (var i= 0; i < ids.length; ++i) {
                        idss.push(ids[i].userid);
                    }
                    var modelIds = idss.join(",");
                    location.href = getWebPath() + 'tbuser/exportsExcel?modelIds='  + modelIds;
                }
            },
            exportsAllToCvs:function () {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q))  + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=true";
                location.href = getWebPath() + 'tbuser/exportsExcelAll?' + data;
            },
            exportsAllToExcel:function () {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q))  + "&sidx=" + aSortName + "&order=" + aSortValue  + "&isCvs=false";
                location.href = getWebPath() + 'tbuser/exportsExcelAll?' + data;
            },
            getInfo: function(userid){
                $.get(getWebPath() + "tbuser/info/"+userid, function(r){
                    vm.tbUser = r.tbUser;
                });
            },
            reload: function (sortObj) {
                vm.showList = true;
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
                        //'name': vm.q.name,
                        // 'mobile': vm.q.mobile,
                        sidx: aSortName //排序字段
                        ,order: aSortValue//排序方式
                    }
                });
            }
        }
    });
});