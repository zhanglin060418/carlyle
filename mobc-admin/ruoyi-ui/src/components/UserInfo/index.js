import Vue from 'vue'
import UserInfo from './UserInfo.vue'
import { getUser } from "@/api/system/user";

const open = ({userId}) => {

    close()
    const element = document.createElement("div")
    element.setAttribute("id", "user-info-dialog")

    document.getElementsByTagName("body")[0].appendChild(element)

    getUser(userId).then(response => {
        const form = {...response.data, childList: response.childList}
        new Vue({
            el: '#user-info-dialog',
            render: h => (
                <UserInfo v-on:close={close} form={form} title="修改用户"/>
            )
        })
    });

}

const close = () => {
    const element = document.getElementById("user-info-dialog")
    element && element.remove()
}

export default {
    open, close
}
