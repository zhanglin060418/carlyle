<template>
  <!-- <div class=".comment-container">
    <h1>Revisão do usuário</h1>
    <div class="box" v-for="item in list">
      <div class="top">
        <p>
          <span class="h1">{{ item.title }}</span>
          <span>Feb 7</span>
        </p>
        <p>
          <span class="star">
            <template v-for="d in 5">
              <van-icon name="star" />
            </template>
          </span>
          <span>{{ item.author }}</span>
        </p>
      </div>
      <div class="mid">
        <p class="coms">{{ item.problem }}</p>
        <p class="name">
          <span>Resposta do desenvolvedor</span>
        </p>
        <p>
          {{ item.answer }}
          <a v-if="item.link" class="green" :href="item.link" target="_blank"
            >{{ item.link }}
          </a>
        </p>
      </div>
    </div>
  </div> -->
  <div class="comment-container">
    <div class="comment-info" v-for="(item, index) in list" :key="index">
      <div class="name-data">
        <div class="name">{{ item.title }}</div>
        <div class="data">Feb 7</div>
      </div>
      <div class="star-info">
        <div>
          <template v-for="d in 5">
            <van-icon name="star" />
          </template>
        </div>
        <div>{{ item.author }}</div>
      </div>
      <p class="comment-text">
        {{ item.problem }}
      </p>
      <p class="comment-text">
        {{ item.answer }}
        <a v-if="item.link" class="green" :href="item.link" target="_blank">{{ item.link }}
        </a>
      </p>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      list: [
        {
          title: 'Great get the benefits',
          author: 'Adriana',
          problem:
            'My colleague recommended I download this app and I tried it and was very pleased that I could actually accept it without spending a penny. How long will this campaign last? I will share it with more of my friends.',
          answer:
            'Thank you for your support, our company has prepared 1000JT rewards for all new users, first served  activity will stop when rewards are issued, please remember to log in every day to receive gifts and follow our news to get the latest news.',
          link: '',
        },
        {
          title: 'Real and valid free to receive',
          author: 'Fernanda',
          problem:
            'Very convenient, no installation, you can collect every day.I want to rent a solar machine with more energy when I calculate how long I can make a profit. I will invest.',
          answer: `Hi, I will recommend you the following options and provide you with detailed information
            1. 20kW photovoltaic panels worth 400K produce 20kw/day energy, daily yield 30.8K  only need 20 days to be profitable.
            2. 1.48kw photovoltaic panels  worth 100K, power output 1.48kw/day, daily output 2500 and takes only 19 days to become profitable. For other packages, visit official.
            `,
          link: '',
        },
        {
          title: 'Good  my family uses it',
          author: 'Magda',
          problem: `I've invited all my family and friends to sign up.I'm now earning 400K daily by lying down the important thing is I didn't spend a penny can someone help me know when I can get my iPhone 12pro?`,
          answer:
            'I can answer this question, if you can invite 1000 new users, then after 7 days the people of apple express will knock on your door.Please open the official channel.',
          link: '',
        },
        {
          title: 'Beyond imagination',
          author: 'Rafael ',
          problem: `R100K to collect honestly I just came to give it a try. But I don't have enough money to rent a bigger photovoltaic panel.`,
          answer:
            'Thank you for your feedback.You can share with bigger friends to get rewards no gift limit.If you invite 60 more users  can have a very nice 5-20kw photovoltaic panel',
          link: '',
        },
        {
          title: 'Eu recebi-osaya accept it',
          author: 'Teresa ',
          problem:
            'I wonder if the money to rent photovoltaic panels will really be returned.I plan to rent full power photovoltaic panels, which will cost a lot of money.',
          answer:
            'Hi, rent will be refunded full power photovoltaic panels will be profitable in just 15 days and owner will have income for 180 days.',
          link: '',
        },
      ],
    }
  },
}
</script>

<style lang="less" scoped>
.comment-container {
  margin-top: 35px;
  .comment-info {
    padding: 15px 20px 20px 20px;
    background: #ffffff;
    border-radius: 8px;
    margin-bottom: 16px;
    .name-data {
      display: flex;
      .name {
        font-weight: 500;
        font-size: 16px;
        flex: 4;
      }
      .data {
        flex: 1;
        font-size: 15px;
        color: #85858a;
        text-align: right;
      }
    }
    .star-info {
      display: flex;
      margin-top: 4px;
      & > div {
        flex: 1;
        &:nth-of-type(1) {
          color: #ee8834;
        }
        &:nth-of-type(2) {
          text-align: right;
          color: #85858a;
          font-size: 15px;
        }
      }
    }
    .comment-text {
      margin-top: 11px;
      color: #000;
      font-size: 15px;
      line-height: 15px;
    }
  }
}
</style>
