/**
 * UEditor文档-如何自定义请求地址
 * http://fex.baidu.com/ueditor/#qa-customurl
 * http://fex.baidu.com/ueditor/#dev-request_specification
 * @author dazer
 * @date 2018.1.3

 uploadimage：//执行上传图片或截图的action名称
 uploadscrawl：//执行上传涂鸦的action名称
 uploadvideo：//执行上传视频的action名称
 uploadfile：//controller里,执行上传视频的action名称

 catchimage：//执行抓取远程图片的action名称

 listimage：//执行列出图片的action名称
 listfile：//执行列出文件的action名称

 *
 */
(function () {
    // window.UE.Editor.prototype._bkGetActionUrl = window.UE.Editor.prototype.getActionUrl;
    // window.UE.Editor.prototype.getActionUrl = function (action) {
    //     if (action == 'uploadimage' || action == 'uploadvideo' || action == 'uploadfile'  || action == 'uploadscrawl') {
    //         return getWebPath('/ueditor/uploadsFile');
    //     }
    //     //抓取远程图片配置
    //     else if (action == 'catchimage') {
    //        return getWebPath('/ueditor/uploadsCatchimage');
    //     }
    //     //列出指定目录下的图片|文件
    //     else if (action == 'listimage' || action == 'listfile') {
    //        return getWebPath('/ueditor/uploadsList');
    //     }
    //     else {
    //         return this._bkGetActionUrl.call(this, action);
    //     }
    // }
})();