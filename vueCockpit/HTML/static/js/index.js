var year = '2019', month = '7', shiqu = '杭州市';
var options = {
  useEasing: true,
  useGrouping: true,
  separator: '',
  decimal: '.',
  prefix: '',
  suffix: ''
};

var html = document.getElementsByTagName("html")[0];
var width = html.clientWidth;
var fontSize = 10 / 3840 * width;
getData()
getChartData()

function getData () {
  $.ajax({
    url: ctxPath + "/cockpit/getInfo",
    type: "GET", dataType: 'json', data: {},
    success: function (data) {
      if (data.code === 0) {
        let aqyhData = data.info.hiddenDanger.data,
          znyjData = data.info.intelligenttips.data,
          zbjcData = data.info.targetmonitor.data,
          syqyData = data.info.cloudpriseData.data,
          aqmData = data.info.safeCode.data

        // 安全隐患
        let str1 = '';
        for (let i = 0; i < aqyhData.length; i++) {
          str1 += `<div class="cb_tr">
              <div class="cb_td">${aqyhData[i].ename}</div>
              <div class="cb_td">${aqyhData[i].content}</div>
            </div>`
        }
        $('#aqyh').html(str1)
        // 智能预警
        let str2 = '';
        znyjData.forEach(element => {
          str2 += `<div class="cb_tr">
                <div class="cb_td">${element.ename}</div>
                <div class="cb_td">${element.sdanger}</div>
              </div>`
        });
        $('#znyj').html(str2)
        // 上云企业
        let str3 = '', syqyMax = 0;
        for (let i = 0; i < syqyData.length; i++) {
          if (syqyData[i].num > syqyMax) {
            syqyMax = syqyData[i].num
          }
        }
        console.log(syqyMax);
        syqyData.forEach(element => {
          str3 += `<li>
                <div class="s_name">${element.town}</div>
                <div class="s_line"><span style="width:${element.num / syqyMax * 100}%"></span></div>
                <div class="s_num">${element.num}</div>
              </li>`
        });
        $('#syqy').html(str3)
        // 安全码
        new CountUp("hongma", 0, Number(aqmData[0].red), 0, 1, options).start();
        new CountUp("chengma", 0, Number(aqmData[0].orange), 0, 1, options).start();
        new CountUp("huangma", 0, Number(aqmData[0].yellow), 0, 1, options).start();
        new CountUp("lvma", 0, Number(aqmData[0].green), 0, 1, options).start();
        // 指标检测
        new CountUp("qyzs", 0, Number(zbjcData[0].ecount), 0, 1, options).start();
        new CountUp("yxj", 0, Number(zbjcData[0].hascheck), 0, 1, options).start();
        new CountUp("wxj", 0, Number(zbjcData[0].notcheck), 0, 1, options).start();
      }
    }
  });
}

