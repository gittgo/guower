//jqGrid使用说明：http://www.cnblogs.com/dl201314/p/5070527.html
$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sys/schedule/list'
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'jobId', minWidth: 50, title: '任务ID', align: 'left', sort: true}
                , {field: 'beanName', minWidth: 150, title: 'bean名称', align: 'center', sort: true}
                , {field: 'methodName', minWidth: 160, title: '方法名称', align: 'center', sort: true}
                , {field: 'params', minWidth: 150, title: '参数', align: 'center', sort: true}
                , {field: 'name', minWidth: 150, title: 'cron表达式', align: 'center', sort: true}
                , {field: 'remark', minWidth: 100, title: '备注', align: 'center', sort: true}
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
	el:'#rrapp',
	data:{
		q:{
			beanName: null
		},
		showList: true,
		title: null,
		schedule: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.schedule = {};
            vm.schedule.cronExpressions = '';
		},
		update: function () {
            var jobId = vm.getCheckData();
            if (jobId.length != 1) {
                alert("请选择一条记录");
                return;
            }

            vm.schedule.cronExpressions = '';
			$.get("../sys/schedule/info/"+jobId[0].jobId, function(r){
				vm.showList = false;
                vm.title = "修改";
				vm.schedule = r.schedule;
                vm.schedule.cronExpressions = '';
			});
		},
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    subClick(event);
                    var url = vm.schedule.jobId == null ? "sys/schedule/save" : "sys/schedule/update";
                    //对象删除一个属性
                    delete  vm.schedule.cronExpressions;
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.schedule),
                        success: function(r){
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
            var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选择一条记录");
                return;
            }
            var jobIds = [];
            for (var i = 0; i < selectRows.length; ++i) {
                jobIds.push(selectRows[i].jobId);
            }
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: getWebPath() + "sys/schedule/delete",
				    data: JSON.stringify(jobIds),
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
		pause: function (event) {
            var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选择一条记录");
                return;
            }
            var jobIds = [];
            for (var i = 0; i < selectRows.length; ++i) {
                jobIds.push(selectRows[i].jobId);
            }

			confirm('确定要暂停选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url:getWebPath() +  "sys/schedule/pause",
				    data: JSON.stringify(jobIds),
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
		resume: function (event) {
            var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选择一条记录");
                return;
            }
            var jobIds = [];
            for (var i = 0; i < selectRows.length; ++i) {
                jobIds.push(selectRows[i].jobId);
            }

			confirm('确定要恢复选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: getWebPath() + "sys/schedule/resume",
				    data: JSON.stringify(jobIds),
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
		runOnce: function (event) {
            var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选择一条记录");
                return;
            }
            var jobIds = [];
            for (var i = 0; i < selectRows.length; ++i) {
                jobIds.push(selectRows[i].jobId);
            }

			confirm('确定要立即执行选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: getWebPath() + "sys/schedule/run",
				    data: JSON.stringify(jobIds),
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
        cronNotes: function () {
            var jobId = getSelectedRow();
            if(jobId == null){
                return ;
            }

            $.get(getWebPath() + "sys/schedule/cronNotes/"+jobId, function(r){
				if (r.code == 0) {
					var cornNote = r.cornNote;
					var json = JSON.parse(cornNote);
					alert(json["description"])
				} else {
					alert(r.msg);
				}
            });

            //var scheduleData = getSelectedRowData();
            //var cronExpression = scheduleData.cronExpression;
            //跨域
            //$.get("https://cronexpressiondescriptor.azurewebsites.net/api/descriptor/?locale=zh-CN&expression="+cronExpression, function(json){
            //    alert(json["description"]);
            //});
        },
        selectCronExpressions : function() {
            //选项cron表达式
            vm.$nextTick(function () {
                vm.schedule.cronExpression =  vm.schedule.cronExpressions;
                //vue强制刷新
                vm.$forceUpdate()
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
                    'beanName': vm.q.beanName,
                    sidx: aSortName //排序字段
                    ,order: aSortValue//排序方式
                }
            });
		}
	}
});