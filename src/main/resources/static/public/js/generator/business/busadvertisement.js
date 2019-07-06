$(function () {

    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'busadvertisement/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            //说明：如果是固定长度的就居中；货币：就居右，并用千分位分割； 比较长的标题、内容等全部居左；
            //还有一个table里面至少有一个是百分比布局或者是一个minWidth，否则在超大分辨率下面会有问题.
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                //, {field: 'id', width: 120, title: 'id', align: 'center', sort: true}
                , {field: 'title', minWidth: 200, title: '标题', align: 'center', sort: true}
                , {field: 'smallTitle', minWidth: 200, title: '副标题', align: 'center', sort: true}
                // , {field: 'image', minWidth: 120, title: '大图标', align: 'center', sort: true}
                // , {field: 'smallImage', minWidth: 120, title: '小图标', align: 'center', sort: true}
                // , {field: 'mainText', minWidth: 120, title: '正文【详情】', align: 'center', sort: true}
                , {field: 'advertisementType', minWidth: 150, title: '广告类型', align: 'center', sort: true,templet : function(data){
                        if(data.advertisementType == 1){
                            return '果味box';
                        }else if(data.advertisementType == 2){
                            return '合作内容';
                        }else if(data.advertisementType == 3){
                            return '战略合作';
                        }else if(data.advertisementType == 11){
                            return '首页顶部Button';
                        }else if(data.advertisementType == 12){
                            return '首页轮播广告';
                        }else if(data.advertisementType == 13){
                            return '首页焦点Button';
                        }else if(data.advertisementType == 14){
                            return '首页右侧Button';
                        }else if(data.advertisementType == 15){
                            return '首页作者推荐';
                        }else if(data.advertisementType == 16){
                            return '首页企业推荐';
                        }else if(data.advertisementType == 21){
                            return '新闻轮播广告';
                        }else if(data.advertisementType == 22){
                            return '新闻栏目页视频广告';
                        }else if(data.advertisementType == 23){
                            return '新闻栏目页右侧Button';
                        }else if(data.advertisementType == 24){
                            return '文章详情右侧Button';
                        }else if(data.advertisementType == 31){
                            return '快报栏目右侧Button';
                        }else if(data.advertisementType == 32){
                            return '专栏栏目页视频广告';
                        }else if(data.advertisementType == 33){
                            return '专栏栏目页右侧Button';
                        }else if(data.advertisementType == 34){
                            return '专栏轮播广告';
                        }else if(data.advertisementType == 41){
                            return 'APP轮播';
                        }else if(data.advertisementType == 51){
                            return '二级页面banner';
                        }else if(data.advertisementType == 61){
                            return 'APP详情页banner';
                        }else{
                            return '';
                        }
                    }}
                , {field: 'jumpType', minWidth: 120, title: '跳转方式', align: 'center', sort: true,templet : function(data){
                        if(data.jumpType == 1){
                            return '链接';
                        }else if(data.jumpType == 2){
                            return '视频';
                        }else if(data.jumpType == 3){
                            return '';//'富文本';
                        }else if(data.jumpType == 4){
                            return '新闻文章';
                        }else{
                            return '';
                        }
                    }}
                , {field: 'lookTimes', minWidth: 120, title: '阅读量', align: 'center', sort: true}
                // , {field: 'jumpUrl', minWidth: 120, title: '跳转地址', align: 'center', sort: true}
                // , {field: 'jumpNewsId', minWidth: 120, title: '跳转新闻文章id', align: 'center', sort: true}
                // , {field: 'advertisemenRemarks1', minWidth: 120, title: '暂留', align: 'center', sort: true}
                // , {field: 'advertisemenRemarks2', minWidth: 120, title: '暂留', align: 'center', sort: true}
                // , {field: 'advertisemenRemarks3', minWidth: 120, title: '暂留', align: 'center', sort: true}
                , {field: 'sort', minWidth: 120, title: '排序', align: 'center', sort: true}
                , {field: 'releaseUserName', minWidth: 150, title: '发布人', align: 'center', sort: true}
                , {field: 'releaseDate', minWidth: 200, title: '发布时间', align: 'center', sort: true}
                // , {field: 'sortTime', minWidth: 120, title: '排序到期时间', align: 'center', sort: true}
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


    /**
     * 新闻资讯列表
     */
    var newsTable = null;
    layui.use('table', function () {
        newsTable = layui.table;
        newsTable.render({
            elem: '#newsJqGrid'
            , url: getWebPath() + 'busnews/list'
            // , height: 480
            , cellMinWidth: 80
            , cols: [[
                {field: 'id', width: 120, title: '操作', align: 'center', sort: false,templet : function(data){
                        var str = "";
                        var id = data.id;
                        if(Number(data.examineType) == 1){
                            return '<div name="'+id+'" class="choiceBtn layui-table-cell laytable-cell-1-lock"><div class="layui-unselect layui-form-checkbox layui-form-checked'+str+'"><span>确定选择</span><i class="layui-icon layui-icon-ok">√</i></div> </div>';
                        }else{
                            return '<div class="layui-table-cell laytable-cell-1-lock"><div class="layui-unselect layui-form-checkbox'+str+'"><span>不可选择</span><i class="layui-icon layui-icon-ok">√</i></div> </div>';
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
                // , {field: 'newsRemarks1', minWidth: 120, title: '暂留', align: 'center', sort: true}
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
            , done: function (res, page, count) {}
        });

        //排序重新加载
        newsTable.on('sort(newsJqGridFilter)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            vm.layerUiSortObj = obj;
            // vm.newsReload(obj);
        });

    });



    var vm = new Vue({
        el:'#rrapp',
        data:{
            //查询条件，根据需要打开
            q:{
                advertisementType: ''
                ,releaseUserId: ''
                ,jumpType: ''
                ,createDate_BETWEEN:''
                ,news:{
                    title:'',
                    limit:10,
                    page:1,
                }
            },
            showList: 1,
            title: null,
            layerUiSortObj:null,
            haveSaveOrUpdate: false,
            busAdvertisement: {},
            sysUsers : [],//发布人
            busNews:{}//选择的新闻文章
        },
        mounted: function() {
            vm = this;
            //钩子函数、页面挂载完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx；比如页面初始化的时候，查询一些数据
            //加载系统人员列表
            $.get(getWebPath() + "/sys/user/list",{}, function(r){
                vm.sysUsers = r.page.list;
                for (var i = 0; i < vm.sysUsers.length; i++) {
                    $("#selectSysUser").append("<option value='" + vm.sysUsers[i].userId + "'>" + vm.sysUsers[i].username + "</option>");
                }
                $(".selectpicker").selectpicker({noneSelectedText: '请选择发布人'});
                $(".selectpicker").selectpicker('refresh');
                $('#selectSysUser').selectpicker("val","");
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
                vm.title = "广告表";
                vm.busAdvertisement = selectRow;

                //vm.getInfo(selectRow.id);//这里根据情况打开，如果是要管理查询就打开，否则不用重新请求后台

                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            showChoiceNews : function(){
                vm.q.news.title = '';
                vm.q.news.limit = 10;
                vm.q.news.page = 1;
                vm.newsReload();
                vm.showList = 2;
            },
            closeChoiceNews : function(){
                $("#newsQueryTitle").val("");
                vm.showList = 0;
            },
            choiceNews(newsId){
                $.ajax({
                    url:getWebPath() + "busnews/info/"+newsId,
                    success:function(r){
                        vm.busNews = r.busNews;
                        vm.busAdvertisement.jumpNewsId = vm.busNews.id;
                        $("#adcJumpNews").val(vm.busNews.title);
                        $("#adcJumpNews").isValid();
                        $("#adcJumpNews").validator();
                        $("#adcJumpNews").trigger("hidemsg");
                        vm.showList = 0;
                    }
                });
            },
            queryThisTypeCount : function(){
              console.info("updateCount");
            },
            reloadDiv : function(){
                $(".advertisement").hide();
                $(".dataRule").each(function(){
                    $(this).val("");
                });
                $('#addOrUpdateForm').validator("cleanUp");
                if(vm.busAdvertisement.jumpType != undefined && vm.busAdvertisement.jumpType != null && vm.busAdvertisement.jumpType != ''){
                    if(vm.busAdvertisement.jumpType == 1){
                        $(".one").show();
                        $(".oneDataRule").val("true");
                    }else if (vm.busAdvertisement.jumpType == 2){
                        $(".two").show();
                        $(".twoDataRule").val("true");
                    }else if (vm.busAdvertisement.jumpType == 3){
                        $(".three").show();
                        $(".threeDataRule").val("true");
                    }else if (vm.busAdvertisement.jumpType == 4){
                        $(".four").show();
                        $(".fourDataRule").val("true");
                    }
                }
                // $(".advertisement:hidden").each(function(){
                //     //vue双向绑定，无效
                //     $(this).find("input").val("");
                // });
            },
            add: function(){
                vm.showList = false;
                vm.title = "新增";
                vm.busNews = {};
                vm.busAdvertisement = {};
                vm.busAdvertisement.advertisementType = '';
                vm.busAdvertisement.jumpType = '';
                var ue = UE.getEditor('ueditor');
                ue.ready(function() {
                    ue.setContent("");
                });
                vm.reloadDiv();
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

                vm.busAdvertisement = selectRow;
                vm.getInfo(selectRow.id);
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            saveOrUpdate: function (event) {
                vm.busAdvertisement.mainText =  UE.getEditor('ueditor').getContent();
                vm.busAdvertisement.image = $("#imgUrl").val();
                vm.busAdvertisement.smallImage = $("#imgUrl1").val();
                //如果是视频，赋值跳转路径
                if(vm.busAdvertisement.jumpType == 2){
                    vm.busAdvertisement.jumpUrl = $("#videoPath").val();
                }
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        if((vm.busAdvertisement.advertisementType == 22 || vm.busAdvertisement.advertisementType == 32) && vm.busAdvertisement.jumpType != 2){
                            alert("视频位广告跳转类型只能为视频");
                            return ;
                        }
                        if(vm.busAdvertisement.image == null || vm.busAdvertisement.image == ''){
                            alert("请选择图标");
                            return ;
                        }
                        if(vm.busAdvertisement.jumpType == 3 && (vm.busAdvertisement.mainText == null || vm.busAdvertisement.mainText == '')){
                            alert("请输入正文");
                            return ;
                        }
                        subClick(event);
                        showLoading(event);
                        var url = vm.busAdvertisement.id == null ? "busadvertisement/save" : "busadvertisement/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.busAdvertisement),
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
                        url: getWebPath() + "busadvertisement/delete",
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
                        url: getWebPath() + "busadvertisement/operateBusAdvertisementByIds",
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
                location.href = getWebPath() + 'busadvertisement/exportsExcelAll?' + data;
            },
            getInfo: function(id){
                //获取详情信息
                $.get(getWebPath() + "busadvertisement/info/"+id, function(r){
                    vm.busAdvertisement = r.busAdvertisement;
                    if(vm.busAdvertisement.jumpNewsId != null){
                        $.ajax({
                            url:getWebPath() + "busnews/info/"+vm.busAdvertisement.jumpNewsId,
                            success:function(r){
                                if(r.busNews == undefined || r.busNews == null){
                                    alert("跳转文章已不存在，请重新选择");
                                }else{
                                    vm.busNews = r.busNews;
                                }
                            }
                        });
                    }
                    vm.$nextTick(function () {
                        var ue = UE.getEditor('ueditor');
                        var content = vm.busAdvertisement.mainText;
                        ue.ready(function () {
                            ue.setContent(content);
                        });
                    });
                    //如果是视频，赋值跳转路径
                    if(vm.busAdvertisement.jumpType == 2){
                       $("#videoPath").val(vm.busAdvertisement.jumpUrl);
                    }
                    //有图片则赋值
                    if(vm.busAdvertisement.image != null && vm.busAdvertisement.image != ''){
                        $("#myImageShow").attr("src",vm.busAdvertisement.image);
                        $("#imgUrl").val(vm.busAdvertisement.image);
                    }
                    if(vm.busAdvertisement.smallImage != null && vm.busAdvertisement.smallImage != ''){
                        $("#myImageShow1").attr("src",vm.busAdvertisement.smallImage);
                        $("#imgUrl1").val(vm.busAdvertisement.smallImage);
                    }
                    vm.reloadDiv();
                });
            },
            goBack: function () {
                //详情返回列表
                vm.showList = 1;
                vm.title = null;
                clearFormInfo();
                if (vm.haveSaveOrUpdate) {
                    vm.reload();
                    vm.haveSaveOrUpdate = false;
                }
            },
            reload: function (sortObj) {
                debugger;
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
                        'advertisementType': vm.q.advertisementType,
                        'releaseUserId': vm.q.releaseUserId,
                        'jumpType': vm.q.jumpType,
                        'createDate_BETWEEN':vm.q.createDate_BETWEEN,
                        sidx: aSortName //排序字段
                        ,order: aSortValue//排序方式
                    }
                });
            },
            newsQuery:function(){
                vm.newsReload();
            },
            newsReload: function (sortObj) {
                var aSort = sortObj;
                var aSortName  = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                if (isNull(sortObj)) {

                }
                //执行重载
                //1 //重新从第 1 页开始
                newsTable.reload('newsJqGrid', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    },
                    initSort : aSort
                    ,where: {
                        'examineType':'1'
                        ,'title': vm.q.news.title
                    }
                });
            }
        }
        //自定添加的方法，请放到reload方法之前，reload统一放到方法最后面
    });
    $(document).on("click", ".choiceBtn", function () {
        vm.choiceNews($(this).attr("name"));
    });
    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();
});