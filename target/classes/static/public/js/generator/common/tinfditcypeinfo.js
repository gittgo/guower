$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'xaditcypeinfo/list'
            // , height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'code', width: '31%', title: '类型编码', align: 'left', sort: true}
                , {field: 'name', width: '31%', title: '类型名称', align: 'center', sort: true}
                , {field: 'createTime', width: '32%', title: '创建时间', align: 'center', sort: true}
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
		//查询条件，根据需要打开
		q:{
			name: null
		},
		showList: true,
		title: null,
        isEdit:false,
		xaDitcypeInfo: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.xaDitcypeInfo = {};
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		update: function (event) {
            var selectRows = vm.getCheckData();
            if(selectRows.length != 1){
                alert("请选择一条记录");
                return;
            }
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(selectRows[0].id)
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    var url = vm.xaDitcypeInfo.id == null ? "xaditcypeinfo/save" : "xaditcypeinfo/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() +  url,
                        data: JSON.stringify(vm.xaDitcypeInfo),

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
            if(selectRows.length == 0){
                alert("请选择记录！");
                return;
            }
            var ids = [];
            for (var i = 0; i < selectRows.length; ++i) {
                ids.push(selectRows[i].id);
            }
			
			confirm('确定要删除字典分类，以及分类的字典数据？', function(){
				$.ajax({
					type: "POST",
				    url: getWebPath() + "xaditcypeinfo/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get( getWebPath() +  "xaditcypeinfo/info/"+id, function(r){
                vm.xaDitcypeInfo = r.xaDitcypeInfo;
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
            vm.showList = true;
            vm.showProList = false;
            vm.showInfo = false;
            vm.copyList = false;
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
                    'name': vm.q.name,
                    sidx: aSortName //排序字段
                    , order: aSortValue//排序方式
                }
            });
		}
	}
});