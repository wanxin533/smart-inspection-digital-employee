import request from './request'

export function getTaskList(params) {
  return request({
    url: '/inspection/tasks',
    method: 'get',
    params
  })
}

export function getTaskDetail(id) {
  return request({
    url: `/inspection/tasks/${id}`,
    method: 'get'
  })
}

export function createTask(data) {
  return request({
    url: '/inspection/tasks',
    method: 'post',
    data
  })
}

export function executeTask(id, data) {
  return request({
    url: `/inspection/tasks/${id}/execute`,
    method: 'post',
    data
  })
}

export function getTaskRecords(params) {
  return request({
    url: '/inspection/records',
    method: 'get',
    params
  })
}
