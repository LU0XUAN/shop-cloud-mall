import request from '@/utils/request'
export function getById(id) {
    return request({
        url: `/shop-goods/goods/getById/${id}`,
        method: 'get',
    })
}
