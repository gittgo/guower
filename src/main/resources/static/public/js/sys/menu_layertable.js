$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sys/menu/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            //还有一个table里面至少有一个是百分比布局或者是一个minWidth，否则在超大分辨率下面会有问题.
            , cols: [[
              {field: 'menuId', width: 100, title: '菜单ID', align: 'left', sort: true,rowspan: 2}
                , { title: '菜单信息', align: 'center', colspan: 5}
                , {field: 'orderNum', width: 80, title: '排序号', align: 'left', sort: true,rowspan: 2}

                , {field: 'createTime', width: 170, title: '创建时间', align: 'center', sort: true,rowspan: 2}
                , {field: 'type', width: 100, title: '类型', align: 'center', sort: true, rowspan: 2 ,templet:function (data) {
                        if(data.type === 0){
                            return '<span class="label label-primary">目录</span>';
                        }
                        if(data.type === 1){
                            return '<span class="label label-success">菜单</span>';
                        }
                        if(data.type === 2){
                            return '<span class="label label-warning">按钮</span>';
                        }
                    }}
                , {width: 120,height:75, title: '操作', toolbar: '#barDemo',rowspan: 2}
            ],[
                {field: 'name', width: 120, title: '菜单名称', align: 'left', sort: true}
                , {field: 'parentName', width: 120, title: '上级菜单', align: 'left', sort: true}
                , {field: 'icon', width: 80, title: '菜单图标', align: 'left', sort: true,templet:function (data) {
                        return data.icon == null ? '' : '<i class="'+data.icon+' fa-lg"></i>';
                    }}
                , {field: 'url', width: 150, title: '菜单URL', align: 'left', sort: true}
                , {field: 'perms', minWidth: 120, title: '授权标识', align: 'left', sort: true}
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
                // $(".layui-table-body").find("td").each(function (index,element) {
                //     if($(this).attr("data-field")>0){
                //         $(this).css("display","none");
                //     }
                // })
            }
        });
        //排序重新加载
        table.on('sort(jqGridFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            vm.layerUiSortObj = obj;
            //vm.reload(obj);
        });
        //监听工具条
        table.on('tool(jqGridFilter)', function(obj){
            var data = obj.data;
            var pa = "["+data.menuId+"]";
            if(obj.event === 'del'){
                layer.confirm('确定要删除选中的记录', function(index){
                    $.ajax({
                        type: "POST",
                        url: getWebPath() +  "sys/menu/delete",
                        data: pa,
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
                });
            } else if(obj.event === 'edit'){
                $.get(getWebPath() + "sys/menu/info/"+data.menuId, function(r){
                    vm.showList = false;
                    vm.title = "修改";
                    vm.menu = r.menu;

                    vm.getMenu();
                });
            }
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

var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "menuId",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	}
};
var ztree;

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			name: null,
			type:'',
			parentId:'',
            createTime_BETWEEN:'',
            updateTime_BETWEEN:'',
            parentMenus: []
		},
		showList: true,
		title: null,
		menu:{
			parentName:null,
			parentId:0,
			type:1,
			orderNum:0
		}
	},
    mounted:function(){
		this.getMenuCategory();
		this.initDaterangepicker();
    },
	methods: {
		getMenu: function(menuId){
			//加载菜单树
			$.get(getWebPath() + "sys/menu/select", function(r){
				ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
				var node = ztree.getNodeByParam("menuId", vm.menu.parentId);
				ztree.selectNode(node);
				
				vm.menu.parentName = node.name;
			})
		},
		query: function () {
			$('#menuLayer').hide();
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.menu = {parentName:null,parentId:0,type:1,orderNum:0};
			vm.getMenu();
		},
		update: function (event) {
			var menuId = getSelectedRow();
			if(menuId == null){
				return ;
			}
			
			$.get(getWebPath() + "sys/menu/info/"+menuId, function(r){
				vm.showList = false;
                vm.title = "修改";
                vm.menu = r.menu;
                
                vm.getMenu();
            });
		},
		del: function (event) {
			var menuIds = getSelectedRows();
			if(menuIds == null){
				return ;
			}
			var datas = JSON.stringify(menuIds);
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: getWebPath() +  "sys/menu/delete",
				    data: datas,
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
			});
		},
		getMenuCategory : function () {
			//筛选之-请选择菜单目录
            var url=getWebPath() + "sys/menu/catalogList";
            $.get(url,function(data){
                vm.parentMenus = data.catalogList;
                for (var i = 0; i < vm.parentMenus.length; i++) {
                    $("#qParentId").append("<option value='" + vm.parentMenus[i].menuId + "'>" + vm.parentMenus[i].name + "</option>");
                }
                $(".selectpicker").selectpicker({noneSelectedText: '请选择菜单目录'});
                $(".selectpicker").selectpicker('refresh');
                $('#qParentId').selectpicker("val","");
            })
        },
		saveOrUpdate: function (event) {
            //$('#addOrUpdateForm').isValid(function (result) {
                //if (!result) {
                //    return;
                //} else {
                    var url = vm.menu.menuId == null ? "sys/menu/save" : "sys/menu/update";
                     subClick(event);
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.menu),
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
                //}
            //});
		},
		menuTree: function(){
			layer.open({
				type: 1,
				offset: '50px',
				skin: 'layui-layer-molv',
				title: "选择菜单",
				area: ['300px', '450px'],
				shade: 0,
				shadeClose: false,
				content: jQuery("#menuLayer"),
				btn: ['确定', '取消'],
				btn1: function (index) {
					var node = ztree.getSelectedNodes();
					//选择上级菜单
					vm.menu.parentId = node[0].menuId;
					vm.menu.parentName = node[0].name;
					
					layer.close(index);
					$('#menuLayer').hide();
	            },
                end : function (){
                    $("#menuLayer").hide();
                }
			});
		},
        initDaterangepicker: function () {
            //这里初始化所有的日期范围选择，用不到的请自行删除
            $(document).ready(function () {
                //这里要手工给 日期文本框增加一个id, id 为：createDate_BETWEEN}
                $('#createTime_BETWEEN').daterangepicker(daterangepickerConfig, function (start, end, label) {
                    console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
                    var dateValue = start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD');
                    if ('全部' === label) {
                        vm.q.createTime_BETWEEN = '';
                    } else {
                        vm.q.createTime_BETWEEN = dateValue;
                    }
                    vm.reload();
                })
                //.click();
            });
            //end
            $(document).ready(function () {
                //这里要手工给 日期文本框增加一个id, id 为：createDate_BETWEEN}
                $('#updateTime_BETWEEN').daterangepicker(daterangepickerConfig, function (start, end, label) {
                    console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
                    var dateValue = start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD');
                    if ('全部' === label) {
                        vm.q.updateTime_BETWEEN = '';
                    } else {
                        vm.q.updateTime_BETWEEN = dateValue;
                    }
                    vm.reload();
                })
                //.click();
            });
        },
		reload: function (sortObj) {
            var table = layui.table;
            vm.showList = true;
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
                    'perms': vm.q.perms,
                    'type':vm.q.type,
                    'createTime_BETWEEN':vm.q.createTime_BETWEEN,
                    'updateTime_BETWEEN':vm.q.updateTime_BETWEEN,
                    'parentId':vm.q.parentId,
                    sidx: aSortName //排序字段
                    , order: aSortValue//排序方式
                }
            });
		}

	}
});

