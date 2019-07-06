$(function () {

    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'busnews/list?'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            //还有一个table里面至少有一个是百分比布局或者是一个minWidth，否则在超大分辨率下面会有问题.
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'isRelease', width: 120, title: '状态', align: 'center', sort: true,templet : function(data){
                        var str = "";
                        var id = data.id;
                        if(data.examineType == 0){
                            return '<span  class="label label-primary">审核中</span>';
                        }else if(data.examineType == 2){
                            return '<span class="label label-danger">审核未通过</span>';
                        }else{
                            if(Number(data.isRelease) == 1){
                                str = "layui-form-checked"
                            }
                            return '<div id="UpDownBtn" name="'+id+'" class="layui-table-cell laytable-cell-1-lock"><div class="layui-unselect layui-form-checkbox '+str+'"><span>发布</span><i class="layui-icon layui-icon-ok">√</i></div> </div>';
                        }
                    }}
                , {field: 'title', minWidth: 200, title: '标题', align: 'center', sort: true}
                , {field: 'smallTitle', minWidth: 200, title: '副标题', align: 'center', sort: true}
                // , {field: 'image', minWidth: 120, title: '大图标', align: 'center', sort: true}
                // , {field: 'smallImage', minWidth: 120, title: '小图标', align: 'center', sort: true}
                , {field: 'tag', minWidth: 120, title: '文章标签', align: 'center', sort: true,templet : function(data){
                        if(data.tag == 1){
                            return 'HOT';
                        }else if(data.tag == 2){
                            return 'NEW';
                        }else{
                            return '无标签';
                        }
                    }}
                // , {field: 'tag1', minWidth: 120, title: '内容标签1', align: 'center', sort: true}
                // , {field: 'tag2', minWidth: 120, title: '内容标签2', align: 'center', sort: true}
                // , {field: 'tag3', minWidth: 120, title: '内容标签3', align: 'center', sort: true}
                // , {field: 'mainText', minWidth: 120, title: '正文【详情】', align: 'center', sort: true}
                , {field: 'newsTypeName', minWidth: 150, title: '文章类型', align: 'center', sort: true}
                , {field: 'lookTimes', minWidth: 120, title: '阅读量', align: 'center', sort: true}
                , {field: 'authorName', minWidth: 150, title: '作者', align: 'center', sort: true}
                , {field: 'responsibleEditor', minWidth: 120, title: '责任编辑', align: 'center', sort: true}
                , {field: 'releaseType', minWidth: 120, title: '发布类型', align: 'center', sort: true,templet : function(data){
                        if(data.releaseType == 1){
                            return '后台';
                        }else if(data.releaseType == 2){
                            return '作者';
                        }else{
                            return '';
                        }
                    }}
                , {field: 'isAdvertisement', minWidth: 120, title: '是否为广告', align: 'center', sort: true,templet : function(data){
                        if(data.isAdvertisement == 1){
                            return '广告';
                        }else if(data.isAdvertisement == 0){
                            return '非广告';
                        }else{
                            return '';
                        }
                    }}
                , {field: 'isHotspot', minWidth: 120, title: '是否上热点', align: 'center', sort: true,templet : function(data){
                        if(data.isHotspot == 1){
                            return '热点';
                        }else if(data.isHotspot == 0){
                            return '非热点';
                        }else{
                            return '';
                        }
                    }}
                , {field: 'releaseUserName', minWidth: 150, title: '发布人', align: 'center', sort: true}
                , {field: 'releaseDate', minWidth: 200, title: '发布时间', align: 'center', sort: true}
                // , {field: 'isRelease', minWidth: 120, title: '暂留', align: 'center', sort: true}
                // , {field: 'newsRemarks2', minWidth: 120, title: '暂留', align: 'center', sort: true}
                // , {field: 'newsRemarks3', minWidth: 120, title: '暂留', align: 'center', sort: true}
                // , {field: 'sort', minWidth: 120, title: '排序', align: 'center', sort: true}
                // , {field: 'sortTime', minWidth: 120, title: '排序到期时间', align: 'center', sort: true}
                // , {field: 'examineType', minWidth: 120, title: '审核类型【2.未通过 0.审核中 1.通过】', align: 'center', sort: true}
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
                newsType: ''
                ,author: ''
                ,releaseType: ''
                ,releaseUserId: ''
                ,isAdvertisement: ''
                ,isHotspot: ''
                ,createDate_BETWEEN:''
                ,examineType:''
                ,isRelease:''
                ,tag:''
            },
            newsTypeList:[],
            showList: 1,
            title: null,
            layerUiSortObj:null,
            haveSaveOrUpdate: false,
            busNews: {},
            sysUsers : [],//发布人
            users : []//作者
        },
        mounted: function() {
            vm = this;
            //钩子函数、页面挂载完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx；比如页面初始化的时候，查询一些数据
            $.get(getWebPath() + "xadictinfo/list","type=TYPE_NEWS", function(r){
                vm.newsTypeList = r.page.list;
            });
            $.get(getWebPath() + "/sys/user/list",{}, function(r){
                vm.sysUsers = r.page.list;
                for (var i = 0; i < vm.sysUsers.length; i++) {
                    $("#selectSysUser").append("<option value='" + vm.sysUsers[i].userId + "'>" + vm.sysUsers[i].username + "</option>");
                }
                $(".selectpicker").selectpicker({noneSelectedText: '请选择发布人'});
                $(".selectpicker").selectpicker('refresh');
                $('#selectSysUser').selectpicker("val","");
            });
            $.get(getWebPath() + "/user/list",{}, function(r){
                vm.users = r.page.list;
                for (var i = 0; i < vm.users.length; i++) {
                    var ur = vm.users[i].nickName == null || vm.users[i].nickName == ''?'':'['+vm.users[i].nickName+']'
                    $("#selectUser").append("<option value='" + vm.users[i].id + "'>" + vm.users[i].userName + ur + "</option>");
                }
                $(".selectpicker").selectpicker({noneSelectedText: '请选择作者'});
                $(".selectpicker").selectpicker('refresh');
                $('#selectUser').selectpicker("val","");
            });
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
                vm.title = "新闻文章表";
                vm.busNews = selectRow;

                //vm.getInfo(selectRow.id);//这里根据情况打开，如果是要管理查询就打开，否则不用重新请求后台

                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            UpDownClick:function(id){
                vm.busNews.id = id;
                $.ajax({
                    type: "GET",
                    url: getWebPath() + "busnews/info/"+id,
                    data: {},
                    success: function(r){
                        vm.busNews = r.busNews;
                        if(vm.busNews.isRelease == 1){
                            vm.busNews.isRelease = 0;
                        }else{
                            vm.busNews.isRelease = 1;
                        }
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + "busnews/update",
                            contentType: "application/json; charset=utf-8",
                            data: JSON.stringify(vm.busNews),
                            success: function(r){
                                if(r.code === 0){
                                    alert('操作成功', function(){
                                        if(vm.busNews.isRelease == 1){
                                            $("div[name='"+id+"']>div").addClass("layui-form-checked");
                                        }else{
                                            $("div[name='"+id+"']>div").removeClass("layui-form-checked");
                                        }
                                    });
                                }else{
                                    alert(r.msg);
                                }
                            }
                        });
                    }
                });
            },
            add: function(){
                vm.showList = false;
                vm.title = "新增";
                vm.busNews = {};
                vm.busNews.isHotspot = 0;
                vm.busNews.isAdvertisement = 0;
                vm.busNews.tag = 0;
                vm.busNews.newsType = '';
                var ue = UE.getEditor('ueditor');
                ue.ready(function() {
                    ue.setContent("");
                });
                $("#readOnlyUeditor").hide();
                $("#ueditor").show();
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

                vm.busNews = selectRow;
                vm.getInfo(selectRow.id);
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            saveOrUpdate: function (event) {
                vm.busNews.mainText =  UE.getEditor('ueditor').getContent();
                vm.busNews.image = $("#imgUrl").val();
                vm.busNews.smallImage = $("#imgUrl1").val();
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        if(vm.busNews.smallImage == null || vm.busNews.smallImage == ''){
                            alert("请选择小图标");
                            return ;
                        }
                        if(vm.busNews.mainText == null || vm.busNews.mainText == ''){
                            alert("请输入正文");
                            return ;
                        }
                        subClick(event);
                        showLoading(event);
                        var url = vm.busNews.id == null ? "busnews/save" : "busnews/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.busNews),
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
                    if(selectRows[i].releaseType == 2){
                        alert("只可删除后台发布的文章");
                        return ;
                    }
                    ids.push(selectRows[i].id);
                }

                confirm('确定要删除选中的记录？', function(){
                    showLoading();
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "busnews/delete",
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
                        url: getWebPath() + "busnews/operateBusNewsByIds",
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
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            $(document).ready(function () {
                    //这里要手工给 日期文本框增加一个id, id 为：releaseDate_BETWEEN}
                    $('#createDate_BETWEEN').daterangepicker(daterangepickerConfig, function (start, end, label) {
                        console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
                        var dateValue = start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD');
                        if ('全部' === label) {
                            vm.q.createDate_BETWEEN = '';
                        } else {
                            vm.q.createDate_BETWEEN = dateValue;
                        }
                        vm.reload();
                    })
                    //.click();
                });
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
                location.href = getWebPath() + 'busnews/exportsExcelAll?' + data;
            },
            getInfo: function(id){
                //获取详情信息
                $.get(getWebPath() + "busnews/info/"+id, function(r){
                    vm.busNews = r.busNews;
                    if(vm.busNews.releaseType == 2){
                        $("#ueditor").hide();
                        $("#readOnlyUeditor").show();
                    }else{
                        $("#readOnlyUeditor").hide();
                        $("#ueditor").show();
                    }
                    vm.$nextTick(function () {
                        var ue = UE.getEditor('ueditor');
                        var content = vm.busNews.mainText;
                        ue.ready(function () {
                            ue.setContent(content);
                        });
                        //需要加一个只读的文本框，只用于修改作者文章时显示
                        var roue = UE.getEditor('readOnlyUeditor',{readonly:true});
                        roue.ready(function () {
                            roue.setContent(content);
                        });
                    });
                    //有图片则赋值
                    if(vm.busNews.image != null && vm.busNews.image != ''){
                        $("#myImageShow").attr("src",vm.busNews.image);
                        $("#imgUrl").val(vm.busNews.image);
                    }
                    if(vm.busNews.smallImage != null && vm.busNews.smallImage != ''){
                        $("#myImageShow1").attr("src",vm.busNews.smallImage);
                        $("#imgUrl1").val(vm.busNews.smallImage);
                    }
                });
            },
            goBack: function () {
                //详情返回列表
                vm.showList = 1;
                clearFormInfo();
                vm.title = null;
                if (vm.haveSaveOrUpdate) {
                    vm.reload();
                    vm.haveSaveOrUpdate = false;
                }
            },
            reload: function (sortObj) {
                vm.showList = 1;
                clearFormInfo();
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
                        'newsType': vm.q.newsType,
                        'author': vm.q.author,
                        'releaseType': vm.q.releaseType,
                        'releaseUserId': vm.q.releaseUserId,
                        'isAdvertisement': vm.q.isAdvertisement,
                        'isHotspot': vm.q.isHotspot,
                        'createDate_BETWEEN':vm.q.createDate_BETWEEN,
                        'examineType':vm.q.examineType,
                        'isRelease':vm.q.isRelease,
                        'tag':vm.q.tag,
                                                //end
                        sidx: aSortName //排序字段
                        ,order: aSortValue//排序方式
                    }
                });
            }
        }
        //自定添加的方法，请放到reload方法之前，reload统一放到方法最后面
    });
    $(document).on("click", "#UpDownBtn", function () {
        var id = $(this).attr("name");
        vm.UpDownClick(id);
    });
    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();
});