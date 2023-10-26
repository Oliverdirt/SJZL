import Util from '@/libs/util'

// 生成html文档
export function exportHtml (query) {
  return Util.http({
    url: '/api/system/db-doc/export-html',
    method: 'get',
    params: query
  })
}

// 生成word文档
export function exportWord (query) {
  return Util.http({
    url: '/api/system/db-doc/export-word',
    method: 'get',
    params: query
  })
}

// 生成Markdown文档
export function exportMarkdown (query) {
  return Util.http({
    url: '/api/system/db-doc/export-markdown',
    method: 'get',
    params: query
  })
}
