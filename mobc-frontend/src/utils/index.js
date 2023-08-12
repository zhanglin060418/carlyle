import i18n from '../i18n'
export default {
  /**
   * 判断流水类型是否加还是减
   * @param {流水类型} type
   * @returns true + else -
   */
  isInOut(type) {
    let resault = true
    switch (type) {
      case 'CREDIT_PAYMENT_OTC': //充值（OTC购买）
        resault = true
        break
      case 'CREDIT_PAYMENT_COIN': //充值（钱包充币）
        resault = true
        break
      case 'CREDIT_MOVEMENT': //人工上分
        resault = true
        break
      case 'CREDIT_PROFIT': //盈利
        resault = true
        break
      case 'CREDIT': //收入
        resault = true
        break
      case 'DEBIT': //支出
        resault = false
        break
      case 'FREEZE': //资金冻结
        resault = false
        break
      case 'UNFREEZE': //资金冻结
        resault = true
        break
      case 'DEBIT_WAGER': //交易
        resault = false
        break
      case 'DEBIT_WITHDRAW_OTC': //提现（OTC出售）
        resault = false
        break
      case 'DEBIT_WITHDRAW_COIN': //提现（钱包提币）
        resault = false
        break
      case 'DEBIT_MOVEMENT': //人工下分
        resault = false
        break
      case 'DEBIT_LOSS': //亏损
        resault = false
        break
    }
    return resault
  },
  getTranTxt(type) {
    let resault = '-'
    switch (type) {
      case 'CREDIT_PAYMENT_OTC': //充值（OTC购买）
        resault = i18n.tc('status.CREDIT_PAYMENT_OTC')
        break
      case 'CREDIT_PAYMENT_COIN': //充值（钱包充币）
        resault = i18n.tc('status.CREDIT_PAYMENT_COIN')
        break
      case 'CREDIT_MOVEMENT': //人工上分
        resault = i18n.tc('status.CREDIT_MOVEMENT')
        break
      case 'CREDIT_PROFIT': //盈利
        resault = i18n.tc('status.CREDIT_PROFIT')
        break
      case 'DEBIT_WAGER': //交易
        resault = i18n.tc('status.DEBIT_WAGER')
        break
      case 'DEBIT_WITHDRAW_OTC': //提现（OTC出售）
        resault = i18n.tc('status.DEBIT_WITHDRAW_OTC')
        break
      case 'DEBIT_WITHDRAW_COIN': //提现（钱包提币）
        resault = i18n.tc('status.DEBIT_WITHDRAW_COIN')
        break
      case 'DEBIT_MOVEMENT': //人工下分
        resault = i18n.tc('status.DEBIT_MOVEMENT')
        break
      case 'DEBIT_LOSS': //亏损
        resault = i18n.tc('status.DEBIT_LOSS')
        break
      case 'FREEZE_WITHDRAW_COIN': //亏损
        resault = i18n.tc('status.FREEZE_WITHDRAW_COIN')
        break
    }
    return resault
  },
  // 获取加减号
  getSign(type) {
    return this.isInOut(type) ? '+' : '-'
  },
}
