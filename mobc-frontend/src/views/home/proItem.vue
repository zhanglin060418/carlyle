<template>
  <div>
    <div class="dw-box in-home">
      <div class="top-h">
        <div class="title ell">
          {{ item.name }}
        </div>
        <div class="rush-time">
          <span>Harga adopsi: {{ sym }}{{ $utils.getJt(item.actualPrice) }}
          </span>
        </div>
        <i class="shan"></i>
      </div>
      <div class="box-info">
        <div class="top-info-box">
          <div class="td-box">
            <p>{{ $utils.getJt(item.income) }}</p>
            <span>{{ $t('dw.t32') }}</span>
          </div>
          <div class="td-box">
            <p>{{ $utils.getJt($utils.accMul(item.cycle, item.income)) }}</p>
            <span>{{ $t('dw.t33') }}</span>
          </div>
          <div class="td-box">
            <p>{{ item.cycle }}{{ $t('dw.t7') }}</p>
            <span>{{ $t('dw.t6') }}</span>
          </div>
        </div>

        <div class="pro-btn-box" :class="{ disabled: item.id == 1 || !item.enable }" @click="toBuy(item)">
          {{ item.enable ? $t('btn.t8') : $t('btn.t7') }}
        </div>
      </div>
    </div>
    <van-swipe class="my-swipe" :autoplay="3000" :show-indicators="false">
      <template v-for="item in list">
        <van-swipe-item :key="item.id" @click="toBuy(item)">
          <div :key="item.id" class="dw-box">
            <div class="top-h">
              <div class="title ell">
                {{ item.name }}
              </div>
              <div class="day">
                {{ $t('dw.t4') }}：{{ sym }}{{ $utils.getJt(item.actualPrice) }}
              </div>
              <i class="shan"></i>
            </div>
            <div class="box-info">
              <div class="img-box">
                <div class="pro-box" :class="{ jinyu: item.actualPrice >= 40000000 }">
                  <img v-if="item.imgUrl" :src="imgBaseUrl + item.imgUrl" alt="" />
                  <img v-else src="static/assets/image/wind/wind-1.jpg" alt="" />
                </div>
                <template v-if="
                    item.fragmentTemplateList &&
                      item.fragmentTemplateList.length &&
                      item.isShowFragment
                  ">
                  <!-- <div class="gift" v-for="d in item.fragmentTemplateList">
                    {{ $t('dw.t25') }}:
                    <img :src="imgBaseUrl + d.shredImgUrl" alt="" />X1
                  </div> -->
                </template>
              </div>
              <div class="info-box">
                <div class="top-info">
                  <div class="lab-box">
                    <span>{{ $utils.getJt(item.income) }}</span>
                    <span>{{ $t('dw.t26') }}({{ sym }})</span>
                  </div>
                  <div class="lab-box">
                    <span>{{ $utils.accMul(item.returnRate, 100) }}%/{{
                        $t('dw.t7')
                      }}</span>
                    <span>{{ $t('dw.t27') }}</span>
                  </div>
                  <div class="lab-box">
                    <span>{{ item.cycle }}{{ $t('dw.t7') }}</span>
                    <span>{{ $t('dw.t6') }}</span>
                  </div>
                  <div class="lab-box">
                    <span>{{
                      $utils.getJt($utils.accMul(item.income, item.cycle))
                    }}</span>
                    <span>{{ $t('dw.t8') }}</span>
                  </div>
                </div>
                <!-- <div
                  class="pro-btn-box"
                  :class="{ disabled: item.id == 1 || !item.enable }"
                  @click="toBuy(item)"
                >
                  {{ item.enable ? $t('btn.t8') : $t('btn.t7') }}
                </div> -->
              </div>
            </div>
          </div>
        </van-swipe-item>
      </template>
    </van-swipe>
  </div>
</template>
<script>
export default {
  props: {
    list: {
      type: Array,
      default: [],
    },
    item: {
      type: Object,
    },
  },
  data() {
    return {}
  },
  methods: {
    toBuy() {
      let item = this.item
      if (item.id == 1 || !item.enable) return
      this.$router.push({
        path: '/proDesc',
        query: {
          data: JSON.stringify(item),
        },
      })
    },
  },
}
</script>
