import request from '@/utils/request'

export function add(data) {
    return request({
        url: '/shop-user/addr',
        method: 'post',
        data:data
    })
}
export function del(id) {
    return request({
        url: '/shop-user/addr/'+id,
        method: 'delete'
    })
}
export function edit(data) {
    return request({
        url: '/shop-user/addr',
        method: 'put',
        data: data
    })
}


export function lists(data) {
    return request({
        url: '/shop-user/addr/list',
        method: 'get',
        params:data,
    })
}

export function getAll() {
    return request({
        url: '/shop-user/addr/getAll',
        method: 'get',
    })
}
