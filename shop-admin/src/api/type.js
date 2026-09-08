import request from '@/utils/request'

export function add(data) {
    return request({
        url: '/shop-goods/type',
        method: 'post',
        data:data
    })
}
export function del(id) {
    return request({
        url: '/shop-goods/type/'+id,
        method: 'delete'
    })
}
export function edit(data) {
    return request({
        url: '/shop-goods/type',
        method: 'put',
        data: data
    })
}


export function lists(data) {
    return request({
        url: '/shop-goods/type/list',
        method: 'get',
        params:data,
    })
}

export function getAll() {
    return request({
        url: '/shop-goods/type/getAll',
        method: 'get',
    })
}

