$(function () {
    $("#jqGrid").jqGrid({
        url: getWebPath() + 'sys/menu/list',
        datatype: "json",
        colModel: [			
			{ label: '菜单ID', name: 'menuId', index: "menu_id", width: 60, key: true },
			{ label: '菜单名称', name: 'name', width: 100 },
			{ label: '上级菜单', name: 'parentName', sortable: false, width: 100 },
			{ label: '菜单图标', name: 'icon', sortable: false, width: 100, formatter: function(value, options, row){
				return value == null ? '' : '<i class="'+value+' fa-lg"></i>';
			}},
			{ label: '菜单URL', name: 'url', width: 100 },
			{ label: '授权标识', name: 'perms', width: 250 },
			{ label: '<span style="float: right">类型</span>', name: 'type', width: 50, align : 'right' ,formatter: function(value, options, row){
				if(value === 0){
					return '<span class="label label-primary">目录</span>';
				}
				if(value === 1){
					return '<span class="label label-success">菜单</span>';
				}
				if(value === 2){
					return '<span class="label label-warning">按钮</span>';
				}
			}},
			{ label: '排序号', name: 'orderNum', index: "order_num", width: 66},
            /**
			 * jq前端分页插件jqgrid
			 * http://blog.csdn.net/lze693/article/details/65633835
             */
            { label : "操作", name: "opertate",index: "menuId", width:66, sortable :false, hidden : false, formatter:function(cellvalue, options, rowObject){
                return ['<button type="button" class="btn btn-outline btn-default btn-xs btn-edit" data-id="'+options.rowId+'" title="编辑" ><i class="glyphicon glyphicon-edit" aria-hidden="true"></i></button>' +
				'<button type="button" class="btn btn-outline btn-default btn-xs btn-del" data-id="'+options.rowId+'" title="删除"><i class="glyphicon glyphicon-trash" aria-hidden="true"></i></button>'].join('');
            }}
            ],
		viewrecords: true, //是否要显示总记录数,可修改
        height: 385,
        rowNum: 10,
		rowList : [10,30,50,100],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth: true,//是否宽度开关
        shrinkToFit: true,//是否根据父宽度自动分配每列的宽度，如果列少于8列，建议设置true,否则设置为false
        multiselect: true,//多选开关
        scroll: 0, // 此属性开启，1是开启，类似app那种自动加载
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list", //Json数据
            page: "page.currPage",　//当前页
            total: "page.totalPage", //总页数
            records: "page.totalCount"//总记录数
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        //当表格所有数据都加载完成而且其他的处理也都完成时触发此事件，排序，翻页同样也会触发此事件
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            //列表按钮事件：修改
            $('.btn-edit').click(function () {
                //通过重置选择行的方式来解决这个问题，这个方法好用，比起上一个方法
                $("#jqGrid").jqGrid('resetSelection');
                var dataid=$(this).data("id");
                /**
				 * JqGrid选中行、取消选中行、获得选中行数据
				 * 设定选中行,可设定多行选中：
                 */
				$("#jqGrid").jqGrid('setSelection',dataid);
				vm.update();
            });
            //列表按钮事件：删除
            $('.btn-del').click(function () {
                $("#jqGrid").jqGrid('resetSelection');
                $("#jqGrid").jqGrid('setSelection',$(this).data("id"));
                vm.del();
			});
            //设置按钮权限:修改
            if (hasPermission('sys:menu:update')) {
				$('.btn-edit').css('display', 'inline');
			} else {
                $('.btn-edit').css('display', 'none');
			}
            //设置按钮权限:删除
            if (hasPermission('sys:menu:delete')) {
                $('.btn-del').css('display', 'inline');
            } else {
                $('.btn-del').css('display', 'none');
            }
        }
    });
    //jqGrid表头合并;当表头有冻结列设置的时候，有问题；
    //属性解释：useColSpanStyle:此属性为false时，标题头会占一正行，没有表头则空着。为true时当为空时，下面的额列标题会占用
	//该设置不要放到gridComplete里面，否则翻页的时候会出问题
    $("#jqGrid").jqGrid('setGroupHeaders', {
        useColSpanStyle: true,
        groupHeaders:[
            //startColumnName:表示开始的列名，numberOfColumns:规定从startColumnName开始往后的几列都共用一个大标题。titleText:表示大标题显示的名称
            {startColumnName:'name', numberOfColumns:5, titleText: '<strong>菜单信息</strong>'}
        ]
    })
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
					subClick(event);
                    var url = vm.menu.menuId == null ? "sys/menu/save" : "sys/menu/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.menu),
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
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{
					'name': vm.q.name,
					'perms': vm.q.perms,
					'type':vm.q.type,
					'createTime_BETWEEN':vm.q.createTime_BETWEEN,
					'updateTime_BETWEEN':vm.q.updateTime_BETWEEN,
					'parentId':vm.q.parentId
				},
                page:page
            }).trigger("reloadGrid");
		}
	}
});

