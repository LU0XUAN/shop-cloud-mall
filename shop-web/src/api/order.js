import request from '@/utils/request'
export function add(data,useIntegrals,point) {
    return request({
        url: `/shop-order/order?useIntegrals=${useIntegrals}&point=${point}`,
        method: 'post',
        data
    })
}

export function getMyOrder(data) {
    return request({
        url: `/shop-order/order/getMyOrder`,
        method: 'get',
        params:data,
    })
}
export function productStats() {
    return request({
        url: `/shop-order/order/productStats`,
        method: 'get',
    })
}

export function del(id) {
    return request({
        url: '/shop-order/order/'+id,
        method: 'delete'
    })
}

export function editStatus(data) {
    return request({
        url: '/shop-order/order/editStatus',
        method: 'put',
        data: data
    })
}
