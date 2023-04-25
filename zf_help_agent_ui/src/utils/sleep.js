export default function sleep (sleepTime = 1000) {
    return new Promise((resolve) => {
        let timer = setTimeout(() => {
            clearTimeout(timer);
            timer = null;
            resolve(true);
        }, sleepTime);
    })
}