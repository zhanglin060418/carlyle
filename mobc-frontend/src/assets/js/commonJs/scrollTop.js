// var scrollToTop = window.setInterval(function() {
//     var pos = window.pageYOffset;
//     if (pos > 0) {
//         window.scrollTo(0, pos - 20); // how far to scroll on each step
//     } else {
//         window.clearInterval(scrollToTop);
//     }
// }, 16);






export let back2Top = () => {
    var currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
    if (currentScroll > 0) {
        window.requestAnimationFrame(back2Top);
        window.scrollTo(0, currentScroll - (currentScroll / 5));
    }
}
export let scrollToSome = (arm) => {
    var currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
    if (currentScroll < arm) {
        window.requestAnimationFrame(scrollToSome);
        window.scrollTo(arm, currentScroll + (currentScroll / 5));
    }
}

export let jump = (index) => {
    // 用 class="d_jump" 添加锚点
    // let jump = document.querySelectorAll('.d_jump')
    // let total = jump[index].offsetTop
    let jump = document.querySelector(index)
    let total = jump.offsetTop
    let distance = document.documentElement.scrollTop || document.body.scrollTop
        // 平滑滚动，时长500ms，每10ms一跳，共50跳
    let step = total / 50
    if (total > distance) {
        smoothDown()
    } else {
        let newTotal = distance - total
        step = newTotal / 50
        smoothUp()
    }

    function smoothDown() {
        if (distance < total) {
            distance += step　　　　　　　
            document.body.scrollTop = distance
            document.documentElement.scrollTop = distance
            setTimeout(smoothDown, 10)
        } else {
            document.body.scrollTop = total
            document.documentElement.scrollTop = total
        }
    }

    function smoothUp() {
        if (distance > total) {
            distance -= step　　　　　　　
            document.body.scrollTop = distance
            document.documentElement.scrollTop = distance
            setTimeout(smoothUp, 10)
        } else {
            document.body.scrollTop = total
            document.documentElement.scrollTop = total
        }
    }
}