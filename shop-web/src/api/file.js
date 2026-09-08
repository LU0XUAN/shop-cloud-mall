import request from '@/utils/request'

export function uploadFile(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
        url: '/shop-file/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

export function uploadAvatar(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
        url: '/shop-file/avatar',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}