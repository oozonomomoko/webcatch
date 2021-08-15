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
                if (that.onclick)
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
var confirmpop = function (data) {
    return {
        "titleStr": data.titleStr,
        "innerEle": data.innerEle,
        "confirm": data.confirm,
        "cancel": data.cancel,
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

            // head
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

            // body
            let body = document.createElement("div");
            body.className = "pop-body";
            if (this.innerEle)
                body.appendChild(this.innerEle);

            // bottom
            let bottom = document.createElement("div");
            bottom.className = "pop-bottom";
            let cancel = document.createElement("div");
            cancel.className = "pop-cancel";
            cancel.textContent = "取消";
            let confirm = document.createElement("div");
            confirm.className = "pop-confirm";
            confirm.textContent = "确认";
            bottom.appendChild(cancel);
            bottom.appendChild(confirm);

            main.appendChild(head);
            main.appendChild(body);
            main.appendChild(bottom);

            let that = this;
            cancel.onclick = function () {
                if (that.close)
                    that.close();
                pop.remove();
            }
            close.onclick = cancel.onclick;
            confirm.onclick = function () {
                if (that.confirm)
                    that.confirm(that);
                pop.remove();
            }
            document.body.appendChild(pop);
            this.pop = pop;
        }
    }
}
var hugpop = function (data) {
    return {
        "desc": data.desc,
        "result": data.result,
        "show": function () {
            let pop = document.createElement("div");
            pop.className = "hugpop";
            let result = document.createElement("div");
            result.className = "hugpop-result";
            let main = document.createElement("div");
            main.className = data.result ? "hugpop-suc" : "hugpop-err";
            pop.appendChild(result);
            result.appendChild(main);
            let desc = document.createElement("div");
            desc.className = "hugpop-desc";
            desc.textContent = data.desc ? data.desc : (data.status + ':' + data.statusText);
            pop.appendChild(desc);
            document.body.appendChild(pop);
            setTimeout(function () {
                pop.remove();
            }, 1900);
        }
    }
}




/**
 * 弹出框选择步骤
 * @param stepIdx 插入位置
 */
function addStep() {
    let parent = this.parentElement;
    let div = document.createElement("div");
    let steps = document.getElementById("steps");
    createCopy(OPERATIONS).forEach(operation => {
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
            step.style.marginTop = "10px";
            step.style.marginBottom = "10px";
            step.style.backgroundColor = "rgb(212 214 255)";
            if (parent) {
                steps.insertBefore(step, parent);
            } else {
                steps.appendChild(step);
            }
            setTimeout(function () {
                step.style.marginTop = "1px";
                step.style.marginBottom = "0px";
                step.style.backgroundColor = "";
            }, 500);
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
    add.textContent = "插入↑";
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
    if (!operation.vars) {
        return base;
    }
    operation.vars.forEach(variable => {
        let step = document.createElement(variable.type);
        step.name = variable.name;
        step.style = variable.style;
        if (variable.type == "input") {
            step.autocomplete = "off";
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
                optionEle.selected = variable.value == optionEle.value;
                step.appendChild(optionEle);
            })
        }
        params.appendChild(step);
    });
    return base;
}

function start() {
    let steps = [];
    let originContents = document.getElementById("origin");
    let forms = document.querySelectorAll('#steps form');
    if (!originContents.value) {
        new hugpop({ result: false, desc: "未填写待处理内容" }).show();
        $(originContents).addClass("input-error");
        setTimeout(function () {
            $(originContents).removeClass("input-error");
        }, 1500);
        return;
    }
    if (forms.length == 0) {
        new hugpop({ result: false, desc: "未添加步骤" }).show();
        return;
    }
    for (let i = 0; i < forms.length; i++) {
        let form = forms[i];
        let map = {};
        let inputs = form.querySelectorAll('select,input');
        inputs.forEach(input => {
            map[input.name] = input.value;
        })
        let curoperate =  createCopy(OPERATIONS).filter(operate => operate.operate == map.operate);
        let checkResult = curoperate[0].check(map);
        if (!checkResult.result) {
            new hugpop(checkResult).show();
            let err = form.querySelector('select[name="' + checkResult.name + '"],input[name="' + checkResult.name + '"]');
            $(err).addClass("input-error");
            setTimeout(function () {
                $(err).removeClass("input-error");
            }, 1500);
            return;
        }
        steps.push(map);
    }
    $.ajax({
        'url': '/main/start.do',
        'method': 'POST',
        'contentType': 'application/json;charset=UTF-8',
        'data': JSON.stringify({
            'contents': [originContents.value],
            'steps': steps
        }),
        'success': function (data) {
            new hugpop(data).show();
        },
        'error': function (data) {
            new hugpop(data).show();
        }
    })
}

