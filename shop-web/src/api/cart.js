import request from '@/utils/request'
export function addCart(data) {
    return request({
        url: `/shop-goods/cart`,
        method: 'post',
        data
    })
}

export function getMyCart() {
    return request({
        url: `/shop-goods/cart/getMyCart`,
        method: 'get',
    })
}

export function editNum(data) {
    return request({
        url: `/shop-goods/cart/editNum`,
        method: 'put',
        data
    })
}

export function del(id) {
    return request({
        url: '/shop-goods/cart/'+id,
        method: 'delete'
    })
}
