import request from '@/utils/request'
export function getType(data) {
    return request({
        url: '/shop-goods/goods/getType',
        method: 'get',
        params:data,
    })
}
