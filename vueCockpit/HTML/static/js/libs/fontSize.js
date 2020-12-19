        /* 加载设置rem字体大小 */
        ; (function (win, lib) {
            var win = window
            var doc = win.document
            var docEl = doc.documentElement
            var tid

            function refreshRem() {
                var clientWidth = docEl.clientWidth;
                
                if (!clientWidth) return;
                docEl.style.fontSize = clientWidth / (1920 * 2 / 100) + 'px';             
            }

            win.addEventListener(
                'resize',
                function () {
                    clearTimeout(tid)
                    tid = setTimeout(refreshRem, 100)
                },
                false
            )

            win.addEventListener(
                'pageshow',
                function (e) {
                    if (e.persisted) {
                        clearTimeout(tid)
                        tid = setTimeout(refreshRem, 100)
                    }
                },
                false
            )
            win.onresize = function () {
                refreshRem()
            }
            refreshRem()
        })(window, window['lib'] || (window['lib'] = {}))