//jqGrid的配置信息
$.jgrid.defaults.width = 1000;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};
T.p = url;

//获取项目根路径,形如：http://localhost:8980/
function getRootPath() {
    return window.location.protocol + '//' + window.location.host + '/';
}

//获取项目根路径,形如：http://localhost:8980/guower/
//获取项目根路径,形如：http://localhost:8980/guower/url
function getWebPath(url) {
    if (1 !== 1) {//前端测试地址
        return 'http://localhost:8980/guower/' + (typeof(url) === 'undefined' ? '' : url);
    }
    var pathName = window.location.pathname.substring(1);
    var webName = pathName === '' ? '' : pathName.substring(0, pathName.indexOf('/'));
    return window.location.protocol + '//' + window.location.host + '/' + webName + '/' + (typeof(url) === 'undefined' ? '' : url);
}

//获取当前页面访问的全路径,形如：http://localhost:8980/guower/index.html?id=3
function getFullPath() {
    var pathName = window.location.pathname;
    return window.location.protocol + '//' + window.location.host + '' + pathName;
}

// 获取浏览器参数，name是参数的名称。
function getUrlParam(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  // 匹配目标参数
    if (r!=null) return unescape(r[2]); return null; // 返回参数值
}

//全局配置
$.ajaxSetup({
    dataType: "json",
    contentType: "application/json",
    cache: false
});

function hasPermission(permission) {
    if (window.parent.permissions.indexOf(permission) > -1) {
        return true;
    } else {
        return false;
    }
}

//重写alert
window.alert = function (msg, callback) {
    window.layer.alert(msg, {
        skin: 'layui-layer-molv' //样式类名
        ,closeBtn: 0
    }, function (index) {
        layer.close(index);
        if (typeof(callback) === "function") {
            callback("ok");
        }
    });
}

//重写confirm式样框
window.confirm = function (msg, callback) {
    window.layer.confirm(msg, {btn: ['确定', '取消']},
        function () {//确定事件
            if (typeof(callback) === "function") {
                callback("ok");
            }
        });
}


//=====================================================
//                   JQuery gridui
//=====================================================
//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        alert("只能选择一条记录");
        return;
    }
    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    return grid.getGridParam("selarrrow");
}

//获取选中的一条记录的数据对象
function getSelectedRowData() {
    var rowDataId = getSelectedRow();
    return $("#jqGrid").jqGrid('getRowData', rowDataId);
}


//=====================================================
//                   layer ui
//=====================================================
//选择一条记录
function getLayerSelectedRow(layerTableObj, tableName) {
    var checkStatus = layerTableObj.checkStatus(tableName);
    var data = checkStatus.data;
    if (data.length === 0) {
        alert("请选择一条记录");
        return;
    }
    if (data.length > 1) {
        alert("只能选择一条记录");
        return;
    }
    return data[0];
}

//选择多条记录
function getLayerSelectedRows(layerTableObj, tableName) {
    var checkStatus = layerTableObj.checkStatus(tableName);
    var data = checkStatus.data;
    if (data.length === 0) {
        alert("请选择一条记录");
        return;
    }
    return data;
}

function getLayerSelectedRowsNoAlert(layerTableObj, tableName) {
    var checkStatus = layerTableObj.checkStatus(tableName);
    var data = checkStatus.data;
    if (data.length === 0) {
        return;
    }
    return data;
}

//全部记录
function getLayerRows(layerTableObj, tableName) {
   /* var checkStatus = layerTableObj.checkStatus(tableName);*/
    var data = layerTableObj.cache.jqGrid;
    var rowDate = '';
    for(var a=0;a<data.length;a++){
        rowDate = rowDate + data[a].id + ','
    }

    rowDate=rowDate.replace(/(^\s*)|(\s*$)/g, ",");
    return rowDate;
}

//=====================================================
//                   other
//=====================================================

