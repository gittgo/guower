$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'infcoupons/list'
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                                                            , {field: 'couponid', width: 120, title: 'couponid', align: 'center', sort: true}
                                                                                , {field: 'discountname', width: 120, title: '优惠券名称', align: 'center', sort: true}
                                                                                , {field: 'condition', width: 120, title: '使用条件(满多少金额使用)', align: 'center', sort: true}
                                                                                , {field: 'discountmoney', width: 120, title: '优惠券金额', align: 'center', sort: true}
                                                                                , {field: 'usestatus', width: 120, title: '使用方式，预留', align: 'center', sort: true}
                                                                                , {field: 'source', width: 120, title: '领取途径，预留', align: 'center', sort: true}
                                                                                , {field: 'termofvalidity', width: 120, title: '有效期,注意是当天的最后一一秒', align: 'center', sort: true}
                                                                                , {field: 'createuser', width: 120, title: '创建人', align: 'center', sort: true}
                                                                                , {field: 'createtime', width: 120, title: '创建时间', align: 'center', sort: true}
                                                                                , {field: 'rangeshopbigtype', width: 120, title: '指定范围-商品大类id；说明：大类、小类、店铺id只能三选一', align: 'center', sort: true}
                                                                                , {field: 'rangeshopsmalltype', width: 120, title: '指定范围-商品小类id', align: 'center', sort: true}
                                                                                , {field: 'rangedirect', width: 120, title: '指定范围-是否是学有惠直营商品，1直营，其他：具体的店铺id', align: 'center', sort: true}
                                                                                , {field: 'rangegoodsid', width: 120, title: '指定访问-指定商品id', align: 'center', sort: true}
                                                                                , {field: 'status', width: 120, title: '状态，参见全系统常量，1：正常 2：发布 3:删除; 发布过的不能重新编辑', align: 'center', sort: true}
                                                                                , {field: 'remarks', width: 120, title: '扩展字段', align: 'center', sort: true}
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
        table.on('checkbox(jqGridFilter)', function(obj){

            //取消选中
            $('.layui-form-checkbox').each(function () {
                var tr =  $(this).parent().parent().parent();
                var selectIndex = tr.attr('data-index');
                $('tr[data-index='+ selectIndex +']').removeClass('layuiSucess');
            });

            //选中
            $('.layui-form-checked').each(function () {
                var tr =  $(this).parent().parent().parent();
                var selectIndex = tr.attr('data-index');
                $('tr[data-index='+ selectIndex +']').addClass('layuiSucess');
            });
        });

    });

var vm = new Vue({
	el:'#rrapp',
	data:{
		//查询条件，根据需要打开
		//q:{
		//	username: null
		//	,mobile: null
		//},
		showList: true,
		title: null,
        layerUiSortObj:null,
		infCoupons: {}
	},
    mounted: function() {
        //页面挂载完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx
    },
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.infCoupons = {};
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		update: function (event) {
			var selectRow = getLayerSelectedRow(table, 'jqGrid');
			if(selectRow == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(selectRow.couponid);
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    subClick(event);
                    var url = vm.infCoupons.couponid == null ? "infcoupons/save" : "infcoupons/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.infCoupons),
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
		del: function (event) {
			var selectRows = getLayerSelectedRows(table, 'jqGrid');
			if(selectRows == null){
				return ;
			}
			var ids = [];
			for (var i = 0; i < selectRows.length; ++i) {
                ids.push(selectRows[i].CouponId);
            }

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: getWebPath() + "infcoupons/delete",
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
        exportsToExcel:function () {
            var ids = getLayerSelectedRows(table, 'jqGrid');
            if (ids == null) {
                return;
            }

            if (ids.length == 0) {
                alert("请选择你要导出的数据!");
            }
            //导出选中项
            else if (ids.length > 0) {
                var idss = [];
                for (var i= 0; i < ids.length; ++i) {
                    idss.push(ids[i].CouponId);
                }
                var modelIds = idss.join(",");
                location.href = getWebPath() + 'infcoupons/exportsExcel?modelIds='  + modelIds;
            }
        },
        exportsAllToCvs:function () {
            var aSort = vm.layerUiSortObj;
            var aSortName = isNotnull(aSort) ? aSort.field : null;
            var aSortValue = isNotnull(aSort) ? aSort.type : null;
            var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q))  + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=true";
            location.href = getWebPath() + 'infcoupons/exportsExcelAll?' + data;
        },
        exportsAllToExcel:function () {
            var aSort = vm.layerUiSortObj;
            var aSortName = isNotnull(aSort) ? aSort.field : null;
            var aSortValue = isNotnull(aSort) ? aSort.type : null;
            var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q))  + "&sidx=" + aSortName + "&order=" + aSortValue  + "&isCvs=false";
            location.href = getWebPath() + 'infcoupons/exportsExcelAll?' + data;
        },
		getInfo: function(couponid){
			$.get(getWebPath() + "infcoupons/info/"+couponid, function(r){
                vm.infCoupons = r.infCoupons;
            });
		},
		reload: function (sortObj) {
			vm.showList = true;
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
                    //'name': vm.q.name,
                    // 'mobile': vm.q.mobile,
                    sidx: aSortName //排序字段
                    ,order: aSortValue//排序方式
                }
            });
		}
	}
});
});