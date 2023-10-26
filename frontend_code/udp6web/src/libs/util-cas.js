import Setting from '../setting'

const cas = {}

cas.login = function () {
  window.location.replace(`${Setting.casUrl}/cas/login?service=${encodeURIComponent(cas.removeTicket().indexOf('login') > -1 ? cas.removeTicket().replace('login', '') : cas.removeTicket())}`)
}

cas.logout = function () {
  window.location.href = `${Setting.casUrl}/cas/logout?service=${window.location.href}`
}

cas.removeTicket = function () {
  if (location.href.includes('ticket=')) {
    const searchs = location.search.split('&')
    for (let i = 0; i < searchs.length; i++) {
      if (searchs[i].includes('ticket=')) {
        searchs.splice(i, 1)
        break
      }
    }
    const readictUrl = `${window.location.protocol}//${window.location.host}${window.location.pathname}${searchs.join('&')}${location.hash}`
    return readictUrl
  }
  return location.href
}

export default cas
