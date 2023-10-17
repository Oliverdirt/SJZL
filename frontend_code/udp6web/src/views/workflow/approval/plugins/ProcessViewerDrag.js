export function dragBox (drag, wrap) {
    function getCss (ele, prop) {
      return parseInt(window.getComputedStyle(ele)[prop])
    }
    let initX
    let initY
    let dragable = false
    let wrapLeft = getCss(wrap, 'left')
    let wrapRight = getCss(wrap, 'top')
  
    drag.addEventListener('mousedown', function (e) {
      dragable = true
      initX = e.clientX
      initY = e.clientY
    }, false)
  
    document.addEventListener('mousemove', function (e) {
      if (dragable === true) {
        let nowX = e.clientX
        let nowY = e.clientY
        let disX = nowX - initX
        let disY = nowY - initY
        wrap.style.left = wrapLeft + disX + 'px'
        wrap.style.top = wrapRight + disY + 'px'
      }
    })
    drag.addEventListener('mouseup', function (e) {
      dragable = false
      wrapLeft = getCss(wrap, 'left')
      wrapRight = getCss(wrap, 'top')
    }, false)
  }
