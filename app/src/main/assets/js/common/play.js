(function() {
    const video = document.querySelector("#video")
    video.volume = 1
    video.src = "{{liveUrl}}"
    video.play()
    video.addEventListener('loadeddata', function() {
        hideLoading()
    })
})();
