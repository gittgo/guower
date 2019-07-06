$(function () {
    var table = null;
    layui.use('table', function () {
        table = layui.table.render({
            elem: '#jqGrid'
            , url: getWebPath() + 'user/list'
            //, height: 480
            , cellMinWidth: 80
            , cols: [[
                {type: 'numbers', fixed: 'left'}
                , {type: 'checkbox', fixed: 'left'}
                , {field: 'username', width: '13%', title: '用户名', align: 'left', sort: true}
                , {
                    field: 'userTypes',
                    width: '13%',
                    title: '用户类型',
                    align: 'left',
                    sort: true,
                    templet: function (data) {
                        if (data.userTypes == '21' || data.userTypes === 21) {
                            return '普通学生';
                        } else
                        if (data.userTypes == '22' || data.userTypes === 22) {
                            return '负责人团队';
                        } else
                        if (data.userTypes == '23' || data.userTypes === 23) {
                            return '合伙人团队';
                        } else
                        if (data.userTypes == '24' || data.userTypes === 24) {
                            return '骑兵团队';
                        } else
                        if (data.userTypes == '26' || data.userTypes === 26) {
                            return '栋楼长团队';
                        } else
                        if (data.userTypes == '25' || data.userTypes === 25) {
                            return '团队';
                        } else {
                            return '';
                        }
                    }
                }
                , {field: 'mobile', width: '13%', title: '手机号', align: 'center', sort: true}
                // , {field: 'headportraitimg', width: 215, title: '用户头像', align: 'center', sort: true,templet:function (data) {
                //     return '<img src=' + data.headportraitimg + ' style="width:50px;height:50px;" />';
                // }}
                , {
                    field: 'payEndBalance',
                    width: '13%',
                    title: '账户余额',
                    align: 'right',
                    sort: true,
                    templet: function (data) {
                        if (data.payEndBalance == null || data.payEndBalance == '0') {
                            return '￥0.00元';
                        }
                        return '￥' + toThousands(data.payEndBalance,2) + '元';
                    }
                }
                , {field: 'usercode', width: '13%', title: '邀请码', align: 'center', sort: true}
                , {field: 'status', width: '13%', title: '状态', align: 'center', sort: true, templet: function (data) {
                    if(data.status == 1 || data.status == null){
                        return '<span class="label label-success">启用</span>';
                    }
                    if(data.status == 0){
                        return '<span class="label label-danger">禁用</span>';
                    }
                }
                }
                , {field: 'createtime', width: '16%', title: '注册时间', align: 'center', fixed: 'right', sort: true}
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
           // vm.reload(obj);
        });

        //监听复选框事件
        table.on('checkbox(jqGridFilter)', function (obj) {

            //取消选中
            $('.layui-form-checkbox').each(function () {
                var tr = $(this).parent().parent().parent();
                var selectIndex = tr.attr('data-index');
                $('tr[data-index=' + selectIndex + ']').removeClass('layuiSucess');
            });

            //选中
            $('.layui-form-checked').each(function () {
                var tr = $(this).parent().parent().parent();
                var selectIndex = tr.attr('data-index');
                $('tr[data-index=' + selectIndex + ']').addClass('layuiSucess');
            });
        });
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        //查询条件，根据需要打开
        q: {
            userTypes: ''
            , mobile: null
            , username: null
            , usercode: null
            , createtime_BETWEEN : null
        },
        showList: true,
        title: null,
        layerUiSortObj: null,
        tbUser: {},
        school: '',
        campus: '',
        hotel: ''
    },
    mounted: function () {
        //页面挂载完毕，处理一些事情，这里不能使用vm.xx,只能使用this.xx
    },
    methods: {
        inits: function() {
            //这里同mounted 进行初始化 操作; 如果在mounted加载有问题的就放到这里，进行初始化
            //日期范围1
            //vm.initLayerUIDate();
            //日期范围2
            vm.initDaterangepicker();
        },
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.getschool();
            vm.showList = false;
            vm.title = "新增";
            vm.tbUser = {};
            vm.tbUser.school = '';
            vm.tbUser.sex = '';
            vm.tbUser.userTypes = '';
            vm.tbUser.headportraitimg = '../../public/image/background.png';
            vm.tbUser.school = '';
            vm.tbUser.extends3 = '';
            vm.tbUser.studyHotel = '';
            // nice-validate 清空表单验证消息
            $('#addOrUpdateForm').validator("cleanUp");
        },
        update: function (event) {
            var selectRows = vm.getCheckData();
            if (selectRows.length != 1) {
                alert("请选择一条记录");
                return ;
            } else {
                vm.showList = false;
                vm.title = "修改";
                vm.tbUser = selectRows[0];

                vm.getInfo(selectRows[0].userid);
                vm.getschool();
                vm.getHotel(selectRows[0].school);
                vm.getCampus(selectRows[0].school);
                // nice-validate 清空表单验证消息
                $('#addOrUpdateForm').validator("cleanUp");
            }
        },
        saveOrUpdate: function (event) {
            $('#addOrUpdateForm').isValid(function (result) {
                if (!result) {
                    return;
                } else {
                    subClick(event);
                    var url = vm.tbUser.userid == null ? "user/save" : "user/update";
                    $.ajax({
                        type: "POST",
                        url: getWebPath() + url,
                        data: JSON.stringify(vm.tbUser),
                        success: function (r) {
                            changeClick(event);
                            if (r.code === 0 && vm.tbUser.userid != null) {
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
            var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选择记录！");
                return ;
            } else {
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].userid);
                }
                var flag = false;

                confirm('确定要查封选中的记录？', function () {
                    if(!flag){
                        flag = true;
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + "user/chafeng",
                            data: JSON.stringify(ids),
                            success: function (r) {
                                if (r.code == 0) {
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
            }
        },
        jiefeng: function (event) {
            var selectRows = vm.getCheckData();
            if (selectRows.length == 0) {
                alert("请选择记录！");
                return ;
            } else {
                var ids = [];
                for (var i = 0; i < selectRows.length; ++i) {
                    ids.push(selectRows[i].userid);
                }
                var flag = false;

                confirm('确定要解封选中的记录？', function () {
                    if(!flag){
                        flag = true;
                        $.ajax({
                            type: "POST",
                            url: getWebPath() + "user/jiefeng",
                            data: JSON.stringify(ids),
                            success: function (r) {
                                if (r.code == 0) {
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
            }
        },
        exportsToExcel: function () {
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
                for (var i = 0; i < ids.length; ++i) {
                    idss.push(ids[i].userid);
                }
                var modelIds = idss.join(",");
                location.href = getWebPath() + 'tbuser/exportsExcel?modelIds=' + modelIds;
            }
        },
        exportsAllToCvs: function () {
            var aSort = vm.layerUiSortObj;
            var aSortName = isNotnull(aSort) ? aSort.field : null;
            var aSortValue = isNotnull(aSort) ? aSort.type : null;
            var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q)) + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=true";
            location.href = getWebPath() + 'tbuser/exportsExcelAll?' + data;
        },
        exportsAllToExcel: function () {
            var aSort = vm.layerUiSortObj;
            var aSortName = isNotnull(aSort) ? aSort.field : null;
            var aSortValue = isNotnull(aSort) ? aSort.type : null;
            var data = "searchParams=" + encodeURIComponent(JSON.stringify(vm.q)) + "&sidx=" + aSortName + "&order=" + aSortValue + "&isCvs=false";
            location.href = getWebPath() + 'tbuser/exportsExcelAll?' + data;
        },
        getInfo: function (userid) {
            $.get(getWebPath() + "user/info/" + userid, function (r) {
                vm.tbUser = r.user;
            });
        },
        getschool: function () {
            $.get(getWebPath() + "user/schoollist", function (r) {
                vm.school = r.school;
            });
        },
        getHotel: function (campusid) {
            $.get(getWebPath() + "user/hotelList?campusid=" + campusid, function (r) {
                vm.hotel = r.hotelLists;
            });
        },
        getCampus: function (schoolid) {
            $.get(getWebPath() + "user/campusList?schoolid=" + schoolid, function (r) {
                vm.campus = r.campusLists;
            });
        },
        getCheckData: function () { //获取选中数据
            var table = layui.table;
            var checkStatus = table.checkStatus('jqGrid')
                , data = checkStatus.data;
            return data;
        },
        changeCampus: function () {
            $.ajax({
                type: 'POST',
                dataType: 'json',//不写这个会提示下载或者改为json也是
                url: getWebPath() + "user/campusList?schoolid=" + vm.tbUser.school,
                data: {
                    campusList: null
                },
                success: function (r) {
                    var secondCampusObj = document.getElementById("campus");
                    var secondHotelObj = document.getElementById("hotel");
                    var campusList = r.campusLists;
                    secondCampusObj.innerHTML = null;
                    secondHotelObj.innerHTML = "<option value=''>----请--选--择----</option>";
                    secondCampusObj.innerHTML = "<option value=''>----请--选--择----</option>";
                    for (var i = 0; i < campusList.length; i++)//遍历
                    {
                        var hotel = campusList[i];
                        //进行添加到标签里
                        secondCampusObj.innerHTML = secondCampusObj.innerHTML + "<option value='" + hotel.id + "'>" + hotel.name + "</option>";
                    }
                }
            });
        },
        changeHotel: function () {
            $.ajax({
                type: 'POST',
                dataType: 'json',//不写这个会提示下载或者改为json也是
                url: getWebPath() + "user/hotelList?campusid=" + vm.tbUser.school,
                data: {
                    hotelList: null
                },
                success: function (r) {
                    var secondHotelObj = document.getElementById("hotel");
                    var hotelList = r.hotelLists;
                    secondHotelObj.innerHTML = null;
                    secondHotelObj.innerHTML = "<option value=''>----请--选--择----</option>";
                    for (var i = 0; i < hotelList.length; i++)//遍历
                    {
                        var hotel = hotelList[i];
                        //进行添加到标签里
                        secondHotelObj.innerHTML = secondHotelObj.innerHTML + "<option value='" + hotel.id + "'>" + hotel.name + "</option>";
                    }
                }
            });
        },
        initDaterangepicker: function() {
            $(document).ready(function () {
                $('#createtime_BETWEEN').daterangepicker(daterangepickerConfig, function (start, end, label) {
                    console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
                    var dateValue =  start.format('YYYY-MM-DD') + ' - ' + end.format('YYYY-MM-DD');
                    if ('全部' === label) {
                        vm.q.createtime_BETWEEN = '';
                    } else {
                        vm.q.createtime_BETWEEN = dateValue;
                    }
                    vm.reload();
                })
                //.click();
            });
        },
        reload: function (sortObj) {
            var table = layui.table;
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
                    'userTypes': vm.q.userTypes,
                    'mobile': vm.q.mobile,
                    'createtime_BETWEEN': vm.q.createtime_BETWEEN,
                    'username': vm.q.username,
                    'usercode': vm.q.usercode,
                    sidx: aSortName //排序字段
                    ,order: aSortValue//排序方式
                }
            });
        }
    }
});
//这个方法，进行初始化一些数据，类似mounted()
vm.inits();

/**
 * 下面都是上传图片的回调函数，如果有n个图片，依次命名：
 * VueRefreshModelImgFun、VueRefreshModelImgFun1、VueRefreshModelImgFun2
 * @param ds
 * @constructor
 */
function VueRefreshModelImgFun(ds) {
    vm.tbUser.headportraitimg = ds;
}

function HTMLEncode(html) {
    var temp = document.createElement("div");
    (temp.textContent != null) ? (temp.textContent = html) : (temp.innerText = html);
    var output = temp.innerHTML;
    temp = null;
    return output;
}
var img;

//自定义图片的格式，可以根据rowdata自定义
function alarmFormatter(cellvalue, options, rowdata) {
    return '<img src=' + cellvalue + ' style="width:50px;height:50px;" />';
}