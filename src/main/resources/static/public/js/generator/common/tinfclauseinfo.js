$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'tinfclauseinfo/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'name', width: '42%', title: '文案类型', align: 'center', sort: true}
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

    var vm = new Vue({
        el: '#rrapp',
        data: {
            //查询条件，根据需要打开
            q:{
                name: null
            },
            showList: true,
            title: null,
            tInfClauseInfo: {},
            typeInfo: [],
            haveSaveOrUpdate: false,
            tInfClauseInfoUpdateTypeCode: null //修改页面-类型-默认选中的
        },
        mounted: function () {
            //提前加载文案分类列表数据
            this.getClauseTypeList();
        },
        methods: {
            inits: function() {

            },
            query: function () {
                vm.reload();
            },
            add: function () {
                vm.showList = false;
                vm.title = "新增";
                vm.tInfClauseInfo = {};
                vm.getClauseTypeList();
                var ue = UE.getEditor('ueditor');
                ue.setContent("");
                vm.tInfClauseInfo.clausetype = '';
            },
            update: function (event) {
                var selectRow = getLayerSelectedRow(table, 'jqGrid');
                if (selectRow == null) {
                    return;
                }
                vm.showList = false;
                vm.title = "修改";
                vm.tInfClauseInfo = selectRow;
                vm.getInfo(selectRow.clauseId);

                $('#addOrUpdateForm').validator("cleanUp");
            },
            saveOrUpdate: function (event) {
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        subClick(event);
                        vm.tInfClauseInfo.content = UE.getEditor('ueditor').getContent();
                        var url = vm.tInfClauseInfo.clauseId == null ? "tinfclauseinfo/save" : "tinfclauseinfo/update";
                        subClick(event);
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.tInfClauseInfo),
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
                    ids.push(selectRows[i].clauseId);
                }

                confirm('确定要删除选中的记录？', function () {
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "tinfclauseinfo/delete",
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
            getInfo: function (clauseId) {
                $.get(getWebPath() + "tinfclauseinfo/info/" + clauseId, function (r) {
                    vm.tInfClauseInfo = r.tInfClauseInfo;
                    vm.tInfClauseInfoUpdateTypeCode = vm.tInfClauseInfo.clausetype;
                    vm.$nextTick(function () {
                        var ue = UE.getEditor('ueditor');
                        var content = vm.tInfClauseInfo.content;
                        ue.ready(function () {
                            ue.setContent(content);
                        });
                    });
                    if ( isNull(vm.typeInfo) ) {
                        vm.getClauseTypeList();
                    }
                });
            },
            lookDetail: function() {

            },
            getCheckData: function () { //获取选中数据
                var table = layui.table;
                var checkStatus = table.checkStatus('jqGrid')
                    , data = checkStatus.data;
                return data;
            },
            getClauseTypeList: function () {
                $.get(getWebPath() + "xadictinfo/listByType?type=TYPE_CLAUS", function (r) {
                    vm.typeInfo = r.data;
                    if (vm.typeInfo !== null && vm.typeInfo.length !== 0) {
                        if ( isNull(vm.tInfClauseInfo) || isNull(vm.tInfClauseInfo.id) ) {//新增
                            vm.tInfClauseInfo.clausetype = '';
                        } else {//修改
                            vm.tInfClauseInfo.clausetype = vm.tInfClauseInfoUpdateTypeCode;
                        }
                    }
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
                //执行重载
                //1 //重新从第 1 页开始
                table.reload('jqGrid', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    initSort : aSort
                    , where: {//设定异步数据接口的额外参数，任意设
                        //查询条件，根据需要打开
                        'name': vm.q.name,

                        sidx: aSortName //排序字段
                        ,order: aSortValue//排序方式
                    }
                });
            }
        }
    });
    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();
});