function getChartData () {
  $.ajax({
    url: ctxPath + "/cockpit/getAnalysisInfo",
    type: "GET", dataType: 'json', data: {},
    success: function (data) {
      if (data.code === 0) {
        yhzgData = data.info.hidderDangerChange.data;
        fxdjData = data.info.riskLevelAnaly.data;
        yhflData = data.info.hidderDangerClass.data;

        let newObj1 = new Array(), newObj2 = new Array(), newObj3 = new Array();
        for (let i = 0; i < yhzgData.length; i++) {
          newObj1.push({
            name: yhzgData[i].treatment,
            value: yhzgData[i].hidDangerNum
          })
        }
        for (let i = 0; i < fxdjData.length; i++) {
          newObj2.push({
            name: fxdjData[i].enterPriseName,
            value: fxdjData[i].riskLevel
          })
        }
        for (let i = 0; i < yhflData.length; i++) {
          newObj3.push({
            name: yhflData[i].hidDangerType,
            value: yhflData[i].hidDangerNum
          })
        }
        getChart(newObj1, '隐患整改分析', 'charts1')
        getChart(newObj2, '风险等级分析', 'charts2')
        getChart(newObj3, '隐患分类分析', 'charts3')
      }
    }
  })
}
//项目占比划分
let ec1Data = [{ name: '分类一', value: 18 }, { name: '分类二', value: 18 }, { name: '分类三', value: 18 }, { name: '分类四', value: 18 }]
function getChart (obj1, obj2, id) {
  var charts1Data = obj1;
  var text = obj2;

  var data1 = new Array();

  for (var i = 0; i < charts1Data.length; i++) {
    data1.push(charts1Data[i].name);

  }

  let legendData = getLegendData(charts1Data, "name");
  let allData = getAllData(charts1Data, "value");


  var gcharts1 = echarts.init(document.getElementById(id));
  gcharts1option = {
    color: ['#2fc25b', '#3196fa', '#f3d530', '#ff4e00', '#ef4864'],
    backgroundColor: 'transparent',
    title: {
      text: text,
      left: fontSize * 3,
      y: '0%',
      textStyle: {
        fontSize: fontSize * 3.2,
        color: '#fff'
      }
    },
    tooltip: {
      trigger: 'item',
      textStyle: {
        fontSize: fontSize * 2
      }
    },
    // grid: {
    //     left: '8%',
    //     right: '5%',
    //     bottom: '4%',
    //     top: '50%',
    //     containLabel: true
    // },
    legend: {
      show: true,
      top: 'center',
      left: '50%',
      itemGap: 5,
      itemWidth: 10,
      itemHeight: 10,
      icon: 'rect',
      data: data1,
      formatter: function (params) {
        console.log(legendData[params].value);
        return "{title|" + params + "}{value|" + legendData[params].value + "}{title2|家}{value|" + ((legendData[params].value / allData * 100).toFixed(2)) + "}{title2|%}"
      },
      textStyle: {
        rich: {
          title: {
            width: fontSize * 13,
            color: '#fff',
            fontSize: fontSize * 2.4,
          },
          title2: {
            width: fontSize * 5,
            color: 'rgba(255,255,255,.9)',
            fontSize: fontSize * 2.4,
          },
          value: {
            color: '#00f4fb',
            fontSize: fontSize * 3,
            fontWeight: 'bold'
          }
        }
      }
    },

    series: [{
      name: '项目数',
      type: 'pie',
      radius: ['55%', '65%'],
      avoidLabelOverlap: false,
      center: ['23%', '50%'],
      color: ['#2fc25b', '#3196fa', '#f3d530', '#ff4e00', '#ef4864'],

      itemStyle: { //图形样式
        normal: {
          borderColor: 'rgba(6, 28, 91, .5)',
          borderWidth: 1,
        },
      },
      label: {
        normal: {
          show: false,
          position: 'center',
          textStyle: {
            fontSize: fontSize * 2,
            fontWeight: 'bold'
          }
        },
        emphasis: {
          show: false,
          textStyle: {
            fontSize: fontSize * 3,
            color: '#00f4fb',
            fontWeight: 'bold'
          },
          formatter: function (params) {
            return params.value;
          }
        }
      },
      labelLine: {
        normal: {
          show: false
        }
      },
      data: charts1Data
    },]
  };
  gcharts1.setOption(gcharts1option);


}


getLineChart('charts4')
getLineChart('charts5')
getLineChart('charts6')


function getLineChart (id) {
  var mychart3 = echarts.init(document.getElementById(id));
  option3 = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
    },
    grid: {
      left: '5%',
      right: '5%',
      bottom: '8%',
      top: '10%',
      containLabel: true
    },
    xAxis: [{
      type: 'category',
      boundaryGap: true,
      axisTick: {
        show: false
      },
      axisLine: {
        show: false,
      },
      axisLine: {
        show: true,
        lineStyle: {
          color: '#2e598f',
          width: 3
        }
      },
      axisLabel: {
        interval: 0,
        rotate: 30,
        textStyle: {
          fontSize: fontSize * 2,
          color: '#fff',
        }
      },
      splitLine: {
        show: false
      },
      data: [2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018]
    }],
    yAxis: [{
      type: 'value',

      axisTick: {
        show: false
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: 'rgba(255,255,255,0.7)'
        }
      },
      axisLabel: {
        show: true,
        textStyle: {
          fontSize: fontSize * 2,
          color: '#fff',
        }
      },
      splitLine: {
        show: true,
        lineStyle: { color: '#2e598f', type: 'dashed' }
      }
    }],
    series: [{
      name: '',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 10,
      showSymbol: true,
      // label: {
      //     normal: {
      //         show: true,
      //         position: 'top',
      //         fontSize: '24',
      //         color: '#3196fa',
      //         formatter: function (params) {
      //             console.log(params);
      //         }
      //     }
      // },
      lineStyle: {
        normal: {
          width: 3
        }
      },
      areaStyle: {
        normal: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: 'rgba(49, 150, 250, .5)'
          }, {
            offset: 1,
            color: 'rgba(49, 150, 250, .2)'
          }], false),
          shadowColor: 'rgba(0, 0, 0, 0.1)',
          shadowBlur: 10
        }
      },

      itemStyle: {
        normal: {
          color: 'rgb(0,136,212)',
          borderColor: 'rgba(0,136,212,0.2)',
          borderWidth: 12

        }
      },
      data: [7926, 7120, 6890, 6134, 5818, 3840, 3692, 3561, 2672, 4525]
    }]
  };
  mychart3.setOption(option3);
}


$('.center_btns li').click(function () {
  let n = $(this).index()
  $(this).addClass('active').siblings().removeClass('active')
  $('.center_check>div').eq(n).addClass('active').siblings().removeClass('active')
})