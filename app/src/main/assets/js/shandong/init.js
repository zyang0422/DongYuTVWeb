const key = 'BDNZNQSRYWXYCKNA'
const iv = '0000000000000000'
const salt = 'GBHWERMTGPHFELSJ'

function decrypt(d) {
  const a = CryptoJS.AES.decrypt(d, CryptoJS.enc.Utf8.parse(key), {
    iv: CryptoJS.enc.Utf8.parse(iv),
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7
  });
  const b = a.toString(CryptoJS.enc.Utf8);
  return b;
}

function encrypt(c) {
  if (!c) {
    return "".split("").reverse().join("");
  }
  return CryptoJS.AES.encrypt(c, CryptoJS.enc.Utf8.parse(key), {
    iv: CryptoJS.enc.Utf8.parse(iv),
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7
  }).toString();
}

function getShanDongLiveUrl(channelId) {
    const now = Date.now()
    const s = CryptoJS.MD5(channelId + now + salt)
    const body = encrypt(JSON.stringify({
      'channelMark': channelId
    }))

    fetch(`https://feiying.litenews.cn/api/v1/auth/exchange?t=${now}&s=${s}`, {
      "headers": {
        "accept": "*/*",
        "accept-language": "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
        "content-type": "text/plain",
        "priority": "u=1, i",
        "sec-ch-ua": "\"Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24\"",
        "sec-ch-ua-mobile": "?0",
        "sec-ch-ua-platform": "\"Windows\"",
        "sec-fetch-dest": "empty",
        "sec-fetch-mode": "cors",
        "sec-fetch-site": "cross-site",
        "Referer": "https://v.iqilu.com/"
      },
      "body": body,
      "method": "POST"
    }).then(res => {
      return res.text()
    }).then(res => {
      const response = JSON.parse(decrypt(res))
      playLive(response.data)
    })
}

getShanDongLiveUrl('{{channelId}}')