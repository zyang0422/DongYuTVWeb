(async function() {
    const ts = window.initTimeStamp || 0
    // 判断时间间隔是否超过30分钟
    if (Date.now() - ts > 30 * 1000 * 60) {
        // 进行初始化操作
        await initHenanLiveList()
    }
    // 通过频道名称取值
    const channelItem = window.channelList_henan.find(item => item.name === '{{channelName}}')
    console.log('video_streams: ' + channelItem.video_streams[0])
    playLive(channelItem.video_streams[0])
})();