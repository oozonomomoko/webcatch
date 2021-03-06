// 支持的操作类型
var OPERATIONS = [
    {
        "operate": "saveFile",
        "name": "保存文件",
        "desc": "将内容当做链接访问，并保存为文件[支持引用变量]",
        "check": function (data) {
            if (3 == data.fileNameFrom && !data.fileName) {
                return { result: false, desc: "自定义文件名必填", name: "fileName" };
            }
            if (2 == data.fileTypeFrom && !data.fileType) {
                return { result: false, desc: "自定义文件类型必填", name: "fileType" };
            }
            if (!data.downloadDir) {
                return { result: false, desc: "保存路径必填", name: "downloadDir" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [
            {
                "type": "select",
                "name": "downloadType",
                "options": [
                    {
                        "value": "1",
                        "name": "访问并下载"
                        // }, {
                        //     "value": "2",
                        //     "name": "直接保存待处理内容"
                    }
                ]
            },
            {
                "type": "select",
                "name": "fileNameFrom",
                "options": [
                    {
                        "value": "1",
                        "name": "文件名-自动获取",
                        "hide": "input[name=fileName]"
                    }, {
                        "value": "2",
                        "name": "文件名-随机",
                        "hide": "input[name=fileName]"
                    }, {
                        "value": "3",
                        "name": "文件名-自定义",
                        "show": "input[name=fileName]"
                    }
                ]
            }, {
                "type": "input",
                "name": "fileName",
                "placeholder": "自定义时填写"
            }, {
                "type": "select",
                "name": "fileTypeFrom",
                "options": [
                    {
                        "value": "1",
                        "name": "文件类型-自动获取",
                        "hide": "input[name=fileType]"
                    }, {
                        "value": "2",
                        "name": "文件类型-自定义",
                        "show": "input[name=fileType]"
                    }
                ]
            }, {
                "type": "input",
                "name": "fileType",
                "placeholder": "文件类型,如.jpg",
                "style": "width:50px;"
            }, {
                "type": "label",
                "textContent": "保存路径"
            }, {
                "type": "input",
                "name": "downloadDir",
                "placeholder": "例如E:\\nogizaka"
            }
        ]
    },
    {
        "operate": "getResult",
        "name": "访问链接",
        "desc": "将内容当做链接访问，并且将结果交给下一步处理",
        "check": function (data) {
            if (2 == data.requestType && !data.body) {
                return { result: false, desc: "消息体内容不可为空", name: "body" };

            }
            return { result: true, desc: "Success" };
        },
        "vars": [
            {
                "type": "label",
                "textContent": "请求方式"
            },
            {
                "type": "select",
                "name": "requestType",
                "options": [
                    {
                        "value": "1",
                        "name": "Get"
                    }, {
                        "value": "2",
                        "name": "Post"
                    }
                ]
            },
            {
                "type": "input",
                "name": "body",
                "placeholder": "消息体内容"
            },
            {
                "type": "label",
                "textContent": "响应类型"
            },
            {
                "type": "select",
                "name": "resultType",
                "options": [
                    {
                        "value": "1",
                        "name": "html"
                    }, {
                        "value": "2",
                        "name": "json"
                    }
                ]
            }
        ]
    },
    {
        "operate": "findContent",
        "name": "查找内容",
        "desc": "在内容中查找字符，并且将结果交给下一步处理。查找方式支持正则表达式，CSS选择器，jpath等[支持引用变量]",
        "check": function (data) {
            if (!data.express) {
                return { result: false, desc: "表达式内容必填", name: "express" };
            }

            if (2 == data.findType && 1 == data.cssType && !data.attrName) {
                return { result: false, desc: "元素属性名必填", name: "attrName" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "查找方式"
        }, {
            "type": "select",
            "name": "findType",
            "options": [
                {
                    "value": "1",
                    "name": "正则表达式",
                    "hide": "select[name=cssType],input[name=attrName]"
                }, {
                    "value": "2",
                    "name": "CSS选择器",
                    "show": "select[name=cssType],input[name=attrName]"
                }, {
                    "value": "3",
                    "name": "JPath",
                    "hide": "select[name=cssType],input[name=attrName]"
                }
            ]
        }, {
            "type": "input",
            "name": "express",
            "placeholder": "表达式内容"
        }, {
            "type": "select",
            "name": "cssType",
            "options": [
                {
                    "value": "1",
                    "name": "获取元素属性",
                    "show": "input[name=attrName]"
                }, {
                    "value": "2",
                    "name": "outerHTML",
                    "hide": "input[name=attrName]"
                }, {
                    "value": "3",
                    "name": "innerHTML",
                    "hide": "input[name=attrName]"
                }
            ]
        }, {
            "type": "input",
            "name": "attrName",
            "placeholder": "元素属性名"
        }]
    },
    {
        "operate": "setVariable",
        "name": "设置变量",
        "desc": "新增一个变量，对后续步骤生效。变量值可从内容查找，查找方式同[查找内容]，查找失败则中断流程，也支持直接定义变量值。（只取查找结果的第一个值作为变量，之后的步骤中可以用{变量名}的方式引用变量。）[支持引用变量]",
        "check": function (data) {
            if (!data.key) {
                return { result: false, desc: "变量名必填", name: "key" };
            }
            if (!data.express) {
                return { result: false, desc: "表达式内容必填", name: "express" };
            }
            if (2 == data.findType && 1 == data.cssType && !data.attrName) {
                return { result: false, desc: "元素属性名必填", name: "attrName" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "变量名称"
        }, {
            "type": "input",
            "name": "key",
            "placeholder": "变量名称"
        }, {
            "type": "label",
            "textContent": "变量值"
        }, {
            "type": "select",
            "name": "findType",
            "options": [
                {
                    "value": "1",
                    "name": "正则表达式",
                    "hide": "select[name=cssType],input[name=attrName]"
                }, {
                    "value": "2",
                    "name": "CSS选择器",
                    "show": "select[name=cssType],input[name=attrName]"
                }, {
                    "value": "3",
                    "name": "JPath",
                    "hide": "select[name=cssType],input[name=attrName]"
                }, {
                    "value": "5",
                    "name": "自定义变量值",
                    "hide": "select[name=cssType],input[name=attrName]"
                }
            ]
        }, {
            "type": "input",
            "name": "express",
            "placeholder": "表达式内容/变量值"
        }, {
            "type": "select",
            "name": "cssType",
            "options": [
                {
                    "value": "1",
                    "name": "获取元素属性",
                    "show": "input[name=attrName]"
                }, {
                    "value": "2",
                    "name": "outerHTML",
                    "hide": "input[name=attrName]"
                }, {
                    "value": "3",
                    "name": "innerHTML",
                    "hide": "input[name=attrName]"
                }
            ]
        }, {
            "type": "input",
            "name": "attrName",
            "placeholder": "获取元素属性时填写"
        }]
    },
    {
        "operate": "operateVariable",
        "name": "变量运算",
        "desc": "对指定变量进行加减运算[支持引用变量]",
        "check": function (data) {
            if (!data.key) {
                return { result: false, desc: "变量名必填", name: "key" };
            }
            if (isNaN(data.value) || !data.value) {
                return { result: false, desc: "加减的值必填", name: "value" };
            }
            if (data.value < 0) {
                return { result: false, desc: "值不可为负数", name: "value" };
            }
            if (isNaN(data.limit) || !data.limit) {
                return { result: false, desc: "最大/最小值必填", name: "limit" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "将变量"
        }, {
            "type": "input",
            "name": "key",
            "placeholder": "变量名称"
        }, {
            "type": "select",
            "name": "type",
            "options": [
                {
                    "value": "1",
                    "name": "加"
                }, {
                    "value": "2",
                    "name": "减"
                }
            ]
        }, {
            "type": "input",
            "name": "value",
            "placeholder": "加减的值"
        }, {
            "type": "label",
            "textContent": "最大值限制"
        }, {
            "type": "input",
            "name": "limit",
            "placeholder": "填写最大/最小值"
        }]
    },
    {
        "operate": "replaceString",
        "name": "替换文本",
        "desc": "对待处理内容或变量值中的文本进行替换，也可使用正则替换[支持引用变量]",
        "check": function (data) {
            if (!data.express) {
                return { result: false, desc: "文本内容/正则表达式必填", name: "express" };
            }

            if (!data.target) {
                return { result: false, desc: "变量名必填", name: "target" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "将"
        }, {
            "type": "select",
            "name": "replaceObj",
            "options": [
                {
                    "value": "1",
                    "name": "待处理内容",
                    "hide": "input[name=varName]"
                }, {
                    "value": "2",
                    "name": "变量",
                    "show": "input[name=varName]"
                }
            ]
        }, {
            "type": "input",
            "name": "varName",
            "placeholder": "变量名"
        }, {
            "type": "label",
            "textContent": "中的"
        }, {
            "type": "select",
            "name": "replaceType",
            "options": [
                {
                    "value": "1",
                    "name": "正则表达式"
                }, {
                    "value": "2",
                    "name": "文本"
                }
            ]
        }, {
            "type": "input",
            "name": "express",
            "placeholder": "文本内容/正则表达式"
        }, {
            "type": "label",
            "textContent": "替换为"
        }, {
            "type": "input",
            "name": "target",
            "placeholder": ""
        }]
    },
    {
        "operate": "resetContent",
        "name": "重置内容",
        "desc": "重置待处理内容[支持引用变量]",
        "check": function (data) {
            if (!data.content) {
                return { result: false, desc: "待处理内容必填", name: "content" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "待处理内容"
        }, {
            "type": "input",
            "name": "content",
            "placeholder": "待处理内容",
            "style": "width:500px;"
        }]
    },
    {
        "operate": "skipTo",
        "name": "步骤跳转",
        "desc": "跳转到指定步骤",
        "check": function (data) {
            if (1== data.skipType) {
                if (isNaN(data.stepIdx) || data.stepIdx < 0) {
                    return { result: false, desc: "步骤下标需要填写正整数", name: "stepIdx" };
                }
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "select",
            "name": "skipType",
            "options": [
                {
                    "value": "1",
                    "name": "跳转至",
                    "show": "input[name=stepIdx],select[name=continueFlow]"
                }, {
                    "value": "2",
                    "name": "停止步骤",
                    "hide": "input[name=stepIdx],select[name=continueFlow]"
                }
            ]
        }, {
            "type": "input",
            "name": "stepIdx",
            "placeholder": "步骤下标"
        }, {
            "type": "select",
            "name": "continueFlow",
            "options": [
                {
                    "value": "1",
                    "name": "同时继续下一步骤"
                }, {
                    "value": "2",
                    "name": "停止下一步骤"
                }
            ]
        }]
    },
    {
        "operate": "setPagination",
        "name": "生成分页",
        "desc": "按照指定的变量名，将待处理内容中的{变量名}，替换为指填写范围内的所有数字，并将结果交给下一步处理[支持使用变量]",
        "check": function (data) {
            if (!data.forReplace) {
                return { result: false, desc: "要替换的变量名必填", name: "forReplace" };
            }
            if (2 == data.findType && 1 == data.cssType && !data.attrName) {
                return { result: false, desc: "元素属性名必填", name: "attrName" };
            }
            if (isNaN(data.from)) {
                return { result: false, desc: "需要填写数字", name: "from" };
            }
            if (isNaN(data.to)) {
                return { result: false, desc: "需要填写数字", name: "to" };
            }
            if (data.fixedLen && isNaN(data.fixedLen)) {
                return { result: false, desc: "需要填写数字", name: "fixedLen" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "要替换的变量名"
        }, {
            "type": "input",
            "name": "forReplace",
            "placeholder": "例如 page"
        }, {
            "type": "label",
            "textContent": "从"
        }, {
            "type": "input",
            "name": "from",
            "placeholder": "填写数字，例如 1"
        }, {
            "type": "label",
            "textContent": "到"
        }, {
            "type": "input",
            "name": "to",
            "placeholder": "填写数字，例如 10"
        }, {
            "type": "label",
            "textContent": "固定长度"
        }, {
            "type": "input",
            "name": "fixedLen",
            "placeholder": "填写数字长度，空位补0"
        }]
    },
    {
        "operate": "setHeader",
        "name": "设置请求消息头",
        "desc": "对http请求消息头进行设置，支持引用变量[支持引用变量]",
        "check": function (data) {
            if (!data.key) {
                return { result: false, desc: "消息头名称必填", name: "key" };
            }
            if (!data.value) {
                return { result: false, desc: "消息头内容必填", name: "value" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "消息头名称"
        }, {
            "type": "input",
            "name": "key",
            "placeholder": "例如 Content-Type"
        }, {
            "type": "label",
            "textContent": "消息头内容"
        }, {
            "type": "input",
            "name": "value",
            "placeholder": "例如 application/json"
        }]
    },
    {
        "operate": "logContent",
        "name": "打印内容",
        "desc": "打印待处理内容/变量",
        "check": function (data) {
            if (2 == data.type && !data.key) {
                return { result: false, desc: "变量名必填", name: "key" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "select",
            "name": "type",
            "options": [
                {
                    "value": "1",
                    "name": "打印待处理内容",
                    "hide": "input[name=key]"
                }, {
                    "value": "2",
                    "name": "打印变量",
                    "show": "input[name=key]"
                }
            ]
        }, {
            "type": "input",
            "name": "key",
            "placeholder": "变量名"
        }, {
            "type": "select",
            "name": "continueFlow",
            "options": [
                {
                    "value": "1",
                    "name": "停止下一步骤"
                }, {
                    "value": "2",
                    "name": "继续下一步骤"
                }
            ]
        }]
    }

];