$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid',
            url: getWebPath() + 'sys/config/list'
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'} ,
                {type: 'checkbox', fixed: 'left'}
                // , {field: 'id', width: 130, title: 'ID', align: 'center', sort: true}
                , {field: 'key', width:  280, title: '参数名', align: 'left', sort: true}
                , {field: 'value', width: 120, title: '参数值', align: 'left', sort: true}
                , {field: 'code', width: 350 , title: '参数编码', align: 'left', sort: true}
                , {field: 'createTime', width: 180, title: '创建时间', align: 'center', sort: true}
                , {field: 'remark', minWidth: 350, title: '备注', align: 'left', sort: true}
                // , {field: 'updateTime', width: 180 , title: '修改时间', align: 'center', sort: true}
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
            code: null,
		},
		showList: 1,
		title: null,
		config: {},
        flag:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = 2;
			vm.title = "新增";
			vm.config = {};
		},
		update: function () {
            var selectRows = vm.getCheckData();
            if(selectRows.length != 1){
                alert("请选择一条记录");
                return;
            }
			
			$.get(getWebPath() + "sys/config/info/"+selectRows[0].id, function(r){
                vm.showList = 2;
                vm.title = "修改";
                vm.config = r.config;
            });
		},
        copy: function (event) {
            var selectRows = vm.getCheckData();
            if(selectRows.length != 1){
                alert("请选择一条记录");
                return;
            }

            $.get(getWebPath() + "sys/config/info/"+selectRows[0].id, function(r){
                vm.showList = 3;
                vm.title = "复制";
                vm.config = r.config;
            });
            vm.flag = '1';
        },
		del: function (event) {
            var selectRows = vm.getCheckData();
            if(selectRows.length == 0){
                alert("请选择记录！");
                return;
            }
            var ids = [];
            for (var i = 0; i < selectRows.length; ++i) {
                ids.push(selectRows[i].id);
            }

            confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: getWebPath() + "sys/config/delete",
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
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    if(vm.flag == '1'){
                        vm.config.id = null;
                    }
                    var url = vm.config.id == null ? "sys/config/save" : "sys/config/update";
                    $.ajax({
                        type: "POST",
                        url:getWebPath() +  url,
                        data: JSON.stringify(vm.config),
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
        getCheckData: function(){ //获取选中数据
            var table = layui.table;
            var checkStatus = table.checkStatus('jqGrid')
                ,data = checkStatus.data;
            return data;
        },
		reload: function (sortObj) {
            var table = layui.table;
            vm.showList = 1;
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
                    'code' : vm.q.code,
                    sidx: aSortName //排序字段
                    , order: aSortValue//排序方式
                }
            });
		}
	}
});