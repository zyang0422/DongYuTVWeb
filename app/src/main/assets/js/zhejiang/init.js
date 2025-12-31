const _hashParams = {
  "zwebl01.cztv.com": {
    key: "CHWr9VybUeBZE1VB"
  },
  "zwebl02.cztv.com": {
    key: "CHWr9VybUeBZE1VB"
  },
  "zwebl03.cztv.com": {
    key: "CHWr9VybUeBZE1VB"
  },
  "zwebl04.cztv.com": {
    key: "CHWr9VybUeBZE1VB"
  },
  "zwebl05.cztv.com": {
    key: "CHWr9VybUeBZE1VB"
  },
  "zwebl06.cztv.com": {
    key: "CHWr9VybUeBZE1VB"
  },
  "zwebl07.cztv.com": {
    key: "CHWr9VybUeBZE1VB"
  },
  "zhfivel01.cztv.com": {
    key: "9T08yiAoqM4eeCwV"
  },
  "zhfivel02.cztv.com": {
    key: "9T08yiAoqM4eeCwV"
  },
  "zhfivel03.cztv.com": {
    key: "9T08yiAoqM4eeCwV"
  },
  "zhfivel04.cztv.com": {
    key: "9T08yiAoqM4eeCwV"
  },
  "zhfivel05.cztv.com": {
    key: "9T08yiAoqM4eeCwV"
  },
  "zhfivel06.cztv.com": {
    key: "9T08yiAoqM4eeCwV"
  },
  "zhfivel07.cztv.com": {
    key: "9T08yiAoqM4eeCwV"
  }
}
  , ci = "cdn-auth";

function getAuthKey(videoUrl) {
  const url = new URL(videoUrl)
  const param = _hashParams[url.host]
  const e = param.timestamp || Math.floor(Date.now() / 1e3)
  const r = cryptoJs.MD5(`${url.pathname}-${e}-${param.rand}-${param.uid}-${param.key}`)
  return `${e}-${param.rand}-${param.uid}-${r}`
}

var $t, ei = new Uint8Array(16);
function ti() {
  if (!$t && !($t = typeof crypto < "u" && crypto.getRandomValues && crypto.getRandomValues.bind(crypto) || typeof msCrypto < "u" && "function" == typeof msCrypto.getRandomValues && msCrypto.getRandomValues.bind(msCrypto)))
    throw new Error("crypto.getRandomValues() not supported. See https://github.com/uuidjs/uuid#getrandomvalues-not-supported");
  return $t(ei)
}
const ii = /^(?:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}|00000000-0000-0000-0000-000000000000)$/i;
for (var ri = [], ni = 0; ni < 256; ++ni)
  ri.push((ni + 256).toString(16).substr(1));

function ai(e) {
  var t = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : 0
    , i = (ri[e[t + 0]] + ri[e[t + 1]] + ri[e[t + 2]] + ri[e[t + 3]] + "-" + ri[e[t + 4]] + ri[e[t + 5]] + "-" + ri[e[t + 6]] + ri[e[t + 7]] + "-" + ri[e[t + 8]] + ri[e[t + 9]] + "-" + ri[e[t + 10]] + ri[e[t + 11]] + ri[e[t + 12]] + ri[e[t + 13]] + ri[e[t + 14]] + ri[e[t + 15]]).toLowerCase();
  if (!function (e) {
    return "string" == typeof e && ii.test(e)
  }(i))
    throw TypeError("Stringified UUID is invalid");
  return i
}

function si(e, t, i) {
  var r = (e = e || {}).random || (e.rng || ti)();
  return r[6] = 15 & r[6] | 64,
    r[8] = 63 & r[8] | 128,
    ai(r)
}

Object.keys(_hashParams).forEach((e => {
  const t = _hashParams[e];
  t.timestamp = t.timestamp,
    t.rand = t.rand || si().replace(/-/g, ""),
    t.uid = t.uid || 0
}
))

function getZheJiangTVLive(code) {
    const url = `https://zwebl02.cztv.com/live/${code}1080Pnew.m3u8`
    playLive(url + '?auth_key=' + getAuthKey(url))
}

getZheJiangTVLive('{{code}}')
