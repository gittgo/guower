/*var str = date.Format("yyyyMMdd");*/

/**
 * 日期转字符串
 * @param fmt
 * @returns
 */
function chufa(val){
    /*var radioss = $(".cb-radio");
    radioss.eq(0).attr("checked", false);
    radioss.eq(1).attr("checked", false);*/
    $('#caldate').val("")
    $('#status').val("");
    $('#calname').val("");
    /*alert($(val).attr('class').split(" ")[1].split("date")[1])*/
    /*alert()*/
    if($(val).attr('id')!=null&&$(val).attr('id')!=''){
        /* alert($(val).attr('id'))*/

        $.ajax({
            type: "POST",
            url: getWebPath() + "wcalspecialdate/info/"+$(val).attr('id'),
            success: function(r){

                $('#calid').val(r.wCalSpecialdate.calid);
                console.log(r)
                var radios = $(".cb-radio");
                $('#caldate').val(r.wCalSpecialdate.caldate);
                /*$('#status').val(r.wCalSpecialdate.status);*/
                var i =r.wCalSpecialdate.status==2?1:0;
                debugger

                $(".cb-radio").eq(i).prop('checked', 'checked');

                /*$("#updateApplyForSmsNotification [name='applyForSmsNotification']").eq(0).removeProp('checked');*/

                $('#calname').val(r.wCalSpecialdate.calname);
                /*

                * */
                layer.open({
                    type: 1,
                    skin: 'layui-layer-molv',
                    title: "修改日期的信息",
                    area: ['650px', '350px'],
                    shadeClose: false,
                    content: jQuery("#chulidate"),
                    btn: ['确认','取消'],
                    btn1: function (index) {
                        /*alert( $("#updatedata").serialize())*/
                        $.ajax({
                            type: "POST",
                            data:{},
                            url: getWebPath() + "wcalspecialdate/update?"+$("#updatedata").serialize(),
                            success: function(r){
                                location=location
                            }
                        })
                    }
                })
            }
        });

    }else{
        debugger
        $('#caldate').val($(val).attr('class').split(" ")[1].split("date")[1])
        $(".cb-radio").eq(1).prop('checked', 'checked');
        layer.open({
            type: 1,
            skin: 'layui-layer-molv',
            title: "修改日期的信息",
            area: ['650px', '350px'],
            shadeClose: false,
            content: jQuery("#chulidate"),
            btn: ['确认','取消'],
            btn1: function (index) {
                $.ajax({
                    type: "POST",
                    data:{ caldate:$('#caldate').val()
                        ,status: $('#xxx input[name="status"]:checked ').val()
                        ,calname: $('#calname').val()
                    },
                    url: getWebPath() + "wcalspecialdate/save?"+$("#updatedata").serialize(),
                    success: function(r){
                        layer.close(index);
                        $("#m-main").empty();
                    }
                })
            }
        })
    }
    /*
    });*/

}
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o){
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}


/*2.字符串转日期

var date =  formatDate(str);*/

// 字符串转日期
function formatDate(value) {
    var date = new Date(value);
    if (date == "1970-01-01 08:00")
        return "--";
    else
        return date;
}
var WorkCalendar = function(year) {
    this.now = new Date();
    if (!year) {
        this.year = this.now.getFullYear();
    } else {
        this.year = year;
    }
}

