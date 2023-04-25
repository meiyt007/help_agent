export default {
    data () {
        return {
            resolutionWidth: 1920,
            resolutionHeight: 1080,
        }
    },
    computed: {
        /** 第一种 1920下 form表单一行展示 1280下 form表单展示两行 */
        calcHeight1 () {
            return (this.resolutionHeight === 1080 && this.resolutionWidth === 1280)
                || (this.resolutionHeight === 1024 && this.resolutionWidth === 1280)
                ? 'calc(100% - 170px)' : 'calc(100% - 160px)';
        },

        /** 第二种 1920下 两行 1280下 三行 */
        calcHeight2 () {
            return (this.resolutionHeight === 1080 && this.resolutionWidth === 1280)
                || (this.resolutionHeight === 1024 && this.resolutionWidth === 1280)
                ? 'calc(100% - 210px)' : 'calc(100% - 160px)';
        },

        calcHeight3 () {
            return (this.resolutionHeight === 1080 && this.resolutionWidth === 1280)
                || (this.resolutionHeight === 1024 && this.resolutionWidth === 1280)
                ? 'calc(100% - 170px)' : 'calc(100% - 120px)';
        },
    },
    created () {
        this.resolutionResize = () => {
            this.resolutionWidth = screen.width;
            this.resolutionHeight = screen.height;
            console.log('%c [screen]:', 'color:red;font-weight:700;', this.resolutionWidth, this.resolutionHeight);
        }

        window.addEventListener('resize', this.resolutionResize);
        this.resolutionResize();
    },
    beforeDestroy () {
        window.removeEventListener('resize', this.resolutionResize);
    }
}