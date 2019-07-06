$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sys/log/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            //还有一个table里面至少有一个是百分比布局或者是一个minWidth，否则在超大分辨率下面会有问题.
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'operation', width: 180, title: '用户操作', align: 'left', sort: true}
                , {field: 'username', minWidth: 100, title: '用户名', align: 'left', sort: true}
                , {
                    field: 'deviceRemark',
                    minWidth: 450,
                    title: '设备信息',
                    align: 'left',
                    sort: false
                }
                , {field: 'method', minWidth: 220, title: '请求方法', align: 'left', sort: false}
                , {field: 'params', minWidth: 220, title: '请求参数', align: 'left', sort: false}
                , {field: 'createDate', width: 170, title: '创建时间', align: 'center', sort: true}
                , {field: 'deviceInfo', width: 220, title: '设备型号', align: 'left', sort: false}
                , {field: 'sourceType', width: 120, title: '来源类型', align: 'left', fixed:'right', sort: false,templet:function (data) {
                        if (data.sourceType == 0) {
                            return '系统日志';
                        } else if (data.sourceType == 1) {
                            return '接口日志';
                        } else {
                            return '--';
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
});
var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			key: null,
            createDate_BETWEEN : null,
            operation:'',
            sourceType:'',
            userAgent:'',
            deviceInfo:''
		},
        operationList:null
	},
    mounted: function () {
        vm = this;
        //提前加载日志操作类型
        vm.getOperationList();
    },
	methods: {
		query: function () {
			vm.reload();
		},
        inits: function() {
            //这里同mounted 进行初始化 操作; 如果在mounted加载有问题的就放到这里，进行初始化
            //日期范围1
            //vm.initLayerUIDate();
            //日期范围2
            vm.initDaterangepicker();
        },
        getOperationList: function () {
            $.get(getWebPath() + "sys/log/operationList", function (r) {
                vm.operationList = r.data;
            });
        },
        initLayerUIDate: function() {
            layui.use('laydate', function () {
                var laydate = layui.laydate;
                laydate.render({
                    elem: '#createDate_BETWEEN'
                    ,range: true,
                    done : function (value, date, endDate) {
                        vm.q.createDate_BETWEEN = value;
                    }
                });
            });
        },
        initDaterangepicker: function() {
            $(document).ready(function () {
                $('#createDate_BETWEEN').daterangepicker(daterangepickerConfig, function (start, end, label) {
                    console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
                    var dateValue =  start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD');
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
		reload: function (sortObj) {
            var table = layui.table;

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
                    'key': vm.q.key,
                    'createDate_BETWEEN': vm.q.createDate_BETWEEN,
                    'operation': vm.q.operation,
                    'sourceType': vm.q.sourceType,
                    'userAgent': vm.q.userAgent,
                    'userAgent':vm.q.userAgent,
                    'deviceInfo':vm.q.deviceInfo,
                    sidx: aSortName //排序字段
                    , order: aSortValue//排序方式
                }
            });
		}
	}
});
vm.inits();