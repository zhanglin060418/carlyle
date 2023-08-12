<template>
  <div class="day-task-new">
    <div class="task-bg">
      <div class="task-title">
        HADIAH MISI
      </div>
    </div>
    <div class="task-content">
      <div class="intive-box">
        <div class="left">
          <img src="static/assets/image/task/intive.png" alt="" />
        </div>
        <div class="right">
          <template v-if="isMissCntting < minLimitVolume">
            <p>Undang teman untuk mendapatkan lebih banyak pendapatan</p>
            <div
              class="intive-btn"
              @click="onClick('INVITE')"
              :class="{
                goto: inviteVolume1 > 0,
              }"
            >
              {{
                isMissCntting < minLimitVolume
                  ? $t('sa.get') + `(${isMissCntting}/${minLimitVolume})`
                  : $t('sa.goto')
              }}
            </div>
            <p class="red">Setiap undangan nyata, Bonus+{{ sym }}500</p>
          </template>
          <div v-else class="con-invite">
            <h2>
              Undang {{ minLimitVolume }} teman untuk mendapatkan:<span
                class="red"
                >{{ sym }}{{ minLimitVolume * 500 }}</span
              >
            </h2>
            <p>{{ $t('sa.txt239') }}</p>
            <p>
              2.Untuk lebih dari {{ minLimitVolume }} orang, sistem akan
              memberikan hadiah setiap malam pukul 00:00
            </p>
            <div class="time">
              {{ $t('sa.txt242') }}
              <span class="red">:{{ isMissCnt1 }}</span>
            </div>
            <div
              class="intive-btn"
              :class="{
                goto: inviteVolume1 > 0,
              }"
              @click="onClick('INVITE')"
            >
              {{ inviteVolume1 > 0 ? $t('sa.get') : $t('sa.goto') }}
            </div>
          </div>
        </div>
      </div>
      <div class="daily-box">
        <div class="daily-title">
          Imbalan yang melimpah
        </div>
        <div class="task-info" v-if="!isMiss9">
          <div class="task-info-icon">
            <img src="static/assets/image/task/tg.png" alt="" />
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>
                <lottie-player
                  :src="jsondata"
                  :options="palyoption"
                  class="lottie"
                />{{ $t('sa.txt238') }}
              </p>
              <p>Bergabunglah dengan grup Telegram resmi dan dapatkan bonus</p>
              <p>Bonus+{{ sym }}6000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{ disable: isMiss9, goto: !isMiss9 && isMissCnt9 > 0 }"
              @click="onClick('JOIN_TG_GROUP')"
            >
              {{ isMiss9 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
        <div class="task-info">
          <div class="task-info-icon">
            <img src="static/assets/image/wind/money-gif.gif" alt="" />
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>
                <lottie-player
                  :src="jsondata"
                  :options="palyoption"
                  class="lottie"
                />Bonus investasi({{ isMiss3 ? 1 : 0 }}/1)
              </p>
              <p>Hadiah untuk berinvestasi dalam peralatan</p>
              <p>Bonus+{{ sym }}5000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{ disable: isMiss3, goto: !isMiss3 && isMissCnt3 > 0 }"
              @click="onClick('INVESTMENT')"
            >
              {{ isMiss3 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
        <div class="task-info">
          <div class="task-info-icon">
            <img src="static/assets/image/task/wa.png" alt="" />
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>{{ $t('sa.txt254a') }}WhatsApp ({{ successCount }}/4)</p>
              <p>Bagikan ke grup WhatsApp</p>
              <p>Bonus+{{ sym }}4000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{
                goto: isMissWhats > 0 && successCount != 4,
                disable: successCount == 4,
              }"
              @click="onClick('CREDIT_WHATSAPP')"
            >
              {{ successCount == 4 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
        <div class="task-info">
          <div class="task-info-icon">
            <img src="static/assets/image/task/tg.png" alt="" />
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>{{ $t('sa.txt254a') }}Telegram({{ successCountTg }}/4)</p>
              <p>Bagikan ke grup Telegram</p>
              <p>Bonus+{{ sym }}4000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{
                goto: isMissTg > 0 && successCountTg != 4,
                disable: successCountTg == 4,
              }"
              @click="onClick('CREDIT_TELEGRAM')"
            >
              {{ successCountTg == 4 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
        <div class="daily-title">Tugas sehari hari (Bonus+{{ sym }}3500)</div>
        <div class="task-info">
          <div class="task-info-icon">
            <!-- <img src="static/assets/image/dw/task/signin.png" alt="" /> -->
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>
                <lottie-player
                  :src="jsondata"
                  :options="palyoption"
                  class="lottie"
                />{{ $t('sa.txt244') }}({{ isMiss2 ? 1 : 0 }}/1)
              </p>
              <p>Masuk setiap hari untuk menerima hadiah</p>
              <p>Bonus+{{ sym }}1000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{ disable: isMiss2, goto: !isMiss2 }"
              @click="onClick('SIGN_IN')"
            >
              {{ isMiss2 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
        <div class="task-info">
          <div class="task-info-icon">
            <!-- <img src="static/assets/image/dw/task/upload-pic.png" alt="" /> -->
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>
                <lottie-player
                  :src="jsondata"
                  :options="palyoption"
                  class="lottie"
                />Unggah tangkapan layar({{ isMiss13 ? 1 : 0 }}/1)
              </p>
              <p>Unggah tangkapan layar pengisian ulang yang berhasil</p>
              <p>Bonus+{{ sym }}1000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{ disable: isMiss13, goto: !isMiss13 && isMissCnt13 > 0 }"
              @click="onClick('RECHARGE')"
            >
              {{ isMiss13 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
        <div class="task-info">
          <div class="task-info-icon">
            <!-- <img src="static/assets/image/dw/task/upload-pic.png" alt="" /> -->
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>
                <lottie-player
                  :src="jsondata"
                  :options="palyoption"
                  class="lottie"
                />Unggah tangkapan layar({{ isMiss14 ? 1 : 0 }}/1)
              </p>
              <p>Unggah tangkapan layar penarikan yang berhasil</p>
              <p>Bonus+{{ sym }}1000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{ disable: isMiss14, goto: !isMiss14 && isMissCnt14 > 0 }"
              @click="onClick('WITHDRAWAL')"
            >
              {{ isMiss14 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
        <div class="task-info">
          <div class="task-info-icon">
            <img src="static/assets/image/task/fb.png" alt="" />
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>Bagikan ke Facebook({{ isMiss5 ? 1 : 0 }}/1)</p>
              <p>Kirim tautan berbagi ke Facebook</p>
              <p>Bonus+{{ sym }}1000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{ disable: isMiss5, goto: !isMiss5 && isMissCnt5 > 0 }"
              @click="onClick('FACE_BOOK')"
            >
              {{ isMiss5 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
        <div class="task-info">
          <div class="task-info-icon">
            <!-- <img src="static/assets/image/dw/task/twitter.png" alt="" /> -->
          </div>
          <div class="task-info-txt-btn">
            <div class="txt-box">
              <p>Bagikan ke Twitter({{ isMiss7 ? 1 : 0 }}/1)</p>
              <p>Kirim tautan berbagi ke Twitter</p>
              <p>Bonus+{{ sym }}1000</p>
            </div>
            <div
              class="task-info-btn"
              :class="{ disable: isMiss7, goto: !isMiss7 && isMissCnt7 > 0 }"
              @click="onClick('TWITTER')"
            >
              {{ isMiss7 ? $t('sa.getting') : $t('sa.get') }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <result-popup ref="showResult">
      <template slot="content">
        <div class="info">
          <!-- <img src="static/assets/image/dw/success_icon.png" class="s-icon" alt="" /> -->
          <p class="txt">{{ popInfo }}</p>
          <!-- <p class="txt img" v-if="fragmentId">
            {{ $t('dw.t21') }}
            <img
              :src="require('@/assets/image' + fragmentId)"
              alt=""
            />Monyet2X1
          </p> -->
        </div>
      </template>
    </result-popup>
  </div>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import jsondata from '@/assets/json/5609-hot.json'
export default {
  data() {
    return {
      jsondata,
      palyoption: {
        loop: true,
        autoplay: true,
        speed: 1,
        width: '18px',
      },
      popInfo: '',
      showisMissCnt1: 0,
      isMissCnt1: 0,
      minLimitVolume: 100,
      isMissCntting: 0,
      isMissCnt2: -1,
      isMissCnt3: -1,
      isMissCnt4: -1,
      isMissCnt5: -1,
      isMissCnt6: -1,
      isMissCnt7: -1,
      isMissCnt8: -1,
      isMissCnt9: -1,
      isMissCnt10: -1,
      isMissCnt11: -1,
      isMissCnt12: -1,
      isMissCnt13: -1,
      isMissCnt14: -1,
      countList: [],
      statusList: [],
      isMiss1: false,
      isMiss2: false,
      isMiss3: false,
      isMiss4: false,
      isMiss5: false,
      isMiss6: false,
      isMiss7: false,
      isMiss8: false, // 创建wa群组
      isMiss9: false,
      isMiss10: false,
      isMiss11: false, //穿件tg群组
      isMiss12: false,
      isMiss13: false,
      isMiss14: false,
      inviteVolume1: 0,
      inviteVolume10: 0,
      // minLimitVolume: 0, //广告最大值
      isMissWhats: 0,
      isMissTg: 0,
      successCount: 0,
      successCountTg: 0,
      fragmentId: null,
    }
  },
  created() {
    this.getawardStatus()
  },
  methods: {
    ...mapActions({
      award: 'user/award', // 领取奖励
      awardStatus: 'user/awardStatus', // 查看是否领取状态
      whatsappQueryCount: 'user/whatsappQueryCount',
      whatsappAward: 'user/whatsappAward',
    }),
    //奖励所需方法
    getawardStatus() {
      if (!this.userInfo || !this.userInfo.username) {
        return
      }
      this.awardStatus().then(res => {
        this.statusList = res.data || null
        this.isMiss1 = this.isHasGet('INVITE')
        this.isMiss2 = this.isHasGet('SIGN_IN')
        this.isMiss3 = this.isHasGet('INVESTMENT')
        this.isMiss4 = this.isHasGet('SUNSHINE')
        this.isMiss5 = this.isHasGet('FACE_BOOK')
        this.isMiss6 = this.isHasGet('YOUTUBE')
        this.isMiss7 = this.isHasGet('TWITTER')
        this.isMiss8 = this.isHasGet('WHATS_APP') //创建wa群组
        this.isMiss9 = this.isHasGet('JOIN_TG_GROUP')
        this.isMiss10 = this.isHasGet('SEE_AD_TASK')
        this.isMiss11 = this.isHasGet('TELEGRAM') //船舰tg群组
        this.isMiss13 = this.isHasGet('RECHARGE')
        this.isMiss14 = this.isHasGet('WITHDRAWAL')
      })
      this.whatsappQueryCount().then(res => {
        let data = res.data || []
        if (data.length > 0) {
          let waObj = data.find(d => d.type == 'WHATSAPP')
          let tgObj = data.find(d => d.type == 'TELEGRAM')
          if (waObj) {
            let { auditedCount, successCount } = waObj
            this.isMissWhats = auditedCount // 可领取条数
            this.successCount = successCount //已领取条数
          }
          if (tgObj) {
            let { auditedCount, successCount } = tgObj
            this.isMissTg = auditedCount // 可领取条数
            this.successCountTg = successCount //已领取条数
          }
        }
      })
    },
    // getCount() {
    //   this.awardQueryCount({
    //     activityType:
    //       'INVITE,SIGN_IN,INVESTMENT,SUNSHINE,FACE_BOOK,TELEGRAM,TWITTER,WHATS_APP,YOUTUBE，WITHDRAWAL,RECHARGE',
    //   }).then(res => {
    //     this.countList = res.data || []
    //   })
    // },
    isLogin() {
      if (!this.userInfo || !this.userInfo.username) {
        this.$router.push('/login')
        return false
      }
      return true
    },
    isHasGet(type) {
      //是否获得
      if (!this.statusList || this.statusList.length == 0) return false
      let obj = this.statusList.find(d => d.activityType == type)
      if (obj) {
        if (type == 'INVITE') {
          let { inviteVolume, volume, minLimitVolume } = obj
          this.isMissCnt1 = inviteVolume
          this.isMissCntting = volume
          this.minLimitVolume = minLimitVolume
          let num =
            inviteVolume > minLimitVolume ? minLimitVolume : inviteVolume
          // this.showisMissCnt1 = num
          this.inviteVolume1 = num - obj.volume // 可以领取的数量
        } else if (type == 'SIGN_IN') {
          this.isMissCnt2 = obj.inviteVolume
        } else if (type == 'INVESTMENT') {
          this.isMissCnt3 = obj.inviteVolume
        } else if (type == 'SUNSHINE') {
          this.isMissCnt4 = obj.inviteVolume
        } else if (type == 'FACE_BOOK') {
          this.isMissCnt5 = obj.inviteVolume
        } else if (type == 'YOUTUBE') {
          this.isMissCnt6 = obj.inviteVolume
        } else if (type == 'TWITTER') {
          this.isMissCnt7 = obj.inviteVolume
        } else if (type == 'WHATS_APP') {
          this.isMissCnt8 = obj.inviteVolume
        } else if (type == 'TELEGRAM') {
          this.isMissCnt11 = obj.inviteVolume
        } else if (type == 'JOIN_TG_GROUP') {
          this.isMissCnt9 = obj.inviteVolume
        } else if (type == 'RECHARGE') {
          this.isMissCnt13 = obj.inviteVolume
        } else if (type == 'WITHDRAWAL') {
          this.isMissCnt14 = obj.inviteVolume
        }
        //  else if (type == 'SEE_AD_TASK') {
        //   this.isMissCnt10 = obj.inviteVolume
        //   let num = obj.inviteVolume
        //   this.inviteVolume10 = num - obj.volume
        //   this.minLimitVolume = obj.minLimitVolume
        // }
        return obj.status
      } else {
        return false
      }
    },
    onClick(type) {
      if (this.isLogin()) {
        let num = 0
        let isStatus = false
        if (type == 'INVITE') {
          if (this.inviteVolume1 > 0) {
            this.paward(type, this.inviteVolume1)
          } else {
            this.toShare()
          }
        } else if (type == 'SIGN_IN' && !this.isMiss2) {
          this.paward(type)
        } else if (type == 'INVESTMENT' && !this.isMiss3) {
          if (this.isMissCnt3 > 0) {
            this.paward(type)
          } else {
            this.$router.push('/product')
          }
        } else if (
          type == 'FACE_BOOK' ||
          type == 'TWITTER' ||
          type == 'WHATS_APP' ||
          type == 'JOIN_TG_GROUP' ||
          type == 'YOUTUBE' ||
          type == 'TELEGRAM' ||
          type == 'RECHARGE' ||
          type == 'WITHDRAWAL'
        ) {
          if (type == 'FACE_BOOK') {
            num = this.isMissCnt5
            isStatus = this.isMiss5
          } else if (type == 'YOUTUBE') {
            num = this.isMissCnt6
            isStatus = this.isMiss6
          } else if (type == 'TWITTER') {
            num = this.isMissCnt7
            isStatus = this.isMiss7
          } else if (type == 'WHATS_APP') {
            num = this.isMissCnt8
            isStatus = this.isMiss8
          } else if (type == 'TELEGRAM') {
            num = this.isMissCnt11
            isStatus = this.isMiss11
          } else if (type == 'JOIN_TG_GROUP') {
            num = this.isMissCnt9
            isStatus = this.isMiss9
          } else if (type == 'RECHARGE') {
            num = this.isMissCnt13
            isStatus = this.isMiss13
          } else if (type == 'WITHDRAWAL') {
            num = this.isMissCnt14
            isStatus = this.isMiss14
          }
          if (isStatus) return
          if (num > 0) {
            this.paward(type)
          } else {
            let path = '/shareUpload'
            if (type == 'RECHARGE' || type == 'WITHDRAWAL') {
              path = '/shareUpload2'
            } else {
              path = '/shareUpload'
            }
            this.$router.push({
              path: path,
              query: {
                type,
              },
            })
          }
        } else if (type == 'CREDIT_WHATSAPP' || type == 'CREDIT_TELEGRAM') {
          if (this.isMissWhats > 0 || this.isMissTg > 0) {
            this.pawardWhatsapp(type.split('_')[1])
          } else {
            if (type == 'CREDIT_WHATSAPP' && this.successCount == 4) return
            if (type == 'CREDIT_TELEGRAM' && this.successCountTg == 4) return
            this.$router.push({
              path: '/uploadWhats',
              query: {
                type: type == 'CREDIT_TELEGRAM' ? 'TELEGRAM' : 'WHATSAPP',
              },
            })
          }
        } else if (type == 'SEE_AD_TASK' && !this.isMiss10) {
          if (this.inviteVolume10 > 0 && this.isMissCnt10 > 0) {
            this.paward(type, this.inviteVolume10)
          } else {
            this.$router.push('/viewAd')
          }
        }
      }
    },
    pawardWhatsapp(type) {
      this.whatsappAward({ type }).then(res => {
        this.popInfo =
          this.$t('sa.reAmout') +
          ' ' +
          this.sym +
          this.$utils.accMul(
            1000,
            type == 'TELEGRAM' ? this.isMissTg : this.isMissWhats
          )
        this.$refs.showResult.show = true
        type == 'TELEGRAM' ? (this.isMissTg = 0) : (this.isMissWhats = 0)
        this.getawardStatus()
      })
    },
    paward(type, finishCount = 1) {
      this.award({ activityType: type, finishCount }).then(res => {
        if (res.code == 0) {
          let receiveAmount = 500
          this.fragmentId = null
          if (type == 'INVITE') {
            this.isMiss1 = true
            receiveAmount = this.$utils.accMul(this.inviteVolume1, 500)
          } else if (type == 'SIGN_IN') {
            receiveAmount = 1000
            this.isMiss2 = true
          } else if (type == 'INVESTMENT') {
            this.isMiss3 = true
            receiveAmount = 5000
          } else if (type == 'SUNSHINE') {
            this.isMiss4 = true
            receiveAmount = 1000
            localStorage.removeItem('sun_shine_img')
          } else if (type == 'FACE_BOOK') {
            this.isMiss5 = true
            receiveAmount = 1000
            localStorage.removeItem('sun_shine_img1')
          } else if (type == 'YOUTUBE') {
            this.isMiss6 = true
            receiveAmount = 1000
            localStorage.removeItem('sun_shine_img2')
          } else if (type == 'TWITTER') {
            this.isMiss7 = true
            receiveAmount = 1000
            localStorage.removeItem('sun_shine_img3')
          } else if (type == 'WHATS_APP') {
            this.isMiss8 = true
            receiveAmount = 5000
            localStorage.removeItem('sun_shine_img4')
          } else if (type == 'TELEGRAM') {
            this.isMiss11 = true
            receiveAmount = 5000
            localStorage.removeItem('sun_shine_img8')
          } else if (type == 'JOIN_TG_GROUP') {
            receiveAmount = 6000
            this.isMiss9 = true
            localStorage.removeItem('sun_shine_img5')
          } else if (type == 'RECHARGE') {
            this.isMiss13 = true
            receiveAmount = 1000
            localStorage.removeItem('sun_shine_img6')
          } else if (type == 'WITHDRAWAL') {
            this.isMiss14 = true
            receiveAmount = 1000
            localStorage.removeItem('sun_shine_img7')
          } else if (type == 'SEE_AD_TASK') {
            receiveAmount = this.$utils.accMul(this.inviteVolume10, 200)
            this.isMiss10 = this.minLimitVolume == this.isMissCnt10
          }
          this.popInfo = this.$t('sa.reAmout') + ' ' + this.sym + receiveAmount
          if (type == 'INVITE') {
            this.popInfo =
              'Selamat mendapatkan komisi dari ' +
              finishCount +
              ' user terdaftar yang diundang, total ' +
              this.sym +
              receiveAmount
          }
          this.$refs.showResult.show = true
          this.getawardStatus()
        } else {
          this.errDialog(res.msg)
        }
      })
    },
  },
}
</script>
