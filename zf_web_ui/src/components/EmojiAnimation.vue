<template>
    <div class="emoji">
        <Lottie
            v-for="(item, index) in EmojiList" :key="index"
            v-if="emojiName === item.name"
            :options="item.options" :height="emojiSize" :width="emojiSize"
            @animCreated="handleAnimation"/>
    </div>
</template>

<script>
import Lottie from 'vue-lottie'
import * as angel from '../assets/expression/Angel_Flat.json'
import * as angry from '../assets/expression/Angry_Flat.json'
import * as blushing from '../assets/expression/Blushing_Flat.json'
import * as cool from '../assets/expression/Cool_Flat.json'
import * as crying from '../assets/expression/Crying_Flat.json'
import * as cussing from '../assets/expression/Cussing_Flat.json'
import * as flushed from '../assets/expression/Flushed_Flat.json'
import * as happy from '../assets/expression/Happy_Flat.json'
import * as heartEyes from '../assets/expression/Heart_Eyes_Flat.json'
import * as kiss from '../assets/expression/Kiss_Flat.json'
import * as laughing from '../assets/expression/Laughing_Flat.json'
import * as laughingTears from '../assets/expression/Laughing_Tears_Flat.json'
import * as meh from '../assets/expression/Meh_Flat.json'
import * as noseSteam from '../assets/expression/Nose_Steam_Flat.json'
import * as sad from '../assets/expression/Sad_Flat.json'
import * as sadTear from '../assets/expression/Sad_Tear_Flat.json'
import * as shocked from '../assets/expression/Shocked_Flat.json'
import * as silly from '../assets/expression/Silly_Flat.json'
import * as sleeping from '../assets/expression/Sleeping_Flat.json'
import * as vomiting from '../assets/expression/Vomiting_Flat.json'
export default {
    props: {
        emojiName: String,
        emojiSize: Number
    },
    data () {
        return {
            EmojiList: [
                {
                    options: {
                        animationData: angel.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[angel]'
                },
                {
                    options: {
                        animationData: angry.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[angry]'
                },
                {
                    options: {
                        animationData: blushing.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[blushing]'
                },
                {
                    options: {
                        animationData: cool.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[cool]'
                },
                {
                    options: {
                        animationData: crying.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[crying]'
                },
                {
                    options: {
                        animationData: cussing.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[cussing]'
                },
                {
                    options: {
                        animationData: flushed.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[flushed]'
                },
                {
                    options: {
                        animationData: happy.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[happy]'
                },
                {
                    options: {
                        animationData: heartEyes.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[heartEyes]'
                },
                {
                    options: {
                        animationData: kiss.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[kiss]'
                },
                {
                    options: {
                        animationData: laughing.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[laughing]'
                },
                {
                    options: {
                        animationData: laughingTears.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[laughingTears]'
                },
                {
                    options: {
                        animationData: meh.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[meh]'
                },
                {
                    options: {
                        animationData: noseSteam.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[noseSteam]'
                },
                {
                    options: {
                        animationData: sad.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[sad]'
                },
                {
                    options: {
                        animationData: sadTear.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[sadTear]'
                },
                {
                    options: {
                        animationData: shocked.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[shocked]'
                },
                {
                    options: {
                        animationData: silly.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[silly]'
                },
                {
                    options: {
                        animationData: sleeping.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[sleeping]'
                },
                {
                    options: {
                        animationData: vomiting.default,
                        loop: true,
                        autoplay: true
                    },
                    name: '[vomiting]'
                }
            ],
            defaultAnim: ''
        }
    },
    components: {
        Lottie: Lottie
    },
    methods: {
        handleAnimation: function (anim) {
            this.defaultAnim = anim;
            this.defaultAnim.setSpeed(1);
        },
        stop: function () {
            this.defaultAnim.stop();
        },
        play: function () {
            this.defaultAnim.play();
        },
        pause: function () {
            this.defaultAnim.pause();
        }
    }
}
</script>

<style lang="less" scoped>
.emoji {
    display: inline-block;
}
</style>