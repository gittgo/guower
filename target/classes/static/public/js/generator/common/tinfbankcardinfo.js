$(function () {
    $("#jqGrid").jqGrid({
        url: getWebPath() + 'tinfbankcardinfo/list',
        datatype: "json",
        colModel: [			
			{ label: 'bankcardid', name: 'bankcardid', index: 'BankCardId', width: 50, key: true },
			{ label: '银行卡号', name: 'bankcardcode', index: 'BankCardCode', width: 80 }, 			
			{ label: '银行名称', name: 'bankcardname', index: 'BankCardName', width: 80 }, 			
			{ label: '司机/用户ID', name: 'userid', index: 'UserId', width: 80 }, 			
			{ label: '创建人', name: 'createuser', index: 'CreateUser', width: 80 }, 			
			{ label: '创建时间', name: 'createtime', index: 'CreateTime', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50,100],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		//查询条件，根据需要打开
		//q:{
		//	username: null
		//},
		showList: true,
		title: null,
		tInfBankCardInfo: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.tInfBankCardInfo = {};
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		update: function (event) {
			var bankcardid = getSelectedRow();
			if(bankcardid == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(bankcardid)
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    var url = vm.tInfBankCardInfo.bankcardid == null ? "tinfbankcardinfo/save" : "tinfbankcardinfo/update";
                    $.ajax({
                        type: "POST",
                        url:  getWebPath() + url,
                        data: JSON.stringify(vm.tInfBankCardInfo),
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
			var bankcardids = getSelectedRows();
			if(bankcardids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url:  getWebPath() + "tinfbankcardinfo/delete",
				    data: JSON.stringify(bankcardids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(bankcardid){
			$.get( getWebPath() + "tinfbankcardinfo/info/"+bankcardid, function(r){
                vm.tInfBankCardInfo = r.tInfBankCardInfo;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				//查询条件，根据需要打开
				//postData:{'username': vm.q.username},
                page:page
            }).trigger("reloadGrid");
		}
	}
});