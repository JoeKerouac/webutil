/**
 * 发起网络请求（依赖jquery）
 * @param setting 参数列表，包含url、data、async（Boolean，是否异步，默认异步）、type（请求类型，默认GET）、contentType（
 * 请求header中的contentType，默认json）、dataType（响应数据类型，默认json，可以为xml、html、script、json、jsonp）、error（
 * 网络原因请求失败回调函数）、serviceSuccess（业务请求成功调用函数，会将返回数据中的data字段作为参数传进来）、serviceError
 * （业务请求失败回调函数，会将返回数据中的status和message字段作为参数传入）、complete（请求完成回调函数）
 */
function ajax(setting) {
    var url = setting.url;
    var data = setting.data;
    var async = setting.async == null ? true : setting.async;
    var cache = setting.cache == null ? true : setting.cache;
    var type = setting.type == null ? "GET" : setting.type;
    var contentType = setting.contentType == null ? "application/json"
        : setting.contentType;
    var dataType = setting.dataType == null ? "json" : setting.dataType;
    // 请求错误调用函数
    var error = setting.error == null ? function () {
    } : setting.error;
    // 业务请求失败调用函数
    var serviceError = setting.serviceError == null ? function (data) {
    } : setting.serviceError;
    // 业务请求成功调用函数
    var serviceSuccess = setting.serviceSuccess == null ? function (data) {
    } : setting.serviceSuccess;
    // 请求完成调用函数
    var complete = setting.complete == null ? function (XMLHttpRequest, status) {
    } : setting.complete;
    $.ajax({
        url: url,
        data: data,
        async: async,
        cache: cache,
        contentType: contentType,
        dataType: dataType,
        type: type,
        error: error,
        success: function (data) {
            if (data.status == "200") {
                // 成功
                serviceSuccess(data.data);
            } else if (data.status == "302") {
                // 跳转重定向
                window.location.href = data.url;
            } else {
                // 失败
                serviceError(data.status, data.message);
            }
        },
        complete: complete
    });
}