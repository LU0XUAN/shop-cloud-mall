import request from '@/utils/request'

export function add(data) {
    return request({
        url: '/shop-order/order',
        method: 'post',
        data:data
    })
}
export function del(id) {
    return request({
        url: '/shop-order/order/'+id,
        method: 'delete'
    })
}
export function edit(data) {
    return request({
        url: '/shop-order/order',
        method: 'put',
        data: data
    })
}

export function editStatus(data) {
    return request({
        url: '/shop-order/order/editStatus',
        method: 'put',
        data: data
    })
}


export function lists(data) {
    return request({
        url: '/shop-order/order/list',
        method: 'get',
        params:data,
    })
}
