$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sys/scheduleLog/list'
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'logId', minWidth: 50, title: '日志ID', align: 'left', sort: true}
                , {field: 'jobId', minWidth: 50, title: '任务ID', align: 'left', sort: true}
                , {field: 'beanName', minWidth: 100, title: 'bean名称', align: 'center', sort: true}
                , {field: 'methodName', minWidth: 100, title: '方法名称', align: 'center', sort: true}
                , {field: 'params', minWidth: 100, title: '参数', align: 'center', sort: true}
                , {
                    field: 'status',
                    width: 100,
                    title: '状态',
                    align: 'center',
                    sort: true,
                    templet: function (data) {
                        if (data.status == 0){
                            return '<span class="label label-success">正常</span>';
                        } else{
                            return  '<span class="label label-danger">暂停</span>';
                        }
                    }
                },
				{field: 'times', minWidth: 70, title: '耗时(单位：毫秒)', align: 'center', sort: true}
                , {field: 'error', minWidth: 70, title: '错误信息', align: 'center', sort: true}
                , {field: 'createTime', minWidth: 80, title: '执行时间', align: 'center', sort: true}
                //{ label: '日志ID', name: 'logId', width: 50, key: true },
                // 	{ label: '任务ID', name: 'jobId', width: 50},
                // 	{ label: 'bean名称', name: 'beanName', width: 60 },
                // 	{ label: '方法名称', name: 'methodName', width: 60 },
                // 	{ label: '参数', name: 'params', width: 60 },
                // 	{ label: '状态', name: 'status', width: 50, formatter: function(value, options, row){
                // 		return value === 0 ?
                // 			'<span class="label label-success">成功</span>' :
                // 			'<span class="label label-danger pointer" onclick="vm.showError('+row.logId+')">失败</span>';
                // 	}},
                // 	{ label: '耗时(单位：毫秒)', name: 'times', width: 70 },
                // 	{ label: '错误信息', name: 'error', width: 70 },
                // 	{ label: '执行时间', name: 'createTime', width: 80 }
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
	el:'#rrapp',
	data:{
		q:{
			jobId: null,
            beanName: null,
            methodName: null
		}
	},
	methods: {
		query: function () {
		    vm.reload()
		},
		showError: function(logId) {
			$.get(getWebPath() + "sys/scheduleLog/info/"+logId, function(r){
				parent.layer.open({
				  title:'失败信息',
				  closeBtn:0,
				  content: r.log.error
				});
			});
		},
		back: function (event) {
			history.go(-1);
		},
        reload: function (sortObj) {
            var table = layui.table;
            vm.showList = 0;
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
                    //postData:{'jobId': vm.q.jobId,'methodName': vm.q.methodName,'beanName': vm.q.beanName},
                    'methodName': vm.q.methodName,
                    'beanName': vm.q.beanName,
                    sidx: aSortName //排序字段
                    , order: aSortValue//排序方式
                }
            });
        }
	}
});

