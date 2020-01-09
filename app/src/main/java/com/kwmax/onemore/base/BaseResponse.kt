package com.kwmax.onemore.base

import java.io.Serializable

/**
 * Created by kwmax on 2020/1/9.
 */
class BaseResponse<T> : Serializable {
    var status:String?=""
    var message:String?=""
    var error:String?=""
    var data:T ?= null
}