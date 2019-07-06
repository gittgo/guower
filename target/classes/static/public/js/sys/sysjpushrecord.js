$(function () {

    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'sys/jpushrecord/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                //, {field: 'id', width: 120, title: 'id', align: 'center', sort: true}

                , {field: 'title', width: 200, title: '标题', align: 'left', sort: true}
                , {field: 'subtitle', width: 350, title: '副标题', align: 'left', sort: true}
                , {field: 'audience', width: 200, title: '推送目标', align: 'center', sort: true}
                , {field: 'platform', width: 120, title: '推送平台', align: 'center', sort: true}
                , {field: 'sendNo', width: 120, title: '发送编号', align: 'center', sort: true}
                , {field: 'notification', minWidth: 220, title: '发送参数', align: 'center', sort: true}
                , {field: 'createDate', width: 250, title: '发送时间', align: 'center', sort: true}
                // , {field: 'appKey', width: 120, title: '推送账号key', align: 'center', sort: true}
                // , {field: 'appSecert', width: 120, title: '推送账号secert', align: 'center', sort: true}
                , {field: 'errorCode', width: 120, title: '错误码', align: 'center', sort: true}
                , {field: 'errorMsg', width: 350, title: '推送失败原因', align: 'left', sort: true}
                , {field: 'apnsProduction', width: 120, title: 'ios环境', align: 'center', fixed : 'right', sort: true,templet: function(data) {
                        return data.apnsProduction == 'true' ?
                            '生产环境' :
                            '测试环境';
                    }}
                , {field: 'result', width: 120, title: '推送结果', align: 'center', fixed : 'right', sort: true,templet: function(data) {
                        return Number(data.result) === 0 ?
                            '<span class="label label-danger">失败</span>' :
                            '<span class="label label-success">成功</span>';
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
                createDate_BETWEEN:'',
                result:'',
                platform:'',
                errorCode:'',
                apnsProduction:''
            },
            showList: 1,
            title: null,
            layerUiSortObj:null,
            haveSaveOrUpdate: false,
            sysJpushRecord: {},
            //图表
            chart:{
                chartsType : 'line'
            },
            myChart : {},
            chartsConfig : {
                title: {
                    text: 'title'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:['dataTile1','dataTile2','dataTile3','dataTile4','dataTile5','dataTile6']
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    show: true,
                    feature: {
                        dataZoom: {
                            yAxisIndex: 'none'
                        },
                        dataView: {readOnly: false},
                        magicType: {type: ['line', 'bar']},
                        restore: {},
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: true,
                    data: ['周一','周二','周三','周四','周五','周六','周日']
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: '{value} 次'
                    }
                },
                color : ['#669900','#FF6600','#669999','#FFFF33','#00FF00','#FF0000']
                ,
                series: [
                    {
                        name:'dataTile1',
                        type:'line',
                        stack: '总量',
                        data:[120, 132, 101, 134, 90, 230, 210]
                    },
                    {
                        name:'dataTile2',
                        type:'line',
                        stack: '总量',
                        data:[220, 182, 191, 234, 290, 330, 310]
                    },
                    {
                        name:'dataTile3',
                        type:'line',
                        stack: '总量',
                        data:[150, 232, 201, 154, 190, 330, 410]
                    },
                    {
                        name:'dataTile4',
                        type:'line',
                        stack: '总量',
                        data:[320, 332, 301, 334, 390, 330, 320]
                    },
                    {
                        name:'dataTile5',
                        type:'line',
                        stack: '总量',
                        data:[820, 932, 901, 934, 1290, 1330, 1320]
                    },
                    {
                        name:'dataTile6',
                        type:'line',
                        stack: '总量',
                        data:[820, 932, 901, 934, 1290, 1330, 1320]
                    }
                ]
            }
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
                vm.initMultilselect();
                vm.initChart();
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
                vm.showList = 2;
                vm.title = "";
                vm.sysJpushRecord = selectRow;
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            add: function(){
                vm.showList = false;
                vm.title = "新增";
                vm.sysJpushRecord = {};
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

                vm.getInfo(selectRow.id);
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            },
            saveOrUpdate: function (event) {
                $('#addOrUpdateForm').isValid(function (result) {
                    if (!result) {
                        return;
                    } else {
                        subClick(event);
                        var url = vm.sysJpushRecord.id == null ? "sysjpushrecord/save" : "sysjpushrecord/update";
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + url,
                            data: JSON.stringify(vm.sysJpushRecord),
                            success: function(r){
                                changeClick(event);
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
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "sysjpushrecord/delete",
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
                    var data = "modelIds=" + ids.join(",") + "&status=" + status;
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + "sysjpushrecord/operateSysJpushRecordByIds",
                        contentType: "application/x-www-form-urlencoded",
                        data: data,
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
            initDaterangepicker: function () {
                //这里初始化所有的日期范围选择，用不到的请自行删除
                                                                                                                                                                                                            $(document).ready(function () {
                            //这里要手工给 日期文本框增加一个id, id 为：createDate_BETWEEN}
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
            initMultilselect: function() {
                <!--bootstrap-multiselect-->
                $(document).ready(function() {
                    $('#sel_search_platform').multiselect({
                        includeSelectAllOption: true,
                        enableFiltering: true,
                        onChange: function (p1, p2) {
                            vm.q.platform = $('#sel_search_platform').val();
                            vm.reload();
                        }
                    });
                });
            },
            initChart: function() {
                //图表初始化
                var dom = document.getElementById("myChartDiv");
                dom.style.width = '1000px';
                dom.style.height = '500px';
                vm.myChart = echarts.init(dom);
            },
            exportsToExcel : function ()  {
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
                        idss.push(ids[i].id);
                    }
                    var modelIds = idss.join(",");
                    location.href = getWebPath() + 'sysjpushrecord/exportsExcel?modelIds='  + modelIds;
                }
            },
            exportsAllToCvs:function () {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q))  + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=true";
                location.href = getWebPath() + 'sysjpushrecord/exportsExcelAll?' + data;
            },
            exportsAllToExcel:function () {
                var aSort = vm.layerUiSortObj;
                var aSortName = isNotnull(aSort) ? aSort.field : null;
                var aSortValue = isNotnull(aSort) ? aSort.type : null;
                var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q))  + "&sidx=" + aSortName + "&order=" + aSortValue  + "&isCvs=false";
                location.href = getWebPath() + 'sysjpushrecord/exportsExcelAll?' + data;
            },
            changeResultValue : function (){
                if($("#result").val() != undefined && $(dom).val() == '1'){
                    $(dom).val('成功');
                }else{
                    $(dom).val('失败');
                }
            },
            getInfo: function(id){
                //获取详情信息
                $.get(getWebPath() + "sys/jpushrecord/info/"+id, function(r){
                    vm.sysJpushRecord = r.sysJpushRecord;
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
            setChartsConfig : function (title,xList,dataTitleList,dataList,colorList){
                //获取图标信息
                vm.chartsConfig.title.text = title;
                vm.chartsConfig.xAxis.data = xList;
                vm.chartsConfig.legend.data = dataTitleList;
                vm.chartsConfig.series = dataList;
                vm.chartsConfig.color = colorList;
                if (dataList[0].type == 'line'){
                    vm.chartsConfig.xAxis.boundaryGap = false;
                }else if(dataList[0].type == 'per'){
                    vm.chartsConfig.xAxis.boundaryGap = true;
                }
            },
            showCharts : function(){
                showLoading();
                $.ajax({
                    type: "POST",
                    url: getWebPath() + "sys/jpushrecord/chart",
                    data: JSON.stringify(vm.chart),
                    success: function(r){
                        closeLoading();
                        vm.setChartsConfig(r.chartData.title,r.chartData.xList,r.chartData.dataTitleList,r.chartData.dataList,r.chartData.colorList);
                        if (vm.chartsConfig && typeof vm.chartsConfig === "object") {
                            vm.myChart.setOption(vm.chartsConfig, true);
                            vm.showList = 3;
                        }
                    }
                });
            },
            reload: function (sortObj) {
                vm.showList = 1;
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
                        //这里是所有的筛选条件，没用的请自行删除，不用注释
                        'createDate_BETWEEN' : vm.q.createDate_BETWEEN,
                        'result' : vm.q.result,
                        'platform' : vm.q.platform,
                        'errorCode' : vm.q.errorCode,
                        'apnsProduction' : vm.q.apnsProduction,
                         //end
                        sidx: aSortName //排序字段
                        ,order: aSortValue//排序方式
                    }
                });
            }
        }
    });
    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();
    window.addEventListener("resize", function () {
        vm.myChart.resize();
    });
});
