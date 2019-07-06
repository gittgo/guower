$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'infversioninfo/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'versioncode', width: 120, title: '版本代码', align: 'center', sort: true}
                , {field: 'device', width: 140, title: '更新平台', align: 'center', sort: true, templet: function (data) {
                        if (data.device == '1') {
                            return '<span class="fa fa-apple">&nbsp;&nbsp;IOS</span>';
                        } else if (data.device == '11') {
                            return '<span class="fa fa-apple">&nbsp;&nbsp;IOS2</span>';
                        } else if (data.device == '2') {
                            return '<span class="fa fa-android">&nbsp;&nbsp;ANDROID</span>';
                        } else if (data.device == '22') {
                            return '<span class="fa fa-android">&nbsp;&nbsp;ANDROID2</span>';
                        } else if (data.device == '3') {
                            return '<span class="fa fa-windows">&nbsp;&nbsp;WEB</span>';
                        } else {
                            return data.device;
                        }
                    }
                }
                , {field: 'versionname', width: 120, title: '版本名称', align: 'center', sort: true}
                , {field: 'isforceupdatename', width: 140, title: '是否强制更新', align: 'center', sort: true}
                , {field: 'updatecontent', minWidth: 200, title: '版本更新内容说明', align: 'left', sort: true}
                , {field: 'downloadurl', minWidth: 150, title: '下载地址', align: 'center', sort: true}
                , {field: 'createtime', width: 180, title: '创建日期', align: 'center', sort: true}
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

    //这个方法，进行初始化一些数据，类似mounted()
    vm.inits();
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        //查询条件，根据需要打开
        //q:{
        //	username: null
        //},
        showList: true,
        title: null,
        deviceInfos: {},
        infVersionInfo: {}
    },
    mounted: function () {
        this.getDeviceList();
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.infVersionInfo = {isforceupdate: 1};
            vm.deviceInfos = {};
            vm.getDeviceList();
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
        },
        update: function (event) {
            var id = vm.getCheckData();
            if (id.length != 1) {
                alert("请选择一条记录");
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id[0].id);
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
        },
        saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    subClick(event);
                    var url = vm.infVersionInfo.id == null ? "infversioninfo/save" : "infversioninfo/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.infVersionInfo),
                        success: function (r) {
                            if (r.code === 0) {
                                alert('操作成功', function (index) {
                                    vm.reload();
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        del: function (event) {
            var ids = vm.getCheckData();
            if (ids == null) {
                return;
            }
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: getWebPath() + "infversioninfo/delete",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (id) {
            $.get(getWebPath() + "infversioninfo/info/" + id, function (r) {
                vm.infVersionInfo = r.infVersionInfo;
            });
        },
        getDeviceList: function () {
            $.get(getWebPath() + "xadictinfo/deviceList", function (r) {
                vm.deviceInfos = r;
                if (vm.deviceInfos !== null && vm.deviceInfos.length !== 0) {
                    vm.infVersionInfo.device = r[0];
                }
            });
        },
        downloadApk: function() {
            location.href = vm.infVersionInfo.downloadurl;
        },
        uploadApk: function() {
            console.log("这里是用ajax进行上传文件...");
            var excelFormData = new FormData();
            //excelFormData.append("params1", vm.obj.id);
            excelFormData.append('uploadFile', document.getElementById('filePath').files[0]);//filename是键，file是值，就是要传的文件，test.zip是要传的文件名

            $.ajax({
                type: "POST",
                //encType: 'multipart/form-data', //表明上传类型为文件
                dataType: "json",
                url: getWebPath() + 'infversioninfo/uploadApk',
                data: excelFormData,
                cache: false,//上传文件无需缓存
                processData: false,//用于对data参数进行序列化处理 这里必须false
                contentType: false, //必须
                success: function (r) {
                    if (r.code === 0) {
                        alert('上传成功', function (index) {
                            vm.infVersionInfo.downloadurl = r.filePath;
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        getCheckData: function () { //获取选中数据
            var table = layui.table;
            var checkStatus = table.checkStatus('jqGrid')
                , data = checkStatus.data;
            return data;
        },
        reload: function (sortObj) {
            var table = layui.table;
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
                    // 'username': vm.q.username,
                    sidx: aSortName //排序字段
                    ,order: aSortValue//排序方式
                }
            });
        }
    }
});