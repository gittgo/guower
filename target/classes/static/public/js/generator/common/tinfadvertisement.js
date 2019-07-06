$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table;
        table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'tinfadvertisement/list'
            //, width: 1100
            //, height: 488
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'advertisementtitle', width: '23%', title: '标题', align: 'left', sort: true}
                , {field: 'advertisementTypeName', width: '23%', title: '广告类型', align: 'center', sort: true}
                , {field: 'createtime', width: '23%', title: '创建时间', align: 'center', sort: true}
                , {field: 'ordernum', width: '23%', title: '排序', align: 'center', sort: true}
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
});


var vm = new Vue({
    el: '#rrapp',
    data: {
        //查询条件，根据需要打开
        q:{
            advertisementtitle:'',
            advertisementtyoe:''

        },
        Data: "",
        showList: true,
        title: null,
        tInfAdvertisement: {},
        bannerTypeInfo:[],
        bannerTypeUpdateCode:null//修改页面-广告类型-默认选中的
    },
    mounted: function () {
        //提前加载广告类型数据
        this.getBannerTypeList();
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.tInfAdvertisement = {};
            vm.tInfAdvertisement.platform = "0";
            vm.getBannerTypeList();
            vm.tInfAdvertisement.jumptype= "0";
            var ue = UE.getEditor('ueditor');
            ue.ready(function() {
                ue.setContent("");
            });
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
        },
        update: function (event) {
            var advertisementid = vm.getCheckData();
            if (advertisementid.length != 1) {
                alert("请选择一条记录");
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.tInfAdvertisement.platform = "0";
            vm.getInfo(advertisementid[0].advertisementid);
            $('#addOrUpdateForm').validator("cleanUp");
        },
        saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    subClick(event);
                    vm.tInfAdvertisement.content =  UE.getEditor('ueditor').getContent();
                    console.log("getEditor=="+UE.getEditor('ueditor').getContent());
                    var url = vm.tInfAdvertisement.advertisementid == null ? "tinfadvertisement/save" : "tinfadvertisement/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.tInfAdvertisement),
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
            var advertisementids = vm.getCheckData();
            if (advertisementids.length == 0) {
                alert("请选择记录");
                return;
            }
            var ids = [];
            for (var i = 0; i < advertisementids.length; ++i) {
                ids.push(advertisementids[i].dvertisementid);
            }
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: getWebPath() + "tinfadvertisement/delete",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (advertisementid) {
            $.get(getWebPath() + "tinfadvertisement/info/" + advertisementid, function (r) {
                vm.tInfAdvertisement = r.tInfAdvertisement;
                vm.bannerTypeUpdateCode = r.tInfAdvertisement.advertisementtyoe;
                vm.$nextTick(function () {
                    var ue = UE.getEditor('ueditor');
                    var content = vm.tInfAdvertisement.content;
                    ue.ready(function () {
                        ue.setContent(content);
                    });
                });
            });
        },
        getBannerTypeList : function(){
            $.get(getWebPath() + "xadictinfo/list","type=TYPE_BANNER_ADVERTISING", function(r){
                vm.bannerTypeInfo = r.page.list;
                if (vm.bannerTypeInfo !== null && vm.bannerTypeInfo.length !== 0) {
                    if ( isNull(vm.tInfAdvertisement.id) || isNull(vm.tInfAdvertisement) ) {//新增
                        vm.tInfAdvertisement.advertisementtyoe = r.page.list[0].code;
                    } else {//修改
                        vm.tInfAdvertisement.advertisementtyoe = vm.bannerTypeUpdateCode;
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
                    'advertisementtyoe': vm.q.advertisementtyoe,
                    'advertisementtitle': vm.q.advertisementtitle,
                    sidx: aSortName //排序字段
                    ,order: aSortValue//排序方式
                }
            });
        }
    }
});

/**
 * 下面都是上传图片的回调函数，如果有n个图片，依次命名：
 * VueRefreshModelImgFun、VueRefreshModelImgFun1、VueRefreshModelImgFun2
 * @param ds
 * @constructor
 */
function VueRefreshModelImgFun(ds) {
    //vm.tInfAdvertisement.advertisementimg = ds; //也可以
    Vue.set(vm.tInfAdvertisement, 'advertisementimg', ds);
    console.log("图片地址:" + ds + ";");
}

function VueRefreshModelImgFun1(ds) {
    console.log("图片地址:" + ds);
    //vm.tInfAdvertisement.advertisementimg = ds; //也可以
    Vue.set(vm.tInfAdvertisement, 'advertisementimg1', ds);
}

function VueRefreshModelImgFun2(ds) {
    console.log("图片地址:" + ds);
    //vm.tInfAdvertisement.advertisementimg = ds; //也可以
    Vue.set(vm.tInfAdvertisement, 'advertisementimg2', ds);
}