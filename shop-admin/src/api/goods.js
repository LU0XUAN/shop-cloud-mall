import request from '@/utils/request'

export function add(data) {
    return request({
        url: '/shop-goods/goods',
        method: 'post',
        data:data
    })
}
export function del(id) {
    return request({
        url: '/shop-goods/goods/'+id,
        method: 'delete'
    })
}
export function getById(id) {
    return request({
        url: '/shop-goods/goods/getById/'+id,
        method: 'get'
    })
}
export function edit(data) {
    return request({
        url: '/shop-goods/goods',
        method: 'put',
        data: data
    })
}


export function lists(data) {
    return request({
        url: '/shop-goods/goods/list',
        method: 'get',
        params:data,
    })
}

export function editStatus(data) {
    return request({
        url: '/shop-goods/goods/editStatus',
        method: 'put',
        data: data
    })
}
