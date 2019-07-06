$(function () {
    $("#jqGrid").jqGrid({
        url: getWebPath() + 'sys/user/list',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'userId', index: "user_id", width: 80, key: true, frozen : true },
			{ label: '用户名', name: 'username', width: 100 },
            { label: '手机号', name: 'mobile', width: 200 },
			{ label: '邮箱', name: 'email', width: 180 },
			{ label: '状态', name: 'status', width: 60 , align : 'center', formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '创建时间', name: 'createTime', index: "create_time", width: 200}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50,100],
        rownumbers: true,
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        shrinkToFit:true,//是否根据父宽度自动分配每列的宽度，如果列少于8列，建议设置true,否则设置为false
        autoScroll:true,
        pager: "#jqGridPager",
        scroll: 0, // 此属性开启，1是开启，类似app那种自动加载
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "auto" });
        	//jqgrid冻结列设置 1:冻结列必须在前面几列重头开始 2:必须设置下面的代码
            $("#jqGrid").jqGrid('setFrozenColumns');
        }
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
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = 2;
			vm.title = "新增";
			vm.roleList = {};
			vm.user = {status:1,roleIdList:[]};
			vm.userinfoIsAdd = true;
            vm.mobileOldValue = null;
            vm.usernameOldValue = null;

			//获取角色信息
			this.getRoleList();
		},
		update: function () {
			var userId = getSelectedRow();
			if(userId == null){
				return ;
			}
			
			vm.showList = 1;
            vm.title = "修改";
            vm.userinfoIsAdd = false;
			
			vm.getUser(userId);
			//获取角色信息
			this.getRoleList();
		},
		del: function () {
			var userIds = getSelectedRows();
			if(userIds == null){
				return ;
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
		},
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
				if (!result) {
					return;
				} else {
                    var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.user),
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
		getUser: function(userId){
			$.get(getWebPath() + "sys/user/info/"+userId, function(r){
				vm.user = r.user;
                vm.mobileOldValue = r.user.mobile;
                vm.usernameOldValue = r.user.username;
			});
		},
		getRoleList: function(){
			$.get(getWebPath() + "sys/role/select", function(r){
				vm.roleList = r.list;
			});
		},
		reload: function (event) {
			vm.showList = 0;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'username': vm.q.username},
                page:page
            }).trigger("reloadGrid");
		}
	}
});