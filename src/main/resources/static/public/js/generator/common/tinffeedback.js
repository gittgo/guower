$(function () {
    $("#jqGrid").jqGrid({
        url: getWebPath() + 'tinffeedback/list',
        datatype: "json",
        colModel: [			
			{ label: 'feedbackid', name: 'feedbackid', index: 'FeedbackId', width: 50, key: true },
			{ label: '反馈用户Id', name: 'feedbackuserid', index: 'FeedbackUserId', width: 80 }, 			
			{ label: '反馈内容', name: 'feedbackcontent', index: 'FeedbackContent', width: 80 }, 			
			/*{ label: '创建人', name: 'createuser', index: 'CreateUser', width: 80 }, */			
			{ label: '创建时间', name: 'createtime', index: 'CreateTime', width: 80 }, 			
		/*	{ label: '修改人', name: 'modifyuser', index: 'ModifyUser', width: 80 }, 	*/		
			{ label: '修改时间', name: 'modifytime', index: 'ModifyTime', width: 80 }			
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
		tInfFeedback: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.tInfFeedback = {};
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		update: function (event) {
			var feedbackid = getSelectedRow();
			if(feedbackid == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(feedbackid)
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    var url = vm.tInfFeedback.feedbackid == null ?  getWebPath() + "tinffeedback/save" : "tinffeedback/update";
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: JSON.stringify(vm.tInfFeedback),
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
			var feedbackids = getSelectedRows();
			if(feedbackids.length != 0){
				alert("请选择记录");
				return ;
			}
            var ids = [];
            for (var i = 0; i < feedbackids.length; ++i) {
                ids.push(feedbackids[i].feedbackid);
            }
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url:  getWebPath() + "tinffeedback/delete",
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
		getInfo: function(feedbackid){
			$.get( getWebPath() + "tinffeedback/info/"+feedbackid, function(r){
                vm.tInfFeedback = r.tInfFeedback;
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