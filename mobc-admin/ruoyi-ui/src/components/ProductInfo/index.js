import Vue from 'vue'
import { getProduct } from "@/api/system/product";
import ProductInfo from "@/components/ProductInfo/ProductInfo.vue";

const open = ({productId}) => {

    close()

    const element = document.createElement("div")
    element.setAttribute("id", "product-info-dialog")

    document.getElementsByTagName("body")[0].appendChild(element)

    getProduct(productId).then(response => {

        new Vue({
            el: '#product-info-dialog',
            render: h => (
                <ProductInfo v-on:close={close} form={response.data} title="修改用户"/>
            )
        })
    });

}

const close = () => {
    const element = document.getElementById("product-info-dialog")
    element && element.remove()
}

export default {
    open, close
}
