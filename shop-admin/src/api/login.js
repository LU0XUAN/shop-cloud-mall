import request from '@/utils/request'

//登录
export function login(data) {
    return request({
        url: '/auth/login',
        method: 'post',
        data:data
    })
}

export function reg(data) {
    return request({
        url: '/auth/register',
        method: 'post',
        data:data
    })
}