WorkCalendar.prototype = {
    render: function(id) {
        var html = '<div class="container" style="width:90%;padding: 0 auto"><div class="row" style="padding-bottom:10px;">';
        for (var i = 0; i < 12; i++) {
            if (i == 4 || i == 8) {
                html += '</div><div class="row" style="padding-bottom:10px;">';
            }
            html += this.generateMonth(i);
        }
        html += '</div></div>';
        $(id).append(html);
    },

    generateMonth: function(month) {
        var html = '<div class="datepicker dropdown-menu col-md-2" style="display: block; position: relative; z-index: 500;margin: 20px 50px 20px 50px">'
            +'  <div class="datepicker-days" style="display: block;">'
            +'    <table class="table-condensed" style="width: 100%">'
            +'	  <thead>'
            +'	    <tr>'
            +'		  <th class="prev"></th>'
            +'		  <th class="switch" colspan="5">' + this.year + '年' + (month + 1) + '月</th>'
            +'		  <th class="next"></th>'
            +'		</tr>'
            +'		<tr>'
            +'		  <th class="dow">日</th>'
            +'		  <th class="dow">一</th>'
            +'		  <th class="dow">二</th>'
            +'		  <th class="dow">三</th>'
            +'		  <th class="dow">四</th>'
            +'		  <th class="dow">五</th>'
            +'		  <th class="dow">六</th>'
            +'		</tr>'
            +'	  </thead>'
            +'	  <tbody>'
            +'      <tr>';

        var date = new Date(this.year, month, 1);
        var day = date.getDay();

        var lastDayOfMonth = this.getLastDayOfMonth(month);

        for (var j = 0; j < day; j++) {
            html += '<td>&nbsp;</td>';
        }

        for (var j = 0; j < lastDayOfMonth; j++) {
            var week = (j + day) % 7;
            if (week == 0) {
                html += '</tr><tr>';
            }

            var date = this.year + '' + this.completion(month + 1) + '' + this.completion(j + 1);
            html += '<td onclick="chufa(this)" class="week' + week + ' date' + date + '">' + (j + 1) + '</td>';



        }

        if ((lastDayOfMonth + day) % 7 != 0) {
            for (var j = (lastDayOfMonth + day) % 7; j < 7; j++) {
                html += '<td>&nbsp;</td>';
            }
        }
        html += '</tr>';

        if (lastDayOfMonth + day < 36) {
            html += "<tr><td>&nbsp;</td></tr>"
        }

        html += '</tbody>'
            +'	</table>'
            +'  </div>'
            +'</div>';
        return html;
    },

    completion: function(value) {
        if (value >= 10) {
            return '' + value;
        } else {
            return '0' + value;
        }
    },

    getLastDayOfMonth: function(month) {
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 1:
                if (((this.year % 100 != 0) && (this.year % 4 == 0)) || (this.year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                throw new Error('invalid month : ' + month);

        }
    },

    activeByWeek: function(weeks) {
        for (var i = 0; i < weeks.split(',').length; i++) {
            /*$('.week' + weeks[i]).css({*/
            $('.' + weeks.split(',')[i]).css({
                backgroundColor: 'lightgray'
            });
        }
    },

    /*markHolidays: function(holidays) {
        var hl =  JSON.parse(holidays.result.data.holiday)
        console.log(hl)
        for (var i = 0; i < hl.length; i++) {
            var item = hl[i];
            for(var j = 0;j<item.list.length;j++){
                var res = item.list[j];
                if(res.status == 1){
                    $('.date' +formatDate(res.date).Format("yyyyMMdd")).css({
                        backgroundColor: '#DFF0D8'
                    });
                    $('.date' + formatDate(res.date).Format("yyyyMMdd")).attr('title', item.name);
                }

            }

        }
    },*/
    markHolidays: function(holidays) {
        for (var i = 0; i < holidays.length; i++) {
            var res = holidays[i];
            if(res.status!=2){
                /*if(res.enteringbs==1){
                    $('.date' +res.caldate).css({
                        backgroundColor: '#63B8FF'
                    });
                    $('.date' + res.caldate).attr('title', res.calname);
                }else{
                    $('.date' +res.caldate).css({
                        backgroundColor: '#FF4500'
                    });
                    $('.date' + res.caldate).attr('title', res.calname+"补假");
                }*/
                $('.date' +res.caldate).css({
                    backgroundColor: '#FF8C69'
                });


            }else{

                /*if(res.calname=='周天'){
                    $('.date' +res.caldate).css({
                        backgroundColor: '#FFFF00'
                    });
                    $('.date' + res.caldate).attr('title', res.calname);
                }else {
                    $('.date' +res.caldate).css({
                        backgroundColor: '#DFF0D8'
                    });
                    $('.date' + res.caldate).attr('title', res.calname);
                }*/
            }
            //在表单初始化时  做的操作
            $('.date' + res.caldate).attr('id',res.calid)

        }
    },

    markWorkdays: function(workdays) {
        for (var i = 0; i < workdays.split(',').length; i++) {
            var item = workdays.split(',')[i];
            $('.date' + item).css({
                backgroundColor: '#F2DEDE'
            });
            $('.date' + item).attr('title', item);
        }
    },

    markExtrdays: function(extrdays) {
        for (var i = 0; i < extrdays.split(',').length; i++) {
            var item = extrdays.split(',')[i];
            $('.date' + item).css({
                backgroundColor: '#ADD8E6'
            });
            $('.date' + item).attr('title', item);
        }
    },

    /*markNow: function() {
        var now = new Date();
        var dateText = now.getFullYear() + "" + this.completion(now.getMonth() + 1) + "" + now.getDate();

        $('.date' + dateText).css({
            backgroundColor: '#000000',
            color: '#FFFFFF'
        });
        $('.date' + dateText).attr('title', '今天');
    }*/
    //调取后台 接口获取 的节假日数据

};
function  getjjr(val,wval) {
    $.ajax({
        type: "POST",
        url: getWebPath() + "wcalspecialdate/list?yearbs="+val,
        data: {
            "yearbs":val
        },
        success: function(r){
            console.log(r)
            wval.markHolidays(r);
        }
    });
}