// 上传图片
function upload(modelName, isImage, imgWidthHeightRate,rate) {
    if (isImage == null || isImage != false) {
        isImage = true;
    } else {
        isImage = false;
    }
    if (imgWidthHeightRate == null) {
        imgWidthHeightRate = 0;//长宽高的比例设置
    }
    $(document).on("change", ".upload_file", function () {
        var _id = $(this).attr('id');
        var _this = $('.upload_file').index(this);
        if(_this == 0){
            _this = "";
        }
        $.ajaxFileUpload({
            url: getWebPath() + 'api/fileUpload',
            data: {moduleName: modelName, isImage: isImage},
            secureuri: false,
            fileElementId: _id,
            type: 'POST',
            success: function (data, status) {
                debugger;
                if (imgWidthHeightRate !== 0) {
                    var img = new Image();
                    // 改变图片的src
                    img.src = data.object;
                    // 加载完成执行
                    img.onload = function(){
                        // 打印
                        if(img.width / img.height !== imgWidthHeightRate) {
                            data.object = '';
                            alert("上传图片宽高比为" + rate);
                        }else {
                            uploading(data, _this);
                        }
                    }
                } else {
                    uploading(data, _this);
                }
            },
            error: function (data, status, e) {
                console.log(data+"");
            }
        });
    });
    //删除图片
    $(document).on('click','.upload_file_del',function(){
        console.info("删除图片");
        var _this = $('.upload_file_del').index(this);
        if(_this == 0){
            _this = "";
        }
        if($("#imgUrl" + _this).val()==null||$("#imgUrl" + _this).val()==''){
            return ;
        }
        //清空该图片相关数据
        $("#imgUrl" + _this).val("");
        $("#myImageShow" + _this).attr('src', "../../public/image/imgError.jpg");
        document.getElementById('uploadPhotoFile').outerHTML=document.getElementById('uploadPhotoFile').outerHTML;
        alert("删除成功");
    });
}

function uploading(data, _this) {
    if (typeof data === "string") {
        data = JSON.parse(data);
    }
    if (data) {
        var ds = data.object;
        console.log("上传图片服务器返回地址：" + ds);

        $("#imgUrl" + _this).val(ds);
        $("#myImageShow" + _this).attr('src', ds);
        // $("#myImageShow" + _this).attr('width', '100px');
        // $("#myImageShow" + _this).attr('height', '100px');

        //如果没用vue那这里就算处理完成，但是使用vue，dom变动了，对应的model属性还没有变化，这里只能调用各个模块内部的方法
        //如在回调方法里面：vm.tInfAdvertisement.advertisementimg =ds
        //VueRefreshModelImgFun(ds);
        //这里使用反射动态调用js函数
        // var funcName = 'VueRefreshModelImgFun' + _this + '( "'+ ds +'" )';
        // eval(funcName);
    } else {
        alert("上传文件失败！");
    }
}


//清除表单信息
function clearFormInfo(isAll){
    if(isAll != null && isAll == true){
        $("form").each(function(){
            $(this).reset();
        });
        $("[id^='myImageShow']").each(function(){
            $(this).attr("src","../../public/image/imgError.jpg");
        });
        $("[id^='imgUrl']").each(function(){
            $(this).val("");
        });
    }else{
        $('#addOrUpdateForm')[0].reset();
        //主要清除图片
        $('#addOrUpdateForm').find("[id^='myImageShow']").each(function(){
            $(this).attr("src","../../public/image/imgError.jpg");
        });
        $('#addOrUpdateForm').find("[id^='imgUrl']").each(function(){
            $(this).val("");
        });
    }
}

//上传视频 截取关键帧
function uploadVideo(modelName, isImage) {
    if (isImage == null || isImage != false) {
        isImage = true;
    } else {
        isImage = false;
    }
    $(document).on("change", ".upload_video_file", function () {
        var _id = $(this).attr('id');
        var _this = $('.upload_video_file').index(this);
        if(_this == 0){
            _this = "";
        }
        if((this.files[0].size)/1024>500000){
            alert("文件不可超过500M");
            return ;
        }
        var index = layer.msg('上传中', {
            icon: 16
            , shade: 0.01
        });
        $("#videoPath"+_this).val('');//把值清空
        console.log(_id);
        $.ajaxFileUpload({
            url: getWebPath() + 'api/fileUpload',
            data: {moduleName: modelName, isImage: isImage},
            secureuri: false,
            fileElementId: _id,
            type: 'POST',
            success: function (data, status) {
                if (typeof data === "string") {
                    data = JSON.parse(data);
                }
                if (data) {
                    var temp = "";
                    for (var i in data) {//用javascript的for/in循环遍历对象的属性
                        temp += i + ":" + data[i] + "\n";
                    }
                    console.log(temp);
                    if (data.code == 500 && data.msg.indexOf("未知") < 0) {
                        alert("上传的类型不允许");
                    }
                    if (data.code == 500 && data.msg.indexOf("未知") >= 0) {
                        alert("上传文件过大");
                    }
                    var ds = data.object;
                    // var a = $("#duration").attr("id");
                    field = ds;
                    // VueRefreshModelImgFun(ds);
                    /* vm.tInfAdvertisement.advertisementimg =ds;*/
                    $("#videoPath"+_this).val(ds);
                    $("#videoPath"+_this).isValid();
                    $("#videoPath"+_this).validator();
                    $("#videoPath"+_this).trigger("hidemsg");
                    var setVideoInterval = setInterval(function () {
                        if ($("#videoPath"+_this).val() == ds) {
                            closeLoading(index);
                            window.clearInterval(setVideoInterval);
                        }
                    },1000);
                    // $("#myImageShow").attr('width', '300px');
                    // $("#myImageShow").attr('height', '200px');
                }
            },
            error: function (data, status, e) {
                var des = "";
                for (var name in data) {
                    des += name + ":" + data[name] + ";";
                }
                console.log(des);
            }
        });
    })
}

