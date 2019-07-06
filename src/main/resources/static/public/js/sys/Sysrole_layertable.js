$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sys/role/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            //还有一个table里面至少有一个是百分比布局或者是一个minWidth，否则在超大分辨率下面会有问题.
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'roleId', width: 100, title: '角色ID', align: 'left', sort: true}
                , {field: 'roleName', minWidth: 120, title: '角色名称', align: 'left', sort: true}
                , {field: 'remark', minWidth: 220, title: '备注', align: 'left', sort: true}
                , {field: 'createTime', width: 170, title: '创建时间', align: 'center', sort: true}
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
	},
	check:{
		enable:true,
		nocheckInherit:true
	}
};
var ztree;
	
var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			roleName: null
		},
		showList: true,
		title:null,
        roleIsAdd:null,
        roleNameOldValue:null,
		role:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.role = {};
            vm.roleIsAdd = true;
            vm.roleNameOldValue = null;
			vm.getMenuTree(null);
		},
		update: function () {
            var selectRows = vm.getCheckData();
            if(selectRows.length != 1){
                alert("请选择一条记录");
                return;
            }else {
                vm.showList = false;
                vm.title = "修改";
                vm.roleIsAdd = false;

                vm.getMenuTree(selectRows[0].roleId);
            }
			

		},
		del: function (event) {
			var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选择记录！");
                return;
            } else {
                var roleIds = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    if(selectRows[i].roleId == 10 ){
                        alert("大区、居间商、代理角色无法删除")
                        return ;
                    }
                    roleIds.push(selectRows[i].roleId);
                }
                confirm('确定要删除选中的记录？', function(){
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "sys/role/delete",
                        data: JSON.stringify(roleIds),
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
			}
		},
		getRole: function(roleId){
            $.get(getWebPath() + "sys/role/info/"+roleId, function(r){
            	vm.role = r.role;

                vm.roleNameOldValue = r.role.roleName;

                //勾选角色所拥有的菜单
    			var menuIds = vm.role.menuIdList;
    			for(var i=0; i<menuIds.length; i++) {
    				var node = ztree.getNodeByParam("menuId", menuIds[i]);
    				ztree.checkNode(node, true, false);
    			}
    		});
		},
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    subClick(event);
                    //获取选择的菜单
                    var nodes = ztree.getCheckedNodes(true);
                    var menuIdList = new Array();
                    for(var i=0; i<nodes.length; i++) {
                        menuIdList.push(nodes[i].menuId);
                    }
                    vm.role.menuIdList = menuIdList;

                    var url = vm.role.roleId == null ? "sys/role/save" : "sys/role/update";
                    subClick(event);
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.role),
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
		getMenuTree: function(roleId) {
			//加载菜单树
			$.get(getWebPath() + "sys/menu/perms", function(r){
				ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
				//展开所有节点
				ztree.expandAll(true);
				
				if(roleId != null){
					vm.getRole(roleId);
				}
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
                    'roleName': vm.q.roleName,
                    sidx: aSortName //排序字段
                    , order: aSortValue//排序方式
                }
            });

		}
	}
});