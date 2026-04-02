import request from './request'

export function getDefectList(params) {
  return request({
    url: '/defects',
    method: 'get',
    params
  })
}

export function getDefectDetail(id) {
  return request({
    url: `/defects/${id}`,
    method: 'get'
  })
}

export function createDefect(data) {
  return request({
    url: '/defects',
    method: 'post',
    data
  })
}

export function auditDefect(id, data) {
  return request({
    url: `/defects/${id}/audit`,
    method: 'post',
    data
  })
}

export function getDefectStatistics(params) {
  return request({
    url: '/defects/statistics',
    method: 'get',
    params
  })
}
