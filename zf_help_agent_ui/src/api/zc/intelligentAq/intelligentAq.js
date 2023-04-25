import request from '@/utils/request';

export function getIntelligentAnswer(params) {
    return request({
        url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/intelligentWebsocket/intelligentAsk?message=' + params,
        method: 'GET'
    })
}

