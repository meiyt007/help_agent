<template>
    <div class="depart-container video-container">
        <div class="head">
            <img class="head-img" src="@/assets/images/pad/video/head.png" alt="" />
            <img class="signal" src="@/assets/images/pad/video/signal.png" alt="" />
        </div>
        <div class="name">{{name}}</div>
        <div class="status">等待接听...</div>
        <div class="btn centerYAround">
            <div class="speak" @click="speak">
                <img v-show="isSpeak" src="@/assets/images/pad/video/speak.png" alt="" />
                <img v-show="!isSpeak" src="@/assets/images/pad/video/speak-stop.png" alt="" />
                <div>免提</div>
            </div>
            <div class="handUp" @click="handUp">
                <img src="@/assets/images/pad/video/handUp.png" alt="" />
            </div>
            <div class="microphone" @click="microphone">
                <img v-show="isMicrophone" src="@/assets/images/pad/video/microphone.png" alt="" />
                <img v-show="!isMicrophone" src="@/assets/images/pad/video/microphone-stop.png" alt="" />
                <div>静音</div>
            </div>
            
        </div> 
    </div>
</template>
<script>
    export default{
        name:'videoPhone',
        data(){
            return{
                isSpeak:true,
                isMicrophone:true
            }
        },
        components:{
            
        },
        props:{
            name: {
                type: String,
                default: () => "",
            },
            phone: {
                type: String,
                default: () => "",
            },
        },
        mounted(){

        },
        methods:{
            speak(){
                this.isSpeak = !this.isSpeak;
            },
            microphone(){
                this.isMicrophone = !this.isMicrophone;
            },
            handUp(){
                this.$emit('close');
            }
        }
    }
</script>
<style lang="scss" scoped>  
    @import './css/chooseWorker.scss';
</style>