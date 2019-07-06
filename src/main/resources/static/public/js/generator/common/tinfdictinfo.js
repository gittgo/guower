$(function () {

    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'xadictinfo/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'code', width: '28%', title: '字典编码', align: 'left', sort: true}
                , {field: 'name', width: '16%', title: '字典名称', align: 'left', sort: true}
                , {field: 'typeCode', width: '20%', title: '类型代码', align: 'left', sort: true}
                , {field: 'typeName', width: '16%', title: '类型名称', align: 'center', sort: true}
                , {field: 'sort', width: '14%', title: '排序', align: 'left', sort: true}

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
});
var vm = new Vue({
	el:'#rrapp',
	data:{
		//查询条件，根据需要打开
		q:{
			name: '',
            code: '',
            type: ''
		},
		showList: 0,
		title: null,
		xaDictInfo: {},
        xaDitcypeInfo: [],
        flag:{},
        // showRemarks1: false,
        dictTypeUpdateCode: null //修改页面-字典类型-默认选中的
	},
    mounted: function () {
        //提前加载字典类型数据
        this.getDictTypeList();
    },
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = 1;
			vm.title = "新增";
			vm.xaDictInfo = {};
            vm.xaDictInfo.isHomePage = 0;
            vm.getDictTypeList();

            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
		update: function (event) {
            var selectRows = vm.getCheckData();
            if(selectRows.length != 1){
                alert("请选择一条记录");
                return;
            }
			vm.showList = 1;
            vm.title = "修改";
            
            vm.getInfo(selectRows[0].id)
            // if (vm.xaDictInfo.type == 'TYPE_STRATEGIC_COOPERATION' || vm.xaDictInfo.type == 'TYPE_COOPERATION_CONTENT'){
            //     vm.showRemarks1 = true;
            // }else {
            //     vm.showRemarks1 = false;
            // }
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
		},
        copy: function (event) {
            var selectRows = vm.getCheckData();
            if(selectRows.length != 1){
                alert("请选择一条记录");
                return;
            }

            vm.showList = 2;
            vm.title = "复制";
            vm.flag = 1;
            vm.getInfo(selectRows[0].id);
            $('#addOrUpdateForm').validator("cleanUp");
        },
		saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    // if(vm.flag == '1'){
                    //     vm.xaDictInfo.id = null;
                    // }
                    var url = vm.xaDictInfo.id == null ? "xadictinfo/save" : "xadictinfo/update";

                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.xaDictInfo),
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
            var isCanDel = true;
            var ids = [];
            for (var i = 0; i < selectRows.length; ++i) {
                ids.push(selectRows[i].id);
                debugger;
                if(selectRows[i].typeCode == 'TYPE_NEWS'){
                    $.ajax({
                        type: "POST",
                        url: getWebPath() +"busnews/list?newsType="+selectRows[i].remarks,
                        success: function(r){
                            if(r.code == 0){
                                if(r.page != null && r.page.list.length > 0){
                                    alert("已有使用"+selectRows[i].name+"类型的文章，不可删除");
                                    isCanDel = false;
                                    return ;
                                }
                            }else{
                                alert(r.msg);
                            }
                        },
                        async : false
                    });
                }
            }
            if(isCanDel){
                confirm('确定要删除选中的记录？', function(){
                    $.ajax({
                        type: "POST",
                        url: getWebPath() +"xadictinfo/delete",
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
            }
		},
		getInfo: function(id){
			$.get( getWebPath() + "xadictinfo/info/"+id, function(r){
                vm.xaDictInfo = r.xaDictInfo;
                // if (vm.xaDictInfo.type == 'TYPE_STRATEGIC_COOPERATION' || vm.xaDictInfo.type == 'TYPE_COOPERATION_CONTENT'){
                //     vm.showRemarks1 = true;
                // }else {
                //     vm.showRemarks1 = false;
                // }
                vm.dictTypeUpdateCode = r.xaDictInfo.type;
                if (isNull(vm.xaDitcypeInfo)) {
                    vm.getDictTypeList();
                }
            });
		},
        getDictTypeList: function(){
            $.get(getWebPath() + "xaditcypeinfo/list", function(r){
                vm.xaDitcypeInfo = r.page.list;
                if (vm.xaDitcypeInfo !== null && vm.xaDitcypeInfo.length !== 0) {
                    if ( isNull(vm.xaDictInfo.id) || isNull(vm.xaDictInfo) ) {//新增
                        vm.xaDictInfo.type = r.page.list[0].code;
                    } else {//修改
                        vm.xaDictInfo.type = vm.dictTypeUpdateCode;
                    }
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
            vm.showList = 0;
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
                    'code' : vm.q.code,
                    'type' : vm.q.type,
                    sidx: aSortName //排序字段
                    , order: aSortValue//排序方式
                }
            });
		},
        changeType: function () {
            if (vm.xaDictInfo.type == 'TYPE_NEWS'){
                $(".isHomePageDiv").show();
            }else {
                $(".isHomePageDiv").hide();
            }
        }
	}
});