/* eslint-disable */
require('script-loader!file-saver');
require('../vendor/Blod');
require('script-loader!xlsx/dist/xlsx.core.min');
function generateArray(table) {
  var out = [];
  var rows = table.querySelectorAll('tr');
  var ranges = [];
  for (var R = 0; R < rows.length; ++R) {
    var outRow = [];
    var row = rows[R];
    var columns = row.querySelectorAll('td');
    for (var C = 0; C < columns.length; ++C) {
      var cell = columns[C];
      var colspan = cell.getAttribute('colspan');
      var rowspan = cell.getAttribute('rowspan');
      var cellValue = cell.innerText;
      if (cellValue !== "" && cellValue == +cellValue) cellValue = +cellValue;

      //Skip ranges
      ranges.forEach(function (range) {
        if (R >= range.s.r && R <= range.e.r && outRow.length >= range.s.c && outRow.length <= range.e.c) {
          for (var i = 0; i <= range.e.c - range.s.c; ++i) outRow.push(null);
        }
      });

      //Handle Row Span
      if (rowspan || colspan) {
        rowspan = rowspan || 1;
        colspan = colspan || 1;
        ranges.push({s: {r: R, c: outRow.length}, e: {r: R + rowspan - 1, c: outRow.length + colspan - 1}});
      }
      ;

      //Handle Value
      outRow.push(cellValue !== "" ? cellValue : null);

      //Handle Colspan
      if (colspan) for (var k = 0; k < colspan - 1; ++k) outRow.push(null);
    }
    out.push(outRow);
  }
  return [out, ranges];
};

function datenum(v, date1904) {
  if (date1904) v += 1462;
  var epoch = Date.parse(v);
  return (epoch - new Date(Date.UTC(1899, 11, 30))) / (24 * 60 * 60 * 1000);
}

function sheet_from_array_of_arrays(data, opts) {
  var ws = {};
  var range = {s: {c: 10000000, r: 10000000}, e: {c: 0, r: 0}};
  for (var R = 0; R != data.length; ++R) {
    for (var C = 0; C != data[R].length; ++C) {
      if (range.s.r > R) range.s.r = R;
      if (range.s.c > C) range.s.c = C;
      if (range.e.r < R) range.e.r = R;
      if (range.e.c < C) range.e.c = C;
      var cell = {v: data[R][C]};
      if (cell.v == null) continue;
      var cell_ref = XLSX.utils.encode_cell({c: C, r: R});

      if (typeof cell.v === 'number') cell.t = 'n';
      else if (typeof cell.v === 'boolean') cell.t = 'b';
      else if (cell.v instanceof Date) {
        cell.t = 'n';
        cell.z = XLSX.SSF._table[14];
        cell.v = datenum(cell.v);
      }
      else cell.t = 's';

      ws[cell_ref] = cell;
    }
  }
  if (range.s.c < 10000000) ws['!ref'] = XLSX.utils.encode_range(range);
  return ws;
}

function Workbook() {
  if (!(this instanceof Workbook)) return new Workbook();
  this.SheetNames = [];
  this.Sheets = {};
}

function s2ab(s) {
  var buf = new ArrayBuffer(s.length);
  var view = new Uint8Array(buf);
  for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
  return buf;
}

export function export_table_to_excel(id) {
  var theTable = document.getElementById(id);
  var oo = generateArray(theTable);
  var ranges = oo[1];
  /* original data */
  var data = oo[0];
  var ws_name = "SheetJS";

  var wb = new Workbook(), ws = sheet_from_array_of_arrays(data);

  /* add ranges to worksheet */
  // ws['!cols'] = ['apple', 'banan'];
  ws['!merges'] = ranges;

  /* add worksheet to workbook */
  wb.SheetNames.push(ws_name);
  wb.Sheets[ws_name] = ws;

  var wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: false, type: 'binary'});

  saveAs(new Blob([s2ab(wbout)], {type: "application/octet-stream"}), "test.xlsx")
}

