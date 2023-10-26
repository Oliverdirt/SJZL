const Setting = {
  /**
   * 是否含有cas登录 false->否   true->是
   */
  isCas: false,
  /**
   * captchaType:验证码类型：默认为字符验证码，
   * blockPuzzle：滑动验证码
   * clickWord：点选验证码
   */

  /**
   * 验证码登录开关 false->一直有验证码   true->密码错误达一定次数后有验证码
   */
  captchaSwitch: false,
  

  // captchaTypeCode1: 'clickWord',
  // captchaTypeCode2: 'blockPuzzle',
  /**
  * cas跳转的地址
  */
  casUrl: 'http://10.1.44.95:8900',
  //
  dataBankType: '',
  columnNamesql: [],
  columnNamegs: ['tid', 'oid', 'tableoid', 'ctid', 'xmin', 'xmax', 'cmin', 'cmax']
}

export default Setting