function isNull(value) {
    if (value === undefined || value === 'undefined' || value === '' || value === null) {
        return true;
    }
    return false;
}

function isNotnull(obj) {
    return !isNull(obj);
}

//daterangepicker 全局配置，如果全局不满足，自己copy一份；
var daterangepickerConfig = {
    "showDropdowns": true,
    "showWeekNumbers": true,
    "timePicker": false,
    "timePicker24Hour": false,
    "linkedCalendars": false,
    "autoUpdateInput": false,//自动回显input
    "autoApply": false,//自动应用
    "locale": {
        "format": "YYYY-MM-DD",//格式化
        "separator": " - ",
        "applyLabel": '确定',
        "cancelLabel": '取消',
        "fromLabel": '起始时间',
        "toLabel": '结束时间',
        "customRangeLabel": '自定义',
        "daysOfWeek": ['日', '一', '二', '三', '四', '五', '六'],
        "monthNames": ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        "firstDay": 1
    },
    "ranges": {
        "全部": [moment().subtract(600, 'month').startOf('month'), moment()],
        "今天": [moment(), moment()],
        "昨天": [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        "前天": [moment().subtract(2, 'days'), moment().subtract(2, 'days')],
        "过去7天": [moment().subtract(6, 'days'), moment()],
        "过去30天": [moment().subtract(29, 'days'), moment()],
        "过去90天": [moment().subtract(89, 'days'), moment()],
        "过去一年": [moment().subtract(365, 'days'), moment()],
        "这个月": [moment().startOf('month'), moment().endOf('month')],
        "上个月": [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
    },
    "showCustomRangeLabel": true,
    "startDate": moment().subtract(600, 'month').startOf('month'),  //默认的控件结束时间 moment().subtract(29, 'days')， 过去30天
    "endDate": moment()
};

// ==============================================下面都是VUE相关当前全局设置===============================================
/**
 * VUE1.O中内置有很多过滤器，2.0中全部废除了
 * 作用：
 *      如：时间日期格式化、货币格式化、大小写格式化
 * 如何解决：
 *      a.使用第三方工具库，如 lodash 工具库、date-fns日期格式化（推荐）、Moment日期处理、accounting.js货币格式化、Axios.js是http客户端等
 *      b.使用自定义过滤器
 * 这里注册全局的过滤器
 *
 * https://lodash.com/docs/
 *
 * Object.defineProperty(Vue.prototype, '$moment', { value: moment });
 *
 */

//配置是否允许vue-devtools检查代码，方便调试，生产环境中需要设置为false
Vue.config.devtools=true;
Vue.config.productionTip=true; //阻止vue启动时生成生产消息;生成环境false

/**
 * 日期处理过滤器
 * 当前日期：Date.now()
 * eg:
 * <h3>{{currentTime | dates}}</h3>
 *      说明：把VUE实例中的currentTime对象进行过滤处理,日期格式之后的示例：2018-1-7 16:12:55
 */
Vue.filter('dates', function (data) {
    // 返回处理后的值
    var d = new Date(data);
    return d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + ' ' + d.getHours() + ':' + d.getMinutes()
        + ':' + d.getSeconds();
});

/**
 * JS将number数值转化成为货币格式 https://www.cnblogs.com/mingmingruyuedlut/archive/2013/05/19/3082177.html
 *
 * 日期格式化 {{datesFormat | 'YYYY-MM-DD'}}
 *
 * var revenue = 12345678;
 * alert(revenue.formatMoney()); // $12,345,678.00
 * alert(revenue.formatMoney(0, "HK$ ")); // HK$ 12,345,678
 *
 * // Remove non-numeric chars (except decimal point/minus sign):
 *    priceVal = parseFloat(price.replace(/[^0-9-.]/g, '')); // 12345.99
 *    这个方法仅仅应用于小数分隔符为"."的模式，如果小数分隔符是"," 那么正则表达式为/[^0-9-,]/g
 */
Vue.filter('datesFormat', function (date, type) {
    //请注意date-fns这个库是被命名在一个 dateFns 对象下，就好比是 jQuery 被命名在 $ 之下。
    return dateFns.format(date,type);
});

/**
 * 数字千位符格式化方法 {{vueData | toThousands}}
 * @priceData 数字
 * @precision 小数点后面保留位数，默认2
 * 使用方式： v-bind:value="bssOrder.orderSumMoney | toThousands"
 * 使用方式： v-bind:value="bssOrder.orderSumMoney | toThousands(4)"
 * 使用方式： v-bind:value="bssOrder.orderSumMoney | toThousands(4, '$', ''元)"
 */
Vue.filter('toThousands', function (priceData, precision, prefix, suffix) {
    return toThousands(priceData, precision);
});

//获取首图 {{vueData | firstImgs}}
Vue.filter('firstImgs', function (str) {
   return firstImgs(str);
});

//求子串,0~lens {{vueData | subStrings(10)}}
Vue.filter('subStrings', function (data, lens) {
    return subStrings(data,lens);
});

//分割字符串,获取对应下标的值 {{vueData | splits('^', 5)}}
Vue.filter('splits', function (str, separator, indexs) {
    return splits(str, separator, indexs);
});


//===================================================================
function toThousands(priceData, precision) {
    var num = Number(priceData);
    precision = isNull(precision) ? 2 : precision;
    //var formatMoneyStr = accounting.formatMoney(num, '￥', precision, ',', '.');
    var formatMoneyStr = accounting.formatMoney(num, '', precision, ',', '.');
    return formatMoneyStr;
}

function firstImgs(str) {
    var arr = [];
    if (str == null || str == "" || typeof(str) != 'string') {
        str = "";
    }
    arr = str.split(",");
    if (arr.length > 0) {
        return arr[0];
    }
    return "";
}

function subStrings(data, lens) {
    if (data == null || data == "") {
        return "";
    }
    if (typeof(lens) == undefined || typeof(lens) != 'number') {
        lens = 15;
    }
    if (data.length > lens) {
        return data.substring(0, lens) + "...";
    } else {
        return data;
    }
}

function splits(str, separator, indexs) {
    if (str == null || str == "") {
        return "";
    }
    var index = indexs;//下标
    var separators = separator;//分割符号
    if (typeof(index) == undefined || typeof(index) != 'number') {
        index = 0;
    }
    if (typeof(separators) == undefined || separators == '') {
        separators = ",";
    }
    var strArr = str.split(separators);
    if (index > strArr.length - 1) {
        return str;
    } else {
        return strArr[index];
    }
}

//封装  提交事件
function subClick(yuan) {
    var a = (yuan.srcElement ? yuan.srcElement : yuan.target);
    $(a).button('loading');
}
//封装回调
function changeClick(yuan) {
    var a = (yuan.srcElement ? yuan.srcElement : yuan.target);
    $(a).button('reset');
}

function showLoading(event) {
    var index = layer.msg('加载中', {
        icon: 16
        , shade: 0.01
    });
    if (event) {
        event.index = index;
    }
}

function closeLoading(event) {
    if (event && event.index) {
        layer.close(event.index);
    } else {
        layer.closeAll('loading');
    }
}

/*
 *　时间戳（毫秒）改成时间（yyyy-MM-dd HH:mm:ss）
 */
function timestampToTime(cellvalue, options, rowObject) {
    if (cellvalue == null || cellvalue == 0 || cellvalue == "")
        return "";
    var date = new Date(cellvalue);//如果date为13位不需要乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    return Y + M + D + h + m + s;
}

/*
 *　时间戳（毫秒）改成日期（yyyy-MM-dd）
 */
function timestampToDate(cellvalue) {
    if (cellvalue == null || cellvalue == 0 || cellvalue == "")
        return "";
    var date = new Date(cellvalue);//如果date为13位不需要乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
    return Y + M + D;
}

/*
 *　日期改成时间戳（毫秒）
 */
function timeToTimestamp(value) {
    if (value == null || value == 0 || value == "")
        return null;
    var date = new Date(value.replace(/-/g,'/'));
    return date.getTime();
}

/*
 *　状态：启用禁用
 */
function gridStatusUse(value) {
    if (value == 0) return "禁用";
    if (value == 1) return "启用";
    return value;
}