/*********************************
 * Themes, rules, and i18n support
 * Locale: Chinese; 中文
 *********************************/
(function(factory) {
    typeof module === "object" && module.exports ? module.exports = factory( require( "jquery" ) ) :
    typeof define === 'function' && define.amd ? define(['jquery'], factory) :
    factory(jQuery);
}(function($) {

    /* Global configuration
     */
    $.validator.config({
        //控制错误样式和主体
        stopOnError: true,
        focusCleanup: true,
        theme: 'yellow_right_effect',
        //timely: 2,

        // Custom rules
        rules: {
            digits: [/^\d+$/, "请填写数字"]
            , letters: [/^[a-z]+$/i, "请填写字母"]
            , date: [/^\d{4}-\d{2}-\d{2}$/, "请填写有效的日期，格式:yyyy-mm-dd"]
            , time: [/^([01]\d|2[0-3])(:[0-5]\d){1,2}$/, "请填写有效的时间，00:00到23:59之间"]
            , email: [/^[\w\+\-]+(\.[\w\+\-]+)*@[a-z\d\-]+(\.[a-z\d\-]+)*\.([a-z]{2,4})$/i, "请填写有效的邮箱"]
            , url: [/^(https?|s?ftp):\/\/\S+$/i, "请填写有效的网址"]
            , qq: [/^[1-9]\d{4,}$/, "请填写有效的QQ号"]
            , IDcard: [/^\d{6}(19|2\d)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)?$/, "请填写正确的身份证号码"]
            , tel: [/^(?:(?:0\d{2,3}[\- ]?[1-9]\d{6,7})|(?:[48]00[\- ]?[1-9]\d{6}))$/, "请填写有效的电话号码"]
            , mobile: [/^1[3-9]\d{9}$/, "请填写有效的手机号"]
            , mobile_tel: [/^(?:(?:0\d{2,3}[\- ]?[1-9]\d{6,7})|(?:[48]00[\- ]?[1-9]\d{6}))$|^(1[3-9]\d{9})$/, "请输入正确的手机号码或者电话号码"]
            , zipcode: [/^\d{6}$/, "请检查邮政编码格式"]
            , chinese: [/^[\u0391-\uFFE5]+$/, "请填写中文字符"]
            , username: [/^\w{3,12}$/, "请填写3-12位数字、字母、下划线"]
            , password: [/^[\S]{6,16}$/, "请填写6-16位字符，不能包含空格"]
            , number: [/^(-?\d+)(\.\d+)?$/, "请输入有效数字"]
            , D_card: [/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/, "请输入正确的身份证号码"]
            , postcode: [/^[1-9]\d{5}$/, "邮政编码格式不正确"]
            , plus: [/^[0-9]*[0-9][0-9]*$/, "请输入一个正整数"]
            , money: [/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/, "请输入正确的数字"]
            , sort: [/^([1-6])?$/, "请输入1-6的数字"]
            , exchangeCode: [/^\s*\d{16}\s*$/, "请输入16位数字的兑换码"]
            , propotion: [/^(0|1|1\.[0]*|0?\.[\d]+)$/, "请输入0到1之间的数字"]
            , format: [/^(\d*,*)*$/, "请输入格式 数字,数字"]
            , startWeigt: [/^\d+(\.\d{1})?$/, "只能输入正一位小数、整数"]
            , starWeigt: [/^\d+(\.\d{1,2})?$/, "只能输入正两位小数、整数"]
            , standardwight: [/^([1-9][\d]{0,3}|0)(\.[\d]{1,2})?$/, "最大9999.99"]
            , plusNum: [/^[0-9]\d*(\.\d+)?$/, "必须大于0"]
            , plusNums: [/^[1-9]\d*(\.\d+)?$/, "必须大于0"]
            , plusnums: [/^([1-9][\d]{0,1}|0)(\.[\d]{1,4})?$/, "1到100之间，小数点后最多4位"]
            , plusnums1: [/^1$|^0(\.\d{1,3})?$/, "0.001-1范围内的数字，小数位最多3位"]
            , plusnums2: [/^1.00$|^0(\.\d{1,2})?$/, "0到1之间，保留两位小数"]
            , shopno: [/^\w{1,12}$/, "格式不正确"]
            , nozero: [/^(\+|-)?([1-9][0-9]*(\.\d+)?|(0\.(?!0+$)\d+))$/, "必须大于0"]
            , chineseletternum: [/^[\u4E00-\u9FA5A-Za-z0-9]{4,20}$/, "请输入4到20位中英文、数字"]
            , roleName: [/^[\u4E00-\u9FA5A-Za-z0-9]{2,30}$/, "请输入2到30位中英文、数字"]
            , creditcard: [/^\d+$/, "请输入合法的信用卡号"]
            , busIntDeadline:[/^([12][0-9]|[3][0-5]|36|[1-9])$/,"整数1~36"]
            , busIntSum:[/^([3-9]\d|[1-9]\d{2}|[1-4]\d{3}|5000)$/,"整数30~5000"]
            , busIntMonthlyrate:[/^(([0-2]\.[0])|([0-2]\.[0][0])|([0-2]\.[0][0][0])|(([0]\.\d{1,3}|[0-1]\.\d{1,3})))$/,"小数（可精确到3位）0~2,单位%"]
            , busIntServicesfee:[/^(([0-1][0]\.[0])|([0-1][0]\.[0][0])|([0-1][0]\.[0][0][0])|(([0]\.\d{1,3}|[0-9]\.\d{1,3})))$/,"小数（可精确到3位）0~10,单位%"]
            , int_0_10000:[/^([1-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|10000)$/,"整数（1~10000）"]
            , busInt:[/^(([0-9]\d{0,5})|999999)$/,"整数（1~999999)"]
            , asdasdas:[/(^[1-9]\d{0,7}$|^100000000$|^100000000(\.[\d]{1,2})\d+?|^0$)|(^[1-9]\d{0,7}\.{1}\d+)|(^0\.{1}\d+)/,"小数（0~100000000），以文本形式存储（即保留小数点后输入的任何内容）"]
            , busChdArea:[/(^[1-9]\d{0,3}$|^2000$|^2000(\.[\d]{1,2})\d+?|^0$)|(^[1-9]\d{0,3}\.{1}\d+)|(^0\.{1}\d+)/,"小数（0~2000），以文本形式存储（即保留小数点后输入的任何内容）"]
            , money_0_200000:[/^(([0-9]\d{0,4})(\.[\d]{1,2})?|([1]\d{0,5})(\.[\d]{1,2})?|200000|200000\.[0]|200000\.[0][0])$/,"实数0~200000，保留两位小数,单位万"]
            , money_0_999999:[/^(([0-9]\d{0,5})(\.[\d]{1,2})?|999999|999999\.[0]|999999\.[0][0])$/,"实数0~999999，保留两位小数,单位元"]
            , money_0_100:[/^(([0-9]\d{0,1})(\.[\d]{1,2})?|100|100\.[0]|100\.[0][0])$/,"实数0~100，保留两位小数,单位%"]
            , number_0_999: [/^([0-9]\d{0,3})?$/, "请输入0-999的数字"]
            , money_0_9999999999999999:[/^(([0-9]\d{0,15})(\.[\d]{1,4})?|9999999999999999|9999999999999999\.[0]|9999999999999999\.[0][0])$/,"正数0~9999999999999999，保留四位小数,单位万元"]
            ,regex_BizLience: [/^([0-9a-zA-Z]{18}$|\d{15}$)/,"请填写正确的营业执照"]
            ,map:[/^([0-9]+\.{0,1}[0-9]{0,})+\,+([0-9]+\.{0,1}[0-9]{0,})$/,"请填写正确的坐标"]
            ,accept: function (element, params){
                if (!params) return true;
                var ext = params[0],
                    value = $(element).val();
                return (ext === '*') ||
                       (new RegExp(".(?:" + ext + ")$", "i")).test(value) ||
                       this.renderMsg("只接受{1}后缀的文件", ext.replace(/\|/g, ','));
            }
        },

        // Default error messages
        messages: {
            0: "此处",
            fallback: "{0}格式不正确",
            loading: "正在验证...",
            error: "网络异常",
            timeout: "请求超时",
            required: "{0}不能为空",
            remote: "{0}已被使用",
            integer: {
                '*': "请填写整数",
                '+': "请填写正整数",
                '+0': "请填写正整数或0",
                '-': "请填写负整数",
                '-0': "请填写负整数或0"
            },
            match: {
                eq: "{0}与{1}不一致",
                neq: "{0}与{1}不能相同",
                lt: "{0}必须小于{1}",
                gt: "{0}必须大于{1}",
                lte: "{0}不能大于{1}",
                gte: "{0}不能小于{1}"
            },
            range: {
                rg: "请填写{1}到{2}的数",
                gte: "请填写不小于{1}的数",
                lte: "请填写最大{1}的数",
                gtlt: "请填写{1}到{2}之间的数",
                gt: "请填写大于{1}的数",
                lt: "请填写小于{1}的数"
            },
            checked: {
                eq: "请选择{1}项",
                rg: "请选择{1}到{2}项",
                gte: "请至少选择{1}项",
                lte: "请最多选择{1}项"
            },
            length: {
                eq: "请填写{1}个字符",
                rg: "请填写{1}到{2}个字符",
                gte: "请至少填写{1}个字符",
                lte: "请最多填写{1}个字符",
                eq_2: "",
                rg_2: "",
                gte_2: "",
                lte_2: ""
            }
        }
    });

    /* Themes
     */
    var TPL_ARROW = '<span class="n-arrow"><b>◆</b><i>◆</i></span>';
    $.validator.setTheme({
        'simple_right': {
            formClass: 'n-simple',
            msgClass: 'n-right'
        },
        'simple_bottom': {
            formClass: 'n-simple',
            msgClass: 'n-bottom'
        },
        'yellow_top': {
            formClass: 'n-yellow',
            msgClass: 'n-top',
            msgArrow: TPL_ARROW
        },
        'yellow_right': {
            formClass: 'n-yellow',
            msgClass: 'n-right',
            msgArrow: TPL_ARROW
        },
        'yellow_right_effect': {
            formClass: 'n-yellow',
            msgClass: 'n-right',
            msgArrow: TPL_ARROW,
            msgShow: function($msgbox, type){
                var $el = $msgbox.children();
                if ($el.is(':animated')) return;
                if (type === 'error') {
                    $el.css({left: '20px', opacity: 0})
                        .delay(100).show().stop()
                        .animate({left: '-4px', opacity: 1}, 150)
                        .animate({left: '3px'}, 80)
                        .animate({left: 0}, 80);
                } else {
                    $el.css({left: 0, opacity: 1}).fadeIn(200);
                }
            },
            msgHide: function($msgbox, type){
                var $el = $msgbox.children();
                $el.stop().delay(100).show()
                    .animate({left: '20px', opacity: 0}, 300, function(){
                        $msgbox.hide();
                    });
            }
        }
    });

}));
