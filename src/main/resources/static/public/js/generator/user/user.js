$(function () {

    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'user/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            //还有一个table里面至少有一个是百分比布局或者是一个minWidth，否则在超大分辨率下面会有问题.
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                //, {field: 'id', width: 120, title: 'id', align: 'center', sort: true}
                , {field: 'userName', minWidth: 150, title: '用户名', align: 'center', sort: true}
                , {field: 'headPortrait', minWidth: 120, title: '用户头像', align: 'center', sort: true,templet : function(data){
                        var imgList = '暂无头像';
                        var value = data.headPortrait;
                        if(value != undefined && value != ''){
                            imgList = '';
                            value.split(",").forEach(function(dom){
                                imgList += '<img width="45px" height="45px" style="margin-right: 5px" src="'+dom+'">';
                            });
                        }
                        return imgList;
                    }}
                , {field: 'userType', minWidth: 120, title: '用户类型', align: 'center', sort: true,templet : function(data){
                        if(data.userType == 1){
                            return 'app';
                        }else if(data.userType == 2){
                            return 'web';
                        }else{
                            return '';
                        }
                    }}
                , {field: 'userLevel', minWidth: 120, title: '用户认证', align: 'center', sort: true,templet : function(data){
                        if(data.userLevel == 1){
                            return '企业认证';
                        }else if(data.userLevel == 2){
                            return '作者认证';
                        }else if(data.userLevel == 3){
                            return '媒体认证';
                        }else{
                            return '';
                        }
                    }}
                , {field: 'tel', minWidth: 200, title: '手机号', align: 'center', sort: true}
                , {field: 'pushPosition', minWidth: 300, title: '推送位置', align: 'center', sort: true,templet : function(data){
                        var result = '';
                        if(data.pushPosition != null && data.pushPosition != ''){
                            var pushPositionArray = data.pushPosition.split(",");
                            for(i in pushPositionArray){
                                if(pushPositionArray[i] == 1){
                                    result += '专栏作者、';
                                }else if(pushPositionArray[i] == 2){
                                    result += '企业专栏、';
                                }else if(pushPositionArray[i] == 3){
                                    result += '作者排行、';
                                }
                            }
                            result = result.substr(0,result.lastIndexOf('、'));
                        }
                        return result;

                    }}
                // , {field: 'password', minWidth: 120, title: '密码', align: 'center', sort: true}
                , {field: 'nickName', minWidth: 150, title: '昵称', align: 'center', sort: true}
                , {field: 'newsNumber', minWidth: 120, title: '文章数', align: 'center', sort: true}
                , {field: 'totalBrowsing', minWidth: 120, title: '总浏览量', align: 'center', sort: true}
                , {field: 'score', minWidth: 120, title: '积分', align: 'center', sort: true}
                , {field: 'signature', minWidth: 120, title: '个性签名', align: 'center', sort: true}
                // , {field: 'sex', minWidth: 120, title: '性别【0.未知  1,男  2.女】', align: 'center', sort: true,templet : function(data){
                //         if(data.sex == 1){
                //             return '男';
                //         }else if(data.sex == 2){
                //             return '女';
                //         }else{
                //             return '未知';
                //         }
                //     }}
                , {field: 'pushOneSort', minWidth: 150, title: '专栏作者排行', align: 'center', sort: true}
                , {field: 'pushTwoSort', minWidth: 150, title: '企业专栏排行', align: 'center', sort: true}
                , {field: 'pushThreeSort', minWidth: 150, title: '作者排行排行', align: 'center', sort: true}
                // , {field: 'state', minWidth: 120, title: '状态', align: 'center', sort: true}
                , {field: 'createDate', minWidth: 200, title: '创建时间', align: 'center', sort: true}
                // , {field: 'userRemarks2', minWidth: 120, title: '暂留', align: 'center', sort: true}
                // , {field: 'userRemarks3', minWidth: 120, title: '暂留', align: 'center', sort: true}
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

    var vm = new Vue({
        el:'#rrapp',
        data:{
            //查询条件，根据需要打开
            q:{
                userName: ''
                ,tel: ''
                ,nickName: ''
                ,userType: ''
                ,userLevel: ''
                ,pushPosition: ''
                // ,state: null
                // ,createDate: null
            },
            showList: 1,
            title: null,
            layerUiSortObj:null,
            haveSaveOrUpdate: false,
            user: {}
        },
        mounted: function() {
            //钩子函数、页面挂载完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx；比如页面初始化的时候，查询一些数据
        },
        computed: {
            //这里是vue计算属性
        },
        methods: {
            inits: function () {
                //这里同mounted 进行初始化 操作; 如果在mounted加载有问题的就放到这里，进行初始化
                vm.initDaterangepicker();
            },
            query: function () {
                vm.reload();
            },
            info: function () {
                //查看详情
                var selectRow = getLayerSelectedRow(table, 'jqGrid');
                if (selectRow == null) {
                    return;
                }
                vm.showList  == 2;
                vm.title = "用户表";
                vm.user = selectRow;

                //vm.getInfo(selectRow.id);//这里根据情况打开，如果是要管理查询就打开，否则不用重新请求后台

                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            add: function(){
                vm.showList = false;
                vm.title = "新增";
                vm.user = {};
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            update: function (event) {
                var selectRow = getLayerSelectedRow(table, 'jqGrid');
                if(selectRow == null){
                    return ;
                }
                vm.showList = 0;
                vm.title = "修改";

                vm.user = selectRow;
                vm.getInfo(selectRow.id);
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            saveOrUpdate: function (event) {
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        vm.user.pushPosition = '';
                        $("#pushPositionDiv .layui-form-checked").each(function() { // 遍历推送位置选项
                            vm.user.pushPosition += $(this).attr("name");  // 每一个被选中项的值
                            vm.user.pushPosition += ",";  // 每一个被选中项的值

                        });
                        //限制：
                        //  未认证：不可推送
                        //  作者认证/媒体认证：可推专栏作者和作者排行
                        //  企业认证：可推企业专栏
                        if(vm.user.pushPosition.length > 1){
                            if(vm.user.userLevel == null || vm.user.userLevel == 0){
                                alert("该用户未认证，不可推送");
                                return ;
                            }
                            if(vm.user.userLevel == 1 && vm.user.pushPosition.indexOf('1') != -1){//企业
                                alert("该用户为企业认证，不可推送至专栏作者");
                                return ;
                            }
                            if(vm.user.userLevel == 1 && vm.user.pushPosition.indexOf('3') != -1){//企业
                                alert("该用户为企业认证，不可推送至作者排行");
                                return ;
                            }
                            if(vm.user.userLevel == 2 && vm.user.pushPosition.indexOf('2') != -1){
                                alert("该用户为作者认证，不可推送至企业专栏");
                                return ;
                            }
                            if(vm.user.userLevel == 3 && vm.user.pushPosition.indexOf('2') != -1){
                                alert("该用户为媒体认证，不可推送至企业专栏");
                                return ;
                            }
                        }
                        debugger
                        vm.user.headPortrait = $("#imgUrl").val();
                        //限制end
                        if(vm.user.pushPosition.length > 1){
                            vm.user.pushPosition = vm.user.pushPosition.substr(0,vm.user.pushPosition.lastIndexOf(','));
                        }
                        console.info(vm.user.pushPosition);
                        subClick(event);
                        showLoading(event);
                        var url = vm.user.id == null ? "user/save" : "user/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.user),
                            success: function(r){
                                changeClick(event);
                                closeLoading(event);
                                vm.haveSaveOrUpdate = true;
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
                //这里专门处理物理删除
                var selectRows = getLayerSelectedRows(table, 'jqGrid');
                if(selectRows == null){
                    return ;
                }
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].id);
                }

                confirm('确定要删除选中的记录？', function(){
                    showLoading();
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "user/delete",
                        data: JSON.stringify(ids),
                        success: function(r){
                            closeLoading();
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
            operateByIds: function(status) {
                //这里处理，发布、取消发布、删除、禁用等状态操作; 参数status由调用放传入
                var selectRows = getLayerSelectedRows(table, 'jqGrid');
                if(selectRows == null){
                    return ;
                }
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].id);
                }

                status = isNull(status) ? 3 : status;
                var tips = "请确定是否要进行操作??????";
                if (status === 2) {
                    tips = "您确定要发布吗?";
                } else if (status === 3) {
                    tips = "确定要删除选中的记录?";
                } else if (status === 1) {
                    tips = "请选择要撤消选中项吗？";
                    //tips = "请选择要启用选中项吗？";
                } else if (status === -1) {
                    tips = "确定要禁用选中项吗？";
                }

                confirm(tips, function(){
                    showLoading();
                    var data = "modelIds=" + ids.join(",") + "&status=" + status;
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "user/operateUserByIds",
                        contentType: "application/x-www-form-urlencoded",
                        data: data,
                        success: function(r){
                            closeLoading();
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
            initDaterangepicker: function () {
                //这里初始化所有的日期范围选择，用不到的请自行删除
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    //end
            },
            exportsAllToExcel:function (isCvs) {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;

                //指定行导出
                var modelIds = "";
                {
                    var ids = getLayerSelectedRowsNoAlert(table, 'jqGrid');
                    if (ids != null && ids.length > 0) {
                        var idss = [];
                        for (var i= 0; i < ids.length; ++i) {
                            idss.push(ids[i].id);
                        }
                        modelIds = idss.join(",");
                    }
                }

                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q))  + "&sidx=" + aSortName + "&order=" + aSortValue  + "&isCvs=" + isCvs +"&modelIds=" + modelIds;
                location.href = getWebPath() + 'user/exportsExcelAll?' + data;
            },
            getInfo: function(id){
                //获取详情信息
                $.get(getWebPath() + "user/info/"+id, function(r){
                    vm.user = r.user;
                    //勾选推送位置
                    if(vm.user.pushPosition != null){
                        var pushPositionArray = vm.user.pushPosition.split(",");
                        $("#pushPositionDiv .pushPositionCheckBox").removeClass("layui-form-checked");
                        for(i in pushPositionArray) {
                            var checkBoxItem = $("#pushPositionDiv .pushPositionCheckBox[name='"+pushPositionArray[i]+"']");
                            if(checkBoxItem!=undefined){
                                $(checkBoxItem).addClass("layui-form-checked");
                            }
                        }
                    }
                    //有图片则赋值
                    if(vm.user.headPortrait != null && vm.user.headPortrait != ''){
                        $("#myImageShow").attr("src",vm.user.headPortrait);
                        $("#imgUrl").val(vm.user.headPortrait);
                    }
                });
            },
            goBack: function () {
                //详情返回列表
                vm.showList = 1;
                vm.title = null;
                if (vm.haveSaveOrUpdate) {
                    vm.reload();
                    vm.haveSaveOrUpdate = false;
                }
            },
            reload: function (sortObj) {
                vm.showList = 1;
                var aSort = sortObj;
                var aSortName  = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                if (isNull(sortObj)) {

                }
                showLoading();
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
                        //这里是所有的筛选条件，没用的请自行删除，不用注释
                        'userName': vm.q.userName,
                        'tel': vm.q.tel,
                        'nickName': vm.q.nickName,
                        'userType': vm.q.userType,
                        'userLevel': vm.q.userLevel,
                        'pushPosition': vm.q.pushPosition,
                        // 'state': vm.q.state,
                        // 'createDate': vm.q.createDate,
                        //end
                        sidx: aSortName //排序字段
                        ,order: aSortValue//排序方式
                    }
                });
            }
        }
        //自定添加的方法，请放到reload方法之前，reload统一放到方法最后面
    });
    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();

    //绑定推送位置复选框事件
    $(document).on("click", ".pushPositionCheckBox", function () {
        if($(this).hasClass("layui-form-checked")){
            $(this).removeClass("layui-form-checked");
        }else{
            $(this).addClass("layui-form-checked");
        }
    });

});