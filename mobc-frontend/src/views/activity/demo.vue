<template>
  <LuckyGrid
    ref="LuckDraw"
    width="300px"
    height="300px"
    :prizes="prizes"
    :buttons="buttons"
    :blocks="blocks"
    :default-config="defaultConfig"
    :default-style="defaultStyle"
    :active-style="activeStyle"
    @start="startCallBack"
    @end="endCallBack"
  />
</template>

<script>
export default {
  data() {
    return {
      luckyNum: 0,
      prizes: [],
      blocks: [
        { padding: '4px', background: '#f6cb44', borderRadius: 7 },
        { padding: '0px', background: '#f6cb44', borderRadius: 7 },
        { padding: '0px', background: '#f6cb44', borderRadius: 7 },
      ],
      defaultConfig: {
        gutter: 5,
      },
      defaultStyle: {
        borderRadius: 15,
        fontColor: '#3B3B43',
        fontSize: '18px',
        textAlign: 'center',
        background: '#FFFADE',
        shadow: '0 -5 0 #F6E9B4',
      },
      activeStyle: {
        background: 'linear-gradient(270deg, #FFDCB8, #FDC689)',
        shadow: '',
      },
    }
  },
  computed: {
    buttons() {
      return [
        {
          x: 1,
          y: 1,
          background: '#EA536E',
          shadow: '0 5 0 #DD4660',
          fonts: [
            {
              text: 'Mulai',
              fontColor: '#ffffff',
              top: '30%',
              fontSize: '30px',
            },
          ],
        },
      ]
    },
  },
  mounted() {
    this.getPrizeList()
  },
  methods: {
    getPrizeList() {
      // 模拟接口请求奖品列表
      setTimeout(() => {
        const data = [
          { name: '1000 R' },
          { name: '100元红包' },
          { name: '0.5元红包' },
          { name: '2元红包' },
          { name: '10元红包' },
          { name: '50元红包' },
          { name: '0.3元红包' },
          { name: '5元红包' },
        ]
        const prizes = []
        this.luckyNum = 1
        let axis = [
          [0, 0],
          [1, 0],
          [2, 0],
          [2, 1],
          [2, 2],
          [1, 2],
          [0, 2],
          [0, 1],
        ]
        for (let i = 0; i < 8; i++) {
          let item = data[i]
          prizes.push({
            index: i,
            x: axis[i][0],
            y: axis[i][1],
            fonts: [{ text: item.name, top: '40%' }],
            // imgs: [{ src: item.img, width: '55%', top: '8%' }],
          })
        }
        this.prizes = prizes
      }, 0)
    },

    startCallBack() {
      if (!this.luckyNum) return alert('还剩0次抽奖机会')
      this.$refs.LuckDraw.play()
      setTimeout(() => {
        this.$refs.LuckDraw.stop((Math.random() * 7) >> 0)
      }, 2000)
    },
    endCallBack(prize) {
      alert(`恭喜你获得大奖: ${prize.fonts[0].text}`)
      this.luckyNum--
    },
  },
}
</script>
