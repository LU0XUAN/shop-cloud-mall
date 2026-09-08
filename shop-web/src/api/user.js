import request from '@/utils/request'

//获取菜单信息
export function listUser(data) {
    return request({
        url: '/shop-user/user/list',
        method: 'get',
        params:data,
    })
}

export function getUserById() {
    return request({
        url: '/shop-user/user/getInfo',
        method: 'get',
    })
}


//添加用户
export function adduser(data) {
    return request({
        url: '/shop-user/user',
        method: 'post',
        data: data
    })
}

//修改用户
export function edituser(data) {
    data.password = null
    return request({
        url: '/shop-user/user',
        method: 'put',
        data: data
    })
}

//修改个人用户
export function edituserInfo(data) {
    data.password = null
    return request({
        url: '/shop-user/user/edit',
        method: 'put',
        data: data
    })
}

export function pwd(data) {
    return request({
        url: '/shop-user/user/pwd',
        method: 'put',
        data: data
    })
}


//修改密码
export function editPwd(data) {
    return request({
        url: '/shop-user/user/pwd',
        method: 'put',
        data: data
    })
}


