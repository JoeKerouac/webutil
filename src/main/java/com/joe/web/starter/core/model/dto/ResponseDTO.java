package com.joe.web.starter.core.model.dto;

import lombok.Data;

/**
 * 响应数据
 *
 * @author JoeKerouac
 * @data 2021-02-16 23:44
 */
@Data
public class ResponseDTO<T> {

    /**
     * 响应数据，成功时有
     */
    private T data;

    /**
     * 是否成功，true表示成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String errCode;

    /**
     * 错误消息
     */
    private String errMsg;

    /**
     * 构建成功响应
     *
     * @param data
     *            响应数据，允许为空
     * @param <T>
     *            数据类型
     * @return 成功响应
     */
    public static <T> ResponseDTO<T> buildSuccess(T data) {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setSuccess(true);
        responseDTO.setData(data);
        return responseDTO;
    }

    /**
     * 构建失败响应
     *
     * @param errCode
     *            错误码
     * @param errMsg
     *            错误消息
     * @param <T>
     *            数据类型
     * @return 失败响应
     */
    public static <T> ResponseDTO<T> buildError(String errCode, String errMsg) {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        responseDTO.setSuccess(false);
        responseDTO.setErrCode(errCode);
        responseDTO.setErrMsg(errMsg);
        return responseDTO;
    }

}
