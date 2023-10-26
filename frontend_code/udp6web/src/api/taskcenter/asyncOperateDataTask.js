
import Util from '@/libs/util'

export function asyncImportExportTaskShow (taskId){
    return Util.http({
        url: `/api/asyncImportExportTasks/${taskId}`,
        method: 'get'
    })
}

export function asyncImportExportTaskDel (taskId){
    return Util.http({
        url: `/api/asyncImportExportTasks/${taskId}`,
        method: 'delete'
    })
}

export function asyncImportExportTaskUpdate (data){
    return Util.http({
        url: `/api/asyncImportExportTasks`,
        method: 'put',
        data: data
    })
}

export function asyncImportExportTaskCreate (data){
    return Util.http({
        url: `/api/asyncImportExportTasks`,
        method: 'post',
        data: data
    })
}

export function asyncImportExportTaskParams(params){
    return Util.http({
        url: `/api/asyncImportExportTasks`,
        method: 'get',
        params
    })
}

export function asyncImportExportTaskDelAll(taskIds){
    return Util.http({
        url: `/api/asyncImportExportTasks/delAll?taskIds=${taskIds}`,
        method: 'delete'
    })
}


