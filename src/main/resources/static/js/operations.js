// 支持的操作类型
var OPERATIONS = [
    {
        "operate": "saveFile",
        "name": "保存内容",
        "desc": "将待处理内容作为链接访问并下载，或直接保存待处理内容[支持使用变量]",
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
                        "name": "文件名-自动获取"
                    }, {
                        "value": "2",
                        "name": "文件名-随机"
                    }, {
                        "value": "3",
                        "name": "文件名-自定义"
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
                        "name": "文件类型-自动获取"
                    }, {
                        "value": "2",
                        "name": "文件类型-自定义"
                    }
                ]
            }, {
                "type": "input",
                "name": "fileType",
                "placeholder": "自定义时填写,例如.jpg"
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
        "name": "访问链接并返回结果",
        "desc": "将待处理内容作为链接访问，并将结果给下一步处理",
        "check": function (data) {
            return { result: true, desc: "Success" };
        }
    },
    {
        "operate": "findContent",
        "name": "查找内容/设置变量",
        "desc": "以各种方式在待处理内容中匹配查找，并将结果交给下一步处理，或将结果作为变量（只取查找结果的第一个值作为变量，之后的步骤中可以使用{变量名}引用变量。）",
        "check": function (data) {
            if (!data.express) {
                return { result: false, desc: "表达式内容必填", name: "express" };
            }

            if (2 == data.findType && 1 == data.cssType && !data.attrName) {
                return { result: false, desc: "元素属性名必填", name: "attrName" };
            }
            if (2 == data.resultType && !data.key) {
                return { result: false, desc: "变量名必填", name: "key" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "表达式/变量值"
        }, {
            "type": "select",
            "name": "findType",
            "options": [
                {
                    "value": "1",
                    "name": "正则表达式"
                }, {
                    "value": "2",
                    "name": "CSS选择器"
                }, {
                    "value": "3",
                    "name": "JPath"
                    // }, {
                    //     "value": "4",
                    //     "name": "XPath"
                }, {
                    "value": "5",
                    "name": "设置变量值"
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
                    "name": "获取元素属性"
                }, {
                    "value": "2",
                    "name": "outerHTML"
                }, {
                    "value": "3",
                    "name": "innerHTML"
                }
            ]
        }, {
            "type": "input",
            "name": "attrName",
            "placeholder": "元素属性名"
        }, {
            "type": "label",
            "textContent": "处理方式"
        }, {
            "type": "select",
            "name": "resultType",
            "options": [
                {
                    "value": "1",
                    "name": "给下一步处理"
                }, {
                    "value": "2",
                    "name": "作为变量"
                }
            ]
        }, {
            "type": "label",
            "textContent": "变量名"
        }, {
            "type": "input",
            "name": "key",
            "placeholder": "作为变量时填写"
        }]
    },
    {
        "operate": "resetContent",
        "name": "重置内容",
        "desc": "重置待处理内容[支持使用变量]",
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
    // {
    //     "operate": "nextPagination",
    //     "name": "持续查找下一页",
    //     "desc": "将代处理内容作为链接访问，从访问结果中查找下一个链接并重复此步骤，并且每次的访问结果都会交给下一步处理[支持使用变量]",
    //     "check": function (data) {
    //         if (!data.express) {
    //             return { result: false, desc: "表达式内容必填", name: "express" };
    //         }
    //         if (2 == data.findType && 1 == data.cssType && !data.attrName) {
    //             return { result: false, desc: "元素属性名必填", name: "attrName" };
    //         }
    //         return { result: true, desc: "Success" };
    //     },
    //     "vars": [{
    //         "type": "label",
    //         "textContent": "查找下一页链接"
    //     }, {
    //         "type": "select",
    //         "name": "findType",
    //         "options": [
    //             {
    //                 "value": "1",
    //                 "name": "正则表达式"
    //             }, {
    //                 "value": "2",
    //                 "name": "CSS选择器"
    //             }, {
    //                 "value": "3",
    //                 "name": "JPath"
    //                 // }, {
    //                 //     "value": "4",
    //                 //     "name": "XPath"
    //             }
    //         ]
    //     }, {
    //         "type": "input",
    //         "name": "express",
    //         "placeholder": "表达式内容"
    //     }, {
    //         "type": "select",
    //         "name": "cssType",
    //         "options": [
    //             {
    //                 "value": "1",
    //                 "name": "获取元素属性"
    //             }, {
    //                 "value": "2",
    //                 "name": "outerHTML"
    //             }, {
    //                 "value": "3",
    //                 "name": "innerHTML"
    //             }
    //         ]
    //     }, {
    //         "type": "input",
    //         "name": "attrName",
    //         "placeholder": "元素属性名"
    //     }]
    // },
    {
        "operate": "loop",
        "name": "步骤回退",
        "desc": "将代处理内容直接交给下一步，同时回退指定个数的步骤，实现循环",
        "check": function (data) {
            if (!data.loopCount) {
                return { result: false, desc: "回退步数必填", name: "loopCount" };
            }
            if (isNaN(data.loopCount) || data.loopCount <= 0) {
                return { result: false, desc: "回退步数需要填写正整数", name: "loopCount" };
            }
            return { result: true, desc: "Success" };
        },
        "vars": [{
            "type": "label",
            "textContent": "回退步数"
        }, {
            "type": "input",
            "name": "loopCount",
            "placeholder": "填写步数"
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
        "desc": "为访问链接时的请求设置消息头，一般用于需要登录的场景设置cookie[支持使用变量]",
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
    }

];