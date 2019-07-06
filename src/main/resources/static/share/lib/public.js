/**
 * Created by Zero on 2018/3/19
 */
// var domain="http://47.100.34.60/guower"
// var imgURL
function getUrlPath() {
    return window.location.protocol + '//' + window.location.host + '/' ;
}
var domain=getUrlPath()+'guower/';

/**
 * 根据URL参数key获取value
 * @param key
 * @returns {string}
 */
var getQueryValue = function (key) {
    var q = location.search,
        keyValuePairs = new Array();

    if (q.length > 1) {
        var idx = q.indexOf('?');
        q = q.substring(idx + 1, q.length);
    } else {
        q = null;
    }

    if (q) {
        for (var i = 0; i < q.split("&").length; i++) {
            keyValuePairs[i] = q.split("&")[i];
        }
    }

    for (var j = 0; j < keyValuePairs.length; j++) {
        if (keyValuePairs[j].split("=")[0] == key) {
            // 这里需要解码，url传递中文时location.href获取的是编码后的值
            // 但FireFox下的url编码有问题
            return decodeURI(keyValuePairs[j].split("=")[1]);

        }
    }
    return '';
}
var goNewUrl=function (url) {
    window.location.href = url;
}
var Ajax = {
    Post:function (data, callback) {
        var _data = {
        };//默认值
        _data = $.extend(_data, data.data)

        $.ajax({
            url: domain + data.url,
            type:"POST",
            data: _data,
            dataType: "text",
            timeout: 30000,
            cache: false,
            beforSend: function () {
                // 禁用按钮防止重复提交
                $("#submit").attr({disabled: "disabled"});
            },
            error: function (data) {
                console.log(data);
            },
            success: function (data) {
                // data = JSON.parse(data)
                data = $.parseJSON( data );
                callback(data);
            }
        });
    },
    Get:function (data,callback) {
        var _data = {
            token:'e10d87b7-5bb6-4719-b850-27979a567810',
            // accessToken: UserService.getAccessToken()
        };//默认值

        _data = $.extend(_data, data.data)

        $.ajax({
            url: domain + data.url,
            type:"GET",
            data: _data,
            dataType: "text",
            timeout: 30000,
            cache: false,
            error: function (data) {
                console.log(data);
            },
            success: function (data) {
                // data = JSON.parse(data)
                data = $.parseJSON( data );
                callback(data);
            }
        });
    }
};
