<template>
    <div>
        <div class="send_message" @click="gotoRelease">
           <div>
                <img src="static/assets/image/xapp/add.png" alt=""/>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'ReleaseInfo',
        data() {
            return {
            };
        },
        created() {
        },
        mounted() {
        },
        methods: {
            gotoRelease(){
                this.$router.push('release')
            }
        }
    }
</script>
