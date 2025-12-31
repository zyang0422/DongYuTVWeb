function addJsWsQuery(url) {
    const key = 'HCPMPKxQNrKAyjzR67JGjswspro' // 固定
    const salt = Math.floor(Date.now() / 1e3) + 180
    const txTime = salt.toString(16)
    const txSecret = CryptoJS.MD5(key + txTime).toString()
    return url + '?txSecret=' + txSecret + '&txTime=' + txTime
}

(function() {
    const liveUrl = '{{liveUrl}}'
    playLive(addJsWsQuery(liveUrl))
})();