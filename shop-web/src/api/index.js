import request from '@/utils/request'
export function getAll(data) {
    return request({
        url: '/shop-goods/goods/getAll',
        method: 'get',
        params:data,
    })
}
