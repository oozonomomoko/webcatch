var pop = function (data) {
    return {
        "titleStr": data.titleStr,
        "innerEle": data.innerEle,
        "onclick": data.onclick,
        "onclose": data.onclose,
        "close": function () {
            this.pop.remove();
        },
        "pop": null,
        "show": function () {
            let pop = document.createElement("div");
            pop.className = "pop";
            let back = document.createElement("div");
            back.className = "pop-back";
            let main = document.createElement("div");
            main.className = "pop-main";
            pop.appendChild(back);
            pop.appendChild(main);

            let head = document.createElement("div");
            head.className = "pop-head";
            let close = document.createElement("div");
            close.className = "pop-close";
            close.textContent = "×";
            let title = document.createElement("div");
            title.className = "pop-title";
            title.textContent = this.titleStr;
            head.appendChild(close);
            head.appendChild(title);
            let body = document.createElement("div");
            body.className = "pop-body";
            body.appendChild(this.innerEle);
            main.appendChild(head);
            main.appendChild(body);

            let that = this;
            body.onclick = function () {
                that.onclick(that);
            }
            close.onclick = function () {
                if (that.onclose)
                    that.onclose(that);
                pop.remove();
            }
            document.body.appendChild(pop);
            this.pop = pop;
        }
    }
}

/**
 * 弹出框选择步骤
 * @param stepIdx 插入位置
 */
function addStep(stepIdx) {
    let div = document.createElement("div");
    let steps = document.getElementById("steps");
    operations.forEach(operation => {
        // 公共元素
        let base = document.createElement("div");
        base.className = "step";
        let description = document.createElement("span");
        description.className = "description";
        description.textContent = operation.name;
        let detail = document.createElement("span");
        detail.className = "detail";
        detail.textContent = "  " + operation.desc;
        base.appendChild(description);
        base.appendChild(detail);
        base.onclick = function () {
            let step = buildStepEle(operation);
            steps.appendChild(step);
            step.style.marginTop = "50px";
            step.style.marginBottom = "50px";
            setTimeout(function(){
                step.style.marginTop = "0px";
                step.style.marginBottom = "0px";
            }, 100);
        };
        div.appendChild(base);
    })
    new pop({
        "titleStr": "添加步骤",
        "innerEle": div,
        "onclick": function (that) {
            that.close();
        }
    }).show();
}

function buildStepEle(operation) {
    // 公共元素
    let base = document.createElement("div");
    base.className = "step";
    let description = document.createElement("span");
    description.className = "description";
    description.textContent = operation.name;
    let del = document.createElement("button");
    del.className = "btn-s";
    del.textContent = "删除";
    let add = document.createElement("button");
    add.className = "btn-s";
    add.textContent = "添加";
    let form = document.createElement("form");
    form.name = "params";
    let params = document.createElement("div");
    params.className = "params";
    form.appendChild(params);

    base.appendChild(description);
    base.appendChild(add);
    base.appendChild(del);
    base.appendChild(form);

    let operate = document.createElement("input");
    operate.type = "hidden";
    operate.name = "operate";
    operate.value = operation.operate;
    params.appendChild(operate);
    // 添加删除
    del.onclick = function () {
        base.remove();
    };
    add.onclick = addStep;
    // 各步骤元素
    if(!operation.vars) {
        return base;
    }
    operation.vars.forEach(variable => {
        let step = document.createElement(variable.type);
        step.name = variable.name;
        step.style = variable.style;
        if (variable.type == "input") {
            step.type = "text";
            if (variable.value) {
                step.value = variable.value;
            }
            if (variable.placeholder) {
                step.placeholder = variable.placeholder;
            }
        }
        if (variable.type == "label") {
            step.textContent = variable.textContent + ":";
        }
        if (variable.type == "select") {
            variable.options.forEach(option => {
                let optionEle = document.createElement("option");
                optionEle.value = option.value;
                optionEle.textContent = option.value + '.' + option.name;
                optionEle.selected = option.selected;
                step.appendChild(optionEle);
            })
        }
        params.appendChild(step);
    });
    return base;
}

function start() {
    let steps = [];
    $('#steps form').each((i, form) => {
        let map = {};
        let inputs = form.querySelectorAll('select,input');
        inputs.forEach(input => {
            map[input.name] = input.value;
        })
        steps.push(map);
    })
    let originContents = [document.getElementById("origin").value];
    $.ajax({
        'url': '/main/start.do',
        'method': 'POST',
        'contentType': 'application/json;charset=UTF-8',
        'data': JSON.stringify({ 
            'contents':originContents,
            'steps': steps
        }),
        'success': function (data) {
            alert(JSON.stringify(data));
        }
    })
}

function stop() {
    $.ajax({
        'url': '/main/stop.do',
        'method': 'GET',
        'success': function (data) {
            alert(JSON.stringify(data));
        }
    })
}

$(function(){
    let print = document.getElementById("print");
    setInterval(() => {
        $.ajax({
            'url': '/main/log.do',
            'method': 'GET',
            'headers': {
                Accept: "application/json; charset=UTF-8"
            },
            'success': function (data) {
                if (data.logs.length == 0) {
                    return;
                }
                print.value += data.logs.join('\n') + '\n';
            }
        })
    }, 1000);
})
