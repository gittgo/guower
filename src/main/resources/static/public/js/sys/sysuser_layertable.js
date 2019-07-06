$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sys/user/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            //还有一个table里面至少有一个是百分比布局或者是一个minWidth，否则在超大分辨率下面会有问题.
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'userId', minWidth: 100, title: '用户ID', align: 'left', sort: true}
                , {field: 'username', minWidth: 120, title: '用户名', align: 'left', sort: true}
                , {field: 'mobile', width: 160, title: '手机号', align: 'left', sort: true}
                , {field: 'email', minWidth: 160, title: '邮箱', align: 'left', sort: true}
                , {field: 'createTime', width: 170, title: '创建时间', align: 'center', sort: true}
                , {field: 'status', width: 110, title: '状态', align: 'center', sort: true, fixed: 'right' , templet:function (data) {
						if(data.status == 0){
                           return '<span class="label label-danger">禁用</span>' ;
						}else{
                           return '<span class="label label-success">正常</span>';
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
			username: null
		},
		showList: 0,
		title:null,
		roleList:{},
        userinfoIsAdd:null,
        mobileOldValue:null,
        usernameOldValue:null,
		user:{
			status:1,
			roleIdList:[]
		},
        users:[],//用户列表
	},
    mounted:function(){
        vm = this;
        $.get(getWebPath() + "/user/list",{}, function(r){
            vm.users = r.page.list;
            for (var i = 0; i < vm.users.length; i++) {
                var ur = vm.users[i].nickName == null || vm.users[i].nickName == ''?'':'['+vm.users[i].nickName+']'
                $("#selectUser").append("<option value='" + vm.users[i].id + "'>" + vm.users[i].userName + ur + "</option>");
            }
            $(".selectpicker").selectpicker({noneSelectedText: '请选择用户'});
            $(".selectpicker").selectpicker('refresh');
            $('#selectUser').selectpicker("val","");

            for (var i = 0; i < vm.users.length; i++) {
                var ur = vm.users[i].nickName == null || vm.users[i].nickName == ''?'':'['+vm.users[i].nickName+']'
                $("#selectUser2").append("<option value='" + vm.users[i].id + "'>" + vm.users[i].userName + ur + "</option>");
            }
            $(".selectpicker").selectpicker({noneSelectedText: '请选择用户'});
            $(".selectpicker").selectpicker('refresh');
            $('#selectUser2').selectpicker("val","");
        });
    },
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
            $('#addOrUpdateForm').validator("cleanUp");
			vm.showList = 2;
			vm.title = "新增";
			vm.roleList = {};
			vm.user = {status:1,roleIdList:[]};
			vm.userinfoIsAdd = true;
            vm.mobileOldValue = null;
            vm.usernameOldValue = null;
            vm.user.author = '';
			//获取角色信息
			this.getRoleList();
		},
		update: function () {
            $('#addOrUpdateForm').validator("cleanUp");
			var selectRows = vm.getCheckData();
			if(selectRows.length != 1){
                alert("请选择一条记录");
                return;
			}else {
                vm.showList = 1;
                vm.title = "修改";
                vm.userinfoIsAdd = false;

                vm.getUser(selectRows[0].userId);
                //获取角色信息
                this.getRoleList();
			}
			

		},
		del: function () {
			var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选择记录！");
                return;
            } else {
                var userIds = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    userIds.push(selectRows[i].userId);
                }
                confirm('确定要删除选中的记录？', function(){
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "sys/user/delete",
                        data: JSON.stringify(userIds),
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
		saveOrUpdate: function (event) {
		    debugger;
            if (vm.user.userId == null){
                $('#UpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        subClick(event);
                        var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
                        subClick(event);
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.user),
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
            }else{
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        subClick(event);
                        var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
                        subClick(event);
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.user),
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
            }
		},
		getUser: function(userId){
			$.get(getWebPath() + "sys/user/info/"+userId, function(r){
				vm.user = r.user;
                vm.mobileOldValue = r.user.mobile;
                vm.usernameOldValue = r.user.username;
                $('#selectUser').selectpicker("val",vm.user.author);
			});
		},
		getRoleList: function(){
			$.get(getWebPath() + "sys/role/select", function(r){
				vm.roleList = r.list;
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
                    'username': vm.q.username,
                    sidx: aSortName //排序字段
                    , order: aSortValue//排序方式
                }
            });
		}
	}
});