function stop() {
    $.ajax({
        'url': '/main/stop.do',
        'method': 'GET',
        'success': function (data) {
            new hugpop(data).show();
        },
        'error': function (data) {
            new hugpop(data).show();
        }
    })
}

$(function () {
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

function showSetting() {
    $.ajax({
        'url': '/main/querySetting.do',
        'method': 'GET',
        'success': function (data) {
            if (!data.configs || data.configs.length == 0) {
                new hugpop({ result: false, desc: "异常：配置读取失败" }).show();
            }

            new confirmpop({
                "titleStr": "设置",
                "innerEle": buildSettingEle(data),
                "confirm": function (that) {
                    pushSetting(buildConfigs(that.innerEle));
                }
            }).show();
        },
        'error': function (data) {
            new hugpop(data).show();
        }
    })
}
function pushSetting(configs) {
    $.ajax({
        'url': '/main/pushSetting.do',
        'method': 'POST',
        'contentType': 'application/json;charset=UTF-8',
        'data': JSON.stringify({
            'configs': configs,
        }),
        'success': function (data) {
        },
        'error': function (data) {
            new hugpop(data).show();
        }
    })
}
function buildConfigs(innerEle) {
    let inputs = innerEle.querySelectorAll("select,input");
    let configs = {};
    inputs.forEach(input => {
        if (input.name.endsWith("desc")) {
            return;
        }
        configs[input.name] = input.value;
    })
    return configs;
}
function buildSettingEle(data) {
    let div = document.createElement("div");
    for (let key in data.configs) {
        let value = data.configs[key];
        if (!key.endsWith("desc")) {
            let configEle = document.createElement("div");
            let label = document.createElement("label");
            label.textContent = data.configs[key + ".desc"];
            let valueEle = document.createElement("input");
            valueEle.name = key;
            valueEle.value = value;
            configEle.appendChild(label);
            configEle.appendChild(valueEle);
            div.appendChild(configEle);
        }
    };
    div.className = "setting";
    return div;
}


String.prototype.endsWith = function (endStr) {
    var d = this.length - endStr.length;
    return (d >= 0 && this.lastIndexOf(endStr) == d);
}


function exportSteps() {
    let forms = document.querySelectorAll('#steps form');
    if (forms.length == 0) {
        new hugpop({ result: false, desc: "未添加步骤" }).show();
        return;
    }
    let steps = [];
    for (let i = 0; i < forms.length; i++) {
        let form = forms[i];
        let map = {};
        let inputs = form.querySelectorAll('select,input');
        inputs.forEach(input => {
            map[input.name] = input.value;
        })
        steps.push(map);
    }
    let config = {
        originContents: [document.getElementById("origin").value],
        steps: steps
    };

    let text = document.createElement("textarea");
    text.className = "configs-show";
    text.readOnly = "readOnly";
    text.spellcheck = "false";
    text.value = JSON.stringify(config);
    new pop({
        titleStr: "导出步骤",
        innerEle: text,
    }).show();
}
function importSteps() {
    let stepsEle = document.getElementById("steps");

    let text = document.createElement("textarea");
    text.className = "configs-show";
    text.spellcheck = "false";
    text.placeholder = "输入步骤配置"
    new confirmpop({
        "titleStr": "导入步骤",
        "innerEle": text,
        "confirm": function (that) {
            if (!text.value) {
                new hugpop({ result: false, desc: "导入失败" }).show();
                return;
            }
            stepsEle.innerHTML = '';
            let config = JSON.parse(text.value);
            config.steps.forEach(step => {
                let curoperate = createCopy(OPERATIONS).filter(operate => operate.operate == step.operate)[0];
                if (curoperate.vars) {
                    curoperate.vars.forEach(variable => {
                        variable.value = step[variable.name];
                    })
                }
                stepsEle.appendChild(buildStepEle(curoperate));
            });
            if (config.originContents && config.originContents.length > 0)
                document.getElementById("origin").value = config.originContents[0];
        }
    }).show();
}
function clearlog() {
    let print = document.getElementById("print");
    print.value = '';
}
function createCopy(obj){
    return JSON.parse(JSON.stringify(obj));
}