function formatJson(jsonData) {
  console.log(jsonData)
}
export function exportJsonToExcel(th, jsonData, defaultTitle) {
  
  var autoWidth = true;

  /* original data */

  var data = jsonData;
  data.unshift(th);
  var ws_name = defaultTitle;
  var wb = new Workbook(), ws = sheet_from_array_of_arrays(data);

  // 设置单元格宽度
  if (autoWidth) {
    /*设置worksheet每列的最大宽度*/
    const colWidth = data.map(row =>
      row.map(val => {
        /*先判断是否为null/undefined*/
        if (val == null || val == undefined) {
          return {
            wch: 10
          };
        } else if (val.toString().charCodeAt(0) > 255) {
          /*再判断是否为中文*/
          return {
            wch: val.toString().length * 2
          };
        } else {
          return {
            wch: val.toString().length * 1.5
          };
        }
      })
    );
    /*以主表第二行为初始值，因为我的第一行是表格标题，会比较长，所以以主表第二行为初始值*/
    let result = colWidth[1];
    for (let i = 1; i < colWidth.length; i++) {
      for (let j = 0; j < colWidth[i].length; j++) {
        if (result[j]["wch"] < colWidth[i][j]["wch"]) {
          result[j]["wch"] = colWidth[i][j]["wch"];
        }
      }
    }
    ws["!cols"] = result;
  }

    // 给所有单元格加上边框，内容居中，字体，字号，标题表头特殊格式部分后面替换
    for (var i in dataInfo) {
      if (
        i == "!ref" ||
        i == "!merges" ||
        i == "!cols" ||
        i == "!rows" ||
        i == "A1"
      ) { } else {
        dataInfo[i + ""].s = {
          // border: borderAll,
          alignment: {
            horizontal: "center",
            vertical: "center"
          },
          font: {
            name: "微软雅黑",
            sz: 10
          }
        };
      }
    }

  /* add worksheet to workbook */
  wb.SheetNames.push(ws_name);
  wb.Sheets[ws_name] = ws;

  var dataInfo = wb.Sheets[wb.SheetNames[0]];

    // 给所有单元格加上边框，内容居中，字体，字号，标题表头特殊格式部分后面替换
    for (var i in dataInfo) {
      if (
        i == "!ref" ||
        i == "!merges" ||
        i == "!cols" ||
        i == "!rows" ||
        i == "A1"
      ) { } else {
        dataInfo[i + ""].s = {
          alignment: {
            horizontal: "center",
            vertical: "center"
          },
          font: {
            name: "宋体",
            sz: 10
          }
        };
      }
    }
 // 设置表格样式
 const arrabc = ["A",
                  "B",
                  "C",
                  "D",
                  "E",
                  "F",
                  "G",
                  "H",
                  "I",
                  "J",
                  "K",
                  "L",
                  "M",
                  "N",
                  "O",
                  "P",
                  "Q",
                  "R",
                  "S",
                  "T",
                  "U",
                  "V",
                  "W",
                  "X",
                  "Y",
                  "Z"
                  ]
     
  // 给标题、表格描述信息、表头等部分加上特殊格式
  var multiHeader = th;
  arrabc.some(function (v) {
    for (let j = 1; j < multiHeader.length + 3; j++) {
      const _v = v + j
      if (dataInfo[_v]) {
        debugger;
        dataInfo[_v].s = {};
        console.log(dataInfo[_v].s);
        // 标题部分A1-Z1
        if (j == 1) {
          dataInfo[v + j].s = {
            font: {
              name: "宋体",
              sz: 12,
              color: {
                rgb: "666666"
              },
              bold: true,
              italic: false,
              underline: false
            },
            fill: {
              fgColor: {
                rgb: "f0f0f0"
              },
            },
            alignment: {
              horizontal: "center",
              vertical: "center"
            }
          };
        // } else {
        // // 表头部分,根据表头特殊格式设置
        //   if (multiHeader.length == 0) {
        //   // multiHeader.length = 0 时表头没有合并单元格，表头只占1行A2-Z2
        //     const fv = v + (multiHeader.length + 2)
        //     dataInfo[fv].s = {
        //       // border: borderAll,
        //       font: {
        //         name: "宋体",
        //         sz: 11,
        //         bold: true
        //       },
        //       alignment: {
        //         horizontal: "center",
        //         vertical: "center"
        //       },
        //       fill: {
        //         fgColor: {
        //           rgb: "409EFF"
        //         },
        //       },
        //     }
        //   } else if (multiHeader.length == 1) {
        //   // multiHeader.length = 0 时表头有合并单元格，表头只占2行A2-Z2，A3-Z3，这是没有描述信息只有表头合并的
        //     dataInfo[v + j].s = {
        //       // border: borderAll,
        //       font: {
        //         name: "宋体",
        //         sz: 11,
        //       },
        //       alignment: {
        //         horizontal: "center",
        //         vertical: "center"
        //       },
        //       fill: {
        //         fgColor: {
        //           rgb: "30 144 255"
        //         }
        //       },
        //     }
        //   } else {
        //    // multiHeader.length = 0 时表头有合并单元格，表头多行
        //     dataInfo[v + j].s = {
        //       // border: borderAll,
        //       font: {
        //         name: "宋体",
        //         sz: 9,
        //       },
        //       fill: {
        //         fgColor: {
        //           rgb: "409EFF"
        //         }
        //       },
        //       alignment: {
        //         horizontal: "left",
        //         vertical: "center"
        //       }
        //     }
        //   }
        }
        console.log( dataInfo[_v].s);
        // multiHeader.length + 2 是表头的最后1行
        // dataInfo[v + 12].s = {
        //   // border: borderAll,
        //   font: {
        //     name: "宋体",
        //     sz: 10,
        //   },
        //   alignment: {
        //     horizontal: "center",
        //     vertical: "center"
        //   },
        //   fill: {
        //     fgColor: {
        //       rgb: "f0f0f0"
        //     }
        //   },
        // }
      }
    }
  });

  var wbout = XLSX.write(wb, {bookType: 'xlsx', bookSST: false, type: 'binary'});
  var title = defaultTitle || '列表'
  saveAs(new Blob([s2ab(wbout)], {type: "application/octet-stream"}), title + ".xlsx")
}
