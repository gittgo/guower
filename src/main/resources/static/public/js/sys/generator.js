$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sys/generator/list'
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'tableName', width: 288, title: '表名', align: 'left', sort: true}
                , {field: 'engine', width: 120, title: 'engine', align: 'center', sort: true}
                , {field: 'tableComment', minWidth: 100, title: '表备注', align: 'left', sort: true}
                , {
                    field: 'createTime',
                    width: 180,
                    title: '创建时间',
                    align: 'center',
                    sort: true,
                    templet: function (data) {
                        return dateFns.format(data.createTime, 'YYYY-MM-DD hh:mm:ss');
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
    });
    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();

});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            tableName: null
            , createDate_BETWEEN: null
        }
    },
    mounted: function () {
        vm = this;
    },
    methods: {
        inits: function () {
            //这里同mounted 进行初始化 操作; 如果在mounted加载有问题的就放到这里，进行初始化
            //日期范围1
            //vm.initLayerUIDate();
            //日期范围2
            vm.initDaterangepicker();
        },
        generator: function () {
            var tableNames = vm.getCheckData();
            if (tableNames == null) {
                return;
            }
            var ids = [];
            for (var i = 0; i < tableNames.length; ++i) {
                ids.push(tableNames[i].tableName);
            }
            location.href = getWebPath() + "sys/generator/code?tables=" + JSON.stringify(ids);
        },
        initLayerUIDate: function () {
            layui.use('laydate', function () {
                var laydate = layui.laydate;
                laydate.render({
                    elem: '#createDate_BETWEEN'
                    , range: true,
                    done: function (value, date, endDate) {
                        vm.q.createDate_BETWEEN = value;
                    }
                });
            });
        },
        initDaterangepicker: function () {
            $('#createDate_BETWEEN').daterangepicker(daterangepickerConfig, function (start, end, label) {
                console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
                var dateValue = start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD');
                if ('全部' === label) {
                    vm.q.createDate_BETWEEN = '';
                } else {
                    vm.q.createDate_BETWEEN = dateValue;
                }
                vm.query();
            });
            //.click();
            vm.q.createDate_BETWEEN = '';
        },
        //获取选中数据
        getCheckData: function () {
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
                    // 'username': vm.q.username,
                    'tableName': vm.q.tableName,
                    'tableComment': vm.q.tableComment,
                    'createDate_BETWEEN': vm.q.createDate_BETWEEN,
                    'updateTime_BETWEEN': vm.q.updateTime_BETWEEN,
                    sidx: aSortName //排序字段
                    ,order: aSortValue//排序方式
                }
            });
        },
        query: function () {
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'tableName': vm.q.tableName,
                    'tableComment': vm.q.tableComment,
                    'createDate_BETWEEN': vm.q.createDate_BETWEEN,
                    'updateTime_BETWEEN': vm.q.updateTime_BETWEEN
                },
                page: 1
            }).trigger("reloadGrid");
        }
    }
});