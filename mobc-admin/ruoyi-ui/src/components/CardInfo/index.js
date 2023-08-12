import Vue from 'vue'
import CardInfo from './CardInfo.vue'
import { getCard } from "@/api/system/card";

const open = ({cardId}) => {

  close()

  const element = document.createElement("div")
  element.setAttribute("id", "card-info-dialog")

  document.getElementsByTagName("body")[0].appendChild(element)

  getCard(cardId).then(response => {
    new Vue({
      el: '#card-info-dialog',
      render: h => (
        <CardInfo v-on:close={close} form={response.data} title="修改用户"/>
      )
    })
  });

}

const close = () => {
  const element = document.getElementById("card-info-dialog")
  element && element.remove()
}

export default {
  open, close
}
