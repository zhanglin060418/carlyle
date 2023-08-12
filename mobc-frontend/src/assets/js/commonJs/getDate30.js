export let get30Date = (arr) => {
    let ArrDate = [{
            date: 0,
            cnDate: '周天'
        },
        {
            date: 1,
            cnDate: '周一'
        },
        {
            date: 2,
            cnDate: '周二'
        },
        {
            date: 3,
            cnDate: '周三'
        },
        {
            date: 4,
            cnDate: '周四'
        },
        {
            date: 5,
            cnDate: '周五'
        },
        {
            date: 6,
            cnDate: '周六'
        },
    ]
    if (arr instanceof Array) {
        for (var i = 0; i < 30; i++) {
            if (i === 0 || i % 1 === 0) {
                let s = new Date(
                    new Date().setDate(new Date().getDate() - i)
                ).toLocaleDateString();

                let t = new Date(s).getDay()

                for (let j = 0; j < ArrDate.length; j++) {
                    if (t == ArrDate[j].date) {
                        t = ArrDate[j].cnDate
                        break;
                    }

                }
                arr.unshift(
                    s + ' ' + t
                );
            }
        }
    }

    return arr
}


export let timeSort = (arr) => {
    if (arr instanceof Array) {
        arr.sort((a, b) => {
            return new Date(a.daystr).getTime() - new Date(b.daystr).getTime()
        })
    }
}

export let getDIYDay = (key) => {
    let today = new Date()
    today.setHours(0);
    today.setMinutes(0);
    today.setSeconds(0);
    today.setMilliseconds(0);
    let oneday = 1000 * 60 * 60 * 24
    if (key == "today") {
        return today
    }
    if (key == "yesterday") {
        return new Date(today - oneday);
    }
    if (key == "monday") {
        return new Date(today - oneday * (today.getDay() == 0 ? today.getDay() + 6 : today.getDay() - 1));
    }
    if (key == "lastMonday") {
        return new Date(today - oneday * (today.getDay() + 6));
    }
    if (key == "monthFirst") {
        var monthFirst = new Date(today - oneday * (today.getDate() - 1));
        return new Date(monthFirst);
    }
    if (key == "lastMonthFirst") {
        var lastMonthFirst = new Date(today - oneday * (today.getDate()));
        return new Date(lastMonthFirst - oneday * (lastMonthFirst.getDate() - 1));
    }
    if (key == "halfYear") {
        let d = new Date();
        d.setMonth(d.getMonth() - 6);
        return new Date(d);
    }
}